package com.kt.yapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 데이터 박스
 */
@Data
public class Databox 
{
	@ApiModelProperty(value = "당월 소멸 예정 박스 데이터 용량(MB)")
	private int dboxDataAmtCurMnth;

	@ApiModelProperty(value = "익월 소멸 예정 박스 데이터 용량(MB)")
	private int dboxDataAmtNextMnth;

	@ApiModelProperty(value = "박스 데이터 총 용량(MB)")
	private int dboxDataAmt;

	@ApiModelProperty(value = "데이터 박스 기본 사이즈(MB)")
	private int dboxDataSize;
	
	@ApiModelProperty(value = "데이터 박스 꺼내기 정보")
	private DataboxPullInfo dboxPullInfo;

	@ApiModelProperty(value="선물한 횟수")
	@JsonIgnore
	private long giftCnt;
	
	@ApiModelProperty(value="선물한 용량(MB)")
	@JsonIgnore
	private long giftDataAmt;

	@ApiModelProperty(value = "데이터박스ID")
	@JsonIgnore
	private String dboxId;
	
	@ApiModelProperty(value="선물받기 가능 용량")
	private int giftRcvAmt;

}
