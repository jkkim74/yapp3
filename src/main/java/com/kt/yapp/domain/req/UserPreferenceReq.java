package com.kt.yapp.domain.req;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserPreferenceReq 
{
	@ApiModelProperty(value="선호도 플래그 값(Y:선택하기, null:Default, Z:다음에 하기, X:다시 안보기)")
	private String prfFlag;
	
	@ApiModelProperty(value="선호도 순번 리스트")
	private List<Integer> paramPrfSeqList;
}
