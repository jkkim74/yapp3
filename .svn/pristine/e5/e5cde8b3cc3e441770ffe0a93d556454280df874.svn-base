package com.kt.yapp.web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
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
public class YappTestNormal extends TestCase 
{
//	private static String URL = "http://localhost:8080/yapp";
//	private static String URL = "http://10.217.81.117/yapp";
	private static String URL = "http://14.63.149.227/yapp";
//	private static String URL = "http://14.63.149.114/yapp";
	@Autowired
	private UserService service;
	
	private static final Logger logger = LoggerFactory.getLogger(YappTestNormal.class);
	
	protected void setUp() throws Exception {
		super.setUp();
	}

	private static Map<String, Object> PARAM_MAP = new HashMap<String, Object>() {
	{
//			put("userId", "ywd022");	put("pwd", "family2@");		put("cnt", 1);	put("mobileNo", "01073860901");	// Y틴 27(LTE)
//			put("userId", "kksopee");	put("pwd", "test1234!");	put("cnt", 2);
//			put("userId", "samagu74");	put("pwd", "family2@");		put("cnt", 3);	put("mobileNo", "01028733096");	// 순 광대역안심무한51(LTE)
		//put("userId", "yanaq22");	put("pwd", "family2@");		put("cnt", 3);	put("mobileNo", "01043176325");	// LTE 데이터 선택 54.8
//		put("userId", "yanaq22");	put("pwd", "family2@");		put("cnt", 3);	put("mobileNo", "01043166325");	// Y24 전역
	}
	};
	
	/** 회수처리 데이턱ID */
	/*private static String returnDatukId = "a9282c058ca74310a352a2b828";
	
	private static String ysid;
	private static String cntrNo;
	private static String mobileNo;
	private static String ysidMain;
	private static String cntrNoMain;
	private static String mobileNoMain;
	private static String cid;
	private static JSONObject resultObj;
	private static String url;
	*/
	/**
	 * 휴대폰 로그인 요청
	 */
//	private static String authMobileNo = "01028733096";	// 이원태 - 부가서비스 테스트용
	//private static String authMobileNo = "01042973391";	// G6
	
//	public void test010ReqLoginPhone() throws Exception
//	
//	/**
//	 * 본인인증 요청
//	 */
//	public void test030reqAuth() throws Exception
//	{
//		try {
//			JSONObject paramObj = new JSONObject();
//			
//			logger.info("\n==== 본인인증 요청 : 정상 동작 체크");
//			paramObj.put("userNm", "강신제");
//			paramObj.put("birthDate", "19801231");
//			paramObj.put("genderCd", "02");
//			paramObj.put("foreignerCd", "01");
//			paramObj.put("mobileNo", authMobileNo);
//			
//			url = URL + "/common/auth/req";
//			resultObj = new JSONObject(callFunc(url, paramObj));
//			assertEquals("200", resultObj.getString("resultCd"));
//			logger.info("randomNo : " + YappUtil.nToStr(resultObj.get("resultData")));
//		} catch(Exception e) {
//			e.printStackTrace();
//			fail();
//		}
//	}
//	
//	/**
//	 * 본인 인증 체크
//	 */
//	public void test040checkAuth() throws Exception
//	{
//		try {
//			JSONObject paramObj = new JSONObject();
//			String randomNo = "";
//			String authCd= "";
//			
//			logger.info("\n==== 본인 인증 체크 : 정상 동작 체크");
//			paramObj.put("userNm", "강신제");
//			paramObj.put("birthDate", "19801231");
//			paramObj.put("genderCd", "02");
//			paramObj.put("foreignerCd", "01");
//			paramObj.put("mobileNo", authMobileNo);
//			paramObj.put("randomNo", randomNo);
//			paramObj.put("authCd", authCd);
//			
//			url = URL + "/common/auth/check";
//			resultObj = new JSONObject(callFunc(url, paramObj));
//			assertEquals("200", resultObj.getString("resultCd"));
//			logger.info("authNum : " + YappUtil.toInt(resultObj.get("resultData")));
//		} catch(Exception e) {
//			e.printStackTrace();
//			fail();
//		} finally {
//			System.exit(0);
//		}
//	}
//	
	/**
	 * 앱 정보 조회(버전 등)
	 */
	/*public void test1010Appinfo() 
	{
		try {
			logger.info("\n==== 앱 정보 조회(버전 등) : 정상 동작 체크");
			url = URL + "/na/cms/appinfo?osTp=G0001";
			resultObj = new JSONObject(callFunc(url, null, "GET"));
			assertEquals("200", resultObj.getString("resultCd"));

			JSONObject resultData = resultObj.getJSONObject("resultData");
			assertEquals("1.2", resultData.getString("appVrsn"));
			
			url = URL + "/na/cms/appinfo?osTp=G0002";
			resultObj = new JSONObject(callFunc(url, null, "GET"));
			assertEquals("200", resultObj.getString("resultCd"));

			resultData = resultObj.getJSONObject("resultData");
			assertEquals("1.3", resultData.getString("appVrsn"));
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}*/

	/**
	 * 시스템 점검 정보 조회
	 */
	/*public void test1020Syscheck() 
	{
		try {
			logger.info("\n==== 시스템 점검 정보 조회 : 정상 동작 체크");
			url = URL + "/na/cms/syscheck";
			resultObj = new JSONObject(callFunc(url, null, "GET"));
			assertEquals("200", resultObj.getString("resultCd"));

			JSONObject resultData = resultObj.getJSONObject("resultData");
			assertEquals("201708310101", resultData.getString("chkStDt"));
			assertEquals("201712252359", resultData.getString("chkEdDt"));
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}*/

	/**
	 * ID/PW 로그인
	 */
	/*public void test1030CheckLoginAccnt() 
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

	/**
	 * 서비스 가입
	 */
	/*public void test1050JoinService() throws Exception 
	{
		try {
			JSONObject paramTerms = new JSONObject();
			paramTerms.put("piUseTermsVrsn", "1.2");
			paramTerms.put("piPolicyTermsVrsn", "1.2");
			paramTerms.put("agreeChnl", "G0001");
			paramTerms.put("piPolicyAgreeYn", "Y");
			paramTerms.put("piUseAgreeYn", "Y");
			paramTerms.put("optTermsAgreeYn", "N");
			JSONObject paramUser = new JSONObject();
			paramUser.put("cntrNo", cntrNo);
			paramUser.put("mobileNo", mobileNo);
			paramUser.put("termsAgree", paramTerms);
			
			logger.info("\n==== 서비스 가입 :  정상 동작 체크");
			paramUser.put("cntrNo", cntrNo);

			url = URL + "/user/join";
			resultObj = new JSONObject(callFunc(url, paramUser));
			JSONObject resultData = resultObj.getJSONObject("resultData");
			assertEquals("200", resultObj.getString("resultCd"));

			assertNotNull(resultData.get("cntrNo"));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}*/

	/**
	 * 약관 정보 조회
	 */
	/*private static String piUseTermsVrsn;
	private static String piPolicyTermsVrsn;
	public void test1060GetTermsInfo() throws Exception
	{
		logger.info("\n==== 약관 정보 조회 : 정상 동작 체크");
		
		url = URL + "/na/cms/terms";
		resultObj = new JSONObject(callFunc(url, null, "GET"));
		assertEquals("200", resultObj.getString("resultCd"));

		JSONObject resultData = resultObj.getJSONObject("resultData");
		assertEquals("1.2", resultData.get("piUseTermsVrsn"));
		assertEquals("1.2", resultData.get("piPolicyTermsVrsn"));
		
		piUseTermsVrsn = resultData.getString("piUseTermsVrsn");
		piPolicyTermsVrsn = resultData.getString("piPolicyTermsVrsn");
	}*/
	
	/**
	 * 보호자에게 인증메일 발송
	 */
	/*private static String mailKey;
	public void test1070sendAuthMail() throws Exception
	{
		logger.info("\n==== 보호자에게 인증메일 발송 : 정상 동작 체크");
		
		JSONObject paramObj = new JSONObject();
		paramObj.put("cntrNo", cntrNo);
		paramObj.put("parentsNm", "정기영");
		paramObj.put("emailAddr", "00492@daum.net");
		
		url = URL + "/user/auth/sendmail";
		resultObj = new JSONObject(callFunc(url, paramObj, "POST"));
		assertEquals("200", resultObj.getString("resultCd"));

		String resultData = resultObj.getString("resultData");
		assertEquals(32, resultData.length());
		mailKey = resultData;
	}*/

	/**
	 * 보호자 인증 정보 조회
	 */
	/*public void test1080checkAuthMail() throws Exception
	{
		logger.info("\n==== 보호자 인증 정보 조회 : 정상 동작 체크");
		
		url = URL + "/user/auth/checkmail?mailKey=" + mailKey;
		resultObj = new JSONObject(callFunc(url, null, "GET"));
		assertEquals("200", resultObj.getString("resultCd"));

		JSONObject resultData = resultObj.getJSONObject("resultData");
		assertEquals("00492@daum.net", resultData.getString("emailAddr"));
		assertEquals("N", resultData.getString("mailAgreeYn"));
	}*/
	
	/**
	 * 약관 동의 정보 추가
	 */
	/*public void test1090UpdateTerms() throws Exception
	{
		logger.info("\n==== 약관 동의 정보 추가 : 정상 동작 체크");
		
		JSONObject paramObj = new JSONObject();
		paramObj.put("piUseTermsVrsn", "1.3");
		paramObj.put("piPolicyTermsVrsn", piPolicyTermsVrsn);
		paramObj.put("agreeChnl", "G0001");
		paramObj.put("piUseAgreeYn", "Y");
		paramObj.put("piPolicyAgreeYn", "Y");
		paramObj.put("optTermsAgreeYn", "N");
		
		url = URL + "/user/terms/agree";
		resultObj = new JSONObject(callFunc(url, paramObj, "POST"));
		assertEquals("200", resultObj.getString("resultCd"));
		
		// 회선 선택
		url = URL + "/na/user/line?cntrNo=" + cntrNo;
		resultObj = new JSONObject(callFunc(url));
		assertEquals("200", resultObj.getString("resultCd"));

		JSONObject resultData = resultObj.getJSONObject("resultData");
		JSONObject termsAgree = resultData.getJSONObject("termsAgree");
		assertEquals("1.3", termsAgree.get("piUseTermsVrsn"));
		assertEquals(piPolicyTermsVrsn, termsAgree.get("piPolicyTermsVrsn"));
	}*/
	
	/**
	 * 메인화면 정보 조회
	 */
	/*private static String bonusId;
	private static String bonusIdPromotion;
	public void test1100viewMain() throws Exception
	{
		logger.info("\n==== 메인화면 정보 조회 : 정상 동작 체크");
		
		url = URL + "/main";
		resultObj = new JSONObject(callFunc(url, null, "GET"));
		assertEquals("200", resultObj.getString("resultCd"));

		JSONObject resultData = resultObj.getJSONObject("resultData");
		// 보너스 데이터 목록
		bonusId = resultData.getJSONArray("bonusDataList").getJSONObject(0).getString("bonusId");
		bonusIdPromotion = resultData.getJSONArray("bonusDataList").getJSONObject(1).getString("bonusId");
	}*/
	
	/**
	 * 데이턱 생성
	 */
	/*private static String receiveDatukId;
	public void test1105createDatuk() throws Exception
	{
		logger.info("\n==== 데이턱 생성 : 1회 최대용량을 초과 오류 체크");
		
		url = URL + "/datuk?cntrNo=" + cntrNo + "&datukAmt=" + 1100;
		resultObj = new JSONObject(callFunc(url, null, "POST"));
		assertEquals("선물하기 1회 최대용량을 초과했습니다.", resultObj.getString("resultMsg"));

		logger.info("\n==== 데이턱 생성 : 정상 동작 체크");
		
		url = URL + "/datuk?cntrNo=" + cntrNo + "&datukAmt=" + 200;
		resultObj = new JSONObject(callFunc(url, null, "POST"));
		
		receiveDatukId = resultObj.getString("resultData");
		assertEquals(32, resultObj.getString("resultData").length());
		
		logger.info("\n==== 데이턱 생성 : 공유 중 데이턱 생성 오류 체크");
		
		url = URL + "/datuk?cntrNo=" + cntrNo + "&datukAmt=" + 200;
		resultObj = new JSONObject(callFunc(url, null, "POST"));
		assertEquals("일정시간(10분) 이 지난 후 다시 이용해주세요.", resultObj.getString("resultMsg").length());
	}*/
	
	/**
	 * 수령 대상 데이턱 데이터 조회
	 */
	/*public void test1110getRcvDatukData() throws Exception
	{
		logger.info("\n==== 수령 대상 데이터 데이터 조회 : 정상 동작 체크");
		
		url = URL + "/datuk/receive?datukId=" + receiveDatukId;
		resultObj = new JSONObject(callFunc(url, null, "GET"));

		JSONObject resultData = resultObj.getJSONObject("resultData");
		assertEquals(200, resultData.getInt("datukAmt"));
		assertEquals(YappUtil.getCurDate("yyyyMMdd", Calendar.DATE, 1), resultData.getString("joinEdYmd"));
		assertEquals("20171231", resultData.getString("rtnEdYmd"));
	}*/

	/**
	 * 데이턱 데이터 수령
	 */
	/*public void test1115receiveDatukData() throws Exception
	{
		logger.info("\n==== 데이턱 데이터 수령 : 정상 동작 체크");
		
		url = URL + "/datuk/receive?datukId=" + receiveDatukId;
		resultObj = new JSONObject(callFunc(url, null, "POST"));
		assertEquals("200", resultObj.getString("resultCd"));

		// 재수령
		resultObj = new JSONObject(callFunc(url, null, "POST"));
		assertEquals("한번 받은 데이턱은 다시 받을 수 없어요.", resultObj.getString("resultMsg"));
	}*/
	
	/**
	 * 선물하기
	 */
	/*public void test1118gift() throws Exception
	{
		logger.info("\n==== 선물하기 : 정상 동작 체크");
		
		JSONObject paramObj = new JSONObject();
		paramObj.put("sndCntrNo", cntrNo);
		paramObj.put("rcvMobileNo", "01043166325");
		paramObj.put("rcvUserNm", "채한얼");
		paramObj.put("dataAmt", 200);

		url = URL + "/gift";
		resultObj = new JSONObject(callFunc(url, paramObj, "POST"));
		assertEquals("200", resultObj.getString("resultCd"));
		
	}*/
	
	/**
	 * 누적 선물 데이터 목록 조회
	 */
	/*public void test1120getAccmGiftDataList() throws Exception
	{
		logger.info("\n==== 누적 선물 데이터 목록 조회 : 정상 동작 체크");
		
		url = URL + "/gift/accm";
		resultObj = new JSONObject(callFunc(url, null, "GET"));
		assertEquals("200", resultObj.getString("resultCd"));

		JSONArray resultDataList = resultObj.getJSONArray("resultInfoList");
		assertEquals(2, resultDataList.length());

		assertEquals(200, resultDataList.getJSONObject(0).getInt("dataAmt"));
		assertEquals("G0001", resultDataList.getJSONObject(0).getString("reqTp"));
		assertEquals(200, resultDataList.getJSONObject(1).getInt("dataAmt"));
		assertEquals("G0002", resultDataList.getJSONObject(1).getString("reqTp"));
		
	}*/
	
	/**
	 * 데이터 박스 상세 목록 조회
	 */
//	public void test1130getDboxDtlList() throws Exception
//	{
//		logger.info("\n==== 데이터 박스 상세 목록 조회 : 정상 동작 체크");
//		
//		url = URL + "/dbox/dtl?srchStYm=201709&srchEdYm=201710";
//		resultObj = new JSONObject(callFunc(url, null, "GET"));
//
//		JSONArray resultDataList = resultObj.getJSONArray("resultInfoList");
//		assertEquals(2, resultDataList.length());
//		JSONObject resultData = resultDataList.getJSONObject(0);
//		assertEquals("201710", resultData.getString("srchYm"));
//		JSONArray databoxDtlList = resultData.getJSONArray("databoxDtlList");
//		
//		assertEquals("홍길동", databoxDtlList.getJSONObject(0).getString("dboxContents"));
//		assertEquals(100, databoxDtlList.getJSONObject(0).getInt("useDataAmt"));
//		assertEquals(1000, databoxDtlList.getJSONObject(0).getInt("rmnDataAmt"));
//		assertEquals("G0001", databoxDtlList.getJSONObject(0).getString("useTp"));
//	}
	
//	/**
//	 * 데이턱 데이터를 회수 처리한다.
//	 */
//	public void test1150returnDatukData() throws Exception
//	{
//		logger.info("\n==== 데이턱 데이터를 회수 처리한다. : 정상 동작 체크");
//		
//		url = URL + "/datuk/return?datukId=" + returnDatukId + "&cntrNo=" + cntrNo;
//		resultObj = new JSONObject(callFunc(url, null, "POST"));
//		assertEquals("200", resultObj.getString("resultCd"));
//		assertEquals(1, resultObj.getInt("resultData"));
//
//	}
//	
//	/**
//	 * 보너스 데이터 수령한다. 수령건수를 반환.
//	 */
//	public void test1160receiveBonusData() throws Exception
//	{
//		logger.info("\n==== 보너스 데이터 수령한다. : 정상 동작 체크");
//		
//		url = URL + "/gift/rcv/bonus?cntrNo=" + cntrNo + "&bonusId=" + bonusId;
//		resultObj = new JSONObject(callFunc(url, null, "POST"));
//		assertEquals(1, resultObj.getInt("resultData"));
//
//		// 보너스 중복오류 체크
//		resultObj = new JSONObject(callFunc(url, null, "POST"));
//		assertEquals("이번 달 보너스 데이터를 이미 받으셨어요. 보너스 데이터는 월 1회만 받을 수 있어요.", resultObj.getString("resultMsg"));
//		
//	}
//	
//	/**
//	 * 프로모션 데이터 수령한다. 수령건수를 반환.
//	 */
//	public void test1170receivePromotionData() throws Exception
//	{
//		logger.info("\n==== 프로모션 데이터 수령한다. 수령건수를 반환. : 정상 동작 체크");
//		
//		url = URL + "/gift/rcv/bonus?cntrNo=" + cntrNo + "&bonusId=" + bonusIdPromotion;
//		resultObj = new JSONObject(callFunc(url, null, "POST"));
//		assertEquals("200", resultObj.getString("resultCd"));
//		assertEquals(1, resultObj.getInt("resultData"));
//	}
//	
//	/**
//	 * 데이터 박스에서 데이터 꺼내기
//	 */
//	public void test1190pullDboxData() throws Exception
//	{
//		logger.info("\n==== 데이터 박스에서 데이터 꺼내기 : 정상 동작 체크");
//		
//		url = URL + "/dbox/pull?cntrNo=" + cntrNo + "&dataAmt=" + 200;
//		resultObj = new JSONObject(callFunc(url, null, "POST"));
//		assertEquals("200", resultObj.getString("resultCd"));
//
//		url = URL + "/dbox";
//		resultObj = new JSONObject(callFunc(url, null, "GET"));
//
//		JSONObject resultData = resultObj.getJSONObject("resultData").getJSONObject("dboxPullInfo");
//		assertEquals(30, resultData.getInt("dboxMaxPullCnt"));
//		assertEquals(1, resultData.getInt("dboxPullCnt"));
//		assertEquals(200, resultData.getInt("dboxPullDataAmt"));
//	}
//	
//	/**
//	 * 데이터 박스 정보를 조회한다.
//	 */
//	public void test1200getDboxInfo() throws Exception
//	{
//		logger.info("\n==== 데이터 박스 정보를 조회한다. : 정상 동작 체크");
//		
//		url = URL + "/dbox";
//		resultObj = new JSONObject(callFunc(url, null, "GET"));
//		assertEquals("200", resultObj.getString("resultCd"));
//
//		JSONObject resultData = resultObj.getJSONObject("resultData");
//		assertEquals(500, resultData.getInt("dboxDataAmtCurMnth"));
//		assertEquals(1500, resultData.getInt("dboxDataAmtNextMnth"));
//		assertEquals(2000, resultData.getInt("dboxDataAmt"));
//		
//	}
//	
//	/**
//	 * 데이터 나눔 화면 정보 조회
//	 */
//	public void test1205_viewDataDiv() throws Exception
//	{
//		logger.info("\n==== 데이터 나눔 화면 정보 조회 : 정상 동작 체크");
//		
//		url = URL + "/main/divide";
//		resultObj = new JSONObject(callFunc(url, null, "GET"));
//		assertEquals("200", resultObj.getString("resultCd"));
//		
//		JSONObject myDataInfo = resultObj.getJSONObject("resultData").getJSONObject("myDataInfo");
//		JSONObject datukInfo = resultObj.getJSONObject("resultData").getJSONObject("datukInfo");
//		assertEquals(200, datukInfo.getInt("datukAmt"));
//		assertEquals(YappUtil.getCurDate("yyyyMMdd", Calendar.DATE, 1), datukInfo.getString("joinEdYmd"));
//		assertEquals(8657, myDataInfo.getInt("myDataSize"));
//	}
//	
//	/**
//	 * 데이터 나눔 상세 목록 조회
//	 */
//	public void test1206_getDataDivDtlList() throws Exception
//	{
//		logger.info("\n==== 데이터 나눔 상세 목록 조회 : 정상 동작 체크");
//		
//		url = URL + "/gift/dtl?srchYm=201710";
//		resultObj = new JSONObject(callFunc(url, null, "GET"));
//		assertEquals("200", resultObj.getString("resultCd"));
//		
//		JSONArray resultDataList = resultObj.getJSONArray("resultInfoList");
//		assertEquals(2, resultDataList.length());
//		JSONObject resultData = resultDataList.getJSONObject(0);
//		assertEquals("201710", resultData.getString("srchYm"));
//		JSONArray databoxDtlList = resultData.getJSONArray("dataDivDtlList");
//		
//		assertEquals("홍길동", databoxDtlList.getJSONObject(0).getString("divContents"));
//		assertEquals(100, databoxDtlList.getJSONObject(0).getInt("useDataAmt"));
//		assertEquals(1000, databoxDtlList.getJSONObject(0).getInt("rmnDataAmt"));
//	}
//	
//	/**
//	 * yapp 가입여부 조회
//	 */
//	public void test1210GetJoinInfoList() throws Exception
//	{
//		JSONArray paramArray = new JSONArray();
//		paramArray.put("01043176325");
//		paramArray.put("01028733096");
//		paramArray.put("01080801004");
//
//		logger.info("\n==== yapp 가입여부 조회 : 정상 동작 체크");
//		
//		url = URL + "/user/joininfo";
//		resultObj = new JSONObject(callFunc(url, paramArray));
//
//		JSONArray joinInfoList = resultObj.getJSONObject("resultData").getJSONArray("joinInfoList");
//		assertEquals(joinInfoList.length(), paramArray.length());
//		assertEquals("Y", joinInfoList.getJSONObject(0).get("yappJoinYn"));
//		assertEquals("N", joinInfoList.getJSONObject(2).get("yappJoinYn"));
//		assertEquals("G0001", joinInfoList.getJSONObject(0).get("dboxStatus"));
//		assertEquals("G0002", joinInfoList.getJSONObject(2).get("dboxStatus"));
//	}
//
//	/**
//	 * 초대하기 정보 추가
//	 */
//	public void test1220insertInvtInfo() throws Exception
//	{
//		logger.info("\n==== 초대하기 정보 추가 : 정상 동작 체크");
//		
//		JSONObject paramObj = new JSONObject();
//		paramObj.put("invCntrNo", "561924238");
//		paramObj.put("invTp", "G0001");
//
//		url = URL + "/user/invt";
//		resultObj = new JSONObject(callFunc(url, paramObj, "POST"));
//		assertEquals(1, resultObj.getInt("resultData"));
//
//	}
//	
//	/**
//	 * 데이터 조르기
//	 */
//	public void test1230reqGiftData() throws Exception
//	{
//		logger.info("\n==== 데이터 조르기 : 정상 동작 체크");
//		
//		url = URL + "/gift/req?rcvMobileNo=01043166325&dataAmt=7000";
//		resultObj = new JSONObject(callFunc(url, null, "POST"));
//		assertEquals("200", resultObj.getString("resultCd"));
//	}
//	
//	/**
//	 * 내 데이터 잔여량 정보 조회
//	 */
//	public void test1240viewMyRmnDataInfo() throws Exception
//	{
//		logger.info("\n==== 내 데이터 잔여량 정보 조회 : 정상 동작 체크");
//		
//		url = URL + "/main/mydata";
//		resultObj = new JSONObject(callFunc(url, null, "GET"));
//		
//		JSONObject resultData = resultObj.getJSONObject("resultData");
//		assertEquals(1, resultData.getJSONArray("dsprDataList").length());
//		assertEquals(8296, resultData.getInt("myRmnDataAmt"));
//		assertEquals(8557, resultData.getInt("myDataSize"));
//		
//	}
//	
//	/**
//	 * 내 데이터 잔여량 정보 조회(알요금제)
//	 */
//	public void test1250viewMyRmnEggDataInfo() throws Exception
//	{
//		logger.info("\n==== 내 데이터 잔여량 정보 조회(알요금제) : 정상 동작 체크");
//		
//		url = URL + "/main/mydata/egg";
//		resultObj = new JSONObject(callFunc(url, null, "GET"));
//		assertEquals("200", resultObj.getString("resultCd"));
//		
//		JSONObject resultData = resultObj.getJSONObject("resultData");
//		assertEquals(0, resultData.getInt("myDataSize"));
//		assertEquals(4958, resultData.getInt("myRmnDataAmt"));
//	}
//	
//	/**
//	 * 내 데이터 잔여량 상세 정보 조회
//	 */
//	public void test1260viewMyRmnDataDtlInfo() throws Exception
//	{
//		logger.info("\n==== 내 데이터 잔여량 상세 정보 조회 : 정상 동작 체크");
//		
//		url = URL + "/main/mydata/dtl";
//		resultObj = new JSONObject(callFunc(url, null, "GET"));
//		JSONArray resultInfoList = resultObj.getJSONArray("resultInfoList");
//		assertEquals("기본제공", resultInfoList.getJSONObject(0).get("dataNm"));
//		assertEquals(2000, resultInfoList.getJSONObject(0).get("dataAmt"));
//	}
//	
//	/**
//	 * 내 데이터 월별 상세 정보 조회
//	 */
//	public void test1270viewUseDataByMnth() throws Exception
//	{
//		logger.info("\n==== 내 데이터 월별 상세 정보 조회 : 정상 동작 체크");
//		
//		url = URL + "/main/mydata/mnth";
//		resultObj = new JSONObject(callFunc(url, null, "GET"));
//		
//		JSONArray resultInfoList = resultObj.getJSONArray("resultInfoList");
//		assertEquals("201711", resultInfoList.getJSONObject(0).get("useYm"));
//		assertEquals(1423, resultInfoList.getJSONObject(0).get("useDataAmt"));
//	}
//	
//	/**
//	 * 선물 가능 정보를 조회한다.
//	 */
//	public void test1280getGiftPsbInfo() throws Exception
//	{
//		logger.info("\n==== 선물 가능 정보를 조회한다. : 정상 동작 체크");
//		
//		url = URL + "/gift/psbinfo";
//		resultObj = new JSONObject(callFunc(url, null, "GET"));
//
//		JSONObject resultData = resultObj.getJSONObject("resultData");
//		assertEquals(2, resultData.getInt("giftCnt"));
//		assertEquals(400, resultData.getInt("giftDataAmt"));
//		assertEquals(1600, resultData.getInt("giftPsbDataAmt"));
//		assertEquals(20, resultData.getInt("giftPsbMaxCntPerMnth"));
//		assertEquals(2000, resultData.getInt("giftPsbMaxDataAmtPerMnth"));
//		assertEquals(1000, resultData.getInt("giftPsbMaxDataAmtOneTime"));
//		assertEquals(500, resultData.getInt("giftMinDataAmtAfter"));
//	}
//	
//	/**
//	 * 계약 목록 조회
//	 */
//	public void test1290GetCntrList() throws Exception
//	{
//		logger.info("\n==== 계약 목록 조회 : 정상 동작 체크");
//		
//		url = URL + "/user/contract/list?userId=" + PARAM_MAP.get("userId") + "&credentialId=" + cid;
//		resultObj = new JSONObject(callFunc(url, null, "GET"));
//		assertEquals("200", resultObj.getString("resultCd"));
//
//		JSONArray resultData = resultObj.getJSONArray("resultInfoList");
//		assertEquals(resultData.length(), PARAM_MAP.get("cnt"));
//	}
//
//	/**
//	 * 공지사항 목록 조회
//	 */
//	public void test1300GetNoticeList() throws Exception
//	{
//		logger.info("\n==== 공지사항 목록 조회 : 정상 동작 체크");
//		
//		JSONObject paramObj = new JSONObject();
//		paramObj.put("stIdx", 2);
//		paramObj.put("edIdx", 3);
//		
//		url = URL + "/cms/bbs/notice?stIdx=2&edIdx=3";
//		resultObj = new JSONObject(callFunc(url, null, "GET"));
//
//		JSONArray resultInfoList = resultObj.getJSONArray("resultInfoList");
//		assertEquals(2, resultInfoList.length());
//		assertEquals("20170828", resultInfoList.getJSONObject(0).get("postStYmd"));
//		assertEquals("20180930", resultInfoList.getJSONObject(0).get("postEdYmd"));
//		assertEquals("공지사항2", resultInfoList.getJSONObject(0).get("title"));
//		assertEquals("공지사항 내용2", resultInfoList.getJSONObject(0).get("contents"));
//	}
//	
//	/**
//	 * 알림 목록 조회
//	 */
//	public void test1310GetNotiMsgList() throws Exception
//	{
//		logger.info("\n==== 알림 목록 조회 : 정상 동작 체크");
//		
//		url = URL + "/cms/notimsg?cntrNo=" + cntrNo;
//		resultObj = new JSONObject(callFunc(url, null, "GET"));
//		
//		JSONArray resultInfoList = resultObj.getJSONArray("resultInfoList");
//		assertEquals(7, resultInfoList.length());
//		assertEquals("G0001", resultInfoList.getJSONObject(1).get("notiTp"));
//		assertEquals("Y", resultInfoList.getJSONObject(1).get("newYn"));
//		assertEquals("메시지1...", resultInfoList.getJSONObject(1).get("notiMsg"));
//
//		url = URL + "/cms/notimsg?cntrNo=" + cntrNo;
//		resultObj = new JSONObject(callFunc(url, null, "GET"));
//		
//		resultInfoList = resultObj.getJSONArray("resultInfoList");
//		assertEquals("N", resultInfoList.getJSONObject(0).get("newYn"));
//	}
//	
//	/**
//	 * 알림 전체 삭제
//	 */
//	public void test1320DeleteAllNotiMsg() throws Exception
//	{
//		logger.info("\n==== 알림 전체 삭제 : 정상 동작 체크");
//		
//		url = URL + "/cms/notimsg?cntrNo=" + cntrNo;
//		resultObj = new JSONObject(callFunc(url, null, "DELETE"));
//		
//		assertEquals("200", resultObj.getString("resultCd"));
//		
//		// 알림 목록 조회
//		url = URL + "/cms/notimsg?cntrNo=" + cntrNo;
//		resultObj = new JSONObject(callFunc(url, null, "GET"));
//		
//		JSONArray resultInfoList = resultObj.getJSONArray("resultInfoList");
//		assertEquals(0, resultInfoList.length());
//	}
//	
//	private static int evtSeq;
//	
//	/**
//	 * 이벤트 목록 조회
//	 */
//	public void test1330GetEventList() throws Exception
//	{
//		logger.info("\n==== 이벤트 목록 조회 : 정상 동작 체크");
//		
//		url = URL + "/cms/event";
//		resultObj = new JSONObject(callFunc(url, null, "GET"));
//		assertEquals("200", resultObj.getString("resultCd"));
//
//		JSONArray resultInfoList = resultObj.getJSONArray("resultInfoList");
//		evtSeq = YappUtil.toInt(resultInfoList.getJSONObject(0).get("evtSeq"));
//		
//		assertEquals(2, resultInfoList.length());
//		assertEquals("이벤트2", resultInfoList.getJSONObject(0).get("title"));
//		assertEquals("이벤트2 내용...", resultInfoList.getJSONObject(0).get("contents"));
//		assertEquals("유의사항2...", resultInfoList.getJSONObject(0).get("notes"));
//		assertEquals("20170901", resultInfoList.getJSONObject(0).get("evtStDt"));
//		assertEquals("20171215", resultInfoList.getJSONObject(0).get("evtEdDt"));
//		assertEquals("b5858515-489e-4861-8bea-87ad7f9e1571", resultInfoList.getJSONObject(0).get("listImgFileId"));
//		assertEquals("G0001", resultInfoList.getJSONObject(0).get("evtTp"));
//		assertEquals("win!!", resultInfoList.getJSONObject(0).get("winMsg"));
//		assertEquals("20171015", resultInfoList.getJSONObject(0).get("winYmd"));
//		assertEquals("외부이동버튼", resultInfoList.getJSONObject(0).get("btnLbl"));
//		assertEquals("http://www.naver.com", resultInfoList.getJSONObject(0).get("linkUrl"));
//		assertEquals("N", resultInfoList.getJSONObject(0).get("applYn"));
//		assertEquals("N", resultInfoList.getJSONObject(0).get("winYn"));
//	}
//	
//	/**
//	 * 이벤트 응모
//	 */
//	public void test1340ApplyEvent() throws Exception
//	{
//		JSONObject paramObj = new JSONObject();
//		
//		logger.info("\n==== 이벤트 응모 : 정상 동작 체크");
//		
//		url = URL + "/cms/event/appl?evtSeq=" + evtSeq;
//		resultObj = new JSONObject(callFunc(url, paramObj));
//		
//		assertEquals("200", resultObj.getString("resultCd"));
//
//		// 이벤트 목록 조회
//		url = URL + "/cms/event?cntrNo=" + cntrNo;
//		resultObj = new JSONObject(callFunc(url, null, "GET"));
//
//		JSONArray resultInfoList = resultObj.getJSONArray("resultInfoList");
//		assertEquals("Y", resultInfoList.getJSONObject(0).get("applYn"));
//	}
//	
//	/**
//	 * 이용안내 목록 조회
//	 */
//	public void test1345getGuideList() throws Exception
//	{
//		logger.info("\n==== 이용안내 목록 조회 : 정상 동작 체크");
//		
//		url = URL + "/cms/guide";
//		resultObj = new JSONObject(callFunc(url, null, "GET"));
//		
//		JSONArray resultInfoList = resultObj.getJSONArray("resultInfoList");
//		assertEquals(2, resultInfoList.length());
//		assertEquals("guide 1", resultInfoList.getJSONObject(0).get("title"));
//		assertEquals("guide 1..........", resultInfoList.getJSONObject(0).get("contents"));
//	}
//	
//	/**
//	 * 사용자 정보 변경
//	 */
//	public void test1350UpdateUserSetting() 
//	{
//		try {
//			JSONObject paramObj = new JSONObject();
//			paramObj.put("reqRcvYn", "N");
//			paramObj.put("mktRcvYn", "N");
//			paramObj.put("pushRcvYn", "N");
//	
//			logger.info("\n==== 사용자 정보 변경 : 정상 동작 체크");
//			
//			url = URL + "/user/setting";
//			resultObj = new JSONObject(callFunc(url, paramObj));
//
//			JSONObject resultData = resultObj.getJSONObject("resultData");
//			assertEquals(resultData.get("pushRcvYn"), "N");
//			assertEquals(resultData.get("mktRcvYn"), "N");
//			assertEquals(resultData.get("reqRcvYn"), "N");
//			
//			paramObj.put("pushRcvYn", "Y");
//			url = URL + "/user/setting";
//			resultObj = new JSONObject(callFunc(url, paramObj));
//
//			resultData = resultObj.getJSONObject("resultData");
//			assertEquals(resultData.get("pushRcvYn"), "Y");
//			assertEquals(resultData.get("mktRcvYn"), "N");
//			assertEquals(resultData.get("reqRcvYn"), "N");
//
//			paramObj.put("mktRcvYn", "Y");
//			url = URL + "/user/setting";
//			resultObj = new JSONObject(callFunc(url, paramObj));
//			
//			resultData = resultObj.getJSONObject("resultData");
//			assertEquals(resultData.get("pushRcvYn"), "Y");
//			assertEquals(resultData.get("mktRcvYn"), "Y");
//			assertEquals(resultData.get("reqRcvYn"), "N");
//			
//			paramObj.put("reqRcvYn", "Y");
//			url = URL + "/user/setting";
//			resultObj = new JSONObject(callFunc(url, paramObj));
//			
//			resultData = resultObj.getJSONObject("resultData");
//			assertEquals(resultData.get("pushRcvYn"), "Y");
//			assertEquals(resultData.get("mktRcvYn"), "Y");
//			assertEquals(resultData.get("reqRcvYn"), "Y");
//			
//			resultObj = new JSONObject(callFunc(url, paramObj));
//			resultData = resultObj.getJSONObject("resultData");
//			assertEquals(resultData.get("pushRcvYn"), "Y");
//
//			paramObj.put("reqRcvYn", "Y");
//			paramObj.put("mktRcvYn", "Y");
//			paramObj.put("pushRcvYn", "Y");
//			
//			resultObj = new JSONObject(callFunc(url, paramObj));
//			resultData = resultObj.getJSONObject("resultData");
//			assertEquals(resultData.get("pushRcvYn"), "Y");
//			assertEquals(resultData.get("mktRcvYn"), "Y");
//			assertEquals(resultData.get("reqRcvYn"), "Y");
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//			fail();
//		}
//	}
//	
//	/**
//	 * 선택 약관 정보 변경
//	 */
//	public void test1360updateOptTerms() throws Exception
//	{
//		logger.info("\n==== 선택 약관 정보 변경 : 정상 동작 체크");
//		
//		url = URL + "/user/terms/opt?optTermsAgreeYn=N";
//		resultObj = new JSONObject(callFunc(url, null));
//		
//		assertEquals(1, resultObj.get("resultData"));
//
//		// 회선 선택
//		url = URL + "/na/user/line?cntrNo=" + cntrNo;
//		resultObj = new JSONObject(callFunc(url));
//
//		JSONObject resultData = resultObj.getJSONObject("resultData");
//		JSONObject termsAgree = resultData.getJSONObject("termsAgree");
//		assertEquals("N", termsAgree.get("optTermsAgreeYn"));
//		
//		url = URL + "/user/terms/opt?optTermsAgreeYn=Y";
//		resultObj = new JSONObject(callFunc(url, null));
//		
//		assertEquals(1, resultObj.get("resultData"));
//		
//		// 회선 선택
//		url = URL + "/na/user/line?cntrNo=" + cntrNo;
//		resultObj = new JSONObject(callFunc(url));
//		
//		resultData = resultObj.getJSONObject("resultData");
//		termsAgree = resultData.getJSONObject("termsAgree");
//		assertEquals("Y", termsAgree.get("optTermsAgreeYn"));
//	}
//	
//	/**
//	 * 부가서비스 상품 목록 조회
//	 */
//	public void test1500getVasItemList() throws Exception
//	{
//		logger.info("\n==== 부가서비스 상품 목록 조회 : 정상 동작 체크");
////		MTPMN: My Time Plan, PLLDAT: 당겨쓰기, LTEDTC: 후불 데이터 충전, LTERTE: 룰렛, LTEDTP: 멤버십 데이터 충전 
//		
//		url = URL + "/vas/item?vasCd=LTEDTC";
//		resultObj = new JSONObject(callFunc(url, null, "GET"));
//		
//		JSONArray resultInfoList = resultObj.getJSONObject("resultData").getJSONArray("vasItemList");
//		assertEquals(5, resultInfoList.length());
//		assertEquals("LTEDTC100", resultInfoList.getJSONObject(0).get("vasItemCd"));
//	}
////	
////	/**
////	 * 데이터 충전(후불)
////	 */
////	public void test1510chargePayLater() throws Exception
////	{
////		logger.info("\n==== 데이터 충전(후불) : 정상 동작 체크");
////		
////		url = URL + "/vas/charge/later?cntrNo=" + cntrNo + "&vasItemCd=LTEDTC100";
////		resultObj = new JSONObject(callFunc(url, null, "POST"));
////		
////		JSONObject resultData = resultObj.getJSONObject("resultData");
////		assertEquals("200", resultData.get("resultCd"));
////	}
////	
////	/**
////	 * 데이터 충전(멤버십)
////	 */
////	public void test1520chargeMembership() throws Exception
////	{
////		logger.info("\n==== 데이터 충전(멤버십) : 정상 동작 체크");
////		
////		url = URL + "/vas/charge/membership?cntrNo=" + cntrNo + "&vasItemCd=LTEDTP300";
////		resultObj = new JSONObject(callFunc(url, null, "POST"));
////		
////		url = URL + "/vas/charge/membership?cntrNo=" + cntrNo + "&vasItemCd=LTEDTP500";
////		resultObj = new JSONObject(callFunc(url, null, "POST"));
////		
////		url = URL + "/vas/charge/membership?cntrNo=" + cntrNo + "&vasItemCd=LTEDTP01G";
////		resultObj = new JSONObject(callFunc(url, null, "POST"));
////		
////		url = URL + "/vas/charge/membership?cntrNo=" + cntrNo + "&vasItemCd=LTEDTP02G";
////		resultObj = new JSONObject(callFunc(url, null, "POST"));
////		
////		JSONObject resultData = resultObj.getJSONObject("resultData");
////		assertEquals("200", resultData.get("resultCd"));
////	}
////	
////	/**
////	 * 데이터 룰렛
////	 */
////	public void test1530roulette() throws Exception
////	{
////		logger.info("\n==== 데이터 룰렛 : 정상 동작 체크");
////		
////		url = URL + "/vas/roulette?cntrNo=" + cntrNo + "&vasItemCd=LTERTE100";
////		resultObj = new JSONObject(callFunc(url, null, "POST"));
////		
////		JSONObject resultData = resultObj.getJSONObject("resultData");
////		assertEquals("200", resultData.get("resultCd"));
////	}
////	
////	/**
////	 * 당겨쓰기 신청
////	 */
////	public void test1540pullData() throws Exception
////	{
////		logger.info("\n==== 당겨쓰기 신청 : 정상 동작 체크");
////		
////		url = URL + "/vas/pull?cntrNo=" + cntrNo + "&vasItemCd=PLLDAT100";
////		resultObj = new JSONObject(callFunc(url, null, "POST"));
////		
////		JSONObject resultData = resultObj.getJSONObject("resultData");
////		assertEquals("200", resultData.get("resultCd"));
////	}
////	
////	/**
////	 * 매일 3시간 부가서비스 신청 정보 조회
////	 */
////	public void test1550getTimePlan() throws Exception
////	{
////		logger.info("\n==== 매일 3시간 부가서비스 신청 정보 조회 : 정상 동작 체크");
////		
////		url = URL + "/vas/timeplan?cntrNo=" + cntrNo + "&vasItemCd=PLL100";
////		resultObj = new JSONObject(callFunc(url, null, "GET"));
////		
////		JSONObject resultData = resultObj.getJSONObject("resultData");
////		assertEquals("200", resultData.get("resultCd"));
////	}
////	
////	/**
////	 * 매일 3시간 부가서비스 신청, 변경
////	 */
////	public void test1560setTimePlan() throws Exception
////	{
////		logger.info("\n==== 매일 3시간 부가서비스 신청 정보 조회 : 정상 동작 체크");
////		
////		url = URL + "/vas/timeplan?cntrNo=" + cntrNo + "&vasItemCd=PLL100";
////		resultObj = new JSONObject(callFunc(url, null, "POST"));
////		
////		JSONObject resultData = resultObj.getJSONObject("resultData");
////		assertEquals("200", resultData.get("resultCd"));
////	}
////	
//	/**
//	 * 반값팩 쿠폰 목록 조회
//	 */
//	public void test1570getHalfPrice() throws Exception
//	{
//		logger.info("\n==== 반값팩 쿠폰 목록 조회 : 정상 동작 체크");
//		
//		login("01043166325");
//		
//		url = URL + "/vas/half";
//		resultObj = new JSONObject(callFunc(url, null, "GET"));
//		
//		JSONArray resultInfoList = resultObj.getJSONArray("resultInfoList");
//		assertEquals(5, resultInfoList.length());
//		for ( int i = 0; i < resultInfoList.length(); i++ )
//		{
//			assertNotNull(resultInfoList.getJSONObject(i).get("cpnNm"));
//			assertNotNull(resultInfoList.getJSONObject(i).get("cpnNo"));
//			assertNotNull(resultInfoList.getJSONObject(i).get("cpnPrice"));
//			assertNotNull(resultInfoList.getJSONObject(i).get("cpnUseYn"));
//			assertNotNull(resultInfoList.getJSONObject(i).get("cpnValidStYmd"));
//			assertNotNull(resultInfoList.getJSONObject(i).get("cpnValidEdYmd"));
//		}
//	}
////	
////	/**
////	 * 반값팩 신청
////	 */
////	public void test1580setHalfPrice() throws Exception
////	{
////		logger.info("\n==== 반값팩 신청 : 정상 동작 체크");
////		
////		url = URL + "/vas/half?cntrNo=" + cntrNo + "&vasItemCd=PLL100";
////		resultObj = new JSONObject(callFunc(url, null, "POST"));
////		
////		JSONObject resultData = resultObj.getJSONObject("resultData");
////		assertEquals("200", resultData.get("resultCd"));
////	}
////	
////	/**
////	 * 두배쓰기 설정 정보 조회
////	 */
////	public void test1590getDoubleDataInfo() throws Exception
////	{
////		logger.info("\n==== 두배쓰기 설정 정보 조회 : 정상 동작 체크");
////		
////		url = URL + "/vas/double";
////		resultObj = new JSONObject(callFunc(url, null, "GET"));
////		
////		JSONObject resultData = resultObj.getJSONObject("resultData");
////		assertEquals("200", resultData.get("resultCd"));
////	}
////	
////	/**
////	 * 두배쓰기 설정
////	 */
////	public void test1600setDoubleData() throws Exception
////	{
////		logger.info("\n==== 두배쓰기 설정 : 정상 동작 체크");
////		
////		url = URL + "/vas/double?cntrNo=" + cntrNo + "&applTp=G0002";
////		resultObj = new JSONObject(callFunc(url, null, "POST"));
////		
////		JSONObject resultData = resultObj.getJSONObject("resultData");
////		assertEquals("200", resultData.get("resultCd"));
////	}
////	
////	/**
////	 * 바꿔쓰기 상품 목록 조회
////	 */
////	public void test1610getExchangeItemList() throws Exception
////	{
////		logger.info("\n==== 바꿔쓰기 상품 목록 조회 : 정상 동작 체크");
////		
////		url = URL + "/vas/exchange";
////		resultObj = new JSONObject(callFunc(url, null, "GET"));
////		
////		JSONObject resultData = resultObj.getJSONObject("resultData");
////		assertEquals("200", resultData.get("resultCd"));
////	}
////	
////	/**
////	 * 바꿔쓰기 설정
////	 */
////	public void test1620setExchangeItem() throws Exception
////	{
////		logger.info("\n==== 바꿔쓰기 설정 : 정상 동작 체크");
////		
////		url = URL + "/vas/exchange?cntrNo=" + cntrNo + "&vasItemCd=TYCHGVCDY&applTp=G0001";
////		resultObj = new JSONObject(callFunc(url, null, "POST"));
////		
////		JSONObject resultData = resultObj.getJSONObject("resultData");
////		assertEquals("200", resultData.get("resultCd"));
////	}
////	
////	/**
////	 * 장기혜택 쿠폰 목록 조회
////	 */
////	public void test1630getLongTermBnfCpnList() throws Exception
////	{
////		logger.info("\n==== 장기혜택 쿠폰 목록 조회 : 정상 동작 체크");
////		
////		url = URL + "/vas/longterm";
////		resultObj = new JSONObject(callFunc(url, null, "GET"));
////		
////		JSONObject resultData = resultObj.getJSONObject("resultData");
////		assertEquals("200", resultData.get("resultCd"));
////	}
////	
////	/**
////	 * 장기혜택 쿠폰 설정
////	 */
////	public void test1640setLongTermBnfCpn() throws Exception
////	{
////		logger.info("\n==== 장기혜택 쿠폰 설정 : 정상 동작 체크");
////		
////		url = URL + "/vas/longterm?cntrNo=" + cntrNo + "&vasItemCd=PLL100";
////		resultObj = new JSONObject(callFunc(url, null, "POST"));
////		
////		JSONObject resultData = resultObj.getJSONObject("resultData");
////		assertEquals("200", resultData.get("resultCd"));
////	}
////	
////	/**
////	 * 서비스 탈퇴
////	 */
////	public void test1900serviceOut() throws Exception
////	{
////		logger.info("\n==== 서비스 탈퇴 : 정상 동작 체크");
////		
////		JSONObject paramObj = new JSONObject();
////		paramObj.put("svcoutTp", "G0001");
////		paramObj.put("svcoutDesc", "service out");
////
////		url = URL + "/user/svcout";
////		resultObj = new JSONObject(callFunc(url, paramObj));
////		assertEquals("200", resultObj.getString("resultCd"));
////		
////	}
//	
//	/**
//	 * 로그인 처리
//	 */
//	private void loginMain(String mobileNo) throws Exception
//	{
//		cntrNo = cntrNoMain;
//		mobileNo = mobileNoMain;
//		ysid = ysidMain;
//	}
//	
//	private void login(String mobileNo) throws Exception
//	{
//		url = URL + "/na/login/mobileno?iptMobileNo=" + mobileNo;
//		resultObj = new JSONObject(callFunc(url, null, "GET"));
//		
//		JSONObject resultData = resultObj.getJSONObject("resultData");
//		ysid = resultData.getString("ysid");
//
//		JSONObject cntrInfo = resultData.getJSONObject("cntrInfo");
//		cntrNo = cntrInfo.getString("cntrNo");
//		mobileNo = cntrInfo.getString("mobileNo");
//	}
//
////
////	/***************************************************************************************************/
////	/***************************************************************************************************/
////	/*********************************** CmsController *******************************************************/
////
//	/**
//	 * 내 계약정보 조회
//	 */
////	public void test1032GetMyCntrInfo() throws Exception
////	{
////		logger.info("\n==== 내 계약 정보 조회 : 정상 동작 체크");
////		url = URL + "/user/mycontract";
////		resultObj = new JSONObject(callFunc(url, null, "GET"));
////		
////				assertEquals("200", resultObj.getString("resultCd"));
//
////		
////		JSONObject resultData = resultObj.getJSONObject("resultData");
////		assertEquals(mobileNo, resultData.getString("mobileNo"));
////		assertEquals(cntrNo, resultData.getString("cntrNo"));
////		assertEquals("01", resultData.getString("cntrcStatusCd"));
////		assertEquals("이원태", resultData.getString("userNm"));
////	}
//	
//	/**
//	 * 내 데이터 정보 조회
//	 */
////	public void test1033GetMyDataInfo() throws Exception
////	{
////		logger.info("\n==== 내 데이터 정보 조회 : 정상 동작 체크");
////		url = URL + "/user/mydata";
////		resultObj = new JSONObject(callFunc(url, null, "GET"));
////
////				assertEquals("200", resultObj.getString("resultCd"));
//
////		
////		JSONObject resultData = resultObj.getJSONObject("resultData");
////		assertEquals(2500000, resultData.getInt("totFreeQnt"));
////		assertEquals(1500000, resultData.getInt("tmonFreeQnt"));
////		assertEquals(1000000, resultData.getInt("cfwdFreeQnt"));
////		assertEquals(500000, resultData.getInt("freeUseQnt"));
////	}
//	
//
//
//	private static String cookieVal;
//	private String callFunc(String url) throws Exception
//	{
//		return callFunc(url, null);
//	}
//	private String callFunc(String url, Object outputParam) throws Exception
//	{
//		return callFunc(url, outputParam, "POST");
//	}
//	private static int cnt = 0;
//	private String callFunc(String url, Object outputParam, String method) throws Exception
//	{
//		boolean isPrintLog = true;
//		
//		if ( url.indexOf("/na/login/mobileno") > -1 )
//			isPrintLog = false;
//		
//		if ( isPrintLog )
//			logger.info("URL : " + url);
//		
//		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
//		conn.setRequestMethod(method);
//		conn.setDoOutput(true);
//		conn.setDoInput(true);
//		conn.setRequestProperty("Content-Type", "application/json");
//		conn.setRequestProperty("Accept", "application/json");
//		if ( ysid != null )
//			conn.setRequestProperty("ysid", ysid);
//		
//		conn.setRequestProperty("charset", "utf-8");
////		if ( cookieVal != null && cnt++ < 8 )
//		if ( cookieVal != null  )
//			conn.setRequestProperty("Cookie", cookieVal);
////		else
////			conn.setRequestProperty("Cookie", cookieVal + new Random().nextInt(10000));
//			
//
//		conn.connect();
//		if ( outputParam != null ) {
//			OutputStream os = conn.getOutputStream();
//			os.write(outputParam.toString().getBytes("utf-8"));
//			if ( isPrintLog )
//				logger.info(outputParam.toString());
//
//			os.flush();
//			
//			os.close();
//		}
//		
//		StringBuffer sb = new StringBuffer();
//		BufferedReader br = null;
//		if ( conn.getResponseCode() == 200 )
//			br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
//		else
//			br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "utf-8"));
//		
//		String line = null;
//		while ( (line = br.readLine()) != null ) {
//			sb.append(line);
////			logger.info(line);
//			if ( isPrintLog )
//				logger.info(line.replaceAll("([:][{])", "\n{"));
//		}
//		br.close();
//		
//		cookieVal = cookieVal == null ? conn.getHeaderField("Set-Cookie") : cookieVal;
//
//		return sb.toString();
//	}

}
