package com.kt.yapp.em;

/**
 * API 호출 결과 코드
 */
public enum EnumGift 
{
	/** G00001 보내기 */
	G00001("G00001")
	/** G00002 받기 */
	, G00002("G00002")
	/** G00003 조르기 */
	, G00003("G00003")
	;
	
	private String giftCd;
	
	private EnumGift(String giftCd) {
		this.giftCd = giftCd;
	}
	
	public String getValue() {
		return this.giftCd;
	}
	
}
