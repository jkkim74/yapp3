package com.kt.yapp.em;

/**
 * 쿠폰 패키지 유형 코드
 */
public enum EnumPkgTpCd 
{
	/** PC 팝콘 */
	PC("PC")
	/** YT Y24반값팩 쿠폰 */
	, YT("YT")
	;
	
	private String value;
	
	private EnumPkgTpCd(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
	
}
