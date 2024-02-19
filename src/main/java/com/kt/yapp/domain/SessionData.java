package com.kt.yapp.domain;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 세션 데이터
 */
@Data
public class SessionData implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8580369358253181116L;

	@ApiModelProperty(value="사용자ID)")
	private boolean existUser;
	
	@ApiModelProperty(value="사용자ID)")
	private String userId;
	
	@ApiModelProperty(value="이름")
	private String name;
	
	@ApiModelProperty(value="회원상태  G0001: 계약번호 + KTID, G0002: 계약번호 G0003: KTID)")
	private String memStatus;

	@ApiModelProperty(value="휴대폰 번호)")
	private String mobileNo;
	
	@ApiModelProperty(value="크레덴셜ID")
	private String credentialId;
	
	@ApiModelProperty(value="갱신일시")
	private Date updDt = new Date();
}
