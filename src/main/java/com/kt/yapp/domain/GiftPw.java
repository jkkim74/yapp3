package com.kt.yapp.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 선물하기 비밀번호
 */
@Data
public class GiftPw 
{
	@ApiModelProperty(value="비밀번호 체크여부(Y:일치 , N:불일치")
	private String checkYn;
}
