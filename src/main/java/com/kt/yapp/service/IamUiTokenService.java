package com.kt.yapp.service;

import com.kt.yapp.domain.IamUiTokenInfo;

/**
 * IAMUI 토큰 검증 서비스 인터페이스 (BE-CMN-003)
 *
 * <pre>
 * IAMUI WebView 인증 완료 후 앱이 전달한 encTokenId를 검증하고
 * SHUB 연동을 통해 credentialId를 획득한다.
 *
 * ── 처리 흐름 ──────────────────────────────────────────────────
 * encTokenId (앱 전달, IAMUI 공통로그인 발급 암호화 토큰)
 *   ① ShubService.callFn265(encTokenId)
 *        → POST https://cus.api.kt.com/scap/v1.0/getComLoginTokenIdNoCredtId
 *        → Body: { "request": { "enc_token_id": "{encTokenId}" } }
 *        → 응답: Oif265Response (token_id, username, virtual_subscpn_type_cd 등)
 *   ② IamUiTokenInfo 반환
 *      → BE-LOGIN-001 에서 callFn140(userId, tokenId) → 계약목록 조회
 * ──────────────────────────────────────────────────────────────
 *
 * ── MEM_STATUS 정의 (로그인 타입이 아니라 계정 유형) ───────────
 *   G0001 : KT ID 보유  + KT 회선 가입 완료
 *           (userKtInfo.joinStatus == "G0001")
 *   G0002 : KT ID 없음  + KT 회선 가입 완료
 *           (userKtInfo.joinStatus != "G0001" 이고 KT 회선 존재)
 *   G0003 : KT ID만 보유 (KT 회선 기반 최초 진입 – KT 간편로그인 / ID-PW 로그인)
 *           YBOX 계정이 아직 없는 상태이므로 BE-LOGIN-001 처리 후 신규 생성
 *
 *   ※ memStatus 는 로그인 수단(SNS·간편·ID-PW)과 무관하다.
 *     SNS 로그인으로 진입한 정회원도 G0001 이 될 수 있다.
 *     IAMUI 전환 후에도 동일 로직으로 결정되므로 변경 없음.
 * ──────────────────────────────────────────────────────────────
 *
 * ── 설계 원칙 ──────────────────────────────────────────────────
 *   - Interface/Impl 분리: OIF 번호 변경 시 Impl 만 교체
 *   - IAMUI 연동 오류는 YappException("CHECK_MSG") 또는
 *     YappException("SHUB_MSG")로 처리
 *   - 단위 테스트 시 Mock 대상
 * ──────────────────────────────────────────────────────────────
 * </pre>
 *
 * @author YBOX IAMUI 전환 개발팀
 * @version 1.0 (2026-03-04, BE-CMN-003)
 */
public interface IamUiTokenService {

    /**
     * encTokenId를 검증하여 credentialId를 반환한다.
     *
     * <pre>
     * 처리 순서:
     *   1. encTokenId AES128 복호화 → tokenId  ← OIF_265 이전 방식 (AS-IS callFn198 흐름)
     *   2. ShubService.callFn265(encTokenId) → Oif265Response
     *      → token_id, username, virtual_subscpn_type_cd, phone_number 획득
     *   3. IamUiTokenInfo 반환
     *      (encTokenId, tokenId=OIF_265 token_id, loginId=username, mobileNo=phone_number 등)
     *
     * YappException 생성자 규칙:
     *   - 2-arg YappException(msgTypeCode, msg)
     *       : msgTypeCode 설정, msgCd = "999" (EnumRsltCd.C999 기본값)
     *   - 5-arg YappException(msgTypeCode, msgCd, msg, msgDetail, msgKey)
     *       : SHUB_MSG 시 errorcode/errordescription/transactionid 포함
     *
     * 예외:
     *   - encTokenId 누락/null     → YappException("CHECK_MSG")
     *   - OIF_265 호출 실패        → YappException("SHUB_MSG") – callFn265 내부 발생
     *   - token_id 미반환          → YappException("SHUB_MSG") – callFn265 내부 발생
     * </pre>
     *
     * @param encTokenId 앱에서 전달받은 IAMUI 암호화 토큰 (AES128 암호화된 tokenId)
     * @return IamUiTokenInfo (encTokenId, tokenId, credentialId, credentialTypeCd, partyName, loginId, mobileNo)
     * @throws Exception SHUB 연동 오류 또는 검증 실패
     */
    IamUiTokenInfo validateAndGetCredentialId(String encTokenId) throws Exception;
}
