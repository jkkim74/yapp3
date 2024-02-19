package com.kt.yapp.domain.resp;

import com.kt.yapp.domain.Datuk;
import com.kt.yapp.domain.GiftPsbInfo;
import com.kt.yapp.domain.WsgDataUseQntRmn;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 데이터 나눔
 */
@Data
public class DataDivResp 
{
	@ApiModelProperty(value="내 데이터 정보")
	private WsgDataUseQntRmn myDataInfo;

	@ApiModelProperty(value="공유중인 데이턱")
	private Datuk datukInfo;

	@ApiModelProperty(value="선물 가능 정보")
	private GiftPsbInfo giftPsbInfo;
}
