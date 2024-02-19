package com.kt.yapp.validator;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * RestAPI 호출 파라미터 체크 정보
 */
@Data
public class ApiParam
{
	private String paramNm;
	private String paramDesc;
	private String paramType;
	private boolean required;
	private String method;
	private int order;

	private List<IApiParamValidator> validatorClassNmList = new ArrayList<>();
	
	public void addValidator(IApiParamValidator validator)
	{
		this.validatorClassNmList.add(validator);
	}
}

