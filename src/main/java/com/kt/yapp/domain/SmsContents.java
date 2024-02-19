package com.kt.yapp.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * SMS 송신 내용
 */
@Data
public class SmsContents 
{
	@ApiModelProperty(value="MMS 제목")
	private String title;
	
	@ApiModelProperty(value="SMS 내용")
	private String msgContents;
	
	@ApiModelProperty(value="송신자 휴대폰번호")
	private String sendMobileNo;

	@ApiModelProperty(value="수신자 휴대폰번호")
	private String rcvMobileNo;
	
	@ApiModelProperty(value="접근api 경로")
	private String accessUrl;
	
	@ApiModelProperty(value="전송번호(114)'")
	private String callbackCtn;
	
	
}
