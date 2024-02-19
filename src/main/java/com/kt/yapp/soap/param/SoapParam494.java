package com.kt.yapp.soap.param;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPMessage;

import com.kt.yapp.soap.SoapConnUtil;
import com.kt.yapp.soap.response.SoapResponse494;

import lombok.Getter;
import lombok.Setter;

/**
 * SHUB 인증 호출을 위한 파라미터 클래스<br>
 * (OIF_494)
 */
public class SoapParam494 
{
	@Getter
	private String elementKey = "couponSearch";
	@Getter @Setter
	private String cntrNo;
	@Getter @Setter
	private String mobileNo;
	@Getter @Setter
	private String pkgTpCd;
	
	private SoapConnUtil soapConnUtil;
	public SoapParam494(SoapConnUtil soapConnUtil) {
		this.soapConnUtil = soapConnUtil;
	}
	
	/**
	 * SHUB 인증 메소드를 호출한다.<br>
	 * 결과 반환값을 반환객체로 파싱하여 리턴한다.
	 */
	public SoapResponse494 execute() throws Exception
	{
		SOAPMessage soapMessage = soapConnUtil.makeSoapMessage();
		
		SOAPBody soapBody = soapMessage.getSOAPPart().getEnvelope().getBody();
		SOAPElement soapBodyElem = soapBody.addChildElement(elementKey, SoapConnUtil.NAMESPACE_MAIN);
		
		soapConnUtil.makeParam(soapBodyElem, SoapConnUtil.NAMESPACE_MAIN, "svcchannelcode", "33");		// 채널 33 = YAPP
		soapConnUtil.makeParam(soapBodyElem, SoapConnUtil.NAMESPACE_MAIN, "reqsearchtypecode", "01");	// 고객ID 로 검색
		soapConnUtil.makeParam(soapBodyElem, SoapConnUtil.NAMESPACE_MAIN, "svctypecode", "MB");			// 모바일
		soapConnUtil.makeParam(soapBodyElem, SoapConnUtil.NAMESPACE_MAIN, "contractid", cntrNo);		// 계약번호
		soapConnUtil.makeParam(soapBodyElem, SoapConnUtil.NAMESPACE_MAIN, "reqid", mobileNo);			// CTN (495의 cust id와 동일)

		SOAPMessage response = SoapConnUtil.call(soapConnUtil.soapEpUrlCpn, soapMessage);
		
		return new SoapResponse494(response);
	}
}
