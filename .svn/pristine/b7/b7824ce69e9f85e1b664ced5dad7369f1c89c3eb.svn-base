package com.kt.yapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kt.yapp.common.repository.CommonDao;
import com.kt.yapp.domain.AppIosTracking;
import com.kt.yapp.domain.AttachFile;
import com.kt.yapp.domain.AuthFail;
import com.kt.yapp.domain.ContractInfo;
import com.kt.yapp.domain.DeviceTokenInfo;
import com.kt.yapp.domain.ErrMsg;
import com.kt.yapp.domain.GuideMsg;
import com.kt.yapp.domain.JoinInfo;
import com.kt.yapp.domain.KmcAuthInfo;
import com.kt.yapp.domain.SendSmsLogInfo;
import com.kt.yapp.domain.SessionContractInfo;
import com.kt.yapp.domain.SmsAccessInfo;
import com.kt.yapp.domain.TermsAgree;
import com.kt.yapp.domain.UserInfo;
import com.kt.yapp.domain.req.UserInfoReq;
import com.kt.yapp.em.EnumRsltCd;
import com.kt.yapp.exception.YappAuthException;
import com.kt.yapp.exception.YappException;
import com.kt.yapp.soap.response.SoapResponse139;
import com.kt.yapp.util.AppEncryptUtils;
import com.kt.yapp.util.KeyFixUtil;
import com.kt.yapp.util.SessionKeeper;
import com.kt.yapp.util.YappCvtUtil;
import com.kt.yapp.util.YappUtil;

@Service
public class CommonService 
{
	private static final Logger logger = LoggerFactory.getLogger(CommonService.class);
	
	@Autowired
	private CommonDao cmnDao;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private ShubService shubService;
	
	@Autowired
	private KeyFixUtil keyFixUtil;
	@Autowired
	private UserService userService;
	@Autowired
	private KosService kosService;
	@Autowired
	private AppEncryptUtils appEncryptUtils;
	
	/**
	 * 안내 메시지를 조회한다.
	 */
	public GuideMsg getGuideMsgInfo(String msgCd) throws Exception
	{
		GuideMsg paramObj = new GuideMsg();
		paramObj.setMsgCd(msgCd);
		
		return cmnDao.selectOne("mybatis.mapper.common.getGuideMsgInfo", paramObj);
	}
	
	public <T> T getData(String sqlId, Map<String, Object> paramMap)
	{
		return cmnDao.selectOne(sqlId, paramMap);
	}

	/**
	 * 인증 에러 카운트를 조회한다.
	 */
	public AuthFail getAuthFailCnt(AuthFail paramObj)
	{
		return cmnDao.selectOne("mybatis.mapper.common.getAuthFailCnt", paramObj);
	}
	
	/**
	 * 인증 에러 카운트를 업데이트 한다.
	 */
	public int updateAuthFailCnt(AuthFail paramObj)
	{
		return cmnDao.insert("mybatis.mapper.common.updateAuthFailCnt", paramObj);
	}

	/**
	 * 첨부파일 정보를 조회한다.
	 */
	public AttachFile getAttachFileInfo(String fileId)
	{
		AttachFile paramObj = new AttachFile();
		paramObj.setFileId(fileId);
		
		return cmnDao.selectOne("mybatis.mapper.common.getAttachFile", paramObj);
	}
	
	/**
	 * 입력된 계약번호와 세션에 저장되어 있는 계약번호가 일치하는지 체크한다.
	 */
	public void checkLoginCntrNo(String cntrNo, String mobileNo) throws Exception
	{
		ContractInfo cntrInfo = shubService.callFn139(mobileNo).getCntrInfo();
		
		if ( YappUtil.isNotEq(cntrNo, cntrInfo.getCntrNo()) )
			throw new YappException("CHECK_MSG",getMsg("ERR_NOT_SAME_LOGIN"));	// 로그인된 정보와 일치하지 않습니다.
	
	}

	/**
	 * SMS 전송 정보를 저장한다.
	 */
	public int insertSmsSendInfo(SmsAccessInfo paramObj)
	{
		return cmnDao.insert("mybatis.mapper.common.insertSmsSendInfo", paramObj);
	}

	/**
	 * SMS 전송 정보 건수를 조회한다.
	 */
	public SmsAccessInfo getSmsSendInfoCount(String accessIpAddr)
	{
		return cmnDao.selectOne("mybatis.mapper.common.getSmsSendInfoCount", YappUtil.makeParamMap("accessIpAddr", accessIpAddr));
	}

	/**
	 * 메시지를 조회한다.
	 */
	public String getMsg(String msgCd)
	{
		return getMsg(msgCd, null);
	}
	public String getMsg(String msgCd, Map<String, Object> replaceMapData)
	{
		String rtnMsg = null;
		try {
			rtnMsg = messageSource.getMessage(msgCd, null, null);
			// 치환 항목이 있는경우 치환한다.
			if ( YappUtil.isNotEmpty(replaceMapData) )
				rtnMsg = YappUtil.replaceMapData(rtnMsg, replaceMapData);
		} catch (RuntimeException e) {
			logger.error("RuntimeException CommonService getMsg ERROR : "+e);
		}catch (Exception e) {
			logger.error("Exception CommonService getMsg ERROR");
		}
		
		return rtnMsg;
	}

	/**
	 * 단말기 DeviceToken ID 를 조회한다.
	 */
	public DeviceTokenInfo getDeviceTokenIdInfo(String cntrNo)
	{
		// 단말기 DeviceToken ID  를 조회
		DeviceTokenInfo deviceInfo = cmnDao.selectOne("mybatis.mapper.common.getDeviceTokenInfo", YappUtil.makeParamMap("cntrNo", cntrNo));
		
		return deviceInfo;
	}
	
	/**
	 * 유입경로를 저장한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int updateRouteLink(String linkType) throws RuntimeException
	{
		Map<String, Object> paramObj = YappUtil.makeParamMap(new String[]{"linkType"}, new String[]{linkType});
		
		int uptCnt = cmnDao.update("mybatis.mapper.common.updateRouteLink", paramObj);
		
		return uptCnt;
	}

	/**
	 * 단말기 DeviceToken ID 를 저장한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int updateDeviceToken(String deviceToken, String cntrNo, String osVrsn, String appVrsn) throws Exception
	{
		Map<String, Object> paramObj = YappUtil.makeParamMap(new String[]{"deviceToken", "cntrNo", "osVrsn", "appVrsn"}, new String[]{deviceToken, cntrNo, osVrsn, appVrsn});
		
		int uptCnt = cmnDao.update("mybatis.mapper.common.updateDeviceToken", paramObj);
		
		return uptCnt;
	}
	
	
	/** 220412
	 * user kt테이블 단말기 DeviceToken ID 를 조회한다.
	 */
	public DeviceTokenInfo getDeviceTokenIdInfoKt(String userId)
	{
		// 단말기 DeviceToken ID  를 조회
		DeviceTokenInfo deviceInfo = cmnDao.selectOne("mybatis.mapper.common.getDeviceTokenIdInfoKt", YappUtil.makeParamMap("userId", userId));
		
		return deviceInfo;
	}
	
	@Transactional(rollbackFor = Throwable.class)
	public int updateDeviceTokenKt(String deviceToken, String userId, String osVrsn, String appVrsn, String osTp) throws Exception
	{
		Map<String, Object> paramObj = YappUtil.makeParamMap(new String[]{"deviceToken", "userId", "osVrsn", "appVrsn", "osTp"}, new String[]{deviceToken, userId, osVrsn, appVrsn, osTp});
		
		int uptCnt = cmnDao.update("mybatis.mapper.common.updateDeviceTokenKt", paramObj);
		
		return uptCnt;
	}
	
	@Transactional(rollbackFor = Throwable.class)
	public int updateDeviceTokenAll(String deviceToken, String cntrNo, String userId, String osVrsn, String appVrsn, String osTp) throws Exception
	{
		Map<String, Object> paramObj = YappUtil.makeParamMap(new String[]{"deviceToken", "cntrNo", "osVrsn", "appVrsn"}, new String[]{deviceToken, cntrNo, osVrsn, appVrsn});
		
		int uptCnt = cmnDao.update("mybatis.mapper.common.updateDeviceToken", paramObj);
		
		Map<String, Object> paramObjKt = YappUtil.makeParamMap(new String[]{"deviceToken", "userId", "osVrsn", "appVrsn", "osTp"}, new String[]{deviceToken, userId, osVrsn, appVrsn, osTp});
		
		cmnDao.update("mybatis.mapper.common.updateDeviceTokenKt", paramObjKt);
		
		return uptCnt;
	}
	
	public String getSearchMsg(String errMsgId, String msgData, String errMsgCd, String errStandardMsgCode) throws Exception
	{
		String rtnMsg = null;
		String errMsgCode = "";

		if(errMsgCd == null || errMsgCd.equals("")){
			errMsgCode = EnumRsltCd.C999.getRsltCd();
		}else{
			errMsgCode = errMsgCd;
		}

		try{
			Map<String, Object> paramObj = 
					YappUtil.makeParamMap(new String[]{"errMsgId", "errMsgCode"}, new Object[]{errMsgId, errMsgCode});
			ErrMsg errMsg = cmnDao.selectOne("mybatis.mapper.common.getSearchMsg", paramObj);
			if(errMsg!= null){
				// 연동에서 오는 메세지 바이패쓰
				if(errMsg.getErrMsgUseType().equals("P")){
					rtnMsg = msgData + " \n( CODE : " + errMsgCode + " )";
				// 연동에서 오는 메세지 중 가변적인것만 추출하여 DB에 있는 메세지와 함께 보여줌 
				}else if(errMsg.getErrMsgUseType().equals("S")){
					rtnMsg = msgData.replaceAll(errMsg.getErrMsgOrg(),"") + " " + errMsg.getErrMsg() + " \n( CODE : " + errMsgCode + " )";
				// DB메세지
				}else{
					if("300055".equals(errMsgCode)){
						rtnMsg = errMsg.getErrMsg();
					}else{
						rtnMsg = errMsg.getErrMsg()+ " \n( CODE : " + errMsgCode + " )";
					}
				}
			}else{
				if(errMsgId.equals("SHUB_ERR_MSG")){
					if("300055".equals(errMsgCode)){
						rtnMsg = msgData;
					}else{
						rtnMsg = msgData+ " \n( CODE : " + errMsgCode + ")";
					}
				}else{
					rtnMsg = messageSource.getMessage(errStandardMsgCode, null, null)+ " \n( CODE : " + errMsgCode + " )";
				}

				if (rtnMsg == null || rtnMsg.equals("")) {
					rtnMsg = getMsg("ERR_SYS_ERROR", null) + " \n( CODE : " + errMsgCode + " )";
				}
			}
		}catch (RuntimeException e){
			rtnMsg = getMsg("ERR_SYS_ERROR", null) + " \n( CODE : " + errMsgCode + " )";
		}catch (Exception e){
			rtnMsg = getMsg("ERR_SYS_ERROR", null) + " \n( CODE : " + errMsgCode + " )";
		}
		return rtnMsg;
	}
	
	/**
	 * KMC 인증 정보를 저장한다.
	 */
	public int insertKmcAuthInfo(KmcAuthInfo paramObj)
	{
		return cmnDao.insert("mybatis.mapper.common.insertKmcAuthInfo", paramObj);
	}

	/**
	 *KMC 인증 정보 를 조회한다.
	 */
	public KmcAuthInfo getKmcAuthInfo(KmcAuthInfo paramObj)
	{
		KmcAuthInfo kmcAuthInfo = cmnDao.selectOne("mybatis.mapper.common.getKmcAuthInfo", paramObj);
		
		return kmcAuthInfo;
	}
	
	/**
	 * SMS 문자메시지 발송 로그를 저장한다.
	 */
	public int insertSmsLogInfo(SendSmsLogInfo paramObj)
	{
		return cmnDao.insert("mybatis.mapper.common.insertSmsLogInfo", paramObj);
	}
	
	/**
	 * SMS 문자메시지 발송  후 통신 결과를 업데이트 한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int updateSmsLogInfo(SendSmsLogInfo paramObj) throws Exception
	{
		return cmnDao.update("mybatis.mapper.common.updateSmsLogInfo", paramObj);
	}
	
	/**210521
	 *사용자의 IOS앱 추적정보를 조회한다.
	 */
	public AppIosTracking getAppIosTrackingInfo(AppIosTracking paramObj)
	{
		AppIosTracking appIosTrackingInfo = cmnDao.selectOne("mybatis.mapper.common.getAppIosTrackingInfo", paramObj);
		
		return appIosTrackingInfo;
	}
	
	
	/**210521
	 * IOS앱 추적 정보를 저장한다.
	 */
	public int insertAppIosTrackingInfo(AppIosTracking paramObj)
	{
		return cmnDao.insert("mybatis.mapper.common.insertAppIosTrackingInfo", paramObj);
		
	}
	
	/**210521
	 * IOS앱 추적 정보를 UPDATE한다.
	 */
	public int updateAppIosTrackingInfo(AppIosTracking paramObj)
	{
		return cmnDao.insert("mybatis.mapper.common.updateAppIosTrackingInfo", paramObj);
		
	}
	
	/**210406
	 *비kt회선 사용자의 IOS앱 추적정보를 조회한다.
	 */
	public AppIosTracking getAppIosTrackingInfoKt(AppIosTracking paramObj)
	{
		AppIosTracking appIosTrackingInfo = cmnDao.selectOne("mybatis.mapper.common.getAppIosTrackingInfoKt", paramObj);
		
		return appIosTrackingInfo;
	}
	
	
	/**210406
	 * 비kt회선 IOS앱 추적 정보를 저장한다.
	 */
	public int insertAppIosTrackingInfoKt(AppIosTracking paramObj)
	{
		return cmnDao.insert("mybatis.mapper.common.insertAppIosTrackingInfoKt", paramObj);
		
	}
	
	/**210406
	 * 비kt회선 IOS앱 추적 정보를 UPDATE한다.
	 */
	public int updateAppIosTrackingInfoKt(AppIosTracking paramObj)
	{
		return cmnDao.insert("mybatis.mapper.common.updateAppIosTrackingInfoKt", paramObj);
		
	}
	
	public String loginCheck(HttpServletRequest req) throws Exception{
		String loginYn = "Y";
		
		// 헤더의 세션ID
		String sessionId = req.getHeader(SessionKeeper.KEY_SESSION_ID);
		String autoLoginYn = req.getHeader("autoLogin");
		
		String reqSessionId = (String) SessionKeeper.get(req, SessionKeeper.KEY_SESSION_ID);
		if(logger.isInfoEnabled()) {
			logger.info("sessionId      : " + sessionId);
			logger.info("autoLoginYn    : " + autoLoginYn);
			logger.info("server session : " + reqSessionId);
		}

		// 단말기에서 ysid가 안넘어오면 에러처리
		if(sessionId == null || sessionId.equals("")){
			return loginYn = "N";
		}
		
		// ysid 복호화처리
		boolean sessExistUser = false;
		String decSessionKey = "";
		try{
			decSessionKey = keyFixUtil.decode(sessionId);

		}catch(RuntimeException e){
			return loginYn = "N";
		}catch(Exception e){
			return loginYn = "N";
		}

		String[] tmp = decSessionKey.split("\\|\\|");
		String[] tmp2 = decSessionKey.split("\\|\\|");

		/** 2020.02.20 : PLAN-357 : 미사용 기간 2주 초과 시, 로그아웃 처리 로직 추가 */
		// if(tmp == null || tmp.length != 5){
		if(tmp == null ||tmp.length < 6 || tmp.length > 7) {
			return loginYn = "N";
			
		}else if(tmp.length == 6){
			
			tmp = new String[7];
			tmp[0] = tmp2[0];
			tmp[1] = tmp2[1];
			tmp[2] = tmp2[2];
			tmp[3] = tmp2[3];
			tmp[4] = tmp2[4];
			tmp[5] = tmp2[5];
			tmp[6] = "G0002";
		}

		JoinInfo joinInfo = new JoinInfo();
		
		if(YappUtil.isEmpty(tmp[0])||tmp[0].indexOf("ktid")!=-1){
			joinInfo = userService.getDupIdInfoKt(tmp[2]);
		}else{
			joinInfo = userService.getDupIdInfo(tmp[0]);
		}

		if(joinInfo != null){
			sessExistUser = true;
		}

		if(tmp[3] != null){
			if(tmp[3].equals("true")){
				sessExistUser = true;
			}
		}
		
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
		
		String autoLoginPassYn = "N";
		if(YappUtil.isEmpty(reqSessionId)){
			autoLoginPassYn = "Y";
		}else{
			if(!reqSessionId.equals(sessionId)){
				autoLoginPassYn = "Y";
			}
		}

		if(autoLoginPassYn.equals("Y")){
			if(autoLoginYn != null){
				//서버 세션에 정보가 없고 자동로그인(로그인유지)인 경우 단말에서 넘오는 정보를 서버 세션에 저장. 
				if(autoLoginYn.equals("true")){
					SessionKeeper.setSessionId(req,sessionId);
					if(SessionKeeper.getSdata(req) != null){
						SessionKeeper.getSdata(req).setUserId(tmp[2]);
						SessionKeeper.getSdata(req).setMobileNo(tmp[1]);
						SessionKeeper.getSdata(req).setExistUser(sessExistUser);
						SessionKeeper.getSdata(req).setMemStatus(tmp[6]);
					}
				}
			}
		}
		
		String mobileNo = null;
		String memStatus = null;
		
		if(SessionKeeper.getSdata(req) != null) {
			
			logger.info("SessionKeeper.getSdata(req) != null -> getMobileNo : "+SessionKeeper.getSdata(req).getMobileNo());
			logger.info("SessionKeeper.getSdata(req) != null -> getMemStatus : "+SessionKeeper.getSdata(req).getMemStatus());
			
			mobileNo = SessionKeeper.getSdata(req) == null ? null : SessionKeeper.getSdata(req).getMobileNo();
			memStatus = SessionKeeper.getSdata(req) == null ? null : SessionKeeper.getSdata(req).getMemStatus();
		}
		
		// 계약정보, 사용자 정보가 없으면 실행을 종료한다.  
		if ( YappUtil.isEmpty(mobileNo)
				|| YappUtil.isEmpty(memStatus)
				|| SessionKeeper.getSdata(req) == null 
				|| SessionKeeper.getSdata(req).isExistUser() == false ) 
		{
			return loginYn = "N";
		}
		
		String apiUrl = YappUtil.nToStr(req.getRequestURI());
		// 로그인 중복 체크로직.
		String passYn = "Y";
		if ( apiUrl.indexOf("/user/svcout") == -1 && apiUrl.indexOf("/user/logout") == -1 && apiUrl.indexOf("/user/join") == -1){
			passYn = "N";
		} 

		if ( apiUrl.indexOf("/user/joininfo") > -1){
			passYn = "N";
		}

		if(passYn.equals("N")){
			if(joinInfo != null){
				if(!tmp[4].equals(joinInfo.getDupId())){
					logger.info("session dup id = " + tmp[4]);
					logger.info("DB dup id = " + joinInfo.getDupId());
					logger.info("중복 로그인 불가. 다른 기기에서 로그인 되었습니다.");
					throw new YappAuthException("410","중복 로그인 불가. 다른 기기에서 로그인 되었습니다.");
				}
			}else{
				logger.info("DUP ID 정보가 없습니다.(해당 계약번호로 TB_USER에 정보가 없음.)");
				throw new YappAuthException("410","로그인 정보가 없습니다.");
			}
		}
		
		// 계약정보 재로딩
		SessionContractInfo cntrInfo = (SessionContractInfo) SessionKeeper.get(req, SessionKeeper.KEY_CNTR_INFO);
		if ( cntrInfo == null && mobileNo.indexOf("ktid")==-1) {
			SoapResponse139 resp = shubService.callFn139(mobileNo, false);
			SessionKeeper.addToReq(req, SessionKeeper.KEY_CNTR_INFO, YappCvtUtil.cvt(resp.getCntrInfo(), new SessionContractInfo()));
			loginYn = "Y";
		}
		
		return loginYn;
	}
	
	/** 220606
	 * KOS 가입제한 조회후, 가입정보에서 제거
	 */
	public List<ContractInfo> kosJoinLimit(List<ContractInfo> cntrList) throws Exception{
		
		List<ContractInfo> cntrLIstTmp = new ArrayList<ContractInfo>();

		if(!YappUtil.isEmpty(cntrList)){
						
			for(ContractInfo cntrInfo : cntrList){
				
				//220617 user테이블에 존재 회선일시 계약정보에 넣어줌
				UserInfo userInfo = userService.getYappUserInfo(cntrInfo.getCntrNo());
				
				if(!YappUtil.isEmpty(userInfo)){
					cntrLIstTmp.add(cntrInfo);
				}else{
					if(kosService.joinLimit(cntrInfo.getCntrNo())){
						cntrLIstTmp.add(cntrInfo);
					}
				}
			}
		}
		return cntrLIstTmp;
	}

	/** 220613
	 * 계약정보 리스트 암호화 파라미터 (모바일번호/생년월일)
	 */
	public List<ContractInfo> aesEncCntrInfoListField(List<ContractInfo> cntrList, HttpServletRequest req) throws Exception{
		String osTp = req.getHeader("osTp");
		String appVrsn = req.getHeader("appVrsn");
		
		if(!YappUtil.isEmpty(cntrList)){
						
			for(ContractInfo cntrInfo : cntrList){
				
				if(YappUtil.isEmpty(cntrInfo.getMaskingMobileNo())){
					cntrInfo.setMaskingMobileNo(YappUtil.blindMidEndMobileNo(cntrInfo.getMobileNo()));
				}
				
				String encMobileNo = appEncryptUtils.aesEnc128(cntrInfo.getMobileNo(), osTp + appVrsn);
				String encBirthDay = appEncryptUtils.aesEnc128(cntrInfo.getBirthDate(), osTp + appVrsn);
				
				cntrInfo.setMobileNo(encMobileNo);
				cntrInfo.setBirthDate(encBirthDay);
				
				
				if(!YappUtil.isEmpty(cntrInfo.getUserInfo())){
					UserInfo encUserInfo = aesEncUserInfoField(cntrInfo.getUserInfo(), req);
					cntrInfo.setUserInfo(encUserInfo);
				}
				
			}
		}
		return cntrList;
	}
	
	/** 220613
	 * 사용자정보 암호화 파라미터(모바일번호/생년월일/userId/이메일/이름)
	 */
	public UserInfo aesEncUserInfoField(UserInfo userInfo, HttpServletRequest req) throws Exception{
		String osTp = req.getHeader("osTp");
		String appVrsn = req.getHeader("appVrsn");
		
		if(!YappUtil.isEmpty(userInfo)){
			String encMobileNo = appEncryptUtils.aesEnc128(userInfo.getMobileNo(), osTp + appVrsn);
			String encBirthDay = appEncryptUtils.aesEnc128(userInfo.getBirthDay(), osTp + appVrsn);
			String encUserId = appEncryptUtils.aesEnc128(userInfo.getUserId(), osTp + appVrsn);
			String encEmail = appEncryptUtils.aesEnc128(userInfo.getEmail(), osTp + appVrsn);
			String encUserNm = appEncryptUtils.aesEnc128(userInfo.getUserNm(), osTp + appVrsn);
			
			userInfo.setMobileNo(encMobileNo);
			userInfo.setBirthDay(encBirthDay);
			userInfo.setUserId(encUserId);
			userInfo.setEmail(encEmail);
			userInfo.setUserNm(encUserNm);
			
			if(!YappUtil.isEmpty(userInfo.getTermsAgree())){
				if(!YappUtil.isEmpty(userInfo.getTermsAgree().getUserId())){
					TermsAgree termsAgree = aesEncTermsAgreeField(userInfo.getTermsAgree(), osTp,appVrsn);
					userInfo.setTermsAgree(termsAgree);
				}
			}
				
		}
		return userInfo;
	}
	
	/** 220620
	 * 계약정보 암호화 파라미터 (모바일번호/생년월일)
	 */
	public ContractInfo aesEncCntrInfoField(ContractInfo cntrInfo, HttpServletRequest req) throws Exception{
		String osTp = req.getHeader("osTp");
		String appVrsn = req.getHeader("appVrsn");
		
		if(!YappUtil.isEmpty(cntrInfo)){
						
			String encMobileNo = appEncryptUtils.aesEnc128(cntrInfo.getMobileNo(), osTp + appVrsn);
			String encBirthDay = appEncryptUtils.aesEnc128(cntrInfo.getBirthDate(), osTp + appVrsn);
			
			cntrInfo.setMobileNo(encMobileNo);
			cntrInfo.setBirthDate(encBirthDay);
			
			if(!YappUtil.isEmpty(cntrInfo.getUserInfo())){
				UserInfo encUserInfo = aesEncUserInfoField(cntrInfo.getUserInfo(), req);
				cntrInfo.setUserInfo(encUserInfo);
			}
				
		}
		return cntrInfo;
	}
	
	/** 220620
	 * 사용자정보 복호화 파라미터(모바일번호/생년월일/userId/이메일/이름)
	 */
	public UserInfoReq aesDecUserInfoReqField(UserInfoReq userInfo, HttpServletRequest req) throws Exception{
		String osTp = req.getHeader("osTp");
		String appVrsn = req.getHeader("appVrsn");
		
		if(!YappUtil.isEmpty(userInfo)){
			String decMobileNo = appEncryptUtils.aesDec128(userInfo.getMobileNo(), osTp + appVrsn);
			String decBirthDay = appEncryptUtils.aesDec128(userInfo.getBirthDay(), osTp + appVrsn);
			String decUserId = appEncryptUtils.aesDec128(userInfo.getUserId(), osTp + appVrsn);
			String decEmail = appEncryptUtils.aesDec128(userInfo.getEmail(), osTp + appVrsn);
			String decUserNm = appEncryptUtils.aesDec128(userInfo.getUserNm(), osTp + appVrsn);
			
			userInfo.setMobileNo(decMobileNo);
			userInfo.setBirthDay(decBirthDay);
			userInfo.setUserId(decUserId);
			userInfo.setEmail(decEmail);
			userInfo.setUserNm(decUserNm);
			
			if(!YappUtil.isEmpty(userInfo.getTermsAgree())){
				if(!YappUtil.isEmpty(userInfo.getTermsAgree().getUserId())){
					TermsAgree termsAgree = aesDecTermsAgreeField(userInfo.getTermsAgree(), osTp, appVrsn);
					userInfo.setTermsAgree(termsAgree);
				}
			}
				
		}
		return userInfo;
	}
	
	/**
	 * 220711
	 * 약관정보 암호화(userId)
	 * @param termsAgree
	 * @param osTp
	 * @param appVrsn
	 * @return
	 * @throws Exception
	 */
	public TermsAgree aesEncTermsAgreeField(TermsAgree termsAgree, String osTp, String appVrsn) throws Exception{
		
		if(!YappUtil.isEmpty(termsAgree.getUserId())){
			String encUserId = appEncryptUtils.aesEnc128(termsAgree.getUserId(), osTp + appVrsn);
			
			termsAgree.setUserId(encUserId);
		}
		
		return termsAgree;
	}
	
	/**
	 * 220711
	 * 약관정보 복호화(userId)
	 * @param termsAgree
	 * @param osTp
	 * @param appVrsn
	 * @return
	 * @throws Exception
	 */
	public TermsAgree aesDecTermsAgreeField(TermsAgree termsAgree, String osTp, String appVrsn) throws Exception{
		
		if(!YappUtil.isEmpty(termsAgree.getUserId())){
			String decUserId = appEncryptUtils.aesDec128(termsAgree.getUserId(), osTp + appVrsn);
			
			termsAgree.setUserId(decUserId);
		}
		
		return termsAgree;
	}
}
