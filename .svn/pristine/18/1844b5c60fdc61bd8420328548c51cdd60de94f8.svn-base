package com.kt.yapp.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * SMS 발송
 */
@Data
public class SendSms 
{
	@ApiModelProperty(value="SMS seq")
	private int authSmsSeq;
	
	@ApiModelProperty(value="고객번호")
	private String cntrNo;
	
	@ApiModelProperty(value="실패횟수")
	private int failCnt;

	@ApiModelProperty(value="전화번호")
	private String revMobileNo;
	
	@ApiModelProperty(value="인증여부")
	private String authSmsYn;
	
	@ApiModelProperty(value="최근실패날짜")
	private Date lastFailDt;
	
	@ApiModelProperty(value="초기화여부")
	@JsonIgnore
	private String isInit;
	
	@ApiModelProperty(value="인증타입")
	private String authTp;
}
