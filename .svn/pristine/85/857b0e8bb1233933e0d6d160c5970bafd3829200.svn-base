package com.kt.yapp.soap.param;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPMessage;

import com.initech.inisafenet.iniplugin.log.Logger;
import com.kt.yapp.soap.SoapConnUtil;
import com.kt.yapp.soap.response.SoapResponse107;
import com.kt.yapp.soap.response.SoapResponse2118;

import lombok.Getter;
import lombok.Setter;

/**
 * SHUB 인증 호출을 위한 파라미터 클래스<br>
 * (OIF_2118)
 */
public class SoapParam2118 
{
	@Getter
	private String elementKey = "MessageSendSMSReportNoNetCharge";
	@Getter @Setter
	private String msgContents;
	@Getter @Setter
	private String sendMobileNo;
	@Getter @Setter
	private String rcvMobileNo;
	
	private SoapConnUtil soapConnUtil;
	public SoapParam2118(SoapConnUtil soapConnUtil) {
		this.soapConnUtil = soapConnUtil;
	}
	
	/**
	 * SHUB 인증 메소드를 호출한다.<br>
	 * 결과 반환값을 반환객체로 파싱하여 리턴한다.
	 */
	public SoapResponse2118 execute() throws Exception
	{
		SOAPMessage soapMessage = soapConnUtil.makeSoapMessage();
		
		SOAPBody soapBody = soapMessage.getSOAPPart().getEnvelope().getBody();
		SOAPElement soapBodyElem = soapBody.addChildElement(elementKey, SoapConnUtil.NAMESPACE_MAIN);
		
		soapConnUtil.makeParam(soapBodyElem, SoapConnUtil.NAMESPACE_MAIN, "MSG_TYPE", "S001");			// SMS
		soapConnUtil.makeParam(soapBodyElem, SoapConnUtil.NAMESPACE_MAIN, "MSG_CONTENT", msgContents);
		soapConnUtil.makeParam(soapBodyElem, SoapConnUtil.NAMESPACE_MAIN, "CALL_CTN", sendMobileNo);
		soapConnUtil.makeParam(soapBodyElem, SoapConnUtil.NAMESPACE_MAIN, "CALLBACK_CTN", "114");
		soapConnUtil.makeParam(soapBodyElem, SoapConnUtil.NAMESPACE_MAIN, "RCV_CTN", rcvMobileNo);
		soapConnUtil.makeParam(soapBodyElem, SoapConnUtil.NAMESPACE_MAIN, "PFORM_TYPE", "1");		// ME/KUN/유선일 경우
		soapConnUtil.makeParam(soapBodyElem, SoapConnUtil.NAMESPACE_MAIN, "SERVICE_TYPE", "A");		// 무선

		SOAPMessage response = SoapConnUtil.call(soapConnUtil.soapEpUrlMsg, soapMessage);
		
		return new SoapResponse2118(response);
	}
}
