package com.kt.yapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 데이터 제공량
 */
@Data
public class BigiChargBlnc 
{
	@ApiModelProperty(value="내 데이터 기본 사이즈(MB), 999999999는 무제한")
	private long myDataSize;

	@ApiModelProperty(value="잔여 알")
	private long myEggDataAmt;
	
	@ApiModelProperty(value="잔여 데이터_공통알(MB)")
	private long myRmnDataAmt;
	
	@ApiModelProperty(value="당월 제공량_기본알(MB)")
	private long curMnthDataAmt;
	
	@ApiModelProperty(value="이월 제공량_이월알(MB)")
	private long fwdMnthDataAmt;
	
	@ApiModelProperty(value="데이터 알(MB)(알요금제)")
	private long dataAmt;
	
	@ApiModelProperty(value="데이터 알(알요금제)")
	private long eggDataAmt;
	
	@ApiModelProperty(value="받은알(MB)(알요금제)")
	private long rcvDataAmt;
	
	@ApiModelProperty(value="받은알(알요금제)")
	private long eggRcvDataAmt;
	
	@ApiModelProperty(value="충전알(MB)(알요금제)")
	private long chgDataAmt;
	
	@ApiModelProperty(value="충전알(알요금제)")
	private long eggChgDataAmt;
	
	@ApiModelProperty(value="공통알(알요금제)")
	private long eggMyRmnDataAmt;
	
	@ApiModelProperty(value="기본알(알요금제)")
	private long eggCurMnthDataAmt;
	
	@ApiModelProperty(value="이월알(알요금제)")
	private long eggFwdMnthDataAmt;
	
	@ApiModelProperty(value="SMS 제공량(알요금제)")
	private long eggSmsDataAmt;
	
	@ApiModelProperty(value="사용 데이터(MB)")
	@JsonIgnore
	private long useDataAmt;
	
	@ApiModelProperty(value="데이터 조회시점")
	@JsonIgnore
	private String lastTlkTod;
	
	@ApiModelProperty(value="사용항목")
	@JsonIgnore
	private String unitSvcGroupHnglNm;
	
	@ApiModelProperty(value="서비스 코드")
	@JsonIgnore
	private String unitSvcGroupCd;
}
