package com.kt.yapp.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserInfo 
{
	@ApiModelProperty(value="계약번호")
	private String cntrNo;
	
	@ApiModelProperty(value="휴대폰번호")
	private String mobileNo;

	@ApiModelProperty(value="사용자ID")
	private String userId;
	
	@ApiModelProperty(value="푸시 수신여부")
	private String pushRcvYn;
	
	@ApiModelProperty(value="마케팅 수신여부")
	private String mktRcvYn;

	@ApiModelProperty(value="선택약관2 수신여부")
	private String opt2TermsAgreeYn;
	
	@ApiModelProperty(value="선택약관3 수신여부")
	private String opt3TermsAgreeYn;

	@ApiModelProperty(value="선택약관3 ver")
	private String opt3TermsVrsn;
	
	@ApiModelProperty(value="선택약관4 수신여부")
	private String opt4TermsAgreeYn;
	
	@ApiModelProperty(value="선택약관4 ver")
	private String opt4TermsVrsn;

	@ApiModelProperty(value="조르기 수신여부")
	private String reqRcvYn;
	
	@ApiModelProperty(value="약관 동의 정보")
	private TermsAgree termsAgree;

	@ApiModelProperty(value="마케팅 수신동의(철회) 일시")
	private Date mktChgDt;
	
	@ApiModelProperty(value="14세여부")
	private String fourteenYn;
	
	@ApiModelProperty(value="가입상태(G0001: 가입, G0002: 탈퇴중, G0003: 탈퇴)")
	private String joinStatus;
	
	@ApiModelProperty(value="KTID가입상태(G0001: 가입, G0002: 탈퇴중, G0003: 탈퇴)")
	private String joinStatusKt;
	
	@ApiModelProperty(value="회원상태(G0001: CNTR + KTID, G0002: CNTR, G0003: KTID)")
	private String memStatus;
	
	@ApiModelProperty(value="다음동작(G0001: 메인으로 이동, G0002: KTID인증 화면 띄우기, G0003: KT.COM 가입유도, G0004: 종료(로그인 화면으로 이동))")
	private String actionCode;
	
	@ApiModelProperty(value="데이터박스 ID")
	private String dboxId;
	
	@ApiModelProperty(value="데이터박스 상태(G0001: 사용중, G0002: 해지, G0003: 정지)")
	private String dboxStatus;
	
	@ApiModelProperty(value="세션ID")
	private String ysid;
	
	@ApiModelProperty(value="보호자메일동의여부")
	private String mailAgreeYn;

	@ApiModelProperty(value="인증여부(E: 이메일인증YES, Y: sms인증YES, N: 인증NO")
	private String authYn;

	@ApiModelProperty(value="사용자명 (암호화")
	//@JsonIgnore
	private String userNm;
	
	@ApiModelProperty(value="데이터박스 가입년월")
	@JsonIgnore
	private String regMmDt;
	
	@ApiModelProperty(value="단말기 유형")
	@JsonIgnore
	private String osTp;
	
	@ApiModelProperty(value="중복로그인 KEY")
	@JsonIgnore
	private String dupId;
	
	@ApiModelProperty(value="os 버젼")
	@JsonIgnore
	private String osVrsn;
	
	@ApiModelProperty(value="app 버젼")
	@JsonIgnore
	private String appVrsn;

	@ApiModelProperty(value="이벤트 초대 여부")
	private String evtInvtYn;
	
	@ApiModelProperty(value="유휴계정 여부")
	private String sleepUserYn;
	
	@ApiModelProperty(value="유휴계정 등록날짜")
	private String sleepRegDt;
	
	@ApiModelProperty(value="생년월일")
	private String birthDay;
	
	@ApiModelProperty(value="SNS타입(S0001:카카오톡,S0002:라인,S0003:구글,S0004:애플)")
	private String snsType;
	
	@ApiModelProperty(value="선호도 노출여부")
	private String prfYn;
	
	@ApiModelProperty(value="선호도 노출 제한 일수")
	private int prfDay;
	
	@ApiModelProperty(value="선호도 플래그 값(Y:선택하기, null:Default, Z:다음에 하기, X:다시 안보기)")
	private String prfFlag;
	
	/*YBOX 3.0 20220308*/
	@ApiModelProperty(value="성별")
	private String gender;
	
	@ApiModelProperty(value="이메일")
	private String email;
	
	@ApiModelProperty(value="kt회선여부")
	private String ktYn;
	
	@ApiModelProperty(value="현재나이")
	private int currentAge;
	
	@ApiModelProperty(value="shop연동 uuid")
	private String shud;
	
	@ApiModelProperty(value="마스킹 모바일번호")
	private String maskingMobileNo;
	
	@ApiModelProperty(value="요금제코드")
	private String ppCd;
	
	@ApiModelProperty(value="모바일코드")
	private String mobileCd;
}
