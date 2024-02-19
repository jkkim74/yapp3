package com.kt.yapp.soap.response;

import javax.xml.soap.SOAPMessage;

/**
 * KOS 데이터 박스 생성 결과
 */
public class SoapResponseSaveDataboxSbsc extends YappSoapKosResponse
{
	private static final String ERR_CD_PREFIX = "retvDatabox";

	public SoapResponseSaveDataboxSbsc(SOAPMessage response) throws Exception {
		super.parsingData(response);
	}

	@Override
	protected void setData() throws Exception
	{
		sucesYn = getNodeText("KEY_VALUE_01_SBST");
		rtnCd = getNodeText("KEY_VALUE_02_SBST");
		rtnDesc = getNodeText("KEY_VALUE_03_SBST");

		if(rtnCd == null){
			this.rtnCd = getNodeText("responseCode");
			this.rtnDesc = getNodeText("responseBasc");
		}
	}

	@Override
	protected String getErrCdPrefix()
	{
		return ERR_CD_PREFIX;
	}
}
