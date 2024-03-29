package com.kt.yapp.web;

import java.util.*;

import javax.crypto.BadPaddingException;
import javax.servlet.http.HttpServletRequest;

import com.kt.yapp.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kt.yapp.domain.Attend;
import com.kt.yapp.domain.BannerBenefit;
import com.kt.yapp.domain.BannerBenefitEvent;
import com.kt.yapp.domain.BannerBenefitUser;
import com.kt.yapp.domain.CallingPlan;
import com.kt.yapp.domain.ContractInfo;
import com.kt.yapp.domain.CustAgreeInfoRetvListDtoDetail;
import com.kt.yapp.domain.DataInfo;
import com.kt.yapp.domain.DataRewardData;
import com.kt.yapp.domain.DataShareList;
import com.kt.yapp.domain.Databox;
import com.kt.yapp.domain.Datuk;
import com.kt.yapp.domain.DsprData;
import com.kt.yapp.domain.EventMaster;
import com.kt.yapp.domain.GiftData;
import com.kt.yapp.domain.GrpCode;
import com.kt.yapp.domain.JoinInfo;
import com.kt.yapp.domain.MnthUsage;
import com.kt.yapp.domain.MyRmnData;
import com.kt.yapp.domain.Notice;
import com.kt.yapp.domain.PreferenceInfo;
import com.kt.yapp.domain.ReviewInfo;
import com.kt.yapp.domain.RewardData;
import com.kt.yapp.domain.RewardMenu;
import com.kt.yapp.domain.RoomChkInfo;
import com.kt.yapp.domain.RoomInvt;
import com.kt.yapp.domain.SessionContractInfo;
import com.kt.yapp.domain.TicketRewardData;
import com.kt.yapp.domain.UserInfo;
import com.kt.yapp.domain.VasItem;
import com.kt.yapp.domain.WsgDataUseQnt;
import com.kt.yapp.domain.WsgDataUseQntRmn;
import com.kt.yapp.domain.YfriendsMenu;
import com.kt.yapp.domain.resp.DataDivResp;
import com.kt.yapp.domain.resp.EventMainViewResp;
import com.kt.yapp.domain.resp.MainViewResp;
import com.kt.yapp.domain.resp.ResultInfo;
import com.kt.yapp.exception.YappAuthException;
import com.kt.yapp.exception.YappException;
import com.kt.yapp.redis.RedisComponent;
import com.kt.yapp.service.AttendService;
import com.kt.yapp.service.CmsService;
import com.kt.yapp.service.CommonService;
import com.kt.yapp.service.DatukService;
import com.kt.yapp.service.EventService;
import com.kt.yapp.service.GiftService;
import com.kt.yapp.service.HistService;
import com.kt.yapp.service.KosService;
import com.kt.yapp.service.RctContactService;
import com.kt.yapp.service.RewardService;
import com.kt.yapp.service.ShubService;
import com.kt.yapp.service.UserKtService;
import com.kt.yapp.service.UserService;
import com.kt.yapp.service.VasService;
import com.kt.yapp.service.WsgService;
import com.kt.yapp.service.YFriendsService;
import com.kt.yapp.soap.response.SoapResponse139;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * MainViewController.java
 * 
 * @author seungman.yu
 * @since 2018. 8. 10.
 * @version 1.0
 * 
 * Modification Information
 * Mod Date		Modifier		Description
 * ========================================
 * 2018. 8. 24.	seungman.yu 	Y데이터박스 이벤트 전체 개선
 * 2019. 3. 23.	seungman.yu 	사용자 리뷰
 * Copyright (c) 2018 KTDS, Inc. All Rights Reserved
 */
@RestController
@Api(description="메인화면 처리 컨트롤러")
public class MainViewController 
{
	private static final Logger logger = LoggerFactory.getLogger(MainViewController.class);
	private static final String SP = "||"; //20210712 세션 ID 갱신

	@Autowired
	private CommonService cmnService;
	@Autowired
	private UserService userService;
	@Autowired
	private CmsService cmsService;
	@Autowired
	private ShubService shubService;
	@Autowired
	private WsgService wsgService;
	@Autowired
	private GiftService giftService;
	@Autowired
	private DatukService datukService;
	@Autowired
	private KosService kosService;
	@Autowired
	private VasService vasService;
	@Autowired
	private HistService histService;
	@Autowired
	private YFriendsService yFriendsService;
	@Autowired
	private RewardService rewardService;
	@Autowired
	private RctContactService rctContactService;
	@Autowired
	private AttendService attendService;
	@Autowired
	private EventService eventService;
	@Autowired
	private RedisComponent redisComponent;
	@Autowired
	private KeyFixUtil keyFixUtil;
	@Autowired
	private AppEncryptUtils appEncryptUtils;
	@Autowired
	private UserKtService userKtService;
	
	@Value("${yapp.url}")
	private String yappUrl;

	@Value("${yapp.gift.url}")
	private String yappGiftUrl;
	
	@Value("${yapp.request.url}")
	private String yappReqUrl;
	
	@Value("${yapp.share.url}")
	private String yappShareUrl;
	
	@Value("${yapp.invite.url}")
	private String yappInviteUrl;

	@Value("${om.banner.url}")
	private String omBannerApiUrl;

	@Value("${om.banner.main.zoneCode}")
	private String omBannerMainZoneCode;

	@Autowired
	private Environment environment;

	@RequestMapping(value = "/yboxMain", method = RequestMethod.GET)
	@ApiOperation(value="Y박스 메인화면 정보 조회")
	@ApiImplicitParams({
		@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),
		@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), 
		@ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"),
		@ApiImplicitParam(name="osVrsn", value="단말 OS 버전", dataType="string", paramType="header"),
		@ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<MainViewResp> viewYboxMain(HttpServletRequest req) throws Exception
	{
		ResultInfo<MainViewResp> resultInfo = new ResultInfo<>();
		MainViewResp mainViewResp = new MainViewResp();
		resultInfo.setResultData(mainViewResp);

		String osTp = req.getHeader("osTp");
		String osVrsn = req.getHeader("osVrsn");
		String appVrsn = req.getHeader("appVrsn");
		
		String memStatus = "";
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		String loginUserId = null;
		String loginMobileNo = null;
		
		UserInfo userKtInfo = null;
		
		boolean existUser = false;
		boolean existUserKt = false;

		if(SessionKeeper.getSdata(req) != null){
			memStatus = SessionKeeper.getSdata(req).getMemStatus();
			loginUserId = SessionKeeper.getSdata(req).getUserId();
			loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
			userKtInfo = userService.getYappUserKtInfo(loginUserId);
		}
        // 온마시 정보 세팅 - Start
		// 1) ktId가 존재 하지 않는 경우, 온마시 배너 노출하지 않음, 디폴트가 오픈하지 않음(N) 으로 정의되어 있음..
		if(!StringUtils.isEmpty(loginUserId)){
			//2)공통코드에서 온마시 오픈하지 않는 경우(N) 온마시 배너노출하지 않음
			//3)공통코드에서 온마시 노출 처리 한경우(Y) 온마시 배너정보(API URL, zoneCode, ktId, 온마시배너오픈여부)를 앱에 내려줌..
			Optional<GrpCode> omBnrUseYn = Optional.ofNullable(cmsService.getCodeNm(CommonCodes.OmCodes.OM_GROUP_CODE, CommonCodes.OmCodes.OM_CODE_OPENYN));
			final String omLoginUserId = loginUserId;
			omBnrUseYn.ifPresent(omYn -> {
				final String codeKey = omYn.getCodeKey().trim().isEmpty() ? CommonCodes.YesOrNo.NO : omYn.getCodeKey().trim();
				mainViewResp.setOmBannerUseYn(codeKey);
				if (!StringUtils.isEmpty(codeKey) && codeKey.equals(CommonCodes.YesOrNo.YES)) {
					mainViewResp.setOmBannerApiUrl(omBannerApiUrl);//온마시 API URL 정보 추가
					mainViewResp.setOmBannerMainZoneCode(omBannerMainZoneCode); // 온마시 zoneCode정보(Main A배너)
					try {
						mainViewResp.setOmEncKtId(keyFixUtil.encode(omLoginUserId));//온마시 inisafeNet암호화 KT ID정보
					} catch (Exception e) {
						mainViewResp.setOmBannerUseYn(CommonCodes.YesOrNo.NO);
						logger.error("loginUserId inisafeNet encoding error : "+e.getMessage());
					}
				}
			});
		}
		// -End
		
		if(YappUtil.isEq(memStatus, "G0001") || YappUtil.isEq(memStatus, "G0003")){
		  if(userKtInfo!=null){
		    existUserKt = true;
		  }
		}
		
		logger.info("==============================================================");
		logger.info("/yboxMain -> memStatus : "+memStatus);
		logger.info("/yboxMain -> loginUserId : "+loginUserId);
		logger.info("/yboxMain -> loginCntrNo : "+loginCntrNo);
		logger.info("/yboxMain -> existUserKt : "+existUserKt);
		logger.info("==============================================================");
		
		// 계약정보 조회
		ContractInfo cntrInfo = shubService.callFn139(loginMobileNo, true, true).getCntrInfo();
		if(cntrInfo != null && cntrInfo.getUserInfo()!= null){
			
			existUser = true;
			
			// 번호 이동인 경우 TB_USER 전화번호와 비교하여 틀린경우 업데이트 해준다. redis db에서 기존 번호 삭제
			if(!cntrInfo.getMobileNo().equals(cntrInfo.getUserInfo().getMobileNo())){
				try{
					redisComponent.del(RedisComponent.MOBILE_NO_PFX_FRD + cntrInfo.getUserInfo().getMobileNo(), RedisComponent.KEY2_DBOX_STATUS);
					redisComponent.del(RedisComponent.MOBILE_NO_PFX_FRD + cntrInfo.getUserInfo().getMobileNo(), RedisComponent.KEY2_CNTR_NO);
					redisComponent.del(RedisComponent.MOBILE_NO_PFX_FRD + cntrInfo.getUserInfo().getMobileNo(), RedisComponent.KEY2_REQ_RCV_YN);
				}catch(RuntimeException e){
					logger.error("============= REDIS 접속이상 =============== : 전화번호 update 불가");
					histService.insertRedisErr(req);
				}catch(Exception e){
					logger.error("============= REDIS 접속이상 =============== : 전화번호 update 불가");
					histService.insertRedisErr(req);
				}
				cntrInfo.getUserInfo().setMobileNo(cntrInfo.getMobileNo());
				userService.updateUserMobileInfo(loginCntrNo, loginMobileNo);
			}
			
			cntrInfo.getUserInfo().setMemStatus(memStatus);
			
			//2022-06-01 by cha KTID정보 추가
			if(existUserKt){
				cntrInfo.getUserInfo().setEmail(userKtInfo.getEmail());
				cntrInfo.getUserInfo().setGender(userKtInfo.getGender());
			}
		}
		try{
			List<CustAgreeInfoRetvListDtoDetail> custAgreeList = shubService.callFn11267(cntrInfo.getCntrNo());
			cntrInfo.getUserInfo().getTermsAgree().setOpt3TermsAgreeYn(custAgreeList.get(1).getCustInfoAgreeYn().toString());
			cntrInfo.getUserInfo().getTermsAgree().setOpt3TermsVrsn(custAgreeList.get(1).getAgreeVerNo().toString());
			cntrInfo.getUserInfo().getTermsAgree().setOpt4TermsAgreeYn(custAgreeList.get(0).getCustInfoAgreeYn().toString());
			cntrInfo.getUserInfo().getTermsAgree().setOpt4TermsVrsn(custAgreeList.get(0).getAgreeVerNo().toString());
			cntrInfo.getUserInfo().setOpt3TermsAgreeYn(custAgreeList.get(1).getCustInfoAgreeYn().toString());		//선택이용약관3 동의여부
			cntrInfo.getUserInfo().setOpt3TermsVrsn(custAgreeList.get(1).getAgreeVerNo().toString())	;			//선택이용약관3 버전
			cntrInfo.getUserInfo().setOpt4TermsAgreeYn(custAgreeList.get(0).getCustInfoAgreeYn().toString());		//선택이용약관4 동의여부
			cntrInfo.getUserInfo().setOpt4TermsVrsn(custAgreeList.get(0).getAgreeVerNo().toString());				//선택이용약관4 버전
		
		}catch (RuntimeException e) {

			logger.info("이용약관 조회 error");
			cntrInfo.getUserInfo().getTermsAgree().setOpt3TermsAgreeYn("N");
			cntrInfo.getUserInfo().getTermsAgree().setOpt3TermsVrsn("1.0");
			cntrInfo.getUserInfo().getTermsAgree().setOpt4TermsAgreeYn("N");
			cntrInfo.getUserInfo().getTermsAgree().setOpt4TermsVrsn("1.0");
			cntrInfo.getUserInfo().setOpt3TermsAgreeYn("N");			//선택이용약관3 동의여부
			cntrInfo.getUserInfo().setOpt3TermsVrsn("1.0");				//선택이용약관3 버전
			cntrInfo.getUserInfo().setOpt4TermsAgreeYn("N");			//선택이용약관4 동의여부
			cntrInfo.getUserInfo().setOpt4TermsVrsn("1.0");				//선택이용약관4 버전
		}catch (Exception e) {

			logger.info("이용약관 조회 error");
			cntrInfo.getUserInfo().getTermsAgree().setOpt3TermsAgreeYn("N");
			cntrInfo.getUserInfo().getTermsAgree().setOpt3TermsVrsn("1.0");
			cntrInfo.getUserInfo().getTermsAgree().setOpt4TermsAgreeYn("N");
			cntrInfo.getUserInfo().getTermsAgree().setOpt4TermsVrsn("1.0");
			cntrInfo.getUserInfo().setOpt3TermsAgreeYn("N");			//선택이용약관3 동의여부
			cntrInfo.getUserInfo().setOpt3TermsVrsn("1.0");				//선택이용약관3 버전
			cntrInfo.getUserInfo().setOpt4TermsAgreeYn("N");			//선택이용약관4 동의여부
			cntrInfo.getUserInfo().setOpt4TermsVrsn("1.0");				//선택이용약관4 버전
		}
		
		mainViewResp.setCntrInfo(cntrInfo);
		
		// 데이터 박스 정보 조회
		Databox dbox = kosService.getDboxInfo(loginCntrNo);
		mainViewResp.setMyDboxInfo(dbox);

		logger.info("loginCntrNo  ====> " + loginCntrNo);
		logger.info("loginMobileNo  ====> " + loginMobileNo);
		// 내 데이터 정보 조회
		WsgDataUseQnt dataUseQnt = wsgService.getMobileTotalUseWeb(loginCntrNo, loginMobileNo, null, cntrInfo.getCallingPlan());

		mainViewResp.setMyDataInfo(YappCvtUtil.cvt(dataUseQnt, new WsgDataUseQntRmn()));

		// 공통 설정 목록
		List<GrpCode> cmnSetList = cmsService.getYBoxSrvBlockList();
		mainViewResp.setCmnSetList(cmnSetList);
		
		//나눔가능 정보
		mainViewResp.setGiftPsbInfo(giftService.getGiftPsbInfo(cntrInfo));
		 
		List<TicketRewardData> ticketRewardList= rewardService.getTicketRewardList(loginCntrNo, null, "Y");
		
		//데이터리워드 리스트 211207
		List<DataRewardData> dataRewardList= rewardService.getDataRewardList(loginCntrNo);
		
		//리워드 개수
		if(rewardService.getRewardCnt(loginCntrNo, null) > 0 || YappUtil.isNotEmpty(ticketRewardList) || YappUtil.isNotEmpty(dataRewardList)){ //211109 응모권 리워드 추가 //211207 데이터리워드 추가
			mainViewResp.setNewRewardYn("Y");
		}else{
			mainViewResp.setNewRewardYn("N");
		}
		
		//최근 연락처 리스트
		mainViewResp.setRecentContactList(rctContactService.getRecentContactList(loginCntrNo, req));
		
		//출석체크 정보
		int attEvtSeq = attendService.getAttendEvtSeq();
		Attend attend = attendService.getAttend(attEvtSeq, loginCntrNo);

		if(attEvtSeq == 0){
			//출석이벤트가 없을때
			mainViewResp.setNewAttendYn("");
		}else{
			EventMaster evtMaster = eventService.getEventDetailMaster(attEvtSeq);
			
			if(YappUtil.isNotEmpty(evtMaster)){
				if(evtMaster.getEvtType().equals("G0010")){
					String startDt = evtMaster.getEvtStartDt();
					//응모권 출석체크 210827 당월 말일자 구하기 9월배포
					Calendar currentCalendar = Calendar.getInstance();
					
					int year = Integer.valueOf(startDt.substring(0, 4));
					int month = Integer.valueOf(startDt.substring(5, 7));
					month = month -1;
					
					currentCalendar.set(year, month, 1);
					
					int endDay = currentCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
					
					if(attend.getMaxDay() == endDay){
						//응모권 출석체크 이벤트 완료시
						mainViewResp.setNewAttendYn("N");
					}else if(attend.getMaxDay() == 0){
						//응모권 출석체크 이벤트 입력 안했을때
						mainViewResp.setNewAttendYn("Y");
					}else{
						if(attendService.getAttendNowChk(attEvtSeq, loginCntrNo) > 0){
							mainViewResp.setNewAttendYn("N");
						}else{
							mainViewResp.setNewAttendYn("Y");
						}
					}
					//응모권 출석체크 210827 당월 말일자 구하기 9월배포
	
				}else{
					if(attend.getMaxDay() == 7){
						//출석체크 이벤트 완료시
						mainViewResp.setNewAttendYn("N");
					}else if(attend.getMaxDay() == 0){
						//출석체크 이벤트 입력 안했을때
						mainViewResp.setNewAttendYn("Y");
					}else{
						if(attendService.getAttendNowChk(attEvtSeq, loginCntrNo) > 0){
							mainViewResp.setNewAttendYn("N");
						}else{
							mainViewResp.setNewAttendYn("Y");
						}
					}
				}
			}else{
				mainViewResp.setNewAttendYn("");
			}
			
		}
		
		mainViewResp.setAttendEvtSeq(attEvtSeq);
		
		//혜택배너 목록 내려주기
		if(YappUtil.isEq(memStatus, "G0001") || YappUtil.isEq(memStatus, "G0002")){
			
			if(existUser){
				
				//23.07.04 landing URL 추가 @park
				String ppCd = "";
				String ppCatL = "";
				
				ppCd = cntrInfo.getUserInfo().getPpCd();
				if(YappUtil.isNotEmpty(ppCd)){
					ppCatL = cmsService.getPpCatLCode(ppCd).getPpCatL();
				}
				
				List<GrpCode> grpList = cmsService.getGrpCodeList("POINT_LAND_URL");
				//요금제에 따라 URL 별도로 적용되게 처리		
				for(GrpCode group : grpList){
					if(YappUtil.isNotEmpty(ppCd) && group.getCodeId().equals(ppCatL)){
						mainViewResp.setPointLandUrl(group.getCodeNm());
						break;
					}else{
						mainViewResp.setPointLandUrl("https://www.kt.com");
					}
				}
				
				logger.info("==============================================================");
				logger.info("/yboxMain -> setPpCd : "+ppCd);
				logger.info("/yboxMain -> setPpCatL : "+ppCatL);
				logger.info("/yboxMain -> setBirthDay : "+cntrInfo.getBirthDate());
				logger.info("/yboxMain -> setOsTp : "+osTp);
				logger.info("/yboxMain -> setOsVrsn : "+osVrsn);
				logger.info("==============================================================");
				
				BannerBenefitUser param = new BannerBenefitUser();
				
				param.setPpCd(cntrInfo.getPpCd());
				param.setOsTp(osTp);
				
				if(osVrsn != null && osVrsn.length()>2){
					
					osVrsn = osVrsn.substring(0,2);
					
					logger.info("==============================================================");
					logger.info("/yboxMain -> osVrsn.substring(0,2) : "+osVrsn);
					logger.info("==============================================================");
				}
				
				param.setOsVrsn(osVrsn);
				
				if(YappUtil.isNotEmpty(cntrInfo.getBirthDate())){
					
					param.setAge(YappUtil.getAgeGroupYear(cntrInfo.getBirthDate()));
				}
				
				String prfArrStr = userService.getPreferenceArrayString(loginCntrNo);
				
				if(YappUtil.isNotEmpty(prfArrStr)){
					
					String[] prfList = prfArrStr.split("\\|\\|");
					
					logger.info("==============================================================");
					logger.info("/yboxMain -> prfList : "+prfList.toString());
					logger.info("==============================================================");
					
					param.setPrfList(prfList);
				}
				
				if(existUserKt){
					
					String gender = "";
					
					if(YappUtil.isEq(userKtInfo.getGender(), "01")){
						gender = "G0001";
					}else if(YappUtil.isEq(userKtInfo.getGender(), "02")){
						gender = "G0002";
					}
					param.setGender(gender);
					
					logger.info("==============================================================");
					logger.info("/yboxMain -> gender : "+gender);
					logger.info("==============================================================");
				}
				
				List<BannerBenefitEvent> eventList = cmsService.getBenefitBannerEventList(param);
				
				if(eventList!=null && eventList.size()>0){
					
					logger.info("==============================================================");
					logger.info("/yboxMain -> eventList.size() : "+eventList.size());
					logger.info("==============================================================");
					
					List<BannerBenefit> bannerBenefitList = new ArrayList<>();
					
					int i = 0;
					
					for(BannerBenefitEvent event: eventList){
						
						if(i>5){
							break;
						}
						
						BannerBenefit banner = new BannerBenefit();
						
						banner.setTitle(event.getTitle());
						banner.setContents(event.getContents());
						banner.setFileUrl(event.getFileUrl());
						banner.setImageUrl(event.getImageUrl());
						banner.setLandingUrl(event.getLandingUrl());
						banner.setLinkType(event.getLinkType());
						banner.setTagNm(event.getTagNm());
						banner.setPhrases(event.getPhrases());
						banner.setBackgroundUrl(event.getBackgroundUrl());
						
						bannerBenefitList.add(banner);
						
						i++;
						
						logger.info("==============================================================");
						logger.info("/yboxMain -> 혜택배너 -> setTitle         : "+event.getTitle());
						logger.info("/yboxMain -> 혜택배너 -> setContents      : "+event.getContents());
						logger.info("/yboxMain -> 혜택배너 -> setFile_url      : "+event.getFileUrl());
						logger.info("/yboxMain -> 혜택배너 -> setImageUrl      : "+event.getImageUrl());
						logger.info("/yboxMain -> 혜택배너 -> setLanding_url   : "+event.getLandingUrl());
						logger.info("/yboxMain -> 혜택배너 -> setLink_type     : "+event.getLinkType());
						logger.info("/yboxMain -> 혜택배너 -> setTagNm         : "+event.getTagNm());
						logger.info("/yboxMain -> 혜택배너 -> setPhrases       : "+event.getPhrases());
						logger.info("/yboxMain -> 혜택배너 -> setBackgroundUrl : "+event.getBackgroundUrl());
						logger.info("==============================================================");
					}
					
					mainViewResp.setBannerBenefitList(bannerBenefitList);
				}
			}
		}
		
		//개인정보 암호화 및 제거
		mainViewResp.getCntrInfo().setBirthDate("");
		
		//220613 마스킹 모바일번호 셋팅
		mainViewResp.getCntrInfo().setMaskingMobileNo(YappUtil.blindMidEndMobileNo(mainViewResp.getCntrInfo().getMobileNo()));
		
		//220608 마스킹 모바일번호 셋팅
		mainViewResp.getCntrInfo().getUserInfo().setMaskingMobileNo(YappUtil.blindMidEndMobileNo(mainViewResp.getCntrInfo().getMobileNo()));
	
		//220620 계약정보 암호화(모바일번호/생년월일)
		cntrInfo = cmnService.aesEncCntrInfoField(cntrInfo, req);

		logger.info("yboxMain mainViewResp : " + mainViewResp);
		
		return resultInfo;
	}
	
	@RequestMapping(value = "/eventMain", method = RequestMethod.GET)
	@ApiOperation(value="이벤트 메인화면 정보 조회")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<EventMainViewResp> viewEventMain(HttpServletRequest req) throws Exception
	{
		ResultInfo<EventMainViewResp> resultEventInfo = new ResultInfo<>();
		EventMainViewResp eventMainViewResp = new EventMainViewResp();
		resultEventInfo.setResultData(eventMainViewResp);
		
		//20210712  관리자 이벤트 테스트 용이성 향상 시작
		//관리자 회선 or 사용자 회선 체크
		//테스트 이벤트 여부 동적쿼리로 처리
		String adminYn = "N";
		String cntrNo = SessionKeeper.getCntrNo(req);
		String grpCodeId = "EVT_TEST_CNTR_NO";
		
		if(!YappUtil.isEmpty(cntrNo)){
			if(cmsService.getGrpCodeList(grpCodeId, cntrNo).size() > 0){
				adminYn = "Y";
			}
		}

		List<EventMaster> eventMainList = eventService.getEventMainList(adminYn);
		//20210712  관리자 이벤트 테스트 용이성 향상  끝

		for (EventMaster eventMaster : eventMainList) {
			if(YappUtil.isEmpty(eventMaster.getEvtOptionTitle())){
				if("G0001".equals(eventMaster.getEvtType()) || "G0002".equals(eventMaster.getEvtType())){
					eventMaster.setEvtTypeNm("이벤트");
				}
			}else{
				eventMaster.setEvtTypeNm(eventMaster.getEvtOptionTitle());
			}
			
		}
		
		eventMainViewResp.setEventMainList(eventMainList);
		
		return resultEventInfo;
	}
	
	@RequestMapping(value = "/main", method = RequestMethod.GET)
	@ApiOperation(value="메인화면 정보 조회")
	@ApiImplicitParams({
		@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),
		@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), 
		@ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), 
		@ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header"),
		@ApiImplicitParam(name="mobileCd", value="모바일 코드", dataType="string", paramType="header")})
	public ResultInfo<MainViewResp> viewMain(HttpServletRequest req) throws Exception
	{
		ResultInfo<MainViewResp> resultInfo = new ResultInfo<>();
		MainViewResp mainViewResp = new MainViewResp();
		resultInfo.setResultData(mainViewResp);
		
		// y 앱 URL
		mainViewResp.setYappGiftUrl(yappGiftUrl);
		mainViewResp.setYappReqUrl(yappReqUrl);
		mainViewResp.setYappShareUrl(yappShareUrl);
		mainViewResp.setYappInviteUrl(yappInviteUrl);
		
		//이벤트 배너 이미지를 랜덤으로 조회한다.
		mainViewResp.setEventBanner(eventService.getEventBanner());
		
		String loginYn = loginCheck(req);
		String ktPopYn = "N";
		
		// 팝업 공지사항 조회
		List<Notice> noticeList = cmsService.getNewNoticeList();
		
		List<Notice> noticePopupList =  new ArrayList<>();
		for(Notice noticeTmp : noticeList){
			if("Y".equals(noticeTmp.getPopupYn())){
				noticePopupList.add(noticeTmp);
			}
			
		}
		mainViewResp.setPopupNoticeList(noticePopupList);

		//로그인 했을때만
		if("Y".equals(loginYn)){
			String loginCntrNo = SessionKeeper.getCntrNo(req);
			String loginMobileNo = null;
			String loginUserId = null;
			String memStatus ="";
			String loginUserNm = null;
			
			if(SessionKeeper.getSdata(req) != null){
				loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
				loginUserId = SessionKeeper.getSdata(req).getUserId();
				memStatus = SessionKeeper.getSdata(req).getMemStatus();
				loginUserNm = SessionKeeper.getSdata(req).getName();
			}
			
			String osTp = req.getHeader("osTp");
			String appVrsn = req.getHeader("appVrsn");
			String mobileCd = req.getHeader("mobileCd");
			
			logger.info("==============================================================");
			logger.info("/main -> loginCntrNo : "+loginCntrNo);
			logger.info("/main -> loginMobileNo : "+loginMobileNo);
			logger.info("/main -> loginUserId : "+loginUserId);
			logger.info("/main -> memStatus : "+memStatus);
			logger.info("/main -> loginUserNm : "+loginUserNm);
			logger.info("==============================================================");
			
			//kt 비회선
			if(YappUtil.isEmpty(loginCntrNo)){
				if(!YappUtil.isEmpty(loginMobileNo)){
					if(loginMobileNo.indexOf("ktid") != -1 ){
						// 사용자 review 정보 조회
						ReviewInfo reviewReq = new ReviewInfo();
						reviewReq.setUserId(loginUserId);
						ReviewInfo reviewResp = userKtService.getUserKtReviewInfo(reviewReq);
						
						//220711 userReviewInfo userId암호화
						String encUserId = appEncryptUtils.aesEnc128(reviewResp.getUserId(), osTp+appVrsn);
						reviewResp.setUserId(encUserId);
						
						mainViewResp.setUserReviewInfo(reviewResp);
						
						ContractInfo cntrInfo = new ContractInfo();
						//비kt회선 사용자정보 220406
						UserInfo userKtInfo = userService.getYappUserKtInfo(loginUserId);
						
						if((mobileCd !=null && !mobileCd.equals(userKtInfo.getMobileCd()))
								||!memStatus.equals(userKtInfo.getMemStatus())){
							
							//사용자 정보 업데이트
							userService.updateUserPpcdAndMobileCd(false, true, null, loginUserId, null, mobileCd, memStatus, null);
						}
						
						userKtInfo.setMemStatus(memStatus);
						
						//220607 마스킹 모바일번호 셋팅
						userKtInfo.setMaskingMobileNo(YappUtil.blindMidEndMobileNo(userKtInfo.getMobileNo()));
						
						//221114 자동로그인으로 세션정보에 이름이 없을 경우
						if(SessionKeeper.getSdata(req) != null && YappUtil.isEmpty(loginUserNm)){
							loginUserNm = YappUtil.blindNameToName(userKtInfo.getUserNm(),1);
							SessionKeeper.getSdata(req).setName(loginUserNm);
						}
						
						//TODO 마스킹된 이름 세팅
						//cntrPlan.setUserNm(YappUtil.blindNameToName(partyName, 1));
						cntrInfo.setUserNm(YappUtil.blindNameToName(userKtInfo.getUserNm(),1));
						cntrInfo.setUserInfo(userKtInfo);
						
						//리워드 메뉴 노출 20220716 by cha
						List<RewardData> rewardList=rewardService.getRewardList(null, loginUserId);
						List<TicketRewardData> ticketRewardList= rewardService.getTicketRewardList(null, loginUserId, "Y");
						GrpCode grpCode = cmsService.getCodeNm("MENU_USE_STATUS", "M0001");
						
						RewardMenu rewardMenu = new RewardMenu();
						rewardMenu.setMenuUrl("/reward/reward");
						rewardMenu.setMenuName("리워드");
						rewardMenu.setMenuUseYn(grpCode.getUseYn());
						
						logger.info("rewardList : "+rewardList);
						logger.info("ticketRewardList : "+ticketRewardList);
						
						if(YappUtil.isNotEmpty(rewardList) || YappUtil.isNotEmpty(ticketRewardList)){
							rewardMenu.setNewMenuYn("Y");
						}else{
							rewardMenu.setNewMenuYn("N");
						}
						
						logger.info("rewardMenu getNewMenuYn : "+rewardMenu.getNewMenuYn());

						mainViewResp.setRewardMenu(rewardMenu);
						
						mainViewResp.setCntrInfo(cntrInfo);
					
						//220620 계약정보 암호화(모바일번호/생년월일)
						cntrInfo = cmnService.aesEncCntrInfoField(cntrInfo, req);
						
						logger.info("main kt비회선 mainViewResp : "+mainViewResp);
					}
				}
				
			//kt 회선
			}else{
				
				// 계약정보 조회
				ContractInfo cntrInfo = shubService.callFn139(loginMobileNo, true, true).getCntrInfo();
				
				if(cntrInfo != null && cntrInfo.getUserInfo()!= null){
					// 번호 이동인 경우 TB_USER 전화번호와 비교하여 틀린경우 업데이트 해준다. redis db에서 기존 번호 삭제
					if(!cntrInfo.getMobileNo().equals(cntrInfo.getUserInfo().getMobileNo())){
						try{
							redisComponent.del(RedisComponent.MOBILE_NO_PFX_FRD + cntrInfo.getUserInfo().getMobileNo(), RedisComponent.KEY2_DBOX_STATUS);
							redisComponent.del(RedisComponent.MOBILE_NO_PFX_FRD + cntrInfo.getUserInfo().getMobileNo(), RedisComponent.KEY2_CNTR_NO);
							redisComponent.del(RedisComponent.MOBILE_NO_PFX_FRD + cntrInfo.getUserInfo().getMobileNo(), RedisComponent.KEY2_REQ_RCV_YN);
						}catch(RuntimeException e){
							logger.error("============= REDIS 접속이상 =============== : 전화번호 update 불가");
							histService.insertRedisErr(req);
						}catch(Exception e){
							logger.error("============= REDIS 접속이상 =============== : 전화번호 update 불가");
							histService.insertRedisErr(req);
						}
						cntrInfo.getUserInfo().setMobileNo(cntrInfo.getMobileNo());
						userService.updateUserMobileInfo(loginCntrNo, loginMobileNo);
					}
					
					if(!cntrInfo.getPpCd().equals(cntrInfo.getUserInfo().getPpCd())
							||(mobileCd !=null && !mobileCd.equals(cntrInfo.getUserInfo().getMobileCd()))
							||!memStatus.equals(cntrInfo.getUserInfo().getMemStatus())){
						
						//사용자 정보 업데이트
						userService.updateUserPpcdAndMobileCd(true, false, cntrInfo.getCntrNo(), null, cntrInfo.getPpCd(), mobileCd, memStatus, null);
					}

					//사용자정보에 생년월일이 없는 경우 업데이트
					if(YappUtil.isEmpty(cntrInfo.getUserInfo().getBirthDay())){
						userService.updateUserBirthDayInfo(loginCntrNo, cntrInfo.getBirthDate());
					}
					
					//221114 자동로그인으로 세션정보에 이름이 없을 경우
					if(SessionKeeper.getSdata(req) != null && YappUtil.isEmpty(loginUserNm)){
						loginUserNm =cntrInfo.getUserNm();
						SessionKeeper.getSdata(req).setName(loginUserNm);
					}
				}
				
				try{
					List<CustAgreeInfoRetvListDtoDetail> custAgreeList = shubService.callFn11267(cntrInfo.getCntrNo());			
					cntrInfo.getUserInfo().getTermsAgree().setOpt3TermsAgreeYn(custAgreeList.get(1).getCustInfoAgreeYn().toString());
					cntrInfo.getUserInfo().getTermsAgree().setOpt3TermsVrsn(custAgreeList.get(1).getAgreeVerNo().toString());
					cntrInfo.getUserInfo().getTermsAgree().setOpt4TermsAgreeYn(custAgreeList.get(0).getCustInfoAgreeYn().toString());
					cntrInfo.getUserInfo().getTermsAgree().setOpt4TermsVrsn(custAgreeList.get(0).getAgreeVerNo().toString());
					cntrInfo.getUserInfo().setOpt3TermsAgreeYn(custAgreeList.get(1).getCustInfoAgreeYn().toString());		//선택이용약관3 동의여부
					cntrInfo.getUserInfo().setOpt3TermsVrsn(custAgreeList.get(1).getAgreeVerNo().toString())	;			//선택이용약관3 버전
					cntrInfo.getUserInfo().setOpt4TermsAgreeYn(custAgreeList.get(0).getCustInfoAgreeYn().toString());		//선택이용약관4 동의여부
					cntrInfo.getUserInfo().setOpt4TermsVrsn(custAgreeList.get(0).getAgreeVerNo().toString());				//선택이용약관4 버전
				
				}catch (RuntimeException e) {
	
					logger.info("이용약관 조회 error");
					cntrInfo.getUserInfo().getTermsAgree().setOpt3TermsAgreeYn("N");
					cntrInfo.getUserInfo().getTermsAgree().setOpt3TermsVrsn("1.0");
					cntrInfo.getUserInfo().getTermsAgree().setOpt4TermsAgreeYn("N");
					cntrInfo.getUserInfo().getTermsAgree().setOpt4TermsVrsn("1.0");
					cntrInfo.getUserInfo().setOpt3TermsAgreeYn("N");			//선택이용약관3 동의여부
					cntrInfo.getUserInfo().setOpt3TermsVrsn("1.0");				//선택이용약관3 버전
					cntrInfo.getUserInfo().setOpt4TermsAgreeYn("N");			//선택이용약관4 동의여부
					cntrInfo.getUserInfo().setOpt4TermsVrsn("1.0");				//선택이용약관4 버전
				}catch (Exception e) {
	
					logger.info("이용약관 조회 error");
					cntrInfo.getUserInfo().getTermsAgree().setOpt3TermsAgreeYn("N");
					cntrInfo.getUserInfo().getTermsAgree().setOpt3TermsVrsn("1.0");
					cntrInfo.getUserInfo().getTermsAgree().setOpt4TermsAgreeYn("N");
					cntrInfo.getUserInfo().getTermsAgree().setOpt4TermsVrsn("1.0");
					cntrInfo.getUserInfo().setOpt3TermsAgreeYn("N");			//선택이용약관3 동의여부
					cntrInfo.getUserInfo().setOpt3TermsVrsn("1.0");				//선택이용약관3 버전
					cntrInfo.getUserInfo().setOpt4TermsAgreeYn("N");			//선택이용약관4 동의여부
					cntrInfo.getUserInfo().setOpt4TermsVrsn("1.0");				//선택이용약관4 버전
				}
				
				cntrInfo.getUserInfo().setMemStatus(memStatus);
				mainViewResp.setCntrInfo(cntrInfo);
				
				// 데이턱 회수 데이터가 있는 경우 정보 조회 (데이턱 공유 기간 종료)
				Datuk datuk = new Datuk();
				datuk.setCntrNo(loginCntrNo);
				List<Datuk> datukRtnList = datukService.getDatukRtnDataList(loginCntrNo, null);
				mainViewResp.setDatukRtnList(datukRtnList);
				
				// 받은 선물 목록 조회
				GiftData paramGift = new GiftData();
				paramGift.setRcvCntrNo(loginCntrNo);
				List<GiftData> rcvGiftList = giftService.getRcvGiftDataList(paramGift);
				mainViewResp.setRcvGiftDataList(rcvGiftList);

				// 받은 선물 수신 확인 처리
				if ( YappUtil.isNotEmpty(rcvGiftList) ){
					giftService.updateRcvConfirmGift(loginCntrNo);
				}
				
				// 받은 조르기 목록 조회
				GiftData paramDataReq = new GiftData();
				paramDataReq.setRcvCntrNo(loginCntrNo);
				List<GiftData> rcvDataReqList = giftService.getDataReqList(paramDataReq);
				mainViewResp.setRcvDataReqList(rcvDataReqList);

				// 받은 조르기 수신 확인 처리
				if ( YappUtil.isNotEmpty(rcvDataReqList) ){
					giftService.updateRcvConfirmDataReq(loginCntrNo);
				}
				
				// 사용자 review 정보 조회
				ReviewInfo reviewReq = new ReviewInfo();
				reviewReq.setCntrNo(loginCntrNo);
				ReviewInfo reviewResp = userService.getUserReviewInfo(reviewReq);
				mainViewResp.setUserReviewInfo(reviewResp);
				
				//20211025  관리자 이벤트 테스트 용이성 향상 시작
				//관리자 회선 or 사용자 회선 체크
				//테스트 이벤트 여부 동적쿼리로 처리
				String adminYn = "N";
				String grpCodeId = "EVT_TEST_CNTR_NO";
				
				if(!YappUtil.isEmpty(loginCntrNo)){
					if(cmsService.getGrpCodeList(grpCodeId, loginCntrNo).size() > 0){
						adminYn = "Y";
					}
				}
				
				// 와이프렌즈 메뉴조회
				List<YfriendsMenu> yfriendsMenuList = yFriendsService.getYfriendsMenuList(osTp, "Y", loginCntrNo, adminYn);
				mainViewResp.setYfriendsMenuList(yfriendsMenuList);
				// 이벤트 방초대 정보 조회
					
				//와이 프렌즈 초대 목록
				if("Y".equals(cntrInfo.getUserInfo().getEvtInvtYn())){
					//참여한 방목록 조회
					List<RoomChkInfo> joinCountList =  yFriendsService.getEventJoinCount(loginCntrNo, 0);
					ArrayList<Object> emSeqList = new ArrayList<Object>();
					
					for (RoomChkInfo roomChkInfo : joinCountList) {
						if(roomChkInfo.getRmCnt() == 0){
							emSeqList.add(roomChkInfo.getEmSeq());
						}
					}
					
					//초대리스트 조회
					List<RoomInvt> roomInvtList = yFriendsService.getRoomInvtList(loginCntrNo, 0, osTp, emSeqList);
					if(roomInvtList != null && roomInvtList.size() > 0){
						if("N".equals(roomInvtList.get(0).getAgeChkYn()) || "N".equals(roomInvtList.get(0).getDeviceChkYn()) || "N".equals(roomInvtList.get(0).getCallingChkYn())){
							if(YappUtil.getCurDate().equals(roomInvtList.get(0).getRegDt())){
								mainViewResp.setRoomInvtList(roomInvtList);
							}
						}else{
							mainViewResp.setRoomInvtList(roomInvtList);
						}
						
					}
				}
				
				List<RewardData> rewardList=rewardService.getRewardList(loginCntrNo, null);
				List<TicketRewardData> ticketRewardList= rewardService.getTicketRewardList(loginCntrNo, null, "Y");
				GrpCode grpCode = cmsService.getCodeNm("MENU_USE_STATUS", "M0001");
				
				RewardMenu rewardMenu = new RewardMenu();
				rewardMenu.setMenuUrl("/reward/reward");
				rewardMenu.setMenuName("리워드");
				rewardMenu.setMenuUseYn(grpCode.getUseYn());
				
				logger.info("rewardList : "+rewardList);
				logger.info("ticketRewardList : "+ticketRewardList);
				
				//데이터리워드 리스트 211207
				List<DataRewardData> dataRewardList= rewardService.getDataRewardList(loginCntrNo);
				logger.info("dataRewardList : "+dataRewardList);
				
				if(YappUtil.isNotEmpty(rewardList) || YappUtil.isNotEmpty(ticketRewardList) || YappUtil.isNotEmpty(dataRewardList)){
					rewardMenu.setNewMenuYn("Y");
				}else{
					rewardMenu.setNewMenuYn("N");
				}
				
				logger.info("rewardMenu getNewMenuYn : "+rewardMenu.getNewMenuYn());

				mainViewResp.setRewardMenu(rewardMenu);
				
				//개인정보 암호화 및 제거
				mainViewResp.getCntrInfo().setBirthDate("");
				//220613 마스킹 모바일번호 셋팅
				mainViewResp.getCntrInfo().setMaskingMobileNo(YappUtil.blindMidEndMobileNo(mainViewResp.getCntrInfo().getMobileNo()));
				//220607 마스킹 모바일번호 셋팅
				mainViewResp.getCntrInfo().getUserInfo().setMaskingMobileNo(YappUtil.blindMidEndMobileNo(mainViewResp.getCntrInfo().getMobileNo()));

				//220620 계약정보 암호화(모바일번호/생년월일)
				cntrInfo = cmnService.aesEncCntrInfoField(cntrInfo, req);
				logger.info("main kt회선 mainViewResp : "+mainViewResp);
				
			}
			
			//20210712 2주 미사용시 로그아웃 처리개선 시작
			String encSessionKey = reBuildYsid(req);
			mainViewResp.getCntrInfo().getUserInfo().setYsid(encSessionKey);
			
			//mainViewResp.setYsid(encSessionKey);
			//20210712 2주 미사용시 로그아웃 처리개선 끝
		}
		return resultInfo;
	}
	
	//230228 메인화면 호출 api
	@RequestMapping(value = "/mainApi", method = RequestMethod.GET)
	@ApiOperation(value="메인화면 정보 조회")
	@ApiImplicitParams({
		@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),
		@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), 
		@ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), 
		@ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header"),
		@ApiImplicitParam(name="mobileCd", value="모바일 코드", dataType="string", paramType="header")})
	public ResultInfo<MainViewResp> viewMainApi(HttpServletRequest req) throws Exception
	{
		ResultInfo<MainViewResp> resultInfo = new ResultInfo<>();
		MainViewResp mainViewResp = new MainViewResp();
		resultInfo.setResultData(mainViewResp);
		
		// y 앱 URL
		mainViewResp.setYappGiftUrl(yappGiftUrl);
		mainViewResp.setYappReqUrl(yappReqUrl);
		mainViewResp.setYappShareUrl(yappShareUrl);
		mainViewResp.setYappInviteUrl(yappInviteUrl);
		
		//이벤트 배너 이미지를 랜덤으로 조회한다.
		mainViewResp.setEventBanner(eventService.getEventBanner());
		
		String loginYn = loginCheck(req);
		String ktPopYn = "N";
		
		// 팝업 공지사항 조회
		List<Notice> noticeList = cmsService.getNewNoticeList();
		
		List<Notice> noticePopupList =  new ArrayList<>();
		for(Notice noticeTmp : noticeList){
			if("Y".equals(noticeTmp.getPopupYn())){
				noticePopupList.add(noticeTmp);
			}
			
		}
		mainViewResp.setPopupNoticeList(noticePopupList);

		//로그인 했을때만
		if("Y".equals(loginYn)){
			String loginCntrNo = SessionKeeper.getCntrNo(req);
			String loginMobileNo = null;
			String loginUserId = null;
			String memStatus ="";
			String loginUserNm = null;
			
			if(SessionKeeper.getSdata(req) != null){
				loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
				loginUserId = SessionKeeper.getSdata(req).getUserId();
				memStatus = SessionKeeper.getSdata(req).getMemStatus();
				loginUserNm = SessionKeeper.getSdata(req).getName();
			}
			
			String osTp = req.getHeader("osTp");
			String appVrsn = req.getHeader("appVrsn");
			String mobileCd = req.getHeader("mobileCd");
			
			logger.info("==============================================================");
			logger.info("/yMain -> loginCntrNo : "+loginCntrNo);
			logger.info("/yMain -> loginMobileNo : "+loginMobileNo);
			logger.info("/yMain -> loginUserId : "+loginUserId);
			logger.info("/yMain -> memStatus : "+memStatus);
			logger.info("/yMain -> loginUserNm : "+loginUserNm);
			logger.info("==============================================================");
			
			//kt 비회선
			if(YappUtil.isEmpty(loginCntrNo)){
				if(!YappUtil.isEmpty(loginMobileNo)){
					if(loginMobileNo.indexOf("ktid") != -1 ){
						// 사용자 review 정보 조회
						ReviewInfo reviewReq = new ReviewInfo();
						reviewReq.setUserId(loginUserId);
						ReviewInfo reviewResp = userKtService.getUserKtReviewInfo(reviewReq);
						
						//220711 userReviewInfo userId암호화
						String encUserId = appEncryptUtils.aesEnc128(reviewResp.getUserId(), osTp+appVrsn);
						reviewResp.setUserId(encUserId);
						
						mainViewResp.setUserReviewInfo(reviewResp);
						
						ContractInfo cntrInfo = new ContractInfo();
						//비kt회선 사용자정보 220406
						UserInfo userKtInfo = userService.getYappUserKtInfo(loginUserId);
						
						if((mobileCd !=null && !mobileCd.equals(userKtInfo.getMobileCd()))
								||!memStatus.equals(userKtInfo.getMemStatus())){
							
							//사용자 정보 업데이트
							userService.updateUserPpcdAndMobileCd(false, true, null, loginUserId, null, mobileCd, memStatus, null);
						}
						
						userKtInfo.setMemStatus(memStatus);
						
						//220607 마스킹 모바일번호 셋팅
						userKtInfo.setMaskingMobileNo(YappUtil.blindMidEndMobileNo(userKtInfo.getMobileNo()));
						
						//221114 자동로그인으로 세션정보에 이름이 없을 경우
						if(SessionKeeper.getSdata(req) != null && YappUtil.isEmpty(loginUserNm)){
							loginUserNm = YappUtil.blindNameToName(userKtInfo.getUserNm(),1);
							SessionKeeper.getSdata(req).setName(loginUserNm);
						}
						
						//TODO 마스킹된 이름 세팅
						//cntrPlan.setUserNm(YappUtil.blindNameToName(partyName, 1));
						cntrInfo.setUserNm(YappUtil.blindNameToName(userKtInfo.getUserNm(),1));
						cntrInfo.setUserInfo(userKtInfo);
						
						//리워드 메뉴 노출 20220716 by cha
						List<RewardData> rewardList=rewardService.getRewardList(null, loginUserId);
						List<TicketRewardData> ticketRewardList= rewardService.getTicketRewardList(null, loginUserId, "Y");
						GrpCode grpCode = cmsService.getCodeNm("MENU_USE_STATUS", "M0001");
						
						RewardMenu rewardMenu = new RewardMenu();
						rewardMenu.setMenuUrl("/reward/reward");
						rewardMenu.setMenuName("리워드");
						rewardMenu.setMenuUseYn(grpCode.getUseYn());
						
						logger.info("rewardList : "+rewardList);
						logger.info("ticketRewardList : "+ticketRewardList);
						
						if(YappUtil.isNotEmpty(rewardList) || YappUtil.isNotEmpty(ticketRewardList)){
							rewardMenu.setNewMenuYn("Y");
						}else{
							rewardMenu.setNewMenuYn("N");
						}
						
						logger.info("rewardMenu getNewMenuYn : "+rewardMenu.getNewMenuYn());

						mainViewResp.setRewardMenu(rewardMenu);
						
						mainViewResp.setCntrInfo(cntrInfo);
					
						//220620 계약정보 암호화(모바일번호/생년월일)
						cntrInfo = cmnService.aesEncCntrInfoField(cntrInfo, req);
						
						logger.info("main kt비회선 mainViewResp : "+mainViewResp);
					}
				}
				
			//kt 회선
			}else{
				
				// 계약정보 조회
				ContractInfo cntrInfo = shubService.callFn139(loginMobileNo, true, true).getCntrInfo();
				
				if(cntrInfo != null && cntrInfo.getUserInfo()!= null){
					// 번호 이동인 경우 TB_USER 전화번호와 비교하여 틀린경우 업데이트 해준다. redis db에서 기존 번호 삭제
					if(!cntrInfo.getMobileNo().equals(cntrInfo.getUserInfo().getMobileNo())){
						try{
							redisComponent.del(RedisComponent.MOBILE_NO_PFX_FRD + cntrInfo.getUserInfo().getMobileNo(), RedisComponent.KEY2_DBOX_STATUS);
							redisComponent.del(RedisComponent.MOBILE_NO_PFX_FRD + cntrInfo.getUserInfo().getMobileNo(), RedisComponent.KEY2_CNTR_NO);
							redisComponent.del(RedisComponent.MOBILE_NO_PFX_FRD + cntrInfo.getUserInfo().getMobileNo(), RedisComponent.KEY2_REQ_RCV_YN);
						}catch(RuntimeException e){
							logger.error("============= REDIS 접속이상 =============== : 전화번호 update 불가");
							histService.insertRedisErr(req);
						}catch(Exception e){
							logger.error("============= REDIS 접속이상 =============== : 전화번호 update 불가");
							histService.insertRedisErr(req);
						}
						cntrInfo.getUserInfo().setMobileNo(cntrInfo.getMobileNo());
						userService.updateUserMobileInfo(loginCntrNo, loginMobileNo);
					}
					
					if(!cntrInfo.getPpCd().equals(cntrInfo.getUserInfo().getPpCd())
							||(mobileCd !=null && !mobileCd.equals(cntrInfo.getUserInfo().getMobileCd()))
							||!memStatus.equals(cntrInfo.getUserInfo().getMemStatus())){
						
						//사용자 정보 업데이트
						userService.updateUserPpcdAndMobileCd(true, false, cntrInfo.getCntrNo(), null, cntrInfo.getPpCd(), mobileCd, memStatus, null);
					}

					//사용자정보에 생년월일이 없는 경우 업데이트
					if(YappUtil.isEmpty(cntrInfo.getUserInfo().getBirthDay())){
						userService.updateUserBirthDayInfo(loginCntrNo, cntrInfo.getBirthDate());
					}
					
					//221114 자동로그인으로 세션정보에 이름이 없을 경우
					if(SessionKeeper.getSdata(req) != null && YappUtil.isEmpty(loginUserNm)){
						loginUserNm =cntrInfo.getUserNm();
						SessionKeeper.getSdata(req).setName(loginUserNm);
					}
				}
				
				try{
					List<CustAgreeInfoRetvListDtoDetail> custAgreeList = shubService.callFn11267(cntrInfo.getCntrNo());		
					cntrInfo.getUserInfo().getTermsAgree().setOpt3TermsAgreeYn(custAgreeList.get(1).getCustInfoAgreeYn().toString());
					cntrInfo.getUserInfo().getTermsAgree().setOpt3TermsVrsn(custAgreeList.get(1).getAgreeVerNo().toString());
					cntrInfo.getUserInfo().getTermsAgree().setOpt4TermsAgreeYn(custAgreeList.get(0).getCustInfoAgreeYn().toString());
					cntrInfo.getUserInfo().getTermsAgree().setOpt4TermsVrsn(custAgreeList.get(0).getAgreeVerNo().toString());
					cntrInfo.getUserInfo().setOpt3TermsAgreeYn(custAgreeList.get(1).getCustInfoAgreeYn().toString());		//선택이용약관3 동의여부
					cntrInfo.getUserInfo().setOpt3TermsVrsn(custAgreeList.get(1).getAgreeVerNo().toString())	;			//선택이용약관3 버전
					cntrInfo.getUserInfo().setOpt4TermsAgreeYn(custAgreeList.get(0).getCustInfoAgreeYn().toString());		//선택이용약관4 동의여부
					cntrInfo.getUserInfo().setOpt4TermsVrsn(custAgreeList.get(0).getAgreeVerNo().toString());				//선택이용약관4 버전
				
				}catch (RuntimeException e) {
	
					logger.info("이용약관 조회 error");
					cntrInfo.getUserInfo().getTermsAgree().setOpt3TermsAgreeYn("N");
					cntrInfo.getUserInfo().getTermsAgree().setOpt3TermsVrsn("1.0");
					cntrInfo.getUserInfo().getTermsAgree().setOpt4TermsAgreeYn("N");
					cntrInfo.getUserInfo().getTermsAgree().setOpt4TermsVrsn("1.0");
					cntrInfo.getUserInfo().setOpt3TermsAgreeYn("N");			//선택이용약관3 동의여부
					cntrInfo.getUserInfo().setOpt3TermsVrsn("1.0");				//선택이용약관3 버전
					cntrInfo.getUserInfo().setOpt4TermsAgreeYn("N");			//선택이용약관4 동의여부
					cntrInfo.getUserInfo().setOpt4TermsVrsn("1.0");				//선택이용약관4 버전
				}catch (Exception e) {
	
					logger.info("이용약관 조회 error");
					cntrInfo.getUserInfo().getTermsAgree().setOpt3TermsAgreeYn("N");
					cntrInfo.getUserInfo().getTermsAgree().setOpt3TermsVrsn("1.0");
					cntrInfo.getUserInfo().getTermsAgree().setOpt4TermsAgreeYn("N");
					cntrInfo.getUserInfo().getTermsAgree().setOpt4TermsVrsn("1.0");
					cntrInfo.getUserInfo().setOpt3TermsAgreeYn("N");			//선택이용약관3 동의여부
					cntrInfo.getUserInfo().setOpt3TermsVrsn("1.0");				//선택이용약관3 버전
					cntrInfo.getUserInfo().setOpt4TermsAgreeYn("N");			//선택이용약관4 동의여부
					cntrInfo.getUserInfo().setOpt4TermsVrsn("1.0");				//선택이용약관4 버전
				}
				
				if("N".equals(cntrInfo.getUserInfo().getTermsAgree().getOpt3TermsAgreeYn())
						||"N".equals(cntrInfo.getUserInfo().getTermsAgree().getOpt4TermsAgreeYn())){

					GrpCode ktGrpCode = cmsService.getCodeNm("KT_TERMS_POP_CODE", "KTP01");
					
					logger.info("============================================================");
					logger.info("/yMain -> ktGrpCode yn: "+ktGrpCode.getUseYn());
					logger.info("/yMain -> ktGrpCode day: "+ktGrpCode.getCodeNm());
					logger.info("============================================================");
					
					if("Y".equals(ktGrpCode.getUseYn())){
						
						String strDay = ktGrpCode.getCodeNm();
						int day = 0;
						
						if(strDay!=null){
							day = Integer.parseInt(strDay);
						}
						
						if(day == 0){
							
							ktPopYn = "Y";
							mainViewResp.setTerms(cmsService.getTermsAgreeInfo());
						}else{
							
							Calendar now = Calendar.getInstance();
							
							logger.info("============================================================");
							logger.info("/main -> ktGrpCode today: "+now.get(Calendar.DAY_OF_MONTH));
							logger.info("============================================================");
							
							if(day == now.get(Calendar.DAY_OF_MONTH)){
								ktPopYn = "Y";
								mainViewResp.setTerms(cmsService.getTermsAgreeInfo());
							}
						}
					}
				}
				
				mainViewResp.setKtPopYn(ktPopYn);
				cntrInfo.getUserInfo().setMemStatus(memStatus);
				mainViewResp.setCntrInfo(cntrInfo);
				
				// 데이턱 회수 데이터가 있는 경우 정보 조회 (데이턱 공유 기간 종료)
				Datuk datuk = new Datuk();
				datuk.setCntrNo(loginCntrNo);
				List<Datuk> datukRtnList = datukService.getDatukRtnDataList(loginCntrNo, null);
				mainViewResp.setDatukRtnList(datukRtnList);
				
				// 받은 선물 목록 조회
				GiftData paramGift = new GiftData();
				paramGift.setRcvCntrNo(loginCntrNo);
				List<GiftData> rcvGiftList = giftService.getRcvGiftDataList(paramGift);
				mainViewResp.setRcvGiftDataList(rcvGiftList);

				// 받은 선물 수신 확인 처리
				if ( YappUtil.isNotEmpty(rcvGiftList) ){
					giftService.updateRcvConfirmGift(loginCntrNo);
				}
				
				// 받은 조르기 목록 조회
				GiftData paramDataReq = new GiftData();
				paramDataReq.setRcvCntrNo(loginCntrNo);
				List<GiftData> rcvDataReqList = giftService.getDataReqList(paramDataReq);
				mainViewResp.setRcvDataReqList(rcvDataReqList);

				// 받은 조르기 수신 확인 처리
				if ( YappUtil.isNotEmpty(rcvDataReqList) ){
					giftService.updateRcvConfirmDataReq(loginCntrNo);
				}
				
				// 사용자 review 정보 조회
				ReviewInfo reviewReq = new ReviewInfo();
				reviewReq.setCntrNo(loginCntrNo);
				ReviewInfo reviewResp = userService.getUserReviewInfo(reviewReq);
				mainViewResp.setUserReviewInfo(reviewResp);
				
				//20211025  관리자 이벤트 테스트 용이성 향상 시작
				//관리자 회선 or 사용자 회선 체크
				//테스트 이벤트 여부 동적쿼리로 처리
				String adminYn = "N";
				String grpCodeId = "EVT_TEST_CNTR_NO";
				
				if(!YappUtil.isEmpty(loginCntrNo)){
					if(cmsService.getGrpCodeList(grpCodeId, loginCntrNo).size() > 0){
						adminYn = "Y";
					}
				}
				
				// 와이프렌즈 메뉴조회
				List<YfriendsMenu> yfriendsMenuList = yFriendsService.getYfriendsMenuList(osTp, "Y", loginCntrNo, adminYn);
				mainViewResp.setYfriendsMenuList(yfriendsMenuList);
				// 이벤트 방초대 정보 조회
					
				//와이 프렌즈 초대 목록
				if("Y".equals(cntrInfo.getUserInfo().getEvtInvtYn())){
					//참여한 방목록 조회
					List<RoomChkInfo> joinCountList =  yFriendsService.getEventJoinCount(loginCntrNo, 0);
					ArrayList<Object> emSeqList = new ArrayList<Object>();
					
					for (RoomChkInfo roomChkInfo : joinCountList) {
						if(roomChkInfo.getRmCnt() == 0){
							emSeqList.add(roomChkInfo.getEmSeq());
						}
					}
					
					//초대리스트 조회
					List<RoomInvt> roomInvtList = yFriendsService.getRoomInvtList(loginCntrNo, 0, osTp, emSeqList);
					if(roomInvtList != null && roomInvtList.size() > 0){
						if("N".equals(roomInvtList.get(0).getAgeChkYn()) || "N".equals(roomInvtList.get(0).getDeviceChkYn()) || "N".equals(roomInvtList.get(0).getCallingChkYn())){
							if(YappUtil.getCurDate().equals(roomInvtList.get(0).getRegDt())){
								mainViewResp.setRoomInvtList(roomInvtList);
							}
						}else{
							mainViewResp.setRoomInvtList(roomInvtList);
						}
						
					}
				}
				
				List<RewardData> rewardList=rewardService.getRewardList(loginCntrNo, null);
				List<TicketRewardData> ticketRewardList= rewardService.getTicketRewardList(loginCntrNo, null, "Y");
				GrpCode grpCode = cmsService.getCodeNm("MENU_USE_STATUS", "M0001");
				
				RewardMenu rewardMenu = new RewardMenu();
				rewardMenu.setMenuUrl("/reward/reward");
				rewardMenu.setMenuName("리워드");
				rewardMenu.setMenuUseYn(grpCode.getUseYn());
				
				logger.info("rewardList : "+rewardList);
				logger.info("ticketRewardList : "+ticketRewardList);
				
				//데이터리워드 리스트 211207
				List<DataRewardData> dataRewardList= rewardService.getDataRewardList(loginCntrNo);
				logger.info("dataRewardList : "+dataRewardList);
				
				if(YappUtil.isNotEmpty(rewardList) || YappUtil.isNotEmpty(ticketRewardList) || YappUtil.isNotEmpty(dataRewardList)){
					rewardMenu.setNewMenuYn("Y");
				}else{
					rewardMenu.setNewMenuYn("N");
				}
				
				logger.info("rewardMenu getNewMenuYn : "+rewardMenu.getNewMenuYn());

				mainViewResp.setRewardMenu(rewardMenu);
				
				//개인정보 암호화 및 제거
				mainViewResp.getCntrInfo().setBirthDate("");
				//220613 마스킹 모바일번호 셋팅
				mainViewResp.getCntrInfo().setMaskingMobileNo(YappUtil.blindMidEndMobileNo(mainViewResp.getCntrInfo().getMobileNo()));
				//220607 마스킹 모바일번호 셋팅
				mainViewResp.getCntrInfo().getUserInfo().setMaskingMobileNo(YappUtil.blindMidEndMobileNo(mainViewResp.getCntrInfo().getMobileNo()));

				//220620 계약정보 암호화(모바일번호/생년월일)
				cntrInfo = cmnService.aesEncCntrInfoField(cntrInfo, req);
				logger.info("main kt회선 mainViewResp : "+mainViewResp);
				
			}
			
			//20210712 2주 미사용시 로그아웃 처리개선 시작
			String encSessionKey = reBuildYsid(req);
			mainViewResp.getCntrInfo().getUserInfo().setYsid(encSessionKey);
			
			//mainViewResp.setYsid(encSessionKey);
			//20210712 2주 미사용시 로그아웃 처리개선 끝
		}
		return resultInfo;
	}

	@RequestMapping(value = "/main/divide", method = RequestMethod.GET)
	@ApiOperation(value="데이터 나눔 화면 정보 조회")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<DataDivResp> viewDataDiv(HttpServletRequest req) throws Exception
	{
		String loginMobileNo = null;
		if(SessionKeeper.getSdata(req) != null){
			loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
		}
		
		// 계약정보 조회
		ContractInfo cntrInfo = shubService.callFn139(loginMobileNo, false, true).getCntrInfo();

		DataDivResp dataDivResp = new DataDivResp();
		
		// 내 데이터 정보 조회
		WsgDataUseQnt dataUseQnt = wsgService.getMobileTotalUseWeb(cntrInfo.getCntrNo(), loginMobileNo, null, cntrInfo.getCallingPlan());
		dataDivResp.setMyDataInfo(YappCvtUtil.cvt(dataUseQnt, new WsgDataUseQntRmn()));
		
		// 선물 가능 정보를 조회한다.
		dataDivResp.setGiftPsbInfo(giftService.getGiftPsbInfo(cntrInfo));
		
		// 진행중인 데이턱 조회
		Datuk shareDdatukData = datukService.getShareDatukData(cntrInfo.getCntrNo());
		dataDivResp.setDatukInfo(shareDdatukData);
		
		return new ResultInfo<DataDivResp>(dataDivResp);
	}
	
	@RequestMapping(value = "/main/mydata", method = RequestMethod.GET)
	@ApiOperation(value="내 데이터 잔여량 정보 조회")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<MyRmnData> viewMyRmnDataInfo(HttpServletRequest req) throws Exception
	{
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		String loginMobileNo = null;
		if(SessionKeeper.getSdata(req) != null){
			loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
		}
		ContractInfo cntrInfo = shubService.callFn139(loginMobileNo).getCntrInfo();
		CallingPlan cplan = cntrInfo.getCallingPlan();
		
		// LTE, 5G 요금제만 이용가능
		if ( YappUtil.isEq(cplan.getPpCatL(), "G0001") == false && YappUtil.isEq(cplan.getPpCatL(), "G0002") == false && YappUtil.isEq(cplan.getPpCatL(), "G0005") == false){
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_ONLY_LTE"));		// LTE 요금제만 이용 가능합니다.
		}

		// 내 데이터 정보 조회
		List<DataInfo> dataInfoList = kosService.retrieveDrctlyUseQntDtl3(loginCntrNo, loginMobileNo);
		
		int myDataSize = 0;
		int myRmnDataAmt = 0;
		int dsprCurMnthDataAmt = 0;
		int dsprNextMnthDataAmt = 0;

		logger.info("=======================================");
		logger.info("size : "+dataInfoList.size());
		
		List<DsprData> dsprDataList = new ArrayList<>();
		if ( YappUtil.isNotEmpty(dataInfoList) )
		{
			for ( int i = 0; i < dataInfoList.size(); i++ )
			{
				DataInfo dataInfo = dataInfoList.get(i);
				logger.info("getDataAmt : "+dataInfo.getDataAmt());
				myDataSize += dataInfo.getDataAmt();
				myRmnDataAmt += dataInfo.getRmnDataAmt();
				
				// 기본 요금제는 당월, 익월 KOS에서 넘어온 값으로 세팅한다.
				if ( i == 0 )
				{
					dsprCurMnthDataAmt += dataInfo.getDsprCurMnthDataAmt();
					dsprNextMnthDataAmt += dataInfo.getDsprNextMnthDataAmt();
					continue;
				}

				// 특정일자 소멸 확인
				VasItem vasItem = new VasItem();
				vasItem.setVasCd("CUPNDATA");
				vasItem.setVasItemCd(dataInfo.getDataTp());

				List<VasItem> vasItemList = vasService.getVasItemList(vasItem);

				if ( YappUtil.isNotEmpty(vasItemList) )
				{
					DsprData dsprData = new DsprData();
					dsprData.setDsprDataAmt(dataInfo.getRmnDataAmt());
//					dsprData.setDsprYmd("20171125");
					dsprDataList.add(dsprData);
					continue;
				}

				// 충전, 당겨쓰기는 KOS에서 넘어온 값으로 그외는 당월소멸로 한다.
				vasItem.setVasCd(null);
				vasItem.setVasItemCd(dataInfo.getDataTp());
				VasItem vasItemInfo = vasService.getVasItemInfo(vasItem);
				
				if ( vasItemInfo != null && YappUtil.contains(vasItemInfo.getVasCd(), "PLLDAT", "LTEDTC", "LTEDTP", "LTE장기혜택ㅋ코드 추가필요") )
				{
					dsprCurMnthDataAmt += dataInfo.getDsprCurMnthDataAmt();
					dsprNextMnthDataAmt += dataInfo.getDsprNextMnthDataAmt();
				}
				else
					dsprCurMnthDataAmt += dataInfo.getRmnDataAmt();
			}
		}
		
		// 내 데이터 잔여량 정보 조회
		MyRmnData rmnData = new MyRmnData();
		logger.info("myDataSize : "+myDataSize);
		logger.info("myRmnDataAmt : "+myRmnDataAmt);
		logger.info("=======================================");
		rmnData.setMyDataSize(myDataSize);
		rmnData.setMyRmnDataAmt(myRmnDataAmt);
		rmnData.setDsprCurMnthDataAmt(dsprCurMnthDataAmt);
		rmnData.setDsprNextMnthDataAmt(dsprNextMnthDataAmt);
		
		rmnData.setDsprDataList(dsprDataList);
		
		return new ResultInfo<>(rmnData);
	}
	
	@RequestMapping(value = "/main/mydata/egg", method = RequestMethod.GET)
	@ApiOperation(value="내 데이터 잔여량 정보 조회(알요금제)")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<WsgDataUseQnt> viewMyRmnEggDataInfo(HttpServletRequest req) throws Exception
	{
		String loginMobileNo = null;
		if(SessionKeeper.getSdata(req) != null){
			loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
		}
		ContractInfo cntrInfo = shubService.callFn139(loginMobileNo).getCntrInfo();
		CallingPlan cplan = cntrInfo.getCallingPlan();
		
		// LTE, 5G 요금제만 이용가능
		if ( YappUtil.isEq(cplan.getPpCatL(), "G0001") == false && YappUtil.isEq(cplan.getPpCatL(), "G0002") == false && YappUtil.isEq(cplan.getPpCatL(), "G0005") == false){
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_ONLY_LTE"));		// LTE 요금제만 이용 가능합니다.
		}
		
		// 내 데이터 잔여량 정보 조회
		WsgDataUseQnt dataUseQnt = wsgService.getMobileTotalUseWeb(cntrInfo.getCntrNo(), loginMobileNo, null, cplan);
		
		return new ResultInfo<>(dataUseQnt);
	}
	
	@RequestMapping(value = "/main/mydata/dtl", method = RequestMethod.GET)
	@ApiOperation(value="내 데이터 잔여량 상세 정보 조회")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<DataInfo> viewMyRmnDataDtlInfo(HttpServletRequest req) throws Exception
	{
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		String loginMobileNo = null;
		if(SessionKeeper.getSdata(req) != null){
			loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
		}
		ContractInfo cntrInfo = shubService.callFn139(loginMobileNo).getCntrInfo();
		CallingPlan cplan = cntrInfo.getCallingPlan();

		// LTE, 5G 요금제만 이용가능
		if ( YappUtil.isEq(cplan.getPpCatL(), "G0001") == false && YappUtil.isEq(cplan.getPpCatL(), "G0002") == false && YappUtil.isEq(cplan.getPpCatL(), "G0005") == false){
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_ONLY_LTE"));		// LTE 요금제만 이용 가능합니다.
		}
        // 개발 테스트 -S
		String profileInfo = environment.getActiveProfiles()[0];
		if("local".equals(profileInfo) || "dev".equals(profileInfo) ) {
			if("dev".equals(profileInfo)){
				switch(loginMobileNo) {
					case "01059181475"://ydbox14
						loginMobileNo = "01056190116";
						loginCntrNo = "601042192";
						break;
					case "01064280908"://dtffahah
						loginMobileNo = "01029952091";
						loginCntrNo = "604111008";
						break;
					case "01066598046"://ydbox16
						loginMobileNo = "01063768349";
						loginCntrNo = "505621570";
						break;
					default:
				}
			} else {
				loginMobileNo = req.getParameter("loginMobileNo");
				loginCntrNo = req.getParameter("loginCntrNo");
			}
		}
		// -E

		// 내 데이터 잔여량 정보 조회
		logger.info("=================================================/main/mydata/dtl=================================================");
		List<DataInfo> dataInfoList = kosService.retrieveDrctlyUseQntDtl3(loginCntrNo, loginMobileNo);
		List<DataInfo> dataInfoListTmp = new ArrayList<>();
		List<DataInfo> dataInfoListTmp2 = new ArrayList<>();
		
		if ( YappUtil.isNotEmpty(dataInfoList) ){
			for ( DataInfo dataInfo : dataInfoList ) {
				DataInfo dataInfoTmp = new DataInfo();	
				int tmpCnt = 0;
				int dataAmtSum = 0;
				int rmnDataAmtSum = 0;
				String dataNmSum = "";
				for(int idx = 0 ; idx < dataInfoListTmp.size() ; idx++) {
					DataInfo dataInfoTmp2 = dataInfoListTmp.get(idx);
					
					if (dataInfo.getDataNm().equals(dataInfoTmp2.getDataNm())) {
						tmpCnt = idx;
						dataNmSum = dataInfo.getDataNm();
						dataAmtSum = dataInfo.getDataAmt() + dataInfoTmp2.getDataAmt();
						rmnDataAmtSum = dataInfo.getRmnDataAmt() + dataInfoTmp2.getRmnDataAmt();
					}
				}

				if (tmpCnt == 0) {
					dataInfoTmp.setDataNm(dataInfo.getDataNm());
					dataInfoTmp.setDataAmt(dataInfo.getDataAmt());
					dataInfoTmp.setRmnDataAmt(dataInfo.getRmnDataAmt()); 
					dataInfoListTmp.add(dataInfoTmp);
				} else {
					dataInfoTmp.setDataNm(dataNmSum);
					dataInfoTmp.setDataAmt(dataAmtSum);
					dataInfoTmp.setRmnDataAmt(rmnDataAmtSum);
					dataInfoListTmp.set(tmpCnt, dataInfoTmp);
				}
			}
		}

		//5G 완전무제한일 경우 요금제(기본제공), 공유데이터만 보여준다.
		//if (YappUtil.isEq(cplan.getPpCatL(), "G0005") == true && YappUtil.isEq(cplan.getFginfYn(), "Y") == true){
		if (YappUtil.isEq(cplan.getFginfYn(), "Y") == true){

			String curDate = YappUtil.getCurDate("yyyyMMdd");
			DataShareList dataInfoListShare = kosService.dataSharPrvQntRetv(loginCntrNo, curDate);

			//20220217
			List<String> ydumDataTp = new ArrayList<String>();	//ydum데이터타입
			boolean ydumYn = false;		//ydum 여부
			//20220217
			
			//20220221
			int shareDataAmtSum = 0;
			int shareRmnDataAmtSum = 0;
			//20220221
			
			if ( YappUtil.isNotEmpty(dataInfoListShare) ){
				DataInfo dataInfoTmp = new DataInfo();	
				dataInfoTmp.setDataNm(dataInfoListShare.getDataNm());
				dataInfoTmp.setDataAmt(dataInfoListShare.getDataAmt());
				dataInfoTmp.setRmnDataAmt(dataInfoListShare.getRmnDataAmt());
				dataInfoListTmp.add(dataInfoTmp);
				
				//20220222
				List<GrpCode> YdumGrpList = cmsService.getGrpCodeList("YDUM_VAS_CODE");

				if(YappUtil.isNotEmpty(YdumGrpList)){
					
					//20220216 Y덤 공유 데이터 추가
					DataInfo dataInfoTmp2 = new DataInfo();	
					List<DataInfo> dataInfoList2 = dataInfoListShare.getDataShareList();
					
					if(YappUtil.isNotEmpty(dataInfoList2)){
						
						for ( DataInfo dataInfo : dataInfoList2 ) {
							boolean ydumTpYn = false;
							
							for(GrpCode grp : YdumGrpList){
								if(grp.getCodeNm().contains(dataInfo.getDataTp())){
									ydumTpYn = true;
									break;
								}
							}
							
							//20220217 ydum 부가서비스 코드 조회 ydum 값 add
							if(ydumTpYn){
								ydumYn = true;
								
								ydumDataTp.add(dataInfo.getDataTp());
	
								dataInfoTmp2.setDataTp(dataInfo.getDataTp());
								dataInfoTmp2.setDataNm(dataInfo.getDataNm());
								dataInfoTmp2.setDataAmt(dataInfo.getDataAmt());
								dataInfoTmp2.setRmnDataAmt(dataInfo.getRmnDataAmt());
	
								dataInfoListTmp.add(dataInfoTmp2);
							}else{
								shareDataAmtSum += dataInfo.getDataAmt();
								shareRmnDataAmtSum += dataInfo.getRmnDataAmt();
							}
								
						}
					}
				}

				//20220216

			}

			for ( int i = dataInfoListTmp.size() - 1; i >= 0; i-- )
			{
				DataInfo dataInfo = dataInfoListTmp.get(i);

				if ( YappUtil.isEq(dataInfo.getDataNm(), "공유데이터") == false && YappUtil.isEq(dataInfo.getDataNm(), "요금제(기본제공)") == false) {
//					dataInfoListTmp.remove(i);
					
					//20220217 ydum 부가서비스 포함될때
					if(ydumYn){
						boolean ydTpYn = false;
						
						for(String ydTp : ydumDataTp){
							if(YappUtil.isEq(dataInfo.getDataTp(), ydTp) == true){ 
								ydTpYn = true;
								break;
								
							}else{
								ydTpYn = false;
							}
						}
						
						if(!ydTpYn){// ydum아닌 리스트 제거
							dataInfoListTmp.remove(i);
						}
						
					}else{
						dataInfoListTmp.remove(i);
					}
					//20220217
					
				//20220217 공유데이터일때  Y덤을 제외한 공유데이터리스트들의 합 셋팅
				}else if(YappUtil.isEq(dataInfo.getDataNm(), "공유데이터") == true){
					//y덤일때만 공유리스트에서의 합산값을 공유데이터 값에 셋팅
					if(ydumYn){
						dataInfoListTmp.get(i).setDataAmt(shareDataAmtSum);
						dataInfoListTmp.get(i).setRmnDataAmt(shareRmnDataAmtSum);
					}
				}
				//20220217
			}

			dataInfoListTmp2 = dataInfoListTmp;
		}else{
			if(dataInfoListTmp.size() > 4){
				DataInfo dataInfoTmp = new DataInfo();
				int inCnt1 = 0;
				int dataAmtSum1 = 0;
				int rmnDataAmtSum1 = 0;
				String dataNmSum1 = "";
				
				int inCnt2 = 0;
				int dataAmtSum2 = 0;
				int rmnDataAmtSum2 = 0;
				String dataNmSum2 = "";
				
				int inCnt3 = 0;
				int dataAmtSum3 = 0;
				int rmnDataAmtSum3 = 0;
				String dataNmSum3 = "";
				
				int inCnt4 = 0;
				int dataAmtSum4 = 0;
				int rmnDataAmtSum4 = 0;
				String dataNmSum4 = "";
	
				for ( DataInfo dataInfo : dataInfoListTmp ) {
					if(dataInfo.getDataNm().indexOf("룰렛") > -1 || dataInfo.getDataNm().indexOf("충전") > -1 || dataInfo.getDataNm().indexOf("팝콘") > -1 || dataInfo.getDataNm().indexOf("데이터플러스") > -1){
						dataNmSum1 = "충전 데이터";
						dataAmtSum1 = dataAmtSum1 + dataInfo.getDataAmt();
						rmnDataAmtSum1 = rmnDataAmtSum1 + dataInfo.getRmnDataAmt();
						inCnt1 = 1;
					}else if(dataInfo.getDataNm().indexOf("데이터박스") > -1 || dataInfo.getDataNm().indexOf("패밀리박스") > -1){
						dataNmSum2 = "꺼낸 데이터";
						dataAmtSum2 = dataAmtSum2 + dataInfo.getDataAmt();
						rmnDataAmtSum2 = rmnDataAmtSum2 + dataInfo.getRmnDataAmt();
						inCnt2 = 1;
					}else if(dataInfo.getDataNm().indexOf("쿠폰") > -1){
						dataNmSum4 = "데이터쿠폰";
						dataAmtSum4 = dataAmtSum4 + dataInfo.getDataAmt();
						rmnDataAmtSum4 = rmnDataAmtSum4 + dataInfo.getRmnDataAmt();
						inCnt4 = 1;
					}else{
						dataNmSum3 = "기본제공 데이터";
						dataAmtSum3 = dataAmtSum3 + dataInfo.getDataAmt();
						rmnDataAmtSum3 = rmnDataAmtSum3 + dataInfo.getRmnDataAmt();
						inCnt3 = 1;
					}
				}
	
				if(inCnt1 == 1){
					dataInfoTmp = new DataInfo();
					dataInfoTmp.setDataNm(dataNmSum1);
					dataInfoTmp.setDataAmt(dataAmtSum1);
					dataInfoTmp.setRmnDataAmt(rmnDataAmtSum1);
					dataInfoListTmp2.add(dataInfoTmp);
				}
	
				if(inCnt2 == 1){
					dataInfoTmp = new DataInfo();
					dataInfoTmp.setDataNm(dataNmSum2);
					dataInfoTmp.setDataAmt(dataAmtSum2);
					dataInfoTmp.setRmnDataAmt(rmnDataAmtSum2);
					dataInfoListTmp2.add(dataInfoTmp);
				}
	
				if(inCnt3 == 1){
					dataInfoTmp = new DataInfo();
					dataInfoTmp.setDataNm(dataNmSum3);
					dataInfoTmp.setDataAmt(dataAmtSum3);
					dataInfoTmp.setRmnDataAmt(rmnDataAmtSum3);
					dataInfoListTmp2.add(dataInfoTmp);
				}
	
				if(inCnt4 == 1){
					dataInfoTmp = new DataInfo();
					dataInfoTmp.setDataNm(dataNmSum4);
					dataInfoTmp.setDataAmt(dataAmtSum4);
					dataInfoTmp.setRmnDataAmt(rmnDataAmtSum4);
					dataInfoListTmp2.add(dataInfoTmp);
				}
			}else{
				if(dataInfoListTmp.size() > 0){
					// 5G무제한 요금제가 아니고 + KOS 호출 시 부가서비스 중에 "Y덤_통합데이터" 문구가 포함 되어있으면
					// 요금제(기본제공)에 해당 Y덤_통합데이타 합산하여 처리함. -S
					final String ydTotalData = "Y덤_통합데이터";
					final String mergeTargetData = "요금제(기본제공)";
					List<DataInfo> dataInfoListTmp9 = new ArrayList<>();
					Optional<DataInfo> optTotData = dataInfoListTmp.stream().filter(d -> d.getDataNm().contains(ydTotalData)).findAny();
					if(optTotData.isPresent()){
						DataInfo dataInfo = optTotData.get();
						dataInfoListTmp.forEach(c -> {
							if(c.getDataNm().contains(mergeTargetData)){
								final int basicRmnDumData = c.getRmnDataAmt() + dataInfo.getRmnDataAmt();
								final int basicAmtDumData = c.getDataAmt() + dataInfo.getDataAmt();
								c.setDataAmt(basicAmtDumData);
								c.setRmnDataAmt(basicRmnDumData);
							}
							if(!c.getDataNm().contains(ydTotalData)) {
								dataInfoListTmp9.add(c);
							}
						});
						dataInfoListTmp2 = dataInfoListTmp9;
					} else {
						dataInfoListTmp2 = dataInfoListTmp;
					}
					// -E
				}else{
					DataInfo dataInfoTmp = new DataInfo();
					dataInfoTmp.setDataNm("");
					dataInfoTmp.setDataAmt(0);
					dataInfoTmp.setRmnDataAmt(0);
					dataInfoListTmp.add(dataInfoTmp);
					dataInfoListTmp2 = dataInfoListTmp;
				}
				
			}
		}

		return new ResultInfo<DataInfo>(dataInfoListTmp2);
	}
	
	@RequestMapping(value = "/main/mydata/fivedtl", method = RequestMethod.GET)
	@ApiOperation(value="내 데이터 잔여량 상세 정보 조회")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<DataInfo> viewMyRmnDataFiveDtlInfo(HttpServletRequest req) throws Exception
	{
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		String loginMobileNo = null;
		if(SessionKeeper.getSdata(req) != null){
			loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
		}
		ContractInfo cntrInfo = shubService.callFn139(loginMobileNo).getCntrInfo();
		CallingPlan cplan = cntrInfo.getCallingPlan();

		// LTE, 5G 요금제만 이용가능
		if ( YappUtil.isEq(cplan.getPpCatL(), "G0001") == false && YappUtil.isEq(cplan.getPpCatL(), "G0002") == false && YappUtil.isEq(cplan.getPpCatL(), "G0005") == false){
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_ONLY_LTE"));		// LTE 요금제만 이용 가능합니다.
		}
	
		List<DataInfo> dataInfoListTmp = new ArrayList<>();
		List<DataInfo> dataInfoListTmp2 = new ArrayList<>();
		
		//5G 요금제만 조회
		if (YappUtil.isEq(cplan.getFginfYn(), "Y") == true){
			String curDate = YappUtil.getCurDate("yyyyMMdd");
			DataShareList dataInfoListShare = kosService.dataSharPrvQntRetv(loginCntrNo, curDate);
		

			// 내 데이터 잔여량 정보 조회
			List<DataInfo> dataInfoList = dataInfoListShare.getDataShareList();
			
			if ( YappUtil.isNotEmpty(dataInfoList) ){
				for ( DataInfo dataInfo : dataInfoList ) {
					DataInfo dataInfoTmp = new DataInfo();	
					int tmpCnt = 0;
					int dataAmtSum = 0;
					int rmnDataAmtSum = 0;
					String dataNmSum = "";
					for(int idx = 0 ; idx < dataInfoListTmp.size() ; idx++) {
						DataInfo dataInfoTmp2 = dataInfoListTmp.get(idx);
						
						if (dataInfo.getDataNm().equals(dataInfoTmp2.getDataNm())) {
							tmpCnt = idx;
							dataNmSum = dataInfo.getDataNm();
							dataAmtSum = dataInfo.getDataAmt() + dataInfoTmp2.getDataAmt();
							rmnDataAmtSum = dataInfo.getRmnDataAmt() + dataInfoTmp2.getRmnDataAmt();
						}
					}
	
					if (tmpCnt == 0) {
						dataInfoTmp.setDataNm(dataInfo.getDataNm());
						dataInfoTmp.setDataAmt(dataInfo.getDataAmt());
						dataInfoTmp.setRmnDataAmt(dataInfo.getRmnDataAmt());
						dataInfoListTmp.add(dataInfoTmp);
					} else {
						dataInfoTmp.setDataNm(dataNmSum);
						dataInfoTmp.setDataAmt(dataAmtSum);
						dataInfoTmp.setRmnDataAmt(rmnDataAmtSum);
						dataInfoListTmp.set(tmpCnt, dataInfoTmp);
					}
				}
			}

			if(dataInfoListTmp.size() > 4){
				DataInfo dataInfoTmp = new DataInfo();
				int inCnt1 = 0;
				int dataAmtSum1 = 0;
				int rmnDataAmtSum1 = 0;
				String dataNmSum1 = "";
				
				int inCnt2 = 0;
				int dataAmtSum2 = 0;
				int rmnDataAmtSum2 = 0;
				String dataNmSum2 = "";
				
				int inCnt3 = 0;
				int dataAmtSum3 = 0;
				int rmnDataAmtSum3 = 0;
				String dataNmSum3 = "";
				
				int inCnt4 = 0;
				int dataAmtSum4 = 0;
				int rmnDataAmtSum4 = 0;
				String dataNmSum4 = "";
	
				for ( DataInfo dataInfo : dataInfoListTmp ) {
					if(dataInfo.getDataNm().indexOf("룰렛") > -1 || dataInfo.getDataNm().indexOf("충전") > -1 || dataInfo.getDataNm().indexOf("팝콘") > -1 || dataInfo.getDataNm().indexOf("데이터플러스") > -1){
						dataNmSum1 = "5G 충전 데이터";
						dataAmtSum1 = dataAmtSum1 + dataInfo.getDataAmt();
						rmnDataAmtSum1 = rmnDataAmtSum1 + dataInfo.getRmnDataAmt();
						inCnt1 = 1;
					}else if(dataInfo.getDataNm().indexOf("데이터박스") > -1 || dataInfo.getDataNm().indexOf("패밀리박스") > -1){
						dataNmSum2 = "꺼낸 데이터";
						dataAmtSum2 = dataAmtSum2 + dataInfo.getDataAmt();
						rmnDataAmtSum2 = rmnDataAmtSum2 + dataInfo.getRmnDataAmt();
						inCnt2 = 1;
					}else if(dataInfo.getDataNm().indexOf("쿠폰") > -1){
						dataNmSum4 = "데이터쿠폰";
						dataAmtSum4 = dataAmtSum4 + dataInfo.getDataAmt();
						rmnDataAmtSum4 = rmnDataAmtSum4 + dataInfo.getRmnDataAmt();
						inCnt4 = 1;
					}else{
						dataNmSum3 = "공유 데이터";
						dataAmtSum3 = dataAmtSum3 + dataInfo.getDataAmt();
						rmnDataAmtSum3 = rmnDataAmtSum3 + dataInfo.getRmnDataAmt();
						inCnt3 = 1;
					}
				}
	
				if(inCnt1 == 1){
					dataInfoTmp = new DataInfo();
					dataInfoTmp.setDataNm(dataNmSum1);
					dataInfoTmp.setDataAmt(dataAmtSum1);
					dataInfoTmp.setRmnDataAmt(rmnDataAmtSum1);
					dataInfoListTmp2.add(dataInfoTmp);
				}
	
				if(inCnt2 == 1){
					dataInfoTmp = new DataInfo();
					dataInfoTmp.setDataNm(dataNmSum2);
					dataInfoTmp.setDataAmt(dataAmtSum2);
					dataInfoTmp.setRmnDataAmt(rmnDataAmtSum2);
					dataInfoListTmp2.add(dataInfoTmp);
				}
	
				if(inCnt3 == 1){
					dataInfoTmp = new DataInfo();
					dataInfoTmp.setDataNm(dataNmSum3);
					dataInfoTmp.setDataAmt(dataAmtSum3);
					dataInfoTmp.setRmnDataAmt(rmnDataAmtSum3);
					dataInfoListTmp2.add(dataInfoTmp);
				}
	
				if(inCnt4 == 1){
					dataInfoTmp = new DataInfo();
					dataInfoTmp.setDataNm(dataNmSum4);
					dataInfoTmp.setDataAmt(dataAmtSum4);
					dataInfoTmp.setRmnDataAmt(rmnDataAmtSum4);
					dataInfoListTmp2.add(dataInfoTmp);
				}
			}else{
				if(dataInfoListTmp.size() > 0){
					dataInfoListTmp2 = dataInfoListTmp;
				}else{
					DataInfo dataInfoTmp = new DataInfo();
					dataInfoTmp.setDataNm("");
					dataInfoTmp.setDataAmt(0);
					dataInfoTmp.setRmnDataAmt(0);
					dataInfoListTmp.add(dataInfoTmp);
					dataInfoListTmp2 = dataInfoListTmp;
				}
				
			}
		}

		return new ResultInfo<DataInfo>(dataInfoListTmp2);
	}
	
	@RequestMapping(value = "/main/mydata/mnth", method = RequestMethod.GET)
	@ApiOperation(value="내 데이터 월별 상세 정보 조회")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<MnthUsage> viewUseDataByMnth(HttpServletRequest req) throws Exception
	{
		String loginMobileNo = null;
		if(SessionKeeper.getSdata(req) != null){
			loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
		}
		ContractInfo cntrInfo = shubService.callFn139(loginMobileNo).getCntrInfo();
		CallingPlan cplan = cntrInfo.getCallingPlan();
		
		// LTE, 5G 요금제만 이용가능
		if ( YappUtil.isEq(cplan.getPpCatL(), "G0001") == false && YappUtil.isEq(cplan.getPpCatL(), "G0002") == false && YappUtil.isEq(cplan.getPpCatL(), "G0005") == false){
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_ONLY_LTE"));		// LTE 요금제만 이용 가능합니다.
		}
		
		List<MnthUsage> dataInfoList = new ArrayList<>();
		int dataCnt = 0;
		int dataAmt = 0;

		// 지난 5개월 사용량 조회
		//for ( int i = 0; i < 5; i++ )
		for ( int i = 0; i < 6; i++ )
		{
			int mnCnt = -i;
			//String tarMnth = YappUtil.getCurDate("yyyyMM", Calendar.MONTH, (-i -1));
			String tarMnth = "";
			if(i == 0){
				tarMnth = null;
			}else{
				tarMnth = YappUtil.getCurDate("yyyyMM", Calendar.MONTH, (mnCnt));
			}

			WsgDataUseQnt dataUseQnt = wsgService.getMobileTotalUseWeb(cntrInfo.getCntrNo(), loginMobileNo, tarMnth, cplan);
			MnthUsage usage = new MnthUsage();
			usage.setUseYm(tarMnth);
			if ( dataUseQnt.getUseDataAmt() > 0 )
			{
				usage.setUseDataAmt(dataUseQnt.getUseDataAmt());
				dataAmt += dataUseQnt.getUseDataAmt();
				dataCnt++;
			}
			dataInfoList.add(usage);
		}
		
		return new ResultInfo<MnthUsage>(dataInfoList);
	}
	
	private String loginCheck(HttpServletRequest req) throws Exception{
		String loginYn = "Y";
		
		// 헤더의 세션ID
		String sessionId = req.getHeader(SessionKeeper.KEY_SESSION_ID);
		String autoLoginYn = req.getHeader("autoLogin");
		
		String reqSessionId = (String) SessionKeeper.get(req, SessionKeeper.KEY_SESSION_ID);
		if(logger.isInfoEnabled()) {
			logger.info("sessionId      : " + sessionId);
			logger.info("autoLoginYn    : " + autoLoginYn);
			logger.info("server session : " + reqSessionId);
		}

		// 단말기에서 ysid가 안넘어오면 에러처리
		if(sessionId == null || sessionId.equals("")){
			return loginYn = "N";
		}
		
		// ysid 복호화처리
		boolean sessExistUser = false;
		String decSessionKey = "";
		try{
			decSessionKey = keyFixUtil.decode(sessionId);
		}catch(RuntimeException e){
			return loginYn = "N";
		}catch(Exception e){
			return loginYn = "N";
		}
		
		
		String[] tmp = decSessionKey.split("\\|\\|");
		String[] tmp2 = decSessionKey.split("\\|\\|");
		
		/** 2020.02.20 : PLAN-357 : 미사용 기간 2주 초과 시, 로그아웃 처리 로직 추가 */
		if(tmp == null ||tmp.length < 6 || tmp.length > 7) {
			return loginYn = "N";
			
		}else if(tmp.length == 6){
			
			tmp = new String[7];
			tmp[0] = tmp2[0];
			tmp[1] = tmp2[1];
			tmp[2] = tmp2[2];
			tmp[3] = tmp2[3];
			tmp[4] = tmp2[4];
			tmp[5] = tmp2[5];
			tmp[6] = "G0002";
		}
		
		
		JoinInfo joinInfo = new JoinInfo();
		
		if(YappUtil.isEmpty(tmp[0])||tmp[0].indexOf("ktid")!=-1){
			joinInfo = userService.getDupIdInfoKt(tmp[2]);
		}else{
			joinInfo = userService.getDupIdInfo(tmp[0]);
		}

		if(joinInfo != null){
			sessExistUser = true;
		}

		if(tmp[3] != null){
			if(tmp[3].equals("true")){
				sessExistUser = true;
			}
		}
		
		if(logger.isInfoEnabled()) {
			logger.info("[0] = " + tmp[0]);
			logger.info("[1] = " + tmp[1]);
			logger.info("[2] = " + tmp[2]);
			logger.info("[3] = " + tmp[3]);
			logger.info("[4] = " + tmp[4]);
			logger.info("[5] = " + tmp[5]);
			logger.info("[6] = " + tmp[6]);
			logger.info("sessExistUser = " + sessExistUser);
		}
		
		String autoLoginPassYn = "N";
		if(YappUtil.isEmpty(reqSessionId)){
			autoLoginPassYn = "Y";
		}else{
			if(!reqSessionId.equals(sessionId)){
				autoLoginPassYn = "Y";
			}
		}
		
		logger.info("======================================================");
		logger.info("checkLogin -> autoLoginPassYn : "+autoLoginPassYn);
		logger.info("checkLogin -> autoLoginYn : "+autoLoginYn);
		logger.info("======================================================");

		if(autoLoginPassYn.equals("Y")){
			if(autoLoginYn != null){
				//서버 세션에 정보가 없고 자동로그인(로그인유지)인 경우 단말에서 넘오는 정보를 서버 세션에 저장. 
				if(autoLoginYn.equals("true")){
					SessionKeeper.setSessionId(req,sessionId);
					if(SessionKeeper.getSdata(req) != null){
						SessionKeeper.getSdata(req).setUserId(tmp[2]);
						SessionKeeper.getSdata(req).setMobileNo(tmp[1]);
						SessionKeeper.getSdata(req).setExistUser(sessExistUser);
						SessionKeeper.getSdata(req).setMemStatus(tmp[6]);
					}
				}
			}
		}
		
		String mobileNo = null;
		String memStatus = null;
		
		if(SessionKeeper.getSdata(req) != null) {
			
			logger.info("SessionKeeper.getSdata(req) != null -> getMobileNo : "+SessionKeeper.getSdata(req).getMobileNo());
			logger.info("SessionKeeper.getSdata(req) != null -> getMemStatus : "+SessionKeeper.getSdata(req).getMemStatus());
			
			mobileNo = SessionKeeper.getSdata(req) == null ? null : SessionKeeper.getSdata(req).getMobileNo();
			memStatus = SessionKeeper.getSdata(req) == null ? null : SessionKeeper.getSdata(req).getMemStatus();
		}
		
		// 계약정보, 사용자 정보가 없으면 실행을 종료한다.  
		if ( YappUtil.isEmpty(mobileNo)
				|| YappUtil.isEmpty(memStatus)
				|| SessionKeeper.getSdata(req) == null 
				|| SessionKeeper.getSdata(req).isExistUser() == false ) 
		{
			return loginYn = "N";
		}
		
		String apiUrl = YappUtil.nToStr(req.getRequestURI());
		// 로그인 중복 체크로직.
		String passYn = "Y";
		if ( apiUrl.indexOf("/user/svcout") == -1 && apiUrl.indexOf("/user/logout") == -1 && apiUrl.indexOf("/user/join") == -1){
			passYn = "N";
		} 

		if ( apiUrl.indexOf("/user/joininfo") > -1){
			passYn = "N";
		}

		if(passYn.equals("N")){
			if(joinInfo != null){
				if(!tmp[4].equals(joinInfo.getDupId())){
					logger.info("session dup id = " + tmp[4]);
					logger.info("DB dup id = " + joinInfo.getDupId());
					logger.info("중복 로그인 불가. 다른 기기에서 로그인 되었습니다.");
					throw new YappAuthException("410","중복 로그인 불가. 다른 기기에서 로그인 되었습니다.");
				}
			}else{
				logger.info("DUP ID 정보가 없습니다.(해당 계약번호로 TB_USER에 정보가 없음.)");
				throw new YappAuthException("410","로그인 정보가 없습니다.");
			}
		}
		
		// 계약정보 재로딩
		SessionContractInfo cntrInfo = (SessionContractInfo) SessionKeeper.get(req, SessionKeeper.KEY_CNTR_INFO);
		if ( cntrInfo == null && mobileNo.indexOf("ktid")==-1) {
			SoapResponse139 resp = shubService.callFn139(mobileNo, false);
			SessionKeeper.addToReq(req, SessionKeeper.KEY_CNTR_INFO, YappCvtUtil.cvt(resp.getCntrInfo(), new SessionContractInfo()));
			loginYn = "Y";
		}
		
		return loginYn;
	}
	
	/**
	 * 20210712 2주 미사용시 로그아웃 처리개선
	 * ysid 값 조립 후 리턴
	 * @return ysid
	 */
	private String joinYsid(String cntrNo, String mobileNo, String userId, boolean existUser, String dupId, String memStatus) {
		logger.debug("====================== ysid create date ========================");
		logger.debug("YappUtil.getCurDate() : "+YappUtil.getCurDate());
		logger.debug("====================== ysid create date ========================");
		
		return new StringBuilder().append(cntrNo).append(SP)
								  .append(mobileNo).append(SP)
								  .append(userId).append(SP)
								  .append(existUser).append(SP)
								  .append(dupId).append(SP)
								  .append(YappUtil.getCurDate()).append(SP)
								  .append(memStatus).toString();
	}
	
	/**
	 * 20210712 2주 미사용시 로그아웃 처리개선
	 *  세션ID 갱신
	 * @param req
	 * @return enc(ysid)
	 * @throws Exception
	 */
	private String reBuildYsid(HttpServletRequest req) throws Exception{
		//20210712 2주 미사용시 로그아웃 처리개선 시작
		// 헤더의 세션ID
		String sessionId = req.getHeader(SessionKeeper.KEY_SESSION_ID);
		String memStatus ="";
		
		if(SessionKeeper.getSdata(req) != null){
			memStatus = SessionKeeper.getSdata(req).getMemStatus();
		}
		
		logger.info("==============================================================");
		logger.info("/reBuildYsid -> sessionId : "+sessionId);
		logger.info("/reBuildYsid -> memStatus : "+memStatus);
		logger.info("==============================================================");
		
		// ysid 복호화처리
		boolean sessExistUser = false;
		String decSessionKey = "";
		try{
			decSessionKey = keyFixUtil.decode(sessionId);
		}catch(RuntimeException e){
			logger.info("단말기 정보가 제대로 넘어오지 않았습니다.[ YSID 복호화 오류 ] " + sessionId);
			throw new YappAuthException("410","로그인 정보가 없습니다.");
		}catch(Exception e){
			logger.info("단말기 정보가 제대로 넘어오지 않았습니다.[ YSID 복호화 오류 ] " + sessionId);
			throw new YappAuthException("410","로그인 정보가 없습니다.");
		}
		
		String[] tmp = decSessionKey.split("\\|\\|");
		String[] tmp2 = decSessionKey.split("\\|\\|");
		
		/** 2020.02.20 : PLAN-357 : 미사용 기간 2주 초과 시, 로그아웃 처리 로직 추가 */
		if(tmp == null ||tmp.length < 6 || tmp.length > 7) {
			logger.info("단말기 정보가 제대로 넘어오지 않았습니다.[ YSID length ERROR ]");
			throw new YappAuthException("410","로그인 정보가 없습니다.");
			
		}else if(tmp.length == 6){
			
			tmp = new String[7];
			tmp[0] = tmp2[0];
			tmp[1] = tmp2[1];
			tmp[2] = tmp2[2].trim();
			tmp[3] = tmp2[3];
			tmp[4] = tmp2[4];
			tmp[5] = tmp2[5];
			tmp[6] = "G0002";
		}
		
		sessExistUser = Boolean.parseBoolean(tmp[3]);

		if(logger.isInfoEnabled()) {
			logger.info("[0] = " + tmp[0]);
			logger.info("[1] = " + tmp[1]);
			logger.info("[2] = " + tmp[2]);
			logger.info("[3] = " + tmp[3]);
			logger.info("[4] = " + tmp[4]);
			logger.info("[5] = " + tmp[5]);
			logger.info("[6] = " + tmp[6]);
			logger.info("sessExistUser = " + sessExistUser);
		}
		
		String tmpSessionKey = joinYsid(tmp[0], tmp[1], tmp[2], sessExistUser, tmp[4], memStatus);
		String encSessionKey = "";
		
		try {
			encSessionKey = keyFixUtil.encode(tmpSessionKey);
			
		} catch (RuntimeException e) {
			// TODO: handle exception
			throw new BadPaddingException();
		} catch (Exception e) {
			// TODO: handle exception
			throw new BadPaddingException();
		}
		
		//20210712 2주 미사용시 로그아웃 처리개선 끝
		
		return encSessionKey;
	}
}
