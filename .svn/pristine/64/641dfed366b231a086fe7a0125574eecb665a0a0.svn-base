package com.kt.yapp.soap.response;

import javax.xml.soap.SOAPMessage;

/**
 * KOS 데이터 꺼내기 제약 조회 결과
 */
public class SoapResponseRetvDataOutputRstr extends YappSoapKosResponse
{
	private static final String ERR_CD_PREFIX = "RetvDatabox";

	public SoapResponseRetvDataOutputRstr(SOAPMessage response) throws Exception {
		super.parsingData(response);
	}

	@Override
	protected void setData() throws Exception
	{
		sucesYn = getNodeText("KEY_VALUE_01_SBST");
		rtnCd = getNodeText("KEY_VALUE_02_SBST");
		rtnDesc = getNodeText("KEY_VALUE_03_SBST");
	}

	@Override
	protected String getErrCdPrefix()
	{
		return ERR_CD_PREFIX;
	}
}
