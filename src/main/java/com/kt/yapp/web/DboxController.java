package com.kt.yapp.web;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kt.yapp.config.LimitationPropConfig;
import com.kt.yapp.domain.ContractInfo;
import com.kt.yapp.domain.Databox;
import com.kt.yapp.domain.GiftNum;
import com.kt.yapp.domain.GrpCode;
import com.kt.yapp.domain.UserInfo;
import com.kt.yapp.domain.WsgDataUseQnt;
import com.kt.yapp.domain.resp.DataboxDtlResp;
import com.kt.yapp.domain.resp.ResultInfo;
import com.kt.yapp.em.EnumBlock;
import com.kt.yapp.em.EnumYn;
import com.kt.yapp.exception.YappException;
import com.kt.yapp.service.CmsService;
import com.kt.yapp.service.CommonService;
import com.kt.yapp.service.DboxService;
import com.kt.yapp.service.GiftService;
import com.kt.yapp.service.KosService;
import com.kt.yapp.service.ShubService;
import com.kt.yapp.service.UserService;
import com.kt.yapp.service.WsgService;
import com.kt.yapp.util.AppEncryptUtils;
import com.kt.yapp.util.SessionKeeper;
import com.kt.yapp.util.YappUtil;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 데이터 박스 처리 컨트롤러
 */
@RestController
public class DboxController 
{
	private static final Logger logger = LoggerFactory.getLogger(DboxController.class);
	
	@Autowired
	private CommonService cmnService;
	@Autowired
	private DboxService dboxService;
	@Autowired
	private KosService kosService;
	@Autowired
	private UserService userService;
	@Autowired
	private CmsService cmsService;
	@Autowired
	private GiftService giftService;
	@Autowired
	private WsgService wsgService;
	@Autowired
	private ShubService shubService;
	@Autowired
	private LimitationPropConfig limitPropConfig;
	@Autowired
	private AppEncryptUtils appEncryptUtils;

	@ApiOperation(value="데이터 박스에서 데이터 꺼내기")
	@RequestMapping(value = "/dbox/pulls", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<WsgDataUseQnt> pullDbxData(@RequestParam String cntrNo, @RequestParam int dataAmt, HttpServletRequest req) throws Exception
	{
		List<GrpCode> srvBlockList = cmsService.getYBoxSrvBlockServerList();
		for(GrpCode srvBlock : srvBlockList) {
			logger.info(srvBlock.getCodeId()+", "+srvBlock.getUseYn());
			if(EnumBlock.SB005.getValue().equals(srvBlock.getCodeId()) && EnumYn.C_N.getValue().equals(srvBlock.getUseYn())) {
				throw new YappException("CHECK_MSG", cmnService.getMsg("SRV_BLOCK_DATA"));
			}
		}
		
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		String loginMobileNo = null;
		if(SessionKeeper.getSdata(req) != null){
			loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
		}
		String decCntrNo  = null;
		if(loginMobileNo != null) {
			decCntrNo  = appEncryptUtils.aesDec128(cntrNo, loginMobileNo);
		}
		if ( YappUtil.isNotEq(decCntrNo, loginCntrNo) )
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_NOT_SAME_LOGIN"));	// 로그인된 정보와 일치하지 않습니다.

		Databox dbox = kosService.getDboxInfo(loginCntrNo);
		logger.info("데이터 박스 정보: " + dbox.getDboxPullInfo());

		// 꺼내기 횟수 제한 체크
		if ( dbox.getDboxPullInfo().getDboxPullCnt() >= dbox.getDboxPullInfo().getDboxMaxPullCnt() )
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_DBOX_USE_PULL_CNT"));	// 이번달 꺼내기 횟수를 모두 사용했어요.

		// TODO 꺼내기 용량 제한 체크
		
		// 꺼내기 용량, 데이턱 박스 용량 비교체크
		if ( dataAmt > dbox.getDboxDataAmt() )
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_DBOX_LACK_DATA"));	// 꺼내기 용량이 데이터박스 잔여량 보다 큽니다.
		
		GrpCode grpCode = cmsService.getCodeNm("DATUK_WAIT_TIME", "D0001");		//꺼내기 시간을 공통코드에서 가지고 온다.
		int waitMinute = Integer.parseInt(grpCode.getCodeNm());
		// 직전 꺼내기가 1분 이내에 있었는지 체크
		if ( dbox.getDboxPullInfo().getMaxRegDt() != null )
		{
			Calendar curCal = Calendar.getInstance();
			
			Calendar lastRegCal = Calendar.getInstance();
			lastRegCal.setTime(dbox.getDboxPullInfo().getMaxRegDt());
			lastRegCal.add(Calendar.MINUTE, waitMinute);
			
			if ( curCal.getTimeInMillis() < lastRegCal.getTimeInMillis() ){
				throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_DBOX_PULL_WAIT", YappUtil.makeParamMap("waitMinute", waitMinute)));	// 방금 꺼내기를 했어요. 다시 꺼내시려면 @waitMinute@분만 기다려 주세요.
			}
		}

		dboxService.pullData(decCntrNo, loginMobileNo, dataAmt);
		
		// 내 데이터 정보 조회
		// 계약정보 조회
		ContractInfo cntrInfo = shubService.callFn139(loginMobileNo, true, true).getCntrInfo();
		WsgDataUseQnt dataUseQnt = wsgService.getMobileTotalUseWeb(decCntrNo, loginMobileNo, null, cntrInfo.getCallingPlan());
		return new ResultInfo<>(dataUseQnt);
	}

	@RequestMapping(value = "/dbox", method = RequestMethod.GET)
	@ApiOperation(value="데이터 박스 정보를 조회한다.")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<Databox> getDboxInfo(HttpServletRequest req) throws Exception
	{
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		Databox dbox = kosService.getDboxInfo(loginCntrNo);

		return new ResultInfo<>(dbox);
	}

	@ApiOperation(value="데이터 박스 상세 목록 조회")
	@RequestMapping(value = "/dbox/dtl", method = RequestMethod.GET)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<DataboxDtlResp> getDboxDtlList(
			@ApiParam(value="조회년월 INDEX(당월은 0부터 시작)")	@RequestParam int srchYmIdx
			, HttpServletRequest req) throws Exception
	{
		String loginCntrNo = SessionKeeper.getCntrNo(req);

		Calendar curCal = Calendar.getInstance();
		curCal.add(Calendar.MONTH, -srchYmIdx);
		
		String srchYm = YappUtil.getCurDate(curCal.getTime(), "yyyyMM");
		
		UserInfo paramUserInfo = new UserInfo();
		paramUserInfo.setCntrNo(loginCntrNo);
		
		// 사용자 정보 조회
		UserInfo userInfo = userService.getYappUserInfo(loginCntrNo);
		String joinRegMmDt = userInfo.getRegMmDt();
		//가입월 전 데이터는 조회 못함
		if(joinRegMmDt != null){
			if(Integer.parseInt(srchYm) < Integer.parseInt(joinRegMmDt)){
				throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_DBOX_PRE_MONTH_NO_DATA"));	// 가입월 전 데이터는 조회하실 수 없습니다.
			}
		}

		List<DataboxDtlResp> dtlRespList = new ArrayList<>();
		Databox dboxInfo = kosService.getDboxInfo(loginCntrNo);
		// 선물 받기 가능 데이터 조회
		GiftNum rcvGiftNum = giftService.getGiftRcvNumInfo(loginCntrNo);
		int giftRcvAmt = limitPropConfig.getLmtDboxGiftMaxAmt() - rcvGiftNum.getGiftAmt();
		// 선물 받기 가능 데이터 조회
		dboxInfo.setGiftRcvAmt(giftRcvAmt);
		// 데이터박스 상세 목록 조회
		DataboxDtlResp dtlResp = new DataboxDtlResp();
		dtlResp.setSrchYm(srchYm);
		dtlResp.setDboxInfo(dboxInfo);
		dtlResp.setDataboxDtlList(kosService.getDboxDtlList(loginCntrNo, srchYm));
		dtlRespList.add(dtlResp);
		
		return new ResultInfo<>(dtlRespList);
	}
	
//	@ApiOperation(value="데이터 박스 정보 조회", notes="꺼내기 용량/횟수 체크, 잔여량, 소멸예정 조회")
//	@RequestMapping(value = "/dbox/info", method = RequestMethod.GET)
//	public ResultInfo<Object> getDboxInfo()
//	{
//		ResultInfo<Object> resultInfo = new ResultInfo<>();
//		
//		Map<String, Object> paramMap = new HashMap<>();
//		
//		return resultInfo;
//	}
//	
//	@ApiOperation(value="데이터 박스의 데이터 목록 조회")
//	@RequestMapping(value = "/dbox/datalist", method = RequestMethod.GET)
//	public ResultInfo<Object> getDboxDataList()
//	{
//		ResultInfo<Object> resultInfo = new ResultInfo<>();
//		
//		Map<String, Object> paramMap = new HashMap<>();
//		
//		return resultInfo;
//	}
//	
//	@ApiOperation(value="데이터 박스에 데이터 적립"
//			, notes="데이터 박스 사용가능 요금제 여부, 데이터 박스 적립한도 초과여부")
//	@RequestMapping(value = "/dbox/data", method = RequestMethod.POST)
//	public ResultInfo<Object> saveDboxData()
//	{
//		ResultInfo<Object> resultInfo = new ResultInfo<>();
//		
//		Map<String, Object> paramMap = new HashMap<>();
//		
//		return resultInfo;
//	}
	

}
