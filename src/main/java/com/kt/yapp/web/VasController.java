package com.kt.yapp.web;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.kt.yapp.em.*;
import com.kt.yapp.exception.YappRuntimeException;
import com.kt.yapp.service.*;
import com.kt.yapp.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kt.yapp.domain.CallingPlan;
import com.kt.yapp.domain.ContractInfo;
import com.kt.yapp.domain.Coupon;
import com.kt.yapp.domain.CouponDtl;
import com.kt.yapp.domain.CouponEnd;
import com.kt.yapp.domain.CouponPackage;
import com.kt.yapp.domain.CouponSearch;
import com.kt.yapp.domain.DataInfo;
import com.kt.yapp.domain.GrpCode;
import com.kt.yapp.domain.MemPointGet;
import com.kt.yapp.domain.TimeOption;
import com.kt.yapp.domain.VaSvcAppl;
import com.kt.yapp.domain.VasItem;
import com.kt.yapp.domain.resp.ResultInfo;
import com.kt.yapp.domain.resp.VasItemResp;
import com.kt.yapp.exception.YappException;
import com.kt.yapp.soap.response.SoapResponse139;
import com.kt.yapp.soap.response.SoapResponse494;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api(description="부가서비스 처리 컨트롤러")
public class VasController 
{
	private static final Logger logger = LoggerFactory.getLogger(VasController.class);
	
	@Autowired
	private CommonService cmnService;
	@Autowired
	private ShubService shubService;
	@Autowired
	private VasService vasService;
	@Autowired
	private KosService kosService;
	@Autowired
	private WsgService wsgService;
	@Autowired
	private MemPointService memPointService;
	@Autowired
	private KeyFixUtilForRyt keyFixUtilRyt;

	@Value("${om.banner.point.url}")
	private String omBannerPointApiUrl;

	@Value("${om.banner.point.zoneCode}")
	private String omBannerPointZoneCode;

	@Value("${om.banner.after.zoneCode}")
	private String omBannerAfterZoneCode;

	@Value("${om.banner.gtdata.zoneCode}")
	private String omBannerGtDataZoneCode;


	@Autowired
	private AppEncryptUtils appEncryptUtils;

	@Autowired
	private CmsService cmsService;

	@Autowired
	private KeyFixUtil keyFixUtil;

	@RequestMapping(value = "/vas/item", method = RequestMethod.GET)
	@ApiOperation(value="부가서비스 상품 목록 조회")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<VasItemResp> getVasItemList(
			@ApiParam(value="부가서비스 코드(MTPMN: My Time Plan, PLLDAT: 당겨쓰기, LTEDTC: 후불 데이터 충전, LTERTE: 룰렛, LTEDTP: 멤버십 데이터 충전)") 
			@RequestParam  String vasCd
			, HttpServletRequest req) throws Exception
	{
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		String loginMobileNo = null;
		String loginUserId = null;
		if(SessionKeeper.getSdata(req) != null){
			loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
			loginUserId = SessionKeeper.getSdata(req).getUserId();
		}
		String osTp = req.getHeader("osTp");
		String appVrsn = req.getHeader("appVrsn");
		String passYn = "N";
		String orgVasCd = vasCd;
		
		if(logger.isInfoEnabled()) {
			logger.info("=================== /vas/item ==================");
			logger.info("loginCntrNo:"+loginCntrNo);
			logger.info("loginMobileNo:"+loginMobileNo);
			logger.info("osTp:"+osTp);
			logger.info("appVrsn:"+appVrsn);
			logger.info("passYn:"+passYn);
			logger.info("vasCd:"+vasCd);
		}


		
		//2019-04-19 5G 후불충전, 멤버십 데이터 충전 때문에 부가서비스 치환 분기처리
		if(YappUtil.contains(vasCd, EnumVasCd.LTEDTC.getVasCd(), EnumVasCd.LTEDTP.getVasCd())){
			CallingPlan callingPlan = shubService.callFn139(loginMobileNo, false, true).getCntrInfo().getCallingPlan();
			if(YappUtil.isEq(callingPlan.getPpCatL(), "G0005") && YappUtil.isEq(vasCd, EnumVasCd.LTEDTC.getVasCd()) && YappUtil.isEq(passYn, "N")){
				vasCd = "LTEDTC5";
			}else if(YappUtil.isEq(callingPlan.getPpCatL(), "G0005") && YappUtil.isEq(vasCd, EnumVasCd.LTEDTP.getVasCd()) && YappUtil.isEq(passYn, "N")){
				vasCd = "LTEDTP5";
			}
		}

		// 상품 목록 조회
		VasItem vasItem = new VasItem();
		vasItem.setVasCd(vasCd);
		List<VasItem> vasItemList = vasService.getVasItemList(vasItem);
		
		VasItemResp itemResp = new VasItemResp();
		itemResp.setVasItemList(vasItemList);
		// My Time Plan
		if ( YappUtil.isEq(vasCd, EnumVasCd.MTPMN.getVasCd()) ) {
			String timePlan = kosService.getMyTimePlan(SessionKeeper.getCntrNo(req));
			logger.info("timePlan:"+timePlan);
			
			if(timePlan != null){
				for ( VasItem vasItem2 : vasItemList )
				{
					// 부가 서비스 설정여부 세팅
					if ( YappUtil.isEq(vasItem2.getVasItemId(), timePlan) ) {
						vasItem2.setApplYn(EnumYn.C_Y.getValue());
						vasItem2.setOptTime(timePlan);
					}
				}
			}
		}

		if(logger.isInfoEnabled()) {
			logger.info("vasCd   :"+vasCd);
			logger.info("orgVasCd:"+orgVasCd);
			logger.info("EnumVasCd.LTERTE:"+EnumVasCd.LTERTE.getVasCd());
			logger.info("EnumVasCd.LTEDTP:"+EnumVasCd.LTEDTP.getVasCd());
			logger.info("=================== /vas/item ==================");
		}
		
		/**
		 * 2020.02.17 : 멤버십 포인트 조회시, 단말로 부터 입력 받은 vasCd 원본 값으로 조회 되도록 변경
		 */
		// 룰렛, 멤버십 충전시 멤버십 포인트 조회
		if ( YappUtil.contains(orgVasCd, EnumVasCd.LTERTE.getVasCd(), EnumVasCd.LTEDTP.getVasCd())){
			MemPointGet memPointInfo = new MemPointGet();
			if(SessionKeeper.getCntrNo(req) != null){
				memPointInfo = memPointService.getMemPoint(SessionKeeper.getCntrNo(req));
			}
			
			if(memPointInfo != null){
				itemResp.setMemPoint(Integer.parseInt(keyFixUtilRyt.decode(memPointInfo.getRmnPoint())));
				logger.info("itemResp.getMemPoint:"+memPointInfo.getRmnPoint());
				logger.info("itemResp.getMemPoint:"+itemResp.getMemPoint());
			}else{
				itemResp.setMemPoint(-1);
				logger.info("itemResp.getMemPoint:-1");
			}
			// 룰렛인 경우 이번달 응모여부 체크
			if ( YappUtil.isEq(vasCd, EnumVasCd.LTERTE.getVasCd()) ){
				VasItem paramSrchHist = makeVasItem(EnumVasCd.LTERTE.getVasCd(), null, loginCntrNo, null);
				paramSrchHist.setIsSrchCurMnth(EnumYn.C_Y.getValue());
				List<VasItem> vaSvcHistList = vasService.getVasHistList(paramSrchHist);
				if ( YappUtil.isEmpty(vaSvcHistList) ){
					GrpCode vasInfo = vasService.getRouletteList(vasItem);
					itemResp.setRouletteExecYn(EnumYn.C_N.getValue());
					itemResp.setRouletteVasItem(vasInfo.getCodeKey());
				}else{
					itemResp.setRouletteExecYn(EnumYn.C_Y.getValue());
					itemResp.setRouletteVasItem("");
					throw new YappException("CHECK_MSG", cmnService.getMsg("ERR_APPL_THIS_MNTH")); // 이번달은 이미 응모하셨습니다.
				}
			}
		}

		// 당겨쓰기인 경우 가능용량 조회
		if ( YappUtil.isEq(vasCd, EnumVasCd.PLLDAT.getVasCd()) ) {
			// 설정된 부가서비스 목록을 조회한다.
			List<DataInfo> dataInfoList = kosService.retrieveDrctlyUseQntDtl(loginCntrNo, loginMobileNo);
			
			// 당겨쓴 데이터
			int pulledDataAmt = 0;
			if ( YappUtil.isNotEmpty(dataInfoList) ){

				for ( DataInfo dataInfo : dataInfoList ){

					if ( YappUtil.contains(dataInfo.getDataTp(), "PLLDAT100", "PLLDAT500", "PLLDAT01G") ){
						pulledDataAmt += dataInfo.getDataAmt();
					}
				}
			}
			
			CallingPlan callingPlan = shubService.callFn139(loginMobileNo, false, true).getCntrInfo().getCallingPlan();
			
			int maxPullDataAmt = callingPlan == null ? 0 : callingPlan.getPullPsAmt();
			
			logger.info("당겨쓰기 최대용량 : " + maxPullDataAmt);
			logger.info("당겨쓴 용량 : " + pulledDataAmt);
			
			itemResp.setPullPsbDataAmt((maxPullDataAmt - pulledDataAmt) < 0 ? 0 : (maxPullDataAmt - pulledDataAmt));
		}

		// 온마시 정보 세팅 - Start
		// 1) ktId가 존재 하지 않는 경우, 온마시 배너 노출하지 않음, 디폴트가 오픈하지 않음(N) 으로 정의되어 있음..
		if(!StringUtils.isEmpty(loginUserId) && !StringUtils.isEmpty(orgVasCd)){
			//2)공통코드에서 온마시 오픈하지 않는 경우(N) 온마시 배너노출하지 않음
			//3)공통코드에서 온마시 노출 처리 한경우(Y) 온마시 배너정보(API URL, zoneCode, ktId, 온마시배너오픈여부)를 앱에 내려줌..
			Optional<GrpCode> omBnrUseYn = Optional.ofNullable(cmsService.getCodeNm(CommonCodes.OmCodes.OM_GROUP_CODE, CommonCodes.OmCodes.OM_CODE_OPENYN));
			final String omLoginUserId = loginUserId;
			omBnrUseYn.ifPresent(omYn -> {
				final String codeKey = omYn.getCodeKey().trim().isEmpty() ? CommonCodes.YesOrNo.NO : omYn.getCodeKey().trim();
				itemResp.setOmBannerUseYn(codeKey);
				if (!StringUtils.isEmpty(codeKey) && codeKey.equals(CommonCodes.YesOrNo.YES)) {
					itemResp.setOmBannerPointApiUrl(omBannerPointApiUrl);//온마시 API URL 정보 추가
					// 각 화면별로 zonCode를 별개로 구현 처리함..(포인트충전,후불데이타충전,당겨쓰기) -S
					// PLLDAT: 당겨쓰기, LTEDTC: 후불 데이터 충전, LTEDTP: 멤버십 데이터 충전
					switch (orgVasCd) {
						case "LTEDTC":
							itemResp.setOmBannerPointZoneCode(omBannerAfterZoneCode); // 온마시 zoneCode정보 후불충전
							break;
						case "LTEDTP":
							itemResp.setOmBannerPointZoneCode(omBannerPointZoneCode); // 온마시 zoneCode정보 포인트 충전
							break;
						case "PLLDAT":
							itemResp.setOmBannerPointZoneCode(omBannerGtDataZoneCode); // 온마시 zoneCode정보 당겨쓰기
							break;
						default:
							logger.error("orgVasCd info error : "+orgVasCd);
							throw new YappRuntimeException(CommonCodes.OmCodes.OM_MSG_TYPE_CODE
									, EnumRsltCd.OM720.getRsltCd()
									, cmnService.getMsg("OM_BNR_NOT_FOUND_VAS_CD")); // 부가서비스 코드 예외처리
					}
					// -E
					try {
						itemResp.setOmEncKtId(keyFixUtil.encode(omLoginUserId));//온마시 inisafeNet암호화 KT ID정보
					} catch (Exception e) {
						itemResp.setOmBannerUseYn(CommonCodes.YesOrNo.NO);
						logger.error("loginUserId inisafeNet encoding error : "+e.getMessage());
					}
				}
			});
		}
		// -End
		
		return new ResultInfo<>(itemResp);
	}

	@RequestMapping(value = "/vas/charge/later", method = RequestMethod.POST)
	@ApiOperation(value="데이터 충전(후불)")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<String> chargePayLater(
			@RequestParam String cntrNo, 
			@ApiParam(value="부가서비스 상품 코드") 
			@RequestParam String vasItemCd, 
			HttpServletRequest req) throws Exception
	{
		String loginMobileNo = null;
		if(SessionKeeper.getSdata(req) != null){
			loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
		}
		String osTp = req.getHeader("osTp");
		String appVrsn = req.getHeader("appVrsn");
		
		// 계약 정보 조회
		SoapResponse139 resp = shubService.callFn139(loginMobileNo, false, true);
		ContractInfo cntrInfo = resp.getCntrInfo();
		
		//임시로 IOS에서 만19세미만 충전안되도록 막는다.
		if ( appVrsn.indexOf("1.0.0") > -1 || appVrsn.indexOf("1.0.1") > -1 || appVrsn.indexOf("1.0.2") > -1){
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_LONG_CHECK"));
		}
		if("Y".equals(cntrInfo.getNineteenYn()) && "G0002".equals(osTp) && Integer.valueOf(appVrsn.replace(".", "")).intValue() <= 200){
			//19세 미만이고 IOS일때 후불충전 , 포인트 충전 조회안되게 수정
			logger.info("ERR_19_IOS === > " + cmnService.getMsg("ERR_19_IOS"));
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_19_IOS")); //기능문제로 인하여 IOS 만19세 미만은 이용하실수 없습니다.
		}
		
		//2019-04-19 5G 후불충전, 멤버십 데이터 충전 때문에 부가서비스 치환 분기처리
		String vasCdTmp = EnumVasCd.LTEDTC.getVasCd();
		CallingPlan callingPlan = cntrInfo.getCallingPlan();
		if(YappUtil.isEq(callingPlan.getPpCatL(), "G0005")){
			vasCdTmp = "LTEDTC5";
		}

		// 유효성 체크 & 부가서비스 코드 조회
		VasItem vasInfo = validateVas(vasCdTmp, vasItemCd, cntrInfo, cntrInfo.getCallingPlan().getLaterChgUsePsYn());
		
		// 부가 서비스 설정
		VasItem paramInsHist = makeVasItem(vasCdTmp, vasItemCd, cntrInfo.getCntrNo(), EnumVasActionTp.A.getValue());
		VaSvcAppl vaSvcAppl = makeVaSvcAppl(cntrInfo.getMobileNo(), EnumVasActionTp.A.getKey(), vasInfo.getVasItemId());

		vasService.applyVasService(vaSvcAppl, paramInsHist);
		
		return new ResultInfo<>();
	}
	
	@RequestMapping(value = "/vas/charge/membership", method = RequestMethod.POST)
	@ApiOperation(value="데이터 충전(멤버십)")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<Object> chargeMembership(
			@RequestParam String cntrNo, 
			@ApiParam(value="부가서비스 상품 코드") 
			@RequestParam String vasItemCd, 
			HttpServletRequest req) throws Exception
	{
		String loginMobileNo = null;
		if(SessionKeeper.getSdata(req) != null){
			loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
		}
		
		
		

		// 계약 정보 조회
		SoapResponse139 resp = shubService.callFn139(loginMobileNo, false, true);
		ContractInfo cntrInfo = resp.getCntrInfo();
		String appVrsn = req.getHeader("appVrsn");
		String osTp = req.getHeader("osTp");
		
		//임시로 IOS에서 만19세미만 충전안되도록 막는다.
		if("Y".equals(cntrInfo.getNineteenYn()) && "G0002".equals(osTp) && Integer.valueOf(appVrsn.replace(".", "")).intValue() <= 200){
			//19세 미만이고 IOS일때 후불충전 , 포인트 충전 조회안되게 수정
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_19_IOS")); //기능문제로 인하여 IOS 만19세 미만은 이용하실수 없습니다.
		}
		
		//ios 2.0.0버전일때 당겨쓰기로 처리하도록 변경
		if(("PLLDAT100".equals(vasItemCd) || "PLLDAT500".equals(vasItemCd) || "PLLDAT01G".equals(vasItemCd)) && "G0002".equals(osTp) && Integer.valueOf(appVrsn.replace(".", "")).intValue() == 200){
			//-------------------- 당겨쓰기 로직(추후제거)-----------------------
			// 유효성 체크 & 부가서비스 코드 조회
			VasItem vasInfo = validateVas(EnumVasCd.PLLDAT.getVasCd(), vasItemCd, cntrInfo, cntrInfo.getCallingPlan().getPullPsYn());
			
			// 부가 서비스 설정
			VasItem paramInsHist = makeVasItem(EnumVasCd.PLLDAT.getVasCd(), vasItemCd, cntrInfo.getCntrNo(), EnumVasActionTp.A.getValue());
			VaSvcAppl vaSvcAppl = this.makeVaSvcAppl(cntrInfo.getMobileNo(), EnumVasActionTp.A.getKey(), vasInfo.getVasItemId());

			vasService.applyVasService(vaSvcAppl, paramInsHist);
			//-------------------- 당겨쓰기 로직(추후제거)-----------------------
		}else{
			//2019-04-19 5G 후불충전, 멤버십 데이터 충전 때문에 부가서비스 치환 분기처리
			String vasCdTmp = EnumVasCd.LTEDTP.getVasCd();
			CallingPlan callingPlan = cntrInfo.getCallingPlan();
			if(YappUtil.isEq(callingPlan.getPpCatL(), "G0005")){
				vasCdTmp = "LTEDTP5";
			}
			
			// 유효성 체크 & 부가서비스 코드 조회
			VasItem vasInfo = validateVas(vasCdTmp, vasItemCd, cntrInfo, cntrInfo.getCallingPlan().getMemChgUsePsYn());
			MemPointGet memPointInfo = null;
			if(SessionKeeper.getCntrNo(req) != null){
				memPointInfo = memPointService.getMemPoint(SessionKeeper.getCntrNo(req));
			}
			
			if(memPointInfo != null){
				if(!keyFixUtilRyt.decode(memPointInfo.getMbrClCd()).equals("R")){
					throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_NOT_JOING_MEM")); //멤버십 가입자가 아닙니다. \n멤버십 가입하시고 이벤트에 참가해주세요.
				}

				if (Integer.parseInt(keyFixUtilRyt.decode(memPointInfo.getRmnPoint())) < vasInfo.getPrice() ){
					throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_LACK_MEMPOINT"));	// 멤버십포인트가 부족합니다.
				}
			}
			// 부가 서비스 설정 & 멤버십 차감
			VasItem paramInsHist = makeVasItem(vasCdTmp, vasItemCd, cntrInfo.getCntrNo(), EnumVasActionTp.A.getValue());
			VaSvcAppl vaSvcAppl = makeVaSvcAppl(cntrInfo.getMobileNo(), EnumVasActionTp.A.getKey(), vasInfo.getVasItemId());
			vaSvcAppl.setAttrNm("apdInfoChrSbst1");
			vaSvcAppl.setAttrVal("1_" + vasInfo.getPrice());

			vasService.applyVasService(vaSvcAppl, paramInsHist, vasInfo.getPrice());
		}
		
		// 
		return new ResultInfo<>();
	}
	
	@RequestMapping(value = "/vas/roulette", method = RequestMethod.POST)
	@ApiOperation(value="데이터 룰렛")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<String> roulette(
			@RequestParam String cntrNo, 
			@ApiParam(value="부가서비스 상품 코드") 
			@RequestParam String vasItemCd, 
			HttpServletRequest req) throws Exception
	{
		String loginMobileNo = null;
		if(SessionKeeper.getSdata(req) != null){
			loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
		}

		// 계약 정보 조회
		SoapResponse139 resp = shubService.callFn139(loginMobileNo, false, true);
		ContractInfo cntrInfo = resp.getCntrInfo();
		
		// 유효성 체크 & 부가서비스 코드 조회
		VasItem vasInfo = validateVas(EnumVasCd.LTERTE.getVasCd(), vasItemCd, cntrInfo, cntrInfo.getCallingPlan().getRouletPsYn());
		
		// 점검시간 체크(매일 23:50 ~ 24:10)
		Calendar curCal = Calendar.getInstance();
		String curTime = YappUtil.lpad(curCal.get(Calendar.HOUR_OF_DAY), 2, "0") + YappUtil.lpad(curCal.get(Calendar.MINUTE), 2, "0");
		if ( curTime.compareTo("2350") > -1 || curTime.compareTo("0010") < 0 ){
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_SYS_CHK_TIME"));		// 지금은 시스템점검시간(매일 23:50 ~ 24:10) 입니다.
		}
		MemPointGet memPointInfo = new MemPointGet();
		if(SessionKeeper.getCntrNo(req) != null){
			 memPointInfo = memPointService.getMemPoint(SessionKeeper.getCntrNo(req));
		}
		
		if(memPointInfo != null){
			if(!keyFixUtilRyt.decode(memPointInfo.getMbrClCd()).equals("R")){
				throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_APPL_NO_MEMBER")); //멤버십 가입자가 아닙니다. \n멤버십 가입하시고 이벤트에 참가해주세요.
			}
			
			if (Integer.parseInt(keyFixUtilRyt.decode(memPointInfo.getRmnPoint())) < vasInfo.getPrice() ){
				throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_LACK_MEMPOINT"));	// 멤버십포인트가 부족합니다.
			}
		}

		// 이번달 응모여부 체크
		VasItem paramSrchHist = makeVasItem(EnumVasCd.LTERTE.getVasCd(), null, cntrInfo.getCntrNo(), null);
		paramSrchHist.setIsSrchCurMnth(EnumYn.C_Y.getValue());
		
		List<VasItem> vaSvcHistList = vasService.getVasHistList(paramSrchHist);
		if ( vaSvcHistList != null && vaSvcHistList.size() > 0 ){
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_APPL_THIS_MNTH"));	// 이번달은 이미 응모하셨습니다.
		}

		// 부가 서비스 설정 & 멤버십 차감
		VasItem paramInsHist = makeVasItem(EnumVasCd.LTERTE.getVasCd(), vasItemCd, cntrInfo.getCntrNo(), EnumVasActionTp.A.getValue());
		VaSvcAppl vaSvcAppl = this.makeVaSvcAppl(cntrInfo.getMobileNo(), EnumVasActionTp.A.getKey(), vasInfo.getVasItemId());
		vaSvcAppl.setAttrNm("apdInfoChrSbst1");
		vaSvcAppl.setAttrVal("");

		String vasItemIdTmp = vasInfo.getVasItemId();
		String pointPdCd = "";
		if(cntrInfo.getCallingPlan().getEggYn().equals("Y")){
			if(vasItemIdTmp.equals("LTRT10")){
				pointPdCd = "DATARLBT1M";
			}else if(vasItemIdTmp.equals("LTRT30")){
				pointPdCd = "DATARLBT3M";
			}else if(vasItemIdTmp.equals("LTRT50")){
				pointPdCd = "DATARLBT5M";
			}else if(vasItemIdTmp.equals("LTRT1G")){
				pointPdCd = "DATARLBT1G";
			}
		}else{
			if(vasItemIdTmp.equals("LTRT10")){
				pointPdCd = "DATARLB1M";
			}else if(vasItemIdTmp.equals("LTRT30")){
				pointPdCd = "DATARLB3M";
			}else if(vasItemIdTmp.equals("LTRT50")){
				pointPdCd = "DATARLB5M";
			}else if(vasItemIdTmp.equals("LTRT1G")){
				pointPdCd = "DATARLB1G";
			}
		}
		/*DATARLB1G   데이터룰렛(1GB)
		DATARLB1M   데이터룰렛(100MB)
		DATARLB3M   데이터룰렛(300MB)
		DATARLB5M   데이터룰렛(500MB)
		DATARLBT1G  데이터룰렛(틴)(1GB)
		DATARLBT1M  데이터룰렛(틴)(100MB)
		DATARLBT3M  데이터룰렛(틴)(300MB)
		DATARLBT5M  데이터룰렛(틴)(500MB)*/
		if(memPointInfo != null) {
			vasService.applyVasService(vaSvcAppl, paramInsHist, cntrInfo.getCntrNo(), keyFixUtilRyt.decode(memPointInfo.getMemId()), pointPdCd, vasInfo.getPrice());
		}
		
		return new ResultInfo<>();
	}
	
	@RequestMapping(value = "/vas/pull", method = RequestMethod.POST)
	@ApiOperation(value="당겨쓰기 신청")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<Object> pullData(
			@RequestParam String cntrNo, 
			@ApiParam(value="부가서비스 상품 코드") 
			@RequestParam String vasItemCd, 
			HttpServletRequest req) throws Exception
	{
		String loginMobileNo = null;
		if(SessionKeeper.getSdata(req) != null){
			loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
		}

		// 계약 정보 조회
		SoapResponse139 resp = shubService.callFn139(loginMobileNo, false, true);
		ContractInfo cntrInfo = resp.getCntrInfo();
		
		// 유효성 체크 & 부가서비스 코드 조회
		VasItem vasInfo = validateVas(EnumVasCd.PLLDAT.getVasCd(), vasItemCd, cntrInfo, cntrInfo.getCallingPlan().getPullPsYn());
		
		// 부가 서비스 설정
		VasItem paramInsHist = makeVasItem(EnumVasCd.PLLDAT.getVasCd(), vasItemCd, cntrInfo.getCntrNo(), EnumVasActionTp.A.getValue());
		VaSvcAppl vaSvcAppl = this.makeVaSvcAppl(cntrInfo.getMobileNo(), EnumVasActionTp.A.getKey(), vasInfo.getVasItemId());

		vasService.applyVasService(vaSvcAppl, paramInsHist);

		return new ResultInfo<>();
	}

	@RequestMapping(value = "/vas/timeoption", method = RequestMethod.GET)
	@ApiOperation(value="매일 3시간 부가서비스 시간옵션  조회. ")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<TimeOption> getTimeOption(HttpServletRequest req) throws Exception
	{
		List<TimeOption> timeOptionList = kosService.searchTimeOption();
		return new ResultInfo<TimeOption>(timeOptionList);
	}

	@RequestMapping(value = "/vas/timeplan", method = RequestMethod.GET)
	@ApiOperation(value="매일 3시간 부가서비스 신청 정보 조회. 설정 시간대역 반환(00000300 : 00시 ~ 03시,01000400 : 01시 ~ 04시,02000500 : 02시 ~ 05시,03000600 : 03시 ~ 06시,.........,20002300 : 20시 ~ 23시,21000000 : 21시 ~ 00시)")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<String> getTimePlan(HttpServletRequest req) throws Exception
	{
		String timePlan = kosService.getMyTimePlan(SessionKeeper.getCntrNo(req));
		return new ResultInfo<>(timePlan);
	}
	
	@RequestMapping(value = "/vas/timeplan", method = RequestMethod.POST)
	@ApiOperation(value="매일 3시간 부가서비스 변경")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<Object> setTimePlan(
			@RequestParam String cntrNo, 
			@ApiParam(value="설정 시간대역(00000300 : 00시 ~ 03시,01000400 : 01시 ~ 04시,02000500 : 02시 ~ 05시,03000600 : 03시 ~ 06시,.........,20002300 : 20시 ~ 23시,21000000 : 21시 ~ 00시)") 
			@RequestParam String vasItemCd, 
			@ApiParam(value="가입유형(G0003 : 변경)") 
			@RequestParam String applTp, 
			HttpServletRequest req) throws Exception
	{
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		String loginMobileNo = null;
		if(SessionKeeper.getSdata(req) != null){
			loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
		}
		
		// 계약 정보 조회
		ContractInfo cntrInfo = shubService.callFn139(loginMobileNo, false, true).getCntrInfo();

		// 유효성 체크 & 부가서비스 코드 조회
		VasItem vasInfo = validateVas(EnumVasCd.MTPMN.getVasCd(), vasItemCd, cntrInfo, cntrInfo.getCallingPlan().getThreeUsePsYn());
		VasItem paramInsHist = makeVasItem(EnumVasCd.MTPMN.getVasCd(), vasInfo.getVasItemId(), cntrInfo.getCntrNo(), EnumVasActionTp.A.getValue());
		VaSvcAppl vaSvcAppl = this.makeVaSvcAppl(cntrInfo.getMobileNo(), EnumVasActionTp.A.getKey(), vasInfo.getVasItemId());
		
		vasService.applyVasTimeService(vaSvcAppl, paramInsHist,cntrInfo.getCntrNo(), vasInfo.getVasItemId());
		return new ResultInfo<>();
	}
	
	@RequestMapping(value = "/vas/half", method = RequestMethod.GET)
	@ApiOperation(value="반값팩 쿠폰 목록 조회")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<Coupon> getHalfPack(HttpServletRequest req) throws Exception
	{
		String loginCntrNo= SessionKeeper.getCntrNo(req);
		String loginMobileNo = null;
		if(SessionKeeper.getSdata(req) != null){
			loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
		}
		
		ResultInfo<Coupon> resultInfo = new ResultInfo<>();
		
		SoapResponse494 resp494 = shubService.callFn494(loginCntrNo, loginMobileNo, EnumPkgTpCd.YT.getValue());
		if ( YappUtil.is1(resp494.getRtnCd()) == false )
			logger.info(resp494.getRtnCd() + ", " + resp494.getRtnDesc());
		else
			resultInfo.setResultInfoList(resp494.getCouponList());
		
		return resultInfo;
	}
	
	@RequestMapping(value = "/vas/half", method = RequestMethod.POST)
	@ApiOperation(value="반값팩 신청")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<Object> setHalfPrice(
			@RequestParam String cntrNo, 
			@ApiParam(value="쿠폰번호") 
			@RequestParam String cpnNo, 
			HttpServletRequest req) throws Exception
	{
		String loginCntrNo = null;
		String loginMobileNo = null;
		if(SessionKeeper.getSdata(req) != null){
			loginCntrNo= SessionKeeper.getCntrNo(req);
			loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
		}
		
		Coupon cpn = new Coupon();
		cpn.setCntrNo(loginCntrNo);
		cpn.setMobileNo(loginMobileNo);
		cpn.setCpnNo(cpnNo);
		String userId =  null;
		if(SessionKeeper.getSdata(req) != null){
			userId = SessionKeeper.getSdata(req).getUserId();
		}
		if ( YappUtil.isEmpty(userId) )
			userId = shubService.callFn110(loginMobileNo).getUserId();
		
		cpn.setUserId(userId);
		
		// 계약 정보 조회
		ContractInfo cntrInfo = shubService.callFn139(loginMobileNo, false, true).getCntrInfo();
				
		VasItem paramInsHist = makeVasItem(EnumVasCd.HALFCP.getVasCd(), cpnNo, cntrInfo.getCntrNo(), EnumVasActionTp.A.getValue());
		VaSvcAppl vaSvcAppl = this.makeVaSvcAppl(cntrInfo.getMobileNo(), EnumVasActionTp.A.getKey(), cpnNo);
		
		vasService.applyVasCpnService(vaSvcAppl, paramInsHist, cpn);
		
		return new ResultInfo<>();
	}

	@RequestMapping(value = "/vas/sethalf", method = RequestMethod.POST)
	@ApiOperation(value="반값팩 신청")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<Object> setHalfPriceSecond(
			@RequestParam String cntrNo,
			@ApiParam(value="부가서비스 상품 코드(HALFCPGENIE: 지니팩, HALFCPOTM: OTM 데일리팩, HALFCPDATA: 데이터충전)") 
			@RequestParam String vasItemCd,
			@ApiParam(value="쿠폰번호") 
			@RequestParam String cpnNo, 
			HttpServletRequest req) throws Exception
	{
		String loginCntrNo= null;
		String loginMobileNo = null;
		String userId = null;
		if(SessionKeeper.getSdata(req) != null){
			loginCntrNo= SessionKeeper.getCntrNo(req);
			loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
			userId = SessionKeeper.getSdata(req).getUserId();
		}
		
		Coupon cpn = new Coupon();
		cpn.setCntrNo(loginCntrNo);
		cpn.setMobileNo(loginMobileNo);
		cpn.setCpnNo(cpnNo);
		
		//String userId = SessionKeeper.getSdata(req).getUserId();
		if ( YappUtil.isEmpty(userId) )
			userId = shubService.callFn110(loginMobileNo).getUserId();
		
		cpn.setUserId(userId);
		
		// 계약 정보 조회
		ContractInfo cntrInfo = shubService.callFn139(loginMobileNo, false, true).getCntrInfo();
				
		VasItem paramInsHist = makeVasItem(EnumVasCd.HALFCP.getVasCd(), vasItemCd, cntrInfo.getCntrNo(), EnumVasActionTp.A.getValue());
		VaSvcAppl vaSvcAppl = this.makeVaSvcAppl(cntrInfo.getMobileNo(), EnumVasActionTp.A.getKey(), vasItemCd);
		
		vasService.applyVasCpnService(vaSvcAppl, paramInsHist, cpn);
		
		return new ResultInfo<>();
	}
	
	@RequestMapping(value = "/vas/longterm", method = RequestMethod.GET)
	@ApiOperation(value="장기혜택 쿠폰 목록 조회")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<Coupon> getLongTermBnfCpnList(HttpServletRequest req) throws Exception
	{
		String loginCntrNo= SessionKeeper.getCntrNo(req);
		String loginMobileNo = null;
		if(SessionKeeper.getSdata(req) != null){
			loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
		}
		
		ResultInfo<Coupon> resultInfo = new ResultInfo<>();
		String appVrsn = req.getHeader("appVrsn");
		String osTp = req.getHeader("osTp");
		String passYn = "N";

		if ( appVrsn.indexOf("1.0.0") > -1 || appVrsn.indexOf("1.0.1") > -1 || appVrsn.indexOf("1.0.2") > -1){
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_LONG_CHECK"));
		}

		SoapResponse494 resp494 = shubService.callFn494(loginCntrNo, loginMobileNo, EnumPkgTpCd.PC.getValue());
		if ( YappUtil.is1(resp494.getRtnCd()) == false ){
			logger.info(resp494.getRtnCd() + ", " + resp494.getRtnDesc());
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_NO_COUPON"));
		}else{
			CallingPlan callingPlan = shubService.callFn139(loginMobileNo, false, true).getCntrInfo().getCallingPlan();

			//5G 요금제 사용 고객이 아니면
			//  => '5G 쿠폰' 나오지 않도록 제외 추가

			if(YappUtil.isEq(callingPlan.getPpCatL(), "G0005")){
				passYn = "N";
			}else{
				passYn = "Y";
			}

			//Android 1.0.5 이하 or iOS 1.0.6 이하
			//  => '5G 쿠폰' 나오지 않도록 제외 추가			
			
			List<Coupon> couponList = resp494.getCouponList();
			List<Coupon> couponNewList = new ArrayList<>();
			int lteCnt = 0;
			for ( int i = 0; i < couponList.size(); i++ ){
				if("G0002".equals(couponList.get(i).getCpnTp())){
					lteCnt++;
				}
			}
			
			for ( int i = 0; i < couponList.size(); i++ ){
				Coupon cpn = new Coupon();
				cpn = couponList.get(i);
				if(YappUtil.isEq(cpn.getCpnTp(), "G0006") && YappUtil.isEq(passYn, "Y")){
				}else{
					couponNewList.add(cpn);
				}

				if(YappUtil.isEq(cpn.getCpnTp(), "G0001") && lteCnt == 0){
					Coupon cpnTmp = new Coupon();
					cpnTmp.setCpnNm(cpn.getCpnNm());
					cpnTmp.setCpnNo(cpn.getCpnNo());
					cpnTmp.setCpnUseYn(cpn.getCpnUseYn());
					cpnTmp.setCpnValidEdYmd(cpn.getCpnValidEdYmd());
					cpnTmp.setCpnValidStYmd(cpn.getCpnValidStYmd());
					cpnTmp.setCpnPrice(cpn.getCpnPrice());
					cpnTmp.setCpnTp("G0002");
					couponNewList.add(cpnTmp);
				}
			}
			resultInfo.setResultInfoList(couponNewList);
		}
		
		return resultInfo;
	}

	@RequestMapping(value = "/vas/cpnpkg", method = RequestMethod.GET)
	@ApiOperation(value="고객이 배포받은 쿠폰 패키지 목록조회")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<CouponSearch> getCpnPkgList(HttpServletRequest req) throws Exception
	{
		String osTp = req.getHeader("osTp");
		String appVrsn = req.getHeader("appVrsn");
		
		String loginCntrNo= SessionKeeper.getCntrNo(req);
		String loginMobileNo = null;
		if(SessionKeeper.getSdata(req) != null){
			loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
		}
		CouponSearch result = null;

		try{
			result = shubService.callFn809(loginCntrNo, loginMobileNo);
		}catch(RuntimeException e){
			result = new CouponSearch();
		}catch(Exception e){
			result = new CouponSearch();
		}
		
		//220711 파라미터 암호화 조치
		if(!YappUtil.isEmpty(result.getReqid())){
			String encReqId = appEncryptUtils.aesEnc128(result.getReqid(), osTp + appVrsn);
			result.setReqid(encReqId);
		}
		
		return new ResultInfo<CouponSearch>(result);
	}

	@RequestMapping(value = "/vas/cpn", method = RequestMethod.GET)
	@ApiOperation(value="고객의 쿠폰의 보관함에 있는 (미사용상태) 패키지쿠폰 목록조회")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<CouponSearch> getCpnList(HttpServletRequest req, @ApiParam(value="패키지 ID") @RequestParam String pkgId) throws Exception
	{
		String osTp = req.getHeader("osTp");
		String appVrsn = req.getHeader("appVrsn");
		
		String loginCntrNo= SessionKeeper.getCntrNo(req);
		String loginMobileNo = null;
		if(SessionKeeper.getSdata(req) != null){
			loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
		}
		CouponSearch result = null;

		try{
			result = shubService.callFn810(loginCntrNo, loginMobileNo, pkgId);
		}catch(RuntimeException e){
			result = new CouponSearch();
		}catch(Exception e){
			result = new CouponSearch();
		}
		
		//220711 파라미터 암호화 조치
		if(!YappUtil.isEmpty(result.getReqid())){
			String encReqId = appEncryptUtils.aesEnc128(result.getReqid(), osTp + appVrsn);
			result.setReqid(encReqId);
		}

		return new ResultInfo<CouponSearch>(result);
	}
	
	@RequestMapping(value = "/vas/cpnend", method = RequestMethod.GET)
	@ApiOperation(value="고객이 사용한 쿠폰의 목록조회")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<CouponEnd> getCpnEndInfo(HttpServletRequest req) throws Exception
	{
		String osTp = req.getHeader("osTp");
		String appVrsn = req.getHeader("appVrsn");
		
		String loginCntrNo= SessionKeeper.getCntrNo(req);
		String loginMobileNo = null;
		if(SessionKeeper.getSdata(req) != null){
			loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
		}
		CouponEnd result = null;

		try{
			result = shubService.callFn812(loginCntrNo, loginMobileNo);
		}catch(RuntimeException e){
			result = new CouponEnd();
		}catch(Exception e){
			result = new CouponEnd();
		}
		
		//220711 파라미터 암호화 조치
		if(!YappUtil.isEmpty(result.getReqid())){
			String encReqId = appEncryptUtils.aesEnc128(result.getReqid(), osTp + appVrsn);
			result.setReqid(encReqId);
		}

		return new ResultInfo<CouponEnd>(result);
	}

	@RequestMapping(value = "/vas/cpndtl", method = RequestMethod.GET)
	@ApiOperation(value="쿠폰에 대한 상세정보 조회")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<CouponDtl> getCpnDtlInfo(HttpServletRequest req, @ApiParam(value="쿠폰번호") @RequestParam String serialnum) throws Exception
	{
		CouponDtl result = null;

		try{
			result = shubService.callFn813(serialnum);
		}catch(RuntimeException e){
			result = new CouponDtl();
		}catch(Exception e){
			result = new CouponDtl();
		}
		return new ResultInfo<CouponDtl>(result);
	}

	@RequestMapping(value = "/vas/longtermpkg", method = RequestMethod.GET)
	@ApiOperation(value="장기혜택 쿠폰(패키지) 목록 조회")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<CouponPackage> getLongTermPkgBnfCpnList(HttpServletRequest req) throws Exception
	{
		String loginCntrNo= SessionKeeper.getCntrNo(req);
		String loginMobileNo = null;
		if(SessionKeeper.getSdata(req) != null){
			loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
		}
		
		ResultInfo<CouponPackage> resultInfo = new ResultInfo<>();
		resultInfo.setResultInfoList(shubService.couponList(loginCntrNo, loginMobileNo, EnumPkgTpCd.PC.getValue()));

		return resultInfo;
	}
	@RequestMapping(value = "/vas/longterm", method = RequestMethod.POST)
	@ApiOperation(value="장기혜택 쿠폰 설정")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<String> setLongTermBnfCpn(
			@RequestParam String cntrNo, 
			@ApiParam(value="쿠폰번호") 
			@RequestParam String cpnNo, 
			HttpServletRequest req) throws Exception
	{
		String loginCntrNo= null;
		String loginMobileNo = null;
		if(SessionKeeper.getSdata(req) != null){
			loginCntrNo= SessionKeeper.getCntrNo(req);
			loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
		}
		String appVrsn = req.getHeader("appVrsn");

		Coupon cpn = new Coupon();
		cpn.setCntrNo(loginCntrNo);
		cpn.setMobileNo(loginMobileNo);
		cpn.setCpnNo(cpnNo);
		String userId = null;
		if(SessionKeeper.getSdata(req) != null){
			userId = SessionKeeper.getSdata(req).getUserId();
		}
		
		if ( YappUtil.isEmpty(userId) )
			userId = shubService.callFn110(loginMobileNo).getUserId();
		
		cpn.setUserId(userId);
		
		ContractInfo cntrInfo = shubService.callFn139(loginMobileNo, false, true).getCntrInfo();
		
		VasItem paramInsHist = makeVasItem(EnumVasCd.LONGCP.getVasCd(), cpnNo, cntrInfo.getCntrNo(), EnumVasActionTp.A.getValue());
		VaSvcAppl vaSvcAppl = this.makeVaSvcAppl(cntrInfo.getMobileNo(), EnumVasActionTp.A.getKey(), cpnNo);
		
		vasService.applyVasCpnService(vaSvcAppl, paramInsHist, cpn);
		
		return new ResultInfo<>();
	}

	@RequestMapping(value = "/vas/setlongterm", method = RequestMethod.POST)
	@ApiOperation(value="장기혜택 쿠폰 설정")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<String> setLongTermBnfCpnSecond(
			@RequestParam String cntrNo, 
			@ApiParam(value="부가서비스 상품 코드(LONGCPLTE: 데이터 LTE, LONGCP3G: 데이터 3G, LONGCPTV: 올레tv, LONGCP30M: 통화 30분, LONGCP5EGG: 기본알 5천알)") 
			@RequestParam String vasItemCd,
			@ApiParam(value="쿠폰번호") 
			@RequestParam String cpnNo, 
			HttpServletRequest req) throws Exception
	{
		String loginCntrNo= null;
		String loginMobileNo = null;
		String userId = null;
		if(SessionKeeper.getSdata(req) != null){
			loginCntrNo= SessionKeeper.getCntrNo(req);
			loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
			userId = SessionKeeper.getSdata(req).getUserId();
		}
		
		Coupon cpn = new Coupon();
		cpn.setCntrNo(loginCntrNo);
		cpn.setMobileNo(loginMobileNo);
		cpn.setCpnNo(cpnNo);
		if ( YappUtil.isEmpty(userId) )
			userId = shubService.callFn110(loginMobileNo).getUserId();
		
		cpn.setUserId(userId);
		
		ContractInfo cntrInfo = shubService.callFn139(loginMobileNo, false, true).getCntrInfo();
		
		VasItem paramInsHist = makeVasItem(EnumVasCd.LONGCP.getVasCd(), vasItemCd, cntrInfo.getCntrNo(), EnumVasActionTp.A.getValue());
		VaSvcAppl vaSvcAppl = this.makeVaSvcAppl(cntrInfo.getMobileNo(), EnumVasActionTp.A.getKey(), vasItemCd);
		
		vasService.applyVasCpnService(vaSvcAppl, paramInsHist, cpn);
		
		return new ResultInfo<>();
	}
	
	@RequestMapping(value = "/vas/double", method = RequestMethod.GET)
	@ApiOperation(value="두배쓰기 설정 정보 조회", notes="설정 되어 있으면 'Y', 미설정시 'N'을 반환한다.")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<String> getDoubleDataInfo(HttpServletRequest req) throws Exception
	{
		String loginMobileNo = null;
		if(SessionKeeper.getSdata(req) != null){
			loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
		}
		
		// 설정된 부가서비스 목록을 조회한다.
		List<VasItem> settingVasItemList = shubService.callFn133(loginMobileNo).getVasCdList();
		
		if ( YappUtil.isNotEmpty(settingVasItemList) )
		{
			for ( VasItem settingVasItem : settingVasItemList )
			{
				// 부가 서비스 설정여부 세팅
				if ( YappUtil.isEq(settingVasItem.getVasItemCd(), "TYCHGDTEP") )
					return new ResultInfo<>(EnumYn.C_Y.getValue());
			}
		}
		
		
		return new ResultInfo<>(EnumYn.C_N.getValue());
	}
	
	@RequestMapping(value = "/vas/double", method = RequestMethod.POST)
	@ApiOperation(value="두배쓰기 설정")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<String> setDoubleData(
			@RequestParam String cntrNo, 
			@ApiParam(value="가입유형(G0001 : 가입, G0002 : 해지)") 
			@RequestParam String applTp, 
			HttpServletRequest req) throws Exception
	{
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		String loginMobileNo = null;
		if(SessionKeeper.getSdata(req) != null){
			loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
		}
		
		// 계약 정보 조회
		ContractInfo cntrInfo = shubService.callFn139(loginMobileNo, false, true).getCntrInfo();

		// 유효성 체크 & 부가서비스 코드 조회
		VasItem vasInfo = validateVas(EnumVasCd.TYCDBL.getVasCd(), "TYCHGDTEP", cntrInfo, cntrInfo.getCallingPlan().getDblUsePsYn());

		if ( YappUtil.isEq(applTp, EnumVasActionTp.A.getValue()) )
		{
			VasItem paramSrchHist = makeVasItem(EnumVasCd.TYCDBL.getVasCd(), null, loginCntrNo, EnumVasActionTp.A.getValue());
			paramSrchHist.setIsSrchCurHour(EnumYn.C_Y.getValue());
			
			List<VasItem> vaSvcHistList = vasService.getVasHistList(paramSrchHist);
			if ( YappUtil.isNotEmpty(vaSvcHistList) )
				throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_APPL_THIS_HOUR"));	// 현 시간대에 이미 2배쓰기 설정 이력이 있어, 설정 불가합니다.\n2배쓰기 서비스는 시간대별 1회에 한하여 설정 가능하니, 다음 시간대에 재설정 해주세요.
		}
		
		VasItem paramInsHist = makeVasItem(vasInfo.getVasCd(), vasInfo.getVasItemCd(), loginCntrNo, applTp);
		VaSvcAppl vaSvcAppl = this.makeVaSvcAppl(loginMobileNo, EnumVasActionTp.getObj(applTp).getKey(), vasInfo.getVasItemId());

		vasService.applyVasService(vaSvcAppl, paramInsHist);

		return new ResultInfo<>();
	}
	
	@RequestMapping(value = "/vas/exchange", method = RequestMethod.GET)
	@ApiOperation(value="바꿔쓰기 상품 목록 조회")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<VasItem> getExchangeItemList(HttpServletRequest req) throws Exception
	{
		String loginMobileNo = null;
		if(SessionKeeper.getSdata(req) != null){
			loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
		}
		
		// 상품 목록 조회
		VasItem paramVasItem = new VasItem();
		paramVasItem.setVasCd(EnumVasCd.TYCHGE.getVasCd());
		
		List<VasItem> vasItemList = vasService.getVasItemList(paramVasItem);
		
		// 설정된 부가서비스 목록을 조회한다.
		List<VasItem> settingVasItemList = shubService.callFn133(loginMobileNo).getVasCdList();
		
		if ( YappUtil.isNotEmpty(settingVasItemList) )
		{
			for ( VasItem vasItem : vasItemList )
			{
				for ( VasItem settingVasItem : settingVasItemList )
				{
					// 부가 서비스 설정여부 세팅
					if ( YappUtil.isEq(vasItem.getVasItemCd(), settingVasItem.getVasItemCd()) ) {
						vasItem.setApplYn(EnumYn.C_Y.getValue());
						vasItem.setOptTime("2");
					}
				}
			}
		}
		
		return new ResultInfo<>(vasItemList);
	}
	
	@RequestMapping(value = "/vas/exchange", method = RequestMethod.POST)
	@ApiOperation(value="바꿔쓰기 설정")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<String> setExchangeItem(
			@RequestParam String cntrNo, 
			@ApiParam(value="부가서비스 상품 코드") 
			@RequestParam String vasItemCd, 
			@ApiParam(value="가입유형(G0001 : 가입, G0002 : 해지)") 
			@RequestParam String applTp, 
			@ApiParam(value="My TIme Plan용 설정시간 대역(00000300 : 00시 ~ 03시,01000400 : 01시 ~ 04시,02000500 : 02시 ~ 05시,03000600 : 03시 ~ 06시,.........,20002300 : 20시 ~ 23시,21000000 : 21시 ~ 00시)") 
			@RequestParam String optTime , 
			HttpServletRequest req) throws Exception
	{
		/*optTime
		00000300 : 00시 ~ 03시,01000400 : 01시 ~ 04시,02000500 : 02시 ~ 05시,03000600 : 03시 ~ 06시
		04000700 : 04시 ~ 07시,05000800 : 05시 ~ 08시,06000900 : 06시 ~ 09시,07001000 : 07시 ~ 10시
		08001100 : 08시 ~ 11시,09001200 : 09시 ~ 12시,10001300 : 10시 ~ 13시,11001400 : 11시 ~ 14시
		12001500 : 12시 ~ 15시,13001600 : 13시 ~ 16시,14001700 : 14시 ~ 17시,15001800 : 15시 ~ 18시
		16001900 : 16시 ~ 19시,17002000 : 17시 ~ 20시,18002100 : 18시 ~ 21시,19002200 : 19시 ~ 22시
		20002300 : 20시 ~ 23시,21000000 : 21시 ~ 00시
		*/ 
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		String loginMobileNo = null;
		if(SessionKeeper.getSdata(req) != null){
			loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
		}
		
		// 계약 정보 조회
		ContractInfo cntrInfo = shubService.callFn139(loginMobileNo, false, true).getCntrInfo();

		// 유효성 체크 & 부가서비스 코드 조회
		VasItem vasInfo = validateVas(EnumVasCd.TYCHGE.getVasCd(), vasItemCd, cntrInfo, cntrInfo.getCallingPlan().getChgUsePsYn());

		// 당월 재가입 체크
		if ( YappUtil.isEq(applTp, EnumVasActionTp.A.getValue()) )
		{
			VasItem paramSrchHist = makeVasItem(null, vasItemCd, cntrInfo.getCntrNo(), null);
			paramSrchHist.setIsSrchCurMnth(EnumYn.C_Y.getValue());
			
			List<VasItem> vaSvcHistList = vasService.getVasHistList(paramSrchHist);
			if ( vaSvcHistList != null && vaSvcHistList.size() > 0 )
				throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_REAPPL_THIS_MNTH"));	// 당월 재가입 할 수 없습니다.
		}

		VasItem paramInsHist = makeVasItem(vasInfo.getVasCd(), vasInfo.getVasItemCd(), loginCntrNo, applTp);
		VaSvcAppl vaSvcAppl = this.makeVaSvcAppl(loginMobileNo, EnumVasActionTp.getObj(applTp).getKey(), vasInfo.getVasItemId());
		if ( YappUtil.isEq(vasItemCd, "TYCHGMTPL") )
		{
			vaSvcAppl.setAttrNm("");
			if("G0002".equals(applTp)){
				vaSvcAppl.setAttrVal("");
			}else{
				vaSvcAppl.setAttrVal(optTime);
			}
			
		}

		vasService.applyVasService(vaSvcAppl, paramInsHist);
		
		return new ResultInfo<>();
	}
	
	
	/**
	 * 부가 서비스 (이력) 객체 생성
	 */
	private VasItem makeVasItem(String vasCd, String vasItemCd, String cntrNo, String applTp)
	{
		VasItem vasItem = new VasItem();
		vasItem.setCntrNo(cntrNo);
		vasItem.setVasCd(vasCd);
		vasItem.setVasItemCd(vasItemCd);
		vasItem.setApplTp(applTp);
		
		return vasItem;
	}
	
	/**
	 * 부가 서비스 설정 객체 생성
	 */
	private VaSvcAppl makeVaSvcAppl(String mobileNo, String actionTp, String vasItemId)
	{
		VaSvcAppl vaSvcAppl = new VaSvcAppl();
		vaSvcAppl.setMobileNo(mobileNo);
		vaSvcAppl.setActionTp(actionTp);
		vaSvcAppl.setVasItemId(vasItemId);
		
		return vaSvcAppl;
	}
	
	/**
	 * 부가 서비스 설정을 위한 유효성 체크 후 부가서비스 설정 정보를 반환한다.
	 */
	private VasItem validateVas(String vasCd, String vasItemCd, ContractInfo cntrInfo, String vasUseYn) throws Exception
	{
		// 충전불가 요금제 확인
		if ( YappUtil.isNotEq(vasUseYn, EnumYn.C_Y.getValue()) )
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_VAS_PLAN"));		// 해당 부가서비스를 이용할 수 없는 요금제를 사용중입니다.
		
		// 일시정지/이용정지 상태 확인
		if ( YappUtil.isNotEq(cntrInfo.getCntrcStatusCd(), EnumCntrcStatusCd.C01.getStatusCd()) )
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_VAS_IMPL_ST"));		// 해당 부가서비스를 이용할 수 없는 계약상태입니다.
		
		// 부가서비스 유무 체크
		VasItem paramVaSvc = makeVasItem(vasCd, vasItemCd, null, null);
		VasItem vasInfo = vasService.getVasItemInfo(paramVaSvc);
		if ( vasInfo == null )
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_NOT_EXIST_VAS_CD"));		// 부가 서비스 코드가 존재하지 않습니다.
		
		return vasInfo;
	}
	
}
