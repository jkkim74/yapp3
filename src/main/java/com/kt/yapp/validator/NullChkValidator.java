package com.kt.yapp.validator;

import com.kt.yapp.util.YappUtil;

/**
 * 입력값의 NULL 여부 체크
 */
public class NullChkValidator implements IApiParamValidator 
{
	private static final String ERR_MSG = "입력값이 NULL 입니다.";
	
	@Override
	public String validate(Object obj) 
	{
		return YappUtil.isEmpty(obj) ? ERR_MSG : null;
	}

}
