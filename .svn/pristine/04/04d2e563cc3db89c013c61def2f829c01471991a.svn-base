package com.kt.yapp.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 이용안내
 */
@Data
public class Guide 
{
	@ApiModelProperty(value="제목")
	private String title;

	@ApiModelProperty(value="내용")
	private String contents;

	@ApiModelProperty(value="이미지 URL")
	private String imgUrl;
	
	@ApiModelProperty(value="순서")
	@JsonIgnore
	private int ordSeq;

	@ApiModelProperty(value="사용여부")
	@JsonIgnore
	private String useYn;
	
	@ApiModelProperty(value="seq")
	@JsonIgnore
	private int seq;

	@ApiModelProperty(value="등록일시")
	private Date regDt;
	
	@ApiModelProperty(value="수정일시")
	@JsonIgnore
	private Date modDt;
}
