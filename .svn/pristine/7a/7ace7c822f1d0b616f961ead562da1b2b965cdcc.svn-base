package com.kt.yapp.validator;

/**
 * 입력값의 그룹코드 여부 체크
 */
public class GrpCdChkValidator implements IApiParamValidator 
{
	private static final String ERR_MSG = "그룹코드 형식(Gxxxx)이 아닙니다.";
	
	@Override
	public String validate(Object obj) 
	{
		boolean rtnVal = (obj == null || ! obj.toString().startsWith("G") || obj.toString().length() != 5 );
		
		return rtnVal  ? ERR_MSG + "(입력값 : " + obj + ")" : null;
	}

}
