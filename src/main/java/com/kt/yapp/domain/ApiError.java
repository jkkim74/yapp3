package com.kt.yapp.domain;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * REST API 호출 에러 정보
 */
@Data
public class ApiError 
{
	@ApiModelProperty(value="고객번호")
	private String cntrNo;
	
	@ApiModelProperty(value="호출 IP 주소")
	private String callerIpAddr;
	
	@ApiModelProperty(value="API URL")
	private String apiUrl;
	
	@ApiModelProperty(value="에러 메시지")
	private String errMsg;

	@ApiModelProperty(value="에러 코드")
	private String errCd;

	@ApiModelProperty(value="에러 메세지 상세")
	private String errMsgDetail;
	
	@ApiModelProperty(value=" 메세지 구분코드")
	private String msgTypeCd;

	@ApiModelProperty(value=" 메세지 키값")
	private String msgKey;
	
	@ApiModelProperty(value="등록일시")
	private Date regDt;
	
	@ApiModelProperty(value="서버 IP 주소")
	private String accessServerIpAddr;
	
	//220406
	@ApiModelProperty(value="사용자ID")
	private String userId;
}
