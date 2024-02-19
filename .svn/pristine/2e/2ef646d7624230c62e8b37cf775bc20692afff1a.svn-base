package com.kt.yapp.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 초대하기
 */
@Data
public class Invitation 
{
	@ApiModelProperty(value="초대 대상 휴대폰번호")
	private String invMobileNo;

	@ApiModelProperty(value="초대 대상자명")
	private String invUserNm;

	@ApiModelProperty(value="계약번호")
	@JsonIgnore
	private String cntrNo;
	
	@ApiModelProperty(value="사용자 아이디")
	@JsonIgnore
	private String userId;

	@ApiModelProperty(value="계약자명")
	@JsonIgnore
	private String userNm;

	@ApiModelProperty(value="등록일시")
	@JsonIgnore
	private Date regDt;
}
