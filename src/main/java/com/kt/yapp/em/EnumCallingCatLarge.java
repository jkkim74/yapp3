package com.kt.yapp.em;

/**
 * 요금제 대분류 코드
 */
public enum EnumCallingCatLarge 
{
	/** G0001 LTE */
	G0001("G0001")
	/** G0002 LTE */
	, G0002("G0002")
	/** G0003 3G */
	, G0003("G0003")
	/** G0004 3G */
	, G0004("G0004")
	;
	
	private String catCd;
	
	private EnumCallingCatLarge(String catCd) {
		this.catCd = catCd;
	}
	
	public String getCatCd() {
		return this.catCd;
	}
	
}
