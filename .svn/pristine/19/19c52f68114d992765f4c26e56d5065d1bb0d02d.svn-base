package com.kt.yapp.soap.param;

import javax.xml.soap.SOAPMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kt.yapp.domain.AplyRoaming;
import com.kt.yapp.soap.SoapConnUtil;
import com.kt.yapp.soap.response.SoapResponseProdSbscTrmnStoreTrtPreCheck;

import lombok.Getter;
import lombok.Setter;

/**
 * (사전 체크)KOS 부가서비스 변경
 */
public class SoapParamProdSbscTrmnStoreTrtPreCheck 
{
	private static final Logger logger = LoggerFactory.getLogger(SoapParamProdSbscTrmnStoreTrtPreCheck.class);
	
	@Getter @Setter /** 가입상품정보조회 목록 */
	private AplyRoaming aplyRoamingInfo = new AplyRoaming();
	
	@Getter @Setter /** 변경시간옵션 */
	private String timeOption = "";
	
	private SoapConnUtil soapConnUtil;
	public SoapParamProdSbscTrmnStoreTrtPreCheck (SoapConnUtil soapConnUtil) {
		this.soapConnUtil = soapConnUtil;
	}
	
	/**
	 * (사전 체크)KOS 메소드를 호출한다.<br>
	 * 결과 반환값을 반환객체로 파싱하여 리턴한다.
	 */
	public SoapResponseProdSbscTrmnStoreTrtPreCheck execute() throws Exception
	{
		logger.info("(사전 체크)KOS 메소드를 호출 START ============================");
		logger.info("(사전 체크)END POINT : "+soapConnUtil.soapEpUrlKosDynamicSvc);
		
		SOAPMessage soapMessage = soapConnUtil.makeSoapMessageForKosProdSbscTrmnStoreTrtPreCheck("mobileProdPreChk", aplyRoamingInfo);
		SOAPMessage response = SoapConnUtil.call(soapConnUtil.soapEpUrlKosDynamicSvc, soapMessage);
		
		logger.info("(사전 체크)KOS 메소드를 호출 END ============================");
		return new SoapResponseProdSbscTrmnStoreTrtPreCheck(response);
	}
}
