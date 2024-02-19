package com.kt.yapp.domain.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 요금제
 */
@Data
public class CallingPlanResp 
{
	@ApiModelProperty(value="요금제명")
	private String ppNm;

	@ApiModelProperty(value="가입 가능여부")
	private String joinPsYn;

	@ApiModelProperty(value="선물하기 가능여부")
	private String giftPsYn;
}
