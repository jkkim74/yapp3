package com.kt.yapp.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 데이턱 회수 정보
 */
@Data
public class DatukRtn 
{
	@ApiModelProperty(value="데이턱 용량")
	private int datukAmt;

	@ApiModelProperty(value="회수 대상 용량")
	private int rtnAmt;
	
	@ApiModelProperty(value="데이턱 ID")
	private String datukId;

	@ApiModelProperty(value="계약번호")
	@JsonIgnore
	private String cntrNo;

	@ApiModelProperty(value="등록일시")
	@JsonIgnore
	private Date regDt;

}
