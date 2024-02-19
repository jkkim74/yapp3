package com.kt.yapp.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Event.java : 이벤트 정보
 * 
 * @author seungman.yu
 * @since 2018. 8. 10.
 * @version 1.0
 * 
 * Modification Information
 * Mod Date		Modifier		Description
 * ========================================
 * 2018. 8. 24.	seungman.yu 	Y데이터박스 이벤트 전체 개선
 * Copyright (c) 2018 KTDS, Inc. All Rights Reserved
 */
@Data
public class Event 
{
	@ApiModelProperty(value="이벤트SEQ")
	private int evtSeq;
	
	@ApiModelProperty(value="제목")
	private String title;
	
	@ApiModelProperty(value="내용")
	private String contents;
	
	@ApiModelProperty(value="이벤트 유형(G0001: 외부링크, G0002: 단순배너, G0003: 페이스북공유, G0004: 당첨자 발표안내, G0005: 멤버쉽 , G0006: 콜라보)")
	private String evtTp;
	
	@ApiModelProperty(value="이벤트 시작일시")
	private String evtStDt;
	
	@ApiModelProperty(value="이벤트 종료일시")
	private String evtEdDt;
	
	@ApiModelProperty(value="버튼명")
	private String btnLbl;
	
	@ApiModelProperty(value="상세버튼명")
	private String btnDtlLbl;
	
	@ApiModelProperty(value="진행구분")
	private String progressType;
	
	@ApiModelProperty(value="응모여부")
	private String applYn;
	
	@ApiModelProperty(value="종료여부")
	private String endYn;
	
	@ApiModelProperty(value="리스트 이미지 URL. 복수개 가능( ',' 로 구분)")
	private String listImgURL;

	@ApiModelProperty(value="히든 메인 이미지 URL")
	private String hiddenMainImgUrl;

	@ApiModelProperty(value="히든 백그라운드이미지 URL")
	private String hiddenBgImgUrl;

	@ApiModelProperty(value="외부 링크 URL")
	private String linkUrl;
	
	@ApiModelProperty(value="당첨여부")
	private String winYn;
	
	@ApiModelProperty(value="달성여부")
	private String achieveYn;
	
	@ApiModelProperty(value="달성금액")
	private int achieveAmt;
	
	@ApiModelProperty(value="띠배너 여부")
	@JsonIgnore
	private String bannerYn;
	
	@ApiModelProperty(value="띠배너 이미지 URL")
	@JsonIgnore
	private String bannerImgUrl;
	
	@ApiModelProperty(value="팝업 이미지 URL")
	private String popupImgUrl;
	
	@ApiModelProperty(value="팝업 여부")
	@JsonIgnore
	private String popupYn;
	
	@ApiModelProperty(value="사용여부")
	@JsonIgnore
	private String useYn;
	
	@ApiModelProperty(value="등록일시")
	private Date regDt;
	
	@ApiModelProperty(value="수정일시")
	@JsonIgnore
	private Date modDt;
	
	@ApiModelProperty(value="신규여부")
	@JsonIgnore
	private String newYn;

	@ApiModelProperty(value="조회시작 INDEX")
	@JsonIgnore
	private int stIdx;
	
	@ApiModelProperty(value="조회종료 INDEX")
	@JsonIgnore
	private int edIdx;
	
	@ApiModelProperty(value="hidden 영역 여부")
	@JsonIgnore
	private String hiddenYn;
	
	@ApiModelProperty(value="조건체크여부")
	@JsonIgnore
	private String conditionChkYn;
	
	@ApiModelProperty(value="조건데이터량")
	@JsonIgnore
	private int conditionDataAmt;
	
	@ApiModelProperty(value="멤버쉽 포인트 이벤트 여부")
	@JsonIgnore
	private String memberPointYn;
	
	@ApiModelProperty(value="멤버쉽 포인트 조건 데이터량")
	@JsonIgnore
	private int memberPointDivAmt;
	
	@ApiModelProperty(value="멤버쉽 이벤트 지급 포인트량")
	@JsonIgnore
	private int memberPointPayAmt;
	
	@ApiModelProperty(value="hidden 이미지 URL")
	@JsonIgnore
	private String hiddenImgUrl;
	
	@ApiModelProperty(value="hidden 아이콘 이미지 URL")
	@JsonIgnore
	private String hiddenIconImgUrl;
	
	@ApiModelProperty(value="띠배너 아이콘 seq")
	private int bannerIconSeq;

	@ApiModelProperty(value="띠배너 아이콘 url")
	private String iconUrl;
	
	@ApiModelProperty(value="좌측하단 사용여부")
	private String lgifUseYn;
	
	@ApiModelProperty(value="좌측하단gif url")
	private String lgifUrl;
	
	@ApiModelProperty(value="좌측하단gif 시작날짜")
	private String lgifStDt;
	
	@ApiModelProperty(value="좌측하단gif 종료날짜")
	private String lgifEdDt;
	
	@ApiModelProperty(value="응모시작날짜")
	private String applStDt;
	
	@ApiModelProperty(value="응모종료날짜")
	private String applEdDt;
	
	@ApiModelProperty(value="당첨날짜")
	private String winDt;
	
	@ApiModelProperty(value="배송시작날짜")
	private String deliverDt;
	
	@ApiModelProperty(value="이벤트 남은 기간")
	private String remainDay;
	
	@ApiModelProperty(value="이벤트 상태(ES001: 당첨,ES002: 완료,ES003: 발표대기중,ES004: 응모완료,ES005: 진행중)")
	private String evtStat;
	
	@ApiModelProperty(value="이벤트 상태명")
	private String evtStatNm;
}
