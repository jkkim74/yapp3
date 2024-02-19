package com.kt.yapp.em;

/**
 * Credential Type Code 계정 타입
 */
public enum EnumCrdntlTypeCd 
{
	/** 01 올레통합계정 */
	C01("01")
	/** 02 SHOW 계정 */
	, C03("03")
	/** 02 인터넷전화번호 */
	, C04("04")
	/** 02 휴대전화번호 */
	, C05("05")
	/** 02 집전화번호 */
	, C06("06")
	/** 02 Netizen */
	, C23("23")
	;
	
	private String crdntlTpCd;
	
	private EnumCrdntlTypeCd(String crdntlTpCd) {
		this.crdntlTpCd = crdntlTpCd;
	}
	
	public String getGndCd() {
		return this.crdntlTpCd;
	}
	
}
