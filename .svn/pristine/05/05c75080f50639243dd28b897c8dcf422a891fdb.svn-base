package com.kt.yapp.soap.response;

import javax.xml.soap.SOAPMessage;

import com.kt.yapp.domain.ContractInfo;
import com.kt.yapp.util.YappUtil;

import lombok.Getter;

/**
 * SHUB 인증 호출 결과 클래스<br>
 * (OIF_139)
 * 
 * @author seungman.yu
 * @since 2018. 10. 4.
 * @version 1.0
 * 
 * Modification Information
 * Mod Date		Modifier		Description
 * ========================================
 * 2018. 10. 4.	seungman.yu 	SR과제명
 * Copyright (c) 2018 KTDS, Inc. All Rights Reserved
 */
public class SoapResponse139 extends YappSoapResponse
{
	private static final String ERR_CD_PREFIX = "139";

	@Getter
	private ContractInfo cntrInfo = new ContractInfo();
	
	public SoapResponse139(SOAPMessage response) throws Exception {
		super.parsingData(response);
	}
	
	@Override
	protected void setData() throws Exception
	{
		cntrInfo.setCntrcTypeCd(getNodeText("sdp:Subscpn_Type_Cd"));
		cntrInfo.setCntrcStatusCd(getNodeText("sdp:Subscpn_Status_Cd"));
		cntrInfo.setCntrNo(getNodeText("sdp:Nstep_Cn"));
		cntrInfo.setPpCd(getNodeText("sdp:PP_Cd"));
		cntrInfo.setPpNm(getNodeText("sdp:PP_Name"));
		cntrInfo.setVendorCd(getNodeText("sdp:Vendor_Cd"));
		cntrInfo.setMobileNo(getNodeText("sdp:User_Name"));
		cntrInfo.setFourteenYn(YappUtil.checkFourteenYn(getNodeText("sdp:Birth_Date")));
		cntrInfo.setEightteenYn(YappUtil.checkEightteenYn(getNodeText("sdp:Birth_Date")));
		cntrInfo.setNineteenYn(YappUtil.checkNineteenYn(getNodeText("sdp:Birth_Date")));
		cntrInfo.setTwentyNineYn(YappUtil.checkTwentyNineYn(getNodeText("sdp:Birth_Date")));
		cntrInfo.setBirthDate(getNodeText("sdp:Birth_Date"));
		cntrInfo.setUserNm(YappUtil.blindNameToName(getNodeText("sdp:Party_Name"), 1));
		cntrInfo.setOrgUserNm(getNodeText("sdp:Party_Name"));
	}
	
	@Override
	protected String getErrCdPrefix()
	{
		return ERR_CD_PREFIX;
	}

}
