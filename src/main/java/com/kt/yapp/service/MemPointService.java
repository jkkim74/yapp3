package com.kt.yapp.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kt.yapp.domain.MemPointGet;
import com.kt.yapp.exception.YappException;
import com.kt.yapp.soap.SoapConnUtil;
import com.kt.yapp.soap.param.SoapParamMemPointAcu;
import com.kt.yapp.soap.param.SoapParamMemPointGet;
import com.kt.yapp.soap.param.SoapParamMemPointUse;
import com.kt.yapp.soap.param.SoapParamProdSbscTrmnStoreTrtPreCheck;
import com.kt.yapp.soap.response.SoapResponseMemPointAcu;
import com.kt.yapp.soap.response.SoapResponseMemPointGet;
import com.kt.yapp.soap.response.SoapResponseMemPointUse;
import com.kt.yapp.util.KeyFixUtilForRyt;
import com.kt.yapp.util.YappUtil;

/**
 * 멤버십 포인트 처리 서비스
 */
@Service
public class MemPointService 
{
	@Autowired
	private SoapConnUtil soapConnUtil;
	@Autowired
	private CommonService cmnService;
	@Autowired
	private HistService histService;
	@Autowired
	private KeyFixUtilForRyt keyFixUtilRyt;

	/** 호출 성공 반환 코드 */
	private static final String SUCCESS_RESP_CD = "00";
	private static final Logger logger = LoggerFactory.getLogger(MemPointService.class);

	/**
	 * 멤버십 포인트 조회
	 */
	public MemPointGet getMemPoint(String cntrNo) throws Exception
	{
		SoapParamMemPointGet param = new SoapParamMemPointGet(soapConnUtil);
		param.setCntrNo(keyFixUtilRyt.encode(cntrNo));
		param.setMemType(keyFixUtilRyt.encode("S"));

		SoapResponseMemPointGet resp = param.execute();
		if ( YappUtil.isNotEq(keyFixUtilRyt.decode(resp.getRtnCd()), SUCCESS_RESP_CD) ){
			String rtnCode = keyFixUtilRyt.decode(resp.getRtnCd());
			String rtnDesc = keyFixUtilRyt.decode(resp.getRtnDesc());
			throw new YappException("ROYALTY_MSG", rtnCode, cmnService.getSearchMsg("MEM_ERR_MSG", rtnDesc, rtnCode, "ERR_GET_MEM"), "[selectCustCombineInfoV2] "+rtnDesc, "");
		}
		return resp.getMemPointGet();
	}
	
	/**
	 * 멤버십 포인트 사용
	 */
	public SoapResponseMemPointUse useMemPoint(String cntrNo, String memId, String pointPdCd, int usePoint) throws Exception
	{
		
		/* pointPdCd (상품코드)
		DATARLB1G   데이터룰렛(1GB)
		DATARLB1M   데이터룰렛(100MB)
		DATARLB3M   데이터룰렛(300MB)
		DATARLB5M   데이터룰렛(500MB)
		DATARLBT1G  데이터룰렛(틴)(1GB)
		DATARLBT1M  데이터룰렛(틴)(100MB)
		DATARLBT3M  데이터룰렛(틴)(300MB)
		DATARLBT5M  데이터룰렛(틴)(500MB)*/
		
		SoapParamMemPointUse param = new SoapParamMemPointUse(soapConnUtil);
		param.setMemType(keyFixUtilRyt.encode("R"));
		param.setMemId(keyFixUtilRyt.encode(memId));
		param.setCntrNo(keyFixUtilRyt.encode(cntrNo));
		param.setPointPdCd(keyFixUtilRyt.encode(pointPdCd));
		param.setUsePoint(keyFixUtilRyt.encode(YappUtil.nToStr(usePoint)));
		Date now = new Date();
		String sDateStr = new SimpleDateFormat("yyMMddhhmms").format(now);
		String coorpCoOrdNo = cntrNo+sDateStr;
		if(coorpCoOrdNo.length()>20){
			coorpCoOrdNo = coorpCoOrdNo.substring(0, 20);
		}

		param.setOrdNo(keyFixUtilRyt.encode(coorpCoOrdNo));		// TODO 임시 세팅
		param.setCustRelnCd(keyFixUtilRyt.encode("1"));				// 1 (본인) 고정
		param.setUserNm(keyFixUtilRyt.encode("YAPP"));				// TODO 임시 세팅
		param.setMobileNo(keyFixUtilRyt.encode("01011112222"));		// TODO 임시 세팅
		param.setRegrId(keyFixUtilRyt.encode("91118817"));			// Y앱 시스템 사번
		param.setRegrNm(keyFixUtilRyt.encode("Y데이터박스"));			// 고정
		param.setRegOfficeId(keyFixUtilRyt.encode("SPT8050"));		// 고정

		SoapResponseMemPointUse resp = param.execute();
		if ( YappUtil.isNotEq(keyFixUtilRyt.decode(resp.getRtnCd()), SUCCESS_RESP_CD) ){
			String rtnCode = keyFixUtilRyt.decode(resp.getRtnCd());
			String rtnDesc = keyFixUtilRyt.decode(resp.getRtnDesc());
			throw new YappException("ROYALTY_MSG", rtnCode, cmnService.getSearchMsg("MEM_ERR_MSG", rtnDesc, rtnCode, "ERR_USE_MEM"),"[pointUsePrcSvc] "+ rtnDesc, coorpCoOrdNo);
		}
		resp.setCoorpCoOrdNo(coorpCoOrdNo);
		//이력 테이블에 저장한다.
		MemPointGet memPointGet =  new MemPointGet();
		memPointGet.setPointAmt(usePoint);
		memPointGet.setOrdNo(keyFixUtilRyt.decode(resp.getOrdNo()));
		memPointGet.setCoorpCoOrdNo(coorpCoOrdNo);
		memPointGet.setPointSndType("U");
		memPointGet.setCntrNo(cntrNo);
		memPointGet.setMemId(memId);
		histService.insertPointSndInfo(memPointGet);
		return resp;
	}
	
	/**220421
	 * Shop 멤버십 포인트 사용
	 */
	public SoapResponseMemPointUse useShopMemPoint(String cntrNo, String memId, String pointPdCd, int usePoint) throws Exception
	{
		
		/* pointPdCd (상품코드)
		개발계 임시 R2YBOX
		*/
		
		SoapParamMemPointUse param = new SoapParamMemPointUse(soapConnUtil);
		param.setMemType(keyFixUtilRyt.encode("R"));
		param.setMemId(keyFixUtilRyt.encode(memId));
//		param.setCntrNo(null);
		param.setPointPdCd(keyFixUtilRyt.encode(pointPdCd));
		param.setUsePoint(keyFixUtilRyt.encode(YappUtil.nToStr(usePoint)));
		Date now = new Date();
		String sDateStr = new SimpleDateFormat("yyMMddhhmms").format(now);
		String coorpCoOrdNo = cntrNo+sDateStr; // 실제 제휴사 현재는 임시로 기존처럼 계약번호로
		if(coorpCoOrdNo.length()>20){
			coorpCoOrdNo = coorpCoOrdNo.substring(0, 20);
		}

		param.setOrdNo(keyFixUtilRyt.encode(coorpCoOrdNo));		// TODO 임시 세팅
		param.setCustRelnCd(keyFixUtilRyt.encode("1"));				// 1 (본인) 고정
		param.setUserNm(keyFixUtilRyt.encode("YAPP"));				// TODO 임시 세팅
		param.setMobileNo(keyFixUtilRyt.encode("01011112222"));		// TODO 임시 세팅
		param.setRegrId("");			// Y앱 시스템 사번
		param.setRegrNm("");			// 고정
		param.setRegOfficeId("");		// 고정

		SoapResponseMemPointUse resp = param.execute();
		String rtnCode2 = keyFixUtilRyt.decode(resp.getRtnCd());
		String rtnDesc2 = keyFixUtilRyt.decode(resp.getRtnDesc());
		String getOrdNo = keyFixUtilRyt.decode(resp.getOrdNo());
		if(YappUtil.isNotEmpty(resp.getRmnPoint())){
			String getRmnPoint = keyFixUtilRyt.decode(resp.getRmnPoint());
			logger.info("getRmnPoint getRmnPoint : "+getRmnPoint);
		}else{
			logger.info("11SoapResponseMemPointUse getRmnPoint : "+resp.getRmnPoint());
		}
		logger.info("11SoapResponseMemPointUse rtnCode : "+rtnCode2);
		logger.info("11SoapResponseMemPointUse rtnDesc : "+rtnDesc2);
		logger.info("11SoapResponseMemPointUse getOrdNo : "+getOrdNo);
		
		if ( YappUtil.isNotEq(keyFixUtilRyt.decode(resp.getRtnCd()), SUCCESS_RESP_CD) ){
			String rtnCode = keyFixUtilRyt.decode(resp.getRtnCd());
			String rtnDesc = keyFixUtilRyt.decode(resp.getRtnDesc());
			logger.info("SoapResponseMemPointUse rtnCode : "+rtnCode);
			logger.info("SoapResponseMemPointUse rtnDesc : "+rtnDesc);

			throw new YappException("ROYALTY_MSG", rtnCode, cmnService.getSearchMsg("MEM_ERR_MSG", rtnDesc, rtnCode, "ERR_USE_MEM"),"[pointUsePrcSvc] "+ rtnDesc, coorpCoOrdNo);
		}
		resp.setCoorpCoOrdNo(coorpCoOrdNo);
		//이력 테이블에 저장한다.
		MemPointGet memPointGet =  new MemPointGet();
		memPointGet.setPointAmt(usePoint);
		memPointGet.setOrdNo(keyFixUtilRyt.decode(resp.getOrdNo()));
		memPointGet.setCoorpCoOrdNo(coorpCoOrdNo);
		memPointGet.setPointSndType("U");
		memPointGet.setCntrNo(cntrNo);
		memPointGet.setMemId(memId);
		histService.insertPointSndInfo(memPointGet);
		return resp;
	}
	
	/**
	 * 멤버십 포인트 적립
	 */
	public SoapResponseMemPointAcu acuMemPoint(String cntrNo, String memId, int acuPoint) throws Exception
	{
		SoapParamMemPointAcu param = new SoapParamMemPointAcu(soapConnUtil);
		param.setMemType(keyFixUtilRyt.encode("R"));
		param.setMemId(keyFixUtilRyt.encode(memId));
		param.setCntrNo(keyFixUtilRyt.encode(cntrNo));
		param.setPointPdCd(keyFixUtilRyt.encode("RAEXTDB001"));		// 고정
		param.setPointPdDesc(keyFixUtilRyt.encode("이벤트"));		// TODO 임시 세팅 48
		param.setAcuPoint(keyFixUtilRyt.encode(YappUtil.nToStr(acuPoint)));
		param.setPointCoorpCoCd(keyFixUtilRyt.encode("DATBX"));		// 고정
		//String coorpCoOrdNo = cntrNo+YappUtil.getUUIDStr().substring(0, 7);
		Date now = new Date();
		String sDateStr = new SimpleDateFormat("yyMMddhhmms").format(now);
		String coorpCoOrdNo = cntrNo+sDateStr;
		if(coorpCoOrdNo.length()>20){
			coorpCoOrdNo = coorpCoOrdNo.substring(0, 20);
		}

		param.setCoorpCoOrdNo(keyFixUtilRyt.encode(coorpCoOrdNo));		// TODO 임시 세팅

		SoapResponseMemPointAcu resp = param.execute();
		if ( YappUtil.isNotEq(keyFixUtilRyt.decode(resp.getRtnCd()), SUCCESS_RESP_CD) ){
			String rtnCode = keyFixUtilRyt.decode(resp.getRtnCd());
			String rtnDesc = keyFixUtilRyt.decode(resp.getRtnDesc());
			throw new YappException("ROYALTY_MSG", rtnCode, cmnService.getSearchMsg("MEM_ERR_MSG", rtnDesc, rtnCode, "ERR_ACU_MEM"), "[pointAcuPrcSvc] "+rtnDesc, coorpCoOrdNo);
		}
		resp.setCoorpCoOrdNo(coorpCoOrdNo);

		//이력 테이블에 저장한다.
		MemPointGet memPointGet =  new MemPointGet();
		memPointGet.setPointAmt(acuPoint);
		memPointGet.setOrdNo(keyFixUtilRyt.decode(resp.getOrdNo()));
		memPointGet.setCoorpCoOrdNo(coorpCoOrdNo);
		memPointGet.setPointSndType("A");
		memPointGet.setCntrNo(cntrNo);
		memPointGet.setMemId(memId);
		histService.insertPointSndInfo(memPointGet);
		return resp;
	}
	
}
