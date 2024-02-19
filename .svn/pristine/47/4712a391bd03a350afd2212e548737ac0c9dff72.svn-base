package com.kt.yapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 쿠폰 사용
 */
@Data
public class Coupon 
{
	@ApiModelProperty(value="쿠폰명")
	private String cpnNm;

	@ApiModelProperty(value="쿠폰번호")
	private String cpnNo;

	@ApiModelProperty(value="쿠폰 가격")
	private int cpnPrice;
	
	@ApiModelProperty(value="쿠폰 사용여부")
	private String cpnUseYn = "N";
	
	@ApiModelProperty(value="쿠폰 유효 시작일자")
	private String cpnValidStYmd;
	
	@ApiModelProperty(value="쿠폰 유효 종료일자")
	private String cpnValidEdYmd;

	@ApiModelProperty(value="쿠폰 타입(장기혜택쿠폰 LONGCPLTE: 데이터 LTE, LONGCP3G: 데이터 3G, LONGCPTV: 올레tv, LONGCP30M: 통화 30분, LONGCP5EGG: 기본알 5천알, 반값팩 쿠폰 HALFCPGENIE: 지니팩, HALFCPOTM: OTM 데일리팩, HALFCPDATA: 데이터충전)")
	private String cpnTp;
	
	@ApiModelProperty(value="패키지 유형 코드")
	@JsonIgnore
	private String pkgTpCd;
	
	@ApiModelProperty(value="고객번호")
	@JsonIgnore
	private String cntrNo;
	
	@ApiModelProperty(value="휴대폰번호")
	@JsonIgnore
	private String mobileNo;
	
	@ApiModelProperty(value="사용자ID")
	@JsonIgnore
	private String userId;

}
