package com.kt.yapp.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kt.yapp.domain.DataDivDtl;
import com.kt.yapp.domain.DataInfo;
import com.kt.yapp.domain.Databox;
import com.kt.yapp.domain.DataboxDtl;
import com.kt.yapp.domain.DataboxPullInfo;
import com.kt.yapp.em.EnumDboxUseTp;
import com.kt.yapp.exception.YappException;
import com.kt.yapp.soap.SoapConnUtil;
import com.kt.yapp.soap.param.SoapParamRetvDataGiftRstr;
import com.kt.yapp.soap.param.SoapParamRetvDataOutputRstr;
import com.kt.yapp.soap.param.SoapParamRetvDataStats;
import com.kt.yapp.soap.param.SoapParamRetvDataboxBas;
import com.kt.yapp.soap.param.SoapParamRetvDataboxIosHst;
import com.kt.yapp.soap.param.SoapParamRetvDataboxSbscRstr;
import com.kt.yapp.soap.param.SoapParamSaveApdDataPrvd;
import com.kt.yapp.soap.param.SoapParamSaveDataClect;
import com.kt.yapp.soap.param.SoapParamSaveDataGift;
import com.kt.yapp.soap.param.SoapParamSaveDataOneShot;
import com.kt.yapp.soap.param.SoapParamSaveDataOneShotRecp;
import com.kt.yapp.soap.param.SoapParamSaveDataOutput;
import com.kt.yapp.soap.param.SoapParamSaveDataboxSbsc;
import com.kt.yapp.soap.param.SoapParamSaveDataboxTrmn;
import com.kt.yapp.soap.response.SoapResponseRetvDataGiftRstr;
import com.kt.yapp.soap.response.SoapResponseRetvDataOutputRstr;
import com.kt.yapp.soap.response.SoapResponseRetvDataStats;
import com.kt.yapp.soap.response.SoapResponseRetvDataboxBas;
import com.kt.yapp.soap.response.SoapResponseRetvDataboxIosHst;
import com.kt.yapp.soap.response.SoapResponseRetvDataboxIosHst.DboxHist;
import com.kt.yapp.soap.response.SoapResponseRetvDataboxSbscRstr;
import com.kt.yapp.soap.response.SoapResponseSaveApdDataPrvd;
import com.kt.yapp.soap.response.SoapResponseSaveDataClect;
import com.kt.yapp.soap.response.SoapResponseSaveDataGift;
import com.kt.yapp.soap.response.SoapResponseSaveDataOneShot;
import com.kt.yapp.soap.response.SoapResponseSaveDataOneShotRecp;
import com.kt.yapp.soap.response.SoapResponseSaveDataOutput;
import com.kt.yapp.soap.response.SoapResponseSaveDataboxSbsc;
import com.kt.yapp.soap.response.SoapResponseSaveDataboxTrmn;
import com.kt.yapp.util.YappUtil;

@Service
public class KosService2
{
	@Autowired
	private SoapConnUtil soapConnUtil;
	@Autowired
	private CommonService cmnService;
	@Autowired
	private DboxService dboxService;
	
	private static final Logger logger = LoggerFactory.getLogger(KosService.class);

	/**
	 * 데이터 박스를 생성한다.
	 */
	public String createDbox(String cntrNo) throws Exception
	{
		return "databoxid" + System.currentTimeMillis();
	}
	
	/**
	 * 데이터 박스를 해지한다.
	 */
	public void closeDbox(String cntrNo) throws Exception
	{
	}
	
	/**
	 * 데이터 박스 ID 를 조회한다.
	 */
	public String getDboxId(String cntrNo) throws Exception
	{
		return "14321";
	}
	/**
	 * 데이터 박스 정보를 조회한다.
	 */
	public Databox getDboxInfo(String cntrNo) throws Exception
	{
		Databox dbox = new Databox();
		dbox.setDboxDataAmtCurMnth(500);
		dbox.setDboxDataAmtNextMnth(1500);
		dbox.setDboxDataAmt(2000);
		dbox.setDboxDataSize(2000);
		
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
		List<DataDivDtl> dboxDtlList = new ArrayList<>();
		DataDivDtl dtl = new DataDivDtl();
		dtl.setDivContents("홍길동");
		dtl.setUseDataAmt(100);
		dtl.setRmnDataAmt(1000);
		dtl.setUseDate(new Date());
		
		dboxDtlList.add(dtl);
		
		DataDivDtl dtl2 = new DataDivDtl();
		dtl2.setDivContents("데이턱");
		dtl2.setUseDataAmt(300);
		dtl2.setRmnDataAmt(700);
		dtl2.setRtnDatukDataAmt(100);
		dtl2.setRtnDatukStatus("G0002");
		dtl2.setUseDate(new Date());
		
		dboxDtlList.add(dtl2);
		
		
		return dboxDtlList;
	}
	
	/**
	 * 데이터박스 상세 목록을 조회한다.
	 */
	public List<DataboxDtl> getDboxDtlList(String cntrNo, String validYm) throws Exception
	{
		List<DataboxDtl> dboxDtlList = new ArrayList<>();
		DataboxDtl dtl = new DataboxDtl();
		dtl.setDboxContents("홍길동");
		dtl.setUseDataAmt(100);
		dtl.setRmnDataAmt(1000);
		dtl.setUseTp("G0001");
		dtl.setUseDate(new Date());
		
		dboxDtlList.add(dtl);

		DataboxDtl dtl2 = new DataboxDtl();
		dtl2.setDboxContents("꺼내기");
		dtl2.setUseDataAmt(300);
		dtl2.setRmnDataAmt(700);
		dtl2.setUseTp("G0002");
		dtl2.setUseDate(new Date());
		
		dboxDtlList.add(dtl2);
		
		return dboxDtlList;
	}
	
	/**
	 * 소멸 데이터 정보를 조회한다.
	 */
	private DataboxDtl getDsprDboxData(String cntrNo, String validYm, int pullDataAmt) throws Exception
	{
		DataboxDtl dtl = new DataboxDtl();
		
		// 1개월 이전 데이터 조회
		Calendar srchCal = Calendar.getInstance();
		srchCal.add(Calendar.MONTH, -1);
		String srchYm = YappUtil.getCurDate(srchCal.getTime(), "yyyyMM");
		
		int maxDate = srchCal.getActualMaximum(Calendar.DAY_OF_MONTH);
		SoapResponseRetvDataboxIosHst resp = getDboxHist(cntrNo, srchYm + "01", srchYm + maxDate, "R");

		if ( YappUtil.isEmpty(resp.getDboxHistList()) )
			return null;
		
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
			if ( rmnDataSetting == false && YappUtil.isNotEq(dboxHist.getTrtDivCd(), "DO") )
			{
				rmnDataAmt = dboxHist.getRmndQnt() - dboxHist.getTrtQnt();
				rmnDataSetting = true;
				logger.info("최초 잔여량 : " + rmnDataAmt);
			}
			
			// 월 중 최후 잔여량
			if ( i == 0 )
				rmnDataAmtLast = dboxHist.getRmndQnt();
			
			// 꺼내기면 최초 잔여량에서 빼고, 그 외는 소멸예정 데이터에 더한다.
			if ( YappUtil.isEq(dboxHist.getTrtDivCd(), "DO") )
				rmnDataAmt -= dboxHist.getTrtQnt();
			else
				dsprExpectedDataAmt += dboxHist.getTrtQnt();
		}
		logger.info("소멸 예정 데이터양 : " + dsprExpectedDataAmt);

		// 전월에 최초 잔여량보다 꺼내쓴 데이터가 많은 경우 
		// 소멸예정 데이터 = 적립 데이터 + (최초잔여량 - 꺼내쓴데이터) - 당월 꺼내쓴 데이터
		if ( rmnDataAmt < 0 )
			dsprExpectedDataAmt = dsprExpectedDataAmt + rmnDataAmt - pullDataAmt;
		// 전월에 최초 잔여량보다 꺼내쓴 데이터가 적은 경우 
		// 소멸예정 데이터 = 적립 데이터 - 당월 꺼내쓴 데이터
		else
			dsprExpectedDataAmt = dsprExpectedDataAmt - pullDataAmt;
		
		// 소멸 예정 데이터가 0 이하면 소멸 없음
		if ( dsprExpectedDataAmt <= 0 )
			return null;
		
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
		
		return resp;
	}
	
	/**
	 * 데이터를 선물한다.
	 */
	public void giftData(String mobileNo, String rcvMobileNo, int dataAmt) throws Exception
	{
	}
	
	/**
	 * 데이터를 꺼낸다.
	 */
	public void pullData(String cntrNo, String mobileNo, int dataAmt) throws Exception
	{

	}
	
	/**
	 * 보너스(프로모션) 데이터를 수령한다.
	 */
	public void receiveBonusData(String mobileNo, int dataAmt, String dataTp) throws Exception
	{

	}
	
	/**
	 * 데이턱을 생성한다.
	 */
	public void createDatuk(String cntrNo, int dataAmt, String datukId) throws Exception
	{
	}
	
	/**
	 * 데이턱 데이터를 수령한다.
	 */
	public void receiveDatukData(String cntrNo, String dboxId, String datukId) throws Exception
	{
		
	}
	
	/**
	 * 데이턱 데이터를 회수한다.
	 */
	public void returnDatukData(String dboxId, String datukId) throws Exception
	{
	}
	
	/**
	 * 데이터 박스 정보를 조회한다.
	 */
	public Databox getDataboxInfo(String cntrNo)
	{
		//  데이터박스 정보 조회
		Databox dbox = new Databox();
		dbox.setDboxDataAmtCurMnth(500);
		dbox.setDboxDataAmtNextMnth(1500);
		dbox.setDboxDataAmt(2000);
		dbox.setDboxDataSize(2000);
		
		// 데이터박스 꺼내기 정보 세팅
		DataboxPullInfo dboxPullInfo = dboxService.getDboxPullInfo(cntrNo);
		dbox.setDboxPullInfo(dboxPullInfo);
		
		return dbox;
	}

	/**
	 * 데이터박스에서 데이터를 꺼낸다.
	 */
	public void pullData(String cntrNo, long dataAmt)
	{
		//  데이터 꺼내기
	}
	
	/**
	 * 지정 사용자에게 선물한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void giftData(String sndCntrNo, String rcvCntrNo, long dataAmt)
	{
		//  선물하기
	}
	
	/**
	 * 데이터 박스 상세 정보를 조회한다.
	 */
	public List<DataboxDtl> getDataboxDtlList(String cntrNo, String srchYm)
	{
		//  데이터박스 상세 정보 조회
		
		List<DataboxDtl> dboxDtlList = new ArrayList<>();
		DataboxDtl dtl = new DataboxDtl();
		dtl.setDboxContents("홍길동");
		dtl.setUseDataAmt(100);
		dtl.setRmnDataAmt(1000);
		dtl.setUseTp("G0001");
		dtl.setUseDate(new Date());
		
		dboxDtlList.add(dtl);

		DataboxDtl dtl2 = new DataboxDtl();
		dtl2.setDboxContents("꺼내기");
		dtl2.setUseDataAmt(300);
		dtl2.setRmnDataAmt(700);
		dtl2.setUseTp("G0002");
		dtl2.setUseDate(new Date());
		
		dboxDtlList.add(dtl2);
		
		
		return dboxDtlList;
	}

	/**
	 * 데이터 나눔 상세 정보를 조회한다.
	 */
	public List<DataDivDtl> getDataDivDtlList2(String cntrNo, String srchYm)
	{
		//  데이터박스 상세 정보 조회
		
		List<DataDivDtl> dboxDtlList = new ArrayList<>();
		DataDivDtl dtl = new DataDivDtl();
		dtl.setDivContents("홍길동");
		dtl.setUseDataAmt(100);
		dtl.setRmnDataAmt(1000);
		dtl.setUseDate(new Date());
		
		dboxDtlList.add(dtl);
		
		DataDivDtl dtl2 = new DataDivDtl();
		dtl2.setDivContents("데이턱");
		dtl2.setUseDataAmt(300);
		dtl2.setRmnDataAmt(700);
		dtl2.setRtnDatukDataAmt(100);
		dtl2.setRtnDatukStatus("G0002");
		dtl2.setUseDate(new Date());
		
		dboxDtlList.add(dtl2);
		
		
		return dboxDtlList;
	}
	
	/**
	 * 보너스 데이터를 추가한다.
	 */
	public void insertBonusData()
	{
		// 
	}
	
	/**
	 * 데이터를 차감(사용)한다.
	 */
	public void useData()
	{
		// 	
	}
	
	public List<DataInfo> retrieveDrctlyUseQntDtl(String cntrNo, String mobileNo) throws Exception
	{
		return null;
	}
}
