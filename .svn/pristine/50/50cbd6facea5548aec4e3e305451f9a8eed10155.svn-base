package com.kt.yapp.soap.param;

import javax.xml.soap.SOAPMessage;

import com.kt.yapp.soap.SoapConnUtil;
import com.kt.yapp.soap.response.SoapResponseGetDataInfoBySvcNo;
import com.kt.yapp.soap.response.SoapResponseSaveApdDataPrvd;

import lombok.Getter;
import lombok.Setter;

/**
 * 나눔 가능 데이터 조회
 */
public class SoapParamGetDataInfoBySvcNo
{
	@Getter @Setter	/** 휴대폰번호 */
	private String mobileNo;
	
	private SoapConnUtil soapConnUtil;
	public SoapParamGetDataInfoBySvcNo(SoapConnUtil soapConnUtil) {
		this.soapConnUtil = soapConnUtil;
	}
	
	/**
	 * KOS 메소드를 호출한다.<br>
	 * 결과 반환값을 반환객체로 파싱하여 리턴한다.
	 */
	public SoapResponseGetDataInfoBySvcNo execute() throws Exception
	{
		String [][] params = new String[][] {
			{"svcNo", mobileNo}
		};
		
		SOAPMessage soapMessage = soapConnUtil.makeSoapMessageForKos2("GetDataInfoBySvcNo", params);
		SOAPMessage response = SoapConnUtil.call(soapConnUtil.soapEpUrlKosDynamicSvc, soapMessage);
		
		return new SoapResponseGetDataInfoBySvcNo(response);
	}
}
