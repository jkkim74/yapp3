package com.kt.yapp.soap.param;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kt.yapp.soap.SoapConnUtil;
import com.kt.yapp.soap.response.SoapResponseMemPointUse;

import lombok.Getter;
import lombok.Setter;

/**
 * 포인트 차감 호출을 위한 파라미터 클래스<br>
 */
public class SoapParamMemPointUse 
{
	@Getter
	private static final String elementKey = "pointUsePrcSvc";
	@Getter @Setter
	private String memType;
	@Getter @Setter
	private String memId;
	@Getter @Setter
	private String cntrNo;
	@Getter @Setter
	private String pointPdCd;
	@Getter @Setter
	private String usePoint;
	@Getter @Setter
	private String ordNo;
	@Getter @Setter
	private String custRelnCd;
	@Getter @Setter
	private String userNm;
	@Getter @Setter
	private String mobileNo;
	@Getter @Setter
	private String regrId;
	@Getter @Setter
	private String regrNm;
	@Getter @Setter
	private String regOfficeId;
	
	private SoapConnUtil soapConnUtil;
	
	private static final Logger logger = LoggerFactory.getLogger(SoapParamMemPointUse.class);

	
	public SoapParamMemPointUse(SoapConnUtil soapConnUtil) {
		this.soapConnUtil = soapConnUtil;
	}
	
	/**
	 * SHUB 인증 메소드를 호출한다.<br>
	 * 결과 반환값을 반환객체로 파싱하여 리턴한다.
	 */
	public SoapResponseMemPointUse execute() throws Exception
	{
		logger.info("point use start =====");
		SOAPMessage soapMessage = soapConnUtil.makeSoapMessageForMemPoint(SoapConnUtil.NAMESPACE_PUSE, SoapConnUtil.NAMESPACE_URI_PUSE);
		
		SOAPBody soapBody = soapMessage.getSOAPPart().getEnvelope().getBody();
		SOAPElement soapBodyElem = soapBody.addChildElement("pointUsePrcSvc", SoapConnUtil.NAMESPACE_PUSE);
		
		SOAPElement requestBodyElem = soapBodyElem.addChildElement("requestBody");

		soapConnUtil.makeParam(requestBodyElem, null, "COORP_CO_CD", 		"DATBX");
		soapConnUtil.makeParam(requestBodyElem, null, "MEMBER_TYPE", 		memType);	// 암호화(식별번호유형)
		soapConnUtil.makeParam(requestBodyElem, null, "MEMBER_ID",	 		memId);		// 암호화(식별번호)
		soapConnUtil.makeParam(requestBodyElem, null, "SA_ID", 				cntrNo);	// 암호화(계약번호)
		soapConnUtil.makeParam(requestBodyElem, null, "POINT_PD_CD", 		pointPdCd);	// 암호화(상품코드)
		soapConnUtil.makeParam(requestBodyElem, null, "ORD_POINT", 			usePoint);	// 암호화(사용 포인트)
		soapConnUtil.makeParam(requestBodyElem, null, "COORP_CO_ORD_NO", 	ordNo);		// 암호화(제휴사오더번호)
		soapConnUtil.makeParam(requestBodyElem, null, "REGR_ID", 			regrId);	// 암호화(등록자ID)
		soapConnUtil.makeParam(requestBodyElem, null, "REGR_NAME", 			regrNm);	// 암호화(등록자명)
		soapConnUtil.makeParam(requestBodyElem, null, "REG_OFFICE_ID",		regOfficeId);	// 암호화(등록자조직코드)
		soapConnUtil.makeParam(requestBodyElem, null, "CUST_RELN_CD", 		custRelnCd);// 암호화(관계코드: 1(본인))
		soapConnUtil.makeParam(requestBodyElem, null, "MEMBER_NAME", 		userNm);	// 암호화(사용자명)
		soapConnUtil.makeParam(requestBodyElem, null, "MEMBER_TEL_NO", 		mobileNo);	// 암호화(사용자 휴대폰번호)
		logger.info("point use soapMessage start ====="+soapMessage);

		SOAPMessage response = SoapConnUtil.call(soapConnUtil.soapEpUrlMemPointUse, soapMessage);
		
		logger.info("point use end ====="+ response);
		
		return new SoapResponseMemPointUse(response);
	}
}
