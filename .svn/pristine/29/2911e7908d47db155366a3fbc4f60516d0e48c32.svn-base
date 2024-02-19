package com.kt.yapp.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 프로모션 데이터
 */
@Data
public class PromotionData 
{
	@ApiModelProperty(value="데이터 용량(MB)")
	private int dataAmt;

	@ApiModelProperty(value="선물 메시지")
	private String msg;
	
	@ApiModelProperty(value="수신자 계약번호")
	@JsonIgnore
	private String rcvCntrNo;

	@ApiModelProperty(value="순번")
	@JsonIgnore
	private int seq;

	@ApiModelProperty(value="수령여부")
	@JsonIgnore
	private String rcvYn;
	
	@ApiModelProperty(value="요청년월")
	@JsonIgnore
	private String reqYm;

	@ApiModelProperty(value="등록일시")
	@JsonIgnore
	private Date regDt;
}
