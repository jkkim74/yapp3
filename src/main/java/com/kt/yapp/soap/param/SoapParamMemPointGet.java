package com.kt.yapp.soap.param;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPMessage;

import com.kt.yapp.soap.SoapConnUtil;
import com.kt.yapp.soap.response.SoapResponse086;
import com.kt.yapp.soap.response.SoapResponseMemPointGet;

import lombok.Getter;
import lombok.Setter;

/**
 * 포인트 조회 호출을 위한 파라미터 클래스<br>
 */
public class SoapParamMemPointGet 
{
	@Getter
	private static final String elementKey = "selectCustCombineInfoV2";
	@Getter @Setter
	private String memType;
	@Getter @Setter
	private String cntrNo;
	
	private SoapConnUtil soapConnUtil;
	public SoapParamMemPointGet(SoapConnUtil soapConnUtil) {
		this.soapConnUtil = soapConnUtil;
	}
	
	/**
	 * SHUB 인증 메소드를 호출한다.<br>
	 * 결과 반환값을 반환객체로 파싱하여 리턴한다.
	 */
	public SoapResponseMemPointGet execute() throws Exception
	{
		SOAPMessage soapMessage = soapConnUtil.makeSoapMessageForMemPoint(SoapConnUtil.NAMESPACE_PGET, SoapConnUtil.NAMESPACE_URI_PGET);
		
		SOAPBody soapBody = soapMessage.getSOAPPart().getEnvelope().getBody();
		SOAPElement soapBodyElem = soapBody.addChildElement("selectCustCombineInfoV2", SoapConnUtil.NAMESPACE_PGET);
		
		SOAPElement requestBodyElem = soapBodyElem.addChildElement("requestBody");

		soapConnUtil.makeParam(requestBodyElem, null, "COORP_CO_CD", "DATBX");
		soapConnUtil.makeParam(requestBodyElem, null, "MEMBER_TYPE", memType);	// 암호화
		soapConnUtil.makeParam(requestBodyElem, null, "MEMBER_ID", cntrNo);		// 암호화

		SOAPMessage response = SoapConnUtil.call(soapConnUtil.soapEpUrlMemPointGet, soapMessage);
		
		return new SoapResponseMemPointGet(response);
	}
}
