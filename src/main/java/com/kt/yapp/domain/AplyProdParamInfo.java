package com.kt.yapp.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 가입 단위 상품 파람정보
 */
@Data
public class AplyProdParamInfo 
{
	@ApiModelProperty(value="서비스계약아이디")
	private String svcContId;
	
	@ApiModelProperty(value="상품아이디")
	private String prodId;
	
	@ApiModelProperty(value="단위서비스아이디")
	private String unitSvcId;

	@ApiModelProperty(value="단위서비스명")
	private String unitSvcNm;

	@ApiModelProperty(value="상품이력일련번호")
	private String prodHstSeq;
	
	@ApiModelProperty(value="상품유형코드")
	private String prodTypeCd;
	
	@ApiModelProperty(value="유효시작일")
	private String efctStDt;
	
	@ApiModelProperty(value="유효종료일")
	private String efctFnsDt;
	
	@ApiModelProperty(value="파람내용")
	private String paramSbst;
}
