package com.kt.yapp.domain.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 선물하기
 */
@Data
public class GiftDataReq 
{
	@ApiModelProperty(value="송신자 계약번호")
	private String sndCntrNo;
	
	@ApiModelProperty(value="수신자 휴대폰번호")
	private String rcvMobileNo;

	@ApiModelProperty(value="수신자명")
	private String rcvUserNm;
	
	@ApiModelProperty(value="데이터 용량(MB)")
	private int dataAmt;
	
	@ApiModelProperty(value="푸쉬를 본내기 위한 단말기 device Token")
	private String deviceToken;
	
	@ApiModelProperty(value="SHA256 암호화된 비밀번호")
	private String giftPw;
}
