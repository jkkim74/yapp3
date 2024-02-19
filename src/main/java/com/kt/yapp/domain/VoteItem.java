package com.kt.yapp.domain;

import java.util.Date;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 210610
 * 투표대상 데이터
 * @author dejay
 */
@Data
public class VoteItem {
	@ApiModelProperty(value="투표대상순번")
	private int voteItemSeq;
	
	@ApiModelProperty(value="투표순번")
	private int voteSeq;

	@ApiModelProperty(value="투표대상텍스트")
	private String itemText;
	
	@ApiModelProperty(value="투표대상이미지")
	private String itemImage;
	
	//210628 투표대상건수 추가
	@ApiModelProperty(value="투표대상건수")
	private int voteItemCnt;

	@ApiModelProperty(value="등록일시")
	private Date regDt;
	
	@ApiModelProperty(value="수정일시")
	private Date modDt;
	
	@ApiModelProperty(value="투표율")
	private int votePercent;

}
