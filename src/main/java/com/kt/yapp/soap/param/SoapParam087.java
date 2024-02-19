package com.kt.yapp.soap.param;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPMessage;

import com.kt.yapp.soap.SoapConnUtil;
import com.kt.yapp.soap.response.SoapResponse087;
import com.kt.yapp.soap.response.SoapResponse107;

import lombok.Getter;
import lombok.Setter;

/**
 * SHUB 인증 호출을 위한 파라미터 클래스<br>
 * (OIF_087)
 */
public class SoapParam087 
{
	@Getter
	private static final String elementKey = "retrieveUserProfilebyAuthenticationCodeWithTablet";
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
	@Getter @Setter
	private String authCode;
	@Getter @Setter
	private String randomNo;
	
	private SoapConnUtil soapConnUtil;
	public SoapParam087(SoapConnUtil soapConnUtil) {
		this.soapConnUtil = soapConnUtil;
	}
	
	/**
	 * SHUB 인증 메소드를 호출한다.<br>
	 * 결과 반환값을 반환객체로 파싱하여 리턴한다.
	 */
	public SoapResponse087 execute() throws Exception
	{
		SOAPMessage soapMessage = soapConnUtil.makeSoapMessage();
		
		SOAPBody soapBody = soapMessage.getSOAPPart().getEnvelope().getBody();
		SOAPElement soapBodyElem = soapBody.addChildElement(elementKey, SoapConnUtil.NAMESPACE_MAIN);
		
		soapConnUtil.makeParam(soapBodyElem, SoapConnUtil.NAMESPACE_MAIN, "party_name", partiName);
		soapConnUtil.makeParam(soapBodyElem, SoapConnUtil.NAMESPACE_MAIN, "birth_date", birthDate);
		soapConnUtil.makeParam(soapBodyElem, SoapConnUtil.NAMESPACE_MAIN, "gender", genderCd);
		soapConnUtil.makeParam(soapBodyElem, SoapConnUtil.NAMESPACE_MAIN, "foreigner", foreignerCd);
		soapConnUtil.makeParam(soapBodyElem, SoapConnUtil.NAMESPACE_MAIN, "phone_number", phoneNumber);
		soapConnUtil.makeParam(soapBodyElem, SoapConnUtil.NAMESPACE_MAIN, "auth_code", authCode);
		soapConnUtil.makeParam(soapBodyElem, SoapConnUtil.NAMESPACE_MAIN, "random_no", randomNo);
		soapConnUtil.makeParam(soapBodyElem, SoapConnUtil.NAMESPACE_MAIN, "telco_code", "2");	// KT 고정
		soapConnUtil.makeParam(soapBodyElem, SoapConnUtil.NAMESPACE_MAIN, "action_type", "1");	// 법인도 가능하게 하기위해 고정

		SOAPMessage response = SoapConnUtil.call(soapConnUtil.soapEpUrlAuth, soapMessage);
		
		return new SoapResponse087(response);
	}
}
