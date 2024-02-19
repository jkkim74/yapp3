package com.kt.yapp.soap.response;

import javax.xml.soap.SOAPMessage;
import javax.xml.xpath.XPathConstants;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.kt.yapp.domain.AplyPplProdInfo;
import com.kt.yapp.domain.AplyProdParamInfo;
import com.kt.yapp.util.YappUtil;

import lombok.Getter;
import lombok.Setter;

/**
 * KOS 기가입상품정보조회 결과
 */
public class SoapResponseAplyProdInfoRetv extends YappSoapKosResponse
{
	private static final String ERR_CD_PREFIX = "aplyProdInfoRetv";

	@Getter @Setter /** 가입상품정보조회 목록 */
	private AplyPplProdInfo aplyPplProdInfo = new AplyPplProdInfo();
	
	@Getter @Setter /** 가입단위상품파람정보조회 목록 */
	private AplyProdParamInfo aplyProdParamInfo = new AplyProdParamInfo();
	
	public SoapResponseAplyProdInfoRetv(SOAPMessage response) throws Exception {
		super.parsingData(response);
	}

	@Override
	protected void setData() throws Exception
	{
		respTp = getNodeText("responseType");

		NodeList listOfVasList = (NodeList) xpath.evaluate("//aplyPplProdInfoDto", doc, XPathConstants.NODESET);
		for ( int idx1 = 0; idx1 < listOfVasList.getLength(); idx1++ ) 
		{
			Node nodeLvl1 = listOfVasList.item(idx1);
			if ( nodeLvl1 == null )
				continue;
			aplyPplProdInfo.setSvcContId(YappUtil.nToStr(getNodeText(nodeLvl1, "svcContId")));
			aplyPplProdInfo.setTrmnPosblYn(YappUtil.nToStr(getNodeText(nodeLvl1, "trmnPosblYn")));
			aplyPplProdInfo.setProdId(YappUtil.nToStr(getNodeText(nodeLvl1, "prodId")));
			aplyPplProdInfo.setProdNm(YappUtil.nToStr(getNodeText(nodeLvl1, "prodNm")));
			aplyPplProdInfo.setProdTypeCd(YappUtil.nToStr(getNodeText(nodeLvl1, "prodTypeCd")));
			aplyPplProdInfo.setEfctStDt(YappUtil.nToStr(getNodeText(nodeLvl1, "efctStDt")));
			aplyPplProdInfo.setEfctFnsDt(YappUtil.nToStr(getNodeText(nodeLvl1, "efctFnsDt")));
			aplyPplProdInfo.setProdHstSeq(YappUtil.nToStr(getNodeText(nodeLvl1, "prodHstSeq")));
		}
		
		NodeList listOfVasList2 = (NodeList) xpath.evaluate("//aplyProdParamInfoDto", doc, XPathConstants.NODESET);
		for ( int idx2 = 0; idx2 < listOfVasList2.getLength(); idx2++ ) 
		{
			Node nodeLvl2 = listOfVasList2.item(idx2);
			if ( nodeLvl2 == null )
				continue;
			// MTPLN3 마이타임 플랜인 것만 추출한다.
			if(YappUtil.nToStr(getNodeText(nodeLvl2, "unitSvcId")).equals("MTPLN3")){
				aplyProdParamInfo.setSvcContId(YappUtil.nToStr(getNodeText(nodeLvl2, "svcContId")));
				aplyProdParamInfo.setProdId(YappUtil.nToStr(getNodeText(nodeLvl2, "prodId")));
				aplyProdParamInfo.setUnitSvcId(YappUtil.nToStr(getNodeText(nodeLvl2, "unitSvcId")));
				aplyProdParamInfo.setUnitSvcNm(YappUtil.nToStr(getNodeText(nodeLvl2, "unitSvcNm")));
				aplyProdParamInfo.setProdHstSeq(YappUtil.nToStr(getNodeText(nodeLvl2, "prodHstSeq")));
				aplyProdParamInfo.setProdTypeCd(YappUtil.nToStr(getNodeText(nodeLvl2, "prodTypeCd")));
				aplyProdParamInfo.setEfctStDt(YappUtil.nToStr(getNodeText(nodeLvl2, "efctStDt")));
				aplyProdParamInfo.setEfctFnsDt(YappUtil.nToStr(getNodeText(nodeLvl2, "efctFnsDt")));
				aplyProdParamInfo.setParamSbst(YappUtil.nToStr(getNodeText(nodeLvl2, "paramSbst")));
			}
		}
	}

	@Override
	protected String getErrCdPrefix()
	{
		return ERR_CD_PREFIX;
	}
}
