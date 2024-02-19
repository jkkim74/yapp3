package com.kt.yapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 시간옵션 조회
 */
@Data
public class TimeOption 
{
	@ApiModelProperty(value="시간옵션코드")
	private String paramCd;
	
	@ApiModelProperty(value="옵션코드")
	private String paramEfctCd;
	
	@ApiModelProperty(value="옵션코드명")
	private String paramEfctCdNm;
	
	@ApiModelProperty(value="시간옵션시작점")
	@JsonIgnore
	private String efctCdCtgSbst1;

	@ApiModelProperty(value="시간옵션문자열")
	@JsonIgnore
	private String efctCdCtgSbst2;
	
	
}
