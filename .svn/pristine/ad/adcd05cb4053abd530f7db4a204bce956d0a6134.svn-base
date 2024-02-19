package com.kt.yapp.soap.response;

import javax.xml.soap.SOAPMessage;

/**
 * SHUB 인증 호출 결과 클래스<br>
 * (OIF_2102)
 */
public class SoapResponse2102 extends YappSoapResponse
{
	private static final String ERR_CD_PREFIX = "2102";

	public SoapResponse2102(SOAPMessage response) throws Exception {
		super.parsingData(response);
	}
	
	@Override
	protected void setData() throws Exception
	{
		setRtnCd(getNodeText("RT"));
		setRtnDesc(getNodeText("RT_MSG"));
		this.transId = getNodeText("TRANSACTIONID");
	}

	@Override
	protected String getErrCdPrefix()
	{
		return ERR_CD_PREFIX;
	}
}
