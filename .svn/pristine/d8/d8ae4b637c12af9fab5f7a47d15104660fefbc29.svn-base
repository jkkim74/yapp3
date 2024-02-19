package com.kt.yapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kt.yapp.common.repository.CommonDao;
import com.kt.yapp.domain.Coupon;
import com.kt.yapp.domain.GrpCode;
import com.kt.yapp.domain.VaSvcAppl;
import com.kt.yapp.domain.VasItem;
import com.kt.yapp.em.EnumVasActionTp;
import com.kt.yapp.em.EnumVasCd;

/**
 * 부가서비스 처리 Service
 */
@Service
public class VasService
{
	@Autowired
	private ShubService shubService;
	@Autowired
	private MemPointService memPointService;
	@Autowired
	private KosService kosService;
	
	@Autowired
	private CommonDao cmnDao;

	/**
	 * 부가 서비스 상품 정보를 조회한다.
	 */
	public VasItem getVasItemInfo(VasItem paramObj)
	{
		return cmnDao.selectOne("mybatis.mapper.vas.getVasList", paramObj);
	}

	/**
	 * 부가 서비스 상품 목록 정보를 조회한다.
	 */
	public List<VasItem> getVasItemList(VasItem paramObj)
	{
		return cmnDao.selectList("mybatis.mapper.vas.getVasList", paramObj);
	}
	
	/**
	 * 부가 서비스 이력 목록을 조회한다.
	 */
	public List<VasItem> getVasHistList(VasItem paramObj)
	{
		return cmnDao.selectList("mybatis.mapper.vas.getVasHistList", paramObj);
	}
	
	/**
	 * 부가 서비스를 설정한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void applyVasService(VaSvcAppl vaSvcAppl, VasItem paramInsHist) throws Exception
	{
		// 이력추가
		cmnDao.insert("mybatis.mapper.vas.insertVasHistList", paramInsHist);

		// 부가 서비스를 설정
		shubService.callFn750(vaSvcAppl);
	}
	
	/**
	 * 부가 서비스를 설정한다.(설정 후 멤버십 포인트 차감)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void applyVasService(VaSvcAppl vaSvcAppl, VasItem paramInsHist, int sbtrtMemPoint) throws Exception
	{
		// 이력추가
		cmnDao.insert("mybatis.mapper.vas.insertVasHistList", paramInsHist);
		
		// TODO 멤버십 차감

		// 부가 서비스를 설정
		shubService.callFn750(vaSvcAppl);
	}
	
	/**
	 * 부가 서비스를 설정한다.(룰렛)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void applyVasService(VaSvcAppl vaSvcAppl, VasItem paramInsHist, String cntrNo, String memId, String pointPdCd, int sbtrtMemPoint) throws Exception
	{
		// 이력추가
		cmnDao.insert("mybatis.mapper.vas.insertVasHistList", paramInsHist);
		
		// 부가 서비스를 설정
		shubService.callFn750(vaSvcAppl);
		
		// TODO 멤버십 차감
		memPointService.useMemPoint(cntrNo, memId, pointPdCd, sbtrtMemPoint);
	}

	/**
	 * 부가 서비스를 설정한다.(마이타임플랜)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void applyVasTimeService(VaSvcAppl vaSvcAppl, VasItem paramInsHist, String cntrNo, String vasItemCd) throws Exception
	{
		// 이력추가
		cmnDao.insert("mybatis.mapper.vas.insertVasHistList", paramInsHist);

		// 부가 서비스를 설정
		kosService.setProcessProdStoreBas(cntrNo, vasItemCd);
	}

	/**
	 * 부가 서비스를 설정한다.(반값팩, 장기혜택쿠폰)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void applyVasCpnService(VaSvcAppl vaSvcAppl, VasItem paramInsHist, Coupon cpn) throws Exception
	{
		// 이력추가
		cmnDao.insert("mybatis.mapper.vas.insertVasHistList", paramInsHist);

		// 부가 서비스를 설정
		shubService.callFn495(cpn);
	}
	
	/**
	 * 부가 서비스 룰렛 당첨정보를 조회한다.
	 */
	public GrpCode getRouletteList(VasItem paramObj)
	{
		return cmnDao.selectOne("mybatis.mapper.vas.getRouletteList", paramObj);
	}
	
//	/**
//	 * 부가 서비스 이력 정보를 추가한다.
//	 */
//	public int insertVasHistList(VasItem paramObj)
//	{
//		return cmnDao.insert("mybatis.mapper.vas.insertVasHistList", paramObj);
//	}
	
}
