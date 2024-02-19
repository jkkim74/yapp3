package com.kt.yapp.soap.param;

import javax.xml.soap.SOAPMessage;

import com.kt.yapp.soap.SoapConnUtil;
import com.kt.yapp.soap.response.SoapResponseDataSharUseQntRetv;
import com.kt.yapp.util.SessionKeeper;

import lombok.Getter;
import lombok.Setter;

/**
 * KOS 5G 공유 데이터
 */
public class SoapParamDataSharUseQntRetv 
{
	@Getter @Setter	/** 계약번호 */
	private String cntrNo;
	@Getter @Setter	/** 해당날짜 */
	private String retvDate;
	
	private SoapConnUtil soapConnUtil;
	public SoapParamDataSharUseQntRetv(SoapConnUtil soapConnUtil) {
		this.soapConnUtil = soapConnUtil;
	}
	
	/**
	 * KOS 메소드를 호출한다.<br>
	 * 결과 반환값을 반환객체로 파싱하여 리턴한다.
	 */
	public SoapResponseDataSharUseQntRetv execute() throws Exception
	{
		SOAPMessage soapMessage = soapConnUtil.makeSoapMessageForKosDataSharUseQntRetv("dataSharPrvQntRetv", cntrNo, retvDate);
		SOAPMessage response = SoapConnUtil.call(soapConnUtil.soapEpUrlKosDataSharUseRetv, soapMessage);
		return new SoapResponseDataSharUseQntRetv(response);
	}
}
