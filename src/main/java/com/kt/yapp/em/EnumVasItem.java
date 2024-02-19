package com.kt.yapp.em;

/**
 * 계약 상태 코드
 */
public enum EnumVasItem 
{
	/** 01 사용중 */
	C01("01")
	/** 02 일시정지 */
	, C02("02")
	/** 03 해지 */
	, C03("03")
	/** 08 예약 */
	, C08("08")
	;
	
	private String statusCd;
	
	private EnumVasItem(String statusCd) {
		this.statusCd = statusCd;
	}
	
	public String getStatusCd() {
		return this.statusCd;
	}
	
}
