package com.kt.yapp.soap.param;

import javax.xml.soap.SOAPMessage;

import com.kt.yapp.soap.SoapConnUtil;
import com.kt.yapp.soap.response.SoapResponseRetvDataboxIosHst;

import lombok.Getter;
import lombok.Setter;

/**
 * KOS 데이터 입출력 이력 조회
 */
public class SoapParamRetvDataboxIosHst 
{
	@Getter @Setter	/** 계약번호 */
	private String cntrNo;
	
	@Getter @Setter	/** 유효시작일 */
	private String validStYmd;
	
	@Getter @Setter	/** 유효종료일 */
	private String validEdYmd;
	
	@Getter @Setter	/** 데이터 입출력 타입 (P: 데이터 선물하기, R: 데이터꺼내기)*/
	private String dataTp;
	
	private SoapConnUtil soapConnUtil;
	public SoapParamRetvDataboxIosHst(SoapConnUtil soapConnUtil) {
		this.soapConnUtil = soapConnUtil;
	}
	
	/**
	 * KOS 메소드를 호출한다.<br>
	 * 결과 반환값을 반환객체로 파싱하여 리턴한다.
	 */
	public SoapResponseRetvDataboxIosHst execute() throws Exception
	{
		String [][] params = new String[][] {
			{"svcContId", cntrNo}
			, {"efctStDt", validStYmd}
			, {"efctFnsDt", validEdYmd}
			, {"databoxIosCd", dataTp}
		};
		
		SOAPMessage soapMessage = soapConnUtil.makeSoapMessageForKos("retvDataboxIosHst", params);
		SOAPMessage response = SoapConnUtil.call(soapConnUtil.soapEpUrlKosDynamicSvc, soapMessage);
		
		return new SoapResponseRetvDataboxIosHst(response);
	}
}
