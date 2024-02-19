package com.kt.yapp.soap.response;

import javax.xml.soap.SOAPMessage;

import lombok.Getter;
import lombok.Setter;

/**
 * KOS 법정대리인조회 
 */
public class SoapResponseYdbSendSms extends YappSoapKosResponse
{
	private static final String ERR_CD_PREFIX = "YdbSendSms";

	@Getter @Setter /** 결과코드 */
	private String resltCd;
	@Getter @Setter /** 결과상세코드 */
	private String trtResltSbst;

	public SoapResponseYdbSendSms(SOAPMessage response) throws Exception {
		super.parsingData(response);
	}

	@Override
	protected void setData() throws Exception
	{
		respTp = getNodeText("biz:responseType");
		globalNo = getNodeText("biz:globalNo");

		if(globalNo == null || globalNo.equals("")){
			this.globalNo = getNodeText("ns1:globalNo");
		}

		if(svcName == null || svcName.equals("")){
			this.svcName = getNodeText("ns1:svcName");
			this.srcId = getNodeText("ns1:srcId");
		}

		if(respTp == null || !respTp.equals("I")){
			this.errCd = getNodeText("ns1:responseCode");
			this.errDesc = getNodeText("ns1:responseBasc");
			this.rtnDesc = getNodeText("ns1:responseDtal");
		}

		resltCd = getNodeText("RESULT_CODE");
	}

	@Override
	protected String getErrCdPrefix()
	{
		return ERR_CD_PREFIX;
	}
}
