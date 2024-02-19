package com.kt.yapp.soap.param;

import javax.xml.soap.SOAPMessage;

import com.kt.yapp.soap.SoapConnUtil;
import com.kt.yapp.soap.response.SoapResponseSaveDataGift;

import lombok.Getter;
import lombok.Setter;

/**
 * KOS 선물하기
 */
public class SoapParamSaveDataGift 
{
	@Getter @Setter	/** 휴대폰 번호 */
	private String mobileNo;
	
	@Getter @Setter	/** 수신 휴대폰 번호 */
	private String rcvMobileNo;
	
	@Getter @Setter	/** 데이터양 */
	private int dataAmt;
	
	@Getter @Setter	/** 데이터 입출력 타입 */
	private String dataTp;
	
	private SoapConnUtil soapConnUtil;
	public SoapParamSaveDataGift(SoapConnUtil soapConnUtil) {
		this.soapConnUtil = soapConnUtil;
	}
	
	/**
	 * KOS 메소드를 호출한다.<br>
	 * 결과 반환값을 반환객체로 파싱하여 리턴한다.
	 */
	public SoapResponseSaveDataGift execute() throws Exception
	{
		String [][] params = new String[][] {
			{"svcNo"		, mobileNo}
			, {"tgtSvcNo"	, rcvMobileNo}
			, {"dataCpct"	, String.valueOf(dataAmt)}
		};
		
		SOAPMessage soapMessage = soapConnUtil.makeSoapMessageForKos("saveDataGift", params);
		SOAPMessage response = SoapConnUtil.call(soapConnUtil.soapEpUrlKosDynamicSvc, soapMessage);
		
		return new SoapResponseSaveDataGift(response);
	}
}
