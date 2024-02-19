package com.kt.yapp.soap.response;

import javax.xml.soap.SOAPMessage;

/**
 * SHUB 인증 호출 결과 클래스<br>
 * (OIF_087)
 */
public class SoapResponse087 extends YappSoapResponse
{
	private static final String ERR_CD_PREFIX = "087";
	
	public SoapResponse087(SOAPMessage response) throws Exception {
		super.parsingData(response);
	}
	
	@Override
	protected void setData() throws Exception
	{
	}

	@Override
	protected String getErrCdPrefix()
	{
		return ERR_CD_PREFIX;
	}
}
