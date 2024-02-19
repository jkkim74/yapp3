package com.kt.yapp.soap.param;

import javax.xml.soap.SOAPMessage;

import com.kt.yapp.soap.SoapConnUtil;
import com.kt.yapp.soap.response.SoapResponseLegalAgntInfoAdm;

import lombok.Getter;
import lombok.Setter;

/**
 * KOS 법정대리인조회
 */
public class SoapParamLegalAgntInfoAdm 
{
	@Getter @Setter /** 전화번호 */
	private String telno = "";
	
	private SoapConnUtil soapConnUtil;
	public SoapParamLegalAgntInfoAdm (SoapConnUtil soapConnUtil) {
		this.soapConnUtil = soapConnUtil;
	}

	/**
	 * KOS 메소드를 호출한다.<br>
	 * 결과 반환값을 반환객체로 파싱하여 리턴한다.
	 */
	public SoapResponseLegalAgntInfoAdm execute() throws Exception
	{
		SOAPMessage soapMessage = soapConnUtil.makeSoapMessageForKosParentsInfo("LegalAgntInfoAdm", telno);
		SOAPMessage response = SoapConnUtil.call(soapConnUtil.soapEpUrlKosSvc, soapMessage);

		return new SoapResponseLegalAgntInfoAdm(response);
	}
}
