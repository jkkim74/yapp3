package com.kt.yapp.soap.response;

import javax.xml.soap.SOAPMessage;

import com.kt.yapp.util.YappUtil;

/**
 * SHUB 인증 호출 결과 클래스<br>
 * (OIF_750)
 */
public class SoapResponse750 extends YappSoapResponse
{
	private static final String ERR_CD_PREFIX = "750";

	public SoapResponse750(SOAPMessage response) throws Exception {
		super.parsingData(response);
	}
	
	@Override
	protected void setData() throws Exception
	{
		// 에러인 경우 에러정보 세팅
		if ( YappUtil.is1(super.rtnCd) == false )
		{
			super.rtnDesc = getNodeText("sdp:errordescription");
		}
	}
	
	@Override
	protected String getErrCdPrefix()
	{
		return ERR_CD_PREFIX;
	}

}
