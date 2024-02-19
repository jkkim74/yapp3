package com.kt.yapp.domain.req;

import java.util.List;

import com.kt.yapp.domain.EventGiftJoin;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EventGiftMultiReq {
	
	private Integer evtSeq;
	private Integer giftMaxCnt;
	private List<EventGiftJoin> eventGiftJoinList;

}
