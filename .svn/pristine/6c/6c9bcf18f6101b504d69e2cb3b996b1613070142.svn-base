package com.kt.yapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * ContractInfo.java : 계약 정보
 * 
 * @author seungman.yu
 * @since 2018. 10. 4.
 * @version 1.0
 * 
 * Modification Information
 * Mod Date		Modifier		Description
 * ========================================
 * 2018. 10. 4.	seungman.yu 	Y Event 기능 추가
 * Copyright (c) 2018 KTDS, Inc. All Rights Reserved
 */
@Data
public class ContractInfo 
{
	@ApiModelProperty(value="고객명")
	private String userNm;

	@ApiModelProperty(value="마스킹 안된 고객명")
	@JsonIgnore
	private String orgUserNm;
	
	@ApiModelProperty(value="계약상태코드(01: 사용중, 02: 일시정지, 03: 해지, 08: 예약")
	private String cntrcStatusCd;
	
	@ApiModelProperty(value="생년월일")
	private String birthDate;

	@ApiModelProperty(value="고객세부유형코드(01:내국인,02:외국인,03:법인,04:공공기관,05:임의단체,06:법인사업자,07:개인사업자,08:파트너(MVNO),09:파트너(CP),10:그룹사,11:LEAD고객,12:직원")
	private String partyDetailTypeCd;
	
	@ApiModelProperty(value="만 14세여부")
	private String fourteenYn;

	@ApiModelProperty(value="만 18세여부")
	private String eightteenYn;

	@ApiModelProperty(value="만 19세여부")
	private String nineteenYn;

	@ApiModelProperty(value="만 29세여부")
	private String twentyNineYn;

	@ApiModelProperty(value="계약번호")
	private String cntrNo;
	
	@ApiModelProperty(value="휴대폰 번호")
	private String mobileNo;
	
	//220613 마스킹 모바일번호
	@ApiModelProperty(value="마스킹 휴대폰 번호")
	private String maskingMobileNo;
	
	@ApiModelProperty(value="사용자정보")
	private UserInfo userInfo;
	
	@ApiModelProperty(value="요금제정보")
	private CallingPlan callingPlan;

	@ApiModelProperty(value="요금제코드")
	private String ppCd;
	
	@ApiModelProperty(value="요금제명")
	@JsonIgnore
	private String ppNm;
	
	@ApiModelProperty(value="벤더코드")
	@JsonIgnore
	private String vendorCd;
	
	@ApiModelProperty(value="계약유형코드")
	@JsonIgnore
	private String cntrcTypeCd;
	
}
