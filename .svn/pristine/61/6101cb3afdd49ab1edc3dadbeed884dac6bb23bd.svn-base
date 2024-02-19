package com.kt.yapp.web;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.icert.comm.secu.IcertSecuManager;
import com.kt.yapp.domain.KmcAuthInfo;
import com.kt.yapp.service.CommonService;
import com.kt.yapp.util.AppEncryptUtils;
import com.kt.yapp.util.YappUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "KMC auth 인증")
@Controller
public class KmcAuthController 
{
	private static final Logger logger = LoggerFactory.getLogger(KmcAuthController.class);

	@Autowired
	private CommonService cmnService;
	@Value("${kmc.url.code}")
	public String kmcURLCode;
	@Value("${kmc.target.url}")
	public String kmcTargetUrl;
	@Autowired
	private AppEncryptUtils appEncryptUtils;

	@ApiOperation(value = "KMC 본인인증")
	@RequestMapping(value = "/na/kmc/auth", method = RequestMethod.GET)
	public String kmcisAuth(Model model, HttpServletRequest req) throws Exception {

		//tr_cert 데이터 변수 선언 ---------------------------------------------------------------
		Calendar curCal = Calendar.getInstance();
		String srchYm = YappUtil.getCurDate(curCal.getTime(), "yyyyMMddHHmmss");

		String tr_cert			= "";
		String cpId				= "KBZM1029";				// 회원사ID
		String urlCode			= kmcURLCode;				// URL코드
		String certNum			= YappUtil.getUUIDStr();	// 요청번호
		String date				= srchYm;					// 요청일시
		String certMet			= "M";						// 본인인증방법
		String name				= "";						// 성명
		String phoneNo			= "";						// 휴대폰번호
		String phoneCorp		= "";						// 이동통신사
		String birthDay			= "";						// 생년월일
		String gender			= "";						// 성별
		String nation			= "";						// 내외국인 구분
		String plusInfo			= "";
		String extendVar		= "0000000000000000";		// 확장변수

		IcertSecuManager seed  = new IcertSecuManager();

		//02. 1차 암호화 (tr_cert 데이터변수 조합 후 암호화)
		String enc_tr_cert	= "";
		tr_cert				= cpId +"/"+ urlCode +"/"+ certNum +"/"+ date +"/"+ certMet +"/"+ birthDay +"/"+ gender +"/"+ name +"/"+ phoneNo +"/"+ phoneCorp +"/"+ nation +"/"+ plusInfo +"/"+ extendVar;
		enc_tr_cert			= seed.getEnc(tr_cert, "");

		//03. 1차 암호화 데이터에 대한 위변조 검증값 생성 (HMAC)
		String hmacMsg = "";
		hmacMsg = seed.getMsg(enc_tr_cert);

		//04. 2차 암호화 (1차 암호화 데이터, HMAC 데이터, extendVar 조합 후 암호화)
		tr_cert  = seed.getEnc(enc_tr_cert + "/" + hmacMsg + "/" + extendVar, "");

		String tr_url = kmcTargetUrl;
		String tr_add = "N";

		model.addAttribute("tr_cert", tr_cert);
		model.addAttribute("tr_url", tr_url);
		model.addAttribute("tr_add", tr_add);
		
		String plusUrl = "?tr_cert=" + tr_cert + "&tr_url=" + tr_url +"&tr_add=" + tr_add;
		return "redirect:https://www.kmcert.com/kmcis/web/kmcisReq.jsp" + plusUrl;
	}

	
	@ApiOperation(value = "본인인증")
	@RequestMapping(value = "/na/kmc/auth/ok", method = RequestMethod.GET)
	public String kmcisAuthOk(Model model
					, @RequestParam(name="rec_cert", required = false) String rec_cert
					, @RequestParam(name="certNum", required = false) String certNum
						) throws Exception {
		IcertSecuManager seed  = new IcertSecuManager();

		String date						= "";			// 요청일시
		String CI						= "";			// 연계정보(CI)
		String DI						= "";			// 중복가입확인정보(DI)
		String phoneNo					= "";			// 휴대폰번호
		String phoneCorp				= "";			// 이동통신사
		String birthDay					= "";			// 생년월일
		String gender					= "";			// 성별
		String nation					= "";			// 내국인
		String name						= "";			// 성명
		String M_name					= "";			// 미성년자 성명
		String M_birthDay				= "";			// 미성년자 생년월일
		String M_Gender					= "";			// 미성년자 성별
		String M_nation					= "";			// 미성년자 내외국인
		String result					= "";			// 결과값
		String certMet					= "";			// 인증방법
		String ip						= "";			// ip주소
		String plusInfo					= "";
		String encPara					= "";
		String encMsg1					= ""; 
		String encMsg2					= "";
		String msgChk					= "";
		String resCertNum				= "";
		String returnMsg				= "";			// 결과 메시지

		//복호화
		String decRecCert = seed.getDec(rec_cert, certNum);

		//03. 1차 파싱
		int inf1 = decRecCert.indexOf("/",0);
		int inf2 = decRecCert.indexOf("/",inf1+1);

		encPara  = decRecCert.substring(0,inf1);			//암호화된 통합 파라미터
		encMsg1  = decRecCert.substring(inf1+1,inf2);		//암호화된 통합 파라미터의 Hash값

		//04. 위변조 검증
		encMsg2  = seed.getMsg(encPara);

		if(encMsg2.equals(encMsg1)){
			msgChk="Y";
		}

		if(msgChk.equals("N")){
			//비정상접근 오류 메시지
		}

		//05. 2차 복호화
		decRecCert  = seed.getDec(encPara, certNum);

		// 06. 2차 파싱
		int info1 = decRecCert.indexOf("/", 0);
		int info2 = decRecCert.indexOf("/", info1 + 1);
		int info3 = decRecCert.indexOf("/", info2 + 1);
		int info4 = decRecCert.indexOf("/", info3 + 1);
		int info5 = decRecCert.indexOf("/", info4 + 1);
		int info6 = decRecCert.indexOf("/", info5 + 1);
		int info7 = decRecCert.indexOf("/", info6 + 1);
		int info8 = decRecCert.indexOf("/", info7 + 1);
		int info9 = decRecCert.indexOf("/", info8 + 1);
		int info10 = decRecCert.indexOf("/", info9 + 1);
		int info11 = decRecCert.indexOf("/", info10 + 1);
		int info12 = decRecCert.indexOf("/", info11 + 1);
		int info13 = decRecCert.indexOf("/", info12 + 1);
		int info14 = decRecCert.indexOf("/", info13 + 1);
		int info15 = decRecCert.indexOf("/", info14 + 1);
		int info16 = decRecCert.indexOf("/", info15 + 1);
		int info17 = decRecCert.indexOf("/", info16 + 1);
		int info18 = decRecCert.indexOf("/", info17 + 1);

		resCertNum = decRecCert.substring(0, info1);
		date = decRecCert.substring(info1 + 1, info2);
		CI = decRecCert.substring(info2 + 1, info3);
		phoneNo = decRecCert.substring(info3 + 1, info4);
		phoneCorp = decRecCert.substring(info4 + 1, info5);
		birthDay = decRecCert.substring(info5 + 1, info6);
		gender = decRecCert.substring(info6 + 1, info7);
		nation = decRecCert.substring(info7 + 1, info8);
		name = decRecCert.substring(info8 + 1, info9);
		result = decRecCert.substring(info9 + 1, info10);
		certMet = decRecCert.substring(info10 + 1, info11);
		ip = decRecCert.substring(info11 + 1, info12);
		M_name = decRecCert.substring(info12 + 1, info13);
		M_birthDay = decRecCert.substring(info13 + 1, info14);
		M_Gender = decRecCert.substring(info14 + 1, info15);
		M_nation = decRecCert.substring(info15 + 1, info16);
		plusInfo = decRecCert.substring(info16 + 1, info17);
		DI = decRecCert.substring(info17 + 1, info18);

		/*
		LOGGER.info("resCertNum :: " + resCertNum);
		LOGGER.info("date :: " + date);
		LOGGER.info("CI :: " + CI);
		LOGGER.info("phoneNo :: " + phoneNo);
		LOGGER.info("phoneCorp :: " + phoneCorp);
		LOGGER.info("birthDay :: " + birthDay);
		LOGGER.info("gender :: " + gender);
		LOGGER.info("nation :: " + nation);
		LOGGER.info("name :: " + name);
		LOGGER.info("result :: " + result);
		LOGGER.info("certMet :: " + certMet);
		LOGGER.info("ip :: " + ip);
		LOGGER.info("M_name :: " + M_name);
		LOGGER.info("M_birthDay :: " + M_birthDay);
		LOGGER.info("M_Gender :: " + M_Gender);
		LOGGER.info("M_nation :: " + M_nation);
		LOGGER.info("plusInfo :: " + plusInfo);
		LOGGER.info("dec plusInfo :: " + AES128Cipher.decodingAEStoCBC(plusInfo));
		LOGGER.info("DI :: " + DI);
		*/
		int kmcAuthSeq = 0;
		try {
			KmcAuthInfo paramObj = new KmcAuthInfo();
			paramObj.setKmcReqDt(date);
			paramObj.setCertMet(certMet);
			paramObj.setPhoneCorp(phoneCorp);
			paramObj.setPhoneNumber(phoneNo);
			paramObj.setResult(result);
			logger.info("paramObj ::::" + paramObj.toString());
			int i = cmnService.insertKmcAuthInfo(paramObj);
			kmcAuthSeq = Integer.valueOf(paramObj.getKmcAuthSeq()).intValue();
		} catch (RuntimeException e) {
			logger.error("KmcAuthInfo 저장 ERROR : "  + e.getMessage());
		} catch (Exception e) {
			logger.error("KmcAuthInfo 저장 ERROR : "  + e.getMessage());
		}
		
		logger.info("kmcAuthSeq :::::" + kmcAuthSeq);
		//07. CI, DI 복호화
		CI	= seed.getDec(CI, certNum);
		DI	= seed.getDec(DI, certNum);

		String regex = "";
		if( certNum != null && (certNum.length() == 0 || certNum.length() > 40)){
			returnMsg = "[KMC] 요청번호가  비정상 입니다.";
			returnMsg = URLEncoder.encode(returnMsg, "UTF-8");
			return "redirect:yboxapp://?authResult=1&&seq=&number=&msg=" + returnMsg;
		}

		regex = "[0-9]*";
		if( date.length() != 14 || !paramChk(regex, date) ){
			returnMsg = "[KMC] 요청일시가 비정상 입니다.";
			returnMsg = URLEncoder.encode(returnMsg, "UTF-8");
			return "redirect:yboxapp://?authResult=1&&seq=&number=&msg=" + returnMsg;
		}

		regex = "[A-Z]*";
		if( certMet.length() != 1 || !paramChk(regex, certMet) ){
			returnMsg = "[KMC] 본인인증방법이 비정상 입니다.";
			returnMsg = URLEncoder.encode(returnMsg, "UTF-8");
			return "redirect:yboxapp://?authResult=1&&seq=&number=&msg=" + returnMsg;
		}

		regex = "[0-9]*";
		if( (phoneNo.length() != 10 && phoneNo.length() != 11) || !paramChk(regex, phoneNo) ){
			returnMsg = "[KMC] 휴대폰번호가 비정상 입니다.";
			returnMsg = URLEncoder.encode(returnMsg, "UTF-8");
			return "redirect:yboxapp://?authResult=1&&seq=&number=&msg=" + returnMsg;
		}

		regex = "[A-Z]*";
		if( phoneCorp.length() != 3 || !paramChk(regex, phoneCorp) ){
			returnMsg = "[KMC] 이동통신사가 비정상 입니다.";
			returnMsg = URLEncoder.encode(returnMsg, "UTF-8");
			return "redirect:yboxapp://?authResult=1&&seq=&number=&msg=" + returnMsg;
		}

		regex = "[0-9]*";
		if( birthDay.length() != 8 || !paramChk(regex, birthDay) ){
			returnMsg = "[KMC] 생년월일이 비정상 입니다.";
			returnMsg = URLEncoder.encode(returnMsg, "UTF-8");
			return "redirect:yboxapp://?authResult=1&&seq=&number=&msg=" + returnMsg;
		}

		regex = "[0-9]*";
		if( gender.length() != 1 || !paramChk(regex, gender) ){
			returnMsg = "[KMC] 성별이 비정상 압니다.";
			returnMsg = URLEncoder.encode(returnMsg, "UTF-8");
			return "redirect:yboxapp://?authResult=1&&seq=&number=&msg=" + returnMsg;
		}

		regex = "[0-9]*";
		if( nation.length() != 1 || !paramChk(regex, nation) ){
			returnMsg = "[KMC] 내/외국인 정보가 비정상 입니다";
			returnMsg = URLEncoder.encode(returnMsg, "UTF-8");
			return "redirect:yboxapp://?authResult=1&&seq=&number=&msg=" + returnMsg;
		}

		regex = "[\\sA-Za-z가-힣.,-]*";
		if( name.length() > 60 || !paramChk(regex, name) ){
			returnMsg = "[KMC] 성명이 비정상 입니다.";
			returnMsg = URLEncoder.encode(returnMsg, "UTF-8");
			return "redirect:yboxapp://?authResult=1&&seq=&number=&msg=" + returnMsg;
		}

		regex = "[A-Z]*";
		if( result.length() != 1 || !paramChk(regex, result) ){
			returnMsg = "[KMC] 결과값 이 비정상 입니다.";
			returnMsg = URLEncoder.encode(returnMsg, "UTF-8");
			return "redirect:yboxapp://?authResult=1&&seq=&number=&msg=" + returnMsg;
		}

		regex = "[\\sA-Za-z가-?.,-]*";
		if( M_name.length() != 0 ){
			if( M_name.length() > 60 || !paramChk(regex, M_name) ){
				returnMsg = "[KMC] 미성년자 성명이 비정상 입니다.";
				returnMsg = URLEncoder.encode(returnMsg, "UTF-8");
				return "redirect:yboxapp://?authResult=1&&seq=&number=&msg=" + returnMsg;
			}
		}

		regex = "[0-9]*";
		if( M_birthDay.length() != 0 ){
			if( M_birthDay.length() != 8 || !paramChk(regex, M_birthDay) ){
				returnMsg = "[KMC] 미성년자 생년월일이 비정상 입니다.";
				returnMsg = URLEncoder.encode(returnMsg, "UTF-8");
				return "redirect:yboxapp://?authResult=1&&seq=&number=&msg=" + returnMsg;
			}
		}

		regex = "[0-9]*";
		if( M_Gender.length() != 0 ){
			if( M_Gender.length() != 1 || !paramChk(regex, M_Gender) ){
				returnMsg = "[KMC] 미성년자 성별이 비정상 입니다.";
				returnMsg = URLEncoder.encode(returnMsg, "UTF-8");
				return "redirect:yboxapp://?authResult=1&&seq=&number=&msg=" + returnMsg;
			 }
		}

		regex = "[0-9]*";
		if( M_nation.length() != 0 ){
			if( M_nation.length() != 1 || !paramChk(regex, M_nation) ){
				returnMsg = "[KMC] 미성년자 내/외국인 이 비정상 입니다.";
				returnMsg = URLEncoder.encode(returnMsg, "UTF-8");
				return "redirect:yboxapp://?authResult=1&&seq=&number=&msg=" + returnMsg;
			}
		}

		// Start - 수신내역 유효성 검증(사설망의 사설 IP로 인해 미사용, 공용망의 경우 확인 후 사용) *********************/
		// 1. date 값 검증
		SimpleDateFormat formatter	= new SimpleDateFormat("yyyyMMddHHmmss",Locale.KOREAN); // 현재 서버 시각 구하기
		String strCurrentTime	= formatter.format(new Date());

		Date toDate				= formatter.parse(strCurrentTime);
		Date fromDate			= formatter.parse(date);
		long timediff			= toDate.getTime()-fromDate.getTime();

		if ( timediff < -30*60*1000 || 30*60*100 < timediff  ){	
			 returnMsg = "[KMC] 비정상적인 접근입니다. (요청시간경과)";
			 returnMsg = URLEncoder.encode(returnMsg, "UTF-8");
		}
		// End - 수신내역 유효성 검증(사설망의 사설 IP로 인해 미사용, 공용망의 경우 확인 후 사용) ***********************/
		
		// SoapResponse139 resp = shubService.callFn139(phoneNo, false);
		
		/** PLAN-420 : 암호화대상 필드에 인코딩하여 단말로 전송, 단말에서 그대로 서버단 재전송 */
		return "redirect:yboxapp://?authResult=0&seq=" + kmcAuthSeq + "&name=" + URLEncoder.encode(name, "UTF-8") + "&birthDay=" + birthDay + "&number=" + URLEncoder.encode(appEncryptUtils.aesEnc(phoneNo), "UTF-8") + "&msg=" + URLEncoder.encode(returnMsg, "UTF-8");
	}

	public Boolean paramChk(String patn, String param) {
		boolean b = true;

		Pattern pattern = Pattern.compile(patn);
		Matcher matcher = pattern.matcher(param);
		b = matcher.matches();
		return b;
	}
}