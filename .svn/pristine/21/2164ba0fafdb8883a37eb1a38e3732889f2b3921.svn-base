package com.kt.yapp.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 주소 정보
 */
@Data
public class KmcAuthInfo 
{
	@ApiModelProperty(value="KMC 인증SEQ")
	private String kmcAuthSeq;
	
	@ApiModelProperty(value="요청일시 [yyyyMMddHHmmss]")
	private String kmcReqDt;
	
	@ApiModelProperty(value="처리결과 [Y:성공 , N:실패 , F:오류]")
	private String result;
	
	@ApiModelProperty(value="서비스방법 [M:휴대폰 본인확인 , P:공인인증서]")
	private String certMet;
	
	@ApiModelProperty(value="이동통신사 [SKT:SK],[KTF:KT],[LGT:LG U+],[SKM:SKT mvno],[KTM:KT mvno],[LGM:LG U+ mvno]")
	private String phoneCorp;
	
	@ApiModelProperty(value="휴대전화번호")
	private String phoneNumber;
	
}
