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
public class CustAgreeInfoResponse extends RestCommon
{
	@ApiModelProperty(value="선택동의내역")
	private CustAgreeInfoResponseDetail response;
}
