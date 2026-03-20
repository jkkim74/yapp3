package com.kt.yapp.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * IAMUI 토큰 정보 VO (BE-CMN-004)
 *
 * <pre>
 * IAMUI WebView 인증 완료 후 앱이 전달하는 encTokenId를
 * 복호화하여 획득한 tokenId와 SHUB 연동으로 획득한 credentialId를 담는다.
 *
 * encTokenId → (AES128 복호화) → tokenId
 * tokenId    → (SHUB OIF_188)  → credentialId
 * </pre>
 *
 * @author YBOX IAMUI 전환 개발팀
 * @version 1.0 (2026-03-04, BE-CMN-004)
 */
@Data
public class IamUiTokenInfo {

    /** IAMUI에서 발급한 암호화 토큰 (앱 수신 원문) */
    @ApiModelProperty(value = "IAMUI 암호화 토큰 (AES128 암호화된 tokenId)")
    private String encTokenId;

    /** AES128 복호화 후 tokenId */
    @ApiModelProperty(value = "SHUB 연동용 tokenId (encTokenId 복호화 결과)")
    private String tokenId;

    /** SHUB OIF_188 응답으로 획득한 credentialId */
    @ApiModelProperty(value = "SHUB credentialId (callFn188 응답)")
    private String credentialId;

    /** SHUB OIF_188 응답 - credentialTypeCd */
    @ApiModelProperty(value = "크레덴셜 타입 코드")
    private String credentialTypeCd;

    /** SHUB OIF_188 응답 - 사용자 성명 */
    @ApiModelProperty(value = "사용자 성명 (SHUB 응답)")
    private String partyName;

    /** SHUB OIF_188 응답 - 로그인 ID (userId) */
    @ApiModelProperty(value = "로그인 ID (SHUB 응답)")
    private String loginId;

    /** SHUB OIF_188 응답 - 휴대폰 번호 */
    @ApiModelProperty(value = "휴대폰 번호 (SHUB 응답)")
    private String mobileNo;
}
