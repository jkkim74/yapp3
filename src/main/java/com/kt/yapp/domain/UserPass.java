package com.kt.yapp.domain;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserPass 
{
	@ApiModelProperty(value="휴대폰번호")
	private String mobileNo;
	
	@ApiModelProperty(value="사용여부")
	private String useYn;
	
	@ApiModelProperty(value="등록일")
	private Date regDt;
}
