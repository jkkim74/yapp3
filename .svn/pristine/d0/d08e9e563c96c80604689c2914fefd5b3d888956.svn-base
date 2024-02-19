package com.kt.yapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * REST 연동 공통정보
 */
@Data
public class RestCommon 
{
	@ApiModelProperty(value="시스템발급일련번호")
	@JsonIgnore
	private String transactionid;

	@ApiModelProperty(value="시스템내부구간순서")
	@JsonIgnore
	private String sequenceno;

	@ApiModelProperty(value="오류코드")
	@JsonIgnore
	private String errorcode;

	@ApiModelProperty(value="오루설명")
	@JsonIgnore
	private String errordescription;

	@ApiModelProperty(value="결과코드")
	private String returncode;

	@ApiModelProperty(value="결과설명")
	@JsonIgnore
	private String returndescription;

}
