package com.kt.yapp.util;

import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;

import com.kt.yapp.domain.ApiError;
import com.kt.yapp.domain.resp.ResultInfo;
import com.kt.yapp.em.EnumRsltCd;
import com.kt.yapp.em.EnumYn;


/**
 * YappUtil.java
 * 
 * @author seungman.yu
 * @since 2018. 10. 2.
 * @version 1.0
 * 
 * Modification Information
 * Mod Date		Modifier		Description
 * ========================================
 * 2018. 10. 2.	seungman.yu 	Y Event 기능 추가
 * Copyright (c) 2018 KTDS, Inc. All Rights Reserved
 */
public class YappUtil 
{
	private static final Logger logger = LoggerFactory.getLogger(YappUtil.class);

	public static boolean isEmpty(Object obj)
	{
		if ( obj == null )					return true;
		else if (obj instanceof String)		return "".equals(obj.toString().trim());
		else if (obj instanceof List)		return ((List<?>) obj).isEmpty();
		else if (obj instanceof Map)		return ((Map<?,?>) obj).isEmpty();
		else if (obj instanceof Object[])	return Array.getLength(obj) == 0;
		else								return false;
	}
	
	public static boolean isNotEmpty(Object obj)
	{
		return ! isEmpty(obj);
	}

	public static String blindNameToTelno(String telno){
		String reName = "";
		/*String[] regex = {"[0-9]{3}[0-9]{3}[0-9]{4}", "[0-9]{3}[0-9]{4}[0-9]{4}"};
		if(Pattern.matches(regex[0], telno)){
			reName = telno.substring(0,5) + "**" + telno.substring(7); 
		}else if(Pattern.matches(regex[1], telno)){
			reName = telno.substring(0,5) + "***" + telno.substring(8);
		}*/
		if(telno.length() == 10){
			reName = telno.substring(0,5) + "**" + telno.substring(7); 
		}else if(telno.length() == 11){
			reName = telno.substring(0,5) + "***" + telno.substring(8);
		}else{
			reName = telno;
		}
		return reName;
	}
	
	public static String blindNameToName(String name, int bCnt)
	{
		if(name != null && name.length() > 20){
			name = name.substring(0, 20);
		}
		
		String reName = "";
		if(name != null && !name.equals("") && !name.equals("본인")){
			int namelen = name.length();
			double douCnt = 0d;
			if(bCnt == 1){
				douCnt = 1.3;
			}else{
				douCnt = (double)bCnt;
			}
			int blindCnt = (int)Math.round((((double)namelen/(double)3)*douCnt));

			if(namelen == 1){
				reName = name;
			}else{
				//String masking = "";
				StringBuffer sb = new StringBuffer();
				for(int i = 0 ; i < blindCnt; i++){
					//masking = masking + "*";
					sb.append("*");
				}
				//reName = name.substring(0, namelen-blindCnt) + masking;
				reName = name.substring(0, namelen-blindCnt) + sb.toString();
			}
		}

		if(name != null && name.equals("본인")){
			reName = name;
		}
		
		return reName;
	}
	
	public static String blindNameToName2(String name, int bCnt)
	{
		String reName = "";
		if(name != null && !name.equals("") && !name.equals("본인")){
			int namelen = name.length();
			double douCnt = 0d;
			if(bCnt == 1){
				douCnt = 1.3;
			}else{
				douCnt = (double)bCnt;
			}
			int blindCnt = (int)Math.round((((double)namelen/(double)3)*douCnt));

			if(namelen == 1){
				reName = name;
			}else{
				//String masking = "";
				StringBuffer sb = new StringBuffer();
				for(int i = 0 ; i < blindCnt; i++){
					//masking = masking + "*";
					sb.append("●");
				}
				//reName = name.substring(0, namelen-blindCnt) + masking;
				reName = name.substring(0, namelen-blindCnt) + sb.toString();
			}
		}

		if(name != null && name.equals("본인")){
			reName = name;
		}
		
		return reName;
	}

	/**
	 * 두값의 일치여부를 체크한다.
	 */
	public static boolean isEq(Object obj1, Object obj2)
	{
		if ( obj1 == null || obj2 == null )		return false;
		return obj1.toString().equals(obj2.toString());
	}
	public static boolean isNotEq(Object obj1, Object obj2) { return ! isEq(obj1, obj2); }

	/**
	 * 1값이 2값에 포함되는지 체크한다.
	 */
	public static boolean contains(Object obj1, Object ... obj2Arr)
	{
		if ( obj1 == null || obj2Arr == null )		return false;
		for ( Object tarObj : obj2Arr ) {
			if ( isEq(obj1, tarObj) )
				return true;
		}
		return false;
	}
	
	/**
	 * Object -> 문자열로 바꾼다. Null 이면 공백 문자열을 반환
	 */
	public static String nToStr(Object obj)
	{
		return isEmpty(obj) ? "" : obj.toString();
	}

	/**
	 * 입력된 파라미터를 저장하는 맵을 생성한다.
	 */
	public static Map<String, Object> makeParamMap(String key, Object val)
	{
		return makeParamMap(new String[]{key}, new Object[]{val});
	}
	public static Map<String, Object> makeParamMap(String[] keyArray, Object[] valArray)
	{
		Map<String, Object> paramMap = new HashMap<>();
		if ( YappUtil.isNotEmpty(keyArray) )
		{
			for ( int i = 0; i < keyArray.length; i++ ) {
				paramMap.put(keyArray[i], valArray[i]);
			}
		}
		
		return paramMap;
	}

	public static void setErrResultInfo(ResultInfo<?> resultInfo, String errMsg)
	{
		setErrResultInfo(resultInfo, EnumRsltCd.C999.getRsltCd(), errMsg);
	}
	
	public static void setErrResultInfo(ResultInfo<?> resultInfo, String rlstCd, String errMsg)
	{
		String errorMsg = "";
		resultInfo.setResultCd(rlstCd);
		if ( isEmpty(errMsg) ){
			errorMsg = "시스템 에러입니다. 잠시 후 다시 시도해주세요. 동일 문제 지속 발생시 고객센터(114)로 문의해주세요.( CODE: 999 )";
		}else{
			errorMsg = errMsg;
		}
		
		resultInfo.setResultMsg(errorMsg);
	}
	
	/**
	 * API 에러 로그 출력을 위한 파라미터 객체를 생성한다.
	 */
	public static ApiError makeApiErrInfo(HttpServletRequest req, String errCd, String errMsg, String msgTypeCd, String msgDetail, String msgKey)
	{
		String cntrNo = SessionKeeper.getCntrNo(req);
		String restApiUrl = YappUtil.nToStr(req.getRequestURL());
		String ipAddr = req.getRemoteHost();
		String userId = "UNKNOWN";
		
		//220406 user id 추가.
		if(SessionKeeper.getSdata(req) != null){
			userId = SessionKeeper.getSdata(req).getUserId();
		}
		//220406 user id 추가.
		
		// API 에러 객체 생성
		return makeApiErrInfo(cntrNo, ipAddr, restApiUrl, errCd, errMsg, msgTypeCd, msgDetail, msgKey, userId);
	}
	public static ApiError makeApiErrInfo(String cntrNo, String ipAddr, String restApiUrl, String errCd, String errMsg, String msgTypeCd, String msgDetail, String msgKey, String userId)
	{
		ApiError apiError = new ApiError();
		apiError.setCntrNo(cntrNo);
		apiError.setUserId(userId);
		apiError.setCallerIpAddr(ipAddr);
		apiError.setErrMsg(errMsg);
		apiError.setApiUrl(restApiUrl);
		
		apiError.setErrMsgDetail(msgDetail);
		apiError.setMsgTypeCd(msgTypeCd);
		apiError.setErrCd(errCd);
		apiError.setMsgKey(msgKey);

		return apiError;
	}
	
	/** 220418
	 * shop API 에러 로그 출력을 위한 파라미터 객체를 생성한다.
	 */
	public static ApiError makeApiErrInfo(HttpServletRequest req, String errCd, String errMsg, String msgTypeCd, String msgDetail, String msgKey, String userId)
	{
		String cntrNo = SessionKeeper.getCntrNo(req);
		String restApiUrl = YappUtil.nToStr(req.getRequestURL());
		String ipAddr = req.getRemoteHost();
		
		// API 에러 객체 생성
		return makeApiErrInfo(cntrNo, ipAddr, restApiUrl, errCd, errMsg, msgTypeCd, msgDetail, msgKey, userId);
	}

	/**
	 * 숫자형으로 변환한다.<br>
	 * 숫자형이 아닌 경우는 -1을 반환한다.
	 */
	public static int toInt(Object obj)
	{
		return toInt(obj, -1);
	}
	public static int toInt(Object obj, int dfNum)
	{
		try {
			return Integer.parseInt(obj.toString());
		} catch (RuntimeException e) {
			return dfNum;
		} catch (Exception e) {
			return dfNum;
		}
	}
	
	/**
	 * 숫자형으로 변환한다.<br>
	 * 숫자형이 아닌 경우는 -1을 반환한다.
	 */
	public static long toLong(Object obj)
	{
		return toLong(obj, -1);
	}
	public static long toLong(Object obj, int dfNum)
	{
		try {
			return Long.parseLong(obj.toString());
		} catch (RuntimeException e) {
			return dfNum;
		} catch (Exception e) {
			return dfNum;
		}
	}
	
	/**
	 * boolean형으로 변환한다.<br>
	 * boolean형이 아닌 경우는 false을 반환한다.
	 */
	public static boolean toBool(Object obj)
	{
		return toBool(obj, false);
	}
	public static boolean toBool(Object obj, boolean dfBool)
	{
		try {
			return Boolean.parseBoolean(obj.toString());
		} catch (RuntimeException e) {
			return dfBool;
		} catch (Exception e) {
			return dfBool;
		}
	}

	/**
	 * LPAD
	 */
	public static String lpad(Object tarObj, int len, String padStr)
	{
		String tarStr = isEmpty(tarObj) ? "" : tarObj.toString();

		//String rtnVal = tarStr;
		StringBuffer sb = new StringBuffer();
		
		for ( int i = tarStr.length(); i < len; i++ )
		{
			//rtnVal = padStr + rtnVal;
			sb.append(padStr);
		}
		sb.append(tarStr);
		return sb.toString();
	}
	
	/**
	 * 첫번째 입력값이 NULL 이면 두번째 입력값을 리턴한다.
	 */
	public static String ifNull(Object obj1, Object obj2)
	{
		return isEmpty(obj1) ? nToStr(obj2) : obj1.toString();
	}
	
	/**
	 * 입력 문자열을 Map의 값으로 치환한다.
	 */
	public static String replaceMapData(String str, Map<String, Object> paramMap)
	{
		String reStr = str;
		if ( reStr == null || paramMap == null )
			return reStr;
		
		Iterator<String> iter = paramMap.keySet().iterator();
		while ( iter.hasNext() ) 
		{
			String replaceKey = iter.next();
			reStr = reStr.replaceAll("@" + replaceKey + "@", nToStr(paramMap.get(replaceKey)));
		}
		return reStr;
	}
	
	/**
	 * 입력값중 NULL이 아닌 문자열을 찾는다.
	 */
	public static String findNotNullVal(String ... strs)
	{
		if ( strs == null )
			return null;
		
		for ( String str : strs ) {
			if ( isNotEmpty(str) )
				return str;
		}
		return null;
	}
	
	/**
	 * 지정 날짜를 지정 포맷으로 바꾸어 문자열로 반환한다.<br>
	 * 날짜 미지정시 현재 날짜가 디폴트<br>
	 * yyyyMMdd 포맷이 디폴트
	 */
	public static String getCurDate(Date date, String format)
	{
		return getCurDate(date, format, -1, -1);
	}
	public static String getCurDate(String format)
	{
		return getCurDate(format, -1, -1);
	}
	public static String getCurDate()
	{
		return getCurDate(null, -1, -1);
	}
	
	/**
	 * 지정 날짜를 지정 포맷으로 바꾸어 문자열로 반환한다.<br>
	 * 날짜 미지정시 현재 날짜가 디폴트<br>
	 * yyyyMMdd 포맷이 디폴트
	 * 
	 * @param format 포맷
	 * @param addField Calendar 필드
	 * @param addNum 현재일에 더할 날짜 수
	 */
	public static String getCurDate(String format, int addField, int addNum)
	{
		String formatDt = "";
		if ( isEmpty(format) ){
			formatDt = "yyyyMMdd";
		}else{
			formatDt = format;
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat(formatDt);
		
		Calendar curCal = Calendar.getInstance();
		if ( addField != -1 )
			curCal.add(addField, addNum);
		
		return sdf.format(curCal.getTime());
	}
	public static String getCurDate(Date date, String format, int addField, int addNum)
	{
		String formatDt = "";
		if ( isEmpty(format) ){
			formatDt = "yyyyMMdd";
		}else{
			formatDt = format;
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat(formatDt);
		
		Calendar curCal = Calendar.getInstance();
		if ( date != null )
			curCal.setTime(date);
		
		if ( addField != -1 )
			curCal.add(addField, addNum);
		
		return sdf.format(curCal.getTime());
	}
	
	public static Date getDate(String format, String dateStr)
	{
		String formatDt = "";
		if ( isEmpty(format) ){
			formatDt = "yyyyMMdd";
		}else{
			formatDt = format;
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat(formatDt);
		
		Date rtnDate = null;
		try {
			rtnDate = sdf.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			logger.error("error : " + e);
		}
		
		return rtnDate;
	}

	public static long getDayDistance( String startDate, String endDate ) {
		return getDayDistance( startDate, endDate, null );
	}

	public static long getDayDistance( String startDate, String endDate, String format ){
		String formatDt = "";
		if( format == null ){
			formatDt = "yyyyMMdd";
		}else{
			formatDt = format;
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat( formatDt );
		Date sDate = null;
		Date eDate = null;
		long day2day = 0;
	
		try {
			sDate = sdf.parse( startDate );
			eDate = sdf.parse( endDate );
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			logger.error("error : " + e);
		}

		day2day = (eDate.getTime() - sDate.getTime() ) / (1000*60*60*24);

		return Math.abs( day2day );
	}
	/**
	 * 기준일시로부터 대상일시가 지정 시간(분)을 지났는지 체크한다.
	 */
	public static boolean isPassedTime(Date tarDate, int minute, Date baseDate)
	{
		// 대상일시가 없으면 true 반환
		if ( tarDate == null )
			return true;
		
		Calendar baseCal = Calendar.getInstance();
		// 기준일시가 없으면 현재일시를 기준일시로 한다.
		if ( baseDate != null )
			baseCal.setTime(baseDate);
		
		Calendar tarCal = Calendar.getInstance();
		tarCal.setTime(tarDate);
		tarCal.add(Calendar.MINUTE, minute);

		if ( baseCal.getTimeInMillis() >= tarCal.getTimeInMillis() )
			return true;
		else
			return false;
	}
	
	/**
	 * UUID 랜덤 문자열을 생성한다.
	 */
	public static String getUUIDStr()
	{
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/** 입력값이 "Y" 인지 체크한다. */
	public static boolean isY(Object obj) {	return isEq(obj, EnumYn.C_Y.getValue()); }
	/** 입력값이 "N" 인지 체크한다. */
	public static boolean isN(Object obj) {	return isEq(obj, EnumYn.C_N.getValue()); }
	/** 입력값이 "1" 인지 체크한다. */
	public static boolean is1(Object obj) {	return isEq(obj, EnumYn.C_1.getValue()); }
	
	public static String encode(String str)
	{
		return Base64Utils.encodeToString(str.getBytes());
	}
	
	public static String decode(String str)
	{
		return new String(Base64Utils.decodeFromString(str));
	}
	
	/**
	 * 14세 여부(Y, N)을 반환한다.
	 */
	public static String checkFourteenYn(String birthStr)
	{
		try {
/*			Date birthDate = YappUtil.getDate("yyyyMMdd", birthStr);
			Calendar birthCal = Calendar.getInstance();
			birthCal.setTime(birthDate);
			Calendar curCal = Calendar.getInstance();
			
			if ( curCal.get(Calendar.YEAR) - birthCal.get(Calendar.YEAR) < 13)
				return EnumYn.C_Y.getValue();
			else
				return EnumYn.C_N.getValue();*/
			int age = getAge(Integer.parseInt(birthStr.substring(0, 4)), Integer.parseInt(birthStr.substring(4, 6)), Integer.parseInt(birthStr.substring(6, 8)));
			if ( age < 14){
				return EnumYn.C_Y.getValue();
			}else{
				return EnumYn.C_N.getValue();
			}
		} catch (RuntimeException e) {
			//e.printStackTrace();
			logger.error("error : " +e);
			return EnumYn.C_Y.getValue();
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("error : " +e);
			return EnumYn.C_Y.getValue();
		}
	}

	/**
	 * 18세 여부(Y, N)을 반환한다.
	 */
	public static String checkEightteenYn(String birthStr)
	{
		try { 
			int age = getAge(Integer.parseInt(birthStr.substring(0, 4)), Integer.parseInt(birthStr.substring(4, 6)), Integer.parseInt(birthStr.substring(6, 8)));
			if ( age < 18){
				return EnumYn.C_Y.getValue();
			}else{
				return EnumYn.C_N.getValue();
			}
		} catch (RuntimeException e) {
			//e.printStackTrace();
			logger.error("error : " +e);
			return EnumYn.C_Y.getValue();
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("error : " +e);
			return EnumYn.C_Y.getValue();
		}
	}

	/**
	 * 19세 여부(Y, N)을 반환한다.
	 */
	public static String checkNineteenYn(String birthStr)
	{
		try { 
			int age = getAge(Integer.parseInt(birthStr.substring(0, 4)), Integer.parseInt(birthStr.substring(4, 6)), Integer.parseInt(birthStr.substring(6, 8)));
			if ( age < 19){
				return EnumYn.C_Y.getValue();
			}else{
				return EnumYn.C_N.getValue();
			}
		} catch (RuntimeException e) {
			//e.printStackTrace();
			logger.error("error : " +e);
			return EnumYn.C_Y.getValue();
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("error : " +e);
			return EnumYn.C_Y.getValue();
		}
	}

	/**
	 * 29세 여부(Y, N)을 반환한다.
	 */
	public static String checkTwentyNineYn(String birthStr)
	{
		try { 
			/**
			 * 2019.12.26 : 만 29세까지 허용 (29 이하 허용)
			 */
			// int age = getAge(Integer.parseInt(birthStr.substring(0, 4)), Integer.parseInt(birthStr.substring(4, 6)), Integer.parseInt(birthStr.substring(6, 8)));
			int age = getAgeYear(Integer.parseInt(birthStr.substring(0, 4)));
			
			/** 2020.01.30 : 만 29세까지 허용 (생일 안 지난 만 나이로 계산) : PLAN-433 
			 *  즉, 무조건 생일 안 지난 만 나이로 계산 
			 */
			age--;
			
			// SIT : 129, REAL : 29
			if ( age <= 29){
				return EnumYn.C_Y.getValue();
			}else{
				return EnumYn.C_N.getValue();
			}
		} catch (RuntimeException e) {
			//e.printStackTrace();
			logger.error("error : " +e);
			return EnumYn.C_Y.getValue();
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("error : " +e);
			return EnumYn.C_Y.getValue();
		}
	}
	
	public static int getAgeYear(int bYear){
		int age = 0;

		Calendar current = Calendar.getInstance();
		int cYear = current.get(Calendar.YEAR);
		
		age = cYear - bYear;

		return age;
	}
	
	public static int getAgeYear(String birthDay){
		
		logger.info("==============================================================");
		logger.info("YappUtil -> getAgeYear : "+birthDay);
		logger.info("==============================================================");
		
		int birth = Integer.parseInt(birthDay);
		int age = 0;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		Calendar current = Calendar.getInstance();
		String strToday = sdf.format(current.getTime());
		int today = Integer.parseInt(strToday);
		
		age = (today - birth)/10000;
		
		logger.info("==============================================================");
		logger.info("YappUtil -> birth : "+birth);
		logger.info("YappUtil -> today : "+today);
		logger.info("YappUtil -> age : "+age);
		logger.info("==============================================================");

		return age;
	}
	
	public static String getAgeGroupYear(String birthDay){
		
		logger.info("==============================================================");
		logger.info("YappUtil -> getAgeGroupYear : "+birthDay);
		logger.info("==============================================================");
		
		int birth = Integer.parseInt(birthDay);
		int age = 0;
		
		String strAge = "";
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		Calendar current = Calendar.getInstance();
		String strToday = sdf.format(current.getTime());
		int today = Integer.parseInt(strToday);
		
		age = (today - birth)/10000;
		
		logger.info("==============================================================");
		logger.info("YappUtil -> birth : "+birth);
		logger.info("YappUtil -> today : "+today);
		logger.info("YappUtil -> age : "+age);
		logger.info("==============================================================");
		
		if(age >= 10 && age < 20){
			strAge = "10";
		}else if(age >= 20 && age < 30){
			strAge = "20";
		}else if(age >= 30 && age < 40){
			strAge = "30";
		}else if(age >= 40 && age < 50){
			strAge = "40";
		}else if(age >= 50 && age < 60){
			strAge = "50";
		}else if(age >= 60 && age < 70){
			strAge = "60";
		}

		return strAge;
	}
	
	//211108 연도 나이로 변경
	public static int getAge(int bYear, int bMonth, int bDay){
		int age = 0;

		Calendar current = Calendar.getInstance();
		int cYear = current.get(Calendar.YEAR);

		age = cYear - bYear;
		
		return age;
	}

	public static String replaceCpnNm(String cpnNm){
		String reCpnNm = "";
		int indexNum = cpnNm.indexOf("(");
		if(indexNum > -1){
			reCpnNm = cpnNm.substring(0, indexNum);
		}else{
			reCpnNm = cpnNm;
		}
		return reCpnNm;
	}

	public static String getRefacTelno(String telno){
		String reTelno = "";

		if( telno == null || telno.equals("") || telno.length() == 0){
			return reTelno;
		}

		if(telno.length() == 12){
			String reTelnoTmp = telno.substring(1);
			if(reTelnoTmp.substring(3,4).equals("0") ){
				reTelno = reTelnoTmp.substring(0, 3) + reTelnoTmp.substring(4); 
			}else{
				reTelno = reTelnoTmp;
			}
		}else if(telno.length() == 11){
			if(telno.substring(3,4).equals("0") ){
				reTelno = telno.substring(0, 3) + telno.substring(4); 
			}else{
				reTelno = telno;
			}
		}else{
			reTelno = telno;
		}

		return reTelno;
	}
	
	/*public static void main(String[] args) {
		logger.info(encode("Content-Type: text/plain; charset='euc-kr' Content-ID :mmsToVasContent MO "));
		logger.info(lpad(null, 5, "T"));
	}*/
	
	/**
	 * 마스킹 처리
	 * @param type :I(아이디), N(성명), E(이메일), T(전화번호), A(주소)
	 * @param value
	 * @return
	 */
	public static String dataToMask(String type, String value) {
		String regEx = "";
    	String retVal = value;
     
    	if(!"".equals(retVal) && retVal != null) {
    		if ("I".equals(type)) { //아이디
    			retVal = retVal.substring(0, retVal.length()-3).concat("***");
    		}else if ("BI".equals(type)) { //아이디 동그라미 마스킹
    			retVal = retVal.substring(0, retVal.length()-3).concat("●●●");
    		}else if ("N".equals(type)) { // 성명 마스킹
    			retVal = rpad(retVal.substring(0, retVal.length()-retVal.length()/3), '*', retVal.length());
    		}else if ("BN".equals(type)) { // 성명 동그라미 마스킹
    			retVal = rpad(retVal.substring(0, retVal.length()-retVal.length()/3), '●', retVal.length());
    		}else if ("E".equals(type)) { // 이메일 마스킹
    			if (retVal.indexOf("@") >= 3) {
    				retVal = retVal.substring(0, retVal.indexOf("@")-3).concat("***").concat(retVal.substring(retVal.indexOf("@")));
    			} else if (retVal.indexOf("@") != -1) {
    				retVal =  "***".concat(retVal.substring(retVal.indexOf("@")));
    			}
    		}else if ("T".equals(type)) { // 전화번호 마스킹
    			String[] aryVal = retVal.split("-");
    			retVal = retVal.replaceAll("-", "");
    			
    			if(retVal.length() == 11) {
					regEx = "(\\d{3})(\\d{3,4})(\\d{4})";
				}else if(value.length() == 10) {
					regEx = "(\\d{2})(\\d{3,4})(\\d{4})";
				}else {		//예외
					return retVal;
				}
				
				if(!Pattern.matches(regEx, retVal)) return value;
				
				if(aryVal.length == 3) {
					retVal = retVal.replaceAll(regEx, "$1-$2-$3");
				       
	    			String first = retVal.substring(0, retVal.length()-7);
	    			String third = retVal.substring(retVal.length()-3);
	    			retVal = first.concat("**-*").concat(third);
				}else {
					String tempStr = "";
					tempStr = retVal.replaceAll(regEx, "$1");
					tempStr += rpad(retVal.replaceAll(regEx, "$2").substring(0, retVal.replaceAll(regEx, "$2").length()-2), '*', retVal.replaceAll(regEx, "$2").length());
					tempStr += "*" + retVal.replaceAll(regEx, "$3").substring(1, retVal.replaceAll(regEx, "$3").length());
					retVal = tempStr;
				}
			}else if ("A".equals(type)) { //주소
				String[] aryVal = retVal.split(" ");
				//System.out.println("len:" + aryVal.length);
				String tmpVal = "";
				if(aryVal.length > 2) {
					for(int i=0; i < aryVal.length; i++) {
						if(i == 2) {
							break;
						}
						if(i > 0) {
							tmpVal = tmpVal.concat(" ");
						}
						tmpVal = tmpVal.concat(aryVal[i]);
					}
					retVal = rpad(tmpVal, '*', value.length());
				}else {
					retVal = value;
				}
    		}
    	}
     
    	return retVal;
	}
	
	/**
	 * RPAD
	 */
	public static String rpad(String text, char filter, int size) {
		StringBuffer result = new StringBuffer(text);
		while(result.length() < size) {
			result.append(filter);
		}
		
		return result.toString();
	}
	
	public static String getServerHostName() {
		String hostname = "c-plany-pk1-a01";
		
		try {
			InetAddress addr;
			addr = InetAddress.getLocalHost();
			hostname = addr.getHostName();
		} catch (UnknownHostException e) {
			logger.error("error:" + e.getMessage());
		}
		
		return hostname;
	}
	
	public static String getTodayYmd(int year, int month, int day){
		SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");		
		
		Calendar cal = Calendar.getInstance();		
		cal.add(cal.YEAR, year);
		cal.add(cal.MONTH, month);
		cal.add(cal.DATE, day);		
		
		String thisday = format1.format(cal.getTime());
		return thisday;
	}
	
	public static Date changeStringToDate(String date) throws ParseException{
		SimpleDateFormat fm = new SimpleDateFormat("yyyyMMdd");
		Date to = fm.parse(date);
		return to;
	}
	
	/**
	 * 211221 KOS 에러 메시지
	 * @param rtnCd
	 * @return
	 */
	public static String kosResultErrMsg(String rtnCd){
		String kos_err_msg = "";
		
		switch(rtnCd){
		case "PRDE0001":
			kos_err_msg="KOS_ERROR_PRDE0001";
			break;
		case "CMNE0014":
			kos_err_msg="KOS_ERROR_CMNE0014";
			break;
		case "CMNE0010":
			kos_err_msg="KOS_ERROR_CMNE0010";
			break;
		case "CMNE0041":
			kos_err_msg="KOS_ERROR_CMNE0041";
			break;
		case "CMNE0042":
			kos_err_msg="KOS_ERROR_CMNE0042";
			break;
		default :
			kos_err_msg="KOS_ERROR_DEFAULT";
			break;
		}
		
		return kos_err_msg;
	}
	
	/** 220607
	 * 모바일번호 중간 2글자 마스킹처리
	 * @param mobileNo
	 * @return
	 */
	public static String blindMidEndMobileNo(String mobileNo){
		if(isEmpty(mobileNo)){ return ""; }

		String blindMobileNo = mobileNo;
		String num1 = "";
		String num3 = "";
		
		if(blindMobileNo.contains("-")){
			blindMobileNo = blindMobileNo.replaceAll("[^0-9]", "");
		}
		
		if(blindMobileNo.length() > 11 || blindMobileNo.length() < 10){
			blindMobileNo = "";
		}else{
			if(blindMobileNo.length() == 11){
				num1 = blindMobileNo.substring(0, 5);
				num3 = blindMobileNo.substring(8, 11);
				
			}else{
				num1 = blindMobileNo.substring(0, 4);
				num3 = blindMobileNo.substring(7, 10);
				
			}
			
			blindMobileNo = num1 + "***"+ num3;
		}
		
		return blindMobileNo;
	}
	
	/** 220607
	 * 모바일번호 끝 1글자 마스킹처리
	 * @param mobileNo
	 * @return
	 */
	public static String blindEndMobileNo(String mobileNo){
		if(isEmpty(mobileNo)){ return ""; }
		
		String blindMobileNo = mobileNo;
		String num1 = "";
		String num3 = "";
		
		if(blindMobileNo.contains("-")){
			blindMobileNo = blindMobileNo.replaceAll("[^0-9]", "");
		}
		
		if(blindMobileNo.length() > 11 || blindMobileNo.length() < 10){
			blindMobileNo = "";
		}else{
			if(blindMobileNo.length() == 11){
				num1 = blindMobileNo.substring(0, 7);
				num3 = blindMobileNo.substring(8, 11);
				
			}else{
				num1 = blindMobileNo.substring(0, 6);
				num3 = blindMobileNo.substring(7, 10);
				
			}
			
			blindMobileNo = num1 + "*"+ num3;
		}
		
		return blindMobileNo;
	}
	
	public static String setStringNumber (int count){
		
		if(count >=1000){
			return (count/1000) + "." + Math.round(count%1000/100) + "K";
		}else{
			return count+"";
		}
	}

	public static boolean isBetweenDate(String startDate, String endDate, String strFormat) throws Exception{
		boolean bResult = false;
		SimpleDateFormat format = new SimpleDateFormat(strFormat);
		Date dtToday = format.parse(format.format(new Date()));
		Date dtStart = format.parse(startDate);
		Date dtEnd = format.parse(endDate);

		if(dtToday.compareTo(dtStart) >= 0 && dtEnd.compareTo(dtToday) >= 0) {
			bResult = true;
		}
		return bResult;
	}
}
