package com.kt.yapp.validator;

import com.kt.yapp.em.EnumYn;
import com.kt.yapp.util.YappUtil;

/**
 * 입력값이 "Y" 인지 체크
 */
public class YvalChkValidator implements IApiParamValidator 
{
	private static final String ERR_MSG = "입력값이 Y 가 아닙니다.";
	
	@Override
	public String validate(Object obj) 
	{
		return YappUtil.isEq(obj, EnumYn.C_Y.getValue()) ? null : ERR_MSG;
	}

}
