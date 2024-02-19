package com.kt.yapp.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 앱 관련 정보
 */
@Data
public class AppInfo 
{
	@ApiModelProperty(value="앱 버전")
	private String appVrsn;
	
	@ApiModelProperty(value="단말 OS 유형(G0001: Android, G0002: IOS)")
	private String osTp;
	
	@ApiModelProperty(value="강제 업데이트 여부")
	private String forceUptYn;
	
	@ApiModelProperty(value="SNS로그인 활성화 여부")
	private String snsLoginYn;
	
	@ApiModelProperty(value="사용여부")
	@JsonIgnore
	private String useYn;
	
	@ApiModelProperty(value="등록일시")
	@JsonIgnore
	private Date regDt;

	@ApiModelProperty(value="수정일시")
	@JsonIgnore
	private Date modDt;

	/* 220310 Y 이면 가입/비번찾기버튼 숨김 */
	@ApiModelProperty(value="Y 이면 가입/비번찾기버튼 숨김")
	private String inReviewYn;
}
