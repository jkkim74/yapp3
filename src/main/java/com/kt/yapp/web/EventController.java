package com.kt.yapp.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kt.yapp.annotation.NotCheckLogin;
import com.kt.yapp.domain.AttendDayCheck;
import com.kt.yapp.domain.ContractInfo;
import com.kt.yapp.domain.EventAppl;
import com.kt.yapp.domain.EventContent;
import com.kt.yapp.domain.EventGift;
import com.kt.yapp.domain.EventGiftJoin;
import com.kt.yapp.domain.EventLike;
import com.kt.yapp.domain.EventMaster;
import com.kt.yapp.domain.EventReply;
import com.kt.yapp.domain.GrpCode;
import com.kt.yapp.domain.Invitation;
import com.kt.yapp.domain.JoinInfo;
import com.kt.yapp.domain.RoomInfo;
import com.kt.yapp.domain.SessionContractInfo;
import com.kt.yapp.domain.UserInfo;
import com.kt.yapp.domain.VasItem;
import com.kt.yapp.domain.VoteHistory;
import com.kt.yapp.domain.VoteItem;
import com.kt.yapp.domain.YcanvasItem;
import com.kt.yapp.domain.YfriendsMenu;
import com.kt.yapp.domain.req.EventGiftMultiReq;
import com.kt.yapp.domain.resp.EventIconCountResp;
import com.kt.yapp.domain.resp.ResultInfo;
import com.kt.yapp.exception.YappException;
import com.kt.yapp.exception.YappRuntimeException;
import com.kt.yapp.service.CmsService;
import com.kt.yapp.service.CommonService;
import com.kt.yapp.service.EventService;
import com.kt.yapp.service.HistService;
import com.kt.yapp.service.ShubService;
import com.kt.yapp.service.TicketService;
import com.kt.yapp.service.UserService;
import com.kt.yapp.service.VoteService;
import com.kt.yapp.service.YFriendsService;
import com.kt.yapp.service.YCanvasService;
import com.kt.yapp.soap.response.SoapResponse139;
import com.kt.yapp.soap.response.SoapResponse140;
import com.kt.yapp.util.KeyFixUtil;
import com.kt.yapp.util.SessionKeeper;
import com.kt.yapp.util.YappCvtUtil;
import com.kt.yapp.util.YappUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "이벤트 컨트롤러")
@Controller
public class EventController {
	private static final Logger logger = LoggerFactory.getLogger(EventController.class);

	@Autowired
	private EventService eventService;
	@Autowired
	private ShubService shubService;
	@Autowired
	private CommonService cmnService;
	@Autowired
	private CmsService cmsService;
	@Autowired
	private KeyFixUtil keyFixUtil;
	@Autowired
	private UserService userService;
	@Autowired
	private YFriendsService yFriendsService;
	@Autowired
	private HistService histService;
	@Autowired
	private VoteService voteService;
	@Autowired
	private TicketService ticketService;
	@Autowired
	private YCanvasService canvasService;
	
	@Autowired
	Environment environment;
	
	@ApiOperation(value="이벤트 상세 화면 호출")
	@RequestMapping(value = "/event/eventDetail", method = RequestMethod.GET)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header")
	,@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header")
	, @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header")
	, @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")
	})
	public ModelAndView eventDetail(int evtSeq, HttpServletRequest req) throws Exception {
		ModelAndView mv = new ModelAndView();
		String ysid = req.getHeader("ysid");
		String autoLogin = req.getHeader("autoLogin");
		String osTp = req.getHeader("osTp");
		String appVrsn = req.getHeader("appVrsn");
		String errorMsg = "";
		String loginCntrNo = "";
		String loginUserId = "";
		String loginMobileNo = null;
		String partyName = "";
		String apiUrl = YappUtil.nToStr(req.getRequestURI());
		String eventJoinYn = "N";
		String eventTicketHaveYn = "N";  //210820 응모권보유 이벤트여부
		String eventTicketJoinYn = "N"; //210820 응모권보유 이벤트 참여 여부
		EventMaster eventDetail = new EventMaster();
		List<EventContent> eventContentList =  new ArrayList<EventContent>();
		List<EventGift> eventGiftList =  new ArrayList<EventGift>();
		List<EventGift> eventAllGiftList =  new ArrayList<EventGift>();
		List<EventGift> eventAttendList =  new ArrayList<EventGift>();
		List<VoteItem> voteItemList =  new ArrayList<VoteItem>(); //210610 투표대상리스트
		List<YcanvasItem> canvasItemList =  new ArrayList<YcanvasItem>(); //클래스 리스트
		VoteHistory voteHis = new VoteHistory();
		
		String soldoutYn = "N";
		String nowAttendYn = "N";
		String voteYn = "N"; //210610 투표 상태
		String voteRateYn = "N";//230106 투표율 표시여부
		
		//로그인 여부
		String loginYn = loginCheck(req, apiUrl);
		String memStatus = "";
		String targetYn = "Y";
		String targetCode = "01";
		
		//유저 정보 
		UserInfo userInfo = null;
		UserInfo userKtInfo = null;
		
		//210608 이벤트 상세시 로그인한 사용자 로그찍기위해
		try {
			histService.insertApiAccessLog(req, osTp, appVrsn);

		} catch (RuntimeException e) {logger.warn(e.toString());}
		catch (Exception e) {logger.warn(e.toString());}
		
		try{
			if(logger.isInfoEnabled()) {
				logger.info("evtSeq           : "+evtSeq);
			}
			if(evtSeq == 0){
				errorMsg = cmnService.getMsg("ERR_Y_SYSTEM");
			}
			
			
			loginCntrNo = SessionKeeper.getCntrNo(req);
			
			if(SessionKeeper.getSdata(req) != null){
				memStatus = SessionKeeper.getSdata(req).getMemStatus();
				loginUserId = SessionKeeper.getSdata(req).getUserId();
				loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
				partyName = SessionKeeper.getSdata(req).getName();
			}
			
			logger.info("==============================================================");
			logger.info("eventDetail -> loginYn         : "+loginYn);	
			logger.info("eventDetail -> memStatus       : "+memStatus);
			logger.info("eventDetail -> partyName       : "+partyName);
			logger.info("eventDetail -> loginCntrNo     : "+loginCntrNo);
			logger.info("eventDetail -> loginUserId     : "+loginUserId);
			logger.info("eventDetail -> osTp            : "+osTp);
			logger.info("eventDetail -> appVrsn         : "+appVrsn);
			logger.info("==============================================================");
			
			//이벤트 상세 접속 로그 저장 (임시)
			Invitation paramObjAccess = new Invitation();
			
			if("Y".equals(loginYn)){
				paramObjAccess.setCntrNo(loginCntrNo);
				paramObjAccess.setUserId(loginUserId);
			}else{
				paramObjAccess.setCntrNo("UNKNOWN");
				paramObjAccess.setUserId("UNKNOWN");
			}
			
			paramObjAccess.setInvMobileNo(Integer.toString(evtSeq));
			userService.insertInvt(paramObjAccess);
			
			
			//로컬 테스트용(서버올릴땐 삭제)
			String profileInfo = environment.getActiveProfiles()[0];
			if("local".equals(profileInfo)){
				loginYn = "Y";
				memStatus = getDevUserInfo().get("memStatus");
				loginCntrNo = getDevUserInfo().get("loginCntrNo");
				loginUserId = getDevUserInfo().get("loginUserId");
			}
	
				
			// 이벤트 정보 조회
			eventDetail = eventService.getEventDetailMaster(evtSeq, osTp);
			if(eventDetail == null){
				errorMsg = cmnService.getMsg("ERR_Y_SYSTEM");
			}
			
			if(eventDetail != null){
				
				logger.info("==============================================================");
				logger.info("eventDetail -> evtTitle        : "+eventDetail.getEvtTitle());
				logger.info("eventDetail -> linkType        : "+eventDetail.getLinkType());
				logger.info("==============================================================");
				
				if(YappUtil.isEmpty(eventDetail.getEvtOptionTitle())){
					if("G0001".equals(eventDetail.getEvtType()) || "G0002".equals(eventDetail.getEvtType())){
						eventDetail.setEvtTypeNm("이벤트");
					}
				}else{
					eventDetail.setEvtTypeNm(eventDetail.getEvtOptionTitle());
				}
			}
			
			// 이벤트 상세내용 조회
			eventContentList = eventService.getEventDetailContentList(evtSeq);
			
			// 이벤트 참여 여부 조회
			if("Y".equals(loginYn)){
				//210820 조건추가 일반/매거진/y소식과 구분하기위해
				if(eventDetail != null && ("G0002".equals(eventDetail.getEvtType()) || "G0008".equals(eventDetail.getEvtType()))){
					//랜덤제공일때
					if(eventDetail != null && ("C0002".equals(eventDetail.getGiftChoiceType()))){
						if(eventService.getEventRandomJoinCount(evtSeq, loginCntrNo, loginUserId) > 0){
							eventJoinYn = "Y";
						}
					}else{
						//선택제공일때 210809 쿼리수정
						if(eventService.getEventJoinCount(evtSeq, loginCntrNo, loginUserId) > 0){
							eventJoinYn = "Y";
						}
					}
					
					logger.info("=====================================================");
					logger.info("eventDetail -> eventJoinYn: "+eventJoinYn);
					logger.info("=====================================================");
					
				}else{
					//210820 일반/매거진/Y소식 응모권이 상품으로 잇을 경우 보유여부체크
					 if(eventDetail != null && (!"G0005".equals(eventDetail.getEvtType()) && !"G0010".equals(eventDetail.getEvtType()))){
						
						//랜덤제공일때
						if(eventDetail != null && ("C0002".equals(eventDetail.getGiftChoiceType())) && ("Y".equals(eventDetail.getTicketYn())) ){
							eventTicketHaveYn = "Y"; // 티켓 보유 여부
							
							if(eventService.getEventRandomJoinCount(evtSeq, loginCntrNo, loginUserId) > 0){
								eventTicketJoinYn = "Y"; // 티켓잇는 이벤트 참여 여부
							}
						}else if(eventDetail != null && ("C0001".equals(eventDetail.getGiftChoiceType())) && ("Y".equals(eventDetail.getTicketYn())) ){
							eventTicketHaveYn = "Y"; // 티켓 보유 여부
							
							//선택제공일때 210809 쿼리수정
							if(eventService.getEventJoinCount(evtSeq, loginCntrNo, loginUserId) > 0){
								eventTicketJoinYn = "Y";// 티켓잇는 이벤트 참여 여부
							}
						}
						 
					}
				}
				
				//참여대상 여부 조회
				String targetMem = eventDetail.getTargetMem();
				String targetGender = eventDetail.getGender();
				String targetPpCdList = eventDetail.getPpCdList();
				String targetPpCdL = eventDetail.getPpCdL();
				String targetAgeList = eventDetail.getAgeList();
				//230508 부가서비스 추가
				String targetVasCdList = eventDetail.getVasCdList();
				String targetVasCdL = eventDetail.getVasCdL();
				
				int targetAgeStartNum = 0;
				int targetAgeEndNum = 0;
				
				if(YappUtil.isNotEmpty(eventDetail.getAgeStartNum())){
					targetAgeStartNum = Integer.parseInt(eventDetail.getAgeStartNum());
				}
				
				if(YappUtil.isNotEmpty(eventDetail.getAgeEndNum())){
					targetAgeEndNum = Integer.parseInt(eventDetail.getAgeEndNum());
				}
				
				logger.info("=====================================================");
				logger.info("eventDetail -> targetMem: "+targetMem);
				logger.info("eventDetail -> targetGender: "+targetGender);
				logger.info("eventDetail -> targetPpCdList: "+targetPpCdList);
				logger.info("eventDetail -> targetPpCdL: "+targetPpCdL);
				logger.info("eventDetail -> targetAgeList: "+targetAgeList);
				logger.info("eventDetail -> targetAgeStartNum: "+targetAgeStartNum);
				logger.info("eventDetail -> targetAgeEndNum: "+targetAgeEndNum);
				logger.info("eventDetail -> targetVasCdL: "+targetVasCdL);
				logger.info("=====================================================");
				
				String gender = "";
				String birthDay = "";
				String ppCd = "";
				String ppCatL = "";
				
				if(YappUtil.isEq(memStatus, "G0001")){
					if(YappUtil.isNotEmpty(loginCntrNo)){
						
						userInfo = userService.getYappUserInfo(loginCntrNo);
						
						if(userInfo != null){
							birthDay = userInfo.getBirthDay();
							ppCd = userInfo.getPpCd();
							
							if(YappUtil.isNotEmpty(ppCd)){
								ppCatL = cmsService.getPpCatLCode(ppCd).getPpCatL();
							}
						}
					}
					
					if(YappUtil.isNotEmpty(loginUserId)){
						
						userKtInfo = userService.getYappUserKtInfo(loginUserId);
						
						if(userKtInfo != null){
							gender = userKtInfo.getGender();
						}
					}
				}else if(YappUtil.isEq(memStatus, "G0002")){
					if(YappUtil.isNotEmpty(loginCntrNo)){
						userInfo = userService.getYappUserInfo(loginCntrNo);
						
						if(userInfo != null){
							birthDay = userInfo.getBirthDay();
							ppCd = userInfo.getPpCd();
							
							if(YappUtil.isNotEmpty(ppCd)){
								ppCatL = cmsService.getPpCatLCode(ppCd).getPpCatL();
							}
						}
					}
				}else{
					if(YappUtil.isNotEmpty(loginUserId)){
						userKtInfo = userService.getYappUserKtInfo(loginUserId);
						
						if(userKtInfo != null){
							birthDay = userKtInfo.getBirthDay();
							gender = userKtInfo.getGender();
						}
					}
				}
				
				logger.info("=====================================================");
				logger.info("eventDetail -> gender: "+gender);
				logger.info("eventDetail -> birthDay: "+birthDay);
				logger.info("eventDetail -> ppCd: "+ppCd);
				logger.info("eventDetail -> ppCatL: "+ppCatL);
				logger.info("eventDetail -> memStatus: "+memStatus);
				logger.info("=====================================================");
				
				//회원유형 검증 (전체 또는 정회원일때 이벤트 참여 가능하게 변경)
				if(YappUtil.isEq(targetMem, "G0001")){//정회원일때			
					if(YappUtil.isNotEq(memStatus, "G0001") && YappUtil.isNotEq(memStatus, "G0002")){
						targetYn = "N";
						targetCode = "02";
					}
				}else if(YappUtil.isEq(targetMem, "G0002")){//준회원일때
					if(YappUtil.isNotEq(memStatus, "G0003")){
						targetYn = "N";
						targetCode = "03";
					}
				}
				
				//성별 검증
				if(YappUtil.isEq(targetGender, "G0001")){
					if(YappUtil.isNotEq(gender, "01")){
						targetYn = "N";
						targetCode = "04";
					}
					
				}else if(YappUtil.isEq(targetGender, "G0002")){
					if(YappUtil.isNotEq(gender, "02")){
						targetYn = "N";
						targetCode = "05";
					}
				}
				
				//나이 검증
				int age =0;
				String ageGrp = "";
				
				if(YappUtil.isNotEmpty(birthDay)){
					age = YappUtil.getAgeYear(birthDay);
					ageGrp = YappUtil.getAgeGroupYear(birthDay);
				}
				
				if(YappUtil.isNotEmpty(targetAgeList) && !targetAgeList.contains("ALL")){
					if(YappUtil.isEmpty(ageGrp) || !targetAgeList.contains(ageGrp)){
						
						if(targetAgeStartNum !=0 && targetAgeEndNum !=0){
							if(age==0 || age<targetAgeStartNum || age>targetAgeEndNum){
								targetYn = "N";
								targetCode = "06";
							}
						}else{
							targetYn = "N";
							targetCode = "07";
						}
					}
				}else{
					if(targetAgeStartNum !=0 && targetAgeEndNum !=0){
						if(age==0 || age<targetAgeStartNum || age>targetAgeEndNum){
							targetYn = "N";
							targetCode = "08";
						}
					}
				}
				
				//요금제 검증
				if(YappUtil.isNotEmpty(targetPpCdList) && !targetPpCdList.contains("ALL")){
					if(YappUtil.isEmpty(ppCatL) || !targetPpCdList.contains(ppCatL)){
						
						if(YappUtil.isNotEmpty(targetPpCdL)){
							if(YappUtil.isEmpty(ppCd) || !targetPpCdL.contains(ppCd)){
								targetYn = "N";
								targetCode = "10";
							}
						}else{
							targetYn = "N";
							targetCode = "09";
						}
					}
				}else{
					if(YappUtil.isNotEmpty(targetPpCdL)){
						if(YappUtil.isEmpty(ppCd) || !targetPpCdL.contains(ppCd)){
							targetYn = "N";
							targetCode = "10";
						}
					}
				}
				
				logger.info("================= 부가서비스 =================");
				if(!YappUtil.isEq(memStatus, "G0003")){//정회원일때			
				// 설정된 부가서비스 목록을 조회한다.
				List<VasItem> settingVasItemList = shubService.callFn133(loginMobileNo).getVasCdList();
				
				if(YappUtil.isNotEmpty(targetVasCdList) && !targetVasCdList.contains("NONE")){
					//부가서비스(직접입력이 아닌 선택) 설정되어 있으면 그룹코드에서 가져온다.
					String[] vasList = targetVasCdList.split("\\|\\|");
					
					for(int i = 0; i < vasList.length; i++){
						List<String> tempList = new ArrayList<>();
						List<GrpCode> groupVasItemList = cmsService.getVasCodeList(vasList[i]);
												
						for(GrpCode group : groupVasItemList){
							tempList.add(group.getCodeNm());
						}

							targetVasCdL = String.join(",", tempList); //그룹코드에서 가져온 부가서비스 코드를 문자열에 더한다
							logger.info("targetVasCdL :: "+ targetVasCdL);
					}
					
					//사용자가 가입한 부가서비스가 있을 경우
					if(!settingVasItemList.isEmpty()){
						
						boolean containYn = false;
						
						for(int i = 0; i < settingVasItemList.size(); i++){
							String vasItemCd = settingVasItemList.get(i).getVasItemCd();
							
							if(targetVasCdL.contains(vasItemCd)){
								containYn = true;
								break;
							}
						}
						
						if(!containYn){ //부가서비스 중 하나라도 속하지 않으면 대상자 아님
							targetYn = "N";
							targetCode = "11";
						}
						
					}else{
						targetYn = "N";
						targetCode = "12";
					}
					
				}else{
					//부가서비스를 선택하지 않고 직접 입력하기로만 등록했을 경우
					if(YappUtil.isNotEmpty(targetVasCdL)){
						logger.info("targetVasCdL :: "+ targetVasCdL);
						if(!settingVasItemList.isEmpty()){
							boolean containYn = false;
							
							for(int i = 0; i < settingVasItemList.size(); i++){
								String vasItemCd = settingVasItemList.get(i).getVasItemCd();
								
								if(targetVasCdL.contains(vasItemCd)){
									containYn = true;
									break;
								}
							}
							
							if(!containYn){ //부가서비스 중 하나라도 속하지 않으면 대상자 아님
								targetYn = "N";
								targetCode = "11";
							}
							
						}else{
							targetYn = "N";
							targetCode = "12";
						}
					}
					
				}
				}
			}
		
			//출석체크
			 if(eventDetail != null && ("G0005".equals(eventDetail.getEvtType()) || "G0010".equals(eventDetail.getEvtType()))){
				
				//출석체크
				if("G0005".equals(eventDetail.getEvtType())){
					eventAttendList = eventService.getMyEventAttendList(evtSeq, loginCntrNo, loginUserId);
				//응모권 출석체크
				}else{
					eventAttendList = eventService.getMyTicketEventAttendList(evtSeq, loginCntrNo, loginUserId, eventDetail.getEvtStartDt());
				}
				
				int eventAttendListCnt = eventAttendList.size();
				if(eventService.getEventAttendNowChk(evtSeq, loginCntrNo, loginUserId) > 0){
					nowAttendYn = "Y";
				}else{
					if("Y".equals(eventAttendList.get(eventAttendListCnt-1).getAttendYn())){
						nowAttendYn = "Y";
					}
				}
			}
			 
			// 이벤트 경품 목록 조회
			if(eventDetail != null && ("G0005".equals(eventDetail.getEvtType()) || "G0010".equals(eventDetail.getEvtType()))){
				for (EventGift eventGift : eventAttendList) {
					if("N".equals(eventGift.getAttendYn())){
						eventGiftList = eventService.getEventDetailAttendGiftList(evtSeq, eventGift.getGiftDay());
						break;
					}
				}
				for (EventGift eventGiftObj : eventGiftList) {
					if("Y".equals(eventGiftObj.getSoldoutYn())){
						soldoutYn = "Y";
					}
				}
				
				//이벤트상세 상품명/상품건수
				eventAllGiftList = eventService.getAttendEventAllGiftList(evtSeq);
				
			}else if(eventDetail != null && ("G0004".equals(eventDetail.getEvtType()))){	// 20211026 추가 (Y프렌즈 이벤트 다중경품 지급)
				
				logger.info("Y프렌즈 : " + eventDetail.getEvtType());
				
				String multiGiftRewardChk = eventDetail.getGiftMultiReward();
				
				if("Y".equals(multiGiftRewardChk)){
					eventGiftList = eventService.getEventDetailOfflineGiftList(evtSeq);
					eventAllGiftList = eventService.getEventDetailOfflineGiftList(evtSeq);
				}else{
					eventGiftList = eventService.getEventDetailGiftList(evtSeq, null);
					eventAllGiftList = eventService.getEventDetailGiftList(evtSeq, null);
				}
				
			}else{
				eventGiftList = eventService.getEventDetailGiftList(evtSeq, null);
				// 선착순 / 온라인 경품 관련 잔여 상품수량 소진처리 로직 수정 적용 2023.06.14 by jk - S
				// ( 0번째 경품이 소진시 이벤트 경품 소진처리 되는 현상 수정 처리 )
				if(("G0002".equals(eventDetail.getEvtType()) && "O0002".equals(eventDetail.getGiftOfferType()))){
					int remainCnt = eventService.getEventRemainGiftTotCountByOrder(evtSeq);
					if(remainCnt == 0){
						soldoutYn = "Y";
					}
				} else { // - E
				
					//응모권 보유한 이벤트일때 소진여부 체크 
					if(eventTicketHaveYn == "Y"){
						int remainCnt = eventService.getEventRemainGiftCount(eventGiftList.get(0).getIssueSeq());
						if(remainCnt == 0){
							soldoutYn = "Y";
						}
					}
				}
				
				eventAllGiftList = eventService.getEventDetailGiftList(evtSeq, null);
			}

			mv.addObject("soldoutYn", soldoutYn);
			
			 if(eventDetail != null && ("G0004".equals(eventDetail.getEvtType()))){
				YfriendsMenu yfriendsMenuInfo = yFriendsService.getEventMenu(evtSeq, "N");
				mv.addObject("yfriendsMenuName", yfriendsMenuInfo.getMenuName());
			}
			
			 //210610 투표하기
			 if(eventDetail != null && ("G0008".equals(eventDetail.getEvtType()))){
				 for(int i = 0; i < eventContentList.size(); i++){
					 if(eventContentList.get(i).getConDtlType().equals("C0009")){
						 int voteSeq = Integer.parseInt(eventContentList.get(i).getConDtl());
						 //투표대상 조회
						 voteItemList = voteService.getVoteItemList(voteSeq);

						 break;
					 }
				 }
				 
				 //210628 투표율 계산 수정
				 int voteTotal = 0;
				 for(int x = 0; x < voteItemList.size(); x++){
					 voteTotal = voteTotal + voteItemList.get(x).getVoteItemCnt();
				 }
				 
				 for(int i = 0; i < voteItemList.size(); i++){
					 int percent = 0;
					 if(voteItemList.get(i).getVoteItemCnt() > 0 && voteTotal > 0){
						 double per = 0;
						 per = (double) voteItemList.get(i).getVoteItemCnt()/(double)voteTotal;
						 per = per * 100.0;
						 percent = (int) Math.round(per);
					 }								
					 voteItemList.get(i).setVotePercent(percent);
				 }
						 
				 //투표상태 확인
				 voteHis = eventService.getVoteChk(evtSeq, loginCntrNo, loginUserId);
				 
				 //투표율 표시여부 확인
				 voteRateYn = eventService.getVoteRateYn(evtSeq);
				 
				 if(voteHis != null){
					 
					 String voteType = eventService.getVoteType(evtSeq);
					 
					 logger.info("=====================================================");
					 logger.info("eventDetail -> voteHis.getEvtSeq(): "+voteHis.getEvtSeq());
					 logger.info("eventDetail -> voteHis.getRegDt(): "+voteHis.getRegDt());
					 logger.info("eventDetail -> getVoteType(): "+voteType);
					 logger.info("eventDetail -> getVoteRateYn(): "+voteRateYn);
					 logger.info("=====================================================");
					 
					 if(YappUtil.isNotEq(voteType, "G0002")){
						 //한번투표 케이스
						 voteYn = "Y";
						 
					 }else{
						 //매일 반복 가능 투표 케이스

						 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
						 
						 String strRegDt = voteHis.getRegDt();
						 String strToday = format.format(new Date());

						 Date dRegDt = format.parse(strRegDt);
						 Date dToday = format.parse(strToday);
						 
						 if(dToday.after(dRegDt)){
							 voteYn = "N";
						 }else{
							 voteYn = "Y";
						 }
						 
						 logger.info("=====================================================");
						 logger.info("eventDetail -> today: "+dToday.toString());
						 logger.info("eventDetail -> dRegDt: "+dRegDt.toString());
						 logger.info("eventDetail -> voteYn: "+voteYn);
						 logger.info("=====================================================");
						 
					 }
				 }else {
					 voteHis = new VoteHistory();
				 }
			 }
			 
			//230331 Y캔버스 수강 정보
			if(eventDetail != null && ("G0011".equals(eventDetail.getEvtType()))){
				canvasItemList = canvasService.getYcanvasItemList(evtSeq, null, null, loginCntrNo, loginUserId);
			}
			 
			int replyCnt = 0;
			int intlikeCnt = 0;
				
			replyCnt += eventService.getEventReplyCnt(evtSeq);
			intlikeCnt += eventService.getEventLikeCnt(evtSeq);
			
			String likeCnt = YappUtil.setStringNumber(intlikeCnt);
			 
			if("Y".equals(loginYn)){
				 
				EventLike likeInfo = eventService.getUserLikeInfo(evtSeq, loginCntrNo, loginUserId);
					
				if(likeInfo != null){
					mv.addObject("likeYn", likeInfo.getLikeYn());
					}
			 }
			 
			 mv.addObject("replyCnt", replyCnt);
			 mv.addObject("likeCnt", likeCnt);
			 
			 //classnotFoundexception에러
//			 try{
//				 URLClassLoader loader = new URLClassLoader(new URL[]{
//						 new URL("file://C:/CL_Article/ClassNotFoundException/")});
//				 loader.loadClass("DesNotExist");
//			 }catch(ClassNotFoundException e){
//				logger.error("ClassNotFoundException : "+ e);
//				errorMsg = cmnService.getMsg("ERR_Y_SYSTEM");
//			 }
			 
			//준회원 이벤트 참여 코드값
			List<GrpCode> grpList = cmsService.getGrpCodeList("ASSO_ADDR_YN");
				
			for(GrpCode group : grpList){
				mv.addObject("assoAddrYn",group.getCodeKey());
				//준회원일떄 회선정보 
				if(memStatus.equals("G0003")){
					mv.addObject("assoUserMobileNo", userKtInfo.getMobileNo());
				}
			}
			 
		}catch(RuntimeException e){
			//e.printStackTrace();
			if("".equals(errorMsg)){
				logger.error("ERROR ::" + e);
				errorMsg = cmnService.getMsg("ERR_Y_SYSTEM");
			}
		}catch(Exception e){
			//e.printStackTrace();
			if("".equals(errorMsg)){
				logger.error("ERROR ::" + e);
				errorMsg = cmnService.getMsg("ERR_Y_SYSTEM");
			}
		}
		
		mv.addObject("canvasItemList", canvasItemList);
		mv.addObject("memStatus", memStatus);
		mv.addObject("cntrNo", loginCntrNo);
		mv.addObject("userId", loginUserId);
		mv.addObject("chkMsg", errorMsg);
		mv.addObject("ysid", ysid);
		mv.addObject("autoLogin", autoLogin);
		mv.addObject("osTp", osTp);
		mv.addObject("appVrsn", appVrsn);
		mv.addObject("partyName", partyName);
		
		mv.addObject("chkMsg", errorMsg);
		
		mv.addObject("evtSeq", evtSeq);
		mv.addObject("eventDetail", eventDetail);
		mv.addObject("eventContentList", eventContentList);
		mv.addObject("eventGiftList", eventGiftList);
		mv.addObject("eventAllGiftList", eventAllGiftList);
		
		mv.addObject("eventAttendList", eventAttendList);
		mv.addObject("nowAttendYn", nowAttendYn);
		
		mv.addObject("loginYn", loginYn);
		mv.addObject("targetYn", targetYn); //응모대상
		mv.addObject("targetCode", targetCode);
		mv.addObject("eventJoinYn", eventJoinYn);
		mv.addObject("eventTicketHaveYn", eventTicketHaveYn); //210820 추가 이벤트 응모권보유여부
		mv.addObject("eventTicketJoinYn", eventTicketJoinYn); //210820 추가 응모권 보유한 이벤트  참여 여부
		
		//210610 투표상태
		mv.addObject("voteYn", voteYn);
		mv.addObject("voteItemList", voteItemList);
		
		//230106 투표율 표시여부
		mv.addObject("voteRateYn", voteRateYn);
		
		//210615 사용자가 선택한 투표대상
		mv.addObject("voteItem", voteHis.getVoteItemSeq());
		
		// 경품 복수선택 기능 구현 : 경품을 복수 선택 가능한 경우, eventDetailMulti 페이지로 넘긴다. 2023.06.12 by jk  -Start
		if(null != eventDetail.getGiftMaxChoice() && eventDetail.getGiftMaxChoice() > 1){
			mv.setViewName("/event/eventDetailMulti");// 복수 응모 가능 UI (TO-BE)
		} else {
			mv.setViewName("/event/eventDetail"); // 단수 경품 응모 기능 UI (AS-IS)
		}
		// End
		return mv; 
	}
	
	
	
	
	
	
	@ApiOperation(value="외부에서 이벤트 상세 화면 호출")
	@RequestMapping(value = "/event/evtDetail", method = RequestMethod.GET)
	public ModelAndView evtDetail(String evtSeq, HttpServletRequest req) throws Exception {
		ModelAndView mv = new ModelAndView();
		EventMaster eventDetail = new EventMaster();
		if(!YappUtil.isEmpty(evtSeq)){
			eventDetail = eventService.getEventDetailMaster(Integer.parseInt(evtSeq));
		}
		
		mv.addObject("evtSeq", evtSeq);
		mv.addObject("eventDetail", eventDetail);
		
		mv.setViewName("/event/evtDetail");
		return mv; 
	}
	
	@ApiOperation(value="이벤트 온라인(리워드) 경품참여")
	@ResponseBody
	@RequestMapping(value = "/event/giftRewardJoin", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<EventGift> eventGiftOnlineJoin(EventGiftJoin eventGiftJoin, HttpServletRequest req) throws Exception {
		//로그인 여부
		String apiUrl = YappUtil.nToStr(req.getRequestURI());
		String loginYn = loginCheck(req, apiUrl);
		
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		String memStatus = "";
		String loginUserId = "";
		String credentialIde = "";

		if(SessionKeeper.getSdata(req) != null){
			memStatus = SessionKeeper.getSdata(req).getMemStatus();
			loginUserId = SessionKeeper.getSdata(req).getUserId();
			credentialIde = SessionKeeper.getSdata(req).getCredentialId();
		}
		
		logger.info("==============================================================");
		logger.info("/event/giftRewardJoin -> memStatus       : "+memStatus);
		logger.info("/event/giftRewardJoin -> loginCntrNo     : "+loginCntrNo);
		logger.info("/event/giftRewardJoin -> loginUserId     : "+loginUserId);
		logger.info("==============================================================");
		
		String chkMsg = "";
		ResultInfo<EventGift> resultInfo = new ResultInfo<>();
		EventGift eventGift = new EventGift();
		
		try{
			//준회원일때 회선번호 확인 후 다르거나 없다면 update
			if(memStatus.equals("G0003")){
				setCheckUserMobileNo(loginUserId, YappUtil.isEmpty(credentialIde) ? "" : credentialIde, YappUtil.isEmpty(eventGiftJoin.getRecvMobileNo()) ? "" : eventGiftJoin.getRecvMobileNo());
			}
			
			//이벤트 참여 횟수 조회
			int eventJoinCount = eventService.getEventJoinCount(eventGiftJoin.getEvtSeq(), loginCntrNo, loginUserId);
			if(eventJoinCount > 0){
				chkMsg = cmnService.getMsg("CHK_Y_NO_GIFT");
				throw new YappRuntimeException("CHECK_MSG", chkMsg);
			}
			
			//이벤트 종료여부 체크
			EventMaster eventDetail = eventService.getEventDetailMaster(eventGiftJoin.getEvtSeq());
			if(eventDetail  == null || "Y".equals(eventDetail.getEndYn())){
				chkMsg = cmnService.getMsg("CHK_Y_NO_GIFT");
				throw new YappRuntimeException("CHECK_MSG", chkMsg);
			}
			
			//경품 잔여수량 체크
			if(eventGiftJoin.getIssueSeq() == 0){
				chkMsg = cmnService.getMsg("CHK_Y_FULL_GIFT2");
				throw new YappRuntimeException("CHECK_MSG", chkMsg);
			}
			
			//Y프렌즈일때 인원수별 경품 잔여수량 체크
			if(eventDetail.getEvtType().equals("G0004")){
				boolean bRet = giftRewardTargetCheck(eventDetail.getEvtSeq(), loginCntrNo);
			
				if(bRet==false){
					chkMsg = cmnService.getMsg("CHK_Y_FULL_GIFT2");
					throw new YappRuntimeException("CHECK_MSG", chkMsg);
				}
			}else{
				if("C0001".equals(eventDetail.getGiftChoiceType()) && "O0002".equals(eventDetail.getGiftOfferType())){
					int remainCnt = eventService.getEventRemainGiftCount(eventGiftJoin.getIssueSeq());
					if(remainCnt == 0){
						chkMsg = cmnService.getMsg("CHK_Y_FULL_GIFT2");
						throw new YappRuntimeException("CHECK_MSG", chkMsg);
					}
				}
			}
			
			EventAppl appl = new EventAppl();
			appl.setEvtSeq(eventGiftJoin.getEvtSeq());
			appl.setCntrNo(loginCntrNo);
			appl.setUserId(loginUserId);
			appl.setWinYn("N");
			String giftOfferType = eventDetail.getGiftOfferType();
			if("O0002".equals(giftOfferType)){
				//선착순일때
				int rewardCnt = eventService.getRewardCount(eventGiftJoin.getIssueSeq(), loginCntrNo, loginUserId);
				if(rewardCnt > 0){
					chkMsg = cmnService.getMsg("ERR_EXST_APPL_EVENT");
					throw new YappRuntimeException("CHECK_MSG", chkMsg);
				}
				
				//210913
				int remainCnt = eventService.getEventRemainGiftCount(eventGiftJoin.getIssueSeq());
				if(remainCnt == 0){
					chkMsg = cmnService.getMsg("CHK_Y_FULL_GIFT2");
					throw new YappException("CHECK_MSG", chkMsg);
				}else{
					
					eventGiftJoin.setCntrNo(loginCntrNo);
					eventGiftJoin.setUserId(loginUserId);
					
//					int updateCnt = eventService.updateGiftIssueInfo(eventGiftJoin);
//					if(updateCnt == 0){
//						chkMsg = cmnService.getMsg("CHK_Y_NO_GIFT");
//						throw new YappRuntimeException("CHECK_MSG", chkMsg);
//					}
					
					logger.info("setGiftSeq : " +eventGiftJoin.getGiftSeq());

					int rewardUpdCnt = eventService.updateEventRewardInfo(eventGiftJoin);
					
					if(rewardUpdCnt > 1){
						eventGift = eventService.getMyEventGiftInfo(eventGiftJoin);
						resultInfo.setResultData(eventGift);
						appl.setWinYn("Y");
					}else{
						chkMsg = cmnService.getMsg("CHK_Y_FULL_GIFT2");
						throw new YappRuntimeException("CHECK_MSG", chkMsg);
					}
					
				}

				// 이벤트 응모
				cmsService.applEventMaster(appl);
				
			}else if("O0003".equals(giftOfferType)){
				//추첨일때
				eventGiftJoin.setJoinSeq(0);
				eventGiftJoin.setRecvCntrNo(loginCntrNo);
				eventGiftJoin.setRecvUserId(loginUserId);
				eventGiftJoin.setWinYn("N");
				//eventGiftJoin.setRecvMobileNo(loginMobileNo);
				//eventGiftJoin.setRecvName(YappUtil.blindNameToName(cntrInfo.getUserNm(), 1));
				eventService.insertEntryInfo(eventGiftJoin);
				
				// 이벤트 응모
				cmsService.applEventMaster(appl);
			}
			

			
			logger.info("온라인 상품 완료");
			
		} catch (RuntimeException e){
			//e.printStackTrace();
			logger.error("API : /event/giftRewardJoin, ERROR [경품참여 불가] " + e);
			if("".equals(chkMsg)){
				throw new YappRuntimeException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_GIFT"));
			}else{
				throw new YappRuntimeException("CHECK_MSG", chkMsg);	
			}	
		}catch (Exception e){
			//e.printStackTrace();
			logger.error("API : /event/giftRewardJoin, ERROR [경품참여 불가] " + e);
			if("".equals(chkMsg)){
				throw new YappRuntimeException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_GIFT"));
			}else{
				throw new YappRuntimeException("CHECK_MSG", chkMsg);	
			}	
		}
		return resultInfo; 
	}
	
	/***
	 * 이벤트 온라인 복수 응모 기능 추가 API 2023.06.16
	 * @param eventGiftMultiReq
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="이벤트 온라인(리워드) 복수 경품참여")
	@ResponseBody
	@RequestMapping(value = "/event/giftRewardJoinMulti", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	//@NotCheckLogin
	public ResultInfo<EventGift> eventGiftOnlineJoinList(@RequestBody EventGiftMultiReq eventGiftMultiReq, HttpServletRequest req) throws Exception {
		//로그인 여부
		String apiUrl = YappUtil.nToStr(req.getRequestURI());
		String loginYn = loginCheck(req, apiUrl);
		
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		String memStatus = "";
		String loginUserId = "";
		String credentialIde = "";
		
		if(SessionKeeper.getSdata(req) != null){
			memStatus = SessionKeeper.getSdata(req).getMemStatus();
			loginUserId = SessionKeeper.getSdata(req).getUserId();
			credentialIde = SessionKeeper.getSdata(req).getCredentialId();
		}
		
		String profileInfo = environment.getActiveProfiles()[0];
		if("local".equals(profileInfo)){
			loginYn = "Y";
			memStatus = getDevUserInfo().get("memStatus");
			loginCntrNo = getDevUserInfo().get("loginCntrNo");
			loginUserId = getDevUserInfo().get("loginUserId");
		}
		
		logger.info("==============================================================");
		logger.info("/event/giftRewardJoin -> memStatus       : "+memStatus);
		logger.info("/event/giftRewardJoin -> loginCntrNo     : "+loginCntrNo);
		logger.info("/event/giftRewardJoin -> loginUserId     : "+loginUserId);
		logger.info("==============================================================");
		
		String chkMsg = "";
		ResultInfo<EventGift> resultInfo = new ResultInfo<>();
		
		try{
			
			//준회원일때 회선번호 확인 후 다르거나 없다면 update
			if(memStatus.equals("G0003")){
				setCheckUserMobileNo(loginUserId, YappUtil.isEmpty(credentialIde) ? "" : credentialIde, YappUtil.isEmpty(eventGiftMultiReq.getEventGiftJoinList().get(0).getRecvMobileNo()) ? "" : eventGiftMultiReq.getEventGiftJoinList().get(0).getRecvMobileNo());
			}
			
			List<EventGiftJoin> eventGiftJoinList = eventGiftMultiReq.getEventGiftJoinList();
			List<Integer> issueSeqList = eventGiftJoinList.stream().map(EventGiftJoin::getIssueSeq).collect(Collectors.toList());
			List<Integer> remainTotCnt = eventService.getEventRemainGiftTotCountByIssueSeq(issueSeqList);
			Integer evtTotGiftRemCnt = eventService.getEventRemainGiftTotCountByOrder(eventGiftMultiReq.getEvtSeq());
			int seqEmtCnt = (int)remainTotCnt.stream().filter(c -> c == 0).count();
			
			if(seqEmtCnt > 0 && issueSeqList.size() >= seqEmtCnt && evtTotGiftRemCnt > 0){//1) 사용자가 선택한건 중에 한건이라도 소진된 케이스 가 있는 경우 2) 사용자가선택한 건은 소진되고 전체 이벤트의 다른 상품은 존재하는 경우.
			
				chkMsg = cmnService.getMsg("CHK_Y_FULL_GIFT");
				throw new YappRuntimeException("CHECK_MSG", chkMsg);
			
			} else if(evtTotGiftRemCnt == 0){ // 이벤트 전체 상품이 소진된경우
				
				chkMsg = cmnService.getMsg("CHK_Y_FULL_GIFT2");
				throw new YappRuntimeException("CHECK_MSG", chkMsg);
			
			} else {
				
				
				// 복수 응모시, 하나의 이벤트 참여를 위해 루프 구조 밖으로 이벤트 참여 변경처리..
				EventAppl appl = new EventAppl();
				appl.setCntrNo(loginCntrNo);
				appl.setUserId(loginUserId);
			
				for(EventGiftJoin eventGiftJoin : eventGiftJoinList){
					//이벤트 참여 횟수 조회
					int eventJoinCount = eventService.getEventJoinCount(eventGiftJoin.getEvtSeq(), loginCntrNo, loginUserId);
					if(eventJoinCount > eventGiftMultiReq.getGiftMaxCnt()){
						chkMsg = cmnService.getMsg("CHK_Y_NO_GIFT");
						throw new YappRuntimeException("CHECK_MSG", chkMsg);
					}
					
					//이벤트 종료여부 체크
					EventMaster eventDetail = eventService.getEventDetailMaster(eventGiftJoin.getEvtSeq());
					if(eventDetail  == null || "Y".equals(eventDetail.getEndYn())){
						chkMsg = cmnService.getMsg("CHK_Y_NO_GIFT");
						throw new YappRuntimeException("CHECK_MSG", chkMsg);
					}
					
					//경품 잔여수량 체크
					if(eventGiftJoin.getIssueSeq() == 0){
						chkMsg = cmnService.getMsg("CHK_Y_FULL_GIFT2");
						throw new YappRuntimeException("CHECK_MSG", chkMsg);
					}
					
					//Y프렌즈일때 인원수별 경품 잔여수량 체크
					if(eventDetail.getEvtType().equals("G0004")){
						boolean bRet = giftRewardTargetCheck(eventDetail.getEvtSeq(), loginCntrNo);
					
						if(bRet==false){
							chkMsg = cmnService.getMsg("CHK_Y_FULL_GIFT2");
							throw new YappRuntimeException("CHECK_MSG", chkMsg);
						}
					}else{
						if("C0001".equals(eventDetail.getGiftChoiceType()) && "O0002".equals(eventDetail.getGiftOfferType())){
							int remainCnt = eventService.getEventRemainGiftCount(eventGiftJoin.getIssueSeq());
							if(remainCnt == 0){
								chkMsg = cmnService.getMsg("CHK_Y_FULL_GIFT2");
								throw new YappRuntimeException("CHECK_MSG", chkMsg);
							}
						}
					}
					
	
					appl.setEvtSeq(eventGiftJoin.getEvtSeq());
					appl.setWinYn("N");
					String giftOfferType = eventDetail.getGiftOfferType();
					if("O0002".equals(giftOfferType)){
						//선착순일때
						int rewardCnt = eventService.getRewardCount(eventGiftJoin.getIssueSeq(), loginCntrNo, loginUserId);
						if(rewardCnt > 0){
							chkMsg = cmnService.getMsg("ERR_EXST_APPL_EVENT");
							throw new YappRuntimeException("CHECK_MSG", chkMsg);
						}
						
						//210913
						int remainCnt = eventService.getEventRemainGiftCount(eventGiftJoin.getIssueSeq());
						if(remainCnt == 0){
							chkMsg = cmnService.getMsg("CHK_Y_FULL_GIFT2");
							throw new YappException("CHECK_MSG", chkMsg);
						}else{
							
							eventGiftJoin.setCntrNo(loginCntrNo);
							eventGiftJoin.setUserId(loginUserId);
							
		//					int updateCnt = eventService.updateGiftIssueInfo(eventGiftJoin);
		//					if(updateCnt == 0){
		//						chkMsg = cmnService.getMsg("CHK_Y_NO_GIFT");
		//						throw new YappRuntimeException("CHECK_MSG", chkMsg);
		//					}
							
							logger.info("setGiftSeq : " +eventGiftJoin.getGiftSeq());
		
							int rewardUpdCnt = eventService.updateEventRewardInfo(eventGiftJoin);
							
							if(rewardUpdCnt > 1){
								//eventGift = eventService.getMyEventGiftInfo(eventGiftJoin);
								//resultInfo.setResultData(eventGift);
								appl.setWinYn("Y");
							}else{
								chkMsg = cmnService.getMsg("CHK_Y_FULL_GIFT2");
								throw new YappRuntimeException("CHECK_MSG", chkMsg);
							}
							
						}					
					}else if("O0003".equals(giftOfferType)){
						//추첨일때
						eventGiftJoin.setJoinSeq(0);
						eventGiftJoin.setRecvCntrNo(loginCntrNo);
						eventGiftJoin.setRecvUserId(loginUserId);
						eventGiftJoin.setWinYn("N");
						//eventGiftJoin.setRecvMobileNo(loginMobileNo);
						//eventGiftJoin.setRecvName(YappUtil.blindNameToName(cntrInfo.getUserNm(), 1));
						eventService.insertEntryInfo(eventGiftJoin);
						
					}

				}
				// 복수 응모시, 하나의 이벤트 참여를 위해 루프 구조 밖으로 이벤트 참여 변경처리..
				// 이벤트 응모
				cmsService.applEventMaster(appl);
				logger.info("온라인 상품 완료");
			}
			
		} catch (RuntimeException e){
			//e.printStackTrace();
			logger.error("API : /event/giftRewardJoin, ERROR [경품참여 불가] " + e);
			if("".equals(chkMsg)){
				throw new YappRuntimeException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_GIFT"));
			}else{
				throw new YappRuntimeException("CHECK_MSG", chkMsg);	
			}	
		}catch (Exception e){
			//e.printStackTrace();
			logger.error("API : /event/giftRewardJoin, ERROR [경품참여 불가] " + e);
			if("".equals(chkMsg)){
				throw new YappRuntimeException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_GIFT"));
			}else{
				throw new YappRuntimeException("CHECK_MSG", chkMsg);	
			}	
		}
		return resultInfo; 
	}
	
	
	/**
	 * 210811 응모권 리워드 추가
	 * @param eventGiftJoin
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="이벤트 응모권(리워드) 경품참여")
	@ResponseBody
	@RequestMapping(value = "/event/giftTicketRewardJoin", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<EventGift> eventGiftTicketJoin(EventGiftJoin eventGiftJoin, HttpServletRequest req) throws Exception {
		//로그인 여부
		String apiUrl = YappUtil.nToStr(req.getRequestURI());
		String loginYn = loginCheck(req, apiUrl);
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		String memStatus = "";
		String loginUserId = "";
		String credentialIde = "";
		
		if(SessionKeeper.getSdata(req) != null){
			memStatus = SessionKeeper.getSdata(req).getMemStatus();
			loginUserId = SessionKeeper.getSdata(req).getUserId();
			credentialIde = SessionKeeper.getSdata(req).getCredentialId();
		}
		
		logger.info("==============================================================");
		logger.info("/event/giftTicketRewardJoin -> memStatus       : "+memStatus);
		logger.info("/event/giftTicketRewardJoin -> loginCntrNo     : "+loginCntrNo);
		logger.info("/event/giftTicketRewardJoin -> loginUserId     : "+loginUserId);
		logger.info("==============================================================");
		
		//로컬 테스트용(서버올릴땐 삭제)
		//loginCntrNo = "524511905";
		
		String chkMsg = "";
		ResultInfo<EventGift> resultInfo = new ResultInfo<>();
		EventGift eventGift = new EventGift();
		
		try{
			//준회원일때 회선번호 확인 후 다르거나 없다면 update
			if(memStatus.equals("G0003")){
				setCheckUserMobileNo(loginUserId, YappUtil.isEmpty(credentialIde) ? "" : credentialIde, YappUtil.isEmpty(eventGiftJoin.getRecvMobileNo()) ? "" : eventGiftJoin.getRecvMobileNo());
			}
			
			
			//이벤트 참여 횟수 조회
			int eventJoinCount = eventService.getEventJoinCount(eventGiftJoin.getEvtSeq(), loginCntrNo, loginUserId);
			if(eventJoinCount > 0){
				chkMsg = cmnService.getMsg("CHK_Y_NO_GIFT");
				throw new YappRuntimeException("CHECK_MSG", chkMsg);
			}
			
			//이벤트 종료여부 체크
			EventMaster eventDetail = eventService.getEventDetailMaster(eventGiftJoin.getEvtSeq());
			if(eventDetail  == null || "Y".equals(eventDetail.getEndYn())){
				chkMsg = cmnService.getMsg("CHK_Y_NO_GIFT");
				throw new YappRuntimeException("CHECK_MSG", chkMsg);
			}
			
			//경품 잔여수량 체크
			if(eventGiftJoin.getIssueSeq() == 0){
				chkMsg = cmnService.getMsg("CHK_Y_FULL_GIFT2");
				throw new YappRuntimeException("CHECK_MSG", chkMsg);
			}
			if("C0001".equals(eventDetail.getGiftChoiceType()) && "O0002".equals(eventDetail.getGiftOfferType())){
				
				int remainCnt = eventService.getEventRemainGiftCount(eventGiftJoin.getIssueSeq());
				
				if(remainCnt == 0){
					chkMsg = cmnService.getMsg("CHK_Y_FULL_GIFT2");
					throw new YappRuntimeException("CHECK_MSG", chkMsg);
				}
			}
			
			EventAppl appl = new EventAppl();
			appl.setEvtSeq(eventGiftJoin.getEvtSeq());
			appl.setCntrNo(loginCntrNo);
			appl.setUserId(loginUserId);
			appl.setWinYn("N");
			String giftOfferType = eventDetail.getGiftOfferType();
			if("O0002".equals(giftOfferType)){
				//선착순일때
//				int rewardCnt = eventService.getRewardCount(eventGiftJoin.getIssueSeq(), loginCntrNo);
//				if(rewardCnt > 0){
//					chkMsg = cmnService.getMsg("ERR_EXST_APPL_EVENT");
//					throw new YappRuntimeException("CHECK_MSG", chkMsg);
//				}
//				eventGiftJoin.setCntrNo(loginCntrNo);
//				int updateCnt = eventService.updateGiftIssueInfo(eventGiftJoin);
//				if(updateCnt == 0){
//					chkMsg = cmnService.getMsg("CHK_Y_NO_GIFT");
//					throw new YappRuntimeException("CHECK_MSG", chkMsg);
//				}
//				eventService.updateRewardInfo(eventGiftJoin);
				
				
				eventGiftJoin.setCntrNo(loginCntrNo);
				eventGiftJoin.setUserId(loginUserId);
				
				//사용자 응모권 발급
				ticketService.insertEventTicket(eventGiftJoin);
				int updateCnt = eventService.updateGiftIssueInfo(eventGiftJoin);
				if(updateCnt == 0){
					chkMsg = cmnService.getMsg("CHK_Y_NO_GIFT");
					throw new YappRuntimeException("CHECK_MSG", chkMsg);
				}
				
				
				eventGift = eventService.getMyEventTicketGiftInfo(eventGiftJoin);
				resultInfo.setResultData(eventGift);
				
				appl.setWinYn("Y");
			}else if("O0003".equals(giftOfferType)){
				//추첨일때
				eventGiftJoin.setJoinSeq(0);
				eventGiftJoin.setRecvCntrNo(loginCntrNo);
				eventGiftJoin.setRecvUserId(loginUserId);
				eventGiftJoin.setWinYn("N");
				eventService.insertEntryInfo(eventGiftJoin);
			}
			
			// 이벤트 응모
			cmsService.applEventMaster(appl);
			
			logger.info("응모권 상품 완료");
			
		} catch (RuntimeException e){
			//e.printStackTrace();
			logger.error("API : /event/giftTicketRewardJoin, ERROR [경품참여 불가] " + e);
			if("".equals(chkMsg)){
				throw new YappRuntimeException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_GIFT"));
			}else{
				throw new YappRuntimeException("CHECK_MSG", chkMsg);	
			}	
		}catch (Exception e){
			//e.printStackTrace();
			logger.error("API : /event/giftTicketRewardJoin, ERROR [경품참여 불가] " + e);
			if("".equals(chkMsg)){
				throw new YappRuntimeException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_GIFT"));
			}else{
				throw new YappRuntimeException("CHECK_MSG", chkMsg);	
			}	
		}
		return resultInfo; 
	}
	
	@ApiOperation(value="이벤트 오프라인 경품참여")
	@ResponseBody
	@RequestMapping(value = "/event/giftOfflineJoin", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<String> eventGiftOfflineJoin(EventGiftJoin eventGiftJoin, HttpServletRequest req) throws Exception {
		//로그인 여부
		String apiUrl = YappUtil.nToStr(req.getRequestURI());
		String loginYn = loginCheck(req, apiUrl);
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		String memStatus = "";
		String loginUserId = "";
		String credentialIde = "";
		
		if(SessionKeeper.getSdata(req) != null){
			memStatus = SessionKeeper.getSdata(req).getMemStatus();
			loginUserId = SessionKeeper.getSdata(req).getUserId();
			credentialIde = SessionKeeper.getSdata(req).getCredentialId();
		}
		
		logger.info("==============================================================");
		logger.info("/event/giftOfflineJoin -> memStatus       : "+memStatus);
		logger.info("/event/giftOfflineJoin -> loginCntrNo     : "+loginCntrNo);
		logger.info("/event/giftOfflineJoin -> loginUserId     : "+loginUserId);
		logger.info("==============================================================");
		
		
		String chkMsg = "";
		try{
			//준회원일때 회선번호 확인 후 다르거나 없다면 update
			if(memStatus.equals("G0003")){
				setCheckUserMobileNo(loginUserId, YappUtil.isEmpty(credentialIde) ? "" : credentialIde, YappUtil.isEmpty(eventGiftJoin.getRecvMobileNo()) ? "" : eventGiftJoin.getRecvMobileNo());
			}
			
			//이벤트 참여 횟수 조회
			int eventJoinCount = eventService.getEventJoinCount(eventGiftJoin.getEvtSeq(), loginCntrNo, loginUserId);
			if(eventJoinCount > 0){
				chkMsg = cmnService.getMsg("CHK_Y_NO_GIFT");
				throw new YappRuntimeException("CHECK_MSG", chkMsg);
			}
			
			//이벤트 종료여부 체크
			EventMaster eventDetail = eventService.getEventDetailMaster(eventGiftJoin.getEvtSeq());
			if(eventDetail  == null || "Y".equals(eventDetail.getEndYn())){
				chkMsg = cmnService.getMsg("CHK_Y_NO_GIFT");
				throw new YappRuntimeException("CHECK_MSG", chkMsg);
			}
			
			String giftOfferType = eventDetail.getGiftOfferType();
			
			
			if(eventGiftJoin.getIssueSeq() == 0){
				chkMsg = cmnService.getMsg("CHK_Y_FULL_GIFT2");
				throw new YappRuntimeException("CHECK_MSG", chkMsg);
			}
			
			//경품 잔여수량 체크
			if("C0001".equals(eventDetail.getGiftChoiceType()) && "O0002".equals(eventDetail.getGiftOfferType())){
				int remainCnt = eventService.getEventRemainGiftCount(eventGiftJoin.getIssueSeq());
				if(remainCnt == 0){
					chkMsg = cmnService.getMsg("CHK_Y_FULL_GIFT2");
					throw new YappRuntimeException("CHECK_MSG", chkMsg);
				}
			}
			
			EventAppl appl = new EventAppl();
			appl.setEvtSeq(eventGiftJoin.getEvtSeq());
			appl.setCntrNo(loginCntrNo);
			appl.setUserId(loginUserId);
			appl.setWinYn("N");
			if("O0002".equals(giftOfferType)){
				//선착순일때
				eventGiftJoin.setCntrNo(loginCntrNo);
				eventGiftJoin.setUserId(loginUserId);
				int updateCnt = eventService.updateGiftIssueInfo(eventGiftJoin);
				if(updateCnt == 0){
					chkMsg = cmnService.getMsg("CHK_Y_NO_GIFT");
					throw new YappRuntimeException("CHECK_MSG", chkMsg);
				}
				eventGiftJoin.setJoinSeq(0);
				eventGiftJoin.setRecvCntrNo(loginCntrNo);
				eventGiftJoin.setRecvUserId(loginUserId);
				eventGiftJoin.setWinYn("Y");
				eventService.insertEntryInfo(eventGiftJoin);
				appl.setWinYn("Y");
				
			}else if("O0003".equals(giftOfferType)){
				//추첨일때
				eventGiftJoin.setJoinSeq(0);
				eventGiftJoin.setRecvCntrNo(loginCntrNo);
				eventGiftJoin.setRecvUserId(loginUserId);
				eventGiftJoin.setWinYn("N");
				//eventGiftJoin.setRecvMobileNo(loginMobileNo);
				//eventGiftJoin.setRecvName(YappUtil.blindNameToName(cntrInfo.getUserNm(), 1));
				eventService.insertEntryInfo(eventGiftJoin);
			}else{
				chkMsg = cmnService.getMsg("CHK_Y_NO_GIFT");
				throw new YappRuntimeException("CHECK_MSG", chkMsg);
			}
			// 이벤트 응모
			cmsService.applEventMaster(appl);
		} catch (RuntimeException e){
			//e.printStackTrace();
			logger.error("API : /event/giftOfflineJoin, ERROR [경품참여 불가] " + e);
			if("".equals(chkMsg)){
				throw new YappRuntimeException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_GIFT"));
			}else{
				throw new YappRuntimeException("CHECK_MSG", chkMsg);	
			}
		} catch (Exception e){
			//e.printStackTrace();
			logger.error("API : /event/giftOfflineJoin, ERROR [경품참여 불가] " + e);
			if("".equals(chkMsg)){
				throw new YappException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_GIFT"));
			}else{
				throw new YappException("CHECK_MSG", chkMsg);	
			}
		}
		return new ResultInfo<>(); 
	}
	
	/***
	 * 이벤트 오프라인 복수 경품 참여 기능 구현 2023.06.16 6월과제로 추가함..
	 * @param eventGiftMultiReq
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="이벤트 오프라인 복수 경품참여")
	@ResponseBody
	@RequestMapping(value = "/event/giftOfflineJoinMulti", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	//@NotCheckLogin
	public ResultInfo<String> eventGiftOfflineJoinMulti(@RequestBody EventGiftMultiReq eventGiftMultiReq, HttpServletRequest req) throws Exception {
		//로그인 여부
		String apiUrl = YappUtil.nToStr(req.getRequestURI());
		String loginYn = loginCheck(req, apiUrl);
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		String memStatus = "";
		String loginUserId = "";
		String credentialIde = "";
		
		if(SessionKeeper.getSdata(req) != null){
			memStatus = SessionKeeper.getSdata(req).getMemStatus();
			loginUserId = SessionKeeper.getSdata(req).getUserId();
			credentialIde = SessionKeeper.getSdata(req).getCredentialId();
		}
		
		// 로컬 테스트 위한 소스(로컬인 경우에만 플로우를 탐) - S
		String profileInfo = environment.getActiveProfiles()[0];
		if("local".equals(profileInfo)){
			loginYn = "Y";
			memStatus = getDevUserInfo().get("memStatus");
			loginCntrNo = getDevUserInfo().get("loginCntrNo");
			loginUserId = getDevUserInfo().get("loginUserId");
		}
		// -E
		
		logger.info("==============================================================");
		logger.info("/event/giftOfflineJoin -> memStatus       : "+memStatus);
		logger.info("/event/giftOfflineJoin -> loginCntrNo     : "+loginCntrNo);
		logger.info("/event/giftOfflineJoin -> loginUserId     : "+loginUserId);
		logger.info("==============================================================");
		
		
		String chkMsg = "";
		try{
			//준회원일때 회선번호 확인 후 다르거나 없다면 update
			if(memStatus.equals("G0003")){
				setCheckUserMobileNo(loginUserId, YappUtil.isEmpty(credentialIde) ? "" : credentialIde, YappUtil.isEmpty(eventGiftMultiReq.getEventGiftJoinList().get(0).getRecvMobileNo()) ? "" : eventGiftMultiReq.getEventGiftJoinList().get(0).getRecvMobileNo());
			}
			
			// 이벤트 참여 관련 로직 별도로 뺌 한번만 저장하기위해 - S
			EventAppl appl = new EventAppl();
			appl.setCntrNo(loginCntrNo);
			appl.setUserId(loginUserId);
			appl.setWinYn("N");
			List<EventGiftJoin> eventGiftJoinList = eventGiftMultiReq.getEventGiftJoinList();
			EventMaster eventMaster = eventService.getEventDetailMaster(eventGiftMultiReq.getEvtSeq());
			// -E
			
			// 선착순 경품 인경우 부분소진 / 전체 소진 체크 로직 추가 - Start
			if("O0002".equals(eventMaster.getGiftOfferType())){
				List<Integer> issueSeqList = eventGiftJoinList.stream().map(EventGiftJoin::getIssueSeq).collect(Collectors.toList());
				List<Integer> remainTotCnt = eventService.getEventRemainGiftTotCountByIssueSeq(issueSeqList);
				Integer evtTotGiftRemCnt = eventService.getEventRemainGiftTotCountByOrder(eventGiftMultiReq.getEvtSeq());
				int seqEmtCnt = (int)remainTotCnt.stream().filter(c -> c == 0).count();

				if(seqEmtCnt > 0 && issueSeqList.size() >= seqEmtCnt && evtTotGiftRemCnt > 0){//1) 사용자가 선택한건 중에 한건이라도 소진된 케이스 가 있는 경우 2) 사용자가선택한 건은 소진되고 전체 이벤트의 다른 상품은 존재하는 경우.

					chkMsg = cmnService.getMsg("CHK_Y_FULL_GIFT");
					throw new YappRuntimeException("CHECK_MSG", chkMsg);

				} else if(evtTotGiftRemCnt == 0){ // 이벤트 전체 상품이 소진된경우

					chkMsg = cmnService.getMsg("CHK_Y_FULL_GIFT2");
					throw new YappRuntimeException("CHECK_MSG", chkMsg);

				}
			}// - End
			// 복수의 경품 선택 위해 루프 구조를 추가함...
			for(EventGiftJoin eventGiftJoin : eventGiftJoinList){
				//이벤트 참여 횟수 조회
				int eventJoinCount = eventService.getEventJoinCount(eventGiftJoin.getEvtSeq(), loginCntrNo, loginUserId);
				if(eventJoinCount > eventGiftMultiReq.getGiftMaxCnt()){
					chkMsg = cmnService.getMsg("CHK_Y_NO_GIFT");
					throw new YappRuntimeException("CHECK_MSG", chkMsg);
				}
				
				//이벤트 종료여부 체크
				EventMaster eventDetail = eventService.getEventDetailMaster(eventGiftJoin.getEvtSeq());
				if(eventDetail  == null || "Y".equals(eventDetail.getEndYn())){
					chkMsg = cmnService.getMsg("CHK_Y_NO_GIFT");
					throw new YappRuntimeException("CHECK_MSG", chkMsg);
				}
				
				String giftOfferType = eventDetail.getGiftOfferType();
				
				
				if(eventGiftJoin.getIssueSeq() == 0){
					chkMsg = cmnService.getMsg("CHK_Y_FULL_GIFT2");
					throw new YappRuntimeException("CHECK_MSG", chkMsg);
				}
				// 오프라인 경품일때..
				if("G0001".equals(eventGiftJoin.getGiftType())){
					
					//경품 잔여수량 체크
					if("C0001".equals(eventDetail.getGiftChoiceType()) && "O0002".equals(eventDetail.getGiftOfferType())){
						int remainCnt = eventService.getEventRemainGiftCount(eventGiftJoin.getIssueSeq());
						if(remainCnt == 0){
							chkMsg = cmnService.getMsg("CHK_Y_FULL_GIFT2");
							throw new YappRuntimeException("CHECK_MSG", chkMsg);
						}
					}

					appl.setEvtSeq(eventGiftJoin.getEvtSeq());
					if("O0002".equals(giftOfferType)){
						//선착순일때
						eventGiftJoin.setCntrNo(loginCntrNo);
						eventGiftJoin.setUserId(loginUserId);
						int updateCnt = eventService.updateGiftIssueInfo(eventGiftJoin);
						if(updateCnt == 0){
							chkMsg = cmnService.getMsg("CHK_Y_NO_GIFT");
							throw new YappRuntimeException("CHECK_MSG", chkMsg);
						}
						eventGiftJoin.setJoinSeq(0);
						eventGiftJoin.setRecvCntrNo(loginCntrNo);
						eventGiftJoin.setRecvUserId(loginUserId);
						eventGiftJoin.setWinYn("Y");
						eventService.insertEntryInfo(eventGiftJoin);
						appl.setWinYn("Y");
						
					}else if("O0003".equals(giftOfferType)){
						//추첨일때
						eventGiftJoin.setJoinSeq(0);
						eventGiftJoin.setRecvCntrNo(loginCntrNo);
						eventGiftJoin.setRecvUserId(loginUserId);
						eventGiftJoin.setWinYn("N");
						//eventGiftJoin.setRecvMobileNo(loginMobileNo);
						//eventGiftJoin.setRecvName(YappUtil.blindNameToName(cntrInfo.getUserNm(), 1));
						eventService.insertEntryInfo(eventGiftJoin);
					}else{
						chkMsg = cmnService.getMsg("CHK_Y_NO_GIFT");
						throw new YappRuntimeException("CHECK_MSG", chkMsg);
					}
					

				} else if("O0003".equals(giftOfferType) && "G0002".equals(eventGiftJoin.getGiftType())) { // 온라인 경품일때..

					//Y프렌즈일때 인원수별 경품 잔여수량 체크
					if(eventDetail.getEvtType().equals("G0004")){
						boolean bRet = giftRewardTargetCheck(eventDetail.getEvtSeq(), loginCntrNo);

						if(bRet==false){
							chkMsg = cmnService.getMsg("CHK_Y_FULL_GIFT2");
							throw new YappRuntimeException("CHECK_MSG", chkMsg);
						}
					}else{
						if("C0001".equals(eventDetail.getGiftChoiceType()) && "O0002".equals(eventDetail.getGiftOfferType())){
							int remainCnt = eventService.getEventRemainGiftCount(eventGiftJoin.getIssueSeq());
							if(remainCnt == 0){
								chkMsg = cmnService.getMsg("CHK_Y_FULL_GIFT2");
								throw new YappRuntimeException("CHECK_MSG", chkMsg);
							}
						}
					}

					appl.setEvtSeq(eventGiftJoin.getEvtSeq());
					appl.setWinYn("N");

					//추첨일때
					eventGiftJoin.setJoinSeq(0);
					eventGiftJoin.setRecvCntrNo(loginCntrNo);
					eventGiftJoin.setRecvUserId(loginUserId);
					eventGiftJoin.setWinYn("N");
					//eventGiftJoin.setRecvMobileNo(loginMobileNo);
					//eventGiftJoin.setRecvName(YappUtil.blindNameToName(cntrInfo.getUserNm(), 1));
					eventService.insertEntryInfo(eventGiftJoin);
				}
		
				
			}
			
			// 이벤트 응모
			cmsService.applEventMaster(appl);
			
		} catch (RuntimeException e){
			//e.printStackTrace();
			logger.error("API : /event/giftOfflineJoin, ERROR [경품참여 불가] " + e);
			if("".equals(chkMsg)){
				throw new YappRuntimeException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_GIFT"));
			}else{
				throw new YappRuntimeException("CHECK_MSG", chkMsg);	
			}
		} catch (Exception e){
			//e.printStackTrace();
			logger.error("API : /event/giftOfflineJoin, ERROR [경품참여 불가] " + e);
			if("".equals(chkMsg)){
				throw new YappException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_GIFT"));
			}else{
				throw new YappException("CHECK_MSG", chkMsg);	
			}
		}
		return new ResultInfo<>(); 
	}
	
	
	@ApiOperation(value="이벤트 데이터 상품 경품참여")
	@ResponseBody
	@RequestMapping(value = "/event/giftDataCpJoin", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<EventGift> eventGiftDataCpJoin(EventGiftJoin eventGiftJoin, HttpServletRequest req) throws Exception {
		//로그인 여부
		String apiUrl = YappUtil.nToStr(req.getRequestURI());
		String loginYn = loginCheck(req, apiUrl);
		
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		String memStatus = "";
		String loginUserId = "";
		
		if(SessionKeeper.getSdata(req) != null){
			memStatus = SessionKeeper.getSdata(req).getMemStatus();
			loginUserId = SessionKeeper.getSdata(req).getUserId();
		}
		
		logger.info("==============================================================");
		logger.info("/event/giftDataCpJoin -> memStatus       : "+memStatus);
		logger.info("/event/giftDataCpJoin -> loginCntrNo     : "+loginCntrNo);
		logger.info("/event/giftDataCpJoin -> loginUserId     : "+loginUserId);
		logger.info("==============================================================");
		
		String chkMsg = "";
		ResultInfo<EventGift> resultInfo = new ResultInfo<>();
		EventGift eventGift = new EventGift();
		List<EventGift> eventGiftList = new ArrayList<>();
		try{
			//경품 소진 이미치 추출용 쿼리 실행
			eventGiftList = eventService.getEventDetailGiftList(eventGiftJoin.getEvtSeq(), eventGiftJoin.getGiftSeq());
			resultInfo.setResultData(eventGiftList.get(0));
			
			//이벤트 참여 횟수 조회
			int eventJoinCount = eventService.getEventJoinCount(eventGiftJoin.getEvtSeq(), loginCntrNo, loginUserId);
			if(eventJoinCount > 0){
				chkMsg = cmnService.getMsg("CHK_Y_NO_GIFT");
				throw new YappRuntimeException("CHECK_MSG", chkMsg);
			}
			
			//이벤트 종료여부 체크
			EventMaster eventDetail = eventService.getEventDetailMaster(eventGiftJoin.getEvtSeq());
			if(eventDetail  == null || "Y".equals(eventDetail.getEndYn())){
				chkMsg = cmnService.getMsg("CHK_Y_NO_GIFT");
				throw new YappRuntimeException("CHECK_MSG", chkMsg);
			}
			
			//경품 잔여수량 체크
			if(eventGiftJoin.getIssueSeq() == 0){
				chkMsg = cmnService.getMsg("CHK_Y_FULL_GIFT2");
				resultInfo.setResultCd("500");
				resultInfo.setResultMsg(chkMsg);
				return resultInfo;
			}
			
			//인원수별 경품 잔여수량 체크
			boolean bRet = giftRewardTargetCheck(eventDetail.getEvtSeq(), loginCntrNo);
			
			if(bRet==false){				
				chkMsg = cmnService.getMsg("CHK_Y_FULL_GIFT2");
				resultInfo.setResultCd("500");
				resultInfo.setResultMsg(chkMsg);
				return resultInfo;
			}
			
			EventAppl appl = new EventAppl();
			appl.setEvtSeq(eventGiftJoin.getEvtSeq());
			appl.setCntrNo(loginCntrNo);
			appl.setUserId(loginUserId);
			appl.setWinYn("N");
			String giftOfferType = eventDetail.getGiftOfferType();
			if("O0002".equals(giftOfferType)){
				//선착순일때
				int rewardCnt = eventService.getRewardCount(eventGiftJoin.getIssueSeq(), loginCntrNo, loginUserId);
				if(rewardCnt > 0){
					chkMsg = cmnService.getMsg("ERR_EXST_APPL_EVENT");
					throw new YappRuntimeException("CHECK_MSG", chkMsg);
				}
				
				//210913
				int remainCnt = eventService.getEventRemainGiftCount(eventGiftJoin.getIssueSeq());
				if(remainCnt == 0){
					chkMsg = cmnService.getMsg("CHK_Y_FULL_GIFT2");
					throw new YappException("CHECK_MSG", chkMsg);
				}else{
					
					eventGiftJoin.setCntrNo(loginCntrNo);
					eventGiftJoin.setUserId(loginUserId);
					
					logger.info("setGiftSeq : " +eventGiftJoin.getGiftSeq());
					
					//데이터 상품 업데이트
					int dataRewSeq = eventService.selectEventDataCp(eventGiftJoin.getGiftSeq(), eventGiftJoin.getIssueSeq());
					int rewardUpdCnt = eventService.updateEventDataCp(dataRewSeq, eventGiftJoin.getIssueSeq(), loginCntrNo);
					
					if(rewardUpdCnt > 1){
						eventGift = eventService.getMyEventGiftDataCpInfo(eventGiftJoin);
						resultInfo.setResultData(eventGift);
						appl.setWinYn("Y");
					}else{
						chkMsg = cmnService.getMsg("CHK_Y_FULL_GIFT2");
						throw new YappRuntimeException("CHECK_MSG", chkMsg);
					}
					
				}

				// 이벤트 응모
				cmsService.applEventMaster(appl);
				
			}
			logger.info("데이터 상품 완료");
			
		} catch (RuntimeException e){
			//e.printStackTrace();
			logger.error("API : /event/giftDataCpJoin, ERROR [경품참여 불가] " + e);
			if("".equals(chkMsg)){
				throw new YappRuntimeException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_GIFT"));
			}else{
				throw new YappRuntimeException("CHECK_MSG", chkMsg);	
			}	
		}catch (Exception e){
			//e.printStackTrace();
			logger.error("API : /event/giftDataCpJoin, ERROR [경품참여 불가] " + e);
			if("".equals(chkMsg)){
				throw new YappRuntimeException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_GIFT"));
			}else{
				throw new YappRuntimeException("CHECK_MSG", chkMsg);	
			}	
		}
		return resultInfo; 
	}
	
	@ApiOperation(value="이벤트 Y캔버스(수강)상품 참여")
	@ResponseBody
	@RequestMapping(value = "/event/yCanvasRewardJoin", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<EventGift> eventGiftYCanvasJoin(EventGiftJoin eventGiftJoin, HttpServletRequest req) throws Exception {
		//로그인 여부
		String apiUrl = YappUtil.nToStr(req.getRequestURI());
		String loginYn = loginCheck(req, apiUrl);
		
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		String memStatus = "";
		String loginUserId = "";
		
		if(SessionKeeper.getSdata(req) != null){
			memStatus = SessionKeeper.getSdata(req).getMemStatus();
			loginUserId = SessionKeeper.getSdata(req).getUserId();
		}
		
		//로컬 테스트용(서버올릴땐 삭제)
		/*loginYn = "Y";
		memStatus = "G0001";
		loginCntrNo = "625231423";
		loginUserId = "ydbox16";*/
		
		logger.info("==============================================================");
		logger.info("/event/yCanvasRewardJoin -> memStatus       : "+memStatus);
		logger.info("/event/yCanvasRewardJoin -> loginCntrNo     : "+loginCntrNo);
		logger.info("/event/yCanvasRewardJoin -> loginUserId     : "+loginUserId);
		logger.info("==============================================================");
		
		String chkMsg = "";
		ResultInfo<EventGift> resultInfo = new ResultInfo<>();
		
		try{
			//이벤트 종료여부 체크
			EventMaster eventDetail = eventService.getEventDetailMaster(eventGiftJoin.getEvtSeq());
			if(eventDetail  == null || "Y".equals(eventDetail.getEndYn())){
				chkMsg = cmnService.getMsg("CHK_Y_NO_CLASS");
				throw new YappException("CHECK_MSG", chkMsg);
			}
			
			if(eventGiftJoin.getIssueSeq() == 0){
				chkMsg = cmnService.getMsg("CHK_Y_NO_CLASS_FULL");
				throw new YappException("CHECK_MSG", chkMsg);
			}
			
			//경품 잔여수량 체크
			if("C0001".equals(eventDetail.getGiftChoiceType())){
				int remainCnt = eventService.getEventRemainGiftCount(eventGiftJoin.getIssueSeq());
				if(remainCnt == 0){
					chkMsg = cmnService.getMsg("CHK_Y_NO_CLASS_FULL");
					throw new YappException("CHECK_MSG", chkMsg);
				}
			}
			
			EventAppl appl = new EventAppl();
			appl.setEvtSeq(eventGiftJoin.getEvtSeq());
			appl.setCntrNo(loginCntrNo);
			appl.setUserId(loginUserId);
			appl.setWinYn("N");
			

			int rewardCnt = canvasService.getYCanvasJoinCount(eventGiftJoin.getEvtSeq(), loginCntrNo, loginUserId);//인원별 전체 수강 상품 지급 카운트
			int remainCnt = eventService.getEventRemainGiftCount(eventGiftJoin.getIssueSeq());//특정 수강 상품 전체 수량
			
			//기존 수강 상품 정보를 가져온다.
			List<EventGift> eventGiftList = eventService.getEventDetailGiftList(eventGiftJoin.getEvtSeq(), eventGiftJoin.getGiftSeq());
			eventGiftJoin.setOverlapCheckDate(eventGiftList.get(0).getValidStartDt());
			eventGiftJoin.setCntrNo(loginCntrNo);
			eventGiftJoin.setUserId(loginUserId);
				
			//전체 수강 상품이 최대 수강 인원과 같거나 큰경우
			if(rewardCnt >= eventGiftJoin.getMaxClassCnt()){
				chkMsg = cmnService.getMsg("CHK_Y_NO_CLASS_JOIN").replace("@targetCnt@", Integer.toString(eventGiftJoin.getMaxClassCnt()));
				resultInfo.setResultCd("500");
				resultInfo.setResultMsg(chkMsg);
				return resultInfo;
			}
				
			//새로 신청할 수강상품의 수량이 없을 경우
			if(remainCnt == 0){
				chkMsg = cmnService.getMsg("CHK_Y_FULL_GIFT2");
				resultInfo.setResultCd("500");
				resultInfo.setResultMsg(chkMsg);
				return resultInfo;
			}else{
					
				YcanvasItem paramObj = new YcanvasItem();
				paramObj.setRewSeq(eventGiftJoin.getRewSeq());
				paramObj.setCntrNo(loginCntrNo);
				paramObj.setUserId(loginUserId);
				paramObj.setGiftSeq(eventGiftJoin.getGiftSeq());
				paramObj.setIssueSeq(eventGiftJoin.getIssueSeq());
				paramObj.setJoinSeq(eventGiftJoin.getJoinSeq());
					
				//상품 수량 증감
				int updateCnt = eventService.updateGiftIssueInfo(eventGiftJoin);
				if(updateCnt == 0){
					chkMsg = cmnService.getMsg("CHK_Y_NO_GIFT");
					throw new YappException("CHECK_MSG", chkMsg);
				}
					
				//신규수강 등록
				int result = canvasService.insertYCanvasJoin(paramObj);
					
				if(2 > result){
					chkMsg = cmnService.getMsg("CHK_Y_NO_GIFT");
					throw new YappException("CHECK_MSG", chkMsg);
				}
					
				logger.info("setGiftSeq : " +eventGiftJoin.getGiftSeq());
			}

			// 이벤트 응모
			cmsService.applEventMaster(appl);

			//230331 Y캔버스 수강 정보
			List<YcanvasItem> canvasItemList = canvasService.getYcanvasItemList(eventGiftJoin.getEvtSeq(), null, null, loginCntrNo, loginUserId);
			EventGift eventGift = new EventGift();
			eventGift.setYcanvasItem(canvasItemList);
			resultInfo.setResultData(eventGift);
			
			logger.info("수강 상품 완료");
			resultInfo.setResultCd("200");
			
		} catch (RuntimeException e){
			//e.printStackTrace();
			logger.error("API : /event/yCanvasRewardJoin, ERROR [수강참여 불가] " + e);
			if("".equals(chkMsg)){
				throw new YappRuntimeException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_GIFT"));
			}else{
				throw new YappRuntimeException("CHECK_MSG", chkMsg);	
			}	
		}catch (Exception e){
			//e.printStackTrace();
			logger.error("API : /event/yCanvasRewardJoin, ERROR [수강참여 불가] " + e);
			if("".equals(chkMsg)){
				throw new YappRuntimeException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_GIFT"));
			}else{
				throw new YappRuntimeException("CHECK_MSG", chkMsg);	
			}	
		}
		return resultInfo; 
	}
	
	@ApiOperation(value="출석체크 하기")
	@ResponseBody
	@RequestMapping(value = "/event/attendJoin", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<String> eventAttendJoin(int evtSeq, HttpServletRequest req) throws Exception {
		//로그인 여부
		String apiUrl = YappUtil.nToStr(req.getRequestURI());
		String loginYn = loginCheck(req, apiUrl);
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		String memStatus = "";
		String loginUserId = "";
		
		if(SessionKeeper.getSdata(req) != null){
			memStatus = SessionKeeper.getSdata(req).getMemStatus();
			loginUserId = SessionKeeper.getSdata(req).getUserId();
		}
		
		logger.info("==============================================================");
		logger.info("/event/attendJoin -> memStatus       : "+memStatus);
		logger.info("/event/attendJoin -> loginCntrNo     : "+loginCntrNo);
		logger.info("/event/attendJoin -> loginUserId     : "+loginUserId);
		logger.info("==============================================================");
		
		//로컬 테스트용(서버올릴땐 삭제)
		//loginCntrNo = "524511905";
		
		try{
			if(eventService.getEventAttendNowChk(evtSeq, loginCntrNo, loginUserId) > 0){
				throw new YappRuntimeException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_ATTEND"));	
			}
			AttendDayCheck attendDayCheck = new AttendDayCheck();
			attendDayCheck.setEvtSeq(evtSeq);
			attendDayCheck.setCntrNo(loginCntrNo);
			attendDayCheck.setUserId(loginUserId);
			eventService.insertAttendDayCheck(attendDayCheck);
			
		} catch (RuntimeException e){
			//e.printStackTrace();
			logger.error("API : /event/attendJoin, ERROR [출석체크 불가] " + e);
			throw new YappRuntimeException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_ATTEND"));	
		} catch (Exception e){
			//e.printStackTrace();
			logger.error("API : /event/attendJoin, ERROR [출석체크 불가] " + e);
			throw new YappException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_ATTEND"));	
		}
		return new ResultInfo<>(); 
	}
	
	@ApiOperation(value="출석체크(리워드경품) 하기")
	@ResponseBody
	@RequestMapping(value = "/event/attendRewardJoin", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<EventGift> eventAttendRewardJoin(EventGiftJoin eventGiftJoin, HttpServletRequest req) throws Exception {
		//로그인 여부
		String apiUrl = YappUtil.nToStr(req.getRequestURI());
		String loginYn = loginCheck(req, apiUrl);
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		String memStatus = "";
		String loginUserId = "";
		
		if(SessionKeeper.getSdata(req) != null){
			memStatus = SessionKeeper.getSdata(req).getMemStatus();
			loginUserId = SessionKeeper.getSdata(req).getUserId();
		}
		
		logger.info("==============================================================");
		logger.info("/event/attendRewardJoin -> memStatus       : "+memStatus);
		logger.info("/event/attendRewardJoin -> loginCntrNo     : "+loginCntrNo);
		logger.info("/event/attendRewardJoin -> loginUserId     : "+loginUserId);
		logger.info("==============================================================");
		
		//로컬 테스트용(서버올릴땐 삭제)
		//loginCntrNo = "524511905";
		
		ResultInfo<EventGift> resultInfo = new ResultInfo<>();
		EventGift eventGift = new EventGift();
		
		String chkMsg = "";
		
		try{
			//이벤트 참여 횟수 조회
			int eventJoinCount = eventService.getAttendEventJoinCount(eventGiftJoin.getEvtSeq(), loginCntrNo, loginUserId);
			
			//210811 응모권시 해당 주석 풀기
			//int eventJoinCount = eventService.getAttendTicketEventJoinCount(eventGiftJoin.getEvtSeq(), loginCntrNo);
			
			if(eventJoinCount > 0){
				chkMsg = cmnService.getMsg("CHK_Y_NO_GIFT");
				throw new YappException("CHECK_MSG", chkMsg);
			}
			
			//이벤트 종료여부 체크
			EventMaster eventDetail = eventService.getEventDetailMaster(eventGiftJoin.getEvtSeq());
			if(eventDetail  == null || "Y".equals(eventDetail.getEndYn())){
				chkMsg = cmnService.getMsg("CHK_Y_NO_GIFT");
				throw new YappException("CHECK_MSG", chkMsg);
			}
			
			//경품 잔여수량 체크
			if(eventGiftJoin.getIssueSeq() == 0){
				chkMsg = cmnService.getMsg("CHK_Y_FULL_GIFT");
				throw new YappException("CHECK_MSG", chkMsg);
			}
			
			if(eventService.getEventAttendNowChk(eventGiftJoin.getEvtSeq(), loginCntrNo, loginUserId) > 0){
				chkMsg = cmnService.getMsg("CHK_Y_NO_ATTEND");
				throw new YappException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_ATTEND"));	
			}
			
			EventAppl appl = new EventAppl();
			appl.setEvtSeq(eventGiftJoin.getEvtSeq());
			appl.setCntrNo(loginCntrNo);
			appl.setUserId(loginUserId);
			appl.setWinYn("N");
			String giftOfferType = eventDetail.getGiftOfferType();
			
			AttendDayCheck attendDayCheck = new AttendDayCheck();
			attendDayCheck.setEvtSeq(eventGiftJoin.getEvtSeq());
			attendDayCheck.setCntrNo(loginCntrNo);
			attendDayCheck.setUserId(loginUserId);
			
			if("C0001".equals(eventDetail.getGiftChoiceType()) && "O0002".equals(eventDetail.getGiftOfferType())){
				//선택형이고 선착순일때
				int remainCnt = eventService.getEventRemainGiftCount(eventGiftJoin.getIssueSeq());
				
				if(remainCnt == 0){
					int remainTotCnt = eventService.getEventRemainGiftTotCount(eventGiftJoin.getIssueSeq());
					if(remainTotCnt > 0){
						//선택한 상품은 수량이 없지만 선택한 일자의 상품의 수량이 있는 상품이 있을때는 다시 선택하게 유도
						chkMsg = cmnService.getMsg("CHK_Y_FULL_GIFT");
						throw new YappException("CHECK_MSG", chkMsg);
					}else{
						//모든 상품의 잔여수량이 없을 경우에는 출석체크만 하고 팝업으로 공지
						eventGift.setOnlyAttend("Y");
						resultInfo.setResultData(eventGift);
						eventService.insertAttendDayCheck(attendDayCheck);
					}
				}else{
					//선착순일때
					eventService.insertAttendDayCheck(attendDayCheck);
					
					EventGiftJoin paramObj1 = new EventGiftJoin();
					paramObj1.setIssueSeq(eventGiftJoin.getIssueSeq());
					paramObj1.setCntrNo(loginCntrNo);
					paramObj1.setUserId(loginUserId);
					paramObj1.setEvtSeq(eventGiftJoin.getEvtSeq());
					paramObj1.setGiftSeq(eventGiftJoin.getGiftSeq());
					int rewSeq = eventService.getAttendRewardSeq(paramObj1);
					if(rewSeq == 0){
						eventGift.setOnlyAttend("Y");
						resultInfo.setResultData(eventGift);
					}else{
						eventGiftJoin.setCntrNo(loginCntrNo);
						int updateCnt = eventService.updateGiftIssueInfo(eventGiftJoin);
						if(updateCnt == 0){
							chkMsg = cmnService.getMsg("CHK_Y_NO_GIFT");
							throw new YappException("CHECK_MSG", chkMsg);
						}
						
						eventGiftJoin.setRewSeq(rewSeq);
						eventService.updateRewardInfo(eventGiftJoin);
						/*if(updateCnt2 == 0){
							chkMsg = cmnService.getMsg("CHK_Y_NO_GIFT");
							throw new YappException("CHECK_MSG", chkMsg);
						}*/
						eventGift = eventService.getMyEventAttendGiftInfo(eventGiftJoin);
						resultInfo.setResultData(eventGift);
						appl.setWinYn("Y");
					}
					/*int rewardCnt = eventService.getRewardCount(eventGiftJoin.getIssueSeq(), loginCntrNo);
					if(rewardCnt > 0){
						chkMsg = cmnService.getMsg("ERR_EXST_ATT_APPL_EVENT");
						throw new YappRuntimeException("CHECK_MSG", chkMsg);
					}*/
					
				}
			}else if("O0002".equals(giftOfferType)){
				//선착순일때
				eventService.insertAttendDayCheck(attendDayCheck);
				
				EventGiftJoin paramObj1 = new EventGiftJoin();
				paramObj1.setIssueSeq(eventGiftJoin.getIssueSeq());
				paramObj1.setCntrNo(loginCntrNo);
				paramObj1.setUserId(loginUserId);
				paramObj1.setEvtSeq(eventGiftJoin.getEvtSeq());
				paramObj1.setGiftSeq(eventGiftJoin.getGiftSeq());
				int rewSeq = eventService.getAttendRewardSeq(paramObj1);
				if(rewSeq == 0){
					eventGift.setOnlyAttend("Y");
					resultInfo.setResultData(eventGift);
				}else{
					eventGiftJoin.setCntrNo(loginCntrNo);
					int updateCnt = eventService.updateGiftIssueInfo(eventGiftJoin);
					if(updateCnt == 0){
						chkMsg = cmnService.getMsg("CHK_Y_NO_GIFT");
						throw new YappException("CHECK_MSG", chkMsg);
					}
					
					eventGiftJoin.setRewSeq(rewSeq);
					eventService.updateRewardInfo(eventGiftJoin);
					/*if(updateCnt2 == 0){
						chkMsg = cmnService.getMsg("CHK_Y_NO_GIFT");
						throw new YappException("CHECK_MSG", chkMsg);
					}*/
					eventGift = eventService.getMyEventAttendGiftInfo(eventGiftJoin);
					resultInfo.setResultData(eventGift);
					appl.setWinYn("Y");
				}
				
			}else if("O0003".equals(giftOfferType)){
				//추첨일때
				eventService.insertAttendDayCheck(attendDayCheck);
				
				eventGiftJoin.setJoinSeq(0);
				eventGiftJoin.setRecvCntrNo(loginCntrNo);
				eventGiftJoin.setRecvUserId(loginUserId);
				eventGiftJoin.setWinYn("N");
				eventService.insertEntryInfo(eventGiftJoin);
				resultInfo.setResultData(eventGift);
			}else{
				chkMsg = cmnService.getMsg("CHK_Y_NO_GIFT");
				throw new YappException("CHECK_MSG", chkMsg);
			}
			
			// 이벤트 응모
			cmsService.applEventMaster(appl);
			
		} catch (RuntimeException e){
			//e.printStackTrace();
			logger.error("API : /event/attendRewardJoin, ERROR [출석체크 불가] " + e);
			if("".equals(chkMsg)){
				throw new YappException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_ATTEND"));	
			}else{
				throw new YappException("CHECK_MSG", chkMsg);	
			}
			
		} catch (Exception e){
			//e.printStackTrace();
			logger.error("API : /event/attendRewardJoin, ERROR [출석체크 불가] " + e);
			if("".equals(chkMsg)){
				throw new YappException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_ATTEND"));	
			}else{
				throw new YappException("CHECK_MSG", chkMsg);	
			}
			
		}
		return resultInfo;
	}
	
	/**
	 * 20210811 응모권 출석체크 리워드 추가
	 * @param eventGiftJoin
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="출석체크(응모권리워드) 하기")
	@ResponseBody
	@RequestMapping(value = "/event/attendTicketRewardJoin", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<EventGift> eventAttendTicketRewardJoin(EventGiftJoin eventGiftJoin, HttpServletRequest req) throws Exception {
		//로그인 여부
		String apiUrl = YappUtil.nToStr(req.getRequestURI());
		String loginYn = loginCheck(req, apiUrl);
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		String memStatus = "";
		String loginUserId = "";
		
		if(SessionKeeper.getSdata(req) != null){
			memStatus = SessionKeeper.getSdata(req).getMemStatus();
			loginUserId = SessionKeeper.getSdata(req).getUserId();
		}
		
		logger.info("==============================================================");
		logger.info("/event/attendTicketRewardJoin -> memStatus       : "+memStatus);
		logger.info("/event/attendTicketRewardJoin -> loginCntrNo     : "+loginCntrNo);
		logger.info("/event/attendTicketRewardJoin -> loginUserId     : "+loginUserId);
		logger.info("==============================================================");
		
		//로컬 테스트용(서버올릴땐 삭제)
		//loginCntrNo = "524511905";
		
		ResultInfo<EventGift> resultInfo = new ResultInfo<>();
		EventGift eventGift = new EventGift();
		
		String chkMsg = "";
		
		try{
			
			//이벤트 참여 횟수 조회
			int eventJoinCount = eventService.getAttendTicketEventJoinCount(eventGiftJoin.getEvtSeq(), loginCntrNo, loginUserId);
			if(eventJoinCount > 0){
				chkMsg = cmnService.getMsg("CHK_Y_NO_GIFT");
				throw new YappException("CHECK_MSG", chkMsg);
			}
			
			//이벤트 종료여부 체크
			EventMaster eventDetail = eventService.getEventDetailMaster(eventGiftJoin.getEvtSeq());
			if(eventDetail  == null || "Y".equals(eventDetail.getEndYn())){
				chkMsg = cmnService.getMsg("CHK_Y_NO_GIFT");
				throw new YappException("CHECK_MSG", chkMsg);
			}
			
			//경품 잔여수량 체크
			if(eventGiftJoin.getIssueSeq() == 0){
				chkMsg = cmnService.getMsg("CHK_Y_FULL_GIFT");
				throw new YappException("CHECK_MSG", chkMsg);
			}
			
			if(eventService.getEventAttendNowChk(eventGiftJoin.getEvtSeq(), loginCntrNo, loginUserId) > 0){
				chkMsg = cmnService.getMsg("CHK_Y_NO_ATTEND");
				throw new YappException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_ATTEND"));	
			}
			
			EventAppl appl = new EventAppl();
			appl.setEvtSeq(eventGiftJoin.getEvtSeq());
			appl.setCntrNo(loginCntrNo);
			appl.setUserId(loginUserId);
			appl.setWinYn("N");
			String giftOfferType = eventDetail.getGiftOfferType();
			
			AttendDayCheck attendDayCheck = new AttendDayCheck();
			attendDayCheck.setEvtSeq(eventGiftJoin.getEvtSeq());
			attendDayCheck.setCntrNo(loginCntrNo);
			attendDayCheck.setUserId(loginUserId);
			
			if("C0001".equals(eventDetail.getGiftChoiceType()) && "O0002".equals(eventDetail.getGiftOfferType())){
				//선택형이고 선착순일때
				int remainCnt = eventService.getEventRemainGiftCount(eventGiftJoin.getIssueSeq());
				
				if(remainCnt == 0){
					int remainTotCnt = eventService.getEventRemainGiftTotCount(eventGiftJoin.getIssueSeq());
					if(remainTotCnt > 0){
						//선택한 상품은 수량이 없지만 선택한 일자의 상품의 수량이 있는 상품이 있을때는 다시 선택하게 유도
						chkMsg = cmnService.getMsg("CHK_Y_FULL_GIFT");
						throw new YappException("CHECK_MSG", chkMsg);
					}else{
						//모든 상품의 잔여수량이 없을 경우에는 출석체크만 하고 팝업으로 공지
						eventGift.setOnlyAttend("Y");
						resultInfo.setResultData(eventGift);
						eventService.insertAttendDayCheck(attendDayCheck);
					}
				}else{
					//선착순일때
					eventService.insertAttendDayCheck(attendDayCheck);
					eventGiftJoin.setCntrNo(loginCntrNo);
					eventGiftJoin.setUserId(loginUserId);
					
					//사용자 응모권 발급
					ticketService.insertEventTicket(eventGiftJoin);
					int updateCnt = eventService.updateGiftIssueInfo(eventGiftJoin);
					if(updateCnt == 0){
						chkMsg = cmnService.getMsg("CHK_Y_NO_GIFT");
						throw new YappException("CHECK_MSG", chkMsg);
					}
					
					//응모권이미지 어디에 적재할지 정해지면 사용
					eventGift = eventService.getMyEventAttendTicketGiftInfo(eventGiftJoin);
					resultInfo.setResultData(eventGift);
					
					appl.setWinYn("Y");
					
				}
			}else if("O0002".equals(giftOfferType)){
				//선착순일때
				eventService.insertAttendDayCheck(attendDayCheck);
				eventGiftJoin.setCntrNo(loginCntrNo);
				eventGiftJoin.setUserId(loginUserId);

				//사용자 응모권 발급
				ticketService.insertEventTicket(eventGiftJoin);
				int updateCnt = eventService.updateGiftIssueInfo(eventGiftJoin);
				if(updateCnt == 0){
					chkMsg = cmnService.getMsg("CHK_Y_NO_GIFT");
					throw new YappException("CHECK_MSG", chkMsg);
				}
				
				//응모권이미지 어디에 적재할지 정해지면 사용
				eventGift = eventService.getMyEventAttendTicketGiftInfo(eventGiftJoin);
				resultInfo.setResultData(eventGift);
				
				appl.setWinYn("Y");
				
				
			}else if("O0003".equals(giftOfferType)){
				//추첨일때
				eventService.insertAttendDayCheck(attendDayCheck);
				
				eventGiftJoin.setJoinSeq(0);
				eventGiftJoin.setRecvCntrNo(loginCntrNo);
				eventGiftJoin.setRecvUserId(loginUserId);
				eventGiftJoin.setWinYn("N");
				eventService.insertEntryInfo(eventGiftJoin);
				
			}else{
				chkMsg = cmnService.getMsg("CHK_Y_NO_GIFT");
				throw new YappException("CHECK_MSG", chkMsg);
			}
			
			// 이벤트 응모
			cmsService.applEventMaster(appl);
			
		} catch (RuntimeException e){
			//e.printStackTrace();
			logger.error("API : /event/attendTicketRewardJoin, ERROR [출석체크 불가] " + e);
			if("".equals(chkMsg)){
				throw new YappException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_ATTEND"));	
			}else{
				throw new YappException("CHECK_MSG", chkMsg);	
			}
			
		} catch (Exception e){
			//e.printStackTrace();
			logger.error("API : /event/attendTicketRewardJoin, ERROR [출석체크 불가] " + e);
			if("".equals(chkMsg)){
				throw new YappException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_ATTEND"));	
			}else{
				throw new YappException("CHECK_MSG", chkMsg);	
			}
			
		}
		return resultInfo;
	}
	
	@ApiOperation(value="출석체크(오프라인 경품) 하기")
	@ResponseBody
	@RequestMapping(value = "/event/attendOfflineJoin", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<EventGift> eventAttendOfflineJoin(EventGiftJoin eventGiftJoin, HttpServletRequest req) throws Exception {
		//로그인 여부
		String apiUrl = YappUtil.nToStr(req.getRequestURI());
		String loginYn = loginCheck(req, apiUrl);
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		String memStatus = "";
		String loginUserId = "";
		
		if(SessionKeeper.getSdata(req) != null){
			memStatus = SessionKeeper.getSdata(req).getMemStatus();
			loginUserId = SessionKeeper.getSdata(req).getUserId();
		}
		
		logger.info("==============================================================");
		logger.info("/event/attendOfflineJoin -> memStatus       : "+memStatus);
		logger.info("/event/attendOfflineJoin -> loginCntrNo     : "+loginCntrNo);
		logger.info("/event/attendOfflineJoin -> loginUserId     : "+loginUserId);
		logger.info("==============================================================");
		
		//로컬 테스트용(서버올릴땐 삭제)
		//loginCntrNo = "524511905";
		
		ResultInfo<EventGift> resultInfo = new ResultInfo<>();
		EventGift eventGift = new EventGift();
		
		String chkMsg = "";
		try{
			//이벤트 참여 횟수 조회
			int eventJoinCount = eventService.getAttendEventJoinCount(eventGiftJoin.getEvtSeq(), loginCntrNo, loginUserId);
			//210811 응모권시 해당 주석 풀기
			//int eventJoinCount = eventService.getAttendTicketEventJoinCount(eventGiftJoin.getEvtSeq(), loginCntrNo);
			
			if(eventJoinCount > 0){
				chkMsg = cmnService.getMsg("CHK_Y_NO_GIFT");
				throw new YappRuntimeException("CHECK_MSG", chkMsg);
			}
			
			//이벤트 종료여부 체크
			EventMaster eventDetail = eventService.getEventDetailMaster(eventGiftJoin.getEvtSeq());
			if(eventDetail  == null || "Y".equals(eventDetail.getEndYn())){
				chkMsg = cmnService.getMsg("CHK_Y_NO_GIFT");
				throw new YappRuntimeException("CHECK_MSG", chkMsg);
			}
			
			//경품 잔여수량 체크
			if(eventGiftJoin.getIssueSeq() == 0){
				chkMsg = cmnService.getMsg("CHK_Y_FULL_GIFT");
				throw new YappRuntimeException("CHECK_MSG", chkMsg);
			}
			
			EventAppl appl = new EventAppl();
			appl.setEvtSeq(eventGiftJoin.getEvtSeq());
			appl.setCntrNo(loginCntrNo);
			appl.setUserId(loginUserId);
			appl.setWinYn("N");
			String giftOfferType = eventDetail.getGiftOfferType();
			
			if("C0001".equals(eventDetail.getGiftChoiceType()) && "O0002".equals(eventDetail.getGiftOfferType())){
				//선택형이고 선착순일때
				int remainCnt = eventService.getEventRemainGiftCount(eventGiftJoin.getIssueSeq());
				if(remainCnt == 0){
					int remainTotCnt = eventService.getEventRemainGiftTotCount(eventGiftJoin.getIssueSeq());
					if(remainTotCnt > 0){
						//선택한 상품은 수량이 없지만 선택한 일자의 상품의 수량이 있는 상품이 있을때는 다시 선택하게 유도
						chkMsg = cmnService.getMsg("CHK_Y_FULL_GIFT");
						throw new YappRuntimeException("CHECK_MSG", chkMsg);
					}else{
						//모든 상품의 잔여수량이 없을 경우에는 출석체크만 하고 팝업으로 공지
						eventGift.setOnlyAttend("Y");
						resultInfo.setResultData(eventGift);
					}
				}else{
					//선착순일때
					eventGiftJoin.setCntrNo(loginCntrNo);
					eventGiftJoin.setUserId(loginUserId);
					int updateCnt = eventService.updateGiftIssueInfo(eventGiftJoin);
					if(updateCnt == 0){
						chkMsg = cmnService.getMsg("CHK_Y_NO_GIFT");
						throw new YappRuntimeException("CHECK_MSG", chkMsg);
					}
					eventGiftJoin.setJoinSeq(0);
					eventGiftJoin.setRecvCntrNo(loginCntrNo);
					eventGiftJoin.setRecvUserId(loginUserId);
					eventGiftJoin.setWinYn("Y");
					eventService.insertEntryInfo(eventGiftJoin);

					appl.setWinYn("Y");
					resultInfo.setResultData(eventGift);
				}
			}else if("O0002".equals(giftOfferType)){
				//선착순일때
				eventGiftJoin.setCntrNo(loginCntrNo);
				eventGiftJoin.setUserId(loginUserId);
				int updateCnt = eventService.updateGiftIssueInfo(eventGiftJoin);
				if(updateCnt == 0){
					chkMsg = cmnService.getMsg("CHK_Y_NO_GIFT");
					throw new YappRuntimeException("CHECK_MSG", chkMsg);
				}
				eventGiftJoin.setJoinSeq(0);
				eventGiftJoin.setRecvCntrNo(loginCntrNo);
				eventGiftJoin.setRecvUserId(loginUserId);
				eventGiftJoin.setWinYn("Y");
				eventService.insertEntryInfo(eventGiftJoin);

				appl.setWinYn("Y");
				resultInfo.setResultData(eventGift);
			}else if("O0003".equals(giftOfferType)){
				//추첨일때
				eventGiftJoin.setJoinSeq(0);
				eventGiftJoin.setRecvCntrNo(loginCntrNo);
				eventGiftJoin.setRecvUserId(loginUserId);
				eventGiftJoin.setWinYn("N");
				//eventGiftJoin.setRecvMobileNo(loginMobileNo);
				//eventGiftJoin.setRecvName(YappUtil.blindNameToName(cntrInfo.getUserNm(), 1));
				eventService.insertEntryInfo(eventGiftJoin);
				resultInfo.setResultData(eventGift);
			}else{
				chkMsg = cmnService.getMsg("CHK_Y_NO_GIFT");
				throw new YappRuntimeException("CHECK_MSG", chkMsg);
			}
			
			if(eventService.getEventAttendNowChk(eventGiftJoin.getEvtSeq(), loginCntrNo, loginUserId) > 0){
				chkMsg = cmnService.getMsg("CHK_Y_NO_ATTEND");
				throw new YappRuntimeException("CHECK_MSG", chkMsg);	
			}
			AttendDayCheck attendDayCheck = new AttendDayCheck();
			attendDayCheck.setEvtSeq(eventGiftJoin.getEvtSeq());
			attendDayCheck.setCntrNo(loginCntrNo);
			attendDayCheck.setUserId(loginUserId);
			eventService.insertAttendDayCheck(attendDayCheck);

			// 이벤트 응모
			cmsService.applEventMaster(appl);
		} catch (RuntimeException e){
			//e.printStackTrace();
			logger.error("API : /event/attendOfflineJoin, ERROR [출석체크 불가] " + e);
			if("".equals(chkMsg)){
				throw new YappRuntimeException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_ATTEND"));	
			}else{
				throw new YappRuntimeException("CHECK_MSG", chkMsg);	
			}
		}catch (Exception e){
			//e.printStackTrace();
			logger.error("API : /event/attendOfflineJoin, ERROR [출석체크 불가] " + e);
			if("".equals(chkMsg)){
				throw new YappException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_ATTEND"));	
			}else{
				throw new YappException("CHECK_MSG", chkMsg);	
			}
		}
		return resultInfo; 
	}
	
	/**
	 * 210610 투표하기
	 * @param req
	 * @param apiUrl
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="투표 하기")
	@ResponseBody
	@RequestMapping(value = "/event/eventVote", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<VoteItem> eventVote(VoteHistory voteData, HttpServletRequest req) throws Exception {
		//로그인 여부
		String apiUrl = YappUtil.nToStr(req.getRequestURI());
		String loginYn = loginCheck(req, apiUrl);
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		String memStatus = "";
		String loginUserId = "";
		
		if(SessionKeeper.getSdata(req) != null){
			memStatus = SessionKeeper.getSdata(req).getMemStatus();
			loginUserId = SessionKeeper.getSdata(req).getUserId();
		}
		
		logger.info("==============================================================");
		logger.info("/event/eventVote -> memStatus       : "+memStatus);
		logger.info("/event/eventVote -> loginCntrNo     : "+loginCntrNo);
		logger.info("/event/eventVote -> loginUserId     : "+loginUserId);
		logger.info("==============================================================");
		
		ResultInfo<VoteItem> resultInfo = new ResultInfo<>();
		List<VoteItem> voteItemList =  new ArrayList<VoteItem>(); //210610 투표대상리스트
		
		try{
			
			/*if(eventService.getVoteChk(voteData.getEvtSeq(), loginCntrNo, loginUserId) != null){
				throw new YappRuntimeException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_VOTE"));	
			}*/
			
			//210628 투표대상의 투표건수 추가
			voteService.updateVoteCnt(voteData);

			voteData.setCntrNo(loginCntrNo);
			voteData.setUserId(loginUserId);
			eventService.insertVoteHistory(voteData);

			//투표리스트
			voteItemList = voteService.getVoteItemList(voteData.getVoteSeq());
			
			 int voteTotal = 0;
			 for(int x = 0; x < voteItemList.size(); x++){
				 voteTotal = voteTotal + voteItemList.get(x).getVoteItemCnt();
			 }
			  
			 for(int y = 0; y < voteItemList.size(); y++){
				 int percent = 0;
				 if(voteItemList.get(y).getVoteItemCnt() > 0 && voteTotal > 0){
					 double per = 0;
					 per = (double) voteItemList.get(y).getVoteItemCnt()/(double)voteTotal;
					 per = per * 100.0;
					 percent = (int) Math.round(per);
					 
				 }
				 voteItemList.get(y).setVotePercent(percent);
			 }
			 resultInfo.setResultInfoList(voteItemList);
			 logger.info("resultInfo : "+resultInfo);
			
		} catch (RuntimeException e){
			logger.error("API : /event/eventVote, ERROR [투표 불가] " + e);
			throw new YappRuntimeException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_VOTE"));	
		} catch (Exception e){
			logger.error("API : /event/eventVote, ERROR [투표 불가] " + e);
			throw new YappException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_VOTE"));	
		}
		return resultInfo; 
	}
	
	/**
	 * 211022 이벤트 온라인(리워드) 다중 경품참여
	 * @param req
	 * @param apiUrl
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="이벤트 온라인(리워드) 다중 경품참여")
	@ResponseBody
	@RequestMapping(value = "/event/giftMultiRewardJoin", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<EventGift> giftMultiRewardJoin(EventGiftJoin eventGiftJoin, HttpServletRequest req) throws Exception {
		//로그인 여부
		String apiUrl = YappUtil.nToStr(req.getRequestURI());
		String loginYn = loginCheck(req, apiUrl);
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		String memStatus = "";
		String loginUserId = "";
		String credentialIde = "";
		
		if(SessionKeeper.getSdata(req) != null){
			memStatus = SessionKeeper.getSdata(req).getMemStatus();
			loginUserId = SessionKeeper.getSdata(req).getUserId();
			credentialIde = SessionKeeper.getSdata(req).getCredentialId();
		}
		//준회원일때 회선번호 확인 후 다르거나 없다면 update
		if(memStatus.equals("G0003")){
			setCheckUserMobileNo(loginUserId, YappUtil.isEmpty(credentialIde) ? "" : credentialIde, YappUtil.isEmpty(eventGiftJoin.getRecvMobileNo()) ? "" : eventGiftJoin.getRecvMobileNo());
		}
		
		logger.info("==============================================================");
		logger.info("/event/giftMultiRewardJoin -> memStatus       : "+memStatus);
		logger.info("/event/giftMultiRewardJoin -> loginCntrNo     : "+loginCntrNo);
		logger.info("/event/giftMultiRewardJoin -> loginUserId     : "+loginUserId);
		logger.info("==============================================================");
		
		String chkMsg = "";
		ResultInfo<EventGift> resultInfo = new ResultInfo<>();
		EventGift eventGift = new EventGift();
		List<EventGift> eventGiftList =  new ArrayList<EventGift>();
		
		try{
			
			// 리워드 경품 목록 구하기
			eventGiftList = eventService.getEventDetailOnlineGiftList(eventGiftJoin.getEvtSeq());
			
			logger.info("eventGiftList size : " + eventGiftList.size());
			
			// 이벤트 참여 횟수 조회
			int eventJoinCount = eventService.getEventJoinCount(eventGiftJoin.getEvtSeq(), loginCntrNo, loginUserId);
			if(eventJoinCount > 0){
				chkMsg = cmnService.getMsg("CHK_Y_NO_GIFT");
				throw new YappRuntimeException("CHECK_MSG", chkMsg);
			}
			
			//이벤트 종료여부 체크
			EventMaster eventDetail = eventService.getEventDetailMaster(eventGiftJoin.getEvtSeq());
			if(eventDetail  == null || "Y".equals(eventDetail.getEndYn())){
				chkMsg = cmnService.getMsg("CHK_Y_NO_GIFT");
				throw new YappRuntimeException("CHECK_MSG", chkMsg);
			}
			
			//Y프렌즈일때 인원수별 경품 잔여수량 체크
			if(eventDetail.getEvtType().equals("G0004")){
				boolean bRet = giftRewardTargetCheck(eventDetail.getEvtSeq(), loginCntrNo);
			
				if(bRet==false){
					chkMsg = cmnService.getMsg("CHK_Y_FULL_GIFT2");
					throw new YappRuntimeException("CHECK_MSG", chkMsg);
				}
			}else{
				// 경품 잔여수량 체크
				for(int i=0; i<eventGiftList.size(); i++){
					
					EventGiftJoin eventOnlineGiftJoin = new EventGiftJoin();
					eventOnlineGiftJoin.setIssueSeq(eventGiftList.get(i).getIssueSeq());
					eventOnlineGiftJoin.setGiftSeq(eventGiftList.get(i).getGiftSeq());
					
					if("C0001".equals(eventDetail.getGiftChoiceType()) && "O0002".equals(eventDetail.getGiftOfferType())){
						int remainCnt = eventService.getEventRemainGiftCount(eventOnlineGiftJoin.getIssueSeq());
						if(remainCnt == 0){
							chkMsg = cmnService.getMsg("CHK_Y_FULL_GIFT2");
							throw new YappRuntimeException("CHECK_MSG", chkMsg);
						}
					}
				}
			}
			
			EventAppl appl = new EventAppl();
			appl.setEvtSeq(eventGiftJoin.getEvtSeq());
			appl.setCntrNo(loginCntrNo);
			appl.setUserId(loginUserId);
			appl.setWinYn("N");
			String giftOfferType = eventDetail.getGiftOfferType();
			
			String REWARD_SUCCESS = "N";
			
			for(int j=0; j<eventGiftList.size(); j++){
				
				// 선착순 검증
				if("O0002".equals(giftOfferType)){
					
					EventGiftJoin eventOnlineGiftJoin = new EventGiftJoin();
					eventOnlineGiftJoin.setIssueSeq(eventGiftList.get(j).getIssueSeq());
					eventOnlineGiftJoin.setGiftSeq(eventGiftList.get(j).getGiftSeq());
					
					logger.info("이벤트 참여 횟수 조회");
					
					// 이벤트 참여 횟수 조회
					int rewardCnt = eventService.getRewardCount(eventOnlineGiftJoin.getIssueSeq(), loginCntrNo, loginUserId);
					
					if(rewardCnt > 0){
						chkMsg = cmnService.getMsg("ERR_EXST_APPL_EVENT");
						throw new YappRuntimeException("CHECK_MSG", chkMsg);
					}
					
					logger.info("이벤트 경품 잔여 수량 조회");
					
					// 이벤트 경품 잔여 수량 조회
					int remainCnt = eventService.getEventRemainGiftCount(eventOnlineGiftJoin.getIssueSeq());
					
					logger.info("remainCnt : " + remainCnt);
					
					if(remainCnt == 0){
						chkMsg = cmnService.getMsg("CHK_Y_FULL_GIFT2");
						throw new YappException("CHECK_MSG", chkMsg);
					}else{
						
						eventOnlineGiftJoin.setCntrNo(loginCntrNo);
						eventOnlineGiftJoin.setUserId(loginUserId);
						
						logger.info("setGiftSeq : " +eventOnlineGiftJoin.getGiftSeq());
						logger.info("setIssueSeq : " +eventOnlineGiftJoin.getIssueSeq());
						logger.info("setCntrNo : " +eventOnlineGiftJoin.getCntrNo());
						logger.info("setUserId : " +eventOnlineGiftJoin.getUserId());
						
						int rewardUpdCnt = eventService.updateEventRewardInfo(eventOnlineGiftJoin);
						
						logger.info("rewardUpdCnt : " + rewardUpdCnt);
						
						if(rewardUpdCnt > 0){
							REWARD_SUCCESS = "Y";
						}else{
							REWARD_SUCCESS = "N";
							chkMsg = cmnService.getMsg("CHK_Y_FULL_GIFT2");
							throw new YappRuntimeException("CHECK_MSG", chkMsg);
						}
						
					}
					
				}
				
			}
			
			logger.info("오프라인 경품(세트 상품 정보) 선착순 처리");
			
			//오프라인 경품(세트 상품 정보) 선착순 처리
			if("O0002".equals(giftOfferType)){
				if("Y".equals(REWARD_SUCCESS)){
					
					eventGiftJoin.setCntrNo(loginCntrNo);
					eventGiftJoin.setUserId(loginUserId);
					
					int updateCnt = eventService.updateGiftIssueInfo(eventGiftJoin);
					
					if(updateCnt == 0){
						chkMsg = cmnService.getMsg("CHK_Y_NO_GIFT");
						throw new YappRuntimeException("CHECK_MSG", chkMsg);
					}
					eventGiftJoin.setJoinSeq(0);
					eventGiftJoin.setRecvCntrNo(loginCntrNo);
					eventGiftJoin.setRecvUserId(loginUserId);
					eventGiftJoin.setWinYn("Y");
					eventService.insertEntryInfo(eventGiftJoin);
					appl.setWinYn("Y");
					
				}
			}
			
			logger.info("이벤트 응모  처리");
			
			// 이벤트 응모
			cmsService.applEventMaster(appl);
			
			// <선착순 for문>
				//이벤트 참여 횟수 조회
				//경품잔여 수량 조회
				//이벤트 경품 리워드 수행
			// </선착순 for문>
			
			//오프라인 경품(세트 상품 정보) 선착순 처리 
			//이벤트 응모 정보 insert 'cmsService.applEventMaster(appl);'


		} catch (RuntimeException e){
			//e.printStackTrace();
			logger.error("API : /event/giftMultiRewardJoin, ERROR [경품참여 불가] " + e);
			if("".equals(chkMsg)){
				throw new YappRuntimeException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_GIFT"));
			}else{
				throw new YappRuntimeException("CHECK_MSG", chkMsg);	
			}	
		}catch (Exception e){
			//e.printStackTrace();
			logger.error("API : /event/giftMultiRewardJoin, ERROR [경품참여 불가] " + e);
			if("".equals(chkMsg)){
				throw new YappRuntimeException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_GIFT"));
			}else{
				throw new YappRuntimeException("CHECK_MSG", chkMsg);	
			}	
		}
		return resultInfo; 
	}
	
	
	/**
	 * 221113 댓글달기
	 * @param req
	 * @param apiUrl
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="댓글 달기")
	@ResponseBody
	@RequestMapping(value = "/event/eventReplyWrite", method = RequestMethod.POST)
	@ApiImplicitParams({
		@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),
		@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), 
		@ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), 
		@ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<EventReply> eventReplyWrite(EventReply eventReply, HttpServletRequest req) throws Exception {
		
		//로그인 여부
		String apiUrl = YappUtil.nToStr(req.getRequestURI());
		String loginYn = loginCheck(req, apiUrl);
		String memStatus = "";
		String mobileNo = "";
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		String loginUserId = "";
		String loginUserNm = "";
		
		if(SessionKeeper.getSdata(req) != null){
			memStatus = SessionKeeper.getSdata(req).getMemStatus();
			mobileNo = SessionKeeper.getSdata(req).getMobileNo();
			loginUserId = SessionKeeper.getSdata(req).getUserId();
		}
		
		logger.info("==============================================================");
		logger.info("/event/eventReplyWrite -> memStatus       : "+memStatus);
		logger.info("/event/eventReplyWrite -> mobileNo        : "+mobileNo);
		logger.info("/event/eventReplyWrite -> loginCntrNo     : "+loginCntrNo);
		logger.info("/event/eventReplyWrite -> loginUserId     : "+loginUserId);
		logger.info("==============================================================");
		
		ResultInfo<EventReply> resultInfo = new ResultInfo<>();
		
		try{
			
			if(YappUtil.isNotEmpty(loginUserId)){
				loginUserNm = YappUtil.blindNameToName(loginUserId, 1);
			}else if(YappUtil.isNotEmpty(mobileNo)){
				loginUserNm = YappUtil.blindMidEndMobileNo(mobileNo);
			}else{
				loginUserNm = "UNKNOWN";
			}
			
			eventReply.setName(loginUserNm);
			eventReply.setCntrNo(loginCntrNo);
			eventReply.setUserId(loginUserId);
			eventService.insertEventReply(eventReply);
			
		} catch (RuntimeException e){
			logger.error("API : /event/eventReplyWrite, ERROR [댓글 등록 불가] " + e);
			throw new YappRuntimeException("CHECK_MSG", cmnService.getMsg("ERR_Y_SYSTEM"));	
		} catch (Exception e){
			logger.error("API : /event/eventReplyWrite, ERROR [댓글 등록 불가] " + e);
			throw new YappException("CHECK_MSG", cmnService.getMsg("ERR_Y_SYSTEM"));	
		}
		return resultInfo; 
	}
	
	
	/**
	 * 221117 댓글수정/삭제
	 * @param req
	 * @param apiUrl
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="댓글 수정/삭제")
	@ResponseBody
	@RequestMapping(value = "/event/eventReplyModify", method = RequestMethod.POST)
	@ApiImplicitParams({
		@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),
		@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), 
		@ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), 
		@ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<EventReply> eventReplyModify(EventReply eventReply, HttpServletRequest req) throws Exception {
		
		//로그인 여부
		String apiUrl = YappUtil.nToStr(req.getRequestURI());
		String loginYn = loginCheck(req, apiUrl);
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		String memStatus = "";
		String loginUserId = "";
		String loginUserNm = "";
		boolean replyCheck = false;
		
		if(SessionKeeper.getSdata(req) != null){
			memStatus = SessionKeeper.getSdata(req).getMemStatus();
			loginUserId = SessionKeeper.getSdata(req).getUserId();
			loginUserNm = SessionKeeper.getSdata(req).getName();
		}
		
		logger.info("==============================================================");
		logger.info("/event/eventReplyWrite -> memStatus       : "+memStatus);
		logger.info("/event/eventReplyWrite -> loginCntrNo     : "+loginCntrNo);
		logger.info("/event/eventReplyWrite -> loginUserId     : "+loginUserId);
		logger.info("/event/eventReplyWrite -> loginUserNm     : "+loginUserNm);
		logger.info("/event/eventReplyWrite -> eventReply     : "+eventReply.getReplySeq());
		logger.info("/event/eventReplyWrite -> eventReply     : "+eventReply.getContents());
		logger.info("/event/eventReplyWrite -> eventReply     : "+eventReply.getDelYn());
		logger.info("==============================================================");
		
		replyCheck = replyParamForgeryCheck(eventReply, loginCntrNo, loginUserId);
		
		if(replyCheck==false){
			logger.error("API : /event/eventReplyModify, ERROR [파라미터 위변조 댓글 수정 불가] ");
			throw new YappRuntimeException("CHECK_MSG", cmnService.getMsg("ERR_Y_SYSTEM"));	
		}
		
		ResultInfo<EventReply> resultInfo = new ResultInfo<>();
		
		try{
			eventService.updateEventReply(eventReply);
			
		} catch (RuntimeException e){
			logger.error("API : /event/eventReplyModify, ERROR [댓글 수정 불가] " + e);
			throw new YappRuntimeException("CHECK_MSG", cmnService.getMsg("ERR_Y_SYSTEM"));	
		} catch (Exception e){
			logger.error("API : /event/eventReplyModify, ERROR [댓글 수정 불가] " + e);
			throw new YappException("CHECK_MSG", cmnService.getMsg("ERR_Y_SYSTEM"));	
		}
		return resultInfo; 
	}
	
	
	/**
	 * 221113 댓글목록
	 * @param req
	 * @param apiUrl
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="댓글목록")
	@ResponseBody
	@RequestMapping(value = "/event/eventReplyList", method = RequestMethod.GET)
	@ApiImplicitParams({
		@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),
		@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), 
		@ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), 
		@ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<EventReply> eventReplyList(int evtSeq, int endSeq, HttpServletRequest req) throws Exception {
		
		//로그인 여부
		String loginCntrNo = "";
		String loginUserId = "";
			
		if(SessionKeeper.getSdata(req) != null){
			if(SessionKeeper.getCntrNo(req)!=null)
				loginCntrNo = SessionKeeper.getCntrNo(req);
			if(SessionKeeper.getSdata(req).getUserId()!=null)
				loginUserId = SessionKeeper.getSdata(req).getUserId();
		}			
		
		logger.info("==============================================================");
		logger.info("/event/eventReplyList -> evtSeq     : "+evtSeq);
		logger.info("/event/eventReplyList -> endSeq     : "+endSeq);
		logger.info("/event/eventReplyList -> endSeq     : "+loginCntrNo);
		logger.info("/event/eventReplyList -> endSeq     : "+loginUserId);
		logger.info("==============================================================");
		
		ResultInfo<EventReply> resultInfo = new ResultInfo<>();
		
		try{
			//한번에 호출되는 댓글 갯수
			final int LIMIT_CNT = 30;
			
			List<EventReply> eventReplyList = eventService.getEventReplyList(evtSeq, endSeq, loginCntrNo, loginUserId, LIMIT_CNT+1);
			
			//userId 마스킹 처리 주석처리 함..userId는 마스킹 필요없음..name에 마스킹이 들어감..2023.06.21 by jk
			//아이디 마스킹 처리
			//eventReplyList.stream().forEach(reply -> reply.setUserId(YappUtil.dataToMask("I", reply.getUserId())));
			
			resultInfo.setResultInfoList(eventReplyList);
			
		} catch (RuntimeException e){
			logger.error("API : /event/eventReplyList, ERROR [댓글 목록 불가] " + e);
			throw new YappRuntimeException("CHECK_MSG", cmnService.getMsg("ERR_Y_SYSTEM"));	
		} catch (Exception e){
			logger.error("API : /event/eventReplyList, ERROR [댓글 불러오기 불가] " + e);
			throw new YappException("CHECK_MSG", cmnService.getMsg("ERR_Y_SYSTEM"));	
		}
		return resultInfo; 
	}
	
	/**
	 * 230320 운영자댓글목록
	 * @param req
	 * @param apiUrl
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="운영자댓글목록")
	@ResponseBody
	@RequestMapping(value = "/event/eventAdminReplyList", method = RequestMethod.GET)
	@ApiImplicitParams({
		@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),
		@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), 
		@ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), 
		@ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<EventReply> eventAdminReplyList(int evtSeq, HttpServletRequest req) throws Exception {
		
		logger.info("==============================================================");
		logger.info("/event/eventAdminReplyList -> evtSeq     : "+evtSeq);
		logger.info("==============================================================");
		
		ResultInfo<EventReply> resultInfo = new ResultInfo<>();
		
		try{

			List<EventReply> eventReplyList = eventService.getEventAdminReplyList(evtSeq);
			
			resultInfo.setResultInfoList(eventReplyList);
			
		} catch (RuntimeException e){
			logger.error("API : /event/eventAdminReplyList, ERROR [댓글 목록 불가] " + e);
			throw new YappRuntimeException("CHECK_MSG", cmnService.getMsg("ERR_Y_SYSTEM"));	
		} catch (Exception e){
			logger.error("API : /event/eventAdminReplyList, ERROR [댓글 불러오기 불가] " + e);
			throw new YappException("CHECK_MSG", cmnService.getMsg("ERR_Y_SYSTEM"));	
		}
		return resultInfo; 
	}
	
	/**
	 * 221119 카운트 가져오기
	 * @param req
	 * @param apiUrl
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="카운트 가져오기")
	@ResponseBody
	@RequestMapping(value = "/event/eventIconCount", method = RequestMethod.GET)
	@ApiImplicitParams({
		@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),
		@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), 
		@ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), 
		@ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<EventIconCountResp> eventIconCount(int evtSeq, HttpServletRequest req) throws Exception {
		
		logger.info("==============================================================");
		logger.info("/event/eventIconCount -> evtSeq     : "+evtSeq);
		logger.info("==============================================================");
		
		String apiUrl = YappUtil.nToStr(req.getRequestURI());
		String loginYn = loginCheck(req, apiUrl);
		String loginCntrNo = "";
		String loginUserId = "";
		
		loginCntrNo = SessionKeeper.getCntrNo(req);
		
		if(SessionKeeper.getSdata(req) != null){
			loginUserId = SessionKeeper.getSdata(req).getUserId();
		}
		
		logger.info("==============================================================");
		logger.info("eventIconCount -> loginCntrNo     : "+loginCntrNo);
		logger.info("eventIconCount -> loginUserId     : "+loginUserId);
		logger.info("==============================================================");
		
		ResultInfo<EventIconCountResp> resultInfo = new ResultInfo<>();
		EventIconCountResp resultData = new EventIconCountResp();

		try{
			int replyCnt = 0;
			int intlikeCnt = 0;
			
			replyCnt += eventService.getEventReplyCnt(evtSeq);
			intlikeCnt += eventService.getEventLikeCnt(evtSeq);
			
			String likeCnt = YappUtil.setStringNumber(intlikeCnt);
			
			if("Y".equals(loginYn)){
				
				EventLike likeInfo = eventService.getUserLikeInfo(evtSeq, loginCntrNo, loginUserId);
				
				if(likeInfo != null){
					resultData.setLikeSeq(likeInfo.getLikeSeq());
					resultData.setLikeYn(likeInfo.getLikeYn());
				}
			}
			
			resultData.setLikeCnt(likeCnt);
			resultData.setReplyCnt(replyCnt);
			resultInfo.setResultData(resultData);
			
		} catch (RuntimeException e){
			logger.error("API : /event/eventIconCount, ERROR [카운트 가져오기 불가] " + e);
			throw new YappRuntimeException("CHECK_MSG", cmnService.getMsg("ERR_Y_SYSTEM"));	
		} catch (Exception e){
			logger.error("API : /event/eventIconCount, ERROR [카운트 가져오기 불가] " + e);
			throw new YappException("CHECK_MSG", cmnService.getMsg("ERR_Y_SYSTEM"));	
		}
		return resultInfo; 
	}
	
	
	/**
	 * 221120 좋아요 등록
	 * @param req
	 * @param apiUrl
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="좋아요 등록")
	@ResponseBody
	@RequestMapping(value = "/event/eventLikeWrite", method = RequestMethod.POST)
	@ApiImplicitParams({
		@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),
		@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), 
		@ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), 
		@ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<EventLike> eventLikeWrite(int evtSeq, HttpServletRequest req) throws Exception {
		
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		String memStatus = "";
		String loginUserId = "";
		
		if(SessionKeeper.getSdata(req) != null){
			memStatus = SessionKeeper.getSdata(req).getMemStatus();
			loginUserId = SessionKeeper.getSdata(req).getUserId();
		}
		
		logger.info("==============================================================");
		logger.info("/event/eventLikeWrite -> memStatus       : "+memStatus);
		logger.info("/event/eventLikeWrite -> loginCntrNo     : "+loginCntrNo);
		logger.info("/event/eventLikeWrite -> loginUserId     : "+loginUserId);
		logger.info("==============================================================");
		
		ResultInfo<EventLike> resultInfo = new ResultInfo<>();
		
		try{
			
			EventLike likeInfo = eventService.getUserLikeInfo(evtSeq, loginCntrNo, loginUserId);
			
			if(likeInfo == null){
				
				likeInfo = new EventLike();
				
				likeInfo.setEvtSeq(evtSeq);
				likeInfo.setCntrNo(loginCntrNo);
				likeInfo.setUserId(loginUserId);
				
				eventService.insertLikeData(likeInfo);
				
			}else{
				
				if("Y".equals(likeInfo.getLikeYn())){
					likeInfo.setLikeYn("N");
				}else{
					likeInfo.setLikeYn("Y");
				}
				
				eventService.updateEventLike(likeInfo);
				
				resultInfo.setResultData(likeInfo);
			}
			
		} catch (RuntimeException e){
			logger.error("API : /event/eventReplyWrite, ERROR [좋아요 등록 불가] " + e);
			throw new YappRuntimeException("CHECK_MSG", cmnService.getMsg("ERR_Y_SYSTEM"));	
		} catch (Exception e){
			logger.error("API : /event/eventReplyWrite, ERROR [좋아요 등록 불가] " + e);
			throw new YappException("CHECK_MSG", cmnService.getMsg("ERR_Y_SYSTEM"));	
		}
		return resultInfo; 
	}
	

	private String loginCheck(HttpServletRequest req, String apiUrl) throws Exception{
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
			tmp[2] = tmp2[2].trim();
			tmp[3] = tmp2[3];
			tmp[4] = tmp2[4];
			tmp[5] = tmp2[5];
			tmp[6] = "G0002";
		}
		
		if(autoLoginYn.equals("true")){
			
			if(YappUtil.isEmpty(tmp[0])||tmp[0].indexOf("ktid")!=-1){

				if(apiUrl.indexOf("/user/join") == -1) {
					//휴면계정 복구
					userService.sleepUserKtRestore(tmp[0], tmp[1], tmp[2], tmp[4]);
				}
				
				//mod_dt 업데이트
				userService.updateUserKtModDtInfo(tmp[2]);
				
			}else{
				
				if(apiUrl.indexOf("/user/join") == -1) {
					//휴면계정 복구
					userService.sleepUserRestore(tmp[0], tmp[1], tmp[2], tmp[4]);
				}
				
				//mod_dt 업데이트
				userService.updateUserModDtInfo(tmp[0]);
			}
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

		if(autoLoginPassYn.equals("Y")){
			if(autoLoginYn != null){
				//서버 세션에 정보가 없고 자동로그인(로그인유지)인 경우 단말에서 넘오는 정보를 서버 세션에 저장. 
				if(autoLoginYn.equals("true")){
					logger.info("SessionKeeper 세팅");
					logger.info("tmp[2]:"+tmp[2]);
					logger.info("tmp[1]:"+tmp[1]);
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
		
		if(SessionKeeper.getSdata(req) != null){
			
			mobileNo = SessionKeeper.getSdata(req) == null ? null : SessionKeeper.getSdata(req).getMobileNo();
			memStatus = SessionKeeper.getSdata(req) == null ? null : SessionKeeper.getSdata(req).getMemStatus();
		}
		
		// 계약정보, 사용자 정보가 없으면 실행을 종료한다.  
		if (YappUtil.isEmpty(mobileNo) 
				|| YappUtil.isEmpty(memStatus)
				|| ( SessionKeeper.getSdata(req) != null && SessionKeeper.getSdata(req).isExistUser() == false) ) 
		{
			return loginYn = "N";
		}

		if(joinInfo != null){
			if(!tmp[4].equals(joinInfo.getDupId())){
				logger.error("session dup id = " + tmp[4]);
				logger.error("DB dup id = " + joinInfo.getDupId());
				logger.error("중복 로그인 불가. 다른 기기에서 로그인 되었습니다.");
				// throw new YappAuthException("410","중복 로그인 불가. 다른 기기에서 로그인 되었습니다.");
				return loginYn = "N";
			}
		}else{
			logger.error("DUP ID 정보가 없습니다.(해당 계약번호로 TB_USER에 정보가 없음.)");
			// throw new YappAuthException("410","로그인 정보가 없습니다.");
			return loginYn = "N";
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
	 * 230302 인원수에 따른 상품 리워드 선택 할떄 검증 처리
	 * @param evtSeq
	 * @param loginCntrNo (방 인원수)
	 * @param giftType (G0001 : 오프라인, G0002 : 온라인, G0004 : 데이터상품)
	 * @return boolean
	 * @throws Exception
	 */
	private boolean giftRewardTargetCheck(int evtSeq, String loginCntrNo){
		
		int iRet = 0;
		boolean bRet = false;
		
		try{
			
			List<RoomInfo> roomInfoList = new ArrayList<RoomInfo>();
			roomInfoList = yFriendsService.getRoomInfoList(evtSeq, loginCntrNo);
		
			//만약에 방에 가입된 인원에 상품이 충분한 경우(해당 방 상품 수량 > 0)
			iRet = yFriendsService.getGiftRemainCount(evtSeq, roomInfoList.get(0).getJoinCnt());
				
			if(iRet > 0){
				bRet = true;
			}else{
				bRet = false;
			}
		
		}catch(Exception e){
			return bRet = false;
		}
		
		return bRet;
	}
	
	private boolean replyParamForgeryCheck(EventReply eventReply, String cntrNo, String userId){
		
		boolean replyCheck = true;
		
		//수정하려는 댓글이 본인것인지 확인
		eventReply = eventService.getEventReplyMine(eventReply);
		//계약번호가 없고 아이디만 있는 댓글
		if(eventReply.getOriginKey().equals("I")){
			//세션에 있는 아이디와 비교하여 틀리면 throw
			if(!eventReply.getOriginValue().equals(userId)){
				replyCheck = false;
			}
		}
		//계약번호가 있는 댓글
		else{
			//세션에 있는 계약번호와 비교하여 틀리면 throw
			if(!eventReply.getOriginValue().equals(cntrNo)){
				replyCheck = false;
			}
		}
		
		return replyCheck;
	}
	
	/***
	 * 230711 준회원 모바일번호 체크 함수
	 * @param String userId(유저ID), String credentialIde(크레덴셜ID), String loginMobileNo(입력받은 회선번호) 
	 * @return void
	 */
    private void setCheckUserMobileNo(String userId, String credentialIde, String mobileNo) throws Exception{
    	UserInfo userKtInfo = null; //서비스 호출용 user객체
    	UserInfo userInfoKt = new UserInfo(); //update용 user객체
    	
    	String cntrMobileNo = "";
    	
    	//회선번호 넘겨받는게 없으면 return
    	if(mobileNo.equals("")){
    		logger.info("=================setCheckUserMobileNo=================");
			logger.info("no param mobileNo");
    	}
		
    	//OIF_140(user_id, credentialIde)로 호출
    	SoapResponse140 resp = shubService.callFn140(userId, credentialIde);
    	//계약정보 가져오기
		List<ContractInfo> cntrList = resp.getCntrInfoList();
		
		//null값 빈값으로 치환
		for(ContractInfo contract: cntrList){
			//회선이 여러개인 고객이면 무조껀 마지막 회선번호로 insert
			if(contract.getMobileNo()==null){
				cntrMobileNo = "";
			}else{
				cntrMobileNo = contract.getMobileNo();
			}
		}

		//기존 저장된 유저 정보 가져오기
		userKtInfo = userService.getYappUserKtInfo(userId);
		//null처리
		userKtInfo.setMobileNo(YappUtil.isEmpty(userKtInfo.getMobileNo()) ? "" : userKtInfo.getMobileNo());

		logger.info("cntrMobileNo : " + cntrMobileNo);
		logger.info("mobileNo : " + mobileNo);

		//기존 저장된 유저정보에 회선번호가 존재하지 않다면
		if(YappUtil.isEmpty(userKtInfo.getMobileNo())){
			//호출된 계약정보에 회선번호값이 존재하고 기존 유저정보에 회선번호가 없고 입력받은 회선번호도 존재하지 않는다면 OIF_140으로 호출된 값으로 update
			if(!cntrMobileNo.equals("") && userKtInfo.getMobileNo().equals("") && mobileNo.equals("")){
				logger.info("=================setCheckUserMobileNo.1=================");
				userInfoKt.setUserId(userId);
				userInfoKt.setMobileNo(cntrMobileNo);
				userService.updateUserKtMobileInfo(userInfoKt);
			}else{//아니면 입력받은 회선번호로 update
				logger.info("=================setCheckUserMobileNo.2=================");
				userInfoKt.setUserId(userId);
				userInfoKt.setMobileNo(mobileNo);
				userService.updateUserKtMobileInfo(userInfoKt);
			}
		}else{//존재한다면
			//기존 유저정보의 회선번호와 입력받은 회선번호가 다르고 입력받은 값이 존재하면 새로 입력받은 회선번호로 update진행
			if(!userKtInfo.getMobileNo().equals(mobileNo) && !mobileNo.equals("")){
				logger.info("=================setCheckUserMobileNo.3=================");
				userInfoKt.setUserId(userId);
				userInfoKt.setMobileNo(mobileNo);		
				userService.updateUserKtMobileInfo(userInfoKt);
			}
		}
	}
	
	/***
	 * 개발서버 테스트 계정정보 로드...
	 * @return
	 */
    @Profile("!prod")
	public Map<String,String> getDevUserInfo(){
		Map<String,String> devUserMap = new HashMap<>();

		int nType = 5;
		switch(nType){
			case 1:
				devUserMap.put("memStatus","G0001");
				devUserMap.put("loginCntrNo", "625231423");
				devUserMap.put("loginUserId", "ydbox16");
				break;
			case 2:
				devUserMap.put("memStatus","G0001");
				devUserMap.put("loginCntrNo", "603465777");
				devUserMap.put("loginUserId", "ydbox13");
				break;
			case 3:
				devUserMap.put("memStatus","G0001");
				devUserMap.put("loginCntrNo", "624365242");
				devUserMap.put("loginUserId", "ydbox14");
				break;
			case 4:
				devUserMap.put("memStatus","G0001");
				devUserMap.put("loginCntrNo", "624390084");
				devUserMap.put("loginUserId", "ydbox15");
				break;
			case 5://준회원
				devUserMap.put("memStatus","G0003");
				devUserMap.put("loginCntrNo", "");
				devUserMap.put("loginUserId", "nmovi3");
				break;
			case 6://준회원
				devUserMap.put("memStatus","G0003");
				devUserMap.put("loginCntrNo", "");
				devUserMap.put("loginUserId", "nmovi4");
				break;
			case 7://준회원
				devUserMap.put("memStatus","G0003");
				devUserMap.put("loginCntrNo", "");
				devUserMap.put("loginUserId", "nmovi7");
				break;
			case 8://준회원
				devUserMap.put("memStatus","G0003");
				devUserMap.put("loginCntrNo", "");
				devUserMap.put("loginUserId", "nmovi8");
				break;
			default:
				devUserMap.put("memStatus","G0001");
				devUserMap.put("loginCntrNo", "496999161");
				devUserMap.put("loginUserId", "dtffahah");
				
		}
		
		return devUserMap;
	}
}