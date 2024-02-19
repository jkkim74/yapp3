package com.kt.yapp.domain.req;

import com.kt.yapp.domain.TermsAgree;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserInfoJoinReq 
{
	@ApiModelProperty(value="계약번호")
	private String cntrNo;
	
	@ApiModelProperty(value="휴대폰번호")
	private String mobileNo;

	@ApiModelProperty(value="14세여부")
	private String fourteenYn;

	@ApiModelProperty(value="푸시 수신여부")
	private String pushRcvYn;
	
	@ApiModelProperty(value="마케팅 수신여부")
	private String mktRcvYn;
	
	@ApiModelProperty(value="조르기 수신여부")
	private String reqRcvYn;

	@ApiModelProperty(value="약관 동의 정보")
	private TermsAgree termsAgree;
}
