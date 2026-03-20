package com.kt.yapp.web;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kt.yapp.client.IamUiApiClient;
import com.kt.yapp.domain.ContractInfo;
import com.kt.yapp.domain.IamUiTokenInfo;
import com.kt.yapp.domain.SessionContractInfo;
import com.kt.yapp.domain.UserInfo;
import com.kt.yapp.domain.req.IamUiLoginReq;
import com.kt.yapp.domain.resp.LoginAcctResp;
import com.kt.yapp.domain.resp.ResultInfo;
import com.kt.yapp.service.CommonService;
import com.kt.yapp.service.IamUiTokenService;
import com.kt.yapp.service.ShubService;
import com.kt.yapp.service.UserService;
import com.kt.yapp.soap.response.SoapResponse101;
import com.kt.yapp.soap.response.SoapResponse140;
import com.kt.yapp.util.KeyFixUtil;
import com.kt.yapp.util.SessionKeeper;
import com.kt.yapp.util.YappCvtUtil;
import com.kt.yapp.util.YappUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * IAMUI 통합 로그인 컨트롤러 (BE-LOGIN-001)
 *
 * <pre>
 * UserController 에서 IAMUI 관련 API 를 분리한 별도 컨트롤러.
 *
 * ── 제공 API ─────────────────────────────────────────────────────
 *   POST /na/user/login/iamui   checkLoginIamui()   BE-LOGIN-001
 * ─────────────────────────────────────────────────────────────────
 *
 * ── BE-LOGIN-001 처리 흐름 ────────────────────────────────────────
 * ① @RequestBody IamUiLoginReq { encTokenId, userId } 수신
 * ② iamUiTokenService.validateAndGetCredentialId(encTokenId)
 *    → ShubService.callFn265(encTokenId)
 *    → OIF_265 응답에서 tokenId(=token_id) 획득
 * ③ shubService.callFn140(userId, tokenId) → 계약목록 조회
 * ④ kosJoinLimit → KOS 가입 제한 체크
 * ⑤ cntrList.size() 분기 (회선 사용자 / 비회선 KT ID 사용자)
 *    ·회선 있음 (cntrList.size > 0) : 계약목록 반환, 세션 저장
 *    ·회선 없음 (cntrList.size == 0): memStatus=G0003, YSID 발급
 * ⑥ iamUiApiClient.loginHistory(encTokenId) – 비차단(try-catch)
 * ⑦ aesEncCntrInfoListField → 응답 필드 AES 암호화 후 반환
 *
 * ── memStatus 결정 방식 (AS-IS 동일) ─────────────────────────────
 *   G0001 : KT 회선 있음 + KT ID 가입 완료
 *   G0002 : KT 회선 있음 + KT ID 없거나 미연동
 *   G0003 : KT 회선 없음 (cntrList.size == 0)
 *
 * ── 회선 2개 이상일 때 세션 저장 (→ /na/user/oneline 정상 동작 조건)
 *   ① KEY_CNTR_LIST 저장 (selectOneLine에서 참조)
 *   ② SessionKeeper.setSessionId(req, "") – YSID 미발급 상태
 *   ③ getSdata().setUserId(userId), setCredentialId(tokenId)
 *
 * ── AS-IS 대비 제거된 로직 ────────────────────────────────────────
 *   ✂ RSA 키 관리 (getLoginKeySeq / updateLoginKey)
 *   ✂ RSA 비밀번호 복호화 (RsaCipherUtil.decryptRSA)
 *   ✂ ID/PW SHUB 인증 (callFn200)
 *   ✂ SMS 인증번호 검증 블록
 *   ✂ AuthFail 카운트 로직
 *   ✂ snsId/snsType/snsToken 직접 처리
 * </pre>
 *
 * @author YBOX IAMUI 전환 개발팀
 * @version 1.0 (2026-03-05, BE-LOGIN-001)
 *
 * Modification Information
 * Mod Date        Modifier        Description
 * ========================================
 * 2026-03-05      YBOX Dev        최초 작성 (UserController에서 분리)
 */
@RestController
@Api(description = "IAMUI 통합 로그인 컨트롤러")
public class IamUiLoginController {

	private static final Logger logger = LoggerFactory.getLogger(IamUiLoginController.class);

	/** YSID 필드 구분자 (UserController.SP 와 동일) */
	private static final String SP = "||";

	@Autowired
	private CommonService cmnService;

	@Autowired
	private ShubService shubService;

	@Autowired
	private UserService userService;

	@Autowired
	private IamUiTokenService iamUiTokenService;

	@Autowired
	private IamUiApiClient iamUiApiClient;

	@Autowired
	private KeyFixUtil keyFixUtil;

	// ──────────────────────────────────────────────────────────────────────────
	// BE-LOGIN-001 : IAMUI 통합 로그인 API
	// ──────────────────────────────────────────────────────────────────────────

	/**
	 * IAMUI 통합 로그인 (BE-LOGIN-001).
	 *
	 * <pre>
	 * POST /na/user/login/iamui
	 *
	 * IAMUI WebView 인증 완료 후 앱이 브릿지(afterLoginReq)를 통해 수신한
	 * encTokenId를 백엔드에 전달하면, 아래 순서로 처리한다.
	 * </pre>
	 *
	 * @param loginReq { encTokenId: IAMUI 암호화 토큰, userId: KT 아이디 }
	 * @param req      HttpServletRequest
	 * @return LoginAcctResp (cntrInfoList, credentialId)
	 * @throws Exception SHUB/IAMUI 연동 오류
	 */
	@RequestMapping(value = "/na/user/login/iamui", method = RequestMethod.POST)
	@ApiOperation(value = "IAMUI 통합 로그인 (BE-LOGIN-001)",
	              notes = "IAMUI WebView 인증 완료 후 encTokenId로 계약목록 반환")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "osTp",     value = "단말 OS 유형(G0001: Android, G0002: iOS)",
			                  dataType = "string", paramType = "header"),
			@ApiImplicitParam(name = "appVrsn",  value = "앱 버전",
			                  dataType = "string", paramType = "header"),
			@ApiImplicitParam(name = "mobileCd", value = "모바일 코드",
			                  dataType = "string", paramType = "header")
	})
	public ResultInfo<LoginAcctResp> checkLoginIamui(
			@RequestBody IamUiLoginReq loginReq,
			HttpServletRequest req) throws Exception {

		final String encTokenId = loginReq.getEncTokenId();
		final String userId     = loginReq.getUserId();

		logger.info("========================================================");
		logger.info("[BE-LOGIN-001] /na/user/login/iamui – START");
		logger.info("[BE-LOGIN-001] userId     : {}", userId);
		logger.info("[BE-LOGIN-001] encTokenId : (masked)");
		logger.info("========================================================");

		ResultInfo<LoginAcctResp> resultInfo = new ResultInfo<>();
		LoginAcctResp respInfo = new LoginAcctResp();
		resultInfo.setResultData(respInfo);

		// ── ① IamUiTokenService: OIF_265 호출 → tokenId 획득 ─────────────
		// validateAndGetCredentialId 내부에서 encTokenId 검증 + callFn265 수행
		// 실패 시 YappException("CHECK_MSG" or "SHUB_MSG") 자동 발생
		IamUiTokenInfo tokenInfo = iamUiTokenService.validateAndGetCredentialId(encTokenId);
		String tokenId = tokenInfo.getTokenId();   // OIF_265 response.token_id

		logger.info("[BE-LOGIN-001] OIF_265 tokenId 획득 완료");

		// ── ② callFn140: 계약목록 조회 ────────────────────────────────────
		// AS-IS accntusrforpwdbykey / newsimple 에서 credentialId 대신 tokenId 사용
		SoapResponse140 resp = shubService.callFn140(userId, tokenId);
		List<ContractInfo> cntrList = resp.getCntrInfoList();

		// ── ③ KOS 가입 제한 체크 ──────────────────────────────────────────
		cntrList = cmnService.kosJoinLimit(cntrList);

		logger.info("[BE-LOGIN-001] cntrList.size: {}", cntrList.size());

		// ── ④ 헤더 정보 수집 ──────────────────────────────────────────────
		String appVrsn  = req.getHeader("appVrsn");
		String osTp     = req.getHeader("osTp");
		String mobileCd = req.getHeader("mobileCd");

		// ── ⑤ 회선 유무에 따른 분기 처리 ─────────────────────────────────
		if (cntrList.size() > 0) {

			// ── [회선 있음] G0001 or G0002 후보 ──────────────────────────
			logger.info("[BE-LOGIN-001] KT 회선 있음 – cntrList={}", cntrList.size());

			respInfo.setCntrInfoList(cntrList);
			respInfo.setCredentialId(tokenId);   // oneline에서 callFn140 재호출용

			// 세션 저장 (회선 2개 이상 시 /na/user/oneline 에서 참조)
			SessionKeeper.addToReq(req, SessionKeeper.KEY_CNTR_LIST,
					YappCvtUtil.cvtToSessionCntrInfo(cntrList));
			SessionKeeper.setSessionId(req, "");  // YSID 미발급 상태

			if (SessionKeeper.getSdata(req) != null) {
				SessionKeeper.getSdata(req).setUserId(userId);
				SessionKeeper.getSdata(req).setCredentialId(tokenId);
				SessionKeeper.getSdata(req).setName(cntrList.get(0).getUserNm());
			}

		} else {

			// ── [회선 없음] G0003 (KT ID만 보유, YBOX 미가입 or KT ID 전용) ─
			logger.info("[BE-LOGIN-001] KT 회선 없음 – G0003 처리");

			respInfo.setCntrInfoList(cntrList);
			respInfo.setCredentialId(tokenId);

			SessionKeeper.addToReq(req, SessionKeeper.KEY_CNTR_LIST,
					YappCvtUtil.cvtToSessionCntrInfo(cntrList));
			SessionKeeper.setSessionId(req, "");

			if (SessionKeeper.getSdata(req) != null) {
				SessionKeeper.getSdata(req).setUserId(userId);
				SessionKeeper.getSdata(req).setCredentialId(tokenId);
			}

			ContractInfo cntrInfo = new ContractInfo();
			cntrList.add(cntrInfo);

			// 사용자 정보 조회
			UserInfo userKtInfo = userService.getYappUserKtInfo(userId);
			boolean existUserKt = (userKtInfo != null);
			String userIdTmp    = userId;
			String joinStatus   = "";
			String memStatus;
			String dupId = UUID.randomUUID().toString();

			if (existUserKt) {
				// 기존 KT ID 사용자
				if (userIdTmp.length() > 20) {
					logger.warn("[BE-LOGIN-001][USER ID] {} → 20자리 초과, 빈칸으로 저장", userIdTmp);
					userIdTmp = "";
				}
				userService.updateDupIdInfoKt(dupId, osTp, userId, appVrsn);

				// 정회원이 KT 계약 해지 시 ktYn=N 업데이트
				if (!YappUtil.isEmpty(userKtInfo.getKtYn())
						&& YappUtil.isEq(userKtInfo.getKtYn(), "Y")) {
					UserInfo userInfoKtYn = new UserInfo();
					userInfoKtYn.setUserId(userKtInfo.getUserId());
					userInfoKtYn.setKtYn("N");
					userService.updateUserKtSettingInfo(userInfoKtYn);
				}

			} else {
				// 신규 KT ID 사용자 – callFn101로 기본 정보 조회
				UserInfo sleepUserInfo = null;
				try {
					sleepUserInfo = userService.getSleepUserKtInfo(userId);
				} catch (Exception e) {
					logger.info("[BE-LOGIN-001] 탈퇴회원 고객 (sleepUser 없음)");
				}

				SoapResponse101 resp101 = shubService.callFn101(tokenId);

				userKtInfo = new UserInfo();
				userKtInfo.setJoinStatus("G0002");
				userKtInfo.setUserId(userId);
				userKtInfo.setUserNm(resp101.getEncUserNm());
				userKtInfo.setGender(resp101.getGender() != null ? resp101.getGender().trim() : "");
				userKtInfo.setEmail(resp101.getEmail());
				userKtInfo.setBirthDay(resp101.getBirthDate());
				userKtInfo.setSleepUserYn("N");

				if (YappUtil.isNotEmpty(sleepUserInfo)) {
					userKtInfo.setSleepUserYn(sleepUserInfo.getSleepUserYn());
					userKtInfo.setSleepRegDt(sleepUserInfo.getSleepRegDt());
				}
			}

			// G0003: KT 회선 없음 → YSID 즉시 발급
			memStatus = "G0003";

			String tmpSessionKey = joinYsid("ktid_cntr", "ktid_mobile", userId,
			                                existUserKt, dupId, memStatus);
			String encSessionKey = keyFixUtil.encode(tmpSessionKey);

			cntrInfo.setUserNm(YappUtil.blindNameToName(userKtInfo.getUserNm(), 1));
			cntrInfo.setBirthDate(userKtInfo.getBirthDay());
			userKtInfo.setMemStatus(memStatus);
			userKtInfo.setActionCode("G0001");
			userKtInfo.setYsid(encSessionKey);
			cntrList.get(0).setUserInfo(userKtInfo);

			if (userKtInfo.getJoinStatus() != null) {
				joinStatus = userKtInfo.getJoinStatus();
			}

			// 사용자 정보 업데이트
			userService.updateUserPpcdAndMobileCd(false, existUserKt, null, userId,
					null, mobileCd, memStatus, null);

			// 세션에 계약정보, 사용자 정보 저장
			SessionKeeper.addToReq(req, SessionKeeper.KEY_SESSION_ID, encSessionKey);
			SessionKeeper.addToReq(req, SessionKeeper.KEY_DUP_ID, dupId);
			SessionKeeper.addToReq(req, SessionKeeper.KEY_CNTR_INFO,
					YappCvtUtil.cvt(cntrList.get(0), new SessionContractInfo()));

			if (SessionKeeper.getSdata(req) != null) {
				SessionKeeper.getSdata(req).setMobileNo("ktid_mobile");
				SessionKeeper.getSdata(req).setExistUser(existUserKt);
				SessionKeeper.getSdata(req).setMemStatus(memStatus);
				SessionKeeper.getSdata(req).setName(YappUtil.blindNameToName(userKtInfo.getUserNm(), 1));
			}

			// G0001 기존 가입자: 계약 목록 세션에서 제거
			if (existUserKt && "G0001".equals(joinStatus)) {
				req.getSession().removeAttribute(SessionKeeper.KEY_CNTR_LIST);
			}
		}

		// ── ⑥ userId 최신화 (계약번호에 userId 연결) ──────────────────────
		if (YappUtil.isNotEmpty(cntrList) && YappUtil.ifNull(userId, "").length() <= 20) {
			try {
				userService.updateUserModDtInfo(cntrList.get(0).getCntrNo(), userId);
			} catch (Exception e) {
				logger.error("[BE-LOGIN-001] updateUserModDtInfo 오류 (무시): {}", e.getMessage());
			}
		}

		// ── ⑦ 응답 필드 AES 암호화 (모바일번호/생년월일) ──────────────────
		cntrList = cmnService.aesEncCntrInfoListField(cntrList, req);

		// ── ⑧ IAMUI 로그인 이력 기록 (비차단) ─────────────────────────────
		// 실패해도 YBOX 세션 정상 처리 (IamUiApiClient 내부에서 try-catch 처리)
		iamUiApiClient.loginHistory(encTokenId);

		logger.info("[BE-LOGIN-001] /na/user/login/iamui – 완료");

		return resultInfo;
	}

	// ──────────────────────────────────────────────────────────────────────────
	// Private helpers (UserController.joinYsid 와 동일 로직)
	// ──────────────────────────────────────────────────────────────────────────

	/**
	 * YSID 값 조립 후 리턴.
	 * <p>UserController#joinYsid 와 동일한 포맷을 사용한다.</p>
	 *
	 * @param cntrNo     계약번호
	 * @param mobileNo   모바일 번호
	 * @param userId     사용자 ID
	 * @param existUser  기존 사용자 여부
	 * @param dupId      중복 ID (UUID)
	 * @param memStatus  회원 상태 코드
	 * @return 조립된 YSID 원문
	 */
	private String joinYsid(String cntrNo, String mobileNo, String userId,
	                         boolean existUser, String dupId, String memStatus) {
		logger.debug("====================== ysid create ========================");
		logger.debug("joinYsid -> cntrNo    : {}", cntrNo);
		logger.debug("joinYsid -> mobileNo  : {}", mobileNo);
		logger.debug("joinYsid -> userId    : {}", userId);
		logger.debug("joinYsid -> existUser : {}", existUser);
		logger.debug("joinYsid -> dupId     : {}", dupId);
		logger.debug("joinYsid -> getCurDate: {}", YappUtil.getCurDate());
		logger.debug("joinYsid -> memStatus : {}", memStatus);
		logger.debug("====================== ysid create ========================");

		return new StringBuilder()
				.append(cntrNo).append(SP)
				.append(mobileNo).append(SP)
				.append(userId).append(SP)
				.append(existUser).append(SP)
				.append(dupId).append(SP)
				.append(YappUtil.getCurDate()).append(SP)
				.append(memStatus).toString();
	}
}
