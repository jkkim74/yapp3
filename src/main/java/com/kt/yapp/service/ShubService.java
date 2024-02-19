package com.kt.yapp.service;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.kt.yapp.domain.CallingPlan;
import com.kt.yapp.domain.ContractInfo;
import com.kt.yapp.domain.Coupon;
import com.kt.yapp.domain.CouponDtl;
import com.kt.yapp.domain.CouponEnd;
import com.kt.yapp.domain.CouponEndInfo;
import com.kt.yapp.domain.CouponPackage;
import com.kt.yapp.domain.CouponPkg;
import com.kt.yapp.domain.CouponSearch;
import com.kt.yapp.domain.CustAgreeInfoRegWthdOutDTO;
import com.kt.yapp.domain.CustAgreeInfoRegWthdResponse;
import com.kt.yapp.domain.CustAgreeInfoResponse;
import com.kt.yapp.domain.CustAgreeInfoResponseDetail;
import com.kt.yapp.domain.CustAgreeInfoRetvListDTO;
import com.kt.yapp.domain.CustAgreeInfoRetvListDtoDetail;
import com.kt.yapp.domain.OwnAuth;
import com.kt.yapp.domain.SendSmsLogInfo;
import com.kt.yapp.domain.SmsContents;
import com.kt.yapp.domain.TermsAgree;
import com.kt.yapp.domain.TokenInfo;
import com.kt.yapp.domain.UserInfo;
import com.kt.yapp.domain.VaSvcAppl;
import com.kt.yapp.domain.VasItems;
import com.kt.yapp.em.EnumCntrcTypeCd;
import com.kt.yapp.em.EnumRsltCd;
import com.kt.yapp.em.EnumYn;
import com.kt.yapp.exception.YappException;
import com.kt.yapp.exception.YappRuntimeException;
import com.kt.yapp.soap.SoapConnUtil;
import com.kt.yapp.soap.param.SoapParam003;
import com.kt.yapp.soap.param.SoapParam086;
import com.kt.yapp.soap.param.SoapParam087;
import com.kt.yapp.soap.param.SoapParam101;
import com.kt.yapp.soap.param.SoapParam103;
import com.kt.yapp.soap.param.SoapParam107;
import com.kt.yapp.soap.param.SoapParam110;
import com.kt.yapp.soap.param.SoapParam133;
import com.kt.yapp.soap.param.SoapParam139;
import com.kt.yapp.soap.param.SoapParam140;
import com.kt.yapp.soap.param.SoapParam184;
import com.kt.yapp.soap.param.SoapParam184a;
import com.kt.yapp.soap.param.SoapParam188;
import com.kt.yapp.soap.param.SoapParam200;
import com.kt.yapp.soap.param.SoapParam2102;
import com.kt.yapp.soap.param.SoapParam2118;
import com.kt.yapp.soap.param.SoapParam235;
import com.kt.yapp.soap.param.SoapParam494;
import com.kt.yapp.soap.param.SoapParam495;
import com.kt.yapp.soap.param.SoapParam750;
import com.kt.yapp.soap.response.SoapResponse003;
import com.kt.yapp.soap.response.SoapResponse086;
import com.kt.yapp.soap.response.SoapResponse087;
import com.kt.yapp.soap.response.SoapResponse101;
import com.kt.yapp.soap.response.SoapResponse103;
import com.kt.yapp.soap.response.SoapResponse107;
import com.kt.yapp.soap.response.SoapResponse110;
import com.kt.yapp.soap.response.SoapResponse133;
import com.kt.yapp.soap.response.SoapResponse139;
import com.kt.yapp.soap.response.SoapResponse140;
import com.kt.yapp.soap.response.SoapResponse184;
import com.kt.yapp.soap.response.SoapResponse184a;
import com.kt.yapp.soap.response.SoapResponse188;
import com.kt.yapp.soap.response.SoapResponse200;
import com.kt.yapp.soap.response.SoapResponse2102;
import com.kt.yapp.soap.response.SoapResponse2118;
import com.kt.yapp.soap.response.SoapResponse235;
import com.kt.yapp.soap.response.SoapResponse494;
import com.kt.yapp.soap.response.SoapResponse495;
import com.kt.yapp.soap.response.SoapResponse750;
import com.kt.yapp.soap.response.YappSoapResponse;
import com.kt.yapp.util.AppEncryptUtils;
import com.kt.yapp.util.YappUtil;
import org.springframework.web.client.RestTemplate;

@Service
public class ShubService 
{
	@Autowired
	private SoapConnUtil soapConnUtil;
	@Autowired
	private CmsService cmsService;
	@Autowired
	private CommonService cmnService;
	@Autowired
	private UserService userService;
	@Value("${shub.authentication.rest.url}")
	public String restShubUrlAuth;
	@Value("${shub.couponpkgsearch.rest.url}")
	public String cpnPkgSchUrl;
	@Value("${shub.couponpkgsearchlist.rest.url}")
	public String cpnPkgSchListUrl;
	@Value("${shub.couponendsearch.rest.url}")
	public String cpnEndSchUrl;
	@Value("${shub.couponcustomerdetail.rest.url}")
	public String cpnCstDtlUrl;
	@Value("${shub.conn.id}")
	public String connId;
	@Value("${shub.conn.pwd}")
	public String connPwd;
	@Value("${shub.custagreeinforetv.rest.url}")
	public String custAgreeInfoUrl;
	@Value("${shub.custagreeinforegwthd.rest.url}")
	public String custAgreeInfoChgUrl;
	@Autowired
	private AppEncryptUtils appEncryptUtils;
	
	
	private static final String SND_MOBILE_NO = "01072019895";
	private static final Logger logger = LoggerFactory.getLogger(ShubService.class);
	private static final String YAPP   = "YAPP"; 
	
	/**
	 * 전화번호로 사용자 아이디 정보 얻기
	 */
	public SoapResponse003 callFn003(String mobileNo) throws Exception
	{
		SoapParam003 paramAuth = new SoapParam003(soapConnUtil);
		paramAuth.setMobileNo(mobileNo);
		
		SoapResponse003 resp = paramAuth.execute();
		
		return resp;
	}
	
	/**
	 * OIF_086 본인인증 요청
	 */
	public SoapResponse086 callFn086(OwnAuth paramObj) throws Exception
	{
		SoapParam086 param = new SoapParam086(soapConnUtil);
		param.setPartiName(paramObj.getUserNm());
		param.setForeignerCd(paramObj.getForeignerCd());
		param.setBirthDate(paramObj.getBirthDate());
		param.setGenderCd(paramObj.getGenderCd());
		param.setPhoneNumber(paramObj.getMobileNo());
		
		SendSmsLogInfo sendSmsLogInfo = new SendSmsLogInfo();
		try {
			sendSmsLogInfo.setServiceType(YAPP);
			sendSmsLogInfo.setAccessUrl(paramObj.getAccessUrl());
			sendSmsLogInfo.setCallbackCtn(paramObj.getCallbackCtn());
			sendSmsLogInfo.setRcvCtn(paramObj.getMobileNo());
			sendSmsLogInfo.setCallCtn(paramObj.getPhoneNumber());
			sendSmsLogInfo.setMsgContent("인증번호 ["+paramObj.getAuthCd()+"], 인증확인번호 ["+paramObj.getRandomNo()+"]");
			
			cmnService.insertSmsLogInfo(sendSmsLogInfo);
		}
		catch(RuntimeException ex) {
			logger.info("Exception : "+ex.toString());
		}
		
		SoapResponse086 resp = param.execute();

		try {
			if(logger.isInfoEnabled()) {
				logger.info("TransactionId : "+resp.getTransId());
				logger.info("ResultCd      : "+resp.getRtnCd());
				logger.info("ResultMsg     : "+resp.getRtnDesc());
			}
			sendSmsLogInfo.setTransactionId(resp.getTransId());
			sendSmsLogInfo.setResultCd(resp.getRtnCd());
			sendSmsLogInfo.setResultMsg(resp.getRtnDesc());
	
			cmnService.updateSmsLogInfo(sendSmsLogInfo);
		}
		catch(RuntimeException ex) {
			logger.info("Exception : "+ex.toString());
		}
		
		if ( YappUtil.isNotEq(resp.getRtnCd(), EnumYn.C_1.getValue()) )
			throw new YappException("SHUB_MSG", resp.getRtnCd(), cmnService.getMsg(resp.getRtnCd()), "[OIF_086] " + resp.getRtnDesc(), resp.getTransId());
		
		return resp;
	}
	
	/**
	 * OIF_087 본인인증 확인
	 */
	public SoapResponse087 callFn087(OwnAuth paramObj) throws Exception
	{
		SoapParam087 param = new SoapParam087(soapConnUtil);
		param.setPartiName(paramObj.getUserNm());
		param.setForeignerCd(paramObj.getForeignerCd());
		param.setBirthDate(paramObj.getBirthDate());
		param.setGenderCd(paramObj.getGenderCd());
		param.setPhoneNumber(paramObj.getMobileNo());
		param.setAuthCode(paramObj.getAuthCd());
		param.setRandomNo(paramObj.getRandomNo());
		
		SoapResponse087 resp = param.execute();
		if ( YappUtil.isNotEq(resp.getRtnCd(), EnumYn.C_1.getValue()) )
			throw new YappException("SHUB_MSG", resp.getRtnCd(), cmnService.getMsg(resp.getRtnCd()), "[OIF_087] " +resp.getRtnDesc(), resp.getTransId());
		
		return resp;
	}

	/**
	 * 크레덴셜ID로 사용자 프로필 조회
	 */
	public SoapResponse101 callFn101(String credentialId) throws Exception
	{
		SoapParam101 paramAuth = new SoapParam101(soapConnUtil);
		paramAuth.setCredentialId(credentialId);
		
		SoapResponse101 resp = paramAuth.execute();
		if ( YappUtil.isNotEq(resp.getRtnCd(), EnumYn.C_1.getValue()) )
			throw new YappException("SHUB_MSG", resp.getRtnCd(), cmnService.getMsg(resp.getRtnCd()), "[OIF_101] " +resp.getRtnDesc(), resp.getTransId());
		
		return resp;
	}
	
	/**
	 * 부가서비스 상품 목록 조회
	 */
	public SoapResponse133 callFn133(String mobileNo) throws Exception
	{
		SoapParam133 paramAuth = new SoapParam133(soapConnUtil);
		paramAuth.setMobileNo(mobileNo);
		
		SoapResponse133 resp = paramAuth.execute();
		if ( YappUtil.isNotEq(resp.getRtnCd(), EnumYn.C_1.getValue()) )
			throw new YappException("SHUB_MSG", resp.getRtnCd(), cmnService.getMsg(resp.getRtnCd()), "[OIF_133] " +resp.getRtnDesc(), resp.getTransId());
		
		return resp;
	}
	
	/**
	 * 휴대폰 번호로 사용자 프로필 조회
	 */
	public SoapResponse101 callFn101WithMobileNo(String mobileNo) throws Exception
	{
		String credentialId = callFn110(mobileNo).getCredentialId();
		
		SoapParam101 paramAuth = new SoapParam101(soapConnUtil);
		paramAuth.setCredentialId(credentialId);
		
		SoapResponse101 resp = paramAuth.execute();
		if ( YappUtil.isNotEq(resp.getRtnCd(), EnumYn.C_1.getValue()) )
			throw new YappException("SHUB_MSG", resp.getRtnCd(), cmnService.getMsg(resp.getRtnCd()), "[OIF_101] " +resp.getRtnDesc(), resp.getTransId());
		
		return resp;
	}
	
	/**
	 * ID/PWD로 인증 처리
	 */
	public SoapResponse107 callFn107(String userId, String pwd) throws Exception
	{
		SoapParam107 paramAuth = new SoapParam107(soapConnUtil);
		paramAuth.setUserName(userId);
		paramAuth.setPassword(pwd);
		
		SoapResponse107 resp = paramAuth.execute();
		
		return resp;
	}
	
	/**
	 * 휴대폰 번호로 크레덴셜ID 조회
	 */
	public SoapResponse110 callFn110(String mobileNo) throws Exception
	{
		SoapParam110 paramAuth = new SoapParam110(soapConnUtil);
		paramAuth.setMobileNo(mobileNo);
		
		SoapResponse110 resp = paramAuth.execute();
		if ( YappUtil.isNotEq(resp.getRtnCd(), EnumYn.C_1.getValue()) )
			throw new YappException("SHUB_MSG", resp.getRtnCd(), cmnService.getMsg(resp.getRtnCd()), "[OIF_110] " +resp.getRtnDesc(), resp.getTransId());
		
		return resp;
	}
	
	/**
	 * 계약번호로 휴대폰 번호 조회
	 */
	public SoapResponse103 callFn103(String cntrNo) throws Exception
	{
		SoapParam103 paramAuth = new SoapParam103(soapConnUtil);
		paramAuth.setCntrNo(cntrNo);
		
		SoapResponse103 resp = paramAuth.execute();
		if ( YappUtil.isNotEq(resp.getRtnCd(), EnumYn.C_1.getValue()) )
			throw new YappException("SHUB_MSG", resp.getRtnCd(), cmnService.getMsg(resp.getRtnCd()), "[OIF_103] " +resp.getRtnDesc(), resp.getTransId());
		
		return resp;
	}
	
	/**
	 * 계약 정보를 조회한다.
	 */
	public SoapResponse139 callFn139(String mobileNo) throws Exception
	{
		return callFn139(mobileNo, true);
	}
	public SoapResponse139 callFn139(String mobileNo, boolean isSrchDtlInfo) throws Exception
	{
		return callFn139(mobileNo, isSrchDtlInfo, isSrchDtlInfo);
	}
	public SoapResponse139 callFn139(String mobileNo, boolean isSrchUserInfo, boolean isSrchCallingPlan) throws Exception
	{
		SoapParam139 soapParam = new SoapParam139(soapConnUtil);
		soapParam.setMobileNo(mobileNo);
		 
		SoapResponse139 resp = soapParam.execute();
		// 계약정보 조회
		ContractInfo cntrInfo = resp.getCntrInfo();

		if ( YappUtil.isNotEq(resp.getRtnCd(), EnumYn.C_1.getValue()) || cntrInfo == null ){
			throw new YappException("SHUB_MSG", "410", cmnService.getMsg(resp.getRtnCd()));
		}

		// 사용자 정보 조회
		if ( isSrchUserInfo )
		{
			UserInfo userInfo = userService.getYappUserInfo(cntrInfo.getCntrNo());
			
			if(userInfo!=null && userInfo.getUserId()!=null){
				
				String userId = userInfo.getUserId().trim();
				
				if(YappUtil.isNotEmpty(userId)){
					TermsAgree termsAgree = userService.getTermsAgreeKtInfoWithUserId(userId);
					if(termsAgree!=null){
						userInfo.getTermsAgree().setShopTermsAgreeYn(termsAgree.getShopTermsAgreeYn());
						userInfo.getTermsAgree().setShopTermsVrsn(termsAgree.getShopTermsVrsn());
					}
				}
			}
			
			cntrInfo.setUserInfo(userInfo);
			
			//220613 모바일번호 마스킹 필드 추가
			cntrInfo.setMaskingMobileNo(YappUtil.blindMidEndMobileNo(cntrInfo.getMobileNo()));
		}
		
		// 요금제 정보 세팅
		if ( isSrchCallingPlan )
		{
			cntrInfo.setCallingPlan(cmsService.getCallingPlanInfo(cntrInfo.getPpCd()));
		}
		
		return resp;
	}
	
	public SoapResponse139 callFn139_1(String mobileNo) throws Exception
	{
		return callFn139_1(mobileNo, true);
	}
	public SoapResponse139 callFn139_1(String mobileNo, boolean isSrchDtlInfo) throws Exception
	{
		return callFn139_1(mobileNo, isSrchDtlInfo, isSrchDtlInfo);
	}
	public SoapResponse139 callFn139_1(String mobileNo, boolean isSrchUserInfo, boolean isSrchCallingPlan) throws Exception
	{
		SoapParam139 soapParam = new SoapParam139(soapConnUtil);
		soapParam.setMobileNo(mobileNo);
		 
		SoapResponse139 resp = soapParam.execute();
		// 계약정보 조회
		ContractInfo cntrInfo = resp.getCntrInfo();

		if ( YappUtil.isNotEq(resp.getRtnCd(), EnumYn.C_1.getValue()) || cntrInfo == null ){
			throw new YappException("SHUB_MSG", EnumRsltCd.C999.getRsltCd(), cmnService.getMsg("1391", YappUtil.makeParamMap("rtnCd", resp.getRtnCd())));
		}

		// 사용자 정보 조회
		if ( isSrchUserInfo )
		{
			UserInfo userInfo = userService.getYappUserInfo(cntrInfo.getCntrNo());
			cntrInfo.setUserInfo(userInfo);
		}
		
		// 요금제 정보 세팅
		if ( isSrchCallingPlan )
		{
			cntrInfo.setCallingPlan(cmsService.getCallingPlanInfo(cntrInfo.getPpCd()));
		}
		
		return resp;
	}
	
	public SoapResponse139 callFn139_2(String mobileNo) throws Exception
	{
		return callFn139_2(mobileNo, true);
	}
	public SoapResponse139 callFn139_2(String mobileNo, boolean isSrchDtlInfo) throws Exception
	{
		return callFn139_2(mobileNo, isSrchDtlInfo, isSrchDtlInfo);
	}
	public SoapResponse139 callFn139_2(String mobileNo, boolean isSrchUserInfo, boolean isSrchCallingPlan) throws Exception
	{
		SoapParam139 soapParam = new SoapParam139(soapConnUtil);
		soapParam.setMobileNo(mobileNo);
		 
		SoapResponse139 resp = soapParam.execute();
		// 계약정보 조회
		ContractInfo cntrInfo = resp.getCntrInfo();

		if ( YappUtil.isNotEq(resp.getRtnCd(), EnumYn.C_1.getValue()) || cntrInfo == null ){
			throw new YappRuntimeException();
		}

		// 사용자 정보 조회
		if ( isSrchUserInfo )
		{
			UserInfo userInfo = userService.getYappUserInfo(cntrInfo.getCntrNo());
			cntrInfo.setUserInfo(userInfo);
		}
		
		// 요금제 정보 세팅
		if ( isSrchCallingPlan )
		{
			cntrInfo.setCallingPlan(cmsService.getCallingPlanInfo(cntrInfo.getPpCd()));
		}
		
		return resp;
	}
	
	/**
	 * 계약 목록을 조회한다.
	 */
	public SoapResponse140 callFn140(String userId, String credentialId) throws Exception
	{
		SoapParam140 soapParam = new SoapParam140(soapConnUtil);
		soapParam.setCrendentialId(credentialId);
		soapParam.setUserName(userId);
		
		SoapResponse140 resp = soapParam.execute();
		
		// 계약 정보가 없을시, 빈값 반환 (비회선 KT사용자)
		if ( YappUtil.isEq(resp.getRtnCd(), EnumYn.C_2.getValue()) )
			return resp;
		
		if ( YappUtil.isNotEq(resp.getRtnCd(), EnumYn.C_1.getValue()) )
			throw new YappException("SHUB_MSG", resp.getRtnCd(), cmnService.getMsg(resp.getRtnCd()), "[OIF_140] " +resp.getRtnDesc(), resp.getTransId());
		
		List<ContractInfo> cntrInfoList = resp.getCntrInfoList();
		
		// 계약 유형코드가 24 인 상품만 조회
		for ( int i = cntrInfoList.size() - 1; i >= 0; i-- )
		{
			ContractInfo cntrInfo = cntrInfoList.get(i);
			if ( cntrInfo == null || YappUtil.isNotEq(cntrInfo.getCntrcTypeCd(), EnumCntrcTypeCd.C24.getTypeCd()) )  {
				cntrInfoList.remove(i);
				continue;
			}
			
			// 요금제 정보 세팅
			CallingPlan paramPlan = new CallingPlan();
			paramPlan.setPpCd(cntrInfo.getPpCd());
			
			// 요금제 정보가 없으면 번호 무시
			CallingPlan callingPlan = cmsService.getCallingPlanInfo(cntrInfo.getPpCd());
			if ( callingPlan == null || YappUtil.isEmpty(callingPlan.getPpCd()) ) {
				//cntrInfoList.remove(i);
				continue;
			}
			
			cntrInfo.setCallingPlan(callingPlan);
			
			cntrInfo.setMaskingMobileNo(YappUtil.blindMidEndMobileNo(cntrInfo.getMobileNo()));
		}
		
		return resp;
	}

	/**
	 * 계약번호로 휴대폰 번호 조회
	 */
	public SoapResponse184 callFn184(String cntrNo) throws Exception
	{
		SoapParam184 paramAuth = new SoapParam184(soapConnUtil);
		paramAuth.setCntrNo(cntrNo);

		SoapResponse184 resp = paramAuth.execute();
		if ( YappUtil.isNotEq(resp.getRtnCd(), EnumYn.C_1.getValue()) ){
			throw new YappException("SHUB_MSG", resp.getRtnCd(), cmnService.getMsg(resp.getRtnCd()), "[OIF_184] " +resp.getRtnDesc(), resp.getTransId());
		}

		return resp;
	}

	
	/**
	 * 계약번호로 부가서비스  조회
	 */
	public List<VasItems> callFn184a(String cntrNo) throws Exception
	{
		SoapParam184a paramAuth = new SoapParam184a(soapConnUtil);
		paramAuth.setCntrNo(cntrNo);

		SoapResponse184a resp = paramAuth.execute();
		if ( YappUtil.isNotEq(resp.getRtnCd(), EnumYn.C_1.getValue()) ){
			throw new YappException("SHUB_MSG", resp.getRtnCd(), cmnService.getMsg(resp.getRtnCd()), "[OIF_184] " +resp.getRtnDesc(), resp.getTransId());
		}

		return resp.getVasItemList();
	}
	
	/**
	 * 토큰 ID를 이용하여 인증 정보 획득
	 */
	public SoapResponse188 callFn188(String tokenId) throws Exception
	{
		SoapParam188 paramToken = new SoapParam188(soapConnUtil);
		paramToken.setTokenId(tokenId);

		SoapResponse188 resp = paramToken.execute();
		return resp;
	}

	/**
	 * 토큰 ID 복호화.
	 */
	public String callFn198(String tokenId, String userId, String uid) throws Exception
	{
		String ssoReqDate = YappUtil.getCurDate("yyyyMMddHHmmss");
		String applogininfo = uid + ";" + userId + ";" + tokenId + ";" + ssoReqDate;

		logger.info("applogininfo: " + applogininfo);
		applogininfo = appEncryptUtils.aesEnc(applogininfo);
		
		RestTemplate restCall = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		String key = connId +":"+ connPwd;
		
		byte[] encVal = key.getBytes("utf-8");
		String encryptedkey = "Basic "+ Base64Utils.encodeToString(encVal);
		logger.info("========================================================");
		logger.info("URL: "+restShubUrlAuth);
		logger.info("authorization: "+encryptedkey);
		logger.info("========================================================");
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", encryptedkey);

		MultiValueMap<String, String> param = new LinkedMultiValueMap<String, String>();
		param.add("applogininfo", applogininfo);
		param.add("ssoreqdate", ssoReqDate);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(param, headers);
		
		ResponseEntity<TokenInfo> repense198 = restCall.postForEntity(restShubUrlAuth, request, TokenInfo.class);
		logger.info("========================================================");
		logger.info("SHUB REST response 1 : " + repense198.getBody().getReturncode());
		logger.info("SHUB REST response 2 : " + repense198.getBody().getReturndesc());
		logger.info("SHUB REST response 3 : " + repense198.getBody().getSequenceno());
		logger.info("SHUB REST response 4 : " + repense198.getBody().getToken_id());
		logger.info("========================================================");
		if ( repense198 == null || YappUtil.isNotEq(repense198.getBody().getReturncode(), EnumYn.C_1.getValue()) )
		{
			if(repense198 != null) {
				logger.warn(cmnService.getMsg(YappUtil.nToStr(repense198.getBody().getReturncode())));
			}else{
				logger.warn(cmnService.getMsg(YappUtil.nToStr("")));
			}
			
			return "";
		}
		return repense198.getBody().getToken_id();
	}
	
	/**
	 * 토큰 ID 복호화.
	 */
	public String callFn198back(String tokenId, String userId, String uid) throws Exception
	{
		String applogininfo = "";

		String ssoReqDate = YappUtil.getCurDate("yyyyMMddHHmmss");
/*
	- key : uid
	- value : CEj0Wp4o2tl9WffCW+TmGI2SmWpzIM1fQIuvJgishK1yYreXC4zKq/lI/1GkLWMT9o1E3XafTHKHR7IbmO9XPw==
    - key : ollehID
    - value : yanaq22
	- key : token_id
    - value : A72D49D2A4B211E5ED0972FA59D830B174E29276C76D90898B0FC0BF08DB5439AE8A7A04C6629FB05579DD607C6E5410BDB80991CE1FC0ED8681856584F19E3942FFCE86B234CAE369C0A836DFF0E7D920C29E8EEDE6A632AC38657D75BB849E
 */
		applogininfo = uid + ";" + userId + ";" + tokenId + ";" + ssoReqDate;

		applogininfo = appEncryptUtils.aesEnc(applogininfo);
		
		String[][] paramArray = new String[][]{{"applogininfo", applogininfo}};
			
		String apiUrl = makeUrl(restShubUrlAuth, paramArray);
		logger.info("SHUB REST URL: " + apiUrl);
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> tokenObj = mapper.readValue(new URL(apiUrl), Map.class);
		logger.info("SHUB REST RETURN: " + tokenObj);
		if ( tokenObj == null || YappUtil.isNotEq(tokenObj.get("returncode"), EnumYn.C_1.getValue()) )
		{
			if(tokenObj != null) {
				logger.warn(cmnService.getMsg(YappUtil.nToStr(tokenObj.get("returncode"))));
			}else{
				logger.warn(cmnService.getMsg(YappUtil.nToStr("")));
			}
			
			
			return "";
		}
		return YappUtil.nToStr(tokenObj.get("token_id"));
	}

	/**
	 * GET 호출을 위한 URL 을 만든다.
	 */
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

	/**
	 * ID/PWD로 인증 처리
	 */
	public SoapResponse200 callFn200(String userId, String pwd) throws Exception
	{
		SoapParam200 paramAuth = new SoapParam200(soapConnUtil);
		paramAuth.setUserName(userId);
		paramAuth.setPassword(pwd);
		
		SoapResponse200 resp = paramAuth.execute();
		
		return resp;
	}
	
	/**
	 * 쿠폰을 조회한다.
	 */
	public SoapResponse494 callFn494(String cntrNo, String mobileNo, String pkyTpCd) throws Exception
	{
		SoapParam494 soapParam = new SoapParam494(soapConnUtil);
		soapParam.setCntrNo(cntrNo);
		soapParam.setMobileNo(mobileNo);
		
		SoapResponse494 resp = soapParam.execute();
		
		// Pakage Type 이 다르면 삭제한다.
		if ( resp != null && YappUtil.isNotEmpty(resp.getCouponList()) )
		{
			List<Coupon> cpnList = resp.getCouponList();
			for ( int i = cpnList.size() - 1; i >= 0; i-- )
			{
				// 쿠폰 유효기간이 지났거나 패키지명이 다른 경우 삭제. 쿠폰 사용한경우 제외처리(2018년 4월 24일 ).
				if ( YappUtil.toInt(cpnList.get(i).getCpnValidEdYmd()) < YappUtil.toInt(YappUtil.getCurDate())
						|| (YappUtil.isNotEmpty(pkyTpCd) && YappUtil.isNotEq(cpnList.get(i).getPkgTpCd(), pkyTpCd))
						|| cpnList.get(i).getCpnUseYn().equals("Y"))
					cpnList.remove(i);
			}
			
			// 쿠폰 타입을 세팅한다.
			for ( int i = 0; i < cpnList.size(); i++ )
			{
				//장기혜택쿠폰 LONGCPLTE: 데이터 LTE, LONGCP3G: 데이터 3G, LONGCPTV: 올레tv, LONGCP30M: 통화 30분, LONGCP5EGG: 기본알 5천알
				//반값팩 쿠폰 HALFCPGENIE: 지니팩, HALFCPOTM: OTM 데일리팩, HALFCPDATA: 데이터충전
				
				/** 2020.02.03 : 반값팩 먼저 체크, 뒤에 장기혜택 체크로 변경 */
				// 반값팩 쿠폰
				if ( cpnList.get(i).getCpnNm().indexOf("지니팩") > -1 )
					cpnList.get(i).setCpnTp("G0001");
				else if ( cpnList.get(i).getCpnNm().indexOf("OTM 데일리팩") > -1  || cpnList.get(i).getCpnNm().indexOf("반값팩_시즌") > -1)
					cpnList.get(i).setCpnTp("G0002");
				else if ( cpnList.get(i).getCpnNm().indexOf("데이터충전") > -1 )
					cpnList.get(i).setCpnTp("G0003");
				// 장기혜택 쿠폰
				else if ( cpnList.get(i).getCpnNm().indexOf("LTE") > -1 && cpnList.get(i).getCpnNm().indexOf("데이터") > -1)
					cpnList.get(i).setCpnTp("G0001");
				else if ( cpnList.get(i).getCpnNm().indexOf("3G") > -1 && cpnList.get(i).getCpnNm().indexOf("데이터") > -1)
					cpnList.get(i).setCpnTp("G0002");
				else if ( cpnList.get(i).getCpnNm().indexOf("올레tv") > -1 || cpnList.get(i).getCpnNm().indexOf("올레TV") > -1 || cpnList.get(i).getCpnNm().indexOf("시즌") > -1)
					cpnList.get(i).setCpnTp("G0003");
				else if ( cpnList.get(i).getCpnNm().indexOf("통화") > -1 )
					cpnList.get(i).setCpnTp("G0004");
				else if ( cpnList.get(i).getCpnNm().indexOf("기본알") > -1 )
					cpnList.get(i).setCpnTp("G0005");
				else if ( cpnList.get(i).getCpnNm().indexOf("5G") > -1 )
					cpnList.get(i).setCpnTp("G0006");
				else if ( cpnList.get(i).getCpnNm().indexOf("서재") > -1 ) //220727
					cpnList.get(i).setCpnTp("G0003");				
				else if ( cpnList.get(i).getCpnNm().indexOf("블라") > -1 ) //221017
					cpnList.get(i).setCpnTp("G0003");
			}
		}
		
		return resp;
	}

	//쿠폰정보를 가지고와 패키지화 시킨다.
	public List<CouponPackage> couponList(String cntrNo, String mobileNo, String pkyTpCd) throws Exception
	{
		List<Coupon> couponList = new ArrayList<>();
		List<Coupon> couponListRcv = new ArrayList<>();
		List<String> cpnTpList = new ArrayList<>();
		List<CouponPackage> couponPackageList = new ArrayList<>();
		String cpnPkgValidStYmd = "";
		String cpnPkgValidEdYmd = "";
		SoapResponse494 resp494 = callFn494(cntrNo, mobileNo, pkyTpCd);
		if ( YappUtil.is1(resp494.getRtnCd()) == false || resp494.getCouponList().size() == 0){
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_NO_COUPON"));
		}else{
			couponList = resp494.getCouponList();
			String cpnTpTmp = "";
			for ( int i = 0; i < couponList.size(); i++ ){
				cpnTpTmp = couponList.get(i).getCpnTp();
				if(cpnTpList.size() == 0){
					cpnTpList.add(cpnTpTmp);
				}else{
					String addYn = "Y";
					for(int y = 0; y < cpnTpList.size(); y++){
						if(cpnTpTmp.equals(cpnTpList.get(y))){
							addYn = "N";
						}
					}
					if(addYn.equals("Y")){
						cpnTpList.add(cpnTpTmp);
					}
				}
			}

			int packageCnt = 0;
			if(couponList.size() == 0 || cpnTpList.size() == 0){
				packageCnt = couponList.size();
			}else{
				packageCnt = (int)Math.ceil(((double)couponList.size()/(double)cpnTpList.size()));
			}

			for(int z = 0; z < packageCnt; z++){
				CouponPackage cpnPkg = new CouponPackage();
				couponListRcv = new ArrayList<>();
				String cpnTpRcvTmp = "";
				int pkgCnt = 0;
				cpnPkgValidStYmd = "";
				cpnPkgValidEdYmd = "";
				for(int w = 0; w < cpnTpList.size(); w++){
					cpnTpRcvTmp = cpnTpList.get(w);
					for ( int q = 0; q < couponList.size(); q++ ){
						if(cpnTpRcvTmp.equals(couponList.get(q).getCpnTp())){
							couponListRcv.add(couponList.get(q));
							cpnPkgValidStYmd = couponList.get(q).getCpnValidStYmd();
							cpnPkgValidEdYmd = couponList.get(q).getCpnValidEdYmd();
							pkgCnt++;
							couponList.remove(q);
							break;
						}
					}
				}

				cpnPkg.setCpnPackageNm("장기혜택쿠폰");
				cpnPkg.setCpnPkgValidStYmd(cpnPkgValidStYmd);
				cpnPkg.setCpnPkgValidEdYmd(cpnPkgValidEdYmd);
				if(cpnTpList.size() == pkgCnt){
					cpnPkg.setCouponList(couponListRcv);
					cpnPkg.setCpnPkgUseYn("N");
				}else{
					cpnPkg.setCpnPkgUseYn("Y");
				}
				couponPackageList.add(cpnPkg);
			}
		}
		return couponPackageList;
	}
	/**
	 * 쿠폰을 사용한다.
	 */
	public SoapResponse495 callFn495(Coupon cpnUse) throws Exception
	{
		SoapParam495 soapParam = new SoapParam495(soapConnUtil);
		soapParam.setUserId(cpnUse.getUserId());
		soapParam.setCntrNo(cpnUse.getCntrNo());
		soapParam.setMobileNo(cpnUse.getMobileNo());
		soapParam.setCpnNo(cpnUse.getCpnNo());
		
		SoapResponse495 resp = soapParam.execute();
		if ( YappUtil.isNotEq(resp.getRtnCd(), EnumYn.C_1.getValue()) )
			throw new YappException("SHUB_MSG", resp.getRtnCd(), YappUtil.ifNull(cmnService.getMsg(resp.getRtnCd()), resp.getRtnDesc()), "[OIF_495] " +resp.getRtnDesc(), resp.getTransId());
		
		return resp;
	}
	
	/**
	 * 부가서비스를 설정한다.
	 */
	public SoapResponse750 callFn750(VaSvcAppl vaSvcAppl) throws Exception
	{
		SoapParam750 soapParam = new SoapParam750(soapConnUtil);
		soapParam.setMobileNo(vaSvcAppl.getMobileNo());
		soapParam.setActionTp(vaSvcAppl.getActionTp());
		soapParam.setVasId(vaSvcAppl.getVasItemId());
		soapParam.setAttrNm(vaSvcAppl.getAttrNm());
		soapParam.setAttrVal(vaSvcAppl.getAttrVal());
		
		SoapResponse750 resp = soapParam.execute();
		if ( YappUtil.isNotEq(resp.getRtnCd(), EnumYn.C_1.getValue()) ){
			//throw new YappException(resp.getRtnCd(), YappUtil.ifNull(cmnService.getMsg(resp.getRtnCd()), resp.getRtnDesc()));
			throw new YappException("SHUB_MSG", resp.getRtnCd(), cmnService.getSearchMsg("SHUB_ERR_MSG", resp.getRtnDesc(), resp.getRtnCd(), ""), "[OIF_750] " +resp.getRtnDesc(), resp.getTransId());
		}
		
		return resp;
	}
	
	/**
	 * SMS를 발송한다.
	 */
	public SoapResponse2118 callFn2118(SmsContents paramObj) throws Exception
	{
		SoapParam2118 param = new SoapParam2118(soapConnUtil);
		param.setMsgContents(paramObj.getMsgContents());
		if ( YappUtil.isEmpty(paramObj.getSendMobileNo()) )
			param.setSendMobileNo(SND_MOBILE_NO);
		else
			param.setSendMobileNo(paramObj.getSendMobileNo());
			
		param.setRcvMobileNo(paramObj.getRcvMobileNo());
		
		/**
		 * 2020.03.26 배포 : SMS 전송시 이력 추가
		 */
		SendSmsLogInfo sendSmsLogInfo = new SendSmsLogInfo();
		try {
			sendSmsLogInfo.setServiceType(YAPP);
			sendSmsLogInfo.setAccessUrl(paramObj.getAccessUrl());
			sendSmsLogInfo.setCallbackCtn(paramObj.getCallbackCtn());
			sendSmsLogInfo.setRcvCtn(paramObj.getRcvMobileNo());
			sendSmsLogInfo.setCallCtn(paramObj.getSendMobileNo());
			sendSmsLogInfo.setMsgContent(paramObj.getMsgContents());
			
			cmnService.insertSmsLogInfo(sendSmsLogInfo);
		}
		catch(RuntimeException ex) {
			logger.info("Exception : "+ex.toString());
		}
		
		/*
		 *  soap 통신상 정상리턴 받지 못하는 경우에도 정상 메시지가 단말로 리턴되도록 처리 함.
		 * 2020-01-09 KBH
		 */		
		// SMS 발송
		SoapResponse2118 resp = param.execute();
		
		try {
			if(logger.isInfoEnabled()) {
				logger.info("TransactionId : "+resp.getTransId());
				logger.info("ResultCd      : "+resp.getRtnCd());
				logger.info("ResultMsg     : "+resp.getRtnDesc());
			}
			sendSmsLogInfo.setTransactionId(resp.getTransId());
			sendSmsLogInfo.setResultCd(resp.getRtnCd());
			sendSmsLogInfo.setResultMsg(resp.getRtnDesc());
			
			cmnService.updateSmsLogInfo(sendSmsLogInfo);
		}
		catch(RuntimeException ex) {
			logger.info("Exception : "+ex.toString());
		}
		
		if ( YappUtil.isNotEq(resp.getRtnCd(), EnumYn.C_1.getValue()) ){
			//throw new YappException("SHUB_MSG", resp.getRtnCd(), cmnService.getSearchMsg("SHUB_ERR_MSG", resp.getErrDesc(), "300055", ""), "[OIF_2118] " +resp.getRtnDesc(), resp.getTransId());
			throw new YappException("SHUB_MSG", "200", "요청하신 번호로 인증번호를 발송하였습니다. 인증번호를 수신하지 못하셨다면 입력하신 정보가 정확한지 확인해 주십시오.", "[OIF_2118] " +resp.getRtnDesc(), resp.getTransId());
		}
		return resp;
	}
	
	/**
	 * MMS를 발송한다.
	 */
	public SoapResponse2102 callFn2102(SmsContents paramObj) throws Exception
	{
		SoapParam2102 param = new SoapParam2102(soapConnUtil);
		param.setSubject(paramObj.getTitle());
		param.setMsgContents(paramObj.getMsgContents());
		if ( YappUtil.isEmpty(paramObj.getSendMobileNo()) )
			param.setSendMobileNo(SND_MOBILE_NO);
		else
			param.setSendMobileNo(paramObj.getSendMobileNo());

		param.setRcvMobileNo(paramObj.getRcvMobileNo());
		
		/**
		 * 2020.03.26 배포 : SMS 전송시 이력 추가
		 */
		SendSmsLogInfo sendSmsLogInfo = new SendSmsLogInfo();
		try {
			sendSmsLogInfo.setServiceType(YAPP);
			sendSmsLogInfo.setAccessUrl(paramObj.getAccessUrl());
			sendSmsLogInfo.setCallbackCtn(paramObj.getCallbackCtn());
			sendSmsLogInfo.setRcvCtn(paramObj.getRcvMobileNo());
			sendSmsLogInfo.setCallCtn(paramObj.getSendMobileNo());
			sendSmsLogInfo.setMsgContent(paramObj.getMsgContents());
			
			cmnService.insertSmsLogInfo(sendSmsLogInfo);
		}
		catch(RuntimeException ex) {
			logger.info("Exception : "+ex.toString());
		}
		
		// SMS 발송
		SoapResponse2102 resp = param.execute();
		
		try {
			if(logger.isInfoEnabled()) {
				logger.info("TransactionId : "+resp.getTransId());
				logger.info("ResultCd      : "+resp.getRtnCd());
				logger.info("ResultMsg     : "+resp.getRtnDesc());
			}
			sendSmsLogInfo.setTransactionId(resp.getTransId());
			sendSmsLogInfo.setResultCd(resp.getRtnCd());
			sendSmsLogInfo.setResultMsg(resp.getRtnDesc());
	
			cmnService.updateSmsLogInfo(sendSmsLogInfo);
		}
		catch(RuntimeException ex) {
			logger.info("Exception : "+ex.toString());
		}
		
		if ( YappUtil.isNotEq(resp.getRtnCd(), EnumYn.C_1.getValue()) )
			throw new YappException("SHUB_MSG", resp.getRtnCd(), cmnService.getMsg(resp.getRtnCd()), "[OIF_2102] " +resp.getRtnDesc(), resp.getTransId());
		
		return resp;
	}

	
	/**
	 * OIF_809 패키지 쿠폰 목록 API (고객이 배포받은 쿠폰 패키지 목록조회) [CouponPkgSearch]
	 */
	public CouponSearch callFn809(String cntrNo, String mobileNo) throws RuntimeException
	{
		RestTemplate restCall = new RestTemplate();
		String curMonth = YappUtil.getCurDate("yyyyMM");
		HttpHeaders headers = new HttpHeaders();
		String key = connId +":"+ connPwd;
		
		byte[] encVal = null;
		try {
			encVal = key.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			logger.error("UnsupportedEncodingException : " +e);
		}
		String encryptedkey = "Basic "+ Base64Utils.encodeToString(encVal);
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", encryptedkey);
		logger.info("Authorization : " + encryptedkey);
		MultiValueMap<String, String> param = new LinkedMultiValueMap<String, String>();
		param.add("svcchannelcode", "33");
		param.add("reqid", mobileNo);
		param.add("contractid", cntrNo);
		param.add("pkgdatefrom", curMonth);
		param.add("pkgdateto", curMonth);
		param.add("pagenum", "1");
		param.add("pagesize", "20");
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(param, headers);
		logger.info("request : " + request);
		ResponseEntity<CouponSearch> result = restCall.postForEntity(cpnPkgSchUrl, request, CouponSearch.class);
		logger.info("result : " + result);
		logger.info("result.getBody() : " + result.getBody());
		logger.info("result.getBody().getReturncode() : " + result.getBody().getReturncode());
		if(result == null){
			throw new YappRuntimeException("SHUB_MSG", "", cmnService.getMsg("CPN_NO_PKGLIST"), "[OIF_809] ", "");
		}else if(YappUtil.isNotEq(result.getBody().getReturncode(), EnumYn.C_1.getValue())){
			throw new YappRuntimeException("SHUB_MSG", result.getBody().getErrorcode(), cmnService.getMsg("CPN_NO_PKGLIST"), "[OIF_809] " +result.getBody().getErrordescription(), result.getBody().getTransactionid());
		}

		if(result != null) {
			return result.getBody();
		}else{
			return null;
		}
		
	}

	/**
	 * OIF_810 패키지 쿠폰 상품목록 API (고객의 쿠폰의 보관함에 있는 (미사용상태) 패키지쿠폰 목록조회) [CouponPkgSearchList]
	 */
	public CouponSearch callFn810(String cntrNo, String mobileNo, String pkgid) throws RuntimeException
	{
		RestTemplate restCall = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		String key = connId +":"+ connPwd;
		
		byte[] encVal = null;
		try {
			encVal = key.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			logger.error("UnsupportedEncodingException : " +e);
		}
		String encryptedkey = "Basic "+ Base64Utils.encodeToString(encVal);
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", encryptedkey);

		MultiValueMap<String, String> param = new LinkedMultiValueMap<String, String>();
		param.add("svcchannelcode", "33");
		param.add("reqid", mobileNo);
		param.add("contractid", cntrNo);
		param.add("pkgid", pkgid);
		param.add("pagenum", "1");
		param.add("pagesize", "20");
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(param, headers);
		logger.info("request : " + request);
		ResponseEntity<CouponSearch> result = restCall.postForEntity(cpnPkgSchListUrl, request, CouponSearch.class);
		logger.info("result : " + result);
		logger.info("result.getBody() : " + result.getBody());
		logger.info("result.getBody().getReturncode() : " + result.getBody().getReturncode());
			
		if ( result == null)
		{
			throw new YappRuntimeException("SHUB_MSG", "", cmnService.getMsg("CPN_NO_PRODLIST"), "[OIF_810] ", "");
			
		}else if(YappUtil.isNotEq(result.getBody().getReturncode(), EnumYn.C_1.getValue())){
			throw new YappRuntimeException("SHUB_MSG", result.getBody().getErrorcode(), cmnService.getMsg("CPN_NO_PRODLIST"), "[OIF_810] " +result.getBody().getErrordescription(), result.getBody().getTransactionid());
		}else{
			List<CouponPkg> cpnList = result.getBody().getPkgprod();

			// 쿠폰 타입을 세팅한다.
			for ( int i = 0; i < cpnList.size(); i++ ){
				// 장기혜택 쿠폰
				if ( cpnList.get(i).getCpnname().indexOf("LTE") > -1 && cpnList.get(i).getCpnname().indexOf("데이터") > -1){
					cpnList.get(i).setCpnTp("G0001");
					cpnList.get(i).setReservation01("데이터2GB");
					cpnList.get(i).setReservation02("LTE&3G");
				}else if ( cpnList.get(i).getCpnname().indexOf("3G") > -1 && cpnList.get(i).getCpnname().indexOf("데이터") > -1){
					cpnList.get(i).setCpnTp("G0002");
					cpnList.get(i).setReservation01("데이터2GB");
					cpnList.get(i).setReservation02("LTE&3G");
				}else if ( cpnList.get(i).getCpnname().indexOf("올레tv") > -1 || cpnList.get(i).getCpnname().indexOf("올레TV") > -1  || cpnList.get(i).getCpnname().indexOf("시즌") > -1 ){
					cpnList.get(i).setCpnTp("G0003");
					cpnList.get(i).setReservation01("시즌 플레인");
					cpnList.get(i).setReservation02("월10GB_1개월");
				}else if ( cpnList.get(i).getCpnname().indexOf("통화") > -1 ){
					cpnList.get(i).setCpnTp("G0004");
					cpnList.get(i).setReservation01("100분");
					cpnList.get(i).setReservation02("무료통화");
				}else if ( cpnList.get(i).getCpnname().indexOf("기본알") > -1 ){
					cpnList.get(i).setCpnTp("G0005");
					cpnList.get(i).setReservation01("기본알 1만알");
					cpnList.get(i).setReservation02("청소년");
				}else if ( cpnList.get(i).getCpnname().indexOf("5G") > -1 ){
					cpnList.get(i).setCpnTp("G0006");
					cpnList.get(i).setReservation01("데이터2GB");
					cpnList.get(i).setReservation02("5G");
				}else if ( cpnList.get(i).getCpnname().indexOf("서재") > -1 ){ //220727
					cpnList.get(i).setCpnTp("G0003");
					cpnList.get(i).setReservation01("밀리의 서재");
					cpnList.get(i).setReservation02("1개월 이용권");
				}else if ( cpnList.get(i).getCpnname().indexOf("블라") > -1 ){ //221017
					cpnList.get(i).setCpnTp("G0003");
					cpnList.get(i).setReservation01("블라이스 셀렉트");
					cpnList.get(i).setReservation02("1개월 이용권");
				}
			}
		}
		return result.getBody();
	}

	/**
	 * OIF_812 사용완료 쿠폰목록 API (고객이 사용한 쿠폰의 목록조회) [CouponEndSearch]
	 */
	public CouponEnd callFn812(String cntrNo, String mobileNo) throws RuntimeException
	{
		RestTemplate restCall = new RestTemplate();
		
		//Calendar curCal = Calendar.getInstance();
		//curCal.add(Calendar.MONTH, -2);
		String srchYm = YappUtil.getCurDate("yyyyMM");
		Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(srchYm.substring(0,4)), Integer.parseInt(srchYm.substring(4,6)) -1, 1);
		String curMd = srchYm + String.valueOf(cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		
		//String curMd = YappUtil.getCurDate("yyyyMMdd");
		//String srchYm = YappUtil.getCurDate("yyyyMM");
	
		HttpHeaders headers = new HttpHeaders();
		String key = connId +":"+ connPwd;
		
		byte[] encVal = null;
		try {
			encVal = key.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			logger.error("UnsupportedEncodingException : " +e);
		}
		String encryptedkey = "Basic "+ Base64Utils.encodeToString(encVal);
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", encryptedkey);

		MultiValueMap<String, String> param = new LinkedMultiValueMap<String, String>();
		param.add("svcchannelcode", "33");
		param.add("reqid", mobileNo);
		param.add("contractid", cntrNo);
		param.add("usedatefrom", srchYm+"01");
		param.add("usedateto", curMd);
		param.add("pagenum", "1");
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(param, headers);
		logger.info("request : " + request);
		ResponseEntity<CouponEnd> result = restCall.postForEntity(cpnEndSchUrl, request, CouponEnd.class);
		logger.info("result : " + result);
		logger.info("result.getBody() : " + result.getBody());
		logger.info("result.getBody().getReturncode() : " + result.getBody().getReturncode());
		
		if(result == null){
			throw new YappRuntimeException("SHUB_MSG", "", cmnService.getMsg("CPN_NO_USE"), "[OIF_812] ", "");
		}else if(YappUtil.isNotEq(result.getBody().getReturncode(), EnumYn.C_1.getValue())){
			throw new YappRuntimeException("SHUB_MSG", result.getBody().getErrorcode(), cmnService.getMsg("CPN_NO_USE"), "[OIF_812] " +result.getBody().getErrordescription(), result.getBody().getTransactionid());
		}else{
			List<CouponEndInfo> cpnList = result.getBody().getEnd();

			// 쿠폰 타입을 세팅한다.
			for ( int i = 0; i < cpnList.size(); i++ ){
				// 장기혜택 쿠폰
				if ( cpnList.get(i).getCpnname().indexOf("LTE") > -1 && cpnList.get(i).getCpnname().indexOf("데이터") > -1){
					cpnList.get(i).setCpnTp("G0001");
					cpnList.get(i).setReservation01("데이터2GB");
					cpnList.get(i).setReservation02("LTE&3G");
				}else if ( cpnList.get(i).getCpnname().indexOf("3G") > -1 && cpnList.get(i).getCpnname().indexOf("데이터") > -1){
					cpnList.get(i).setCpnTp("G0002");
					cpnList.get(i).setReservation01("데이터2GB");
					cpnList.get(i).setReservation02("LTE&3G");
				}else if ( cpnList.get(i).getCpnname().indexOf("올레tv") > -1 || cpnList.get(i).getCpnname().indexOf("올레TV") > -1  || cpnList.get(i).getCpnname().indexOf("시즌") > -1 ){
					cpnList.get(i).setCpnTp("G0003");
					cpnList.get(i).setReservation01("시즌 플레인");
					cpnList.get(i).setReservation02("월10GB_1개월");
				}else if ( cpnList.get(i).getCpnname().indexOf("통화") > -1 ){
					cpnList.get(i).setCpnTp("G0004");
					cpnList.get(i).setReservation01("100분");
					cpnList.get(i).setReservation02("무료통화");
				}else if ( cpnList.get(i).getCpnname().indexOf("기본알") > -1 ){
					cpnList.get(i).setCpnTp("G0005");
					cpnList.get(i).setReservation01("기본알 1만알");
					cpnList.get(i).setReservation02("청소년");
				}else if ( cpnList.get(i).getCpnname().indexOf("5G") > -1 ){
					cpnList.get(i).setCpnTp("G0006");
					cpnList.get(i).setReservation01("데이터2GB");
					cpnList.get(i).setReservation02("5G");
				}else if ( cpnList.get(i).getCpnname().indexOf("서재") > -1 ){ //220727
					cpnList.get(i).setCpnTp("G0003");
					cpnList.get(i).setReservation01("밀리의 서재");
					cpnList.get(i).setReservation02("1개월 이용권");
				}else if ( cpnList.get(i).getCpnname().indexOf("블라") > -1 ){ //221017
					cpnList.get(i).setCpnTp("G0003");
					cpnList.get(i).setReservation01("블라이스 셀렉트");
					cpnList.get(i).setReservation02("1개월 이용권");
				}
			}
		}
		if(result != null) {
			return result.getBody();
		}else{
			return null;
		}
		
	}

	/**
	 * OIF_813 쿠폰 상세조회 API (쿠폰에 대한 상세정보 조회) [CouponCustomerDetail]
	 */
	public CouponDtl callFn813(String serialnum) throws RuntimeException
	{
		RestTemplate restCall = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		String key = connId +":"+ connPwd;
		
		byte[] encVal = null;
		try {
			encVal = key.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			logger.error("UnsupportedEncodingException : " +e);
		}
		String encryptedkey = "Basic "+ Base64Utils.encodeToString(encVal);
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", encryptedkey);

		MultiValueMap<String, String> param = new LinkedMultiValueMap<String, String>();
		param.add("svcchannelcode", "33");
		param.add("serialnum", serialnum);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(param, headers);
		logger.info("request : " + request);
		ResponseEntity<CouponDtl> result = restCall.postForEntity(cpnCstDtlUrl, request, CouponDtl.class);
		logger.info("result : " + result);
		logger.info("result.getBody() : " + result.getBody());
		logger.info("result.getBody().getReturncode() : " + result.getBody().getReturncode());
		if ( result == null)
		{
			throw new YappRuntimeException("SHUB_MSG", "", cmnService.getMsg("CPN_NO_DTL"), "[OIF_813] ", "");
			
		}else if(YappUtil.isNotEq(result.getBody().getReturncode(), EnumYn.C_1.getValue())){
			throw new YappRuntimeException("SHUB_MSG", result.getBody().getErrorcode(), cmnService.getMsg("CPN_NO_DTL"), "[OIF_810] " +result.getBody().getErrordescription(), result.getBody().getTransactionid());
		}
		
		return result.getBody();
	}

	/**
	 * 단말정보 조회한다.
	 */
	public SoapResponse235 callFn235(String cntrNo, String mobileNo) throws Exception
	{
		SoapParam235 soapParam = new SoapParam235(soapConnUtil);
		soapParam.setUserName(mobileNo);
		if(cntrNo != null && !"".equals(cntrNo)){
			soapParam.setSvcContId(cntrNo);
		}else{
			soapParam.setSvcContId("");
		}

		SoapResponse235 resp = soapParam.execute();
		if ( YappUtil.isNotEq(resp.getRtnCd(), EnumYn.C_1.getValue()) ){
			throw new YappException("SHUB_MSG", resp.getRtnCd(), cmnService.getSearchMsg("SHUB_ERR_MSG", resp.getRtnDesc(), resp.getRtnCd(), ""), "[OIF_235] " +resp.getRtnDesc(), resp.getTransId());
		}

		return resp;
	}

	/**
	 * OIF_11267 고객동의정보의 동의여부 조회 API (선택약관 동의여부 조회)
	 */
	public List<CustAgreeInfoRetvListDtoDetail> callFn11267(String cntrNo) throws Exception
	{
		List<CustAgreeInfoRetvListDtoDetail> custAgreeList = new ArrayList<CustAgreeInfoRetvListDtoDetail>();
		RestTemplate restCall = new RestTemplate();
		
		String srchYm = YappUtil.getCurDate("yyyyMM");
		Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(srchYm.substring(0,4)), Integer.parseInt(srchYm.substring(4,6)) -1, 1);
		
		HttpHeaders headers = new HttpHeaders();
		String key = connId +":"+ connPwd;
		
		byte[] encVal = key.getBytes("utf-8");
		String encryptedkey = "Basic "+ Base64Utils.encodeToString(encVal);
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", encryptedkey);
		headers.add("chnlType", "yd");
		headers.add("userId", "91192332");
		headers.add("orgId", "SPT8050");
		headers.add("srcId", "IF-OShOP-2015-107");
		
		Map<String, String> bizHeaderMap = new HashMap<String, String>();
		bizHeaderMap.put("orderId", "orderId");
		bizHeaderMap.put("cbSvcName", "CustAgreeInfoRetvSO");
		bizHeaderMap.put("cbFnName", "service");
		
		Map<String, String> custAgreeInfoRetvInDTO = new HashMap<String, String>();
		custAgreeInfoRetvInDTO.put("svcContId", cntrNo);
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("bizHeader", bizHeaderMap);
		param.put("CustAgreeInfoRetvInDTO", custAgreeInfoRetvInDTO);
		
		Map<String, Object> request = new HashMap<String, Object>();
		request.put("request", param);
		
		Gson gson = new Gson();
		
		HttpEntity<String> requestMap = new HttpEntity<>(gson.toJson(request), headers);
		
		logger.info("request : " + requestMap);
		logger.info("custAgreeInfoUrl : " + custAgreeInfoUrl);
		ParameterizedTypeReference<Map<String, Object>> typeRef = new ParameterizedTypeReference<Map<String, Object>>(){};
		ResponseEntity<Map<String, Object>> result = restCall.exchange(custAgreeInfoUrl, HttpMethod.POST, requestMap, typeRef);
		logger.info("result : " + result.getBody());
		logger.info("result.getBody().getTransactionid() : " + result.getBody().get("transactionid").toString());
		logger.info("result.getBody().getReturncode() : " + result.getBody().get("returncode").toString());
		logger.info("result.getBody().getReturndescription() : " + result.getBody().get("returndescription").toString());
		
		if ( result == null || YappUtil.isNotEq(result.getBody().get("returncode"), EnumYn.C_1.getValue()) )
		{
			 if(result != null) {
				 throw new YappException("SHUB_MSG", result.getBody().get("errorcode").toString(), result.getBody().get("returndescription").toString(), "[OIF_11267] " +result.getBody().get("errorcode"), result.getBody().get("transactionid").toString());
			 }else{
				 throw new YappException("SHUB_MSG", "", "", "[OIF_11267] " +"", "");
			 }
		}else{
			Map<String, Object> responseMap = (Map<String, Object>) result.getBody().get("response");
			
			ObjectMapper mapper = new ObjectMapper();
			CustAgreeInfoRetvListDTO response = mapper.convertValue(responseMap.get("CustAgreeInfoRetvListDTO") , new TypeReference<CustAgreeInfoRetvListDTO>(){}) ;
			logger.info("response.getCustAgreeInfoRetvListDTO().getResltCd : " + response.getResltCd());
			logger.info("response.getCustAgreeInfoRetvListDTO().getResltMsg : " + response.getResltMsg());
			if(response.getCustAgreeInfoRetvListDto() != null){
				custAgreeList = response.getCustAgreeInfoRetvListDto();
			}
		}
		return custAgreeList;
	}
	
	/**
	 * OIF_11268 고객동의정보의 동의여부 변경 API (선택약관 동의여부 변경)
	 * @throws UnsupportedEncodingException 
	 */
	public String callFn11268(String cntrNo, String agreeCd56, String agreeCd55, String agreeCd56Ver, String agreeCd55Ver) throws RuntimeException, UnsupportedEncodingException
	{
		String successYn = "N";
		RestTemplate restCall = new RestTemplate();
		
		String srchYm = YappUtil.getCurDate("yyyyMM");
		Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(srchYm.substring(0,4)), Integer.parseInt(srchYm.substring(4,6)) -1, 1);
		
		String curDateString = YappUtil.getCurDate("yyyyMMddHHmmss");
	
		HttpHeaders headers = new HttpHeaders();
		String key = connId +":"+ connPwd;
		
		byte[] encVal = key.getBytes("utf-8");
		String encryptedkey = "Basic "+ Base64Utils.encodeToString(encVal);
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", encryptedkey);
		headers.add("orgId", "SPT8050");
		headers.add("srcId", "IF-OShOP-2015-107");
		headers.add("chnlType", "yd");
		headers.add("userId", "91192332");
		
		Map<String, String> bizHeaderMap = new HashMap<String, String>();
		bizHeaderMap.put("orderId", "orderId");
		bizHeaderMap.put("cbSvcName", "CustAgreeInfoRetvSO");
		bizHeaderMap.put("cbFnName", "service");
		
		HashMap<String, String> custAgreeInfoRegWthdInDTO55 = new HashMap<String, String>();
		custAgreeInfoRegWthdInDTO55.put("custId", "");
		custAgreeInfoRegWthdInDTO55.put("custInfoAgreeCd", "55");
		custAgreeInfoRegWthdInDTO55.put("custInfoAgreeYn", agreeCd55);
		custAgreeInfoRegWthdInDTO55.put("agreeDt", curDateString);
		custAgreeInfoRegWthdInDTO55.put("sysTrtrId", "91192332");
		custAgreeInfoRegWthdInDTO55.put("svcTrtOrgId", "SPT8050");
		custAgreeInfoRegWthdInDTO55.put("agreeChCd", "YBX");
		custAgreeInfoRegWthdInDTO55.put("agreeVerNo", agreeCd55Ver);
		custAgreeInfoRegWthdInDTO55.put("svcContId", cntrNo);
		
		HashMap<String, String> custAgreeInfoRegWthdInDTO56 = new HashMap<String, String>();
		custAgreeInfoRegWthdInDTO56.put("custId", "");
		custAgreeInfoRegWthdInDTO56.put("custInfoAgreeCd", "56");
		custAgreeInfoRegWthdInDTO56.put("custInfoAgreeYn", agreeCd56);
		custAgreeInfoRegWthdInDTO56.put("agreeDt", curDateString);
		custAgreeInfoRegWthdInDTO56.put("sysTrtrId", "91192332");
		custAgreeInfoRegWthdInDTO56.put("svcTrtOrgId", "SPT8050");
		custAgreeInfoRegWthdInDTO56.put("agreeChCd", "YBX");
		custAgreeInfoRegWthdInDTO56.put("agreeVerNo", agreeCd56Ver);
		custAgreeInfoRegWthdInDTO56.put("svcContId", cntrNo);
		
		List<HashMap<String, String>> custAgreeInfoRegWthdInDTOList = new ArrayList<HashMap<String, String>>();
		custAgreeInfoRegWthdInDTOList.add(custAgreeInfoRegWthdInDTO55);
		custAgreeInfoRegWthdInDTOList.add(custAgreeInfoRegWthdInDTO56);
		
		Map<String, Object> custAgreeInfoRegWthdDto = new HashMap<String, Object>();
		custAgreeInfoRegWthdDto.put("custAgreeInfoRegWthdDto", custAgreeInfoRegWthdInDTOList);
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("bizHeader", bizHeaderMap);
		param.put("CustAgreeInfoRegWthdInDTO", custAgreeInfoRegWthdDto);
		
		Map<String, Object> request = new HashMap<String, Object>();
		request.put("request", param);
		
		Gson gson = new Gson();
		
		HttpEntity<String> requestMap = new HttpEntity<>(gson.toJson(request), headers);
		
		logger.info("request : " + requestMap);
		
		ParameterizedTypeReference<Map<String, Object>> typeRef = new ParameterizedTypeReference<Map<String, Object>>(){};
		ResponseEntity<Map<String, Object>> result = restCall.exchange(custAgreeInfoChgUrl, HttpMethod.POST, requestMap, typeRef);
		
		logger.info("result : " + result);
		logger.info("result.getBody() : " + result.getBody());
		logger.info("result.getBody().getReturncode() : " + result.getBody().get("returncode"));
		if ( result == null || YappUtil.isNotEq(result.getBody().get("returncode"), EnumYn.C_1.getValue()) )
		{
			successYn = "N";
			if(result != null) {
				throw new YappRuntimeException("SHUB_MSG", result.getBody().get("errorcode").toString(), result.getBody().get("returndescription").toString(), "[OIF_11268] " +result.getBody().get("errorcode"), result.getBody().get("transactionid").toString());
			}else{
				throw new YappRuntimeException("SHUB_MSG", "", "", "[OIF_11268] " , "");
			}
			
			
		}else{
			Map<String, Object> responseMap = (Map<String, Object>) result.getBody().get("response");
			
			ObjectMapper mapper = new ObjectMapper();
			CustAgreeInfoRegWthdOutDTO response = mapper.convertValue(responseMap.get("CustAgreeInfoRegWthdOutDTO") , new TypeReference<CustAgreeInfoRegWthdOutDTO>(){}) ;
			if(response != null){
				successYn = response.getResltCd();
			}
			
		}
		logger.info("successYn : " + successYn);
		return successYn;
	}
}