package com.kt.yapp.soap.response;

import javax.xml.soap.SOAPMessage;

import com.kt.yapp.util.YappUtil;

import lombok.Getter;
import lombok.Setter;

/**
 * 나눔 가능 데이터 조회 결과
 */
public class SoapResponseGetDataInfoBySvcNo extends YappSoapKosResponse
{
	private static final String ERR_CD_PREFIX = "GetDataInfoBySvcNo";
	
	@Getter @Setter
	private int rmnDataAmt;

	public SoapResponseGetDataInfoBySvcNo(SOAPMessage response) throws Exception {
		super.parsingData(response);
	}

	@Override
	protected void setData() throws Exception
	{
		this.rmnDataAmt 		= (int) (YappUtil.toLong(getNodeText("KEY_VALUE_03_SBST"), 0) / 1024 / 1024);
	}

	@Override
	protected String getErrCdPrefix()
	{
		return ERR_CD_PREFIX;
	}
}
