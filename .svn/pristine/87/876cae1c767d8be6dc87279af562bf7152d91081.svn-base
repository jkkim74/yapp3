package com.kt.yapp.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 쿠폰 패키지
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class CouponSearch extends RestCommon
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

	@ApiModelProperty(value="쿠폰패키지 List")
	private List<CouponPkg> pkg;
	
	@ApiModelProperty(value="쿠폰목록 List")
	private List<CouponPkg> pkgprod;

}
