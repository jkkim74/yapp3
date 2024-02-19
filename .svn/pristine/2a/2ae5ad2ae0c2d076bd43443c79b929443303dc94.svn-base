package com.kt.yapp.soap.response;

import javax.xml.soap.SOAPMessage;

import lombok.Getter;
import lombok.Setter;

/**
 * KOS 데이터 박스 기본 정보 조회 결과
 */
public class SoapResponseRetvDataboxBas extends YappSoapKosResponse
{
	private static final String ERR_CD_PREFIX = "retvDataboxBas";

	@Getter @Setter /** 데이터박스 ID */
	private String dboxId;
	
	public SoapResponseRetvDataboxBas(SOAPMessage response) throws Exception {
		super.parsingData(response);
	}

	@Override
	protected void setData() throws Exception
	{
		String sucessType  = getNodeText("KEY_01_SBST");
		if(sucessType != null && sucessType.equals("sucesYn")){
			sucesYn = getNodeText("KEY_VALUE_01_SBST");
			rtnCd = getNodeText("KEY_VALUE_02_SBST");
			rtnDesc = getNodeText("KEY_VALUE_03_SBST");
		} else {
			dboxId = getNodeText("KEY_VALUE_02_SBST");
		}
		
	}

	@Override
	protected String getErrCdPrefix()
	{
		return ERR_CD_PREFIX;
	}
}
