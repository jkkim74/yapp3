package com.kt.yapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *  리워드 메뉴정보
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class RewardMenu extends RestCommon {
	
	@ApiModelProperty(value="메뉴명")
	private String menuName;
	
	@ApiModelProperty(value="메뉴URL")
	private String menuUrl;
	
	@ApiModelProperty(value="메뉴노출여부")
	private String menuUseYn;
	
	@ApiModelProperty(value="메뉴New표시여부")
	private String newMenuYn;
	
}
