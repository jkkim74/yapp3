package com.kt.yapp.soap.response;

import javax.xml.soap.SOAPMessage;
import javax.xml.xpath.XPathConstants;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.kt.yapp.util.YappUtil;

import lombok.Getter;
import lombok.Setter;

/**
 * KOS 내 데이터 상세 정보 조회 결과
 */
public class SoapResponseCondByModelInfoRetv extends YappSoapKosResponse
{
	private static final String ERR_CD_PREFIX = "condByModelInfoRetv";

	@Getter @Setter /** 5G여부 */
	private String fivegSuprtYn = "N";
	@Getter @Setter /** 모델정보 */
	private String intmModelNm = "";
	
	public SoapResponseCondByModelInfoRetv(SOAPMessage response, String intmModelNm) throws Exception {
		this.intmModelNm = intmModelNm;
		super.parsingData(response);
	}

	@Override
	protected void setData() throws Exception
	{
		
		NodeList listOfVasList = (NodeList) xpath.evaluate("//modelInfoDTO", doc, XPathConstants.NODESET);
		for ( int idx1 = 0; idx1 < listOfVasList.getLength(); idx1++ ) 
		{
			Node nodeLvl1 = listOfVasList.item(idx1);
			if ( nodeLvl1 == null ){
				continue;
			}
			String tmpModelNm = YappUtil.nToStr(getNodeText(nodeLvl1, "intmModelNm"));
			if(intmModelNm.equals(tmpModelNm)){
				fivegSuprtYn = YappUtil.nToStr(getNodeText(nodeLvl1, "fivegSuprtYn"));
			}
		}
	}

	@Override
	protected String getErrCdPrefix()
	{
		return ERR_CD_PREFIX;
	}
}
