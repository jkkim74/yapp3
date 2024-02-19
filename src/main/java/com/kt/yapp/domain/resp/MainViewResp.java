package com.kt.yapp.domain.resp;

import java.util.List;

import com.kt.yapp.domain.BannerBenefit;
import com.kt.yapp.domain.BonusData;
import com.kt.yapp.domain.ContractInfo;
import com.kt.yapp.domain.Databox;
import com.kt.yapp.domain.Datuk;
import com.kt.yapp.domain.Event;
import com.kt.yapp.domain.EventMaster;
import com.kt.yapp.domain.GiftData;
import com.kt.yapp.domain.GiftPsbInfo;
import com.kt.yapp.domain.GrpCode;
import com.kt.yapp.domain.HiddenAreaData;
import com.kt.yapp.domain.HiddenAreaVasData;
import com.kt.yapp.domain.Notice;
import com.kt.yapp.domain.RecentContact;
import com.kt.yapp.domain.ReviewInfo;
import com.kt.yapp.domain.RewardMenu;
import com.kt.yapp.domain.RoomInvt;
import com.kt.yapp.domain.Terms;
import com.kt.yapp.domain.WsgDataUseQntRmn;
import com.kt.yapp.domain.YfriendsMenu;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 메인화면 표시 Response
 * 
 * @author
 * @since
 * @version 1.0
 * 
 * Modification Information
 * Mod Date		Modifier		Description
 * ========================================
 * 2019. 3. 23.	seungman.yu 	사용자 리뷰
 * Copyright (c) 2018 KTDS, Inc. All Rights Reserved
 */
@Data
public class MainViewResp 
{
	@ApiModelProperty(value="계약 정보")
	private ContractInfo cntrInfo;
	
	@ApiModelProperty(value="내 데이터박스 정보")
	private Databox myDboxInfo;

	@ApiModelProperty(value="내 데이터 정보")
	private WsgDataUseQntRmn myDataInfo;
	
	@ApiModelProperty(value="선물 가능 정보")
	private GiftPsbInfo giftPsbInfo;

	@ApiModelProperty(value="메인팝업 공지사항 목록")
	private List<Notice> popupNoticeList;

	@ApiModelProperty(value="메인팝업  이벤트 목록")
	private List<Event> popupEventList;
	
	@ApiModelProperty(value="신규 알림 메시지 유무")
	private String newNotiMsgYn;
//	private List<NoticeMsg> newNotiMsgList;
	
	@ApiModelProperty(value="띠배너 이벤트 목록")
	private List<Event> bannerEventList;
	
	@ApiModelProperty(value="받은 선물 목록")
	private List<GiftData> rcvGiftDataList;
	
	@ApiModelProperty(value="받은 조르기 목록")
	private List<GiftData> rcvDataReqList;
	
	@ApiModelProperty(value="보너스 데이터 목록")
	private List<BonusData> bonusDataList;

	@ApiModelProperty(value="이벤트 방초대 정보 목록")
	private List<RoomInvt> roomInvtList;
	
	@ApiModelProperty(value="와이프렌즈 메뉴 목록")
	private List<YfriendsMenu> yfriendsMenuList;
	
	@ApiModelProperty(value="hidden 영역 목록")
	private List<HiddenAreaData> hiddenAreaDataList;

	@ApiModelProperty(value="hidden 영역 부가 서비스 목록")
	private List<HiddenAreaVasData> hiddenAreaVasDataList;
	
	@ApiModelProperty(value="움직이는 Gif목록")
	private List<Event> movingEventGifList;

	@ApiModelProperty(value="Y 콜라보 이벤트목록")
	private List<Event> yCollaboEventList;

	@ApiModelProperty(value="회수대상 데이턱 목록")
	private List<Datuk> datukRtnList;
	
	@ApiModelProperty(value="Y앱 URL")
	private String yappUrl;

	@ApiModelProperty(value="Y앱 선물 URL")
	private String yappGiftUrl;
	
	@ApiModelProperty(value="Y앱 조르기 URL")
	private String yappReqUrl;
	
	@ApiModelProperty(value="Y앱 데이턱 URL")
	private String yappShareUrl;
	
	@ApiModelProperty(value="Y앱 초대 URL")
	private String yappInviteUrl;
	
	@ApiModelProperty(value="사용자 review 정보")
	private ReviewInfo userReviewInfo;
	
	@ApiModelProperty(value="공통 설정 목록")
	private List<GrpCode> cmnSetList;
	
	@ApiModelProperty(value="신규 리워드 알림 여부")
	private String newRewardYn;
	
	@ApiModelProperty(value="최근 연락처 목록")
	private List<RecentContact> recentContactList;
	
	@ApiModelProperty(value="신규 출석체크 알림 여부")
	private String newAttendYn;
	
	@ApiModelProperty(value="출석체크 이벤트 순번")
	private int attendEvtSeq;
	
	@ApiModelProperty(value="이벤트 배너")
	private EventMaster eventBanner;
	
	@ApiModelProperty(value="리워드 메뉴 정보")
	private RewardMenu rewardMenu;
	
	@ApiModelProperty(value="혜택배너 목록")
	private List<BannerBenefit> bannerBenefitList;
	
	@ApiModelProperty(value="KT선택약관 팝업여부")
	private String ktPopYn;

	@ApiModelProperty(value="약관 컨텐츠")
	private Terms terms;

	@ApiModelProperty(value="온라이마켓팅 API URL")
	private String omBannerApiUrl;

	@ApiModelProperty(value="온라이마켓팅 Y데이타박스 MainA 배너 ZondeCode정보")
	private String omBannerMainZoneCode;

	@ApiModelProperty(value="온라이마켓팅 API URL")
	private String omEncKtId;

	@ApiModelProperty(value="온라이마켓팅 배너 사용여부, 디폴트를 N으로 세팅")
	private String omBannerUseYn = "N";
	
	@ApiModelProperty(value="포인트 버튼 클릭시 랜딩 URL")
	private String pointLandUrl;
}
