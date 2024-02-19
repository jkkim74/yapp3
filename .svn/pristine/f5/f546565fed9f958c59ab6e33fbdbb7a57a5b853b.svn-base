package com.kt.yapp.service;

import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kt.yapp.aop.CommonAspect;
import com.kt.yapp.common.repository.CommonDao;
import com.kt.yapp.domain.ApiAccess;
import com.kt.yapp.domain.ApiError;
import com.kt.yapp.domain.MemPointGet;
import com.kt.yapp.domain.SnsShare;
import com.kt.yapp.em.EnumRsltCd;
import com.kt.yapp.util.SessionKeeper;
import com.kt.yapp.util.YappUtil;

@Service
public class HistService
{
	@Autowired
	private CommonService cmnService;
	@Autowired
	private CommonDao cmnDao;

	private static final String UNKNOWN_CNTR_NO = "UNKNOWN";
	private static final Logger logger = LoggerFactory.getLogger(HistService.class);

	/**
	 * 접속 로그를 추가한다.
	 */
	public int insertApiAccessLog(HttpServletRequest req, String osTp, String appVrsn)
	{
		// server ip + jvmRoute name
		String serverIp = req.getServerName();
		String sessionId = req.getSession() == null ? null : YappUtil.nToStr(req.getSession().getId());
		
		if ( sessionId != null && (sessionId.indexOf(".") > -1) ){
			if(sessionId != null) {
				serverIp = serverIp + "." + sessionId.substring(sessionId.indexOf(".") + 1);
			}
			
		}else{
			
			//wildfly 14 jvmRoute 20220228
			String jvmRoute = "";
			Properties p = System.getProperties();
			Enumeration keys = p.keys();
			while(keys.hasMoreElements()){
				String key = (String)keys.nextElement();
				if(key.equals("jboss.jvmRoute")){
					jvmRoute = (String)p.get(key);
					break;
				}
			}
			
			if(!jvmRoute.equals("")){
				serverIp = serverIp + "."+ jvmRoute;
			}
			
			//wildfly 14 jvmRoute 20220228
			
		}
		
		//220406 user id 적재 추가.
		String cntrNo = SessionKeeper.getCntrNo(req);
		String userId = "UNKNOWN";
		
		if(SessionKeeper.getSdata(req) != null){
			userId = SessionKeeper.getSdata(req).getUserId();
			logger.info("userId : "+userId);
		}
		//220406 user id 적재 추가.
		
		return insertApiAccessLog(cntrNo, YappUtil.nToStr(req.getRequestURL()), req.getRemoteAddr(), serverIp, osTp, appVrsn, userId);
	}
	public int insertApiAccessLog(String cntrNo, String apiUrl, String accessIpAddr, String serverIpAddr, String osTp, String appVrsn, String userId)
	{
		ApiAccess paramObj = new ApiAccess();
		paramObj.setCntrNo(cntrNo);
		paramObj.setUserId(userId);
		paramObj.setApiUrl(apiUrl);
		paramObj.setAccessIpAddr(accessIpAddr);
		paramObj.setAccessServerIpAddr(serverIpAddr);
		paramObj.setOsTp(osTp);
		paramObj.setAppVrsn(appVrsn);
		
		if ( YappUtil.isEmpty(paramObj.getUserId()) ){
			paramObj.setUserId(UNKNOWN_CNTR_NO);
		}
		
		if ( YappUtil.isEmpty(paramObj.getCntrNo()) )
			paramObj.setCntrNo(UNKNOWN_CNTR_NO);
		
		return cmnDao.insert("mybatis.mapper.hist.insertApiAccessLog", paramObj);
	}
	
	/**20220629
	 * shop api 접근이력 추가
	 */
	public int insertShopApiAccessLog(HttpServletRequest req, String userId)
	{
		// server ip + jvmRoute name
		String serverIp = req.getServerName();
		
		//wildfly 14 jvmRoute 20220228
		String jvmRoute = "";
		Properties p = System.getProperties();
		Enumeration keys = p.keys();
		while(keys.hasMoreElements()){
			String key = (String)keys.nextElement();
			if(key.equals("jboss.jvmRoute")){
				jvmRoute = (String)p.get(key);
				break;
			}
		}
		
		if(!jvmRoute.equals("")){
			serverIp =serverIp + "."+ jvmRoute;
		}
		//wildfly 14 jvmRoute 20220228
		
		ApiAccess paramObj = new ApiAccess();
		paramObj.setUserId(userId);
		paramObj.setApiUrl(YappUtil.nToStr(req.getRequestURL()));
		paramObj.setAccessIpAddr(req.getRemoteAddr());
		paramObj.setAccessServerIpAddr(serverIp);
		paramObj.setCntrNo(UNKNOWN_CNTR_NO);
		
		return cmnDao.insert("mybatis.mapper.hist.insertApiAccessLog", paramObj);
	}
	
	
	/**
	 * 에러 로그를 추가한다.
	 */
	public void insertApiErrLog(ApiError paramObj, HttpServletRequest req)
	{
		if ( YappUtil.isEmpty(paramObj.getUserId()) )
			paramObj.setUserId(UNKNOWN_CNTR_NO);
		
		if ( YappUtil.isEmpty(paramObj.getCntrNo()) )
			paramObj.setCntrNo(UNKNOWN_CNTR_NO);
		
		if ( YappUtil.isEmpty(paramObj.getErrMsg()) )
			paramObj.setErrMsg(cmnService.getMsg("SYSTEM_ERROR"));
		
		String serverIp = req.getServerName();
		String sessionId = req.getSession() == null ? null : YappUtil.nToStr(req.getSession().getId());
		if ( sessionId != null  && sessionId.indexOf(".") > -1 ){
			serverIp = serverIp + "." + sessionId.substring(sessionId.indexOf(".") + 1);
		}else{
			
			//wildfly 14 jvmRoute 20220629
			String jvmRoute = "";
			Properties p = System.getProperties();
			Enumeration keys = p.keys();
			while(keys.hasMoreElements()){
				String key = (String)keys.nextElement();
				if(key.equals("jboss.jvmRoute")){
					jvmRoute = (String)p.get(key);
					break;
				}
			}
			
			if(!jvmRoute.equals("")){
				serverIp = serverIp + "."+ jvmRoute;
			}
			
			//wildfly 14 jvmRoute 20220228
			
		}
		paramObj.setAccessServerIpAddr(serverIp);
		cmnDao.insert("mybatis.mapper.hist.insertApiErrLog", paramObj);
	}
	/**
	 * 에러 로그(관제테이블)를 추가한다.
	 */
	public void insertErrControl(ApiError paramObj, HttpServletRequest req)
	{
		if ( YappUtil.isEmpty(paramObj.getCntrNo()) )
			paramObj.setCntrNo(UNKNOWN_CNTR_NO);
		
		if ( YappUtil.isEmpty(paramObj.getErrMsg()) )
			paramObj.setErrMsg(cmnService.getMsg("SYSTEM_ERROR"));
		
		if ( YappUtil.isEmpty(paramObj.getUserId()) ){
			paramObj.setUserId(UNKNOWN_CNTR_NO);
		}
		
		String serverIp = req.getServerName();
		String sessionId = req.getSession() == null ? null : YappUtil.nToStr(req.getSession().getId());
		if ( sessionId != null && sessionId.indexOf(".") > -1 ){
			serverIp = serverIp + "." + sessionId.substring(sessionId.indexOf(".") + 1);
		}else{
			
			//wildfly 14 jvmRoute 20220629
			String jvmRoute = "";
			Properties p = System.getProperties();
			Enumeration keys = p.keys();
			while(keys.hasMoreElements()){
				String key = (String)keys.nextElement();
				if(key.equals("jboss.jvmRoute")){
					jvmRoute = (String)p.get(key);
					break;
				}
			}
			
			if(!jvmRoute.equals("")){
				serverIp = serverIp + "."+ jvmRoute;
			}
			
			//wildfly 14 jvmRoute 20220228
			
		}
		
		paramObj.setAccessServerIpAddr(serverIp);
		cmnDao.insert("mybatis.mapper.hist.insertErrControl", paramObj);
	}
	
	/**
	 * Redis Error 
	 */
	public void insertRedisErr(HttpServletRequest req)
	{
		String cntrNo = SessionKeeper.getCntrNo(req);
		String userId = "UNKNOWN";
		if(SessionKeeper.getSdata(req) != null){
			userId = SessionKeeper.getSdata(req).getUserId();
			logger.info("redis userId : "+userId);
		}
		
		String restApiUrl = YappUtil.nToStr(req.getRequestURL());
		String ipAddr = req.getRemoteHost();
		ApiError apiError = new ApiError();
		apiError.setCntrNo(cntrNo);
		apiError.setUserId(userId); //220406
		apiError.setCallerIpAddr(ipAddr);
		apiError.setErrMsg("REDIS 접속 이상");
		apiError.setErrMsgDetail("REDIS 접속 이상");
		apiError.setApiUrl(restApiUrl);
		apiError.setMsgTypeCd("REDIS_ERROR");
		apiError.setErrCd(EnumRsltCd.C999.getRsltCd());
		insertErrControl(apiError, req);
	}

	/**
	 * SNS 공유 정보를 추가한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int insertSnsShareInfo(SnsShare paramObj) throws Exception
	{
		return cmnDao.insert("mybatis.mapper.hist.insertSnsShareInfo", paramObj);
	}

	/**
	 * 포인트 전송내역을 추가한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int insertPointSndInfo(MemPointGet paramObj) throws Exception
	{
		return cmnDao.insert("mybatis.mapper.hist.insertPointSndInfo", paramObj);
	}
	
	/**
	 * 메뉴접근 정보를 추가한다.
	 */
	public void insertMenuAccess(String menuId, HttpServletRequest req)
	{
		String serverIp = req.getServerName();
		String sessionId = req.getSession() == null ? null : YappUtil.nToStr(req.getSession().getId());
		
		if ( sessionId != null && sessionId.indexOf(".") > -1 ){
			if(sessionId != null) {
				serverIp = serverIp + "." + sessionId.substring(sessionId.indexOf(".") + 1);
			}
		}else{
			
			//wildfly 14 jvmRoute 20220228
			String jvmRoute = "";
			Properties p = System.getProperties();
			Enumeration keys = p.keys();
			while(keys.hasMoreElements()){
				String key = (String)keys.nextElement();
				if(key.equals("jboss.jvmRoute")){
					jvmRoute = (String)p.get(key);
					break;
				}
			}
			
			if(!jvmRoute.equals("")){
				serverIp = serverIp + "."+ jvmRoute;
			}
			
			//wildfly 14 jvmRoute 20220228
			
		}
		
		//220406 user id 추가.
		String cntrNo = SessionKeeper.getCntrNo(req);
		String userId = "UNKNOWN";
		if(SessionKeeper.getSdata(req) != null){
			userId = SessionKeeper.getSdata(req).getUserId();
			logger.info("insertMenuAccess userId : "+userId);
		}
		//220406 user id 추가.
		
		ApiAccess paramObj = new ApiAccess();
		paramObj.setCntrNo(cntrNo);
		paramObj.setUserId(userId);
		paramObj.setApiUrl(YappUtil.nToStr(menuId));
		paramObj.setAccessIpAddr(req.getRemoteAddr());
		paramObj.setAccessServerIpAddr(serverIp);
		
		if ( YappUtil.isEmpty(paramObj.getCntrNo()) ){
			paramObj.setCntrNo(UNKNOWN_CNTR_NO);
		}
		
		if ( YappUtil.isEmpty(paramObj.getUserId()) ){
			paramObj.setUserId(UNKNOWN_CNTR_NO);
		}
		
		cmnDao.insert("mybatis.mapper.hist.insertApiAccessLog", paramObj);
	}
}
