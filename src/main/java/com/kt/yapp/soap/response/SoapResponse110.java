package com.kt.yapp.soap.response;

import javax.xml.soap.SOAPMessage;
import javax.xml.xpath.XPathConstants;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.kt.yapp.util.YappUtil;

import lombok.Getter;

/**
 * SHUB 인증 호출 결과 클래스<br>
 * (OIF_110)<br>
 * 휴대폰 번호로 크레덴셜ID 조회
 */
public class SoapResponse110 extends YappSoapResponse
{
	private static final String ERR_CD_PREFIX = "110";
	
	@Getter	/** 크레덴설 ID */
	private String userId;
	
	@Getter	/** 크레덴설 ID */
	private String credentialId;

	@Getter	/** 크레덴설 타입 코드 */
	private String credentialTypeCd;
	
	@Getter	/** 크레덴설 상세 타입 코드 */
	private String credentialDetailTypeCd;
	
	@Getter	/** 상품과 웹 ID 매핑여부 */
	private String mappingYn;

	public SoapResponse110(SOAPMessage response) throws Exception {
		super.parsingData(response);
	}
	
	@Override
	protected void setData() throws Exception
	{
		NodeList existingIdsList = (NodeList) xpath.evaluate("//sdp:existing_ids_list", doc, XPathConstants.NODESET);

		for ( int idx = 0; idx < existingIdsList.getLength(); idx++ ) 
		{
			Node nodeLvl2 = existingIdsList.item(idx);
			if ( nodeLvl2 == null )
				continue;
			
			String cid = getNodeText(nodeLvl2, "sdp:credential_id");
			String credentialTypeCd = getNodeText(nodeLvl2, "sdp:credential_type_code");
			String credentialDetailTypeCd = getNodeText(nodeLvl2, "sdp:credential_detail_type_code");
			
			if ( YappUtil.isNotEmpty(cid) ) {
				this.credentialId = cid;
				this.userId = getNodeText(nodeLvl2, "sdp:user_name");
				this.credentialTypeCd = credentialTypeCd;
				this.credentialDetailTypeCd = credentialDetailTypeCd;
				
				// olleh 통합 정보
				if ( YappUtil.isEq(credentialTypeCd, "01") && YappUtil.isEq(credentialDetailTypeCd, "00")){
					break;
				}
			}
		}
	}

	@Override
	protected String getErrCdPrefix()
	{
		return ERR_CD_PREFIX;
	}
}
