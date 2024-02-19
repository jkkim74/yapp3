package com.kt.yapp.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * EventContent.java : 이벤트 정보 컨텐츠
 * 
 * @author kkb
 * @since 2020. 9. 08.
 * @version 1.0
 * 
 * Modification Information
 * Mod Date		Modifier		Description
 * ========================================
 * 2020. 9. 08.	kkb 			최초작성
 * Copyright (c) 2018 KTDS, Inc. All Rights Reserved
 */
@Data
public class EventContent 
{
	@ApiModelProperty(value="이벤트 내용 순번")
	private int conSeq;
	
	@ApiModelProperty(value="이벤트 순번")
	private int evtSeq;
	
	@ApiModelProperty(value="이벤트 내용 타입(자체구성:C0001, 외부HTML:C0002)")
	private String conType;
	
	@ApiModelProperty(value="내용 상세 타입(C0001:텍스트(대), C0002:텍스트(중), C0003(소), C0004:이미지, C0005:영상(URL), C0006:라인, C0007:외부html태그)")
	private String conDtlType;
	
	@ApiModelProperty(value="이벤트 내용 상세")
	private String conDtl;
	
	@ApiModelProperty(value="텍스트 색상코드")
	private String colorCode;
	
	@ApiModelProperty(value="유튜브 Id")
	private String youtubeId;
	
	@ApiModelProperty(value="이벤트 상세 내용 순서")
	private int conOrder;
	
}
