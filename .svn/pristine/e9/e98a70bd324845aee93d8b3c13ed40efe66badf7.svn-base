package com.kt.yapp.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kt.yapp.config.LimitationPropConfig;
import com.kt.yapp.domain.BigiChargBlnc;
import com.kt.yapp.domain.CallingPlan;
import com.kt.yapp.domain.DataDivDtl;
import com.kt.yapp.domain.DataInfo;
import com.kt.yapp.domain.DataRefill;
import com.kt.yapp.domain.DataRewardResult;
import com.kt.yapp.domain.DataShareList;
import com.kt.yapp.domain.Databox;
import com.kt.yapp.domain.DataboxDtl;
import com.kt.yapp.domain.DataboxPullInfo;
import com.kt.yapp.domain.DatukDtl;
import com.kt.yapp.domain.KosAccess;
import com.kt.yapp.domain.ParentsInfo;
import com.kt.yapp.domain.SvcHstByCustInfo;
import com.kt.yapp.domain.TimeOption;
import com.kt.yapp.em.EnumDboxUseTp;
import com.kt.yapp.em.EnumRsltCd;
import com.kt.yapp.exception.YappException;
import com.kt.yapp.soap.SoapConnUtil;
import com.kt.yapp.soap.param.SoapParamAplyProdInfoRetv;
import com.kt.yapp.soap.param.SoapParamCondByModelInfoRetv;
import com.kt.yapp.soap.param.SoapParamDataSharUseQntRetv;
import com.kt.yapp.soap.param.SoapParamEfctCdRetv;
import com.kt.yapp.soap.param.SoapParamGetDataInfoBySvcNo;
import com.kt.yapp.soap.param.SoapParamLegalAgntInfoAdm;
import com.kt.yapp.soap.param.SoapParamProcessProdStoreBas;
import com.kt.yapp.soap.param.SoapParamProdPrevChk;
import com.kt.yapp.soap.param.SoapParamProdSbscTrmnStoreTrt;
import com.kt.yapp.soap.param.SoapParamProdSbscTrmnStoreTrtPreCheck;
import com.kt.yapp.soap.param.SoapParamRetrieveBigiChargBlnc;
import com.kt.yapp.soap.param.SoapParamRetrieveDataRefill;
import com.kt.yapp.soap.param.SoapParamRetrieveDrctlyUseQnt;
import com.kt.yapp.soap.param.SoapParamRetrieveDrctlyUseQntDtl;
import com.kt.yapp.soap.param.SoapParamRetrieveDrctlyUseQntDtl2;
import com.kt.yapp.soap.param.SoapParamRetvDataGiftRstr;
import com.kt.yapp.soap.param.SoapParamRetvDataOneShotDtl;
import com.kt.yapp.soap.param.SoapParamRetvDataOutputRstr;
import com.kt.yapp.soap.param.SoapParamRetvDataStats;
import com.kt.yapp.soap.param.SoapParamRetvDataboxBas;
import com.kt.yapp.soap.param.SoapParamRetvDataboxIosHst;
import com.kt.yapp.soap.param.SoapParamRetvDataboxSbscRstr;
import com.kt.yapp.soap.param.SoapParamRetvDatukGiftRstr;
import com.kt.yapp.soap.param.SoapParamSaveApdDataPrvd;
import com.kt.yapp.soap.param.SoapParamSaveDataClect;
import com.kt.yapp.soap.param.SoapParamSaveDataGift;
import com.kt.yapp.soap.param.SoapParamSaveDataOneShot;
import com.kt.yapp.soap.param.SoapParamSaveDataOneShotRecp;
import com.kt.yapp.soap.param.SoapParamSaveDataOutput;
import com.kt.yapp.soap.param.SoapParamSaveDataboxSbsc;
import com.kt.yapp.soap.param.SoapParamSaveDataboxTrmn;
import com.kt.yapp.soap.param.SoapParamSvcHstByCustIdRetv;
import com.kt.yapp.soap.param.SoapParamYdbSendSms;
import com.kt.yapp.soap.response.SoapResponseAplyProdInfoRetv;
import com.kt.yapp.soap.response.SoapResponseCondByModelInfoRetv;
import com.kt.yapp.soap.response.SoapResponseDataSharUseQntRetv;
import com.kt.yapp.soap.response.SoapResponseEfctCdRetv;
import com.kt.yapp.soap.response.SoapResponseGetDataInfoBySvcNo;
import com.kt.yapp.soap.response.SoapResponseLegalAgntInfoAdm;
import com.kt.yapp.soap.response.SoapResponseProcessProdStoreBas;
import com.kt.yapp.soap.response.SoapResponseProdPrevChk;
import com.kt.yapp.soap.response.SoapResponseProdSbscTrmnStoreTrt;
import com.kt.yapp.soap.response.SoapResponseProdSbscTrmnStoreTrtPreCheck;
import com.kt.yapp.soap.response.SoapResponseRetrieveBigiChargBlnc;
import com.kt.yapp.soap.response.SoapResponseRetrieveDataRefill;
import com.kt.yapp.soap.response.SoapResponseRetrieveDrctlyUseQnt;
import com.kt.yapp.soap.response.SoapResponseRetrieveDrctlyUseQntDtl;
import com.kt.yapp.soap.response.SoapResponseRetrieveDrctlyUseQntDtl2;
import com.kt.yapp.soap.response.SoapResponseRetvDataGiftRstr;
import com.kt.yapp.soap.response.SoapResponseRetvDataOneShotDtl;
import com.kt.yapp.soap.response.SoapResponseRetvDataOutputRstr;
import com.kt.yapp.soap.response.SoapResponseRetvDataStats;
import com.kt.yapp.soap.response.SoapResponseRetvDataboxBas;
import com.kt.yapp.soap.response.SoapResponseRetvDataboxIosHst;
import com.kt.yapp.soap.response.SoapResponseRetvDataboxIosHst.DboxHist;
import com.kt.yapp.soap.response.SoapResponseRetvDataboxSbscRstr;
import com.kt.yapp.soap.response.SoapResponseRetvDatukGiftRstr;
import com.kt.yapp.soap.response.SoapResponseSaveApdDataPrvd;
import com.kt.yapp.soap.response.SoapResponseSaveDataClect;
import com.kt.yapp.soap.response.SoapResponseSaveDataGift;
import com.kt.yapp.soap.response.SoapResponseSaveDataOneShot;
import com.kt.yapp.soap.response.SoapResponseSaveDataOneShotRecp;
import com.kt.yapp.soap.response.SoapResponseSaveDataOutput;
import com.kt.yapp.soap.response.SoapResponseSaveDataboxSbsc;
import com.kt.yapp.soap.response.SoapResponseSaveDataboxTrmn;
import com.kt.yapp.soap.response.SoapResponseSvcHstByCustIdRetv;
import com.kt.yapp.soap.response.SoapResponseYdbSendSms;
import com.kt.yapp.util.YappUtil;

@Service
public class KosService
{
	@Autowired
	private SoapConnUtil soapConnUtil;
	@Autowired
	private CommonService cmnService;
	@Autowired
	private DboxService dboxService;
	@Autowired
	private CmsService cmsService;
	@Autowired
	private LimitationPropConfig limitConfig;
	
	private static final Logger logger = LoggerFactory.getLogger(KosService.class);
	
	private static final String SUCCESS_RESP_TP = "I";
	
	
	@Value("${profile.name}")
    private String profileName;
	
	/**
	 * 데이터 박스를 생성한다.
	 */
	public String createDbox(String cntrNo) throws Exception
	{
		// 가입 제한 조회
		SoapParamRetvDataboxSbscRstr param = new SoapParamRetvDataboxSbscRstr(soapConnUtil);
		param.setCntrNo(cntrNo);
		
		SoapResponseRetvDataboxSbscRstr resp = param.execute();
		
		if ( YappUtil.isY(resp.getSucesYn()) == false ){
			//throw new YappException(cmnService.getMsg("ERR_DBOX_CRT"));	// 데이터 박스 생성에 실패했습니다.
			String svc = "";
			if(resp.getSvcName() != null && !resp.getSvcName().equals("")){
				svc = "["+resp.getSvcName() +" / "+resp.getSrcId() + "] ";
			}

			throw new YappException("KOS_MSG", resp.getRtnCd(), cmnService.getSearchMsg("KOS_ERR_MSG", resp.getRtnDesc(), resp.getRtnCd(), "ERR_DBOX_CRT"), svc + resp.getRtnDesc(), resp.getGlobalNo());
		}
			

		// 데이터 박스 가입
		SoapParamSaveDataboxSbsc paramJoin = new SoapParamSaveDataboxSbsc(soapConnUtil);
		paramJoin.setCntrNo(cntrNo);

		SoapResponseSaveDataboxSbsc respJoin = paramJoin.execute();

		if ( YappUtil.isY(respJoin.getSucesYn()) == false ){
			//throw new YappException(cmnService.getMsg("ERR_DBOX_CRT"));	// 데이터 박스 생성에 실패했습니다.
			String svc = "";
			if(respJoin.getSvcName() != null && !respJoin.getSvcName().equals("")){
				svc = "["+respJoin.getSvcName() +" / "+respJoin.getSrcId() + "] ";
			}
			throw new YappException("KOS_MSG", respJoin.getRtnCd(), cmnService.getSearchMsg("KOS_ERR_MSG", respJoin.getRtnDesc(), respJoin.getRtnCd(), "ERR_DBOX_CRT"), svc + respJoin.getRtnDesc(), respJoin.getGlobalNo());
		}
			

		// 데이터 박스 기본 정보 조회
		SoapParamRetvDataboxBas paramBasicInfo = new SoapParamRetvDataboxBas(soapConnUtil);
		paramBasicInfo.setCntrNo(cntrNo);
		
		SoapResponseRetvDataboxBas respBasicInfo = paramBasicInfo.execute();
		
		if ( YappUtil.isEmpty(respBasicInfo.getDboxId()) ){
			//throw new YappException(cmnService.getMsg("ERR_DBOX_GET_INFO"));	// 데이터 박스 정보 조회에 실패했습니다.
			String svc = "";
			if(respBasicInfo.getSvcName() != null && !respBasicInfo.getSvcName().equals("")){
				svc = "["+respBasicInfo.getSvcName() +" / "+respBasicInfo.getSrcId() + "] ";
			}
			throw new YappException("KOS_MSG", respBasicInfo.getRtnCd(), cmnService.getSearchMsg("KOS_ERR_MSG", respBasicInfo.getRtnDesc(), respBasicInfo.getRtnCd(), "ERR_DBOX_GET_INFO"), svc + respBasicInfo.getRtnDesc(), respBasicInfo.getGlobalNo());
		}
			

		return respBasicInfo.getDboxId();
	}
	
	/**
	 * 데이터 박스를 생성한다.
	 */
	public String createDbox(String cntrNo, boolean isSleepUser) throws Exception
	{
		// 가입 제한 조회
		SoapParamRetvDataboxSbscRstr param = new SoapParamRetvDataboxSbscRstr(soapConnUtil);
		param.setCntrNo(cntrNo);
		
		SoapResponseRetvDataboxSbscRstr resp = param.execute();
		
		if ( YappUtil.isY(resp.getSucesYn()) == false ){
			//throw new YappException(cmnService.getMsg("ERR_DBOX_CRT"));	// 데이터 박스 생성에 실패했습니다.
			String svc = "";
			if(resp.getSvcName() != null && !resp.getSvcName().equals("")){
				svc = "["+resp.getSvcName() +" / "+resp.getSrcId() + "] ";
			}

			throw new YappException("KOS_MSG", resp.getRtnCd(), cmnService.getSearchMsg("KOS_ERR_MSG", resp.getRtnDesc(), resp.getRtnCd(), "ERR_DBOX_CRT"), svc + resp.getRtnDesc(), resp.getGlobalNo());
		}
			
		
		if(!isSleepUser) { 
			// 데이터 박스 가입
			SoapParamSaveDataboxSbsc paramJoin = new SoapParamSaveDataboxSbsc(soapConnUtil);
			paramJoin.setCntrNo(cntrNo);
	
			SoapResponseSaveDataboxSbsc respJoin = paramJoin.execute();
	
			if ( YappUtil.isY(respJoin.getSucesYn()) == false ){
				//throw new YappException(cmnService.getMsg("ERR_DBOX_CRT"));	// 데이터 박스 생성에 실패했습니다.
				String svc = "";
				if(respJoin.getSvcName() != null && !respJoin.getSvcName().equals("")){
					svc = "["+respJoin.getSvcName() +" / "+respJoin.getSrcId() + "] ";
				}
				throw new YappException("KOS_MSG", respJoin.getRtnCd(), cmnService.getSearchMsg("KOS_ERR_MSG", respJoin.getRtnDesc(), respJoin.getRtnCd(), "ERR_DBOX_CRT"), svc + respJoin.getRtnDesc(), respJoin.getGlobalNo());
			}
		}

		// 데이터 박스 기본 정보 조회
		SoapParamRetvDataboxBas paramBasicInfo = new SoapParamRetvDataboxBas(soapConnUtil);
		paramBasicInfo.setCntrNo(cntrNo);
		
		SoapResponseRetvDataboxBas respBasicInfo = paramBasicInfo.execute();
		
		if ( YappUtil.isEmpty(respBasicInfo.getDboxId()) ){
			//throw new YappException(cmnService.getMsg("ERR_DBOX_GET_INFO"));	// 데이터 박스 정보 조회에 실패했습니다.
			String svc = "";
			if(respBasicInfo.getSvcName() != null && !respBasicInfo.getSvcName().equals("")){
				svc = "["+respBasicInfo.getSvcName() +" / "+respBasicInfo.getSrcId() + "] ";
			}
			throw new YappException("KOS_MSG", respBasicInfo.getRtnCd(), cmnService.getSearchMsg("KOS_ERR_MSG", respBasicInfo.getRtnDesc(), respBasicInfo.getRtnCd(), "ERR_DBOX_GET_INFO"), svc + respBasicInfo.getRtnDesc(), respBasicInfo.getGlobalNo());
		}
			

		return respBasicInfo.getDboxId();
	}
	
	/**
	 * 데이터 박스를 해지한다.
	 */
	public void closeDbox(String cntrNo) throws Exception
	{
		SoapParamSaveDataboxTrmn param = new SoapParamSaveDataboxTrmn(soapConnUtil);
		param.setCntrNo(cntrNo);
		
		SoapResponseSaveDataboxTrmn resp = param.execute();

		if ( YappUtil.isY(resp.getSucesYn()) == false ){
			//throw new YappException(cmnService.getMsg("ERR_DBOX_CLS"));	// 데이터 박스 해지에 실패했습니다.
			String svc = "";
			if(resp.getSvcName() != null && !resp.getSvcName().equals("")){
				svc = "["+resp.getSvcName() +" / "+resp.getSrcId() + "] ";
			}
			throw new YappException("KOS_MSG", resp.getRtnCd(), cmnService.getSearchMsg("KOS_ERR_MSG", resp.getRtnDesc(), resp.getRtnCd(), "ERR_DBOX_CLS"), svc + resp.getRtnDesc(), resp.getGlobalNo());
		}
			
	}
	
	/**
	 * 데이터 박스 ID 를 조회한다.
	 */
	public String getDboxId(String cntrNo) throws Exception
	{
		// 데이터 박스 기본 정보 조회
		SoapParamRetvDataboxBas paramBasicInfo = new SoapParamRetvDataboxBas(soapConnUtil);
		paramBasicInfo.setCntrNo(cntrNo);
		
		SoapResponseRetvDataboxBas respBasicInfo = paramBasicInfo.execute();
		
		if ( YappUtil.isNotEmpty(respBasicInfo.getSucesYn()) && YappUtil.isY(respBasicInfo.getSucesYn()) == false){
			//throw new YappException(cmnService.getMsg("ERR_DBOX_GET_INFO"));	// 데이터 박스 정보 조회에 실패했습니다.
			String svc = "";
			if(respBasicInfo.getSvcName() != null && !respBasicInfo.getSvcName().equals("")){
				svc = "["+respBasicInfo.getSvcName() +" / "+respBasicInfo.getSrcId() + "] ";
			}
			throw new YappException("KOS_MSG", respBasicInfo.getRtnCd(), cmnService.getSearchMsg("KOS_ERR_MSG", respBasicInfo.getRtnDesc(), respBasicInfo.getRtnCd(), "ERR_DBOX_GET_INFO"), svc + respBasicInfo.getRtnDesc(), respBasicInfo.getGlobalNo());
		}
		
		return respBasicInfo.getDboxId();
	}

	/**
	 * 잔여 데이터 호출
	 */
	public int getDataInfoBySvcNo(String mobileNo) throws Exception
	{
		// 잔여 데이터 호출
		SoapParamGetDataInfoBySvcNo paramInfo = new SoapParamGetDataInfoBySvcNo(soapConnUtil);
		paramInfo.setMobileNo(mobileNo);
		
		SoapResponseGetDataInfoBySvcNo respInfo = paramInfo.execute();
		
		if ( YappUtil.isEmpty(respInfo.getRmnDataAmt()) ){
			//throw new YappException(cmnService.getMsg("ERR_DBOX_GET_INFO"));	// 데이터 박스 정보 조회에 실패했습니다.
			String svc = "";
			if(respInfo.getSvcName() != null && !respInfo.getSvcName().equals("")){
				svc = "["+respInfo.getSvcName() +" / "+respInfo.getSrcId() + "] ";
			}
			throw new YappException("KOS_MSG", respInfo.getRtnCd(), cmnService.getSearchMsg("KOS_ERR_MSG", respInfo.getRtnDesc(), respInfo.getRtnCd(), "ERR_NO_DATA"), svc + respInfo.getRtnDesc(), respInfo.getGlobalNo());
		}
		
		return respInfo.getRmnDataAmt();
	}

	/**
	 * 데이터 박스 정보를 조회한다.
	 */
	public Databox getDboxInfo(String cntrNo) throws Exception
	{
		SoapParamRetvDataStats param = new SoapParamRetvDataStats(soapConnUtil);
		param.setCntrNo(cntrNo);
		
		SoapResponseRetvDataStats resp = param.execute();
		if ( YappUtil.isNotEmpty(resp.getSucesYn()) && YappUtil.isY(resp.getSucesYn()) == false){
			//throw new YappException(cmnService.getMsg("ERR_DBOX_GET_INFO"));	// 데이터 박스 정보 조회에 실패했습니다.
			String svc = "";
			if(resp.getSvcName() != null && !resp.getSvcName().equals("")){
				svc = "["+resp.getSvcName() +" / "+resp.getSrcId() + "] ";
			}
			throw new YappException("KOS_MSG", resp.getRtnCd(), cmnService.getSearchMsg("KOS_ERR_MSG", resp.getRtnDesc(), resp.getRtnCd(), "ERR_DBOX_GET_INFO"), svc+resp.getRtnDesc(), resp.getGlobalNo());
		}
		
		Databox dbox = new Databox();
		dbox.setDboxDataAmtCurMnth(resp.getDsprCurMnthDataAmt());
		dbox.setDboxDataAmtNextMnth(resp.getDsprNextMnthDataAmt());
		dbox.setDboxDataAmt(resp.getBoxDataAmt());
		dbox.setDboxDataSize(limitConfig.getLmtDboxDataSize());
		dbox.setGiftCnt(resp.getGiftCnt());
		dbox.setGiftDataAmt(resp.getGiftDataAmt());
		
		// 데이터박스 꺼내기 정보 세팅
		DataboxPullInfo dboxPullInfo = dboxService.getDboxPullInfo(cntrNo);
		dbox.setDboxPullInfo(dboxPullInfo);
		
		return dbox;
	}
	
	/**
	 * 데이터 나눔 상세 목록을 조회한다.
	 */
	public List<DataDivDtl> getDataDivDtlList(String cntrNo, String validYm) throws Exception
	{
		Calendar curCal = Calendar.getInstance();
		//cal.add(Calendar.MONTH , -3);
		curCal.setTime(YappUtil.getDate("yyyyMM", validYm));
		
		int maxDate = curCal.getActualMaximum(Calendar.DAY_OF_MONTH);

		SoapResponseRetvDataboxIosHst resp = getDboxHist(cntrNo, validYm + "01", validYm + maxDate, "P");
		
		List<DataDivDtl> dtlList = new ArrayList<>();
		
		if ( resp == null || YappUtil.isEmpty(resp.getDboxHistList()) ){
			return dtlList;
		}
		// 데이턱 정보 임시 저장 맵
		Map<String, DataDivDtl> datukImsiMap = new HashMap<>();

		for ( DboxHist dboxHist : resp.getDboxHistList() )
		{
			DataDivDtl dtl = new DataDivDtl();
			
			if (  YappUtil.contains(dboxHist.getTrtDivCd(), "DT", "CD") ){
				dtl.setDivContents(dboxHist.getTrtDivNm());
			}else{
				dtl.setDivContents(YappUtil.blindNameToName(dboxHist.getCustNm(), 1));
			}
			dtl.setDivContentsTp(dboxHist.getTrtDivCd());
			dtl.setMobileNo(dboxHist.getSvcNo());
			dtl.setUseDataAmt(dboxHist.getTrtQnt());
			dtl.setUseDate(YappUtil.getDate("yyyyMMddHHmmss", dboxHist.getTrtDate()));
			dtl.setEfctStdt(dboxHist.getEfctStDt());
			dtl.setDatukId(dboxHist.getDatukId());
			// 데이턱 정보 추가
			if (  YappUtil.isEq(dboxHist.getTrtDivCd(), "DT") )
			{
				DataDivDtl dtlImsi = datukImsiMap.get(dboxHist.getTrtDate());
				
				// 임시 저장 맵에 값이 없으면 추가 있으면 데이턱 회수정보 세팅
				if ( YappUtil.isEmpty(dtlImsi) )
				{
					
					if(dboxHist.getTrtDate() != null){
						try {
							String srchYmd = YappUtil.getCurDate(YappUtil.getDate("yyyyMMddHHmmss", dboxHist.getTrtDate()), "yyyyMMdd");
							String todayYmd = YappUtil.getCurDate("yyyyMMdd");
							int aa = (int)YappUtil.getDayDistance(srchYmd,todayYmd);
							if(Integer.parseInt(srchYmd) <= Integer.parseInt(todayYmd)){
								if(aa > 1){
									dtl.setRtnDatukStatus("G0004");
								}else{
									dtl.setRtnDatukStatus("G0001");
								}
							}
						}catch (RuntimeException e) {
							logger.info("==================  상세조회 날짜 계산 오류 -====================");
							e.printStackTrace();
						}catch (Exception e) {
							logger.info("==================  상세조회 날짜 계산 오류 -====================");
							//e.printStackTrace();
						}
					}
					datukImsiMap.put(dboxHist.getTrtDate(), dtl);
				} else {
					if ( YappUtil.isEq(dtlImsi.getDivContentsTp(), "CD") ) {
						// 회수 대상 있음
						dtl.setRtnDatukDataAmt(datukImsiMap.get(dboxHist.getTrtDate()).getUseDataAmt());
						dtl.setRtnDatukStatus("G0002");
					} else if ( YappUtil.isEq(dtlImsi.getDivContentsTp(), "YD") ) {
						// 회수 완료
						dtl.setRtnDatukDataAmt(datukImsiMap.get(dboxHist.getTrtDate()).getUseDataAmt());
						dtl.setRtnDatukStatus("G0003");
					} else if ( YappUtil.isEq(dtlImsi.getDivContentsTp(), "DT") ) {
						if ( dtlImsi.getUseDataAmt() == 0 || dboxHist.getTrtQnt() == 0 ) {
							// 종료
							dtlImsi.setRtnDatukStatus("G0004");
							if ( dtlImsi.getUseDataAmt() == 0 ){
								dtlImsi.setUseDataAmt(dboxHist.getTrtQnt());
							}
						} else {
							// 진행중
							dtlImsi.setRtnDatukStatus("G0001");
							if ( dtlImsi.getUseDataAmt() < dboxHist.getTrtQnt() ){
								dtlImsi.setUseDataAmt(dboxHist.getTrtQnt());
							}
						}
						continue;
					}
				}
			}
			
			// 데이턱 회수대상 데이터
			if (  YappUtil.isEq(dboxHist.getTrtDivCd(), "CD") )
			{
				DataDivDtl dtlImsi = datukImsiMap.get(dboxHist.getTrtDate());

				// 임시 저장 맵에 값이 없으면 추가 있으면 데이턱 회수정보 세팅
				if ( YappUtil.isEmpty(dtlImsi) )
				{
					datukImsiMap.put(dboxHist.getTrtDate(), dtl);
				} else {
					dtlImsi.setRtnDatukDataAmt(dboxHist.getTrtQnt());
					dtlImsi.setRtnDatukStatus("G0002");
				}
				continue;
			}
			// 데이턱 회수완료 데이터
			else if (  YappUtil.isEq(dboxHist.getTrtDivCd(), "YD") )
			{
				DataDivDtl dtlImsi = datukImsiMap.get(dboxHist.getTrtDate());
				
				// 임시 저장 맵에 값이 없으면 추가 있으면 데이턱 회수완료정보 세팅
				if ( YappUtil.isEmpty(dtlImsi) )
				{
					datukImsiMap.put(dboxHist.getTrtDate(), dtl);
				} else {
					dtlImsi.setRtnDatukDataAmt(dboxHist.getTrtQnt());
					dtlImsi.setRtnDatukStatus("G0003");
				}
				continue;
			}
			
			dtlList.add(dtl);
		}

		Calendar curCal2 = Calendar.getInstance();
		curCal2.setTime(YappUtil.getDate("yyyyMM", validYm));
		curCal2.add(Calendar.MONTH , 1);
		int tYear = curCal2.get(Calendar.YEAR);
		int tMonth = curCal2.get(Calendar.MONTH)+1;
		String tmpMon = "0";
		if(tMonth < 10){
			tmpMon = "0"+String.valueOf(tMonth);
		}else{
			tmpMon = String.valueOf(tMonth);
		}

		SoapResponseRetvDataboxIosHst resp2 = getDboxHist(cntrNo, tYear+tmpMon+ "01", tYear+tmpMon+ "02", "P");
		if ( resp2 == null || YappUtil.isEmpty(resp2.getDboxHistList()) ){
			return dtlList;
		}

		for (DboxHist dboxHist2 : resp2.getDboxHistList() ){
			if (YappUtil.contains(dboxHist2.getTrtDivCd(), "CD", "YD") ){
				for(DataDivDtl dataDivDtl2 : dtlList){
					if(dataDivDtl2.getUseDate().equals(YappUtil.getDate("yyyyMMddHHmmss", dboxHist2.getTrtDate()))){
						//회수가능데이터
						if(YappUtil.isEq(dboxHist2.getTrtDivCd(), "CD")){
							dataDivDtl2.setRtnDatukDataAmt(dboxHist2.getTrtQnt());
							dataDivDtl2.setRtnDatukStatus("G0002");
						}
						//회수완료데이터
						if(YappUtil.isEq(dboxHist2.getTrtDivCd(), "YD")){
							dataDivDtl2.setRtnDatukDataAmt(dboxHist2.getTrtQnt());
							dataDivDtl2.setRtnDatukStatus("G0003");
						}
					}
				}
			}
		}
		return dtlList;
	}
	
	/**
	 * 데이터박스 상세 목록을 조회한다.
	 */
	public List<DataboxDtl> getDboxDtlList(String cntrNo, String validYm) throws Exception
	{
		Calendar curCal = Calendar.getInstance();
		curCal.setTime(YappUtil.getDate("yyyyMM", validYm));

		int maxDate = curCal.getActualMaximum(Calendar.DAY_OF_MONTH);

		SoapResponseRetvDataboxIosHst resp = getDboxHist(cntrNo, validYm + "01", validYm + maxDate, "R");

		List<DataboxDtl> dtlList = new ArrayList<>();

		if ( resp == null || YappUtil.isEmpty(resp.getDboxHistList()) ){
			return dtlList;
		}
		// 꺼내기 데이터양 (소멸 계산 시 사용)
		int pullDataAmt = 0;

		for ( DboxHist dboxHist : resp.getDboxHistList() )
		{
			// 꺼내기 데이터양 저장
			if ( YappUtil.isNotEq(dboxHist.getTrtDivCd(), "DO") )
				pullDataAmt += dboxHist.getTrtQnt();
			
			DataboxDtl dtl = new DataboxDtl();
			if ( YappUtil.contains(dboxHist.getTrtDivCd(), "RD") ){
				dtl.setDboxContents(YappUtil.blindNameToName(dboxHist.getCustNm(), 1));
			}else{
				if(YappUtil.contains(dboxHist.getTrtDivNm(), "데이턱 데이터") && YappUtil.contains(dboxHist.getCustNm(),"본인")){
					dtl.setDboxContents("회수완료 데이터");
				}else{
					dtl.setDboxContents(dboxHist.getTrtDivNm());
				}
			}
			dtl.setDboxContentsTp(dboxHist.getTrtDivCd());
			dtl.setUseDataAmt(dboxHist.getTrtQnt());
			dtl.setRmnDataAmt(dboxHist.getRmndQnt());
			dtl.setUseDate(YappUtil.getDate("yyyyMMddHHmmss", dboxHist.getTrtDate()));
			if ( YappUtil.contains(dboxHist.getTrtDivCd(), "DO", "DE") ){
				dtl.setUseTp(EnumDboxUseTp.G0002.getDataTp());
			}else{
				dtl.setUseTp(EnumDboxUseTp.G0001.getDataTp());
			}
			dtlList.add(dtl);
		}

		// 조회월이 현재월이 아니면 소멸 데이터 추가
/*		if ( YappUtil.isNotEq(YappUtil.getCurDate("yyyyMM"), validYm) )
		{
			DataboxDtl dsprDboxData = getDsprDboxData(cntrNo, validYm, pullDataAmt);
			if ( dsprDboxData != null ){
				dtlList.add(dsprDboxData);
			}
		}*/

		return dtlList;
	}
	
	/**
	 * 소멸 데이터 정보를 조회한다.
	 */
	private DataboxDtl getDsprDboxData(String cntrNo, String validYm, int pullDataAmt) throws Exception
	{
		DataboxDtl dtl = new DataboxDtl();
		
		// 1개월 이전 데이터 조회
		Calendar srchCal = Calendar.getInstance();
		srchCal.setTime(YappUtil.getDate("yyyyMM", validYm));
		srchCal.add(Calendar.MONTH, -1);
		String srchYm = YappUtil.getCurDate(srchCal.getTime(), "yyyyMM");
		
		int maxDate = srchCal.getActualMaximum(Calendar.DAY_OF_MONTH);
		SoapResponseRetvDataboxIosHst resp = getDboxHist(cntrNo, srchYm + "01", srchYm + maxDate, "R");

		if ( YappUtil.isEmpty(resp.getDboxHistList()) ){
			return null;
		}
		// 월 중 최초 잔여량
		int rmnDataAmt = 0;
		// 월 중 최후 잔여량
		int rmnDataAmtLast = 0;
		// 잔여량 설정여부
		boolean rmnDataSetting = false;
		// 소멸예정 데이터양
		int dsprExpectedDataAmt = 0;

		// 과거 데이터 먼저 조회한다.
		for ( int i = resp.getDboxHistList().size() - 1; i >= 0; i-- )
		{
			DboxHist dboxHist = resp.getDboxHistList().get(i);

			// 꺼내기가 아니면 월 중 최초 잔여량 값을 세팅한다.
			if ( rmnDataSetting == false )
			{
				if ( YappUtil.isEq(dboxHist.getTrtDivCd(), "DO") ){
					rmnDataAmt = dboxHist.getRmndQnt() + dboxHist.getTrtQnt();
				}else{
					rmnDataAmt = dboxHist.getRmndQnt() - dboxHist.getTrtQnt();
				}
				rmnDataSetting = true;
				logger.info("최초 잔여량 : " + rmnDataAmt);
			}

			// 월 중 최후 잔여량
			if ( i == 0 ){
				rmnDataAmtLast = dboxHist.getRmndQnt();
			}

			// 꺼내기면 최초 잔여량에서 빼고, 그 외는 소멸예정 데이터에 더한다.
			if ( YappUtil.isEq(dboxHist.getTrtDivCd(), "DO") ){
				rmnDataAmt -= dboxHist.getTrtQnt();
			}else{
				dsprExpectedDataAmt += dboxHist.getTrtQnt();
			}
		}
		logger.info("소멸 예정 데이터양 : " + dsprExpectedDataAmt);

		// 전월에 최초 잔여량보다 꺼내쓴 데이터가 많은 경우 
		// 소멸예정 데이터 = 적립 데이터 + (최초잔여량 - 꺼내쓴데이터) - 당월 꺼내쓴 데이터
		if ( rmnDataAmt < 0 ){
			dsprExpectedDataAmt = dsprExpectedDataAmt + rmnDataAmt - pullDataAmt;
		// 전월에 최초 잔여량보다 꺼내쓴 데이터가 적은 경우 
		// 소멸예정 데이터 = 적립 데이터 - 당월 꺼내쓴 데이터
		}else{
			dsprExpectedDataAmt = dsprExpectedDataAmt - pullDataAmt;
		}
		// 소멸 예정 데이터가 0 이하면 소멸 없음
		if ( dsprExpectedDataAmt <= 0 ){
			return null;
		}
		dtl.setDboxContents("소멸");
		dtl.setDboxContentsTp("DP");
		dtl.setUseDataAmt(dsprExpectedDataAmt);
		dtl.setRmnDataAmt(rmnDataAmtLast - dsprExpectedDataAmt);
		dtl.setUseDate(YappUtil.getDate("yyyyMMdd", srchYm + maxDate));
		dtl.setUseTp(EnumDboxUseTp.G0002.getDataTp());

		logger.info("소멸예정 정보 : " + dtl);

		return dtl;
	}
	
	/**
	 * 데이터 박스 입출력 이력 정보를 조회한다.
	 */
	public SoapResponseRetvDataboxIosHst getDboxHist(String cntrNo, String validStYmd, String validEdYmd, String dataTp) throws Exception
	{
		SoapParamRetvDataboxIosHst param = new SoapParamRetvDataboxIosHst(soapConnUtil);
		param.setCntrNo(cntrNo);
		param.setValidStYmd(validStYmd);
		param.setValidEdYmd(validEdYmd);
		param.setDataTp(dataTp);

		SoapResponseRetvDataboxIosHst resp = param.execute();
		if ( YappUtil.isNotEmpty(resp.getSucesYn()) && YappUtil.isY(resp.getSucesYn()) == false){
			String svc = "";
			if(resp.getSvcName() != null && !resp.getSvcName().equals("")){
				svc = "["+resp.getSvcName() +" / "+resp.getSrcId() + "] ";
			}
			throw new YappException("KOS_MSG", resp.getRtnCd(), cmnService.getSearchMsg("KOS_ERR_MSG", resp.getRtnDesc(), resp.getRtnCd(), "ERR_DBOX_GET_INFO"), svc+resp.getRtnDesc(), resp.getGlobalNo());
		}

		return resp;
	}
	
	/**
	 * 데이터를 선물한다.
	 */
	public void giftData(String mobileNo, String rcvMobileNo, int dataAmt) throws Exception
	{
		// 선물제약 조회
		SoapParamRetvDataGiftRstr param = new SoapParamRetvDataGiftRstr(soapConnUtil);
		param.setMobileNo(mobileNo);
		param.setRcvMobileNo(rcvMobileNo);
		param.setDataAmt(dataAmt);

		SoapResponseRetvDataGiftRstr resp = param.execute();

		if ( YappUtil.isY(resp.getSucesYn()) == false ){
			//throw new YappException(cmnService.getMsg("ERR_DBOX_GIFT"));	// 데이터 선물하기에 실패했습니다.
			String svc = "";
			if(resp.getSvcName() != null && !resp.getSvcName().equals("")){
				svc = "["+resp.getSvcName() +" / "+resp.getSrcId() + "] ";
			}
			throw new YappException("KOS_MSG", resp.getRtnCd(), cmnService.getSearchMsg("KOS_ERR_MSG", resp.getRtnDesc(), resp.getRtnCd(), "ERR_DBOX_GIFT"), svc + resp.getRtnDesc(), resp.getGlobalNo());
		}else{
			KosAccess paramObj = new KosAccess();
			//paramObj.setCntrNo(mobileNo);
			paramObj.setGlobalNo(resp.getGlobalNo());
			paramObj.setSvcName(resp.getSvcName() + "/" + "retvDataGiftRstr");
			paramObj.setResponseTitle(resp.getRtnDesc());
			paramObj.setResponseBasc(resp.getErrDesc());
			paramObj.setResponseType(resp.getRespTp());
			paramObj.setResponseCode(resp.getRtnCd());
			paramObj.setEtc(mobileNo);
			paramObj.setApiUrl(soapConnUtil.soapEpUrlKosDynamicSvc);
			cmsService.insertKosApiAccess(paramObj);
		}
		// 선물하기
		SoapParamSaveDataGift paramGift = new SoapParamSaveDataGift(soapConnUtil);
		paramGift.setMobileNo(mobileNo);
		paramGift.setRcvMobileNo(rcvMobileNo);
		paramGift.setDataAmt(dataAmt);

		SoapResponseSaveDataGift respGift = paramGift.execute();

		if ( YappUtil.isY(respGift.getSucesYn()) == false ){
			//throw new YappException(cmnService.getMsg("ERR_DBOX_GIFT"));	// 데이터 선물하기에 실패했습니다.
			String svc = "";
			if(respGift.getSvcName() != null && !respGift.getSvcName().equals("")){
				svc = "["+respGift.getSvcName() +" / "+respGift.getSrcId() + "]";
			}
			throw new YappException("KOS_MSG", respGift.getRtnCd(), cmnService.getSearchMsg("KOS_ERR_MSG", respGift.getRtnDesc(), respGift.getRtnCd(), "ERR_DBOX_GIFT"), svc + respGift.getRtnDesc(), respGift.getGlobalNo());
		}else{
			KosAccess paramObj = new KosAccess();
			//paramObj.setCntrNo(mobileNo);
			paramObj.setGlobalNo(respGift.getGlobalNo());
			paramObj.setSvcName(respGift.getSvcName() + "/" + "saveDataGift");
			paramObj.setResponseTitle(respGift.getRtnDesc());
			paramObj.setResponseBasc(respGift.getErrDesc());
			paramObj.setResponseType(respGift.getRespTp());
			paramObj.setResponseCode(respGift.getRtnCd());
			paramObj.setEtc(mobileNo);
			paramObj.setApiUrl(soapConnUtil.soapEpUrlKosDynamicSvc);
			cmsService.insertKosApiAccess(paramObj);
		}
		
	}
	
	/**
	 * 데이터를 꺼낸다.
	 */
	public void pullData(String cntrNo, String mobileNo, int dataAmt) throws Exception
	{
		// 데이터꺼내기 제약 조회
		SoapParamRetvDataOutputRstr param = new SoapParamRetvDataOutputRstr(soapConnUtil);
		param.setCntrNo(cntrNo);
		param.setDataAmt(dataAmt);

		SoapResponseRetvDataOutputRstr resp = param.execute();

		if ( YappUtil.isY(resp.getSucesYn()) == false ){
			//throw new YappException(cmnService.getMsg("ERR_DBOX_PULL"));	// 데이터 꺼내기에 실패했습니다.
			String svc = "";
			if(resp.getSvcName() != null && !resp.getSvcName().equals("")){
				svc = "["+resp.getSvcName() +" / "+resp.getSrcId() + "] ";
			}
			throw new YappException("KOS_MSG", resp.getRtnCd(), cmnService.getSearchMsg("KOS_ERR_MSG", resp.getRtnDesc(), resp.getRtnCd(), "ERR_DBOX_PULL"), svc + resp.getRtnDesc(), resp.getGlobalNo());
		}else{
			KosAccess paramObj = new KosAccess();
			paramObj.setCntrNo(cntrNo);
			paramObj.setGlobalNo(resp.getGlobalNo());
			paramObj.setSvcName(resp.getSvcName() + "/" + "retvDataOutputRstr");
			paramObj.setResponseTitle(resp.getRtnDesc());
			paramObj.setResponseBasc(resp.getErrDesc());
			paramObj.setResponseType(resp.getRespTp());
			paramObj.setResponseCode(resp.getRtnCd());
			paramObj.setApiUrl(soapConnUtil.soapEpUrlKosDynamicSvc);
			cmsService.insertKosApiAccess(paramObj);
		}

		// 데이터 꺼내기
		SoapParamSaveDataOutput paramPullData = new SoapParamSaveDataOutput(soapConnUtil);
		paramPullData.setMobileNo(mobileNo);
		paramPullData.setDataAmt(dataAmt);
		
		SoapResponseSaveDataOutput respPullData = paramPullData.execute();
		
		if ( YappUtil.isNotEq(respPullData.getRtnCd(), "s") ){
			String svc = "";
			if(respPullData.getSvcName() != null && !respPullData.getSvcName().equals("")){
				svc = "["+respPullData.getSvcName() +" / "+respPullData.getSrcId() + "] ";
			}
			throw new YappException("KOS_MSG", respPullData.getErrCd(), cmnService.getMsg("ERR_DBOX_PULL"), svc + respPullData.getRtnDesc(), respPullData.getGlobalNo());	// 데이터 꺼내기에 실패했습니다.
		}else{
			KosAccess paramObj = new KosAccess();
			paramObj.setCntrNo(cntrNo);
			paramObj.setGlobalNo(respPullData.getGlobalNo());
			paramObj.setSvcName(respPullData.getSvcName() + "/" + "saveDataOutput");
			paramObj.setResponseTitle(respPullData.getRtnDesc());
			paramObj.setResponseBasc(respPullData.getErrDesc());
			paramObj.setResponseType(respPullData.getRespTp());
			paramObj.setResponseCode(respPullData.getRtnCd());
			paramObj.setApiUrl(soapConnUtil.soapEpUrlKosSvc);
			cmsService.insertKosApiAccess(paramObj);
		}

	}
	
	/**
	 * 데이터를 꺼낸다.(잔여)
	 */
	public void pullRemainData(String cntrNo, String mobileNo, int dataAmt) throws Exception
	{
		// 데이터꺼내기 제약 조회
		SoapParamRetvDataOutputRstr param = new SoapParamRetvDataOutputRstr(soapConnUtil);
		param.setCntrNo(cntrNo);
		param.setDataAmt(dataAmt);

		SoapResponseRetvDataOutputRstr resp = param.execute();

		if ( YappUtil.isY(resp.getSucesYn()) == false ){
			//throw new YappException(cmnService.getMsg("ERR_DBOX_PULL"));	// 데이터 꺼내기에 실패했습니다.
			String svc = "";
			if(resp.getSvcName() != null && !resp.getSvcName().equals("")){
				svc = "["+resp.getSvcName() +" / "+resp.getSrcId() + "] ";
			}
			throw new YappException("KOS_MSG", resp.getRtnCd(), cmnService.getSearchMsg("KOS_ERR_MSG", resp.getRtnDesc(), resp.getRtnCd(), "ERR_DBOX_PART_PULL"), svc + resp.getRtnDesc(), resp.getGlobalNo());
		}else{
			KosAccess paramObj = new KosAccess();
			paramObj.setCntrNo(cntrNo);
			paramObj.setGlobalNo(resp.getGlobalNo());
			paramObj.setSvcName(resp.getSvcName() + "/" + "retvDataOutputRstr");
			paramObj.setResponseTitle(resp.getRtnDesc());
			paramObj.setResponseBasc(resp.getErrDesc());
			paramObj.setResponseType(resp.getRespTp());
			paramObj.setResponseCode(resp.getRtnCd());
			paramObj.setApiUrl(soapConnUtil.soapEpUrlKosDynamicSvc);
			cmsService.insertKosApiAccess(paramObj);
		}

		// 데이터 꺼내기
		SoapParamSaveDataOutput paramPullData = new SoapParamSaveDataOutput(soapConnUtil);
		paramPullData.setMobileNo(mobileNo);
		paramPullData.setDataAmt(dataAmt);
		
		SoapResponseSaveDataOutput respPullData = paramPullData.execute();
		
		if ( YappUtil.isNotEq(respPullData.getRtnCd(), "s") ){
			String svc = "";
			if(respPullData.getSvcName() != null && !respPullData.getSvcName().equals("")){
				svc = "["+respPullData.getSvcName() +" / "+respPullData.getSrcId() + "] ";
			}
			throw new YappException("KOS_MSG", respPullData.getErrCd(), cmnService.getMsg("ERR_DBOX_PART_PULL"), svc + respPullData.getRtnDesc(), respPullData.getGlobalNo());	// 데이터 꺼내기에 일부 실패했습니다.
		}else{
			KosAccess paramObj = new KosAccess();
			paramObj.setCntrNo(cntrNo);
			paramObj.setGlobalNo(respPullData.getGlobalNo());
			paramObj.setSvcName(respPullData.getSvcName() + "/" + "saveDataOutput");
			paramObj.setResponseTitle(respPullData.getRtnDesc());
			paramObj.setResponseBasc(respPullData.getErrDesc());
			paramObj.setResponseType(respPullData.getRespTp());
			paramObj.setResponseCode(respPullData.getRtnCd());
			paramObj.setApiUrl(soapConnUtil.soapEpUrlKosSvc);
			cmsService.insertKosApiAccess(paramObj);
		}

	}
	
	/**
	 * 보너스(프로모션) 데이터를 수령한다.
	 */
	public void receiveBonusData(String mobileNo, int dataAmt, String dataTp) throws Exception
	{
		SoapParamSaveApdDataPrvd param = new SoapParamSaveApdDataPrvd(soapConnUtil);
		param.setMobileNo(mobileNo);
		param.setDataAmt(dataAmt);
		param.setDataTp(dataTp);

		SoapResponseSaveApdDataPrvd resp = param.execute();

		if ( YappUtil.isY(resp.getSucesYn()) == false ){
			//throw new YappException(cmnService.getMsg("ERR_DBOX_BONUS"));	// 보너스(프로모션) 데이터 수령에 실패했습니다.
			String svc = "";
			if(resp.getSvcName() != null && !resp.getSvcName().equals("")){
				svc = "["+resp.getSvcName() +" / "+resp.getSrcId() + "] ";
			}
			throw new YappException("KOS_MSG", resp.getRtnCd(), cmnService.getSearchMsg("KOS_ERR_MSG", resp.getRtnDesc(), resp.getRtnCd(), "ERR_DBOX_BONUS"), svc + resp.getRtnDesc(), resp.getGlobalNo());
		}

	}
	
	/** 211213
	 * 응모권/데이터쿠폰 보너스(프로모션) 데이터를 수령한다.
	 */
	public DataRewardResult receiveTicketBonusData(String cntrNo, String mobileNo, int dataAmt, String dataTp, String expireTerm) throws Exception
	{
		DataRewardResult result = new DataRewardResult();
		logger.info("receiveTicketBonusData ========= ");

		SoapParamSaveApdDataPrvd param = new SoapParamSaveApdDataPrvd(soapConnUtil);
		param.setMobileNo(mobileNo);
//		param.setMobileNo("00000000000"); //kos에러만들기 코드
		param.setDataAmt(dataAmt);
		param.setDataTp(dataTp);
		param.setExpireTerm(expireTerm);
		logger.info("param ========= "+param);

		SoapResponseSaveApdDataPrvd resp = param.execute();
		logger.info("resp ========= "+resp);

		if ( YappUtil.isY(resp.getSucesYn()) == false ){
			String svc = "";
			if(resp.getSvcName() != null && !resp.getSvcName().equals("")){
				svc = "["+resp.getSvcName() +" / "+resp.getSrcId() + "] ";
			}
			result.setKosResult(false);
			result.setGlobalNo(resp.getGlobalNo());
			result.setRtnCd(resp.getRtnCd());
			result.setRtnDesc(resp.getRtnDesc());
			result.setSvc(svc);
			
		}else{
			KosAccess paramObj = new KosAccess();
			paramObj.setCntrNo(cntrNo);
			paramObj.setGlobalNo(resp.getGlobalNo());
			paramObj.setSvcName(resp.getSvcName() + "/" + "saveApdDataPrvd");
			paramObj.setResponseTitle(resp.getRtnDesc());
			paramObj.setResponseBasc(resp.getErrDesc());
			paramObj.setResponseType(resp.getRespTp());
			paramObj.setResponseCode(resp.getRtnCd());
			paramObj.setApiUrl(soapConnUtil.soapEpUrlKosDynamicSvc);
			cmsService.insertKosApiAccess(paramObj);
			
			result.setKosResult(true);
		}
		
		return result;
	}
	
	/**
	 * 보너스(프로모션) 데이터를 수령한다. [Test 용]
	 */
	public void receiveBonusData(String mobileNo, int dataAmt, String dataTp, String expireTerm) throws Exception
	{
		SoapParamSaveApdDataPrvd param = new SoapParamSaveApdDataPrvd(soapConnUtil);
		param.setMobileNo(mobileNo);
		param.setDataAmt(dataAmt);
		param.setDataTp(dataTp);
		param.setExpireTerm(expireTerm);

		SoapResponseSaveApdDataPrvd resp = param.execute();

		if ( YappUtil.isY(resp.getSucesYn()) == false ){
			//throw new YappException(cmnService.getMsg("ERR_DBOX_BONUS"));	// 보너스(프로모션) 데이터 수령에 실패했습니다.
			String svc = "";
			if(resp.getSvcName() != null && !resp.getSvcName().equals("")){
				svc = "["+resp.getSvcName() +" / "+resp.getSrcId() + "] ";
			}
			throw new YappException("KOS_MSG", resp.getRtnCd(), cmnService.getSearchMsg("KOS_ERR_MSG", resp.getRtnDesc(), resp.getRtnCd(), "ERR_DBOX_BONUS"), svc + resp.getRtnDesc(), resp.getGlobalNo());
		}

	}
	
	/**
	 * 데이턱을 생성한다.
	 */
	public void createDatuk(String cntrNo, int dataAmt, String datukId, String mobileNo) throws Exception
	//public void createDatuk(String cntrNo, int dataAmt, String datukId) throws Exception
	{
		SoapParamRetvDatukGiftRstr paramDatuk = new SoapParamRetvDatukGiftRstr(soapConnUtil);
		paramDatuk.setMobileNo(mobileNo);
		paramDatuk.setRcvMobileNo("114");
		paramDatuk.setDataAmt(dataAmt);

		SoapResponseRetvDatukGiftRstr respDatuk = paramDatuk.execute();

		if ( YappUtil.isY(respDatuk.getSucesYn()) == false ){
			//throw new YappException(respDatuk.getRtnCd(), respDatuk.getRtnDesc());
			String svc = "";
			if(respDatuk.getSvcName() != null && !respDatuk.getSvcName().equals("")){
				svc = "["+respDatuk.getSvcName() +" / "+respDatuk.getSrcId() + "] ";
			}
			throw new YappException("KOS_MSG", respDatuk.getRtnCd(), cmnService.getSearchMsg("KOS_ERR_MSG", respDatuk.getRtnDesc(), respDatuk.getRtnCd(), "ERR_DBOX_CRT_DATUK"), svc + respDatuk.getRtnDesc(), respDatuk.getGlobalNo());
		}else{
			KosAccess paramObj = new KosAccess();
			paramObj.setCntrNo(cntrNo);
			paramObj.setGlobalNo(respDatuk.getGlobalNo());
			paramObj.setSvcName(respDatuk.getSvcName() + "/" + "retvDataGiftRstr");
			paramObj.setResponseTitle(respDatuk.getRtnDesc());
			paramObj.setResponseBasc(respDatuk.getErrDesc());
			paramObj.setResponseType(respDatuk.getRespTp());
			paramObj.setResponseCode(respDatuk.getRtnCd());
			paramObj.setApiUrl(soapConnUtil.soapEpUrlKosDynamicSvc);
			cmsService.insertKosApiAccess(paramObj);
		}

		SoapParamSaveDataOneShot param = new SoapParamSaveDataOneShot(soapConnUtil);
		param.setCntrNo(cntrNo);
		param.setDataAmt(dataAmt);
		param.setDatukId(datukId);

		SoapResponseSaveDataOneShot resp = param.execute();
		if ( YappUtil.isY(resp.getSucesYn()) == false ){
			//throw new YappException(cmnService.getMsg("ERR_DBOX_CRT_DATUK"));	// 데이턱 생성에 실패했습니다.
			String svc = "";
			if(resp.getSvcName() != null && !resp.getSvcName().equals("")){
				svc = "["+resp.getSvcName() +" / "+resp.getSrcId() + "] ";
			}
			throw new YappException("KOS_MSG", resp.getRtnCd(), cmnService.getSearchMsg("KOS_ERR_MSG", resp.getRtnDesc(), resp.getRtnCd(), "ERR_DBOX_CRT_DATUK"), svc + resp.getRtnDesc(), resp.getGlobalNo());
		}else{
			KosAccess paramObj = new KosAccess();
			paramObj.setCntrNo(cntrNo);
			paramObj.setGlobalNo(resp.getGlobalNo());
			paramObj.setSvcName(resp.getSvcName() + "/" + "saveDataOneShot");
			paramObj.setResponseTitle(resp.getRtnDesc());
			paramObj.setResponseBasc(resp.getErrDesc());
			paramObj.setResponseType(resp.getRespTp());
			paramObj.setResponseCode(resp.getRtnCd());
			paramObj.setApiUrl(soapConnUtil.soapEpUrlKosDynamicSvc);
			cmsService.insertKosApiAccess(paramObj);
		}
		
	}
	
	/**
	 * 데이턱 데이터를 수령한다.
	 */
	public void receiveDatukData(String cntrNo, String dboxId, String datukId) throws Exception
	{
		SoapParamSaveDataOneShotRecp param = new SoapParamSaveDataOneShotRecp(soapConnUtil);
		param.setCntrNo(cntrNo);
		param.setDboxId(dboxId);
		param.setDatukId(datukId);

		SoapResponseSaveDataOneShotRecp resp = param.execute();
		if ( YappUtil.isY(resp.getSucesYn()) == false ){
			//throw new YappException(cmnService.getMsg("ERR_DBOX_RCV_DATUK"));	// 데이턱 수령에 실패했습니다.
			String svc = "";
			if(resp.getSvcName() != null && !resp.getSvcName().equals("")){
				svc = "["+resp.getSvcName() +" / "+resp.getSrcId() + "] ";
			}
			throw new YappException("KOS_MSG", resp.getRtnCd(), cmnService.getSearchMsg("KOS_ERR_MSG", resp.getRtnDesc(), resp.getRtnCd(), "ERR_DBOX_RCV_DATUK"), svc + resp.getRtnDesc(), resp.getGlobalNo());
		}else{
			KosAccess paramObj = new KosAccess();
			paramObj.setCntrNo(cntrNo);
			paramObj.setGlobalNo(resp.getGlobalNo());
			paramObj.setSvcName(resp.getSvcName() + "/" + "saveDataOneShotRecp");
			paramObj.setResponseTitle(resp.getRtnDesc());
			paramObj.setResponseBasc(resp.getErrDesc());
			paramObj.setResponseType(resp.getRespTp());
			paramObj.setResponseCode(resp.getRtnCd());
			paramObj.setApiUrl(soapConnUtil.soapEpUrlKosDynamicSvc);
			cmsService.insertKosApiAccess(paramObj);
		}
		
	}
	
	/**
	 * 데이턱 데이터를 회수한다.
	 */
	public void returnDatukData(String dboxId, String datukId) throws Exception
	{
		SoapParamSaveDataClect param = new SoapParamSaveDataClect(soapConnUtil);
		param.setDboxId(dboxId);
		param.setDatukId(datukId);

		SoapResponseSaveDataClect resp = param.execute();
		if ( YappUtil.isY(resp.getSucesYn()) == false ){
			//throw new YappException(cmnService.getMsg("ERR_DBOX_RTN_DATUK"));	// 데이턱 회수에 실패했습니다.
			String svc = "";
			if(resp.getSvcName() != null && !resp.getSvcName().equals("")){
				svc = "["+resp.getSvcName() +" / "+resp.getSrcId() + "] ";
			}
			throw new YappException("KOS_MSG", resp.getRtnCd(), cmnService.getSearchMsg("KOS_ERR_MSG", resp.getRtnDesc(), resp.getRtnCd(), "ERR_DBOX_RTN_DATUK"), svc + resp.getRtnDesc(), resp.getGlobalNo());
		}else{
			KosAccess paramObj = new KosAccess();
			//paramObj.setCntrNo(cntrNo);
			paramObj.setGlobalNo(resp.getGlobalNo());
			paramObj.setSvcName(resp.getSvcName() + "/" + "saveDataClect");
			paramObj.setResponseTitle(resp.getRtnDesc());
			paramObj.setResponseBasc(resp.getErrDesc());
			paramObj.setResponseType(resp.getRespTp());
			paramObj.setResponseCode(resp.getRtnCd());
			paramObj.setEtc("dboxId=" + dboxId + " datukId=" + datukId);
			paramObj.setApiUrl(soapConnUtil.soapEpUrlKosDynamicSvc);
			cmsService.insertKosApiAccess(paramObj);
		}
	}

	/**
	 * 데이턱 상세내역을 조회한다.
	 */
	public List<DatukDtl> retrieveDataOneShotDtl(String dboxId, String datukId, String validStYmd) throws Exception
	{
		SoapParamRetvDataOneShotDtl param = new SoapParamRetvDataOneShotDtl(soapConnUtil);
		param.setDboxId(dboxId);
		param.setDatukId(datukId);
		param.setValidStYmd(validStYmd);

		SoapResponseRetvDataOneShotDtl resp = param.execute();
		if ( YappUtil.isNotEmpty(resp.getSucesYn()) && YappUtil.isY(resp.getSucesYn()) == false){
			//throw new YappException(cmnService.getMsg("ERR_DATUK_DRCT_DTL"));	// 테이턱 상세조회에 실패했습니다.
			String svc = "";
			if(resp.getSvcName() != null && !resp.getSvcName().equals("")){
				svc = "["+resp.getSvcName() +" / "+resp.getSrcId() + "] ";
			}
			throw new YappException("KOS_MSG", resp.getRtnCd(), cmnService.getSearchMsg("KOS_ERR_MSG", resp.getRtnDesc(), resp.getRtnCd(), "ERR_DATUK_DRCT_DTL"), svc + resp.getRtnDesc(), resp.getGlobalNo());
		}
		return resp.getDatukDtlList();
	}
	
	/**
	 * 내 데이터 상세 정보를 조회한다.
	 */
	public List<DataInfo> retrieveDrctlyUseQntDtl(String cntrNo, String mobileNo) throws Exception
	{
		SoapParamRetrieveDrctlyUseQntDtl param = new SoapParamRetrieveDrctlyUseQntDtl(soapConnUtil);
		param.setCntrNo(cntrNo);
		param.setMobileNo(mobileNo);
		
		SoapResponseRetrieveDrctlyUseQntDtl resp = param.execute();
		if ( YappUtil.isNotEq(resp.getRespTp(), SUCCESS_RESP_TP) ){
			String svc = "";
			if(resp.getSvcName() != null && !resp.getSvcName().equals("")){
				svc = "["+resp.getSvcName() +" | "+resp.getSrcId() + "] ";
			}
			throw new YappException("KOS_MSG", resp.getErrCd(), cmnService.getMsg("ERR_DBOX_DRCT_DTL"), svc + resp.getErrDesc(), resp.getGlobalNo());	// 데이터 상세 조회에 실패했습니다.
		}

		List<DataInfo> dataInfoList = resp.getDataInfoList();
		logger.info("11111 size : "+dataInfoList.size());
		for ( int i = dataInfoList.size() - 1; i >= 0; i-- )
		{
			DataInfo dataInfo = dataInfoList.get(i);
			// 기본 요금제를 제일 상단으로 옮긴다.
			CallingPlan callingPlan = cmsService.getCallingPlanInfo(dataInfo.getDataTp());
			if ( callingPlan != null && YappUtil.isNotEmpty(callingPlan.getPpCd()) ) {
				// 무한 요금제는 당월 소멸로 바꾼다.
				if ( YappUtil.isY(callingPlan.getInfYn()) )
				{
					dataInfo.setDataAmt(dataInfo.getDataAmt());
					dataInfo.setDsprCurMnthDataAmt(dataInfo.getRmnDataAmt());
					dataInfo.setDsprNextMnthDataAmt(0);
				}
				dataInfoList.remove(i);
				dataInfoList.add(0, dataInfo);
			}
		}
		return resp.getDataInfoList();
	}

	/**
	 * 내 데이터 선물가능 용량을 조회한다.
	 */
	public DataInfo retrieveDrctlyUseQntDtl2(String cntrNo, String mobileNo) throws Exception
	{
		SoapParamRetrieveDrctlyUseQntDtl param = new SoapParamRetrieveDrctlyUseQntDtl(soapConnUtil);
		param.setCntrNo(cntrNo);
		param.setMobileNo(mobileNo);

		SoapResponseRetrieveDrctlyUseQntDtl resp = param.execute();
		if ( YappUtil.isNotEq(resp.getRespTp(), SUCCESS_RESP_TP) ){
			String svc = "";
			if(resp.getSvcName() != null && !resp.getSvcName().equals("")){
				svc = "["+resp.getSvcName() +" / "+resp.getSrcId() + "] ";
			}
			throw new YappException("KOS_MSG", resp.getErrCd(), cmnService.getMsg("ERR_DBOX_DRCT_DTL"), svc + resp.getErrDesc(), resp.getGlobalNo());	// 데이터 상세 조회에 실패했습니다.
		}

		List<DataInfo> dataInfoList = resp.getDataInfoList();
		DataInfo dataInfoTmp = new DataInfo();
		for ( int i = dataInfoList.size() - 1; i >= 0; i-- )
		{
			DataInfo dataInfo = dataInfoList.get(i);

			CallingPlan callingPlan = cmsService.getCallingPlanInfo(dataInfo.getDataTp());
			if ( callingPlan != null && YappUtil.isNotEmpty(callingPlan.getPpCd()) ) {
				dataInfoTmp.setRmnDataAmt(dataInfo.getRmnDataAmt());
				dataInfoTmp.setDsprCurMnthDataAmt(dataInfo.getDsprCurMnthDataAmt());
				dataInfoTmp.setGiftPsYn(callingPlan.getGiftPsYn());
			}
		}
		return dataInfoTmp;
	}

	/**
	 * 내 데이터 상세 정보를 조회한다.
	 */
	public List<DataInfo> retrieveDrctlyUseQntDtl3(String cntrNo, String mobileNo) throws Exception
	{
		SoapParamRetrieveDrctlyUseQntDtl2 param = new SoapParamRetrieveDrctlyUseQntDtl2(soapConnUtil);
		param.setCntrNo(cntrNo);
		param.setMobileNo(mobileNo);
		
		SoapResponseRetrieveDrctlyUseQntDtl2 resp = param.execute();
		if ( YappUtil.isNotEq(resp.getRespTp(), SUCCESS_RESP_TP) ){
			String svc = "";
			if(resp.getSvcName() != null && !resp.getSvcName().equals("")){
				svc = "["+resp.getSvcName() +" / "+resp.getSrcId() + "] ";
			}
			throw new YappException("KOS_MSG", resp.getErrCd(), cmnService.getMsg("ERR_DBOX_DRCT_DTL"), svc + resp.getErrDesc(), resp.getGlobalNo());	// 데이터 상세 조회에 실패했습니다.
		}

		List<DataInfo> dataInfoList = resp.getDataInfoList();
		logger.info("11111 size : "+dataInfoList.size());
		for ( int i = dataInfoList.size() - 1; i >= 0; i-- )
		{
			DataInfo dataInfo = dataInfoList.get(i);
			// 기본 요금제를 제일 상단으로 옮긴다.
			CallingPlan callingPlan = cmsService.getCallingPlanInfo(dataInfo.getDataTp());
			if ( callingPlan != null && YappUtil.isNotEmpty(callingPlan.getPpCd()) ) {
				// 무한 요금제는 당월 소멸로 바꾼다.
				if ( YappUtil.isY(callingPlan.getInfYn()) )
				{
					dataInfo.setDataAmt(dataInfo.getDataAmt());
					dataInfo.setDsprCurMnthDataAmt(dataInfo.getRmnDataAmt());
					dataInfo.setDsprNextMnthDataAmt(0);
				}
				dataInfo.setDataNm("요금제(기본제공)");
				dataInfoList.remove(i);
				dataInfoList.add(0, dataInfo);
			}
		}
		return resp.getDataInfoList();
	}

	/**
	 * 내 데이터 정보를 조회한다.
	 */
	public DataInfo retrieveDrctlyUseQnt(String cntrNo, String mobileNo, String trDate) throws Exception
	{
		SoapParamRetrieveDrctlyUseQnt param = new SoapParamRetrieveDrctlyUseQnt(soapConnUtil);
		param.setCntrNo(cntrNo);
		param.setMobileNo(mobileNo);
		param.setTrDate(trDate);
		
		SoapResponseRetrieveDrctlyUseQnt resp = param.execute();
		if ( YappUtil.isNotEq(resp.getRespTp(), SUCCESS_RESP_TP) ){
			String svc = "";
			if(resp.getSvcName() != null && !resp.getSvcName().equals("")){
				svc = "["+resp.getSvcName() +" | "+resp.getSrcId() + "] ";
			}
			throw new YappException("KOS_MSG", resp.getErrCd(), cmnService.getMsg("ERR_DBOX_DRCT_DTL"), svc + resp.getErrDesc(), resp.getGlobalNo());	// 데이터 상세 조회에 실패했습니다.
		}
		return resp.getDataInfo();
	}

	/**
	 * 공유 데이터 합계 정보를 조회한다.
	 */
	public DataShareList dataSharPrvQntRetv(String cntrNo, String retvDate) throws Exception
	{
		SoapParamDataSharUseQntRetv param = new SoapParamDataSharUseQntRetv(soapConnUtil);
		param.setCntrNo(cntrNo);
		param.setRetvDate(retvDate);;
		
		SoapResponseDataSharUseQntRetv resp = param.execute();
		return resp.getDataShareList();
	}
	
	/**
	 * 층전데이터 잔여량을 조회한다.
	 */
	public DataRefill retrieveDataRefill(String cntrNo, String mobileNo) throws Exception
	{
		SoapParamRetrieveDataRefill param = new SoapParamRetrieveDataRefill(soapConnUtil);
		param.setCntrNo(cntrNo);
		param.setMobileNo(mobileNo);
		DataRefill returnDataRefill = new DataRefill();
		try{
			SoapResponseRetrieveDataRefill resp = param.execute();
			returnDataRefill = resp.getDataRefill();
		}catch(RuntimeException e){
			e.printStackTrace();
			logger.info("retrieveDataRefill ERROR : \n" + e);
		}catch(Exception e){
			//e.printStackTrace();
			logger.info("retrieveDataRefill ERROR : \n" + e);
		}
		return returnDataRefill;
	}
	
	/**
	 * 내 데이터 정보를 조회한다.(알요금제)
	 */
	public BigiChargBlnc retrieveBigiChargBlnc(String cntrNo, String mobileNo) throws Exception
	{
		SoapParamRetrieveBigiChargBlnc param = new SoapParamRetrieveBigiChargBlnc(soapConnUtil);
		param.setCntrNo(cntrNo);
		param.setMobileNo(mobileNo);
		
		SoapResponseRetrieveBigiChargBlnc resp = param.execute();
		if ( YappUtil.isNotEq(resp.getRespTp(), SUCCESS_RESP_TP) ){
			String svc = "";
			if(resp.getSvcName() != null && !resp.getSvcName().equals("")){
				svc = "["+resp.getSvcName() +" | "+resp.getSrcId() + "] ";
			}
			throw new YappException("KOS_MSG", resp.getErrCd(), cmnService.getMsg("ERR_DBOX_DRCT_DTL"), svc + resp.getErrDesc(), resp.getGlobalNo());	// 데이터 상세 조회에 실패했습니다.
		}

		BigiChargBlnc dataBigiInfo = resp.getDataBigiInfo();

		return dataBigiInfo;
	}

	public void getRetvDataGiftRstr(String mobileNo, int dataAmt) throws Exception
	{
		// 선물제약 조회
		SoapParamRetvDatukGiftRstr param = new SoapParamRetvDatukGiftRstr(soapConnUtil);
		param.setMobileNo(mobileNo);
		param.setRcvMobileNo(mobileNo);
		param.setDataAmt(dataAmt);

		SoapResponseRetvDatukGiftRstr resp = param.execute();

		if ( YappUtil.isY(resp.getSucesYn()) == false ){
			throw new YappException(resp.getRtnCd(), resp.getRtnDesc());
		}
	}
	
	/**
	 * 시간옵션을 조회한다.
	 */
	public List<TimeOption> searchTimeOption() throws Exception
	{
		SoapParamEfctCdRetv param = new SoapParamEfctCdRetv(soapConnUtil);

		SoapResponseEfctCdRetv resp = param.execute();
		List<TimeOption> timeOptionList = resp.getTimeOptionInfoList();
		return timeOptionList;
	}

	/**
	 * 마이타임플랜 시간 정보 조회한다..
	 */
	public String getMyTimePlan(String cntrNo) throws Exception
	{
		SoapParamAplyProdInfoRetv param = new SoapParamAplyProdInfoRetv(soapConnUtil);
		param.setCntrNo(cntrNo);
		SoapResponseAplyProdInfoRetv resp = param.execute();
		String paramSbst = resp.getAplyProdParamInfo().getParamSbst();
		if(resp.getAplyProdParamInfo().getUnitSvcId() == null || paramSbst == null || "".equals(resp.getAplyProdParamInfo().getUnitSvcId())){
			throw new YappException("KOS_MSG", cmnService.getMsg("ERR_NO_FREE_THREE"));
		}
		
		String[] paramSbstTmp = paramSbst.split("\\|");
		String timePlan = "";
		if(paramSbstTmp != null && paramSbstTmp.length > 1){
			timePlan = paramSbstTmp[1].replaceAll("TIME_DUR=", "");
		}
		return timePlan;
	}

	/**
	 * 기가입상품정보를 조회한다..
	 */
	public void searchAplyProdInfoRetv(String cntrNo) throws Exception
	{
		SoapParamAplyProdInfoRetv param = new SoapParamAplyProdInfoRetv(soapConnUtil);
		param.setCntrNo(cntrNo);
		SoapResponseAplyProdInfoRetv resp = param.execute();

		SoapParamProdPrevChk param1 = new SoapParamProdPrevChk(soapConnUtil);
		param1.setAplyPplProdInfo(resp.getAplyPplProdInfo());
		param1.setAplyProdParamInfo(resp.getAplyProdParamInfo());

		SoapResponseProdPrevChk resp1 = param1.execute();
	}
	
	/**
	 * 기가입상품정보를 설정한다..
	 */
	public void setProcessProdStoreBas(String cntrNo, String timeOption) throws Exception
	{
		SoapParamAplyProdInfoRetv param = new SoapParamAplyProdInfoRetv(soapConnUtil);
		param.setCntrNo(cntrNo);
		SoapResponseAplyProdInfoRetv resp = param.execute();
		String paramSbst = resp.getAplyProdParamInfo().getParamSbst();
		if(resp.getAplyProdParamInfo().getUnitSvcId() == null || paramSbst == null || "".equals(resp.getAplyProdParamInfo().getUnitSvcId())){
			throw new YappException("KOS_MSG", cmnService.getMsg("ERR_NO_FREE_THREE"));
		}

		SoapParamProdPrevChk param1 = new SoapParamProdPrevChk(soapConnUtil);
		param1.setAplyPplProdInfo(resp.getAplyPplProdInfo());
		param1.setAplyProdParamInfo(resp.getAplyProdParamInfo());

		SoapResponseProdPrevChk resp1 = param1.execute();
		if ( YappUtil.isNotEq(resp1.getResltCd(), "Y") ){
			String svc = "";
			if(resp.getSvcName() != null && !resp.getSvcName().equals("")){
				svc = "["+resp.getSvcName() +" / "+resp.getSrcId() + "] ";
			}
			throw new YappException("KOS_MSG", resp1.getRuleMsgId(), cmnService.getSearchMsg("KOS_ERR_MSG", resp1.getRuleMsgSbst(), resp1.getRuleMsgId(), "ERR_NOT_SET_MYTIME_PLAN"), svc + resp1.getRuleMsgSbst(), resp1.getGlobalNo());	// 마이타임플랜 시간변경을 실패했습니다.
		}

		SoapParamProcessProdStoreBas param2 = new SoapParamProcessProdStoreBas(soapConnUtil);
		param2.setAplyPplProdInfo(resp.getAplyPplProdInfo());
		param2.setAplyProdParamInfo(resp.getAplyProdParamInfo());
		//param2.setTimeOption("TIME_OPTION1=7|TIME_DUR=07001000|");
		int option = 0;
		if(timeOption != null){
			option = Integer.parseInt(timeOption.substring(0, 2));
		}

		if(option == 0){
			option = 24;
		}
		logger.info("TIME_OPTION DATA : " + " TIME_OPTION1="+option+"|TIME_DUR="+timeOption+"|");
		param2.setTimeOption("TIME_OPTION1="+option+"|TIME_DUR="+timeOption+"|");
		SoapResponseProcessProdStoreBas resp2 = param2.execute();
		if ( YappUtil.isNotEq(resp2.getResltCd(), "Y") ){
			throw new YappException("KOS_MSG", cmnService.getMsg("ERR_NOT_SET_MYTIME_PLAN"));	// 마이타임플랜 시간변경을 실패했습니다.
		}
	}
	
	/**
	 * 법정대리인을 조회한다.
	 */
	public List<ParentsInfo> retrieveLegalAgntSvcNo(String telno) throws Exception
	{
		String svc = "";
		SoapParamLegalAgntInfoAdm param = new SoapParamLegalAgntInfoAdm(soapConnUtil);
		param.setTelno(telno);

		SoapResponseLegalAgntInfoAdm resp = param.execute();
		if ( YappUtil.isNotEq(resp.getRespTp(), SUCCESS_RESP_TP) ){
			if(resp.getSvcName() != null && !resp.getSvcName().equals("")){
				svc = "["+resp.getSvcName() +" / "+resp.getSrcId() + "] ";
			}
			throw new YappException("KOS_MSG", resp.getErrCd(), cmnService.getMsg("ERR_NOT_PARENTS_INFO"), svc + resp.getErrDesc(), resp.getGlobalNo());
		}else{
			if(resp.getParentsInfoList().size() < 1 || resp.getParentsInfoList().get(0).getParentsTelno().length() < 11){
				if(resp.getSvcName() != null && !resp.getSvcName().equals("")){
					svc = "["+resp.getSvcName() +" / "+resp.getSrcId() + " / "+ resp.getParentsInfoList().get(0).getParentsTelno() + "] ";
				}
				throw new YappException("KOS_MSG", EnumRsltCd.C999.getRsltCd(),cmnService.getMsg("ERR_NOT_PARENTS_INFO"), svc, resp.getGlobalNo());
			}
		}
		return resp.getParentsInfoList();
	}

	public void sendSms(String telno, String smsBody, String msgKey) throws Exception
	{
		// MSG ID 생성
		Date now = new Date();
		//String sDateStr = new SimpleDateFormat("yyMMddHHmmssS").format(now);
		String sDateStr2 = new SimpleDateFormat("D").format(now);

		// SMS TRANSAC ID : 20180612199225(SMS_MSG_ID) + 03 + 165 (DAY_IDX) => 오늘 6/14일 + 050 ( 업무팀 코드) + 0000000000001(seq) 
		String transId = "20180612199225" + "03" + YappUtil.lpad(sDateStr2, 3, "0") + "050" + msgKey;
		// MSG ID 20180612199225 Fix
		SoapParamYdbSendSms param = new SoapParamYdbSendSms(soapConnUtil);
		param.setTelno(telno);
		param.setSmsBody(smsBody);
		param.setTransId(transId);
		param.setMsgId("20180612199225");

		SoapResponseYdbSendSms resp = param.execute();
		if ( YappUtil.isNotEq(resp.getRespTp(), SUCCESS_RESP_TP) ){
			String svc = "";
			if(resp.getSvcName() != null && !resp.getSvcName().equals("")){
				svc = "["+resp.getSvcName() +" / "+resp.getSrcId() + "] ";
			}
			throw new YappException("KOS_MSG", resp.getErrCd(), cmnService.getMsg("ERR_NO_SEND_SMS"), svc + resp.getErrDesc(), resp.getGlobalNo());
		}
	}
	
	/**
	 * 5G여부 호출
	 */
	public String getCondByModelInfoRetv(String resourceModelName) throws Exception
	{
		SoapParamCondByModelInfoRetv paramInfo = new SoapParamCondByModelInfoRetv(soapConnUtil);
		logger.info("========================8=========================" + resourceModelName);
		paramInfo.setIntmModelNm(resourceModelName);
		if(!"local".equals (profileName) && !"dev".equals(profileName)){
			//paramInfo.setIntmModelNm("");
		}
		logger.info("========================9=========================");
		paramInfo.setInqrIndCd("2");
		SoapResponseCondByModelInfoRetv respInfo = paramInfo.execute();
		logger.info("========================10=========================");
		System.out.println("respInfo.getRespTp() " + respInfo.getRespTp());
		if ( YappUtil.isNotEq(respInfo.getRespTp(), SUCCESS_RESP_TP) ){
			logger.info("========================11=========================");
			String svc = "";
			if(respInfo.getSvcName() != null && !respInfo.getSvcName().equals("")){
				svc = "["+respInfo.getSvcName() +" / "+respInfo.getSrcId() + "] ";
			}
			throw new YappException("KOS_MSG", respInfo.getRtnCd(), cmnService.getSearchMsg("KOS_ERR_MSG", respInfo.getRtnDesc(), respInfo.getRtnCd(), "ERR_NO_DATA"), svc + respInfo.getRtnDesc(), respInfo.getGlobalNo());
		}
		logger.info("profileName=============>" + profileName);
		if(!"local".equals (profileName) && !"dev".equals(profileName)){
			//개발계에서는 강제로 5G로 설정
			respInfo.setFivegSuprtYn("Y");
		}
		logger.info("getFivegSuprtYn=============>" + respInfo.getFivegSuprtYn());
		
		return respInfo.getFivegSuprtYn();
	}

	/**
	 * 부가서비스 이력 호출
	 */
	public List<SvcHstByCustInfo> getSvcHstByCustIdRetv(String cntrNo) throws Exception
	{
		SoapParamSvcHstByCustIdRetv paramInfo = new SoapParamSvcHstByCustIdRetv(soapConnUtil);
		paramInfo.setCntrNo(cntrNo);
		SoapResponseSvcHstByCustIdRetv respInfo = paramInfo.execute();
		System.out.println("respInfo.getRespTp() " + respInfo.getRespTp());
		if ( YappUtil.isNotEq(respInfo.getRespTp(), SUCCESS_RESP_TP) ){
			String svc = "";
			if(respInfo.getSvcName() != null && !respInfo.getSvcName().equals("")){
				svc = "["+respInfo.getSvcName() +" / "+respInfo.getSrcId() + "] ";
			}
			throw new YappException("KOS_MSG", respInfo.getRtnCd(), cmnService.getSearchMsg("KOS_ERR_MSG", respInfo.getRtnDesc(), respInfo.getRtnCd(), "ERR_NO_DATA"), svc + respInfo.getRtnDesc(), respInfo.getGlobalNo());
		}
		return respInfo.getSvcHstByCustInfoList();
	}

	/**
	 * (사전 체크)부가서비스 이력 호출
	 */
	public SoapResponseProdSbscTrmnStoreTrtPreCheck setProdSbscTrmnStoreTrtPreCheck(String cntrNo, String vasItemCd, String vasItemId, String efctStDt, String efctFnsDt, String prodSbscTrmnCd, String prodHstSeq) throws Exception
	{
		efctStDt = YappUtil.rpad(efctStDt, '0', 14);
		efctFnsDt = YappUtil.rpad(efctFnsDt, '0', 14);
		
		SoapParamProdSbscTrmnStoreTrtPreCheck paramInfo = new SoapParamProdSbscTrmnStoreTrtPreCheck(soapConnUtil);
		paramInfo.getAplyRoamingInfo().setCntrNo(cntrNo);
		paramInfo.getAplyRoamingInfo().setProdId(vasItemCd);
		paramInfo.getAplyRoamingInfo().setUnitSvcId(vasItemId);
		paramInfo.getAplyRoamingInfo().setEfctStDt(efctStDt);
		paramInfo.getAplyRoamingInfo().setEfctFnsDt(efctFnsDt);
		paramInfo.getAplyRoamingInfo().setProdSbscTrmnCd(prodSbscTrmnCd);
		paramInfo.getAplyRoamingInfo().setProdHstSeq(prodHstSeq);
		
		SoapResponseProdSbscTrmnStoreTrtPreCheck respInfo = paramInfo.execute();
		logger.info("respInfo.getRespTp()       : " + respInfo.getRespTp());
		logger.info("respInfo.getResltCd()      : " + respInfo.getResltCd());
//		logger.info("respInfo.getTrtResltSbst() : " + respInfo.getTrtResltSbst());
		logger.info("respInfo.getErrCd()        : " + respInfo.getErrCd());
		logger.info("respInfo.getErrDesc()      : " + respInfo.getErrDesc());
		logger.info("respInfo.getRtnCd()        : " + respInfo.getRtnCd());
		logger.info("respInfo.getRtnDesc()      : " + respInfo.getRtnDesc());
		
		/** PLAN-426 : header 정보의 respTp 체크에서 body 정보의 resltCd 체크로 변경 */
		if(YappUtil.isEq(respInfo.getResltCd(), "N")) {
			logger.info("respInfo : " + respInfo.toString());
			if(YappUtil.isNotEmpty(respInfo.getRuleNm()) && YappUtil.isNotEmpty(respInfo.getRuleMsgSbst())) {
				logger.info("YappException Rule : " + "KOS_MSG", "", respInfo.getRuleMsgSbst(), "", respInfo.getGlobalNo());
				throw new YappException("KOS_MSG", "", respInfo.getRuleMsgSbst(), "", respInfo.getGlobalNo());
			}
			else {
				logger.info("YappException Err : " + "KOS_MSG", "", respInfo.getErrDesc(), "", respInfo.getGlobalNo());
				throw new YappException("KOS_MSG", "", respInfo.getErrDesc(), "", respInfo.getGlobalNo());
			}
		}else{
			KosAccess paramObj = new KosAccess();
			paramObj.setCntrNo(cntrNo);
			paramObj.setGlobalNo(respInfo.getGlobalNo());
			paramObj.setSvcName(respInfo.getSvcName() + "/" + "mobileProdPreChk");
			paramObj.setResponseTitle(respInfo.getRtnDesc());
			paramObj.setResponseBasc(respInfo.getErrDesc());
			paramObj.setResponseType(respInfo.getRespTp());
			paramObj.setResponseCode(respInfo.getRtnCd());
			paramObj.setApiUrl(soapConnUtil.soapEpUrlKosDynamicSvc);
			cmsService.insertKosApiAccess(paramObj);
		}
		
		return respInfo;
	}
	
	/**
	 * 부가서비스 이력 호출
	 */
	public SoapResponseProdSbscTrmnStoreTrt setProdSbscTrmnStoreTrt(String cntrNo, String vasItemCd, String vasItemId, String efctStDt, String efctFnsDt, String prodSbscTrmnCd, String prodHstSeq) throws Exception
	{
		efctStDt = YappUtil.rpad(efctStDt, '0', 14);
		efctFnsDt = YappUtil.rpad(efctFnsDt, '0', 14);
		
		SoapParamProdSbscTrmnStoreTrt paramInfo = new SoapParamProdSbscTrmnStoreTrt(soapConnUtil);
		paramInfo.getAplyRoamingInfo().setCntrNo(cntrNo);
		paramInfo.getAplyRoamingInfo().setProdId(vasItemCd);
		paramInfo.getAplyRoamingInfo().setUnitSvcId(vasItemId);
		paramInfo.getAplyRoamingInfo().setEfctStDt(efctStDt);
		paramInfo.getAplyRoamingInfo().setEfctFnsDt(efctFnsDt);
		paramInfo.getAplyRoamingInfo().setProdSbscTrmnCd(prodSbscTrmnCd);
		paramInfo.getAplyRoamingInfo().setProdHstSeq(prodHstSeq);
		SoapResponseProdSbscTrmnStoreTrt respInfo = paramInfo.execute();
		logger.info("respInfo.getRespTp() " + respInfo.getRespTp());
		if ( YappUtil.isNotEq(respInfo.getRespTp(), SUCCESS_RESP_TP) ){
			String svc = "";
			if(respInfo.getSvcName() != null && !respInfo.getSvcName().equals("")){
				svc = "["+respInfo.getSvcName() +" / "+respInfo.getSrcId() + "] ";
			}
			//throw new YappException("KOS_MSG", respInfo.getRtnCd(), cmnService.getSearchMsg("KOS_ERR_MSG", respInfo.getRtnDesc(), respInfo.getRtnCd(), "ERR_NO_DATA"), svc + respInfo.getRtnDesc(), respInfo.getGlobalNo());
			throw new YappException("KOS_MSG", respInfo.getRtnCd(), respInfo.getErrDesc(), svc + respInfo.getRtnDesc(), respInfo.getGlobalNo());
		}else{
			KosAccess paramObj = new KosAccess();
			paramObj.setCntrNo(cntrNo);
			paramObj.setGlobalNo(respInfo.getGlobalNo());
			paramObj.setSvcName(respInfo.getSvcName() + "/" + "processProdStoreBas");
			paramObj.setResponseTitle(respInfo.getRtnDesc());
			paramObj.setResponseBasc(respInfo.getErrDesc());
			paramObj.setResponseType(respInfo.getRespTp());
			paramObj.setResponseCode(respInfo.getRtnCd());
			paramObj.setApiUrl(soapConnUtil.soapEpUrlKosSvc);
			cmsService.insertKosApiAccess(paramObj);
		}
		return respInfo;
	}
	
	/** 220602
	 * KOS 가입제한 조회
	 */
	public boolean joinLimit(String cntrNo) throws Exception
	{
		logger.info("joinLimit cntrNo  : "+cntrNo);

		if(!YappUtil.isEmpty(cntrNo)){
			
			// 가입 제한 조회
			SoapParamRetvDataboxSbscRstr param = new SoapParamRetvDataboxSbscRstr(soapConnUtil);
			param.setCntrNo(cntrNo);

			SoapResponseRetvDataboxSbscRstr resp = param.execute();

			if ( YappUtil.isY(resp.getSucesYn()) == false ){
				return false;
			}
		}
		
		return true;
	}
}
