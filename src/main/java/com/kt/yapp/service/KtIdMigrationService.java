package com.kt.yapp.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * KT ID 전환 시 SNS 임시 ID 데이터를 일괄 마이그레이션하는 서비스 (v3)
 * 
 * <h3>v3 핵심 변경: 중복 레코드 방지를 위한 PK 변경 전략</h3>
 * <pre>
 * [문제] SNS 로그인 시 TB_USER_KT에 USER_ID="SNS!XXXXX"로 INSERT.
 *        이후 KT ID 발급 시 USER_ID="honggildong"으로 새 INSERT하면
 *        동일 사용자에 대해 2개 레코드가 생김.
 * 
 * [해결] KT ID 전환 시 기존 SNS 레코드를 DELETE하고
 *        새 PK(KT ID)로 INSERT (기존 데이터 보존).
 *        SNS_TEMP_ID 컬럼에 이전 임시 ID 이력 저장.
 * </pre>
 * 
 * <h3>처리 흐름:</h3>
 * <pre>
 *   migrateToKtId(credentialId, newKtId, oldSnsId)
 *        |
 *        +-- [1] TB_USER_KT PK 변경 (DELETE + INSERT)
 *        |       USER_ID: "SNS!S0001_abc123" -> "honggildong"
 *        |       SNS_TEMP_ID: NULL -> "SNS!S0001_abc123" (이력 보존)
 *        |       CREDENTIAL_ID: NULL -> "cred_abc123"
 *        |       MEM_STATUS: "G0004" -> "G0003"
 *        |
 *        +-- [2] TB_USER_KT 종속 테이블 USER_ID 일괄 변경
 *        |       TB_TERMS_AGREE_KT, TB_SLEEP_USER_KT,
 *        |       TB_UUID_KT, TB_PREFERENCE_INFO_KT
 *        |
 *        +-- [3] 공용 테이블 USER_ID 일괄 변경
 *        |       TB_REWARD_INFO, TB_CLASS_JOIN, ...
 *        |
 *        +-- [4] TB_ENTRY_INFO.RECV_USER_ID 변경
 *        |
 *        +-- [5] TB_USER.KT_ID 갱신 (회선 연동 시)
 *        |
 *        +-- [6] 캐시 갱신
 * </pre>
 */
@Service
public class KtIdMigrationService {

    private static final Logger logger = LoggerFactory.getLogger(KtIdMigrationService.class);

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    private IdMappingCacheService idMappingCacheService;

    /**
     * 공용 테이블 (USER_ID 컬럼) - SNS ID -> KT ID 일괄 변경 대상
     */
    private static final List<String> SHARED_USER_ID_TABLES = Arrays.asList(
        "TB_REWARD_INFO",
        "TB_ATTEND_DAY_CHECK",
        "TB_VOTE_HISTORY",
        "TB_EVENT_LIKE",
        "TB_EVENT_REPLY",
        "TB_TICKET_REWARD_INFO",
        "TB_CLASS_JOIN",
        "TB_THREAD_PUSH_SEND",
        "TB_TICKET_GIFT_REWARD_INFO"
    );

    /**
     * TB_USER_KT 종속 테이블 (USER_ID 컬럼)
     */
    private static final List<String> KT_DEPENDENT_USER_ID_TABLES = Arrays.asList(
        "TB_TERMS_AGREE_KT",
        "TB_SLEEP_USER_KT",
        "TB_UUID_KT"
    );

    /**
     * TB_USER_KT 종속 테이블 (KT_ID 컬럼) - TB_PREFERENCE_INFO_KT만 해당
     */
    private static final List<String> KT_DEPENDENT_KT_ID_TABLES = Arrays.asList(
        "TB_PREFERENCE_INFO_KT"
    );

    /** RECV_USER_ID 컬럼 테이블 */
    private static final List<String> RECV_USER_ID_TABLES = Arrays.asList(
        "TB_ENTRY_INFO"
    );

    // =========================================================================
    // 메인 마이그레이션 메서드
    // =========================================================================

    /**
     * SNS 임시 사용자가 KT ID를 획득했을 때 호출합니다.
     * 
     * <pre>
     * 핵심: TB_USER_KT의 PK(USER_ID)를 SNS 임시 ID에서 KT ID로 변경합니다.
     *       PK는 UPDATE로 변경 불가하므로 DELETE + INSERT 전략을 사용합니다.
     * 
     * 변경 전:
     *   TB_USER_KT: USER_ID="SNS!S0001_abc123", MEM_STATUS=G0004
     *   TB_REWARD_INFO: USER_ID="SNS!S0001_abc123"
     * 
     * 변경 후:
     *   TB_USER_KT: USER_ID="honggildong", SNS_TEMP_ID="SNS!S0001_abc123",
     *               CREDENTIAL_ID="cred_abc123", MEM_STATUS=G0003
     *   TB_REWARD_INFO: USER_ID="honggildong"
     * 
     * ★ TB_USER_KT 레코드는 1개만 존재! 중복 없음!
     * </pre>
     * 
     * @param credentialId IAMUI credential_id (영구 식별자)
     * @param newKtId      새로 획득한 KT ID
     * @param oldSnsId     기존 SNS 임시 ID (예: "SNS!S0001_abc123").
     *                     null이면 credentialId 또는 DB에서 조회
     */
    @Transactional
    public void migrateToKtId(String credentialId, String newKtId, String oldSnsId) {
        logger.info("[KtIdMigration] ===== v3 마이그레이션 시작 =====");
        logger.info("[KtIdMigration] credentialId={}, newKtId={}, oldSnsId={}", 
                    credentialId, newKtId, maskSnsId(oldSnsId));

        // -- 1) 기존 SNS ID 확인 --
        if (oldSnsId == null || oldSnsId.trim().isEmpty()) {
            oldSnsId = sqlSessionTemplate.selectOne(
                "mybatis.mapper.idmapping.getOldSnsIdByCredentialId", credentialId);
            
            if (oldSnsId == null) {
                logger.warn("[KtIdMigration] 기존 SNS 사용자를 찾을 수 없음. credentialId={}", credentialId);
                return;
            }
            logger.info("[KtIdMigration] 기존 SNS ID 조회 완료: {}", maskSnsId(oldSnsId));
        }

        // 이미 같으면 스킵
        if (newKtId.equals(oldSnsId)) {
            logger.info("[KtIdMigration] 이미 KT ID 사용 중, 스킵. userId={}", newKtId);
            return;
        }

        // 이미 KT ID 레코드가 존재하는지 확인
        Integer existingKtCount = sqlSessionTemplate.selectOne(
            "mybatis.mapper.idmapping.checkKtIdExists", newKtId);
        
        int totalUpdated = 0;

        if (existingKtCount != null && existingKtCount > 0) {
            // -- KT ID 레코드가 이미 존재: SNS 레코드만 삭제 --
            logger.info("[KtIdMigration] KT ID '{}' 레코드 이미 존재. SNS 레코드 병합 처리", newKtId);
            
            // SNS 레코드 삭제
            sqlSessionTemplate.delete(
                "mybatis.mapper.idmapping.deleteSnsRecord", oldSnsId);
            logger.info("[KtIdMigration] SNS 레코드 삭제 완료: {}", maskSnsId(oldSnsId));

            // 기존 KT ID 레코드에 SNS_TEMP_ID, CREDENTIAL_ID 갱신
            Map<String, Object> updateParam = new HashMap<>();
            updateParam.put("userId", newKtId);
            updateParam.put("snsTemporaryId", oldSnsId);
            updateParam.put("credentialId", credentialId);
            sqlSessionTemplate.update(
                "mybatis.mapper.idmapping.updateKtRecordWithSnsInfo", updateParam);
            
        } else {
            // -- KT ID 레코드 미존재: PK 변경 (DELETE + INSERT) --
            logger.info("[KtIdMigration] PK 변경: {} -> {}", maskSnsId(oldSnsId), newKtId);

            Map<String, Object> pkChangeParam = new HashMap<>();
            pkChangeParam.put("oldSnsId", oldSnsId);
            pkChangeParam.put("newKtId", newKtId);
            pkChangeParam.put("snsTemporaryId", oldSnsId);
            pkChangeParam.put("credentialId", credentialId);
            pkChangeParam.put("newMemStatus", "G0003"); // SNS임시(G0004) -> 준회원(G0003)

            // DELETE + INSERT (기존 데이터 복사하면서 PK와 신규 컬럼 변경)
            sqlSessionTemplate.update(
                "mybatis.mapper.idmapping.migrateUserKtRecord", pkChangeParam);
            logger.info("[KtIdMigration] TB_USER_KT PK 변경 완료");
        }

        // -- 2) TB_USER_KT 종속 테이블 USER_ID 변경 --
        Map<String, Object> migrateParam = new HashMap<>();
        migrateParam.put("oldSnsId", oldSnsId);
        migrateParam.put("newKtId", newKtId);

        for (String table : KT_DEPENDENT_USER_ID_TABLES) {
            try {
                migrateParam.put("tableName", table);
                int count = sqlSessionTemplate.update(
                    "mybatis.mapper.idmapping.migrateUserId", migrateParam);
                totalUpdated += count;
                if (count > 0) {
                    logger.info("[KtIdMigration] {} USER_ID 갱신: {}건", table, count);
                }
            } catch (Exception e) {
                logger.error("[KtIdMigration] {} 갱신 실패: {}", table, e.getMessage());
            }
        }

        // TB_PREFERENCE_INFO_KT (KT_ID 컬럼 사용)
        for (String table : KT_DEPENDENT_KT_ID_TABLES) {
            try {
                migrateParam.put("tableName", table);
                int count = sqlSessionTemplate.update(
                    "mybatis.mapper.idmapping.migrateKtIdColumn", migrateParam);
                totalUpdated += count;
                if (count > 0) {
                    logger.info("[KtIdMigration] {} KT_ID 갱신: {}건", table, count);
                }
            } catch (Exception e) {
                logger.error("[KtIdMigration] {} KT_ID 갱신 실패: {}", table, e.getMessage());
            }
        }

        // -- 3) 공용 테이블 USER_ID 일괄 변경 --
        for (String table : SHARED_USER_ID_TABLES) {
            try {
                migrateParam.put("tableName", table);
                int count = sqlSessionTemplate.update(
                    "mybatis.mapper.idmapping.migrateUserId", migrateParam);
                totalUpdated += count;
                if (count > 0) {
                    logger.info("[KtIdMigration] {} USER_ID 갱신: {}건", table, count);
                }
            } catch (Exception e) {
                logger.error("[KtIdMigration] {} 갱신 실패: {}", table, e.getMessage());
            }
        }

        // -- 4) RECV_USER_ID 컬럼 처리 --
        for (String table : RECV_USER_ID_TABLES) {
            try {
                migrateParam.put("tableName", table);
                int count = sqlSessionTemplate.update(
                    "mybatis.mapper.idmapping.migrateRecvUserId", migrateParam);
                totalUpdated += count;
                if (count > 0) {
                    logger.info("[KtIdMigration] {} RECV_USER_ID 갱신: {}건", table, count);
                }
            } catch (Exception e) {
                logger.error("[KtIdMigration] {} RECV_USER_ID 갱신 실패: {}", table, e.getMessage());
            }
        }

        // -- 5) TB_USER KT_ID 갱신 (credentialId가 있는 경우) --
        if (credentialId != null && !credentialId.trim().isEmpty()) {
            Map<String, Object> userParam = new HashMap<>();
            userParam.put("credentialId", credentialId);
            userParam.put("newKtId", newKtId);
            
            int userUpdated = sqlSessionTemplate.update(
                "mybatis.mapper.idmapping.updateUserKtId", userParam);
            if (userUpdated > 0) {
                logger.info("[KtIdMigration] TB_USER.KT_ID 갱신: {}건", userUpdated);
            }
        }

        // -- 6) 캐시 갱신 --
        idMappingCacheService.updateMapping(oldSnsId, newKtId);

        logger.info("[KtIdMigration] ===== v3 마이그레이션 완료 =====");
        logger.info("[KtIdMigration] {} -> {}, 공용+종속 테이블 {}건 갱신", 
                    maskSnsId(oldSnsId), newKtId, totalUpdated);
    }

    // =========================================================================
    // 유틸리티
    // =========================================================================

    private String maskSnsId(String id) {
        if (id != null && id.startsWith("SNS!") && id.length() > 12) {
            return id.substring(0, 12) + "***";
        }
        return id;
    }
}
