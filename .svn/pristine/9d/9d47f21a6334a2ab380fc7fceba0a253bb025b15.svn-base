package com.kt.yapp.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kt.yapp.common.repository.CommonDao;
import com.kt.yapp.domain.Datuk;
import com.kt.yapp.domain.DatukDtl;
import com.kt.yapp.domain.DatukRcv;
import com.kt.yapp.domain.DatukRtn;
import com.kt.yapp.domain.GiftData;
import com.kt.yapp.domain.UserInfo;
import com.kt.yapp.em.EnumMasterNotiMsg;
import com.kt.yapp.exception.YappException;
import com.kt.yapp.util.YappUtil;

@Service
public class DatukService 
{
	@Autowired
	private KosService kosService;
	@Autowired
	private CmsService cmsService;
	@Autowired
	private UserService userService;
	@Autowired
	private GiftService giftService;
	
	@Autowired
	private CommonService cmnService;
	@Autowired
	private CommonDao cmnDao;

	/**
	 * 데이턱 데이터를 조회한다.
	 */
	public Datuk getDatukDataInfo(String datukId) 
	{
		return cmnDao.selectOne("mybatis.mapper.datuk.getDatukDataInfo", YappUtil.makeParamMap("datukId", datukId));
	}

	/**
	 * 데이턱 목록을 조회한다.
	 */
//	public List<Datuk> getDatukDataList(Datuk paramObj) 
//	{
//		return cmnDao.selectList("mybatis.mapper.datuk.getDatukDataList", paramObj);
//	}

	/**
	 * 공유 중인 데이턱을  조회한다.
	 */
	public Datuk getShareDatukData(String cntrNo) 
	{
		return cmnDao.selectOne("mybatis.mapper.datuk.getShareDatukData", YappUtil.makeParamMap("cntrNo", cntrNo));
	}
	
	/**
	 * 데이턱을 생성한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public Datuk insertDatukData(Datuk paramObj) throws Exception
	{
		
		// 데이턱 생성
		cmnDao.insert("mybatis.mapper.datuk.insertDatukData", paramObj);
		// KOS 데이턱 생성
		kosService.createDatuk(paramObj.getCntrNo(), paramObj.getDatukAmt(), paramObj.getDatukId(), paramObj.getMobileNo());
		//kosService.createDatuk(paramObj.getCntrNo(), paramObj.getDatukAmt(), paramObj.getDatukId());

		// 자동 선물하기 여부 조회 후 보너스 데이터 추가.
		//giftService.insertAutoBonusData(paramObj.getCntrNo());
		
		return paramObj;
	}

	/**
	 * 데이턱 수령정보를 추가한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int insertDatukRcvData(DatukRcv paramObj, Datuk datukData, String appVrsn) throws Exception 
	{
		// 수령 정보 추가
		int insCnt = cmnDao.insert("mybatis.mapper.datuk.insertDatukRcvData", paramObj);

		// 데이턱 수령 알림 메시지 추가
		int rcvCnt = paramObj.getDatukRcvNo();
		int rcvDatukAmt = rcvCnt * paramObj.getRcvAmt();
		int rmnDatukAmt = datukData.getDatukAmt() - rcvDatukAmt;

		cmsService.insertNotiMsg(datukData.getCntrNo(), EnumMasterNotiMsg.G0004.getSeq(),
				new String[] { "datukCnt", "rcvDatukAmt", "rmnDatukAmt" },
				new Object[] { rcvCnt, rcvDatukAmt, rmnDatukAmt },
				null, appVrsn);

		// 데이턱 수령 완료 알림 메시지 추가
		if ( rmnDatukAmt <= 0 ) 
		{
			Datuk datukSndData = getDatukDataInfo(datukData.getDatukId());
			cmsService.insertNotiMsg(datukSndData.getCntrNo(), EnumMasterNotiMsg.G0005.getSeq(),
					new String[] { "datukYmd", "datukAmt" },
					new Object[] { YappUtil.getCurDate(datukData.getRegDt(), null), datukData.getDatukAmt() },
					null, appVrsn);
		}

		// 데이터박스 ID 조회
		UserInfo datukUserInfo = userService.getYappUserInfo(datukData.getCntrNo());
		
		// KOS 데이턱 수령
		kosService.receiveDatukData(paramObj.getRcvCntrNo(), datukUserInfo.getDboxId(), datukData.getDatukId());

		// 데이턱 수령 정보 추가
		return insCnt;
	}

	/**
	 * 데이턱 수령정보를 조회한다.
	 */
	public List<DatukRcv> getDatukRcvDataList(String cntrNo, String datukId) 
	{
		Map<String, Object> paramObj = YappUtil.makeParamMap(new String[] { "cntrNo", "datukId" },
				new String[] { cntrNo, datukId });

		return cmnDao.selectList("mybatis.mapper.datuk.getDatukRcvDataList", paramObj);
	}

	/**
	 * 회수대상 데이턱 목록을 조회한다.
	 */
	public List<Datuk> getDatukRtnDataList(String cntrNo, String datukId) 
	{
		Map<String, Object> paramObj = YappUtil.makeParamMap(new String[] { "cntrNo", "datukId" },
				new String[] { cntrNo, datukId });

		return cmnDao.selectList("mybatis.mapper.datuk.getDatukRtnDataList", paramObj);
	}

	/**
	 * 데이턱 데이터를 회수 처리한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int returnDatukData(DatukRtn paramObj, String appVrsn) throws Exception
	{
		// 데이턱 회수 정보 추가
		int insCnt = cmnDao.insert("mybatis.mapper.datuk.returnDatukData", paramObj);
		
		// 회수 알림 데이터 추가
		cmsService.insertNotiMsg(
				paramObj.getCntrNo()
				, EnumMasterNotiMsg.G0006.getSeq()
				, new String[]{"rmnDatukAmt"}
				, new Object[]{paramObj.getRtnAmt()}
				, null, appVrsn);

		UserInfo userInfo = userService.getYappUserInfo(paramObj.getCntrNo());
		
		// 데이턱 회수 정보 데이터 박스 추가
		kosService.returnDatukData(userInfo.getDboxId(), paramObj.getDatukId());
		
		return insCnt;
	}

	/**
	 * 데이턱 수령번호를 조회한다.
	 */
	public int getNextDatukRcvNo(Datuk paramObj) 
	{
		return cmnDao.selectOne("mybatis.mapper.datuk.getNextDatukRcvNo", paramObj);
	}
	

	/**
	 * 데이턱 정보를 조회한다.
	 */
	public DatukDtl getDatukInfo(String cntrNo, String validStYmd, String datukId) throws Exception
	{
		Map<String, Object> paramObj = YappUtil.makeParamMap(new String[] { "cntrNo", "validStYmd", "datukId"},
				new String[] { cntrNo, validStYmd, datukId });
		DatukDtl datukDtlInfo = cmnDao.selectOne("mybatis.mapper.datuk.getDatukInfo", paramObj);
		if ( datukDtlInfo == null ){
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_DUK_DTL"));
		}
		return datukDtlInfo;
	}	

	/**
	 * 데이턱 목록을 조회한다.
	 */
	public List<Datuk> getDatukInfoList(String cntrNo, String validStYmd)
	{
		Map<String, Object> paramObj = YappUtil.makeParamMap(new String[] { "cntrNo", "validStYmd"},
				new String[] { cntrNo, validStYmd });
		return cmnDao.selectList("mybatis.mapper.datuk.getDatukInfoList", paramObj);
	}
}
