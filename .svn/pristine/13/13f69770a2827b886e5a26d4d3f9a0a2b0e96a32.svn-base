package com.kt.yapp.soap.param;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPMessage;

import com.kt.yapp.soap.SoapConnUtil;
import com.kt.yapp.soap.response.SoapResponse086;
import com.kt.yapp.soap.response.SoapResponse107;

import lombok.Getter;
import lombok.Setter;

/**
 * SHUB 인증 호출을 위한 파라미터 클래스<br>
 * (OIF_086)
 */
public class SoapParam086 
{
	@Getter
	private static final String elementKey = "authenticateBySMSWithTablet";
	@Getter @Setter
	private String partiName;
	@Getter @Setter
	private String birthDate;
	@Getter @Setter
	private String genderCd;
	@Getter @Setter
	private String foreignerCd;
	@Getter @Setter
	private String phoneNumber;
	
	private SoapConnUtil soapConnUtil;
	public SoapParam086(SoapConnUtil soapConnUtil) {
		this.soapConnUtil = soapConnUtil;
	}
	
	/**
	 * SHUB 인증 메소드를 호출한다.<br>
	 * 결과 반환값을 반환객체로 파싱하여 리턴한다.
	 */
	public SoapResponse086 execute() throws Exception
	{
		SOAPMessage soapMessage = soapConnUtil.makeSoapMessage();
		
		SOAPBody soapBody = soapMessage.getSOAPPart().getEnvelope().getBody();
		SOAPElement soapBodyElem = soapBody.addChildElement(elementKey, SoapConnUtil.NAMESPACE_MAIN);
		
		soapConnUtil.makeParam(soapBodyElem, SoapConnUtil.NAMESPACE_MAIN, "party_name", partiName);
		soapConnUtil.makeParam(soapBodyElem, SoapConnUtil.NAMESPACE_MAIN, "birth_date", birthDate);
		soapConnUtil.makeParam(soapBodyElem, SoapConnUtil.NAMESPACE_MAIN, "gender", genderCd);
		soapConnUtil.makeParam(soapBodyElem, SoapConnUtil.NAMESPACE_MAIN, "foreigner", foreignerCd);
		soapConnUtil.makeParam(soapBodyElem, SoapConnUtil.NAMESPACE_MAIN, "phone_number", phoneNumber);
		soapConnUtil.makeParam(soapBodyElem, SoapConnUtil.NAMESPACE_MAIN, "telco_code", "2");	// KT 고정
		soapConnUtil.makeParam(soapBodyElem, SoapConnUtil.NAMESPACE_MAIN, "action_type", "1");	// 법인도 가능하게 하기위해 고정

		SOAPMessage response = SoapConnUtil.call(soapConnUtil.soapEpUrlAuth, soapMessage);
		
		return new SoapResponse086(response);
	}
}
