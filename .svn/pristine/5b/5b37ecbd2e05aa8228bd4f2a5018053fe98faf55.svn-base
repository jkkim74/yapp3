package com.kt.yapp.soap.response;

import javax.xml.soap.SOAPMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import lombok.Setter;

/**
 * (사전 체크)KOS 부가서비스 변경 결과
 */
public class SoapResponseProdSbscTrmnStoreTrtPreCheck extends YappSoapKosResponse
{
	private static final String ERR_CD_PREFIX = "mobileProdPreChk";
	private static final Logger logger = LoggerFactory.getLogger(SoapResponseProdSbscTrmnStoreTrtPreCheck.class);
	
	@Getter @Setter /** 결과코드 */
	private String resltCd;
	@Getter @Setter /** 결과상세코드 */
	private String trtResltSbst;
	
	@Getter @Setter /** 결과명 */
	private String ruleNm;
	@Getter @Setter /** 결과메세지 */
	private String ruleMsgSbst;

	public SoapResponseProdSbscTrmnStoreTrtPreCheck(SOAPMessage response) throws Exception {
		super.parsingData(response);
	}

	@Override
	protected void setData() throws Exception
	{
//		rtnCd = getNodeText("biz:responseCode");
//		respTp = getNodeText("biz:responseType");
//		resltCd = getNodeText("biz:resltCd");
//		trtResltSbst = getNodeText("biz:trtResltSbst");
//		globalNo = getNodeText("biz:globalNo");
		
//		this.respTp = getNodeText("responseType");
//		this.globalNo = getNodeText("globalNo");
//		this.svcName = getNodeText("svcName");
//		this.srcId = getNodeText("srcId");
		
		try{
			// respTp   = getNodeText("responseType");
			// globalNo = getNodeText("globalNo");
			resltCd  = getNodeText("resltCd");
			errDesc  = getNodeText("msgDesc");
			rtnCd    = getNodeText("rtnCd");
			rtnDesc  = getNodeText("rtnDesc");
			
			ruleNm      = getNodeText("ruleNm");
			ruleMsgSbst = getNodeText("ruleMsgSbst");
			
			logger.info("respTp    :"+respTp);
			logger.info("globalNo  :"+globalNo);
			logger.info("svcName   :"+svcName);
			logger.info("srcId     :"+srcId);
			
			logger.info("resltCd     :"+resltCd);
			logger.info("errDesc     :"+errDesc);
			logger.info("rtnCd       :"+rtnCd);
			logger.info("rtnDesc     :"+rtnDesc);
			
			logger.info("ruleNm      :"+ruleNm);
			logger.info("ruleMsgSbst :"+ruleMsgSbst);
			
		}
		catch(RuntimeException ex) {
			//ex.printStackTrace();
			logger.error("error : " + ex);
		}
		
	}

	@Override
	protected String getErrCdPrefix()
	{
		return ERR_CD_PREFIX;
	}

	@Override
	public String toString() {
		return "SoapResponseProdSbscTrmnStoreTrtPreCheck [resltCd=" + resltCd + ", trtResltSbst=" + trtResltSbst
				+ ", ruleNm=" + ruleNm + ", ruleMsgSbst=" + ruleMsgSbst + "]";
	}
}
