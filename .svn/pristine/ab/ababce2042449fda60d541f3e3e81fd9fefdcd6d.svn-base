package com.kt.yapp.soap.param;

import javax.xml.soap.SOAPMessage;

import com.kt.yapp.soap.SoapConnUtil;
import com.kt.yapp.soap.response.SoapResponseRetvDataboxBas;

import lombok.Getter;
import lombok.Setter;

/**
 * KOS 데이터 박스 기본 정보 조회
 */
public class SoapParamRetvDataboxBas 
{
	@Getter @Setter	/** 계약번호 */
	private String cntrNo;
	
	private SoapConnUtil soapConnUtil;
	public SoapParamRetvDataboxBas(SoapConnUtil soapConnUtil) {
		this.soapConnUtil = soapConnUtil;
	}
	
	/**
	 * KOS 메소드를 호출한다.<br>
	 * 결과 반환값을 반환객체로 파싱하여 리턴한다.
	 */
	public SoapResponseRetvDataboxBas execute() throws Exception
	{
		String [][] params = new String[][] {
			{"svcContId", cntrNo}
		};
		
		SOAPMessage soapMessage = soapConnUtil.makeSoapMessageForKos("retvDataboxBas", params);
		SOAPMessage response = SoapConnUtil.call(soapConnUtil.soapEpUrlKosDynamicSvc, soapMessage);
		
		return new SoapResponseRetvDataboxBas(response);
	}
}
