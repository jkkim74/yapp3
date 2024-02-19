package com.kt.yapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * IOS APP Tracking INFO
 * @author dejay
 *
 */
@Data
public class AppIosTracking {

	@ApiModelProperty(value="계약번호")
	private String cntrNo;
	
	@ApiModelProperty(value="동의여부")
	private String agreeYn;
	
	@ApiModelProperty(value="OS구분")
	@JsonIgnore
	private String osTp;
	
	@ApiModelProperty(value="OS버전")
	@JsonIgnore
	private String osVrsn;
	
	@ApiModelProperty(value="APP버전")
	@JsonIgnore
	private String appVrsn;
	
	//220406
	@ApiModelProperty(value="사용자ID")
	private String userId;
	
}
