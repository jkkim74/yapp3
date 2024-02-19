package com.kt.yapp.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * SMS발송 이력
 */
@Data
public class SendSmsLogInfo 
{
	@ApiModelProperty(value="서비스타입(YAPP,CMS)")
	private String serviceType;
	
	@ApiModelProperty(value="접근api 경로")
	private String accessUrl;
	
	@ApiModelProperty(value="수신자")
	private String rcvCtn;
	
	@ApiModelProperty(value="발송번호")
	private String callCtn;
	
	@ApiModelProperty(value="전송번호(114)")
	private String callbackCtn;
	
	@ApiModelProperty(value="전송메시지")
	private String msgContent;
	
	@ApiModelProperty(value="shub 트랜잭션ID")
	private String transactionId;
	
	@ApiModelProperty(value="통신결과코드")
	private String resultCd;
	
	@ApiModelProperty(value="통신결과 메시지")
	private String resultMsg;
	
	@ApiModelProperty(value="채번")
	private int logSeq;
	
}
