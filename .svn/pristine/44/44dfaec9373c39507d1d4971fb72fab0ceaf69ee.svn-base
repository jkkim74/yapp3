package com.kt.yapp.soap.response;

import java.util.ArrayList;
import java.util.List;

import javax.xml.soap.SOAPMessage;
import javax.xml.xpath.XPathConstants;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.kt.yapp.util.YappUtil;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * KOS 데이터 입출력 이력 조회 결과
 */
public class SoapResponseRetvDataboxIosHst extends YappSoapKosResponse
{
	private static final String ERR_CD_PREFIX = "RetvDatabox";

	@Getter @Setter
	private List<DboxHist> dboxHistList = new ArrayList<>();
	
	public SoapResponseRetvDataboxIosHst(SOAPMessage response) throws Exception {
		super.parsingData(response);
	}

	@Override
	protected void setData() throws Exception
	{
		NodeList dynamicKeyValueList = (NodeList) xpath.evaluate("//dynamicKeyValueDTO", doc, XPathConstants.NODESET);

		for ( int idx1 = 0; idx1 < dynamicKeyValueList.getLength(); idx1++ ) 
		{
			Node nodeLvl1 = dynamicKeyValueList.item(idx1);
			if ( nodeLvl1 == null )
				continue;
			String sucessType  = getNodeText(nodeLvl1,"KEY_01_SBST");
			if(sucessType != null && sucessType.equals("sucesYn")){
				sucesYn = getNodeText(nodeLvl1,"KEY_VALUE_01_SBST");
				rtnCd = getNodeText(nodeLvl1,"KEY_VALUE_02_SBST");
				rtnDesc = getNodeText(nodeLvl1,"KEY_VALUE_03_SBST");
			}else{
				DboxHist hist = new DboxHist();
				hist.setDboxId(		getNodeText(nodeLvl1, "KEY_VALUE_01_SBST"));
				hist.setTarDboxId(	getNodeText(nodeLvl1, "KEY_VALUE_02_SBST"));
				hist.setTrtDate(	getNodeText(nodeLvl1, "KEY_VALUE_03_SBST"));
				hist.setTrtQnt(		(int) (YappUtil.toLong(getNodeText(nodeLvl1, "KEY_VALUE_04_SBST"), 0) / 1024 / 1024));
				hist.setRmndQnt(	(int) (YappUtil.toLong(getNodeText(nodeLvl1, "KEY_VALUE_05_SBST"), 0) / 1024 / 1024));
				hist.setTrtDivCd(	getNodeText(nodeLvl1, "KEY_VALUE_06_SBST"));
				hist.setTrtDivNm(	getNodeText(nodeLvl1, "KEY_VALUE_07_SBST"));
				hist.setCustNm(		getNodeText(nodeLvl1, "KEY_VALUE_08_SBST"));
				hist.setSvcNo(		getNodeText(nodeLvl1, "KEY_VALUE_09_SBST"));
				hist.setDboxIosCd(	getNodeText(nodeLvl1, "KEY_VALUE_10_SBST"));
				hist.setEfctStDt(	getNodeText(nodeLvl1, "KEY_VALUE_11_SBST"));
				hist.setEfctFnsDt(	getNodeText(nodeLvl1, "KEY_VALUE_12_SBST"));
				hist.setSvcContId(	getNodeText(nodeLvl1, "KEY_VALUE_13_SBST"));
				hist.setDatukId(	getNodeText(nodeLvl1, "KEY_VALUE_16_SBST"));
				
				dboxHistList.add(hist);
			}
			
		}
	}

	@Override
	protected String getErrCdPrefix()
	{
		return ERR_CD_PREFIX;
	}
	
	@Data
	public class DboxHist
	{
		/** 데이터 박스 ID */
		private String dboxId;
		/** 대상 데이터 박스 ID */
		private String tarDboxId;
		/** 처리일시 */
		private String trtDate;
		/** 처리량 */
		private int trtQnt;
		/** 잔여량 */
		private int rmndQnt;
		/** 처리구분코드 */
		private String trtDivCd;
		/** 처리구분명 */
		private String trtDivNm;
		/** 고객명 */
		private String custNm;
		/** 서비스 번호 */
		private String svcNo;
		/** 데이터박스 입출력코드 */
		private String dboxIosCd;
		/** 유효시작일 */
		private String efctStDt;
		/** 유효종료일 */
		private String efctFnsDt;
		/** 계약ID */
		private String svcContId;
		/** datuk ID */
		private String datukId;
	}
}
