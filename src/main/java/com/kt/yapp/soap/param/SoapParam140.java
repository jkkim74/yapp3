package com.kt.yapp.soap.param;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPMessage;

import com.kt.yapp.soap.SoapConnUtil;
import com.kt.yapp.soap.response.SoapResponse107;
import com.kt.yapp.soap.response.SoapResponse140;

import lombok.Getter;
import lombok.Setter;

/**
 * SHUB 인증 호출을 위한 파라미터 클래스<br>
 * (OIF_140)
 */
public class SoapParam140 
{
	@Getter
	private String elementKey = "getOTMSubscriptionInfoAndOrgPhoneRequest";
	@Getter @Setter
	private String crendentialId;
	@Getter @Setter
	private String userName;
	
	private SoapConnUtil soapConnUtil;
	public SoapParam140(SoapConnUtil soapConnUtil) {
		this.soapConnUtil = soapConnUtil;
	}
	
	/**
	 * SHUB 인증 메소드를 호출한다.<br>
	 * 결과 반환값을 반환객체로 파싱하여 리턴한다.
	 */
	public SoapResponse140 execute() throws Exception
	{
		SOAPMessage soapMessage = soapConnUtil.makeSoapMessage();
		
		SOAPBody soapBody = soapMessage.getSOAPPart().getEnvelope().getBody();
		SOAPElement soapBodyElem = soapBody.addChildElement(elementKey, SoapConnUtil.NAMESPACE_MAIN);
		
		soapConnUtil.makeParam(soapBodyElem, SoapConnUtil.NAMESPACE_MAIN, "Credt_Id", crendentialId);
		soapConnUtil.makeParam(soapBodyElem, SoapConnUtil.NAMESPACE_MAIN, "User_Name", userName);

		SOAPMessage response = SoapConnUtil.call(soapConnUtil.soapEpUrlScpt, soapMessage);
		
		return new SoapResponse140(response);
	}
}