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
public class CustAgreeInfoResponseDetail
{
	@ApiModelProperty(value="선택동의내역")
	private CustAgreeInfoRetvListDTO CustAgreeInfoRetvListDTO;
}
