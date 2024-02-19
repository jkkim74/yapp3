package com.kt.yapp.web;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kt.yapp.domain.CallingPlan;
import com.kt.yapp.domain.ContractInfo;
import com.kt.yapp.domain.Roaming;
import com.kt.yapp.domain.resp.ResultInfo;
import com.kt.yapp.exception.YappException;
import com.kt.yapp.service.CommonService;
import com.kt.yapp.service.ShubService;
import com.kt.yapp.service.YRoamingService;
import com.kt.yapp.util.SessionKeeper;
import com.kt.yapp.util.YappUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "Y로밍 컨트롤러")
@RestController
public class YRoamingController 
{
	private static final Logger logger = LoggerFactory.getLogger(YRoamingController.class);

	@Autowired
	private ShubService shubService;

	@Autowired
	private CommonService cmnService;	

	@Autowired
	private YRoamingService yRoamingService;	
	
	@ApiOperation(value="Y로밍 조회")
	@RequestMapping(value = "/yRoaming/roaming", method = RequestMethod.GET)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<Roaming> getYRoaming(HttpServletRequest req) throws Exception {
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		String loginMobileNo = null;
		if(SessionKeeper.getSdata(req) != null){
			loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
		}
		ContractInfo cntrInfo = shubService.callFn139(loginMobileNo).getCntrInfo();
		CallingPlan cplan = cntrInfo.getCallingPlan();

		List<Roaming> roamingList = yRoamingService.getRoaming(loginCntrNo, cplan.getPpCatL());
		return new ResultInfo<>(roamingList); 
	}

	@ApiOperation(value="Y로밍 조회 TEST")
	@RequestMapping(value = "/na/yRoaming/roaming", method = RequestMethod.GET)
	public ResultInfo<Roaming> getMaYRoaming(String cntrNo, String pp, HttpServletRequest req) throws Exception {
		List<Roaming> roamingList = yRoamingService.getRoaming(cntrNo, pp);
		return new ResultInfo<>(roamingList); 
	}

	@ApiOperation(value="Y로밍 신청 TEST")
	@RequestMapping(value = "/na/yRoaming/roaming", method = RequestMethod.POST)
	public ResultInfo<Roaming> setNaYRoaming(String cntrNo, String mobileNo,  String pp, String eggYn, String vasItemCd, String vasItemId, String efctStDt, String efctFnsDt, HttpServletRequest req) throws Exception {

		String prodSbscTrmnCd = "A";
		vasChk(cntrNo, mobileNo);
		yRoamingService.setRoaming(cntrNo, vasItemCd, vasItemId, efctStDt, efctFnsDt, prodSbscTrmnCd, "0");
		return new ResultInfo<>(); 
	}

	@ApiOperation(value="Y로밍 신청")
	@RequestMapping(value = "/yRoaming/roaming", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header")
					  , @ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header")
					  , @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header")
					  , @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<Roaming> setYRoaming(String prodHstSeq, String vasItemCd, String vasItemId, String efctStDt, String efctFnsDt, HttpServletRequest req) throws Exception {
		
		logger.info("============================================");
		logger.info("cntrNo(서비스계약아이디):"+SessionKeeper.getCntrNo(req));
		logger.info("vasItemCd(상품코드):"+vasItemCd);
		logger.info("vasItemId(상품ID):"+vasItemId);
		logger.info("efctStDt(유효시작일):"+efctStDt);
		logger.info("efctFnsDt(유효종료일):"+efctFnsDt);
		logger.info("prodSbscTrmnCd(상품가입구분):A");
		logger.info("prodHstSeq(상품이력일련번호):"+prodHstSeq);
		logger.info("============================================");
		
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		// 부가서비스 신청 체크로직 2019.12.10 부가서비스 체크 제외
		// 체크로직 리스트가 있으면 신청하지 않고 return 보낸다.
		//List<Roaming> roamingList = yRoamingService.chkRoaming(loginCntrNo, cplan.getPpCatL());
		//if(roamingList != null){
		//	return new ResultInfo<>(roamingList);
		//}
		String prodSbscTrmnCd = "A";
		dateChk(vasItemCd, efctStDt, efctFnsDt);
		
		/**
		 * PLAN-426 Y로밍 > 부가서비스 신청/변경/해지 전 사전체크 로직 추가
		 * - 전처리
		 */
		yRoamingService.setRoamingPreCheck(loginCntrNo, vasItemCd, vasItemId, efctStDt, efctFnsDt, prodSbscTrmnCd, prodHstSeq);
		/**
		 * 본처리
		 */
		yRoamingService.setRoaming(loginCntrNo, vasItemCd, vasItemId, efctStDt, efctFnsDt, prodSbscTrmnCd, "0");
		return new ResultInfo<>(); 
	}

	@ApiOperation(value="Y로밍 해지")
	@RequestMapping(value = "/yRoaming/cancel", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header")
					  , @ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header")
					  , @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header")
					  , @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<String> cancelYRoaming(String prodHstSeq, String vasItemCd, String vasItemId, String efctStDt, String efctFnsDt, HttpServletRequest req) throws Exception {
		logger.info("============================================");
		logger.info("cntrNo(서비스계약아이디):"+SessionKeeper.getCntrNo(req));
		logger.info("vasItemCd(상품코드):"+vasItemCd);
		logger.info("vasItemId(상품ID):"+vasItemId);
		logger.info("efctStDt(유효시작일):"+efctStDt);
		logger.info("efctFnsDt(유효종료일):"+efctFnsDt);
		logger.info("prodSbscTrmnCd(상품가입구분):C");
		logger.info("prodHstSeq(상품이력일련번호):"+prodHstSeq);
		logger.info("============================================");
		
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		String prodSbscTrmnCd = "C";
		dateChk(vasItemCd, efctStDt, efctFnsDt);
		/**
		 * PLAN-426 Y로밍 > 부가서비스 신청/변경/해지 전 사전체크 로직 추가
		 * - 전처리
		 */
		yRoamingService.setRoamingPreCheck(loginCntrNo, vasItemCd, vasItemId, efctStDt, efctFnsDt, prodSbscTrmnCd, prodHstSeq);
		/**
		 * 본처리
		 */
		yRoamingService.setRoaming(loginCntrNo, vasItemCd, vasItemId, efctStDt, efctFnsDt, prodSbscTrmnCd, prodHstSeq);
		return new ResultInfo<>(); 
	}

	@ApiOperation(value="Y로밍 변경")
	@RequestMapping(value = "/yRoaming/change", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header")
					  , @ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header")
					  , @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header")
					  , @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<String> updateYRoaming(String prodHstSeq, String vasItemCd, String vasItemId, String efctStDt, String efctFnsDt, HttpServletRequest req) throws Exception {
		logger.info("============================================");
		logger.info("cntrNo(서비스계약아이디):"+SessionKeeper.getCntrNo(req));
		logger.info("vasItemCd(상품코드):"+vasItemCd);
		logger.info("vasItemId(상품ID):"+vasItemId);
		logger.info("efctStDt(유효시작일):"+efctStDt);
		logger.info("efctFnsDt(유효종료일):"+efctFnsDt);
		logger.info("prodSbscTrmnCd(상품가입구분):U");
		logger.info("prodHstSeq(상품이력일련번호):"+prodHstSeq);
		logger.info("============================================");
		
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		String prodSbscTrmnCd = "U";
		dateChk(vasItemCd, efctStDt, efctFnsDt);
		/**
		 * PLAN-426 Y로밍 > 부가서비스 신청/변경/해지 전 사전체크 로직 추가
		 * - 전처리
		 */
		yRoamingService.setRoamingPreCheck(loginCntrNo, vasItemCd, vasItemId, efctStDt, efctFnsDt, prodSbscTrmnCd, prodHstSeq);
		/**
		 * 본처리
		 */
		yRoamingService.setRoaming(loginCntrNo, vasItemCd, vasItemId, efctStDt, efctFnsDt, prodSbscTrmnCd, prodHstSeq);
		return new ResultInfo<>(); 
	}

	@ApiOperation(value="Y로밍 신청 전 체크")
	@RequestMapping(value = "/yRoaming/chk", method = RequestMethod.GET)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<String> chkYRoaming(HttpServletRequest req) throws Exception {
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		String loginMobileNo = null;
		if(SessionKeeper.getSdata(req) != null){
			loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
		}
		vasChk(loginCntrNo, loginMobileNo);
		return new ResultInfo<>(); 
	}

	@ApiOperation(value="Y로밍 부가 서비스 체크")
	public void vasChk(String cntrNo, String mobileNo) throws Exception {

		ContractInfo cntrInfo = shubService.callFn139(mobileNo).getCntrInfo();
		CallingPlan cplan = cntrInfo.getCallingPlan();

		// LTE, 5G 체크
		if ( YappUtil.isEq(cplan.getPpCatL(), "G0001") == false && YappUtil.isEq(cplan.getPpCatL(), "G0002") == false && YappUtil.isEq(cplan.getPpCatL(), "G0005") == false){
			//G0001, G0002 = LTE,  G0003, G0004 = 3G, G0005 = 5G NOIPCEXP 청소년 일시 허용
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_ONLY_LTEFIVE"));			// LTE 요금제, 5G요금제만 이용 가능합니다.
		}

		String chkYn = yRoamingService.chkVasEgg(cntrNo);
		
		/**
		 * PLAN-426 : 체크로직 변경
		 * - AS-IS : 청소년 요금제 (알요금제 ) 체크 및 청소년 임시 허용 체크
		 * - TO-BE : GOOO2 체크 및 청소년 임시 허용 체크 (오류메시지도 변경)
		 */
		// 청소년 요금제 (알요금제 ) 체크 및 청소년 임시 허용 체크
		// if(YappUtil.isEq(cplan.getEggYn(), "Y") == true && YappUtil.isEq(chkYn, "N") == true){
		if(YappUtil.isEq(cplan.getPpCatL(), "G0002") && YappUtil.isEq(chkYn, "N")){
			// 전 : 청소년 요금제 고객은 기본적으로 로밍이 차단되어 있습니다.\n 본 혜택 이용을 위해 '청소년 로밍 일시 허용'서비스를 먼저 신청해 주시기 바랍니다.
			// 후 : 고객님은 현재 로밍이 차단되어 있습니다. KT 로밍 고객센터(02-2190-0901)로 ‘청소년 로밍 일시 허용’ 서비스를 먼저 신청해 주시기 바랍니다.
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_VAS_BLOCK_EGG"));
		}
	}

	@ApiOperation(value="Y로밍 부가 날짜 체크")
	public void dateChk(String vasItem, String efctStDt, String efctFnsDt) throws Exception {

		String curDate = YappUtil.getCurDate("yyyyMMddHHmmss");
		
		Calendar curCal = Calendar.getInstance();
		curCal.setTime(YappUtil.getDate("yyyyMMddHHmm", curDate));
		curCal.add(Calendar.DATE, 60);
		String srchYm = YappUtil.getCurDate(curCal.getTime(), "yyyyMMddHHmmss");
		
		Calendar curCal2 = Calendar.getInstance();
		curCal2.setTime(YappUtil.getDate("yyyyMMddHHmm", efctStDt));
		curCal2.add(Calendar.DATE, 5);
		String srchYm2 = YappUtil.getCurDate(curCal2.getTime(), "yyyyMMddHHmmss");

		logger.info("efctStDt:"+YappUtil.toLong(efctStDt+"00"));
		logger.info("efctFnsDt:"+YappUtil.toLong(efctFnsDt+"00"));
		logger.info("srchYm  :"+YappUtil.toLong(srchYm));
		logger.info("srchYm2 :"+YappUtil.toLong(srchYm2));
		logger.info("curDate :"+YappUtil.toLong(curDate));
		
		//60일 체크 시작일이 현재로 부터 60일 안에 설정되야함
		if(YappUtil.toLong(efctStDt+"00") > YappUtil.toLong(srchYm)){
			throw new YappException("CHECK_MSG", "시작일자는 현재일로부터 최대 60일 이내 날짜로만 설정가능합니다.");
		}

		// 시작일이 미래날짜로만 가능
		if(YappUtil.toLong(efctStDt+"00") < YappUtil.toLong(curDate)){
			throw new YappException("CHECK_MSG", "시작일자는 미래의 시간으로만 설정가능합니다.");
		}

		// 종료일 5일 세팅
		if(YappUtil.toLong(efctFnsDt+"00") != YappUtil.toLong(srchYm2)){
			throw new YappException("CHECK_MSG", "종료일자는 5일로 설정되야합니다. 확인해보세요.");
		}
	}
}