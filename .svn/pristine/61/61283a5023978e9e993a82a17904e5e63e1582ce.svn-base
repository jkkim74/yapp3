package com.kt.yapp.domain.req;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * EventReq.java : 이벤트 정보
 * 
 * @author seungman.yu
 * @since 2018. 10. 23.
 * @version 1.0
 * 
 * Modification Information
 * Mod Date		Modifier		Description
 * ========================================
 * 2018. 10. 23.	seungman.yu 	Y Event 기능 추가
 * Copyright (c) 2018 KTDS, Inc. All Rights Reserved
 */
@Data
public class EventReq 
{
	@ApiModelProperty(value="조회시작 INDEX")
	private int stIdx;
	
	@ApiModelProperty(value="조회종료 INDEX")
	private int edIdx;

	@ApiModelProperty(value="계약번호")
	@JsonIgnore
	private String cntrNo;
	
	@ApiModelProperty(value="배너여부")
	@JsonIgnore
	private String bannerYn;
	
	@ApiModelProperty(value="이벤트 seq")
	@JsonIgnore
	private int evtSeq;
	
	@ApiModelProperty(value="이벤트 구분")
	@JsonIgnore
	private String evtTp;
	
	@ApiModelProperty(value="이벤트 passYn")
	@JsonIgnore
	private String passYn;
}
