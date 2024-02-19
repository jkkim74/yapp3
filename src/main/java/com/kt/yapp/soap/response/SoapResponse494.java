package com.kt.yapp.soap.response;

import java.util.ArrayList;
import java.util.List;

import javax.xml.soap.SOAPMessage;
import javax.xml.xpath.XPathConstants;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.kt.yapp.domain.Coupon;
import com.kt.yapp.em.EnumYn;
import com.kt.yapp.util.YappUtil;

import lombok.Getter;

/**
 * SHUB 인증 호출 결과 클래스<br>
 * (OIF_494)
 */
public class SoapResponse494 extends YappSoapResponse
{
	private static final String ERR_CD_PREFIX = "494";

	@Getter	/** 쿠폰 목록 */
	private List<Coupon> couponList = new ArrayList<>();
	
	public SoapResponse494(SOAPMessage response) throws Exception {
		super.parsingData(response);
	}
	
	@Override
	protected void setData() throws Exception
	{
		NodeList customerList = (NodeList) xpath.evaluate("//sdp:ArrayListOfCouponInfo", doc, XPathConstants.NODESET);

		for ( int idx1 = 0; idx1 < customerList.getLength(); idx1++ ) 
		{
			Node nodeLvl1 = customerList.item(idx1);
			if ( nodeLvl1 == null )
				continue;

			Coupon cpnUse = new Coupon();
			cpnUse.setCpnNm(YappUtil.replaceCpnNm(getNodeText(nodeLvl1, "sdp:cpnname")).replaceAll("장기혜택쿠폰 ", ""));
			cpnUse.setCpnNo(getNodeText(nodeLvl1, "sdp:serialnum"));
			cpnUse.setCpnValidStYmd(getYmd(getNodeText(nodeLvl1, "sdp:usestartdate")));
			cpnUse.setCpnValidEdYmd(getYmd(getNodeText(nodeLvl1, "sdp:useenddate")));
			cpnUse.setCpnPrice(YappUtil.toInt(getNodeText(nodeLvl1, "sdp:billingprice"), 0));
			cpnUse.setPkgTpCd(getNodeText(nodeLvl1, "sdp:pkgtypecode"));
			
			String regYmd = getNodeText(nodeLvl1, "sdp:registrationdate");
			
			if ( YappUtil.isEmpty(regYmd) )
				cpnUse.setCpnUseYn(EnumYn.C_N.getValue());
			else
				cpnUse.setCpnUseYn(EnumYn.C_Y.getValue());
			
			couponList.add(cpnUse);
		}
	}
	
	private String getYmd(String date)
	{
		if ( YappUtil.isEmpty(date) || date.length() < 8)
			return null;
		
		return date.substring(0, 8);
	}

	@Override
	protected String getErrCdPrefix()
	{
		return ERR_CD_PREFIX;
	}
}
