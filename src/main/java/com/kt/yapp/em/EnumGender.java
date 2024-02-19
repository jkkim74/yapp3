package com.kt.yapp.em;

/**
 * 성별
 */
public enum EnumGender 
{
	/** 01 여성 */
	C01("01")
	/** 02 남성 */
	, C02("02")
	;
	
	private String gndCd;
	
	private EnumGender(String gndCd) {
		this.gndCd = gndCd;
	}
	
	public String getGndCd() {
		return this.gndCd;
	}
	
}
