package com.kt.yapp.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 고객동의 정보
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class CustAgreeInfoRetvListDTO
{
	@ApiModelProperty(value="선택동의내역")
	private List<CustAgreeInfoRetvListDtoDetail> custAgreeInfoRetvListDto;
	
	@ApiModelProperty(value="결과코드")
	private String resltCd;
	
	@ApiModelProperty(value="결과값")
	private String resltMsg;
}
