package com.kt.yapp.soap.param;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPMessage;

import com.kt.yapp.soap.SoapConnUtil;
import com.kt.yapp.soap.response.SoapResponse495;

import lombok.Getter;
import lombok.Setter;

/**
 * SHUB 인증 호출을 위한 파라미터 클래스<br>
 * (OIF_495)
 */
public class SoapParam495 
{
	@Getter
	private String elementKey = "couponUse";
	@Getter @Setter
	private String cntrNo;
	@Getter @Setter
	private String userId;
	@Getter @Setter
	private String mobileNo;
	@Getter @Setter
	private String cpnNo;
	
	private SoapConnUtil soapConnUtil;
	public SoapParam495(SoapConnUtil soapConnUtil) {
		this.soapConnUtil = soapConnUtil;
	}
	
	/**
	 * SHUB 인증 메소드를 호출한다.<br>
	 * 결과 반환값을 반환객체로 파싱하여 리턴한다.
	 */
	public SoapResponse495 execute() throws Exception
	{
		SOAPMessage soapMessage = soapConnUtil.makeSoapMessage();
		
		SOAPBody soapBody = soapMessage.getSOAPPart().getEnvelope().getBody();
		SOAPElement soapBodyElem = soapBody.addChildElement(elementKey, SoapConnUtil.NAMESPACE_MAIN);
		
		soapConnUtil.makeParam(soapBodyElem, SoapConnUtil.NAMESPACE_MAIN, "svcchannelcode", "33");		// 채널 33 = YAPP
		soapConnUtil.makeParam(soapBodyElem, SoapConnUtil.NAMESPACE_MAIN, "svctypecode", "MB");			// 모바일
		soapConnUtil.makeParam(soapBodyElem, SoapConnUtil.NAMESPACE_MAIN, "usetypecode", "CRU");		// 사용요청(등록된 쿠폰을 사용)
		soapConnUtil.makeParam(soapBodyElem, SoapConnUtil.NAMESPACE_MAIN, "contractid", cntrNo);
		soapConnUtil.makeParam(soapBodyElem, SoapConnUtil.NAMESPACE_MAIN, "reqid", userId);				// 작업자의 Olleh ID
		soapConnUtil.makeParam(soapBodyElem, SoapConnUtil.NAMESPACE_MAIN, "customerid", mobileNo);		// CTN
		soapConnUtil.makeParam(soapBodyElem, SoapConnUtil.NAMESPACE_MAIN, "serialnum", cpnNo);			// 쿠폰번호

		SOAPMessage response = SoapConnUtil.call(soapConnUtil.soapEpUrlCpn, soapMessage);
		
		return new SoapResponse495(response);
	}
}
