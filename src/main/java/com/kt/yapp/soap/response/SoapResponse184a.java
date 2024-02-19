package com.kt.yapp.soap.response;

import java.util.ArrayList;
import java.util.List;

import javax.xml.soap.SOAPMessage;
import javax.xml.xpath.XPathConstants;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.kt.yapp.domain.VasItems;
import com.kt.yapp.util.YappUtil;

import lombok.Getter;

/**
 * SHUB 인증 호출 결과 클래스<br>
 * (OIF_184)
 */
public class SoapResponse184a extends YappSoapResponse
{
	private static final String ERR_CD_PREFIX = "184";
	
	@Getter
	private List<VasItems> vasItemList = new ArrayList<>();

	public SoapResponse184a(SOAPMessage response) throws Exception {
		super.parsingData(response);
	}

	@Override
	protected void setData() throws Exception
	{
		NodeList listOfVasList = (NodeList) xpath.evaluate("//sdp:listofprodsbsc", doc, XPathConstants.NODESET);

		for ( int idx1 = 0; idx1 < listOfVasList.getLength(); idx1++ ) 
		{
			VasItems vasItems = new VasItems();
			Node nodeLvl1 = listOfVasList.item(idx1);
			if ( nodeLvl1 == null ){
				continue;
			}
			vasItems.setVasTypeCd(getNodeText(nodeLvl1, "sdp:vas_type_cd"));
			vasItems.setVasCd(getNodeText(nodeLvl1, "sdp:vas_cd"));
			vasItems.setVasNm(getNodeText(nodeLvl1, "sdp:vas_name"));
			vasItems.setVasStartDate(YappUtil.getDate("yyyyMMddHHmmss", getNodeText(nodeLvl1, "sdp:vas_start_date").replace(" ", "").replace("-", "").replace(":", "")));
			vasItems.setVasEndDate(YappUtil.getDate("yyyyMMddHHmmss", getNodeText(nodeLvl1, "sdp:vas_end_date").replace(" ", "").replace("-", "").replace(":", "")));
			vasItemList.add(vasItems);
		}
	}

	@Override
	protected String getErrCdPrefix()
	{
		return ERR_CD_PREFIX;
	}
}
