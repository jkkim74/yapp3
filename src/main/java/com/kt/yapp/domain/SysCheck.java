package com.kt.yapp.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 시스템 점검
 */
@Data
public class SysCheck 
{
	@ApiModelProperty(value="제목")
	private String title;
	
	@ApiModelProperty(value="점검내용")
	private String chkContents;

	@ApiModelProperty(value="점검 시작일시")
	private String chkStDt;

	@ApiModelProperty(value="점검 종료일시")
	private String chkEdDt;

	@ApiModelProperty(value="SEQ")
	@JsonIgnore
	private int chkSeq;

	@ApiModelProperty(value="사용여부")
	@JsonIgnore
	private String useYn;

	@ApiModelProperty(value="등록일시")
	private Date regDt;
}
