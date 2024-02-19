package com.kt.yapp.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * psuh 결과 정보
 */
@Data
public class PushResult 
{
	@ApiModelProperty(value="device token ID")
	private String deviceTokenId;
	
	@ApiModelProperty(value="push 결과코드")
	private String pushRsltCd;
	
	@ApiModelProperty(value="push 에러코드")
	private String pushErrCd;
	
	@ApiModelProperty(value="push 메세지 ID")
	private String pushMessageId;
}
