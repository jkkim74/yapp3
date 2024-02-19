package com.kt.yapp.domain;


import java.util.Date;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TicketRewardData {
	
	@ApiModelProperty(value="응모권 순번")
	private int ticketSeq;
	
	@ApiModelProperty(value="응모권당첨상품순번")
	private int giftIssueSeq;
	
	@ApiModelProperty(value="이벤트 순번")
	private int evtSeq;
	
	@ApiModelProperty(value="이벤트명")
	private String evtTitle;
	
	@ApiModelProperty(value="이미지 URL")
	private String imgUrl;

	@ApiModelProperty(value="상품명")
	private String giftName;
	
	@ApiModelProperty(value="상품소개")
	private String giftIntro;

	@ApiModelProperty(value="유효시작일")
	private String validStartDt;
	
	@ApiModelProperty(value="유효종료일")
	private String validEndDt;
	
	@ApiModelProperty(value="데이터유효시작일")
	private String dataValidStartDt;
	
	@ApiModelProperty(value="데이터유효종료일")
	private String dataValidEndDt;
	
	@ApiModelProperty(value="계약번호")
	private String cntrNo;
	
	@ApiModelProperty(value="등록일")
	private String regDt;
	
	@ApiModelProperty(value="수정일")
	private String modDt;

	@ApiModelProperty(value="사용 여부")
	private String useYn;
	
	@ApiModelProperty(value="응모권상품당첨여부")
	private String winYn;
	
	@ApiModelProperty(value="응모권당첨상품명")
	private String ticketGiftName;
	
	@ApiModelProperty(value="응모권당첨상품 이미지 URL")
	private String ticketGiftImgUrl;
	
	@ApiModelProperty(value="응모권당첨상품등록일")
	private String ticketGiftRegDt;
	
	@ApiModelProperty(value="응모권상품데이타지급여부")
	private String dataGiveYn;
	
	@ApiModelProperty(value="경품유형")
	private String giftType;
}
