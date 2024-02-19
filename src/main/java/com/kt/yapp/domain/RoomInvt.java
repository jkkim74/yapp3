package com.kt.yapp.domain;

import java.awt.List;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *  Y Friends 방 초대정보
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class RoomInvt extends RestCommon
{
	@ApiModelProperty(value="초대 seq")
	private int invtSeq;

	@ApiModelProperty(value="이벤트 seq")
	private int evtSeq;

	@ApiModelProperty(value="방 seq")
	private int rmSeq;

	@ApiModelProperty(value="이벤트메뉴 seq")
	private int emSeq;
	
	@ApiModelProperty(value="시작나이")
	private int ageStart;
	
	@ApiModelProperty(value="종료나이")
	private int ageEnd;

	@ApiModelProperty(value="이벤트메뉴명")
	private String menuName;
	
	@ApiModelProperty(value="이벤트메뉴 리스트")
	private ArrayList<Object> emSeqList;

	@ApiModelProperty(value="초대 요청자 계약번호")
	@JsonIgnore
	private String askCntrNo;

	@ApiModelProperty(value="초대 요청자명")
	private String askName;

	@ApiModelProperty(value="초대 요청자 휴대번호")
	private String askMobileNo;

	@ApiModelProperty(value="초대 대상자 계약번호")
	@JsonIgnore
	private String rcvCntrNo;

	@ApiModelProperty(value="초대 대상자명")
	@JsonIgnore
	private String rcvName;

	@ApiModelProperty(value="초대 대상자 휴대번호")
	@JsonIgnore
	private String rcvMobileNo;

	@ApiModelProperty(value="나이체크여부")
	private String ageChkYn;
	
	@ApiModelProperty(value="요금제체크여부")
	private String callingChkYn;
	
	@ApiModelProperty(value="단말기체크여부")
	private String deviceChkYn;

	@ApiModelProperty(value="방 참여 인원")
	@JsonIgnore
	private int joinCnt;

	@ApiModelProperty(value="최대 참여 인원")
	@JsonIgnore
	private int maxJoinCnt;

	@ApiModelProperty(value="안드로이드 사용 여부")
	@JsonIgnore
	private String andrdUseYn;

	@ApiModelProperty(value="IOS 사용 여부")
	@JsonIgnore
	private String iosUseYn;
	
	@ApiModelProperty(value="등록일자")
	private String regDt;
}
