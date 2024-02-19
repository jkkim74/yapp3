package com.kt.yapp.em;

/**
 * 선물 데이터 타입
 */
public enum EnumGiftDataTp 
{
	/**G0001 선물하기 */
	G0001("G0001")

	/**G0002 데이턱 */
	, G0002("G0002")
	;
	
	private String dataTp;
	
	private EnumGiftDataTp(String dataTp) {
		this.dataTp = dataTp;
	}
	
	public String getDataTp() {
		return this.dataTp;
	}
	
}
