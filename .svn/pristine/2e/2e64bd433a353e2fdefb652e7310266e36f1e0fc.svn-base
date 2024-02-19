package com.kt.yapp.soap.response;

import javax.xml.soap.SOAPMessage;

import lombok.Getter;

/**
 * 토큰 ID를 이용하여 인증 정보 획득<br>
 * (OIF_188)
 */
public class SoapResponse188 extends YappSoapResponse
{
	private static final String ERR_CD_PREFIX = "188";
	
	@Getter	/** 크레덴설 ID */
	private String credentialId;
	@Getter	/** 크레덴설 타입 코드 */
	private String credentialTypeCd;
	@Getter	/** 성명 */
	private String partyName;
	@Getter	/** 휴대폰 번호 */
	private String mobileNo;
	@Getter	/** 로그인 ID */
	private String loginId;
	
	public SoapResponse188(SOAPMessage response) throws Exception {
		super.parsingData(response);
	}
	
	@Override
	protected void setData() throws Exception
	{
		this.credentialId = getNodeText("sdp:credt_id");
		this.credentialTypeCd = getNodeText("sdp:credt_type_cd");
		this.partyName = getNodeText("sdp:partyName");
		this.loginId = getNodeText("sdp:user_name");
		this.mobileNo = getNodeText("sdp:phone_number");
	}
	
	@Override
	protected String getErrCdPrefix()
	{
		return ERR_CD_PREFIX;
	}
}
