package com.kt.yapp.em;

/**
 * 선물 데이터 요청 타입
 */
public enum EnumGiftDataReqTp 
{
	/**G0001 선물하기 */
	G0001("G0001")

	/**G0002 조르기 */
	, G0002("G0002")

	/**G0003 초대하기 */
	, G0003("G0003")
	
	/**G0004 보너스 */
	, G0004("G0004")
	
	/**G0005 프로모션(페이백) */
	, G0005("G0005")
	;
	
	private String reqCd;
	
	private EnumGiftDataReqTp(String reqCd) {
		this.reqCd = reqCd;
	}
	
	public String getReqTp() {
		return this.reqCd;
	}
	
}
