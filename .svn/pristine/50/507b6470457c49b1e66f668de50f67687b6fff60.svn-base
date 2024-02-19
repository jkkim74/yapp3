package com.kt.yapp.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 누적 선물 데이터
 */
@Data
public class AccmGiftData 
{
	@ApiModelProperty(value="데이터 용량")
	private String dataAmt;
	
	@ApiModelProperty(value="요청타입 (G0001: 선물하기, G0002: 데이턱")
	private String reqTp;

	@ApiModelProperty(value="등록일시")
	private Date regDt;
	
	@ApiModelProperty(value="선물일시")
	@JsonIgnore
	private String sndDt;
	
	@ApiModelProperty(value="선물용량")
	@JsonIgnore
	private int sndDataAmt;
}
