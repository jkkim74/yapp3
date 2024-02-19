package com.kt.yapp.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kt.yapp.domain.AppIosTracking;
import com.kt.yapp.domain.AttachFile;
import com.kt.yapp.domain.LnbInfo;
import com.kt.yapp.domain.NoticeMsg;
import com.kt.yapp.domain.OwnAuth;
import com.kt.yapp.domain.SmsContents;
import com.kt.yapp.domain.resp.ResultInfo;
import com.kt.yapp.exception.YappAuthException;
import com.kt.yapp.exception.YappException;
import com.kt.yapp.service.CmsService;
import com.kt.yapp.service.CommonService;
import com.kt.yapp.service.HistService;
import com.kt.yapp.service.ShubService;
import com.kt.yapp.soap.response.SoapResponse086;
import com.kt.yapp.util.AppEncryptUtils;
import com.kt.yapp.util.SessionKeeper;
import com.kt.yapp.util.YappUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(description="공통 처리 컨트롤러")
public class CommonController 
{
	private static final Logger logger = LoggerFactory.getLogger(CommonController.class);
	@Autowired
	private CommonService cmnService;
	@Autowired
	private ShubService shubService;
	@Autowired
	private HistService histService;
	@Autowired
	private AppEncryptUtils appEncryptUtils;
	@Autowired
	private CmsService cmsService;
	
	private static final String AUTH_REQ = "/common/auth/req";
	private static final String SMS		 = "/common/sendsms";
	private static final String MMS		 = "/common/sendmms";
	private static final String CBC		 = "114";
	
	//210521추가
	private static final String UNKNOWN_CNTR_NO = "UNKNOWN";

	@ApiOperation(value="본인 인증 요청", notes="성공 시 인증 확인번호(Random No) 를 반환한다.")
	@RequestMapping(value = "/common/auth/req", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<String> reqAuth(@RequestBody OwnAuth ownAuth, HttpServletRequest req) throws Exception
	{
		ResultInfo<String> resultInfo = new ResultInfo<>();
		String loginMobileNo = null;
		if(SessionKeeper.getSdata(req) != null){
			loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
		}
		String rcvMobileNo = ownAuth.getAuthCd();
		logger.debug("===================");
		logger.debug("loginMobileNo ; ["+loginMobileNo+"], rcvMobileNo : [" + rcvMobileNo + "]"); 
		logger.debug("==================");
		if(loginMobileNo != null && (!loginMobileNo.equals(rcvMobileNo))){
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_NOT_MATCH_MOBILENO"));
		}
		// 로그인 휴대폰 번호 세팅
		ownAuth.setMobileNo(loginMobileNo);
		
		// 인증 요청
		ownAuth.setAccessUrl(AUTH_REQ);
		ownAuth.setCallbackCtn(CBC);
		SoapResponse086 resp = shubService.callFn086(ownAuth);
		resultInfo.setResultData(resp.getRandomNo());
		
		return resultInfo;
	}

	@ApiOperation(value="본인 인증 체크")
	@RequestMapping(value = "/common/auth/check", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<Object> checkAuth(@RequestBody OwnAuth ownAuth, HttpServletRequest req) throws Exception
	{
		ResultInfo<Object> resultInfo = new ResultInfo<>();

		// 로그인 휴대폰 번호 세팅
		if(SessionKeeper.getSdata(req) != null){
			ownAuth.setMobileNo(SessionKeeper.getSdata(req).getMobileNo());
		}
		

		// 인증 확인
		shubService.callFn087(ownAuth);
		
		return resultInfo;
	}

	@ApiOperation(value="SMS 보내기")
	@RequestMapping(value = "/common/sendsms", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<String> sendSms(@RequestBody SmsContents paramObj, HttpServletRequest req) throws Exception
	{
		paramObj.setAccessUrl(SMS);
		paramObj.setCallbackCtn(CBC);
		shubService.callFn2118(paramObj);
		return new ResultInfo<>();
	}

	@ApiOperation(value="MMS 보내기")
	@RequestMapping(value = "/common/sendmms", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<String> sendMms(@RequestBody SmsContents paramObj) throws Exception
	{
		paramObj.setAccessUrl(MMS);
		paramObj.setCallbackCtn(CBC);
		shubService.callFn2102(paramObj);
		
		return new ResultInfo<>();
	}

//	@ApiOperation(value="안내 메시지 조회")
//	@RequestMapping(value = "/common/guidemsg", method = RequestMethod.GET)
//	public ResultInfo<GuideMsg> getGuideMsg(String msgCd) throws Exception
//	{
//		return new ResultInfo<>(cmnService.getGuideMsgInfo(msgCd));
//	}

	@ApiOperation(value="파일 이미지 조회")
	@RequestMapping(value = "/na/common/view/img", method = RequestMethod.GET)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public void getImg(String fileId, HttpServletResponse resp) throws Exception
	{
		// 첨부파일 조회
		AttachFile attachFile = cmnService.getAttachFileInfo(fileId);
		if ( attachFile == null || attachFile.getFileData() == null )
			return;
		else {
			FileCopyUtils.copy(attachFile.getFileData(), resp.getOutputStream());
			resp.getOutputStream().close();
		}
	}

	@ApiOperation(value="PUSH device token 저장")
	@RequestMapping(value = "/common/settokn", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"),@ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<String> setDeviceTokn(String cntrNo, String deviceToken, String osVrsn, HttpServletRequest req) throws Exception
	{
		if(SessionKeeper.getSdata(req) != null){
			
			String memStatus = SessionKeeper.getSdata(req).getMemStatus();
			String sessCntrNo = SessionKeeper.getCntrNo(req);
			String userId = SessionKeeper.getSdata(req).getUserId();
			
			String appVrsn = req.getHeader("appVrsn");
			String osTp = req.getHeader("osTp");
			
			logger.info("==============================================================");
			logger.info("/common/settokn -> memStatus : "+memStatus);
			logger.info("/common/settokn -> cntrNo : "+sessCntrNo);
			logger.info("/common/settokn -> userId : "+userId);
			logger.info("/common/settokn -> deviceToken : "+deviceToken);
			logger.info("/common/settokn -> osVrsn : "+osVrsn);
			logger.info("/common/settokn -> osTp : "+osTp);
			logger.info("==============================================================");
			
			if(YappUtil.isEq(memStatus, "G0001")){

				cmnService.updateDeviceTokenAll(deviceToken, sessCntrNo, userId, osVrsn, appVrsn,osTp);
				
			}else if(YappUtil.isEq(memStatus, "G0002")){

				cmnService.updateDeviceToken(deviceToken, sessCntrNo, osVrsn, appVrsn);
				
			}else if(YappUtil.isEq(memStatus, "G0003")){

				cmnService.updateDeviceTokenKt(deviceToken, userId, osVrsn, appVrsn,osTp);
				
			}else{
				
				throw new YappAuthException("410","로그인 정보가 없습니다.");
			}
		}
		
		return new ResultInfo<>();
	}

	@ApiOperation(value="유입경로 저장")
	@RequestMapping(value = "/na/common/setroutelink", method = RequestMethod.GET)
	public void setRouteLink(String linkType, HttpServletRequest req) throws Exception
	{
		// 유입경로 저장시 에러가 나지 않도록 catch로 잡아줌.
		try{
			String linkTypeTmp = "";
			if(linkType.length()>2){
				linkTypeTmp = linkType.substring(0, 2);
			}else{
				linkTypeTmp = linkType;
			}
			cmnService.updateRouteLink(linkTypeTmp);
		}catch(RuntimeException e){
			logger.error("================= 유입경로 저장 ERROR ======================");
		}catch(Exception e){
			logger.error("================= 유입경로 저장 ERROR ======================");
		}
	}
	
	@ApiOperation(value="메뉴접근 저장")
	@RequestMapping(value = "/na/common/setmenuaccess", method = RequestMethod.POST)
	public void setMenuAccess(String menuId, String iosAppTrackingYn, HttpServletRequest req) throws Exception
	{
		// 유입경로 저장시 에러가 나지 않도록 catch로 잡아줌.
		try{
			histService.insertMenuAccess(menuId, req);

			if(!StringUtils.isEmpty(iosAppTrackingYn)){

				String appVrsn = req.getHeader("appVrsn");
				String osVrsn = req.getHeader("osVrsn");
				String osTp = req.getHeader("osTp");

				AppIosTracking paramObj = new AppIosTracking();
				paramObj.setAgreeYn(iosAppTrackingYn);
				paramObj.setCntrNo(SessionKeeper.getCntrNo(req));
				paramObj.setUserId(SessionKeeper.getSdata(req).getUserId());
				paramObj.setAppVrsn(appVrsn);
				paramObj.setOsVrsn(osVrsn);
				paramObj.setOsTp(osTp);
				
				if ( !YappUtil.isEmpty(paramObj.getCntrNo()) ){	//앱추적동의테이블은 계약번호로 온마시쪽과 연결됨으로 로그인안한 사용자의 정보는 저장하지 않는다.
					
					//계약번호로 IOS앱추적 DB조회
					AppIosTracking beforeParam = cmnService.getAppIosTrackingInfo(paramObj);
					
					if(beforeParam != null){
						//값변동있는지체크 없으면 아래로직 스킵(update하지않고 스킵)
						if(!paramObj.getAgreeYn().equals(beforeParam.getAgreeYn()) || !paramObj.getOsVrsn().equals(beforeParam.getOsVrsn()) || !paramObj.getAppVrsn().equals(beforeParam.getAppVrsn())){
							cmnService.updateAppIosTrackingInfo(paramObj);
						}
						
					}else{
						cmnService.insertAppIosTrackingInfo(paramObj);
					}
					
				//220406 비kt회선은 세션에 cntrNo null userid는 존재
				}else{
					if(!YappUtil.isEmpty(paramObj.getUserId())){
						//계약번호로 IOS앱추적 DB조회
						AppIosTracking beforeParam = cmnService.getAppIosTrackingInfoKt(paramObj);
						
						if(beforeParam != null){
							//값변동있는지체크 없으면 아래로직 스킵(update하지않고 스킵)
							if(!paramObj.getAgreeYn().equals(beforeParam.getAgreeYn()) || !paramObj.getOsVrsn().equals(beforeParam.getOsVrsn()) || !paramObj.getAppVrsn().equals(beforeParam.getAppVrsn())){
								cmnService.updateAppIosTrackingInfoKt(paramObj);
							}
							
						}else{
							cmnService.insertAppIosTrackingInfoKt(paramObj);
						}
					}
				}
			}
			
		}catch(RuntimeException e){
			logger.error("================= 메뉴접근 저장 ERROR ======================");
		}catch(Exception e){
			logger.error("================= 메뉴접근 저장 ERROR ======================");
		}
	}
	
	/**
	 * 20220503 LNB 정보 조회
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="LNB 정보 조회")
	@RequestMapping(value = "/na/common/lnbInfo", method = RequestMethod.GET)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<LnbInfo> getShopNotiMsgList(HttpServletRequest req) throws Exception
	{
		LnbInfo lnbInfo = new LnbInfo();
		int cnt = 0;
		
		String loginYn = cmnService.loginCheck(req);
		
		if(loginYn.equals("Y")){
			String loginCntrNo = SessionKeeper.getCntrNo(req);
			String loginUserId = null;
			String loginMobileNo = null;
			
			NoticeMsg notiMsg = new NoticeMsg();
			notiMsg.setCntrNo(loginCntrNo);
			
			logger.info("loginCntrNo : "+loginCntrNo);
			logger.info("SessionKeeper.getSdata(req) : "+SessionKeeper.getSdata(req));

					
			if(SessionKeeper.getSdata(req) != null){
				loginUserId = SessionKeeper.getSdata(req).getUserId();
				loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();

				logger.info("loginUserId : "+loginUserId);
				logger.info("loginMobileNo : "+loginMobileNo);
			}
			
			notiMsg.setUserId(loginUserId);
			
			if(YappUtil.isNotEmpty(loginCntrNo)){
				//신규 알림 건수
				cnt = cmsService.getNotiShopNotiMsgNewCount(notiMsg, true);
				
			}else{
				
				if(!YappUtil.isEmpty(loginMobileNo)){
					if(loginMobileNo.indexOf("ktid") != -1 ){
						cnt = cmsService.getNotiShopNotiMsgNewCount(notiMsg, false);
					}
				}
			}
		}
		
		
		if(cnt > 0){
			lnbInfo.setNotiNewYn("Y");
		}else{
			lnbInfo.setNotiNewYn("N");
		}
		
		return new ResultInfo<>(lnbInfo);
	}
}
