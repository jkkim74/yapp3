package com.kt.yapp.soap.response;

import java.util.Date;

import javax.xml.soap.SOAPMessage;
import javax.xml.xpath.XPathConstants;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.kt.yapp.domain.DataRefill;
import com.kt.yapp.util.YappUtil;

import lombok.Getter;
import lombok.Setter;

/**
 * KOS 내 데이터 상세 정보 조회 결과
 */
public class SoapResponseRetrieveDataRefill extends YappSoapKosResponse
{
	private static final String ERR_CD_PREFIX = "retrieveDataRefill";

	@Getter @Setter /** 충전데이터 잔여량 */
	private DataRefill dataRefill = new DataRefill();
	
	public SoapResponseRetrieveDataRefill(SOAPMessage response) throws Exception {
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
		NodeList listOfVasList = (NodeList) xpath.evaluate("//biz:DataRefillRetvOutDTO", doc, XPathConstants.NODESET);
		
		String strYmd = YappUtil.getTodayYmd(0,0,0);
		Date todayYmd = YappUtil.changeStringToDate(strYmd);		
		
		long dataByte = 0;
		for ( int idx1 = 0; idx1 < listOfVasList.getLength(); idx1++ ) 
		{
			Node nodeLvl1 = listOfVasList.item(idx1);
			if ( nodeLvl1 == null ){
				continue;
			}
			 
			String strExpireYmd = getNodeText(nodeLvl1,"biz:refillExpire");
			Date expireYmd = YappUtil.changeStringToDate(strExpireYmd);
			int comp = todayYmd.compareTo(expireYmd);
			
			if(comp <= 0){				
				dataByte = dataByte + YappUtil.toLong(getNodeText(nodeLvl1,"biz:refillRemains"));
			}
			
		}
		dataRefill.setRefillRemainsAmt(dataByte);
	}

	@Override
	protected String getErrCdPrefix()
	{
		return ERR_CD_PREFIX;
	}
}
