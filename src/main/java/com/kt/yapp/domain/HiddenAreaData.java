package com.kt.yapp.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Hidden Area 데이터
 */
@Data
public class HiddenAreaData 
{
	@ApiModelProperty(value="hidden 영역 구분(VOLUME : 누적별 , EVT : 이벤트)")
	private String hiddenGb;
	
	@ApiModelProperty(value="제목")
	private String title;
	
	@ApiModelProperty(value="내용")
	private String contents;

	@ApiModelProperty(value="이벤트 유형(G0001: 외부링크, G0002: 단순배너, G0003: 페이스북공유, G0004: 당첨자 발표안내)")
	private String evtTp;
	
	@ApiModelProperty(value="이벤트SEQ")
	private int evtSeq;
	
	@ApiModelProperty(value="시작일자")
	private String stYmd;

	@ApiModelProperty(value="종료일자")
	private String edYmd;
	
	@ApiModelProperty(value="버튼명")
	private String evtBtnLbl;
	
	@ApiModelProperty(value="상세버튼명")
	private String evtBtnDtlLbl;
	
	@ApiModelProperty(value="진행구분")
	private String progressType;
	
	@ApiModelProperty(value="리스트 이미지 URL. 복수개 가능( ',' 로 구분)")
	private String evtListImgURL;
	
	@ApiModelProperty(value="외부 링크 URL")
	private String evtLinkUrl;
	
	@ApiModelProperty(value="hidden 이미지 URL")
	private String evtHiddenImgUrl;
	
	@ApiModelProperty(value="hidden 아이콘 이미지 URL")
	private String evtHiddenIconImgUrl;
	
	@ApiModelProperty(value="당첨여부")
	private String evtWinYn;
	
	@ApiModelProperty(value="응모여부")
	private String evtApplYn;
	
	@ApiModelProperty(value="종료여부")
	private String evtEndYn;
	
	@ApiModelProperty(value="달성여부")
	private String evtAchieveYn;
	
	@ApiModelProperty(value="달성금액")
	private int evtAchieveAmt;
	
	@ApiModelProperty(value="등록일시")
	private Date regDt;
}
