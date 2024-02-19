package com.kt.yapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 *  Y Friends 참여횟수
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class RoomChkInfo extends RestCommon
{
	@ApiModelProperty(value="방 seq")
	private int rmSeq;
	
	@ApiModelProperty(value="Y프렌즈 메뉴 seq")
	private int emSeq;

	@ApiModelProperty(value="이벤트 seq")
	private int evtSeq;
	
	@ApiModelProperty(value="계약번호")
	private String cntrNo;

	@ApiModelProperty(value="사용자명")
	private String userName;

	@ApiModelProperty(value="핸드폰번호")
	private String mobileNo;
	
	@ApiModelProperty(value="요금제코드 ")
	private String ppCd;
	
	@ApiModelProperty(value="단말기 모델명 ")
	private String resourceModelName;

	@ApiModelProperty(value="단말기 모델ID")
	private String resourceUniqueId;

	@ApiModelProperty(value="방장횟수")
	private int masterCnt;
	
	@ApiModelProperty(value="팀원횟수")
	private int slaveCnt;
	
	@ApiModelProperty(value="참여회수")
	private int joinCnt;
	
	@ApiModelProperty(value="방에 따른 참여회수")
	private int rmCnt;
	
	@ApiModelProperty(value="최대인원")
	private int maxRoomCnt;
	
	@ApiModelProperty(value="체크메시지")
	private String chkMsg;

	@ApiModelProperty(value="나이체크여부")
	@JsonIgnore
	private String ageChkYn;

	@ApiModelProperty(value="요금제체크여부")
	@JsonIgnore
	private String callingChkYn;

	@ApiModelProperty(value="단말기체크여부")
	@JsonIgnore
	private String deviceChkYn;
	
	@ApiModelProperty(value="참여대상여부")
	String targetYn;
	
	@ApiModelProperty(value="참여대상코드")
    String targetCode;

}
