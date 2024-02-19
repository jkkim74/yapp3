package com.kt.yapp.soap.response;

import java.util.ArrayList;
import java.util.List;

import javax.xml.soap.SOAPMessage;
import javax.xml.xpath.XPathConstants;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.kt.yapp.domain.ParentsInfo;
import com.kt.yapp.util.YappUtil;

import lombok.Getter;
import lombok.Setter;

/**
 * KOS 법정대리인조회 
 */
public class SoapResponseLegalAgntInfoAdm extends YappSoapKosResponse
{
	private static final String ERR_CD_PREFIX = "LegalAgntInfoAdm";

	@Getter @Setter /** 결과코드 */
	private String resltCd;
	@Getter @Setter /** 결과상세코드 */
	private String trtResltSbst;
	@Getter @Setter /** 데이터 정보 목록 */
	private List<ParentsInfo> parentsInfoList = new ArrayList<>();

	public SoapResponseLegalAgntInfoAdm(SOAPMessage response) throws Exception {
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

		NodeList listOfVasList = (NodeList) xpath.evaluate("//ns1:LegalAgntContOutDTO", doc, XPathConstants.NODESET);

		for ( int idx1 = 0; idx1 < listOfVasList.getLength(); idx1++ ) 
		{
			Node nodeLvl1 = listOfVasList.item(idx1);
			ParentsInfo parentsInfo = new ParentsInfo();
			String mphonNo = YappUtil.nToStr(getNodeText(nodeLvl1, "ns1:mphonNo"));
			String custIdfyNo = YappUtil.nToStr(getNodeText(nodeLvl1, "ns1:custIdfyNo"));
			String custId = YappUtil.nToStr(getNodeText(nodeLvl1, "ns1:custId"));
			String custNm = YappUtil.nToStr(getNodeText(nodeLvl1, "ns1:custNm"));
			parentsInfo.setCustId(custId);
			parentsInfo.setCustIdfyNo(custIdfyNo);
			parentsInfo.setCustNm(YappUtil.blindNameToName(custNm, 1));
			parentsInfo.setTelno(YappUtil.blindNameToTelno(YappUtil.getRefacTelno(mphonNo)));
			parentsInfo.setParentsTelno(YappUtil.getRefacTelno(mphonNo));
			parentsInfoList.add(parentsInfo);
		}
	}

	@Override
	protected String getErrCdPrefix()
	{
		return ERR_CD_PREFIX;
	}
}
