package com.kt.yapp.soap.response;

import java.util.ArrayList;
import java.util.List;

import javax.xml.soap.SOAPMessage;
import javax.xml.xpath.XPathConstants;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.kt.yapp.domain.DataInfo;
import com.kt.yapp.domain.SvcHstByCustInfo;
import com.kt.yapp.domain.TimeOption;
import com.kt.yapp.util.YappUtil;

import lombok.Getter;
import lombok.Setter;

/**
 * KOS 부가서비스 변경 결과
 */
public class SoapResponseSvcHstByCustIdRetv extends YappSoapKosResponse
{
	private static final String ERR_CD_PREFIX = "svcHstByCustIdRetv";

	@Getter @Setter /** 결과코드 */
	private String resltCd;
	@Getter @Setter /** 결과상세코드 */
	private String trtResltSbst;
	@Getter @Setter
	private List<SvcHstByCustInfo> svcHstByCustInfoList = new ArrayList<>();

	public SoapResponseSvcHstByCustIdRetv(SOAPMessage response) throws Exception {
		super.parsingData(response);
	}

	@Override
	protected void setData() throws Exception
	{
		respTp = getNodeText("responseType");
		globalNo = getNodeText("globalNo");
		svcName = getNodeText("svcName");
		
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
		}else{
			// 잔여량 : <totFreeQnt>149701539</totFreeQnt> // 전체무료량  -  <freeUseQnt>2457600</freeUseQnt> // 무료사용량 
			//5GINTMPP3, 5GINTMPP4, 5GINTMPP6
			NodeList listOfVasList = (NodeList) xpath.evaluate("//svcHstByCustIdRetvListDTO", doc, XPathConstants.NODESET);
	
			for ( int idx1 = 0; idx1 < listOfVasList.getLength(); idx1++ ) 
			{
				Node nodeLvl1 = listOfVasList.item(idx1);
				if ( nodeLvl1 == null )
					continue;
				SvcHstByCustInfo svcHstByCustInfo = new SvcHstByCustInfo();
				svcHstByCustInfo.setProdId(YappUtil.nToStr(getNodeText(nodeLvl1, "prodId")));
				svcHstByCustInfo.setProdNm(YappUtil.nToStr(getNodeText(nodeLvl1, "prodNm")));
				svcHstByCustInfo.setProdHstSeq(YappUtil.nToStr(getNodeText(nodeLvl1, "prodHstSeq")));
				svcHstByCustInfo.setEfctStDt(YappUtil.nToStr(getNodeText(nodeLvl1, "efctStDt")));
				svcHstByCustInfo.setEfctFnsDt(YappUtil.nToStr(getNodeText(nodeLvl1, "efctFnsDt")));
				svcHstByCustInfo.setProdTypeCd(YappUtil.nToStr(getNodeText(nodeLvl1, "prodTypeCd")));
				svcHstByCustInfo.setSbscCpNm(YappUtil.nToStr(getNodeText(nodeLvl1, "sbscCpNm")));
				svcHstByCustInfo.setSbscOrgNm(YappUtil.nToStr(getNodeText(nodeLvl1, "sbscOrgNm")));
				svcHstByCustInfo.setSvcContId(YappUtil.nToStr(getNodeText(nodeLvl1, "svcContId")));
	
				svcHstByCustInfoList.add(svcHstByCustInfo);
			}
		}
	}

	@Override
	protected String getErrCdPrefix()
	{
		return ERR_CD_PREFIX;
	}
}
