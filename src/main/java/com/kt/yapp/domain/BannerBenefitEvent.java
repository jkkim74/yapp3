package com.kt.yapp.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * REST API 호출 혜택배너 이벤트 조건
 */
@Data
public class BannerBenefitEvent 
{
	@ApiModelProperty(value="순번")
	private String seq;
	
	@ApiModelProperty(value="제목")
	private String title;
	
	@ApiModelProperty(value="내용")
	private String contents;
	
	@ApiModelProperty(value="파일주소")
	private String fileUrl;
	
	@ApiModelProperty(value="이미지 주소")
	private String imageUrl;
	
	@ApiModelProperty(value="링크 타입")
	private String linkType;

	@ApiModelProperty(value="랜딩 주소")
	private String landingUrl;
	
	@ApiModelProperty(value="태그명")
	private String tagNm;
	
	@ApiModelProperty(value="문구")
	private String phrases;
	
	@ApiModelProperty(value="배경 주소")
	private String backgroundUrl;

	@ApiModelProperty(value="우선순위")
	private String priority;
	
	@ApiModelProperty(value="요금제 코드")
	private String ppCd;
	
	@ApiModelProperty(value="성별  G0001: 여성, G0002: 남성, G0003: 전체")
	private String gender;
	
	@ApiModelProperty(value="나이대 목록")
	private String ageList;
	
	@ApiModelProperty(value="선호 카테고리 목록")
	private String prfList;
	
	@ApiModelProperty(value="OS타입 G0001: Android, G0002: IOS, G0003: 전체")
	private String osTp;

	@ApiModelProperty(value="OS버전")
	private String osVrsn;
}
