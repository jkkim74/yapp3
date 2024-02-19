package com.kt.yapp.web;

import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kt.yapp.common.repository.TestDao;
import com.kt.yapp.common.service.TestService;
import com.kt.yapp.config.LimitationPropConfig;
import com.kt.yapp.domain.CallingPlan;
import com.kt.yapp.domain.ContractInfo;
import com.kt.yapp.domain.Coupon;
import com.kt.yapp.domain.CouponDtl;
import com.kt.yapp.domain.CouponEnd;
import com.kt.yapp.domain.CouponPackage;
import com.kt.yapp.domain.CouponSearch;
import com.kt.yapp.domain.CustAgreeInfoRetvListDtoDetail;
import com.kt.yapp.domain.DataInfo;
import com.kt.yapp.domain.DataShareList;
import com.kt.yapp.domain.DeviceTokenInfo;
import com.kt.yapp.domain.GiftPw;
import com.kt.yapp.domain.NoticeMsg;
import com.kt.yapp.domain.ParentsInfo;
import com.kt.yapp.domain.PushDevice;
import com.kt.yapp.domain.PushMessage;
import com.kt.yapp.domain.SendSms;
import com.kt.yapp.domain.TokenInfo;
import com.kt.yapp.domain.resp.ResultInfo;
import com.kt.yapp.em.EnumPkgTpCd;
import com.kt.yapp.em.EnumRsltCd;
import com.kt.yapp.em.EnumYn;
import com.kt.yapp.exception.YappException;
import com.kt.yapp.push.FcmMessage;
import com.kt.yapp.push.FcmResult;
import com.kt.yapp.push.FcmSender;
import com.kt.yapp.push.Notification;
import com.kt.yapp.redis.RedisComponent;
import com.kt.yapp.service.CmsService;
import com.kt.yapp.service.CommonService;
import com.kt.yapp.service.GiftService;
import com.kt.yapp.service.KosService;
import com.kt.yapp.service.ShubService;
import com.kt.yapp.service.UserService;
import com.kt.yapp.service.WsgService;
import com.kt.yapp.soap.SoapConnUtil;
import com.kt.yapp.soap.response.SoapResponse188;
import com.kt.yapp.soap.response.SoapResponse494;
import com.kt.yapp.util.AppEncryptUtils;
import com.kt.yapp.util.KeyFixUtil;
import com.kt.yapp.util.SessionKeeper;
import com.kt.yapp.util.YappUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "테스트 컨트롤러")
@Controller
public class TestController2 
{
	private static final Logger logger = LoggerFactory.getLogger(TestController2.class);
	
	@Autowired
	private TestService service;
	@Autowired
	private TestDao dao;
	@Autowired
	private UserController userController;
	
	@Autowired
	private CommonService cmnService;
	@Autowired
	private KosService kosService;
	@Autowired
	private GiftService giftService;
	@Autowired
	private UserService userService;
	@Autowired
	private ShubService shubService;
	@Autowired
	private WsgService wsgService;
	@Autowired
	private TestService testService;
	@Autowired
	private CmsService cmsService;
	@Autowired
	private LimitationPropConfig config;
	@Autowired
	private KeyFixUtil util;
	@Autowired
	private SoapConnUtil soapConnUtil;
	@Resource(name = "redisTemplate")
	private ValueOperations<String, String> valusOps;
	
	@Resource(name="redisTemplate") 
	private SetOperations<String, String> setOperations;

	@Resource(name="redisTemplate") 
	private HashOperations<String, String, String> hashOperations;

	@Resource(name="redisTemplate") 
	private ListOperations<String, String> listOperations;
	@Value("${shub.authentication.rest.url}")
	public String restShubUrlAuth;
	@Value("${fcm.api.serverkey}")
	public String serverkey;
	@Autowired 
	private RedisComponent redisComponent;
	@Autowired
	private KeyFixUtil keyFixUtil;	
	
	/*
	
	@RequestMapping(value = "/checkGiftPw", method = RequestMethod.POST)
	@ResponseBody
	public GiftPw checkGiftPw(String cntrNo, String giftPw) throws Exception
	{
		logger.info("====================================[parameter]=============================================");
		logger.info("cntrNo : " + cntrNo + ", giftPw : " + giftPw);
		int chkCount = giftService.checkGiftPw(cntrNo, giftPw);
		String reCheckYn = "N";
		if(chkCount > 0){
			reCheckYn = "Y";
		}
		GiftPw giftPwDomain = new GiftPw();
		giftPwDomain.setCheckYn(reCheckYn);
		logger.info("====================================[result]=============================================");
		logger.info("chkCount : " + chkCount);
		logger.info("reCheckYn : " + reCheckYn);
		logger.info("=================================================================================");
		return giftPwDomain;
	}
	
	@RequestMapping(value = "/na/test/cms/parentsInfo", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo<ParentsInfo> getTestParentsInfo(String telno) throws Exception
	{
		SessionKeeper.VAL_SERVER_IP = "10.220.170.243";
		List<ParentsInfo> parentsList = kosService.retrieveLegalAgntSvcNo(telno);
		return new ResultInfo<>(parentsList);
	}

	@RequestMapping(value = "/na/test/cms/sendsms", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo<SendSms> sendTestSmsAuth(String cntrNo,String telno,String authTp, HttpServletRequest req) throws Exception
	{
		SessionKeeper.VAL_SERVER_IP = "10.220.170.243";
		String cntrNo2 = SessionKeeper.getCntrNo(req);
		String randomNum = String.valueOf((int)(Math.random() * 900000) + 100000);
		Date now = new Date();
		String sDate = new SimpleDateFormat("yyMMddhhmmss").format(now);
		
		//kosService.sendSms(telno, "123123", YappUtil.lpad(sDate, 13, "0"));
		SendSms smsInfo = cmsService.sndSmsAuth(cntrNo, telno, authTp, randomNum);
		// SMS 호출 성공 시 발송한 "랜덤번호" 를 저장해 둔다.
		SessionKeeper.addToReq(req, SessionKeeper.KEY_AUTH_SMS_NUM, randomNum, 180);
		logger.info("====================================[result]=============================================");
		logger.info("randomNum : " + randomNum);
		logger.info("====================================[result]=============================================");
		return new ResultInfo<>(smsInfo);
	}

	@RequestMapping(value = "/na/test/cms/checkSmsAuth", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo<SendSms> checkTestSmsAuth(String cntrNo, String telno, String authSmsSeq, String authNum, String authTp, HttpServletRequest req) throws Exception
	{
		String cntrNo2 = SessionKeeper.getCntrNo(req);
		SessionKeeper.VAL_SERVER_IP = "10.220.170.243";
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
		logger.info("====================================[session]=============================================");
		logger.info("savedAuthNum : " + savedAuthNum);
		logger.info("====================================[session]=============================================");
		if ( YappUtil.isEmpty(savedAuthNum) ) {
			YappUtil.setErrResultInfo(resultInfo, cmnService.getMsg("ERR_NO_AUTH_REQ"));		// 인증요청 정보가 없습니다.
			respResult.setAuthSmsYn(authSmsYn);
			return resultInfo;
		}
		
		// 입력 번호와 인증 요청에서 저장해 둔 "휴대폰 번호 + 랜덤번호" 를 비교한다.
		if ( YappUtil.isEq(authNum, savedAuthNum)){
			SessionKeeper.remove(req, SessionKeeper.KEY_AUTH_SMS_NUM);
			// 실패횟수 초기화
			cmsService.updateAuthSmsFailCnt(makeAuthSmsFail(cntrNo, telno, "Y", authTp, authSmsSeq), "Y");
			authSmsYn = "Y";
		} else {
			YappUtil.setErrResultInfo(resultInfo, EnumRsltCd.C500.getRsltCd(), cmnService.getMsg("ERR_AUTH_NUM"));		// 인증번호가 올바르지 않습니다.
			// 실패횟수를 업데이트 한다.
			cmsService.updateAuthSmsFailCnt(makeAuthSmsFail(cntrNo, telno, "N",authTp, authSmsSeq), "N");
		}
		
		// 인증 에러 정보 재조회
		authFail = cmsService.getAuthSmsFailCnt(makeAuthSmsFail(cntrNo, telno, "N", authTp, authSmsSeq));
		respResult.setFailCnt(authFail == null ? 0 : authFail.getFailCnt());
		respResult.setAuthSmsYn(authSmsYn);
		
		return resultInfo;
	}

	@RequestMapping(value = "/na/call200", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo call200(String userId, String pwd) throws Exception
	{
		return new ResultInfo<>(shubService.callFn200(userId, pwd));
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

	*//**
	 * 휴대폰 SMS 추가인증횟수를 체크한다.
	 *//*
	private String checkAuthSmsFail(SendSms authFail)
	{
		if ( authFail == null )
			return null;
		
		Calendar curDate = Calendar.getInstance();
		curDate.setTime(authFail.getLastFailDt());
		curDate.add(Calendar.MINUTE, 10);
		
		String rtnStr = null;
		
		// 실패횟수가 5번 이상이면 10분동안 인증처리 불가
		if ( authFail.getFailCnt() >= 5 )
		{
			if ( curDate.getTimeInMillis() >= Calendar.getInstance().getTimeInMillis() ) {
				rtnStr = cmnService.getMsg("ERR_10_AUTH_FAIL");
			} else {
				// 10분이 지났으면 실패횟수 초기화
				authFail.setIsInit("Y");
				cmsService.updateAuthSmsFailCnt(authFail, "N");
			}
		}
		return rtnStr;
	}
	@RequestMapping(value = "/na/getAge", method = RequestMethod.POST)
	@ResponseBody
	public void getAge(String birthDate) throws Exception
	{
		String fyn  = YappUtil.checkFourteenYn(birthDate);
		String eyn  = YappUtil.checkEightteenYn(birthDate);
	
		logger.info("====================================[RESULT START]=============================================");
		logger.info("birthDate : " + birthDate + ", checkFourteenYn : " + fyn + ", checkEightteenYn : " + eyn);
		logger.info("====================================[RESULT END]=============================================");
	}

	@RequestMapping(value = "/na/getMaskingTelno", method = RequestMethod.POST)
	@ResponseBody
	public void getMaskingTelno(String telno) throws Exception
	{
		String fyn2  = YappUtil.getRefacTelno(telno);
		String fyn  = YappUtil.blindNameToTelno(fyn2);
	
		logger.info("====================================[RESULT START]=============================================");
		logger.info("getRefacTelno telno : " + telno + ", fyn2 : " + fyn2);
		logger.info("blindNameToTelno telno : " + telno + ", fyn : " + fyn);
		logger.info("====================================[RESULT END]=============================================");
	}

	@RequestMapping(value = "/na/setGiftPw", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo setGiftPw(String cntrNo, String giftPw) throws Exception
	{
		logger.info("====================================[parameter]=============================================");
		logger.info("cntrNo : " + cntrNo + ", giftPw : " + giftPw);
		int chkCount = giftService.checkGiftPw(cntrNo, "");
		if(chkCount > 0){
			giftService.updateGiftPw(cntrNo, giftPw);
		} else {
			giftService.insertGiftPw(cntrNo, giftPw);
		}
		return new ResultInfo<>();
	}
	
	@RequestMapping(value = "/na/getRetvDataGiftRstr", method = RequestMethod.POST)
	@ResponseBody
	public void getRetvDataGiftRstr(String mobileNo, int dataAmt, HttpServletRequest req) throws Exception
	{
		kosService.getRetvDataGiftRstr(mobileNo, dataAmt);
	}
	
	@RequestMapping(value = "/na/getSearchMsg", method = RequestMethod.POST)
	@ResponseBody
	public void getSearchMsg(String msgCd, String msgData, String errMsgCode, String errStandardMsgCode) throws Exception
	{
		String msg = cmnService.getSearchMsg(msgCd, msgData, errMsgCode, errStandardMsgCode);
		logger.info("====================================[parameter]=============================================");
		logger.info("msgCd : " + msgCd + ", msgData : " + msgData);
		logger.info("====================================[parameter]=============================================");
		logger.info("msg : " + msg);
	}
	
	@RequestMapping(value = "/na/searchTimeOption", method = RequestMethod.POST)
	@ResponseBody
	public void searchTimeOption() throws Exception
	{
		HttpServletRequest req = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		SessionKeeper.VAL_SERVER_IP = "10.220.170.243";
		logger.info("SERVER_IP : " + SessionKeeper.VAL_SERVER_IP);
		kosService.searchTimeOption();
	}
	
	@RequestMapping(value = "/na/searchAplyProdInfoRetv", method = RequestMethod.POST)
	@ResponseBody
	public void searchAplyProdInfoRetv(String cntrNo) throws Exception
	{
		HttpServletRequest req = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		SessionKeeper.VAL_SERVER_IP = "10.220.170.243";
		logger.info("SERVER_IP : " + SessionKeeper.VAL_SERVER_IP);
		kosService.searchAplyProdInfoRetv(cntrNo);
	}
	
	@RequestMapping(value = "/na/setProcessProdStoreBas", method = RequestMethod.POST)
	@ResponseBody
	public void setProcessProdStoreBas(String cntrNo, String timeOption) throws Exception
	{
		HttpServletRequest req = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		SessionKeeper.VAL_SERVER_IP = "10.220.170.243";
		logger.info("SERVER_IP : " + SessionKeeper.VAL_SERVER_IP);
		kosService.setProcessProdStoreBas(cntrNo, timeOption);
	}
	
	@RequestMapping(value = "/na/randomNum", method = RequestMethod.POST)
	@ResponseBody
	public void randomNum() throws Exception
	{
		String randomNum = String.valueOf((int)(Math.random() * 900000) + 100000);
		logger.info("randomNum : " + randomNum);
		logger.info("randomNum2 : " + Math.random());
	}	
	
	@RequestMapping(value = "/na/subStringName", method = RequestMethod.POST)
	@ResponseBody
	public void subStringName(String name, int cnt) throws Exception
	{
		YappUtil.blindNameToName(name, cnt);
	}

	
	@RequestMapping(value = "/na/getCalMonth", method = RequestMethod.POST)
	@ResponseBody
	public void getCalMonth(int srchYmIdx) throws Exception
	{
		Calendar curCal = Calendar.getInstance();
		curCal.add(Calendar.MONTH, -srchYmIdx);
		curCal.add(Calendar.DATE, 1);
		
		String curMonth = YappUtil.getCurDate("yyyyMMdd");
		String srchYm = YappUtil.getCurDate(curCal.getTime(), "yyyyMMdd");
		logger.info("========================");
		logger.info("curMonth    :     "+curMonth);
		logger.info("srchYm    :     "+srchYm);
		logger.info("========================");

	}
	
	@RequestMapping(value = "/na/getNotiMsgNewCount", method = RequestMethod.POST)
	@ResponseBody
	public void getNotiMsgNewCount(String cntrNo) throws Exception
	{
		int cnt = cmsService.getNotiMsgNewCount(cntrNo);
		
		logger.info("========================");
		logger.info("cnt    :     " + cnt);
		logger.info("========================");

	}

	@RequestMapping(value = "/na/sendGcmTest", method = RequestMethod.POST)
	@ResponseBody
	public void sendGcmTest(String deviceToken, String title, String content) throws Exception
	{
		// AIzaSyAtWdQi3Jrd3G-2X3l9I1gG8ApQ8sN8F2A
		PushMessage message = new PushMessage();
		NoticeMsg masterNotiMsg = cmsService.getMasterNotiMsgInfo(1);
		message.setTitle("Y 데이터박스");
		message.setContent(masterNotiMsg.getNotiMsg());
		message.setDisplayType(displayType);
		message.setMenuType(menuType);
		message.setVendorCode(vendorCode);
		message.setLoginYn(loginYn);
		message.setLinkUrl(linkUrl);
		PushDevice device = new PushDevice();
		List<PushDevice> deviceList = new ArrayList<PushDevice>(); 
		device.setDeviceToken(deviceToken);
		deviceList.add(device);
		//gcmSendTask.sendGcmTask("AIzaSyAtWdQi3Jrd3G-2X3l9I1gG8ApQ8sN8F2A", message, deviceList);
		gcmSendTask.sendGcmTask("", message, deviceList);

	}

	@RequestMapping(value = "/na/sendApnsTest", method = RequestMethod.POST)
	@ResponseBody
	public void sendApnsTest(String deviceToken, boolean production, String title, String content) throws Exception
	{
		PushMessage message = new PushMessage();
		message.setTitle(title);
		message.setContent(content);
		message.setDisplayType(displayType);
		message.setMenuType(menuType);
		message.setVendorCode(vendorCode);
		message.setLoginYn(loginYn);
		message.setLinkUrl(linkUrl);
		PushDevice device = new PushDevice();
		List<PushDevice> deviceList = new ArrayList<PushDevice>(); 
		device.setDeviceToken(deviceToken);
		deviceList.add(device);
		apnsSendTask.sendApnsTask(production, message, deviceList);

	}
	
	@RequestMapping(value = "/na/sendGcmTest", method = RequestMethod.GET)
	@ResponseBody
	public void sendGcmTest(String deviceToken) throws Exception
	{
		// AIzaSyAtWdQi3Jrd3G-2X3l9I1gG8ApQ8sN8F2A
		PushMessage message = new PushMessage();
		message.setTitle("Y 데이터박스");
		message.setContent("Y 데이터박스 FCM 테스트립니다.");
		message.setLinkUrl("");
		message.setBadge(2);
		PushDevice device = new PushDevice();
		List<PushDevice> deviceList = new ArrayList<PushDevice>(); 
		device.setDeviceToken(deviceToken);
		deviceList.add(device);
		gcmSendTask.sendGcmTask("", message, deviceList);

	}
	
	@RequestMapping(value = "/na/setToken", method = RequestMethod.POST)
	@ResponseBody
	public void setToken(String deviceToken, String cntrNo, String osVrsn, String appVrsn) throws Exception
	{
		cmnService.updateDeviceToken(deviceToken, cntrNo, osVrsn, appVrsn);
		DeviceTokenInfo deiviceInfo = cmnService.getDeviceTokenIdInfo(cntrNo);
	}
	
	@RequestMapping(value = "/na/getOrdNo", method = RequestMethod.POST)
	@ResponseBody
	public void getOrdNo() throws Exception
	{
		Calendar curDate = Calendar.getInstance();
		Date now = new Date();
		String sDate = new SimpleDateFormat("yyyyMMddhhmmssSSS").format(now);
		String sDate1 = new SimpleDateFormat("yyMMddhhmms").format(now);
		String sDate2 = new SimpleDateFormat("yyMMddhhmms").format(now);
		if(sDate1.length()>11){
			sDate1 = sDate1.substring(0, 11);
		}
		logger.info("========================");
		logger.info("curDate    :     " + curDate.getTimeInMillis());
		logger.info("curDate    :     " + sDate);
		logger.info("curDate    :     " + sDate1);
		logger.info("curDate    :     " + sDate2);
		logger.info("========================"); 
	}

	@RequestMapping(value = "/na/getdate", method = RequestMethod.POST)
	@ResponseBody
	public void getdate() throws Exception
	{
		Calendar curDate = Calendar.getInstance();
		int aa = (int)YappUtil.getDayDistance("20180212","20180203");
		
		int a2 = (int)YappUtil.getDayDistance("20180203","20180212");
		
		int a4 = (int)YappUtil.getDayDistance("20180203","20180203");
		logger.info("========================");
		logger.info("aa    :     " + aa);
		logger.info("a2    :     " + a2);
		logger.info("a4    :     " + a4);
		logger.info("========================");
	}

	@RequestMapping(value = "/na/adddate", method = RequestMethod.POST)
	@ResponseBody
	public void adddate(String validYm) throws Exception
	{
		Calendar curCal = Calendar.getInstance();
		curCal.setTime(YappUtil.getDate("yyyyMM", validYm));
		curCal.add(Calendar.MONTH , 1);
		int tYear = curCal.get(Calendar.YEAR);
		int tMonth = curCal.get(Calendar.MONTH)+1;
		String tmpMon = "0";
		if(tMonth < 10){
			tmpMon = "0"+String.valueOf(tMonth);
		}else{
			tmpMon = String.valueOf(tMonth);
		}
		logger.info("========================");
		logger.info("YYYYMMDD    :     " + tYear+tmpMon);
		logger.info("tYear    :     " + tYear);
		logger.info("tmpMon    :     " + tmpMon);
		logger.info("========================");
	}
	
	@RequestMapping(value = "/na/call494cpnList", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo<CouponPackage> call494(String cntrNo, String mobileNo, String pkgTpCd, HttpServletRequest req) throws Exception
	{
		ResultInfo<CouponPackage> resultInfo = new ResultInfo<>();
		resultInfo.setResultInfoList(shubService.couponList(cntrNo, mobileNo, pkgTpCd));

		return resultInfo;
	}

	@RequestMapping(value = "/na/call494", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo<Coupon> callFn494(String cntrNo, String mobileNo, HttpServletRequest req) throws Exception
	{
		SoapResponse494 resp494 = shubService.callFn494(cntrNo, mobileNo, EnumPkgTpCd.PC.getValue());
		
		ResultInfo<Coupon> resultInfo = new ResultInfo<>();
		if ( YappUtil.is1(resp494.getRtnCd()) == false ){
			logger.info(resp494.getRtnCd() + ", " + resp494.getRtnDesc());
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_NO_COUPON"));
		}else{
			List<Coupon> couponList = resp494.getCouponList();
			List<Coupon> couponNewList = new ArrayList<>();
			int lteCnt = 0;
			for ( int i = 0; i < couponList.size(); i++ ){
				if("G0002".equals(couponList.get(i).getCpnTp())){
					lteCnt++;
				}
			}
			
			if(lteCnt == 0){
				for ( int i = 0; i < couponList.size(); i++ ){
					Coupon cpn = new Coupon();
					cpn = couponList.get(i);
					couponNewList.add(cpn);

					if("G0001".equals(cpn.getCpnTp())){
						Coupon cpnTmp = new Coupon();
						cpnTmp.setCpnNm(cpn.getCpnNm());
						cpnTmp.setCpnNo(cpn.getCpnNo());
						cpnTmp.setCpnUseYn(cpn.getCpnUseYn());
						cpnTmp.setCpnValidEdYmd(cpn.getCpnValidEdYmd());
						cpnTmp.setCpnValidStYmd(cpn.getCpnValidStYmd());
						cpnTmp.setCpnPrice(cpn.getCpnPrice());
						cpnTmp.setCpnTp("G0002");
						couponNewList.add(cpnTmp);
					}
				}
			}
			resultInfo.setResultInfoList(couponNewList);
		}
		return resultInfo;
	}

	@RequestMapping(value = "/na/call495", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo<String> callFn495(String cntrNo ,String mobileNo,String userId ,String cpnNo, HttpServletRequest req) throws Exception
	{
		Coupon cpn = new Coupon();
		cpn.setCntrNo(cntrNo);
		cpn.setMobileNo(mobileNo);
		cpn.setCpnNo(cpnNo);
		cpn.setUserId(userId);
		shubService.callFn495(cpn);
		return new ResultInfo<>();
	}

	@RequestMapping(value = "/na/decode", method = RequestMethod.POST)
	@ResponseBody
	public void getDecode(String str) throws Exception
	{
		String strDe = YappUtil.decode(str);
		logger.info("========================");
		logger.info("strDe    :     " + strDe);
		logger.info("========================");
	}

	@RequestMapping(value = "/na/encode", method = RequestMethod.POST)
	@ResponseBody
	public void getEncode(String str) throws Exception
	{
		String strEn = YappUtil.encode(str);
		logger.info("========================");
		logger.info("strEn    :     " + strEn);
		logger.info("========================");
	}

	
	@RequestMapping(value = "/na/shubcall188", method = RequestMethod.GET)
	@ResponseBody
	public String call188(String tokenId, HttpServletRequest req) throws Exception
	{
		SoapResponse188 aa = shubService.callFn188(tokenId);

		return aa.getCredentialId();
	}
	
	@RequestMapping(value = "/na/shubcall198", method = RequestMethod.POST)
	@ResponseBody
	public String shubcall198(String uid, String userId, String tokenId, String ssoregdt) throws Exception
	{
		String applogininfo = "";

		String ssoReqDate = YappUtil.getCurDate("yyyyMMddHHmmss");
		//ssoReqDate = ssoregdt;

	- key : uid
	- value : CEj0Wp4o2tl9WffCW+TmGI2SmWpzIM1fQIuvJgishK1yYreXC4zKq/lI/1GkLWMT9o1E3XafTHKHR7IbmO9XPw==
	- osvQ35BWhsf2CDMJHmsLQeCjix5uvwW5f8FILjkRrDgKmIcoc6mcZXmuQ0DY5mVyI2/PJtQKV7hlwpeICv3ayw==
	
    - key : ollehID
    - value : yanaq22
	- key : token_id
    - value : A72D49D2A4B211E5ED0972FA59D830B174E29276C76D90898B0FC0BF08DB5439AE8A7A04C6629FB05579DD607C6E5410BDB80991CE1FC0ED8681856584F19E3942FFCE86B234CAE369C0A836DFF0E7D920C29E8EEDE6A632AC38657D75BB849E
- FDDF5408DF9E02ABD271DA74E4FB19E46CE205D1DDDA2912038E22E38A3152E44EC5ED0ECF6CA07C466622F78CB75B8F6971B40100D9E04F32A12928629263FDF431CD16C8711DB311B33018DFCF22DAF6ED6DA30E0D6C26F35DF3E8D043A93B
 
		applogininfo = uid + ";" + userId + ";" + tokenId + ";" + ssoReqDate;
		logger.info("applogininfo 인코딩전: " + applogininfo + "\n");
		applogininfo = AppEncryptUtils.aesEnc(applogininfo);
		
		RestTemplate restCall = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		String key = "AII4720061843WRGAVC:TBK4720061843WOHSYC";
		// key = "AII9200036126QBXNWE:TBK9200036126QZYFZE";
		
		byte[] encVal = key.getBytes("utf-8");
		String encryptedkey = "Basic "+ Base64Utils.encodeToString(encVal);
		logger.info("========================================================");
		logger.info("URL: "+restShubUrlAuth + "\n");
		logger.info("==== http header 정보 ====");
		logger.info("Authorization: "+encryptedkey);
		logger.info("Content-type : "+MediaType.APPLICATION_JSON);
		logger.info("========================================================\n");
		
		logger.info("==== param 정보 ====");
		logger.info("applogininfo: " + applogininfo);
		logger.info("ssoreqdate: " + ssoReqDate);
		logger.info("========================================================");
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", encryptedkey);

		MultiValueMap<String, String> param = new LinkedMultiValueMap<String, String>();
		param.add("applogininfo", applogininfo);
		param.add("ssoreqdate", ssoReqDate);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(param, headers);
		
		ResponseEntity<TokenInfo> repense198 = restCall.postForEntity(restShubUrlAuth, request, TokenInfo.class);
		ResponseEntity<String> repense192 = restCall.postForEntity(restShubUrlAuth, request, String.class);
		logger.info("========================================================");
		logger.info("SHUB REST response : " + repense198.getBody().toString());
		logger.info("SHUB REST response 1 : " + repense198.getBody().getReturncode());
		logger.info("SHUB REST response 2 : " + repense198.getBody().getReturndesc());
		logger.info("SHUB REST response 3 : " + repense198.getBody().getSequenceno());
		logger.info("SHUB REST response 4 : " + repense198.getBody().getToken_id());
		logger.info("SHUB REST response : " + repense192.getBody().toString());
		logger.info("========================================================");
		return repense198.getBody().getToken_id();
	}
	
	@RequestMapping(value = "/na/shubcall809", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo<CouponSearch> shubcall809(String cntrNo, String mobileNo) throws Exception
	{
		logger.info("========================== shubcall809 ==============================");
		CouponSearch csh = shubService.callFn809(cntrNo, mobileNo);
		logger.info("========================================================");
		return new ResultInfo<CouponSearch>(csh);
	}

	@RequestMapping(value = "/na/shubcall810", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo<CouponSearch> shubcall810(String cntrNo, String mobileNo, String pkgid) throws Exception
	{
		logger.info("========================== shubcall810 ==============================");
		CouponSearch csh = shubService.callFn810(cntrNo, mobileNo, pkgid);
		logger.info("========================================================");
		return new ResultInfo<CouponSearch>(csh);
	}
	
	@RequestMapping(value = "/na/shubcall812", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo<CouponEnd> shubcall812(String cntrNo, String mobileNo) throws Exception
	{
		logger.info("========================== shubcall812 ==============================");
		CouponEnd csh = shubService.callFn812(cntrNo, mobileNo);
		logger.info("========================================================");
		return new ResultInfo<CouponEnd>(csh);
	}
	
	@RequestMapping(value = "/na/shubcall813", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo<CouponDtl> shubcall813(String serialnum) throws Exception
	{
		logger.info("========================== shubcall813 ==============================");
		CouponDtl csh = shubService.callFn813(serialnum);
		logger.info("========================================================");
		return new ResultInfo<CouponDtl>(csh);
	}
	
	@RequestMapping(value = "/na/shareData", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo<DataShareList> viewshareDataInfo(HttpServletRequest req, String cntrNo, String curDate) throws Exception
	{
		SessionKeeper.VAL_SERVER_IP = "10.217.41.182";
		DataShareList dataInfoListShare = kosService.dataSharPrvQntRetv(cntrNo, curDate);
		return new ResultInfo<DataShareList>(dataInfoListShare);
	}
	
	
	@RequestMapping(value = "/na/fivedtl", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo<DataInfo> viewfivedtl(HttpServletRequest req, String cntrNo, String ppCatL, String curDate) throws Exception
	{
		SessionKeeper.VAL_SERVER_IP = "10.217.41.182";
		List<DataInfo> dataInfoListTmp = new ArrayList<>();
		List<DataInfo> dataInfoListTmp2 = new ArrayList<>();
		
		//5G 요금제만 조회
		if (YappUtil.isEq(ppCatL, "G0005") == true){
			DataShareList dataInfoListShare = kosService.dataSharPrvQntRetv(cntrNo, curDate);
		

			// 내 데이터 잔여량 정보 조회
			List<DataInfo> dataInfoList = dataInfoListShare.getDataShareList();
			
			if ( YappUtil.isNotEmpty(dataInfoList) ){
				for ( DataInfo dataInfo : dataInfoList ) {
					DataInfo dataInfoTmp = new DataInfo();	
					int tmpCnt = 0;
					int dataAmtSum = 0;
					int rmnDataAmtSum = 0;
					String dataNmSum = "";
					for(int idx = 0 ; idx < dataInfoListTmp.size() ; idx++) {
						DataInfo dataInfoTmp2 = dataInfoListTmp.get(idx);
						
						if (dataInfo.getDataNm().equals(dataInfoTmp2.getDataNm())) {
							tmpCnt = idx;
							dataNmSum = dataInfo.getDataNm();
							dataAmtSum = dataInfo.getDataAmt() + dataInfoTmp2.getDataAmt();
							rmnDataAmtSum = dataInfo.getRmnDataAmt() + dataInfoTmp2.getRmnDataAmt();
						}
					}
	
					if (tmpCnt == 0) {
						dataInfoTmp.setDataNm(dataInfo.getDataNm());
						dataInfoTmp.setDataAmt(dataInfo.getDataAmt());
						dataInfoTmp.setRmnDataAmt(dataInfo.getRmnDataAmt());
						dataInfoListTmp.add(dataInfoTmp);
					} else {
						dataInfoTmp.setDataNm(dataNmSum);
						dataInfoTmp.setDataAmt(dataAmtSum);
						dataInfoTmp.setRmnDataAmt(rmnDataAmtSum);
						dataInfoListTmp.set(tmpCnt, dataInfoTmp);
					}
				}
			}

			if(dataInfoListTmp.size() > 4){
				DataInfo dataInfoTmp = new DataInfo();
				int inCnt1 = 0;
				int dataAmtSum1 = 0;
				int rmnDataAmtSum1 = 0;
				String dataNmSum1 = "";
				
				int inCnt2 = 0;
				int dataAmtSum2 = 0;
				int rmnDataAmtSum2 = 0;
				String dataNmSum2 = "";
				
				int inCnt3 = 0;
				int dataAmtSum3 = 0;
				int rmnDataAmtSum3 = 0;
				String dataNmSum3 = "";
				
				int inCnt4 = 0;
				int dataAmtSum4 = 0;
				int rmnDataAmtSum4 = 0;
				String dataNmSum4 = "";
	
				for ( DataInfo dataInfo : dataInfoListTmp ) {
					if(dataInfo.getDataNm().indexOf("룰렛") > -1 || dataInfo.getDataNm().indexOf("충전") > -1 || dataInfo.getDataNm().indexOf("팝콘") > -1 || dataInfo.getDataNm().indexOf("데이터플러스") > -1){
						dataNmSum1 = "5G 충전 데이터";
						dataAmtSum1 = dataAmtSum1 + dataInfo.getDataAmt();
						rmnDataAmtSum1 = rmnDataAmtSum1 + dataInfo.getRmnDataAmt();
						inCnt1 = 1;
					}else if(dataInfo.getDataNm().indexOf("데이터박스") > -1 || dataInfo.getDataNm().indexOf("패밀리박스") > -1){
						dataNmSum2 = "꺼낸 데이터";
						dataAmtSum2 = dataAmtSum2 + dataInfo.getDataAmt();
						rmnDataAmtSum2 = rmnDataAmtSum2 + dataInfo.getRmnDataAmt();
						inCnt2 = 1;
					}else if(dataInfo.getDataNm().indexOf("쿠폰") > -1){
						dataNmSum4 = "데이터쿠폰";
						dataAmtSum4 = dataAmtSum4 + dataInfo.getDataAmt();
						rmnDataAmtSum4 = rmnDataAmtSum4 + dataInfo.getRmnDataAmt();
						inCnt4 = 1;
					}else{
						dataNmSum3 = "공유 데이터";
						dataAmtSum3 = dataAmtSum3 + dataInfo.getDataAmt();
						rmnDataAmtSum3 = rmnDataAmtSum3 + dataInfo.getRmnDataAmt();
						inCnt3 = 1;
					}
				}
	
				if(inCnt1 == 1){
					dataInfoTmp = new DataInfo();
					dataInfoTmp.setDataNm(dataNmSum1);
					dataInfoTmp.setDataAmt(dataAmtSum1);
					dataInfoTmp.setRmnDataAmt(rmnDataAmtSum1);
					dataInfoListTmp2.add(dataInfoTmp);
				}
	
				if(inCnt2 == 1){
					dataInfoTmp = new DataInfo();
					dataInfoTmp.setDataNm(dataNmSum2);
					dataInfoTmp.setDataAmt(dataAmtSum2);
					dataInfoTmp.setRmnDataAmt(rmnDataAmtSum2);
					dataInfoListTmp2.add(dataInfoTmp);
				}
	
				if(inCnt3 == 1){
					dataInfoTmp = new DataInfo();
					dataInfoTmp.setDataNm(dataNmSum3);
					dataInfoTmp.setDataAmt(dataAmtSum3);
					dataInfoTmp.setRmnDataAmt(rmnDataAmtSum3);
					dataInfoListTmp2.add(dataInfoTmp);
				}
	
				if(inCnt4 == 1){
					dataInfoTmp = new DataInfo();
					dataInfoTmp.setDataNm(dataNmSum4);
					dataInfoTmp.setDataAmt(dataAmtSum4);
					dataInfoTmp.setRmnDataAmt(rmnDataAmtSum4);
					dataInfoListTmp2.add(dataInfoTmp);
				}
			}else{
				if(dataInfoListTmp.size() > 0){
					dataInfoListTmp2 = dataInfoListTmp;
				}else{
					DataInfo dataInfoTmp = new DataInfo();
					dataInfoTmp.setDataNm("");
					dataInfoTmp.setDataAmt(0);
					dataInfoTmp.setRmnDataAmt(0);
					dataInfoListTmp.add(dataInfoTmp);
					dataInfoListTmp2 = dataInfoListTmp;
				}
				
			}
		}

		return new ResultInfo<DataInfo>(dataInfoListTmp2);
	}

	*//**
	 * GET 호출을 위한 URL 을 만든다.
	 *//*
	private String makeUrl(String url, String[] ... params)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(url);
		for ( int i = 0; i < params.length; i++ ) {
			if ( YappUtil.isEmpty(params[i][1]) )
				continue;
			
			sb.append((i == 0 ? "?" : "&") + params[i][0] + "=" + params[i][1]);
		}
		return sb.toString();
	}
	*/
	@RequestMapping(value = "/na/shubcall11267", method = RequestMethod.GET)
	@ResponseBody
	public String call11267(String cntrNo, HttpServletRequest req) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		List<CustAgreeInfoRetvListDtoDetail> call11267List = shubService.callFn11267(cntrNo);
		if(call11267List != null){
			for (CustAgreeInfoRetvListDtoDetail custAgreeInfoRetvListDtoDetail : call11267List) {
				sb.append(" custAgreeInfoRetvListDtoDetail.getCustInfoAgreeCd() ==>" + custAgreeInfoRetvListDtoDetail.getCustInfoAgreeCd());
				sb.append(" custAgreeInfoRetvListDtoDetail.getAgreeVerNo() ==>" + custAgreeInfoRetvListDtoDetail.getAgreeVerNo());
				sb.append(" custAgreeInfoRetvListDtoDetail.getAgreeDt() ==>" + custAgreeInfoRetvListDtoDetail.getAgreeDt());
				sb.append(" custAgreeInfoRetvListDtoDetail.getCustId() ==>" + custAgreeInfoRetvListDtoDetail.getCustId());
				sb.append(" custAgreeInfoRetvListDtoDetail.getCustInfoAgreeYn() ==>" + custAgreeInfoRetvListDtoDetail.getCustInfoAgreeYn());
			}
		}
		
		return sb.toString();
	}
	
	@RequestMapping(value = "/na/shubcall11268", method = RequestMethod.GET)
	@ResponseBody
	public String call11268(String cntrNo, String agreeCd55, String agreeCd56,String agreeCd55Ver,String agreeCd56Ver, HttpServletRequest req) throws Exception
	{
		//String successYn = shubService.callFn11268(cntrNo, agreeCd55, agreeCd56, agreeCd55Ver, agreeCd56Ver);

		//return successYn;
		return null;
	}
	
	/*@RequestMapping(value = "/na/sendGcmTest", method = RequestMethod.GET)
	@ResponseBody
	public void sendGcmTest(String deviceToken) throws Exception
	{
		// AIzaSyAtWdQi3Jrd3G-2X3l9I1gG8ApQ8sN8F2A
		PushMessage message = new PushMessage();
		message.setTitle("Y 데이터박스(title)");
		message.setContent("Y 데이터박스 FCM 테스트입니다.");
		message.setLinkUrl("/yfriends/yfriends");
		message.setBadge(2);		
		message.setNotification(new Notification.Builder(null)
				 .title("YBOX TEST(notification title)")
				 .body("Y 데이터박스 FCM 테스트입니다.(original)")
				 .badge(String.valueOf(2))
				 .build());
		PushDevice device = new PushDevice();
		List<PushDevice> deviceList = new ArrayList<PushDevice>(); 
		device.setDeviceToken(deviceToken);
		deviceList.add(device);
		
		
		fcmSendTask.sendFcmTask("", message, deviceList);

	}*/
	
	@RequestMapping(value = "/na/sendFcmTest", method = RequestMethod.GET)
	@ResponseBody
	public void sendFcmTest(String deviceToken) throws Exception
	{
		// AIzaSyAtWdQi3Jrd3G-2X3l9I1gG8ApQ8sN8F2A
		FcmMessage message = new FcmMessage.Builder()
				 .collapseKey("1")
				 .timeToLive(3)
				 .priority(FcmMessage.Priority.NORMAL)
				 .mutableContent(false)
				 .dryRun(false)
				 .notification(new Notification.Builder(null)
				 .title("Y 데이터박스(fcm title)")
				 .body("Y 데이터박스 FCM 테스트입니다.")
				 .badge(String.valueOf(2))
				 .build())
				 .addData("appName", "Y 데이터박스(fcm title)")
				 .addData("content", "Y 데이터박스 FCM 테스트입니다.")
				 .addData("badge", String.valueOf(2))
				 .addData("linkurl", "/yfriends/yfriends")				
				 .build();
		
		
	
		FcmSender fcmSender = new FcmSender(serverkey);
		FcmResult result = fcmSender.send(message, deviceToken, 1);
		
		if (YappUtil.isNotEmpty(result.getMessageId())) {
            logger.info("result : " + "P");
        } else {
        	logger.info("result : " + "F");
        }
		//fcmSendTask.sendFcmTask("", message, deviceList);

	}
	
}