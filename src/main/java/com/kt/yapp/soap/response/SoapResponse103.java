package com.kt.yapp.soap.response;

import javax.xml.soap.SOAPMessage;

import lombok.Getter;

/**
 * SHUB 인증 호출 결과 클래스<br>
 * (OIF_133)
 */
public class SoapResponse103 extends YappSoapResponse
{
	private static final String ERR_CD_PREFIX = "133";
	
	@Getter	/** 휴대폰 번호 */
	private String mobileNo;
	
	public SoapResponse103(SOAPMessage response) throws Exception {
		super.parsingData(response);
	}

	@Override
	protected void setData() throws Exception
	{
		this.mobileNo = getNodeText("sdp:login_id");
	}

	@Override
	protected String getErrCdPrefix()
	{
		return ERR_CD_PREFIX;
	}
}
