package com.kt.yapp.soap.param;

import javax.xml.soap.SOAPMessage;

import com.kt.yapp.soap.SoapConnUtil;
import com.kt.yapp.soap.response.SoapResponseSaveDataOneShot;

import lombok.Getter;
import lombok.Setter;

/**
 * KOS 데이턱 생성
 */
public class SoapParamSaveDataOneShot 
{
	@Getter @Setter	/** 계약번호 */
	private String cntrNo;
	
	@Getter @Setter	/** 데이터양 */
	private int dataAmt;
	
	@Getter @Setter	/** 데이턱ID */
	private String datukId;
	
	private SoapConnUtil soapConnUtil;
	public SoapParamSaveDataOneShot(SoapConnUtil soapConnUtil) {
		this.soapConnUtil = soapConnUtil;
	}
	
	/**
	 * KOS 메소드를 호출한다.<br>
	 * 결과 반환값을 반환객체로 파싱하여 리턴한다.
	 */
	public SoapResponseSaveDataOneShot execute() throws Exception
	{
		String [][] params = new String[][] {
			{"svcContId", cntrNo}
			, {"dataCpct", String.valueOf(dataAmt)}
			, {"databoxToknNo", datukId}
		};
		
		SOAPMessage soapMessage = soapConnUtil.makeSoapMessageForKos("saveDataOneShot", params);
		SOAPMessage response = SoapConnUtil.call(soapConnUtil.soapEpUrlKosDynamicSvc, soapMessage);
		
		return new SoapResponseSaveDataOneShot(response);
	}
}
