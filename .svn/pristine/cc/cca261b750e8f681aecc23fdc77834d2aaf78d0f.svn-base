package com.kt.yapp.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 14세 미만 보호자 동의
 */
@Data
public class AdultAgree 
{
	@ApiModelProperty(value="보호자명")
	private String parentsNm;
	
	@ApiModelProperty(value="보호자 이메일 주소")
	private String emailAddr;
	
	@ApiModelProperty(value="메일 인증 여부")
	private String mailAgreeYn;
	
	@ApiModelProperty(value="보내는 사람명")
	private String sndUserNm;
	
	@ApiModelProperty(value="보내는 사람 핸드폰번호")
	private String sndUserTel;
	
	@ApiModelProperty(value="계약번호")
	@JsonIgnore
	private String cntrNo;
	
	@ApiModelProperty(value="메일 인증키")
	@JsonIgnore
	private String mailKey;
	
	@ApiModelProperty(value="등록일시")
	@JsonIgnore
	private Date regDt;

	@ApiModelProperty(value="수정일시")
	@JsonIgnore
	private Date modDt;
}
