package com.kt.yapp.service;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;  // Mockito 1.x: org.mockito.Matchers
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;  // Spring Boot 1.5.x → Mockito 1.x 패키지

import com.kt.yapp.domain.IamUiTokenInfo;
import com.kt.yapp.domain.Oif265Response;
import com.kt.yapp.exception.YappException;

/**
 * IamUiTokenService 단위 테스트 (BE-CMN-003 – OIF_265 적용)
 *
 * <pre>
 * 테스트 대상: IamUiTokenServiceImpl.validateAndGetCredentialId()
 *
 * ── MEM_STATUS 정의 (로그인 타입이 아닌 계정 유형) ──────────────
 *   G0001 : KT ID 보유  + KT 회선 가입 완료
 *   G0002 : KT ID 없음  + KT 회선 가입 완료
 *   G0003 : KT ID만 보유 (KT 회선 기반 최초 진입)
 *   ※ SNS 로그인으로 진입한 정회원도 G0001 이 될 수 있으며,
 *      memStatus 는 로그인 수단과 무관한 계정 유형이다.
 *
 * ── OIF_265 흐름 ────────────────────────────────────────────────
 *   encTokenId
 *     → ShubService.callFn265(encTokenId)
 *       → POST /scap/v1.0/getComLoginTokenIdNoCredtId
 *       → Oif265Response { returncode="1", response.token_id, response.username, ... }
 *     → IamUiTokenInfo 반환
 *
 * ── YappException 생성자 규칙 ────────────────────────────────────
 *   new YappException(msgTypeCode, msg)
 *       → msgTypeCode 설정, msgCd = "999" (EnumRsltCd.C999 기본값)
 *   new YappException(msgTypeCode, msgCd, msg, msgDetail, msgKey)  ← SHUB_MSG 용
 *
 * ── 테스트 케이스 ────────────────────────────────────────────────
 *   TC1. 정상: encTokenId → callFn265 성공 → IamUiTokenInfo 반환
 *   TC2. 오류: encTokenId null → YappException("CHECK_MSG", msgCd="999")
 *   TC3. 오류: encTokenId 빈문자열 → YappException("CHECK_MSG")
 *   TC4. 오류: callFn265 예외 발생 → YappException 전파
 *   TC5. 정상: IamUiTokenInfo 필드 전체 매핑 검증
 *   TC6. 정상: virtual_subscpn_type_cd "01"(KT ID) 매핑 검증
 *   TC7. 정상: virtual_subscpn_type_cd "09"(회선ID) 매핑 검증
 * </pre>
 */
@RunWith(MockitoJUnitRunner.class)  // org.mockito.runners (Mockito 1.x)
public class IamUiTokenServiceTest {

    @InjectMocks
    private IamUiTokenServiceImpl iamUiTokenService;

    @Mock
    private ShubService shubService;

    @Mock
    private CommonService cmnService;

    // ── 테스트 픽스처 ──────────────────────────────────────────
    private static final String VALID_ENC_TOKEN    = "7BE15E1E1BB36F7ED8E89E4D936B69FF142CC63750906F2465F769FB959CC0B1";
    private static final String VALID_TOKEN_ID     = "DECRYPTED_TOKEN_ID_12345";
    private static final String VALID_USERNAME     = "KTIDks";
    private static final String VALID_SUBSCPN_TYPE = "01";   // 01=KT ID, 09=회선ID
    private static final String VALID_PHONE_NUMBER = "01012345678";
    private static final String VALID_TOKEN_EXPIRY = "20261231235959";
    private static final String ERR_MSG_EMPTY      = "IAMUI 인증 토큰이 없습니다.";
    private static final String ERR_MSG_SHUB       = "IAMUI 인증 서비스 연동에 실패했습니다.";

    @Before
    public void setUp() throws Exception {
        when(cmnService.getMsg("ERR_IAMUI_TOKEN_EMPTY")).thenReturn(ERR_MSG_EMPTY);
        when(cmnService.getMsg("ERR_IAMUI_TOKEN_SHUB")).thenReturn(ERR_MSG_SHUB);
    }

    // ── TC1: 정상 케이스 – callFn265 성공 ───────────────────────
    @Test
    public void tc1_정상_encTokenId_검증_및_tokenId_반환() throws Exception {
        // given
        Oif265Response mockResp = buildSuccessResp265(
                VALID_TOKEN_ID, VALID_USERNAME, VALID_SUBSCPN_TYPE,
                VALID_PHONE_NUMBER, VALID_TOKEN_EXPIRY);

        when(shubService.callFn265(VALID_ENC_TOKEN)).thenReturn(mockResp);

        // when
        IamUiTokenInfo result = iamUiTokenService.validateAndGetCredentialId(VALID_ENC_TOKEN);

        // then
        assertNotNull("결과가 null이면 안됨", result);
        assertEquals(VALID_ENC_TOKEN, result.getEncTokenId());
        assertEquals(VALID_TOKEN_ID,  result.getTokenId());   // OIF_265 response.token_id
        assertEquals(VALID_USERNAME,  result.getLoginId());   // OIF_265 response.username

        verify(shubService, times(1)).callFn265(VALID_ENC_TOKEN);
    }

    // ── TC2: encTokenId null ────────────────────────────────────
    @Test
    public void tc2_오류_encTokenId_null() throws Exception {
        // when & then
        // YappException("CHECK_MSG", msg) → msgTypeCode="CHECK_MSG", msgCd="999"(기본값)
        try {
            iamUiTokenService.validateAndGetCredentialId(null);
            fail("YappException이 발생해야 함");
        } catch (YappException e) {
            assertEquals("CHECK_MSG", e.getMsgTypeCode());
            // 2-arg 생성자: msgCd는 EnumRsltCd.C999 기본값 "999" 유지
            assertEquals("999", e.getMsgCd());
            // getMessage()는 getMsg() 반환값
            assertEquals(ERR_MSG_EMPTY, e.getMessage());
        }
        verify(shubService, never()).callFn265(anyString());
    }

    // ── TC3: encTokenId 빈문자열 ────────────────────────────────
    @Test
    public void tc3_오류_encTokenId_빈문자열() throws Exception {
        try {
            iamUiTokenService.validateAndGetCredentialId("");
            fail("YappException이 발생해야 함");
        } catch (YappException e) {
            assertEquals("CHECK_MSG", e.getMsgTypeCode());
        }
        verify(shubService, never()).callFn265(anyString());
    }

    // ── TC4: callFn265 예외 전파 ────────────────────────────────
    @Test
    public void tc4_오류_callFn265_예외_전파() throws Exception {
        // given: callFn265 내부에서 YappException("SHUB_MSG") 발생
        YappException shubEx = new YappException("SHUB_MSG", "E0001",
                ERR_MSG_SHUB, "[OIF_265] 인증키오류", "TXN-001");
        when(shubService.callFn265(VALID_ENC_TOKEN)).thenThrow(shubEx);

        // when & then
        try {
            iamUiTokenService.validateAndGetCredentialId(VALID_ENC_TOKEN);
            fail("YappException이 발생해야 함");
        } catch (YappException e) {
            // callFn265에서 발생한 예외가 그대로 전파됨
            assertEquals("SHUB_MSG", e.getMsgTypeCode());
            assertEquals("E0001",    e.getMsgCd());
        }
    }

    // ── TC5: IamUiTokenInfo 필드 전체 매핑 검증 ────────────────
    @Test
    public void tc5_정상_IamUiTokenInfo_필드_전체_검증() throws Exception {
        // given
        Oif265Response mockResp = buildSuccessResp265(
                VALID_TOKEN_ID, VALID_USERNAME, VALID_SUBSCPN_TYPE,
                VALID_PHONE_NUMBER, VALID_TOKEN_EXPIRY);

        when(shubService.callFn265(VALID_ENC_TOKEN)).thenReturn(mockResp);

        // when
        IamUiTokenInfo result = iamUiTokenService.validateAndGetCredentialId(VALID_ENC_TOKEN);

        // then – 필드 매핑 전체 검증
        assertEquals(VALID_ENC_TOKEN,    result.getEncTokenId());
        assertEquals(VALID_TOKEN_ID,     result.getTokenId());           // OIF_265 response.token_id
        assertNull("OIF_265는 credentialId 미제공", result.getCredentialId());
        assertEquals(VALID_SUBSCPN_TYPE, result.getCredentialTypeCd()); // virtual_subscpn_type_cd
        assertNull("OIF_265는 partyName 미제공",    result.getPartyName());
        assertEquals(VALID_USERNAME,     result.getLoginId());           // username (kt_userid)
        assertEquals(VALID_PHONE_NUMBER, result.getMobileNo());          // phone_number
    }

    // ── TC6: virtual_subscpn_type_cd "01" (KT ID 계정) ─────────
    @Test
    public void tc6_정상_KT_ID_계정_virtual_subscpn_type_cd_01() throws Exception {
        // given: virtual_subscpn_type_cd = "01" (KT ID 보유, G0001/G0002 후보)
        Oif265Response mockResp = buildSuccessResp265(
                VALID_TOKEN_ID, "ktuser01", "01", "01099990001", VALID_TOKEN_EXPIRY);

        when(shubService.callFn265(VALID_ENC_TOKEN)).thenReturn(mockResp);

        // when
        IamUiTokenInfo result = iamUiTokenService.validateAndGetCredentialId(VALID_ENC_TOKEN);

        // then
        assertEquals("01", result.getCredentialTypeCd());
        assertEquals("ktuser01", result.getLoginId());
    }

    // ── TC7: virtual_subscpn_type_cd "09" (회선 ID 계정) ────────
    @Test
    public void tc7_정상_회선ID_계정_virtual_subscpn_type_cd_09() throws Exception {
        // given: virtual_subscpn_type_cd = "09" (회선 ID, G0002/G0003 후보)
        Oif265Response mockResp = buildSuccessResp265(
                "TOKENID_LINE99", null, "09", "01033334444", VALID_TOKEN_EXPIRY);

        when(shubService.callFn265(VALID_ENC_TOKEN)).thenReturn(mockResp);

        // when
        IamUiTokenInfo result = iamUiTokenService.validateAndGetCredentialId(VALID_ENC_TOKEN);

        // then
        assertEquals("09", result.getCredentialTypeCd());
        assertEquals("TOKENID_LINE99", result.getTokenId());
        assertNull("회선ID 계정은 username null일 수 있음", result.getLoginId());
    }

    // ── 헬퍼: 성공 Oif265Response Mock 생성 ─────────────────────
    private Oif265Response buildSuccessResp265(
            String tokenId, String username, String virtualSubscpnTypeCd,
            String phoneNumber, String tokenExpiry) {

        Oif265Response resp = mock(Oif265Response.class);
        when(resp.isSuccess()).thenReturn(true);
        when(resp.getReturncode()).thenReturn("1");
        when(resp.getTokenId()).thenReturn(tokenId);
        when(resp.getUsername()).thenReturn(username);
        when(resp.getVirtualSubscpnTypeCd()).thenReturn(virtualSubscpnTypeCd);
        when(resp.getPhoneNumber()).thenReturn(phoneNumber);
        when(resp.getTokenExpiry()).thenReturn(tokenExpiry);
        return resp;
    }
}
