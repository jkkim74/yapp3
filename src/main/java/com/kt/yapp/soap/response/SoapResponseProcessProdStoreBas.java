package com.kt.yapp.soap.response;

import javax.xml.soap.SOAPMessage;

import lombok.Getter;
import lombok.Setter;

/**
 * KOS 마이타일플랜 변경 결과
 */
public class SoapResponseProcessProdStoreBas extends YappSoapKosResponse
{
	private static final String ERR_CD_PREFIX = "processProdStoreBas";

	@Getter @Setter /** 결과코드 */
	private String resltCd;
	@Getter @Setter /** 결과상세코드 */
	private String trtResltSbst;

	public SoapResponseProcessProdStoreBas(SOAPMessage response) throws Exception {
		super.parsingData(response);
	}

	@Override
	protected void setData() throws Exception
	{
		resltCd = getNodeText("biz:resltCd");
		trtResltSbst = getNodeText("biz:trtResltSbst");
	}

	@Override
	protected String getErrCdPrefix()
	{
		return ERR_CD_PREFIX;
	}
}
