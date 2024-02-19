package com.kt.yapp.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 법정대리인 정보(부모)
 */
@Data
public class ParentsInfo 
{
	@ApiModelProperty(value="전화번호(마스킹)")
	private String telno;
	
	@ApiModelProperty(value="가상주민번호")
	private String custIdfyNo;
	
	@ApiModelProperty(value="고객번호")
	private String custId;
	
	@ApiModelProperty(value="고객이름(마스킹)")
	private String custNm;
	
	@ApiModelProperty(value="전화번호")
	private String parentsTelno;
}
