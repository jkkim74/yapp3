package com.kt.yapp.soap.response;

import javax.xml.soap.SOAPMessage;

import lombok.Getter;
import lombok.Setter;

/**
 * KOS 부가서비스 변경 결과
 */
public class SoapResponseProdSbscTrmnStoreTrt extends YappSoapKosResponse
{
	private static final String ERR_CD_PREFIX = "processProdStoreBas";

	@Getter @Setter /** 결과코드 */
	private String resltCd;
	@Getter @Setter /** 결과상세코드 */
	private String trtResltSbst;

	public SoapResponseProdSbscTrmnStoreTrt(SOAPMessage response) throws Exception {
		super.parsingData(response);
	}

	@Override
	protected void setData() throws Exception
	{
		rtnCd = getNodeText("biz:responseCode");
		respTp = getNodeText("biz:responseType");
		resltCd = getNodeText("biz:resltCd");
		trtResltSbst = getNodeText("biz:trtResltSbst");
		globalNo = getNodeText("biz:globalNo");
		
		if(respTp == null || !respTp.equals("I")){
			this.errCd = getNodeText("biz:responseCode");
			this.errDesc = getNodeText("biz:responseBasc");
			this.rtnDesc = getNodeText("biz:responseDtal").length() > 500 ? getNodeText("biz:responseDtal").substring(0, 500) : getNodeText("biz:responseDtal");
		}
	}

	@Override
	protected String getErrCdPrefix()
	{
		return ERR_CD_PREFIX;
	}
}
