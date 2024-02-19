package com.kt.yapp.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 댓글
 */
@Data
public class EventReply 
{
	@ApiModelProperty(value="댓글 순번")
	private int replySeq;
	
	@ApiModelProperty(value="이벤트 번호")
	private int evtSeq;
	
	@ApiModelProperty(value="계약번호")
	private String cntrNo;
	
	@ApiModelProperty(value="아이디")
	private String userId;
	
	@ApiModelProperty(value="작성자")
	private String name;

	@ApiModelProperty(value="내용")
	private String contents;
	
	@ApiModelProperty(value="등록일시")
	private Date regDt;
	
	@ApiModelProperty(value="수정일시")
	private Date modDt;
	
	@ApiModelProperty(value="노출여부")
	private String blockYn;
	
	@ApiModelProperty(value="삭제여부")
	private String delYn;
	
	@ApiModelProperty(value="운영자 댓글 여부")
	private String adminYn;
	
	
	//2202117 댓글 더 보기
	private int endSeq;
	private int limit;
	
	//230411 위변조 확인용
	@ApiModelProperty(value="위변조 확인 타입 (C:계약번호, I:아이디)")
	private String originKey;
	
	@ApiModelProperty(value="위변조 확인 값")
	private String originValue;
	
	//230627
	@ApiModelProperty(value="댓글 작성자 여부")
	private String meYn;
	
}
