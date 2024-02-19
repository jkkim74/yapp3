package com.kt.yapp.soap.param;

import javax.xml.soap.SOAPMessage;

import com.kt.yapp.soap.SoapConnUtil;
import com.kt.yapp.soap.response.SoapResponseRetrieveDataRefill;

import lombok.Getter;
import lombok.Setter;

/**
 * KOS 충전데이타 잔여량
 */
public class SoapParamRetrieveDataRefill
{
	@Getter @Setter	/** 계약번호 */
	private String cntrNo;
	@Getter @Setter	/** 휴대폰번호 */
	private String mobileNo;

	private SoapConnUtil soapConnUtil;
	public SoapParamRetrieveDataRefill(SoapConnUtil soapConnUtil) {
		this.soapConnUtil = soapConnUtil;
	}
	
	/**
	 * KOS 메소드를 호출한다.<br>
	 * 결과 반환값을 반환객체로 파싱하여 리턴한다.
	 */
	public SoapResponseRetrieveDataRefill execute() throws Exception
	{
		SOAPMessage soapMessage = soapConnUtil.makeSoapMessageForKosDataRefill("retrieveDataRefill", cntrNo, mobileNo);
		SOAPMessage response = SoapConnUtil.call(soapConnUtil.soapEpUrlKosRetrieveDrctlyUseQnt, soapMessage);
		
		return new SoapResponseRetrieveDataRefill(response);
	}
}
