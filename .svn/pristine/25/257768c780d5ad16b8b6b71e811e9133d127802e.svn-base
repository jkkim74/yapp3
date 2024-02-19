package com.kt.yapp.domain;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 인증 실패
 */
@Data
public class AuthFail 
{
	/** 휴대폰 로그인 인증 타입 */
	public static final String AUTH_TP_MOBILE = "AUTH_TP_MOBILE";
	
	@ApiModelProperty(value="휴대폰 번호")
	private String mobileNo;

	@ApiModelProperty(value="인증 타입")
	private String authTp;

	@ApiModelProperty(value="실패횟수")
	private int failCnt;

	@ApiModelProperty(value="초기화여부")
	private String isInit;
	
	@ApiModelProperty(value="최근실패 일시")
	private Date lastFailDt;

	@ApiModelProperty(value="등록일")
	private Date regDt;
}
