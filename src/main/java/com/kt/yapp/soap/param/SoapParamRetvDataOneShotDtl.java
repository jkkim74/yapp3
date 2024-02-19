package com.kt.yapp.soap.param;

import javax.xml.soap.SOAPMessage;

import com.kt.yapp.soap.SoapConnUtil;
import com.kt.yapp.soap.response.SoapResponseRetrieveDrctlyUseQntDtl;
import com.kt.yapp.soap.response.SoapResponseRetvDataOneShotDtl;

import lombok.Getter;
import lombok.Setter;

/**
 * KOS 데이턱한턱쏘기 상세조회
 */
public class SoapParamRetvDataOneShotDtl
{
	@Getter @Setter	/** 데이터박스ID */
	private String dboxId;
	@Getter @Setter	/** 데이턱ID */
	private String datukId;
	@Getter @Setter	/** 유효시작일 */
	private String validStYmd;
	
	private SoapConnUtil soapConnUtil;
	public SoapParamRetvDataOneShotDtl(SoapConnUtil soapConnUtil) {
		this.soapConnUtil = soapConnUtil;
	}
	
	/**
	 * KOS 메소드를 호출한다.<br>
	 * 결과 반환값을 반환객체로 파싱하여 리턴한다.
	 */
	public SoapResponseRetvDataOneShotDtl execute() throws Exception
	{
		String [][] params = new String[][] {
			{"databoxId", dboxId}
			, {"databoxToknNo", datukId}
			, {"efctStDt", validStYmd}
		};
		
		SOAPMessage soapMessage = soapConnUtil.makeSoapMessageForKos("retvDataOneShotDtl", params);
		SOAPMessage response = SoapConnUtil.call(soapConnUtil.soapEpUrlKosDynamicSvc, soapMessage);
		
		return new SoapResponseRetvDataOneShotDtl(response);
	}
}
