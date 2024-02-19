package com.kt.yapp.soap.response;

import javax.xml.soap.SOAPMessage;

import com.kt.yapp.util.YappUtil;

import lombok.Getter;
import lombok.Setter;

/**
 * 데이터박스 현황 조회 호출 결과
 */
public class SoapResponseRetvDataStats extends YappSoapKosResponse
{
	private static final String ERR_CD_PREFIX = "retvDataStats";

	@Getter @Setter
	private int bonusDataAmt;
	
	@Getter @Setter
	private int promotionDataAmt;
	
	@Getter @Setter
	private int boxDataAmt;
	
	@Getter @Setter
	private String validYmd;
	
	@Getter @Setter
	private int dsprNextMnthDataAmt;
	
	@Getter @Setter
	private int dsprCurMnthDataAmt;
	
	@Getter @Setter
	private int giftDataAmt;
	
	@Getter @Setter
	private int giftCnt;
	
	@Getter @Setter
	private int pullDataAmt;
	
	@Getter @Setter
	private int pullCnt;
	
	public SoapResponseRetvDataStats(SOAPMessage response) throws Exception {
		super.parsingData(response);
	}

	@Override
	protected void setData() throws Exception
	{
		this.respTp = getNodeText("responseType");
		String sucessType  = getNodeText("KEY_01_SBST");
		if(sucessType != null && sucessType.equals("sucesYn")){
			sucesYn = getNodeText("KEY_VALUE_01_SBST");
			rtnCd = getNodeText("KEY_VALUE_02_SBST");
			rtnDesc = getNodeText("KEY_VALUE_03_SBST");
		}else{
			this.bonusDataAmt 		= (int) (YappUtil.toLong(getNodeText("KEY_VALUE_01_SBST"), 0) / 1024 / 1024);
			this.promotionDataAmt 	= (int) (YappUtil.toLong(getNodeText("KEY_VALUE_02_SBST"), 0) / 1024 / 1024);
			this.boxDataAmt 		= (int) (YappUtil.toLong(getNodeText("KEY_VALUE_03_SBST"), 0) / 1024 / 1024);
			this.validYmd 			= YappUtil.nToStr(getNodeText("KEY_VALUE_04_SBST"));
			this.dsprNextMnthDataAmt = (int) (YappUtil.toLong(getNodeText("KEY_VALUE_05_SBST"), 0) / 1024 / 1024);
			this.dsprCurMnthDataAmt = (int) (YappUtil.toLong(getNodeText("KEY_VALUE_06_SBST"), 0) / 1024 / 1024);
			this.giftDataAmt 		= (int) (YappUtil.toLong(getNodeText("KEY_VALUE_07_SBST"), 0) / 1024 / 1024);
			this.giftCnt 			= YappUtil.toInt(getNodeText("KEY_VALUE_08_SBST"), 0);
			this.pullCnt 			= YappUtil.toInt(getNodeText("KEY_VALUE_09_SBST"), 0);
			this.pullDataAmt 		= (int) (YappUtil.toLong(getNodeText("KEY_VALUE_09_SBST"), 0) / 1024 / 1024);
		}
	}

	@Override
	protected String getErrCdPrefix()
	{
		return ERR_CD_PREFIX;
	}
}
