package com.kt.yapp.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 보너스 데이터
 */
@Data
public class BonusData 
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

	@ApiModelProperty(value="지급 시작일자")
	private String rcvStYmd;

	@ApiModelProperty(value="지급 종료일자")
	private String rcvEdYmd;

	@ApiModelProperty(value="수령 대상 계약번호(복수 가능, ALL은 전체)")
	@JsonIgnore
	private String rcvCntrNo;

	@ApiModelProperty(value="계약번호")
	@JsonIgnore
	private String cntrNo;
	
	@ApiModelProperty(value="조건_요금제 코드")
	@JsonIgnore
	private String condPpCd;

	@ApiModelProperty(value="조건_선물(최소)용량")
	@JsonIgnore
	private int condGiftDataAmt;

	@ApiModelProperty(value="조건_선물(최소)횟수")
	@JsonIgnore
	private int condGiftCnt;

	@ApiModelProperty(value="조건_데이턱(최소)용량")
	@JsonIgnore
	private int condDatukDataAmt;

	@ApiModelProperty(value="조건_데이턱(최소)횟수")
	@JsonIgnore
	private int condDatukCnt;

	@ApiModelProperty(value="조건_초대하기(최소)횟수")
	@JsonIgnore
	private int condInvtCnt;

	@ApiModelProperty(value="조건_선착순명")
	@JsonIgnore
	private int condArrvOrd;

	@ApiModelProperty(value="유효기간")
	@JsonIgnore
	private String expYmd;

	@ApiModelProperty(value="등록일시")
	@JsonIgnore
	private Date regDt;
	
	@ApiModelProperty(value="수신완료 메세지")
	private String rcvMsg;
}
