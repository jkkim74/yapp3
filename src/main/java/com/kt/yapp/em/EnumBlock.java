package com.kt.yapp.em;

/**
 * Y박스 부분차단 결고 코드
 */
public enum EnumBlock 
{
	/** 데이터꺼내기 차단 */
	SB005("SB005")
	/** 선물하기 차단 */
	, SB006("SB006")
	/** 데이턱 차단 */
	, SB007("SB007")
	;
	
	private String codeId;
	
	private EnumBlock(String codeId) {
		this.codeId = codeId;
	}
	
	public String getValue() {
		return this.codeId;
	}
	
	
}
