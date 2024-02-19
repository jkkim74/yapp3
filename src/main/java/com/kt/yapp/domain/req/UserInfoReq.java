package com.kt.yapp.domain.req;

import com.kt.yapp.domain.TermsAgree;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserInfoReq 
{
	@ApiModelProperty(value="계약번호")
	private String cntrNo;
	
	@ApiModelProperty(value="휴대폰번호")
	private String mobileNo;

	@ApiModelProperty(value="14세여부")
	private String fourteenYn;

	@ApiModelProperty(value="푸시 수신여부")
	private String pushRcvYn;
	
	@ApiModelProperty(value="마케팅 수신여부")
	private String mktRcvYn;
	
	@ApiModelProperty(value="마케팅정보 수신 이용 동의 여부(선택)")
	private String opt2TermsAgreeYn;
	
	@ApiModelProperty(value="kt개인정보 처리 위탁 및 고객 혜택 제공을 위한 광고 수신 동의여부(선택)")
	private String opt3TermsAgreeYn;
	
	@ApiModelProperty(value="kt개인정보 처리 위탁 및 고객 혜택 제공을 위한 광고 수신 동의여부(선택) 버전")
	private String opt3TermsVrsn;
	
	@ApiModelProperty(value="kt고객 헤택 제공을 위한 개인 정보 수집 및 이용 관련 동의여부(선택)")
	private String opt4TermsAgreeYn;
	
	@ApiModelProperty(value="kt고객 헤택 제공을 위한 개인 정보 수집 및 이용 관련 동의 동의(선택) 버전")
	private String opt4TermsVrsn;
	
	@ApiModelProperty(value="조르기 수신여부")
	private String reqRcvYn;

	@ApiModelProperty(value="약관 동의 정보")
	private TermsAgree termsAgree;
	
	/*20220318*/
	@ApiModelProperty(value="가입상태(G0001: 가입, G0002: 탈퇴중, G0003: 탈퇴)")
	private String joinStatus;
	
	@ApiModelProperty(value="KTID가입상태(G0001: 가입, G0002: 탈퇴중, G0003: 탈퇴)")
	private String joinStatusKt;
	
	@ApiModelProperty(value="회원상태(G0001: CNTR + KTID, G0002: CNTR, G0003: KTID)")
	private String memStatus;
	
	@ApiModelProperty(value="다음동작(G0001: 메인으로 이동, G0002: KTID인증 화면 띄우기, G0003: KT.COM 가입유도, G0004: 종료(로그인 화면으로 이동))")
	private String actionCode;
	
	@ApiModelProperty(value="사용자ID")
	private String userId;
	
	@ApiModelProperty(value="성별")
	private String gender;
	
	@ApiModelProperty(value="이메일")
	private String email;
	
	@ApiModelProperty(value="생년월일")
	private String birthDay;
	
	@ApiModelProperty(value="이름")
	private String userNm;
	
	@ApiModelProperty(value="요금제코드")
	private String ppCd;
	
	@ApiModelProperty(value="모바일코드")
	private String mobileCd;
	
	@ApiModelProperty(value="SHOP 이용약관 동의여부")
	private String shopTermsAgreeYn;
	
	@ApiModelProperty(value="SHOP 이용약관 버전")
	private String shopTermsVrsn;
}
