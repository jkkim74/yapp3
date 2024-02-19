package com.kt.yapp.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 사용완료 쿠폰 목록
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class CouponEnd extends RestCommon
{
	@ApiModelProperty(value="서비스 채널")
	@JsonIgnore
	private String svcchannelcode;

	@ApiModelProperty(value="고객 ID")
	private String reqid;

	@ApiModelProperty(value="계약 ID")
	private String contractid;

	@ApiModelProperty(value="전체컨텐츠수")
	private String totalcontentnum;

	@ApiModelProperty(value="예약필드 01")
	private String reservation01;

	@ApiModelProperty(value="예약필드 02")
	private String reservation02;

	@ApiModelProperty(value="예약필드 03")
	private String reservation03;

	@ApiModelProperty(value="예약필드 04")
	private String reservation04;

	@ApiModelProperty(value="예약필드 05")
	private String reservation05;

	@ApiModelProperty(value="사용완료 쿠폰 List")
	private List<CouponEndInfo> end;

}
