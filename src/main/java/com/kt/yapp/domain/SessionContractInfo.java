package com.kt.yapp.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * SessionContractInfo.java
 * 세션 계약 데이터
 * 
 * @author seungman.yu
 * @since 2018. 10. 4.
 * @version 1.0
 * 
 * Modification Information
 * Mod Date		Modifier		Description
 * ========================================
 * 2018. 10. 4.	seungman.yu 	Y Event 기능 추가
 * Copyright (c) 2018 KTDS, Inc. All Rights Reserved
 */
@Data
public class SessionContractInfo implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7100252938526688189L;

	@ApiModelProperty(value="계약번호)")
	private String cntrNo;
	
	@ApiModelProperty(value="휴대폰 번호")
	private String mobileNo;
	
	@ApiModelProperty(value="고객명")
	private String userNm;
	
	@ApiModelProperty(value="마스킹 안된 고객명")
	@JsonIgnore
	private String orgUserNm;
	
	@ApiModelProperty(value="요금제코드")
	private String ppCd;
	
}
