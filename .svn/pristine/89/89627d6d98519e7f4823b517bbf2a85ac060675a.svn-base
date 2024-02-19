package com.kt.yapp.soap.param;

import javax.xml.soap.SOAPMessage;

import com.kt.yapp.soap.SoapConnUtil;
import com.kt.yapp.soap.response.SoapResponseYdbSendSms;

import lombok.Getter;
import lombok.Setter;

/**
 * KOS 법정대리인조회
 */
public class SoapParamYdbSendSms 
{
	@Getter @Setter /** 전화번호 */
	private String telno = "";
	@Getter @Setter /** 문자발송 내용 */
	private String smsBody = "";
	@Getter @Setter /** 트랜잭션  ID */
	private String transId = "";
	@Getter @Setter /** 메세지 ID */
	private String msgId = "";
	
	private SoapConnUtil soapConnUtil;
	public SoapParamYdbSendSms (SoapConnUtil soapConnUtil) {
		this.soapConnUtil = soapConnUtil;
	}

	/**
	 * KOS 메소드를 호출한다.<br>
	 * 결과 반환값을 반환객체로 파싱하여 리턴한다.
	 */
	public SoapResponseYdbSendSms execute() throws Exception
	{
		SOAPMessage soapMessage = soapConnUtil.makeSoapMessageForKosSendSms("setYdbSendSms", telno, smsBody, transId, msgId);
		SOAPMessage response = SoapConnUtil.call(soapConnUtil.soapEpUrlKosSvc2, soapMessage);

		return new SoapResponseYdbSendSms(response);
	}
}
