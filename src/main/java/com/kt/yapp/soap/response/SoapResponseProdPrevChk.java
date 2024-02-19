package com.kt.yapp.soap.response;

import javax.xml.soap.SOAPMessage;

import lombok.Getter;
import lombok.Setter;

/**
 * KOS 마이타일플랜 시간 설정전 상품 사전체크 결과
 */
public class SoapResponseProdPrevChk extends YappSoapKosResponse
{
	private static final String ERR_CD_PREFIX = "mobileProdPreChk";

	@Getter @Setter /** 결과코드 */
	private String resltCd;
	@Getter @Setter /** 결과메세지 */
	private String ruleMsgSbst;
	@Getter @Setter /** 결과메세지ID */
	private String ruleMsgId;

	public SoapResponseProdPrevChk(SOAPMessage response) throws Exception {
		super.parsingData(response);
	}

	@Override
	protected void setData() throws Exception
	{
		resltCd = getNodeText("resltCd");
		ruleMsgSbst = getNodeText("ruleMsgSbst");
		ruleMsgId = getNodeText("ruleMsgId");
	}

	@Override
	protected String getErrCdPrefix()
	{
		return ERR_CD_PREFIX;
	}
}
