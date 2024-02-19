package com.kt.yapp.domain.resp;

import java.util.List;

import com.kt.yapp.domain.VasItem;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 부가서비스 상품
 */
@Data
public class VasItemResp 
{
	@ApiModelProperty(value="부가서비스 상품 목록")
	private List<VasItem> vasItemList;

	@ApiModelProperty(value="멤버십 포인트(-1은 멤버십 미가입)")
	private int memPoint;

	@ApiModelProperty(value="이번달 룰렛 실행여부")
	private String rouletteExecYn;
	
	@ApiModelProperty(value="당겨쓰기 가능용량")
	private int pullPsbDataAmt;
	
	@ApiModelProperty(value="이번달 룰렛 당첨상품")
	private String rouletteVasItem;

	@ApiModelProperty(value="온라이마켓팅 API URL")
	private String omBannerPointApiUrl;

	@ApiModelProperty(value="온라이마켓팅 Y데이타박스 MainA 배너 ZondeCode정보")
	private String omBannerPointZoneCode;

	@ApiModelProperty(value="온라이마켓팅 API URL")
	private String omEncKtId;

	@ApiModelProperty(value="온라이마켓팅 배너 사용여부, 디폴트를 N으로 세팅")
	private String omBannerUseYn = "N";
}
