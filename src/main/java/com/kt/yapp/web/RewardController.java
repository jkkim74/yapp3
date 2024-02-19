package com.kt.yapp.web;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kt.yapp.domain.CallingPlan;
import com.kt.yapp.domain.ContractInfo;
import com.kt.yapp.domain.DataRewardData;
import com.kt.yapp.domain.DataRewardResult;
import com.kt.yapp.domain.EventGift;
import com.kt.yapp.domain.EventGiftJoin;
import com.kt.yapp.domain.EventMaster;
import com.kt.yapp.domain.GrpCode;
import com.kt.yapp.domain.RewardData;
import com.kt.yapp.domain.TicketGiftRewardData;
import com.kt.yapp.domain.TicketRewardData;
import com.kt.yapp.domain.WsgDataUseQnt;
import com.kt.yapp.domain.resp.ResultInfo;
import com.kt.yapp.em.EnumCntrcStatusCd;
import com.kt.yapp.em.EnumYn;
import com.kt.yapp.exception.YappException;
import com.kt.yapp.exception.YappRuntimeException;
import com.kt.yapp.service.CmsService;
import com.kt.yapp.service.CommonService;
import com.kt.yapp.service.EventService;
import com.kt.yapp.service.KosService;
import com.kt.yapp.service.RewardService;
import com.kt.yapp.service.ShubService;
import com.kt.yapp.service.TicketService;
import com.kt.yapp.service.WsgService;
import com.kt.yapp.util.SessionKeeper;
import com.kt.yapp.util.YappUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "리워드 컨트롤러")
@Controller
public class RewardController {
	
	private static final Logger logger = LoggerFactory.getLogger(RewardController.class);
	
	@Autowired
	private RewardService rewardService;
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private TicketService ticketService;
	
	@Autowired
	private ShubService shubService;
	
	@Autowired
	private CmsService cmsService;
	
	@Autowired
	private CommonService cmnService;
	
	@Autowired
	private KosService kosService;
	
	@Autowired
	private WsgService wsgService;
	
	@ApiOperation(value="리워드 화면호출")
	@RequestMapping(value = "/reward/reward", method = RequestMethod.GET)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header")
	,@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header")
	, @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header")
	, @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")
	})
	public ModelAndView Reward(HttpServletRequest req) throws Exception{
		ModelAndView mv = new ModelAndView();
		String ysid = req.getHeader("ysid");
		String autoLogin = req.getHeader("autoLogin");
		String osTp = req.getHeader("osTp");
		String appVrsn = req.getHeader("appVrsn");
		String memStatus = "";
		String loginUserId = null;
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		
		if(SessionKeeper.getSdata(req) != null){
			memStatus = SessionKeeper.getSdata(req).getMemStatus();
			loginUserId = SessionKeeper.getSdata(req).getUserId().trim();
		}
		
		logger.info("==============================================================");
		logger.info("/reward/reward -> memStatus       : "+memStatus);
		logger.info("/reward/reward -> loginCntrNo     : "+loginCntrNo);
		logger.info("/reward/reward -> loginUserId     : "+loginUserId);
		logger.info("==============================================================");
		
		//로컬 테스트용(서버올릴땐 삭제)
		/*memStatus = "G0001";
		loginCntrNo = "625231423";
		loginUserId = "ydbox16";*/

		EventMaster ticketEventDetail = new EventMaster();
		
		//응모권사용가능여부
		String ticketOpenYn = "Y";
		
		//리스트 개수 조회
		int rewardCnt = 0;
		
		rewardCnt = rewardService.getRewardCnt(loginCntrNo, loginUserId);
		
		//리워드 리스트 조회
		List<RewardData> rewardList=rewardService.getRewardList(loginCntrNo, loginUserId);
			
		//응모권 회차 조회 210817
		ticketEventDetail = eventService.getTicketEventDetail(loginCntrNo);
		
		if(ticketEventDetail == null){
			ticketOpenYn = "N";
		}
		
		//응모권 리워드 리스트 조회 210817
		List<TicketRewardData> ticketRewardList= rewardService.getTicketRewardList(loginCntrNo, loginUserId);
		
		if(YappUtil.isEmpty(rewardList)){
			logger.info("rewardList : "+rewardList);
			logger.info("리워드 정보가 없습니다.");
			rewardList=null;
		}else{
			logger.info("rewardList : "+rewardList);
		}
		
		if(YappUtil.isEmpty(ticketRewardList)){
			logger.info("ticketRewardList : "+ticketRewardList);
			logger.info("응모권 리워드 정보가 없습니다.");
			ticketRewardList=null;
		}else{
			rewardCnt = rewardCnt + ticketRewardList.size();
			logger.info("ticketRewardList : "+ticketRewardList);
			logger.info("ticketRewardList rewardCnt : "+rewardCnt);
		}
		
		//데이터 리워드 리스트 조회 211206
		List<DataRewardData> dataRewardList= rewardService.getDataRewardList(loginCntrNo);
		
		if(YappUtil.isEmpty(dataRewardList)){
			logger.info("dataRewardList : "+dataRewardList);
			logger.info("데이터 리워드 정보가 없습니다.");
			dataRewardList=null;
		}else{
			rewardCnt = rewardCnt + dataRewardList.size();
			logger.info("dataRewardList : "+dataRewardList);
			logger.info("dataRewardList rewardCnt : "+rewardCnt);
		}
		
		//클래스 리워드 리스트 조회
		List<RewardData> classRewardList = rewardService.getClassRewardList(loginCntrNo, loginUserId);
		
		//클래스 유효날짜 확인하여 오늘날짜 비교
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
		 
		Date dToday = new Date();
		Date dRegDt = null;
		
		for(int i = 0; i < classRewardList.size(); i++){
			try {
				dRegDt = format.parse(classRewardList.get(i).getValidEndDtOrg());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			long diffInMilliseconds = dRegDt.getTime() - dToday.getTime();
			long diffInMinutes = diffInMilliseconds / (60 * 1000);
			
			if(diffInMinutes < 0){
				classRewardList.get(i).setClassCancelYn("N");
			}else{
				classRewardList.get(i).setClassCancelYn("Y");
			}
		}
		 
		
		if(YappUtil.isEmpty(classRewardList)){
			logger.info("classRewardList : "+classRewardList);
			logger.info("수강 리워드 정보가 없습니다.");
			classRewardList=null;
		}else{
			rewardCnt = rewardCnt + classRewardList.size();
			logger.info("classRewardList : "+classRewardList);
		}
		
		mv.addObject("classRewardList",classRewardList);
		mv.addObject("dataRewardList",dataRewardList);

		mv.addObject("cntrNo", loginCntrNo);
		mv.addObject("ysid", ysid);
		mv.addObject("autoLogin", autoLogin);
		mv.addObject("osTp", osTp);
		mv.addObject("appVrsn", appVrsn);
		
		mv.addObject("rewardCnt",rewardCnt);
		mv.addObject("rewardList",rewardList);
		mv.addObject("ticketRewardList",ticketRewardList);
		mv.addObject("ticketOpenYn",ticketOpenYn);
		mv.setViewName("reward");
		return mv; 	
	}
	
	/** 210929
	 * 무제한 요금제 조회
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="무제한요금제 조회")
	@ResponseBody
	@RequestMapping(value = "/reward/callingPlan", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<String> callingPlan(HttpServletRequest req) throws Exception{
		ResultInfo<String> resultInfo = new ResultInfo<String>();
		String infYn = "N";
		String loginMobileNo = null;
		String memStatus = "";
		String loginUserId = null;
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		
		if(SessionKeeper.getSdata(req) != null){
			memStatus = SessionKeeper.getSdata(req).getMemStatus();
			loginUserId = SessionKeeper.getSdata(req).getUserId().trim();
			loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
		}
		
		logger.info("==============================================================");
		logger.info("/reward/callingPlan -> memStatus       : "+memStatus);
		logger.info("/reward/callingPlan -> loginCntrNo     : "+loginCntrNo);
		logger.info("/reward/callingPlan -> loginUserId     : "+loginUserId);
		logger.info("==============================================================");
		
		if(YappUtil.isEmpty(loginCntrNo)){
			
			resultInfo.setResultData(infYn);
			return resultInfo;
		}
		
		//요금제 확인 210818
		ContractInfo cntrInfo = shubService.callFn139(loginMobileNo).getCntrInfo();
		
		//211203 보너스데이터 못받는 로직 무제한요금제처럼 데이터쿠폰 상품 뽑기 제외처리 추가 infYn=Y일때 데이터쿠폰 뽑기에서 제외됨
		if ( YappUtil.isNotEq(cntrInfo.getCntrcStatusCd(), EnumCntrcStatusCd.C01.getStatusCd()) ){// 이용정지/일시정지 고객은 보너스 데이터를 받을 수 없어요.
			infYn = "Y";
			logger.info("infYn getCntrcStatusCd : "+cntrInfo.getCntrcStatusCd());

		}else if( YappUtil.isNotEq(cntrInfo.getCallingPlan().getDboxUsePsYn(), EnumYn.C_Y.getValue()) ){// 데이터박스 사용불가 요금제는 보너스 데이터를 받을 수 없습니다.
			infYn = "Y";
			
			logger.info("infYn getDboxUsePsYn : "+cntrInfo.getCallingPlan().getDboxUsePsYn());

		}else{
			logger.info("infYn else");
			CallingPlan cplan = cntrInfo.getCallingPlan();

			//SHUB 요금제 체크
			if(YappUtil.isEq(cplan.getInfYn(), "Y") == true){
				infYn = cplan.getInfYn();
				
			//210923 데이터 무제한 체크 로직 KOS 추가
			}else{
				//데이터 양 체크 KOS
				// 내 데이터 정보 조회
				WsgDataUseQnt dataUseQnt = wsgService.getMobileTotalUseWeb(loginCntrNo, loginMobileNo, null, cntrInfo.getCallingPlan());
				logger.info("getTmonFreeQnt : "+dataUseQnt.getTmonFreeQnt());
	
				if(dataUseQnt.getTmonFreeQnt() == 999999999){
					infYn = "Y";
				}else{
					infYn = "N";
	
				}
			}
			//
		
		}
		logger.info("infYn : "+infYn);
		resultInfo.setResultData(infYn);
		
		return resultInfo;
	}
	
	
	@ApiOperation(value="리워드 상세")
	@ResponseBody
	@RequestMapping(value = "/reward/rewardInfo", method = RequestMethod.GET)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public RewardData RewardInfo(HttpServletRequest req,int rewSeq) throws Exception{
		RewardData rewardInfo=new RewardData();
		String memStatus = "";
		String loginUserId = null;
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		
		if(SessionKeeper.getSdata(req) != null){
			memStatus = SessionKeeper.getSdata(req).getMemStatus();
			loginUserId = SessionKeeper.getSdata(req).getUserId().trim();
		}
		
		logger.info("==============================================================");
		logger.info("/reward/rewardInfo -> memStatus       : "+memStatus);
		logger.info("/reward/rewardInfo -> loginCntrNo     : "+loginCntrNo);
		logger.info("/reward/rewardInfo -> loginUserId     : "+loginUserId);
		logger.info("==============================================================");
		
		//로컬 테스트용(서버올릴땐 삭제)
		/*memStatus = "G0001";
		loginCntrNo = "625231423";
		loginUserId = "ydbox16";*/
		
		
		rewardInfo=rewardService.getRewardInfo(loginCntrNo, loginUserId, rewSeq);
		if(rewardInfo != null){
			if(YappUtil.isEmpty(rewardInfo.getEvtOptionTitle())){
				if("G0001".equals(rewardInfo.getEvtType()) || "G0002".equals(rewardInfo.getEvtType())){
					rewardInfo.setEvtTypeNm("이벤트");
				}
			}else{
				rewardInfo.setEvtTypeNm(rewardInfo.getEvtOptionTitle());
			}
		}
		return rewardInfo;
	}
	
	
	/** 230412
	 * 수강권 리워드 상세
	 * @param req
	 * @param int rewSeq
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="수강권 리워드 상세")
	@ResponseBody
	@RequestMapping(value = "/reward/classRewardInfo", method = RequestMethod.GET)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public RewardData ClassRewardInfo(HttpServletRequest req,int rewSeq) throws Exception{
		RewardData rewardInfo=new RewardData();
		String memStatus = "";
		String loginUserId = null;
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		
		if(SessionKeeper.getSdata(req) != null){
			memStatus = SessionKeeper.getSdata(req).getMemStatus();
			loginUserId = SessionKeeper.getSdata(req).getUserId().trim();
		}
		
		logger.info("==============================================================");
		logger.info("/reward/classRewardInfo -> memStatus       : "+memStatus);
		logger.info("/reward/classRewardInfo -> loginCntrNo     : "+loginCntrNo);
		logger.info("/reward/classRewardInfo -> loginUserId     : "+loginUserId);
		logger.info("==============================================================");
		
		//로컬 테스트용(서버올릴땐 삭제)
		/*memStatus = "G0001";
		loginCntrNo = "625231423";
		loginUserId = "ydbox16";*/
		
		
		rewardInfo=rewardService.getClassRewardInfo(loginCntrNo, loginUserId, rewSeq);
		if(rewardInfo != null){
			if(YappUtil.isEmpty(rewardInfo.getEvtOptionTitle())){
				if("G0001".equals(rewardInfo.getEvtType()) || "G0002".equals(rewardInfo.getEvtType())){
					rewardInfo.setEvtTypeNm("이벤트");
				}
			}else{
				rewardInfo.setEvtTypeNm(rewardInfo.getEvtOptionTitle());
			}
		}
		return rewardInfo;
	}
	
	/** 230413
	 * 수강권 리워드 삭제
	 * @param req
	 * @param int rewSeq, int joinSeq
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="수강권 리워드 삭제")
	@ResponseBody
	@RequestMapping(value = "/reward/deleteClassReward", method = RequestMethod.PUT)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo deleteClassReward(HttpServletRequest req, RewardData rewardData) throws Exception{
		ResultInfo resultInfo = new ResultInfo<>();
		String chkMsg = "";
		String memStatus = "";
		String loginUserId = null;
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		
		if(SessionKeeper.getSdata(req) != null){
			memStatus = SessionKeeper.getSdata(req).getMemStatus();
			loginUserId = SessionKeeper.getSdata(req).getUserId().trim();
		}
		
		logger.info("==============================================================");
		logger.info("/reward/deleteClassReward -> memStatus       : "+memStatus);
		logger.info("/reward/deleteClassReward -> loginCntrNo     : "+loginCntrNo);
		logger.info("/reward/deleteClassReward -> loginUserId     : "+loginUserId);
		logger.info("==============================================================");
		
		//로컬 테스트용(서버올릴땐 삭제)
		/*memStatus = "G0001";
		loginCntrNo = "625231423";
		loginUserId = "ydbox16";*/

		//수강 상품 신청 이력 및 리워드함에서 삭제
		int result = rewardService.deleteClassReward(rewardData.getIssueSeq(), rewardData.getJoinSeq(), rewardData.getRewSeq());
		
		//삭제가 제대로 진행되지 않은 경우
		if(3 > result){
			chkMsg = cmnService.getMsg("CHK_Y_NO_REWARD_DELETE");
			throw new YappException("CHECK_MSG", chkMsg);
		}
		
		resultInfo.setResultCd("200");
		return resultInfo;
	}
	
	/** 210901
	 * 응모권 리워드 상세
	 * @param req
	 * @param giftIssueSeq
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="응모권 리워드 상세")
	@ResponseBody
	@RequestMapping(value = "/reward/ticketRewardInfo", method = RequestMethod.GET)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public TicketGiftRewardData TicketRewardInfo(HttpServletRequest req,int giftIssueSeq, String status) throws Exception{
		TicketGiftRewardData ticketGiftRewardInfo=new TicketGiftRewardData();
		ticketGiftRewardInfo = ticketService.getTicketGiftRewardIssueDetail(giftIssueSeq);
		
		return ticketGiftRewardInfo;
	}
	
	/** 210907
	 * 응모권 리워드 주소입력
	 * @param req
	 * @param giftIssueSeq
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="응모권 리워드 주소입력")
	@ResponseBody
	@RequestMapping(value = "/reward/ticketGiftRewardAddressInfo", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<String> ticketGiftRewardAddressInfo(HttpServletRequest req,TicketGiftRewardData ticketGiftRewardInfo) throws Exception{
		ResultInfo<String> resultInfo = new ResultInfo<String>();
		String chkMsg = "";
		int updateCnt = ticketService.updateTicketGiftRewardAddressInfo(ticketGiftRewardInfo);
		
		if(updateCnt == 0){
			//주소 정보 저장 중 에러가 발생하였습니다. 잠시후 다시 이용해주세요.return
			chkMsg = cmnService.getMsg("CHK_ADDRESS_SAVE_ERROR");
			throw new YappRuntimeException("CHECK_MSG", chkMsg);
		}else{
			resultInfo.setResultData("Y");
		}
		
		return resultInfo;
	}
	
	/**
	 * 210818
	 * @param req
	 * @param ticketSeq
	 * @param infYn
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="리워드 응모권 사용")
	@ResponseBody
	@RequestMapping(value = "/reward/ticketUse", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<TicketRewardData> ticketUse(HttpServletRequest req, int ticketSeq, String infYn) throws Exception{
		ResultInfo<TicketRewardData> resultInfo = new ResultInfo<>();
		TicketGiftRewardData ticketGiftRewardData = new TicketGiftRewardData();

		String memStatus = "";
		String loginUserId = null;
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		
		if(SessionKeeper.getSdata(req) != null){
			memStatus = SessionKeeper.getSdata(req).getMemStatus();
			loginUserId = SessionKeeper.getSdata(req).getUserId().trim();
		}
		
		logger.info("==============================================================");
		logger.info("/reward/ticketUse -> memStatus       : "+memStatus);
		logger.info("/reward/ticketUse -> loginCntrNo     : "+loginCntrNo);
		logger.info("/reward/ticketUse -> loginUserId     : "+loginUserId);
		logger.info("==============================================================");
		String chkMsg = "";
		
		logger.info("infYn : "+infYn);

		//응모권 중복 체크
		TicketRewardData dupChk = ticketService.getTicketRewardInfo(ticketSeq);

		if(dupChk.getGiftIssueSeq() > 0 ){
			//이미 사용한 응모권입니다. 확인 부탁드립니다.문구
			chkMsg = cmnService.getMsg("CHK_TICKET_USED");
			throw new YappRuntimeException("CHECK_MSG", chkMsg);
		}
		
		//1. 회차 조회
		EventMaster ticketEventDetail = new EventMaster();
		//응모권 회차 조회 210817
		ticketEventDetail = eventService.getTicketEventDetail(loginCntrNo);
		
		if(YappUtil.isEmpty(ticketEventDetail)){
			//에러 응모권 상품이 준비중입니다. 다음에 이용해주세요. 문구 
			chkMsg = cmnService.getMsg("CHK_TICKET_GETTING_READY");
			throw new YappRuntimeException("CHECK_MSG", chkMsg);
			
		}else{
			int round = 0;
			
			try{
				round = Integer.parseInt(ticketEventDetail.getEvtSmallTitle());
			}catch(NumberFormatException e){
				chkMsg = cmnService.getMsg("CHK_TICKET_GETTING_READY");
				throw new YappRuntimeException("CHECK_MSG", chkMsg);
			}catch(Exception e){
				chkMsg = cmnService.getMsg("CHK_TICKET_GETTING_READY");
				throw new YappRuntimeException("CHECK_MSG", chkMsg);
			}

			ticketGiftRewardData.setCntrNo(loginCntrNo);
			ticketGiftRewardData.setUserId(loginUserId);
			ticketGiftRewardData.setRound(round);
			ticketGiftRewardData.setTicketSeq(ticketSeq);
			
			//응모권 사용로직 시작 20220107
			TicketRewardData resultData = ticketService.upsertTicketWinNum(ticketGiftRewardData, infYn);
			
			logger.info("/reward/ticketUse resultData : "+resultData);
			
			resultInfo.setResultData(resultData);
			
		}

		return resultInfo;
	}
	
	/**
	 * 20210902
	 * @param req
	 * @param giftIssueSeq
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="리워드 응모권 데이터 지급")
	@ResponseBody
	@RequestMapping(value = "/reward/ticketDataUse", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<TicketGiftRewardData> ticketDataUse(HttpServletRequest req, int giftIssueSeq) throws Exception{
		ResultInfo<TicketGiftRewardData> resultInfo = new ResultInfo<>();
		String loginMobileNo = null;
		String loginCntrNo = null;
		String chkMsg = "";
		TicketGiftRewardData ticketGiftRewardData = new TicketGiftRewardData();
		TicketGiftRewardData ticketRewardData = new TicketGiftRewardData();
		
		if(SessionKeeper.getSdata(req) != null){
			loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
			loginCntrNo = SessionKeeper.getCntrNo(req);
		}
			
		//211109 데이터쿠폰 방어코드 추가
		TicketGiftRewardData ticketGiftReward = ticketService.getTicketGiftRewardIssueDetail(giftIssueSeq);
		
		if(YappUtil.isNotEmpty(ticketGiftReward)){
			//211214 계약번호 방어코드추가
			if(YappUtil.isEq(ticketGiftReward.getCntrNo(), loginCntrNo)){
				
				if(!"Y".equals(ticketGiftReward.getDataGiveYn())){
					//데이터지급 익월로
					//211213 false시 처리 보완
					DataRewardResult dataResult = kosService.receiveTicketBonusData(ticketGiftReward.getCntrNo(), loginMobileNo, 100, "B", "2");
					
					if(dataResult.isKosResult() == true){
						
						ticketGiftRewardData.setDataGiveYn("Y");
						ticketGiftRewardData.setGiftIssueSeq(giftIssueSeq);
						ticketService.updateTicketGiftDataGiveInfo(ticketGiftRewardData);
						
						ticketRewardData = ticketService.getTicketGiftRewardIssueDetail(giftIssueSeq);
						
						resultInfo.setResultData(ticketRewardData);
					
					}else{
						String kosErrMsg = YappUtil.kosResultErrMsg(dataResult.getRtnCd());

						//211213 에러 적재 보완
						throw new YappException("KOS_MSG", dataResult.getRtnCd(), cmnService.getSearchMsg("KOS_ERR_MSG", dataResult.getRtnDesc(), dataResult.getRtnCd(), kosErrMsg), dataResult.getSvc() + dataResult.getRtnDesc(), dataResult.getGlobalNo());
					}
				
				}else{
					//이미사용한 데이터 쿠폰입니다. 확인 부탁드립니다.
					chkMsg = cmnService.getMsg("CHK_DATA_USED_ERROR");
					throw new YappRuntimeException("CHECK_MSG", chkMsg);
				}
			
			}else{
				//로그인된 정보와 일치하지 않습니다. return
				chkMsg = cmnService.getMsg("ERR_NOT_SAME_LOGIN");
				throw new YappRuntimeException("CHECK_MSG", chkMsg);
			}
		
		}else{
			//데이터 지급 중 문제가 발생하였습니다. 잠시후 다시 이용해주세요.return
			chkMsg = cmnService.getMsg("CHK_DATA_USE_ERROR");
			throw new YappRuntimeException("CHECK_MSG", chkMsg);
		}
	
		return resultInfo;
	}
	
	/**
	 * 20211210
	 * @param req
	 * @param dataRewSeq
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="데이터 쿠폰 사용")
	@ResponseBody
	@RequestMapping(value = "/reward/dataRewardUse", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<DataRewardData> dataRewardUse(HttpServletRequest req, int dataRewSeq) throws Exception{
		ResultInfo<DataRewardData> resultInfo = new ResultInfo<>();
		String loginMobileNo = null;
		String loginCntrNo = null;
		String chkMsg = "";
		DataRewardData dataRewardData = new DataRewardData();
		
		if(SessionKeeper.getSdata(req) != null){
			loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
			loginCntrNo = SessionKeeper.getCntrNo(req);
		}
		
		//211221 쿠폰사용시 shub 체크
		ContractInfo cntrInfo = shubService.callFn139(loginMobileNo).getCntrInfo();
		
		//211221 쿠폰사용시 shub 체크
		if ( YappUtil.isNotEq(cntrInfo.getCntrcStatusCd(), EnumCntrcStatusCd.C01.getStatusCd()) ){
			//이용정지/일시정지 고객은 보너스 데이터를 받을 수 없어요.
			chkMsg = cmnService.getMsg("ERR_GIFT_BNS_NOT_VALID");
			throw new YappException("CHECK_MSG", chkMsg);
			
		}else if( YappUtil.isNotEq(cntrInfo.getCallingPlan().getDboxUsePsYn(), EnumYn.C_Y.getValue()) ){
			//Y박스 사용불가 요금제는 보너스 데이터를 받을 수 없습니다.(LTE 요금제만 사용 가능합니다.)
			chkMsg = cmnService.getMsg("ERR_GIFT_UNSVC_BONUS");
			throw new YappException("CHECK_MSG", chkMsg);
			
		}else{
			//데이터지급 당월로
			DataRewardData dataReward = rewardService.getDataRewardDetail(loginCntrNo, dataRewSeq);
			
			if(YappUtil.isNotEmpty(dataReward)){
				if(!"Y".equals(dataReward.getDataGiveYn())){
					
					DataRewardResult dataResult = kosService.receiveTicketBonusData(loginCntrNo, loginMobileNo, dataReward.getDataAmt(),dataReward.getGiftDataCode(), dataReward.getDataExpireType());
					
					if(dataResult.isKosResult() == true){
						
						dataRewardData.setDataGiveYn("Y");
						dataRewardData.setSuccessYn("Y");
						dataRewardData.setDataRewSeq(dataRewSeq);
						rewardService.updateDataGiveInfo(dataRewardData);
						
						dataRewardData = rewardService.getDataRewardDetail(loginCntrNo, dataRewSeq);
						
						resultInfo.setResultData(dataRewardData);
						
					}else{
						
						dataRewardData.setDataGiveYn("N");
						dataRewardData.setSuccessYn("N");
						dataRewardData.setDataRewSeq(dataRewSeq);
						rewardService.updateDataGiveInfo(dataRewardData);
						
						String kosErrMsg = YappUtil.kosResultErrMsg(dataResult.getRtnCd());
						
						throw new YappException("KOS_MSG", dataResult.getRtnCd(), cmnService.getSearchMsg("KOS_ERR_MSG", dataResult.getRtnDesc(), dataResult.getRtnCd(), kosErrMsg), dataResult.getSvc() + dataResult.getRtnDesc(), dataResult.getGlobalNo());
	
					}
				
				}else{
					//이미사용한 데이터 쿠폰입니다. 확인 부탁드립니다.
					chkMsg = cmnService.getMsg("CHK_DATA_USED_ERROR");
					throw new YappRuntimeException("CHECK_MSG", chkMsg);
				}
			
			}else{
				//데이터 지급 중 문제가 발생하였습니다. 잠시후 다시 이용해주세요.return
				chkMsg = cmnService.getMsg("CHK_DATA_USE_ERROR");
				throw new YappRuntimeException("CHECK_MSG", chkMsg);
			}
		}

			
		
		return resultInfo;
	}

}
