package com.kt.yapp.soap.param;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPMessage;

import com.kt.yapp.soap.SoapConnUtil;
import com.kt.yapp.soap.response.SoapResponse2102;

import lombok.Getter;
import lombok.Setter;

/**
 * SHUB 인증 호출을 위한 파라미터 클래스<br>
 * (OIF_2102)
 */
public class SoapParam2102 
{
	@Getter
	private String elementKey = "MessageMTMMSReportNoNetCharge";
	@Getter @Setter
	private String msgContents;
	@Getter @Setter
	private String subject;
	@Getter @Setter
	private String sendMobileNo;
	@Getter @Setter
	private String rcvMobileNo;
	
	private SoapConnUtil soapConnUtil;
	public SoapParam2102(SoapConnUtil soapConnUtil) {
		this.soapConnUtil = soapConnUtil;
	}
	
	/**
	 * SHUB 인증 메소드를 호출한다.<br>
	 * 결과 반환값을 반환객체로 파싱하여 리턴한다.
	 */
	public SoapResponse2102 execute() throws Exception
	{
		SOAPMessage soapMessage = soapConnUtil.makeSoapMessage();
		
		SOAPBody soapBody = soapMessage.getSOAPPart().getEnvelope().getBody();
		SOAPElement soapBodyElem = soapBody.addChildElement(elementKey, SoapConnUtil.NAMESPACE_AG);
		
		// VAS ID
		soapConnUtil.makeParam(soapBodyElem, null, "TRID", "plany" + System.currentTimeMillis());
		soapConnUtil.makeParam(soapBodyElem, null, "VASID", "plany");
		// 메시지 전송방식: Push로 고정
		soapConnUtil.makeParam(soapBodyElem, null, "PUSH", "Push");
		// 컨텐츠 변환 여부 - true : MMSC에 Contents 변환 요청하지 않음 - false : MMSC에 Contents 변환 요청
		soapConnUtil.makeParam(soapBodyElem, null, "ISCONVERTED", "false");
		// 메시지 제목
		soapConnUtil.makeParam(soapBodyElem, null, "SUBJECT", subject);
		// 메시지내용
		StringBuffer sb = new StringBuffer()
				.append("Content-Type: text/plain; charset=\"euc-kr\"")
				.append("\n")
				.append("Content-ID : mmsToVasContent")
				.append("\n\n")
				.append(msgContents)
				.append("\n\n");

		soapConnUtil.makeParam(soapBodyElem, null, "ENC_CONTENT", sb.toString());
		soapConnUtil.makeParam(soapBodyElem, null, "CALL_CTN", sendMobileNo);
		soapConnUtil.makeParam(soapBodyElem, null, "CALLBACK_CTN", sendMobileNo);
		soapConnUtil.makeParam(soapBodyElem, null, "RCV_CTN", rcvMobileNo);	
		// ME/KUN/유선일 경우
		soapConnUtil.makeParam(soapBodyElem, null, "PFORM_TYPE", "1");
		// 무선
		soapConnUtil.makeParam(soapBodyElem, null, "SERVICE_TYPE", "A");
		// 착신자가 메시지를 정상적으로 읽었을 경우 발신자에게 SMS로 결과를 통지할지 유무- true : 통지함- false : 통지안함
		soapConnUtil.makeParam(soapBodyElem, null, "READNOTI", "false");
		// 착신자가 메시지를 정상적으로 받았을 경우 발신자에게 SMS로 결과를 통지할지 유무- true : 통지함- false : 통지안함
		soapConnUtil.makeParam(soapBodyElem, null, "RECEIVENOTI", "false");

		SOAPMessage response = SoapConnUtil.call(soapConnUtil.soapEpUrlMsg, soapMessage);
//		new SoapResponse2102(response);
//		soapMessage.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, "utf-8");
//		response = SoapConnUtil.call(soapConnUtil.soapEpUrlMsg, soapMessage);
//		new SoapResponse2102(response);
//		soapMessage.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, "euc-kr");
//		response = SoapConnUtil.call(soapConnUtil.soapEpUrlMsg, soapMessage);
		
		return new SoapResponse2102(response);
	}
}
