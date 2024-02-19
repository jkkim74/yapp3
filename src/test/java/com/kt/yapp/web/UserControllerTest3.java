package com.kt.yapp.web;

import com.kt.yapp.domain.GrpCode;
import com.kt.yapp.domain.RsaKeyInfo;
import com.kt.yapp.domain.resp.ResultInfo;
import com.kt.yapp.service.CmsService;
import com.kt.yapp.service.UserService;
import com.kt.yapp.util.AppEncryptUtils;
import com.kt.yapp.util.KeyFixUtil;
import com.kt.yapp.util.RsaCipherUtil;
import com.kt.yapp.util.YappUtil;
import junit.framework.TestCase;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.config.location=" +
		"classpath:application-local.properties,"+
		"classpath:application.properties")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserControllerTest3 extends TestCase
{
	private static String S_URL = "http://localhost:8080/yapp3";
//	private static String S_URL = "https://dev.ybox.kt.com/yapp3";
//	private static String URL = "http://10.217.81.117/yapp";
//	private static String URL = "http://14.63.149.227/yapp";
// private static String URL = "http://14.63.149.114/yapp";
	private static String osTp = "G0001";
	private static String appVrsn = "3.0.4";
	private static String mobileCd = "iPhone 12";

	@Autowired
	private UserService service;

	@Autowired
	private CmsService cmsService;

	@Autowired
	private AppEncryptUtils appEncryptUtils;

	@Value("${om.banner.url}")
	private String omBannerApiUrl;

	@Value("${om.banner.main.zoneCode}")
	private String omBannerMainZoneCode;

	@Autowired
	private KeyFixUtil keyFixUtil;
	
	private static final Logger logger = LoggerFactory.getLogger(UserControllerTest3.class);

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
	private static String userId ="dtffahah"; //"nmovi4";//"dtffahah";//"ydbox14";//"dtffahah";//"dtffahah";//"ydbox16";
	private static String pwd = "new!1234!";//"test1234!";//"new!1234";//"new1234!";//"new!1234";//"new1234!";
	public void setUserLoginInfo() throws Exception {

		RsaKeyInfo resultData = getRsaPublicKeyInfo(userId).getResultData();
		String publicKey = resultData.getPublicKey();
		int keySeq = resultData.getKeySeq();
		String encPwd = RsaCipherUtil.encryptRSA(pwd, publicKey);

		PARAM_MAP.put("userId",userId);
		PARAM_MAP.put("pwd",encPwd);
		PARAM_MAP.put("keySeq",String.valueOf(keySeq));
	}

	/**
	 * 공개키가져오기...
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public ResultInfo<RsaKeyInfo> getRsaPublicKeyInfo(String userId) throws Exception
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


	
	/**
	 * ID/PW 로그인
	 */
	@Order(1)
	@Test
	public void test100CheckLoginAccnt()
	{
		try {
			setUserLoginInfo();
			logger.info("\n==== ID/PW 로그인 : 정상 동작 확인");
			url = S_URL + "/na/user/login/accntusrforpwdbykey?userId=" + PARAM_MAP.get("userId") + "&pwd=" + URLEncoder.encode((String)PARAM_MAP.get("pwd"))+ "&keySeq=" + PARAM_MAP.get("keySeq");
			//url = S_URL + "/na/user/login/accntusrforpwdbykey?userId=" + userId + "&pwd=" + encPwd+ "&keySeq=" + keySeq;

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

	/**
	 * 회선 선택
	 */
	//@Ignore
	@Order(2)
	@Test
	public void test101SelectLine()
	{
		try {
			String encCntrNo= appEncryptUtils.aesEnc128(cntrNo, osTp+appVrsn);
			logger.info("\n==== 회선 선택 : 정상 동작 체크");
			url = S_URL + "/na/user/oneline?cntrNo=" + encCntrNo;
			resultObj = new JSONObject(callFunc(url));
			assertTrue(resultObj.getString("resultCd").equals("200"));
			JSONObject resultData = resultObj.getJSONObject("resultData");
			ysid = resultData.getString("ysid");
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	@Order(3)
	@Test
	public void test102OmBannerTest() throws Exception {
		url = S_URL + "/om/banner";
		String strBody = " {\n" +
				"            \"caVal1\": \"CMP000020313\",\n" +
				"            \"caVal2\": \"Ybox 상시 노출 캠페인 (1순위)\",\n" +
				"            \"caVal3\": \"OFFERAREA245\",\n" +
				"            \"caVal4\": \"BNR000021250\",\n" +
				"            \"html\": \"{ \\n  \\\"bgColor\\\" : \\\"#11e6d8\\\", \\n  \\\"urlNm\\\" : \\\"https://m.my.kt.com/product/s_MobilePriceView.do\\\", \\n  \\\"imageFileName\\\": \\\"https://tb.offer.onmas.kt.com/images/omscms_20230627184400.png\\\",\\n  \\\"title\\\": \\\"\\\",  \\n  \\\"subTitle\\\": \\\"\\\"\\n}\",\n" +
				"            \"bnStDt\": \"20230619000000\",\n" +
				"            \"bnFnsDt\": \"20230731235900\",\n" +
				"            \"statCd\": \"^ONMAS_YBOX^APP_혜택배너B^CMP000020313\",\n" +
				"            \"hashtagId1\": null,\n" +
				"            \"hashtagId2\": null,\n" +
				"            \"hashtagId3\": null\n" +
				"        }";
		resultObj = new JSONObject(callFunc(url,strBody));
		assertTrue(resultObj.getString("resultCd").equals("200"));
		JSONObject resultData = resultObj.getJSONObject("resultData");
	}

	/**
	 * 메인화면 정보 조회
	 */
	private static String bonusId;
	private static String bonusIdPromotion;
	@Ignore
	@Test
	public void test1100viewMain() throws Exception
	{
		logger.info("\n==== 메인화면 정보 조회 : 정상 동작 체크");

		url = S_URL + "/yboxMain";
		resultObj = new JSONObject(callFunc(url, null, "GET"));
		assertEquals("200", resultObj.getString("resultCd"));

		JSONObject resultData = resultObj.getJSONObject("resultData");
		System.out.println("resultData : "+resultData.toString());

        /*
        omBannerApiUrl : 온마시 배너 API URL 정보
		omBannerMainZoneCode : 온마시 배너 메인 ZoneCode 정보
		omEncKtId  : 암호화 된 KT ID 정보
		omBannerUseYn  : 온마시 배너 오픈 여부 정보( ex) Y/N)
         */
		assertTrue(resultData.getString("omBannerUseYn").equals("Y") || resultData.getString("omBannerUseYn").equals("N"));
		if(resultData.getString("omBannerUseYn").equals("N")){
			assertFalse(resultData.has("omBannerMainZoneCode"));
			assertFalse(resultData.has("omBannerApiUrl"));
			assertFalse(resultData.has("omEncKtId"));
		} else {
			assertEquals(omBannerApiUrl,resultData.getString("omBannerApiUrl"));
			assertEquals(omBannerMainZoneCode,resultData.getString("omBannerMainZoneCode"));
			assertEquals(userId,keyFixUtil.decode(resultData.getString("omEncKtId")));
		}
		System.out.println("omBannerUseYn        : "+resultData.getString("omBannerUseYn"));
		System.out.println("omBannerMainZoneCode : "+resultData.getString("omBannerMainZoneCode"));
		System.out.println("omBannerApiUrl       : "+resultData.getString("omBannerApiUrl"));
		System.out.println("omDecKtId            : "+keyFixUtil.decode(resultData.getString("omEncKtId")));
		System.out.println("omEncKtId            : "+resultData.getString("omEncKtId"));
		// 보너스 데이터 목록
		//bonusId = resultData.getJSONArray("bonusDataList").getJSONObject(0).getString("bonusId");
		//bonusIdPromotion = resultData.getJSONArray("bonusDataList").getJSONObject(1).getString("bonusId");
	}
	@Ignore
	@Test
	public void test1500getVasItemList() throws Exception
	{
		logger.info("\n==== 부가서비스 상품 목록 조회 : 정상 동작 체크");
//		MTPMN: My Time Plan, PLLDAT: 당겨쓰기, LTEDTC: 후불 데이터 충전, LTERTE: 룰렛, LTEDTP: 멤버십 데이터 충전

		url = S_URL + "/vas/item?vasCd=LTEDTP";
		resultObj = new JSONObject(callFunc(url, null, "GET"));

		JSONArray resultInfoList = resultObj.getJSONObject("resultData").getJSONArray("vasItemList");
		assertEquals(5, resultInfoList.length());
		assertEquals("LTEDTC100", resultInfoList.getJSONObject(0).get("vasItemCd"));
	}
	/**
	 * 내 데이터 정보 조회
	 */
	@Ignore
	@Test
	public void test1033GetMyDataInfo() throws Exception
	{
		logger.info("\n==== 내 데이터 정보 조회 : 정상 동작 체크");
		/* 신규 테스트 케이스....20230525
		* 01097840288 / 505612865
		* 01033298537 / 523671670
		* 01044690780 / 560215759
		*
		* 686328233 / 01040591988
        * 540127314 / 01041531043
        *
        * 01076271187 /540836557
        *
        *
			1. 5G데이터 비 무제한 가입자 (PL19CB590	5G Y 슬림)
			① Y덤_기본데이터 부가서비스 가입자
			601042192   01056190116 (5G 심플50GB + Y덤기본데이터50GB)
			525378627   01020636302 (5G 심플50GB + Y덤기본데이터50GB)
			605278145   01023098789 (5G 심플50GB + Y덤기본데이터50GB)

			② Y덤_통합데이터 부가서비스 가입자
			 -- 505621570 - 01063768349(5G 슬림)
			 -- 604111008 - 01029952091(5G 슬림)
			 -- 565855118 - 01096133213(5G 슬림)

			2. 5G데이터 무제한 가입자 (PL2058427	5G Y 슈퍼플랜 베이직 Plus)
			③ Y덤_공유데이터 부가서비스 가입자
			508046473 01066386241 (넷플릭스 초이스 프리미엄 + Y덤 공유데이터 100GB)
			605404381 01031296930 (넷플릭스 초이스 프리미엄 + Y덤 공유데이터 100GB)
			543820091 01043556344 (시즌+ 지니초이스 프리미엄 + Y덤 공유데이터 100GB)

			④ Y덤_통합데이터 부가서비스 가입자
			544866746  01028300448 (5G 세이브 + Y덤 통합 데이터 5GB)
			685489125  01025964880 (5G 세이브 + Y덤 통합 데이터 5GB)
			625554492  01067165581 (5G 세이브 + Y덤 통합 데이터 5GB)
		 */
		String loginMobileNo = "01029952091";//"01056190116";//"01033298537";
		String loginCntrNo ="604111008";//"601042192";//"523671670";
        String strUrl5GUnlimited = "/main/mydata/fivedtl";
		String strUrlB = "/main/mydata/dtl";
		url = S_URL + strUrlB+"?loginMobileNo="+loginMobileNo+"&loginCntrNo="+loginCntrNo;
		resultObj = new JSONObject(callFunc(url, null, "GET"));

		assertTrue(resultObj.getString("resultCd").equals("200"));
		
//		JSONObject resultData = resultObj.getJSONObject("resultData");
//		assertEquals(2500000, resultData.getInt("totFreeQnt"));
//		assertEquals(1500000, resultData.getInt("tmonFreeQnt"));
//		assertEquals(1000000, resultData.getInt("cfwdFreeQnt"));
//		assertEquals(500000, resultData.getInt("freeUseQnt"));
	}
	
	private static Integer authNum;
	private static int evtSeq;
	
	/**
	 * 이벤트 목록 조회
	 */
	public void test209GetEventList() throws Exception
	{
		logger.info("\n==== 이벤트 목록 조회 : 정상 동작 체크");
		
		url = S_URL + "/cms/event";
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
//		private static String osTp = "G0002";
//		private static String appVrsn = "3.0.4";
//		private static String mobileCd = "iPhone X";
		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		conn.setRequestMethod(method);
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Accept", "application/json");
		if ( ysid != null )
			conn.setRequestProperty("ysid", ysid);
		
		conn.setRequestProperty("charset", "utf-8");
		conn.setRequestProperty("osTp", osTp);
		conn.setRequestProperty("appVrsn", appVrsn);
		conn.setRequestProperty("mobileCd", mobileCd);
		conn.setRequestProperty("autoLogin", "false");
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
	}

}
