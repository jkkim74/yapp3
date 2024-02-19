package com.kt.yapp.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 데이턱
 */
@Data
public class Datuk 
{
	@ApiModelProperty(value="데이턱 용량")
	private int datukAmt;

	@ApiModelProperty(value="회수 대상 용량")
	private int rtnAmt;
	
	@ApiModelProperty(value="받아간 용량")
	private int rcvAmt;
	
	@ApiModelProperty(value="참여 마감일")
	private String joinEdYmd;

	@ApiModelProperty(value="회수 마감일")
	private String rtnEdYmd;
	
	@ApiModelProperty(value="데이턱 ID")
	private String datukId;

	@ApiModelProperty(value="고객명")
	private String userNm;

	@ApiModelProperty(value="참여 가능여부")
	@JsonIgnore
	private String isAbleJoin;
	
	@ApiModelProperty(value="계약번호")
	@JsonIgnore
	private String cntrNo;

	@ApiModelProperty(value="휴대폰")
	@JsonIgnore
	private String mobileNo;
	
	@ApiModelProperty(value="데이턱 생성년월")
	@JsonIgnore
	private String datukYm;
	
	@ApiModelProperty(value="등록일시")
	@JsonIgnore
	private Date regDt;

}
