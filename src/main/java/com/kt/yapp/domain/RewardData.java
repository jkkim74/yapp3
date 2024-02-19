package com.kt.yapp.domain;


import java.util.Date;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RewardData {
	
	@ApiModelProperty(value="이벤트 순번")
	private int evtSeq;

	@ApiModelProperty(value="이벤트 타입(G0001:일반이벤트, G0002:경품이벤트, G0003:매거진, G0004:Y프렌즈, G0005:출석체크, G0006:퀴즈/게임)")
	private String evtType;
	
	@ApiModelProperty(value="상품 번호")
	private int giftSeq;
	
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
	
	@ApiModelProperty(value="유효시작일(원래포맷)")
	private String validStartDtOrg;
	
	@ApiModelProperty(value="유효종료일(원래포맷)")
	private String validEndDtOrg;
	
	@ApiModelProperty(value="쿠폰사용안내 여부")
	private String cupnUseInfoYn;
	
	@ApiModelProperty(value="쿠폰사용안내")
	private String cupnUseInfo;
	
	@ApiModelProperty(value="유의사항 여부")
	private String cupnEtcYn;
	
	@ApiModelProperty(value="유의사항")
	private String cupnEtc;
	
	@ApiModelProperty(value="쿠폰 번호")
	private String rewId;
	
	@ApiModelProperty(value="랜딩 타입")
	private String landUrlType;
	
	@ApiModelProperty(value="랜딩 URL")
	private String landUrl;
	
	@ApiModelProperty(value="사용 여부")
	private String useYn;
	
	@ApiModelProperty(value="이벤트 타입명")
	private String evtTypeNm;
	
	@ApiModelProperty(value="이벤트옵션타이틀")
	private String evtOptionTitle;

	private String regDt;
	
	private String cntrNo;
	
	private int rewSeq;
	
	//210831 이벤트 명
	@ApiModelProperty(value="이벤트명")
	private String evtTitle;

	//210903
	@ApiModelProperty(value="발행일자")
	private String issueDt;
	
	//211103
	@ApiModelProperty(value="랜딩버튼명")
	private String landButtonText;
	
	//220912
	@ApiModelProperty(value="쿠폰 비밀번호")
	private String rewPw;
	
	@ApiModelProperty(value="쿠폰 번호")
	private String pwYn;
	
	//230413
	@ApiModelProperty(value="상품발행순번")
	private int issueSeq;
	
	//230413
	@ApiModelProperty(value="상품발행순번")
	private int joinSeq;
	
	//230420
	@ApiModelProperty(value="취소하기 활성화 여부")
	private String classCancelYn;
	
	//230515
	@ApiModelProperty(value="리워드 타입 (난수번호 : G0001, url : G0002)")
	private String rewardType;
	
	//230515
	@ApiModelProperty(value="URL ID")
	private String urlId;
		
}
