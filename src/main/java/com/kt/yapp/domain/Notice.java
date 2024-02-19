package com.kt.yapp.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 공지사항
 */
@Data
public class Notice
{
	@ApiModelProperty(value="공지사항 SEQ")
	private int noticeSeq;

	@ApiModelProperty(value="게시글 제목")
	private String title;

	@ApiModelProperty(value="게시글 내용")
	private String contents;

	@ApiModelProperty(value="팝업 이미지 URL. 복수개 가능( ',' 로 구분)")
	private String popupImgUrl;
	
	@ApiModelProperty(value="팝업여부")
	@JsonIgnore
	private String popupYn;
	
	@ApiModelProperty(value="등록일")
	private Date regDt;

	@ApiModelProperty(value="수정일")
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
	
	@ApiModelProperty(value="띠배너 여부")
	private String bannerYn;
	
	@ApiModelProperty(value="띠배너 아이콘 seq")
	private int bannerIconSeq;
	
	@ApiModelProperty(value="띠배너 아이콘 url")
	private String iconUrl;
}
