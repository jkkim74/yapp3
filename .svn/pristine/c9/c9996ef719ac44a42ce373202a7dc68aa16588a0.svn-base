package com.kt.yapp.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 선물하기 데이터
 */
@Data
public class GiftData 
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
	
	@ApiModelProperty(value="데이터 용량(MB)")
	private long dataAmt;
	
	@ApiModelProperty(value="조르기 수신여부")
	private String reqRcvYn;

	@ApiModelProperty(value="잔여 데이터 용량(MB)")
	@JsonIgnore
	private long rmnDataAmt;
	
	@ApiModelProperty(value="수신여부")
	@JsonIgnore
	private String rcvYn;
	
	@ApiModelProperty(value="요청 순번")
	@JsonIgnore
	private int seq;

	@ApiModelProperty(value="조회 제한 수")
	@JsonIgnore
	private int limitCnt;
	
	@ApiModelProperty(value="선물년월")
	@JsonIgnore
	private String giftYm;

	@ApiModelProperty(value="수신 확인여부")
	@JsonIgnore
	private String rcvConfirmYn;
	
	@ApiModelProperty(value="등록일시")
	@JsonIgnore
	private Date regDt;
	
	@ApiModelProperty(value="단말기수정날짜")
	@JsonIgnore
	private String deviceChgDt;
}
