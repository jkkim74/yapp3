package com.kt.yapp.soap.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.soap.SOAPMessage;
import javax.xml.xpath.XPathConstants;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.kt.yapp.domain.DataInfo;
import com.kt.yapp.domain.DatukDtl;
import com.kt.yapp.soap.response.SoapResponseRetvDataboxIosHst.DboxHist;
import com.kt.yapp.util.YappUtil;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * KOS 데이턱한턱쏘기 상세 조회 결과
 */
public class SoapResponseRetvDataOneShotDtl extends YappSoapKosResponse
{
	private static final String ERR_CD_PREFIX = "retrieveDrctlyUseQntDtl";

	@Getter @Setter
	private List<DatukDtl> datukDtlList = new ArrayList<>();
	
	public SoapResponseRetvDataOneShotDtl(SOAPMessage response) throws Exception {
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
				DatukDtl datukDtl = new DatukDtl();
				datukDtl.setDboxId(			getNodeText(nodeLvl1, "KEY_VALUE_01_SBST"));
				datukDtl.setEfctStDt(		getNodeText(nodeLvl1, "KEY_VALUE_02_SBST"));
				datukDtl.setOneShotData(	(int)YappUtil.toLong(getNodeText(nodeLvl1, "KEY_VALUE_03_SBST"),0));
				datukDtl.setClectData(		(int)YappUtil.toLong(getNodeText(nodeLvl1, "KEY_VALUE_04_SBST"), 0));
				datukDtl.setCmpltData(		(int)YappUtil.toLong(getNodeText(nodeLvl1, "KEY_VALUE_05_SBST"), 0));
				datukDtl.setExtncData(		(int)YappUtil.toLong(getNodeText(nodeLvl1, "KEY_VALUE_06_SBST"),0));
				datukDtl.setClectPosblData(	(int)YappUtil.toLong(getNodeText(nodeLvl1, "KEY_VALUE_07_SBST"),0));
				datukDtl.setNcmpltData(		(int)YappUtil.toLong(getNodeText(nodeLvl1, "KEY_VALUE_08_SBST"),0));
				datukDtl.setDatukId(		getNodeText(nodeLvl1, "KEY_VALUE_09_SBST"));
				datukDtl.setRecpDt(			YappUtil.nToStr(getNodeText(nodeLvl1, "KEY_VALUE_10_SBST")));
				datukDtlList.add(datukDtl);
			}
		}
	}

	@Override
	protected String getErrCdPrefix()
	{
		return ERR_CD_PREFIX;
	}
}
