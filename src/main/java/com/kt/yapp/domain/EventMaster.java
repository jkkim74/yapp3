package com.kt.yapp.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * EventMaster.java : 이벤트 정보
 * 
 * @author kkb
 * @since 2020. 8. 31.
 * @version 1.0
 * 
 * Modification Information
 * Mod Date		Modifier		Description
 * ========================================
 * 2020. 8. 31.	kkb 			최초작성
 * Copyright (c) 2018 KTDS, Inc. All Rights Reserved
 */
@Data
public class EventMaster 
{
	@ApiModelProperty(value="계약번호")
	private String cntrNo;
	
	@ApiModelProperty(value="사용자 아이디")
	private String userId;
	
	@ApiModelProperty(value="이벤트 순번")
	private int evtSeq;
	
	@ApiModelProperty(value="이벤트 타입(G0001:일반이벤트, G0002:경품이벤트, G0003:매거진, G0004:Y프렌즈, G0005:출석체크, G0006:Y소식, G0008:투표, G0009:응모권 이벤트, G0010:응모권 출석체크)")
	private String evtType;
	
	@ApiModelProperty(value="이벤트 타입명")
	private String evtTypeNm;
	
	@ApiModelProperty(value="제목")
	private String evtTitle;
	
	@ApiModelProperty(value="제목색상코드	")
	private String btitleColorCode;
	
	@ApiModelProperty(value="소제목")
	private String evtSmallTitle;
	
	@ApiModelProperty(value="소제목색상코드")
	private String stitleColorCode;
	
	@ApiModelProperty(value="이벤트 시작일자")
	private String evtStartDt;
	
	@ApiModelProperty(value="이벤트 종료 일자")
	private String evtEndDt;
	
	@ApiModelProperty(value="상품제공방식(상품없음:O0001, 선착순:O0002, 추첨:O0003)")
	private String giftOfferType;
	
	@ApiModelProperty(value="당첨 발표일")
	private String etrOpenDt;
	
	@ApiModelProperty(value="당첨 발표일(요일)")
	private String etrOpenDweek;
	
	@ApiModelProperty(value="당첨 발표일(시간)")
	private String etrOpenTime;
	
	@ApiModelProperty(value="유의사항제공여부")
	private String evtNoteYn;
	
	@ApiModelProperty(value="유의사항")
	private String evtNote;
	
	@ApiModelProperty(value="상품선택 방식(본인선택:C0001, 랜덤제공:C0002)")
	private String giftChoiceType;
	
	@ApiModelProperty(value="노출순위(0:최하위~100:최상위)")
	private int orderPriority;
	
	@ApiModelProperty(value="하단버튼명")
	private String bottomButtonName;
	
	@ApiModelProperty(value="링크타입(앱내링크:L0001, 랭덤제공:L0002)")
	private String linkType;
	
	@ApiModelProperty(value="이벤트 링크URL")
	private String linkUrl;
	
	@ApiModelProperty(value="이벤트 로고 이미지")
	private String evtLogoImg;
	
	@ApiModelProperty(value="메인이미지")
	private String mainImg;
	
	@ApiModelProperty(value="게시판용 이미지")
	private String boardImg;
	
	@ApiModelProperty(value="이벤트 사용여부")
	private String useYn;
	
	@ApiModelProperty(value="이벤트 종료여부")
	private String endYn;
	
	@ApiModelProperty(value="이벤트 상세 상단(배너) 이미지")
	private String evtDtlTopImg;
	
	@ApiModelProperty(value="좌측 메뉴 하단 이미지")
	private String lnbBottomImg;
	
	@ApiModelProperty(value="좌측 메뉴 하단 이미지 노출 여부")
	private String lnbBottomImgYn;
	
	@ApiModelProperty(value="등록일시")
	private Date regDt;
	
	@ApiModelProperty(value="이벤트상태(P:시작전,I:진행중,E:종료)")
	private String progressType;
	
	@ApiModelProperty(value="이벤트옵션타이틀")
	private String evtOptionTitle;
	
	@ApiModelProperty(value="배송경품비고이름")
	private String giftFormNm;
	
	@ApiModelProperty(value="D-day")
	private String dDay;
	
	@ApiModelProperty(value="단말 OS 유형(G0001: Android, G0002: IOS)")
	private String osTp;
		
	@ApiModelProperty(value="수정일시")
	@JsonIgnore
	private Date modDt;
	
	//210915
	@ApiModelProperty(value="티켓보유여부")
	private String ticketYn;
	
	@ApiModelProperty(value="경품응모 주소노출 여부")
	private String giftAddrYn;
	
	@ApiModelProperty(value="응모 대상 G0001:정회원, G0002:준회원, G0003:전체")
	private String targetMem;
	
	@ApiModelProperty(value="댓글 기능 사용여부")
	private String replyYn;
	
	//211022
	@ApiModelProperty(value="다중경품지급여부")
	private String giftMultiReward;
	
	//221211 참여대상 조건 추가
	@ApiModelProperty(value="요금제 대분류 카테고리 코드")
	private String ppCatL;
	
	@ApiModelProperty(value="요금제 대분류")
	private String ppCdL;
	
	@ApiModelProperty(value="요금제 대분류")
	private String ppCdList;
	
	@ApiModelProperty(value="성별")
	private String gender;
	
	@ApiModelProperty(value="나이")
	private String ageList;
	
	@ApiModelProperty(value="나이-직접입력-시작나이")
	private String ageStartNum;
	
	@ApiModelProperty(value="나이-직접입력-종료나이")
	private String ageEndNum;
	
	@ApiModelProperty(value="참여유도버튼 - 사용여부")
	private String targetButtonYn;
	
	@ApiModelProperty(value="참여유도버튼 - 링크URL")
	private String targetButtonUrl;
	
	@ApiModelProperty(value="참여유도버튼 - 버튼명")
	private String targetButtonNm;
	
	@ApiModelProperty(value="부가서비스 분류")
	private String vasCdL;
	
	@ApiModelProperty(value="부가서비스 분류")
	private String vasCdList;
	
	// 경품 복수 선택 기능 구현 관련 추가 2023.06.12 by jk
	@ApiModelProperty(value="경품 최대 선택 수")
	private Integer giftMaxChoice;
	
}
