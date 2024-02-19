package com.kt.yapp.web;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kt.yapp.config.LimitationPropConfig;
import com.kt.yapp.domain.AccmGiftData;
import com.kt.yapp.domain.BonusData;
import com.kt.yapp.domain.CallingPlan;
import com.kt.yapp.domain.ContractInfo;
import com.kt.yapp.domain.DataReqInfo;
import com.kt.yapp.domain.Databox;
import com.kt.yapp.domain.Datuk;
import com.kt.yapp.domain.GiftData;
import com.kt.yapp.domain.GiftList;
import com.kt.yapp.domain.GiftNum;
import com.kt.yapp.domain.GiftPsbInfo;
import com.kt.yapp.domain.GiftPw;
import com.kt.yapp.domain.GrpCode;
import com.kt.yapp.domain.KmcAuthInfo;
import com.kt.yapp.domain.OwnAuth;
import com.kt.yapp.domain.RecentContact;
import com.kt.yapp.domain.UserInfo;
import com.kt.yapp.domain.WsgDataUseQnt;
import com.kt.yapp.domain.req.GiftDataReq;
import com.kt.yapp.domain.resp.DataDivDtlResp;
import com.kt.yapp.domain.resp.MainViewResp;
import com.kt.yapp.domain.resp.ResultInfo;
import com.kt.yapp.em.EnumBlock;
import com.kt.yapp.em.EnumCntrcStatusCd;
import com.kt.yapp.em.EnumRsltCd;
import com.kt.yapp.em.EnumYn;
import com.kt.yapp.exception.YappAuthException;
import com.kt.yapp.exception.YappException;
import com.kt.yapp.exception.YappRuntimeException;
import com.kt.yapp.service.CmsService;
import com.kt.yapp.service.CommonService;
import com.kt.yapp.service.DboxService;
import com.kt.yapp.service.GiftService;
import com.kt.yapp.service.KosService;
import com.kt.yapp.service.RctContactService;
import com.kt.yapp.service.ShubService;
import com.kt.yapp.service.UserService;
import com.kt.yapp.service.WsgService;
import com.kt.yapp.util.AppEncryptUtils;
import com.kt.yapp.util.SessionKeeper;
import com.kt.yapp.util.YappUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 선물하기 기능 처리 컨트롤러
 */
@RestController
@Api(description="선물하기 처리")
public class GiftController 
{
	private static final Logger logger = LoggerFactory.getLogger(GiftController.class);
	
	@Autowired
	private CommonService cmnService;
	@Autowired
	private UserService userService;
	@Autowired
	private GiftService giftService;
	@Autowired
	private ShubService shubService;
	@Autowired
	private KosService kosService;
	@Autowired
	private CmsService cmsService;
	@Autowired
	private DboxService dboxService;
	@Autowired
	private WsgService wsgService;
	@Autowired
	private RctContactService rctContactService;
	@Autowired
	private AppEncryptUtils appEncryptUtils;
	@Autowired
	private LimitationPropConfig limitPropConfig;


	@RequestMapping(value = "/gift/List", method = RequestMethod.GET)
	@ApiOperation(value="데이턱 정보 데이터 조회")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<GiftList> getDatukDataList(@ApiParam(value="기준날짜") @RequestParam String validStYmd, HttpServletRequest req) throws Exception
	{
		ResultInfo<GiftList> resultInfo = new ResultInfo<>();
		GiftList giftList = new GiftList();
		String loginCntrNo = SessionKeeper.getCntrNo(req);

		GiftData paramDataReq = new GiftData();
		paramDataReq.setSndCntrNo(loginCntrNo);
		
		List<GiftData> sndGiftList = giftService.getGiftInfoList(paramDataReq);
		
		paramDataReq = new GiftData();
		paramDataReq.setSndCntrNo(loginCntrNo);
		List<GiftData> rcvGiftList = giftService.getGiftInfoList(paramDataReq);
		
		paramDataReq = new GiftData();
		paramDataReq.setSndCntrNo(loginCntrNo);
		List<GiftData> pullGiftList = giftService.getPullInfoList(paramDataReq);
		
		giftList.setSndGiftList(sndGiftList);
		giftList.setRcvGiftList(rcvGiftList);
		giftList.setPullGiftList(pullGiftList);
		resultInfo.setResultData(giftList);
		return resultInfo;
	}

	@ApiOperation(value="데이터 조르기")
	@RequestMapping(value = "/gift/req", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<Integer> reqGiftData(
			@ApiParam(value="수신자 휴대폰번호") @RequestParam String rcvMobileNo
			, @ApiParam(value="데이터 용량(MB)") @RequestParam int dataAmt
			, HttpServletRequest req) throws Exception
	{
		String loginMobileNo = null;
		if(SessionKeeper.getSdata(req) != null){
			loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
		}
		String osTp = req.getHeader("osTp");
		String appVrsn = req.getHeader("appVrsn");
		
		rcvMobileNo = appEncryptUtils.aesDec128(rcvMobileNo, osTp+appVrsn);
		
		ContractInfo cntrInfo = shubService.callFn139(loginMobileNo, false, true).getCntrInfo();
		boolean isErrNoCheck = false;
		
		if ( YappUtil.isNotEq(cntrInfo.getCntrcStatusCd(), EnumCntrcStatusCd.C01.getStatusCd()) ){
			throw new YappRuntimeException("CHECK_MSG",cmnService.getMsg("ERR_VALID_LINE"));	// 해당회선이 유효하지 않습니다.
		}
		// 선물하기 가능여부 조회
		if ( YappUtil.isNotEq(cntrInfo.getCallingPlan().getDboxUsePsYn(), EnumYn.C_Y.getValue()) ){
			throw new YappRuntimeException("CHECK_MSG",cmnService.getMsg("ERR_GIFT_REQ_IMP_PL"));	// 조르기 할 수 없는 요금제를 사용 중이에요.
		}
		
		// 수신자 조르기 가능여부 조회
		ContractInfo rcvCntrInfo = null;
		
		try {
			rcvCntrInfo = shubService.callFn139_2(rcvMobileNo).getCntrInfo();
			
			if(YappUtil.isEmpty(rcvCntrInfo.getUserInfo())) {
				//수신자 휴면계정 복구
				UserInfo sleepUserInfo = userService.getSleepUserInfo(rcvCntrInfo.getCntrNo());
				if(YappUtil.isNotEmpty(sleepUserInfo)) {
					isErrNoCheck = true;
					throw new RuntimeException();
				}
			}
		} catch (RuntimeException e) {
			logger.error("GiftController reqGiftData ERROR : " + e);
			if(isErrNoCheck) {
				throw new YappRuntimeException("CHECK_MSG",cmnService.getMsg("ERR_GIFT_SLEEP_USER"));
			}else {
				// 수신자의 계약정보가 없으면 에러
				logger.error("GiftController  reqGiftData callFn139 : ERROR");
			}
		}catch (Exception e) {
			logger.error("GiftController reqGiftData ERROR : " + e);
			if(isErrNoCheck) {
				throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_GIFT_SLEEP_USER"));
			}else {
				// 수신자의 계약정보가 없으면 에러
				logger.error("GiftController  reqGiftData callFn139 : ERROR");
			}
		}
		
		// 수신자가 yapp 사용자여부 체크
		if ( rcvCntrInfo == null || rcvCntrInfo.getUserInfo() == null ){
			throw new YappRuntimeException("CHECK_MSG",cmnService.getMsg("ERR_NOT_JOIN_YAPP"));		// Y앱에 가입되지 않은 사용자입니다.
		}

		// 조르기 가능여부 체크
		if ( YappUtil.isNotEq(rcvCntrInfo.getUserInfo().getReqRcvYn(), EnumYn.C_Y.getValue()) ){
			throw new YappRuntimeException("CHECK_MSG",cmnService.getMsg("ERR_DENY_GIFT_REQ"));		// 조르기를 거부한 고객이에요.
		}

		// 선물하기 가능여부 체크
		if ( YappUtil.isNotEq(rcvCntrInfo.getCallingPlan().getGiftPsYn(), EnumYn.C_Y.getValue()) ){
			throw new YappRuntimeException("CHECK_MSG",cmnService.getMsg("ERR_DENY_GIFT_REQ_IMP_PL"));	// 조르기 할 수 없는 요금제를 사용 중인 고객이에요.
		}

		// 조르기 데이터 추가
		DataReqInfo dataReqParam = new DataReqInfo();
		dataReqParam.setSndCntrNo(cntrInfo.getCntrNo());
		dataReqParam.setSndMobileNo(cntrInfo.getMobileNo());
		dataReqParam.setSndUserNm(YappUtil.blindNameToName(cntrInfo.getUserNm(), 1));
		
		dataReqParam.setRcvCntrNo(rcvCntrInfo.getCntrNo());
		dataReqParam.setRcvMobileNo(rcvCntrInfo.getMobileNo());
		dataReqParam.setRcvUserNm(rcvCntrInfo.getUserNm());
		
		dataReqParam.setDataAmt(dataAmt);
		
		//선물하기 할때 최근연락처에 등록한다.
		RecentContact rctContactParam = new RecentContact();
		rctContactParam.setCntrNo(cntrInfo.getCntrNo());
		rctContactParam.setRecvCntrNo(rcvCntrInfo.getCntrNo());
		rctContactParam.setRecvMobileNo(rcvCntrInfo.getMobileNo());
		rctContactParam.setRecvName(rcvCntrInfo.getUserNm());
		rctContactParam.setDataGiftType("G0002"); //G0002:조르기
		rctContactService.insertRctContact(rctContactParam);
		
		return new ResultInfo<>(giftService.insertDataReq(dataReqParam, appVrsn));
	}

	@ApiOperation(value="데이터 조르기")
	@RequestMapping(value = "/gift/reqList", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<GiftData> reqGiftListData( HttpServletRequest req) throws Exception
	{
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		GiftData paramDataReq = new GiftData();
		paramDataReq.setSndCntrNo(loginCntrNo);
		List<GiftData> sndDataReqList = giftService.getDataReqInfoList(paramDataReq);
		return new ResultInfo<>(sndDataReqList);
	}

	@RequestMapping(value = "/gift/accm", method = RequestMethod.GET)
	@ApiOperation(value="누적 선물 데이터 목록 조회")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<AccmGiftData> getAccmGiftDataList(HttpServletRequest req) throws Exception
	{
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		
		// 누적 선물 데이터 목록 조회
		return new ResultInfo<>(giftService.getAccmGiftDataList(loginCntrNo));
	}
	
	@RequestMapping(value = "/gift/rcv/bonus", method = RequestMethod.POST)
	@ApiOperation(value="보너스 데이터 수령한다. 수령건수를 반환.")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<Integer> receiveBonusData(@RequestParam String cntrNo, @RequestParam String bonusId, HttpServletRequest req) throws Exception
	{
		String mobileNo = null;
		if(SessionKeeper.getSdata(req) != null){
			mobileNo = SessionKeeper.getSdata(req).getMobileNo();
		}
		ContractInfo cntrInfo = shubService.callFn139(mobileNo).getCntrInfo();
		
		if ( YappUtil.isNotEq(cntrNo, cntrInfo.getCntrNo()) )
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_NOT_SAME_LOGIN"));	// 로그인된 정보와 일치하지 않습니다.
		
		if ( YappUtil.isNotEq(cntrInfo.getCntrcStatusCd(), EnumCntrcStatusCd.C01.getStatusCd()) )
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_GIFT_BNS_NOT_VALID"));	// 이용정지/일시정지 고객은 보너스 데이터를 받을 수 없어요.
		
		if ( YappUtil.isNotEq(cntrInfo.getCallingPlan().getDboxUsePsYn(), EnumYn.C_Y.getValue()) )
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_GIFT_UNSVC_BONUS"));	// 데이터박스 사용불가 요금제는 보너스 데이터를 받을 수 없습니다.

		// 수령 보너스 데이터 조회
		List<BonusData> rcvBonusDataList = giftService.getRcvBonusDataList(bonusId, cntrNo);
		
		// 이번달 수령 정보가 있으면 에러
		if ( YappUtil.isNotEmpty(rcvBonusDataList) )
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_GIFT_BNS_RCV_DATA"));	// 이번 달 보너스 데이터를 이미 받으셨어요. 보너스 데이터는 월 1회만 받을 수 있어요.
		
		return new ResultInfo<>(giftService.receiveBonusData(bonusId, cntrNo, mobileNo));
	}

	@RequestMapping(value = "/gift/rcv/bons", method = RequestMethod.POST)
	@ApiOperation(value="보너스 데이터 수령한다. 수령건수를 반환.")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<Integer> receiveBonsData(@RequestParam String cntrNo, @RequestParam String bonusId, HttpServletRequest req) throws Exception
	{
		String mobileNo = null;
		if(SessionKeeper.getSdata(req) != null){
			mobileNo = SessionKeeper.getSdata(req).getMobileNo();
		}
		ContractInfo cntrInfo = shubService.callFn139(mobileNo).getCntrInfo();
		String decCntrNo  = null;
		if(mobileNo != null) {
			decCntrNo  = appEncryptUtils.aesDec128(cntrNo, mobileNo);
		}
		
		if ( YappUtil.isNotEq(decCntrNo, cntrInfo.getCntrNo()) )
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_NOT_SAME_LOGIN"));	// 로그인된 정보와 일치하지 않습니다.
		
		if ( YappUtil.isNotEq(cntrInfo.getCntrcStatusCd(), EnumCntrcStatusCd.C01.getStatusCd()) )
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_GIFT_BNS_NOT_VALID"));	// 이용정지/일시정지 고객은 보너스 데이터를 받을 수 없어요.
		
		if ( YappUtil.isNotEq(cntrInfo.getCallingPlan().getDboxUsePsYn(), EnumYn.C_Y.getValue()) )
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_GIFT_UNSVC_BONUS"));	// 데이터박스 사용불가 요금제는 보너스 데이터를 받을 수 없습니다.

		// 수령 보너스 데이터 조회
		List<BonusData> rcvBonusDataList = giftService.getRcvBonusDataList(bonusId, decCntrNo);
		
		// 이번달 수령 정보가 있으면 에러
		if ( YappUtil.isNotEmpty(rcvBonusDataList) )
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_GIFT_BNS_RCV_DATA"));	// 이번 달 보너스 데이터를 이미 받으셨어요. 보너스 데이터는 월 1회만 받을 수 있어요.
		
		return new ResultInfo<>(giftService.receiveBonusData(bonusId, decCntrNo, mobileNo));
	}

	@RequestMapping(value = "/gift/rcv/bonusnpull", method = RequestMethod.POST)
	@ApiOperation(value="보너스 데이터 수령후 해당데이터 꺼낸다.")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<WsgDataUseQnt> pullBonus(@RequestParam String cntrNo, @RequestParam String bonusId, HttpServletRequest req) throws Exception
	{
		String mobileNo = null;
		if(SessionKeeper.getSdata(req) != null){
			mobileNo = SessionKeeper.getSdata(req).getMobileNo();
		}
		ContractInfo cntrInfo = shubService.callFn139(mobileNo).getCntrInfo();
		
		if ( YappUtil.isNotEq(cntrNo, cntrInfo.getCntrNo()) ){
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_NOT_SAME_LOGIN"));	// 로그인된 정보와 일치하지 않습니다.
		}

		if ( YappUtil.isNotEq(cntrInfo.getCntrcStatusCd(), EnumCntrcStatusCd.C01.getStatusCd()) ){
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_GIFT_BNS_NOT_VALID"));	// 이용정지/일시정지 고객은 보너스 데이터를 받을 수 없어요.
		}

		if ( YappUtil.isNotEq(cntrInfo.getCallingPlan().getDboxUsePsYn(), EnumYn.C_Y.getValue()) ){
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_GIFT_UNSVC_BONUS"));	// 데이터박스 사용불가 요금제는 보너스 데이터를 받을 수 없습니다.
		}

		// 수령 보너스 데이터 조회
		List<BonusData> rcvBonusDataList = giftService.getRcvBonusDataList(bonusId, cntrNo);
		
		// 이번달 수령 정보가 있으면 에러
		if ( YappUtil.isNotEmpty(rcvBonusDataList) ){
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_GIFT_BNS_RCV_DATA"));	// 이번 달 보너스 데이터를 이미 받으셨어요. 보너스 데이터는 월 1회만 받을 수 있어요.
		}

		// 보너스 타입 정보 조회
		BonusData bonusDataInfo = giftService.getBonusData(bonusId);

		Databox dbox = kosService.getDboxInfo(cntrInfo.getCntrNo());
		logger.info("데이터 박스 정보: " + dbox.getDboxPullInfo());

		// 꺼내기 횟수 제한 체크
		if ( dbox.getDboxPullInfo().getDboxPullCnt() >= dbox.getDboxPullInfo().getDboxMaxPullCnt() ){
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_DBOX_USE_PULL_CNT"));	// 이번달 꺼내기 횟수를 모두 사용했어요.
		}

		// 꺼내기 용량, 데이턱 박스 용량 비교체크
		if ( bonusDataInfo.getDataAmt() > dbox.getDboxDataAmt() ){
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_DBOX_LACK_DATA"));	// 꺼내기 용량이 데이터박스 잔여량 보다 큽니다.
		}

		GrpCode grpCode = cmsService.getCodeNm("DATUK_WAIT_TIME", "D0001");		//꺼내기 시간을 공통코드에서 가지고 온다.
		int waitMinute = Integer.parseInt(grpCode.getCodeNm());

		// 직전 꺼내기가 10분 이내에 있었는지 체크
		if ( dbox.getDboxPullInfo().getMaxRegDt() != null ) {
			Calendar curCal = Calendar.getInstance();
			
			Calendar lastRegCal = Calendar.getInstance();
			lastRegCal.setTime(dbox.getDboxPullInfo().getMaxRegDt());
			lastRegCal.add(Calendar.MINUTE, waitMinute);
			
			if ( curCal.getTimeInMillis() < lastRegCal.getTimeInMillis() ){
				throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_DBOX_PULL_WAIT", YappUtil.makeParamMap("waitMinute", waitMinute)));	// 방금 꺼내기를 했어요. 다시 꺼내시려면 @waitMinute@분만 기다려 주세요.
			}
		}

		// 보너스데이터를 받는다.
		giftService.receiveBonusData(bonusId, cntrNo, mobileNo);
		// 데이터박스에서 데이터를 꺼낸다.
		dboxService.pullData(cntrNo, mobileNo, bonusDataInfo.getDataAmt());
		// 계약정보 조회
		WsgDataUseQnt dataUseQnt = wsgService.getMobileTotalUseWeb(cntrNo, mobileNo, null, cntrInfo.getCallingPlan());
		return new ResultInfo<>(dataUseQnt);
	}

	@ApiOperation(value="선물 가능 정보를 조회한다.")
	@RequestMapping(value = "/gift/psbinfo", method = RequestMethod.GET)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<GiftPsbInfo> getGiftPsbInfo(HttpServletRequest req) throws Exception
	{
		String loginMobileNo = null;
		if(SessionKeeper.getSdata(req) != null){
			loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
		}
		ContractInfo cntrInfo = shubService.callFn139(loginMobileNo).getCntrInfo();
		
		return new ResultInfo<>(giftService.getGiftPsbInfo(cntrInfo));
	}
	
	@ApiOperation(value="데이터 나눔 상세 목록 조회")
	@RequestMapping(value = "/gift/dtl", method = RequestMethod.GET)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<DataDivDtlResp> getDataDivDtlList(
			@ApiParam(value="조회년월 INDEX(당월은 0부터 시작)")	@RequestParam int srchYmIdx
			, HttpServletRequest req) throws Exception
	{
		List<DataDivDtlResp> dtlRespList = new ArrayList<>();
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		
		Calendar curCal = Calendar.getInstance();
		curCal.add(Calendar.MONTH, -srchYmIdx);
		
		String srchYm = YappUtil.getCurDate(curCal.getTime(), "yyyyMM");
		// 사용자 정보 조회
		UserInfo userInfo = userService.getYappUserInfo(loginCntrNo);
		String joinRegMmDt = userInfo.getRegMmDt();
		//가입월 전 데이터는 조회 못함
		if(joinRegMmDt != null){
			if(Integer.parseInt(srchYm) < Integer.parseInt(joinRegMmDt)){
				throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_DBOX_PRE_MONTH_NO_DATA"));	// 가입월 전 데이터는 조회하실 수 없습니다.
			}
		}
		// 데이터박스 상세 목록 조회
		DataDivDtlResp dtlResp = new DataDivDtlResp();
		dtlResp.setSrchYm(srchYm);
		dtlResp.setDataDivDtlList(kosService.getDataDivDtlList(loginCntrNo, srchYm));
		dtlRespList.add(dtlResp);
		
		return new ResultInfo<>(dtlRespList);
	}

	@ApiOperation(value="선물하기 비번 사용유무")
	@RequestMapping(value = "/gift/getGiftPwYn", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public GiftPw getGiftPwYn(String cntrNo, HttpServletRequest req) throws Exception
	{
		logger.info("====================================[parameter]=============================================");
		logger.info("cntrNo : " + cntrNo);
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		int chkCount = giftService.checkGiftPw(loginCntrNo, "");
		String reCheckYn = "N";
		if(chkCount > 0){
			reCheckYn = "Y";
		}
		GiftPw giftPwDomain = new GiftPw();
		giftPwDomain.setCheckYn(reCheckYn);
		logger.info("====================================[result]=============================================");
		logger.info("chkCount : " + chkCount);
		logger.info("reCheckYn : " + reCheckYn);
		logger.info("=================================================================================");
		return giftPwDomain;
	}

	@ApiOperation(value="선물하기 비번를 체크(존재여부)")//giftPw 없이 Request
	@RequestMapping(value = "/gift/checkGiftPw", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public GiftPw checkGiftPw(String cntrNo, @ApiParam(value="SHA256 암호화된 비밀번호")	@RequestParam String giftPw, HttpServletRequest req) throws Exception
	{
		logger.info("====================================[parameter]=============================================");
		logger.info("cntrNo : " + cntrNo + ", giftPw : " + giftPw);
		String loginCntrNo = SessionKeeper.getCntrNo(req);		
		
		//220420
		logger.info("loginCntrNo : " + loginCntrNo);
		if(YappUtil.isEmpty(loginCntrNo)){
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_Y_SYSTEM"));
		}
		
		int chkCount = giftService.checkGiftPw(loginCntrNo, giftPw);
		String reCheckYn = "N";
		if(chkCount > 0){
			reCheckYn = "Y";
		}
		GiftPw giftPwDomain = new GiftPw();
		giftPwDomain.setCheckYn(reCheckYn);
		logger.info("====================================[result]=============================================");
		logger.info("chkCount : " + chkCount);
		logger.info("reCheckYn : " + reCheckYn);
		logger.info("=================================================================================");
		return giftPwDomain;
	}
	
	@ApiOperation(value="선물하기 비번를 체크(비교)")//giftPw가 필수 파라미터로 Request
	@RequestMapping(value = "/gift/checkCompGiftPw", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public GiftPw checkCompGiftPw(String cntrNo, @ApiParam(value="SHA256 암호화된 비밀번호")	@RequestParam String giftPw, HttpServletRequest req) throws Exception
	{
		logger.info("====================================[parameter]=============================================");
		logger.info("cntrNo : " + cntrNo + ", giftPw : " + giftPw);
		String loginCntrNo = SessionKeeper.getCntrNo(req);		
		
		//220420
		logger.info("loginCntrNo : " + loginCntrNo);
		if(YappUtil.isEmpty(loginCntrNo)){
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_Y_SYSTEM"));
		}
		
		if(null == giftPw || "".equals(giftPw) ){
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_NOT_SAME_PW"));
		}
		
		int chkCount = giftService.checkGiftPw(loginCntrNo, giftPw);		
		String reCheckYn = "N";
		if(chkCount > 0){
			reCheckYn = "Y";
		}
		GiftPw giftPwDomain = new GiftPw();
		giftPwDomain.setCheckYn(reCheckYn);
		logger.info("====================================[result]=============================================");
		logger.info("chkCount : " + chkCount);
		logger.info("reCheckYn : " + reCheckYn);
		logger.info("=================================================================================");
		return giftPwDomain;
	}

	//210624현재는 호출안하는 API
	@ApiOperation(value="선물하기 비번를 설정")
	@RequestMapping(value = "/gift/setGiftPw", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo setGiftPw(String cntrNo, @ApiParam(value="SHA256 암호화된 비밀번호")	@RequestParam String giftPw, HttpServletRequest req) throws Exception
	{
		logger.info("====================================[parameter]=============================================");
		logger.info("cntrNo : " + cntrNo + ", giftPw : " + giftPw);
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		
		//220420
		logger.info("loginCntrNo : " + loginCntrNo);
		if(YappUtil.isEmpty(loginCntrNo)){
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_Y_SYSTEM"));
		}
		
		int chkCount = giftService.checkGiftPw(loginCntrNo, "");
		if(chkCount > 0){
			giftService.updateGiftPw(loginCntrNo, giftPw);
		} else {
			giftService.insertGiftPw(loginCntrNo, giftPw);
		}
		return new ResultInfo<>();
	}

	@ApiOperation(value="선물하기 비번를 삭제")
	@RequestMapping(value = "/gift/giftPw", method = RequestMethod.DELETE)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo deleteGiftPw(HttpServletRequest req) throws Exception
	{
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		
		giftService.deleteGiftPw(loginCntrNo);
		return new ResultInfo<>();
	}

	// 2018.11.14 보안진단 수정사항
	// cntrNo 세션사용
	//20210622 모의해킹 취약점 검출 최초 본인인증 우회시, 패스워드변경가능부분을 본인인증 로직 탔는지 체크 로직 추가
	@ApiOperation(value="선물하기 비번를 설정")
	@RequestMapping(value = "/gift/setGiftPwNew", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo setGiftPwNew(@ApiParam(value="SHA256 암호화된 기존비밀번호")	@RequestParam String giftOldPw ,@ApiParam(value="SHA256 암호화된 새비밀번호")	@RequestParam String giftPw, HttpServletRequest req) throws Exception
	{
		if(YappUtil.isEmpty(giftOldPw)){
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_Y_SYSTEM"));
		}
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		
		int chkCount = giftService.checkGiftPw(loginCntrNo, "");
		if(chkCount > 0){
			int chkPwCount = giftService.checkGiftPw(loginCntrNo, giftOldPw);
			if(chkPwCount < 1){
				throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_NOT_SAME_PW"));
			}
			giftService.updateGiftPw(loginCntrNo, giftPw);
		} else {
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_NOT_PW_NEW")); //210624 기존패스가 있는 사람들만 호출하는 API에 패스가 없을 경우
		}
		return new ResultInfo<>();
	}
	
	@ApiOperation(value="데이터나눔 비밀번호 재설정")
	@RequestMapping(value = "/gift/reSetGiftPw", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo reSetGiftPw(@RequestBody OwnAuth ownAuth, HttpServletRequest req) throws Exception {
		// 로그인 휴대폰 번호 세팅
		if(SessionKeeper.getSdata(req) != null){
			ownAuth.setMobileNo(SessionKeeper.getSdata(req).getMobileNo());
		}
		// 인증 확인
		shubService.callFn087(ownAuth);
		System.out.println(ownAuth.getGiftPw());
		
		String cntrNo = SessionKeeper.getCntrNo(req);
		
		int chkCount = giftService.checkGiftPw(cntrNo, "");
		if(chkCount > 0){
			giftService.updateGiftPw(cntrNo, ownAuth.getGiftPw());
		} else {
			giftService.insertGiftPw(cntrNo, ownAuth.getGiftPw());
		}
		
		return new ResultInfo<>();
	}

	// KMC 본인인증 처리 후 비밀번호 재설정 
	// 2019.12.10 
	@ApiOperation(value="데이터나눔 비밀번호 재설정(KMC사용)")
	@RequestMapping(value = "/gift/reGiftPwByKmc", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header")
					  , @ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header")
					  , @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header")
					  , @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo reGiftPwByKmc(@RequestBody OwnAuth ownAuth, HttpServletRequest req) throws Exception {
		String kmcAuthSeq = ownAuth.getKmcAuthSeq();
		String phoneNumber = ownAuth.getPhoneNumber();
		
		if(YappUtil.isEmpty(kmcAuthSeq)) {
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_NOT_EXIST_AUTH_SEQ"));
		}
		
		if(YappUtil.isEmpty(phoneNumber)) {
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_NOT_EXIST_PHONE_NUMBER"));
		}
		
		logger.info("params >>>>>>>>");
		logger.info("kmcAuthSeq :: " + kmcAuthSeq);
		logger.info("phoneNumber :: " + phoneNumber);
		
		/** 
		 * PLAN-420 
		 * - 암호화대상 필드에 인코딩하여 단말로 전송, 단말에서 그대로 서버단 재전송(자동 디코딩 되므로 추가 디코딩 작업 없음) 
		 * - PASS 비빌번호 변경시 체크 - 입력받은 휴대폰번호와 등록된 휴대폰번호를 비교하여 같을 경우만 허용
		 */
		String num = appEncryptUtils.aesDec(phoneNumber);
		String mobileNo = null;
		if(SessionKeeper.getSdata(req) != null){
			mobileNo = SessionKeeper.getSdata(req).getMobileNo();
		}
		
		logger.info("phoneNumber(INPUT DATA) :: " + num);
		logger.info("phoneNumber(USER  DATA) :: " + mobileNo);
		
		if(mobileNo != null && (!mobileNo.equals(num))) {
			logger.info("ERROR CODE :: ERR_KMS_SMS_SEND");
			if(cmnService.getMsg("ERR_KMS_SMS_SEND")==null) {
				logger.info("ERROR MSG  :: 본인정보가 일치하지 않습니다. 동일한 휴대폰번호로 인증하여 주세요.");
				throw new YappException("CHECK_MSG", "본인정보가 일치하지 않습니다. 동일한 휴대폰번호로 인증하여 주세요.");
			}
			else {
				logger.info("ERROR MSG  :: " + cmnService.getMsg("ERR_KMS_SMS_SEND"));
				throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_KMS_SMS_SEND"));
			}
		}
		
		
		KmcAuthInfo paramObj = new KmcAuthInfo();
		paramObj.setKmcAuthSeq(kmcAuthSeq);
		paramObj.setPhoneNumber(num);

		KmcAuthInfo kmcAuthInfo = cmnService.getKmcAuthInfo(paramObj);
		if(kmcAuthInfo != null){
			if(!kmcAuthInfo.getResult().equals("Y")){
				throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_NOT_KMC_AUTH"));
			}
		}
		String cntrNo = SessionKeeper.getCntrNo(req);
		
		int chkCount = giftService.checkGiftPw(cntrNo, "");
		if(chkCount > 0){
			giftService.updateGiftPw(cntrNo, ownAuth.getGiftPw());
		} else {
			giftService.insertGiftPw(cntrNo, ownAuth.getGiftPw());
		}
		
		return new ResultInfo<>();
	}
	
	// KMC 본인인증 처리 후 비밀번호 재설정 
	// 2021.04.05 
	@ApiOperation(value="데이터나눔 비밀번호 재설정(KMC사용)")
	@RequestMapping(value = "/gift/reGiftEncPwByKmc", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header")
					  , @ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header")
					  , @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header")
					  , @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo reGiftEncPwByKmc(@RequestBody OwnAuth ownAuth, HttpServletRequest req) throws Exception {
		String kmcAuthSeq = ownAuth.getKmcAuthSeq();
		String phoneNumber = ownAuth.getPhoneNumber();
		
		if(YappUtil.isEmpty(kmcAuthSeq)) {
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_NOT_EXIST_AUTH_SEQ"));
		}
		
		if(YappUtil.isEmpty(phoneNumber)) {
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_NOT_EXIST_PHONE_NUMBER"));
		}
		
		logger.info("params >>>>>>>>");
		logger.info("kmcAuthSeq :: " + kmcAuthSeq);
		logger.info("phoneNumber :: " + phoneNumber);
		
		/** 
		 * PLAN-420 
		 * - 암호화대상 필드에 인코딩하여 단말로 전송, 단말에서 그대로 서버단 재전송(자동 디코딩 되므로 추가 디코딩 작업 없음) 
		 * - PASS 비빌번호 변경시 체크 - 입력받은 휴대폰번호와 등록된 휴대폰번호를 비교하여 같을 경우만 허용
		 */
		String num = appEncryptUtils.aesDec(phoneNumber);
		String mobileNo = null;
		if(SessionKeeper.getSdata(req) != null){
			mobileNo = SessionKeeper.getSdata(req).getMobileNo();
		}
		
		logger.info("phoneNumber(INPUT DATA) :: " + num);
		logger.info("phoneNumber(USER  DATA) :: " + mobileNo);
		
		if(mobileNo != null && (!mobileNo.equals(num))) {
			logger.info("ERROR CODE :: ERR_KMS_SMS_SEND");
			if(cmnService.getMsg("ERR_KMS_SMS_SEND")==null) {
				logger.info("ERROR MSG  :: 본인정보가 일치하지 않습니다. 동일한 휴대폰번호로 인증하여 주세요.");
				throw new YappException("CHECK_MSG", "본인정보가 일치하지 않습니다. 동일한 휴대폰번호로 인증하여 주세요.");
			}
			else {
				logger.info("ERROR MSG  :: " + cmnService.getMsg("ERR_KMS_SMS_SEND"));
				throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_KMS_SMS_SEND"));
			}
		}
		
		
		KmcAuthInfo paramObj = new KmcAuthInfo();
		paramObj.setKmcAuthSeq(kmcAuthSeq);
		paramObj.setPhoneNumber(num);

		KmcAuthInfo kmcAuthInfo = cmnService.getKmcAuthInfo(paramObj);
		if(kmcAuthInfo != null){
			if(!kmcAuthInfo.getResult().equals("Y")){
				throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_NOT_KMC_AUTH"));
			}
		}
		String cntrNo = SessionKeeper.getCntrNo(req);
		
		String osTp = req.getHeader("osTp");
		String appVrsn = req.getHeader("appVrsn");
		
		int chkCount = giftService.checkGiftPw(cntrNo, "");
		String decPwd = "";
		try {
			String giftPw = ownAuth.getGiftPw();
			decPwd = appEncryptUtils.aesDec128(giftPw, osTp + appVrsn);
		} catch (RuntimeException e) {
			// TODO: handle exception
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_NOT_KMC_AUTH"));
		} catch (Exception e) {
			// TODO: handle exception
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_NOT_KMC_AUTH"));
		}
		if(chkCount > 0){
			giftService.updateGiftPw(cntrNo, decPwd);
		} else {
			giftService.insertGiftPw(cntrNo, decPwd);
		}
		
		return new ResultInfo<>();
	}

	@ApiOperation(value="선물하기")
	@RequestMapping(value = "/giftData", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<String> giftData(@RequestBody GiftDataReq giftData, HttpServletRequest req) throws Exception
	{
		if(null == giftData.getGiftPw() || "".equals(giftData.getGiftPw()) ){
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_NOT_SAME_PW"));
		}
		
		List<GrpCode> srvBlockList = cmsService.getYBoxSrvBlockServerList();
		for(GrpCode srvBlock : srvBlockList) {
			logger.info(srvBlock.getCodeId()+", "+srvBlock.getUseYn());
			if(EnumBlock.SB006.getValue().equals(srvBlock.getCodeId()) && EnumYn.C_N.getValue().equals(srvBlock.getUseYn())) {
				throw new YappException("CHECK_MSG", cmnService.getMsg("SRV_BLOCK_GIFT"));
			}
		}
		
		String mobileNo = null;
		if(SessionKeeper.getSdata(req) != null){
			mobileNo = SessionKeeper.getSdata(req).getMobileNo();
		}
		
		String osTp = req.getHeader("osTp");
		String appVrsn = req.getHeader("appVrsn");
		
		ContractInfo cntrInfo = shubService.callFn139(mobileNo).getCntrInfo();
		String decSndCntrNo  = null;
		if(mobileNo != null) {
			decSndCntrNo  = appEncryptUtils.aesDec128(giftData.getSndCntrNo(), mobileNo);
		}
		
		if(!YappUtil.isEmpty(giftData.getRcvMobileNo())){
			giftData.setRcvMobileNo(appEncryptUtils.aesDec128(giftData.getRcvMobileNo(), osTp+appVrsn));
			logger.info("giftData 수신자모바일 복호화 : "+giftData.getRcvMobileNo());
		}
		
		boolean isErrNoCheck = false;
		
		// 선물하기 전 비밀번호 다시 체크		
		int chkCount = giftService.checkGiftPw(cntrInfo.getCntrNo(), giftData.getGiftPw());
		if(chkCount < 1){
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_NOT_SAME_PW"));
		}
		
		if ( YappUtil.isNotEq(cntrInfo.getCntrcStatusCd(), EnumCntrcStatusCd.C01.getStatusCd()) ){
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_VALID_LINE"));	// 해당회선이 유효하지 않습니다.
		}

		////////// 발신자 체크 ////////
		if ( YappUtil.isNotEq(decSndCntrNo, cntrInfo.getCntrNo()) ){
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_NOT_SAME_LOGIN"));	// 로그인된 정보와 일치하지 않습니다.
		}

		// 선물 가능 정보를 체크한다.
		GiftPsbInfo giftPsbInfo = giftService.checkGiftPsbInfo(cntrInfo, giftData.getDataAmt());
		
		////////// 수신자 체크 ////////
		ContractInfo rcvCntrInfo = null;
		try {
			rcvCntrInfo = shubService.callFn139_2(giftData.getRcvMobileNo()).getCntrInfo();
			
			if(YappUtil.isEmpty(rcvCntrInfo.getUserInfo())) {
				//수신자 휴면계정 복구
				UserInfo sleepUserInfo = userService.getSleepUserInfo(rcvCntrInfo.getCntrNo());
				if(YappUtil.isNotEmpty(sleepUserInfo)) {
					isErrNoCheck = true;
					throw new RuntimeException();
				}
			}
			
			// Y앱 가입정보 체크. 없으면 에러
			if ( userService.getYappUserInfo(rcvCntrInfo.getCntrNo()) == null )
				throw new YappRuntimeException();
		} catch (RuntimeException e) {
			logger.error("/giftData ERROR" + e);
			if(isErrNoCheck) {
				throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_GIFT_SLEEP_USER"));
			}else {
				// 수신자의 계약정보가 없으면 에러
				throw new YappException("CHECK_MSG",cmnService.getMsg(
					"ERR_GIFT_NOT_JOIN_YAPP", YappUtil.makeParamMap("rcvUserNm", giftData.getRcvUserNm())));	// [@rcvUserNm@] 님은 Y앱에 가입되지않은 고객입니다.\n데이터 선물이 취소되었습니다.
			}
		}catch (Exception e) {
			logger.error("/giftData ERROR" + e);
			if(isErrNoCheck) {
				throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_GIFT_SLEEP_USER"));
			}else {
				// 수신자의 계약정보가 없으면 에러
				throw new YappException("CHECK_MSG",cmnService.getMsg(
					"ERR_GIFT_NOT_JOIN_YAPP", YappUtil.makeParamMap("rcvUserNm", giftData.getRcvUserNm())));	// [@rcvUserNm@] 님은 Y앱에 가입되지않은 고객입니다.\n데이터 선물이 취소되었습니다.
			}
		}
		// 수신자 요금제 체크
		CallingPlan rcvCplan = rcvCntrInfo.getCallingPlan();
		if ( rcvCplan == null || YappUtil.isY(rcvCplan.getDboxUsePsYn()) == false ){
			throw new YappException("CHECK_MSG",cmnService.getMsg(
					"ERR_GIFT_UNSVC_RCV", YappUtil.makeParamMap("rcvUserNm", giftData.getRcvUserNm())));	// @rcvUserNm@님에게 선물할 수 없습니다.\n선물받을 수 없는 요금제를 사용중입니다.
		}
		// 수신자 적립한도 체크
		GiftNum rcvGiftNum = giftService.getGiftRcvNumInfo(rcvCntrInfo.getCntrNo());
		logger.info("선물 받은 정보 : " + rcvGiftNum);

		if ( rcvGiftNum.getGiftAmt() >= limitPropConfig.getLmtDboxGiftMaxAmt() ){
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_DATUK_EXD_SAVE1", YappUtil.makeParamMap("rcvUserNm", giftData.getRcvUserNm())));	// 데이터를 선물할 수 없습니다. @rcvUserNm@님은 이번달 선물 받을 수 있는 용량을 이미 모두 받았습니다.
		}

		int amtTmp = limitPropConfig.getLmtDboxGiftMaxAmt() - rcvGiftNum.getGiftAmt();
		//if ( (rcvGiftNum.getGiftAmt() + giftData.getDataAmt()) > limitPropConfig.getLmtDboxGiftMaxAmt() ){
		if ( amtTmp <  giftData.getDataAmt()){
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_DATUK_EXD_SAVE2", YappUtil.makeParamMap(new String[]{"rcvUserNm", "rcvAmt"}, new Object[]{ giftData.getRcvUserNm(), amtTmp})));	//데이터를 선물할 수 없습니다. @rcvUserNm@님은 이번달에 @rcvAmt@MB만 더 선물 받을 수 있습니다. 용량을 변경해주세요.
		}
		// 선물 데이터를 추가한다.
		GiftData giftDataParam = new GiftData();
		giftDataParam.setSndCntrNo(cntrInfo.getCntrNo());
		giftDataParam.setSndMobileNo(cntrInfo.getMobileNo());
		giftDataParam.setSndUserNm(YappUtil.blindNameToName(cntrInfo.getUserNm(), 1));
		
		giftDataParam.setRcvCntrNo(rcvCntrInfo.getCntrNo());
		giftDataParam.setRcvMobileNo(rcvCntrInfo.getMobileNo());
		giftDataParam.setRcvUserNm(YappUtil.blindNameToName(rcvCntrInfo.getUserNm(), 1));
		
		giftDataParam.setDataAmt(giftData.getDataAmt());
		giftDataParam.setRmnDataAmt(giftPsbInfo.getRmnDataAmt());
		giftService.insertGiftData(giftDataParam, appVrsn);
		
		//선물하기 할때 최근연락처에 등록한다.
		RecentContact rctContactParam = new RecentContact();
		rctContactParam.setCntrNo(cntrInfo.getCntrNo());
		rctContactParam.setRecvCntrNo(rcvCntrInfo.getCntrNo());
		rctContactParam.setRecvMobileNo(rcvCntrInfo.getMobileNo());
		rctContactParam.setRecvName(rcvCntrInfo.getUserNm());
		rctContactParam.setDataGiftType("G0001"); //G0001:선물하기
		rctContactService.insertRctContact(rctContactParam);

		return new ResultInfo<>();
	}
}