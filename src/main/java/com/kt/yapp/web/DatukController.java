package com.kt.yapp.web;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kt.yapp.config.LimitationPropConfig;
import com.kt.yapp.domain.CallingPlan;
import com.kt.yapp.domain.ContractInfo;
import com.kt.yapp.domain.Datuk;
import com.kt.yapp.domain.DatukDtl;
import com.kt.yapp.domain.DatukRcv;
import com.kt.yapp.domain.DatukRtn;
import com.kt.yapp.domain.GiftNum;
import com.kt.yapp.domain.GrpCode;
import com.kt.yapp.domain.SmsContents;
import com.kt.yapp.domain.resp.ResultInfo;
import com.kt.yapp.em.EnumBlock;
import com.kt.yapp.em.EnumCntrcStatusCd;
import com.kt.yapp.em.EnumMasterNotiMsg;
import com.kt.yapp.em.EnumYn;
import com.kt.yapp.exception.YappException;
import com.kt.yapp.service.CmsService;
import com.kt.yapp.service.CommonService;
import com.kt.yapp.service.DatukService;
import com.kt.yapp.service.GiftService;
import com.kt.yapp.service.KosService;
import com.kt.yapp.service.ShubService;
import com.kt.yapp.service.UserService;
import com.kt.yapp.soap.response.SoapResponse139;
import com.kt.yapp.util.AppEncryptUtils;
import com.kt.yapp.util.SessionKeeper;
import com.kt.yapp.util.YappCvtUtil;
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
@Api(description="데이턱 기능 처리")
public class DatukController 
{
	private static final Logger logger = LoggerFactory.getLogger(DatukController.class);
	
	@Autowired
	private CommonService cmnService;
	@Autowired
	private KosService  kosService;
	@Autowired
	private DatukService datukService;
	@Autowired
	private GiftService giftService;
	@Autowired
	private ShubService shubService;
	@Autowired
	private UserService userService;
	@Autowired
	private LimitationPropConfig limitPropConfig;
	@Autowired
	private CmsService cmsService;
	@Autowired
	private AppEncryptUtils appEncryptUtils;

	@RequestMapping(value = "/datukNew", method = RequestMethod.POST)
	@ApiOperation(value="데이턱 생성", notes="데이턱 ID 반환")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<Datuk> createDatukNew(@RequestParam String cntrNo, @RequestParam int datukAmt, @ApiParam(value="SHA256 암호화된 비밀번호")	@RequestParam String giftPw, HttpServletRequest req) throws Exception
//	public ResultInfo<String> createDatuk(@RequestParam String cntrNo, @RequestParam int datukAmt, HttpServletRequest req) throws Exception
	{
		String appVrsn = req.getHeader("appVrsn");
		List<GrpCode> srvBlockList = cmsService.getYBoxSrvBlockServerList();
		for(GrpCode srvBlock : srvBlockList) {
			logger.info(srvBlock.getCodeId()+", "+srvBlock.getUseYn());
			if(EnumBlock.SB007.getValue().equals(srvBlock.getCodeId()) && EnumYn.C_N.getValue().equals(srvBlock.getUseYn())) {
				throw new YappException("CHECK_MSG", cmnService.getMsg("SRV_BLOCK_DATUK"));
			}
		}
		
		String mobileNo = null;
		if(SessionKeeper.getSdata(req) != null){
			mobileNo = SessionKeeper.getSdata(req).getMobileNo();
		}
		ContractInfo cntrInfo = shubService.callFn139(mobileNo).getCntrInfo();
		CallingPlan cplan = cntrInfo.getCallingPlan();

		// 데이턱하기 전 비밀번호 다시 체크
		int chkCount = giftService.checkGiftPw(cntrInfo.getCntrNo(), giftPw);
		if(chkCount < 1){
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_NOT_SAME_PW"));
		}
		
		if ( YappUtil.isNotEq(cntrNo, cntrInfo.getCntrNo()) ){
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_NOT_SAME_LOGIN"));	// 로그인된 정보와 일치하지 않습니다.
		}
		
		if ( YappUtil.isNotEq(cntrInfo.getCntrcStatusCd(), EnumCntrcStatusCd.C01.getStatusCd()) ){
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_VALID_LINE"));	// 해당회선이 유효하지 않습니다.
		}
		// 선물 가능 정보를 체크한다.
		giftService.checkGiftPsbInfo(cntrInfo, datukAmt);
		
		// 진행중인 데이턱 조회
		Datuk datukInfo = new Datuk();
		datukInfo.setCntrNo(cntrInfo.getCntrNo());
		datukInfo.setIsAbleJoin(EnumYn.C_Y.getValue());
		Datuk shareDdatukData = datukService.getShareDatukData(cntrInfo.getCntrNo());
		if ( shareDdatukData != null )
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_DATUK_SHARE"));	// 공유중인 데이턱이 존재합니다.
		
		// 생성 가능시간 체크 (월말일 D-3일까지 생성가능) 2018-02-14 최유순 과장님 요청(채한얼 과장님 요청)
		/*Calendar curCal = Calendar.getInstance();
		int maxDate = curCal.getActualMaximum(Calendar.DAY_OF_MONTH) - 3;
		if ( curCal.get(Calendar.DAY_OF_MONTH) > maxDate ){
			throw new YappException(cmnService.getMsg("ERR_DATUK_CANT_CREATE").replaceAll("@date@", String.valueOf(maxDate)));	// 이번달 데이턱은 @date@일까지 생성 가능합니다.
		}*/
		
		// 데이턱 생성
		Datuk datukData = new Datuk();
		datukData.setDatukId(UUID.randomUUID().toString().replaceAll("-", ""));
		datukData.setCntrNo(cntrNo);
		datukData.setMobileNo(cntrInfo.getMobileNo());
		datukData.setUserNm(YappUtil.blindNameToName(cntrInfo.getUserNm(), 1));
		datukData.setDatukAmt(datukAmt);

		datukData = datukService.insertDatukData(datukData);
		
		
		// SMS 호출
		String CBC		 = "114";
		String AUTH_REQ = "/datukNew";
		
		SmsContents paramObj = new SmsContents();
		paramObj.setRcvMobileNo(cntrInfo.getMobileNo());
		paramObj.setAccessUrl(AUTH_REQ);
		paramObj.setCallbackCtn(CBC);
		paramObj.setMsgContents("[KT] Y박스 데이턱이 생성되었습니다. 데이턱URL(공유용) : https://ybox.kt.com/share.html?datukid=" + datukData.getDatukId());
		shubService.callFn2118(paramObj);
		
		logger.info("datuk id : " + datukData.getDatukId());
		
		// 데이턱 보내기 알림 메시지 추가
		cmsService.insertNotiMsg(datukData.getCntrNo(), EnumMasterNotiMsg.G0007.getSeq(),
				null,
				null,
				null, appVrsn);

		// 데이턱 생성정보 반환
		return new ResultInfo<>(datukService.getDatukDataInfo(datukData.getDatukId()));
//		return new ResultInfo<>(datukData.getDatukId());
	}

	@RequestMapping(value = "/datuk/receive", method = RequestMethod.GET)
	@ApiOperation(value="수령 대상 데이턱 데이터 조회")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<Datuk> getRcvDatukData(String datukId, HttpServletRequest req) throws Exception
	{
		String loginCntrNo = SessionKeeper.getCntrNo(req);

		Datuk datukData = datukService.getDatukDataInfo(datukId);

		if ( datukData == null ){
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_DATUK_NO_DATA"));	// 데이턱 데이터가 없습니다.
		}

		// 본인이 생성한 데이턱은 수령 대상에서 제외한다.
		if ( datukData != null && YappUtil.isEq(loginCntrNo, datukData.getCntrNo())){
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_DATUK_SELF_RCV"));	// 본인이 생성한 데이턱은 수령 할 수 없습니다.
		}

		return new ResultInfo<>(datukData);
	}
	
	@RequestMapping(value = "/datuk/receive", method = RequestMethod.POST)
	@ApiOperation(value="데이턱 데이터 수령")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<String> receiveDatukData(@RequestParam String datukId, HttpServletRequest req) throws Exception
	{
		String cntrNo = SessionKeeper.getCntrNo(req);
		String mobileNo = null;
		if(SessionKeeper.getSdata(req) != null){
			mobileNo = SessionKeeper.getSdata(req).getMobileNo();
		}
		ContractInfo cntrInfo = shubService.callFn139(mobileNo, false, true).getCntrInfo();
		String appVrsn = req.getHeader("appVrsn");
		// 데이턱 기간 체크
		Datuk datukData = datukService.getDatukDataInfo(datukId);
		if ( datukData == null ){
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_DATUK_NO_DATA"));	// 데이턱 데이터가 없습니다.
		}

		if ( YappUtil.isEq(datukData.getCntrNo(), cntrNo) ){
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_DATUK_SELF_RCV"));	// 본인이 생성한 데이턱은 수령 할 수 없습니다.
		}

		if ( YappUtil.isNotEq(cntrInfo.getCallingPlan().getDboxUsePsYn(), EnumYn.C_Y.getValue()) ){
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_DATUK_UNSVC"));		// 데이터박스 사용불가 요금제는 참여하실 수 없어요.
		}

		if ( datukData.getJoinEdYmd().compareTo(YappUtil.getCurDate("yyyyMMdd")) < 0 ){
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_DATUK_END_PERIOD"));	// 데이턱 기간이 종료되었어요. 다음기회에…
		}

		// 데이턱 모 회선이 회선해지/명의변경 으로 회선 정보가 말소되는 경우 데이턱 수령금지
		SoapResponse139 resp139 = null;
		try {
			resp139 = shubService.callFn139(datukData.getMobileNo(), false, false);
		} catch (YappException ye) {
			logger.error("DatukController  receiveDatukData callFn139 : ERROR");
		} finally {
			if ( resp139 == null || resp139.getCntrInfo() == null 
					||  YappUtil.isNotEq(resp139.getCntrInfo().getCntrcStatusCd(), EnumCntrcStatusCd.C01.getStatusCd()) ) 
					throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_DATUK_NOT_VALID_CRT_LINE"));	// 데이턱 생성회선이 유효하지 않습니다.
		}
		
		// 중복여부 체크
		// 수령 데이턱 목록 조회
		List<DatukRcv> datukRcvList = datukService.getDatukRcvDataList(cntrNo, datukId);
		if ( YappUtil.isNotEmpty(datukRcvList) )
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_DATUK_DUP_RCV"));	// 한번 받은 데이턱은 다시 받을 수 없어요.
		
		// 적립한도 체크
		GiftNum rcvGiftNum = giftService.getGiftRcvNumInfo(cntrNo);
		if ( rcvGiftNum.getGiftAmt() + limitPropConfig.getLmtDatukRcvDataSize() > limitPropConfig.getLmtDboxGiftMaxAmt() )
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_DATUK_EXD_SAVE"));	// 데이터가 너무 많아서 담을 공간이 없어요.

		// 최대 10번 조회
		int tmpNum = 0;
		while (tmpNum < 10)
		{
			tmpNum = tmpNum + 1;
			// 수령번호 조회
			int nextDatukRcvNo = datukService.getNextDatukRcvNo(datukData);
			// 선착순 마감여부 체크
			if ( nextDatukRcvNo == -1 )
				throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_DATUK_SELLOUT"));	// 데이턱이 매진되었어요.
			
			// 수령 정보 추가
			DatukRcv datukRcvData = new DatukRcv();
			datukRcvData.setDatukId(datukId);
			datukRcvData.setRcvCntrNo(cntrNo);
			datukRcvData.setRcvAmt(limitPropConfig.getLmtDatukRcvDataSize());
			datukRcvData.setRcvMobileNo(mobileNo);
			//datukRcvData.setRcvUserNm(cntrInfo.getUserNm());
			datukRcvData.setDatukRcvNo(nextDatukRcvNo);

			// 동시 진입으로 Duplicate 에러가 발생한 경우는 재실행
			try {
				datukService.insertDatukRcvData(datukRcvData, datukData, appVrsn);
			} catch (DuplicateKeyException de) {
				continue;
			}
			tmpNum = 10;
			//break;
		}
		
		return new ResultInfo<>();
	}
	
	@RequestMapping(value = "/datuk/return", method = RequestMethod.POST)
	@ApiOperation(value="데이턱 데이터를 회수 처리한다.", notes="데이턱ID 가 지정되지 않으면 해당 고객의 전체 데이턱 데이터를 회수처리 한다.")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<Integer> returnDatukData(@RequestParam String cntrNo, String datukId, HttpServletRequest req) throws Exception
	{
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		String appVrsn = req.getHeader("appVrsn");
		if ( YappUtil.isNotEq(cntrNo, loginCntrNo) )
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_NOT_SAME_LOGIN"));	// 로그인된 정보와 일치하지 않습니다.
		
		List<Datuk> datukRtnList = datukService.getDatukRtnDataList(cntrNo, datukId);
		int rtnCnt = 0;
		int rtnAmt = 0;
		for ( Datuk datukRtn : datukRtnList )
		{
			if(datukRtn != null && appVrsn != null){
				rtnCnt += datukService.returnDatukData(YappCvtUtil.cvt(datukRtn, new DatukRtn()), appVrsn);
				rtnAmt += datukRtn.getRtnAmt();
			}
			
		}
		
		logger.info("회수처리 데이터 수 : " + rtnCnt + ", 데이터량 : " + rtnAmt);
		
		return new ResultInfo<>(rtnCnt);
	}

	@RequestMapping(value = "/datuk/retrn", method = RequestMethod.POST)
	@ApiOperation(value="데이턱 데이터를 회수 처리한다.", notes="데이턱ID 가 지정되지 않으면 해당 고객의 전체 데이턱 데이터를 회수처리 한다.")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<Integer> retrnDatukData(@RequestParam String cntrNo, String datukId, HttpServletRequest req) throws Exception
	{
		String loginCntrNo = SessionKeeper.getCntrNo(req);
		String appVrsn = req.getHeader("appVrsn");
		String loginMobileNo = null;
		if(SessionKeeper.getSdata(req) != null){
			loginMobileNo = SessionKeeper.getSdata(req).getMobileNo();
		}
		String decCntrNo  = null;
		if(loginMobileNo != null){
			decCntrNo  = appEncryptUtils.aesDec128(cntrNo, loginMobileNo);
		}
		if ( YappUtil.isNotEq(decCntrNo, loginCntrNo) )
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_NOT_SAME_LOGIN"));	// 로그인된 정보와 일치하지 않습니다.
		
		List<Datuk> datukRtnList = datukService.getDatukRtnDataList(decCntrNo, datukId);
		int rtnCnt = 0;
		int rtnAmt = 0;
		for ( Datuk datukRtn : datukRtnList )
		{
			if(datukRtn != null && appVrsn != null){
				rtnCnt += datukService.returnDatukData(YappCvtUtil.cvt(datukRtn, new DatukRtn()), appVrsn);
				rtnAmt += datukRtn.getRtnAmt();
			}
		}
		
		logger.info("회수처리 데이터 수 : " + rtnCnt + ", 데이터량 : " + rtnAmt);
		
		return new ResultInfo<>(rtnCnt);
	}

	@RequestMapping(value = "/datuk/dtl", method = RequestMethod.GET)
	@ApiOperation(value="데이턱 상세 데이터 조회")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<DatukDtl> getDatukDataDtl(@ApiParam(value="유효시작일") @RequestParam String validStYmd, String datukId, HttpServletRequest req) throws Exception
	{
		String loginCntrNo = SessionKeeper.getCntrNo(req);

		// 데이터박스 ID 조회
		DatukDtl datukInfo = datukService.getDatukInfo(loginCntrNo, validStYmd, datukId);
		List<DatukDtl> datukDtlList = kosService.retrieveDataOneShotDtl(datukInfo.getDboxId(), datukInfo.getDatukId(), validStYmd);
		return new ResultInfo<>(datukDtlList);
	}

	@RequestMapping(value = "/datuk/List", method = RequestMethod.GET)
	@ApiOperation(value="데이턱 정보 데이터 조회")
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<Datuk> getDatukDataList(@ApiParam(value="기준날짜") @RequestParam String validStYmd, HttpServletRequest req) throws Exception
	{
		String loginCntrNo = SessionKeeper.getCntrNo(req);

		List<Datuk> datukDtlList = datukService.getDatukInfoList(loginCntrNo, validStYmd);
		return new ResultInfo<>(datukDtlList);
	}
}
