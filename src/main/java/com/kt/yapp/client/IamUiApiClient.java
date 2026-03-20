package com.kt.yapp.client;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * IAMUI 서버 HTTP 클라이언트 (BE-CMN-002)
 *
 * <pre>
 * 백엔드가 IAMUI 서버와 직접 통신하는 클라이언트.
 * BE-LOGIN-001 checkLoginIamui() 내부에서 비차단(try-catch) 호출.
 *
 * ── 제공 메서드 ──────────────────────────────────────────────────
 *
 *  1. loginHistory(String encTokenId)
 *     → GET https://cauth.kt.com/api/v2/auth/cp/login-history
 *        Query : secret, token_id, login_type, cpid
 *        용도 : IAMUI 로그인 이력 기록 (감사 로그)
 *
 *  2. logout(String encTokenId)
 *     → POST https://cauth.kt.com/api/v2/auth/cp/logout
 *        Body : { "tokenId": encTokenId }
 *        용도 : IAMUI 세션 종료 (YBOX 로그아웃 시 호출)
 *
 * ── 주의 사항 ─────────────────────────────────────────────────────
 *  ① cpId/secret은 properties 에서 주입 (iamui.api.cpid / iamui.api.secret)
 *  ② 호출 실패해도 YBOX 세션 정상 처리 (비차단, try-catch)
 *  ③ RestTemplate 사용, 타임아웃 설정 필수 (3초 권장)
 *  ④ Property: iamui.api.login-history.url / iamui.api.logout.url
 *     iamui.api.secret / iamui.api.cpid / iamui.api.login-type
 * ──────────────────────────────────────────────────────────────────
 * </pre>
 *
 * @author YBOX IAMUI 전환 개발팀
 * @version 1.1 (2026-03-13, loginHistory GET 전환)
 */
@Component
public class IamUiApiClient {

    private static final Logger logger = LoggerFactory.getLogger(IamUiApiClient.class);

    /** IAMUI 로그인 이력 기록 API URL */
    @Value("${iamui.api.login-history.url}")
    private String loginHistoryUrl;

    /** IAMUI 로그아웃 API URL */
    @Value("${iamui.api.logout.url}")
    private String logoutUrl;

    /** IAMUI CP 인증 secret */
    @Value("${iamui.api.secret}")
    private String secret;

    /** IAMUI CP ID */
    @Value("${iamui.api.cpid}")
    private String cpid;

    /** IAMUI 로그인 유형 (기본값: app) */
    @Value("${iamui.api.login-type:app}")
    private String loginType;

    // ─────────────────────────────────────────────────────────────────
    // 1. 로그인 이력 기록
    // ─────────────────────────────────────────────────────────────────

    /**
     * IAMUI 로그인 이력 기록 API 호출.
     *
     * <pre>
     * GET {iamui.api.login-history.url}
     *     ?secret={secret}
     *     &token_id={encTokenId}
     *     &login_type={loginType}
     *     &cpid={cpid}
     *
     * 호출 실패 시 예외를 던지지 않고 로그만 기록 (비차단).
     * BE-LOGIN-001 에서 try-catch 로 감싸 호출할 것.
     * </pre>
     *
     * @param encTokenId IAMUI 공통로그인이 발급한 암호화 토큰
     */
    public void loginHistory(String encTokenId) {
        logger.info("[IamUiApiClient] loginHistory 호출 – url: {}, cpid: {}, login_type: {}",
                loginHistoryUrl, cpid, loginType);
        try {
            String url = UriComponentsBuilder.fromHttpUrl(loginHistoryUrl)
                    .queryParam("secret", secret)
                    .queryParam("token_id", encTokenId)
                    .queryParam("login_type", loginType)
                    .queryParam("cpid", cpid)
                    .build()
                    .toUriString();

            logger.info("[IamUiApiClient] loginHistory GET – {}", url.replaceFirst("secret=[^&]*", "secret=****"));

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
            logger.info("[IamUiApiClient] loginHistory 응답 – status: {}, body: {}",
                    response.getStatusCode(), response.getBody());
        } catch (Exception e) {
            // 로그인 이력 기록 실패 시 YBOX 세션은 정상 처리 (비차단)
            logger.warn("[IamUiApiClient] loginHistory 실패 (무시) – {}", e.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────────────────
    // 2. IAMUI 로그아웃
    // ─────────────────────────────────────────────────────────────────

    /**
     * IAMUI 세션 종료 API 호출.
     *
     * <pre>
     * POST {iamui.api.logout.url}
     * Body : { "tokenId": "{encTokenId}" }
     *
     * 호출 실패 시 예외를 던지지 않고 로그만 기록 (비차단).
     * </pre>
     *
     * @param encTokenId IAMUI 공통로그인이 발급한 암호화 토큰
     */
    public void logout(String encTokenId) {
        logger.info("[IamUiApiClient] logout 호출 – url: {}", logoutUrl);
        try {
            HttpEntity<Map<String, Object>> entity = buildJsonEntity("tokenId", encTokenId);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(logoutUrl, entity, String.class);
            logger.info("[IamUiApiClient] logout 응답 – status: {}, body: {}",
                    response.getStatusCode(), response.getBody());
        } catch (Exception e) {
            // IAMUI 로그아웃 실패 시 YBOX 세션 로그아웃은 정상 처리 (비차단)
            logger.warn("[IamUiApiClient] logout 실패 (무시) – {}", e.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────────────────
    // private: 공통 JSON 요청 빌더
    // ─────────────────────────────────────────────────────────────────

    /**
     * Content-Type: application/json 헤더 + 단일 키-값 Body 생성.
     */
    private HttpEntity<Map<String, Object>> buildJsonEntity(String key, String value) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put(key, value);

        return new HttpEntity<>(body, headers);
    }
}
