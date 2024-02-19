package com.kt.yapp.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 부가서비스 상품
 */
@Data
public class VasItems 
{
	@ApiModelProperty(value="부가서비스 구분코드")
	private String vasTypeCd;

	@ApiModelProperty(value="부가서비스 코드")
	private String vasCd;

	@ApiModelProperty(value="부가서비스명")
	private String vasNm;

	@ApiModelProperty(value="시작일시")
	private Date vasStartDate;

	@ApiModelProperty(value="종료일시")
	private Date vasEndDate;
}
