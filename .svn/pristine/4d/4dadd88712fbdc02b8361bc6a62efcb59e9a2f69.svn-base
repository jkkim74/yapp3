package com.kt.yapp.util;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.kt.yapp.domain.SessionContractInfo;
import com.kt.yapp.domain.SessionData;

public class SessionKeeper
{
	/** 세션 ID KEY **/
	public static final String KEY_SESSION_ID = "ysid";
	
	/** 계약정보 KEY **/
	public static final String KEY_CNTR_INFO = "KEY_CNTR_INFO";

	/** 계약목록 KEY **/
	public static final String KEY_CNTR_LIST = "KEY_CNTR_LIST";
	
	/** 계약목록 KEY **/
	public static final String KEY_SESSION_MAP = "KEY_SESSION_MAP";

	/** 휴대폰번호(휴대폰 로그인 인증시 사용) **/
	public static final String KEY_AUTH_MOBILE_NO = "KEY_AUTH_MOBILE_NO";
	/** 휴대폰 로그인에서 인증요청한 임시번호 */
	public static final String KEY_AUTH_NUM = "KEY_AUTH_NUM";

	/** 추가인증을 위한  임시번호 */
	public static final String KEY_AUTH_SMS_NUM = "KEY_AUTH_SMS_NUM";
	
	/** 중복로그인 key*/
	public static final String KEY_DUP_ID = "KEY_DUP_ID";
	/** Server IP **/
	public static String VAL_SERVER_IP;
	
	/**
	 * 세션 데이터를 조회한다.
	 */
	public static SessionData getSdata(HttpServletRequest req) 
	{
		String sessionId = (String)req.getSession().getAttribute(KEY_SESSION_ID);
		SessionData sessionInfo = (SessionData) req.getSession().getAttribute(KEY_SESSION_MAP);
		if ( sessionInfo == null )
			return null;
		
		sessionInfo.setUpdDt(new Date());
		
		return sessionInfo;
	}

	public static void setSessionId(HttpServletRequest req, String sessionId) {
		req.getSession().setAttribute(KEY_SESSION_ID, sessionId);
		req.getSession().setAttribute(KEY_SESSION_MAP, new SessionData());
	}

	public static boolean containsSessionId(HttpServletRequest req, String sessionId)
	{
		String sessionIdKey = (String)req.getSession().getAttribute(KEY_SESSION_ID);
		if(sessionIdKey != null && sessionIdKey.equals(sessionId)){
			return true;
		}
		return false;
	}

	public static SessionContractInfo getCntrInfo(HttpServletRequest req) 
	{
		return (SessionContractInfo) req.getSession().getAttribute(KEY_CNTR_INFO);
	}

	public static String getCntrNo(HttpServletRequest req) 
	{
		SessionContractInfo sCntrInfo = (SessionContractInfo) req.getSession().getAttribute(KEY_CNTR_INFO);
		return sCntrInfo == null ? null : sCntrInfo.getCntrNo();
	}

	/**
	 * 세션에 저장된 계약 목록에서 계약 정보를 조회한다.
	 */
	public static SessionContractInfo getCntrInfo(HttpServletRequest req, String cntrNo)
	{
		List<SessionContractInfo> cntrInfoList = (List<SessionContractInfo>) req.getSession().getAttribute(KEY_CNTR_LIST);
		if ( cntrInfoList != null )
		{
			for ( SessionContractInfo cntrInfo : cntrInfoList )
			{
				if ( YappUtil.isEq(cntrNo, cntrInfo.getCntrNo()) )
					return cntrInfo;
			}
		}
		return null;
	}

	/**
	 * 세션에 값을 저장한다.
	 */
	public static void addToReq(final HttpServletRequest req, final String key, Object value)
	{
		addToReq(req, key, value, 0);
	}
	public static void addToReq(final HttpServletRequest req, final String key, Object value, long delaySecond)
	{
		req.getSession().setAttribute(key, value);
	}

	/**
	 * 세션에 저장한 값을 조회한다.
	 */
	public static Object get(HttpServletRequest req, String key)
	{
		return req.getSession().getAttribute(key);
	}

	/**
	 * 세션에 저장된 값을 삭제한다.
	 */
	public static void remove(HttpServletRequest req, String key)
	{
		req.getSession().setAttribute(key, null);
	}

	/**
	 * 로그아웃 처리한다.
	 */
	public static void logout(HttpServletRequest req)
	{
		remove(req, KEY_SESSION_ID);
		remove(req, KEY_SESSION_MAP);
		remove(req, KEY_CNTR_INFO);
		remove(req, KEY_CNTR_LIST);
		remove(req, KEY_AUTH_MOBILE_NO);
		req.getSession().invalidate();
	}
	
}
