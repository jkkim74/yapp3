package com.kt.yapp.soap.response;

import javax.xml.soap.SOAPMessage;
import javax.xml.xpath.XPathConstants;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.kt.yapp.util.YappUtil;

import lombok.Getter;

/**
 * SHUB 호출 결과 클래스<br>
 * (OIF_235)
 */
public class SoapResponse235 extends YappSoapResponse
{
	private static final String ERR_CD_PREFIX = "235";
	
	@Getter	/** 고객명 */
	private String partyName;
	@Getter	/** 요금제코드 */
	private String ppCd;
	@Getter	/** 계약 가입 구분코드 */
	private String subcpnJoinTypeCd;
	@Getter	/** 단말기 유형코드 */
	private String resourceTypeCd;
	@Getter	/** 단말기 모델명 */
	private String resourceModelName;
	@Getter	/** 단말기 모델ID */
	private String resourceUniqueId;
	@Getter	/** 생년월일 */
	private String birthDate;
	@Getter	/**  계약번호 */
	private String svcContId;
	
	public SoapResponse235(SOAPMessage response) throws Exception {
		super.parsingData(response);
	}

	@Override
	protected void setData() throws Exception
	{
		this.partyName = YappUtil.nToStr(getNodeText("sdp:party_name"));
		this.svcContId = YappUtil.nToStr(getNodeText("sdp:svc_cont_id"));
		this.ppCd = YappUtil.nToStr(getNodeText("sdp:pp_cd"));
		this.subcpnJoinTypeCd = YappUtil.nToStr(getNodeText("sdp:subcpn_join_type_cd"));
		this.birthDate = YappUtil.nToStr(getNodeText("sdp:birth_date"));
		System.out.println("BIRTHDATE : "+birthDate);
		NodeList listOfVasList =  (NodeList) xpath.evaluate("//sdp:listOfSubcpn", doc, XPathConstants.NODESET);
		System.out.println("==================================================================1");
		for ( int idx1 = 0; idx1 < listOfVasList.getLength(); idx1++ ) 
		{
			Node nodeLvl1 = listOfVasList.item(idx1);
			if ( nodeLvl1 == null ){
				continue;
			}
			NodeList listOfResourceNodeList = (NodeList) xpath.evaluate("//sdp:listOfResource", nodeLvl1, XPathConstants.NODESET);
			System.out.println("==================================================================2");
			for ( int idx2 = 0; idx2 < listOfResourceNodeList.getLength(); idx2++ ) 
			{
				Node nodeLvl2 = listOfResourceNodeList.item(idx2);
				if ( nodeLvl2 == null ){
					continue;
				}
				System.out.println("==================================================================3");
				System.out.println("RESOURCE_TYPE_CD : "+YappUtil.nToStr(getNodeText(nodeLvl2,"sdp:resource_type_cd")));
				if("06".equals(YappUtil.nToStr(getNodeText(nodeLvl2,"sdp:resource_type_cd")))){
					this.resourceUniqueId = YappUtil.nToStr(getNodeText(nodeLvl2,"sdp:resource_unique_id"));
					this.resourceModelName = YappUtil.nToStr(getNodeText(nodeLvl2,"sdp:resource_model_name"));
					System.out.println("RESOURCEMODELNAME : "+resourceModelName);
				}
				System.out.println("==================================================================4");
			}
			System.out.println("==================================================================5");
		}
	}

	@Override
	protected String getErrCdPrefix()
	{
		return ERR_CD_PREFIX;
	}
}
