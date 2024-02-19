package com.kt.yapp.soap.response;

import javax.xml.soap.SOAPMessage;

import lombok.Getter;

/**
 * SHUB 인증 호출 결과 클래스<br>
 * (OIF_086)
 */
public class SoapResponse086 extends YappSoapResponse
{
	private static final String ERR_CD_PREFIX = "086";

	@Getter	/** 랜덤번호 */
	private String randomNo;
	
	public SoapResponse086(SOAPMessage response) throws Exception {
		super.parsingData(response);
	}

	@Override
	protected void setData() throws Exception
	{
		randomNo = getNodeText("sdp:random_no");
	}

	@Override
	protected String getErrCdPrefix()
	{
		return ERR_CD_PREFIX;
	}
}
