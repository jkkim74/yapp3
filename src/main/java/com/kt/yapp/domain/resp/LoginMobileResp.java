package com.kt.yapp.domain.resp;

import com.kt.yapp.domain.ContractInfo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 휴대폰 로그인 인증 체크 Response
 */
@Data
public class LoginMobileResp 
{
	@ApiModelProperty(value="인증실패횟수")
	private int authFailCnt;
	
	@ApiModelProperty(value="세션ID")
	private String ysid;
	
	@ApiModelProperty(value="계약정보")
	private ContractInfo cntrInfo;
//	private ContractInfoResp cntrInfo;
}
