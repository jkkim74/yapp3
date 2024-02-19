package com.kt.yapp.domain.req;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 공지사항
 */
@Data
public class NoticeReq
{
	@ApiModelProperty(value="조회시작 INDEX")
	private int stIdx;
	
	@ApiModelProperty(value="조회종료 INDEX")
	private int edIdx;
	
	@ApiModelProperty(value="팝업여부")
	@JsonIgnore
	private String popupYn;
}
