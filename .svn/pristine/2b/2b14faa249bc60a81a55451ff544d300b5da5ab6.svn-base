package com.kt.yapp.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * SNS 공유하기
 */
@Data
public class SnsShare 
{
	@ApiModelProperty(value="공유 대상 휴대폰번호")
	private String shareMobileNo;

	@ApiModelProperty(value="공유 대상자명")
	private String shareUserNm;

	@ApiModelProperty(value="공유 항목(G0001: 선물하기, G0002: 데이턱, G0003: 조르기, G0004: 초대하기)")
	private String shareItem;
	
	@ApiModelProperty(value="공유 타입(G0001: 카카오톡, G0002: 페이스북, G0003: 라인, G0004: SMS)")
	private String shareTp;

	@ApiModelProperty(value="계약번호")
	@JsonIgnore
	private String cntrNo;

	@ApiModelProperty(value="계약자명")
	@JsonIgnore
	private String userNm;

	@ApiModelProperty(value="등록일시")
	@JsonIgnore
	private Date regDt;
}
