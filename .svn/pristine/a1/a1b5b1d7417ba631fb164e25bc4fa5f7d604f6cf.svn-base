package com.kt.yapp.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kt.yapp.common.repository.CommonDao;
import com.kt.yapp.domain.AplyRoaming;
import com.kt.yapp.domain.GrpCode;
import com.kt.yapp.domain.Roaming;
import com.kt.yapp.domain.SvcHstByCustInfo;
import com.kt.yapp.domain.VasItems;
import com.kt.yapp.exception.YappException;
import com.kt.yapp.util.YappUtil;

/**
 * YRoamingService.java
 * 
 * @author min
 * @since 2019. 12. 02.
 * @version 1.0
 * 
 * Modification Information
 * Mod Date		Modifier		Description
 * ========================================
 *
 * Copyright (c) 2018 KTDS, Inc. All Rights Reserved
 */
@Service
public class YRoamingService
{
	private static final Logger logger = LoggerFactory.getLogger(YRoamingService.class);
	
	@Autowired
	private CommonDao cmnDao;
	@Autowired
	private CmsService cmsService;
	@Autowired
	private ShubService shubService;
	@Autowired
	private KosService kosService;
	@Autowired
	private CommonService cmnService;

	/**
	 * 로밍을 조회한다.
	 */
	public List<Roaming> getRoaming(String cntrNo, String ppCatL) throws Exception
	{
		List<Roaming> roamingList = new ArrayList<Roaming>();
		// 요금제에 따른 로밍상품
		/**
		 * 2020.01.03 KCH
		 * - PLAN-419 (LTE일 경우 : USADTROMC, 5G일 경우 : DTROAM05D)
		 * - 코드변경 : USADTROMC --> LDTROM05D
		 */
		String itemCd = "";
		if ( YappUtil.isEq(ppCatL, "G0001") == true || YappUtil.isEq(ppCatL, "G0002") == true){
			// itemCd = "USADTROMC";
			itemCd = "LDTROM05D";
		}else if(YappUtil.isEq(ppCatL, "G0005") == true){
			// 아래 운영버전
			itemCd = "DTROAM05D";
			// SIT 테스트   DTROAM05D -> USADTROMC
			// itemCd = "USADTROMC";
		}

		Calendar curCal = Calendar.getInstance();
		String srchYm = YappUtil.getCurDate(curCal.getTime(), "yyyyMMddHHmmss");
		String srchY = YappUtil.getCurDate(curCal.getTime(), "yyyy");

		String status = "N";
		String efctStDt = "";
		String efctFnsDt = "";
		String prodHstSeq = "";
		// status(상태값) -> N : 미사용, I : 사용(해지가능), C : 사용완료
		//부가서비스 이력
		List<SvcHstByCustInfo> vasHst = kosService.getSvcHstByCustIdRetv(cntrNo);
		for ( SvcHstByCustInfo hstItems : vasHst ){
			// if(YappUtil.isEq(hstItems.getProdId(), itemCd) && YappUtil.isEq(hstItems.getEfctFnsDt().substring(0, 4), srchY)){
			/**
			 * 2020.01.03 KCH
			 * - PLAN-419 (LTE일 경우 : USADTROMC, 5G일 경우 : DTROAM05D)
			 * - 코드변경 : USADTROMC --> LDTROM05D
			 */
			// if(YappUtil.contains(hstItems.getProdId(), "USADTROMC", "DTROAM05D")
			// 아래 : 현재 운영버전
			if(YappUtil.contains(hstItems.getProdId(), "LDTROM05D", "DTROAM05D")
			// SIT 테스트
			// if(YappUtil.contains(hstItems.getProdId(), "LDTROM05D", "USADTROMC")
					&& (YappUtil.isEq(Integer.valueOf(hstItems.getEfctFnsDt().substring(0, 4)), Integer.valueOf(srchY)) || YappUtil.isEq(Integer.valueOf(hstItems.getEfctFnsDt().substring(0, 4)), Integer.valueOf(srchY)+1))){
				//logger.info("long stDt :" + YappUtil.toLong(hstItems.getEfctStDt()) +", ymlong :" + YappUtil.toLong(srchYm));
				if(YappUtil.toLong(hstItems.getEfctStDt()) < YappUtil.toLong(srchYm)){
					status = "C";
				}else{
					status = "I";
				}
				efctStDt = hstItems.getEfctStDt();
				efctFnsDt = hstItems.getEfctFnsDt();
				prodHstSeq = hstItems.getProdHstSeq();
				
				break;
			}else{
				status = "N";
			}
		}

		Map<String, Object> paramObj = 
				YappUtil.makeParamMap(new String[]{"itemCd", "status", "efctStDt", "efctFnsDt", "prodHstSeq"}, new Object[]{itemCd, status, efctStDt, efctFnsDt, prodHstSeq});

		// 해당 로밍상품 리스트
		roamingList = cmnDao.selectList("mybatis.mapper.yroaming.getYRoaming", paramObj);

		return roamingList;
	}

	/**
	 * 청소년 일시허용 체크
	 */
	public String chkVasEgg(String cntrNo) throws Exception
	{
		String chkYn = "N";
		Calendar curCal = Calendar.getInstance();
		String srchYm = YappUtil.getCurDate(curCal.getTime(), "yyyyMMddHHmmss");
		// NOIPCEXP : 청소년 일시 허용
		List<SvcHstByCustInfo> vasHst = kosService.getSvcHstByCustIdRetv(cntrNo);
		for ( SvcHstByCustInfo hstItems : vasHst ){
			if(YappUtil.contains(hstItems.getProdId(), "NOIPCEXP") && (YappUtil.toLong(hstItems.getEfctFnsDt()) > YappUtil.toLong(srchYm))){
				chkYn = "Y";
			}
		}

		return chkYn;
	}

	/**
	 * 로밍부가 서비스 차단 체크.
	 */
	public List<Roaming> chkRoaming(String cntrNo, String ppCatL) throws Exception
	{
		Roaming roamingBlockInfo = new Roaming();
		List<Roaming> roamingList = new ArrayList<Roaming>();
		// 요금제에 따른 로밍상품
		String itemCd = "";
		if ( YappUtil.isEq(ppCatL, "G0001") == true || YappUtil.isEq(ppCatL, "G0002") == true){
			itemCd = "LDTROM05D";
		}else if(YappUtil.isEq(ppCatL, "G0005") == true){
			itemCd = "DTROAM05D";
		}

		Calendar curCal = Calendar.getInstance();
		String srchYm = YappUtil.getCurDate(curCal.getTime(), "yyyyMMddHHmmss");
		String srchY = YappUtil.getCurDate(curCal.getTime(), "yyyy");
		// 184연동으로 부가서비스 조회
		List<VasItems> vasList = shubService.callFn184a(cntrNo);
		//차단 리스트조회
		List<GrpCode> blkRoamingList = cmsService.getGrpCodeList("YROAM_BLK");
		String chkYn = "N";
		int chkCnt = 0;
		for ( VasItems vasItems : vasList ){
			for ( GrpCode codeItem : blkRoamingList ){
				if(YappUtil.isEq(vasItems.getVasCd(), codeItem.getCodeKey())){
					System.out.println("vasItems.getVasCd() >   " + vasItems.getVasCd());
					System.out.println("srchYm >   " + srchYm);
					if(YappUtil.isEq(vasItems.getVasCd(), "DTRMBLOCK") &&  YappUtil.toLong(YappUtil.getCurDate(vasItems.getVasEndDate(), "yyyyMMddHHmmss")) >  YappUtil.toLong(srchYm)){
						System.out.println("date >   " + YappUtil.toLong(YappUtil.getCurDate(vasItems.getVasEndDate(), "yyyyMMddHHmmss")));
						chkYn = "Y";
					}

					if(chkCnt < 1 || !chkYn.equals("Y")){
						roamingBlockInfo.setProdTypeCode("B");
						roamingBlockInfo.setVasItemCd(vasItems.getVasCd());
						roamingBlockInfo.setVasItemNm(vasItems.getVasNm());
						roamingBlockInfo.setEfctStDt(YappUtil.getCurDate(vasItems.getVasStartDate(), "yyyyMMddHHmmss"));
						roamingBlockInfo.setEfctFnsDt(YappUtil.getCurDate(vasItems.getVasEndDate(), "yyyyMMddHHmmss"));
					}
					chkCnt++;
				}
			}
		}

		// status(상태값) -> N : 미사용, I : 사용(해지가능), C : 사용완료
		//부가서비스 이력
		List<SvcHstByCustInfo> vasHst = kosService.getSvcHstByCustIdRetv(cntrNo);
		for ( SvcHstByCustInfo hstItems : vasHst ){
			if(YappUtil.isEq(hstItems.getProdId(), itemCd) && YappUtil.isEq(hstItems.getEfctFnsDt().substring(0, 4), srchY)){
				if(YappUtil.toLong(hstItems.getEfctStDt()) < YappUtil.toLong(srchYm)){
					chkCnt--;
				}
			}
		}

		if(chkCnt == 1){
			roamingList.add(roamingBlockInfo);
		}else if(chkCnt > 1){
			chkCnt--;
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_VAS_BLOCK_ROAMING", YappUtil.makeParamMap(new String[]{"vasNm", "cnt"}, new Object[]{roamingBlockInfo.getVasItemNm() , chkCnt})));
		}
		return roamingList;
	}	

	/**
	 * (사전 체크) 로밍을 신청한다. 
	 * PLAN-426 Y로밍 > 부가서비스 신청/변경/해지 전 사전체크 로직 추가
	 * - 전처리 수행
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void setRoamingPreCheck(String cntrNo, String vasItemCd, String vasItemId, String efctStDt, String efctFnsDt, String prodSbscTrmnCd, String prodHstSeq) throws Exception
	{
		// 상품가입구분 : A:신청, C:해지, U:변경
//		logger.info("============================================");
//		logger.info("cntrNo(서비스계약아이디):"+cntrNo);
//		logger.info("vasItemCd(상품코드):"+vasItemCd);
//		logger.info("vasItemId(상품ID):"+vasItemId);
//		logger.info("efctStDt(유효시작일):"+efctStDt);
//		logger.info("efctFnsDt(유효종료일):"+efctFnsDt);
//		logger.info("prodSbscTrmnCd(상품가입구분):"+prodSbscTrmnCd);
//		logger.info("prodHstSeq(상품이력일련번호):"+prodHstSeq);
//		logger.info("============================================");
		
		AplyRoaming paramObj = new AplyRoaming();
		paramObj.setCntrNo(cntrNo);
		paramObj.setProdId(vasItemCd);
		paramObj.setProdSbscTrmnCd(prodSbscTrmnCd);
		paramObj.setProdHstSeq(prodHstSeq);
		paramObj.setEfctStDt(efctStDt);
		paramObj.setEfctFnsDt(efctFnsDt);
		
		// cmnDao.insert("mybatis.mapper.yroaming.insertYRoaming", paramObj);
		kosService.setProdSbscTrmnStoreTrtPreCheck(cntrNo, vasItemCd, vasItemId, efctStDt, efctFnsDt, prodSbscTrmnCd, prodHstSeq);
	}
	
	/**
	 * 로밍을 신청한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int setRoaming(String cntrNo, String vasItemCd, String vasItemId, String efctStDt, String efctFnsDt, String prodSbscTrmnCd, String prodHstSeq) throws Exception
	{
		int rtnVal = 0;
		AplyRoaming paramObj = new AplyRoaming();
		paramObj.setCntrNo(cntrNo);
		paramObj.setProdId(vasItemCd);
		paramObj.setProdSbscTrmnCd(prodSbscTrmnCd);
		paramObj.setProdHstSeq(prodHstSeq);
		paramObj.setEfctStDt(efctStDt);
		paramObj.setEfctFnsDt(efctFnsDt);
		rtnVal += cmnDao.insert("mybatis.mapper.yroaming.insertYRoaming", paramObj);
		kosService.setProdSbscTrmnStoreTrt(cntrNo, vasItemCd, vasItemId, efctStDt, efctFnsDt, prodSbscTrmnCd, prodHstSeq);
		return rtnVal;
	}
}
