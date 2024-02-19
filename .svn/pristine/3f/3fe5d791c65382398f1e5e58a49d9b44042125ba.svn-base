package com.kt.yapp.soap.param;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPMessage;

import com.kt.yapp.soap.SoapConnUtil;
import com.kt.yapp.soap.response.SoapResponse188;

import lombok.Getter;
import lombok.Setter;

/**
 * 토큰 ID를 이용하여 인증 정보 획득<br>
 * (OIF_188)
 */
public class SoapParam188 
{
	@Getter
	private String elementKey = "getAuthInfoByTokenId";
	@Getter @Setter
	private String tokenId;
	
	private SoapConnUtil soapConnUtil;
	public SoapParam188(SoapConnUtil soapConnUtil) {
		this.soapConnUtil = soapConnUtil;
	}
	
	/**
	 * SHUB 인증 메소드를 호출한다.<br>
	 * 결과 반환값을 반환객체로 파싱하여 리턴한다.
	 */
	public SoapResponse188 execute() throws Exception
	{
		SOAPMessage soapMessage = soapConnUtil.makeSoapMessage();
		
		SOAPBody soapBody = soapMessage.getSOAPPart().getEnvelope().getBody();
		SOAPElement soapBodyElem = soapBody.addChildElement(elementKey, SoapConnUtil.NAMESPACE_MAIN);
		
		soapConnUtil.makeParam(soapBodyElem, SoapConnUtil.NAMESPACE_MAIN, "token_id", tokenId);
		soapConnUtil.makeParam(soapBodyElem, SoapConnUtil.NAMESPACE_MAIN, "action_type", "0");	// 올레 서비스 계약

		SOAPMessage response = SoapConnUtil.call(soapConnUtil.soapEpUrlAuth, soapMessage);
		
		return new SoapResponse188(response);
	}
}
