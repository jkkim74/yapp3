package com.kt.yapp.soap.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.soap.SOAPMessage;
import javax.xml.xpath.XPathConstants;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.kt.yapp.domain.BigiChargBlnc;
import com.kt.yapp.domain.DataInfo;
import com.kt.yapp.util.YappUtil;

import lombok.Getter;
import lombok.Setter;

/**
 * KOS 내 데이터 상세 정보 조회 결과
 */
public class SoapResponseRetrieveBigiChargBlnc extends YappSoapKosResponse
{
	private static final String ERR_CD_PREFIX = "retrieveBigiChargBlnc";

	@Getter @Setter /** 데이터 정보 목록 */
	private List<DataInfo> dataInfoList = new ArrayList<>();
	@Getter @Setter /** 데이터 정보(알요금제) 정보 */
	private BigiChargBlnc dataBigiInfo = new BigiChargBlnc();
	
	public SoapResponseRetrieveBigiChargBlnc(SOAPMessage response) throws Exception {
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
		
		//기본알
		long baseR = YappUtil.toLong(getNodeText("biz:basAlNum"),0);
		// 이월 알
		long forwardR = YappUtil.toLong(getNodeText("biz:cfwdAlNum"),0);
		// 충전 알
		long chgR = YappUtil.toLong(getNodeText("biz:apdChargAlNum"),0);
		// 받은알
		long revR = YappUtil.toLong(getNodeText("biz:rcvAlNum"),0);
		// 데이터알
		long dataR = YappUtil.toLong(getNodeText("biz:dataAlNum"),0);
		
		// 데이터(byte)
		long dataByte = YappUtil.toLong(getNodeText("biz:dataRmndQnt"),0);
		
		// 문자전용알
		long smsR = YappUtil.toLong(getNodeText("biz:freeSmsAlNum"),0);
	
		// 공통 알
		long totalR = baseR + forwardR + chgR + revR;
		
		dataBigiInfo.setEggMyRmnDataAmt(totalR);	// 알 표시에는 '전체' 만 세팅
		dataBigiInfo.setEggFwdMnthDataAmt(forwardR);
		dataBigiInfo.setEggCurMnthDataAmt(baseR);
		dataBigiInfo.setEggRcvDataAmt(revR);
		dataBigiInfo.setEggDataAmt(dataR);
		dataBigiInfo.setDataAmt(dataByte);
		dataBigiInfo.setEggChgDataAmt(chgR);
		dataBigiInfo.setMyEggDataAmt(totalR + dataR);
		dataBigiInfo.setEggSmsDataAmt(smsR);
		
		/*NodeList listOfVasList = (NodeList) xpath.evaluate("//ns1:drctlyUseQntRetvOutDTO", doc, XPathConstants.NODESET);

		for ( int idx1 = 0; idx1 < listOfVasList.getLength(); idx1++ ) 
		{
			Node nodeLvl1 = listOfVasList.item(idx1);
			if ( nodeLvl1 == null )
				continue;
			
			String prodId = YappUtil.nToStr(getNodeText(nodeLvl1, "ns1:prodId"));
			String dataNm = YappUtil.nToStr(getNodeText(nodeLvl1, "ns1:prodNm"));
			
			if ( dataNm.indexOf("당겨쓰기") > -1 ) {
				dataNm = "데이터 당겨쓰기";
			} else if ( dataNm.indexOf("데이터박스") > -1 ) {
				dataNm = "데이터박스 꺼내기";
			} else if ( dataNm.indexOf("패밀리박스") > -1 ) {
				dataNm = "패밀리박스 꺼내기";
			} else if ( dataNm.indexOf("룰렛") > -1 ) {
				dataNm = "데이터 룰렛";
			} else if (dataNm.indexOf("충전") > -1) {
				dataNm = "데이터충전";
			} else if (dataNm.indexOf("쿠폰") > -1){
				dataNm = "데이터 쿠폰";
			}
			
			int totalDataAmt = YappUtil.toInt(getNodeText(nodeLvl1, "ns1:totFreeQnt"), 0) / 2048;
			int useDataAmt = YappUtil.toInt(getNodeText(nodeLvl1, "ns1:freeUseQnt"), 0) / 2048;
			
			// 당월소멸예정
			int dsprCurMnthDataAmt = YappUtil.toInt(getNodeText(nodeLvl1, "ns1:cfwdFreeQnt"), 0) / 2048;
			// 익월소멸예정
			int dsprNextMnthDataAmt = YappUtil.toInt(getNodeText(nodeLvl1, "ns1:tmonFreeQnt"), 0) / 2048;
			int orgDsprCurMnthDataAmt = dsprCurMnthDataAmt;
			if ( dsprCurMnthDataAmt - useDataAmt < 0 ) {
				dsprNextMnthDataAmt = dsprNextMnthDataAmt + dsprCurMnthDataAmt - useDataAmt;
				dsprCurMnthDataAmt = 0;
			} else {
				dsprCurMnthDataAmt = dsprCurMnthDataAmt - useDataAmt;
			}

			// 다 쓴 데이터는 표시하지 않는다.
			if ( totalDataAmt - useDataAmt <= 0 ) {
				continue;
			}

			DataInfo dataInfo = new DataInfo();

			dataInfo.setDataNm(dataNm);
			dataInfo.setDataAmt(totalDataAmt);
			dataInfo.setRmnDataAmt(totalDataAmt - useDataAmt);
			dataInfo.setDataTp(prodId);
			dataInfo.setDsprCurMnthDataAmt(dsprCurMnthDataAmt);
			dataInfo.setOrgDsprCurMnthDataAmt(orgDsprCurMnthDataAmt);
			dataInfo.setDsprNextMnthDataAmt(dsprNextMnthDataAmt);
			
			dataInfoList.add(dataInfo);
		}*/
	}

	@Override
	protected String getErrCdPrefix()
	{
		return ERR_CD_PREFIX;
	}
}
