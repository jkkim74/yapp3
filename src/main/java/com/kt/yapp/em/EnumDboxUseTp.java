package com.kt.yapp.em;

/**
 * 데이터박스 사용 타입
 */
public enum EnumDboxUseTp 
{
	/** 적립 */
	G0001("G0001")

	/** 사용 */
	, G0002("G0002")
	;
	
	private String dataTp;
	
	private EnumDboxUseTp(String dataTp) {
		this.dataTp = dataTp;
	}
	
	public String getDataTp() {
		return this.dataTp;
	}
	
}
