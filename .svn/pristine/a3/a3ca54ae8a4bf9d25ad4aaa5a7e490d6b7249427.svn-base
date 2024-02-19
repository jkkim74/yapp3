package com.kt.yapp.soap.response;

import javax.xml.soap.SOAPMessage;

/**
 * KOS 데이턱 수령 결과
 */
public class SoapResponseSaveDataOneShotRecp extends YappSoapKosResponse
{
	private static final String ERR_CD_PREFIX = "saveDataOneShotRecp";

	public SoapResponseSaveDataOneShotRecp(SOAPMessage response) throws Exception {
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
