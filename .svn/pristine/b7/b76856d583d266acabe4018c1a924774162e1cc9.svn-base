package com.kt.yapp.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Lamp 메뉴
 */
@Data
public class LampMenu 
{
	@ApiModelProperty(value="순번")
	private int lampSeq;

	@ApiModelProperty(value="메뉴명")
	private String lampNm;

	@ApiModelProperty(value="메뉴URL")
	private String lampUrl;

	@ApiModelProperty(value="오퍼레이션명")
	private String lampOperation;
	
	@ApiModelProperty(value="보안탐지 유형")
	private String lampType;

	@ApiModelProperty(value="보안탐지 이벤트")
	private String lampEvent;

	@ApiModelProperty(value="서비스구분-SV001:CMS, SV002:YAPP")
	private String lampService;
	
	@ApiModelProperty(value="사용구분")
	private String useYn;
	
	@ApiModelProperty(value="등록일시")
	private Date regDt;
	
	@ApiModelProperty(value="램프Url 여부")
	private String isLampUrl = "N";
	
	@ApiModelProperty(value="transaction 아이디")
	private String transactionId;
	
	@ApiModelProperty(value="로그 구분")
	private String logType;
	
	@ApiModelProperty(value="응답코드")
	private String resultCd;
	
	@ApiModelProperty(value="응답메세지")
	private String resultMsg;
	
	//20220627
	@ApiModelProperty(value="서비스도메인")
	private String serviceDomain;
	
	//20220718
	@ApiModelProperty(value="모바일번호")
	private String mobileNo;
}