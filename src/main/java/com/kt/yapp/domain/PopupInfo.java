package com.kt.yapp.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 팝업 이벤트 정보
 */
@Data
public class PopupInfo 
{
	@ApiModelProperty(value="팝업SEQ")
	private int popupSeq;
	
	@ApiModelProperty(value="제목")
	private String title;
	
	@ApiModelProperty(value="내용")
	private String contents;
	
	@ApiModelProperty(value="팝업 구분(G0001: 공지, G0002: 이벤트)")
	private String popupTp;
	
	@ApiModelProperty(value="팝업 시작일자")
	private String popupStYmd;
	
	@ApiModelProperty(value="팝업 종료일자")
	private String popupEdYmd;
	
	@ApiModelProperty(value="링크 타입(G0001: Y 앱, G0002: 외부 사이트)")
	private String linkTp;
	
	@ApiModelProperty(value="링크 URL")
	private String linkUrl;
	
	@ApiModelProperty(value="이미지 파일ID. 복수개 가능( ',' 로 구분)")
	private String imgFileId;
	
	@ApiModelProperty(value="띠배너 여부")
	@JsonIgnore
	private String bannerYn;
	
	@ApiModelProperty(value="사용여부")
	@JsonIgnore
	private String useYn;
	
	@ApiModelProperty(value="등록일시")
	@JsonIgnore
	private Date regDt;
	
	@ApiModelProperty(value="수정일시")
	@JsonIgnore
	private Date modDt;
}
