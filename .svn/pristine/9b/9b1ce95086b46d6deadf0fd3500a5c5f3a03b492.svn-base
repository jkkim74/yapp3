package com.kt.yapp.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 데이턱 수령
 */
@Data
public class DatukRcv 
{
	@ApiModelProperty(value="데이턱 ID")
	private String datukId;

	@ApiModelProperty(value="수령 고객 계약번호")
	private String rcvCntrNo;

	@ApiModelProperty(value="수령 고객 휴대폰")
	private String rcvMobileNo;
	
	@ApiModelProperty(value="수령 고객명")
	private String rcvUserNm;

	@ApiModelProperty(value="수령 용량")
	private int rcvAmt;
	
	@ApiModelProperty(value="데이턱 수령번호")
	@JsonIgnore
	private int datukRcvNo;

	@ApiModelProperty(value="등록일시")
	@JsonIgnore
	private Date regDt;

}
