package com.kt.yapp.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kt.yapp.common.repository.CommonDao;
import com.kt.yapp.config.LimitationPropConfig;
import com.kt.yapp.domain.AccmGiftData;
import com.kt.yapp.domain.AutoBonusData;
import com.kt.yapp.domain.BonusData;
import com.kt.yapp.domain.ContractInfo;
import com.kt.yapp.domain.DataInfo;
import com.kt.yapp.domain.DataReqInfo;
import com.kt.yapp.domain.Databox;
import com.kt.yapp.domain.GiftData;
import com.kt.yapp.domain.GiftNum;
import com.kt.yapp.domain.GiftPsbInfo;
import com.kt.yapp.domain.GrpCode;
import com.kt.yapp.domain.WsgDataUseQnt;
import com.kt.yapp.em.EnumBonusTp;
import com.kt.yapp.em.EnumMasterNotiMsg;
import com.kt.yapp.exception.YappException;
import com.kt.yapp.soap.SoapConnUtil;
import com.kt.yapp.util.YappUtil;

@Service
public class GiftService 
{
	@Autowired
	private CommonDao cmnDao;
	@Autowired
	private SoapConnUtil soapConnUtil;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private LimitationPropConfig limitConfig;
	@Autowired
	private CmsService cmsService;
	@Autowired
	private CommonService cmnService;
	@Autowired
	private WsgService wsgService;
	@Autowired
	private KosService kosService;

	private static final Logger logger = LoggerFactory.getLogger(GiftService.class);

	/**
	 * 보너스(프로모션) 데이터 정보를 조회한다.
	 */
	public BonusData getBonusData(String bonusId)
	{
		return cmnDao.selectOne("mybatis.mapper.gift.getBonusData", YappUtil.makeParamMap("bonusId", bonusId));
	}
	
	/**
	 * 누적 선물 데이터 목록을 조회한다.
	 */
	public List<AccmGiftData> getAccmGiftDataList(String cntrNo)
	{
		return cmnDao.selectList("mybatis.mapper.gift.getAccmGiftDataList", YappUtil.makeParamMap("sndCntrNo", cntrNo));
	}

	/**
	 * 전체 누적 선물 데이터 목록을 조회한다.
	 */
	public List<AccmGiftData> getAccmGiftDataAllList(String cntrNo)
	{
		return cmnDao.selectList("mybatis.mapper.gift.getAccmGiftDataAllList", YappUtil.makeParamMap("cntrNo", cntrNo));
	}
	
	/**
	 * 선물 데이터 수령 확인처리 한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int updateRcvConfirmGift(String rcvCntrNo)
	{
		Map<String, Object> paramObj = YappUtil.makeParamMap(new String[]{"rcvCntrNo"}, new Object[]{rcvCntrNo});
		
		return cmnDao.update("mybatis.mapper.gift.updateRcvConfirmGift", paramObj);
	}
	
	/**
	 * 조르기 데이터 수신 확인처리 한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int updateRcvConfirmDataReq(String rcvCntrNo)
	{
		Map<String, Object> paramObj = YappUtil.makeParamMap(new String[]{"rcvCntrNo"}, new Object[]{rcvCntrNo});
		
		return cmnDao.update("mybatis.mapper.gift.updateRcvConfirmDataReq", paramObj);
	}
	
	/**
	 * 선물횟수, 용량 정보를 조회한다.(해당월 기준)
	 */
	public GiftNum getGiftNumInfo(String cntrNo)
	{
		return cmnDao.selectOne("mybatis.mapper.gift.getGiftNumInfo", YappUtil.makeParamMap("cntrNo", cntrNo));
	}

	/**
	 * 선물횟수, 용량 정보를 조회한다.(전체 기준)
	 */
	public GiftNum getGiftAllNumInfo(String cntrNo)
	{
		return cmnDao.selectOne("mybatis.mapper.gift.getGiftAllNumInfo", YappUtil.makeParamMap("cntrNo", cntrNo));
	}
	
	/**
	 * 누적 선물횟수, 용량 정보를 조회한다.
	 */
	public GiftNum getGiftNumALLInfo(String cntrNo)
	{
		return cmnDao.selectOne("mybatis.mapper.gift.getGiftNumALLInfo", YappUtil.makeParamMap("cntrNo", cntrNo));
	}
	
	/**
	 * 선물 타입별 선물횟수, 용량 정보를 조회한다.
	 */
	public List<GiftNum> getGiftNumInfoByGiftTypeList(String cntrNo)
	{
		return cmnDao.selectList("mybatis.mapper.gift.getGiftNumInfoByGiftTypeList", YappUtil.makeParamMap("cntrNo", cntrNo));
	}
	
	/**
	 * 선물받은 횟수, 용량 정보를 조회한다.
	 */
	public GiftNum getGiftRcvNumInfo(String cntrNo)
	{
		return cmnDao.selectOne("mybatis.mapper.gift.getGiftRcvNumInfo", YappUtil.makeParamMap("cntrNo", cntrNo));
	}
	
	/**
	 * 보너스 데이터의 선착순 수령 번호를 조회한다.
	 */
	public int getNextArrvOrdNo(String cntrNo, int condArrvOrd)
	{
		Map<String, Object> paramObj = 
				YappUtil.makeParamMap(new String[]{"cntrNo", "condArrvOrd"}, new Object[]{cntrNo, condArrvOrd});

		return cmnDao.selectOne("mybatis.mapper.gift.getNextArrvOrdNo", paramObj);
	}
	
	/**
	 * 선물 가능 정보를 조회한다.
	 */
	public GiftPsbInfo getGiftPsbInfo_back(ContractInfo cntrInfo) throws Exception
	{
		// 내 데이터 정보 조회
		WsgDataUseQnt dataUseQnt = wsgService.getMobileTotalUseWeb(cntrInfo.getCntrNo(), cntrInfo.getMobileNo(), null, cntrInfo.getCallingPlan());

		Databox dboxInfo = kosService.getDboxInfo(cntrInfo.getCntrNo());
		
		// 선물 횟수, 용량 조회
		GiftNum giftNum = getGiftNumInfo(cntrInfo.getCntrNo());
		
		GiftPsbInfo psbInfo = new GiftPsbInfo();
		// 최근 선물 일시
		psbInfo.setMaxRegDt(giftNum.getMaxRegDt());
		// 선물한 횟수
//		psbInfo.setGiftCnt(giftNum.getGiftCnt());
		psbInfo.setGiftCnt(dboxInfo.getGiftCnt());
		// 월 선물 가능 최대 횟수
		psbInfo.setGiftPsbMaxCntPerMnth(limitConfig.getLmtGiftCnt());
		// 내 데이터 잔여량
		psbInfo.setRmnDataAmt(dataUseQnt.getMyRmnDataAmt());
		
		// 선물하기 완료후의 최소 데이터 잔여량 500
		long lmtGiftAfterMinAmt = limitConfig.getLmtGiftAfterMinAmt();
		psbInfo.setGiftMinDataAmtAfter(lmtGiftAfterMinAmt);
		
		// 내 데이터 용량 = 내 데이터 용량 - 선물하기 완료후의 최소 데이터 잔여량
		DataInfo dataInfo = kosService.retrieveDrctlyUseQntDtl2(cntrInfo.getCntrNo(), cntrInfo.getMobileNo());
		long myDataSize = dataUseQnt.getMyDataSize();
		long myDataAmt = dataInfo.getRmnDataAmt() - dataInfo.getDsprCurMnthDataAmt() - lmtGiftAfterMinAmt;
		long myDataAmt2 =  dataUseQnt.getFwdMnthDataAmt() - lmtGiftAfterMinAmt;
		myDataAmt = myDataAmt < myDataAmt2 ? myDataAmt : myDataAmt2;
		//long myDataAmt = dataUseQnt.getMyDataSize() - (dataUseQnt.getCurMnthDataAmt()+ dataUseQnt.getUseDataAmt()) - lmtGiftAfterMinAmt;
		myDataAmt = myDataAmt < 0 ? 0 : myDataAmt;
		
		// 1회 선물 가능 최대 용량 1000
		psbInfo.setGiftPsbMaxDataAmtOneTime(limitConfig.getLmtGiftMaxAmtOneTime());
		
		// 월 선물 가능 최대 용량 2000
		long lmtGiftAmtPerMnth = limitConfig.getLmtGiftAmt();

		
		// 선물한 용량
//		long giftDataAmt = giftNum.getGiftAmt();
		long giftDataAmt = dboxInfo.getGiftDataAmt();
		psbInfo.setGiftDataAmt(giftDataAmt);
		
		// 선물 가능 용량 계산
		long psbDataAmt = lmtGiftAmtPerMnth - giftDataAmt;
		long myDataSize2 = dataUseQnt.getFwdMnthDataAmt();
		if(myDataSize2 < lmtGiftAmtPerMnth){
			lmtGiftAmtPerMnth = myDataSize2;
		}
	
		psbInfo.setGiftPsbMaxDataAmtPerMnth(lmtGiftAmtPerMnth);
		//long myDataAmtTmp = myDataAmt - giftDataAmt;
		
		//myDataAmtTmp = myDataAmtTmp < 0 ? 0 : myDataAmtTmp;
		psbDataAmt = psbDataAmt > myDataAmt ? myDataAmt  : psbDataAmt;
		
		logger.info("========================================================");
		logger.info("myDataSize[총제공량] " + myDataSize);
		logger.info("RmnDataAmt[잔여량] " + dataInfo.getRmnDataAmt());
		logger.info("FwdMnthDataAmt[당원무료제공] " + dataUseQnt.getFwdMnthDataAmt());
		logger.info("UseDataAmt[총사용량] " + dataUseQnt.getUseDataAmt());
		logger.info("giftDataAmt[선물한용량] " + giftDataAmt);
		logger.info("psbDataAmt[선물가능용량] " + psbDataAmt);
		logger.info("GiftPsYn[선물가능여부] " + dataInfo.getGiftPsYn());
		logger.info("========================================================");
		if(("N").equals(dataInfo.getGiftPsYn())){
			psbDataAmt = 0;
		}
		
		// 18세미만  선물가능용량 0MB 세팅 
		if(("Y").equals(cntrInfo.getEightteenYn())){
			psbDataAmt = 0;
		}
		psbInfo.setGiftPsbDataAmt(psbDataAmt / 100 * 100); // 100 이하 버림
		
		return psbInfo;
	}

	
	/**
	 * 선물 가능 정보를 조회한다.
	 */
	public GiftPsbInfo getGiftPsbInfo(ContractInfo cntrInfo) throws Exception
	{
		// 내 데이터 정보 조회
		WsgDataUseQnt dataUseQnt = wsgService.getMobileTotalUseWeb(cntrInfo.getCntrNo(), cntrInfo.getMobileNo(), null, cntrInfo.getCallingPlan());
		
		Databox dboxInfo = kosService.getDboxInfo(cntrInfo.getCntrNo());
		int rmnDataAmt = kosService.getDataInfoBySvcNo(cntrInfo.getMobileNo());
		String curDate = YappUtil.getCurDate("yyyyMMdd");
		//DataInfo dataInfo = kosService.retrieveDrctlyUseQnt(cntrInfo.getCntrNo(), cntrInfo.getMobileNo(), curDate);
		
		// 선물 횟수, 용량 조회
		GiftNum giftNum = getGiftNumInfo(cntrInfo.getCntrNo());
		
		GiftPsbInfo psbInfo = new GiftPsbInfo();
		///DataInfo dataInfo = kosService.retrieveDrctlyUseQntDtl2(cntrInfo.getCntrNo(), cntrInfo.getMobileNo());
		// 최근 선물 일시
		psbInfo.setMaxRegDt(giftNum.getMaxRegDt());
		// 선물한 횟수
//		psbInfo.setGiftCnt(giftNum.getGiftCnt());
		psbInfo.setGiftCnt(dboxInfo.getGiftCnt());
		// 월 선물 가능 최대 횟수
		psbInfo.setGiftPsbMaxCntPerMnth(limitConfig.getLmtGiftCnt());
		// 내 데이터 잔여량
		psbInfo.setRmnDataAmt(dataUseQnt.getMyRmnDataAmt());
		
		// 선물하기 완료후의 최소 데이터 잔여량 500
		long lmtGiftAfterMinAmt = limitConfig.getLmtGiftAfterMinAmt();
		psbInfo.setGiftMinDataAmtAfter(lmtGiftAfterMinAmt);

		// 1회 선물 가능 최대 용량 1000
		psbInfo.setGiftPsbMaxDataAmtOneTime(limitConfig.getLmtGiftMaxAmtOneTime());

		// 월 선물 가능 최대 용량 2000
		long lmtGiftAmtPerMnth = limitConfig.getLmtGiftAmt();

		// 선물한 용량
//		long giftDataAmt = giftNum.getGiftAmt();
		long giftDataAmt = dboxInfo.getGiftDataAmt();
		psbInfo.setGiftDataAmt(giftDataAmt);
		
		//A= 잔여 데이터 용량 (kos)-선물하기 종료후 최소 데이터 잔여량(500)
		//B = 월 선물가능최대용량(2000) - 선물한 용량 (kos)
		//선물가능용량 = A < B ? A : B;
		long tmon = dataUseQnt.getTmonFreeQnt();
		long myDataAmt = rmnDataAmt - lmtGiftAfterMinAmt;
		
		myDataAmt = myDataAmt < 0 ? 0 : myDataAmt;
		long psbAmt = lmtGiftAmtPerMnth - giftDataAmt;
		
		// 선물 가능 용량 계산
		long psbDataAmt =  psbAmt > myDataAmt ? myDataAmt  : psbAmt;
		long myDataSize2 = dataUseQnt.getFwdMnthDataAmt();

		// 무제한일 경우 (월 선물가능최대용량(2000) - 선물한 용량 (kos)로 세팅)
		if(tmon >= 999999999){
			psbDataAmt = psbAmt;
		}else{
			if(myDataSize2 < lmtGiftAmtPerMnth){
				lmtGiftAmtPerMnth = myDataSize2;
			}
		}
		psbInfo.setGiftPsbMaxDataAmtPerMnth(lmtGiftAmtPerMnth);
		
		logger.info("========================================================");
		logger.info("RmnDataAmt[잔여량] " + dataUseQnt.getMyRmnDataAmt());
		logger.info("FwdMnthDataAmt[당원무료제공] " + myDataSize2);
		logger.info("giftDataAmt[선물한용량] " + giftDataAmt);
		logger.info("psbDataAmt[선물가능용량] " + psbDataAmt);
		logger.info("GiftPsYn[선물가능여부] " + cntrInfo.getCallingPlan().getGiftPsYn());
		logger.info("========================================================");
		if(("N").equals(cntrInfo.getCallingPlan().getGiftPsYn())){
			psbDataAmt = 0;
		}
		
		// 18세미만  선물가능용량 0MB 세팅 
		if(("Y").equals(cntrInfo.getEightteenYn())){
			psbDataAmt = 0;
		}
		psbInfo.setGiftPsbDataAmt(psbDataAmt / 100 * 100); // 100 이하 버림
		
		return psbInfo;
	}

	/**
	 * 선물 가능한지 체크한다.
	 */
	public GiftPsbInfo checkGiftPsbInfo(ContractInfo cntrInfo, int giftDataAmt) throws Exception
	{
		// 선물 가능정보 조회
		GiftPsbInfo giftPsbInfo = getGiftPsbInfo(cntrInfo);
		logger.info("선물 용량 : " + giftDataAmt);
		logger.info("선물 가능 정보 : " + giftPsbInfo);
		
		if ( YappUtil.isY(cntrInfo.getCallingPlan().getGiftPsYn()) == false ){
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_GIFT_UNSVC"));		// 데이터 선물을 할 수 없는 요금제를 사용 중입니다.
		}
/*		if ( giftPsbInfo.getGiftCnt() >= giftPsbInfo.getGiftPsbMaxCntPerMnth() )
			throw new YappException(cmnService.getMsg("ERR_GIFT_EXD_CNT"));	// 선물하기 최대횟수를 초과했습니다.
*/		
		if ( giftDataAmt > giftPsbInfo.getGiftPsbMaxDataAmtOneTime() ){
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_GIFT_FRST_EXD_AMT"));	// 선물하기 1회 최대용량을 초과했습니다.
		}
		if ( giftDataAmt > giftPsbInfo.getGiftPsbDataAmt() ){
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_GIFT_LACK_DATA"));	// 선물가능 용량이 부족해요
		}
	//	if ( (giftPsbInfo.getGiftDataAmt() + giftDataAmt) > giftPsbInfo.getGiftPsbMaxDataAmtPerMnth() )
		if ( giftDataAmt > giftPsbInfo.getGiftPsbMaxDataAmtPerMnth() ){
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_GIFT_USE_GIFT_AMT"));	// 이번달 선물 용량을 모두 사용하셨습니다.
		}

		GrpCode grpCode = cmsService.getCodeNm("GIFT_WAIT_TIME", "D0002");		//꺼내기 시간을 공통코드에서 가지고 온다.
		int waitMinute = Integer.parseInt(grpCode.getCodeNm());
		logger.info("대기 시간 : " + waitMinute);
		if ( YappUtil.isNotEmpty(giftPsbInfo.getMaxRegDt()))
		{
			Calendar maxRegCal = Calendar.getInstance();
			maxRegCal.setTime(giftPsbInfo.getMaxRegDt());
			maxRegCal.add(Calendar.MINUTE, waitMinute);
			
			if ( Calendar.getInstance().getTimeInMillis() < maxRegCal.getTimeInMillis() )
				throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_GIFT_CHK_MIN", YappUtil.makeParamMap("waitMinute", waitMinute)));	// 일정시간( @waitMinute@분) 이 지난 후 다시 이용해주세요.
				//throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_GIFT_CHK_10MIN"));	// 일정시간(10분) 이 지난 후 다시 이용해주세요.
		}

		return giftPsbInfo;
	}
	
	
	/**
	 * 자동 보너스 데이터를 조회한다.
	 */
	public List<BonusData> getAutoBonusDataList(String cntrNo)
	{
		return cmnDao.selectList("mybatis.mapper.gift.getAutoBonusDataList", YappUtil.makeParamMap("cntrNo", cntrNo));
	}
	
	/**
	 * 보너스 데이터 수령 정보를 조회한다.
	 */
	public List<BonusData> getRcvBonusDataList(String bonusId, String cntrNo)
	{
		Map<String, Object> paramObj = 
				YappUtil.makeParamMap(new String[]{"bonusId", "cntrNo"}, new Object[]{bonusId, cntrNo});

		return cmnDao.selectList("mybatis.mapper.gift.getRcvBonusDataList", paramObj);
	}
	
	/**
	 * 보너스(프로모션) 데이터를 수령한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int receiveBonusData(String bonusId, String cntrNo, String mobileNo) throws Exception
	{
		// 보너스 타입 정보 조회
		BonusData bonusDataInfo = getBonusData(bonusId);
		
		Map<String, Object> paramObj = 
				YappUtil.makeParamMap(new String[]{"bonusId", "cntrNo"}, new Object[]{bonusId, cntrNo});

		int insCnt = cmnDao.insert("mybatis.mapper.gift.receiveBonusData", paramObj);
		
		// 데이터 박스에 추가
		kosService.receiveBonusData(mobileNo, bonusDataInfo.getDataAmt(), EnumBonusTp.getObj(bonusDataInfo.getBonusTp()).getValue());
		
		return insCnt;
	}
	
	/**
	 * 선물하기 데이터를 추가한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int insertGiftData(GiftData giftDataParam, String appVrsn) throws Exception
	{
		// KOS 선물하기
		kosService.giftData(giftDataParam.getSndMobileNo(), giftDataParam.getRcvMobileNo(), (int) giftDataParam.getDataAmt());

		// 선물하기 추가
		int insCnt = cmnDao.insert("mybatis.mapper.gift.insertGiftData", giftDataParam);
				
		cmsService.insertNotiMsg(
				giftDataParam.getSndCntrNo()
				, 26
				, new String[]{"dataAmt"}
				, new Object[]{giftDataParam.getDataAmt()}
				, null, appVrsn);
		
		// 선물받기 알림 메시지 추가
		cmsService.insertNotiMsg(
				giftDataParam.getRcvCntrNo()
				, EnumMasterNotiMsg.G0001.getSeq()
				, new String[]{"sndUserNm", "dataAmt"}
				, new Object[]{YappUtil.blindNameToName2(giftDataParam.getSndUserNm(), 1), giftDataParam.getDataAmt()}
				, null, appVrsn);		

		return insCnt; 
	}

	/**
	 * 자동 선물하기 여부 조회 후 보너스 데이터 추가.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void insertAutoBonusData(String cntrNo) throws Exception
	{
		List<AutoBonusData> autoBonusData = cmnDao.selectList("mybatis.mapper.gift.getAutoBonusDataRcvInfo", YappUtil.makeParamMap("cntrNo", cntrNo));
		if(autoBonusData != null){
			int FirstSndBonusCnt = autoBonusData.get(0).getBonusDataCnt();
			BonusData bonusData = new BonusData();
			// 최초 선물 데이터  보너스 데이터 저장(선물)
			if (FirstSndBonusCnt == 0) {
				SimpleDateFormat sDate = new SimpleDateFormat("yyyyMMddHHmmssSSS");
				Date now = new Date();
				String timeTmp = sDate.format(now);
				bonusData.setBonusId(timeTmp);
				bonusData.setDataAmt(100);
				bonusData.setTitle("최초 선물 기념");
				bonusData.setRemarks("최초 선물 기념 보너스 데이터");
				bonusData.setRcvCntrNo(cntrNo);
				cmnDao.insert("mybatis.mapper.gift.insertBonusData", bonusData);
				FirstSndBonusCnt = 1;
			}
			int sndBonusCnt = autoBonusData.get(0).getRcvDataCnt() - (FirstSndBonusCnt-1);
			int rcvDataAmt = 0;
			// 5기가 누적 보너스 테이터 저장(선물)
			if (sndBonusCnt > 0) {
				for(int idx = 1; idx <= sndBonusCnt; idx++ ){
					rcvDataAmt = (idx * 5) + ((FirstSndBonusCnt-1) * 5);
					SimpleDateFormat sDate = new SimpleDateFormat("yyyyMMddHHmmssSSS");
					Date now = new Date();
					String timeTmp = sDate.format(now);
					bonusData.setBonusId(timeTmp);
					bonusData.setDataAmt(100);
					bonusData.setTitle("누적 " + rcvDataAmt + "기가 기념");
					bonusData.setRemarks("누적 " + rcvDataAmt + "기가 기념 보너스 데이터");
					bonusData.setRcvCntrNo(cntrNo);
					cmnDao.insert("mybatis.mapper.gift.insertBonusData", bonusData);
				}
			}
		}
	}

	/**
	 * 선물하기 목록을 조회한다.
	 */
	public List<GiftData> getGiftDataList(GiftData paramObj)
	{
		return cmnDao.selectList("mybatis.mapper.gift.getGiftDataList", paramObj);
	}
	
	/**
	 * 받은 선물 목록을 조회한다.
	 */
	public List<GiftData> getRcvGiftDataList(GiftData paramObj)
	{
		return cmnDao.selectList("mybatis.mapper.gift.getRcvGiftDataList", paramObj);
	}
	
	/**
	 * 조르기 목록을 조회한다.
	 */
	public List<GiftData> getDataReqList(GiftData paramObj)
	{
		return cmnDao.selectList("mybatis.mapper.gift.getDataReqList", paramObj);
	}

	/**
	 * 조르기 목록을 조회한다.
	 */
	public List<GiftData> getDataReqInfoList(GiftData paramObj)
	{
		return cmnDao.selectList("mybatis.mapper.gift.getDataReqInfoList", paramObj);
	}

	/**
	 * 선물 목록을 조회한다.
	 */
	public List<GiftData> getGiftInfoList(GiftData paramObj)
	{
		return cmnDao.selectList("mybatis.mapper.gift.getGiftInfoList", paramObj);
	}
	
	/**
	 * 꺼낸 목록을 조회한다.
	 */
	public List<GiftData> getPullInfoList(GiftData paramObj)
	{
		return cmnDao.selectList("mybatis.mapper.gift.getPullInfoList", paramObj);
	}

	/**
	 * 조르기 정보를 추가한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int insertDataReq(DataReqInfo paramObj, String appVrsn) throws Exception
	{
		// 조르기 정보 추가
		int insCnt = cmnDao.insert("mybatis.mapper.gift.insertDataReq", paramObj);
		
		// 알림 메시지 추가
		cmsService.insertNotiMsg(
				paramObj.getRcvCntrNo()
				, EnumMasterNotiMsg.G0003.getSeq()
				, new String[]{"sndUserNm", "dataAmt"}
				, new Object[]{YappUtil.blindNameToName2(paramObj.getSndUserNm(), 1), paramObj.getDataAmt()}
				, "mobileNo=" + paramObj.getSndMobileNo() + ",userNm=" + YappUtil.blindNameToName(paramObj.getSndUserNm(), 1), appVrsn);
		
		return insCnt;
	}
	
	/**
	 * 선물하기 비번 설정 여부를 조회한다.
	 */	
	public int getGiftPwYn(String cntrNo)
	{
		return cmnDao.selectOne("mybatis.mapper.gift.getGiftPwYn", YappUtil.makeParamMap("cntrNo", cntrNo));
	}
	
	/**
	 * 선물하기 비번를 체크한다.
	 */	
	public int checkGiftPw(String cntrNo, String giftPw)
	{
		Map<String, Object> paramObj = 
				YappUtil.makeParamMap(new String[]{"cntrNo", "giftPw"}, new Object[]{cntrNo, giftPw});

		return cmnDao.selectOne("mybatis.mapper.gift.checkGiftPw", paramObj);
	}
	
	/**
	 * 선물하기 비번를 추가한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int insertGiftPw(String cntrNo, String giftPw) throws Exception
	{
		Map<String, Object> paramObj = 
				YappUtil.makeParamMap(new String[]{"cntrNo", "giftPw"}, new Object[]{cntrNo, giftPw});
		return cmnDao.insert("mybatis.mapper.gift.insertGiftPw", paramObj);
	}
	
	/**
	 * 선물하기 비번를 수정한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int updateGiftPw(String cntrNo, String giftPw)
	{
		Map<String, Object> paramObj = 
				YappUtil.makeParamMap(new String[]{"cntrNo", "giftPw"}, new Object[]{cntrNo, giftPw});
		
		return cmnDao.update("mybatis.mapper.gift.updateGiftPw", paramObj);
	}
	
	/**
	 * 선물하기 비번를 삭제한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int deleteGiftPw(String cntrNo)
	{
		return cmnDao.delete("mybatis.mapper.gift.deleteGiftPw", YappUtil.makeParamMap("cntrNo", cntrNo));
	}
	
	/**
	 * 보너스(프로모션) 데이터를 수령한다.
	 * TestController용(실제 미사용)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int receiveBonusData(String bonusId, String cntrNo, String mobileNo, String expireTerm) throws Exception
	{
		// 보너스 타입 정보 조회
		BonusData bonusDataInfo = getBonusData(bonusId);
		
		Map<String, Object> paramObj = 
				YappUtil.makeParamMap(new String[]{"bonusId", "cntrNo"}, new Object[]{bonusId, cntrNo});

		int insCnt = 1;//cmnDao.insert("mybatis.mapper.gift.receiveBonusData", paramObj);
		
		// 데이터 박스에 추가
		kosService.receiveBonusData(mobileNo, bonusDataInfo.getDataAmt(), EnumBonusTp.getObj(bonusDataInfo.getBonusTp()).getValue(), expireTerm);
		
		return insCnt;
	}
}
