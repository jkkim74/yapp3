package com.kt.yapp.soap.param;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPMessage;

import com.kt.yapp.soap.SoapConnUtil;
import com.kt.yapp.soap.response.SoapResponse750;
import com.kt.yapp.util.YappUtil;

import lombok.Getter;
import lombok.Setter;

/**
 * SHUB 인증 호출을 위한 파라미터 클래스<br>
 * (OIF_750)
 */
public class SoapParam750 
{
	@Getter
	private String elementKey = "valueAddedServiceManagement";
	
	/** 부가서비스 ID */
	@Getter @Setter
	private String vasId;
	
	/** 사용자 휴대폰 번호 */
	@Getter @Setter
	private String mobileNo;
	
	/** A : 부가서비스가입, C : 부가서비스해지, U : 부가서비스갱신 */
	@Getter @Setter
	private String actionTp;
	
	/** 부가서비스속성명 */
	@Getter @Setter
	private String attrNm;
	
	/** 부가서비스속성값 */
	@Getter @Setter
	private String attrVal;
	
	private SoapConnUtil soapConnUtil;
	public SoapParam750(SoapConnUtil soapConnUtil) {
		this.soapConnUtil = soapConnUtil;
	}
	
	/**
	 * SHUB 인증 메소드를 호출한다.<br>
	 * 결과 반환값을 반환객체로 파싱하여 리턴한다.
	 */
	public SoapResponse750 execute() throws Exception
	{
		SOAPMessage soapMessage = soapConnUtil.makeSoapMessage();
		
		SOAPBody soapBody = soapMessage.getSOAPPart().getEnvelope().getBody();
		SOAPElement soapBodyElem = soapBody.addChildElement(elementKey, SoapConnUtil.NAMESPACE_MAIN);
		
		soapConnUtil.makeParam(soapBodyElem, SoapConnUtil.NAMESPACE_MAIN, "NSTEP_VAS", vasId);
		soapConnUtil.makeParam(soapBodyElem, SoapConnUtil.NAMESPACE_MAIN, "CALL_CTN", mobileNo);
		soapConnUtil.makeParam(soapBodyElem, SoapConnUtil.NAMESPACE_MAIN, "ACTION_TYPE", actionTp);
		if ( YappUtil.isNotEmpty(attrNm) )
			soapConnUtil.makeParam(soapBodyElem, SoapConnUtil.NAMESPACE_MAIN, "ATTRIBUTE_NAME", attrNm);
		
		if ( YappUtil.isNotEmpty(attrVal) )
			soapConnUtil.makeParam(soapBodyElem, SoapConnUtil.NAMESPACE_MAIN, "ATTRIBUTE_VALUE", attrVal);
		
		soapConnUtil.makeParam(soapBodyElem, SoapConnUtil.NAMESPACE_MAIN, "CUSTOMER_TYPE", "K");	// KT고객
		soapConnUtil.makeParam(soapBodyElem, SoapConnUtil.NAMESPACE_MAIN, "BIT_VAS", "000000");		// 더미코드

		SOAPMessage response = SoapConnUtil.call(soapConnUtil.soapEpUrlVas, soapMessage);
		
		return new SoapResponse750(response);
	}
}
