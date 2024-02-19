package com.kt.yapp.soap.response;

import javax.xml.soap.SOAPMessage;

/**
 * KOS 데이터 꺼내기 결과
 */
public class SoapResponseSaveDataOutput extends YappSoapKosResponse
{
	private static final String ERR_CD_PREFIX = "saveDataOutput";

	public SoapResponseSaveDataOutput(SOAPMessage response) throws Exception {
		super.parsingData(response);
	}

	@Override
	protected void setData() throws Exception
	{
		rtnCd = getNodeText("biz:resltCd");
		globalNo = getNodeText("biz:globalNo");
		svcName = getNodeText("biz:svcName");
		srcId = getNodeText("biz:srcId");
		errCd = getNodeText("biz:responseCode");
		rtnDesc = getNodeText("biz:responseBasc");
	}

	@Override
	protected String getErrCdPrefix()
	{
		return ERR_CD_PREFIX;
	}
}
