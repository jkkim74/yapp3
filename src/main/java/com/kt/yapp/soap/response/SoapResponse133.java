package com.kt.yapp.soap.response;

import java.util.ArrayList;
import java.util.List;

import javax.xml.soap.SOAPMessage;
import javax.xml.xpath.XPathConstants;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.kt.yapp.domain.VasItem;

import lombok.Getter;

/**
 * SHUB 인증 호출 결과 클래스<br>
 * (OIF_103)
 */
public class SoapResponse133 extends YappSoapResponse
{
	private static final String ERR_CD_PREFIX = "103";
	
	@Getter	/** 부가서비스 상품 목록 */
	private List<VasItem> vasCdList = new ArrayList<>();
	
	public SoapResponse133(SOAPMessage response) throws Exception {
		super.parsingData(response);
	}

	@Override
	protected void setData() throws Exception
	{
		NodeList listOfVasList = (NodeList) xpath.evaluate("//sdp:ListOfVas", doc, XPathConstants.NODESET);

		for ( int idx1 = 0; idx1 < listOfVasList.getLength(); idx1++ ) 
		{
			Node nodeLvl1 = listOfVasList.item(idx1);
			if ( nodeLvl1 == null )
				continue;
			
			VasItem vasItem = new VasItem();
			vasItem.setVasItemCd(getNodeText(nodeLvl1, "sdp:Vas_Cd"));
			vasItem.setVasItemNm(getNodeText(nodeLvl1, "sdp:Vas_Name"));
			
			vasCdList.add(vasItem);
		}
	}

	@Override
	protected String getErrCdPrefix()
	{
		return ERR_CD_PREFIX;
	}
}
