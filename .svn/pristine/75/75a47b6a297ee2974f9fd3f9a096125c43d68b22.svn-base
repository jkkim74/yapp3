package com.kt.yapp.soap.response;

import javax.xml.soap.SOAPMessage;

import org.springframework.beans.factory.annotation.Autowired;

import com.kt.yapp.util.KeyFixUtilForRyt;
import com.kt.yapp.util.YappUtil;

import lombok.Getter;
import lombok.Setter;

/**
 * 포인트 사용 호출 결과 클래스
 */
public class SoapResponseMemPointUse extends YappSoapResponse
{
	private static final String ERR_CD_PREFIX = "pointUsePrcSvc";

	@Getter	/** 랜덤번호 */
	private String randomNo;
	@Getter	/** 잔여포인트 */
	private String rmnPoint;
	@Getter	/** 오더번호 */
	private String ordNo;
	@Getter	@Setter/** 제휴사오더번호 */
	private String coorpCoOrdNo;

	public SoapResponseMemPointUse(SOAPMessage response) throws Exception {
		super.parsingData(response);
	}

	@Override
	protected void setData() throws Exception
	{
		rtnCd = getNodeText("RSLT_CD");
		rtnDesc = YappUtil.findNotNullVal(getNodeText("ERR_TEXT"));
		rmnPoint = getNodeText("RESID_POINT");
		ordNo = getNodeText("ORD_NO");
	}

	@Override
	protected String getErrCdPrefix()
	{
		return ERR_CD_PREFIX;
	}
}
