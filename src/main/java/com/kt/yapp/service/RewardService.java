package com.kt.yapp.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kt.yapp.common.repository.CommonDao;
import com.kt.yapp.domain.DataRewardData;
import com.kt.yapp.domain.RewardData;
import com.kt.yapp.domain.TicketRewardData;
import com.kt.yapp.util.YappUtil;

/**
 * RewardService.java
 * 
 * @author kkb
 * @since 2020. 08. 21.
 * @version 1.0
 * 
 * Modification Information
 * Mod Date		Modifier		Description
 * ========================================
 *
 * Copyright (c) 2018 KTDS, Inc. All Rights Reserved
 */
@Service
public class RewardService
{
	private static final Logger logger = LoggerFactory.getLogger(RewardService.class);
	
	@Autowired
	private CommonDao cmnDao;

	public int getRewardCnt(String cntrNo, String userId) throws Exception
	{
		return cmnDao.selectOne("mybatis.mapper.reward.getRewardCount", YappUtil.makeParamMap(new String[]{"cntrNo", "userId"}, new Object[]{cntrNo, userId}));
	}

	
	public List<RewardData> getRewardList(String cntrNo, String userId) throws Exception {
		return cmnDao.selectList("mybatis.mapper.reward.getRewardList", YappUtil.makeParamMap(new String[]{"cntrNo", "userId"}, new Object[]{cntrNo, userId}));
	}


	public RewardData getRewardInfo(String cntrNo, String userId, int rewSeq) {
		Map<String, Object> paramObj = YappUtil.makeParamMap(new String[]{"cntrNo", "userId", "rewSeq"}, new Object[]{cntrNo, userId, rewSeq});
		return cmnDao.selectOne("mybatis.mapper.reward.getRewardInfo", paramObj);
	}
	
	public List<TicketRewardData> getTicketRewardList(String cntrNo, String userId) throws Exception {
		return cmnDao.selectList("mybatis.mapper.reward.getTicketRewardList", YappUtil.makeParamMap(new String[]{"cntrNo", "userId"}, new Object[]{cntrNo, userId}));
	}
	
	public List<TicketRewardData> getTicketRewardList(String cntrNo, String userId, String lnbYn) throws Exception {
		Map<String, Object> paramObj = YappUtil.makeParamMap(new String[]{"cntrNo", "userId", "lnbYn"}, new Object[]{cntrNo, userId, lnbYn});
		return cmnDao.selectList("mybatis.mapper.reward.getTicketRewardList", paramObj);
	}
	
	public List<DataRewardData> getDataRewardList(String cntrNo) throws Exception {
		return cmnDao.selectList("mybatis.mapper.reward.getDataRewardList", YappUtil.makeParamMap("cntrNo", cntrNo));
	}
	
	public List<RewardData> getClassRewardList(String cntrNo, String userId) throws Exception {
		return cmnDao.selectList("mybatis.mapper.reward.getClassRewardList", YappUtil.makeParamMap(new String[]{"cntrNo", "userId"}, new Object[]{cntrNo, userId}));
	}
	
	public RewardData getClassRewardInfo(String cntrNo, String userId, int rewSeq) {
		Map<String, Object> paramObj = YappUtil.makeParamMap(new String[]{"cntrNo", "userId", "rewSeq"}, new Object[]{cntrNo, userId, rewSeq});
		return cmnDao.selectOne("mybatis.mapper.reward.getClassRewardInfo", paramObj);
	}
	
	public int deleteClassReward(int issueSeq, int joinSeq, int rewSeq) {
		int resultVal = 0;
		Map<String, Object> paramObj = YappUtil.makeParamMap(new String[]{"issueSeq","joinSeq","rewSeq"}, new Object[]{issueSeq, joinSeq, rewSeq});
		//상품 차감
		resultVal += cmnDao.update("mybatis.mapper.event.updateMinusGiftIssueInfo", paramObj);
		//수강 참여 취소
		resultVal += cmnDao.update("mybatis.mapper.ycanvas.updateYCanvasJoin", paramObj);
		//수강 리워드 변경
		resultVal += cmnDao.update("mybatis.mapper.ycanvas.updateYCanvasReward", paramObj);
		
		return resultVal;
	}
	
	/**
	 * 211210 데이터 리워드 사용여부 조회
	 * */
	public DataRewardData getDataRewardDetail(String cntrNo, int dataRewSeq) {
		Map<String, Object> paramObj = YappUtil.makeParamMap(new String[]{"cntrNo", "dataRewSeq"}, new Object[]{cntrNo, dataRewSeq});
		return cmnDao.selectOne("mybatis.mapper.reward.getDataRewardDetail", paramObj);
	}
	
	/**
	 * 211210 데이터쿠폰 KOS 데이터지급여부 UPDATE.
	 * @param paramObj
	 * @return
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int updateDataGiveInfo(DataRewardData paramObj){
		return cmnDao.update("mybatis.mapper.reward.updateDataGiveInfo", paramObj);
	}
}
