package com.kt.yapp.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 약관 동의 이력
 */
@Data
public class TermsAgree
{
	@ApiModelProperty(value="개인정보 이용 약관버전")
	private String piUseTermsVrsn;
	
	@ApiModelProperty(value="개인정보 취급방침 약관버전")
	private String piPolicyTermsVrsn;

	@ApiModelProperty(value="개인정보 이용 동의여부")
	private String piUseAgreeYn;
	
	@ApiModelProperty(value="개인정보 취급방침 동의 여부")
	private String piPolicyAgreeYn;

	@ApiModelProperty(value="마케팅 수신 동의 여부")
	private String mktRcvAgreeYn;
	
	@ApiModelProperty(value="선택약관 동의여부")
	private String optTermsAgreeYn;
	
	@ApiModelProperty(value="선택약관 수정일시")
	private Date optTermsChgDt;
	
	@ApiModelProperty(value="마케팅정보 수집 이용 동의 여부(선택)")
	private String opt2TermsAgreeYn;

	@ApiModelProperty(value="선택약관 수정일시")
	private Date opt2TermsChgDt;
	
	@ApiModelProperty(value="혜택/광고의 수신 및 위탁 동의 여부(선택)")
	private String opt3TermsAgreeYn;

	@ApiModelProperty(value="혜택/광고의 수신 및 위탁 동의 Vrsn(선택)")
	private String opt3TermsVrsn;
	
	@ApiModelProperty(value="선택약관 수정일시")
	private Date opt3TermsChgDt;

	@ApiModelProperty(value="고객 혜택 제공을 위한 정보수집/이용 동의 여부(선택)")
	private String opt4TermsAgreeYn;

	@ApiModelProperty(value="고객 혜택 제공을 위한 정보수집/이용 동의  Vrsn(선택)")
	private String opt4TermsVrsn;
	
	@ApiModelProperty(value="선택약관 수정일시")
	private Date opt4TermsChgDt;

	@ApiModelProperty(value="동의채널(G0001: YAPP, G0002: KOS)")
	private String agreeChnl;
	
	@ApiModelProperty(value="약관 SEQ")
	@JsonIgnore
	private int seq;
	
	@ApiModelProperty(value="계약번호")
	@JsonIgnore
	private String cntrNo;
	
	@ApiModelProperty(value="등록일시")
	@JsonIgnore
	private Date regDt;
	
	/*20220318*/
	@ApiModelProperty(value="사용자ID")
	private String userId;
	
	@ApiModelProperty(value="SHOP 이용약관 동의여부")
	private String shopTermsAgreeYn;
	
	@ApiModelProperty(value="SHOP 이용약관 버전")
	private String shopTermsVrsn;
	
	@ApiModelProperty(value="SHOP 이용약관 수정일시")
	private Date shopTermsChgDt;
}
