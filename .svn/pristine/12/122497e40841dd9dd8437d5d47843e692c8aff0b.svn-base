package com.kt.yapp.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *  최근 연락처 정보
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class RecentContact extends RestCommon
{
	@ApiModelProperty(value="연락처 seq")
	private int rctSeq;

	@ApiModelProperty(value="계약번호")
	private String cntrNo;

	@ApiModelProperty(value="수신연락처 계약번호")
	private String recvCntrNo;

	@ApiModelProperty(value="수신연락처 연락처")
	private String recvMobileNo;
	
	@ApiModelProperty(value="마스킹 수신연락처 연락처")
	private String maskingRecvMobileNo;
	
	@ApiModelProperty(value="수신연락처 이름")
	private String recvName;
	
	@ApiModelProperty(value="수신타입(G0001:선물하기,G0002:조르기)")
	private String dataGiftType;
	
	@ApiModelProperty(value="조르기 가능 여부")
	private String reqRcvYn;
	
	@ApiModelProperty(value="Y박스 가입여부")
	private String yappJoinYn;
}
