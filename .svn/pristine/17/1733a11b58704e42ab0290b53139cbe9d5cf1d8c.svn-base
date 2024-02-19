package com.kt.yapp.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 알 사용량
 */
@Data
public class WsgGetEggTotalUse 
{
	private String returnCode;
	private String TransactionID;
	
	private SystemCheck systemCheck;

	@ApiModelProperty(value="서비스 명")
	private String winSvc;

	@ApiModelProperty(value="가입자 상태")
	private String status;

	@ApiModelProperty(value="월정액 알(기본 제공)")
	private int baseR;

	@ApiModelProperty(value="충전 알")
	private int chgR;

	@ApiModelProperty(value="수신 알")
	private int revR;

	@ApiModelProperty(value="매직엔 알")
	private int magicR;

	@ApiModelProperty(value="sms 잔여알")
	private int fsmsR;

	@ApiModelProperty(value="자동 상한 알")
	private int amaxR;

	@ApiModelProperty(value="영상 전용 알")
	private int videoR;

	@ApiModelProperty(value="ims 부가서비스 전용 알")
	private int imsR;

	@ApiModelProperty(value="영상 전용 부가서비스 알")
	private int videoVasR;

	@ApiModelProperty(value="무료 sms 전용 알")
	private int smsFreeR;

	@ApiModelProperty(value="이월 알")
	private int forwardR;

	@ApiModelProperty(value="데이터 수신 잔여량")
	private int dataRcv;

	@ApiModelProperty(value="선물알")
	private int giftR;

	@ApiModelProperty(value="데이터늘리기(두배쓰기): 0 : 미설정 / 1 : 설정")
	private String upscaleDataFlag;

	@ApiModelProperty(value="쿠폰 잔여량 목록")
	private List<Integer> coupRemainList = new ArrayList<>();
	
	@ApiModelProperty(value="쿠폰 사용 만료일 목록(쿠폰 잔여량 목록과 매핑)")
	private List<String> coupExpireList = new ArrayList<>();
	
	
	@ApiModelProperty(value="1번째 쿠폰 잔여량")
	@JsonIgnore
	private int coup1Remains;

	@ApiModelProperty(value="1번째 쿠폰 사용 만료일")
	@JsonIgnore
	private int coup1Expire;

	@ApiModelProperty(value="2번째 쿠폰 잔여량")
	@JsonIgnore
	private int coup2Remains;

	@ApiModelProperty(value="2번째 쿠폰 사용 만료일")
	@JsonIgnore
	private int coup2Expire;

	@ApiModelProperty(value="3번째 쿠폰 잔여량")
	@JsonIgnore
	private int coup3Remains;

	@ApiModelProperty(value="3번째 쿠폰 사용 만료일")
	@JsonIgnore
	private int coup3Expire;

	@ApiModelProperty(value="4번째 쿠폰 잔여량")
	@JsonIgnore
	private int coup4Remains;

	@ApiModelProperty(value="4번째 쿠폰 사용 만료일")
	@JsonIgnore
	private int coup4Expire;

	@ApiModelProperty(value="5번째 쿠폰 잔여량")
	@JsonIgnore
	private int coup5Remains;

	@ApiModelProperty(value="5번째 쿠폰 사용 만료일")
	@JsonIgnore
	private int coup5Expire;

	@ApiModelProperty(value="6번째 쿠폰 잔여량")
	@JsonIgnore
	private int coup6Remains;

	@ApiModelProperty(value="6번째 쿠폰 사용 만료일")
	@JsonIgnore
	private int coup6Expire;

	@ApiModelProperty(value="7번째 쿠폰 잔여량")
	@JsonIgnore
	private int coup7Remains;

	@ApiModelProperty(value="7번째 쿠폰 사용 만료일")
	@JsonIgnore
	private int coup7Expire;

	@ApiModelProperty(value="8번째 쿠폰 잔여량")
	@JsonIgnore
	private int coup8Remains;

	@ApiModelProperty(value="8번째 쿠폰 사용 만료일")
	@JsonIgnore
	private int coup8Expire;

	@ApiModelProperty(value="9번째 쿠폰 잔여량")
	@JsonIgnore
	private int coup9Remains;

	@ApiModelProperty(value="9번째 쿠폰 사용 만료일")
	@JsonIgnore
	private int coup9Expire;

	@ApiModelProperty(value="10번째 쿠폰 잔여량")
	@JsonIgnore
	private int coup10Remains;

	@ApiModelProperty(value="10번째 쿠폰 사용 만료일")
	@JsonIgnore
	private int coup10Expire;

	@Data
	public class SystemCheck
	{
		private String checkCode;
	}
	
}
