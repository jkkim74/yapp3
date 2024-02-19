package com.kt.yapp.soap.response;

import java.util.ArrayList;
import java.util.List;

import javax.xml.soap.SOAPMessage;
import javax.xml.xpath.XPathConstants;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.kt.yapp.domain.IdsItem;

import lombok.Getter;

/**
 * SHUB 인증 호출 결과 클래스<br>
 * (OIF_103)
 */
public class SoapResponse003 extends YappSoapResponse
{
	private static final String ERR_CD_PREFIX = "103";
	
	@Getter	/** 아이디 목록 */
	private List<IdsItem> idsList = new ArrayList<>();
	
	public SoapResponse003(SOAPMessage response) throws Exception {
		super.parsingData(response);
	}

	@Override
	protected void setData() throws Exception
	{
		NodeList listOfIdsList = (NodeList) xpath.evaluate("//sdp:list_of_ids", doc, XPathConstants.NODESET);

		for ( int idx1 = 0; idx1 < listOfIdsList.getLength(); idx1++ ) 
		{
			Node nodeLvl1 = listOfIdsList.item(idx1);
			if ( nodeLvl1 == null )
				continue;
			
			IdsItem idsItem = new IdsItem();
			
			idsItem.setUserId(getNodeText(nodeLvl1, "sdp:user_name"));
			idsItem.setCredentialTypeCd(getNodeText(nodeLvl1, "sdp:credential_type_code"));
			idsItem.setCredentialDetailTypeCd(getNodeText(nodeLvl1, "sdp:credential_detail_type_code"));
			
			idsList.add(idsItem);
		}
	}

	@Override
	protected String getErrCdPrefix()
	{
		return ERR_CD_PREFIX;
	}
}
