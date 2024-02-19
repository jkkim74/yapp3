package com.kt.yapp.domain.resp;

import java.util.List;

import com.kt.yapp.domain.Databox;
import com.kt.yapp.domain.DataboxDtl;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 데이터 박스 상세 Response
 */
@Data
public class DataboxDtlResp 
{
	@ApiModelProperty(value="조회 년월")
	private String srchYm;
	
	@ApiModelProperty(value="데이터 박스 정보")
	private Databox dboxInfo;
	
	@ApiModelProperty(value="데이터 박스 상세 목록")
	private List<DataboxDtl> databoxDtlList;
}
