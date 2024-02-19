package com.kt.yapp.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Hidden Area 부가서비스 데이터
 */
@Data
public class HiddenAreaVasData 
{
	@ApiModelProperty(value="hidden 영역 구분(LONG : 장기혜택 쿠폰 , ROULET : 데이터 룰렛, LATER : 후불충전, MEM : 멤버십 충전, PULL : 당겨쓰기, THREE : 매일3시간 무료, HALF : 반값팩, DOUBLE : 두배쓰기, CHG : 바꿔쓰기)")
	private String hiddenGb;
	
	@ApiModelProperty(value="제목")
	private String title;
	
	@ApiModelProperty(value="내용")
	@JsonIgnore
	private String contents;

	@ApiModelProperty(value="등록일시")
	@JsonIgnore
	private Date regDt;
}
