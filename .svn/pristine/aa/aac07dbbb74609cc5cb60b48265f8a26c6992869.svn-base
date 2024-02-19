package com.kt.yapp.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * SNS 로그인 관련 정보
 */
@Data
public class SnsInfo 
{
	@ApiModelProperty(value="계약번호")
	private String cntrNo;
	
	@ApiModelProperty(value="SNS타입(S0001:카카오톡,S0002:라인,S0003:구글,S0004:애플)")
	private String snsType;
	
	@ApiModelProperty(value="SNS매핑키")
	private String snsKey;
	
	@ApiModelProperty(value="등록일시")
	@JsonIgnore
	private Date regDt;

	@ApiModelProperty(value="수정일시")
	@JsonIgnore
	private Date modDt;
}
