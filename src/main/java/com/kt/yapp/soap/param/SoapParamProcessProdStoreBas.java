package com.kt.yapp.soap.param;

import javax.xml.soap.SOAPMessage;

import com.kt.yapp.domain.AplyPplProdInfo;
import com.kt.yapp.domain.AplyProdParamInfo;
import com.kt.yapp.soap.SoapConnUtil;
import com.kt.yapp.soap.response.SoapResponseProcessProdStoreBas;

import lombok.Getter;
import lombok.Setter;

/**
 * KOS 마이타일플랜 시간 변경
 */
public class SoapParamProcessProdStoreBas 
{
	@Getter @Setter /** 가입상품정보조회 목록 */
	private AplyPplProdInfo aplyPplProdInfo = new AplyPplProdInfo();
	
	@Getter @Setter /** 가입단위상품파람정보조회 목록 */
	private AplyProdParamInfo aplyProdParamInfo = new AplyProdParamInfo();
	
	@Getter @Setter /** 변경시간옵션 */
	private String timeOption = "";
	
	private SoapConnUtil soapConnUtil;
	public SoapParamProcessProdStoreBas (SoapConnUtil soapConnUtil) {
		this.soapConnUtil = soapConnUtil;
	}
	
	/**
	 * KOS 메소드를 호출한다.<br>
	 * 결과 반환값을 반환객체로 파싱하여 리턴한다.
	 */
	public SoapResponseProcessProdStoreBas execute() throws Exception
	{
		SOAPMessage soapMessage = soapConnUtil.makeSoapMessageForKosProdStoreBas("processProdStoreBas", aplyPplProdInfo, aplyProdParamInfo, timeOption);
		SOAPMessage response = SoapConnUtil.call(soapConnUtil.soapEpUrlKosSvc, soapMessage);
		
		return new SoapResponseProcessProdStoreBas(response);
	}
}
