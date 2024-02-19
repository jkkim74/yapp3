package com.kt.yapp.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *  Y Friends 방정보
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class RoomInfo extends RestCommon
{
	@ApiModelProperty(value="방 seq")
	private int rmSeq;

	@ApiModelProperty(value="이벤트메뉴 seq")
	private int emSeq;
	
	@ApiModelProperty(value="이벤트 seq")
	private int evtSeq;

	@ApiModelProperty(value="이미지 url")
	private String imgUrl1;

	@ApiModelProperty(value="방 최대 인원")
	private int roomMaxJoinCnt;

	@ApiModelProperty(value="방 참여 인원")
	private int joinCnt;

	@ApiModelProperty(value="방제목")
	private String roomName;

	@ApiModelProperty(value="콘텐츠")
	private String contents;

	@ApiModelProperty(value="최대 참여 인원")
	private int maxJoinCnt;

	@ApiModelProperty(value="비고")
	private String remarks;

	@ApiModelProperty(value="삭제구분")
	private String delYn;

	@ApiModelProperty(value="모집완료 여부")
	private String invitedFnshYn;

	@ApiModelProperty(value="참여완료 여부")
	private String joinFnshYn;

	@ApiModelProperty(value="방장 계약번호")
	private String masterCntrNo;

	@ApiModelProperty(value="참여자 계약번호")
	private String joinCntrNo;

	@ApiModelProperty(value="경품신청 번호")
	private int giftReq;

	@ApiModelProperty(value="참여구분(JN001 :방장, JN002:팀원)")
	private String joinType;
	
	@ApiModelProperty(value="경품 대상자 여부")
	private String giftRcvYn;
}
