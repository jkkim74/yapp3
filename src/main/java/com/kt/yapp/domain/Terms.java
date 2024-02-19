package com.kt.yapp.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 약관정보
 */
@Data
public class Terms 
{
	@ApiModelProperty(value="이용 약관버전")
	private String piUseTermsVrsn;

	@ApiModelProperty(value="이용 약관 내용")
	private String piUseTermsContents;
	
	@ApiModelProperty(value="이용약관 URL")
	private String piUseTermsUrl;

	@ApiModelProperty(value="개인정보 수집 및 이용 방침 약관버전")
	private String piPolicyTermsVrsn;

	@ApiModelProperty(value="개인정보 수집 및 이용 방침 약관 내용")
	private String piPolicyTermsContents;
	
	@ApiModelProperty(value="개인정보 수집 및 이용 방침 약관 URL")
	private String piPolicyTermsUrl;

	@ApiModelProperty(value="개인정보 처리방침 약관버전")
	private String piProcessTermsVrsn;

	@ApiModelProperty(value="개인정보 처리방침 약관 내용")
	private String piProcessTermsContents;
	
	@ApiModelProperty(value="개인정보 처리방침 약관 URL")
	private String piProcessTermsUrl;

	@ApiModelProperty(value="오픈소스 라이센스 약관 버전(안드로이드)")
	private String opnSrcTermsVrsn;

	@ApiModelProperty(value="오픈소스 라이센스 약관 내용(안드로이드)")
	private String opnSrcTermsContents;
	
	@ApiModelProperty(value="오픈소스 라이센스 약관 URL(안드로이드)")
	private String opnSrcTermsUrl;

	@ApiModelProperty(value="오픈소스 라이센스 약관 버전(IOS)")
	private String opnSrcIosTermsVrsn;

	@ApiModelProperty(value="오픈소스 라이센스 약관 내용(IOS)")
	private String opnSrcIosTermsContents;
	
	@ApiModelProperty(value="오픈소스 라이센스 약관 URL(IOS)")
	private String opnSrcIosTermsUrl;

	@ApiModelProperty(value="선택 약관 버전")
	@JsonIgnore
	private String optTermsVrsn;

	@ApiModelProperty(value="선택 약관 내용")
	@JsonIgnore
	private String optTermsContents;
	
	@ApiModelProperty(value="마케팅정보 이용동의(선택) 버전")
	private String opt2TermsVrsn;
	
	@ApiModelProperty(value="마케팅정보 이용동의(선택) 내용")
	private String opt2TermsContents;
	
	@ApiModelProperty(value="마케팅정보 이용동의(선택) 약관 URL")
	private String opt2TermsUrl;

	@ApiModelProperty(value="혜택/광고의 수신 및 위탁 동의(선택) 버전")
	private String opt3TermsVrsn;
	
	@ApiModelProperty(value="혜택/광고의 수신 및 위탁 동의(선택) 내용")
	private String opt3TermsContents;
	
	@ApiModelProperty(value="혜택/광고의 수신 및 위탁 동의(선택) 약관 URL")
	private String opt3TermsUrl;
	
	@ApiModelProperty(value="고객 혜택 제공을 위한 정보수집/이용 동의(선택) 버전")
	private String opt4TermsVrsn;
	
	@ApiModelProperty(value="고객 혜택 제공을 위한 정보수집/이용 동의(선택) 내용")
	private String opt4TermsContents;
	
	@ApiModelProperty(value="고객 혜택 제공을 위한 정보수집/이용 동의(선택) 약관 URL")
	private String opt4TermsUrl;
	
	@ApiModelProperty(value="마케팅 수신 약관버전")
	private String mktRcvTermsVrsn;

	@ApiModelProperty(value="마케팅 수신 약관 내용")
	private String mktRcvTermsContents;
	
	@ApiModelProperty(value="마케팅 수신 약관 URL")
	private String mktRcvTermsUrl;
	
	@ApiModelProperty(value="kt 개인정보 처리 위탁 및 고객 혜택 제공을 위한 광고 수신 동의")
	private String test1TermContents;

	@ApiModelProperty(value="kt고객 혜택 제공을 위한 개인정보 수집 및 이용 관련 동의")
	private String test2TermContents;
	
	@ApiModelProperty(value="등록일시")
	@JsonIgnore
	private Date regDt;
	
	@ApiModelProperty(value="샵 이용약관 버전")
	private String shopTermsVrsn;
	
	@ApiModelProperty(value="샵 이용약관 내용")
	private String shopTermsContents;
	
	@ApiModelProperty(value="샵 이용약관 URL")
	private String shopTermsUrl;
	
}
