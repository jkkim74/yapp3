package com.kt.yapp.soap.response;

import javax.xml.soap.SOAPMessage;

import com.kt.yapp.util.YappUtil;

import lombok.Getter;

/**
 * SHUB 인증 호출 결과 클래스<br>
 * (OIF_101)
 */
public class SoapResponse101 extends YappSoapResponse
{
	private static final String ERR_CD_PREFIX = "101";
	
	@Getter	/** 크레덴설 ID */
	private String credentialId;
	@Getter	/** 크레덴설 타입 코드 */
	private String credentialTypeCd;
	@Getter	/** 크레덴설 상세 타입 코드 */
	private String credentialDetailTypeCd;
	@Getter	/** 성명 (암호화) */
	private String encUserNm;
	@Getter	/** 성명 (평문) */
	private String orgUserNm;
	@Getter	/** 휴대폰 번호 */
	private String mobileNo;
	@Getter	/** 이메일 */
	private String email;
	@Getter	/** 성별 */
	private String gender;
	@Getter	/** 생년월일 yyyyMMdd */
	private String birthDate;

	@Getter	/** SDP 부여 고객 ID */
	private String partyId;
	@Getter	/** Party식별번호구분코드 */
	private String partyIdtfNumberCd;
	@Getter	/** Party 식별번호 1 */
	private String partyIdNumber1;
	
	public SoapResponse101(SOAPMessage response) throws Exception {
		super.parsingData(response);
	}

	@Override
	protected void setData() throws Exception
	{
		this.credentialId = getNodeText("sdp:credential_id");
		this.credentialTypeCd = getNodeText("sdp:credential_type_code");
		this.credentialDetailTypeCd = getNodeText("sdp:credential_detail_type_code");
		//TODO 암호화
		this.encUserNm = getNodeText("sdp:party_name");
		this.orgUserNm = getNodeText("sdp:party_name");
		this.mobileNo = getNodeText("sdp:phone_number");
		this.email = getNodeText("sdp:email");
		this.gender = getNodeText("sdp:gender");
		this.birthDate = getNodeText("sdp:birth_date");
		this.partyId = getNodeText("sdp:party_id");
		this.partyIdtfNumberCd = getNodeText("sdp:party_idtf_number_cd");
		this.partyIdNumber1 = getNodeText("sdp:party_id_number1");
	}

	@Override
	protected String getErrCdPrefix()
	{
		return ERR_CD_PREFIX;
	}
}
