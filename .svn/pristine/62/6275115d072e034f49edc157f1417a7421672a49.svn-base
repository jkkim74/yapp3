package com.kt.yapp.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
KosAccess.java
 * 
 * @author ggiri
 * @since 2020. 11. 12.
 * @version 1.0
 * 
 * Modification Information
 * Mod Date		Modifier		Description
 * ========================================
 * Copyright (c) 2018 KTDS, Inc. All Rights Reserved
 */
@Data
public class KosAccess 
{
	@ApiModelProperty(value="계약번호")
	private String cntrNo;
	
	@ApiModelProperty(value="등록일시")
	@JsonIgnore
	private Date regDt;
	
	@ApiModelProperty(value="효출 IP 주소")
	private String callerIpAddr;
	
	@ApiModelProperty(value="API URL")
	private String apiUrl;
	
	@ApiModelProperty(value="API명")
	private String svcName;
	
	@ApiModelProperty(value="응답 메시지(타이틀)")
	private String responseTitle;
	
	@ApiModelProperty(value="응답 메시지(상세)")
	private String responseBasc;
	
	@ApiModelProperty(value="응답 메시지(상세2)")
	private String responseDtal;
	
	@ApiModelProperty(value="서버접속주소")
	private String accessServerIpAddr;
	
	@ApiModelProperty(value="응답메시지타입")
	private String responseType;
	
	@ApiModelProperty(value="응답메시지코드")
	private String responseCode;
	
	@ApiModelProperty(value="글로벌no")
	private String globalNo;
	
	@ApiModelProperty(value="비고")
	private String etc="";
}
