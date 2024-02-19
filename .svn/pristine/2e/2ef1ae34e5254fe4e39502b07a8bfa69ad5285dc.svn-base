package com.kt.yapp.soap.param;

import javax.xml.soap.SOAPMessage;

import com.kt.yapp.soap.SoapConnUtil;
import com.kt.yapp.soap.response.SoapResponseSaveApdDataPrvd;

import lombok.Getter;
import lombok.Setter;

/**
 * 보너스(프로모션) 데이터 수령
 */
public class SoapParamSaveApdDataPrvd 
{
	@Getter @Setter	/** 휴대폰번호 */
	private String mobileNo;
	
	@Getter @Setter	/** 데이터양 */
	private int dataAmt;
	
	@Getter @Setter	/** 데이터 입출력 타입 (S : 프로모션, B : 보너스) */
	private String dataTp;
	
	@Getter @Setter	/** 데이터 입출력 타입 (1 : 당월 만료, 그외 : 익월 만료) */
	private String expireTerm;
	
	private SoapConnUtil soapConnUtil;
	public SoapParamSaveApdDataPrvd(SoapConnUtil soapConnUtil) {
		this.soapConnUtil = soapConnUtil;
	}
	
	/**
	 * KOS 메소드를 호출한다.<br>
	 * 결과 반환값을 반환객체로 파싱하여 리턴한다.
	 */
	public SoapResponseSaveApdDataPrvd execute() throws Exception
	{
		String [][] params = new String[][] {
			{"tgtSvcNo", mobileNo}
			, {"dataCpct", String.valueOf(dataAmt)}
			, {"databoxIosCd", dataTp}
			, {"expireTerm", expireTerm}
		};
		
		SOAPMessage soapMessage = soapConnUtil.makeSoapMessageForKos("saveApdDataPrvd", params);
		SOAPMessage response = SoapConnUtil.call(soapConnUtil.soapEpUrlKosDynamicSvc, soapMessage);
		
		return new SoapResponseSaveApdDataPrvd(response);
	}
}
