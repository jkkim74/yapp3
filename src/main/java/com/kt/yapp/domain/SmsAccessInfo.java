package com.kt.yapp.domain;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * SMS API 접속 정보
 */
@Data
public class SmsAccessInfo 
{
	@ApiModelProperty(value="접근 IP 주소")
	private String accessIpAddr;
	
	@ApiModelProperty(value="비고")
	private String etc;
	
	@ApiModelProperty(value="등록일시")
	private Date regDt;
	
	@ApiModelProperty(value="카운트")
	private int count;
}
