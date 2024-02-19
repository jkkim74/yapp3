package com.kt.yapp.soap.param;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPMessage;

import com.kt.yapp.soap.SoapConnUtil;
import com.kt.yapp.soap.response.SoapResponseMemPointAcu;

import lombok.Getter;
import lombok.Setter;

/**
 * 포인트 적립 호출을 위한 파라미터 클래스<br>
 */
public class SoapParamMemPointAcu 
{
	@Getter
	private static final String elementKey = "pointAcuPrcSvc";
	@Getter @Setter
	private String memType;
	@Getter @Setter
	private String memId;
	@Getter @Setter
	private String cntrNo;
	@Getter @Setter
	private String pointPdCd;
	@Getter @Setter
	private String pointPdDesc;
	@Getter @Setter
	private String acuPoint;
	@Getter @Setter
	private String pointCoorpCoCd;
	@Getter @Setter
	private String coorpCoOrdNo;
	
	private SoapConnUtil soapConnUtil;
	public SoapParamMemPointAcu(SoapConnUtil soapConnUtil) {
		this.soapConnUtil = soapConnUtil;
	}
	
	/**
	 * SHUB 인증 메소드를 호출한다.<br>
	 * 결과 반환값을 반환객체로 파싱하여 리턴한다.
	 */
	public SoapResponseMemPointAcu execute() throws Exception
	{
		SOAPMessage soapMessage = soapConnUtil.makeSoapMessageForMemPoint(SoapConnUtil.NAMESPACE_PACU, SoapConnUtil.NAMESPACE_URI_PACU);
		
		SOAPBody soapBody = soapMessage.getSOAPPart().getEnvelope().getBody();
		SOAPElement soapBodyElem = soapBody.addChildElement("pointAcuPrcSvc", SoapConnUtil.NAMESPACE_PACU);
		
		SOAPElement requestBodyElem = soapBodyElem.addChildElement("requestBody");

		soapConnUtil.makeParam(requestBodyElem, null, "COORP_CO_CD", 		"DATBX");
		soapConnUtil.makeParam(requestBodyElem, null, "MEMBER_TYPE", 		memType);		// 암호화(식별번호유형)
		soapConnUtil.makeParam(requestBodyElem, null, "MEMBER_ID", 			memId);			// 암호화(식별번호)
		soapConnUtil.makeParam(requestBodyElem, null, "SA_ID", 				cntrNo);		// 암호화(계약번호)
		soapConnUtil.makeParam(requestBodyElem, null, "POINT_PD_CD", 		pointPdCd);		// 암호화(상품코드: RAEXTDB001)
		soapConnUtil.makeParam(requestBodyElem, null, "POINT_PD_DESC", 		pointPdDesc);	// 암호화(상품상세정보)
		soapConnUtil.makeParam(requestBodyElem, null, "POINT", 				acuPoint);		// 암호화(적립 포인트)
		soapConnUtil.makeParam(requestBodyElem, null, "POINT_COORP_CO_CD", 	pointCoorpCoCd);// 암호화(제휴사상품코드: DATBX)
		soapConnUtil.makeParam(requestBodyElem, null, "COORP_CO_ORD_NO", 	coorpCoOrdNo);	// 암호화(처리오더번호)

		SOAPMessage response = SoapConnUtil.call(soapConnUtil.soapEpUrlMemPointAcu, soapMessage);
		
		return new SoapResponseMemPointAcu(response);
	}
}
