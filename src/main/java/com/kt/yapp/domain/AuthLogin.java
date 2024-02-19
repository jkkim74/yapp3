package com.kt.yapp.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 휴대폰 로그인 인증
 */
@Data
public class AuthLogin 
{
//	@ApiModelProperty(value="휴대폰 번호")
//	private String mobileNo;

	@ApiModelProperty(value="인증 휴대폰 번호")
	private String authMobileNo;

	@ApiModelProperty(value="인증번호")
	private String authNum;
	
	@ApiModelProperty(value="RSA 인덱스 번호")
	private int keySeq;
	
	@ApiModelProperty(value="SNS타입(S0001:카카오톡,S0002:라인,S0003:구글,S0004:애플)")
	private String snsType;
	
	@ApiModelProperty(value="SNS로그인 ID")
	private String snsId;
	
	@ApiModelProperty(value="SNS로그인 토큰")
	private String snsToken;
	
	@ApiModelProperty(value="SNS로그인 (ios용 인증코드)")
	private String iosAuthCode;
}