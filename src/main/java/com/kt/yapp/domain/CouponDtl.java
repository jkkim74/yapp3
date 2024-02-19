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
public class CouponDtl extends RestCommon
{
	@ApiModelProperty(value="서비스 채널")
	@JsonIgnore
	private String svcchannelcode;
	
	@ApiModelProperty(value="쿠폰번호")
	private String serialnum;

	@ApiModelProperty(value="서비스 유형코드")
	@JsonIgnore
	private String svctypecode;

	@ApiModelProperty(value="쿠폰 유형코드")
	@JsonIgnore
	private String cpntypecode;

	@ApiModelProperty(value="쿠폰명")
	private String cpnname;

	@ApiModelProperty(value="쿠폰분류코드")
	@JsonIgnore
	private String cpncategorycode;

	@ApiModelProperty(value="할인유형코드")
	@JsonIgnore
	private String discounttypecode;
	
	@ApiModelProperty(value="쿠폰 value")
	private String cpnvalue;
	
	@ApiModelProperty(value="등록유효 시작일시")
	private String usestartdate;
	
	@ApiModelProperty(value="등록유효 종료일시")
	private String useenddate;
	
	@ApiModelProperty(value="쿠폰 등록일시")
	private String registrationdate;
	
	@ApiModelProperty(value="사용 요청일시")
	private String usedate;
	
	@ApiModelProperty(value="혜택개월수")
	private String benifitmonths;
	
	@ApiModelProperty(value="쿠폰상태코드")
	private String cpnstatecode;
	
	@ApiModelProperty(value="쿠폰 생성ID")
	private String creationid;
	
	@ApiModelProperty(value="청구금액")
	private String billingprice;
	
	@ApiModelProperty(value="패키지명")
	private String pkgnm;

	@ApiModelProperty(value="패키지ID")
	private String pkgid;

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

}
