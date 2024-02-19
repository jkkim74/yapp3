package com.kt.yapp.soap.param;

import javax.xml.soap.SOAPMessage;

import com.kt.yapp.soap.SoapConnUtil;
import com.kt.yapp.soap.response.SoapResponseSaveDataClect;

import lombok.Getter;
import lombok.Setter;

/**
 * KOS 데이턱 회수
 */
public class SoapParamSaveDataClect 
{
	@Getter @Setter	/** 데이터박스ID */
	private String dboxId;
	
	@Getter @Setter	/** 데이턱ID */
	private String datukId;
	
	private SoapConnUtil soapConnUtil;
	public SoapParamSaveDataClect(SoapConnUtil soapConnUtil) {
		this.soapConnUtil = soapConnUtil;
	}
	
	/**
	 * KOS 메소드를 호출한다.<br>
	 * 결과 반환값을 반환객체로 파싱하여 리턴한다.
	 */
	public SoapResponseSaveDataClect execute() throws Exception
	{
		String [][] params = new String[][] {
			{"databoxId", dboxId}
			, {"databoxToknNo", datukId}
		};
		
		SOAPMessage soapMessage = soapConnUtil.makeSoapMessageForKos("saveDataClect", params);
		SOAPMessage response = SoapConnUtil.call(soapConnUtil.soapEpUrlKosDynamicSvc, soapMessage);
		
		return new SoapResponseSaveDataClect(response);
	}
}
