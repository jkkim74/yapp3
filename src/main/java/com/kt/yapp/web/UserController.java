package com.kt.yapp.web;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kt.yapp.domain.AdultAgree;
import com.kt.yapp.domain.ApiError;
import com.kt.yapp.domain.AppInfo;
import com.kt.yapp.domain.AuthFail;
import com.kt.yapp.domain.AuthLogin;
import com.kt.yapp.domain.ContractInfo;
import com.kt.yapp.domain.CustAgreeInfoRetvListDtoDetail;
import com.kt.yapp.domain.GiftData;
import com.kt.yapp.domain.GrpCode;
import com.kt.yapp.domain.IdsItem;
import com.kt.yapp.domain.Invitation;
import com.kt.yapp.domain.JoinInfo;
import com.kt.yapp.domain.PreferenceInfo;
import com.kt.yapp.domain.ReviewInfo;
import com.kt.yapp.domain.RsaKeyInfo;
import com.kt.yapp.domain.SendSms;
import com.kt.yapp.domain.SessionContractInfo;
import com.kt.yapp.domain.SmsAccessInfo;
import com.kt.yapp.domain.SmsContents;
import com.kt.yapp.domain.SnsInfo;
import com.kt.yapp.domain.SvcOut;
import com.kt.yapp.domain.TermsAgree;
import com.kt.yapp.domain.UserInfo;
import com.kt.yapp.domain.UserPass;
import com.kt.yapp.domain.req.UserInfoReq;
import com.kt.yapp.domain.req.UserInfoSettingReq;
import com.kt.yapp.domain.req.UserPreferenceReq;
import com.kt.yapp.domain.resp.JoinInfoResp;
import com.kt.yapp.domain.resp.LoginAcctResp;
import com.kt.yapp.domain.resp.LoginMobileResp;
import com.kt.yapp.domain.resp.PrfMainViewResp;
import com.kt.yapp.domain.resp.ResultInfo;
import com.kt.yapp.em.EnumJoinStatus;
import com.kt.yapp.em.EnumRsltCd;
import com.kt.yapp.em.EnumYn;
import com.kt.yapp.exception.YappAuthException;
import com.kt.yapp.exception.YappException;
import com.kt.yapp.redis.RedisComponent;
import com.kt.yapp.service.CmsService;
import com.kt.yapp.service.CommonService;
import com.kt.yapp.service.GiftService;
import com.kt.yapp.service.HistService;
import com.kt.yapp.service.ShubService;
import com.kt.yapp.service.UserKtService;
import com.kt.yapp.service.UserService;
import com.kt.yapp.soap.response.SoapResponse003;
import com.kt.yapp.soap.response.SoapResponse101;
import com.kt.yapp.soap.response.SoapResponse139;
import com.kt.yapp.soap.response.SoapResponse140;
import com.kt.yapp.soap.response.SoapResponse184;
import com.kt.yapp.soap.response.SoapResponse188;
import com.kt.yapp.soap.response.SoapResponse200;
import com.kt.yapp.util.AppEncryptUtils;
import com.kt.yapp.util.KeyFixUtil;
import com.kt.yapp.util.RsaCipherUtil;
import com.kt.yapp.util.SessionKeeper;
import com.kt.yapp.util.YappCvtUtil;
import com.kt.yapp.util.YappUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import tm.automail.sendapi.ThunderAutoMail;
import tm.automail.sendapi.ThunderAutoMailSender;

/**
 * User 관련 정보 처리 컨트롤러
 *
 * @author
 * @since
 * @version 1.0
 *
 * Modification Information
 * Mod Date		Modifier		Description
 * ========================================
 * 2019. 3. 23.	seungman.yu 	사용자 리뷰
 * Copyright (c) 2018 KTDS, Inc. All Rights Reserved
 */
@RestController
@Api(description="사용자 정보 처리 컨트롤러")
public class UserController
{
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	private static final String SP = "||";

	@Autowired
	private CommonService cmnService;
	@Autowired
	private CmsService cmsService;
	@Autowired
	private UserService userService;
	@Autowired
	private ShubService shubService;
	@Autowired
	private GiftService giftService;
	@Autowired
	private HistService histService;
	@Autowired
	private RedisComponent redisComponent;
	@Autowired
	private KeyFixUtil keyFixUtil;
	@Autowired
	private AppEncryptUtils appEncryptUtils;

	@Autowired
	private UserKtService userKtService;

	private static final String AUTHREQBYKEY = "/na/user/login/authreqbykey";
	private static final String CBC = "114";


	@RequestMapping(value = "/user/svcout", method = RequestMethod.POST)
	@ApiOperation(value="서비스 탈퇴")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<String> serviceOut(@RequestBody SvcOut svcOut, HttpServletRequest req) throws Exception
	{

		String cntrNo = SessionKeeper.getCntrNo(req);
		String memStatus = "";
		String userId = null;

		if(SessionKeeper.getSdata(req) != null){
			memStatus = SessionKeeper.getSdata(req).getMemStatus();
			userId = SessionKeeper.getSdata(req).getUserId();
		}

		logger.info("==============================================================");
		logger.info("/user/svcout -> memStatus : "+memStatus);
		logger.info("/user/svcout -> cntrNo : "+cntrNo);
		logger.info("/user/svcout -> userId : "+userId);
		logger.info("==============================================================");

		svcOut.setCntrNo(cntrNo);
		svcOut.setUserId(userId);

		if(YappUtil.isEq(memStatus, "G0001")){

			logger.info("/user/svcout - 아이디 + 계약번호");
			if(userService.getCntrCnt(userId) == 1){
				logger.info("/user/svcout - 추가 아이디 삭제");
				// yapp 탈퇴 처리 (KTID)
				userService.serviceOutAll(svcOut);
			}else{
				//SNS정보 삭제
				userService.deleteAllSnsInfo(SessionKeeper.getCntrNo(req));

				// yapp 탈퇴 처리 (계약번호)
				userService.serviceOut(svcOut);
			}

		}else if(YappUtil.isEq(memStatus, "G0002")){

			logger.info("/user/svcout - 계약번호 유저");
			//SNS정보 삭제
			userService.deleteAllSnsInfo(SessionKeeper.getCntrNo(req));

			// yapp 탈퇴 처리 (계약번호)
			userService.serviceOut(svcOut);

		}else if(YappUtil.isEq(memStatus, "G0003")){

			logger.info("/user/svcout- 아이디 유저");
			// yapp 탈퇴 처리 (KTID)
			userService.serviceOutKt(svcOut);

		}else{

			throw new YappAuthException("410","로그인 정보가 없습니다.");
		}

		// 로그아웃 처리
		logout("", req);

		return new ResultInfo<>();
	}

	@RequestMapping(value="/user/logout", method = RequestMethod.POST)
	@ApiOperation(value="로그아웃 처리")
	@ApiImplicitParams({
			@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")
	})
	public ResultInfo<String> logout (String snsType, HttpServletRequest req) throws Exception
	{


		String cntrNo = SessionKeeper.getCntrNo(req);
		String memStatus = "";
		String userId = null;

		if(SessionKeeper.getSdata(req) != null){
			memStatus = SessionKeeper.getSdata(req).getMemStatus();
			userId = SessionKeeper.getSdata(req).getUserId();
		}

		logger.info("==============================================================");
		logger.info("/user/logout -> memStatus : "+memStatus);
		logger.info("/user/logout -> cntrNo : "+cntrNo);
		logger.info("/user/logout -> userId : "+userId);
		logger.info("==============================================================");

		//SNS 로그인 연동정보 삭제
		if(YappUtil.isNotEmpty(cntrNo)&&YappUtil.isNotEmpty(snsType)){
			userService.deleteSnsInfo(snsType, cntrNo);
		}

		if(YappUtil.isEq(memStatus, "G0001")){

			cmnService.updateDeviceTokenAll("", cntrNo, userId, "", "","");

		}else if(YappUtil.isEq(memStatus, "G0002")){

			cmnService.updateDeviceToken("", cntrNo, "", "");

		}else if(YappUtil.isEq(memStatus, "G0003")){

			cmnService.updateDeviceTokenKt("", userId, "", "","");

		}else{

			throw new YappAuthException("410","로그인 정보가 없습니다.");
		}

		SessionKeeper.logout(req);
		req.getSession().invalidate();

		return new ResultInfo<>();
	}

	// 2018.11.14 보안진단 수정사항
	// credentialId 삭제요청
	@RequestMapping(value = "/user/contract/listUp", method = {RequestMethod.GET, RequestMethod.POST})
	@ApiOperation(value="계약(보유회선) 목록 조회")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<ContractInfo> getCntrList(String userId, HttpServletRequest req) throws Exception
	{
		ResultInfo<ContractInfo> resultInfo = new ResultInfo<>();
		String loginMobileNo = null;
		String credentialId = null;
		String appVrsn = req.getHeader("appVrsn");
		String osTp = req.getHeader("osTp");

		if(SessionKeeper.getSdata(req) != null){
			loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
			credentialId = SessionKeeper.getSdata(req).getCredentialId();

			logger.info("==============================================================");
			logger.info("/user/contract/listUp -> loginMobileNo : "+loginMobileNo);
			logger.info("/user/contract/listUp -> credentialId : "+credentialId);
			logger.info("==============================================================");
		}

		if ( YappUtil.isEmpty(credentialId) ){
			credentialId = shubService.callFn110(loginMobileNo).getCredentialId();

			if(SessionKeeper.getSdata(req) != null){
				SessionKeeper.getSdata(req).setCredentialId(credentialId);
			}
		}
		// 계약 목록 조회

		//userId 버전에 따른 복호화
		userId  = appEncryptUtils.aesDec128(userId, osTp+appVrsn);


		SoapResponse140 resp = shubService.callFn140(userId, credentialId);
		List<ContractInfo> cntrList = resp.getCntrInfoList();

		//220610 kos가입제한 통과 여부 체크 이용불가회선은 제거 		
		cntrList = cmnService.kosJoinLimit(cntrList);

		resultInfo.setResultInfoList(cntrList);

		// 세션에 계약 목록을 저장한다.
		SessionKeeper.addToReq(req, SessionKeeper.KEY_CNTR_LIST, YappCvtUtil.cvtToSessionCntrInfo(resp.getCntrInfoList()));

		//220627 암호화(모바일번호/생년월일)
		cntrList = cmnService.aesEncCntrInfoListField(cntrList, req);

		logger.info("/user/contract/listUp resultInfo : "+resultInfo);

		return resultInfo;
	}

	@RequestMapping(value = "/na/user/login/newsimple", method = RequestMethod.POST)
	@ApiOperation(value="로그인 인증 체크 (간편로그인)", notes="인증 성공 시 계약목록 반환")
	@ApiImplicitParams({
			@ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"),
			@ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header"),
			@ApiImplicitParam(name="mobileCd", value="모바일 코드", dataType="string", paramType="header")})
	public ResultInfo<LoginAcctResp> checkLoginNewSimple(String userId, String uid, String tokenId, HttpServletRequest req) throws Exception
	{
		ResultInfo<LoginAcctResp> resultInfo = new ResultInfo<>();
		LoginAcctResp respInfo = new LoginAcctResp();
		resultInfo.setResultData(respInfo);

		String deTid = shubService.callFn198(tokenId, userId, uid);

		SoapResponse188 re188 = shubService.callFn188(deTid);

		if ( YappUtil.isNotEq(re188.getRtnCd(), EnumYn.C_1.getValue()) ){
			throw new YappException("SHUB_MSG", re188.getRtnCd(), cmnService.getMsg("ERR_SIMPLE_LOGIN"), "[OIF_188] " +re188.getRtnDesc(), re188.getTransId());
		}

		String cid = re188.getCredentialId();

		// 계약 목록 조회
		SoapResponse140 resp = shubService.callFn140(userId, cid);
		List<ContractInfo> cntrList = resp.getCntrInfoList();

		//220606 kos가입제한 통과 여부 체크 / 통과못하면 준회원 가입
		cntrList = cmnService.kosJoinLimit(cntrList);

		logger.info("newsimple cntrNo cntrList : "+cntrList);

		// 계약번호 유무에 따라 회선 사용자와 비회선 사용자 분기처리
		if(cntrList.size()>0){

			//respInfo.setCntrInfoList(YappCvtUtil.cvtCntrInfoToRespList(resp.getCntrInfoList()));
			respInfo.setCntrInfoList(cntrList);
			respInfo.setCredentialId(cid);

			// 세션 ID 생성(임의적인 키)
			String sessionId = UUID.randomUUID().toString();
			//respInfo.setYsid(sessionId);

			// 세션에 저장
			//SessionKeeper.addToReq(req, SessionKeeper.KEY_SESSION_ID, sessionId);
			SessionKeeper.addToReq(req, SessionKeeper.KEY_CNTR_LIST, YappCvtUtil.cvtToSessionCntrInfo(cntrList));
			SessionKeeper.setSessionId(req,"");
			if(SessionKeeper.getSdata(req) != null){
				SessionKeeper.getSdata(req).setUserId(userId);
				SessionKeeper.getSdata(req).setCredentialId(cid);
				SessionKeeper.getSdata(req).setName(cntrList.get(0).getUserNm());
			}

			// 회선수가 1인 경우 회선선택 한다.
			/*if ( cntrList.size() == 1 )
			{
				ResultInfo<UserInfo> resultUserInfo = selectLineOne(cntrList.get(0).getCntrNo(), req);
				cntrList.get(0).setUserInfo(resultUserInfo.getResultData());
			}*/

		}else{

			//KTID 사용자 정보 세팅
			//respInfo.setCntrInfoList(YappCvtUtil.cvtCntrInfoToRespList(resp.getCntrInfoList()));
			respInfo.setCntrInfoList(cntrList);
			respInfo.setCredentialId(cid);

			SessionKeeper.addToReq(req, SessionKeeper.KEY_CNTR_LIST, YappCvtUtil.cvtToSessionCntrInfo(cntrList));
			SessionKeeper.setSessionId(req,"");

			if(SessionKeeper.getSdata(req) != null){
				SessionKeeper.getSdata(req).setUserId(userId);
				SessionKeeper.getSdata(req).setCredentialId(cid);
			}

			ContractInfo cntrInfo = new ContractInfo();
			cntrList.add(cntrInfo);

			// User Info를 세팅한다
			String appVrsn = req.getHeader("appVrsn");
			String osTp = req.getHeader("osTp");
			String mobileCd = req.getHeader("mobileCd");

			// 사용자 정보 조회
			UserInfo userKtInfo = userService.getYappUserKtInfo(userId);
			boolean existUserKt = false;
			//String userId = "";
			String userIdTmp = userId;
			String joinStatus = "";
			String memStatus = "";
			String dupId = UUID.randomUUID().toString();

			if (userKtInfo == null){
				existUserKt = false;
			} else {
				existUserKt = true;
			}

			if (existUserKt){
				if(userIdTmp.length()>20){
					logger.warn("[USER ID] : " + userIdTmp + " ----> 20자리이상 사 유로 빈칸으로 저장.");
					userIdTmp = "";
				}
				userService.updateDupIdInfoKt(dupId, osTp, userId, appVrsn);

				//220606 정회원이 kt계약을 해지했을때 준회원으로 ktyn n으로 업데이트
				if(!YappUtil.isEmpty(userKtInfo.getKtYn())){
					if(YappUtil.isEq(userKtInfo.getKtYn(), "Y")){
						UserInfo userInfoKtYn = new UserInfo();
						userInfoKtYn.setUserId(userKtInfo.getUserId());
						userInfoKtYn.setKtYn("N");
						userService.updateUserKtSettingInfo(userInfoKtYn);
					}
				}

			}else{

				UserInfo sleepUserInfo = null;
				try {
					sleepUserInfo = userService.getSleepUserKtInfo(userId);
				} catch (RuntimeException e) {
					logger.info("탈퇴회원 고객");
				}  catch (Exception e) {
					logger.info("탈퇴회원 고객");
				}

				SoapResponse101 resp101 = shubService.callFn101(cid);

				userKtInfo = new UserInfo();
				userKtInfo.setJoinStatus("G0002");
				userKtInfo.setUserId(userId);
				userKtInfo.setUserNm(resp101.getEncUserNm());
				userKtInfo.setGender(resp101.getGender().trim());

				userKtInfo.setEmail(resp101.getEmail());
				userKtInfo.setBirthDay(resp101.getBirthDate());

				userKtInfo.setSleepUserYn("N");

				if(YappUtil.isNotEmpty(sleepUserInfo)) {
					userKtInfo.setSleepUserYn(sleepUserInfo.getSleepUserYn());
					userKtInfo.setSleepRegDt(sleepUserInfo.getSleepRegDt());
				}
			}

			memStatus = "G0003";

			String tmpSessionKey = joinYsid("ktid_cntr", "ktid_mobile", userId, existUserKt, dupId, memStatus);
			String encSessionKey = keyFixUtil.encode(tmpSessionKey);

			//TODO 마스킹된 이름 세팅
			//cntrPlan.setUserNm(YappUtil.blindNameToName(partyName, 1));
			cntrInfo.setUserNm(YappUtil.blindNameToName(userKtInfo.getUserNm(),1));
			cntrInfo.setBirthDate(userKtInfo.getBirthDay());
			userKtInfo.setMemStatus(memStatus);
			userKtInfo.setActionCode("G0001");
			userKtInfo.setYsid(encSessionKey);
			cntrList.get(0).setUserInfo(userKtInfo);
			//resultInfo.setResultData(userInfo);

			if(userKtInfo.getJoinStatus() != null){
				joinStatus = userKtInfo.getJoinStatus();
			}

			//사용자 정보 업데이트
			userService.updateUserPpcdAndMobileCd(false, existUserKt, null, userId, null, mobileCd, memStatus, null);

			// 세션에 계약정보, 사용자 정보 저장
			SessionKeeper.addToReq(req, SessionKeeper.KEY_SESSION_ID, encSessionKey);
			SessionKeeper.addToReq(req, SessionKeeper.KEY_DUP_ID, dupId);
			SessionKeeper.addToReq(req, SessionKeeper.KEY_CNTR_INFO, YappCvtUtil.cvt(cntrList.get(0), new SessionContractInfo()));
			if(SessionKeeper.getSdata(req) != null){
				SessionKeeper.getSdata(req).setMobileNo("ktid_mobile");
				SessionKeeper.getSdata(req).setExistUser(existUserKt);
				SessionKeeper.getSdata(req).setMemStatus(memStatus);
				SessionKeeper.getSdata(req).setName(YappUtil.blindNameToName(userKtInfo.getUserNm(),1));
			}

			if (existUserKt && joinStatus.equals("G0001")){
				//사용후 계약 목록 세션에서 삭제 (USER 테이블에 살이 있는 경우만 삭제)
				req.getSession().removeAttribute(SessionKeeper.KEY_CNTR_LIST);
			}
		}

		//user id 수정
		//logger.info("cntrNo:" + cntrList.get(0).getCntrNo() +",userId:" + userId);
		if(YappUtil.isNotEmpty(cntrList) && YappUtil.ifNull(userId, "").length() <= 20) {
			try {
				userService.updateUserModDtInfo(cntrList.get(0).getCntrNo(), userId);
			} catch (RuntimeException e) {
				logger.error("error : " + e.getMessage());
			} catch (Exception e) {
				logger.error("error : " + e.getMessage());
			}
		}

		//220620 암호화(모바일번호/생년월일)
		cntrList = cmnService.aesEncCntrInfoListField(cntrList, req);

		logger.info("newsimple resultInfo : "+resultInfo);

		return resultInfo;
	}


	@ApiOperation(value="로그인 인증 요청 (휴대폰 인증)")
	@RequestMapping(value = "/na/user/login/authreqbykey", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<Integer> reqLoginPhonebykey(@RequestBody AuthLogin authLogin, HttpServletRequest req) throws Exception
	{
		ResultInfo<Integer> resultInfo = new ResultInfo<>();

		RsaKeyInfo rsaKeyInfo = cmsService.getRsaPublicKeyInfo(authLogin.getKeySeq());
		try{
			String decoded_mobileNo = RsaCipherUtil.decryptRSA(authLogin.getAuthMobileNo(), rsaKeyInfo.getPrivateKey());
			logger.info("rsq_index : " + authLogin.getKeySeq());
			logger.info("encoded_mobileNo : " + authLogin.getAuthMobileNo());
			logger.info("decoded_mobileNo : " + decoded_mobileNo);

			// 인증 에러 정보 조회
			AuthFail authFail = cmnService.getAuthFailCnt(makeAuthFail(decoded_mobileNo, "N"));

			// 인증 에러 횟수 체크 처리
			String authFailChkStr = checkAuthFail(authFail, authLogin);
			if ( authFailChkStr != null ) {
				YappUtil.setErrResultInfo(resultInfo, authFailChkStr);
				return resultInfo;
			}

			// SMS 보내기
			SmsContents paramObj = new SmsContents();
			paramObj.setRcvMobileNo(decoded_mobileNo);
			paramObj.setAccessUrl(AUTHREQBYKEY);
			paramObj.setCallbackCtn(CBC);

			// 랜덤 번호 생성 (1000 - 9999)
			//String randomNum = String.valueOf((int)(Math.random() * 900000) + 100000);

			SecureRandom tmpRandom = new SecureRandom();
			tmpRandom.setSeed(new java.util.Date().getTime());
			String randomNum = String.valueOf((int)(tmpRandom.nextDouble() * 900000) + 100000);

			String msg = "[KT]Y박스 SMS인증번호는 ["+randomNum+"]입니다. 인증번호를 입력해주세요";
			paramObj.setMsgContents(msg);

			GrpCode grpCode = cmsService.getCodeNm("SMS_LIMIT_YN", "S0001");
			String accessIpAddr = req.getRemoteAddr();
			if("Y".equals(grpCode.getCodeNm())){
				SmsAccessInfo smsAccessInfo = cmnService.getSmsSendInfoCount(accessIpAddr);
				if(smsAccessInfo != null){
					if(smsAccessInfo.getCount() > 4){
						throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_SMS_SEND"));
					}
				}
			}

			// SMS 호출
			shubService.callFn2118(paramObj);

			SmsAccessInfo smsAccessInfo = new SmsAccessInfo();
			smsAccessInfo.setAccessIpAddr(accessIpAddr);

			cmnService.insertSmsSendInfo(smsAccessInfo);

			// SMS 호출 성공 시 발송한 "휴대폰 번호 + 랜덤번호" 를 저장해 둔다.
			SessionKeeper.addToReq(req, SessionKeeper.KEY_AUTH_NUM, (decoded_mobileNo + randomNum), 180);

			/**
			 * 2019.12.26 KCH
			 * - 성공시 메시지 변경
			 *   --> SUCCESS 에서 
			 *   --> "요청하신 번호로 인증번호를 발송하였습니다. 인증번호를 수신하지 못하셨다면 입력하신 정보가 정확한지 확인해 주십시오." 로 변경
			 */
			if("200".equals(resultInfo.getResultCd()))
				resultInfo.setResultMsg("요청하신 번호로 인증번호를 발송하였습니다. 인증번호를 수신하지 못하셨다면 입력하신 정보가 정확한지 확인해 주십시오.");

			return resultInfo;
		}catch(BadPaddingException e){
			throw new YappException("CHECK_MSG", cmnService.getMsg("EXPT_BAD_PADDING"));
		}
	}

	@ApiOperation(value="로그인 인증 체크 (휴대폰 인증)", notes="인증 실패 시 실패횟수를 반환한다.")
	@RequestMapping(value = "/na/user/login/authchkbykey", method = RequestMethod.POST)
	@ApiImplicitParams({
			@ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"),
			@ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header"),
			@ApiImplicitParam(name="mobileCd", value="모바일 코드", dataType="string", paramType="header")})
	public ResultInfo<LoginMobileResp> checkLoginPhonebykey(@RequestBody AuthLogin authLogin, HttpServletRequest req) throws Exception
	{
		ResultInfo<LoginMobileResp> resultInfo = new ResultInfo<>();
		LoginMobileResp respResult = new LoginMobileResp();

		resultInfo.setResultData(respResult);

		String appVrsn = req.getHeader("appVrsn");
		String osTp = req.getHeader("osTp");
		String mobileCd = req.getHeader("mobileCd");
		String cntrNo = "";

		RsaKeyInfo rsaKeyInfo = cmsService.getRsaPublicKeyInfo(authLogin.getKeySeq());
		try{
			String decoded_mobileNo = RsaCipherUtil.decryptRSA(authLogin.getAuthMobileNo(), rsaKeyInfo.getPrivateKey());
			logger.info("rsq_index : " + authLogin.getKeySeq());
			logger.info("encoded_mobileNo : " + authLogin.getAuthMobileNo());
			logger.info("decoded_mobileNo : " + decoded_mobileNo);

			String passYn = "N";
			UserPass userPass = userService.getYappPass(decoded_mobileNo);
			if(userPass != null){
				passYn = "Y";
			}

			// 유저 패스 기능 추가(VOC기능)
			if(passYn.equals("N")){
				// 인증 에러 정보 조회
				AuthFail authFail = cmnService.getAuthFailCnt(makeAuthFail(decoded_mobileNo, "N"));

				// 인증 에러 횟수 체크 처리
				String authFailChkStr = checkAuthFail(authFail, authLogin);
				if ( authFailChkStr != null ) {
					YappUtil.setErrResultInfo(resultInfo, authFailChkStr);
					respResult.setAuthFailCnt(authFail.getFailCnt());
					return resultInfo;
				}

				// 저장해둔 인증번호가 있는지 체크
				String savedAuthNum = YappUtil.nToStr(SessionKeeper.get(req, SessionKeeper.KEY_AUTH_NUM));
				if ( YappUtil.isEmpty(savedAuthNum) ) {
					YappUtil.setErrResultInfo(resultInfo, cmnService.getMsg("ERR_NO_AUTH_REQ"));		// 인증요청 정보가 없습니다.
					return resultInfo;
				}

				// 입력 번호와 인증 요청에서 저장해 둔 "휴대폰 번호 + 랜덤번호" 를 비교한다.
				if ( YappUtil.isEq((decoded_mobileNo + authLogin.getAuthNum()), savedAuthNum) ) {

					SessionKeeper.remove(req, SessionKeeper.KEY_AUTH_NUM);

					// 실패횟수 초기화
					cmnService.updateAuthFailCnt(makeAuthFail(decoded_mobileNo, "Y"));

					// 계약목록 저장
					SoapResponse139 resp = shubService.callFn139(decoded_mobileNo, true);

					ContractInfo cntrInfo = resp.getCntrInfo();

					cntrNo = cntrInfo.getCntrNo();

					/**
					 * 20220519 by cha
					 * 회원가입구조 변경 (아이디 존재여부 + KTID 가입여부 추가)
					 */

					String userId = "";
					String memStatus = "";

					boolean existUser = false;
					boolean existUserId = false;
					boolean existUserKt = false;

					UserInfo userInfo = cntrInfo.getUserInfo();
					UserInfo userKtInfo = null;

					SoapResponse003 resp003 = shubService.callFn003(decoded_mobileNo);

					for(IdsItem item:resp003.getIdsList()){
						logger.info("resp003 : " + item.getCredentialTypeCd());
						if(item.getCredentialTypeCd().equals("01")){
							userId = item.getUserId();
							break;
						}
					}

					existUserId = YappUtil.isNotEmpty(userId);

					if(existUserId){
						userKtInfo = userService.getYappUserKtInfo(userId);
					}

					existUser = userInfo != null;
					existUserKt = userKtInfo != null;

					if(existUser){
						if(existUserId){
							if(existUserKt){
								//모두 가입이 되어있는 회원
								userInfo.setUserNm(userKtInfo.getUserNm());
								userInfo.setEmail(userKtInfo.getEmail());
								userInfo.setGender(userKtInfo.getGender());
								userInfo.setBirthDay(userKtInfo.getBirthDay());
								userInfo.setJoinStatusKt(userKtInfo.getJoinStatus());
								userInfo.setActionCode("G0001");

								if(YappUtil.isEq(userKtInfo.getJoinStatus(), "G0001")){
									userInfo.setMemStatus("G0001");
									memStatus = "G0001";
								}else{
									userInfo.setMemStatus("G0002");
									memStatus = "G0002";
								}

							}else{

								if (YappUtil.isEq(userInfo.getJoinStatus(), EnumJoinStatus.G0002.getStatusCd())) {
									//아이디 없는 신규가입 불가 - 로그인페이지 이동
									userInfo.setMemStatus("");
									userInfo.setActionCode("G0004");

								}else if (YappUtil.isEq(userInfo.getJoinStatus(), EnumJoinStatus.G0001.getStatusCd())) {
									//KTID 인증이 필요한 회원
									//TODO KTID 미가입 사용자는 샵동의 값을 기존 약관 테이블에 넣어준다.
									//TODO 변경 -> KTID 미가입 사용자도 재동의 약관을 통해 미리 재샵 동의를 받아논다.
									//userService.updateShopTermsAgreeInfo(cntrNo);
									userInfo.setJoinStatusKt("G0001");
									userInfo.setMemStatus("G0002");
									userInfo.setActionCode("G0002");
									memStatus = "G0002";

								}
							}
						}else{
							//KTID 미존재로 KT.COM 가입페이지 유도
							if (YappUtil.isEq(userInfo.getJoinStatus(), EnumJoinStatus.G0002.getStatusCd())) {
								//아이디 없는 신규가입 불가 - 로그인페이지 이동
								userInfo.setMemStatus("");
								userInfo.setActionCode("G0004");

							}else if (YappUtil.isEq(userInfo.getJoinStatus(), EnumJoinStatus.G0001.getStatusCd())) {
								//아이디 없는 기존 회원 - 다음에 하기 가능
								userInfo.setMemStatus("G0002");
								userInfo.setActionCode("G0003");
								memStatus = "G0002";
							}
						}
					} else {

						UserInfo sleepUserInfo = null;
						try {
							sleepUserInfo = userService.getSleepUserInfo(cntrNo);
						} catch (RuntimeException e) {
							logger.info("탈퇴회원 고객");
						}  catch (Exception e) {
							logger.info("탈퇴회원 고객");
						}

						userInfo = new UserInfo();
						userInfo.setSleepUserYn("N");

						if(YappUtil.isNotEmpty(sleepUserInfo)) {
							userInfo.setSleepUserYn(sleepUserInfo.getSleepUserYn());
							userInfo.setSleepRegDt(sleepUserInfo.getSleepRegDt());
						}

						if(existUserId){
							if(existUserKt){
								//기존 아이디로만 가입한 회원이 KT회선을 추가한 경우
								userInfo.setUserNm(userKtInfo.getUserNm());
								userInfo.setEmail(userKtInfo.getEmail());
								userInfo.setGender(userKtInfo.getGender());
								userInfo.setBirthDay(userKtInfo.getBirthDay());
								userInfo.setJoinStatus("G0002");
								userInfo.setJoinStatusKt(userKtInfo.getJoinStatus());
								userInfo.setActionCode("G0001");
								userInfo.setAuthYn("N");
								userInfo.setMobileNo(cntrInfo.getMobileNo());
								userInfo.setMaskingMobileNo(YappUtil.blindMidEndMobileNo(cntrInfo.getMobileNo()));
								userInfo.setCntrNo(cntrInfo.getCntrNo());

								if(YappUtil.isEq(userKtInfo.getJoinStatus(), "G0001")){
									userInfo.setMemStatus("G0001");
									memStatus = "G0001";
								}else{
									userInfo.setMemStatus("G0002");
									memStatus = "G0002";
								}
							}else{
								//아이디 없는 신규가입 불가 - 로그인페이지 이동
								userInfo.setJoinStatus("G0002");
								userInfo.setMemStatus("");
								userInfo.setActionCode("G0004");
							}
						}else{
							//아이디 없는 신규가입 불가 - KT.COM 가입페이지 유도
							userInfo.setJoinStatus("G0002");
							userInfo.setMemStatus("");
							userInfo.setActionCode("G0004");
						}
					}

					cntrInfo.setUserInfo(userInfo);

					logger.info("==============================================================");
					logger.info("/na/user/login/authchkbykey -> getActionCode : "+userInfo.getActionCode());
					logger.info("/na/user/login/authchkbykey -> getMemStatus : "+userInfo.getMemStatus());
					logger.info("/na/user/login/authchkbykey -> getJoinStatus : "+userInfo.getJoinStatus());
					logger.info("/na/user/login/authchkbykey -> getJoinStatusKt : "+userInfo.getJoinStatusKt());
					logger.info("==============================================================");

					String dupId = UUID.randomUUID().toString();
					String tmpSessionKey = joinYsid(cntrInfo.getCntrNo(), cntrInfo.getMobileNo(), userId, existUser, dupId, memStatus);
					String encSessionKey = "";

					try {
						encSessionKey = keyFixUtil.encode(tmpSessionKey);
					} catch (BadPaddingException e) {
						// TODO: handle exception
						throw new BadPaddingException();
					} catch (Exception e) {
						// TODO: handle exception
						throw new BadPaddingException();
					}

					List<ContractInfo> cntrInfoList = new ArrayList<>();

					cntrInfoList.add(cntrInfo);

					respResult.setCntrInfo(cntrInfo);
					respResult.setYsid(encSessionKey);

					if(existUser){
						userService.updateDupIdInfo(dupId, cntrInfo.getCntrNo(), osTp, "", appVrsn);
					}

					//사용자 정보 업데이트
					userService.updateUserPpcdAndMobileCd(existUser, existUserKt, cntrInfo.getCntrNo(), userId, cntrInfo.getPpCd(), mobileCd, memStatus, userInfo.getPushRcvYn());

					// 세션에 저장
					SessionKeeper.setSessionId(req,encSessionKey);
					SessionKeeper.addToReq(req, SessionKeeper.KEY_DUP_ID, dupId);
					SessionKeeper.addToReq(req, SessionKeeper.KEY_CNTR_INFO, YappCvtUtil.cvt(cntrInfo, new SessionContractInfo()));
					SessionKeeper.addToReq(req, SessionKeeper.KEY_CNTR_LIST, YappCvtUtil.cvtToSessionCntrInfo(cntrInfoList));
					if(SessionKeeper.getSdata(req) != null){
						SessionKeeper.getSdata(req).setUserId(userId);
						SessionKeeper.getSdata(req).setMobileNo(cntrInfo.getMobileNo());
						SessionKeeper.getSdata(req).setExistUser(existUser);
						SessionKeeper.getSdata(req).setMemStatus(memStatus);
						SessionKeeper.getSdata(req).setName(cntrInfo.getUserNm());
					}

					//220620 암호화(모바일번호/생년월일)
					cntrInfoList = cmnService.aesEncCntrInfoListField(cntrInfoList, req);

					logger.info("휴대폰 로그인 pass resultInfo : "+resultInfo);

				} else {
					YappUtil.setErrResultInfo(resultInfo, EnumRsltCd.C999.getRsltCd(), cmnService.getMsg("ERR_AUTH_NUM"));		// 인증번호가 올바르지 않습니다.
					// 실패횟수를 업데이트 한다.
					cmnService.updateAuthFailCnt(makeAuthFail(decoded_mobileNo, "N"));
				}

				// 인증 에러 정보 재조회
				authFail = cmnService.getAuthFailCnt(makeAuthFail(decoded_mobileNo, "N"));
				respResult.setAuthFailCnt(authFail == null ? 0 : authFail.getFailCnt());
			}else{

				SoapResponse139 resp = shubService.callFn139(decoded_mobileNo, true);

				ContractInfo cntrInfo = resp.getCntrInfo();

				cntrNo = cntrInfo.getCntrNo();

				/**
				 * 20220519 by cha
				 * 회원가입구조 변경 (아이디 존재여부 + KTID 가입여부 추가)
				 */

				String userId = "";
				String memStatus = "";

				boolean existUser = false;
				boolean existUserId = false;
				boolean existUserKt = false;

				UserInfo userInfo = cntrInfo.getUserInfo();
				UserInfo userKtInfo = null;

				SoapResponse003 resp003 = shubService.callFn003(decoded_mobileNo);

				for(IdsItem item:resp003.getIdsList()){
					logger.info("resp003 : " + item.getCredentialTypeCd());
					if(item.getCredentialTypeCd().equals("01")){
						userId = item.getUserId();
						break;
					}
				}

				existUserId = YappUtil.isNotEmpty(userId);

				if(existUserId){
					userKtInfo = userService.getYappUserKtInfo(userId);
				}

				existUser = userInfo != null;
				existUserKt = userKtInfo != null;

				if(existUser){
					if(existUserId){
						if(existUserKt){
							//모두 가입이 되어있는 회원
							userInfo.setUserNm(userKtInfo.getUserNm());
							userInfo.setEmail(userKtInfo.getEmail());
							userInfo.setGender(userKtInfo.getGender());
							userInfo.setBirthDay(userKtInfo.getBirthDay());
							userInfo.setJoinStatusKt(userKtInfo.getJoinStatus());
							userInfo.setActionCode("G0001");

							if(YappUtil.isEq(userKtInfo.getJoinStatus(), "G0001")){
								userInfo.setMemStatus("G0001");
								memStatus = "G0001";
							}else{
								userInfo.setMemStatus("G0002");
								memStatus = "G0002";
							}

						}else{

							if (YappUtil.isEq(userInfo.getJoinStatus(), EnumJoinStatus.G0002.getStatusCd())) {
								//아이디 없는 신규가입 불가 - 로그인페이지 이동
								userInfo.setMemStatus("");
								userInfo.setActionCode("G0004");

							}else if (YappUtil.isEq(userInfo.getJoinStatus(), EnumJoinStatus.G0001.getStatusCd())) {
								//KTID 인증이 필요한 회원
								//TODO KTID 미가입 사용자는 샵동의 값을 기존 약관 테이블에 넣어준다.
								//TODO 변경 -> KTID 미가입 사용자도 재동의 약관을 통해 미리 재샵 동의를 받아논다.
								//userService.updateShopTermsAgreeInfo(cntrNo);
								userInfo.setJoinStatusKt("G0001");
								userInfo.setMemStatus("G0002");
								userInfo.setActionCode("G0002");
								memStatus = "G0002";

							}
						}
					}else{
						//KTID 미존재로 KT.COM 가입페이지 유도
						if (YappUtil.isEq(userInfo.getJoinStatus(), EnumJoinStatus.G0002.getStatusCd())) {
							//아이디 없는 신규가입 불가 - 로그인페이지 이동
							userInfo.setMemStatus("");
							userInfo.setActionCode("G0004");

						}else if (YappUtil.isEq(userInfo.getJoinStatus(), EnumJoinStatus.G0001.getStatusCd())) {
							//아이디 없는 기존 회원 - 다음에 하기 가능
							userInfo.setMemStatus("G0002");
							userInfo.setActionCode("G0003");
							memStatus = "G0002";
						}
					}
				} else {

					UserInfo sleepUserInfo = null;
					try {
						sleepUserInfo = userService.getSleepUserInfo(cntrNo);
					} catch (RuntimeException e) {
						logger.info("탈퇴회원 고객");
					}  catch (Exception e) {
						logger.info("탈퇴회원 고객");
					}

					userInfo = new UserInfo();
					userInfo.setSleepUserYn("N");

					if(YappUtil.isNotEmpty(sleepUserInfo)) {
						userInfo.setSleepUserYn(sleepUserInfo.getSleepUserYn());
						userInfo.setSleepRegDt(sleepUserInfo.getSleepRegDt());
					}

					if(existUserId){
						if(existUserKt){
							//기존 아이디로만 가입한 회원이 KT회선을 추가한 경우
							userInfo.setUserNm(userKtInfo.getUserNm());
							userInfo.setEmail(userKtInfo.getEmail());
							userInfo.setGender(userKtInfo.getGender());
							userInfo.setBirthDay(userKtInfo.getBirthDay());
							userInfo.setJoinStatus("G0002");
							userInfo.setJoinStatusKt(userKtInfo.getJoinStatus());
							userInfo.setActionCode("G0001");
							userInfo.setAuthYn("N");
							userInfo.setMobileNo(cntrInfo.getMobileNo());
							userInfo.setMaskingMobileNo(YappUtil.blindMidEndMobileNo(cntrInfo.getMobileNo()));
							userInfo.setCntrNo(cntrInfo.getCntrNo());

							if(YappUtil.isEq(userKtInfo.getJoinStatus(), "G0001")){
								userInfo.setMemStatus("G0001");
								memStatus = "G0001";
							}else{
								userInfo.setMemStatus("G0002");
								memStatus = "G0002";
							}
						}else{
							//아이디 없는 신규가입 불가 - 로그인페이지 이동
							userInfo.setJoinStatus("G0002");
							userInfo.setMemStatus("");
							userInfo.setActionCode("G0004");
						}
					}else{
						//아이디 없는 신규가입 불가 - KT.COM 가입페이지 유도
						userInfo.setJoinStatus("G0002");
						userInfo.setMemStatus("");
						userInfo.setActionCode("G0004");
					}
				}

				cntrInfo.setUserInfo(userInfo);

				logger.info("==============================================================");
				logger.info("/na/user/login/authchkbykey(free pass) -> getActionCode : "+userInfo.getActionCode());
				logger.info("/na/user/login/authchkbykey(free pass) -> getMemStatus : "+userInfo.getMemStatus());
				logger.info("/na/user/login/authchkbykey(free pass) -> getJoinStatus : "+userInfo.getJoinStatus());
				logger.info("/na/user/login/authchkbykey(free pass) -> getJoinStatusKt : "+userInfo.getJoinStatusKt());
				logger.info("==============================================================");

				String dupId = UUID.randomUUID().toString();
				String tmpSessionKey = joinYsid(cntrInfo.getCntrNo(), cntrInfo.getMobileNo(), userId, existUser, dupId, memStatus);
				String encSessionKey = "";

				try {
					encSessionKey = keyFixUtil.encode(tmpSessionKey);
				} catch (BadPaddingException e) {
					// TODO: handle exception
					throw new BadPaddingException();
				} catch (Exception e) {
					// TODO: handle exception
					throw new BadPaddingException();
				}

				List<ContractInfo> cntrInfoList = new ArrayList<>();

				cntrInfoList.add(cntrInfo);

				respResult.setCntrInfo(cntrInfo);
				respResult.setYsid(encSessionKey);

				if(existUser){
					userService.updateDupIdInfo(dupId, cntrInfo.getCntrNo(), osTp, "", appVrsn);
				}

				//사용자 정보 업데이트
				userService.updateUserPpcdAndMobileCd(existUser, existUserKt, cntrInfo.getCntrNo(), userId, cntrInfo.getPpCd(), mobileCd, memStatus, userInfo.getPushRcvYn());

				// 세션에 저장
				SessionKeeper.setSessionId(req,encSessionKey);
				SessionKeeper.addToReq(req, SessionKeeper.KEY_DUP_ID, dupId);
				SessionKeeper.addToReq(req, SessionKeeper.KEY_CNTR_INFO, YappCvtUtil.cvt(cntrInfo, new SessionContractInfo()));
				SessionKeeper.addToReq(req, SessionKeeper.KEY_CNTR_LIST, YappCvtUtil.cvtToSessionCntrInfo(cntrInfoList));
				if(SessionKeeper.getSdata(req) != null){
					SessionKeeper.getSdata(req).setUserId(userId);
					SessionKeeper.getSdata(req).setMobileNo(cntrInfo.getMobileNo());
					SessionKeeper.getSdata(req).setExistUser(existUser);
					SessionKeeper.getSdata(req).setMemStatus(memStatus);
					SessionKeeper.getSdata(req).setName(cntrInfo.getUserNm());
				}

				//220620 암호화(모바일번호/생년월일)
				cntrInfoList = cmnService.aesEncCntrInfoListField(cntrInfoList, req);

				logger.info("휴대폰 로그인 resultInfo : "+resultInfo);

			}

			//SNS로그인 매핑정보 등록
			String snsType = "";
			if(YappUtil.isNotEmpty(authLogin.getSnsType())){
				snsType  = authLogin.getSnsType();
			}

			String snsId = "";
			logger.info("String snsI");
			if(YappUtil.isNotEmpty(authLogin.getSnsId())){
				logger.info("if(YappUtil.isNotEmpty(authLogin.getSnsId())){");
				snsId  = authLogin.getSnsId();
			}

			String snsToken = "";
			if(YappUtil.isNotEmpty(authLogin.getSnsToken())){
				snsToken  = authLogin.getSnsToken();
			}
			logger.info("cntrNo::" + cntrNo+"snsType::" + snsType+"snsId::" + snsId+"snsToken::" + snsToken);
			if(YappUtil.isNotEmpty(cntrNo) && YappUtil.isNotEmpty(snsType) && YappUtil.isNotEmpty(snsId) && YappUtil.isNotEmpty(snsToken)){
				//토큰 검증시 이부부분에 추가

				//토큰 검증시 이부부분에 추가
				AppInfo appinfo = cmsService.getAppInfo(osTp);
				if(YappUtil.isNotEmpty(appinfo) || "Y".equals(appinfo.getSnsLoginYn())){
					String decSnsId  = RsaCipherUtil.decryptRSA(snsId, rsaKeyInfo.getPrivateKey());
					logger.info("decSnsId=================================>"+decSnsId);;
					SnsInfo snsInfo = userService.getSnsInfo(decSnsId, snsType);
					if(YappUtil.isEmpty(snsInfo)){
						//등록된 SNS가 없을 경우 등록해준다.
						userService.insertSnsInfo(decSnsId, snsType, cntrNo);

						if(YappUtil.isNotEmpty(respResult.getCntrInfo()) && YappUtil.isNotEmpty(respResult.getCntrInfo().getUserInfo())){
							respResult.getCntrInfo().getUserInfo().setSnsType(snsType);
						}
					}else{
						//등록된 SNS가 있을 경우
						if(cntrNo.equals(snsInfo.getCntrNo())){
							//계약번호가 동일할 경우 로그인 진행
							if(YappUtil.isNotEmpty(respResult.getCntrInfo()) && YappUtil.isNotEmpty(respResult.getCntrInfo().getUserInfo())){
								respResult.getCntrInfo().getUserInfo().setSnsType(snsType);
							}
						}else{
							//계약번호가 다를 경우 업데이트 후 로그인진행
							userService.updateSnsInfo(decSnsId, snsType, cntrNo);

							if(YappUtil.isNotEmpty(respResult.getCntrInfo()) && YappUtil.isNotEmpty(respResult.getCntrInfo().getUserInfo())){
								respResult.getCntrInfo().getUserInfo().setSnsType(snsType);
							}
						}
					}
				}

			}

			return resultInfo;
		}catch(BadPaddingException e){
			throw new YappException("CHECK_MSG", cmnService.getMsg("EXPT_BAD_PADDING"));
		}catch(Exception e){
			throw new YappException("CHECK_MSG", cmnService.getMsg("EXPT_BAD_PADDING"));
		}
	}

	//예전 1회선일 경우 선택 화면이 없이 넘어갈때 만들어 논것.
/*	public ResultInfo<UserInfo> selectLineOne(String cntrNo, HttpServletRequest req) throws Exception
	{
		ResultInfo<UserInfo> resultInfo = new ResultInfo<>();

		// cntr no가 세션에 저장된 것인지 체크
		SessionContractInfo cntrInfo = SessionKeeper.getCntrInfo(req, cntrNo);
		String reqSessionId = (String) SessionKeeper.get(req, SessionKeeper.KEY_SESSION_ID);
		String sessUserId = null;
		
		if(SessionKeeper.getSdata(req) != null){
			sessUserId = SessionKeeper.getSdata(req).getUserId();
		}
		
		if ( cntrInfo == null ) {
			logger.info("세션에 계약정보가 없습니다.[ 계약정보 cntrInfo null] ");
			throw new YappAuthException("410","로그인 정보가 없습니다.");
		}
		logger.info("server session ======================================" + reqSessionId);
		
		// 사용자 정보 조회
		UserInfo userInfo = userService.getYappUserInfo(cntrNo);
		boolean existUser = false;
		String userId = "";
		
		logger.info("userInfo ============================================" + userInfo);
		
		if(userInfo != null){
			try{
				List<CustAgreeInfoRetvListDtoDetail> custAgreeList = shubService.callFn11267(cntrNo);
				userInfo.getTermsAgree().setOpt3TermsAgreeYn(custAgreeList.get(0).getCustInfoAgreeYn().toString());
				userInfo.getTermsAgree().setOpt3TermsVrsn(custAgreeList.get(0).getAgreeVerNo().toString());
				userInfo.getTermsAgree().setOpt4TermsAgreeYn(custAgreeList.get(1).getCustInfoAgreeYn().toString());
				userInfo.getTermsAgree().setOpt4TermsVrsn(custAgreeList.get(1).getAgreeVerNo().toString());
				userInfo.setOpt3TermsAgreeYn(custAgreeList.get(0).getCustInfoAgreeYn().toString());		//선택이용약관3 동의여부
				userInfo.setOpt3TermsVrsn(custAgreeList.get(0).getAgreeVerNo().toString())	;			//선택이용약관3 버전
				userInfo.setOpt4TermsAgreeYn(custAgreeList.get(1).getCustInfoAgreeYn().toString());		//선택이용약관4 동의여부
				userInfo.setOpt4TermsVrsn(custAgreeList.get(1).getAgreeVerNo().toString());				//선택이용약관4 버전
			
			}catch (RuntimeException e) {

				logger.info("이용약관 조회 error");
				userInfo.getTermsAgree().setOpt3TermsAgreeYn("N");
				userInfo.getTermsAgree().setOpt3TermsVrsn("1.0");
				userInfo.getTermsAgree().setOpt4TermsAgreeYn("N");
				userInfo.getTermsAgree().setOpt4TermsVrsn("1.0");
				userInfo.setOpt3TermsAgreeYn("N");			//선택이용약관3 동의여부
				userInfo.setOpt3TermsVrsn("1.0");			//선택이용약관3 버전
				userInfo.setOpt4TermsAgreeYn("N");			//선택이용약관4 동의여부
				userInfo.setOpt4TermsVrsn("1.0");			//선택이용약관4 버전
			}catch (Exception e) {

				logger.info("이용약관 조회 error");
				userInfo.getTermsAgree().setOpt3TermsAgreeYn("N");
				userInfo.getTermsAgree().setOpt3TermsVrsn("1.0");
				userInfo.getTermsAgree().setOpt4TermsAgreeYn("N");
				userInfo.getTermsAgree().setOpt4TermsVrsn("1.0");
				userInfo.setOpt3TermsAgreeYn("N");			//선택이용약관3 동의여부
				userInfo.setOpt3TermsVrsn("1.0");			//선택이용약관3 버전
				userInfo.setOpt4TermsAgreeYn("N");			//선택이용약관4 동의여부
				userInfo.setOpt4TermsVrsn("1.0");			//선택이용약관4 버전
			}
			logger.info("userInfo : "+userInfo);
		}
		if (userInfo == null){
			existUser = false;
		} else {
			existUser = true;
			userId = userInfo.getUserId();
			if(userId == null || userId.equals("")){
				if(sessUserId != null && !sessUserId.equals("")){
					userId = sessUserId;
				}
			}
		}
		
		// dup ID 생성
		String dupId = UUID.randomUUID().toString();
		String tmpSessionKey = joinYsid(cntrNo, cntrInfo.getMobileNo(), userId, existUser, dupId, "");
		String encSessionKey = keyFixUtil.encode(tmpSessionKey);
		
		if (userInfo == null){
			logger.info("userInfo ======================================" + userInfo);
			userInfo = new UserInfo();
			userInfo.setJoinStatus("G0002");
			userInfo.setAuthYn("N");
			userInfo.setMobileNo(cntrInfo.getMobileNo());
			userInfo.setCntrNo(cntrNo);
		}
		userInfo.setYsid(encSessionKey);
		resultInfo.setResultData(userInfo);

		return resultInfo;
	}*/

	/**
	 * 휴대폰 로그인의 인증 횟수를 체크한다.
	 */
	private String checkAuthFail(AuthFail authFail, AuthLogin authLogin)
	{
		if ( authFail == null )
			return null;

		Calendar curDate = Calendar.getInstance();
		curDate.setTime(authFail.getLastFailDt());
		curDate.add(Calendar.MINUTE, 10);

		String rtnStr = null;

		// 실패횟수가 10번 이상이면 10분동안 인증처리 불가
		if ( authFail.getFailCnt() >= 10 )
		{
			if ( curDate.getTimeInMillis() >= Calendar.getInstance().getTimeInMillis() ) {
				rtnStr = cmnService.getMsg("ERR_10_AUTH_FAIL");
			} else {
				// 10분이 지났으면 실패횟수 초기화
				authFail.setIsInit("Y");
				cmnService.updateAuthFailCnt(authFail);
			}
		}
		return rtnStr;
	}

	/**
	 * 인증 실패 객체 생성
	 */
	private AuthFail makeAuthFail(String mobileNo, String isInit)
	{
		AuthFail authFail = new AuthFail();
		authFail.setMobileNo(mobileNo);
		authFail.setIsInit(isInit);
		authFail.setAuthTp(AuthFail.AUTH_TP_MOBILE);

		return authFail;
	}


	/*
	 새로운 API로 작성 후 Android, IOS 검증 완료 후 삭제 함. 
	 삭제 시 복호화 함수 새로 생성 aesDec128ForPwd로 변경 해야 함.
	 */
	@RequestMapping(value = "/na/user/login/accntusrforpwdbykey", method = RequestMethod.POST)
	@ApiOperation(value="로그인 인증 체크 (ID/PW)", notes="인증 성공 시 계약목록 반환")
	@ApiImplicitParams({
			@ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"),
			@ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header"),
			@ApiImplicitParam(name="mobileCd", value="모바일 코드", dataType="string", paramType="header")})
	public ResultInfo<LoginAcctResp> checkLoginAccntUsrForPwdByKey(String userId, String pwd, int keySeq, HttpServletRequest req) throws Exception
	{
		//String decPwd = AppEncryptUtils.aesDec128ForPwd(pwd, userId);

		logger.info("keySeq : " + keySeq);
		logger.info("pwd : " + pwd);

		if(!YappUtil.isEmpty(userId)){
			//아이디 공백 제거
			userId = userId.trim();

			//220712 패스워드 재사용 방어 코드
			RsaKeyInfo rsaKeyInfo = cmsService.getLoginKeySeq(userId);

			if(!YappUtil.isEmpty(rsaKeyInfo)){
				if(YappUtil.isEq(rsaKeyInfo.getUseYn(),"N")){
					if(!YappUtil.isEq(rsaKeyInfo.getKeySeq(), keySeq)){
						throw new YappException("CHECK_MSG", cmnService.getMsg("ERR_VALID_PASSWORD"));
					}else{
						cmsService.updateLoginKey(userId, 0, "Y");
					}
				}else{
					throw new YappException("CHECK_MSG", cmnService.getMsg("ERR_VALID_PASSWORD"));
				}
			}else{
				throw new YappException("CHECK_MSG", cmnService.getMsg("ERR_VALID_PASSWORD"));
			}

		}

		RsaKeyInfo rsaKeyInfo = cmsService.getRsaPublicKeyInfo(keySeq);

		try{
			String decPwd = RsaCipherUtil.decryptRSA(pwd, rsaKeyInfo.getPrivateKey());

			logger.info("decPwd : " + decPwd);

			ResultInfo<LoginAcctResp> resultInfo = new ResultInfo<>();
			LoginAcctResp respInfo = new LoginAcctResp();

			resultInfo.setResultData(respInfo);

			//아이디, 패스워드 인증 호출
			SoapResponse200 resp200 = shubService.callFn200(userId, decPwd);

			// 호출결과 성공이지만 올레통합계정이 아니면 오류
			if ( YappUtil.isEq(resp200.getRtnCd(), EnumYn.C_17.getValue()) )
			{
				throw new YappException("CHECK_MSG", cmnService.getMsg("ERR_NOT_KT_MAIL"));
			}

			// 호출결과 성공이지만 올레통합계정이 아니면 오류
			if ( YappUtil.isEq(resp200.getRtnCd(), EnumYn.C_1.getValue())
					&& YappUtil.isNotEq(resp200.getCredentialTypeCd(), "01") )
			{
				throw new YappException("CHECK_MSG", cmnService.getMsg("ERR_NOT_OLLEH_ACCT"));
			}

			String cid = resp200.getCredentialId();

			// 인증 성공 시, 계약목록 반환
			if ( YappUtil.isEq(resp200.getRtnCd(), EnumYn.C_1.getValue()) )
			{
				// 계약 목록 조회
				SoapResponse140 resp = shubService.callFn140(userId, cid);

				List<ContractInfo> cntrList = resp.getCntrInfoList();

				//220606 kos가입제한 통과 여부 체크 / 통과못하면 준회원 가입
				cntrList = cmnService.kosJoinLimit(cntrList);

				logger.info("accntusrforpwdbykey cntrList : "+cntrList);

				// 계약번호 유무에 따라 회선 사용자와 비회선 사용자 분기처리
				if(cntrList.size()>0){

					//respInfo.setCntrInfoList(YappCvtUtil.cvtCntrInfoToRespList(resp.getCntrInfoList()));
					respInfo.setCntrInfoList(cntrList);
					respInfo.setCredentialId(cid);

					// 세션 ID 생성(임의적인 키)
					String sessionId = UUID.randomUUID().toString();
					//respInfo.setYsid(sessionId);

					// 세션에 저장
					//SessionKeeper.addToReq(req, SessionKeeper.KEY_SESSION_ID, sessionId);
					SessionKeeper.addToReq(req, SessionKeeper.KEY_CNTR_LIST, YappCvtUtil.cvtToSessionCntrInfo(cntrList));
					SessionKeeper.setSessionId(req,"");

					if(SessionKeeper.getSdata(req) != null){
						SessionKeeper.getSdata(req).setUserId(userId);
						SessionKeeper.getSdata(req).setCredentialId(cid);
						SessionKeeper.getSdata(req).setName(cntrList.get(0).getUserNm());
					}

					// 회선수가 1인 경우 회선선택 한다.
					/*if ( cntrList.size() == 1 )
					{
						ResultInfo<UserInfo> resultUserInfo = selectLineOne(cntrList.get(0).getCntrNo(), req);
						cntrList.get(0).setUserInfo(resultUserInfo.getResultData());
					}*/

				}else{

					//KTID 사용자 정보 세팅
					//respInfo.setCntrInfoList(YappCvtUtil.cvtCntrInfoToRespList(resp.getCntrInfoList()));
					respInfo.setCntrInfoList(cntrList);
					respInfo.setCredentialId(cid);

					SessionKeeper.addToReq(req, SessionKeeper.KEY_CNTR_LIST, YappCvtUtil.cvtToSessionCntrInfo(cntrList));
					SessionKeeper.setSessionId(req,"");

					if(SessionKeeper.getSdata(req) != null){
						SessionKeeper.getSdata(req).setUserId(userId);
						SessionKeeper.getSdata(req).setCredentialId(cid);
					}

					ContractInfo cntrInfo = new ContractInfo();
					cntrList.add(cntrInfo);

					// User Info를 세팅한다
					String appVrsn = req.getHeader("appVrsn");
					String osTp = req.getHeader("osTp");
					String mobileCd = req.getHeader("mobileCd");

					// 사용자 정보 조회
					UserInfo userKtInfo = userService.getYappUserKtInfo(userId);
					boolean existUserKt = false;

					//String userId = "";
					String userIdTmp = userId;
					String joinStatus = "";
					String memStatus = "";
					String dupId = UUID.randomUUID().toString();

					if (userKtInfo == null){
						existUserKt = false;
					} else {
						existUserKt = true;
					}

					if (existUserKt){
						if(userIdTmp.length()>20){
							logger.warn("[USER ID] : " + userIdTmp + " ----> 20자리이상 사 유로 빈칸으로 저장.");
							userIdTmp = "";
						}
						userService.updateDupIdInfoKt(dupId, osTp, userId, appVrsn);

						//220606 정회원이kt계약을 해지했을때 준회원으로 ktyn n으로 업데이트
						if(!YappUtil.isEmpty(userKtInfo.getKtYn())){
							if(YappUtil.isEq(userKtInfo.getKtYn(), "Y")){
								UserInfo userInfoKtYn = new UserInfo();
								userInfoKtYn.setUserId(userKtInfo.getUserId());
								userInfoKtYn.setKtYn("N");
								userService.updateUserKtSettingInfo(userInfoKtYn);
							}
						}

					}else{

						UserInfo sleepUserInfo = null;
						try {
							sleepUserInfo = userService.getSleepUserKtInfo(userId);
						} catch (RuntimeException e) {
							logger.info("탈퇴회원 고객");
						}  catch (Exception e) {
							logger.info("탈퇴회원 고객");
						}

						SoapResponse101 resp101 = shubService.callFn101(cid);

						logger.info("==============================================================");
						logger.info("accntusrforpwdbykey -> resp101: " + resp101.getBirthDate());
						logger.info("==============================================================");

						userKtInfo = new UserInfo();
						userKtInfo.setJoinStatus("G0002");
						userKtInfo.setUserId(userId);
						userKtInfo.setUserNm(resp101.getEncUserNm());
						userKtInfo.setEmail(resp101.getEmail());
						userKtInfo.setGender(resp101.getGender().trim());
						userKtInfo.setBirthDay(resp101.getBirthDate());
						userKtInfo.setSleepUserYn("N");

						if(YappUtil.isNotEmpty(sleepUserInfo)) {
							userKtInfo.setSleepUserYn(sleepUserInfo.getSleepUserYn());
							userKtInfo.setSleepRegDt(sleepUserInfo.getSleepRegDt());
						}
					}

					memStatus = "G0003";

					String tmpSessionKey = joinYsid("ktid_cntr", "ktid_mobile", userId, existUserKt, dupId, memStatus);
					String encSessionKey = keyFixUtil.encode(tmpSessionKey);

					//TODO 마스킹된 이름 세팅
					//cntrPlan.setUserNm(YappUtil.blindNameToName(partyName, 1));
					cntrInfo.setUserNm(YappUtil.blindNameToName(userKtInfo.getUserNm(),1));
					cntrInfo.setBirthDate(userKtInfo.getBirthDay());
					userKtInfo.setMemStatus(memStatus);
					userKtInfo.setActionCode("G0001");
					userKtInfo.setYsid(encSessionKey);
					cntrList.get(0).setUserInfo(userKtInfo);
					//resultInfo.setResultData(userInfo);

					logger.info("==============================================================");
					logger.info("accntusrforpwdbykey -> cntrList.get(0): " + cntrList.get(0).getBirthDate());
					logger.info("==============================================================");

					if(userKtInfo.getJoinStatus() != null){
						joinStatus = userKtInfo.getJoinStatus();
					}

					//사용자 정보 업데이트
					userService.updateUserPpcdAndMobileCd(false, existUserKt, null, userId, null, mobileCd, memStatus, null);

					// 세션에 계약정보, 사용자 정보 저장
					SessionKeeper.addToReq(req, SessionKeeper.KEY_SESSION_ID, encSessionKey);
					SessionKeeper.addToReq(req, SessionKeeper.KEY_DUP_ID, dupId);
					SessionKeeper.addToReq(req, SessionKeeper.KEY_CNTR_INFO, YappCvtUtil.cvt(cntrList.get(0), new SessionContractInfo()));
					if(SessionKeeper.getSdata(req) != null){
						SessionKeeper.getSdata(req).setMobileNo("ktid_mobile");
						SessionKeeper.getSdata(req).setExistUser(existUserKt);
						SessionKeeper.getSdata(req).setMemStatus(memStatus);
						SessionKeeper.getSdata(req).setName(YappUtil.blindNameToName(userKtInfo.getUserNm(),1));
					}
					if (existUserKt && joinStatus.equals("G0001")){
						//사용후 계약 목록 세션에서 삭제 (USER 테이블에 살이 있는 경우만 삭제)
						req.getSession().removeAttribute(SessionKeeper.KEY_CNTR_LIST);
					}
				}

				//220620 암호화(모바일번호/생년월일)
				cntrList = cmnService.aesEncCntrInfoListField(cntrList, req);

				logger.info("ID/PW로그인 resultInfo : "+resultInfo);

			} else
			{
				logger.error("사용자 인증 실패");
				respInfo.setAuthFailCnt(0);
				YappUtil.setErrResultInfo(resultInfo, resp200.getRtnCd(), cmnService.getMsg(resp200.getRtnCd()));

				// API 에러 로그를 추가한다.
				ApiError apiError = YappUtil.makeApiErrInfo(req, resp200.getRtnCd(), cmnService.getMsg(resp200.getRtnCd()), "SYSTEM_MSG", "[OIF_200] Fail", "");
				histService.insertApiErrLog(apiError, req);
				if("2004".equals(resultInfo.getResultCd())){
					resultInfo.setResultCd("20010");
				}
			}


			return resultInfo;
		}catch(BadPaddingException e){
			throw new YappException("CHECK_MSG", cmnService.getMsg("EXPT_BAD_PADDING"));
		}
	}

	@RequestMapping(value = "/na/user/login/certifyusrforpwdbykey", method = RequestMethod.POST)
	@ApiOperation(value="로그인 인증 체크 (ID/PW)", notes="인증 성공 시 계약목록 반환")
	@ApiImplicitParams({
			@ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"),
			@ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header"),
			@ApiImplicitParam(name="mobileCd", value="모바일 코드", dataType="string", paramType="header")})
	public ResultInfo<UserInfo> certifyLoginAccntUsrForPwdByKey(String userId, String pwd, int keySeq, HttpServletRequest req) throws Exception
	{

		String sessCntrNo = SessionKeeper.getCntrNo(req);
		String sessMobileNo = null;
		String sessUserId = null;

		if(SessionKeeper.getSdata(req) != null){
			sessMobileNo = SessionKeeper.getSdata(req).getMobileNo();
			sessUserId = SessionKeeper.getSdata(req).getUserId().trim();
		}

		logger.info("==============================================================");
		logger.info("keySeq : " + keySeq);
		logger.info("userId : " + userId);
		logger.info("pwd : " + pwd);
		logger.info("sessCntrNo : " + sessCntrNo);
		logger.info("sessUserId : " + sessUserId);
		logger.info("sessMobileNo : " + sessMobileNo);
		logger.info("==============================================================");

		//20220614 by cha: 세션에 계약번호나 ID와 모바일 번호가 둘다 없으면 오류 추가
		if(YappUtil.isEmpty(sessCntrNo) || (YappUtil.isEmpty(sessUserId) && YappUtil.isEmpty(sessMobileNo))) {
			throw new YappAuthException("410","로그인 정보가 없습니다.");
		}

		if(YappUtil.isEmpty(sessUserId) && !YappUtil.isEmpty(sessMobileNo)) {
			SoapResponse003 resp003 = shubService.callFn003(sessMobileNo);

			for(IdsItem item:resp003.getIdsList()){
				logger.info("resp003 : " + item.getCredentialTypeCd());
				if(item.getCredentialTypeCd().equals("01")){
					sessUserId = item.getUserId().trim();

					break;
				}
			}
		}

		if(YappUtil.isEmpty(sessUserId)){
			throw new YappException("CHECK_MSG", cmnService.getMsg("ERR_NOT_EXIST_KTID"));
		}

		if(YappUtil.isNotEq(userId.trim(), sessUserId))
		{
			throw new YappException("CHECK_MSG", cmnService.getMsg("ERR_NOT_SAME_LOGIN_KTID"));
		}

		RsaKeyInfo rsaKeyInfo = cmsService.getRsaPublicKeyInfo(keySeq);

		try{

			String decPwd = RsaCipherUtil.decryptRSA(pwd, rsaKeyInfo.getPrivateKey());

			logger.info("decPwd : " + decPwd);

			ResultInfo<UserInfo> resultInfo = new ResultInfo<>();
			UserInfo userInfo = new UserInfo();

			//아이디, 패스워드 인증 호출
			SoapResponse200 resp200 = shubService.callFn200(sessUserId, decPwd);

			// 호출결과 성공이지만 올레통합계정이 아니면 오류
			if ( YappUtil.isEq(resp200.getRtnCd(), EnumYn.C_1.getValue())
					&& YappUtil.isNotEq(resp200.getCredentialTypeCd(), "01") )
			{
				throw new YappException("CHECK_MSG", cmnService.getMsg("ERR_NOT_OLLEH_ACCT"));
			}

			String cid = resp200.getCredentialId();

			logger.info("==============================================================");
			logger.info("certifyLoginAccntUsrForPwdByKey -> cid : "+cid);
			logger.info("==============================================================");

			if ( YappUtil.isEq(resp200.getRtnCd(), EnumYn.C_1.getValue()))
			{
				userInfo = userService.getYappUserInfo(sessCntrNo);

				// 기존 YBOX 미가입자는 KTID인증을 통한 사용 불가
				if (YappUtil.isEq(userInfo.getJoinStatus(), EnumJoinStatus.G0002.getStatusCd()))
				{
					//TODO YBOX 미가입자 인증 불가 문구
					throw new YappException("CHECK_MSG", cmnService.getMsg("ERR_NOT_OLLEH_ACCT"));
				}

				//20220916 by cha 전화번호 변경 케이스 보완
				userInfo.setMobileNo(sessMobileNo);

				SoapResponse101 resp101 = shubService.callFn101(cid);
				userInfo.setUserId(sessUserId);
				userInfo.setUserNm(resp101.getEncUserNm());
				userInfo.setEmail(resp101.getEmail());
				userInfo.setGender(resp101.getGender().trim());
				userInfo.setBirthDay(resp101.getBirthDate());
				userInfo.setYsid(reBuildYsid(req, sessUserId));
				userInfo.setJoinStatusKt("G0002");
				userInfo.setActionCode("G0001");

				resultInfo.setResultData(userInfo);

				if(SessionKeeper.getSdata(req) != null){
					SessionKeeper.getSdata(req).setMemStatus("G0002");
					SessionKeeper.getSdata(req).setUserId(sessUserId);
				}

				//220620 userinfo 암호화
				userInfo = cmnService.aesEncUserInfoField(userInfo, req);

				logger.info("certifyusrforpwdbykey resultInfo : "+resultInfo);

			} else
			{

				logger.error("사용자 인증 실패");
				//respInfo.setAuthFailCnt(0);
				YappUtil.setErrResultInfo(resultInfo, resp200.getRtnCd(), cmnService.getMsg(resp200.getRtnCd()));

				// API 에러 로그를 추가한다.
				ApiError apiError = YappUtil.makeApiErrInfo(req, resp200.getRtnCd(), cmnService.getMsg(resp200.getRtnCd()), "SYSTEM_MSG", "[OIF_200] Fail", "");
				histService.insertApiErrLog(apiError, req);
				if("2004".equals(resultInfo.getResultCd())){
					resultInfo.setResultCd("20010");
				}
			}

			return resultInfo;


		}catch(BadPaddingException e){
			throw new YappException("CHECK_MSG", cmnService.getMsg("EXPT_BAD_PADDING"));
		}
	}

	@RequestMapping(value = "/na/user/oneline", method = RequestMethod.POST)
	@ApiOperation(value="회선 선택")
	@ApiImplicitParams({
			@ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"),
			@ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header"),
			@ApiImplicitParam(name="mobileCd", value="모바일 코드", dataType="string", paramType="header")})
	public ResultInfo<UserInfo> selectOneLine(String cntrNo, String snsType, String snsId, String snsToken, String iosAuthCode, HttpServletRequest req) throws Exception
	{
		ResultInfo<UserInfo> resultInfo = new ResultInfo<>();
		String appVrsn = req.getHeader("appVrsn");
		String osTp = req.getHeader("osTp");
		String mobileCd = req.getHeader("mobileCd");
		String decCntrNo  = appEncryptUtils.aesDec128(cntrNo, osTp+appVrsn);

		// cntr no가 세션에 저장된 것인지 체크
		SessionContractInfo cntrInfo = SessionKeeper.getCntrInfo(req, decCntrNo);
		String reqSessionId = (String) SessionKeeper.get(req, SessionKeeper.KEY_SESSION_ID);
		String sessUserId = null;
		String cid = null;

		logger.info("========================================================================");
		logger.info("/na/user/oneline -> encCntrNo:"+cntrNo);
		logger.info("========================================================================");

		if(SessionKeeper.getSdata(req) != null){
			sessUserId = SessionKeeper.getSdata(req).getUserId().trim();
			cid = SessionKeeper.getSdata(req).getCredentialId();
		}

		//20220407 by cha: 세션에 ID나 크리덴셜ID가 없으면 오류 추가
		if ( cntrInfo == null || sessUserId == null || cid == null) {
			logger.info("[cntrInfo] "+cntrInfo);
			logger.info("[sessUserId] "+sessUserId);
			logger.info("[cid] "+cid);
			throw new YappAuthException("410","로그인 정보가 없습니다.");
		}

		logger.info("========================================================================");
		logger.info("/na/user/oneline -> cntrNo:"+cntrNo);
		logger.info("/na/user/oneline -> decCntrNo:"+decCntrNo);
		logger.info("/na/user/oneline -> ppCd:"+cntrInfo.getPpCd());
		logger.info("/na/user/oneline -> mobileCd:"+mobileCd);
		logger.info("/na/user/oneline -> MobileNo:"+cntrInfo.getMobileNo());
		logger.info("=========================================================================");

		// 사용자 정보 조회
		UserInfo userInfo = userService.getYappUserInfo(decCntrNo);
		UserInfo userKtInfo = userService.getYappUserKtInfo(sessUserId);

		// 회원여부
		boolean existUser = false;
		boolean existUserKt = false;

		String userId = sessUserId;
		String userIdTmp = "";
		String joinStatus = "";
		String joinStatusKt = "";
		String memStatus = "";
		String dupId = UUID.randomUUID().toString();

		existUser = userInfo != null;
		existUserKt = userKtInfo != null;

		logger.info("existUser:"+existUser);
		logger.info("existUserKt:"+existUserKt);

		if(existUser){

			userId = userInfo.getUserId();

			//20220816 by cha 번호가 변경됐을시 DB정보가 안맞기 때문에 새로 셋해줌
			userInfo.setMobileNo(cntrInfo.getMobileNo());

			if(userId == null || userId.equals("")){
				if(sessUserId != null && !sessUserId.equals("")){
					userIdTmp = sessUserId;
					userId = sessUserId;
				}
			}

			if(existUserKt){
				userInfo.setUserNm(userKtInfo.getUserNm());
				userInfo.setEmail(userKtInfo.getEmail());
				userInfo.setGender(userKtInfo.getGender());
				userInfo.setBirthDay(userKtInfo.getBirthDay());
				userInfo.setJoinStatusKt(userKtInfo.getJoinStatus());

				logger.info("existUser && existUserKt=================================================");
				logger.info("/na/user/oneline -> JoinStatus:"+userInfo.getJoinStatus());
				logger.info("/na/user/oneline -> setJoinStatusKt:"+userKtInfo.getJoinStatus());
				logger.info("/na/user/oneline -> setShopTermsAgreeYn:"+userKtInfo.getTermsAgree().getShopTermsAgreeYn());
				logger.info("/na/user/oneline -> setShopTermsVrsn:"+userKtInfo.getTermsAgree().getShopTermsVrsn());
				logger.info("=========================================================================");

				userInfo.getTermsAgree().setShopTermsAgreeYn(userKtInfo.getTermsAgree().getShopTermsAgreeYn());
				userInfo.getTermsAgree().setShopTermsVrsn(userKtInfo.getTermsAgree().getShopTermsVrsn());
				userInfo.setMemStatus("G0001");

			}else{
				SoapResponse101 resp101 = shubService.callFn101(cid);
				userInfo.setUserNm(resp101.getEncUserNm());
				userInfo.setEmail(resp101.getEmail());
				userInfo.setGender(resp101.getGender().trim());
				userInfo.setBirthDay(resp101.getBirthDate());
				userInfo.setJoinStatusKt("G0002");
			}
		}

		if (existUser){

			if(userIdTmp.length()>20){
				logger.warn("[USER ID] : " + userIdTmp + " ----> 20자리이상 사 유로 빈칸으로 저장.");
				userIdTmp = "";
			}
			userService.updateDupIdInfo(dupId, decCntrNo, osTp, userIdTmp, appVrsn);

		}else{

			SendSms sendSms = userService.getAuthYnCntrNo(decCntrNo);
			String skipYn = "N";
			if(sendSms != null && "Y".equals(sendSms.getAuthSmsYn())){
				skipYn = "Y";
			}

			UserInfo sleepUserInfo = null;
			try {
				sleepUserInfo = userService.getSleepUserInfo(decCntrNo);
			} catch (RuntimeException e) {
				logger.info("탈퇴회원 고객");
			}  catch (Exception e) {
				logger.info("탈퇴회원 고객");
			}

			userInfo = new UserInfo();
			userInfo.setJoinStatus("G0002");
			if("G0001".equals(osTp) && "1.0.3".equals(appVrsn) && skipYn.equals("Y")){
				userInfo.setAuthYn("Y");
			}else{
				userInfo.setAuthYn("N");
			}

			userInfo.setMobileNo(cntrInfo.getMobileNo());
			userInfo.setMaskingMobileNo(YappUtil.blindMidEndMobileNo(cntrInfo.getMobileNo()));
			userInfo.setCntrNo(decCntrNo);
			userInfo.setPpCd(cntrInfo.getPpCd());
			userInfo.setSleepUserYn("N");

			if(YappUtil.isNotEmpty(sleepUserInfo)) {
				userInfo.setSleepUserYn(sleepUserInfo.getSleepUserYn());
				userInfo.setSleepRegDt(sleepUserInfo.getSleepRegDt());
			}

			if(existUserKt){
				userInfo.setUserNm(userKtInfo.getUserNm());
				userInfo.setEmail(userKtInfo.getEmail());
				userInfo.setGender(userKtInfo.getGender());
				userInfo.setBirthDay(userKtInfo.getBirthDay());
				userInfo.setJoinStatusKt(userKtInfo.getJoinStatus());
			}else{
				SoapResponse101 resp101 = shubService.callFn101(cid);
				userInfo.setUserNm(resp101.getEncUserNm());
				userInfo.setEmail(resp101.getEmail());
				userInfo.setGender(resp101.getGender().trim());
				userInfo.setBirthDay(resp101.getBirthDate());
				userInfo.setJoinStatusKt("G0002");
			}
		}

		//20220727 by cha 번호 변경시 회원상태 코드 보완
		boolean nonMember = YappUtil.isEq(userInfo.getJoinStatus(), EnumJoinStatus.G0002.getStatusCd());
		boolean nonMemberKt = YappUtil.isEq(userInfo.getJoinStatusKt(), EnumJoinStatus.G0002.getStatusCd());

		if(!nonMember&&nonMemberKt){
			memStatus = "G0002";
		}else{
			memStatus = "G0001";
		}

		String tmpSessionKey = joinYsid(decCntrNo, cntrInfo.getMobileNo(), userId, existUser, dupId, memStatus);
		String encSessionKey = keyFixUtil.encode(tmpSessionKey);

		userInfo.setMemStatus(memStatus);
		userInfo.setActionCode("G0001");
		userInfo.setYsid(encSessionKey);
		resultInfo.setResultData(userInfo);

		if(userInfo.getJoinStatus() != null && userInfo.getJoinStatusKt() != null){
			joinStatus = userInfo.getJoinStatus();
			joinStatusKt = userInfo.getJoinStatusKt();
		}

		logger.info("=========================================================================");
		logger.info("/na/user/oneline -> memStatus:"+memStatus);
		logger.info("/na/user/oneline -> JoinStatus:"+joinStatus);
		logger.info("/na/user/oneline -> joinStatusKt:"+joinStatusKt);
		logger.info("=========================================================================");

		//사용자 정보 업데이트
		userService.updateUserPpcdAndMobileCd(existUser, existUserKt, decCntrNo, userId, cntrInfo.getPpCd(), mobileCd, memStatus, userInfo.getPushRcvYn());

		// 세션에 계약정보, 사용자 정보 저장
		SessionKeeper.addToReq(req, SessionKeeper.KEY_SESSION_ID, encSessionKey);
		SessionKeeper.addToReq(req, SessionKeeper.KEY_DUP_ID, dupId);
		SessionKeeper.addToReq(req, SessionKeeper.KEY_CNTR_INFO, YappCvtUtil.cvt(cntrInfo, new SessionContractInfo()));

		if(SessionKeeper.getSdata(req) != null){
			SessionKeeper.getSdata(req).setMobileNo(cntrInfo.getMobileNo());
			SessionKeeper.getSdata(req).setExistUser(existUser);
			SessionKeeper.getSdata(req).setMemStatus(memStatus);
		}

		if (existUser && joinStatus.equals("G0001") && joinStatusKt.equals("G0001")){
			//사용후 계약 목록 세션에서 삭제 (USER 테이블에 살이 있는 경우만 삭제)
			req.getSession().removeAttribute(SessionKeeper.KEY_CNTR_LIST);
		}

		//SNS로그인 매핑정보 등록
		if(YappUtil.isNotEmpty(snsType) && YappUtil.isNotEmpty(snsId) && YappUtil.isNotEmpty(snsToken)){
			//토큰 검증시 이부부분에 추가

			//토큰 검증시 이부부분에 추가
			AppInfo appinfo = cmsService.getAppInfo(osTp);
			if(YappUtil.isNotEmpty(appinfo) || "Y".equals(appinfo.getSnsLoginYn())){
				String decSnsId  = appEncryptUtils.aesDec128(snsId, osTp+appVrsn);
				SnsInfo snsInfo = userService.getSnsInfo(decSnsId, snsType);
				if(YappUtil.isEmpty(snsInfo)){
					//등록된 SNS가 없을 경우 등록해준다.
					SnsInfo snsCntrInfo = userService.getSnsCntrInfo(decCntrNo, snsType);
					if(YappUtil.isEmpty(snsCntrInfo)){
						userService.insertSnsInfo(decSnsId, snsType, decCntrNo);
					}else{
						userService.updateSnsCntrInfo(decSnsId, snsType, decCntrNo);
					}
					userInfo.setSnsType(snsType);
				}else{
					//등록된 SNS가 있을 경우
					if(decCntrNo.equals(snsInfo.getCntrNo())){
						//계약번호가 동일할 경우 로그인 진행
						userInfo.setSnsType(snsType);
					}else{
						//계약번호가 다를 경우 업데이트 후 로그인진행
						userService.updateSnsInfo(decSnsId, snsType, decCntrNo);

						userInfo.setSnsType(snsType);
					}
				}
			}

		}

		//220620 암호화 userinfo(모바일번호/생년월일/userid/이메일
		userInfo = cmnService.aesEncUserInfoField(userInfo, req);
		logger.info("oneline resultInfo : "+resultInfo);

		return resultInfo;
	}

	@RequestMapping(value = "/user/joins", method = RequestMethod.POST)
	@ApiOperation(value="서비스 가입")
	@ApiImplicitParams({
			@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),
			@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"),
			@ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"),
			@ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header"),
			@ApiImplicitParam(name="mobileCd", value="모바일 코드", dataType="string", paramType="header")})
	public ResultInfo<UserInfo> joinsService(@RequestBody UserInfoReq userInfo, HttpServletRequest req) throws Exception
	{
		ResultInfo<UserInfo> resultInfo = new ResultInfo<>();

		logger.info(userInfo.getEmail());
		logger.info(userInfo.getGender());
		logger.info(userInfo.getUserNm());

		//220620 UserInfoReq 복호화
		userInfo = cmnService.aesDecUserInfoReqField(userInfo, req);
		logger.info("/user/joins 복호화 userInfo : "+userInfo);

		/**
		 * =========================================================================================
		 * 20220318 by cha
		 * case1: KTID만 존재
		 * case2: KTID와 계약번호가 둘다 존재
		 * YBOX 3.0 이후로는 신규로 회선가입 불가. (KTID인증 필수)
		 * =========================================================================================
		 */

		String memStatus = "";

		if(YappUtil.isEmpty(userInfo.getCntrNo())){
			// KTID로만 가입

			String dupId = (String) SessionKeeper.get(req, SessionKeeper.KEY_DUP_ID);

			// 가입 정보 추가
			UserInfo joinUserInfo = YappCvtUtil.cvtReqToUserInfo(userInfo);
			String osTp = req.getHeader("osTp");
			String appVrsn = req.getHeader("appVrsn");
			String mobileCd = req.getHeader("mobileCd");

			logger.info("==============================================================");
			logger.info("/user/joins -> 준회원 가입 ");
			logger.info("/user/joins -> joinUserInfo : "+joinUserInfo);
			logger.info("/user/joins -> joinStatus : "+joinUserInfo.getJoinStatus());
			logger.info("/user/joins -> joinStatusKt : "+joinUserInfo.getJoinStatusKt());
			logger.info("==============================================================");

			joinUserInfo.setDupId(dupId);
			joinUserInfo.setOsTp(osTp);
			joinUserInfo.setAppVrsn(appVrsn);
			joinUserInfo.setMobileCd(mobileCd);
			joinUserInfo.setJoinStatus("");
			joinUserInfo.setMemStatus("G0003");
			//20220606
			joinUserInfo.setKtYn("N");

			memStatus = "G0003";

			userService.insertUserKtInfo(joinUserInfo);

			UserInfo insUserInfo = userService.getYappUserKtInfo(userInfo.getUserId());

			resultInfo.setResultData(insUserInfo);

			//220620 암호화 userinfo(모바일번호/생년월일/userid/이메일
			insUserInfo = cmnService.aesEncUserInfoField(insUserInfo, req);
			logger.info("kt 비회선 join resultInfo : "+resultInfo);

		}else{
			// 계약번호 + KTID 가입

			String loginMobileNo = null;

			if(SessionKeeper.getSdata(req) != null){
				loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
			}

			String decCntrNo  = null;

			if(loginMobileNo != null){
				decCntrNo  = appEncryptUtils.aesDec128(userInfo.getCntrNo(), loginMobileNo);
			}

			userInfo.setCntrNo(decCntrNo);

			if(SessionKeeper.getSdata(req) != null){
				if ( SessionKeeper.getSdata(req) == null
						|| (SessionKeeper.getCntrNo(req) != null && YappUtil.isNotEq(SessionKeeper.getCntrNo(req), userInfo.getCntrNo()))
						|| YappUtil.isNotEq(SessionKeeper.getSdata(req).getMobileNo(), userInfo.getMobileNo()) )
				{
					throw new YappAuthException(cmnService.getMsg("ERR_NOT_SAME_LOGIN"));		// 로그인된 정보와 일치하지 않습니다.
				}
			}else{
				throw new YappAuthException(cmnService.getMsg("ERR_NOT_SAME_LOGIN"));		// 로그인된 정보와 일치하지 않습니다.
			}

			String dupId = (String) SessionKeeper.get(req, SessionKeeper.KEY_DUP_ID);

			// 가입 정보 추가
			UserInfo joinUserInfo = YappCvtUtil.cvtReqToUserInfo(userInfo);

			String osTp = req.getHeader("osTp");
			String appVrsn = req.getHeader("appVrsn");
			String mobileCd = req.getHeader("mobileCd");

			if(SessionKeeper.getSdata(req) != null){
				joinUserInfo.setUserId(SessionKeeper.getSdata(req).getUserId());
			}

			joinUserInfo.setDupId(dupId);
			joinUserInfo.setOsTp(osTp);
			joinUserInfo.setAppVrsn(appVrsn);
			joinUserInfo.setMobileCd(mobileCd);

			logger.info("==============================================================");
			logger.info("/user/joins -> 정회원 가입 ");
			logger.info("/user/joins -> joinUserInfo : "+joinUserInfo);
			logger.info("/user/joins -> joinStatus : "+joinUserInfo.getJoinStatus());
			logger.info("/user/joins -> joinStatusKt : "+joinUserInfo.getJoinStatusKt());
			logger.info("==============================================================");

			boolean nonMember = YappUtil.isEq(joinUserInfo.getJoinStatus(), EnumJoinStatus.G0002.getStatusCd());
			boolean nonMemberKt = YappUtil.isEq(joinUserInfo.getJoinStatusKt(), EnumJoinStatus.G0002.getStatusCd());

			//220628 g0001값추가 빈값이면 user테이블 authYn이 N으로 update됨
			joinUserInfo.setJoinStatus("G0001");
			joinUserInfo.setMemStatus("G0001");

			//20220606
			joinUserInfo.setKtYn("Y");

			if(nonMember && !nonMemberKt){

				userService.insertUserInfo(joinUserInfo);
			}else if(!nonMember && nonMemberKt){

				userService.insertUserKtInfo(joinUserInfo);

			}else if(nonMember && nonMemberKt){

				userService.insertUserAllInfo(joinUserInfo);
			}

			UserInfo insUserInfo = userService.getYappUserInfo(userInfo.getCntrNo());

			insUserInfo.setUserNm(joinUserInfo.getUserNm());
			insUserInfo.setEmail(joinUserInfo.getEmail());
			insUserInfo.setGender(joinUserInfo.getGender());
			insUserInfo.setBirthDay(joinUserInfo.getBirthDay());
			memStatus = "G0001";

			String cntrNo=userInfo.getCntrNo();
			String opt3TermsAgreeYn=userInfo.getTermsAgree().getOpt3TermsAgreeYn();
			String opt4TermsAgreeYn=userInfo.getTermsAgree().getOpt4TermsAgreeYn();
			String opt3TermsVer=userInfo.getTermsAgree().getOpt3TermsVrsn();
			String opt4TermsVer=userInfo.getTermsAgree().getOpt4TermsVrsn();

			if(YappUtil.isNotEmpty(opt3TermsAgreeYn) && YappUtil.isNotEmpty(opt4TermsAgreeYn)){

				try{
					shubService.callFn11268(cntrNo, opt3TermsAgreeYn, opt4TermsAgreeYn, opt3TermsVer, opt4TermsVer);

				}catch (RuntimeException e) {

					logger.info("이용약관 api error");
				}catch (Exception e) {

					logger.info("이용약관 api error");
				}
			}

			resultInfo.setResultData(insUserInfo);

			//220620 암호화 userinfo(모바일번호/생년월일/userid/이메일
			insUserInfo = cmnService.aesEncUserInfoField(insUserInfo, req);
			logger.info("kt회선 join resultInfo : "+resultInfo);
		}

		if(SessionKeeper.getSdata(req) != null){
			SessionKeeper.getSdata(req).setExistUser(true);
			SessionKeeper.getSdata(req).setMemStatus(memStatus);
		}

		return resultInfo;
	}


	@RequestMapping(value = "/user/terms/agree", method = RequestMethod.POST)
	@ApiOperation(value="약관 동의 정보 추가(이용약관 재동의)")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<Integer> updateTerms(@RequestBody TermsAgree termsAgree, HttpServletRequest req) throws Exception
	{
		String memStatus = "";
		String userId = null;
		String cntrNo = SessionKeeper.getCntrNo(req);

		if(SessionKeeper.getSdata(req) != null){
			memStatus = SessionKeeper.getSdata(req).getMemStatus();
			userId = SessionKeeper.getSdata(req).getUserId().trim();
		}

		logger.info("==============================================================");
		logger.info("/user/terms/agree -> memStatus : "+memStatus);
		logger.info("/user/terms/agree -> cntrNo : "+cntrNo);
		logger.info("/user/terms/agree -> userId : "+userId);
		logger.info("==============================================================");

		int result = 0;

		if(YappUtil.isEq(memStatus, "G0001")){

			termsAgree.setCntrNo(cntrNo);
			termsAgree.setUserId(userId);

			userService.insertTermsAgreeAll(termsAgree);

		}else if(YappUtil.isEq(memStatus, "G0002")){

			termsAgree.setCntrNo(cntrNo);

			userService.insertTermsAgree(termsAgree);

		}else if(YappUtil.isEq(memStatus, "G0003")){

			termsAgree.setUserId(userId);
			userService.insertTermsAgreeKt(termsAgree);

		}else{

			throw new YappAuthException("410","로그인 정보가 없습니다.");
		}

		return new ResultInfo<>(result);
	}

	@RequestMapping(value = "/user/terms/opt", method = RequestMethod.POST)
	@ApiOperation(value="선택 약관 정보 변경")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<Integer> updateOptTerms(@RequestBody TermsAgree termsAgree, HttpServletRequest req) throws Exception
	{
		int result = 0;

		// 선택약관 정보를 변경한다.
		if(YappUtil.isEmpty(SessionKeeper.getCntrNo(req))){
			termsAgree.setUserId(SessionKeeper.getSdata(req).getUserId());
			result = userService.updateOptTermsKt(termsAgree);

		}else{
			termsAgree.setCntrNo(SessionKeeper.getCntrNo(req));
			result = userService.updateOptTerms(termsAgree);
		}

		return new ResultInfo<>(result);
	}

	@RequestMapping(value = "/user/auth/sendmail", method = RequestMethod.POST)
	@ApiOperation(value="보호자에게 인증메일 발송", notes="메일인증키 반환")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<String> sendAuthMail(@RequestBody AdultAgree adultAgree, HttpServletRequest req) throws Exception
	{
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		adultAgree.setCntrNo(loginCntrNo);

		// 메일 인증키 생성
		String mailKey = UUID.randomUUID().toString().replaceAll("-", "");
		adultAgree.setMailKey(mailKey);
		String parentNm = adultAgree.getParentsNm();
		adultAgree.setParentsNm(YappUtil.blindNameToName(parentNm, 2));

		// 발송정보 추가
		userService.insertAdultAgree(adultAgree);

		ThunderAutoMailSender tms = new ThunderAutoMailSender();
		ThunderAutoMail tm = new ThunderAutoMail();
		//tm.setThunderMailURL("info.kt.com");
		tm.setThunderMailURL("ems.olleh.com:8080");
		tm.setAutomailID("841");
		//tm.setMailTitle("안녕하세요? "+adultAgree.getSndUserNm() +" 고객님께서 보내신 Y데이터박스 가입동의 메일입니다.");
		tm.setMailTitle("");
		tm.setMailContent("");
		//tm.setMailContent(adultAgree.getParentsNm() +"("+ adultAgree.getSndUserTel() +")");
		tm.setSenderMail("Y_Databox@kt.com");
		tm.setSenderName("Y박스");
		tm.setEmail(adultAgree.getEmailAddr());
		String oneToOneText = "[$name]Ð"+adultAgree.getSndUserNm() +"æ[$fname]Ð"+adultAgree.getSndUserNm() +"æ[$phone]Ð"+ adultAgree.getSndUserTel() +"æ[$tname]Ð"+parentNm+"æ[$mailkey]Ð"+mailKey;
		tm.setOnetooneInfo(oneToOneText);
		tm.setCustomerID("");
		String result = tms.send(tm);
		logger.info("======================= sndMail ==========================");
		logger.info(result);
		logger.info("==========================================================");
		if(result!=null && !result.equals("-100")){
			String errMsg = "";
			if(result.equals("10")){
				errMsg="필수 파라미터 미입력";
			}else if(result.equals("20")){
				errMsg="썬더메일 연결 실패";
			}else if(result.equals("30")){
				errMsg="연동 데이터 입력 실패";
			}else if(result.equals("90")){
				errMsg="기타";
			}
			String cntrNo = SessionKeeper.getCntrNo(req);
			String userId = null; //220406

			if(SessionKeeper.getSdata(req) != null){
				userId = SessionKeeper.getSdata(req).getUserId().trim();
			}

			String restApiUrl = YappUtil.nToStr(req.getRequestURL());
			String ipAddr = req.getRemoteHost();
			ApiError apiError = new ApiError();
			apiError.setCntrNo(cntrNo);
			apiError.setUserId(userId); //220406
			apiError.setCallerIpAddr(ipAddr);
			apiError.setErrMsg(errMsg);
			apiError.setErrMsgDetail(errMsg);
			apiError.setApiUrl(restApiUrl);
			apiError.setMsgTypeCd("EMS_ERROR");
			apiError.setErrCd(result);
			histService.insertErrControl(apiError, req);
		}

		return new ResultInfo<>(mailKey);
	}

	@RequestMapping(value = "/user/auth/checkmail", method = RequestMethod.GET)
	@ApiOperation(value="보호자 인증 정보 조회")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<AdultAgree> checkAuthMail(@RequestParam String mailKey, HttpServletRequest req) throws Exception
	{
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		AdultAgree adultAgree = new AdultAgree();
		if(mailKey == null || mailKey.equals("")){
			adultAgree = userService.getAdultAgreeCntrNo(loginCntrNo);
		}else{
			adultAgree = userService.getAdultAgree(mailKey);
		}
		return new ResultInfo<>(adultAgree);
	}

	@RequestMapping(value = "/na/user/auth/appr", method = RequestMethod.GET)
	@ApiOperation(value="보호자 승인")
	public String apprAuthMail(@RequestParam String mailKey, HttpServletRequest req) throws Exception
	{
		AdultAgree adultAgree = userService.getAdultAgree(mailKey);
		if ( adultAgree == null )
			return "<script>alert('"+cmnService.getMsg("ERR_NO_MAIL")+"');window.open('about:blank','_self').close();</script>";	// 보호자 동의정보가 없습니다.
		else if ( YappUtil.isY(adultAgree.getMailAgreeYn()) == true )
			return "<script>alert('"+cmnService.getMsg("ERR_MAIL_APPR")+"');window.open('about:blank','_self').close();</script>";	// 이미 인증된 동의정보 입니다.

		int uptCnt = userService.apprAdultAgree(mailKey);
		if ( uptCnt > 0 )
			return "<script>alert('"+cmnService.getMsg("MSG_MAIL_APPR")+"');window.open('about:blank','_self').close();</script>";	// 정상적으로 인증 처리 되었습니다.
		else
			return "<script>alert('"+cmnService.getMsg("ERR_FAIL_MAIL_APPR")+"');window.open('about:blank','_self').close();</script>";	// 인증 처리에 실패했습니다.

	}

	@RequestMapping(value = "/user/setting", method = RequestMethod.POST)
	@ApiOperation(value="개인 설정 변경", notes="푸시, 마케팅, 조르기 수신여부 설정")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<UserInfo> updateUserSetting(@RequestBody UserInfoSettingReq userInfo, HttpServletRequest req) throws Exception
	{
		UserInfo rtnUserInfo = new UserInfo();
		String cntrNo = SessionKeeper.getCntrNo(req);
		String userId = null;

		UserInfo loginUserKtInfo =null;

		if(SessionKeeper.getSdata(req) != null){
			userId = SessionKeeper.getSdata(req).getUserId().trim();
			loginUserKtInfo = userService.getYappUserKtInfo(userId);
		}

		if(userInfo != null && loginUserKtInfo != null){
			rtnUserInfo = userService.updateUserKtSettingInfo(YappCvtUtil.cvt(userInfo, loginUserKtInfo));
		}

		//kt회선
		if(!YappUtil.isEmpty(cntrNo)){

			UserInfo loginUserInfo = userService.getYappUserInfo(cntrNo);

			logger.info("loginUserInfo================================>"+loginUserInfo);

			String mobileNoSt = loginUserInfo.getMobileNo();
			String reqRcvYnSt = loginUserInfo.getReqRcvYn();
			String reqRcvYnTmp = userInfo.getReqRcvYn();

			// 설정정보 갱신

			if(userInfo != null && loginUserInfo != null){
				rtnUserInfo = userService.updateUserSettingInfo(YappCvtUtil.cvt(userInfo, loginUserInfo));
			}


			logger.info("rtnUserInfo================================>" + rtnUserInfo.toString());


			try{
				if(!reqRcvYnSt.equals(reqRcvYnTmp)){
					redisComponent.put(RedisComponent.MOBILE_NO_PFX_FRD + mobileNoSt, RedisComponent.KEY2_REQ_RCV_YN, reqRcvYnTmp);
				}
			}catch(RuntimeException e){
				logger.error("============= REDIS 접속이상 =============== : 조르기 수신여부 update 불가");
				histService.insertRedisErr(req);
				//e.printStackTrace();
			}catch(Exception e){
				logger.error("============= REDIS 접속이상 =============== : 조르기 수신여부 update 불가");
				histService.insertRedisErr(req);
				//e.printStackTrace();
			}
		}

		//220620 마스킹 모바일번호 추가
		rtnUserInfo.setMaskingMobileNo(YappUtil.blindMidEndMobileNo(rtnUserInfo.getMobileNo()));
		//220620 userinfo 필드 암호화
		rtnUserInfo = cmnService.aesEncUserInfoField(rtnUserInfo, req);

		logger.info("rtnUserInfo : " + rtnUserInfo);

		return new ResultInfo<>(rtnUserInfo);
	}

	@ApiOperation(value="YAPP 가입정보 조회")
	@RequestMapping(value = "/user/joininfo", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<JoinInfoResp> getJoinInfoList(@RequestBody List<String> paramMobileNoList,@RequestParam String deviceId, HttpServletRequest req) throws Exception
	{
		JoinInfoResp joinInfoResp = new JoinInfoResp();
		List<String> rtnMobileList = new ArrayList<>();
		for ( String mobileNoTmp : paramMobileNoList ){
			rtnMobileList.add(mobileNoTmp.replaceAll("'", "").replaceAll("\"", ""));
		}

		String redisPassYn = "N";

		try{
			// redis 작동여부를 임의적으로 조회해서 상태를 알아봄.
			String test = redisComponent.get(RedisComponent.MOBILE_NO_PFX_FRD + "00011112222", RedisComponent.KEY2_DBOX_STATUS);
		}catch(RuntimeException e){
			redisPassYn = "Y";
			logger.error("============= REDIS 접속이상 =============== :  친구목록 redis DB에서 조회불가");
			histService.insertRedisErr(req);
			//e.printStackTrace();
		}catch(Exception e){
			redisPassYn = "Y";
			logger.error("============= REDIS 접속이상 =============== :  친구목록 redis DB에서 조회불가");
			histService.insertRedisErr(req);
			//e.printStackTrace();
		}
		List<JoinInfo> rtnDataList = new ArrayList<>();

		if(redisPassYn.equals("N")){
			for ( String mobileNo : rtnMobileList )
			{
				JoinInfo joinInfo = new JoinInfo();
				joinInfo.setMobileNo(mobileNo);
				String dboxStatus = redisComponent.get(RedisComponent.MOBILE_NO_PFX_FRD + mobileNo, RedisComponent.KEY2_DBOX_STATUS);
				String reqRcvYn = redisComponent.get(RedisComponent.MOBILE_NO_PFX_FRD + mobileNo, RedisComponent.KEY2_REQ_RCV_YN);
				String yappJoinYn = "N";
				String dboxStatusTmp = "G0002";
				String reqRcvYnTmp = "N";
				if ( YappUtil.isNotEmpty(dboxStatus) ) {
					logger.info("======================= redisComponent ==========================");
					logger.info(mobileNo+" / "+ dboxStatus + " / "+ reqRcvYn);
					logger.info("======================= redisComponent ==========================");
					yappJoinYn = EnumYn.C_Y.getValue();
					dboxStatusTmp = dboxStatus;
					reqRcvYnTmp = reqRcvYn;
				}
				joinInfo.setYappJoinYn(yappJoinYn);
				// TODO 선물가능여부 세팅
				joinInfo.setDboxStatus(dboxStatusTmp);
				joinInfo.setReqRcvYn(reqRcvYnTmp);
				rtnDataList.add(joinInfo);
			}
		}else{
			// 가입정보 조회
			if(rtnMobileList.size() > 0){
				List<JoinInfo> joinMobileNoList = userService.getJoinInfoList(rtnMobileList);

				for ( String mobileNo : rtnMobileList )
				{
					JoinInfo joinInfo = new JoinInfo();
					joinInfo.setMobileNo(mobileNo);
					String yappJoinYn = "N";
					String dboxStatus = "G0002";
					String reqRcvYn = "N";
					for ( int i = 0; i < joinMobileNoList.size(); i++ ){
						if(mobileNo.equals(joinMobileNoList.get(i).getMobileNo())){
							yappJoinYn = "Y";
							dboxStatus = "G0001";
							reqRcvYn = joinMobileNoList.get(i).getReqRcvYn();
						}
					}
					joinInfo.setYappJoinYn(yappJoinYn);
					// TODO 선물가능여부 세팅
					joinInfo.setDboxStatus(dboxStatus);
					joinInfo.setReqRcvYn(reqRcvYn);
					rtnDataList.add(joinInfo);
				}
			}

		}

		joinInfoResp.setJoinInfoList(rtnDataList);

		// 최근 선물한 친구목록 조회
		// 사용자의 단말기 ID 정보를 가지고와서 어플에서 보내주는 단말기 ID 체크하여 최근 선물한 친구목록 조회
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		JoinInfo joinInfo = userService.getDeviceIdInfo(loginCntrNo);
		String srchDeviceId = joinInfo.getDeviceId();
		String srchDeviceaChgDt = joinInfo.getDeviceChgDt();
		String giftDataYn = "N";
		if(deviceId != null && !deviceId.equals("")){
			if(srchDeviceId.equals("")){
				userService.updateDeviceIdInfo(deviceId, loginCntrNo);
				giftDataYn = "Y";
			}else {
				if(srchDeviceId.equals(deviceId)){
					giftDataYn = "Y";
				}else{
					userService.updateDeviceIdInfo(deviceId, loginCntrNo);
					giftDataYn = "N";
				}
			}
		}else{
			giftDataYn = "Y";
		}

		if(giftDataYn.equals("Y")){
			GiftData giftData = new GiftData();
			giftData.setSndCntrNo(loginCntrNo);
			giftData.setDeviceChgDt(srchDeviceaChgDt);
			giftData.setLimitCnt(5);
			joinInfoResp.setGiftDataList(giftService.getGiftDataList(giftData));
		}
		return new ResultInfo<>(joinInfoResp);
	}

	@ApiOperation(value="초대하기 정보 추가")
	@RequestMapping(value = "/user/invt", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<Integer> insertInvtInfo(@RequestBody Invitation paramObj, HttpServletRequest req) throws Exception
	{
		paramObj.setCntrNo(SessionKeeper.getCntrNo(req));
		paramObj.setUserNm(SessionKeeper.getCntrInfo(req).getUserNm());

		// 초대하기 이력 추가
		return new ResultInfo<>(userService.insertInvt(paramObj));
	}

	@ApiOperation(value="사용자 review 정보 조회")
	@RequestMapping(value = "/user/reviewinfo", method = RequestMethod.GET)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<ReviewInfo> getReviewInfo(HttpServletRequest req) throws Exception
	{
		String cntrNo = SessionKeeper.getCntrNo(req);

		ReviewInfo reviewReq = new ReviewInfo();
		reviewReq.setCntrNo(cntrNo);

		return new ResultInfo<ReviewInfo>(userService.getUserReviewInfo(reviewReq));
	}

	@ApiOperation(value="사용자 review 정보 업데이트")
	@RequestMapping(value = "/user/reviewupdate", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<String> setReviewInfo(@ApiParam(value="사용자 review 응답") @RequestParam String reviewYn, HttpServletRequest req) throws Exception
	{
		ResultInfo<String> resultInfo = new ResultInfo<String>();
		String cntrNo = SessionKeeper.getCntrNo(req);
		String userId = null;

		if(SessionKeeper.getSdata(req) != null){
			userId = SessionKeeper.getSdata(req).getUserId().trim();
		}

		ReviewInfo reviewReq = new ReviewInfo();
		reviewReq.setReviewYn(reviewYn);
		reviewReq.setUserId(userId);

		//220329 kt회선만 user테이블 업데이트
		if(!YappUtil.isEmpty(cntrNo)){
			reviewReq.setCntrNo(cntrNo);
			userService.setUserReviewInfo(reviewReq);
		}

		//220329 kt id 기준 사용자 테이블 비kt회선/kt회선 모두 업데이트
		userKtService.setUserKtReviewInfo(reviewReq);

		return resultInfo;
	}


	@RequestMapping(value = "/na/user/snslogin", method = RequestMethod.POST)
	@ApiOperation(value="SNS 로그인")
	@ApiImplicitParams({ @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<LoginMobileResp> snsLogin(String snsId, String snsType, String snsToken, String iosAuthCode, HttpServletRequest req) throws Exception
	{
		ResultInfo<LoginMobileResp> resultInfo = new ResultInfo<>();
		LoginMobileResp respResult = new LoginMobileResp();
		resultInfo.setResultData(respResult);
		String appVrsn = req.getHeader("appVrsn");
		String osTp = req.getHeader("osTp");
		try{
			AppInfo appinfo = cmsService.getAppInfo(osTp);
			if(YappUtil.isEmpty(appinfo) || "N".equals(appinfo.getSnsLoginYn())){
				throw new BadPaddingException();
			}
			logger.info("snsType/snsToken/iosAuthCode==================>" + snsType + "/"+ snsToken + "/"+ iosAuthCode + "/");
			logger.info("snsId/osTp/appVrsn==================>" + snsId + "/"+ osTp + "/"+ appVrsn + "/");
			String decSnsId  = appEncryptUtils.aesDec128(snsId, osTp+appVrsn);
			logger.info("decSnsId==================>" + decSnsId);
			String cntrNo = "";
			String mobileNo = "";
			SnsInfo snsInfo = userService.getSnsInfo(decSnsId, snsType);
			if(YappUtil.isNotEmpty(snsInfo)){
				cntrNo = snsInfo.getCntrNo();
				SoapResponse184 resp_184 = shubService.callFn184(cntrNo);
				mobileNo = resp_184.getMobileNo();

				SoapResponse139 resp = shubService.callFn139(mobileNo, true);
				ContractInfo cntrInfo = resp.getCntrInfo();

				if(YappUtil.isNotEmpty(snsType) && YappUtil.isNotEmpty(cntrInfo) && YappUtil.isNotEmpty(cntrInfo.getUserInfo())){
					cntrInfo.getUserInfo().setSnsType(snsType);
				}
				List<ContractInfo> cntrInfoList = new ArrayList<>();
				cntrInfoList.add(cntrInfo);

				respResult.setCntrInfo(cntrInfo);

				boolean existUser = false;
				if (resp.getCntrInfo().getUserInfo() == null){
					existUser = false;
					UserInfo userInfo = new UserInfo();
					userInfo.setJoinStatus("G0002");
					userInfo.setAuthYn("N");
					userInfo.setMobileNo(cntrInfo.getMobileNo());
					userInfo.setCntrNo(cntrInfo.getCntrNo());
					cntrNo = cntrInfo.getCntrNo();
					resp.getCntrInfo().setUserInfo(userInfo);
				} else {
					existUser = true;
				}

				// dup ID 생성
				String dupId = UUID.randomUUID().toString();
				String tmpSessionKey = joinYsid(cntrInfo.getCntrNo(), cntrInfo.getMobileNo(), " ", existUser, dupId, "");
				String encSessionKey = "";
				try {
					encSessionKey = keyFixUtil.encode(tmpSessionKey);
				} catch (RuntimeException e) {
					// TODO: handle exception
					throw new BadPaddingException();
				} catch (Exception e) {
					// TODO: handle exception
					throw new BadPaddingException();
				}
				respResult.setYsid(encSessionKey);
				if(existUser){
					userService.updateDupIdInfo(dupId, cntrInfo.getCntrNo(), osTp, "", appVrsn);
				}
				// 세션에 저장
				SessionKeeper.setSessionId(req,encSessionKey);
				SessionKeeper.addToReq(req, SessionKeeper.KEY_DUP_ID, dupId);
				SessionKeeper.addToReq(req, SessionKeeper.KEY_CNTR_INFO, YappCvtUtil.cvt(cntrInfo, new SessionContractInfo()));
				SessionKeeper.addToReq(req, SessionKeeper.KEY_CNTR_LIST, YappCvtUtil.cvtToSessionCntrInfo(cntrInfoList));
				if(SessionKeeper.getSdata(req) != null){
					SessionKeeper.getSdata(req).setMobileNo(cntrInfo.getMobileNo());
					SessionKeeper.getSdata(req).setExistUser(existUser);
				}
			}else{
				respResult = null;
			}


		}catch(BadPaddingException e){
			throw new YappException("CHECK_MSG", cmnService.getMsg("ERR_NO_LOGIN"));
		}catch(Exception e){
			throw new YappException("CHECK_MSG", cmnService.getMsg("ERR_NO_LOGIN"));
		}

		return resultInfo;
	}

	@ApiOperation(value="사용자 선호도 정보 조회")
	@RequestMapping(value = "/user/prflist", method = RequestMethod.GET)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<PrfMainViewResp> getPreferenceList(HttpServletRequest req) throws Exception
	{
		ResultInfo<PrfMainViewResp> resultInfo = new ResultInfo<PrfMainViewResp>();
		PrfMainViewResp prfMainViewResp = new PrfMainViewResp();
		resultInfo.setResultData(prfMainViewResp);
		String cntrNo = SessionKeeper.getCntrNo(req);
		List<PreferenceInfo> prfList = new ArrayList<PreferenceInfo>();

		//220406 kt회선 비kt회선 분기
		if(!YappUtil.isEmpty(cntrNo)){
			prfList = userService.getPreferenceList(cntrNo);

		}else{
			if(SessionKeeper.getSdata(req) != null){
				String userId = SessionKeeper.getSdata(req).getUserId().trim();
				prfList = userKtService.getPreferenceListKt(userId);
			}
		}

		if(prfList.isEmpty()){
			logger.info("prfList : "+prfList);
			logger.info("선호도 정보가 없습니다.");
			prfList=null;
		}else{
			logger.info("prfList : "+prfList);
		}

		GrpCode prefrenceLimit = cmsService.getCodeNm("PRF_CODE", "P0001");

		prfMainViewResp.setPrfList(prfList);
		prfMainViewResp.setPrfCnt(prefrenceLimit.getCodeNm());

		return resultInfo;
	}


	@ApiOperation(value="선호도 정보 등록")
	@RequestMapping(value = "/user/prfupdate", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<PreferenceInfo> updatePreference(@RequestBody UserPreferenceReq userPreferenceReq, HttpServletRequest req) throws Exception
	{
		ResultInfo<PreferenceInfo> resultInfo = new ResultInfo<>();

		String cntrNo = SessionKeeper.getCntrNo(req);
		String userId = null;

		if(SessionKeeper.getSdata(req) != null){
			userId = SessionKeeper.getSdata(req).getUserId().trim();
		}

		String prfFlag = userPreferenceReq.getPrfFlag();
		List<Integer> paramPrfSeqList = userPreferenceReq.getParamPrfSeqList();

		try {

			//220406 kt회선 비kt회선 분기
			if(!YappUtil.isEmpty(cntrNo)){

				userService.deletePreferenceInfo(cntrNo);
				userService.updateUserPreferenceInfo(prfFlag, cntrNo);

			}else{
				userKtService.deletePreferenceInfoKt(userId);
				userKtService.updateUserPreferenceInfoKt(prfFlag, userId);
			}


			//211012 선호도 파라미터변조 count 검증 추가
			GrpCode prefrenceLimit = cmsService.getCodeNm("PRF_CODE", "P0001");
			int count = YappUtil.toInt(prefrenceLimit.getCodeNm());
			//

			if(YappUtil.isNotEmpty(paramPrfSeqList)){

				//211013 선호도 파라미터변조 count 검증 추가
				if(paramPrfSeqList.size() > count ){
					throw new YappException("CHECK_MSG", cmnService.getMsg("ERR_SYS_ERROR"));

				}else{
					for (int prfSeq : paramPrfSeqList) {
						if(!YappUtil.isEmpty(cntrNo)){
							userService.insertPreferenceInfo(prfSeq, cntrNo);
						}else{
							if(!YappUtil.isEmpty(userId)){
								userKtService.insertPreferenceInfoKt(prfSeq, userId);
							}
						}
					}
				}
			}

		} catch (RuntimeException e) {
			// TODO: handle exception
			throw new YappException("CHECK_MSG", cmnService.getMsg("ERR_SYS_ERROR"));
		} catch (Exception e) {
			// TODO: handle exception
			throw new YappException("CHECK_MSG", cmnService.getMsg("ERR_SYS_ERROR"));
		}

		return resultInfo;
	}

	/**
	 * ysid 값 조립 후 리턴
	 * @return ysid
	 */
	private String joinYsid(String cntrNo, String mobileNo, String userId, boolean existUser, String dupId, String memStatus) {
		logger.debug("====================== ysid create date ========================");
		logger.debug("joinYsid -> cntrNo : "+cntrNo);
		logger.debug("joinYsid -> mobileNo : "+mobileNo);
		logger.debug("joinYsid -> userId : "+userId);
		logger.debug("joinYsid -> existUser : "+existUser);
		logger.debug("joinYsid -> dupId : "+dupId);
		logger.debug("YappUtil -> getCurDate : "+YappUtil.getCurDate());
		logger.debug("joinYsid -> memStatus : "+memStatus);
		logger.debug("====================== ysid create date ========================");

		return new StringBuilder().append(cntrNo).append(SP)
				.append(mobileNo).append(SP)
				.append(userId).append(SP)
				.append(existUser).append(SP)
				.append(dupId).append(SP)
				.append(YappUtil.getCurDate()).append(SP)
				.append(memStatus).toString();
	}

	/**
	 * 20210712 2주 미사용시 로그아웃 처리개선
	 *  세션ID 갱신
	 * @param req
	 * @return enc(ysid)
	 * @throws Exception
	 */
	private String reBuildYsid(HttpServletRequest req, String userId) throws Exception{
		//20210712 2주 미사용시 로그아웃 처리개선 시작
		// 헤더의 세션ID
		String sessionId = req.getHeader(SessionKeeper.KEY_SESSION_ID);

		// ysid 복호화처리
		boolean sessExistUser = false;
		String decSessionKey = "";
		try{
			decSessionKey = keyFixUtil.decode(sessionId);
		}catch(RuntimeException e){
			logger.info("단말기 정보가 제대로 넘어오지 않았습니다.[ YSID 복호화 오류 ] " + sessionId);
			throw new YappAuthException("410","로그인 정보가 없습니다.");
		}catch(Exception e){
			logger.info("단말기 정보가 제대로 넘어오지 않았습니다.[ YSID 복호화 오류 ] " + sessionId);
			throw new YappAuthException("410","로그인 정보가 없습니다.");
		}

		String[] tmp = decSessionKey.split("\\|\\|");
		String[] tmp2 = decSessionKey.split("\\|\\|");

		/** 2020.02.20 : PLAN-357 : 미사용 기간 2주 초과 시, 로그아웃 처리 로직 추가 */
		if(tmp == null ||tmp.length < 6 || tmp.length > 7) {
			logger.info("단말기 정보가 제대로 넘어오지 않았습니다.[ YSID length ERROR ]");
			throw new YappAuthException("410","로그인 정보가 없습니다.");

		}else{

			tmp = new String[7];
			tmp[0] = tmp2[0];
			tmp[1] = tmp2[1];
			tmp[2] = userId;
			tmp[3] = tmp2[3];
			tmp[4] = tmp2[4];
			tmp[5] = tmp2[5];
			tmp[6] = "G0002";
		}

		sessExistUser = Boolean.parseBoolean(tmp[3]);

		if(logger.isInfoEnabled()) {
			logger.info("[0] = " + tmp[0]);
			logger.info("[1] = " + tmp[1]);
			logger.info("[2] = " + tmp[2]);
			logger.info("[3] = " + tmp[3]);
			logger.info("[4] = " + tmp[4]);
			logger.info("[5] = " + tmp[5]);
			logger.info("[6] = " + tmp[6]);
			logger.info("sessExistUser = " + sessExistUser);
		}

		String tmpSessionKey = joinYsid(tmp[0], tmp[1], tmp[2], sessExistUser, tmp[4], "G0001");
		String encSessionKey = "";

		try {
			encSessionKey = keyFixUtil.encode(tmpSessionKey);

		} catch (RuntimeException e) {
			// TODO: handle exception
			throw new BadPaddingException();
		} catch (Exception e) {
			// TODO: handle exception
			throw new BadPaddingException();
		}

		//20210712 2주 미사용시 로그아웃 처리개선 끝

		return encSessionKey;
	}

}