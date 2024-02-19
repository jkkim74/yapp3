package com.kt.yapp.soap.param;

import java.io.ByteArrayOutputStream;

import javax.xml.soap.SOAPMessage;

import com.kt.yapp.soap.SoapConnUtil;
import com.kt.yapp.soap.response.SoapResponseRetvDataOutputRstr;

import lombok.Getter;
import lombok.Setter;

/**
 * KOS 데이터 꺼내기 제약 조회
 */
public class SoapParamRetvDataOutputRstr 
{
	@Getter @Setter	/** 계약번호 */
	private String cntrNo;
	
	@Getter @Setter	/** 데이터양 */
	private int dataAmt;

	private SoapConnUtil soapConnUtil;
	public SoapParamRetvDataOutputRstr(SoapConnUtil soapConnUtil) {
		this.soapConnUtil = soapConnUtil;
	}
	
	/**
	 * KOS 메소드를 호출한다.<br>
	 * 결과 반환값을 반환객체로 파싱하여 리턴한다.
	 */
	public SoapResponseRetvDataOutputRstr execute() throws Exception
	{
		String [][] params = new String[][] {
			{"svcContId", cntrNo}
			, {"dataCpct", String.valueOf(dataAmt)}
		};
		
		SOAPMessage soapMessage = soapConnUtil.makeSoapMessageForKos("retvDataOutputRstr", params);
		SOAPMessage response = SoapConnUtil.call(soapConnUtil.soapEpUrlKosDynamicSvc, soapMessage);
		
		return new SoapResponseRetvDataOutputRstr(response);
	}
}
