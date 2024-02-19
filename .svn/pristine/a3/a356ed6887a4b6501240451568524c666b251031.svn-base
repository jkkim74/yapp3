package com.kt.yapp.validator;

import com.kt.yapp.em.EnumYn;
import com.kt.yapp.util.YappUtil;

/**
 * 입력값이 "Y" or "N" 인지 체크
 */
public class YnValChkValidator implements IApiParamValidator 
{
	private static final String ERR_MSG = "입력값은 'Y' 또는 'N' 이 되어야 합니다.";
	
	@Override
	public String validate(Object obj) 
	{
		return YappUtil.contains(obj, EnumYn.C_Y.getValue(), EnumYn.C_N.getValue()) ? null : ERR_MSG + "(입력값 : " + obj + ")";
	}

}
