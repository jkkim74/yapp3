package com.kt.yapp.domain;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * EventGift.java : 이벤트 정보 상품
 * 
 * @author kkb
 * @since 2020. 9. 10.
 * @version 1.0
 * 
 * Modification Information
 * Mod Date		Modifier		Description
 * ========================================
 * 2020. 9. 10.	kkb 			최초작성
 * Copyright (c) 2018 KTDS, Inc. All Rights Reserved
 */
@Data
public class EventGift 
{
	@ApiModelProperty(value="이벤트 순번")
	private int evtSeq;
	
	@ApiModelProperty(value="상품 순번")
	private int giftSeq;
	
	@ApiModelProperty(value="상품 발행 순번")
	private int issueSeq;
	
	@ApiModelProperty(value="상품명")
	private String giftName;
	
	@ApiModelProperty(value="상품 이미지 URL")
	private String imgUrl;
	
	@ApiModelProperty(value="상품 소진 이미지 URL")
	private String soldImgUrl;
	
	@ApiModelProperty(value="이벤트 상품 발행수량")
	private int issueCnt;
	
	@ApiModelProperty(value="이벤트 상품 총수량")
	private int giftCnt;
	
	@ApiModelProperty(value="상품발행설정 일차")
	private int giftDay;
	
	@ApiModelProperty(value="경품 여부")
	private String giftYn;
	
	@ApiModelProperty(value="출석 여부")
	private String attendYn;
	
	@ApiModelProperty(value="계약번호")
	private String cntrNo;
	
	@ApiModelProperty(value="사용자 아이디")
	private String userId;
	
	@ApiModelProperty(value="상품타입(배송용상품:G0001, 온라인상품:G0002)")
	private String giftType;
	
	@ApiModelProperty(value="출석체크만 한경우")
	private String onlyAttend;
	
	@ApiModelProperty(value="상품 매진 여부")
	private String soldoutYn;
	
	@ApiModelProperty(value="상품 당첨 여부")
	private String winYn;
	
	@ApiModelProperty(value="인원수별 상품번호(2 : 2명, 3 : 3명, 4 : 4명, 5 : 5명)")
	private int memNum;
	
	@ApiModelProperty(value="상품잔여수량")
	private int cnt;
	
	@ApiModelProperty(value="상품유효시작일자")
	private String validStartDt;
	
	@ApiModelProperty(value="상품유효종료일자")
	private String validEndDt;
	
	//210809 당월일자 9월배포
	@ApiModelProperty(value="당월일자")
	private List<Integer> dayList;
	//210809 당월일자 9월배포
	
	//210825 출첵 일자별 상품명
	@ApiModelProperty(value="출첵 일자별 상품명")
	private String ticketGiftName;
	//210825 출첵 일차별 명
	
	//230410 Y캔버스 수강상품
	@ApiModelProperty(value="Y캔버스 수강상품")
	private List<YcanvasItem> ycanvasItem;
	//230410 Y캔버스 수강상품
	
	//230512 온라인 상품 종류 
	@ApiModelProperty(value="온라인 상품 종류 (G0001 : 난수번호, G0002 : URL 랜딩)")
	private String rewardType;
}
