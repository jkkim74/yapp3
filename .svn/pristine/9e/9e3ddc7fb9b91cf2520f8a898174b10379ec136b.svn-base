package com.kt.yapp.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 서비스 탈퇴
 */
@Data
public class SvcOut 
{
	@ApiModelProperty(value="탈퇴사유유형(G0001: 이용빈도 낮음, G0002: 서비스 불만족, G0003: 기타)")
	private String svcoutTp;

	@ApiModelProperty(value="탈퇴사유내용")
	private String svcoutDesc;

	@ApiModelProperty(value="계약번호")
	@JsonIgnore
	private String cntrNo;
	
	@ApiModelProperty(value="아이디")
	@JsonIgnore
	private String userId;

	@ApiModelProperty(value="재가입여부")
	@JsonIgnore
	private String rejoinYn;

	@ApiModelProperty(value="탈퇴일시")
	@JsonIgnore
	private Date svcoutDt;

	@ApiModelProperty(value="수정일시")
	@JsonIgnore
	private Date modDt;
}
