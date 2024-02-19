package com.kt.yapp.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 주소 정보
 */
@Data
public class AddressInfo 
{
	@ApiModelProperty(value="시도명")
	private String sidoNm;
	
	@ApiModelProperty(value="시군구명")
	private String sggNm;
	
	@ApiModelProperty(value="도로명")
	private String roadNm;
	
	@ApiModelProperty(value="지번명")
	private String eupMyunDongNm;

	@ApiModelProperty(value="건물번호")
	private int bldgNo;

	@ApiModelProperty(value="주소구분")
	private String adrDivCd;
	
	@ApiModelProperty(value="조회시작SEQ")
	private int dbRetvSeq;
	
	@ApiModelProperty(value="조회건수")
	private int dbRetvCascnt;
}
