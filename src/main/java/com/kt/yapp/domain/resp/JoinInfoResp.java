package com.kt.yapp.domain.resp;

import java.util.List;

import com.kt.yapp.domain.GiftData;
import com.kt.yapp.domain.JoinInfo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Y앱 가입정보
 */
@Data
public class JoinInfoResp 
{
	
	@ApiModelProperty(value="가입정보 목록")
	private List<JoinInfo> joinInfoList;
	
	@ApiModelProperty(value="최근 선물한 친구목록")
	private List<GiftData> giftDataList;
	
}
