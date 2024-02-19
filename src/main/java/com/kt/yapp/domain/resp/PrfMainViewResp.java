package com.kt.yapp.domain.resp;

import java.util.List;

import com.kt.yapp.domain.EventMaster;
import com.kt.yapp.domain.PreferenceInfo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 이벤트 메인화면 표시 Response
 * 
 * @author
 * @since
 * @version 1.0
 * 
 * Modification Information
 * Mod Date		Modifier		Description
 * ========================================
 * 2020. 8. 21.	kkb		 	    최초작성
 * Copyright (c) 2018 KTDS, Inc. All Rights Reserved
 */
@Data
public class PrfMainViewResp 
{
	@ApiModelProperty(value="선호도 조사 목록")
	private List<PreferenceInfo> prfList;
	
	@ApiModelProperty(value="선호도 조사 선택 제한 개수")
	private String prfCnt;
}
