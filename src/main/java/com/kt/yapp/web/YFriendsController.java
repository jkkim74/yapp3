package com.kt.yapp.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.kt.yapp.domain.ContractInfo;
import com.kt.yapp.domain.EventGift;
import com.kt.yapp.domain.EventMaster;
import com.kt.yapp.domain.YfriendsMenu;
import com.kt.yapp.domain.JoinInfo;
import com.kt.yapp.domain.RoomChkInfo;
import com.kt.yapp.domain.RoomInfo;
import com.kt.yapp.domain.RoomInvt;
import com.kt.yapp.domain.RoomJoin;
import com.kt.yapp.domain.RoomTitle;
import com.kt.yapp.domain.YfriendsInfo;
import com.kt.yapp.domain.SessionContractInfo;
import com.kt.yapp.domain.SysCheck;
import com.kt.yapp.domain.UserInfo;
import com.kt.yapp.domain.resp.ResultInfo;
import com.kt.yapp.em.EnumYn;
import com.kt.yapp.exception.YappAuthException;
import com.kt.yapp.exception.YappException;
import com.kt.yapp.exception.YappRuntimeException;
import com.kt.yapp.service.CmsService;
import com.kt.yapp.service.CommonService;
import com.kt.yapp.service.EventService;
import com.kt.yapp.service.ShubService;
import com.kt.yapp.service.UserService;
import com.kt.yapp.service.YFriendsService;
import com.kt.yapp.soap.response.SoapResponse139;
import com.kt.yapp.soap.response.SoapResponse235;
import com.kt.yapp.util.KeyFixUtil;
import com.kt.yapp.util.SessionKeeper;
import com.kt.yapp.util.YappCvtUtil;
import com.kt.yapp.util.YappUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "Y 프렌즈 컨트롤러")
@Controller
public class YFriendsController 
{
	private static final Logger logger = LoggerFactory.getLogger(YFriendsController.class);

	@Autowired
	private YFriendsService yFriendsService;
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
	private EventService eventService;
	
	@ApiOperation(value="Y프렌즈 화면호출")
	@RequestMapping(value = "/yFriends/yFriends", method = RequestMethod.GET)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ModelAndView yFriends(int evtSeq, HttpServletRequest req) throws Exception {
		ModelAndView mv = new ModelAndView();
		Gson gson = new Gson();
		String ysid = req.getHeader("ysid");
		String autoLogin = req.getHeader("autoLogin");
		String osTp = req.getHeader("osTp");
		String appVrsn = req.getHeader("appVrsn");
		String errorMsg = "";
		String errorMsg2 = "";
		String endYn = "N";
		String apiUrl = YappUtil.nToStr(req.getRequestURI());
		String commErrorMsg = "";
		String loginCntrNo = "";
		String loginUserId = "";
	    String loginMobileNo = "";
	    String partyName = "";
	    YfriendsMenu yfriendsMenuInfo = null;
	    List<EventGift> eventGiftList =  new ArrayList<EventGift>();
	    EventMaster eventDetail = new EventMaster();
	    String giftReceivedYn = "N";
	    String memStatus = "";
	    String targetYn = "Y";
	    String targetCode = "01";
	    
	    List<RoomInfo> roomInfoList = new ArrayList<RoomInfo>();
		List<RoomJoin> roomJoinList = new ArrayList<RoomJoin>();
		
		//로그인 여부(정회원) (배포시 주석)
		/*ysid = "ydbox16";
		autoLogin = "N";
		osTp = "G0002";
		appVrsn = "3.0.1";
		loginCntrNo = "625231423";
		loginMobileNo = "01066598046";*/
		
		//로그인 여부(준회원) (배포시 주석)
		/*ysid = "nmovi3";
		autoLogin = "N";
		osTp = "G0002";
		appVrsn = "3.0.2";
		loginCntrNo = "";
		loginMobileNo = "";*/
		
		/*//로그인 여부(준회원) (배포시 주석)
		/*ysid = "nmovi4";
		autoLogin = "N";
		osTp = "G0002";
		appVrsn = "3.0.2";
		loginCntrNo = "";
		loginMobileNo = "";*/
		
		//로그인 여부(정회원 방장) (배포시 주석)
		/*ysid = "ydbox16";
		autoLogin = "N";
		osTp = "G0002";
		appVrsn = "3.0.2";
		loginCntrNo = "625231423";
		loginMobileNo = "01066598046";*/
		
		//로그인 여부(정회원) (배포시 주석)
		/*ysid = "ydbox15";
		autoLogin = "N";
		osTp = "G0002";
		appVrsn = "3.0.2";
		loginCntrNo = "";
		loginMobileNo = "";*/
		
		//로그인 여부(정회원) (배포시 주석)
		/*ysid = "ydbox14";
		autoLogin = "N";
		osTp = "G0001";
		appVrsn = "3.0.3";
		loginCntrNo = "624365242";
		loginMobileNo = "01059181475";*/
			
		try{
	        commErrorMsg = yFriendsValidationCheck(req, apiUrl);
	         
	        loginCntrNo = SessionKeeper.getCntrNo(req);
	         
	        if(SessionKeeper.getSdata(req) != null){
	            memStatus = SessionKeeper.getSdata(req).getMemStatus();
	            partyName = SessionKeeper.getSdata(req).getName();
	            loginUserId = SessionKeeper.getSdata(req).getUserId();
	            loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
	        }
	         
	        logger.info("==============================================================");
	        logger.info("yFriends/yFriends -> memStatus       : "+memStatus);
	        logger.info("yFriends/yFriends -> partyName       : "+partyName);
	        logger.info("yFriends/yFriends -> loginCntrNo     : "+loginCntrNo);
	        logger.info("yFriends/yFriends -> loginUserId     : "+loginUserId);
	        logger.info("yFriends/yFriends -> osTp            : "+osTp);
	        logger.info("yFriends/yFriends -> appVrsn         : "+appVrsn);
	        logger.info("==============================================================");
	         
	        // 이벤트 정보 조회, Y프렌즈 정보, Y프렌즈 방정보
	        eventDetail = eventService.getEventDetailMaster(evtSeq);
	        yfriendsMenuInfo = yFriendsService.getEventMenu(evtSeq, "N");
	        roomInfoList = yFriendsService.getRoomInfoList(evtSeq, loginCntrNo);
	         
	        mv.addObject("yfriendsMenuInfo", yfriendsMenuInfo);
	         
	        if(yfriendsMenuInfo == null){
	            errorMsg = cmnService.getMsg("CHK_Y_END_EVENT");
	        }else{
	             
	            endYn = yfriendsMenuInfo.getEndYn();
	            
	            RoomChkInfo chkUserResult = checkValidMember(eventDetail, loginCntrNo, loginUserId, "G0001");
	            
    			if("N".equals(chkUserResult.getTargetYn())){
    				RoomJoin roomJoin = new RoomJoin();
					roomJoin.setRmSeq(roomInfoList.get(0).getRmSeq());
					roomJoin.setJoinCntrNo(loginCntrNo);
					roomJoin.setDelYn("Y");
					
					//초대된 인원중에 문제 발생시 방 퇴출
					yFriendsService.updateRoomJoin(roomJoin);
					
					errorMsg = cmnService.getMsg("CHK_Y_NO_EVENT_TARGET");
					throw new YappRuntimeException("CHECK_MSG", errorMsg);
    			}

	    		List<RoomInvt> roomInvtList = yFriendsService.getRoomChkList(roomInfoList.get(0).getRmSeq());
	    		
	    		int joinCnt = roomInfoList.get(0).getJoinCnt();
	    		
	    		for(int i=0; i<roomInvtList.size(); i++){
	    			
	    			RoomChkInfo chkMemResult = checkValidMember(eventDetail, roomInvtList.get(i).getRcvCntrNo(), null, "G0002");
	    			
	    			if("N".equals(chkMemResult.getTargetYn())){
	    				RoomJoin roomJoin = new RoomJoin();
						roomJoin.setRmSeq(roomInfoList.get(0).getRmSeq());
						roomJoin.setJoinCntrNo(roomInvtList.get(i).getRcvCntrNo());
						roomJoin.setDelYn("Y");
						
						//초대된 인원중에 문제 발생시 방 퇴출
						yFriendsService.updateRoomJoin(roomJoin);
						
						joinCnt--;
	    			}
	    		}
	    		roomInfoList.get(0).setJoinCnt(joinCnt);
	        }
	    }catch(RuntimeException e){
	        //e.printStackTrace();
	        if("".equals(errorMsg)){
	            logger.error("ERROR ::" + e);
	            targetYn = "N";
	    		targetCode = "99";
	            errorMsg = cmnService.getMsg("ERR_Y_SYSTEM");
	        }
	    }catch(Exception e){
	        //e.printStackTrace();
	        if("".equals(errorMsg)){
	            logger.error("ERROR ::" + e);
	            targetYn = "N";
	    		targetCode = "99";
	            errorMsg = cmnService.getMsg("ERR_Y_SYSTEM");
	        }
	    }
		
		if(!"".equals(commErrorMsg)) {
			errorMsg = commErrorMsg;
			mv.addObject("sysErr", "Y");
		}
		else {
			mv.addObject("sysErr", "N");
		}
		
		// 체크로직에 pass한 경우
		if("".equals(errorMsg)){
			
			if(roomInfoList.size() > 1){
				roomInfoList.remove(0);
			}
			
			if(roomInfoList.size() == 1){
				roomJoinList = yFriendsService.getRoomJoinList(evtSeq, loginCntrNo, roomInfoList.get(0).getRmSeq());
				//마스터 계정 마스킹 안되어 있는 부분 처리
				roomJoinList.stream().forEach(mastername -> mastername.setMasterName(YappUtil.blindNameToName2(mastername.getMasterName(),1)));
				//일반 계정 마스킹 안되어 있는 부분 처리
				roomJoinList.stream().forEach(joinName -> joinName.setJoinName(YappUtil.blindNameToName2(joinName.getJoinName(),1)));
				
				mv.addObject("roomJoinList", roomJoinList);
				logger.info("roomJoinList : " + roomJoinList);
			}else{
				mv.addObject("roomJoinList", "");
			}		
		}else{
			mv.addObject("joinCompleteYn", "N");
			mv.addObject("roomInfoList", "");
			mv.addObject("roomJoinList", "");
		}
		
		// 다중경품제공 여부에 따른 경품 목록 불러오기
		String multiGiftRewardChk = eventDetail.getGiftMultiReward();
		
		logger.info("multiGiftRewardChk : "+multiGiftRewardChk);
		
		if("Y".equals(multiGiftRewardChk)){
			logger.info("getEventDetailOfflineGiftList Start");
			eventGiftList = eventService.getEventDetailOfflineGiftList(evtSeq);
		}else{
			logger.info("getEventDetailGiftList Start");
			eventGiftList = eventService.getEventDetailGiftList(evtSeq, null);
		}
		
		//데이터 상품 리스트
		if(eventDetail != null){
			if(YappUtil.isEmpty(eventDetail.getEvtOptionTitle())){
				if("G0001".equals(eventDetail.getEvtType()) || "G0002".equals(eventDetail.getEvtType())){
					eventDetail.setEvtTypeNm("이벤트");
				}
			}else{
				eventDetail.setEvtTypeNm(eventDetail.getEvtOptionTitle());
			}
		}
		
		//이벤트 이벤트 경품 지급완료 횟수로 참여완료 여부 (Y : 참여완료, N : 진행중 또는 참여중)
		if(eventService.getEventJoinCount(evtSeq, loginCntrNo, null) > 0){
			giftReceivedYn = "Y";
		}else{
			giftReceivedYn = "N";
		}
			
		int iRet = 0;
		int joinCnt = roomInfoList.get(0).getJoinCnt();
		String invitedFnshYn =  roomInfoList.get(0).getInvitedFnshYn();
		
		try{
			if("Y".equals(invitedFnshYn)){
				
				//입장 가능여부
				iRet = yFriendsService.getGiftRemainCount(evtSeq, joinCnt);
				
				if(iRet > 0){
					mv.addObject("existGiftYn", "Y");
				}else{
					mv.addObject("existGiftYn", "N");
				}
				
			}else{
				//입장 가능여부
				iRet = yFriendsService.getAllGiftRemainCount(evtSeq, joinCnt);
				
				if(iRet >= 0){
					mv.addObject("remainGiftYn", "Y");
				}else{
					mv.addObject("remainGiftYn", "N");
				}
				
				//완료 가능여부
				iRet = yFriendsService.getCurrentGiftRemainCount(evtSeq, joinCnt);
				//이벤트 아직 참여하지 않은 경우
				if(iRet >= 0 && giftReceivedYn.equals("N")){
					mv.addObject("enableFnshYn", "Y");
				}else{
					mv.addObject("enableFnshYn", "N");
				}
				
				//초대 가능여부
				iRet = yFriendsService.getNextGiftRemainCount(evtSeq, joinCnt);
				
				if(iRet >= 0){
					mv.addObject("enableInviteYn", "Y");
				}else{
					mv.addObject("enableInviteYn", "N");
				}
			}
			

		}catch(RuntimeException e){
			//e.printStackTrace();
			if("".equals(errorMsg)){
				logger.error("ERROR ::" + cmnService.getMsg("CHK_Y_NO_JOIN2"));
				throw new YappException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_JOIN2"));
			}
		}catch(Exception e){
			//e.printStackTrace();
			if("".equals(errorMsg)){
				logger.error("ERROR ::" + cmnService.getMsg("CHK_Y_NO_JOIN2"));
				throw new YappException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_JOIN2"));
			}
		}
		
		RoomTitle roomTitle = yFriendsService.getMemberJoinTitle(evtSeq, joinCnt);
		
        if(YappUtil.isEmpty(partyName)){
        	SoapResponse235 re235 = shubService.callFn235(loginCntrNo, loginMobileNo);
        	partyName= re235.getPartyName();
        }
		
		mv.addObject("roomTitle", roomTitle);
		mv.addObject("evtSeq", evtSeq);
		mv.addObject("cntrNo", loginCntrNo);
		mv.addObject("endYn", endYn);
		mv.addObject("chkMsg", errorMsg);
		mv.addObject("chkSubMsg", errorMsg2);
		mv.addObject("ysid", ysid);
		mv.addObject("autoLogin", autoLogin);
		mv.addObject("osTp", osTp);
		mv.addObject("appVrsn", appVrsn);
		mv.addObject("partyName", YappUtil.blindNameToName2(partyName, 1));
		mv.addObject("multiGiftRewardChk", multiGiftRewardChk);
		mv.addObject("eventDetail", eventDetail);
		mv.addObject("eventGiftList", eventGiftList);//온오프라인 상품
		mv.addObject("eventGiftListJson", gson.toJson(eventGiftList));//데이터 상품
		mv.addObject("eventJoinYn", giftReceivedYn);
		mv.addObject("roomInfoList", roomInfoList);
		mv.setViewName("yFriends/yFriends");
		
		return mv; 
	}
	

	@ApiOperation(value="Y프렌즈 방 개설")
	@ResponseBody
	@RequestMapping(value = "/yFriends/room", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<String> roomAdd(int evtSeq, HttpServletRequest req) throws Exception {
		
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		String loginUserId = "";
		String loginMobileNo = "";
		String partyName = "";
		
		if(SessionKeeper.getSdata(req) != null){
			loginUserId = SessionKeeper.getSdata(req).getUserId();
            partyName = SessionKeeper.getSdata(req).getName();
            loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
		}
		
        logger.info("==============================================================");
        logger.info("yFriends/room -> partyName       : "+partyName);
        logger.info("yFriends/room -> loginCntrNo     : "+loginCntrNo);
        logger.info("yFriends/room -> loginUserId     : "+loginUserId);
        logger.info("yFriends/room -> loginMobileNo   : "+loginMobileNo);
        logger.info("==============================================================");

		/*loginCntrNo = "625231423";
		loginMobileNo = "01066598046";*/
		
		RoomChkInfo chkRoomResult = checkValidRoom(evtSeq, 0, loginCntrNo, "G0001");
		
		if("N".equals(chkRoomResult.getTargetYn())){
			throw new YappException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_ROOM"));
		}
		
		try{
			YfriendsInfo yfriendsInfo = yFriendsService.getYfriendsInfo(evtSeq);
			EventMaster eventDetail = eventService.getEventDetailMaster(evtSeq);
			RoomChkInfo chkMemResult = checkValidMember(eventDetail, loginCntrNo, loginUserId, "G0001");
			
	        logger.info("==============================================================");
	        logger.info("yFriends/room -> yfriendsInfo    : "+yfriendsInfo);
	        logger.info("yFriends/room -> eventDetail     : "+eventDetail);
	        logger.info("yFriends/room -> chkMemResult    : "+chkMemResult);
	        logger.info("==============================================================");
	        
	        if(YappUtil.isEmpty(partyName)){
	        	SoapResponse235 re235 = shubService.callFn235(loginCntrNo, loginMobileNo);
	        	partyName= re235.getPartyName();
	        }

			// 방정보 파라미터
			RoomInfo paramObj = new RoomInfo();
			paramObj.setEmSeq(yfriendsInfo.getEmSeq());
			paramObj.setMaxJoinCnt(yfriendsInfo.getMaxRoomCnt());
			paramObj.setRemarks(yfriendsInfo.getMenuName());
			paramObj.setJoinCntrNo(loginCntrNo);

			// 방참여 파라미터
			RoomJoin joinParam = new RoomJoin();
			joinParam.setJoinType("JN001");
			joinParam.setJoinCntrNo(loginCntrNo);
			joinParam.setJoinName(YappUtil.blindNameToName2(partyName, 1));
			joinParam.setPpCd(chkMemResult.getPpCd());

			yFriendsService.insertRoomInfo(paramObj, joinParam);
			
			//방 개설시 tb_data_reward_info에  cntr_no를 추가 시켜줘야함

		} catch (RuntimeException e){
			//e.printStackTrace();
			logger.error("API : /yFriends/room, ERROR [방개설 불가] " + e);
			throw new YappException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_ROOM"));	
		} catch (Exception e){
			//e.printStackTrace();
			logger.error("API : /yFriends/room, ERROR [방개설 불가] " + e);
			throw new YappException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_ROOM"));	
		}
		
		return new ResultInfo<>(); 
	}

	@ApiOperation(value="Y프렌즈 방 내보내기")
	@ResponseBody
	@RequestMapping(value = "/yFriends/ban", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<String> joinBan(String rmSeq, String cntrNo, HttpServletRequest req) throws Exception {

		RoomInfo roomInfo = yFriendsService.getRoomInfo(Integer.parseInt(rmSeq));
		
		if("Y".equals(roomInfo.getInvitedFnshYn())){
			throw new YappException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_BAN"));
		}

		try{
			// 방참여 파라미터
			RoomJoin joinParam = new RoomJoin();
			joinParam.setRmSeq(Integer.parseInt(rmSeq));
			joinParam.setJoinCntrNo(cntrNo);
			joinParam.setDelYn("Y");

			yFriendsService.updateRoomJoin(joinParam);
			
			RoomInvt paramObj2 = new RoomInvt();
			paramObj2.setRmSeq(Integer.parseInt(rmSeq));
			paramObj2.setRcvCntrNo(cntrNo);
			
			yFriendsService.deleteRoomInvt(paramObj2);
			
			if("Y".equals(roomInfo.getJoinFnshYn())){
				RoomInfo paramObj = new RoomInfo();
				paramObj.setJoinFnshYn("N");
				paramObj.setRmSeq(Integer.parseInt(rmSeq));

				yFriendsService.updateRoomInfo(paramObj);
			}
		} catch (RuntimeException e){
			//e.printStackTrace();
			logger.error("API : /na/room, ERROR [방내보내기 불가] " + e);
			throw new YappException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_BAN2"));	
		} catch (Exception e){
			//e.printStackTrace();
			logger.error("API : /na/room, ERROR [방내보내기 불가] " + e);
			throw new YappException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_BAN2"));	
		}
		return new ResultInfo<>(); 
	}
	
	@ApiOperation(value="Y프렌즈 방 나가기")
	@ResponseBody
	@RequestMapping(value = "/yFriends/exit", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<String> joinDel(String rmSeq, HttpServletRequest req) throws Exception {
		String loginCntrNo = SessionKeeper.getCntrNo(req);

		//1. 해당 사용자에 대한 방체크(모집완료가 됐는지)
		RoomInfo roomInfo = yFriendsService.getRoomInfo(Integer.parseInt(rmSeq));
		if("Y".equals(roomInfo.getInvitedFnshYn())){
			throw new YappException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_EXIT"));
		}

		try{
			// 방참여 파라미터
			RoomJoin joinParam = new RoomJoin();
			RoomInvt paramObj3 = new RoomInvt();
			paramObj3.setRmSeq(Integer.parseInt(rmSeq));
			joinParam.setRmSeq(Integer.parseInt(rmSeq));
			joinParam.setDelYn("Y");
			
			if(loginCntrNo != null && (loginCntrNo.equals(roomInfo.getMasterCntrNo()))){
				//방장일경우
				yFriendsService.updateRoomJoin(joinParam);
				RoomInfo paramObj2 = new RoomInfo();
				paramObj2.setRmSeq(Integer.parseInt(rmSeq));
				paramObj2.setDelYn("Y");
				yFriendsService.updateRoomInfo(paramObj2);
				
				yFriendsService.deleteRoomInvt(paramObj3);
			}else{
				//팀원일경우
				joinParam.setJoinCntrNo(loginCntrNo);
				yFriendsService.updateRoomJoin(joinParam);
				
				paramObj3.setRcvCntrNo(loginCntrNo);
				yFriendsService.deleteRoomInvt(paramObj3);
				
				if("Y".equals(roomInfo.getJoinFnshYn())){
					RoomInfo paramObj = new RoomInfo();
					paramObj.setJoinFnshYn("N");
					paramObj.setRmSeq(Integer.parseInt(rmSeq));
					yFriendsService.updateRoomInfo(paramObj);
				}
			}
		} catch (RuntimeException e){
			//e.printStackTrace();
			logger.error("API : /na/room, ERROR [방탈퇴 불가] " + e);
			throw new YappException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_EXIT2"));	
		} catch (Exception e){
			//e.printStackTrace();
			logger.error("API : /na/room, ERROR [방탈퇴 불가] " + e);
			throw new YappException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_EXIT2"));	
		}
		return new ResultInfo<>(); 
	}

	@ApiOperation(value="Y프렌즈 방 참여")
	@ResponseBody
	@RequestMapping(value = "/yFriends/join", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<String> join(int evtSeq, int rmSeq, HttpServletRequest req) throws Exception {
		
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		String loginUserId = "";
		String loginMobileNo = "";
		String partyName = "";
		
		if(SessionKeeper.getSdata(req) != null){
			loginUserId = SessionKeeper.getSdata(req).getUserId();
			loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
			partyName = SessionKeeper.getSdata(req).getName();
		}
		
		RoomChkInfo chkRoomResult = checkValidRoom(evtSeq, 0, loginCntrNo, "G0001");
		EventMaster eventDetail = eventService.getEventDetailMaster(evtSeq);
		RoomChkInfo chkMemResult = checkValidMember(eventDetail, loginCntrNo, loginUserId, "G0001");
		
		if("N".equals(chkRoomResult.getTargetYn()) || "N".equals(chkMemResult.getTargetYn())){
			throw new YappException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_JOIN"));
		}
		
		RoomChkInfo roomCnt = yFriendsService.getRoomJoinCount(rmSeq);
		if(roomCnt != null){
			if(roomCnt.getJoinCnt() >= roomCnt.getMaxRoomCnt()){
				if(roomCnt.getJoinCnt() == 0){
					throw new YappException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_JOIN2"));
				}else{
					throw new YappException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_JOIN"));
				}
					
			}
		}

		try{
			
			if(YappUtil.isEmpty(partyName)){
				ContractInfo cntrInfo = shubService.callFn139(loginMobileNo).getCntrInfo();
				partyName= cntrInfo.getOrgUserNm();
			}
			
			RoomJoin joinParam = new RoomJoin();
			joinParam.setJoinType("JN002");
			joinParam.setRmSeq(rmSeq);
			joinParam.setJoinCntrNo(loginCntrNo);
			joinParam.setJoinName(YappUtil.blindNameToName2(partyName, 1));
			joinParam.setPpCd(chkMemResult.getPpCd());
			yFriendsService.insertRoomJoin(joinParam);
			if(roomCnt != null && !roomCnt.equals("")){
				if(roomCnt.getJoinCnt() == (roomCnt.getMaxRoomCnt()-1)){
					RoomInfo paramObj = new RoomInfo();
					paramObj.setJoinFnshYn("Y");
					paramObj.setRmSeq(rmSeq);

					yFriendsService.updateRoomInfo(paramObj);
				}
			}
		} catch (RuntimeException e){
			//e.printStackTrace();
			logger.error("API : /na/join, ERROR [방참여 불가] " + e);
			throw new YappException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_JOIN2"));	
		}catch (Exception e){
			//e.printStackTrace();
			logger.error("API : /na/join, ERROR [방참여 불가] " + e);
			throw new YappException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_JOIN2"));	
		}
		return new ResultInfo<>(); 
	}

	@ApiOperation(value="Y프렌즈 모집완료")
	@ResponseBody
	@RequestMapping(value = "/yFriends/finish", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<RoomInfo> invitedFnsh(int evtSeq, int rmSeq, HttpServletRequest req) throws Exception {
		
		String loginCntrNo = "";
		String loginUserId = "";
	    String partyName = "";
	    String memStatus = "";
	    String errorMsg = "";
	    
	    YfriendsMenu yfriendsMenuInfo = null;
	    EventMaster eventDetail = new EventMaster();
	    
		loginCntrNo = SessionKeeper.getCntrNo(req);
        
        if(SessionKeeper.getSdata(req) != null){
            memStatus = SessionKeeper.getSdata(req).getMemStatus();
            partyName = SessionKeeper.getSdata(req).getName();
            loginUserId = SessionKeeper.getSdata(req).getUserId();
        }
         
        logger.info("==============================================================");
        logger.info("yFriends/finish -> memStatus       : "+memStatus);
        logger.info("yFriends/finish -> partyName       : "+partyName);
        logger.info("yFriends/finish -> loginCntrNo     : "+loginCntrNo);
        logger.info("yFriends/finish -> loginUserId     : "+loginUserId);
        logger.info("==============================================================");
		
		ResultInfo<RoomInfo> resultInfo = new ResultInfo<>();
		
        // 이벤트 정보 조회, Y프렌즈 정보, Y프렌즈 방정보
        eventDetail = eventService.getEventDetailMaster(evtSeq);
        yfriendsMenuInfo = yFriendsService.getEventMenu(evtSeq, "N");

        if(yfriendsMenuInfo == null){
            errorMsg = cmnService.getMsg("CHK_Y_END_EVENT");
        }else{
        	
        	RoomChkInfo chkUserResult = checkValidMember(eventDetail, loginCntrNo, loginUserId, "G0001");
            
			if("N".equals(chkUserResult.getTargetYn())){
				RoomJoin roomJoin = new RoomJoin();
				roomJoin.setRmSeq(rmSeq);
				roomJoin.setJoinCntrNo(loginCntrNo);
				roomJoin.setDelYn("Y");
				
				//초대된 인원중에 문제 발생시 방 퇴출
				yFriendsService.updateRoomJoin(roomJoin);
				
				errorMsg = cmnService.getMsg("CHK_Y_NO_EVENT_TARGET");
				throw new YappRuntimeException("CHECK_MSG", errorMsg);
			}
        	
    		List<RoomInvt> roomInvtList = yFriendsService.getRoomChkList(rmSeq);
    		
    		boolean isChanged = false;
    		
    		for(int i=0; i<roomInvtList.size(); i++){
    			
    			RoomChkInfo chkMemResult = checkValidMember(eventDetail, roomInvtList.get(i).getRcvCntrNo(), null, "G0002");
    			
    			if("N".equals(chkMemResult.getTargetYn())){
    				RoomJoin roomJoin = new RoomJoin();
					roomJoin.setRmSeq(rmSeq);
					roomJoin.setJoinCntrNo(roomInvtList.get(i).getRcvCntrNo());
					roomJoin.setDelYn("Y");
					
					//초대된 인원중에 문제 발생시 방 퇴출
					yFriendsService.updateRoomJoin(roomJoin);
					
					isChanged = true;
    			}
    		}
    		
    		if(isChanged){
    			errorMsg = cmnService.getMsg("CHK_Y_NO_FINISH2");
				throw new YappRuntimeException("CHECK_MSG", errorMsg);
    		}
        }
		
		try{
			List<RoomInfo> roomInfoList = new ArrayList<RoomInfo>();
			roomInfoList = yFriendsService.getRoomInfoList(evtSeq, loginCntrNo);
			
			int joinCnt = roomInfoList.get(0).getJoinCnt();
			
			// 방정보 파라미터
			RoomInfo paramObj = new RoomInfo();
			paramObj.setInvitedFnshYn("Y");
			paramObj.setRmSeq(rmSeq);
			
			if("O0002".equals(yfriendsMenuInfo.getGiftOfferType())){
				//선착순 경품일 경우에만 경품잔여수량 체크
				int remainCnt = yFriendsService.getAllGiftRemainCount(evtSeq, joinCnt);
				
				//경품 잔여수량보다 방 신청인원이 많을때만 처리
				if(remainCnt > 0){
					//경품대상방일때 룸정보 업데이트
					paramObj.setGiftRcvYn("Y");
				}else{
					
				}
			}
			
			resultInfo.setResultData(paramObj);
			
			yFriendsService.updateRoomInfo(paramObj);
			
		} catch (RuntimeException e){
			//e.printStackTrace();
			logger.error("API : /yFriends/finish, ERROR [모집완료 불가] " + e);
			throw new YappException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_FINISH"));	
		} catch (Exception e){
			//e.printStackTrace();
			logger.error("API : /yFriends/finish, ERROR [모집완료 불가] " + e);
			throw new YappException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_FINISH"));	
		}
		return resultInfo; 
	}

	@ApiOperation(value="Y프렌즈 친구요청")
	@ResponseBody
	@RequestMapping(value = "/yFriends/invite", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<String> invited(int rmSeq, int evtSeq, String rcvMobileNo, HttpServletRequest req) throws Exception {
		
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		String loginUserId = "";
		String loginMobileNo = null;
		String partyName = "";
		String chkMsg = "";
		
		if(SessionKeeper.getSdata(req) != null){
			loginUserId = SessionKeeper.getSdata(req).getUserId();
			loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
			partyName = SessionKeeper.getSdata(req).getName();
		}

		//상대방 수신거부 체크
		ContractInfo rcvCntrInfo = shubService.callFn139_1(rcvMobileNo).getCntrInfo();
		
		if(YappUtil.isNotEmpty(rcvCntrInfo.getUserInfo())){
			if ( YappUtil.isEq(rcvCntrInfo.getUserInfo().getEvtInvtYn(), EnumYn.C_N.getValue()) ){
				throw new YappException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_INVITE2"));
			}
		}else{
			throw new YappException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_INVITE6"));
		}
		
		try{
			//1. 해당 방에 있는지 체크 
			RoomChkInfo chkRoomResult = checkValidRoom(evtSeq, rmSeq, rcvCntrInfo.getCntrNo(), "G0002");
			EventMaster eventDetail = eventService.getEventDetailMaster(evtSeq);
			RoomChkInfo chkMemResult = checkValidMember(eventDetail, rcvCntrInfo.getCntrNo(), null, "G0002");
			
			if("N".equals(chkRoomResult.getTargetYn())){
				chkMsg = chkRoomResult.getChkMsg();
				throw new YappException("CHECK_MSG", chkMsg);
			}
			
			if("N".equals(chkMemResult.getTargetYn())){
				chkMsg = chkMemResult.getChkMsg();
				throw new YappException("CHECK_MSG", chkMsg);
			}
			
			if(YappUtil.isEmpty(partyName)){
				ContractInfo cntrInfo = shubService.callFn139(loginMobileNo).getCntrInfo();
				partyName= cntrInfo.getOrgUserNm();
			}

			// 방초대 파라미터
			RoomInvt paramObj = new RoomInvt();
			paramObj.setRmSeq(rmSeq);
			paramObj.setAskCntrNo(loginCntrNo);
			paramObj.setAskName(YappUtil.blindNameToName(partyName, 1));
			paramObj.setAskMobileNo(loginMobileNo);
			paramObj.setRcvCntrNo(rcvCntrInfo.getCntrNo());
			paramObj.setRcvMobileNo(rcvMobileNo);
			paramObj.setRcvName(YappUtil.blindNameToName(rcvCntrInfo.getOrgUserNm(), 1));
			paramObj.setAgeChkYn("Y");
			paramObj.setCallingChkYn("Y");
			paramObj.setDeviceChkYn("Y");
		
			yFriendsService.insertRoomInvt(paramObj);
		} catch (RuntimeException e){
			//e.printStackTrace();
			logger.error("API : /yFriends/invite, ERROR [친구요청 불가] " + e);
			if(YappUtil.isEmpty(chkMsg)){
				throw new YappException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_INVITE"));	
			}else{
				throw new YappException("CHECK_MSG", chkMsg);	
			}
			
		} catch (Exception e){
			//e.printStackTrace();
			logger.error("API : /yFriends/invite, ERROR [친구요청 불가] " + e);
			if(YappUtil.isEmpty(chkMsg)){
				throw new YappException("CHECK_MSG", cmnService.getMsg("CHK_Y_NO_INVITE"));	
			}else{
				throw new YappException("CHECK_MSG", chkMsg);	
			}
			
		}
		return new ResultInfo<>(); 
	}
	
	private RoomChkInfo checkValidRoom(int evtSeq, int rmSeq, String cntrNo, String checkType) throws Exception{
		
		RoomChkInfo result = new RoomChkInfo();
		YfriendsMenu evtMenu = yFriendsService.getEventMenu(evtSeq, "Y");
		String chkMsg = "";
		String targetYn = "Y";
	    String targetCode = "01";
		
		try{
			if(YappUtil.isEmpty(evtMenu) || YappUtil.isEmpty(evtMenu.getEndYn()) || "Y".equals(evtMenu.getEndYn())){
				targetYn = "N";
				chkMsg = cmnService.getMsg("CHK_Y_END_EVENT");
				throw new YappRuntimeException("CHECK_MSG", chkMsg);
			}
			
			
			RoomChkInfo roomCnt = new RoomChkInfo();
			
			//방개설시
			if("G0001".equals(checkType)){
				roomCnt = yFriendsService.getJoinCount(cntrNo, rmSeq);
				if(roomCnt != null){
					// 해당방 참여여부
					if(rmSeq != 0){
						if(roomCnt.getRmCnt() > 0){
							targetYn = "N";
							chkMsg = cmnService.getMsg("CHK_Y_ENTER");
							throw new YappRuntimeException("CHECK_MSG", chkMsg);
						}
					}
				}
			}else{
				roomCnt = yFriendsService.getEventJoinCountOne(cntrNo, evtSeq);
				if(roomCnt != null){
					// 해당방 참여여부
					if(rmSeq != 0){
						if(roomCnt.getRmCnt() > 0){
							targetYn = "N";
							chkMsg = cmnService.getMsg("CHK_Y_NO_INVITE3");
							throw new YappRuntimeException("CHECK_MSG", chkMsg);
						}
					}
				}
			}
		} catch(RuntimeException e){
			//e.printStackTrace();
			logger.error("error : " + e);
			if("".equals(chkMsg)){
				chkMsg = cmnService.getMsg("ERR_Y_SYSTEM");
			}
			result.setChkMsg(chkMsg);
		} catch(Exception e){
			//e.printStackTrace();
			logger.error("error : " + e);
			if("".equals(chkMsg)){
				chkMsg = cmnService.getMsg("ERR_Y_SYSTEM");
			}
			result.setChkMsg(chkMsg);
		}
		
		result.setTargetYn(targetYn);
		
		return result;
	}
	
	private RoomChkInfo checkValidMember(EventMaster eventDetail, String rcvCntrNo, String userId, String type) throws Exception{
		
		RoomChkInfo result = new RoomChkInfo();
		
		String ageYn = "Y";
		String callingYn = "Y";
		String deviceYn = "Y";
		String chkMsg = "";
		
		//참여대상 여부 조회
        String targetMem = eventDetail.getTargetMem();
        String targetGender = eventDetail.getGender();
        String targetPpCdList = eventDetail.getPpCdList();
        String targetPpCdL = eventDetail.getPpCdL();
        String targetAgeList = eventDetail.getAgeList();
         
        int targetAgeStartNum = 0;
        int targetAgeEndNum = 0;
         
        if(YappUtil.isNotEmpty(eventDetail.getAgeStartNum())){
            targetAgeStartNum = Integer.parseInt(eventDetail.getAgeStartNum());
        }
         
        if(YappUtil.isNotEmpty(eventDetail.getAgeEndNum())){
            targetAgeEndNum = Integer.parseInt(eventDetail.getAgeEndNum());
        }
        
        logger.info("=========================================================================");
        logger.info("eventDetail checkValidMember -> targetMem: "+targetMem);
        logger.info("eventDetail checkValidMember -> targetGender: "+targetGender);
        logger.info("eventDetail checkValidMember -> targetPpCdList: "+targetPpCdList);
        logger.info("eventDetail checkValidMember -> targetPpCdL: "+targetPpCdL);
        logger.info("eventDetail checkValidMember -> targetAgeList: "+targetAgeList);
        logger.info("eventDetail checkValidMember -> targetAgeStartNum: "+targetAgeStartNum);
        logger.info("eventDetail checkValidMember -> targetAgeEndNum: "+targetAgeEndNum);
        logger.info("=========================================================================");
        
		String loginCntrNo = rcvCntrNo;
		String loginUserId = userId;
		String targetYn = "Y";
	    String targetCode = "01";
		
        UserInfo userInfo = null;
        UserInfo userKtInfo = null;
         
        String gender = "";
        String birthDay = "";
        String ppCd = "";
        String ppCatL = "";
        
        userInfo = userService.getYappUserInfo(loginCntrNo);
        
        if(YappUtil.isNotEmpty(loginUserId)){
            userKtInfo = userService.getYappUserKtInfo(loginUserId);
        }
        
        if(userInfo != null && YappUtil.isEq(userInfo.getJoinStatus(), "G0001")){
        	
        	birthDay = userInfo.getBirthDay();
            ppCd = userInfo.getPpCd();
             
            if(YappUtil.isNotEmpty(ppCd)){
                ppCatL = cmsService.getPpCatLCode(ppCd).getPpCatL();
            }
        	
            if(userKtInfo != null){
                
                userKtInfo = userService.getYappUserKtInfo(loginUserId);
                 
                if(userKtInfo != null){
                    gender = userKtInfo.getGender();
                }
            }
        	
            logger.info("=====================================================");
            logger.info("eventDetail -> gender: "+gender);
            logger.info("eventDetail -> birthDay: "+birthDay);
            logger.info("eventDetail -> ppCd: "+ppCd);
            logger.info("eventDetail -> ppCatL: "+ppCatL);
            logger.info("=====================================================");
            
            if("G0001".equals(type)){
            	
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
                            chkMsg = cmnService.getMsg("CHK_Y_NO_INVITE4");
                        }
                    }else{
                        targetYn = "N";
                        targetCode = "07";
                        chkMsg = cmnService.getMsg("CHK_Y_NO_INVITE4");
                    }
                }
            }else{
                if(targetAgeStartNum !=0 && targetAgeEndNum !=0){
                    if(age==0 || age<targetAgeStartNum || age>targetAgeEndNum){
                        targetYn = "N";
                        targetCode = "08";
                        chkMsg = cmnService.getMsg("CHK_Y_NO_INVITE4");
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
                            chkMsg = cmnService.getMsg("CHK_Y_NO_INVITE5");
                        }
                    }else{
                        targetYn = "N";
                        targetCode = "09";
                        chkMsg = cmnService.getMsg("CHK_Y_NO_INVITE5");
                    }
                }
            }else{
                if(YappUtil.isNotEmpty(targetPpCdL)){
                    if(YappUtil.isEmpty(ppCd) || !targetPpCdL.contains(ppCd)){
                        targetYn = "N";
                        targetCode = "10";
                        chkMsg = cmnService.getMsg("CHK_Y_NO_INVITE5");
                    }
                }
            }
        	
        }else{
    		targetYn = "N";
    	    targetCode = "01";
        }
        
        result.setCntrNo(loginCntrNo);
        result.setPpCd(ppCd);
        result.setAgeChkYn(ageYn);
        result.setCallingChkYn(callingYn);
		result.setDeviceChkYn(deviceYn);
		result.setTargetYn(targetYn);
		result.setTargetCode(targetCode);
		result.setChkMsg(chkMsg);
        
        return result;
	}
	
	private String yFriendsValidationCheck(HttpServletRequest req, String apiUrl) throws Exception{
		
		SysCheck sysCheck = cmsService.getSysChkInfo(null);
		String errorMsg = "";
		if ( YappUtil.isNotEmpty(sysCheck) ){
			logger.error("시스템점검입니다.");
			// throw new YappException("CHECK_MSG","444","시스템점검입니다.");
			return errorMsg = "시스템점검입니다.";
		}
		
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
			logger.error("단말기 정보가 제대로 넘어오지 않았습니다.[ YSID is null ]");
			// throw new YappAuthException("410","로그인 정보가 없습니다.");
			return errorMsg = "로그인 정보가 없습니다.";
		}
		
		// ysid 복호화처리
		boolean sessExistUser = false;
		String decSessionKey = "";
		try {
			decSessionKey = keyFixUtil.decode(sessionId);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			throw new RuntimeException();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			throw new Exception();
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
		if ( YappUtil.isEmpty(mobileNo) 
				|| YappUtil.isEmpty(memStatus)
				|| SessionKeeper.getSdata(req) == null 
				||SessionKeeper.getSdata(req).isExistUser() == false ) 
		{
			logger.error("서버세션에 정보가 없습니다.");
			// throw new YappAuthException("410","로그인 정보가 없습니다.");
			return errorMsg = "로그인 정보가 없습니다.";
		}

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
					logger.error("session dup id = " + tmp[4]);
					logger.error("DB dup id = " + joinInfo.getDupId());
					logger.error("중복 로그인 불가. 다른 기기에서 로그인 되었습니다.");
					// throw new YappAuthException("410","중복 로그인 불가. 다른 기기에서 로그인 되었습니다.");
					return errorMsg = "중복 로그인 불가. 다른 기기에서 로그인 되었습니다.";
				}
			}else{
				logger.error("DUP ID 정보가 없습니다.(해당 계약번호로 TB_USER에 정보가 없음.)");
				// throw new YappAuthException("410","로그인 정보가 없습니다.");
				return errorMsg = "로그인 정보가 없습니다.";
			}
		}
		
		// 계약정보 재로딩
		SessionContractInfo cntrInfo = (SessionContractInfo) SessionKeeper.get(req, SessionKeeper.KEY_CNTR_INFO);
		if ( cntrInfo == null && mobileNo.indexOf("ktid")==-1) {
			SoapResponse139 resp = shubService.callFn139(mobileNo, false);
			SessionKeeper.addToReq(req, SessionKeeper.KEY_CNTR_INFO, YappCvtUtil.cvt(resp.getCntrInfo(), new SessionContractInfo()));
			logger.info("계약정보 재로딩");
		}
		
		return errorMsg;
	}
}