package com.kt.yapp.soap.param;

import java.util.HashMap;
import java.util.Map;

import javax.xml.soap.SOAPMessage;

import com.kt.yapp.soap.SoapConnUtil;
import com.kt.yapp.soap.response.SoapResponseSaveDataOutput;

import lombok.Getter;
import lombok.Setter;

/**
 * KOS 데이터 꺼내기
 */
public class SoapParamSaveDataOutput 
{
	@Getter @Setter	/** 계약번호 */
	private String mobileNo;
	
	@Getter @Setter	/** 데이터양 */
	private int dataAmt;

	private SoapConnUtil soapConnUtil;
	
	private static Map<Integer, String> DATA_AMT_CD_MAP = new HashMap<Integer, String>() {
		{
			put(100, "BOXR01");
			put(200, "BOXR02");
			put(300, "BOXR03");
			put(400, "BOXR04");
			put(500, "BOXR05");
			put(600, "BOXR06");
			put(700, "BOXR07");
			put(800, "BOXR08");
			put(900, "BOXR09");
			put(1000, "BOXR10");
		}
	};
	
	public SoapParamSaveDataOutput(SoapConnUtil soapConnUtil) {
		this.soapConnUtil = soapConnUtil;
	}
	
	/**
	 * KOS 메소드를 호출한다.<br>
	 * 결과 반환값을 반환객체로 파싱하여 리턴한다.
	 */
	public SoapResponseSaveDataOutput execute() throws Exception
	{
		SOAPMessage soapMessage = soapConnUtil.makeSoapMessageForKosPullData("saveDataOutput", mobileNo, DATA_AMT_CD_MAP.get(dataAmt));
		SOAPMessage response = SoapConnUtil.call(soapConnUtil.soapEpUrlKosSvc, soapMessage);
		
		return new SoapResponseSaveDataOutput(response);
	}
}
