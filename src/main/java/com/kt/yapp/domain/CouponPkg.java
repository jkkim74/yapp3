package com.kt.yapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 쿠폰 패키지
 */
@Data
public class CouponPkg
{

	@ApiModelProperty(value="패키지유형코드")
	private String pkgtypecode;

	@ApiModelProperty(value="패키지ID")
	private String pkgid;

	@ApiModelProperty(value="패키지명")
	private String pkgnm;
	
	@ApiModelProperty(value="서비스 유형코드")
	@JsonIgnore
	private String svctypecode;

	@ApiModelProperty(value="쿠폰번호")
	private String serialnum;

	@ApiModelProperty(value="쿠폰명")
	private String cpnname;
	
	@ApiModelProperty(value="쿠폰타입")
	private String cpnTp;
	
	@ApiModelProperty(value="패키지(쿠폰) 등록유효시작일")
	private String pkgregstartdate;

	@ApiModelProperty(value="패키지(쿠폰) 등록유효종료일")
	private String pkgregenddate;

	@ApiModelProperty(value="쿠폰 사용가능 총건수")
	private String cpntotalcnt;

	@ApiModelProperty(value="쿠폰 사용가능 잔여수")
	private String cpnusecnt;

	@ApiModelProperty(value="패키지 서비스코드")
	private String pkgsvccd;

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

	public String getPkgnm() {
		String tmpStr = "쿠폰";
		if("O".equals(this.pkgsvccd)) {
			tmpStr = "혜택쿠폰";
		}else if("V".equals(this.pkgsvccd)) {
			tmpStr = "특별쿠폰";
		}else{
			tmpStr = "기타쿠폰";
		}
		
		pkgnm = tmpStr;
		
		return pkgnm;
	}

	public String getPkgsvccd() {
		if(!"O".equals(this.pkgsvccd) && !"V".equals(this.pkgsvccd)) {
			pkgsvccd = "ETC";
		}
		return pkgsvccd;
	}
}
