package com.kt.yapp.domain;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 안내 메시지
 */
@Data
public class GuideMsg 
{
	@ApiModelProperty(value="메시지 코드")
	private String msgCd;
	
	@ApiModelProperty(value="메시지명")
	private String msgNm;
	
	@ApiModelProperty(value="메시지 내용")
	private String msgContents;

	@ApiModelProperty(value="등록일시")
	private Date regDt;
}
