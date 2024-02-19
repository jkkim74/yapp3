package com.kt.yapp.soap.response;

import javax.xml.soap.SOAPMessage;

import org.springframework.beans.factory.annotation.Autowired;

import com.kt.yapp.util.KeyFixUtilForRyt;
import com.kt.yapp.util.YappUtil;

import lombok.Getter;
import lombok.Setter;

/**
 * 포인트 적립 호출 결과 클래스
 */
public class SoapResponseMemPointAcu extends YappSoapResponse
{
	private static final String ERR_CD_PREFIX = "pointAcuPrcSvc";

	@Getter	/** 랜덤번호 */
	private String randomNo;
	@Getter	/** 잔여포인트 */
	private String rmnPoint;
	@Getter	/** 오더번호 */
	private String ordNo;
	@Getter	@Setter/** 제휴사오더번호 */
	private String coorpCoOrdNo;

	public SoapResponseMemPointAcu(SOAPMessage response) throws Exception {
		super.parsingData(response);
	}

	@Override
	protected void setData() throws Exception
	{
		rtnCd = getNodeText("ERR_CD");
		rtnDesc = YappUtil.findNotNullVal(getNodeText("ERR_MSG"));
		ordNo = getNodeText("ORD_NO");
	}

	@Override
	protected String getErrCdPrefix()
	{
		return ERR_CD_PREFIX;
	}
}
