package com.kt.yapp.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * EventAppl.java
 * 
 * @author seungman.yu
 * @since 2018. 9. 18.
 * @version 1.0
 * 
 * Modification Information
 * Mod Date		Modifier		Description
 * ========================================
 * 2018. 9. 18.	seungman.yu 	Y Event 기능 추가
 * Copyright (c) 2018 KTDS, Inc. All Rights Reserved
 */
@Data
public class EventAppl 
{
	@ApiModelProperty(value="이벤트SEQ")
	private int evtSeq;
	
	@ApiModelProperty(value="계약번호")
	@JsonIgnore
	private String cntrNo;
	
	@ApiModelProperty(value="사용자 아이디")
	@JsonIgnore
	private String userId;
	
	@ApiModelProperty(value="당첨여부")
	@JsonIgnore
	private String winYn;
	
	@ApiModelProperty(value="등록일시")
	@JsonIgnore
	private Date regDt;
	
	@ApiModelProperty(value="리턴 메세지")
	private String evtRetMsg;
	
	@ApiModelProperty(value="성명")
	private String name;
	
	@ApiModelProperty(value="전화번호")
	private String mobileNo;
	
	@ApiModelProperty(value="우편번호")
	private String post;
	
	@ApiModelProperty(value="주소")
	private String addr;
	
	@ApiModelProperty(value="상세주소")
	private String addrDtl;
	
	@ApiModelProperty(value="주소(구주소)")
	private String jibunAddr;
	
	@ApiModelProperty(value="상세주소(구주소)")
	private String jibunAddrDtl;
	
	@ApiModelProperty(value="수정여부")
	private String modYn;
	
	@ApiModelProperty(value="주소명확구분 : 명확-Y, 불명확-N")
	private String addrClearYn="Y";
	
	@ApiModelProperty(value="출석일차")
	private String attendDay="";
}
