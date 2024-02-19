package com.kt.yapp.em;

/**
 * 그룹코드 ID
 */
public enum EnumGrpCodeId 
{
	/** WSG 서비스 코드 */
	WSG_SVC_GRP_CD("WSG_SVC_GRP_CD")
	/** 데이터 멘트 */
	, DATA_MENT("DATA_MENT")
	;
	
	private String grpCodeId;
	
	private EnumGrpCodeId(String grpCodeId) {
		this.grpCodeId = grpCodeId;
	}
	
	public String getGrpCodeId() {
		return this.grpCodeId;
	}
	
}
