package com.kt.yapp.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * lnb 데이터
 * @author 91286059
 *
 */
@Data
public class LnbInfo {
	
	@ApiModelProperty(value="알림신규여부")
	String notiNewYn;
	
}
