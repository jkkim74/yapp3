package com.kt.yapp.soap.response;

import javax.xml.soap.SOAPMessage;

import lombok.Getter;

/**
 * SHUB 인증 호출 결과 클래스<br>
 * (OIF_107)
 */
public class SoapResponse107 extends YappSoapResponse
{
	private static final String ERR_CD_PREFIX = "107";
	
	@Getter	/** 크레덴설 ID */
	private String credentialId;
	@Getter	/** 크레덴설 타입 코드 */
	private String credentialTypeCd;
	@Getter	/** 성명 */
	private String partyName;
	@Getter	/** 휴대폰 번호 */
	private String mobileNo;
	@Getter	/** 인증 실패횟수 */
	private String authFailCnt;
	
	public SoapResponse107(SOAPMessage response) throws Exception {
		super.parsingData(response);
	}
	
	@Override
	protected void setData() throws Exception
	{
		this.credentialId = getNodeText("sdp:credential_id");
		this.credentialTypeCd = getNodeText("sdp:credential_type_code");
		this.partyName = getNodeText("sdp:party_name");
		this.mobileNo = getNodeText("sdp:phone_number");
		this.authFailCnt = getNodeText("sdp:login_fail_counter");
	}
	
	@Override
	protected String getErrCdPrefix()
	{
		return ERR_CD_PREFIX;
	}
}
