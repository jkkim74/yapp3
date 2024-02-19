package com.kt.yapp.soap.param;

import javax.xml.soap.SOAPMessage;

import com.kt.yapp.domain.AplyPplProdInfo;
import com.kt.yapp.domain.AplyRoaming;
import com.kt.yapp.soap.SoapConnUtil;
import com.kt.yapp.soap.response.SoapResponseProdSbscTrmnStoreTrt;

import lombok.Getter;
import lombok.Setter;

/**
 * KOS 부가서비스 변경
 */
public class SoapParamProdSbscTrmnStoreTrt 
{
	@Getter @Setter /** 가입상품정보조회 목록 */
	private AplyRoaming aplyRoamingInfo = new AplyRoaming();
	
	@Getter @Setter /** 변경시간옵션 */
	private String timeOption = "";
	
	private SoapConnUtil soapConnUtil;
	public SoapParamProdSbscTrmnStoreTrt (SoapConnUtil soapConnUtil) {
		this.soapConnUtil = soapConnUtil;
	}
	
	/**
	 * KOS 메소드를 호출한다.<br>
	 * 결과 반환값을 반환객체로 파싱하여 리턴한다.
	 */
	public SoapResponseProdSbscTrmnStoreTrt execute() throws Exception
	{
		SOAPMessage soapMessage = soapConnUtil.makeSoapMessageForKosProdSbscTrmnStoreTrt("processProdStoreBas", aplyRoamingInfo);
		SOAPMessage response = SoapConnUtil.call(soapConnUtil.soapEpUrlKosSvc, soapMessage);
		
		return new SoapResponseProdSbscTrmnStoreTrt(response);
	}
}
