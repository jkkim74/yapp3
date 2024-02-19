package com.kt.yapp.web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
public class UserControllerTest extends TestCase 
{
//	private static String URL = "http://localhost:8080/yapp";
//	private static String URL = "http://10.217.81.117/yapp";
//	private static String URL = "http://14.63.149.227/yapp";
	private static String URL = "http://14.63.149.114/yapp";
	@Autowired
	private UserService service;
	
	private static final Logger logger = LoggerFactory.getLogger(UserControllerTest.class);

	protected void setUp() throws Exception {
		super.setUp();
	}

	private static Map<String, Object> PARAM_MAP = new HashMap<String, Object>() {
		{
//			put("userId", "yanaq22");	put("pwd", "test1234!");	put("cnt", 2);
//			put("userId", "kksopee");	put("pwd", "test1234!");	put("cnt", 2);
//			put("userId", "samagu74");	put("pwd", "family2@");		put("cnt", 4);
		}
	};
	
	private static String ysid;
	private static String cid;
	private static JSONObject resultObj;
	private static String url;
	private static String cntrNo;
	private static String mobileNo;
	
	/**
	 * ID/PW 로그인
	 */
	/*public void test100CheckLoginAccnt() 
	{
		try {
			logger.info("\n==== ID/PW 로그인 : user id 누락 오류 체크");
			url = URL + "/na/user/login/accnt?pwd=" + PARAM_MAP.get("pwd");
			resultObj = new JSONObject(callFunc(url));
			assertTrue(resultObj.getString("resultCd").equals("500"));
			assertTrue(resultObj.getString("resultMsg").equals("userId(사용자ID) : 입력값이 NULL 입니다."));

			logger.info("\n==== ID/PW 로그인 : 누락 오류 체크");
			url = URL + "/na/user/login/accnt?userId=" + PARAM_MAP.get("userId");
			resultObj = new JSONObject(callFunc(url));
			assertTrue(resultObj.getString("resultCd").equals("500"));
			assertTrue(resultObj.getString("resultMsg").equals("pwd(비밀번호) : 입력값이 NULL 입니다."));
			
			logger.info("\n==== ID/PW 로그인 : 존재하지 않는 user id 오류 체크");
			url = URL + "/na/user/login/accnt?userId=" + "aaa" + new Random().nextInt(20) + "bbb" + "&pwd=" + PARAM_MAP.get("pwd");
			resultObj = new JSONObject(callFunc(url));
			assertTrue(resultObj.getString("resultCd").equals("10710"));
			assertTrue(resultObj.getString("resultMsg").equals("ID/비밀번호가 일치하지 않습니다."));

			logger.info("\n==== ID/PW 로그인 : 존재하지 않는 user id, pwd 오류 체크");
			url = URL + "/na/user/login/accnt?userId=" + PARAM_MAP.get("userId") + "&pwd=" + "aaaaa";
			resultObj = new JSONObject(callFunc(url));
			assertTrue(resultObj.getString("resultCd").equals("1074"));
			assertTrue(resultObj.getString("resultMsg").equals("ID/비밀번호가 일치하지 않습니다."));
			
			logger.info("\n==== ID/PW 로그인 : 정상 동작 확인");
			url = URL + "/na/user/login/accnt?userId=" + PARAM_MAP.get("userId") + "&pwd=" + PARAM_MAP.get("pwd");
			
			resultObj = new JSONObject(callFunc(url));
			assertTrue(resultObj.getString("resultCd").equals("200"));

			JSONObject resultData = resultObj.getJSONObject("resultData");
			
			JSONObject cntrInfo = resultData.getJSONArray("cntrInfoList").getJSONObject(0);
			cntrNo = cntrInfo.getString("cntrNo");
			mobileNo = cntrInfo.getString("mobileNo");
			
			ysid = resultData.getString("ysid");
			cid = resultData.getString("credentialId");
			
			assertNotNull(ysid);
			assertNotNull(cid);
			assertNotNull(cntrNo);
			assertNotNull(mobileNo);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
*/
	/**
	 * 회선 선택
	 */
	/*public void test101SelectLine() 
	{
		try {
			logger.info("\n==== 회선 선택 : cntr no 누락 오류 체크");
			url = URL + "/na/user/line?cntrNo=";
			resultObj = new JSONObject(callFunc(url));
			assertTrue(resultObj.getString("resultCd").equals("500"));
			assertTrue(resultObj.getString("resultMsg").equals("cntrNo(계약번호) : 입력값이 NULL 입니다."));

			logger.info("\n==== 회선 선택 : 잘못된 cntr no 오류 체크");
			url = URL + "/na/user/line?cntrNo=" + "aaaaa";
			resultObj = new JSONObject(callFunc(url));
			assertTrue(resultObj.getString("resultCd").equals("401"));
			assertTrue(resultObj.getString("resultMsg").equals("로그인 정보가 없습니다."));
			
			logger.info("\n==== 회선 선택 : 정상 동작 체크");
			url = URL + "/na/user/line?cntrNo=" + cntrNo;
			resultObj = new JSONObject(callFunc(url));
			assertTrue(resultObj.getString("resultCd").equals("200"));
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	*//**
	 * 서비스 가입
	 *//*
	public void test102JoinService() throws Exception 
	{
			JSONObject paramTerms = new JSONObject();
			paramTerms.put("termsVrsn", "1");
			paramTerms.put("agreeChnl", "G0001");
			paramTerms.put("piPolicyAgreeYn", "Y");
			paramTerms.put("piUseAgreeYn", "Y");
			JSONObject paramUser = new JSONObject();
			paramUser.put("cntrNo", "aaaaa");
			paramUser.put("termsVrsn", "1");
			paramUser.put("mobileNo", mobileNo);
			paramUser.put("termsAgree", paramTerms);
			
			logger.info("\n==== 서비스 가입 : cntr no 불일치 오류 체크");
			url = URL + "/user/join";
			resultObj = new JSONObject(callFunc(url, paramUser));
			assertTrue(resultObj.getString("resultCd").equals("500"));
			assertTrue(resultObj.getString("resultMsg").equals("로그인된 정보와 일치하지 않습니다."));

			logger.info("\n==== 서비스 가입 :  정상 동작 체크");
			paramUser.put("cntrNo", cntrNo);

			url = URL + "/user/join";
			resultObj = new JSONObject(callFunc(url, paramUser));
			JSONObject resultData = resultObj.getJSONObject("resultData");
			assertTrue(resultObj.getString("resultCd").equals("200"));
			assertNotNull(resultData.get("cntrNo"));
			
			logger.info("\n==== 서비스 가입 :  가입된 사용자 오류 동작 체크");

			url = URL + "/user/join";
			resultObj = new JSONObject(callFunc(url, paramUser));
			assertTrue(resultObj.getString("resultCd").equals("500"));
			assertTrue(resultObj.getString("resultMsg").equals("이미 가입된 사용자입니다."));
	}*/

	/**
	 * 계약 목록 조회
	 */
	/*public void test103GetCntrList() 
	{
		try {
			logger.info("\n==== 계약 목록 조회 : 사용자ID 누락 오류 체크");
			url = URL + "/user/contract/list";
			resultObj = new JSONObject(callFunc(url, null, "GET"));
			assertTrue(resultObj.getString("resultCd").equals("500"));
			assertTrue(resultObj.getString("resultMsg").equals("userId(사용자ID) : 입력값이 NULL 입니다."));
	
			logger.info("\n==== 계약 목록 조회 : 크레덴셜ID 누락 오류 체크");
			url = URL + "/user/contract/list?userId=" + PARAM_MAP.get("userId");
			resultObj = new JSONObject(callFunc(url, null, "GET"));
			assertTrue(resultObj.getString("resultCd").equals("500"));
			assertTrue(resultObj.getString("resultMsg").equals("credentialId(크레덴셜ID) : 입력값이 NULL 입니다."));
			
			logger.info("\n==== 계약 목록 조회 : 정상 동작 체크");
			
			url = URL + "/user/contract/list?userId=" + PARAM_MAP.get("userId") + "&credentialId=" + cid;
			resultObj = new JSONObject(callFunc(url, null, "GET"));
			assertTrue(resultObj.getString("resultCd").equals("200"));
			JSONArray resultData = resultObj.getJSONArray("resultInfoList");
			assertEquals(resultData.length(), PARAM_MAP.get("cnt"));
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}*/

	/**
	 * 내 계약정보 조회
	 */
	/*public void test1032GetMyCntrInfo() throws Exception
	{
		logger.info("\n==== 내 계약 정보 조회 : 정상 동작 체크");
		url = URL + "/user/mycontract";
		resultObj = new JSONObject(callFunc(url, null, "GET"));
		
		assertTrue(resultObj.getString("resultCd").equals("200"));
		
		JSONObject resultData = resultObj.getJSONObject("resultData");
		assertEquals(mobileNo, resultData.getString("mobileNo"));
		assertEquals(cntrNo, resultData.getString("cntrNo"));
		assertEquals("01", resultData.getString("cntrcStatusCd"));
		assertEquals("이원태", resultData.getString("userNm"));
	}*/
	
	/**
	 * 내 데이터 정보 조회
	 */
	/*public void test1033GetMyDataInfo() throws Exception
	{
		logger.info("\n==== 내 데이터 정보 조회 : 정상 동작 체크");
		url = URL + "/user/mydata";
		resultObj = new JSONObject(callFunc(url, null, "GET"));

		assertTrue(resultObj.getString("resultCd").equals("200"));
		
		JSONObject resultData = resultObj.getJSONObject("resultData");
		assertEquals(2500000, resultData.getInt("totFreeQnt"));
		assertEquals(1500000, resultData.getInt("tmonFreeQnt"));
		assertEquals(1000000, resultData.getInt("cfwdFreeQnt"));
		assertEquals(500000, resultData.getInt("freeUseQnt"));
	}*/
	
	private static Integer authNum;
	
//	/**
//	 * 휴대폰 로그인 요청
//	 */
//	public void test104ReqLoginPhone() throws Exception
//	{
//		try {
//			JSONObject paramObj = new JSONObject();
//	
//			logger.info("\n==== 휴대폰 로그인 요청 : 인증대상 휴대폰 번호 누락 오류 체크");
//			url = URL + "/na/user/login/authreq";
//			resultObj = new JSONObject(callFunc(url, paramObj));
//			assertTrue(resultObj.getString("resultCd").equals("500"));
//			assertTrue(resultObj.getString("resultMsg").equals("authMobileNo(인증 휴대폰 번호) : 숫자형이 아닙니다.(입력값 : null)"));
//	
//			logger.info("\n==== 휴대폰 로그인 요청 : 정상 동작 체크");
//			paramObj.put("authMobileNo", mobileNo);
//	
//			url = URL + "/na/user/login/authreq";
//			resultObj = new JSONObject(callFunc(url, paramObj));
//			assertTrue(resultObj.getString("resultCd").equals("200"));
//			
//			authNum = YappUtil.toInt(resultObj.get("resultData"));
//		} catch(Exception e) {
//			e.printStackTrace();
//			fail();
//		}
//	}
//	
//	/**
//	 * 휴대폰 로그인 확인
//	 */
//	public void test105CheckLoginPhone() throws Exception
//	{
//		try {
//			JSONObject paramObj = new JSONObject();
//	
//			logger.info("\n==== 휴대폰 로그인 확인 : 인증대상 휴대폰 번호 누락 오류 체크");
//			url = URL + "/na/user/login/authchk";
//			resultObj = new JSONObject(callFunc(url, paramObj));
//			assertTrue(resultObj.getString("resultCd").equals("500"));
//			assertTrue(resultObj.getString("resultMsg").equals("authMobileNo(인증 휴대폰 번호) : 숫자형이 아닙니다.(입력값 : null)"));
//	
//			paramObj.put("authMobileNo", mobileNo);
//			
//			logger.info("\n==== 휴대폰 로그인 확인 : 인증번호 오류 체크");
//			url = URL + "/na/user/login/authchk";
//			resultObj = new JSONObject(callFunc(url, paramObj));
//			assertTrue(resultObj.getString("resultCd").equals("500"));
//			assertTrue(resultObj.getString("resultMsg").equals("authNum(인증번호) : 숫자형이 아닙니다.(입력값 : null)"));
//			
//			paramObj.put("authNum", URL.indexOf("227") > -1 ? authNum : "1111");
//	
//			logger.info("\n==== 휴대폰 로그인 확인 : 정상 동작 체크");
//			paramObj.put("authMobileNo", mobileNo);
//	
//			url = URL + "/na/user/login/authchk";
//			resultObj = new JSONObject(callFunc(url, paramObj));
//			assertTrue(resultObj.getString("resultCd").equals("200"));
//			JSONObject resultData = resultObj.getJSONObject("resultData");
//			assertNotNull(resultData);
//			
//			ysid = resultData.getString("ysid");
//		} catch (Exception e) {
//			e.printStackTrace();
//			fail();
//		}
//	}
	
	/**
	 * yapp 가입여부 조회
	 */
	/*public void test106GetJoinInfoList() 
	{
		try {
			JSONArray paramArray = new JSONArray();
			paramArray.put(mobileNo);
			paramArray.put("01043176325");
			paramArray.put("01010041004");
			paramArray.put("01080801004");
			paramArray.put("111");
	
			logger.info("\n==== yapp 가입여부 조회 : 정상 동작 체크");
			
			url = URL + "/user/joininfo";
			resultObj = new JSONObject(callFunc(url, paramArray));
			assertTrue(resultObj.getString("resultCd").equals("200"));
			JSONArray resultInfoList = resultObj.getJSONArray("resultInfoList");
			assertEquals(resultInfoList.length(), paramArray.length());
			assertEquals("Y", resultInfoList.getJSONObject(0).get("yappJoinYn"));
			assertEquals("N", resultInfoList.getJSONObject(4).get("yappJoinYn"));
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}*/

	/**
	 * 사용자 정보 변경
	 */
	/*public void test107UpdateUserSetting() 
	{
		try {
			JSONObject paramObj = new JSONObject();
			paramObj.put("reqRcvYn", "N");
			paramObj.put("mktRcvYn", "N");
			paramObj.put("pushRcvYn", "N");
	
			logger.info("\n==== 사용자 정보 변경 : 정상 동작 체크");
			
			url = URL + "/user/setting";
			resultObj = new JSONObject(callFunc(url, paramObj));
			assertTrue(resultObj.getString("resultCd").equals("200"));
			JSONObject resultData = resultObj.getJSONObject("resultData");
			assertEquals(resultData.get("pushRcvYn"), "N");
			assertEquals(resultData.get("mktRcvYn"), "N");
			assertEquals(resultData.get("reqRcvYn"), "N");
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}*/

	/***************************************************************************************************/
	/***************************************************************************************************/
	/*********************************** CmsController *******************************************************/

	/**
	 * 앱 정보 조회(버전 등)
	 */
	/*public void test201GetAppInfo() throws Exception 
	{
		JSONObject paramObj = new JSONObject();
		
		logger.info("\n==== 앱 정보 조회(버전 등) : osTp 누락 오류 체크");
		url = URL + "/cms/appinfo";
		resultObj = new JSONObject(callFunc(url, null, "GET"));
		assertTrue(resultObj.getString("resultCd").equals("500"));
		assertTrue(resultObj.getString("resultMsg").equals("osTp :  입력값이 NULL 입니다."));

		logger.info("\n==== 앱 정보 조회(버전 등) : 정상 동작 체크");
		
		url = URL + "/cms/appinfo?osTp=G0001";
		resultObj = new JSONObject(callFunc(url, null, "GET"));
		assertTrue(resultObj.getString("resultCd").equals("200"));
		JSONObject resultData = resultObj.getJSONObject("resultData");
		assertEquals("1.0", resultData.get("appVrsn"));
		assertEquals("Y", resultData.get("forceUptYn"));
	}*/
	
	/**
	 * 시스템 점검 정보 조회
	 */
	/*public void test202GetSysCheckInfo() throws Exception 
	{
		logger.info("\n==== 시스템 점검 정보 조회 : 정상 동작 체크");
		
		url = URL + "/cms/syscheck";
		resultObj = new JSONObject(callFunc(url, null, "GET"));
		assertTrue(resultObj.getString("resultCd").equals("200"));
		JSONObject resultData = resultObj.getJSONObject("resultData");
		assertEquals("201708310101", resultData.get("chkStDt"));
		assertEquals("201710312359", resultData.get("chkEdDt"));
	}*/
	
	/**
	 * 약관 정보 조회
	 */
	/*public void test203GetTermsInfo() throws Exception
	{
		logger.info("\n==== 약관 정보 조회 : 정상 동작 체크");
		
		url = URL + "/cms/terms";
		resultObj = new JSONObject(callFunc(url, null, "GET"));
		assertTrue(resultObj.getString("resultCd").equals("200"));

		JSONObject resultData = resultObj.getJSONObject("resultData");
		assertEquals(1, resultData.get("termsVrsn"));
	}*/
	
	/**
	 * 공지사항 목록 조회
	 */
	/*public void test204GetNoticeList() throws Exception
	{
		logger.info("\n==== 공지사항 목록 조회 : 정상 동작 체크");
		
		url = URL + "/cms/bbs/notice";
		resultObj = new JSONObject(callFunc(url, null, "GET"));
		assertEquals("200", resultObj.getString("resultCd"));

		JSONArray resultInfoList = resultObj.getJSONArray("resultInfoList");
		assertEquals(3, resultInfoList.length());
		assertEquals("G0001", resultInfoList.getJSONObject(0).get("itemTp"));
		assertEquals("20170828", resultInfoList.getJSONObject(0).get("postStYmd"));
		assertEquals("20170930", resultInfoList.getJSONObject(0).get("postEdYmd"));
	}*/
	
	/**
	 * 알림 추가
	 */
	/*public void test205InsertNotiMsg() throws Exception
	{
		JSONObject paramObj = new JSONObject();
		
		url = URL + "/cms/notimsg";
		
		logger.info("\n==== 알림 추가 : notiMsg 누락 오류 체크");
		paramObj.put("cntrNo", cntrNo);

		resultObj = new JSONObject(callFunc(url, paramObj));
		
		assertEquals("500", resultObj.getString("resultCd"));
		assertEquals("notiMsg(알림메시지 내용) : 입력값이 NULL 입니다.", resultObj.getString("resultMsg"));
		
		logger.info("\n==== 알림 추가 : notiTp 누락 오류 체크");
		paramObj.put("notiMsg", "msg1 ...");
		
		resultObj = new JSONObject(callFunc(url, paramObj));
		
		assertEquals("500", resultObj.getString("resultCd"));
		assertEquals("notiTp(알림타입) : 그룹코드 형식(Gxxxx)이 아닙니다.(입력값 : null)", resultObj.getString("resultMsg"));
		
		logger.info("\n==== 알림 추가 : notiTpDtl 누락 오류 체크");
		paramObj.put("notiTp", "G0001");
		
		resultObj = new JSONObject(callFunc(url, paramObj));
		
		assertEquals("500", resultObj.getString("resultCd"));
		assertEquals("notiTpDtl(알림타입 상세) : 그룹코드 형식(Gxxxx)이 아닙니다.(입력값 : null)", resultObj.getString("resultMsg"));
		
		logger.info("\n==== 알림 추가 : 정상 동작 체크");
		paramObj.put("notiTpDtl", "G0101");
		
		resultObj = new JSONObject(callFunc(url, paramObj));
		
		assertEquals("200", resultObj.getString("resultCd"));

		paramObj.put("notiTpDtl", "G0102");
		paramObj.put("notiMsg", "msg2 ...");
		resultObj = new JSONObject(callFunc(url, paramObj));

		// 알림 목록 조회
		url = URL + "/cms/notimsg?cntrNo=" + cntrNo;
		resultObj = new JSONObject(callFunc(url, null, "GET"));

		JSONArray resultInfoList = resultObj.getJSONArray("resultInfoList");
		assertEquals(2, resultInfoList.length());
		assertEquals("G0001", resultInfoList.getJSONObject(0).get("notiTp"));
		assertEquals("G0102", resultInfoList.getJSONObject(0).get("notiTpDtl"));
		assertEquals("N", resultInfoList.getJSONObject(0).get("delYn"));
		assertEquals("Y", resultInfoList.getJSONObject(0).get("newYn"));
		assertEquals("msg2 ...", resultInfoList.getJSONObject(0).get("notiMsg"));
	}*/
	
	/**
	 * 알림 목록 조회
	 */
	/*public void test206GetNotiMsgList() throws Exception
	{
		logger.info("\n==== 알림 목록 조회 : 정상 동작 체크");
		
		url = URL + "/cms/notimsg?cntrNo=" + cntrNo;
		resultObj = new JSONObject(callFunc(url, null, "GET"));
		
		assertEquals("200", resultObj.getString("resultCd"));

		JSONArray resultInfoList = resultObj.getJSONArray("resultInfoList");
		assertEquals(2, resultInfoList.length());
		assertEquals("G0001", resultInfoList.getJSONObject(0).get("notiTp"));
		assertEquals("G0102", resultInfoList.getJSONObject(0).get("notiTpDtl"));
		assertEquals("N", resultInfoList.getJSONObject(0).get("delYn"));
		assertEquals("Y", resultInfoList.getJSONObject(0).get("newYn"));
		assertEquals("msg2 ...", resultInfoList.getJSONObject(0).get("notiMsg"));
	}*/
	
	/**
	 * 알림 읽음 처리
	 */
	/*public void test207ReadNotiMsg() throws Exception
	{
		logger.info("\n==== 알림 읽음 처리 : 정상 동작 체크");
		
		url = URL + "/cms/notimsg/read?cntrNo=" + cntrNo;
		resultObj = new JSONObject(callFunc(url, null));
		
		assertEquals("200", resultObj.getString("resultCd"));
		
		// 알림 목록 조회
		url = URL + "/cms/notimsg?cntrNo=" + cntrNo;
		resultObj = new JSONObject(callFunc(url, null, "GET"));

		JSONArray resultInfoList = resultObj.getJSONArray("resultInfoList");
		assertEquals("N", resultInfoList.getJSONObject(0).get("newYn"));
		assertEquals("msg2 ...", resultInfoList.getJSONObject(0).get("notiMsg"));
	}*/
	
	/**
	 * 알림 전체 삭제
	 */
	/*public void test208DeleteAllNotiMsg() throws Exception
	{
		logger.info("\n==== 알림 전체 삭제 : 정상 동작 체크");
		
		url = URL + "/cms/notimsg?cntrNo=" + cntrNo;
		resultObj = new JSONObject(callFunc(url, null, "DELETE"));
		
		assertEquals("200", resultObj.getString("resultCd"));
		
		// 알림 목록 조회
		url = URL + "/cms/notimsg?cntrNo=" + cntrNo;
		resultObj = new JSONObject(callFunc(url, null, "GET"));
		
		JSONArray resultInfoList = resultObj.getJSONArray("resultInfoList");
		assertEquals(0, resultInfoList.length());
	}*/
	
	private static int evtSeq;
	
	/**
	 * 이벤트 목록 조회
	 */
	/*public void test209GetEventList() throws Exception
	{
		logger.info("\n==== 이벤트 목록 조회 : 정상 동작 체크");
		
		url = URL + "/cms/event";
		resultObj = new JSONObject(callFunc(url, null, "GET"));
		assertEquals("200", resultObj.getString("resultCd"));

		JSONArray resultInfoList = resultObj.getJSONArray("resultInfoList");
		evtSeq = YappUtil.toInt(resultInfoList.getJSONObject(0).get("evtSeq"));
		
		assertEquals(2, resultInfoList.length());
		assertEquals("이벤트2", resultInfoList.getJSONObject(0).get("title"));
		assertEquals("이벤트2 내용...", resultInfoList.getJSONObject(0).get("contents"));
		assertEquals("유의사항2...", resultInfoList.getJSONObject(0).get("notes"));
		assertEquals("20170901", resultInfoList.getJSONObject(0).get("evtStDt"));
		assertEquals("20171015", resultInfoList.getJSONObject(0).get("evtEdDt"));
		assertEquals("b5858515-489e-4861-8bea-87ad7f9e1571", resultInfoList.getJSONObject(0).get("listImgFileId"));
		assertEquals("G0001", resultInfoList.getJSONObject(0).get("evtTp"));
		assertEquals("win!!", resultInfoList.getJSONObject(0).get("winMsg"));
		assertEquals("20171015", resultInfoList.getJSONObject(0).get("winYmd"));
//		assertEquals(null, resultInfoList.getJSONObject(0).get("btnLbl"));
//		assertEquals(null, resultInfoList.getJSONObject(0).get("linkUrl"));
		assertEquals("N", resultInfoList.getJSONObject(0).get("applYn"));
		assertEquals("N", resultInfoList.getJSONObject(0).get("winYn"));
	}*/
	
	/**
	 * 이벤트 응모
	 */
	/*public void test210ApplyEvent() throws Exception
	{
		JSONObject paramObj = new JSONObject();
		
		logger.info("\n==== 이벤트 응모 : evtSeq 누락 오류 체크");
		
		url = URL + "/cms/event/appl";
		resultObj = new JSONObject(callFunc(url, paramObj));
		
		assertEquals("500", resultObj.getString("resultCd"));
		assertEquals("evtSeq(이벤트Seq) : 숫자형이 아닙니다.(입력값 : null)", resultObj.getString("resultMsg"));
		
		logger.info("\n==== 이벤트 응모 : 정상 동작 체크");
		
		url = URL + "/cms/event/appl?evtSeq=" + evtSeq;
		resultObj = new JSONObject(callFunc(url, paramObj));
		
		assertEquals("200", resultObj.getString("resultCd"));

		// 이벤트 목록 조회
		url = URL + "/cms/event?cntrNo=" + cntrNo;
		resultObj = new JSONObject(callFunc(url, null, "GET"));

		JSONArray resultInfoList = resultObj.getJSONArray("resultInfoList");
		assertEquals("Y", resultInfoList.getJSONObject(0).get("applYn"));
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
			logger.info(line);
		}
		br.close();
		
		cookieVal = cookieVal == null ? conn.getHeaderField("Set-Cookie") : cookieVal;

		return sb.toString();
	}*/

//	public void testServiceOut() {
//		fail("Not yet implemented");
//	}
//
//	public void testLogout() {
//		fail("Not yet implemented");
//	}
//
//	public void testInsertTerms() {
//		fail("Not yet implemented");
//	}
//
//	public void testUpdateOptTerms() {
//		fail("Not yet implemented");
//	}
//
//	public void testSendAuthMail() {
//		fail("Not yet implemented");
//	}
//
//	public void testCheckAuthMail() {
//		fail("Not yet implemented");
//	}
//

}
