package com.kt.yapp.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PreferenceInfo 
{
	@ApiModelProperty(value="선호도 정보순번")
	private int priSeq;
	
	@ApiModelProperty(value="선호도 순번")
	private int prfSeq;
	
	@ApiModelProperty(value="계약번호")
	private String cntrNo;
	
	@ApiModelProperty(value="선호도 선택 여부")
	private String checkYn;
	
	@ApiModelProperty(value="선호도명")
	private String prfName;
	
	@ApiModelProperty(value="선호도 이미지")
	private String prfImg;
	
	@ApiModelProperty(value="선호도 정렬순번")
	private int prfOrder;
	
	@ApiModelProperty(value="선호도 비고")
	private String remarks;
	
}
