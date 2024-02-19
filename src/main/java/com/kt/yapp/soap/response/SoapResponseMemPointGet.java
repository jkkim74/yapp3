package com.kt.yapp.soap.response;

import javax.xml.soap.SOAPMessage;

import com.kt.yapp.domain.MemPointGet;
import com.kt.yapp.util.YappUtil;

import lombok.Getter;
import lombok.Setter;

/**
 * 포인트 조회 호출 결과 클래스
 */
public class SoapResponseMemPointGet extends YappSoapResponse
{
	private static final String ERR_CD_PREFIX = "CustCombineInfo";

	@Getter @Setter	/** member point 조회 */
	private MemPointGet memPointGet = new MemPointGet();
	
	public SoapResponseMemPointGet(SOAPMessage response) throws Exception {
		super.parsingData(response);
	}

	@Override
	protected void setData() throws Exception
	{
		rtnCd = getNodeText("RSLT_CD");
		rtnDesc = YappUtil.findNotNullVal(getNodeText("ERR_TEXT"));
		memPointGet.setMemId(getNodeText("MBR_ID"));
		memPointGet.setRmnPoint(getNodeText("RESID_POINT"));
		memPointGet.setPointUseYn(getNodeText("POINT_USE_LIMIT_YN"));
		memPointGet.setBrpYn(getNodeText("BRP_YN"));
		memPointGet.setMbrClCd(getNodeText("MBR_CL_CD"));
	}

	@Override
	protected String getErrCdPrefix()
	{
		return ERR_CD_PREFIX;
	}
}
