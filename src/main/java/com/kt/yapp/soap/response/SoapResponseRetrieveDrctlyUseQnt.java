package com.kt.yapp.soap.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.soap.SOAPMessage;
import javax.xml.xpath.XPathConstants;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.kt.yapp.domain.DataInfo;
import com.kt.yapp.util.YappUtil;

import lombok.Getter;
import lombok.Setter;

/**
 * KOS 내 데이터 상세 정보 조회 결과
 */
public class SoapResponseRetrieveDrctlyUseQnt extends YappSoapKosResponse
{
	private static final String ERR_CD_PREFIX = "retrieveDrctlyUseQntDtl";

	@Getter @Setter /** 데이터 정보  */
	private DataInfo dataInfo = new DataInfo();
	
	public SoapResponseRetrieveDrctlyUseQnt(SOAPMessage response) throws Exception {
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
		NodeList listOfVasList = (NodeList) xpath.evaluate("//ns1:drctlyUseQntRetvOutDTO", doc, XPathConstants.NODESET);
		String unitSvcGroupCdEq = "PACKET";
		for ( int idx = 0; idx < listOfVasList.getLength(); idx++ ) 
		{
			Node nodeLvl = listOfVasList.item(idx);
			if ( nodeLvl == null ){
				continue;
			}
			String unitSvcGroupCd = YappUtil.nToStr(getNodeText(nodeLvl, "ns1:unitSvcGroupCd"));
			if(unitSvcGroupCd.equals("PALV_T")){
				unitSvcGroupCdEq = "PALV_T";
			}
		}

		int checkCnt = 0;
		for ( int idx1 = 0; idx1 < listOfVasList.getLength(); idx1++ ) 
		{
			Node nodeLvl1 = listOfVasList.item(idx1);
			if ( nodeLvl1 == null ){
				continue;
			}
			String unitSvcGroupCd = YappUtil.nToStr(getNodeText(nodeLvl1, "ns1:unitSvcGroupCd"));
			String prodId = YappUtil.nToStr(getNodeText(nodeLvl1, "ns1:prodId"));
			String dataNm = YappUtil.nToStr(getNodeText(nodeLvl1, "ns1:prodNm"));
			if(unitSvcGroupCd.equals(unitSvcGroupCdEq)){
				checkCnt++;
				int totalDataAmt = YappUtil.toInt(getNodeText(nodeLvl1, "ns1:totFreeQnt"), 0) / 2048;
				int useDataAmt = YappUtil.toInt(getNodeText(nodeLvl1, "ns1:freeUseQnt"), 0) / 2048;
				
				// 당월소멸예정
				int dsprCurMnthDataAmt = YappUtil.toInt(getNodeText(nodeLvl1, "ns1:cfwdFreeQnt"), 0) / 2048;
				// 익월소멸예정
				int dsprNextMnthDataAmt = YappUtil.toInt(getNodeText(nodeLvl1, "ns1:tmonFreeQnt"), 0) / 2048;
				long tmonFreeQnt = YappUtil.toLong(getNodeText(nodeLvl1, "ns1:tmonFreeQnt"), 0);
				int orgDsprCurMnthDataAmt = dsprCurMnthDataAmt;
				if ( dsprCurMnthDataAmt - useDataAmt < 0 ) {
					dsprNextMnthDataAmt = dsprNextMnthDataAmt + dsprCurMnthDataAmt - useDataAmt;
					dsprCurMnthDataAmt = 0;
				} else {
					dsprCurMnthDataAmt = dsprCurMnthDataAmt - useDataAmt;
				}
				dataInfo.setDataNm(dataNm);
				dataInfo.setDataAmt(totalDataAmt);
				dataInfo.setUseDataAmt(useDataAmt);
				dataInfo.setRmnDataAmt(totalDataAmt - useDataAmt);
				dataInfo.setDataTp(prodId);
				dataInfo.setDsprCurMnthDataAmt(dsprCurMnthDataAmt);
				dataInfo.setOrgDsprCurMnthDataAmt(orgDsprCurMnthDataAmt);
				dataInfo.setDsprNextMnthDataAmt(dsprNextMnthDataAmt);
				dataInfo.setTmonFreeQnt(tmonFreeQnt);
			}
		}
		
		if(checkCnt == 0){
			dataInfo.setDataNm("");
			dataInfo.setDataAmt(0);
			dataInfo.setUseDataAmt(0);
			dataInfo.setRmnDataAmt(0);
			dataInfo.setDataTp("");
			dataInfo.setDsprCurMnthDataAmt(0);
			dataInfo.setOrgDsprCurMnthDataAmt(0);
			dataInfo.setDsprNextMnthDataAmt(0);
			dataInfo.setTmonFreeQnt(0);
		}
	}

	@Override
	protected String getErrCdPrefix()
	{
		return ERR_CD_PREFIX;
	}
}
