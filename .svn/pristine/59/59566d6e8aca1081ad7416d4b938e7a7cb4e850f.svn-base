package com.kt.yapp.aop;

import java.util.Calendar;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.kt.yapp.domain.JoinInfo;
import com.kt.yapp.domain.LampMenu;
import com.kt.yapp.domain.SessionContractInfo;
import com.kt.yapp.domain.SysCheck;
import com.kt.yapp.exception.YappAuthException;
import com.kt.yapp.exception.YappException;
import com.kt.yapp.lamp.LampLogUtil;
import com.kt.yapp.service.CmsService;
import com.kt.yapp.service.HistService;
import com.kt.yapp.service.ShubService;
import com.kt.yapp.service.UserService;
import com.kt.yapp.soap.response.SoapResponse139;
import com.kt.yapp.util.KeyFixUtil;
import com.kt.yapp.util.SessionKeeper;
import com.kt.yapp.util.YappCvtUtil;
import com.kt.yapp.util.YappUtil;
import com.kt.yapp.validator.YappVldr;

@Component
@Aspect
public class CommonAspect 
{
	@Autowired
	private ShubService shubService;
	@Autowired
	private HistService histService;
	@Autowired
	private UserService userService;
	@Autowired
	private CmsService cmsService;
	@Autowired
	private KeyFixUtil keyFixUtil;
	@Autowired
	private LampLogUtil lampLogUtil;
	private LampMenu lampMenuInfo;
	
	@Value("${server.ip.val}")
	public String severIpVal;

	private static final Logger logger = LoggerFactory.getLogger(CommonAspect.class);
	
	/**
	 * 로그인 여부를 체크한다.
	 */
	@Order(1)
//	@Around("execution(* com.kt.yapp..*Controller.*(..))")
	@Around("execution(* com.kt.yapp..*Controller.*(..)) && !@annotation(com.kt.yapp.annotation.NotCheckLogin)")
	public Object checkLogin(ProceedingJoinPoint joinPoint) throws Throwable
	{
		HttpServletRequest req = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		SessionKeeper.VAL_SERVER_IP = severIpVal;//req.getServerName();
		String apiUrl = YappUtil.nToStr(req.getRequestURI());
		String method = req.getMethod();
		
		if(logger.isInfoEnabled()) {
			logger.info("severIpVal ======================================" + severIpVal);
			logger.info("apiUrl     ======================================" + apiUrl);
			logger.info("method     ======================================" + method);
		}
		
		// URL에 "/na/" 가 포함되지 않은 경우만 체크한다.
		if ( apiUrl.indexOf("/na/") == -1 
				&& apiUrl.indexOf("/user/auth/sendmail") == -1 
				&& apiUrl.indexOf("/user/auth/checkmail") == -1
				&& apiUrl.indexOf("/yFriends/yFriends") == -1 
				&& apiUrl.indexOf("/eventMain") == -1 
				&& apiUrl.indexOf("/event/eventDetail") == -1 
				&& apiUrl.indexOf("/main") == -1 
				&& apiUrl.indexOf("/mainApi") == -1 
				&& apiUrl.indexOf("/cms/bbs/notice") == -1
				&& apiUrl.indexOf("/cms/notimsg") == -1
				&& apiUrl.indexOf("/event/evtDetail") == -1
				&& apiUrl.indexOf("/event/eventReplyList") == -1
				&& apiUrl.indexOf("/event/eventAdminReplyList") == -1
				&& apiUrl.indexOf("/event/eventIconCount") == -1
				//테스트
				//&& apiUrl.indexOf("/event/eventLikeWrite") == -1
				//&& apiUrl.indexOf("/event/eventReplyWrite") == -1
				//&& apiUrl.indexOf("/event/eventReplyModify") == -1
				
				&& apiUrl.indexOf("/cms/guide") == -1
				&& apiUrl.indexOf("/cms/event/linkapplNoLogin") == -1)
		{
			SysCheck sysCheck = cmsService.getSysChkInfo(null);
			if ( YappUtil.isNotEmpty(sysCheck) ){
				logger.info("시스템점검입니다.");
				throw new YappException("CHECK_MSG","444","시스템점검입니다.");
			}
			// 헤더의 세션ID
			String sessionId = req.getHeader(SessionKeeper.KEY_SESSION_ID);
			String autoLoginYn = req.getHeader("autoLogin");

			String reqSessionId = (String) SessionKeeper.get(req, SessionKeeper.KEY_SESSION_ID);
			if(logger.isInfoEnabled()) {
				logger.info("sessionId      ======================================" + sessionId);
				logger.info("autoLoginYn    ======================================" + autoLoginYn);
				logger.info("server session ======================================" + reqSessionId);
			}

			Cookie[] cookie = req.getCookies();
			String cookieSessionid = null;
			if(cookie != null){
				for(int i = 0; i < cookie.length; i++){
					//cookieSessionid = cookie[i].getValue();
					//logger.info("[JSessionid] = " + cookie[i].getValue());
				}
			}
			
			// 단말기에서 ysid가 안넘어오면 에러처리
			if(sessionId == null || sessionId.equals("")){
				logger.info("단말기 정보가 제대로 넘어오지 않았습니다.[ YSID is null ]");
				throw new YappAuthException("410","로그인 정보가 없습니다.");
			}
			
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
				
			}else if(tmp.length == 6){
				
				tmp = new String[7];
				tmp[0] = tmp2[0];
				tmp[1] = tmp2[1];
				tmp[2] = tmp2[2].trim();
				tmp[3] = tmp2[3];
				tmp[4] = tmp2[4];
				tmp[5] = tmp2[5];
				tmp[6] = "G0002";
			}
			
			if(autoLoginYn.equals("true")){
				
				if(YappUtil.isEmpty(tmp[0])||tmp[0].indexOf("ktid")!=-1){

					if(apiUrl.indexOf("/user/join") == -1) {
						//휴면계정 복구
						userService.sleepUserKtRestore(tmp[0], tmp[1], tmp[2], tmp[4]);
					}
					
					//mod_dt 업데이트
					userService.updateUserKtModDtInfo(tmp[2]);
					
				}else{
					
					if(apiUrl.indexOf("/user/join") == -1) {
						//휴면계정 복구
						userService.sleepUserRestore(tmp[0], tmp[1], tmp[2], tmp[4]);
					}
					
					//mod_dt 업데이트
					userService.updateUserModDtInfo(tmp[0]);
				}
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
			
			logger.info("======================================================");
			logger.info("checkLogin -> autoLoginPassYn : "+autoLoginPassYn);
			logger.info("checkLogin -> autoLoginYn : "+autoLoginYn);
			logger.info("======================================================");
			
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
			
			if(SessionKeeper.getSdata(req) != null){
				
				logger.info("SessionKeeper.getSdata(req) != null -> getMobileNo : "+SessionKeeper.getSdata(req).getMobileNo());
				logger.info("SessionKeeper.getSdata(req) != null -> getMemStatus : "+SessionKeeper.getSdata(req).getMemStatus());
				
				mobileNo = SessionKeeper.getSdata(req) == null ? null : SessionKeeper.getSdata(req).getMobileNo();
				memStatus = SessionKeeper.getSdata(req) == null ? null : SessionKeeper.getSdata(req).getMemStatus();
			}
			
			// 계약정보, 사용자 정보가 없으면 실행을 종료한다.  
			if(SessionKeeper.getSdata(req) != null){
				
				if ( YappUtil.isEmpty(mobileNo) 
						|| YappUtil.isEmpty(memStatus)
						|| (SessionKeeper.getSdata(req).isExistUser() == false && apiUrl.indexOf("/user/join") == -1) ) 
				{
					
					logger.info("==============================================================");
					logger.info("checkLogin -> mobileNo : "+mobileNo);
					logger.info("checkLogin -> isExistUser : "+SessionKeeper.getSdata(req).isExistUser());
					logger.info("checkLogin -> memStatus : "+memStatus);
					logger.info("==============================================================");
					
					logger.info("서버세션에 정보가 없습니다.");
					throw new YappAuthException("410","로그인 정보가 없습니다.");
				}
			}else{
				logger.info("서버세션에 정보가 없습니다.");
				throw new YappAuthException("410","로그인 정보가 없습니다.");
			}

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
					if(tmp[4] == null){
						logger.info("session dup id = null");
						logger.info("DB dup id = " + joinInfo.getDupId());
						logger.info("중복 로그인 불가. 다른 기기에서 로그인 되었습니다.");
						throw new YappAuthException("410","중복 로그인 불가. 다른 기기에서 로그인 되었습니다.");
					}else{
						if(!tmp[4].equals(joinInfo.getDupId())){
							logger.info("session dup id = " + tmp[4]);
							logger.info("DB dup id = " + joinInfo.getDupId());
							logger.info("중복 로그인 불가. 다른 기기에서 로그인 되었습니다.");
							throw new YappAuthException("410","중복 로그인 불가. 다른 기기에서 로그인 되었습니다.");
						}
					}
				}else{
					logger.info("DUP ID 정보가 없습니다.(해당 계약번호로 TB_USER에 정보가 없음.)");
					throw new YappAuthException("410","로그인 정보가 없습니다.");
				}
			}
			
			/** 2020.02.20 : PLAN-357 : 미사용 기간 2주 초과 시, 로그아웃 처리 로직 추가 */
			if((YappUtil.isEq("/yapp3/yboxMain", apiUrl))
				&& YappUtil.isEq("true", autoLoginYn)
				&& Long.parseLong(tmp[5]) <= Long.parseLong(YappUtil.getCurDate(null, Calendar.DAY_OF_MONTH, -14))) // 운영
			{
				logger.info("단말기 정보가 제대로 넘어오지 않았습니다.[ YSID 14-Day ERROR (create date : "+tmp[5]+")]");
				throw new YappAuthException("410", "로그인 유지 기간(2주)이 만료되었습니다.\n고객님의 소중한 정보보호를 위해 다시 로그인 해주세요.");
			}

			// 계약정보 재로딩
			SessionContractInfo cntrInfo = (SessionContractInfo) SessionKeeper.get(req, SessionKeeper.KEY_CNTR_INFO);
			if ( cntrInfo == null && mobileNo.indexOf("ktid")==-1) {
				SoapResponse139 resp = shubService.callFn139(mobileNo, false);
				SessionKeeper.addToReq(req, SessionKeeper.KEY_CNTR_INFO, YappCvtUtil.cvt(resp.getCntrInfo(), new SessionContractInfo()));
			}
		}
		
		// 접속이력 추가 //210608 위의 로그인체크로직 탄 이후로 위치변경
		try {
			if ( apiUrl.indexOf("/na/common/setmenuaccess") == -1){
				String osTp = req.getHeader("osTp");
				String appVrsn = req.getHeader("appVrsn");
				
				if(apiUrl.indexOf("/event/eventDetail") == -1 ){ //210608 서버세션에 저장된값이 없을 때 로그인체크해서 세션에 값 세팅함 //eventDetail은  eventController에 로그인 체크한 후 접속이력을 저장한다. 
					histService.insertApiAccessLog(req, osTp, appVrsn);
				}
			}
			
		} catch (RuntimeException e) {logger.warn(e.toString());}
		catch (Exception e) {logger.warn(e.toString());}
		
		
		// 파리미터 체크(파라미터의 첫번째가 체크 대상 오브젝트이어야함)
		Object[] args = joinPoint.getArgs();
		if ( YappUtil.isNotEmpty(args) )
		{
			String errMsg = YappVldr.validate(apiUrl, method, args);
			if ( YappUtil.isNotEmpty(errMsg) )
				throw new YappException("PARAM_ERROR", errMsg);
		}
		
		// 사용자 정보가 있으면 정상적으로 메소드를 실행한다.
		return joinPoint.proceed();
	}

	@AfterThrowing(pointcut="execution(* com.kt.yapp.web.UserController.*(..))", throwing = "ex")
	public void lampLogThrowingAction(JoinPoint jp, Throwable ex) {
		//lamp
		lampLogUtil.throwLampLog(ex.getMessage());
	}
	
	@Order(2)
	@Around("execution(* com.kt.yapp.web.UserController.*(..))")
	public Object lampLogJpAction(ProceedingJoinPoint joinPoint) throws Throwable {
		HttpServletRequest req = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		Object result = null;
		
		String lampUrl = req.getPathInfo();
		
		lampMenuInfo = lampLogUtil.beforeLampLog(joinPoint.getTarget().getClass().getName(), lampUrl, joinPoint);
		logger.info("lampMenuInfo :::" + lampMenuInfo.toString());
		
		
		result = joinPoint.proceed();
		
		//lamp
		lampMenuInfo = lampLogUtil.afterLampLog(result);
				
		return result;
	}
}
