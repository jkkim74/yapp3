package com.kt.yapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Y앱 가입정보
 */
@Data
public class JoinInfo 
{
	@ApiModelProperty(value="휴대폰번호")
	private String mobileNo;
	
	@ApiModelProperty(value="Yapp 가입여부")
	private String yappJoinYn;
	
	@ApiModelProperty(value="데이터박스 상태(G0001: 사용중, G0002: 해지, G0003: 정지)")
	private String dboxStatus;
	
	@ApiModelProperty(value="조르기 수신여부")
	private String reqRcvYn;
	
	@ApiModelProperty(value="단말기 ID")
	@JsonIgnore
	private String deviceId;
	
	@ApiModelProperty(value="단말기수정날짜")
	@JsonIgnore
	private String deviceChgDt;
	
	@ApiModelProperty(value="중복 ID")
	@JsonIgnore
	private String dupId;
}
