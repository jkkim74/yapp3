package com.kt.yapp.web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.kt.yapp.service.UserService;
import com.kt.yapp.util.YappUtil;

import junit.framework.TestCase;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class YappTestNormalUnit extends TestCase 
{
//	private static String URL = "http://localhost:8080/yapp";
//	private static String URL = "http://10.217.81.117/yapp";
//	private static String URL = "http://14.63.149.227/yapp";
	private static String URL = "http://14.63.149.114/yapp";
	@Autowired
	private UserService service;
	
	private static final Logger logger = LoggerFactory.getLogger(YappTestNormal.class);
	/*
	protected void setUp() throws Exception {
		super.setUp();
	}

	private static Map<String, Object> PARAM_MAP = new HashMap<String, Object>() {
	{
//			put("userId", "ywd022");	put("pwd", "family2@");		put("cnt", 1);	put("mobileNo", "01073860901");	// Y틴 27(LTE)
//			put("userId", "kksopee");	put("pwd", "test1234!");	put("cnt", 2);
//			put("userId", "samagu74");	put("pwd", "family2@");		put("cnt", 3);	put("mobileNo", "01028733096");	// 순 광대역안심무한51(LTE)
		put("userId", "yanaq22");	put("pwd", "family2@");		put("cnt", 3);	put("mobileNo", "01043176325");	// LTE 데이터 선택 54.8
//		put("userId", "yanaq22");	put("pwd", "family2@");		put("cnt", 3);	put("mobileNo", "01043166325");	// Y24 전역
	}
	};
	*/
	/** 회수처리 데이턱ID */
	/*
	private static String returnDatukId = "a9282c058ca74310a352a2b828";
	
	private static String ysid;
	private static String ysidMain;
	private static String cntrNoMain;
	private static String mobileNoMain;
	private static String cid;
	private static JSONObject resultObj;
	private static String url;
	private static String cntrNo;
	private static String mobileNo;
	*/

	/**
	 * ID/PW 로그인
	 */
	/*
	public void test1030CheckLoginAccnt() 
	{
		try {
			logger.info("\n==== ID/PW 로그인 : 정상 동작 확인");
			url = URL + "/na/user/login/accnt?userId=" + PARAM_MAP.get("userId") + "&pwd=" + PARAM_MAP.get("pwd");
			
			resultObj = new JSONObject(callFunc(url));
			if ( YappUtil.isNotEq("200", resultObj.getString("resultCd")) ) {
				logger.info("계약정보 없음");
				System.exit(0);
			}
			assertEquals("200", resultObj.getString("resultCd"));

			JSONObject resultData = resultObj.getJSONObject("resultData");
			JSONArray jsonArray = resultData.getJSONArray("cntrInfoList");
			
			JSONObject cntrInfo = null;
			for ( int i = 0; i < jsonArray.length(); i++ )
			{
				if ( YappUtil.isEq(PARAM_MAP.get("mobileNo"), jsonArray.getJSONObject(i).getString("mobileNo")) )
					cntrInfo = jsonArray.getJSONObject(i);
			}
			if ( cntrInfo == null ) {
				logger.info("계약정보 없음");
				System.exit(0);
			}
			
			cntrNo = cntrInfo.getString("cntrNo");
			mobileNo = cntrInfo.getString("mobileNo");
			
			ysid = resultData.getString("ysid");
			cid = resultData.getString("credentialId");
			
			cntrNoMain = cntrInfo.getString("cntrNo");
			mobileNoMain = cntrInfo.getString("mobileNo");
			
			ysidMain = resultData.getString("ysid");

			assertNotNull(ysid);
			assertNotNull(cid);
			assertNotNull(cntrNo);
			assertNotNull(mobileNo);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
			System.exit(0);
		}
	}*/

	/**
	 * 회선 선택
	 */
	/*public void test1040SelectLine() 
	{
		try {
			logger.info("\n==== 회선 선택 : 정상 동작 체크");
			url = URL + "/na/user/line?cntrNo=" + cntrNo;
			resultObj = new JSONObject(callFunc(url));
			assertEquals("200", resultObj.getString("resultCd"));
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
//		System.exit(0);
	}*/

//	/**
//	 * 데이턱 생성
//	 */
//	private static String receiveDatukId;
//	public void test1105createDatuk() throws Exception
//	{
//		loginMain();
//		logger.info("\n==== 데이턱 생성 : 1회 최대용량을 초과 오류 체크");
//		url = URL + "/datuk?cntrNo=" + cntrNo + "&datukAmt=" + 1100;
//		resultObj = new JSONObject(callFunc(url, null, "POST"));
//		assertEquals("선물하기 1회 최대용량을 초과했습니다.", resultObj.getString("resultMsg"));
//
//		logger.info("\n==== 데이턱 생성 : 정상 동작 체크");
//		url = URL + "/datuk?cntrNo=" + cntrNo + "&datukAmt=" + 200;
//		resultObj = new JSONObject(callFunc(url, null, "POST"));
//		
//		receiveDatukId = resultObj.getString("resultData");
//		assertEquals(32, resultObj.getString("resultData").length());
//		
////		logger.info("\n==== 데이턱 생성 : 10분 체크");
////		url = URL + "/datuk?cntrNo=" + cntrNo + "&datukAmt=" + 200;
////		resultObj = new JSONObject(callFunc(url, null, "POST"));
////		assertEquals("일정시간(10분) 이 지난 후 다시 이용해주세요.", resultObj.getString("resultMsg"));
//		
//		logger.info("\n==== 데이턱 생성 : 데이터 선물을 할 수 없는 요금제를 사용 중입니다.");
//		login("01029305204");
//		url = URL + "/datuk?cntrNo=" + cntrNo + "&datukAmt=" + 700;
//		resultObj = new JSONObject(callFunc(url, null, "POST"));
//		assertEquals("데이터 선물을 할 수 없는 요금제를 사용 중입니다.", resultObj.getString("resultMsg"));
//		
//		logger.info("\n==== 데이턱 생성 : 선물가능 용량이 부족해요");
//		login("01098285204");
//		url = URL + "/datuk?cntrNo=" + cntrNo + "&datukAmt=" + 200;
//		resultObj = new JSONObject(callFunc(url, null, "POST"));
//		assertEquals("선물가능 용량이 부족해요", resultObj.getString("resultMsg"));
//
//		logger.info("\n==== 데이턱 생성 : 최대횟수를 초과");
//		url = URL + "/datuk?cntrNo=" + cntrNo + "&datukAmt=" + 100;
//		resultObj = new JSONObject(callFunc(url, null, "POST"));
//		url = URL + "/datuk?cntrNo=" + cntrNo + "&datukAmt=" + 200;
//		resultObj = new JSONObject(callFunc(url, null, "POST"));
//		assertEquals("선물하기 최대횟수를 초과했습니다.", resultObj.getString("resultMsg"));
//	}
//	
//	/**
//	 * 수령 대상 데이턱 데이터 조회
//	 */
//	public void test1110getRcvDatukData() throws Exception
//	{
//		loginMain();
//		logger.info("\n==== 수령 대상 데이터 데이터 조회 : 정상 동작 체크");
//		
//		url = URL + "/datuk/receive?datukId=a9282c058ca74310a352a";
//		resultObj = new JSONObject(callFunc(url, null, "GET"));
//
//		JSONObject resultData = resultObj.getJSONObject("resultData");
//		assertEquals(200, resultData.getInt("datukAmt"));
//		assertEquals(YappUtil.getCurDate("yyyyMMdd", Calendar.DATE, 1), resultData.getString("joinEdYmd"));
//		assertEquals("20180131", resultData.getString("rtnEdYmd"));
//	}

	/*public void test1210GetJoinInfoList() throws Exception
	{
		JSONArray paramArray = new JSONArray();
		paramArray.put("01043176325");
		paramArray.put("01028733096");
		paramArray.put("01080801004");

		logger.info("\n==== yapp 가입여부 조회 : 정상 동작 체크");
		
		url = URL + "/user/joininfo";
		resultObj = new JSONObject(callFunc(url, paramArray));

		JSONArray joinInfoList = resultObj.getJSONObject("resultData").getJSONArray("joinInfoList");
		assertEquals(joinInfoList.length(), paramArray.length());
		assertEquals("Y", joinInfoList.getJSONObject(0).get("yappJoinYn"));
		assertEquals("N", joinInfoList.getJSONObject(2).get("yappJoinYn"));
		assertEquals("G0001", joinInfoList.getJSONObject(0).get("dboxStatus"));
		assertEquals("G0002", joinInfoList.getJSONObject(2).get("dboxStatus"));
	}*/

	/**
	 * 데이턱 데이터 수령
	 */
//	public void test1115receiveDatukData() throws Exception
//	{
//		loginMain();
//		logger.info("\n==== 데이턱 데이터 수령 : 데이턱 생성회선이 유효하지 않습니다.");
//		url = URL + "/datuk/receive?datukId=a9282c058cffdsa74310a322a";
//		resultObj = new JSONObject(callFunc(url, null, "POST"));
//		assertEquals("데이턱 생성회선이 유효하지 않습니다.", resultObj.getString("resultMsg"));
//		
//		logger.info("\n==== 데이턱 데이터 수령 : 정상 동작 체크");
//		url = URL + "/datuk/receive?datukId=a9282c058ca74310a352a";
//		resultObj = new JSONObject(callFunc(url, null, "POST"));
//		assertEquals("200", resultObj.getString("resultCd"));
//
//		logger.info("\n==== 데이턱 데이터 수령 : 재수령 오류 체크");
//		resultObj = new JSONObject(callFunc(url, null, "POST"));
//		assertEquals("한번 받은 데이턱은 다시 받을 수 없어요.", resultObj.getString("resultMsg"));
//		
//		logger.info("\n==== 데이턱 데이터 수령 : 데이터가 없음 오류 체크");
//		url = URL + "/datuk/receive?datukId=a9282c058cfdsfdsa74310a352a";
//		resultObj = new JSONObject(callFunc(url, null, "POST"));
//		assertEquals("데이턱 데이터가 없습니다.", resultObj.getString("resultMsg"));
//		
//		logger.info("\n==== 데이턱 데이터 수령 : 기간이 종료 오류 체크");
//		url = URL + "/datuk/receive?datukId=a9282c058ca74310a35fds2a";
//		resultObj = new JSONObject(callFunc(url, null, "POST"));
//		assertEquals("데이턱 기간이 종료되었어요. 다음기회에…", resultObj.getString("resultMsg"));
//		
//		logger.info("\n==== 데이턱 데이터 수령 : 본인이 생성한 데이턱 오류 체크");
//		url = URL + "/datuk/receive?datukId=" + receiveDatukId;
//		resultObj = new JSONObject(callFunc(url, null, "POST"));
//		assertEquals("본인이 생성한 데이턱은 수령 할 수 없습니다.", resultObj.getString("resultMsg"));
//		
//		logger.info("\n==== 데이턱 데이터 수령 : 데이턱이 매진 오류 체크");
//		url = URL + "/datuk/receive?datukId=a9282c058ca74310a322a";
//		resultObj = new JSONObject(callFunc(url, null, "POST"));
//		assertEquals("데이턱이 매진되었어요.", resultObj.getString("resultMsg"));
//		
//		logger.info("\n==== 데이턱 데이터 수령 : 데이턱이 매진 오류 체크");
//		login("01098285204");
//		url = URL + "/datuk/receive?datukId=a9282c058ca74310a322a";
//		resultObj = new JSONObject(callFunc(url, null, "POST"));
//		assertEquals("데이터가 너무 많아서 담을 공간이 없어요.", resultObj.getString("resultMsg"));
//		
//	}
//	
	
	

//
//	/***************************************************************************************************/
//	/***************************************************************************************************/
//	/*********************************** CmsController *******************************************************/

/*	private void loginMain() throws Exception
	{
		cntrNo = cntrNoMain;
		mobileNo = mobileNoMain;
		ysid = ysidMain;
	}*/
	
	/**
	 * 로그인 처리
	 */
	/*private void login(String mobileNo) throws Exception
	{
		url = URL + "/na/login/mobileno?iptMobileNo=" + mobileNo;
		resultObj = new JSONObject(callFunc(url, null, "GET"));
		
		JSONObject resultData = resultObj.getJSONObject("resultData");
		ysid = resultData.getString("ysid");

		JSONObject cntrInfo = resultData.getJSONObject("cntrInfo");
		cntrNo = cntrInfo.getString("cntrNo");
		mobileNo = cntrInfo.getString("mobileNo");
	}

	private static String cookieVal;
	private String callFunc(String url) throws Exception
	{
		return callFunc(url, null);
	}
	private String callFunc(String url, Object outputParam) throws Exception
	{
		return callFunc(url, outputParam, "POST");
	}
	private static int cnt = 0;
	private String callFunc(String url, Object outputParam, String method) throws Exception
	{
		boolean isPrintLog = true;
		
		if ( url.indexOf("/na/login/mobileno") > -1 )
			isPrintLog = false;
		
		if ( isPrintLog )
			logger.info("URL : " + url);
		
		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		conn.setRequestMethod(method);
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Accept", "application/json");
		if ( ysid != null )
			conn.setRequestProperty("ysid", ysid);
		
		conn.setRequestProperty("charset", "utf-8");
//		if ( cookieVal != null && cnt++ < 8 )
		if ( cookieVal != null  )
			conn.setRequestProperty("Cookie", cookieVal);
//		else
//			conn.setRequestProperty("Cookie", cookieVal + new Random().nextInt(10000));
			

		conn.connect();
		if ( outputParam != null ) {
			OutputStream os = conn.getOutputStream();
			os.write(outputParam.toString().getBytes("utf-8"));
			if ( isPrintLog )
				logger.info(outputParam.toString());

			os.flush();
			
			os.close();
		}
		
		StringBuffer sb = new StringBuffer();
		BufferedReader br = null;
		if ( conn.getResponseCode() == 200 )
			br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
		else
			br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "utf-8"));
		
		String line = null;
		while ( (line = br.readLine()) != null ) {
			sb.append(line);
//			logger.info(line);
			if ( isPrintLog )
				logger.info(line.replaceAll("([:][{])", "\n{"));
		}
		br.close();
		
		cookieVal = cookieVal == null ? conn.getHeaderField("Set-Cookie") : cookieVal;

		return sb.toString();
	}*/

}
