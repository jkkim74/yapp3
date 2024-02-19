package com.kt.yapp.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 알림 메시지
 */
@Data
public class NoticeMsg 
{
	@ApiModelProperty(value="알림메시지")
	private String notiMsg;

	@ApiModelProperty(value="알림테이블타입")
	private String notiTableTp;

	@ApiModelProperty(value="알림타입(알림: G0001, 선물: G0002, 정보: G0003, shop: G0004)")
	private String notiTp;

	@ApiModelProperty(value="알림타입명(알림: G0001, 선물: G0002, 정보: G0003, shop: G0004)")
	private String notiTpNm;
	
	@ApiModelProperty(value="신규여부")
	private String newYn;
	
	@ApiModelProperty(value="LINK URL")
	private String linkUrl;

	@ApiModelProperty(value="조르기메세지여부")
	private String reqRcvYn;
	
	@ApiModelProperty(value="등록일시")
	private Date regDt;

	@ApiModelProperty(value="알림 시퀀스")
	private int notiMsgSeq;
	
	@ApiModelProperty(value="계약번호")
	@JsonIgnore
	private String cntrNo;
	
	@ApiModelProperty(value="알림 타입 상세 (광고성: G0101, 정보성: G0102, 데이터선물: G0201, 데이터조르기: G0202, 데이턱: G0203, 데이턱보내기:G0204, 배송상태:G0401, 주문완료:G0402, 결제확인:G0403, 반품완료:G0404, 교환신청:G0405)")
	private String notiTpDtl;
	
	@ApiModelProperty(value="삭제여부")
	@JsonIgnore
	private String delYn;
	
	@ApiModelProperty(value="push여부")
	@JsonIgnore
	private String pushYn;
	
	@ApiModelProperty(value="push결과코드")
	@JsonIgnore
	private String pushResultCd;
	
	@ApiModelProperty(value="수정일시")
	@JsonIgnore
	private Date modDt;
	
	@ApiModelProperty(value="마스터 시퀀스")
	@JsonIgnore
	private int notiSeq;
	
	/*220412 userid추가*/
	@ApiModelProperty(value="사용자ID")
	@JsonIgnore
	private String userId;
	
	/*220413 imgUrl 추가*/
	@ApiModelProperty(value="이미지URL")
	private String imgUrl;
	
	/*220413 notiTitle 추가*/
	@ApiModelProperty(value="알림제목")
	private String notiTitle;
		
}
