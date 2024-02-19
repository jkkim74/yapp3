package com.kt.yapp.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 데이터 나눔 상세
 */
@Data
public class DataDivDtl 
{
	@ApiModelProperty(value="사용 내용")
	private String divContents;
	
	@ApiModelProperty(value="사용 내용 타입 (DG: 선물하기, DT: 데이턱)")
	private String divContentsTp;
	
	@ApiModelProperty(value="휴대폰 번호")
	@JsonIgnore
	private String mobileNo;
	
	@ApiModelProperty(value="데이턱아이디")
	private String datukId;
	
	@ApiModelProperty(value="사용 데이터 용량(MB)")
	private int useDataAmt;
	
	@ApiModelProperty(value="잔여 데이터 용량(MB)")
	private int rmnDataAmt;
	
	@ApiModelProperty(value="회수 대상 데이턱 데이터 용량(MB)")
	private int rtnDatukDataAmt;

	@ApiModelProperty(value="데이턱 진행상태(G0001: 진행중, G0002: 회수하기, G0003: 회수완료, G0004: 종료)")
	private String rtnDatukStatus;
	
	@ApiModelProperty(value="사용일시")
	private Date useDate;

	@ApiModelProperty(value="유효시작일")
	private String efctStdt;

	@ApiModelProperty(value="사용일시 문자열")
	@JsonIgnore
	private String useDateStr;
}
