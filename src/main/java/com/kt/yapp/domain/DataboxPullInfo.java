package com.kt.yapp.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 데이터 박스 꺼내기
 */
@Data
public class DataboxPullInfo 
{
	@ApiModelProperty(value = "데이터박스ID")
	@JsonIgnore
	private String dboxId;

	@ApiModelProperty(value = "데이터박스 꺼내기 가능 횟수")
	private int dboxMaxPullCnt;

	@ApiModelProperty(value = "데이터박스 꺼낸 횟수")
	private int dboxPullCnt;

	@ApiModelProperty(value = "데이터박스 꺼낸 용량(MB)")
	private int dboxPullDataAmt;
	
	@ApiModelProperty(value = "최근 꺼낸 일시")
	private Date maxRegDt;
	
}
