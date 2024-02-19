package com.kt.yapp.soap.response;

import java.util.ArrayList;
import java.util.List;

import javax.xml.soap.SOAPMessage;
import javax.xml.xpath.XPathConstants;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.kt.yapp.domain.TimeOption;
import com.kt.yapp.util.YappUtil;

import lombok.Getter;
import lombok.Setter;

/**
 * KOS 내 데이터 상세 정보 조회 결과
 */
public class SoapResponseEfctCdRetv extends YappSoapKosResponse
{
	private static final String ERR_CD_PREFIX = "efctCdRetv";

	@Getter @Setter /** 시간옵션 목록 */
	private List<TimeOption> timeOptionInfoList = new ArrayList<>();
	
	public SoapResponseEfctCdRetv(SOAPMessage response) throws Exception {
		super.parsingData(response);
	}

	@Override
	protected void setData() throws Exception
	{
		respTp = getNodeText("responseType");

		NodeList listOfVasList = (NodeList) xpath.evaluate("//paramEfctCdRetvOutDTO", doc, XPathConstants.NODESET);
		for ( int idx1 = 0; idx1 < listOfVasList.getLength(); idx1++ ) 
		{
			Node nodeLvl1 = listOfVasList.item(idx1);
			if ( nodeLvl1 == null )
				continue;
			
			String paramCd = YappUtil.nToStr(getNodeText(nodeLvl1, "paramCd"));
			String paramEfctCd = YappUtil.nToStr(getNodeText(nodeLvl1, "paramEfctCd"));
			String paramEfctCdNm = YappUtil.nToStr(getNodeText(nodeLvl1, "paramEfctCdNm"));
			String efctCdCtgSbst1 = YappUtil.nToStr(getNodeText(nodeLvl1, "efctCdCtgSbst1"));
			String efctCdCtgSbst2 = YappUtil.nToStr(getNodeText(nodeLvl1, "efctCdCtgSbst2"));
			TimeOption timeOption = new TimeOption();
			timeOption.setParamCd(paramCd);
			timeOption.setParamEfctCd(paramEfctCdNm);
			timeOption.setParamEfctCdNm(paramEfctCdNm);
			timeOption.setEfctCdCtgSbst1(efctCdCtgSbst1);
			timeOption.setEfctCdCtgSbst2(efctCdCtgSbst2);
			timeOptionInfoList.add(timeOption);
		}
	}

	@Override
	protected String getErrCdPrefix()
	{
		return ERR_CD_PREFIX;
	}
}
