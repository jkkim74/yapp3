package com.kt.yapp.soap.param;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPMessage;

import com.kt.yapp.soap.SoapConnUtil;
import com.kt.yapp.soap.response.SoapResponse235;

import lombok.Getter;
import lombok.Setter;

/**
 * SHUB 호출을 위한 파라미터 클래스<br>
 * (OIF_235)
 */
public class SoapParam235 
{
	@Getter
	private String elementKey = "retrieveSingleSubscpnInfo";
	@Getter @Setter
	private String svcContId;
	@Getter @Setter
	private String userName;
	
	
	private SoapConnUtil soapConnUtil;
	public SoapParam235(SoapConnUtil soapConnUtil) {
		this.soapConnUtil = soapConnUtil;
	}
	
	/**
	 * SHUB 메소드를 호출한다.<br>
	 * 결과 반환값을 반환객체로 파싱하여 리턴한다.
	 */
	public SoapResponse235 execute() throws Exception
	{
		SOAPMessage soapMessage = soapConnUtil.makeSoapMessage();
		
		SOAPBody soapBody = soapMessage.getSOAPPart().getEnvelope().getBody();
		SOAPElement soapBodyElem = soapBody.addChildElement(elementKey, SoapConnUtil.NAMESPACE_MAIN);
		if(svcContId != null && !"".equals(svcContId)){
			soapConnUtil.makeParam(soapBodyElem, SoapConnUtil.NAMESPACE_MAIN, "svc_cont_id", svcContId);
		}
		soapConnUtil.makeParam(soapBodyElem, SoapConnUtil.NAMESPACE_MAIN, "user_name", userName);
		SOAPMessage response = SoapConnUtil.call(soapConnUtil.soapEpUrlScpt, soapMessage);
		
		return new SoapResponse235(response);
	}
}
