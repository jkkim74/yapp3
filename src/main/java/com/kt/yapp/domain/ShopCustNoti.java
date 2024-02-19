package com.kt.yapp.domain;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * shop 알림함 
 */
@Data
public class ShopCustNoti {

	@ApiModelProperty(value="상태코드")
	private String yshopNotiStatusCd;
	
	@ApiModelProperty(value="제목")
	private String yshopNotiTitle;
	
	@ApiModelProperty(value="내용")
	private String yshopNotiDetail;
	
	@ApiModelProperty(value="url")
	private String yshopNotiUrl;
	
	@ApiModelProperty(value="이미지 url")
	private String yshopNotiImgUrl;
	
}
