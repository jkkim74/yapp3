package com.kt.yapp.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 데이터 조르기 정보
 */
@Data
public class DataReqInfo 
{
	@ApiModelProperty(value="송신자 계약번호")
	private String sndCntrNo;

	@ApiModelProperty(value="송신자 휴대폰번호")
	private String sndMobileNo;

	@ApiModelProperty(value="송신자명")
	private String sndUserNm;

	@ApiModelProperty(value="수신자 계약번호")
	private String rcvCntrNo;

	@ApiModelProperty(value="수신자 휴대폰번호")
	private String rcvMobileNo;

	@ApiModelProperty(value="수신자명")
	private String rcvUserNm;

	@ApiModelProperty(value="데이터 용량(mb)")
	private int dataAmt;

	@ApiModelProperty(value="요청년월")
	private String reqYm;

	@ApiModelProperty(value="요청 순번")
	@JsonIgnore
	private int seq;

	@ApiModelProperty(value="수신 확인여부")
	@JsonIgnore
	private String rcvConfirmYn;

	@ApiModelProperty(value="등록일시")
	@JsonIgnore
	private Date regDt;

	@ApiModelProperty(value="수정일시")
	@JsonIgnore
	private Date modDt;
}
