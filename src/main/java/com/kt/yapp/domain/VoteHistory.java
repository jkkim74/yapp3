package com.kt.yapp.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 투표 이력 정보
 * @author dejay
 *
 */
@Data
public class VoteHistory {
	
	@ApiModelProperty(value="이벤트 순번")
	private int evtSeq;
	
	@ApiModelProperty(value="투표대상순번")
	private int voteItemSeq;

	@ApiModelProperty(value="계약번호")
	private String cntrNo;
	
	@ApiModelProperty(value="사용자 아이디")
	private String userId;
	
	@ApiModelProperty(value="투표순번")
	private int voteSeq;
	
	@ApiModelProperty(value="등록일")
	private String regDt;
}
