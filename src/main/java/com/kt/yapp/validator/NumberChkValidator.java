package com.kt.yapp.validator;

import com.kt.yapp.util.YappUtil;

/**
 * 입력값의 숫자형 여부 체크
 */
public class NumberChkValidator implements IApiParamValidator 
{
	private static final String ERR_MSG = "숫자형이 아닙니다.";
	private static final String ERR_MSG2 = "값이 0 입니다. 올바른 값을 입력해 주세요.";
	
	@Override
	public String validate(Object obj) 
	{
		String errMsg = YappUtil.toInt(obj) == -1 ? ERR_MSG : (YappUtil.toInt(obj) == 0 ? ERR_MSG2 : null);
		return errMsg == null ? null : errMsg ;
	}

}
