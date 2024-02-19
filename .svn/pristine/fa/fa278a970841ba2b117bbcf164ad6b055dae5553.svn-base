package com.kt.yapp.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 데이터 제공량(잔여 데이터)
 */
@Data
public class WsgDataUseQntRmn 
{
	@ApiModelProperty(value="내 데이터 기본 사이즈(MB), 999999999는 무제한")
	private long myDataSize;
	
	@ApiModelProperty(value="잔여 데이터_공통알(MB)")
	private long myRmnDataAmt;
	
	@ApiModelProperty(value="공통알(알요금제)")
	private long eggMyRmnDataAmt;
	
	@ApiModelProperty(value="잔여 알")
	private long myEggDataAmt;
	
	@ApiModelProperty(value="당월 잔여 용량, 999999999는 무제한")
	private long tmonFreeQnt;
}
