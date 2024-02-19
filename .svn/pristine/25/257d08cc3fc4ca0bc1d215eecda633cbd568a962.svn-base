package com.kt.yapp.soap.param;

import javax.xml.soap.SOAPMessage;

import com.kt.yapp.soap.SoapConnUtil;
import com.kt.yapp.soap.response.SoapResponseCondByModelInfoRetv;

import lombok.Getter;
import lombok.Setter;

/**
 * KOS 단말정보
 */
public class SoapParamCondByModelInfoRetv
{
	@Getter @Setter	/** inqrIndCd */
	private String inqrIndCd;
	@Getter @Setter	/** 단말제품번호 */
	private String intmModelId;
	@Getter @Setter	/** 단말제품명 */
	private String intmModelNm;

	private SoapConnUtil soapConnUtil;
	public SoapParamCondByModelInfoRetv(SoapConnUtil soapConnUtil) {
		this.soapConnUtil = soapConnUtil;
	}
	
	/**
	 * KOS 메소드를 호출한다.<br>
	 * 결과 반환값을 반환객체로 파싱하여 리턴한다.
	 */
	public SoapResponseCondByModelInfoRetv execute() throws Exception
	{
		SOAPMessage soapMessage = soapConnUtil.makeSoapMessageForKosCondByModelInfoRetv("condByModelInfoRetv", inqrIndCd, intmModelNm);
		SOAPMessage response = SoapConnUtil.call(soapConnUtil.soapRdsKosDynamicSvc, soapMessage);
		
		return new SoapResponseCondByModelInfoRetv(response, intmModelNm);
	}
}
