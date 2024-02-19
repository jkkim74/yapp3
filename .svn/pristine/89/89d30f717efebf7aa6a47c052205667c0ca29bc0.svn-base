package com.kt.yapp.domain;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 내 잔여 데이터
 */
@Data
public class MyRmnData 
{
	@ApiModelProperty(value="총 제공량")
	private long myDataSize;
	
	@ApiModelProperty(value="총 잔여량")
	private long myRmnDataAmt;
	
	@ApiModelProperty(value="당월 소멸 예정 데이터")
	private long dsprCurMnthDataAmt;
	
	@ApiModelProperty(value="익월 소멸 예정 데이터")
	private long dsprNextMnthDataAmt;
	
	@ApiModelProperty(value="특정일 소멸 예정 데이터 목록")
	private List<DsprData> dsprDataList;
	
}
