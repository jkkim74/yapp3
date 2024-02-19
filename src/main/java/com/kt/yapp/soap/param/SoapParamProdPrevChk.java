package com.kt.yapp.soap.param;

import javax.xml.soap.SOAPMessage;

import com.kt.yapp.domain.AplyPplProdInfo;
import com.kt.yapp.domain.AplyProdParamInfo;
import com.kt.yapp.soap.SoapConnUtil;
import com.kt.yapp.soap.response.SoapResponseProdPrevChk;

import lombok.Getter;
import lombok.Setter;

/**
 * KOS 마이타일플랜 시간 설정전 상품 사전체크
 */
public class SoapParamProdPrevChk 
{
	@Getter @Setter /** 가입상품정보조회 목록 */
	private AplyPplProdInfo aplyPplProdInfo = new AplyPplProdInfo();
	
	@Getter @Setter /** 가입단위상품파람정보조회 목록 */
	private AplyProdParamInfo aplyProdParamInfo = new AplyProdParamInfo();
	
	private SoapConnUtil soapConnUtil;
	public SoapParamProdPrevChk (SoapConnUtil soapConnUtil) {
		this.soapConnUtil = soapConnUtil;
	}
	
	/**
	 * KOS 메소드를 호출한다.<br>
	 * 결과 반환값을 반환객체로 파싱하여 리턴한다.
	 */
	public SoapResponseProdPrevChk execute() throws Exception
	{
		SOAPMessage soapMessage = soapConnUtil.makeSoapMessageForKosProdPreChk("mobileProdPreChk", aplyPplProdInfo, aplyProdParamInfo);
		SOAPMessage response = SoapConnUtil.call(soapConnUtil.soapEpUrlKosDynamicSvc, soapMessage);
		
		return new SoapResponseProdPrevChk(response);
	}
}
