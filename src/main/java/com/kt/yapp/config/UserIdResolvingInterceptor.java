package com.kt.yapp.config;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kt.yapp.service.IdMappingCacheService;

/**
 * MyBatis Interceptor: SNS 임시 ID / KT ID 자동 변환 플러그인 (v3)
 * 
 * <h3>v3 핵심 변경: SNS 임시 아이디(SNS!XXXXX) 라이프사이클 대응</h3>
 * <pre>
 * [문제] SNS 로그인 신규 사용자가 "SNS!S0001_abc123" 임시 ID로 YBOX 가입 후
 *        정식 KT ID "honggildong" 발급 시 TB_USER_KT에 레코드 2개 생성됨.
 * 
 * [해결] KT ID 전환 시 기존 레코드의 PK를 변경(DELETE+INSERT)하고
 *        공용 테이블의 USER_ID를 일괄 변경.
 *        Interceptor는 과도기에 SNS 임시 ID를 KT ID로 자동 치환.
 * </pre>
 * 
 * <h3>테이블 구조:</h3>
 * <pre>
 *   TB_USER    (PK = CNTR_NO)  : 회선 사용자 테이블
 *   TB_USER_KT (PK = USER_ID)  : KT ID / SNS 임시 사용자 테이블
 *     - USER_ID    : KT ID 또는 "SNS!XXXXX" (PK)
 *     - SNS_TEMP_ID: KT ID 전환 후 이전 임시 ID 이력 보존 (신규 컬럼)
 *     - CREDENTIAL_ID: IAMUI credential_id (영구 식별자, 신규 컬럼)
 *   공용 테이블 : CNTR_NO + USER_ID 듀얼 키, choose 블록 분기
 * </pre>
 * 
 * <h3>사용자 유형별 memStatus:</h3>
 * <pre>
 *   G0001 : 정회원 + KT ID (TB_USER + TB_USER_KT)
 *   G0002 : 정회원 (TB_USER만)
 *   G0003 : 준회원 KT ID (TB_USER_KT만, USER_ID = KT ID)
 *   G0004 : SNS 임시회원 (TB_USER_KT만, USER_ID = "SNS!XXXXX")
 * </pre>
 * 
 * <h3>변환 규칙:</h3>
 * <ul>
 *   <li>cntrNo 존재 → 변환 안 함 (회선 사용자, 기존 로직 유지)</li>
 *   <li>userId = "SNS!XXXXX" → KT ID 매핑 조회 후 치환 (전환 완료 사용자)</li>
 *   <li>userId = 일반값 → self-mapping 확인 (이미 KT ID면 변환 안 함)</li>
 *   <li>KT ID 매핑 없음 → 원본 유지 (아직 SNS만 있는 사용자)</li>
 * </ul>
 * 
 * <h3>제외 대상 Namespace:</h3>
 * <ul>
 *   <li>mybatis.mapper.user      : TB_USER 직접 접근 (PK=CNTR_NO)</li>
 *   <li>mybatis.mapper.userkt    : TB_USER_KT 직접 접근 (PK=USER_ID)</li>
 *   <li>mybatis.mapper.idmapping : 매핑 조회 쿼리 (순환 방지)</li>
 *   <li>mybatis.mapper.common    : 인증/코드 조회 등 비사용자 쿼리</li>
 * </ul>
 * 
 * @see IdMappingCacheService
 * @see MyBatisInterceptorConfig
 */
@Intercepts({
    @Signature(
        type = Executor.class, 
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
    ),
    @Signature(
        type = Executor.class, 
        method = "update",
        args = {MappedStatement.class, Object.class}
    )
})
public class UserIdResolvingInterceptor implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger(UserIdResolvingInterceptor.class);

    /** SNS 임시 아이디 접두사 */
    private static final String SNS_TEMP_PREFIX = "SNS!";

    /**
     * Interceptor 내부에서 DB 조회 시 재진입 방지용 ThreadLocal 플래그.
     */
    private static final ThreadLocal<Boolean> RESOLVING = ThreadLocal.withInitial(() -> Boolean.FALSE);

    /**
     * ID 변환 로직을 건너뛸 Mapper namespace 목록.
     */
    private static final Set<String> SKIP_NAMESPACES = new HashSet<>();

    static {
        SKIP_NAMESPACES.add("mybatis.mapper.user");        // TB_USER 직접 접근 (PK=CNTR_NO)
        SKIP_NAMESPACES.add("mybatis.mapper.userkt");      // TB_USER_KT 직접 접근 (PK=USER_ID)
        SKIP_NAMESPACES.add("mybatis.mapper.idmapping");   // 매핑 조회 쿼리 (순환 방지)
        SKIP_NAMESPACES.add("mybatis.mapper.common");      // 인증/코드 등 비사용자 쿼리
    }

    /** SNS ID -> KT ID 매핑 캐시 서비스 */
    private IdMappingCacheService idMappingCacheService;

    /**
     * IdMappingCacheService 주입.
     * MyBatis Plugin은 Spring Bean이 아니므로 수동 주입 필요.
     * 
     * @see MyBatisInterceptorConfig#addInterceptor()
     */
    public void setIdMappingCacheService(IdMappingCacheService idMappingCacheService) {
        this.idMappingCacheService = idMappingCacheService;
    }

    // =========================================================================
    // 핵심 메서드: intercept
    // =========================================================================

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        // -- 1) 재진입 체크 --
        if (RESOLVING.get()) {
            return invocation.proceed();
        }

        // -- 2) Mapper namespace 확인 --
        MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
        String statementId = ms.getId();

        if (shouldSkip(statementId)) {
            return invocation.proceed();
        }

        // -- 3) 파라미터 추출 --
        Object parameter = invocation.getArgs()[1];
        if (parameter == null) {
            return invocation.proceed();
        }

        // -- 4) userId 변환 시도 --
        try {
            resolveUserId(parameter, statementId);
        } catch (Exception e) {
            logger.warn("[UserIdResolver] 변환 중 오류, 원본 유지. statement={}, error={}", 
                        statementId, e.getMessage());
        }

        // -- 5) 원래 쿼리 실행 --
        return invocation.proceed();
    }

    // =========================================================================
    // 파라미터 타입별 userId 추출 및 변환
    // =========================================================================

    @SuppressWarnings("unchecked")
    private void resolveUserId(Object parameter, String statementId) {

        if (parameter instanceof Map) {
            Map<String, Object> paramMap = (Map<String, Object>) parameter;

            // cntrNo가 있으면 회선 사용자 -> 변환 불필요
            String cntrNo = getStringValue(paramMap.get("cntrNo"));
            if (isNotEmpty(cntrNo)) {
                logger.debug("[UserIdResolver] cntrNo 존재({}), 변환 건너뜀. statement={}", 
                             cntrNo, statementId);
                return;
            }

            // userId 확인
            String userId = getStringValue(paramMap.get("userId"));
            if (isEmpty(userId)) {
                return;
            }

            // KT ID로 변환 시도
            String resolvedId = resolveToKtId(userId);
            if (resolvedId != null && !resolvedId.equals(userId)) {
                paramMap.put("userId", resolvedId);
                logger.info("[UserIdResolver] Map userId 변환: {} -> {}, statement={}", 
                            maskSnsId(userId), resolvedId, statementId);
            }

        } else {
            resolveFromDto(parameter, statementId);
        }
    }

    /**
     * DTO 객체에서 userId를 리플렉션으로 추출/변환합니다.
     */
    private void resolveFromDto(Object dto, String statementId) {
        try {
            String cntrNo = getFieldValue(dto, "getCntrNo");
            if (isNotEmpty(cntrNo)) {
                return;
            }

            String userId = getFieldValue(dto, "getUserId");
            if (isEmpty(userId)) {
                return;
            }

            String resolvedId = resolveToKtId(userId);
            if (resolvedId != null && !resolvedId.equals(userId)) {
                Method setter = dto.getClass().getMethod("setUserId", String.class);
                setter.invoke(dto, resolvedId);
                logger.info("[UserIdResolver] DTO userId 변환: {} -> {}, type={}, statement={}", 
                            maskSnsId(userId), resolvedId, dto.getClass().getSimpleName(), statementId);
            }

        } catch (NoSuchMethodException e) {
            // userId 필드가 없는 DTO -> 변환 대상 아님
        } catch (Exception e) {
            logger.debug("[UserIdResolver] DTO 접근 실패: {}", e.getMessage());
        }
    }

    // =========================================================================
    // 핵심: SNS ID -> KT ID 변환 로직
    // =========================================================================

    /**
     * SNS 임시 ID 또는 기타 ID를 KT ID로 변환합니다.
     * 
     * <pre>
     * 변환 흐름 (v3 - 3단계 조회):
     * 
     *   1단계: 캐시 조회 (ConcurrentHashMap)
     *          → 히트: 즉시 반환
     *          → "NONE": 매핑 없음, null 반환 (원본 유지)
     * 
     *   2단계: TB_USER_KT.SNS_TEMP_ID로 조회
     *          (SNS 임시 ID -> KT ID 전환 완료된 사용자)
     * 
     *   3단계: TB_USER.CREDENTIAL_ID 기반 폴백 조회
     *          (credential_id 연결을 통한 조회)
     * 
     *   4단계: 결과 캐시 저장 후 반환
     * </pre>
     */
    private String resolveToKtId(String userId) {
        if (idMappingCacheService == null) {
            logger.warn("[UserIdResolver] IdMappingCacheService가 주입되지 않음");
            return null;
        }

        try {
            RESOLVING.set(Boolean.TRUE);
            return idMappingCacheService.resolveKtId(userId);
        } finally {
            RESOLVING.set(Boolean.FALSE);
        }
    }

    // =========================================================================
    // 유틸리티 메서드
    // =========================================================================

    private boolean shouldSkip(String statementId) {
        for (String ns : SKIP_NAMESPACES) {
            if (statementId.startsWith(ns)) {
                return true;
            }
        }
        return false;
    }

    /**
     * SNS 임시 ID 여부를 판단합니다.
     * 형식: "SNS!{SNS_TYPE}_{SNS_KEY}"
     * 예: "SNS!S0001_abc123def456"
     */
    public static boolean isSnsTemporaryId(String userId) {
        return userId != null && userId.startsWith(SNS_TEMP_PREFIX);
    }

    /** SNS 임시 ID를 로그용으로 마스킹 */
    private String maskSnsId(String userId) {
        if (isSnsTemporaryId(userId) && userId.length() > 12) {
            return userId.substring(0, 12) + "***";
        }
        return userId;
    }

    private String getFieldValue(Object obj, String getterName) {
        try {
            Method getter = obj.getClass().getMethod(getterName);
            Object value = getter.invoke(obj);
            return value == null ? null : value.toString();
        } catch (Exception e) {
            return null;
        }
    }

    private String getStringValue(Object obj) {
        return obj == null ? null : obj.toString();
    }

    private boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    private boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    // =========================================================================
    // MyBatis Plugin 필수 메서드
    // =========================================================================

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // 향후 설정 외부화 시 사용
    }
}
