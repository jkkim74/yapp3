package com.kt.yapp.web;

import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.kt.yapp.common.repository.TestDao;
import com.kt.yapp.common.service.TestService;
import com.kt.yapp.config.LimitationPropConfig;
import com.kt.yapp.domain.Event;
import com.kt.yapp.domain.EventAppl;
import com.kt.yapp.domain.EventYData;
import com.kt.yapp.domain.GrpCode;
import com.kt.yapp.domain.OwnAuth;
import com.kt.yapp.domain.ReviewInfo;
import com.kt.yapp.domain.RsaKeyInfo;
import com.kt.yapp.domain.SessionContractInfo;
import com.kt.yapp.domain.TokenInfo;
import com.kt.yapp.domain.req.EventReq;
import com.kt.yapp.domain.resp.ResultInfo;
import com.kt.yapp.em.EnumYn;
import com.kt.yapp.exception.YappException;
import com.kt.yapp.redis.RedisComponent;
import com.kt.yapp.service.CmsService;
import com.kt.yapp.service.CommonService;
import com.kt.yapp.service.GiftService;
import com.kt.yapp.service.KosService;
import com.kt.yapp.service.MemPointService;
import com.kt.yapp.service.ShubService;
import com.kt.yapp.service.UserService;
import com.kt.yapp.service.WsgService;
import com.kt.yapp.service.YRoamingService;
import com.kt.yapp.soap.SoapConnUtil;
import com.kt.yapp.util.AppEncryptUtils;
import com.kt.yapp.util.KeyFixUtil;
import com.kt.yapp.util.KeyFixUtilForRyt;
import com.kt.yapp.util.RsaCipherUtil;
import com.kt.yapp.util.SessionKeeper;
import com.kt.yapp.util.YappUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "테스트 컨트롤러")
@Controller
public class TestController 
{
	private static final Logger logger = LoggerFactory.getLogger(TestController.class);
	
	@Autowired
	private TestService service;
	@Autowired
	private TestDao dao;
	@Autowired
	private UserController userController;
	
	@Autowired
	private CommonService cmnService;
	@Autowired
	private KosService kosService;
	@Autowired
	private GiftService giftService;
	@Autowired
	private UserService userService;
	@Autowired
	private ShubService shubService;
//	@Autowired
//	private IShubService ishubService;
	@Autowired
	private WsgService wsgService;
	@Autowired
	private TestService testService;
	@Autowired
	private CmsService cmsService;
	@Autowired
	private YRoamingService yrmService;
	@Autowired
	private LimitationPropConfig config;
	@Autowired
	private KeyFixUtil util;
	@Autowired
	private SoapConnUtil soapConnUtil;
	@Resource(name = "redisTemplate")
	private ValueOperations<String, String> valusOps;
	@Autowired
	private AppEncryptUtils appEncryptUtils;
	
	@Resource(name="redisTemplate") 
	private SetOperations<String, String> setOperations;
	
	@Resource(name="redisTemplate") 
	private HashOperations<String, String, String> hashOperations;

	@Resource(name="redisTemplate") 
	private ListOperations<String, String> listOperations;

	@Autowired 
	private RedisComponent redisComponent;
	@Autowired
	private KeyFixUtilForRyt utilRyt;
	@Autowired
	private MemPointService memPointService;
	
	/*@RequestMapping(value = "/getredis", method = RequestMethod.POST)
	public void getredis(String mobile_no ,String cntrNo, String dboxStatus, String reqRcvYn) throws Exception
	{
		redisComponent.put(RedisComponent.MOBILE_NO_PFX_FRD + mobile_no, RedisComponent.KEY2_CNTR_NO, cntrNo);
		redisComponent.put(RedisComponent.MOBILE_NO_PFX_FRD + mobile_no, RedisComponent.KEY2_DBOX_STATUS, dboxStatus);
		redisComponent.put(RedisComponent.MOBILE_NO_PFX_FRD + mobile_no, RedisComponent.KEY2_REQ_RCV_YN, reqRcvYn);

		logger.info(redisComponent.get(RedisComponent.MOBILE_NO_PFX_FRD + mobile_no, RedisComponent.KEY2_CNTR_NO));
		logger.info(redisComponent.get(RedisComponent.MOBILE_NO_PFX_FRD + mobile_no, RedisComponent.KEY2_DBOX_STATUS));
		logger.info(redisComponent.get(RedisComponent.MOBILE_NO_PFX_FRD + mobile_no, RedisComponent.KEY2_REQ_RCV_YN));
	}
	
	@RequestMapping(value = "/na/delredis", method = RequestMethod.POST)
	public void delredis(String mobile_no ,String cntrNo, String dboxStatus, String reqRcvYn) throws Exception
	{
		redisComponent.del(RedisComponent.MOBILE_NO_PFX_FRD + mobile_no, RedisComponent.KEY2_CNTR_NO);
		redisComponent.del(RedisComponent.MOBILE_NO_PFX_FRD + mobile_no, RedisComponent.KEY2_DBOX_STATUS);
		redisComponent.del(RedisComponent.MOBILE_NO_PFX_FRD + mobile_no, RedisComponent.KEY2_REQ_RCV_YN);

		logger.info(redisComponent.get(RedisComponent.MOBILE_NO_PFX_FRD + mobile_no, RedisComponent.KEY2_CNTR_NO));
		logger.info(redisComponent.get(RedisComponent.MOBILE_NO_PFX_FRD + mobile_no, RedisComponent.KEY2_DBOX_STATUS));
		logger.info(redisComponent.get(RedisComponent.MOBILE_NO_PFX_FRD + mobile_no, RedisComponent.KEY2_REQ_RCV_YN));
	}
	
	
	 * yteenfriends 데이터 지급(1000M) 배치 컨트롤러
	 * 향 후 삭제 
	 * 
	 
	@RequestMapping(value = "/na/sendData", method = RequestMethod.GET)
	@ResponseBody
	public void sendData(String cntrNo, String mobileNo, String expireTerm) throws Exception
	{
		System.out.println("Data Send Batch Start..............");		
		
		//get the bonus id from common code table
		GrpCode grpCode_bonusid = cmsService.getCodeNm("YTEEN_GROUP_CODE", "YTF01");
		String bonusId = grpCode_bonusid.getCodeNm();
		
		cntrNo = "524840436";
		mobileNo = "01073001391";
		expireTerm = "1";
		
		
		//giftService.receiveBonusData(bonusId, cntrNo, mobileNo, expireTerm);
	}*/
	
	/*@RequestMapping(value = "/na/getRsa", method = RequestMethod.GET)
	@ResponseBody
	public void getRsa(String str, String key, int index) throws Exception
	{
		System.out.println("Get RSA KEY..............");	
		
		
		KeyPair keyPair = RsaCipherUtil.genRSAKeyPair();
        
		PublicKey publicKey = keyPair.getPublic();   
		
		// 공개키를 Base64 인코딩한 문자일을 만듭니다.
		
		byte[] bytePublicKey = publicKey.getEncoded();        
        String base64PublicKey = Base64Utils.encodeToString(bytePublicKey); //Base64.getEncoder().encodeToString(bytePublicKey);        
        System.out.println("Base64 Public Key : " + base64PublicKey);
	        
        PrivateKey privateKey = keyPair.getPrivate();
        // 개인키를 Base64 디코딩한 문자열을 만듭니다.        
        byte[] bytePrivateKey = privateKey.getEncoded();        
        String base64PrivateKey = Base64Utils.encodeToString(bytePrivateKey); //Base64.getEncoder().encodeToString(bytePrivateKey);        
        System.out.println("Base64 Private Key : " + base64PrivateKey);
        
        
		//RsaKeyInfo rsaKeyInfo = cmsService.getRsaPublicKeyInfo(index);
        
        
		String base64PublicKey[] = new String[2];		
		base64PublicKey[0] = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCiuETnAST2JPyV+Jmyes9wjrEG7OENRNkaIvte/pV4bpVHnwLCn7Uh6SUTct6uwpfk60yWB2ArMwUgSl0wmDoVk0vU1j8WEAMKiZGfhF91Lu3PQd3jnh93oozdm52MYSwNgqY9peFJ8SGU1ousWHEqP/pSMMpru1oxZEw10/Sq3QIDAQAB";
		base64PublicKey[1] = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCCHl0sAlpzeoeqKnaOfEL2Ua6k2guHXVP9Lsc1kWqf0G6SifzMQrhKx0Z7QUKiBfOz+cnaix+XVb9NaViuIJ+RHFqOvdNfaA9/HWZ1P2yGTMpm4SzX0i1rCvvbjswAr/jt6T7Qunv8Y2hB93kAJG2wFvBOClyMQiSZ55rnLqiy7wIDAQAB";
		String base64PrivateKey[] = new String[2];
		base64PrivateKey[0] = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKK4ROcBJPYk/JX4mbJ6z3COsQbs4Q1E2Roi+17+lXhulUefAsKftSHpJRNy3q7Cl+TrTJYHYCszBSBKXTCYOhWTS9TWPxYQAwqJkZ+EX3Uu7c9B3eOeH3eijN2bnYxhLA2Cpj2l4UnxIZTWi6xYcSo/+lIwymu7WjFkTDXT9KrdAgMBAAECgYBSLZHOwIs0LJXL/9NPiip9g/Lvtm4qT1z9kDE3VoeSXPbTRwET3aiSybZJzjecNvaTTCpPksYEyy2Jk0ThJ+Ac+tTvlD5KS4KEPyDO16Ck/L/UCkkI33UH6YJXGF748hd9Y37wMpEapIurVDgAa9CILh85qO/8igxzBZYGRYRHWQJBANiKTFPmrjC7wMNVT9+Uo1+sGpqAk9jdIhxl/Uj4F4HY6Xc1PKRX52+3FJEZuQrbZXran6sBe9z2rhkaFRylAx8CQQDAXzkkmljKjxUqk4LjzWXul0ptAZ3a9pIc3c98oRWDDCwM1MRqu5Kx4O8vt081AzaeTPo4nJs0l0umlG+M+66DAkAOXA13uuJuROUhjvS/BOJYo9cXy9MCHJf4fiLvxdP5PmMtDpC47UNhuyhX/vawa6AtJ3ZTJsQKASnGbF5eCetNAkAoyggLws5g5MXQKUbvlnWuiVW2l0kpTG7ewAlmm6E8EDCm59Zd6zfHj7Ino+6fhvblydykdFBUbmAUChU9B4CRAkBExbcYkMk19+eYlGudC64hEMAkLCL3++uPYMgeFX0Rp1jc1V+MhqWBV8quC1XUbvsrfK7la1rf04BV2GeHL8iB"; 
		base64PrivateKey[1] = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIIeXSwCWnN6h6oqdo58QvZRrqTaC4ddU/0uxzWRap/QbpKJ/MxCuErHRntBQqIF87P5ydqLH5dVv01pWK4gn5EcWo69019oD38dZnU/bIZMymbhLNfSLWsK+9uOzACv+O3pPtC6e/xjaEH3eQAkbbAW8E4KXIxCJJnnmucuqLLvAgMBAAECgYAakSKK6swXCeyLNPJR81Mkwda7t/1knQyaBCQObutrR/5bkyr3daP1OKzOW6vEMxzoIVcydPd1Sb+uiZTesLCDgM4HYMOaHP/6Uc8US0pCr8W0ETvguBK2pVij+eUXbclkUUsmKGTRToIDXfpbwNt7RJmNW/KL9ih+pCGgu57MeQJBALsgKy21UMlEmWbDPnyOkO0WseGF8Jp3IC0Vi1+gBhULUaTv56ZeDzSTTcu+9OOkVKDp9PsFPjNmhJ6h6XsoQz0CQQCyArtJCzvkrbcrmb3mWHVm41Qi82lQP+lVLgMmckzuSbojJO1VpKNKWyoVdnns3u9xQ7QnsoaxWjhSedNqY8GbAkEAj4Xa2FILRnQBHJp/4NUQP2h6wrkSk8buvOWbYprAT3/A/TqseCQIkZCxyKYL+lXeOxV2utmIna/x5CHXjIiTGQJAMg58ldy32eVTlcok0WpckTMpzK5AFhXyykYnEp+frH5E/m764lNqq5UZL3HidU53bJVKSsN0BtlE70RcxBULMwJAHH/oP4Z9OCmCufzo2q/muIXphSWWcKhQkwXleyfObQ/Dg2FoTJs/ICkC7Oxn4NQHWcnKFZcwE34H/zlnjoOvKQ=="; 
		  
        
       // String plainText = key;//"암호화 할 문자열 입니다.";
        
        
        
        // Base64 인코딩된 암호화 문자열 입니다.        
        //String encrypted = RsaCipherUtil.encryptRSA(str, rsaKeyInfo.getPublicKey());        
        //System.out.println("encrypted : " + encrypted);
                
        // 복호화 합니다.        
        //String decrypted = RsaCipherUtil.decryptRSA(encrypted, rsaKeyInfo.getPrivateKey());        
        //System.out.println("decrypted : " + decrypted);

                
       

        
        
	}*/
	
//	@RequestMapping(value = "/na/login/mobileno2", method = RequestMethod.POST)
//	@ResponseBody
//	public void loginMobileNo2(@RequestBody UserInfo userInfo, HttpServletRequest req) throws Exception
//	{
//		 BufferedReader bufferedReader = null;
//        StringBuilder stringBuilder = new StringBuilder();
//
//	        try {
//	            InputStream inputStream = req.getInputStream();
//	            if (inputStream != null) {
//	                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//	                char[] charBuffer = new char[128];
//	                int bytesRead = -1;
//	                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
//	                    stringBuilder.append(charBuffer, 0, bytesRead);
//	                }
//	            }
//	        } catch (IOException ex) {
//	            throw ex;
//	        } finally {
//	            if (bufferedReader != null) {
//	                try {
//	                    bufferedReader.close();
//	                } catch (IOException ex) {
//	                    throw ex;
//	                }
//	            }
//	        }			
//			int tmpNum = 0;
//			Enumeration<String> paramNames = req.getParameterNames();
//			while ( paramNames.hasMoreElements() ) {
//				String paramNm = (String) paramNames.nextElement();
//				String [] paramVals = req.getParameterValues(paramNm);
//				logger.info(tmpNum++ + " " + paramNm + ", " + StringUtils.arrayToDelimitedString(paramVals, ":"));
//			}
//	}
//	@RequestMapping(value = "/na/login/mobileno3", method = RequestMethod.POST)
//	@ResponseBody
//	public void loginMobileNo3(@RequestParam String iptMobileNo, @RequestParam String[] vals2, HttpServletRequest req) throws Exception
//	{
//		BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()));
//		String line = null;
//		while ( (line = br.readLine()) != null )
//			logger.info();
//
//		int tmpNum = 0;
//		Enumeration<String> paramNames = req.getParameterNames();
//		while ( paramNames.hasMoreElements() ) {
//			String paramNm = (String) paramNames.nextElement();
//			String [] paramVals = req.getParameterValues(paramNm);
//			logger.info(tmpNum++ + " " + paramNm + ", " + StringUtils.arrayToDelimitedString(paramVals, ":"));
//		}
//	}
/*	@RequestMapping(value = "/na/login/mobileno", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo loginMobileNo(String iptMobileNo, HttpServletRequest req) throws Exception
	{
		int tmpNum = 0;
		logger.info(tmpNum++ + " " + req.getServerName());
		logger.info(tmpNum++ + " " + req.getScheme());
		logger.info(tmpNum++ + " " + req.getContextPath());
		logger.info(tmpNum++ + " " + req.getServletPath());
		logger.info(tmpNum++ + " " + req.getServerPort());
		logger.info(tmpNum++ + " " + req.getServletContext().getContextPath());
		logger.info(tmpNum++ + " " + req.getRemoteHost());
		logger.info(tmpNum++ + " " + req.getRemoteAddr());
		logger.info(tmpNum++ + " " + req.getSession().getId());
		Enumeration<String> paramNames = req.getParameterNames();
		while ( paramNames.hasMoreElements() ) {
			String paramNm = (String) paramNames.nextElement();
			String [] paramVals = req.getParameterValues(paramNm);
			logger.info(tmpNum++ + " " + paramNm + ", " + StringUtils.arrayToDelimitedString(paramVals, ":"));
		}
		ResultInfo<LoginMobileResp> resultInfo = new ResultInfo<>();
		LoginMobileResp respResult = new LoginMobileResp();
		resultInfo.setResultData(respResult);

		// 계약목록 저장
		SoapResponse139 resp = shubService.callFn139(iptMobileNo, true);
		ContractInfo cntrInfo = resp.getCntrInfo();

		respResult.setCntrInfo(cntrInfo);
		
		// 세션 ID 생성
		String sessionId = UUID.randomUUID().toString();
		respResult.setYsid(sessionId);

		// 세션에 저장
		SessionKeeper.addToReq(req, SessionKeeper.KEY_SESSION_ID, sessionId);
		SessionKeeper.addToReq(req, SessionKeeper.KEY_CNTR_INFO, YappCvtUtil.cvt(cntrInfo, new SessionContractInfo()));
		
		SessionKeeper.setSessionId(req,sessionId);
		SessionKeeper.getSdata(req).setMobileNo(cntrInfo.getMobileNo());
		SessionKeeper.getSdata(req).setExistUser(resp.getCntrInfo().getUserInfo() == null ? false : true);

		return resultInfo;
	}
	
	@RequestMapping(value = "/na/execsql", method = RequestMethod.GET)
	@ResponseBody
	public Object execSql(String sql, HttpServletRequest req) throws Exception
	{
		return dao.selectList(sql);
	}
	
	@RequestMapping(value = "/na/call101", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo call101(String credentialId) throws Exception
	{
		return new ResultInfo<>(shubService.callFn101(credentialId));
	}
	*/

	/**
	 * 계약번호로 휴대폰번호 조회
	 * @param cntrNo
	 * @return
	 * @throws Exception
	 *//*
	@RequestMapping(value = "/na/call103", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo call103(String cntrNo) throws Exception
	{
		return new ResultInfo<>(shubService.callFn103(cntrNo));
	}
	
	@RequestMapping(value = "/na/callSIT", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo call(String mobileNo) throws Exception
	{
		return new ResultInfo<>(shubService.callFn110(mobileNo));
	}
	*//**
	 * 부가서비스 상품목록 조회
	 * @param mobileNoNo
	 * @return
	 * @throws Exception
	 *//*
	@RequestMapping(value = "/na/call133", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo call133(String mobileNoNo) throws Exception
	{
		return new ResultInfo<>(shubService.callFn133(mobileNoNo));
	}
	
	*//**
	 * 휴대폰 번호호 계약정보 조회
	 * @param mobileNo
	 * @param req
	 * @return
	 * @throws Exception
	 *//*
	@RequestMapping(value = "/na/call139", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo call139(String mobileNo, HttpServletRequest req) throws Exception
	{
		logger.info("shubService.callFn139 : " + shubService.callFn139(mobileNo, true).getCntrInfo().toString());
		return new ResultInfo<>(shubService.callFn139(mobileNo, true).getCntrInfo());
	}
	
	*//**
	 * 계약 목록을 조회한다.
	 * @param userId
	 * @param credetntialId
	 * @param req
	 * @return
	 * @throws Exception
	 *//*
	@RequestMapping(value = "/na/call140", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo call140(String userId, String credetntialId, HttpServletRequest req) throws Exception
	{
		return new ResultInfo<>(shubService.callFn140(userId, credetntialId));
	}*/
	
	/*
	@RequestMapping(value = "/na/call139_2", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo call139_2(String mobileNo, HttpServletRequest req) throws Exception
	{
		SoapParam139 soapParam = new SoapParam139(soapConnUtil);
		soapParam.setMobileNo(mobileNo);
		
		SoapResponse139 resp = soapParam.execute();
		// 계약정보 조회
		ContractInfo cntrInfo = resp.getCntrInfo();
		if ( cntrInfo == null )
			return new ResultInfo<>();
		else
			return new ResultInfo<>(mobileNo + ", " + cntrInfo.getPpCd() + ", " + cntrInfo.getPpNm());
	}
	
	@RequestMapping(value = "/na/joinService", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo joinService(String cntrNo, String mobileNo, HttpServletRequest req) throws Exception
	{
		UserInfoReq userInfo = new UserInfoReq();
		userInfo.setCntrNo(cntrNo);
		userInfo.setMobileNo(mobileNo);
		
		TermsAgree agree = new TermsAgree();
		agree.setAgreeChnl("G0001");
		agree.setCntrNo(cntrNo);
		agree.setPiPolicyAgreeYn("Y");
		agree.setPiPolicyTermsVrsn("1.1");
		agree.setPiUseAgreeYn("Y");
		agree.setPiUseTermsVrsn("1.1");
		userInfo.setTermsAgree(agree);
		
		// 가입 정보 추가
		UserInfo joinUserInfo = YappCvtUtil.cvtReqToUserInfo(userInfo);
		SoapResponse110 resp110 = shubService.callFn110(mobileNo);
		joinUserInfo.setUserId(resp110 == null ? null : resp110.getUserId());
		
		userService.insertUserInfo(joinUserInfo);
		
		return new ResultInfo<>();
	}
	
	@RequestMapping(value = "/na/call494", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo call494(String cntrNo, String mobileNo, String pkgTpCd, HttpServletRequest req) throws Exception
	{
		return new ResultInfo<>(shubService.callFn494(cntrNo, mobileNo, pkgTpCd));
	}
	
	@RequestMapping(value = "/na/call495", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo call495(String userId, String cntrNo, String mobileNo, String cpnNo, String useTpCd, HttpServletRequest req) throws Exception
	{
		SoapParam495 soapParam = new SoapParam495(soapConnUtil);
		soapParam.setUserId(userId);
		soapParam.setCntrNo(cntrNo);
		soapParam.setMobileNo(mobileNo);
		soapParam.setCpnNo(cpnNo);
		
		SoapResponse495 resp = soapParam.execute();
		
		return new ResultInfo<>(resp);
	}
	
	@RequestMapping(value = "/na/call750", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo call750(String vasId, String mobileNo, String actionTp, String attrNm, String attrVal, HttpServletRequest req) throws Exception
	{
		SoapParam750 soapParam = new SoapParam750(soapConnUtil);
		soapParam.setVasId(vasId);
		soapParam.setActionTp(actionTp);
		soapParam.setMobileNo(mobileNo);
		soapParam.setAttrNm(attrNm);
		soapParam.setAttrVal(attrVal);
		
		SoapResponse750 resp = soapParam.execute();
		
		return new ResultInfo<>(resp);
	}
	
	@RequestMapping(value = "/na/call2102", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo call2102(String mobileNo, String rcvMobileNo, String subject, String contents, HttpServletRequest req) throws Exception
	{
		SoapParam2102 param = new SoapParam2102(soapConnUtil);
		param.setSendMobileNo(mobileNo);
		param.setRcvMobileNo(rcvMobileNo);
		param.setSubject("테스트hangul");

		StringBuffer sb = new StringBuffer();
		logger.info("######## 1: " + subject + ", " + contents);
		sb.append("Content-Type: text/plain; charset=\"euc-kr\"").append("\n").append("Content-ID : mmsToVasContent ")
		.append("\n\n").append("MO test 한글").append("\n\n");

		logger.info(sb.toString());
		if ( YappUtil.isEmpty(contents) )
			param.setMsgContents(sb.toString());
		else
			param.setMsgContents(contents);

		SoapResponse2102 resp = param.execute();
		logger.info(resp.getRtnCd());
		logger.info(resp.getRtnDesc());
		return new ResultInfo<>(resp);
	}
	
	@RequestMapping(value = "/na/call2118", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo call2118(String mobileNo, String rcvMobileNo, String contents, HttpServletRequest req) throws Exception
	{
		SoapParam2118 param = new SoapParam2118(soapConnUtil);
		param.setSendMobileNo(mobileNo);
		param.setRcvMobileNo(rcvMobileNo);
		param.setMsgContents(contents);
		return new ResultInfo<>(param.execute());
	}
	
	@RequestMapping(value = "/na/callKos1CreateDbox", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo call1KosCreateDbox(String cntrNo, HttpServletRequest req) throws Exception
	{
		return new ResultInfo<>(kosService.createDbox(cntrNo));
	}
	
	@RequestMapping(value = "/na/callKos2CloseDbox", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo call2KosCloseDbox(String cntrNo, HttpServletRequest req) throws Exception
	{
		kosService.closeDbox(cntrNo);
		return new ResultInfo<>();
	}
	
	@RequestMapping(value = "/na/callKos3GetDboxInfo", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo call2KosGetDboxInfo(String cntrNo, HttpServletRequest req) throws Exception
	{
		return new ResultInfo<>(kosService.getDboxInfo(cntrNo));
	}
	
	@RequestMapping(value = "/na/callKos4GetDboxHist", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo call4KosGetDboxHist(String cntrNo, String validStYmd, String validEdYmd, String dataTp, HttpServletRequest req) throws Exception
	{
		return new ResultInfo<>(kosService.getDboxHist(cntrNo, validStYmd, validEdYmd, dataTp));
	}
	
	@RequestMapping(value = "/na/callKos41getDataDivDtlList", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo call41KosgetDataDivDtlList(String cntrNo, String validYm, HttpServletRequest req) throws Exception
	{
		return new ResultInfo<>(kosService.getDataDivDtlList(cntrNo, validYm));
	}
	
	@RequestMapping(value = "/na/callKos42getDboxDtlList", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo call42KosgetDboxDtlList(String cntrNo, String validYm, HttpServletRequest req) throws Exception
	{
		return new ResultInfo<>(kosService.getDboxDtlList(cntrNo, validYm));
	}
	
	@RequestMapping(value = "/na/callKos5GiftData", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo call5KosGiftData(String mobileNo, String rcvMobileNo, int dataAmt, HttpServletRequest req) throws Exception
	{
		kosService.giftData(mobileNo, rcvMobileNo, dataAmt);
		return new ResultInfo<>();
	}
	
	@RequestMapping(value = "/na/callKos6PullData", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo call6KosPullData(String cntrNo, String mobileNo, int dataAmt, HttpServletRequest req) throws Exception
	{
		kosService.pullData(cntrNo, mobileNo, dataAmt);
		return new ResultInfo<>();
	}
	
	@RequestMapping(value = "/na/callKos7ReceiveBonusData", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo call7KosReceiveBonusData(String mobileNo, int dataAmt, String dataTp, HttpServletRequest req) throws Exception
	{
		kosService.receiveBonusData(mobileNo, dataAmt, dataTp);
		return new ResultInfo<>();
	}
	
	@RequestMapping(value = "/na/callKos8CreateDatuk", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo call8KosCreateDatuk(String cntrNo, int dataAmt, String datukId, String mobileNo, HttpServletRequest req) throws Exception
	{
		kosService.createDatuk(cntrNo, dataAmt, datukId, mobileNo);
		return new ResultInfo<>();
	}
	
	@RequestMapping(value = "/na/callKos9ReceiveDatukData", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo call9KosReceiveDatukData(String cntrNo, String dboxId, String datukId, HttpServletRequest req) throws Exception
	{
		kosService.receiveDatukData(cntrNo, dboxId, datukId);
		return new ResultInfo<>();
	}
	
	@RequestMapping(value = "/na/callKos92ReturnDatukData", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo call92KosReturnDatukData(String dboxId, String datukId, HttpServletRequest req) throws Exception
	{
		kosService.returnDatukData(dboxId, datukId);
		return new ResultInfo<>();
	}
	
	@RequestMapping(value = "/na/callKos93MyDataDtl", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo callKos93MyDataDtl(String cntrNo, String mobileNo, String ppCd, HttpServletRequest req) throws Exception
	{
		return new ResultInfo<>(kosService.retrieveDrctlyUseQntDtl(cntrNo, mobileNo));
	}

	@RequestMapping(value = "/na/callKosRetrieveDataOneShotDtl", method = RequestMethod.GET)
	@ResponseBody
	public void callKosRetrieveDataOneShotDtl(String dboxId, String datukId, String validStYmd) throws Exception
	{
		kosService.retrieveDataOneShotDtl(dboxId, datukId, validStYmd);
		//return new ResultInfo<>(kosService.retrieveDataOneShotDtl(dboxId, datukId, validStYmd));
	}
*/	
//	@RequestMapping(value = "/na/connsoap", method = RequestMethod.GET)
//	public void connSoap(HttpServletRequest req) throws Exception
//	{
////		BufferedReader msgBr = new BufferedReader(new FileReader("c:/aaa.txt"));
//		BufferedReader msgBr = new BufferedReader(new FileReader("/jboss/applications/planY11/aaa.txt"));
//		String soapMsg = msgBr.readLine();
//		msgBr.close();
//		logger.info(soapMsg);
//		logger.info();
//		
//		String urlStr = "https://msg.api.kt.com/MESG";
//		URL url = new URL(urlStr);
//		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//		
//		conn.setRequestMethod("POST");
//		conn.setDoOutput(true);
//		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
////		conn.setRequestProperty(key, value);
////		String soapMsg = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ag=\"http://xml.kt.com/sdp/so/BS/MessageMTMMSReportNoNetCharge\" xmlns:oas=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:sdp=\"http://kt.com/sdp\"><SOAP-ENV:Header><oas:Security><oas:UsernameToken><oas:Username>AII4720061843WRGAVC</oas:Username><oas:Password>TBK4720061843WOHSYC</oas:Password></oas:UsernameToken></oas:Security></SOAP-ENV:Header><SOAP-ENV:Body><ag:MessageMTMMSReportNoNetCharge><ag:TRID>plany1506393985348</ag:TRID><ag:VASID>plany</ag:VASID><ag:PUSH>push</ag:PUSH><ag:ISCONVERTED>false</ag:ISCONVERTED><ag:SUBJECT>test</ag:SUBJECT><ag:ENC_CONTENT>test</ag:ENC_CONTENT><ag:CALL_CTN>01082280945</ag:CALL_CTN><ag:RCV_CTN>01082280945</ag:RCV_CTN><ag:PFORM_TYPE>1</ag:PFORM_TYPE><ag:SERVICE_TYPE>A</ag:SERVICE_TYPE><ag:READNOTI>false</ag:READNOTI><ag:RECEIVENOTI>false</ag:RECEIVENOTI></ag:MessageMTMMSReportNoNetCharge></SOAP-ENV:Body></SOAP-ENV:Envelope>";
//		
//		if ( soapMsg != null ) {
//			OutputStream os = conn.getOutputStream();
//			os.write(soapMsg.toString().getBytes("utf-8"));
//			logger.info(soapMsg.toString());
//			os.flush();
//			
//			os.close();
//		}
//
//		BufferedReader br = conn.getResponseCode() == 200 ? new BufferedReader(new InputStreamReader(conn.getInputStream())) : new BufferedReader(new InputStreamReader(conn.getErrorStream()));
//		String line = null;
//		StringBuffer sb = new StringBuffer();
//		while ( (line = br.readLine()) != null )
//		{
//			sb.append(line);
//			logger.info(line);
//		}
//	}
	
/*	@RequestMapping(value = "/na/zencode", method = RequestMethod.POST)
	@ResponseBody
	public String encode(@RequestParam String tarStr) throws Exception
	{
		String encodeStr = util.encode(tarStr);
		logger.info(encodeStr);
		
		return encodeStr;
	}
	@RequestMapping(value = "/na/zdecode", method = RequestMethod.POST)
	@ResponseBody
	public String decode(@RequestParam String tarStr) throws Exception
	{
		String decodeStr = util.decode(tarStr);
		logger.info(decodeStr);
		
		return decodeStr;
	}
	@RequestMapping(value = "/na/zencoderyt", method = RequestMethod.POST)
	@ResponseBody
	public String encodeRyt(@RequestParam String tarStr) throws Exception
	{
		String encodeStr = utilRyt.encode(tarStr);
		logger.info(encodeStr);
		
		return encodeStr;
	}
	@RequestMapping(value = "/na/zdecoderyt", method = RequestMethod.POST)
	@ResponseBody
	public String decodeRyt(@RequestParam String tarStr) throws Exception
	{
		String decodeStr = utilRyt.decode(tarStr);
		logger.info(decodeStr);
		
		return decodeStr;
	}
	*/
	/*@RequestMapping(value = "/na/mempoint/get", method = RequestMethod.POST)
	@ResponseBody
	public Object getMemPoint(@RequestParam String cntrNo) throws Exception
	{
		return new Object();
		//return memPointService.getMemPoint(cntrNo);
	}*/
	/*
	@RequestMapping(value = "/na/mempoint/use", method = RequestMethod.POST)
	@ResponseBody
	public Object useMemPoint(@RequestParam String cntrNo, String memId, String pointPdCd, int usePoint) throws Exception
	{
		return memPointService.useMemPoint(cntrNo, memId, pointPdCd, usePoint);
	}
	
	@RequestMapping(value = "/na/mempoint/acu", method = RequestMethod.POST)
	@ResponseBody
	public Object acuMemPoint(@RequestParam String cntrNo, String memId, String pointPdCd, int usePoint) throws Exception
	{
		return memPointService.acuMemPoint(cntrNo, memId, usePoint);
	}*/
//
//	@ApiOperation(value="User 정보를 추가한다.")
//	@RequestMapping(value = "/user", method = RequestMethod.POST)
//	@ResponseBody
//	public void insertUserInfo(@RequestBody UserInfo userInfo)
//	{
//		service.insertUserInfo(userInfo);
//	}
//
//	@ApiOperation(value="User 목록을 조회한다.")
//	@RequestMapping(value = "/users", method = RequestMethod.GET)
//	@ResponseBody
//	public List<UserInfo> getUserInfoList()
//	{
//		logger.info("logging!!!!!!!!!!!!!!!!!!!!!");
//		
//		return service.getUserInfo();
//	}
//	
//	@RequestMapping(value = "/connect", method = RequestMethod.GET)
//	@ApiOperation(value="접속 테스트")
//	public String connectTest(HttpSession session)
//	{
//		return "Success Connection !";
//	}
//	
//	@RequestMapping(value = "/confirmProfile", method = RequestMethod.GET)
//	@ApiOperation(value="현재 서버 프로파일명 조회")
//	@ResponseBody
//	public String confirmProfile(@Value("${profile.name}") String value)
//	{
//		return "profile name : " + value;
//	}
//
//	@ResponseBody
//	@RequestMapping(value = "/na/insimg", method = RequestMethod.POST)
//	public void insertImg(String fileUrl) throws Exception
//	{
//		Map<String, Object> paramMap = new HashMap<>();
//		paramMap.put("fileId", UUID.randomUUID().toString().replaceAll("-", ""));
//		paramMap.put("fileNm", "img");
//		paramMap.put("fileTp", "img");
//		
//		paramMap.put("fileData", FileCopyUtils.copyToByteArray(new FileInputStream(fileUrl)));
//		dao.insertImg(paramMap);
//	}
//
//	@ResponseBody
//	@RequestMapping(value = "/na/insertUser", method = RequestMethod.POST)
//	public void insertUser() throws Exception
//	{
//		testService.insertUserInfoBatch();
//	}
//	
//	@RequestMapping(value = "/getimg", method = RequestMethod.GET)
//	public void getImg(HttpServletResponse resp) throws Exception
//	{
//		Map<String, Object> paramMap = new HashMap<>();
//		paramMap.put("userId", UUID.randomUUID().toString().substring(0, 5));
//		paramMap.put("userName", "jeung");
//		paramMap.put("img", new FileInputStream("c:/creditImg.png"));
//		List<Map<String, Object>> imgList = dao.getImg(paramMap);
//		byte[] imgByte = (byte[]) imgList.get(2).get("img");
//		FileCopyUtils.copy(imgByte, resp.getOutputStream());
//		resp.getOutputStream().close();
//	}
/*	
	@ResponseBody
	@RequestMapping(value = "/na/sndMail", method = RequestMethod.POST)
	public void sndMail() throws Exception
	{
		String mailKey = UUID.randomUUID().toString().replaceAll("-", "");
		ThunderAutoMailSender tms = new ThunderAutoMailSender();
		ThunderAutoMail tm = new ThunderAutoMail();
		//tm.setThunderMailURL("info.kt.com");
		tm.setThunderMailURL("ems.olleh.com:8080");
		tm.setAutomailID("841");
		//tm.setMailTitle("안녕하세요? [$name] 고객님께서 보내신 Y데이터박스 가입동의 메일입니다.");
		//tm.setMailTitle("안녕하세요? 홍길동 고객님께서 보내신 Y데이터박스 가입동의 메일입니다.");
		tm.setMailTitle("");
		tm.setMailContent("");
		tm.setSenderMail("Y_Databox@email.olleh.com");
		tm.setSenderName("Y데이터박스");
		tm.setEmail("mintwin@amoeba.co.kr");
		String oneToOneText = "[$name]Ð홀길동æ[$fname]Ð홀길동æ[$phone]Ð01099992222æ[$tname]Ð김길동æ[$mailkey]Ð"+mailKey;
		tm.setOnetooneInfo(oneToOneText);
		tm.setCustomerID("");
		String result = tms.send(tm);
		logger.info("======================= sndMail ==========================");
		logger.info(result);
		logger.info("==========================================================");
	}
	
	@RequestMapping(value = "/na/sessionTest", method = RequestMethod.GET)
	@ResponseBody
	public void sessionTest(HttpServletRequest req) throws Exception
	{
		Enumeration headers = req.getHeaderNames();
		while(headers.hasMoreElements()){
			String headerName = (String)headers.nextElement();
			String value = req.getHeader(headerName);
			logger.info("headerName = " + headerName);
			logger.info("value = " + value);
			
		}
		logger.info("=================================================================================");
		Cookie[] cookie = req.getCookies();
		String sessionid = null;
		if(cookie != null){
			for(int i = 0; i < cookie.length; i++){
				sessionid = cookie[i].getValue();
				logger.info("[sessionid] = " + sessionid);
			}
		}
		
		String sessionId = "123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		SessionKeeper_back.setSessionId(req, sessionId);
		String rcvSessionId = (String) SessionKeeper_back.get(req, SessionKeeper_back.KEY_SESSION_ID);
		String rcvMobileNo = (String) SessionKeeper_back.getSdata(req).getMobileNo();
		String rcvCredentialId = (String) SessionKeeper_back.getSdata(req).getCredentialId();
		String rcvUserId = (String) SessionKeeper_back.getSdata(req).getUserId();
		boolean rcvExistUser = (boolean) SessionKeeper_back.getSdata(req).isExistUser();
		logger.info("=================================================================================");
		logger.info(" rcvSessionId    => " + rcvSessionId);
		logger.info(" rcvMobileNo     => " + rcvMobileNo);
		logger.info(" rcvCredentialId => " + rcvCredentialId);
		logger.info(" rcvUserId       => " + rcvUserId);
		logger.info(" rcvExistUser    => " + rcvExistUser);
		logger.info("=================================================================================");
		
		
		// 세션에 저장
		SessionKeeper_back.addToReq(req, SessionKeeper_back.KEY_SESSION_ID, sessionId);
		SessionKeeper_back.addToReq(req, SessionKeeper_back.KEY_CNTR_INFO, new SessionContractInfo());
		
		SessionKeeper_back.setSessionId(req, sessionId);
		SessionKeeper_back.getSdata(req).setMobileNo("01066426611");
		SessionKeeper_back.getSdata(req).setExistUser(true);
		SessionKeeper_back.getSdata(req).setCredentialId("000000000000001");
		SessionKeeper_back.getSdata(req).setUserId("USER_ID_TEST");
		
		rcvSessionId = (String) SessionKeeper_back.get(req, SessionKeeper.KEY_SESSION_ID);
		rcvMobileNo = (String) SessionKeeper_back.getSdata(req).getMobileNo();
		rcvCredentialId = (String) SessionKeeper_back.getSdata(req).getCredentialId();
		rcvUserId = (String) SessionKeeper_back.getSdata(req).getUserId();
		rcvExistUser = (boolean) SessionKeeper_back.getSdata(req).isExistUser();
		logger.info("=================================================================================");
		logger.info(" rcvSessionId    => " + rcvSessionId);
		logger.info(" rcvMobileNo     => " + rcvMobileNo);
		logger.info(" rcvCredentialId => " + rcvCredentialId);
		logger.info(" rcvUserId       => " + rcvUserId);
		logger.info(" rcvExistUser    => " + rcvExistUser);
		logger.info("=================================================================================");
		logger.info("=================================================================================");
		
	}
	
	@RequestMapping(value = "/na/sessionTest2", method = RequestMethod.GET)
	@ResponseBody
	public void sessionTes2t(HttpServletRequest req) throws Exception
	{

		Cookie[] cookie = req.getCookies();
		String sessionid = null;
		if(cookie != null){
			for(int i = 0; i < cookie.length; i++){
				sessionid = cookie[i].getValue();
				logger.info("[sessionid] = " + sessionid);
			}
		}
		req.getSession().getMaxInactiveInterval();
		String rcvSessionId = (String) SessionKeeper_back.get(req, SessionKeeper_back.KEY_SESSION_ID);
		String rcvMobileNo = (String) SessionKeeper_back.getSdata(req).getMobileNo();
		String rcvCredentialId = (String) SessionKeeper_back.getSdata(req).getCredentialId();
		String rcvUserId = (String) SessionKeeper_back.getSdata(req).getUserId();
		boolean rcvExistUser = (boolean) SessionKeeper_back.getSdata(req).isExistUser();
		logger.info("=================================================================================");
		logger.info(" getMaxInactiveInterval " + req.getSession().getMaxInactiveInterval());
		logger.info(" rcvSessionId    => " + rcvSessionId);
		logger.info(" rcvMobileNo     => " + rcvMobileNo);
		logger.info(" rcvCredentialId => " + rcvCredentialId);
		logger.info(" rcvUserId       => " + rcvUserId);
		logger.info(" rcvExistUser    => " + rcvExistUser);
		logger.info("=================================================================================");
		String sessionId = "123456789";
		SessionKeeper_back.addToReq(req, SessionKeeper.KEY_SESSION_ID, sessionId);
		String rcvSessionId1 = (String) SessionKeeper_back.get(req, SessionKeeper_back.KEY_SESSION_ID);
		String rcvMobileNo1 = (String) SessionKeeper_back.getSdata(req).getMobileNo();
		String rcvCredentialId1 = (String) SessionKeeper_back.getSdata(req).getCredentialId();
		String rcvUserId1 = (String) SessionKeeper_back.getSdata(req).getUserId();
		boolean rcvExistUser1 = (boolean) SessionKeeper_back.getSdata(req).isExistUser();
		logger.info(" rcvSessionId    => " + rcvSessionId1);
		logger.info(" rcvMobileNo     => " + rcvMobileNo1);
		logger.info(" rcvCredentialId => " + rcvCredentialId1);
		logger.info(" rcvUserId       => " + rcvUserId1);
		logger.info(" rcvExistUser    => " + SessionKeeper_back.getSdata(req).isExistUser());
		logger.info("=================================================================================");
	}
	
	@RequestMapping(value = "/na/getHiddenArea", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo getHiddenArea(String cntrNo) throws Exception
	{
		//List<BonusData> targetAutoBonusDataList = giftService.getAutoBonusDataList(cntrNo);
		List<AccmGiftData> allGiftDataList = giftService.getAccmGiftDataAllList(cntrNo);
		EventReq eventInfo = new EventReq();
		eventInfo.setCntrNo(cntrNo);
		List<Event> targetEventList = cmsService.getHiddenEventList(eventInfo);
		List<HiddenAreaData> hiddenAreaDataList = getHiddenAreaData(allGiftDataList, targetEventList);
		return new ResultInfo<>(hiddenAreaDataList);
	}
	
	@RequestMapping(value = "/na/insertAutoBonusData", method = RequestMethod.GET)
	@ResponseBody
	public void insertAutoBonusData(String cntrNo) throws Exception
	{
		giftService.insertAutoBonusData(cntrNo);
	}
	
	private List<HiddenAreaData> getHiddenAreaData(List<AccmGiftData> allGiftDataList, List<Event> targetEventList)
	{
		List<HiddenAreaData> hiddenAreaDataList = new ArrayList<>();
		int cnt = 0;
		int dataAmtSum = 0;
		int dataStandard = 500;
		for(AccmGiftData giftTarget : allGiftDataList){
			if(cnt == 0){
				HiddenAreaData hiddenAreaBonusData = new HiddenAreaData();
				hiddenAreaBonusData.setHiddenGb("VOL");
				hiddenAreaBonusData.setTitle("최초 선물 기념");
				hiddenAreaBonusData.setStYmd(giftTarget.getSndDt());
				hiddenAreaDataList.add(hiddenAreaBonusData);
			}
			dataAmtSum = dataAmtSum + giftTarget.getSndDataAmt();
			if(dataAmtSum >= dataStandard){
				int divideCnt = dataAmtSum / dataStandard;
				for(int i=0; i < divideCnt; i++){
					HiddenAreaData hiddenAreaBonusData = new HiddenAreaData();
					hiddenAreaBonusData.setHiddenGb("VOLUME");
					hiddenAreaBonusData.setTitle("누적 " + (dataStandard/100) + "기가 기념");
					hiddenAreaBonusData.setStYmd(giftTarget.getSndDt());
					hiddenAreaDataList.add(hiddenAreaBonusData);
					dataStandard = dataStandard + 500;
				}
			}
			cnt++;
		}
		for(Event eventTarget : targetEventList){
			HiddenAreaData hiddenAreaEvtData = new HiddenAreaData();
			hiddenAreaEvtData.setHiddenGb("EVT");
			hiddenAreaEvtData.setEvtSeq(eventTarget.getEvtSeq());
			hiddenAreaEvtData.setTitle(eventTarget.getTitle());
			hiddenAreaEvtData.setContents(eventTarget.getContents());
			hiddenAreaEvtData.setStYmd(eventTarget.getEvtStDt());
			hiddenAreaEvtData.setEdYmd(eventTarget.getEvtEdDt());
			hiddenAreaEvtData.setEvtBtnLbl(eventTarget.getBtnLbl());
			hiddenAreaEvtData.setEvtApplYn(eventTarget.getApplYn());
			hiddenAreaEvtData.setEvtEndYn(eventTarget.getEndYn());
			hiddenAreaEvtData.setEvtLinkUrl(eventTarget.getLinkUrl());
			hiddenAreaEvtData.setEvtListImgURL(eventTarget.getListImgURL());
			hiddenAreaEvtData.setEvtWinYn(eventTarget.getWinYn());
			hiddenAreaEvtData.setEvtTp(eventTarget.getEvtTp());
			hiddenAreaEvtData.setRegDt(eventTarget.getRegDt());
			hiddenAreaDataList.add(hiddenAreaEvtData);
		}
		return hiddenAreaDataList;
	}*/
	
//	private List<HiddenAreaData> getHiddenAreaDataBack(List<AccmGiftData> allGiftDataList, List<Event> targetEventList)
//	{
//		List<HiddenAreaData> hiddenAreaDataList = new ArrayList<>();
//		int cnt = 0;
//		int dataAmtSum = 0;
//		int dataStandard = 500;
//		for(AccmGiftData giftTarget : allGiftDataList){
//			if(cnt == 0){
//				HiddenAreaData hiddenAreaBonusData = new HiddenAreaData();
//				hiddenAreaBonusData.setHiddenGb("VOLUME");
//				hiddenAreaBonusData.setTitle("최초 선물 기념");
//				hiddenAreaBonusData.setStYmd(giftTarget.getSndDt());
//				hiddenAreaDataList.add(hiddenAreaBonusData);
//			}
//			dataAmtSum = dataAmtSum + giftTarget.getSndDataAmt();
//			logger.info("=======================");
//			logger.info("dataAmtSum > " + dataAmtSum);
//			logger.info("dataStandard > " + dataStandard);
//			logger.info("dataAmtSum / dataStandard > " + dataAmtSum / dataStandard);
//			logger.info("=======================");
//			if(dataAmtSum >= dataStandard){
//				int divideCnt = dataAmtSum / dataStandard;
//				for(int i=0; i < divideCnt; i++){
//					HiddenAreaData hiddenAreaBonusData = new HiddenAreaData();
//					hiddenAreaBonusData.setHiddenGb("VOLUME");
//					hiddenAreaBonusData.setTitle("누적 " + (dataStandard/100) + "기가 기념");
//					//bonusData.setTitle("누적 " + rcvDataAmt + "기가 기념");
//					hiddenAreaBonusData.setStYmd(giftTarget.getSndDt());
//					hiddenAreaDataList.add(hiddenAreaBonusData);
//					dataStandard = dataStandard + 500;
//				}
//			}
//			cnt++;
//		}
//		for(BonusData bonusTarget : targetBonusDataList){
//			HiddenAreaData hiddenAreaBonusData = new HiddenAreaData();
//			hiddenAreaBonusData.setHiddenGb("VOLUME");
//			hiddenAreaBonusData.setBonusId(bonusTarget.getBonusId());
//			hiddenAreaBonusData.setBonusTp(bonusTarget.getBonusTp());
//			hiddenAreaBonusData.setTitle(bonusTarget.getTitle());
//			hiddenAreaBonusData.setBonusDataAmt(bonusTarget.getDataAmt());
//			hiddenAreaBonusData.setStYmd(bonusTarget.getRcvStYmd());
//			hiddenAreaBonusData.setEdYmd(bonusTarget.getRcvEdYmd());
//			hiddenAreaBonusData.setContents(bonusTarget.getRemarks());
//			hiddenAreaBonusData.setRegDt(bonusTarget.getRegDt());
//			hiddenAreaDataList.add(hiddenAreaBonusData);
//		}
//		
//		for(Event eventTarget : targetEventList){
//			HiddenAreaData hiddenAreaEvtData = new HiddenAreaData();
//			hiddenAreaEvtData.setHiddenGb("EVT");
//			hiddenAreaEvtData.setEvtSeq(eventTarget.getEvtSeq());
//			hiddenAreaEvtData.setTitle(eventTarget.getTitle());
//			hiddenAreaEvtData.setContents(eventTarget.getContents());
//			hiddenAreaEvtData.setStYmd(eventTarget.getEvtStDt());
//			hiddenAreaEvtData.setEdYmd(eventTarget.getEvtEdDt());
//			hiddenAreaEvtData.setEvtBtnLbl(eventTarget.getBtnLbl());
//			hiddenAreaEvtData.setEvtApplYn(eventTarget.getApplYn());
//			hiddenAreaEvtData.setEvtEndYn(eventTarget.getEndYn());
//			hiddenAreaEvtData.setEvtLinkUrl(eventTarget.getLinkUrl());
//			hiddenAreaEvtData.setEvtListImgURL(eventTarget.getListImgURL());
//			hiddenAreaEvtData.setEvtWinYn(eventTarget.getWinYn());
//			hiddenAreaEvtData.setEvtTp(eventTarget.getEvtTp());
//			hiddenAreaEvtData.setRegDt(eventTarget.getRegDt());
//			hiddenAreaDataList.add(hiddenAreaEvtData);
//		}
//		return hiddenAreaDataList;
//	}
	
//	@RequestMapping(value = "/na/getDataMent", method = RequestMethod.POST)
//	@ResponseBody
//	public void getDataMent(String codeId) throws Exception
//	{
//		String dataMent = "하하하하하하하";
//		logger.info("=================================================================================");
//		logger.info("getDataMent " + dataMent);
//		GrpCode codeMent = cmsService.getDataMent(codeId);
//		if (codeMent != null ){
//			dataMent = codeMent.getCodeNm();
//		}
//		
//		logger.info("getDataMent " + dataMent);
//		logger.info("=================================================================================");
//	}
//	
//	@RequestMapping(value = "/na/getSessionId", method = RequestMethod.POST)
//	@ResponseBody
//	public void getSessionId(String encSessionId) throws Exception
//	{
//		logger.info("=================================================================================");
//		//String tmpSessionKey = cntrNo + "||" +mobileNo + "||" + userId + "||" + true;
//		logger.info("tmpSessionKey " + tmpSessionKey);
//		String encSessionKey = keyFixUtil.encode(tmpSessionKey);
//		logger.info("encSessionKey " + encSessionKey);
//		String decSessionKey = keyFixUtil.decode(encSessionKey);
//		logger.info("decSessionKey " + decSessionKey);
//		String decSessionKey = encSessionId;
//		String[] tmp = decSessionKey.split("\\|\\|");
//		for(int i=0;i<tmp.length;i++){
//			logger.info("vv " + tmp[i]);
//		}
//		logger.info("=================================================================================");
//		StringTokenizer tokens = new StringTokenizer(decSessionKey,"\\|\\|");
//		String[] tmp2 = new String[4];
//		int i = 0;
//		while(tokens.hasMoreTokens()){
//			tmp2[i] = tokens.nextToken();
//			logger.info("vv2 " + tmp2[i]);
//			i++;
//		}
//		logger.info("=================================================================================");
//	}
	
//	@RequestMapping(value = "/na/getHiddenAreaVasData", method = RequestMethod.GET)
//	@ResponseBody
//	public ResultInfo getHiddenAreaVasData(String ppCd)  throws Exception
//	{
//		CallingPlan callingPlan = cmsService.getCallingPlanInfo(ppCd);
//		List<HiddenAreaVasData> hiddenAreaVasDataList = new ArrayList<>();
//		int sumCount = 0;
//		// 장기혜택 쿠폰 가능여부
//		if(callingPlan.getLongPsYn().equals("Y") && sumCount < 5){
//			HiddenAreaVasData hiddenAreaVasData = new HiddenAreaVasData();
//			hiddenAreaVasData.setHiddenGb("LONG");
//			hiddenAreaVasData.setTitle("장기혜택 쿠폰");
//			hiddenAreaVasDataList.add(hiddenAreaVasData);
//			sumCount = sumCount +1;
//		}
//		
//		// 데이터 룰렛 가능여부
//		if(callingPlan.getRouletPsYn().equals("Y") && sumCount < 5){
//			HiddenAreaVasData hiddenAreaVasData = new HiddenAreaVasData();
//			hiddenAreaVasData.setHiddenGb("ROULET");
//			hiddenAreaVasData.setTitle("데이터 룰렛");
//			hiddenAreaVasDataList.add(hiddenAreaVasData);
//			sumCount = sumCount +1;
//		}
//		// 후불충전 가능여부
//		if(callingPlan.getLaterChgUsePsYn().equals("Y") && sumCount < 5){
//			HiddenAreaVasData hiddenAreaVasData = new HiddenAreaVasData();
//			hiddenAreaVasData.setHiddenGb("LATER");
//			hiddenAreaVasData.setTitle("후불충전");
//			hiddenAreaVasDataList.add(hiddenAreaVasData);
//			sumCount = sumCount +1;
//		}
//
//		// 멤버십 충전 가능여부
//		if(callingPlan.getMemChgUsePsYn().equals("Y") && sumCount < 5){
//			HiddenAreaVasData hiddenAreaVasData = new HiddenAreaVasData();
//			hiddenAreaVasData.setHiddenGb("MEM");
//			hiddenAreaVasData.setTitle("멤버십 충전");
//			hiddenAreaVasDataList.add(hiddenAreaVasData);
//			sumCount = sumCount +1;
//		}
//		
//		// 당겨쓰기 가능여부
//		if(callingPlan.getPullPsYn().equals("Y") && sumCount < 5){
//			HiddenAreaVasData hiddenAreaVasData = new HiddenAreaVasData();
//			hiddenAreaVasData.setHiddenGb("PULL");
//			hiddenAreaVasData.setTitle("당겨쓰기");
//			hiddenAreaVasDataList.add(hiddenAreaVasData);
//			sumCount = sumCount +1;
//		}
//		
//		// 매일3시간 무료 가능여부
//		if(callingPlan.getThreeUsePsYn().equals("Y") && sumCount < 5){
//			HiddenAreaVasData hiddenAreaVasData = new HiddenAreaVasData();
//			hiddenAreaVasData.setHiddenGb("THREE");
//			hiddenAreaVasData.setTitle(" 매일3시간 무료");
//			hiddenAreaVasDataList.add(hiddenAreaVasData);
//			sumCount = sumCount +1;
//		}
//		// 반값팩 가능여부
//		if(callingPlan.getHalfUsePsYn().equals("Y") && sumCount < 5){
//			HiddenAreaVasData hiddenAreaVasData = new HiddenAreaVasData();
//			hiddenAreaVasData.setHiddenGb("HALF");
//			hiddenAreaVasData.setTitle("반값팩");
//			hiddenAreaVasDataList.add(hiddenAreaVasData);
//			sumCount = sumCount +1;
//		}
//
//		// 두배쓰기 가능여부
//		if(callingPlan.getDblUsePsYn().equals("Y") && sumCount < 5){
//			HiddenAreaVasData hiddenAreaVasData = new HiddenAreaVasData();
//			hiddenAreaVasData.setHiddenGb("DOUBLE");
//			hiddenAreaVasData.setTitle("두배쓰기");
//			hiddenAreaVasDataList.add(hiddenAreaVasData);
//			sumCount = sumCount +1;
//		}
//		// 바꿔쓰기 가능여부
//		if(callingPlan.getChgUsePsYn().equals("Y") && sumCount < 5){
//			HiddenAreaVasData hiddenAreaVasData = new HiddenAreaVasData();
//			hiddenAreaVasData.setHiddenGb("CHG");
//			hiddenAreaVasData.setTitle("바꿔쓰기");
//			hiddenAreaVasDataList.add(hiddenAreaVasData);
//			sumCount = sumCount +1;
//		}
//		return new ResultInfo<>(hiddenAreaVasDataList);
//	}
	//#######################################
	//## WSG 호출
	//#######################################
	/**
	 * 멤버십 정보를 조회한다.(크레덴션 ID가 없으면 휴대폰 번호로 조회한다.)
	 * @param credentialId
	 * @param mobileNo
	 * @return
	 * @throws Exception
	 */
	/*@RequestMapping(value = "/na/getMemInfo", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo getMemInfo(String credentialId, String mobileNo) throws Exception
	{
		return new ResultInfo<>(wsgService.getMembershipInfo(credentialId, mobileNo));
	}*/
	
	/**
	 * 부가서비스 두배쓰기 설정여부를 조회한다.
	 * @param mobileNo
	 * @return Y, N
	 * @throws Exception
	 */
	/*@RequestMapping(value = "/na/getVasDoubleInfo", method = RequestMethod.GET)
	@ResponseBody
	public String getVasDoubleInfo(String mobileNo) throws Exception
	{
		return "";
		//return wsgService.getVasDoubleInfo(mobileNo);
	}*/
	
	/**
	 * My Time Plan 설정 정보를 조회한다.
	 * @param svcCode
	 * @param mobileNo
	 * @param prdcCd
	 * @return
	 * @throws Exception
	 */
	/*@RequestMapping(value = "/na/getMyTimePlanSetTime", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo getMyTimePlanSetTime(String svcCode, String mobileNo, String prdcCd) throws Exception
	{
		//wsgService.getMyTimePlanSetTime(svcCode, mobileNo, prdcCd);
		return new ResultInfo<>();
	}*/
	
	/*@RequestMapping(value = "/na/getTimePlan", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo<String> getTimePlan(String cntrNo) throws Exception
	{
		String timePlan = kosService.getMyTimePlan(cntrNo);
		return new ResultInfo<>(timePlan);
	}*/
	
	/**
	 * 데이터 사용량 정보를 조회한다.
	 * @param mobileNo
	 * @param tarMnth
	 * @param callingPlan
	 * @return
	 * @throws Exception
	 */
	/*
	@RequestMapping(value = "/na/getMobileTotalUseWeb", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo getMobileTotalUseWeb(String mobileNo, String tarMnth, CallingPlan callingPlan) throws Exception {
		return new ResultInfo<>(wsgService.getMobileTotalUseWeb(mobileNo, tarMnth, callingPlan));
	}*/
	
//	@RequestMapping(value = "/na/callDataUse", method = RequestMethod.GET)
//	@ResponseBody
//	public Map callDataUse(String mobileNo, String month, HttpServletRequest req) throws Exception
//	{
//		String url = "https://appgw.olleh.com/api/bill/getMobileTotalUseWeb?svcCode=YAPN001&ctn=" + mobileNo;
//		if (month != null )
//			url += "&month=" + month;
//		
//		ObjectMapper mapper = new ObjectMapper();
//		Map totalUse = mapper.readValue(new URL(url), Map.class);
//		logger.info(totalUse);
//		
//		return totalUse;
//	}
	
	/**
	 * 휴대폰 번호로 크레덴셜ID 조회
	 * @param mobileNo
	 * @return
	 * @throws Exception
	 */
	/*@RequestMapping(value = "/na/call086", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo call086(OwnAuth paramObj) throws Exception
	{
		return new ResultInfo<>(shubService.callFn086(paramObj));
	}*/
	
	/*public SoapResponse086 callFn086(OwnAuth paramObj) throws Exception
	{
		return ishubService.callFn086(paramObj);
	}*/
	
	/**
	 * 휴대폰 번호로 크레덴셜ID 조회
	 * @param mobileNo
	 * @return
	 * @throws Exception
	 */
	/*@RequestMapping(value = "/na/call110", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo call110(String mobileNo) throws Exception
	{
		return new ResultInfo<>(shubService.callFn110(mobileNo));
	}*/
	
	/*public SoapResponse110 callSH110(String mobileNo) throws Exception
	{
		return ishubService.callFn110(mobileNo);
	}*/
	
	/*@RequestMapping(value = "/na/yCollaboList", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="Y콜라보 이벤트 목록 조회")
	public ResultInfo<List<EventYData>> yCollaboList(EventReq paramObj) throws Exception
	{
		SessionContractInfo cntrInfo = new SessionContractInfo();
		cntrInfo.setCntrNo("541638856");
		cntrInfo.setOrgUserNm("홍길동");
		cntrInfo.setMobileNo("01050731322");
		paramObj.setEvtTp("G0006");
		
		ResultInfo<List<EventYData>> resultList = new ResultInfo<>();
		resultList.setResultData(cmsService.getCollaboEventList(paramObj, cntrInfo));
		
		return resultList;
	}*/
	
	/*@RequestMapping(value = "/na/yCollaboInfo", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="Y콜라보 이벤트 정보 조회")
	public ResultInfo yCollaboInfo(EventReq paramObj) throws Exception
	{
		SessionContractInfo cntrInfo = new SessionContractInfo();
		cntrInfo.setCntrNo("541638856");
		cntrInfo.setOrgUserNm("홍길동");
		cntrInfo.setMobileNo("01050731322");
		
		return new ResultInfo<>(cmsService.getCollaboEventInfo(paramObj, cntrInfo));
	}*/
	
	/*@RequestMapping(value = "/na/yCollaboAppl", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo yCollaboInsert(EventAppl appl) throws Exception
	{
		String msg = "";
		
		String modYn = appl.getModYn();	//응모정보 수정여부
		
		List<EventAppl> evtApplList = cmsService.getEventApplList(appl.getEvtSeq(), appl.getCntrNo());
		if(YappUtil.isEq("Y", modYn)) {	//수정
			if ( YappUtil.isEmpty(evtApplList) ){
				throw new YappException("CHECK_MSG", cmnService.getMsg("ERR_NOT_EXST_APPL_EVENT"));		// 응모한 내역이 없습니다.
			}
		}else {	//등록
			if ( YappUtil.isNotEmpty(evtApplList) ){
				throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_EXST_APPL_EVENT"));	// 이미 응모한 이벤트입니다.
			}
		}
		
		EventReq eventRep = new EventReq();
		eventRep.setCntrNo(appl.getCntrNo());
		eventRep.setEvtSeq(appl.getEvtSeq());
		//List<Event> targetEventList = cmsService.getEventList(eventRep);
		List<EventYData> targetEventList = cmsService.getCollaboEventList(eventRep, null);
		if ( YappUtil.isNotEmpty(targetEventList) ){
			if(targetEventList.get(0).getEndYn().equals("Y")){
				throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_CLOSE_EVENT"));	// 종료된 이벤트 입니다.
			}
		}else{
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_NO_EVENT")); //해당하는 이벤트가 존재하지 않습니다. 다시 시도해주세요.
		}
		
		// 이벤트 응모
		if(YappUtil.isEq("Y", modYn)) {	//수정
			cmsService.updateApplCollaboEvent(appl);
			msg = cmnService.getMsg("MSG_APPL_EVENT_EDIT");
		}else {
			cmsService.applCollaboEvent(appl);
			msg = cmnService.getMsg("MSG_APPL_EVENT");
		}
		
		return new ResultInfo<String>(msg);
	}*/
	
	/*@ApiOperation(value="사용자 review 정보 조회")
	@RequestMapping(value = "/na/reviewinfo", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo<ReviewInfo> getReviewInfo(@ApiParam(value="사용자 cntrNo") @RequestParam String cntrNo, HttpServletRequest req) throws Exception
	{
		ReviewInfo reviewReq = new ReviewInfo();
		reviewReq.setCntrNo(cntrNo);
		
		return new ResultInfo<ReviewInfo>(userService.getUserReviewInfo(reviewReq));
	}*/
	
	/*@ApiOperation(value="사용자 review 정보 업데이트")
	@RequestMapping(value = "/na/reviewupdate", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo<String> setReviewInfo(@ApiParam(value="사용자 cntrNo") @RequestParam String cntrNo, @ApiParam(value="사용자 review 응답") @RequestParam String reviewYn, HttpServletRequest req) throws Exception
	{
		ResultInfo<String> resultInfo = new ResultInfo<String>();
		ReviewInfo reviewReq = new ReviewInfo();
		reviewReq.setCntrNo(cntrNo);
		reviewReq.setReviewYn(reviewYn);
		
		userService.setUserReviewInfo(reviewReq);
		
		return resultInfo;
	}*/
	
	/*@RequestMapping(value = "/na/call235", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo callFn235(String mobileNo, HttpServletRequest req) throws Exception
	{
		return new ResultInfo<>(shubService.callFn235(null, mobileNo));
	}*/

	/*@RequestMapping(value = "/na/base1", method = RequestMethod.PUT)
	@ResponseBody
	public ResultInfo base64enc(String str, HttpServletRequest req) throws Exception
	{
		
		YappUtil.lpad(paramObj.get("authSmsSeq"), 13, "0")
		
		logger.info("========================================================");
		logger.info("STR: "+str);
		String lpadStr = YappUtil.lpad(str, 16, "0");
		logger.info("LPADSTR: "+lpadStr);
		String encStr = YappUtil.encode(lpadStr);
		logger.info("ENC : "+encStr);
		logger.info("========================================================");
		return new ResultInfo<>(encStr);
	}*/

	/*@RequestMapping(value = "/na/base2", method = RequestMethod.PUT)
	@ResponseBody
	public ResultInfo base64dec(String str, HttpServletRequest req) throws Exception
	{
		
		YappUtil.lpad(paramObj.get("authSmsSeq"), 13, "0")
		
		logger.info("========================================================");
		logger.info("STR: "+str);
		String decStr = YappUtil.decode(str);
		logger.info("DEC : "+decStr);
		logger.info("========================================================");
		return new ResultInfo<>(decStr);
	}*/

	/*@RequestMapping(value = "/na/setInfo", method = RequestMethod.PUT)
	@ResponseBody
	public ResultInfo aes128enc(String str, String etc, HttpServletRequest req) throws Exception
	{
		logger.info("========================================================");
		logger.info("STR: "+str);
		logger.info("IV : "+etc);
		logger.info("========================================================");
		//String reStr  = appEncryptUtils.aesEnc128(str, etc);
		String reStr = "";
		logger.info("RESTR : "+reStr);
		logger.info("========================================================");
		return new ResultInfo<>(reStr);
	}

	@RequestMapping(value = "/na/getInfo", method = RequestMethod.PUT)
	@ResponseBody
	public ResultInfo aes128dec(String str, String etc, HttpServletRequest req) throws Exception
	{
		String decStr  = appEncryptUtils.aesDec128(str, etc);
		return new ResultInfo<>(decStr);
	}
	
	@RequestMapping(value = "/na/callFn184", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo callFn184a(String cntrNo) throws Exception
	{
		return new ResultInfo<>(shubService.callFn184a(cntrNo));
	}
	
	@RequestMapping(value = "/na/callYRoaming", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo callYRoaming(String cntrNo, String ppCatL) throws Exception
	{
		SessionKeeper.VAL_SERVER_IP = "10.217.41.182";
		return new ResultInfo<>(yrmService.getRoaming(cntrNo, ppCatL));
	}
	
	@RequestMapping(value = "/na/getCalDay", method = RequestMethod.POST)
	@ResponseBody
	public void getCalMonth(String validYMD) throws Exception
	{
		Calendar curCal = Calendar.getInstance();
		curCal.setTime(YappUtil.getDate("yyyyMMddHHmm", validYMD));
		curCal.add(Calendar.DATE, 60);
		
		String curDate = YappUtil.getCurDate("yyyyMMddHHmmss");
		String srchYm = YappUtil.getCurDate(curCal.getTime(), "yyyyMMddHHmmss");
		
		Calendar curCal2 = Calendar.getInstance();
		curCal2.setTime(YappUtil.getDate("yyyyMMddHHmm", validYMD));
		curCal2.add(Calendar.DATE, 5);
		String srchYm2 = YappUtil.getCurDate(curCal2.getTime(), "yyyyMMddHHmmss");
		logger.info("========================");
		logger.info("validYMD    :     "+validYMD);
		logger.info("curDate    :     "+curDate);
		logger.info("srchYm    :     "+srchYm);
		logger.info("srchYm2    :     "+srchYm2);
		logger.info("========================");

	}
	
	@RequestMapping(value = "/na/redirect", method = RequestMethod.GET)
	@ResponseBody
	public String auth(HttpServletRequest req) throws Exception
	{
		
		return "redirect:ollehook://?authResult=1&&name=&number=&msg=";
	}
	
	@RequestMapping(value = "/na/auth2", method = RequestMethod.GET)
	public String auth2(HttpServletRequest req) throws Exception
	{
		
		return "redirect:https://www.kmcert.com/";
	}*/
	
}
