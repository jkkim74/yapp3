package com.kt.yapp.em;

/**
 * 부가서비스 코드
 */
public enum EnumVasCd 
{
	/**MTPMN My Time Plan */
	MTPMN("MTPMN")

	/**PLLDAT 당겨쓰기 */
	, PLLDAT("PLLDAT")

	/**LTEDTC 후불 데이터 충전 */
	, LTEDTC("LTEDTC")

	/**LTERTE 룰렛 */
	, LTERTE("LTERTE")

	/**LTEDTP 멤버십 데이터 충전 */
	, LTEDTP("LTEDTP")
	
	/**TYCDBL 두배쓰기 */
	, TYCDBL("TYCDBL")
	
	/**LTEDTP 바꿔쓰기 */
	, TYCHGE("TYCHGE")
	
	/**HALFCP 반값팩 */
	, HALFCP("HALFCP")
	
	/**LONGCP 장기혜택쿠폰 */
	, LONGCP("LONGCP")
	;
	private String vasCd;
	
	private EnumVasCd(String vasCd) {
		this.vasCd = vasCd;
	}
	
	public String getVasCd() {
		return this.vasCd;
	}
	
}
