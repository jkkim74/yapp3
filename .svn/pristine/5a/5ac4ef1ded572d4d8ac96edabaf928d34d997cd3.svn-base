package com.kt.yapp.domain;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 선물 가능 정보
 */
@Data
public class GiftPsbInfo 
{
	@ApiModelProperty(value="선물한 횟수")
	private long giftCnt;
	
	@ApiModelProperty(value="내 데이터 잔여량")
	private long rmnDataAmt;
	
	@ApiModelProperty(value="선물한 용량(MB)")
	private long giftDataAmt;
	
	@ApiModelProperty(value="선물 가능용량(MB)")
	private long giftPsbDataAmt;
	
	@ApiModelProperty(value="월 선물 가능 최대횟수")
	private long giftPsbMaxCntPerMnth;

	@ApiModelProperty(value="월 선물 가능 최대용량(MB)")
	private long giftPsbMaxDataAmtPerMnth;
	
	@ApiModelProperty(value="1회 선물 가능 최대용량(MB)")
	private long giftPsbMaxDataAmtOneTime;
	
	@ApiModelProperty(value="선물하기 완료후의 최소 데이터 잔여량")
	private long giftMinDataAmtAfter;
	
	@ApiModelProperty(value = "최근 선물 일시")
	private Date maxRegDt;
}
