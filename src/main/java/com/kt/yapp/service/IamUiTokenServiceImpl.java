package com.kt.yapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kt.yapp.domain.IamUiTokenInfo;
import com.kt.yapp.domain.Oif265Response;
import com.kt.yapp.exception.YappException;
import com.kt.yapp.util.YappUtil;

/**
 * IAMUI 토큰 검증 서비스 구현체 (BE-CMN-003)
 *
 * <pre>
 * IAMUI WebView 공통로그인 완료 후 앱이 전달한 encTokenId를
 * KT API Link OIF_265(getComLoginTokenIdNoCredtId)를 통해 복호화하여
 * 복호화된 token_id 및 사용자 정보를 획득한다.
 *
 * ── 처리 흐름 ──────────────────────────────────────────────────
 * encTokenId (앱 전달, IAMUI 공통로그인 발급 암호화 토큰)
 *   ① ShubService.callFn265(encTokenId)
 *        → POST https://cus.api.kt.com/scap/v1.0/getComLoginTokenIdNoCredtId
 *        → Body: { "request": { "enc_token_id": "{encTokenId}" } }
 *        → 응답: token_id (복호화), username, virtual_subscpn_type_cd 등
 *   ② Oif265Response 검증 (returncode == "1", token_id 존재)
 *   ③ IamUiTokenInfo 구성 후 반환
 *      → BE-LOGIN-001 에서 callFn140(userId, tokenId) → 계약목록 조회
 *
 * ── AS-IS 비교 ────────────────────────────────────────────────
 *   AS-IS checkLoginNewSimple():
 *     callFn198(encTokenId) → 내부에서 AES 암호화 후 SHUB REST 호출 → token_id 반환
 *     callFn188(token_id)   → SOAP → credentialId 반환
 *
 *   TO-BE (IAMUI 전환):
 *     callFn265(encTokenId) → KT API Link REST → token_id 직접 반환 (OIF_265)
 *     callFn188 불필요 – OIF_265가 token_id를 직접 복호화하여 제공
 *     BE-LOGIN-001 에서 callFn140(userId, token_id) 로 계약목록 조회
 *
 * ── OIF_265 응답 필드 활용 ─────────────────────────────────────
 *   token_id                : callFn140 호출용 복호화된 토큰 ID
 *   username                : KT 로그인 ID (kt_userid) 교차 검증에 활용 가능
 *   virtual_subscpn_type_cd : 계정유형 (01=KT ID, 09=회선ID) → MEM_STATUS 결정 참고
 *   phone_number            : 대표 전화번호
 *   token_expiry            : 토큰 만료일 (yyyyMMddHHmmss)
 *
 * ── MEM_STATUS 정의 (로그인 타입이 아닌 계정 유형) ───────────
 *   G0001 : KT ID 보유  + KT 회선 가입 완료
 *   G0002 : KT ID 없음  + KT 회선 가입 완료
 *   G0003 : KT ID만 보유 (KT 회선 기반 최초 진입)
 *   ※ SNS 로그인으로 진입한 정회원도 G0001 이 될 수 있으며,
 *      memStatus 는 로그인 수단과 무관한 계정 유형이다.
 *      IAMUI 전환 이후에도 BE-LOGIN-001 에서 동일 로직으로 결정된다.
 *
 * ── 예외 처리 ──────────────────────────────────────────────────
 *   - encTokenId 누락     → YappException("CHECK_MSG")
 *   - OIF_265 호출 실패   → YappException("SHUB_MSG")  (callFn265 내부에서 발생)
 *   - token_id 미반환     → YappException("SHUB_MSG")  (callFn265 내부에서 발생)
 * ──────────────────────────────────────────────────────────────
 * </pre>
 *
 * @author YBOX IAMUI 전환 개발팀
 * @version 2.0 (2026-03-04, BE-CMN-003 – OIF_265 적용)
 * @see IamUiTokenService
 * @see ShubService#callFn265(String)
 */
@Service
public class IamUiTokenServiceImpl implements IamUiTokenService {

    private static final Logger logger = LoggerFactory.getLogger(IamUiTokenServiceImpl.class);

    @Autowired
    private ShubService shubService;

    @Autowired
    private CommonService cmnService;

    /**
     * {@inheritDoc}
     *
     * <pre>
     * 처리 순서:
     *   1. encTokenId null/빈값 검증
     *   2. ShubService.callFn265(encTokenId) → OIF_265 호출
     *      → 성공 시 token_id, username, virtual_subscpn_type_cd, phone_number 반환
     *      → 실패 시 callFn265 내부에서 YappException("SHUB_MSG") 발생
     *   3. IamUiTokenInfo 구성 후 반환
     *      (encTokenId, tokenId, username, virtualSubscpnTypeCd, phoneNumber, tokenExpiry)
     * </pre>
     */
    @Override
    public IamUiTokenInfo validateAndGetCredentialId(String encTokenId) throws Exception {

        // ── 1. encTokenId 필수 검증 ────────────────────────────
        if (YappUtil.isEmpty(encTokenId)) {
            logger.warn("[IamUiTokenService] encTokenId is null or empty");
            throw new YappException("CHECK_MSG", cmnService.getMsg("ERR_IAMUI_TOKEN_EMPTY"));
        }
        logger.info("[IamUiTokenService] encTokenId 수신 완료, OIF_265 호출 시작");

        // ── 2. OIF_265 호출 → token_id 획득 ───────────────────
        // callFn265() 내부에서 returncode 검증 + token_id 존재 확인 수행
        // 실패 시 YappException("SHUB_MSG") 자동 발생
        Oif265Response oif265Resp = shubService.callFn265(encTokenId);

        logger.info("[IamUiTokenService] OIF_265 성공 – token_id 획득 완료");

        // ── 3. IamUiTokenInfo 구성 후 반환 ────────────────────
        // token_id  → BE-LOGIN-001 에서 callFn140(userId, tokenId) 로 계약목록 조회
        // username  → KT 아이디 교차 검증 또는 세션 저장 시 활용 가능
        IamUiTokenInfo tokenInfo = new IamUiTokenInfo();
        tokenInfo.setEncTokenId(encTokenId);
        tokenInfo.setTokenId(oif265Resp.getTokenId());                      // OIF_265: response.token_id
        tokenInfo.setCredentialId(null);                                     // OIF_265는 credentialId 미제공
        tokenInfo.setCredentialTypeCd(oif265Resp.getVirtualSubscpnTypeCd()); // OIF_265: virtual_subscpn_type_cd
        tokenInfo.setPartyName(null);                                        // OIF_265는 partyName 미제공
        tokenInfo.setLoginId(oif265Resp.getUsername());                      // OIF_265: username (kt_userid)
        tokenInfo.setMobileNo(oif265Resp.getPhoneNumber());                  // OIF_265: phone_number

        return tokenInfo;
    }
}
