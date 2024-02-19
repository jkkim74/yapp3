package com.kt.yapp.soap.param;

import javax.xml.soap.SOAPMessage;

import com.kt.yapp.soap.SoapConnUtil;
import com.kt.yapp.soap.response.SoapResponseRetvDataboxSbscRstr;

import lombok.Getter;
import lombok.Setter;

/**
 * KOS 데이터 박스 가입제한 조회
 */
public class SoapParamRetvDataboxSbscRstr 
{
	@Getter @Setter	/** 계약번호 */
	private String cntrNo;
	
	private SoapConnUtil soapConnUtil;
	public SoapParamRetvDataboxSbscRstr(SoapConnUtil soapConnUtil) {
		this.soapConnUtil = soapConnUtil;
	}
	
	/**
	 * KOS 메소드를 호출한다.<br>
	 * 결과 반환값을 반환객체로 파싱하여 리턴한다.
	 */
	public SoapResponseRetvDataboxSbscRstr execute() throws Exception
	{
		String [][] params = new String[][] {
			{"svcContId", cntrNo}
		};
		
		SOAPMessage soapMessage = soapConnUtil.makeSoapMessageForKos("retvDataboxSbscRstr", params);
		SOAPMessage response = SoapConnUtil.call(soapConnUtil.soapEpUrlKosDynamicSvc, soapMessage);
		
		return new SoapResponseRetvDataboxSbscRstr(response);
	}
}
