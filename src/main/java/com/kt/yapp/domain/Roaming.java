package com.kt.yapp.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Y로밍 정보
 */
@Data
public class Roaming 
{
	@ApiModelProperty(value="상품코드")
	private String vasItemCd;

	@ApiModelProperty(value="상품코드명")
	private String vasItemNm;

	@ApiModelProperty(value="상품ID")
	private String vasItemId;

	@ApiModelProperty(value="가격")
	private int price;

	@ApiModelProperty(value="이용기간")
	private String useTerm;

	@ApiModelProperty(value="데이터제공량")
	private int dataAmount;

	@ApiModelProperty(value="이미지1")
	private String img1;

	@ApiModelProperty(value="이미지2")
	private String img2;

	@ApiModelProperty(value="상품설명")
	private String itemContents;

	@ApiModelProperty(value="'이용기간 설정")
	private String useTermContents;

	@ApiModelProperty(value="할인내용")
	private String discountMemo;

	@ApiModelProperty(value="나이제한")
	private int age;

	@ApiModelProperty(value="이용시작날짜1")
	private String useStDt1;

	@ApiModelProperty(value="이용종료날짜1")
	private String useEdDt1;

	@ApiModelProperty(value="이용시작날짜2")
	private String useStDt2;

	@ApiModelProperty(value="이용종료날짜2")
	private String useEdDt2;

	@ApiModelProperty(value="이용시작날짜3")
	private String useStDt3;

	@ApiModelProperty(value="이용종료날짜3")
	private String useEdDt3;

	@ApiModelProperty(value="이용시작날짜4")
	private String useStDt4;

	@ApiModelProperty(value="이용종료날짜4")
	private String useEdDt4;

	@ApiModelProperty(value="비고1")
	private String remarks1;

	@ApiModelProperty(value="비고2")
	private String remarks2;

	@ApiModelProperty(value="비고3")
	private String remarks3;

	@ApiModelProperty(value="비고4")
	private String remarks4;

	@ApiModelProperty(value="사용여부")
	private String useYn;

	@ApiModelProperty(value="상태(N : 미사용, I : 사용(해지가능), C : 사용완료)")
	private String status;

	@ApiModelProperty(value="유효시작일")
	private String efctStDt;

	@ApiModelProperty(value="유효종료일")
	private String efctFnsDt;

	@ApiModelProperty(value="상품이력일련번호")
	private String prodHstSeq;
	
	@ApiModelProperty(value="상품구분(N:정상상품, B:블록상품")
	private String prodTypeCode;
}
