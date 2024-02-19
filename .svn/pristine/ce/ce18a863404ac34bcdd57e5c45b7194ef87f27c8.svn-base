package com.kt.yapp.domain.resp;

import java.util.List;

import com.kt.yapp.domain.ContractInfo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 로그인 인증 체크 (ID/PW) Response
 */
@Data
public class LoginAcctResp 
{
	@ApiModelProperty(value="크레덴셜ID")
	private String credentialId;
	
	@ApiModelProperty(value="인증실패횟수")
	private int authFailCnt;
	
	@ApiModelProperty(value="세션ID")
	private String ysid;
	
	@ApiModelProperty(value="계약정보 목록")
	private List<ContractInfo> cntrInfoList;
//	private List<ContractInfoResp> cntrInfoList;
}
