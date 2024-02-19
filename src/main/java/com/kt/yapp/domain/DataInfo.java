package com.kt.yapp.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 내 잔여 데이터
 */
@Data
public class DataInfo 
{
	@ApiModelProperty(value="데이터명")
	private String dataNm;
	
	@ApiModelProperty(value="데이터 용량(MB)")
	private int dataAmt;
	
	@ApiModelProperty(value="남은 데이터 용량(MB)")
	private int rmnDataAmt;

	@ApiModelProperty(value="사용 데이터 용량(MB)")
	private int useDataAmt;

	@ApiModelProperty(value="데이터 타입")
	private String dataTp;

	@ApiModelProperty(value="당월 소멸 예정 데이터 용량(MB)")
	private int dsprCurMnthDataAmt;
	
	@ApiModelProperty(value="익월 소멸 예정 데이터 용량(MB)")
	private int dsprNextMnthDataAmt;
	
	@ApiModelProperty(value="당월 소멸 예정 데이터 용량(MB)오리지널")
	private int orgDsprCurMnthDataAmt;
	
	@ApiModelProperty(value="당월 잔여 용량")
	private long tmonFreeQnt;

	@ApiModelProperty(value="선물하기 가능여부")
	private String giftPsYn;
	
}
