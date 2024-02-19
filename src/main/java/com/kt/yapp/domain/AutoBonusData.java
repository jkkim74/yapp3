package com.kt.yapp.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 자동 보너스 데이터
 */
@Data
public class AutoBonusData 
{
	@ApiModelProperty(value="보너스ID")
	private String bonusId;

	@ApiModelProperty(value="제목")
	private String title;

	@ApiModelProperty(value="보너스 용량(MB)")
	private int dataAmt;

	@ApiModelProperty(value="보너스 타입(G0001: 프로모션, G0002: 보너스)")
	private String bonusTp;

	@ApiModelProperty(value="설명")
	private String remarks;

	@ApiModelProperty(value="계약번호")
	private String cntrNo;
	
	@ApiModelProperty(value="받을 자동보너스 횟수")
	private int rcvDataCnt;
	
	@ApiModelProperty(value="선물한 데이터(MB)")
	private int sndDataAmt;
	
	@ApiModelProperty(value="보너스 데이터 회수")
	private int bonusDataCnt;
	
	@ApiModelProperty(value="보너스 데이터(MB)")
	private int bonusDataAmt;
}
