package com.kt.yapp.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 부가서비스 상품
 */
@Data
public class VasItem 
{
	@ApiModelProperty(value="부가서비스 상품코드")
	private String vasItemCd;

	@ApiModelProperty(value="부가서비스 상품명")
	private String vasItemNm;

	@ApiModelProperty(value="부가서비스명")
	private String vasNm;

	@ApiModelProperty(value="가격")
	private int price;

	@ApiModelProperty(value="데이터용량")
	private int dataAmt;

	@ApiModelProperty(value="부가서비스 설정 여부")
	private String applYn = "N";
	
	@ApiModelProperty(value="설정 시간 (My Time Plan or 3시간 무료 전용)")
	private String optTime;
	
	@ApiModelProperty(value="부가서비스 코드")
	@JsonIgnore
	private String vasCd;

	@ApiModelProperty(value="횟수 합계")
	@JsonIgnore
	private int sumDataCnt;

	@ApiModelProperty(value="데이터용량 합계")
	@JsonIgnore
	private int sumDataAmt;

	@ApiModelProperty(value="구매방법( G0001: 포인트 구매, G0002: 청구서 구매)")
	@JsonIgnore
	private String buyTp;
	
	@ApiModelProperty(value="순서")
	@JsonIgnore
	private int ordSeq;

	@ApiModelProperty(value="계약번호")
	@JsonIgnore
	private String cntrNo;
	
	@ApiModelProperty(value="가입유형(G0001 : 가입, G0002 : 해지, G0003 : 갱신)")
	@JsonIgnore
	private String applTp;
	
	@ApiModelProperty(value="가입년월")
	@JsonIgnore
	private String applYm;
	
	@ApiModelProperty(value="부가서비스 상품 아이디(연동)")
	@JsonIgnore
	private String vasItemId;

	@ApiModelProperty(value="사용여부")
	@JsonIgnore
	private String useYn;
	
	@ApiModelProperty(value="등록일시")
	@JsonIgnore
	private Date regDate;
	
	@ApiModelProperty(value="이번달 조회여부")
	@JsonIgnore
	private String isSrchCurMnth;
	
	@ApiModelProperty(value="동시간 조회여부")
	@JsonIgnore
	private String isSrchCurHour;
	
}
