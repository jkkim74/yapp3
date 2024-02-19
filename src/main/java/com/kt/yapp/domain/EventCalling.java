package com.kt.yapp.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *  Y Friends 이벤트 대상 요금제
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class EventCalling extends RestCommon
{
	@ApiModelProperty(value="메뉴 seq")
	private int emSeq;
	
	@ApiModelProperty(value="이벤트 seq")
	private int evtSeq;

	@ApiModelProperty(value="요금제코드")
	private String ppCd;
}
