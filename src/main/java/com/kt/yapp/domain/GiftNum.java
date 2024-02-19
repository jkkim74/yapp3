package com.kt.yapp.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 선물 횟수
 */
@Data
public class GiftNum 
{
	@ApiModelProperty(value="선물횟수")
	private int giftCnt;
	
	@ApiModelProperty(value="선물용량(MB)")
	private int giftAmt;
	
	@ApiModelProperty(value = "최근 선물 일시")
	private Date maxRegDt;
	
	@ApiModelProperty(value="선물타입(G0001: 선물하기, G0002: 데이턱")
	@JsonIgnore
	private String giftTp;
	
}
