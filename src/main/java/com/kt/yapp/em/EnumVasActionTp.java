package com.kt.yapp.em;

import com.kt.yapp.util.YappUtil;

/**
 * 부가서비스 가입 유형
 */
public enum EnumVasActionTp 
{
	/** A 가입 */
	A("A", "G0001")
	/** C 해지 */
	, C("C", "G0002")
	/** U 갱신 */
	, U("U", "G0003")
	;
	
	private String actionTp;
	private String grpCd;
	
	private EnumVasActionTp(String actionTp, String grpCd) {
		this.actionTp = actionTp;
		this.grpCd = grpCd;
	}
	
	public String getKey() {
		return this.actionTp;
	}
	
	public String getValue() {
		return this.grpCd;
	}
	
	public static EnumVasActionTp getObj(String value)
	{
		for ( int i = 0; i < values().length; i++ )
		{
			EnumVasActionTp actionTp = values()[i];
			if ( YappUtil.isEq(value, actionTp.getValue()) )
				return actionTp;
		}
		return null;
	}
}
