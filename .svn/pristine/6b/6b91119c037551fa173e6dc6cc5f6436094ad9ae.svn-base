package com.kt.yapp.domain;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 에러 메시지
 */
@Data
public class ErrMsg 
{
	@ApiModelProperty(value="에러메세지ID")
	private String errMsgId;
	
	@ApiModelProperty(value="에러메세지코드")
	private String errMsgCode;
	
	@ApiModelProperty(value="메시지 에러메세지사용구분(P:연동에러메세지 바로사용, S:연동에러메세지 치환사용, D:DB에러메세지 사용)")
	private String errMsgUseType;

	@ApiModelProperty(value="에러메세지(연동메세지 original)")
	private String errMsgOrg;
	
	@ApiModelProperty(value="에러메세지")
	private String errMsg;
	
	@ApiModelProperty(value="메시지 내용")
	private String remarks;
	
	@ApiModelProperty(value="등록일시")
	private Date regDt;
	
	@ApiModelProperty(value="수정일시")
	private Date modDt;
}
