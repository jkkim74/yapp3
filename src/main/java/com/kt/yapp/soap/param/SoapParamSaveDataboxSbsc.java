package com.kt.yapp.soap.param;

import javax.xml.soap.SOAPMessage;

import com.kt.yapp.soap.SoapConnUtil;
import com.kt.yapp.soap.response.SoapResponseSaveDataboxSbsc;

import lombok.Getter;
import lombok.Setter;

/**
 * KOS 데이터 박스 생성
 */
public class SoapParamSaveDataboxSbsc 
{
	@Getter @Setter	/** 계약번호 */
	private String cntrNo;
	
	private SoapConnUtil soapConnUtil;
	public SoapParamSaveDataboxSbsc(SoapConnUtil soapConnUtil) {
		this.soapConnUtil = soapConnUtil;
	}
	
	/**
	 * KOS 메소드를 호출한다.<br>
	 * 결과 반환값을 반환객체로 파싱하여 리턴한다.
	 */
	public SoapResponseSaveDataboxSbsc execute() throws Exception
	{
		String [][] params = new String[][] {
			{"svcContId", cntrNo}
		};
		
		SOAPMessage soapMessage = soapConnUtil.makeSoapMessageForKos("saveDataboxSbsc", params);
		SOAPMessage response = SoapConnUtil.call(soapConnUtil.soapEpUrlKosDynamicSvc, soapMessage);
		
		return new SoapResponseSaveDataboxSbsc(response);
	}
}
