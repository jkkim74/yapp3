package com.kt.yapp.soap.response;

import javax.xml.soap.SOAPMessage;

/**
 * KOS 데이턱 회수 결과
 */
public class SoapResponseSaveDataClect extends YappSoapKosResponse
{
	private static final String ERR_CD_PREFIX = "saveDataClect";

	public SoapResponseSaveDataClect(SOAPMessage response) throws Exception {
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
