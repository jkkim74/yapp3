package com.kt.yapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 데이턱 상세
 */
@Data
public class DatukDtl 
{
	@ApiModelProperty(value="데이터 박스 ID")
	private String dboxId;
	
	@ApiModelProperty(value="유효시작일")
	private String efctStDt;
	
	@ApiModelProperty(value="수령일자")
	private String recpDt;
	
	@ApiModelProperty(value="한턱데이터")
	private int oneShotData;
	
	@ApiModelProperty(value="회수데이터")
	private int clectData;
	
	@ApiModelProperty(value="완료데이터")
	private int cmpltData;

	@ApiModelProperty(value="소멸데이터")
	private int extncData;
	
	@ApiModelProperty(value="회수가능데이터")
	private int clectPosblData;
	
	@ApiModelProperty(value="미완료데이터")
	private int ncmpltData;
	
	@ApiModelProperty(value="데이턱ID")
	private String datukId;
	
	@ApiModelProperty(value="계약번호")
	@JsonIgnore
	private String cntrNo;
	
}
