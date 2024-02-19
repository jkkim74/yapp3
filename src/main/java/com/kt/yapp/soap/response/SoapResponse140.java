package com.kt.yapp.soap.response;

import java.util.ArrayList;
import java.util.List;

import javax.xml.soap.SOAPMessage;
import javax.xml.xpath.XPathConstants;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.kt.yapp.domain.ContractInfo;
import com.kt.yapp.util.YappUtil;

import lombok.Getter;

/**
 * SHUB 인증 호출 결과 클래스<br>
 * (OIF_140)<br>
 * 계약 목록 조회
 * 
 * @author seungman.yu
 * @since 2018. 10. 4.
 * @version 1.0
 * 
 * Modification Information
 * Mod Date		Modifier		Description
 * ========================================
 * 2018. 10. 4.	seungman.yu 	Y Event 기능 추가
 * Copyright (c) 2018 KTDS, Inc. All Rights Reserved
 */
public class SoapResponse140 extends YappSoapResponse
{
	private static final String ERR_CD_PREFIX = "140";

	@Getter
	private List<ContractInfo> cntrInfoList = new ArrayList<>();
	
	public SoapResponse140(SOAPMessage response) throws Exception {
		super.parsingData(response);
	}
	
	@Override
	protected void setData() throws Exception
	{
		NodeList listOfPartyNodeList = (NodeList) xpath.evaluate("//sdp:ListofParty", doc, XPathConstants.NODESET);

		for ( int idx1 = 0; idx1 < listOfPartyNodeList.getLength(); idx1++ ) 
		{
			Node nodeLvl1 = listOfPartyNodeList.item(idx1);
			if ( nodeLvl1 == null )
				continue;
			
			NodeList arrayOfPartyNodeList = (NodeList) xpath.evaluate("//n1:arrayofparty", nodeLvl1, XPathConstants.NODESET);
			
			for ( int idx2 = 0; idx2 < arrayOfPartyNodeList.getLength(); idx2++ ) 
			{
				Node nodeLvl2 = arrayOfPartyNodeList.item(idx2);
				if ( nodeLvl2 == null )
					continue;
				String partyDetailTypeCd = getNodeText(nodeLvl2, "n1:Party_Detail_Type_Cd");
				String birthDate = getNodeText(nodeLvl2, "n1:Birth_Date");
				String fourteenYn = YappUtil.checkFourteenYn(birthDate);
				String eightteenYn = YappUtil.checkEightteenYn(birthDate);
				String nineteenYn = YappUtil.checkNineteenYn(birthDate);
				String twentyNineYn = YappUtil.checkTwentyNineYn(birthDate);
				String partyName = getNodeText(nodeLvl2, "n1:Party_Name");
				
				NodeList listOfSbscNodeList = (NodeList) xpath.evaluate("//n1:ListofSubscription", nodeLvl2, XPathConstants.NODESET);

				for ( int idx3 = 0; idx3 < listOfSbscNodeList.getLength(); idx3++ ) 
				{
					Node nodeLvl3 = listOfSbscNodeList.item(idx3);
					if ( nodeLvl3 == null )
						continue;
					
					NodeList listOfSbscChildNodeList = (NodeList) xpath.evaluate("//n1:listofsubscription", nodeLvl3, XPathConstants.NODESET);

					for ( int idx4 = 0; idx4 < listOfSbscChildNodeList.getLength(); idx4++ ) {
						Node nodeLvl4 = listOfSbscChildNodeList.item(idx4);
						if ( nodeLvl4 == null )
							continue;
						
						ContractInfo cntrPlan = new ContractInfo();
						String vendorCd = getNodeText(nodeLvl4, "n1:Vendor_Cd");
						// vendor cd 가 null 또는 KT 로 시작하는 경우만 정상 회선 처리
						if ( YappUtil.isNotEmpty(vendorCd) && vendorCd.startsWith("KT") == false )
							continue;

						cntrPlan.setPartyDetailTypeCd(partyDetailTypeCd);
						cntrPlan.setCntrcTypeCd(getNodeText(nodeLvl4, "n1:Subscpn_Type_Cd"));
						cntrPlan.setCntrcStatusCd(getNodeText(nodeLvl4, "n1:Subscpn_Status_Cd"));
						cntrPlan.setCntrNo(getNodeText(nodeLvl4, "n1:Nstep_Cn"));
						cntrPlan.setPpCd(getNodeText(nodeLvl4, "n1:PP_Cd"));
						cntrPlan.setVendorCd(vendorCd);
						cntrPlan.setMobileNo(getNodeText(nodeLvl4, "n1:User_Name"));
						cntrPlan.setBirthDate(birthDate);
						cntrPlan.setUserNm(YappUtil.blindNameToName(partyName, 1));
						cntrPlan.setOrgUserNm(partyName);
						cntrPlan.setFourteenYn(fourteenYn);
						cntrPlan.setEightteenYn(eightteenYn);
						cntrPlan.setNineteenYn(nineteenYn);
						cntrPlan.setTwentyNineYn(twentyNineYn);
						
						cntrInfoList.add(cntrPlan);
						
					}
				}
			}
		}
		
		NodeList listofKTSubscriptionList = (NodeList) xpath.evaluate("//sdp:ListofKTSubscription", doc, XPathConstants.NODESET);

		for ( int idx10 = 0; idx10 < listofKTSubscriptionList.getLength(); idx10++ ) 
		{
			Node nodeLvl10 = listofKTSubscriptionList.item(idx10);
			if ( nodeLvl10 == null )
				continue;
			
			NodeList arrayofktsubscriptionList = (NodeList) xpath.evaluate("//n2:arrayofktsubscription", nodeLvl10, XPathConstants.NODESET);
			for ( int idx11 = 0; idx11 < arrayofktsubscriptionList.getLength(); idx11++ ) 
			{
				Node nodeLvl11 = arrayofktsubscriptionList.item(idx11);
				if ( nodeLvl11 == null ){
					continue;
				}
				String vendorCd1 = getNodeText(nodeLvl11, "n2:Vendor_Cd");
				ContractInfo cntrPlan1 = new ContractInfo();
				// vendor cd 가 null 또는 KT 로 시작하는 경우만 정상 회선 처리
				if ( YappUtil.isNotEmpty(vendorCd1) && vendorCd1.startsWith("KT") == false ){
					continue;
				}

				cntrPlan1.setPartyDetailTypeCd(getNodeText(nodeLvl11, "n2:Party_Detail_Type_Cd"));
				cntrPlan1.setCntrcTypeCd(getNodeText(nodeLvl11, "n2:Subscpn_Type_Cd"));
				cntrPlan1.setCntrcStatusCd(getNodeText(nodeLvl11, "n2:Subscpn_Status_Cd"));
				cntrPlan1.setCntrNo(getNodeText(nodeLvl11, "n2:Nstep_Cn"));
				cntrPlan1.setPpCd(getNodeText(nodeLvl11, "n2:PP_Cd"));
				cntrPlan1.setVendorCd(vendorCd1);
				cntrPlan1.setMobileNo(getNodeText(nodeLvl11, "n2:User_Name"));
				
				cntrInfoList.add(cntrPlan1);
			}
		}
	}
	
	@Override
	protected String getErrCdPrefix()
	{
		return ERR_CD_PREFIX;
	}

}
