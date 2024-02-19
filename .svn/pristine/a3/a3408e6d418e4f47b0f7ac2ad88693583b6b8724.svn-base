package com.kt.yapp.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 데이터 박스 상세
 */
@Data
public class DataboxDtl 
{
	@ApiModelProperty(value="이용 내역")
	private String dboxContents;
	
	@ApiModelProperty(value="이용 내역 타입(RD: 선물데이터, TD: 데이턱, BD: 보너스, PD: 프로모션, DO: 꺼내기, DP: 소멸)")
	private String dboxContentsTp;
	
	@ApiModelProperty(value="이용 타입(적립: G0001, 사용: G0002)")
	private String useTp;
	
	@ApiModelProperty(value="이용 데이터 용량(MB)")
	private int useDataAmt;
	
	@ApiModelProperty(value="잔여 데이터 용량(MB)")
	private int rmnDataAmt;
	
	@ApiModelProperty(value="이용월일")
	private Date useDate;

	@ApiModelProperty(value="데이터박스ID")
	@JsonIgnore
	private String dboxId;
	
}
