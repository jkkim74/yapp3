package com.kt.yapp.soap.response;

import javax.xml.soap.SOAPMessage;

import com.kt.yapp.util.YappUtil;

/**
 * SHUB 인증 호출 결과 클래스<br>
 * (OIF_494)
 */
public class SoapResponse495 extends YappSoapResponse
{
	private static final String ERR_CD_PREFIX = "495";

	public SoapResponse495(SOAPMessage response) throws Exception {
		super.parsingData(response);
	}
	
	@Override
	protected void setData() throws Exception
	{
		// 에러인 경우 에러정보 세팅
		if ( YappUtil.is1(super.rtnCd) == false )
		{
			super.rtnCd = ERR_CD_PREFIX + getNodeText("sdp:resultcode");
			super.rtnDesc = getNodeText("sdp:resultmsg");
		}
	}

	@Override
	protected String getErrCdPrefix()
	{
		return ERR_CD_PREFIX;
	}
}
