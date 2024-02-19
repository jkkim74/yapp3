package com.kt.yapp.soap.response;

import java.util.ArrayList;
import java.util.List;

import javax.xml.soap.SOAPMessage;
import javax.xml.xpath.XPathConstants;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.kt.yapp.domain.BigiChargBlnc;
import com.kt.yapp.domain.DataInfo;
import com.kt.yapp.domain.DataShareList;
import com.kt.yapp.util.YappUtil;

import lombok.Getter;
import lombok.Setter;

/**
 * KOS 5G 공유 데이터
 */
public class SoapResponseDataSharUseQntRetv extends YappSoapKosResponse
{
	private static final String ERR_CD_PREFIX = "dataSharPrvQntRetv";

	@Getter @Setter /** 데이터 정보 목록 */
	private DataShareList dataShareList = new DataShareList();
	@Getter @Setter /** 데이터 정보(알요금제) 정보 */
	private BigiChargBlnc dataBigiInfo = new BigiChargBlnc();
	
	public SoapResponseDataSharUseQntRetv(SOAPMessage response) throws Exception {
		super.parsingData(response);
	}

	@Override
	protected void setData() throws Exception
	{
		respTp = getNodeText("responseType");
		globalNo = getNodeText("globalNo");

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
			NodeList listOfVasList = (NodeList) xpath.evaluate("//DataSharPrvQntRetvOutDTO", doc, XPathConstants.NODESET);
	
			for ( int idx1 = 0; idx1 < listOfVasList.getLength(); idx1++ ) 
			{
				Node nodeLvl1 = listOfVasList.item(idx1);
				if ( nodeLvl1 == null )
					continue;
				
				String prodId = YappUtil.nToStr(getNodeText(nodeLvl1, "prodId"));
	
				int totalDataAmt = YappUtil.toInt(getNodeText(nodeLvl1, "totFreeQnt"), 0) / 2048;
				int useDataAmt = YappUtil.toInt(getNodeText(nodeLvl1, "freeUseQnt"), 0) / 2048;
				
				// 당월소멸예정
				int dsprCurMnthDataAmt = YappUtil.toInt(getNodeText(nodeLvl1, "cfwdFreeQnt"), 0) / 2048;
				// 익월소멸예정
				int dsprNextMnthDataAmt = YappUtil.toInt(getNodeText(nodeLvl1, "tmonFreeQnt"), 0) / 2048;
	
				dataShareList.setDataNm("공유데이터");
				dataShareList.setDataAmt(totalDataAmt);
				dataShareList.setRmnDataAmt(totalDataAmt - useDataAmt);
				dataShareList.setDataTp(prodId);
				dataShareList.setDsprCurMnthDataAmt(dsprCurMnthDataAmt);
				dataShareList.setDsprNextMnthDataAmt(dsprNextMnthDataAmt);
			}
			
			NodeList listOfVasList2 = (NodeList) xpath.evaluate("//dataSharPrvQntListRetvOutDTO", doc, XPathConstants.NODESET);
			List<DataInfo> dataInfoList = new ArrayList<>();
			for ( int idx2 = 0; idx2 < listOfVasList2.getLength(); idx2++ ) 
			{
				Node nodeLvl1 = listOfVasList2.item(idx2);
				if ( nodeLvl1 == null )
					continue;
				
				String prodId = YappUtil.nToStr(getNodeText(nodeLvl1, "prodId"));
				String prodNm = YappUtil.nToStr(getNodeText(nodeLvl1, "prodNm"));

				if ( prodNm.indexOf("당겨쓰기") > -1 ) {
					prodNm = "데이터 당겨쓰기";
				} else if ( prodNm.indexOf("데이터박스") > -1 ) {
					prodNm = "데이터박스 꺼내기";
				} else if ( prodNm.indexOf("패밀리박스") > -1 ) {
					prodNm = "패밀리박스 꺼내기";
				} else if ( prodNm.indexOf("룰렛") > -1 ) {
					prodNm = "데이터 룰렛";
				} else if (prodNm.indexOf("충전") > -1) {
					prodNm = "데이터충전";
				} else if (prodNm.indexOf("쿠폰") > -1){
					prodNm = "데이터 쿠폰";
				}

				int totalDataAmt = YappUtil.toInt(getNodeText(nodeLvl1, "totFreeQnt"), 0) / 2048;
				int useDataAmt = YappUtil.toInt(getNodeText(nodeLvl1, "freeUseQnt"), 0) / 2048;
				
				// 당월소멸예정
				int dsprCurMnthDataAmt = YappUtil.toInt(getNodeText(nodeLvl1, "cfwdFreeQnt"), 0) / 2048;
				// 익월소멸예정
				int dsprNextMnthDataAmt = YappUtil.toInt(getNodeText(nodeLvl1, "tmonFreeQnt"), 0) / 2048;
				DataInfo dataInfo = new DataInfo();
				dataInfo.setDataNm(prodNm);
				dataInfo.setDataAmt(totalDataAmt);
				dataInfo.setRmnDataAmt(totalDataAmt - useDataAmt);
				dataInfo.setDataTp(prodId);
				dataInfo.setDsprCurMnthDataAmt(dsprCurMnthDataAmt);
				dataInfo.setDsprNextMnthDataAmt(dsprNextMnthDataAmt);
				dataInfoList.add(dataInfo);
			}
			dataShareList.setDataShareList(dataInfoList);
		}
	}

	@Override
	protected String getErrCdPrefix()
	{
		return ERR_CD_PREFIX;
	}
}
