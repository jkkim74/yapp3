package com.kt.yapp.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OwnAuth 
{
	@ApiModelProperty(value="사용자명")
	private String userNm;
	
	@ApiModelProperty(value="생년월일")
	private String birthDate;
	
	@ApiModelProperty(value="SHUB 성별코드: (01: 여자, 02: 남자")
	private String genderCd;
	
	@ApiModelProperty(value="SHUB 외국인코드: (01: 내국인, 02: 외국인")
	private String foreignerCd;
	
	@ApiModelProperty(value="인증 확인번호")
	private String randomNo;
	
	@ApiModelProperty(value="인증번호")
	private String authCd;

	@ApiModelProperty(value="휴대폰번호")
	private String mobileNo;
	
	@ApiModelProperty(value="SHA256 암호화된 비밀번호")
	private String giftPw;
	
	@ApiModelProperty(value="kmc auth seq")
	private String kmcAuthSeq;
	
	@ApiModelProperty(value="kmc 휴대폰번호")
	private String phoneNumber;
	
	@ApiModelProperty(value="접근api 경로")
	private String accessUrl;
	
	@ApiModelProperty(value="전송번호(114)'")
	private String callbackCtn;
	
}
