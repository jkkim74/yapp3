package com.kt.yapp.domain.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * IAMUI 통합 로그인 요청 VO (BE-CMN-005)
 *
 * <pre>
 * BE-LOGIN-001 (POST /na/user/login/iamui) 의 @RequestBody.
 * IAMUI WebView 인증 완료 후 앱이 브릿지를 통해 수신한
 * encTokenId와 userId를 백엔드에 전달한다.
 *
 * cpId/secret은 앱에서만 처리하므로 백엔드 수신 불필요.
 * </pre>
 *
 * @author YBOX IAMUI 전환 개발팀
 * @version 1.0 (2026-03-04, BE-CMN-005)
 */
@Data
public class IamUiLoginReq {

    /**
     * IAMUI에서 발급한 암호화 토큰 (필수).
     * AES128로 암호화된 tokenId.
     * IamUiTokenService.validateAndGetCredentialId()에서 복호화 후 SHUB 호출.
     */
    @ApiModelProperty(value = "IAMUI 암호화 토큰 (필수)", required = true)
    private String encTokenId;

    /**
     * KT 아이디 (userId).
     * SHUB callFn140(계약목록 조회) 호출 시 사용.
     * IAMUI 인증 완료 후 브릿지 콜백에서 앱이 전달.
     */
    @ApiModelProperty(value = "KT 아이디 (userId)", required = true)
    private String userId;
}
