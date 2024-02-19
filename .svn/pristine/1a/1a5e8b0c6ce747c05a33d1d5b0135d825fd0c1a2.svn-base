package com.kt.yapp.soap.param;

import javax.xml.soap.SOAPMessage;

import com.kt.yapp.soap.SoapConnUtil;
import com.kt.yapp.soap.response.SoapResponseSvcHstByCustIdRetv;

import lombok.Getter;
import lombok.Setter;

/**
 * KOS 단말정보
 */
public class SoapParamSvcHstByCustIdRetv
{
	@Getter @Setter	/** 계약번호 */
	private String cntrNo;

	private SoapConnUtil soapConnUtil;
	public SoapParamSvcHstByCustIdRetv(SoapConnUtil soapConnUtil) {
		this.soapConnUtil = soapConnUtil;
	}
	
	/**
	 * KOS 메소드를 호출한다.<br>
	 * 결과 반환값을 반환객체로 파싱하여 리턴한다.
	 */
	public SoapResponseSvcHstByCustIdRetv execute() throws Exception
	{
		SOAPMessage soapMessage = soapConnUtil.makeSoapMessageForKosSvcHstByCustIdRetv("svcHstByCustIdRetv", cntrNo);
		SOAPMessage response = SoapConnUtil.call(soapConnUtil.soapEpUrlKosDataSharUseRetv2, soapMessage);
		
		return new SoapResponseSvcHstByCustIdRetv(response);
	}
}
