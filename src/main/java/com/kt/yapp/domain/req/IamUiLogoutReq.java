package com.kt.yapp.domain.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * IAMUI 로그아웃 요청 VO (BE-LGOUT-001)
 *
 * <pre>
 * BE-LGOUT-001 (POST /user/logout) 에서 IAMUI 세션 종료에 사용되는
 * encTokenId를 선택적으로 수신하기 위한 요청 VO.
 *
 * encTokenId는 IAMUI 통합 로그인(BE-LOGIN-001)을 통해 로그인한
 * 사용자만 보유하므로 optional 처리:
 *   - encTokenId 존재 → iamUiApiClient.logout(encTokenId) 호출
 *   - encTokenId 없음 → IAMUI logout 스킵 (AS-IS 방식 로그인 사용자)
 *
 * snsType은 기존 AS-IS 방식 그대로 유지.
 *
 * ── AS-IS 호출 방식과의 호환 ─────────────────────────────────────
 *   기존 클라이언트: POST /user/logout?snsType=xxx
 *     → snsType을 @RequestParam으로 별도 수신하므로 VO 없이 동작
 *   IAMUI 클라이언트: POST /user/logout  Body: { encTokenId: "..." }
 *     → 이 VO를 @RequestBody로 수신
 * ─────────────────────────────────────────────────────────────────
 * </pre>
 *
 * @author YBOX IAMUI 전환 개발팀
 * @version 1.0 (2026-03-09, BE-LGOUT-001)
 *
 * Modification Information
 * Mod Date        Modifier        Description
 * ========================================
 * 2026-03-09      YBOX Dev        최초 작성 (BE-LGOUT-001)
 */
@Data
public class IamUiLogoutReq {

    /**
     * IAMUI 암호화 토큰 (선택).
     *
     * <pre>
     * IAMUI 통합 로그인(BE-LOGIN-001)으로 로그인한 사용자가 전달.
     * IAMUI 서버의 세션 종료를 위해 POST {iamui.api.logout.url}로 전달.
     * null 또는 빈 값이면 IAMUI logout 호출 스킵.
     * </pre>
     */
    @ApiModelProperty(value = "IAMUI 암호화 토큰 (선택 – IAMUI 로그인 사용자만 전달)", required = false)
    private String encTokenId;
}
