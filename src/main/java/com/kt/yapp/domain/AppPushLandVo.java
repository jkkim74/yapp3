package com.kt.yapp.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * PUSH 관련 정보
 */
@Data
public class AppPushLandVo {    
	@ApiModelProperty(value="랜딩할 탭 명")
    private String tab;
	
	@ApiModelProperty(value="title")
    private String title;
	
	@ApiModelProperty(value="url")
    private String url;
}

