package com.kt.yapp.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 인증키 획득 
 */
@Data
public class RsaKeyInfo 
{
	@ApiModelProperty(value="keySeq")
	private int keySeq;
	
	@ApiModelProperty(value="publickey")
	private String publicKey;
	
	@ApiModelProperty(value="privatekey")
	@JsonIgnore
	private String privateKey;

	@ApiModelProperty(value="modDt")
	@JsonIgnore
	private Date modDt;
	
	@ApiModelProperty(value="regDt")
	@JsonIgnore
	private Date regDt;
	
	@ApiModelProperty(value="userId")
	@JsonIgnore
	private String userId;
	
	@ApiModelProperty(value="사용여부")
	@JsonIgnore
	private String useYn;
}
