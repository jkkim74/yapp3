package com.kt.yapp.soap.param;

import javax.xml.soap.SOAPMessage;

import com.kt.yapp.soap.SoapConnUtil;
import com.kt.yapp.soap.response.SoapResponseRetrieveDrctlyUseQntDtl;

import lombok.Getter;
import lombok.Setter;

/**
 * KOS 내 데이터 상세 정보
 */
public class SoapParamRetrieveDrctlyUseQntDtl 
{
	@Getter @Setter	/** 계약번호 */
	private String cntrNo;
	@Getter @Setter	/** 휴대폰번호 */
	private String mobileNo;
	
	private SoapConnUtil soapConnUtil;
	public SoapParamRetrieveDrctlyUseQntDtl(SoapConnUtil soapConnUtil) {
		this.soapConnUtil = soapConnUtil;
	}
	
	/**
	 * KOS 메소드를 호출한다.<br>
	 * 결과 반환값을 반환객체로 파싱하여 리턴한다.
	 */
	public SoapResponseRetrieveDrctlyUseQntDtl execute() throws Exception
	{
		SOAPMessage soapMessage = soapConnUtil.makeSoapMessageForKosDtl("retrieveDrctlyUseQntDtl", cntrNo, mobileNo, null, "PALV_T");
		SOAPMessage response = SoapConnUtil.call(soapConnUtil.soapEpUrlKosRetrieveDrctlyUseQnt, soapMessage);
		
		return new SoapResponseRetrieveDrctlyUseQntDtl(response);
	}
}
