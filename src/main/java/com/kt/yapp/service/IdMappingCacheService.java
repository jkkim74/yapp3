package com.kt.yapp.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * SNS 임시 ID / KT ID 매핑 캐시 서비스 (v3)
 * 
 * <h3>v3 핵심 변경: 3단계 조회 전략</h3>
 * <pre>
 * SNS 임시 아이디(SNS!XXXXX)가 KT ID로 전환된 경우를 탐지하기 위해
 * TB_USER_KT.SNS_TEMP_ID 컬럼을 활용한 3단계 조회를 수행합니다.
 * 
 * resolveKtId("SNS!S0001_abc123")
 *   |
 *   +-- 1단계: 캐시 조회 → 히트 시 즉시 반환
 *   |
 *   +-- 2단계: TB_USER_KT.SNS_TEMP_ID 조회
 *   |          (SNS 임시 ID → KT ID 전환 완료된 사용자)
 *   |          SELECT USER_ID FROM TB_USER_KT
 *   |          WHERE SNS_TEMP_ID = #{snsId}
 *   |          AND JOIN_STATUS = 'G0001'
 *   |
 *   +-- 3단계: TB_USER 기반 CREDENTIAL_ID 폴백 조회
 *              (credential_id 연결을 통한 조회)
 *              SELECT KT_ID FROM TB_USER
 *              WHERE CREDENTIAL_ID = (
 *                  SELECT CREDENTIAL_ID FROM TB_USER_KT
 *                  WHERE USER_ID = #{snsId} OR SNS_TEMP_ID = #{snsId}
 *              )
 * </pre>
 * 
 * @see com.kt.yapp.config.UserIdResolvingInterceptor
 */
@Service
public class IdMappingCacheService {

    private static final Logger logger = LoggerFactory.getLogger(IdMappingCacheService.class);

    /** 매핑 없음을 나타내는 캐시 값 */
    private static final String CACHE_NONE = "NONE";

    /** 매핑 존재 시 캐시 TTL: 1시간 */
    private static final long CACHE_TTL_MS = TimeUnit.HOURS.toMillis(1);

    /** 매핑 없음 캐시 TTL: 10분 (KT ID 전환을 빠르게 감지) */
    private static final long CACHE_NONE_TTL_MS = TimeUnit.MINUTES.toMillis(10);

    /**
     * 인메모리 캐시.
     * <pre>
     * 캐시 키/값 예시:
     *   "SNS!S0001_abc123" -> CacheEntry("honggildong", expTime)  // SNS임시->KT 매핑
     *   "honggildong"      -> CacheEntry("honggildong", expTime)  // self-mapping
     *   "SNS!S0003_xyz789" -> CacheEntry("NONE", expTime)         // 매핑 없음
     * </pre>
     */
    private final ConcurrentHashMap<String, CacheEntry> cache = new ConcurrentHashMap<>();

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    // =========================================================================
    // 핵심: KT ID 조회/변환
    // =========================================================================

    /**
     * SNS 임시 ID 또는 기타 ID를 KT ID로 변환합니다.
     * 
     * <pre>
     * 우선순위:
     *   1) 이미 KT ID → null 반환 (변환 불필요, self-mapping)
     *   2) SNS 임시 ID → 3단계 조회로 KT ID 찾기
     *   3) 매핑 없음 → null 반환 (원본 유지)
     * </pre>
     */
    public String resolveKtId(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return null;
        }

        // -- 1단계: 캐시 조회 --
        CacheEntry cached = cache.get(userId);
        if (cached != null && !cached.isExpired()) {
            if (CACHE_NONE.equals(cached.value)) {
                return null;
            }
            if (cached.value.equals(userId)) {
                return null; // self-mapping (이미 KT ID)
            }
            logger.debug("[IdMapping] 캐시 히트: {} -> {}", maskId(userId), cached.value);
            return cached.value;
        }

        // 만료된 캐시 제거
        if (cached != null && cached.isExpired()) {
            cache.remove(userId);
        }

        // -- 2단계 + 3단계: DB 조회 --
        String ktId = queryKtIdFromDb(userId);

        // -- 결과 캐시 저장 --
        if (ktId != null) {
            cache.put(userId, new CacheEntry(ktId, System.currentTimeMillis() + CACHE_TTL_MS));
            cache.put(ktId, new CacheEntry(ktId, System.currentTimeMillis() + CACHE_TTL_MS));
            logger.info("[IdMapping] 매핑 발견: {} -> {}", maskId(userId), ktId);
            return ktId;
        } else {
            cache.put(userId, new CacheEntry(CACHE_NONE, 
                      System.currentTimeMillis() + CACHE_NONE_TTL_MS));
            logger.debug("[IdMapping] 매핑 없음 (원본 유지): {}", maskId(userId));
            return null;
        }
    }

    // =========================================================================
    // DB 3단계 조회
    // =========================================================================

    /**
     * DB에서 KT ID를 3단계로 조회합니다.
     * 
     * <pre>
     * 2단계: TB_USER_KT.SNS_TEMP_ID 기반 조회
     *        KT ID 전환 완료된 사용자의 이전 SNS 임시 ID로 조회
     * 
     * 3단계: TB_USER.CREDENTIAL_ID 기반 폴백 조회
     *        credential_id 연결을 통한 KT ID 조회
     * </pre>
     */
    private String queryKtIdFromDb(String userId) {
        try {
            // 2단계: TB_USER_KT.SNS_TEMP_ID로 조회
            // KT ID 전환 완료 시 SNS_TEMP_ID에 이전 임시 ID가 보존됨
            String ktId = sqlSessionTemplate.selectOne(
                "mybatis.mapper.idmapping.getKtIdBySnsTemporaryId", userId
            );

            if (ktId != null && !ktId.trim().isEmpty()) {
                logger.debug("[IdMapping] SNS_TEMP_ID 조회 성공: {} -> {}", maskId(userId), ktId);
                return ktId;
            }

            // 3단계: TB_USER.CREDENTIAL_ID 기반 폴백 조회
            ktId = sqlSessionTemplate.selectOne(
                "mybatis.mapper.idmapping.getKtIdByCredentialIdFallback", userId
            );

            if (ktId != null && !ktId.trim().isEmpty()) {
                logger.debug("[IdMapping] CREDENTIAL_ID 폴백 조회 성공: {} -> {}", maskId(userId), ktId);
                return ktId;
            }

            return null;
        } catch (Exception e) {
            logger.error("[IdMapping] DB 조회 실패: userId={}, error={}", maskId(userId), e.getMessage());
            return null;
        }
    }

    // =========================================================================
    // 캐시 관리
    // =========================================================================

    /**
     * KT ID 전환 시 캐시를 즉시 갱신합니다.
     * 
     * @param oldSnsId 기존 SNS 임시 ID (예: "SNS!S0001_abc123")
     * @param newKtId  새로운 KT ID (예: "honggildong")
     */
    public void updateMapping(String oldSnsId, String newKtId) {
        if (oldSnsId != null) {
            cache.put(oldSnsId, new CacheEntry(newKtId, System.currentTimeMillis() + CACHE_TTL_MS));
        }
        if (newKtId != null) {
            cache.put(newKtId, new CacheEntry(newKtId, System.currentTimeMillis() + CACHE_TTL_MS));
        }
        logger.info("[IdMapping] 캐시 갱신: {} -> {}", maskId(oldSnsId), newKtId);
    }

    /**
     * 특정 사용자의 캐시를 무효화합니다.
     */
    public void invalidate(String userId) {
        cache.remove(userId);
        logger.info("[IdMapping] 캐시 무효화: {}", maskId(userId));
    }

    /**
     * 전체 캐시를 비웁니다. (운영 관리용)
     */
    public void clearAll() {
        cache.clear();
        logger.info("[IdMapping] 전체 캐시 클리어");
    }

    /**
     * 현재 캐시 크기를 반환합니다.
     */
    public int getCacheSize() {
        return cache.size();
    }

    // =========================================================================
    // 유틸리티
    // =========================================================================

    /** SNS 임시 ID를 로그용으로 마스킹 */
    private String maskId(String id) {
        if (id != null && id.startsWith("SNS!") && id.length() > 12) {
            return id.substring(0, 12) + "***";
        }
        return id;
    }

    // =========================================================================
    // 캐시 엔트리
    // =========================================================================

    private static class CacheEntry {
        final String value;
        final long expireAt;

        CacheEntry(String value, long expireAt) {
            this.value = value;
            this.expireAt = expireAt;
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expireAt;
        }
    }
}
