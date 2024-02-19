package com.kt.yapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *  Y Friends 메뉴정보
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class YfriendsMenu extends RestCommon
{
	@ApiModelProperty(value="계약번호")
	private String cntrNo;

	@ApiModelProperty(value="이벤트 seq")
	private int evtSeq;

	@ApiModelProperty(value="이벤트메뉴 seq")
	private int emSeq;

	@ApiModelProperty(value="메뉴명")
	private String menuName;

	@ApiModelProperty(value="도움말URL")
	private String helpImgUrl;

	@ApiModelProperty(value="종료여부")
	private String endYn;
	
	@ApiModelProperty(value="이벤트타입")
	private String evtType;
	
	@ApiModelProperty(value="메뉴URL")
	private String menuUrl;
	
	@ApiModelProperty(value="메뉴New표시여부")
	private String newMenuYn;
	
	@ApiModelProperty(value="이벤트 설명")
	private String eventDetail;
	
	@ApiModelProperty(value="방최대인원")
	private int maxRoomCnt;
	
	@ApiModelProperty(value="상품제공방식(상품없음:O0001, 선착순:O0002, 추첨:O0003)")
	private String giftOfferType;
	
	@ApiModelProperty(value="와이프렌즈 메뉴 상세 제목")
	private String dtlTitle;
	
	@ApiModelProperty(value="와이프렌즈 메뉴 상세 방 제목")
	private String dtlRoomTitle;

	@ApiModelProperty(value="비고")
	@JsonIgnore
	private String remarks;

	@ApiModelProperty(value="사용여부")
	@JsonIgnore
	private String useYn;

	@ApiModelProperty(value="안드로이드 사용 여부")
	@JsonIgnore
	private String andrdUseYn;

	@ApiModelProperty(value="IOS 사용 여부")
	@JsonIgnore
	private String iosUseYn;

	@ApiModelProperty(value="요금제 사용 여부")
	@JsonIgnore
	private String callplanYn;

	@ApiModelProperty(value="시작연령")
	@JsonIgnore
	private int ageStart;
	
	@ApiModelProperty(value="종료연령")
	@JsonIgnore
	private int ageEnd;
	
	@ApiModelProperty(value="Y프렌즈 메뉴 사용여부")
	@JsonIgnore
	private String yfriendsMenuYn;
	
	@ApiModelProperty(value="엔드리순번")
	@JsonIgnore
	private String etrSeq;
	
	//211025 관리자 여부
	@ApiModelProperty(value="관리자 여부")
	@JsonIgnore
	private String adminYn;
}
