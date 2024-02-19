package com.kt.yapp.soap.response;

import javax.xml.soap.SOAPMessage;
import javax.xml.xpath.XPathConstants;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import lombok.Getter;

/**
 * SHUB 인증 호출 결과 클래스<br>
 * (OIF_184)
 */
public class SoapResponse184 extends YappSoapResponse
{
	private static final String ERR_CD_PREFIX = "184";
	
	@Getter	/** 휴대폰 번호 */
	private String mobileNo;
	
	public SoapResponse184(SOAPMessage response) throws Exception {
		super.parsingData(response);
	}

	@Override
	protected void setData() throws Exception
	{
		NodeList listOfVasList = (NodeList) xpath.evaluate("//sdp:listofsvcno", doc, XPathConstants.NODESET);

		for ( int idx1 = 0; idx1 < listOfVasList.getLength(); idx1++ ) 
		{
			Node nodeLvl1 = listOfVasList.item(idx1);
			if ( nodeLvl1 == null ){
				continue;
			}
			this.mobileNo = getNodeText(nodeLvl1, "sdp:user_name");
		}
	}

	@Override
	protected String getErrCdPrefix()
	{
		return ERR_CD_PREFIX;
	}
}
