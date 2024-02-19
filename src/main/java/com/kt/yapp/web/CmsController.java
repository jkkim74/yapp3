package com.kt.yapp.web;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kt.yapp.domain.AppInfo;
import com.kt.yapp.domain.EventAppl;
import com.kt.yapp.domain.GrpCode;
import com.kt.yapp.domain.Guide;
import com.kt.yapp.domain.Notice;
import com.kt.yapp.domain.NoticeMsg;
import com.kt.yapp.domain.ParentsInfo;
import com.kt.yapp.domain.RsaKeyInfo;
import com.kt.yapp.domain.SendSms;
import com.kt.yapp.domain.SysCheck;
import com.kt.yapp.domain.Terms;
import com.kt.yapp.domain.req.NoticeReq;
import com.kt.yapp.domain.resp.ResultInfo;
import com.kt.yapp.em.EnumRsltCd;
import com.kt.yapp.exception.YappException;
import com.kt.yapp.service.CmsService;
import com.kt.yapp.service.CommonService;
import com.kt.yapp.service.KosService;
import com.kt.yapp.util.AppEncryptUtils;
import com.kt.yapp.util.SessionKeeper;
import com.kt.yapp.util.YappUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * CMS 처리 컨트롤러
 */
@RestController
@Api(description="공지사항, 알림 등 CMS 관련 처리")
public class CmsController 
{
	private static final Logger logger = LoggerFactory.getLogger(CmsController.class);
	
	@Autowired
	private CmsService cmsService;
	@Autowired
	private CommonService cmnService;
	@Autowired
	private KosService kosService;
	@Autowired
	private AppEncryptUtils appEncryptUtils;

	@ApiOperation(value="앱 정보 조회(버전 등)")
	@RequestMapping(value = "/na/cms/appinfo", method = RequestMethod.GET)
	public ResultInfo<AppInfo> getAppInfo(
			@ApiParam(value="단말 OS 유형(G0001: Android, G0002: IOS)") @RequestParam String osTp
			, HttpServletRequest req) throws Exception
	{
		//220509
		String appVrsn = req.getHeader("appVrsn");
		AppInfo appInfo = cmsService.getAppInfo(osTp, appVrsn);
		
		return new ResultInfo<>(appInfo);
	}

	@ApiOperation(value="시스템 점검 정보 조회")
	@RequestMapping(value = "/na/cms/syscheck", method = RequestMethod.GET)
	public ResultInfo<SysCheck> getSysCheckInfo() throws Exception
	{
		return new ResultInfo<>(cmsService.getSysChkInfo(null));
	}
	
	@ApiOperation(value="약관 정보 조회")
	@RequestMapping(value = "/na/cms/terms", method = RequestMethod.GET)
	@ApiImplicitParams({@ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<Terms> getTermsInfo() throws Exception
	{
		return new ResultInfo<>(cmsService.getTermsInfo());
	}
	
	@ApiOperation(value="약관 정보 조회(url)")
	@RequestMapping(value = "/na/cms/termsAgreeInfo", method = RequestMethod.GET)
	@ApiImplicitParams({@ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<Terms> getTermsInfoNew() throws Exception
	{
		return new ResultInfo<>(cmsService.getTermsAgreeInfo());
	}
	
	@ApiOperation(value="공지사항 목록 조회")
	@RequestMapping(value = "/cms/bbs/notice", method = RequestMethod.GET)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<Notice> getNoticeList(NoticeReq paramInfo) throws Exception
	{
		return new ResultInfo<>(cmsService.getNoticeList(paramInfo));
	}
	
	@ApiOperation(value="알림 목록 조회")
	@RequestMapping(value = "/cms/notimsg", method = RequestMethod.GET)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<NoticeMsg> getNotiMsgList(HttpServletRequest req) throws Exception
	{
		List<NoticeMsg> notiMsgList = null;
		List<NoticeMsg> newThreadPushMsgList = null;
		
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		
		NoticeMsg notiMsg = new NoticeMsg();
		notiMsg.setCntrNo(loginCntrNo);
		
		//2022.05.03
		String loginMobileNo = null;
		String loginUserId = null;
		
		if(SessionKeeper.getSdata(req) != null){
			loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
			loginUserId = SessionKeeper.getSdata(req).getUserId();
		}
		notiMsg.setUserId(loginUserId);
		
		logger.info("cntrNo : "+loginCntrNo+" : userId : "+loginUserId + " : mobileNo : "+loginMobileNo);		
		
		//kt회선
		if(YappUtil.isNotEmpty(loginCntrNo)){
			// 알림 목록 조회
			notiMsgList = cmsService.getNotiMsgList(notiMsg);
			newThreadPushMsgList = cmsService.getPushList(notiMsg); 
		//kt비회선
		}else{
			if(!YappUtil.isEmpty(loginMobileNo)){
				if(loginMobileNo.indexOf("ktid") != -1 ){
					// 푸시 알림 목록 조회
					notiMsgList = cmsService.getPushMsgList(notiMsg);
					newThreadPushMsgList = cmsService.getPushList(notiMsg); 
				}
			}
		}
		
		// 합치기	
		List<NoticeMsg> joined = new ArrayList<>();
		joined.addAll(notiMsgList);
		joined.addAll(newThreadPushMsgList);
		
		// 날짜기준 정렬
		Collections.sort( joined,  new CustomDateSort() );		
		
//		return new ResultInfo<>(notiMsgList);
		return new ResultInfo<>(joined);
	}
	
	/**
	 * NoticeMsg 날짜 기준 정렬
	 * @author 91314937
	 *
	 */
	static class CustomDateSort implements Comparator<NoticeMsg> {

		@Override
		public int compare(NoticeMsg o1, NoticeMsg o2) {
			// TODO Auto-generated method stub 
			return o2.getRegDt().compareTo(o1.getRegDt());
		}
		
	}

	//사용안함
	@ApiOperation(value="알림 new 건수")
	@RequestMapping(value = "/cms/notimsg/newcount", method = RequestMethod.GET)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<Integer> getNotiMsgNewCount(HttpServletRequest req) throws Exception
	{
		int cnt = 0;
		
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		String loginMobileNo = null;
		String loginUserId = null;
		
		if(SessionKeeper.getSdata(req) != null){
			loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
			loginUserId = SessionKeeper.getSdata(req).getUserId();
		}
		
		NoticeMsg notiMsg = new NoticeMsg();
		
		notiMsg.setCntrNo(loginCntrNo);
		notiMsg.setUserId(loginUserId);
		
		//kt회선
		if(YappUtil.isNotEmpty(loginCntrNo)){
			
			logger.info("==========================================================================");	
			logger.info("/cms/notimsg/newcount => loginCntrNo: "+loginCntrNo);	
			logger.info("==========================================================================");	
			
			cnt += cmsService.getNotiMsgNewCount(loginCntrNo);
		//kt비회선
		}else{
			if(!YappUtil.isEmpty(loginMobileNo)){
				if(loginMobileNo.indexOf("ktid") != -1 ){
					logger.info("==========================================================================");	
					logger.info("/cms/notimsg/newcount => loginUserId: "+loginUserId);
					logger.info("==========================================================================");
				}
			}
		}	

		return new ResultInfo<>(cnt);
	}
	
	@ApiOperation(value="알림 전체 읽음 처리")
	@RequestMapping(value = "/cms/notimsg/new", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<String> updateNotiMsgNewYn(HttpServletRequest req) throws Exception
	{
		//2022.05.04
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		String loginMobileNo = null;
		String loginUserId = null;
		
		if(SessionKeeper.getSdata(req) != null){
			loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
			loginUserId = SessionKeeper.getSdata(req).getUserId();
		}
		
		int delCnt = 0;
		
		//kt회원
		if(YappUtil.isNotEmpty(loginCntrNo)){
			delCnt = cmsService.updateNotiMsgNewYn(loginCntrNo);
		//kt비회원
		}else{
			if(!YappUtil.isEmpty(loginMobileNo)){
				if(loginMobileNo.indexOf("ktid") != -1 ){
					delCnt = cmsService.updateUserIdPushMsg(loginUserId);
				}
			}

		}
		
		logger.info("알림  : " + delCnt + " 읽음 처리");
		
		return new ResultInfo<>();
	}

	@ApiOperation(value="알림 개별 읽음 처리")
	@RequestMapping(value = "/cms/notimsg/onenew", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<String> updateNotiMsgOneNewYn(int notiMsgSeq, HttpServletRequest req) throws Exception
	{
		int delCnt = cmsService.updateNotiMsgOneNewYn(notiMsgSeq);
		logger.info("알림  : " + delCnt + " 읽음 처리");
		
		return new ResultInfo<>();
	}

	@ApiOperation(value="알림 전체 삭제")
	@RequestMapping(value = "/cms/notimsg", method = RequestMethod.DELETE)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<String> deleteAllNotiMsg(HttpServletRequest req) throws Exception
	{
		//2022.05.04
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		String loginMobileNo = null;
		String loginUserId = null;
		
		if(SessionKeeper.getSdata(req) != null){
			loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
			loginUserId = SessionKeeper.getSdata(req).getUserId();
		}
		
		int delCnt = 0;
		
		//kt회선
		if(YappUtil.isNotEmpty(loginCntrNo)){
			delCnt = cmsService.deleteNotiMsg(loginCntrNo);

		//kt비회선
		}else{
			if(!YappUtil.isEmpty(loginMobileNo)){
				if(loginMobileNo.indexOf("ktid") != -1 ){
					delCnt = cmsService.deleteUserIdNotiMsg(loginUserId);

				}
			}
		}
		
		logger.info("알림 " + delCnt + " 삭제");
		
		return new ResultInfo<>();
	}
	
	@ApiOperation(value="알림 개별 삭제")
	@RequestMapping(value = "/cms/notimsgone", method = RequestMethod.DELETE)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<String> deleteOneNotiMsg(int notiMsgSeq, HttpServletRequest req) throws Exception
	{
		int delCnt = cmsService.deleteNotiMsgOne(notiMsgSeq);
		logger.info("알림 " + delCnt + " 삭제");
		
		return new ResultInfo<>();
	}
	
	@ApiOperation(value="이벤트 바로가기 이력 저장")
	@ResponseBody
	@RequestMapping(value = "/cms/event/linkappl", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<String> applyLinkEvent(int evtSeq, HttpServletRequest req) throws Exception
	{
		try {
			
			String loginCntrNo = SessionKeeper.getCntrNo(req);
			String memStatus = "";
			String loginUserId = "";
			
			if(SessionKeeper.getSdata(req) != null){
				memStatus = SessionKeeper.getSdata(req).getMemStatus();
				loginUserId = SessionKeeper.getSdata(req).getUserId();
			}
			
			logger.info("==============================================================");
			logger.info("/event/linkappl -> memStatus       : "+memStatus);
			logger.info("/event/linkappl -> loginCntrNo     : "+loginCntrNo);
			logger.info("/event/linkappl -> loginUserId     : "+loginUserId);
			logger.info("==============================================================");
			
			EventAppl appl = new EventAppl();
			appl.setEvtSeq(evtSeq);
			
			if(loginCntrNo!=null){
				appl.setCntrNo(loginCntrNo);
			}else{
				appl.setCntrNo("UNKNOWN");
				appl.setUserId(loginUserId);
			}
			
			// 이벤트 바로가기 이력 저장
			cmsService.applyLinkEvent(appl);
		} catch (RuntimeException e) {
			// TODO: handle exception
			throw new YappException("CHECK_MSG", cmnService.getMsg("ERR_Y_SYSTEM"));
		} catch (Exception e) {
			// TODO: handle exception
			throw new YappException("CHECK_MSG", cmnService.getMsg("ERR_Y_SYSTEM"));
		}
		return new ResultInfo<>();
	}
	
	@ApiOperation(value="이벤트 바로가기 이력 저장")
	@ResponseBody
	@RequestMapping(value = "/cms/event/linkapplNoLogin", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<String> applyLinkEventNoLogin(int evtSeq, HttpServletRequest req) throws Exception
	{
		EventAppl appl = new EventAppl();
		appl.setEvtSeq(evtSeq);
		appl.setCntrNo("UNKNOWN");
		// 이벤트 바로가기 이력 저장
		cmsService.applyLinkEvent(appl);
		return new ResultInfo<>();
	}

	@ApiOperation(value="이용안내 목록 조회")
	@RequestMapping(value = "/cms/guide", method = RequestMethod.GET)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<Guide> getGuideList(HttpServletRequest req) throws Exception
	{
		return new ResultInfo<>(cmsService.getGuideList());
	}

	@ApiOperation(value="법정대리인조회")
	@RequestMapping(value = "/na/cms/parentsInfo", method = RequestMethod.GET)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<ParentsInfo> getParentsInfoList(String telno, HttpServletRequest req) throws Exception
	{
		String tel = telno;
		if(telno == null || telno.length() == 0 || "null".equals(telno)){
			
			if(SessionKeeper.getSdata(req) != null){
				tel = SessionKeeper.getSdata(req).getMobileNo();
			
			}
		//220620 모바일번호 복호화
		}else{
			String osTp = req.getHeader("osTp");
			String appVrsn = req.getHeader("appVrsn");

			tel = appEncryptUtils.aesDec128(tel, osTp+appVrsn);
		}
		
		List<ParentsInfo> parentsList = kosService.retrieveLegalAgntSvcNo(tel);
		
		return new ResultInfo<>(parentsList);
	}

	@ApiOperation(value="문자발송")
	@RequestMapping(value = "/na/cms/sendsms", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<SendSms> sendSms(String telno, String authTp, HttpServletRequest req) throws Exception
	{

		String cntrNo = SessionKeeper.getCntrNo(req);
		/*String randomNum = String.valueOf((int)(Math.random() * 900000) + 100000);*/
		
		SecureRandom tmpRandom = new SecureRandom();
		tmpRandom.setSeed(new Date().getTime());
		String randomNum = String.valueOf((int)(tmpRandom.nextDouble() * 900000) + 100000);
		
		String tel = telno;
		if(telno == null || telno.length() == 0 || "null".equals(telno)){
			if(SessionKeeper.getSdata(req) != null){
				tel = SessionKeeper.getSdata(req).getMobileNo();
			}
		//220620 모바일번호 복호화
		}else{
			String osTp = req.getHeader("osTp");
			String appVrsn = req.getHeader("appVrsn");

			tel = appEncryptUtils.aesDec128(tel, osTp+appVrsn);
		}		
		
		SendSms smsInfo = cmsService.sndSmsAuth(cntrNo, tel, authTp, randomNum);
		// SMS 호출 성공 시 발송한 "휴대폰 번호 + 랜덤번호" 를 저장해 둔다.
		SessionKeeper.addToReq(req, SessionKeeper.KEY_AUTH_SMS_NUM, randomNum, 180);
		
		return new ResultInfo<>(smsInfo);
	}
	
	@ApiOperation(value="문자인증체크)", notes="인증 실패 시 실패횟수를 반환한다.")
	@RequestMapping(value = "/na/cms/checkSmsAuth", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<SendSms> checkSmsAuth(String telno, String authSmsSeq, String authNum, String authTp, HttpServletRequest req) throws Exception
	{
		String cntrNo = SessionKeeper.getCntrNo(req);
		ResultInfo<SendSms> resultInfo = new ResultInfo<>();
		SendSms respResult = new SendSms();
		resultInfo.setResultData(respResult);
		String authSmsYn = "N";
		
		SendSms authFail = cmsService.getAuthSmsFailCnt(makeAuthSmsFail(cntrNo, telno, "N", authTp, authSmsSeq));
		
		// 인증 에러 횟수 체크 처리
		String authFailChkStr = checkAuthSmsFail(authFail);
		if ( authFailChkStr != null ) {
			YappUtil.setErrResultInfo(resultInfo, authFailChkStr);
			respResult.setFailCnt(authFail.getFailCnt());
			respResult.setAuthSmsYn(authSmsYn);
			return resultInfo;
		}
		
		// 저장해둔 인증번호가 있는지 체크
		String savedAuthNum = YappUtil.nToStr(SessionKeeper.get(req, SessionKeeper.KEY_AUTH_SMS_NUM));
		if ( YappUtil.isEmpty(savedAuthNum) ) {
			YappUtil.setErrResultInfo(resultInfo, cmnService.getMsg("ERR_NO_AUTH_REQ"));		// 인증요청 정보가 없습니다.
			respResult.setAuthSmsYn(authSmsYn);
			return resultInfo;
		}
		
		// 입력 번호와 인증 요청에서 저장해 둔 "랜덤번호" 를 비교한다.
		if ( YappUtil.isEq(authNum, savedAuthNum)){
			authSmsYn = "Y";
			SessionKeeper.remove(req, SessionKeeper.KEY_AUTH_SMS_NUM);
			// 실패횟수 초기화
			cmsService.updateAuthSmsFailCnt(makeAuthSmsFail(cntrNo, telno, "Y", authTp, authSmsSeq), "Y");

		} else {
			YappUtil.setErrResultInfo(resultInfo, EnumRsltCd.C999.getRsltCd(), cmnService.getMsg("ERR_AUTH_NUM"));		// 인증번호가 올바르지 않습니다.
			// 실패횟수를 업데이트 한다.
			cmsService.updateAuthSmsFailCnt(makeAuthSmsFail(cntrNo, telno, "N",authTp, authSmsSeq), "N");
		}
		
		// 인증 에러 정보 재조회
		authFail = cmsService.getAuthSmsFailCnt(makeAuthSmsFail(cntrNo, telno, "N", authTp, authSmsSeq));
		respResult.setFailCnt(authFail == null ? 0 : authFail.getFailCnt());
		respResult.setAuthSmsYn(authSmsYn);
		respResult.setCntrNo(cntrNo);
		
		return resultInfo;
	}
	
	@ApiOperation(value="RSA Public Key 획득")
	@RequestMapping(value = "/na/common/getpubkey", method = RequestMethod.POST)
	public ResultInfo<RsaKeyInfo> getRsaPublicKeyInfo(HttpServletRequest req, String userId) throws Exception
	{
		GrpCode grpCode_rsa = cmsService.getCodeNm("RSA_GRP_CODE", "RSA01");
		int rsa_count = Integer.parseInt(grpCode_rsa.getCodeNm());
		
		SecureRandom tmpRandom = new SecureRandom();
		tmpRandom.setSeed(new java.util.Date().getTime());
		
		int index = (int)(31+rsa_count*tmpRandom.nextDouble());
		 
		//int index = (int)(31+rsa_count*Math.random());
		
		RsaKeyInfo rsaKeyInfo = cmsService.getRsaPublicKeyInfo(index);
		
		//220712
		if(!YappUtil.isEmpty(userId)){
			cmsService.LoginKeyLogic(userId, rsaKeyInfo.getKeySeq(), "N");
		}
		
		return new ResultInfo<>(rsaKeyInfo);
	}

	private SendSms makeAuthSmsFail(String cntrNo, String mobileNo, String isInit, String authTp, String authSmsSeq)
	{
		SendSms authSmsFail = new SendSms();
		authSmsFail.setRevMobileNo(mobileNo);
		authSmsFail.setIsInit(isInit);
		authSmsFail.setAuthTp(authTp);
		authSmsFail.setCntrNo(cntrNo);
		authSmsFail.setAuthSmsSeq(Integer.parseInt(authSmsSeq));

		return authSmsFail;
	}

	/**
	 * 휴대폰 SMS 추가인증횟수를 체크한다.
	 */
	private String checkAuthSmsFail(SendSms authFail)
	{
		if ( authFail == null )
			return null;
		
		Calendar curDate = Calendar.getInstance();
		curDate.setTime(authFail.getLastFailDt());
		curDate.add(Calendar.MINUTE, 5);
		
		String rtnStr = null;
		
		// 실패횟수가 5번 이상이면 5분동안 인증처리 불가
		if ( authFail.getFailCnt() >= 5 )
		{
			if ( curDate.getTimeInMillis() >= Calendar.getInstance().getTimeInMillis() ) {
				rtnStr = cmnService.getMsg("ERR_5_AUTH_FAIL");
			} else {
				// 10분이 지났으면 실패횟수 초기화
				authFail.setIsInit("Y");
				cmsService.updateAuthSmsFailCnt(authFail, "N");
			}
		}
		return rtnStr;
	}
//	@ApiOperation(value="그룹코드 정보 조회")
//	@RequestMapping(value = "/cms/grpcd", method = RequestMethod.GET)
//	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
//	public ResultInfo<GrpCode> getGrpCdList(String grpCodeId, HttpServletRequest req) throws Exception
//	{
//		GrpCode paramGrpCode = new GrpCode();
//		paramGrpCode.setGrpCodeId(grpCodeId);
//		
//		return new ResultInfo<>(cmsService.getGrpCodeList(grpCodeId, null));
//	}
	
	/**
	 * 20220502 SHOP 알림조회
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="SHOP 알림 목록 조회")
	@RequestMapping(value = "/cms/shopNotimsg", method = RequestMethod.GET)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<NoticeMsg> getShopNotiMsgList(HttpServletRequest req) throws Exception
	{
		
		String loginUserId = null;
		
		if(SessionKeeper.getSdata(req) != null){
			loginUserId = SessionKeeper.getSdata(req).getUserId().trim();
		}

		NoticeMsg notiMsg = new NoticeMsg();
		notiMsg.setUserId(loginUserId);
		
		// shop 알림 목록 조회
		List<NoticeMsg> notiMsgList = cmsService.getShopNotiMsgList(notiMsg);
		
		return new ResultInfo<>(notiMsgList);
	}
	
	/**
	 * 20220502 SHOP 알림 전체 삭제
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="SHOP 알림 전체 삭제")
	@RequestMapping(value = "/cms/shopNotimsg", method = RequestMethod.DELETE)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<String> deleteAllShopNotiMsg(HttpServletRequest req) throws Exception
	{
		String loginUserId = null;
		
		if(SessionKeeper.getSdata(req) != null){
			loginUserId = SessionKeeper.getSdata(req).getUserId().trim();
		}
		
		int delCnt = cmsService.deleteShopNotiMsg(loginUserId);
		logger.info("shop 알림 " + delCnt + " 삭제");
		
		return new ResultInfo<>();
	}
	
	/**
	 * 20220502 Shop알림 전체 읽음 처리
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="Shop알림 전체 읽음 처리")
	@RequestMapping(value = "/cms/shopNotimsg/new", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<String> updateShopNotiMsgNewYn(HttpServletRequest req) throws Exception
	{
		String loginUserId = null;
		
		if(SessionKeeper.getSdata(req) != null){
			loginUserId = SessionKeeper.getSdata(req).getUserId().trim();
		}
		
		int delCnt = cmsService.updateShopNotiMsgNewYn(loginUserId);
		logger.info("Shop 알림  : " + delCnt + " 읽음 처리");
		
		return new ResultInfo<>();
	}
}
