package com.kt.yapp.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 요금제
 */
@Data
public class CallingPlan 
{
	@ApiModelProperty(value="요금제 코드")
	private String ppCd;

	@ApiModelProperty(value="요금제명")
	private String ppNm;

	@ApiModelProperty(value="요금제 대분류(G0001: LTE, G0002: 3G, G0005: 5G)")
	private String ppCatL;
	
	@ApiModelProperty(value="요금제 소분류명")
	private String ppCatSNm;
	
	@ApiModelProperty(value="알요금제 여부")
	private String eggYn;
	
	@ApiModelProperty(value="가입 가능여부")
	private String joinPsYn;

	@ApiModelProperty(value="무한요금제 여부")
	private String infYn;

	@ApiModelProperty(value="y24 여부")
	private String y24Yn;

	@ApiModelProperty(value="y틴 여부")
	private String yteenYn;

	@ApiModelProperty(value="선물하기 가능여부")
	private String giftPsYn;

	@ApiModelProperty(value="데이터박스 사용 가능여부")
	private String dboxUsePsYn;

	@ApiModelProperty(value="장기혜택 쿠폰 가능여부")
	private String longPsYn;

	@ApiModelProperty(value="데이터 룰렛 가능여부")
	private String rouletPsYn;

	@ApiModelProperty(value="후불충전 가능여부")
	private String laterChgUsePsYn;

	@ApiModelProperty(value="멤버십 충전 가능여부")
	private String memChgUsePsYn;

	@ApiModelProperty(value="당겨쓰기 가능여부")
	private String pullPsYn;

	@ApiModelProperty(value="당겨쓰기 가능 금액")
	private int pullPsAmt;
	
	@ApiModelProperty(value="매일3시간무료 가능여부")
	private String threeUsePsYn;

	@ApiModelProperty(value="반값팩 가능여부")
	private String halfUsePsYn;

	@ApiModelProperty(value="두배쓰기 가능여부")
	private String dblUsePsYn;

	@ApiModelProperty(value="바꿔쓰기 가능여부")
	private String chgUsePsYn;

	@ApiModelProperty(value="5G 무제한")
	private String fginfYn;

	@ApiModelProperty(value="등록일시")
	@JsonIgnore
	private Date regDt;
}
