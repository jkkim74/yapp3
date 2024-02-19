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
import com.kt.yapp.domain.AttendDayCheck;
import com.kt.yapp.domain.EventContent;
import com.kt.yapp.domain.EventGift;
import com.kt.yapp.domain.EventGiftJoin;
import com.kt.yapp.domain.EventLike;
import com.kt.yapp.domain.EventMaster;
import com.kt.yapp.domain.EventReply;
import com.kt.yapp.domain.VoteHistory;
import com.kt.yapp.util.YappUtil;

/**
 * eventService.java
 * 
 * @author kkb
 * @since 2010. 8. 31.
 * @version 1.0
 * 
 * Modification Information
 * Mod Date		Modifier		Description
 * ========================================
 * 2010. 8. 31.	kkb 			최초작성 
 * Copyright (c) 2018 KTDS, Inc. All Rights Reserved
 */
@Service
public class EventService
{
	private static final Logger logger = LoggerFactory.getLogger(EventService.class);
	
	@Autowired
	private CommonDao cmnDao;
	
	/**
	 * 이벤트 메인 목록을 조회한다. 210714 param추가
	 */
	public List<EventMaster> getEventMainList(String adminYn)
	{
		return cmnDao.selectList("mybatis.mapper.event.getEventMainList", YappUtil.makeParamMap("adminYn", adminYn));
	}	
	
	/**
	 * 이벤트 상세를 조회한다.
	 */
	public EventMaster getEventDetailMaster(int evtSeq)
	{
		EventMaster paramObj = new EventMaster();
		paramObj.setEvtSeq(evtSeq);
		return cmnDao.selectOne("mybatis.mapper.event.getEventDetailMaster", paramObj);
	}
	
	/**
	 * 응모권 이벤트 회차를 조회한다.
	 */
	public EventMaster getTicketEventDetail(String cntrNo)
	{
		EventMaster paramObj = new EventMaster();
		paramObj.setCntrNo(cntrNo);
		return cmnDao.selectOne("mybatis.mapper.event.getTicketEventDetail", paramObj);
	}
	
	
	/**
	 * 이벤트 상세를 조회한다.
	 */
	public EventMaster getEventDetailMaster(int evtSeq, String osTp)
	{
		EventMaster paramObj = new EventMaster();
		paramObj.setEvtSeq(evtSeq);
		paramObj.setOsTp(osTp);
		return cmnDao.selectOne("mybatis.mapper.event.getEventDetailMaster", paramObj);
	}
	
	/**
	 * 이벤트 상세 내용을 조회한다.
	 */
	public List<EventContent> getEventDetailContentList(int evtSeq)
	{
		EventMaster paramObj = new EventMaster();
		paramObj.setEvtSeq(evtSeq);
		return cmnDao.selectList("mybatis.mapper.event.getEventDetailContentList", paramObj);
	}
	
	/**
	 * 이벤트 상세 상품을 조회한다.
	 */
	public List<EventGift> getEventDetailGiftList(int evtSeq, Integer giftSeq)
	{
		EventGift paramObj = new EventGift();
		paramObj.setEvtSeq(evtSeq);
		if(giftSeq!=null){
			paramObj.setGiftSeq(giftSeq);
		}
		return cmnDao.selectList("mybatis.mapper.event.getEventDetailGiftList", paramObj);
	}
	
	/**
	 * 이벤트 상세 상품(오프라인)을 조회한다.
	 */
	public List<EventGift> getEventDetailOfflineGiftList(int evtSeq)
	{
		EventGift paramObj = new EventGift();
		paramObj.setEvtSeq(evtSeq);
		return cmnDao.selectList("mybatis.mapper.event.getEventDetailOfflineGiftList", paramObj);
	}
	
	/**
	 * 이벤트 상세 상품(온라인)을 조회한다.
	 */
	public List<EventGift> getEventDetailOnlineGiftList(int evtSeq)
	{
		EventGift paramObj = new EventGift();
		paramObj.setEvtSeq(evtSeq);
		return cmnDao.selectList("mybatis.mapper.event.getEventDetailOnlineGiftList", paramObj);
	}
	
	/**
	 * 이벤트 상세 상품(데이터 상품)을 조회한다.
	 */
	public List<EventGift> getEventDetailDataCpGiftList(int evtSeq)
	{
		EventGift paramObj = new EventGift();
		paramObj.setEvtSeq(evtSeq);
		return cmnDao.selectList("mybatis.mapper.event.getEventDetailDataCpGiftList", paramObj);
	}
	
	/**
	 * 이벤트 이벤트 경품 지급완료 횟수를 조회한다.
	 */
	public int getEventJoinCount(int evtSeq, String cntrNo, String userId)
	{
		EventMaster paramObj = new EventMaster();
		paramObj.setEvtSeq(evtSeq);
		paramObj.setCntrNo(cntrNo);
		paramObj.setUserId(userId);
		Integer cnt = cmnDao.selectOne("mybatis.mapper.event.getEventJoinCount", paramObj);
		if(cnt == null){
			cnt = 0;
		}
		return cnt;
	}
	

	/**
	 * 이벤트 참여 횟수를 조회한다.(출석체크)
	 */
	public int getAttendEventJoinCount(int evtSeq, String cntrNo, String userId)
	{
		EventMaster paramObj = new EventMaster();
		paramObj.setEvtSeq(evtSeq);
		paramObj.setCntrNo(cntrNo);
		paramObj.setUserId(userId);
		Integer cnt = cmnDao.selectOne("mybatis.mapper.event.getAttendEventJoinCount", paramObj);
		if(cnt == null){
			cnt = 0;
		}
		return cnt;
	}
	
	/**
	 * 이벤트 참여 횟수를 조회한다.(응모권 출석체크)
	 */
	public int getAttendTicketEventJoinCount(int evtSeq, String cntrNo, String userId)
	{
		EventMaster paramObj = new EventMaster();
		paramObj.setEvtSeq(evtSeq);
		paramObj.setCntrNo(cntrNo);
		paramObj.setUserId(userId);
		Integer cnt = cmnDao.selectOne("mybatis.mapper.event.getAttendTicketEventJoinCount", paramObj);
		if(cnt == null){
			cnt = 0;
		}
		return cnt;
	}
	
	/**
	 * 이벤트 참여 횟수를 조회한다.(랜덤제공일때)
	 */
	public int getEventRandomJoinCount(int evtSeq, String cntrNo, String userId)
	{
		EventMaster paramObj = new EventMaster();
		paramObj.setEvtSeq(evtSeq);
		paramObj.setCntrNo(cntrNo);
		paramObj.setUserId(userId);
		Integer cnt = cmnDao.selectOne("mybatis.mapper.event.getEventRandomJoinCount", paramObj);
		if(cnt == null){
			cnt = 0;
		}
		return cnt;
	}
	
	/**
	 * 이벤트 경품 잔여수량을 조회한다.
	 */
	public int getEventRemainGiftCount(int issueSeq)
	{
		EventGiftJoin paramObj = new EventGiftJoin();
		paramObj.setIssueSeq(issueSeq);
		Integer cnt = cmnDao.selectOne("mybatis.mapper.event.getEventRemainGiftCount", paramObj);
		if(cnt == null){
			cnt = 0;
		}
		return cnt;
	}
	
	/**
	 * 이벤트 토탈 경품 잔여수량을 조회한다.
	 */
	public int getEventRemainGiftTotCount(int issueSeq)
	{
		EventGiftJoin paramObj = new EventGiftJoin();
		paramObj.setIssueSeq(issueSeq);
		Integer cnt = cmnDao.selectOne("mybatis.mapper.event.getEventRemainGiftTotCount", paramObj);
		if(cnt == null){
			cnt = 0;
		}
		return cnt;
	}
	
	/**
	 * 선착순 / 이벤트 토탈 경품 잔여수량을 조회한다. 2023.06.14 by jk
	 */
	public int getEventRemainGiftTotCountByOrder(int evtSeq)
	{
		EventGiftJoin paramObj = new EventGiftJoin();
		paramObj.setEvtSeq(evtSeq);
		Integer cnt = cmnDao.selectOne("mybatis.mapper.event.getEventRemainGiftTotCountByOrder", paramObj);
		if(cnt == null){
			cnt = 0;
		}
		return cnt;
	}
	
	/**
	 *선착순 / 이벤트 토탈 경품 잔여수량을 조회한다. 2023.06.14 by jk
	 */
	public List<Integer> getEventRemainGiftTotCountByIssueSeq(List<Integer> issueSeqList)
	{
		List<Integer> resultList = cmnDao.selectList("mybatis.mapper.event.getEventRemainGiftTotCountByIssueSeq", issueSeqList);
		return resultList;
		
	}
	
	/** 210909
	 * 리워드 시퀀스를 조회한다.
	 */
//	public int getMinRewardSeq(int giftSeq)
//	{
//		logger.info("service getMinRewardSeq getMinRewardSeq : "+giftSeq);
//		EventGiftJoin paramObj = new EventGiftJoin();
//		paramObj.setGiftSeq(giftSeq);
//		
//		logger.info("service getMinRewardSeq paramObj : "+paramObj);
//
//		Integer cnt = cmnDao.selectOne("mybatis.mapper.event.getMinRewardSeq",paramObj);
//		logger.info("service cnt : "+cnt);
//
//		if(cnt == null){
//			cnt = 0;
//		}
//		return cnt;
//	}
	
	/** 210913
	 * 이벤트 리워드를 발행한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int updateEventRewardInfo(EventGiftJoin paramObj)
	{
		int returnVal = 0;
		returnVal += cmnDao.update("mybatis.mapper.event.updateEventRewardInfo", paramObj);

		returnVal += cmnDao.update("mybatis.mapper.event.updateGiftIssueInfo", paramObj);
		
		return returnVal;
	}
	
	/** 230406
	 * Y캔버스 이벤트 리워드 발행했던 부분을 취소한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int updateMinusGiftIssueInfo(EventGiftJoin paramObj)
	{
		return cmnDao.update("mybatis.mapper.event.updateMinusGiftIssueInfo", paramObj);
	}
	
	/**
	 * 이벤트 리워드를 발행한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int updateRewardInfo(EventGiftJoin paramObj)
	{
		return cmnDao.update("mybatis.mapper.event.updateRewardInfo", paramObj);
	}
	
	/**
	 * 이벤트 상품 발행정보를 업데이트한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int updateGiftIssueInfo(EventGiftJoin paramObj)
	{
		return cmnDao.update("mybatis.mapper.event.updateGiftIssueInfo", paramObj);
	}
	
	/**
	 * 이벤트 참여 정보를 등록한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int insertEntryInfo(EventGiftJoin paramObj)
	{
		return cmnDao.insert("mybatis.mapper.event.insertEntryInfo", paramObj);
	}
	
	/**
	 * 나의 이벤트 경품 정보를 조회한다.
	 */
	public EventGift getMyEventGiftInfo(EventGiftJoin paramObj)
	{
		return cmnDao.selectOne("mybatis.mapper.event.getMyEventGiftInfo", paramObj);
	}
	
	/**
	 * 나의 이벤트 데이터 경품 정보를 조회한다.
	 */
	public EventGift getMyEventGiftDataCpInfo(EventGiftJoin paramObj)
	{
		return cmnDao.selectOne("mybatis.mapper.event.getMyEventGiftDataCpInfo", paramObj);
	}
	
	/**
	 * 나의 이벤트 경품 정보를 조회한다.(출석)
	 */
	public EventGift getMyEventAttendGiftInfo(EventGiftJoin paramObj)
	{
		return cmnDao.selectOne("mybatis.mapper.event.getMyEventAttendGiftInfo", paramObj);
	}
	
	/**
	 * 나의 이벤트 응모권 경품 정보를 조회한다.
	 */
	public EventGift getMyEventTicketGiftInfo(EventGiftJoin paramObj)
	{
		return cmnDao.selectOne("mybatis.mapper.event.getMyEventTicketGiftInfo", paramObj);
	}
	
	/**
	 * 나의 이벤트 응모권 경품 정보를 조회한다.(출석)
	 */
	public EventGift getMyEventAttendTicketGiftInfo(EventGiftJoin paramObj)
	{
		return cmnDao.selectOne("mybatis.mapper.event.getMyEventAttendTicketGiftInfo", paramObj);
	}
	
	/**
	 * 이벤트 출석내역을 조회한다.
	 */
	public List<EventGift> getMyEventAttendList(int evtSeq, String cntrNo, String userId)
	{
		EventGift paramObj = new EventGift();
		paramObj.setEvtSeq(evtSeq);
		paramObj.setCntrNo(cntrNo);
		paramObj.setUserId(userId);
		return cmnDao.selectList("mybatis.mapper.event.getMyEventAttendList", paramObj);
	}
	
	/**
	 * 210819 응모권 출석 이벤트 출석내역을 조회한다.
	 */
	public List<EventGift> getMyTicketEventAttendList(int evtSeq, String cntrNo, String userId, String startDt)
	{
		//응모권 출석체크 210809 당월 일자 구하기 9월배포
		Calendar currentCalendar = Calendar.getInstance();
//		int year = currentCalendar.get(Calendar.YEAR); 
//		int month = currentCalendar.get(Calendar.MONTH); 
		
		int year = Integer.valueOf(startDt.substring(0, 4));
		int month = Integer.valueOf(startDt.substring(5, 7));
		month = month -1;

		currentCalendar.set(year, month, 1);
		
		int endDay = currentCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		List<Integer> dayList = new ArrayList<Integer>();
		for(int i = 0; i < endDay; i++){
			dayList.add(i+1);
		}
		//응모권 출석체크 210809 당월 일자 구하기 9월배포

		EventGift paramObj = new EventGift();
		paramObj.setEvtSeq(evtSeq);
		paramObj.setCntrNo(cntrNo);
		paramObj.setUserId(userId);
		
		//응모권 출석체크 210809 당월 일자 추가 9월배포
		paramObj.setDayList(dayList);
		//응모권 출석체크 210809 당월 일자 추가 9월배포

		return cmnDao.selectList("mybatis.mapper.event.getMyTicketEventAttendList", paramObj);
	}
	
	/**
	 * 당일 이벤트 출석정보 체크한다.
	 */
	public int getEventAttendNowChk(int evtSeq, String cntrNo, String userId)
	{
		EventMaster paramObj = new EventMaster();
		paramObj.setEvtSeq(evtSeq);
		paramObj.setCntrNo(cntrNo);
		paramObj.setUserId(userId);
		return cmnDao.selectOne("mybatis.mapper.event.getEventAttendNowChk", paramObj);
	}
	
	/**
	 * 이벤트 참여 정보를 등록한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int insertAttendDayCheck(AttendDayCheck paramObj)
	{
		return cmnDao.insert("mybatis.mapper.event.insertAttendDayCheck", paramObj);
	}
	
	/**
	 * 이벤트 상세 상품(출석체크)을 조회한다.
	 */
	public List<EventGift> getEventDetailAttendGiftList(int evtSeq, int giftDay)
	{
		EventGift paramObj = new EventGift();
		paramObj.setEvtSeq(evtSeq);
		paramObj.setGiftDay(giftDay);
		return cmnDao.selectList("mybatis.mapper.event.getEventDetailAttendGiftList", paramObj);
	}
	
	/** 210825
	 * 이벤트 상품들(응모권출석/출석체크)을 조회한다.
	 */
	public List<EventGift> getAttendEventAllGiftList(int evtSeq)
	{
		EventGift paramObj = new EventGift();
		paramObj.setEvtSeq(evtSeq);
		return cmnDao.selectList("mybatis.mapper.event.getAttendEventAllGiftList", paramObj);
	}
	
	/**
	 * 이벤트 참여 횟수를 조회한다.
	 */
	public int getRewardCount(int issueSeq, String cntrNo, String userId)
	{
		EventGiftJoin paramObj = new EventGiftJoin();
		paramObj.setIssueSeq(issueSeq);
		paramObj.setCntrNo(cntrNo);
		paramObj.setUserId(userId);
		Integer cnt = cmnDao.selectOne("mybatis.mapper.event.getRewardCount", paramObj);
		if(cnt == null){
			cnt = 0;
		}
		return cnt;
	}
	
	/**
	 * 이벤트 참여 횟수를 조회한다.
	 */
	public int getAttendRewardSeq(EventGiftJoin paramObj)
	{
		Integer cnt = cmnDao.selectOne("mybatis.mapper.event.getAttendRewardSeq", paramObj);
		if(cnt == null){
			cnt = 0;
		}
		return cnt;
	}

	/**
	 * 이벤트 배너 이미지를 랜덤으로 조회한다.
	 */
	public EventMaster getEventBanner() {
		
		return cmnDao.selectOne("mybatis.mapper.event.getEventBanner", "");
	}
	
	/**
	 * 이벤트 리워드를 일괄 발행한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int updateAllRewardInfo(EventGiftJoin paramObj)
	{
		return cmnDao.update("mybatis.mapper.event.updateAllRewardInfo", paramObj);
	}
	
	/**
	 * 220926 투표유형을 조회한다.
	 */
	public String getVoteType(int evtSeq){
		
		EventMaster paramObj = new EventMaster();
		paramObj.setEvtSeq(evtSeq);
		
		return cmnDao.selectOne("mybatis.mapper.vote.getVoteType", paramObj);
	}
	
	
	/**
	 * 230106 투표율 표시유무를 조회한다
	 */
	public String getVoteRateYn(int evtSeq){
		
		EventMaster paramObj = new EventMaster();
		paramObj.setEvtSeq(evtSeq);
		
		return cmnDao.selectOne("mybatis.mapper.vote.getVoteRateYn", paramObj);
	}
	
	/**
	 * 210610 사용자의 해당 이벤트 투표 참여 여부를 조회한다.
	 */
	public VoteHistory getVoteChk(int evtSeq, String cntrNo, String userId){
		
		EventMaster paramObj = new EventMaster();
		
		paramObj.setEvtSeq(evtSeq);
		paramObj.setCntrNo(cntrNo);
		paramObj.setUserId(userId);
		
		VoteHistory voteHis = cmnDao.selectOne("mybatis.mapper.event.getLastedtVoteHistory", paramObj);
		
		return voteHis;
	}
	
	/**
	 * 210610 사용자의 투표 정보를 등록한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int insertVoteHistory(VoteHistory paramObj)
	{
		return cmnDao.insert("mybatis.mapper.event.insertVoteHistory", paramObj);
	}
	
	/**
	 * 210615 사용자의 투표이력을 조회한다.
	 */
	public VoteHistory getVoteHistory(int evtSeq, String cntrNo, String userId) {
		EventMaster paramObj = new EventMaster();
		paramObj.setEvtSeq(evtSeq);
		paramObj.setCntrNo(cntrNo);
		paramObj.setUserId(userId);
		return cmnDao.selectOne("mybatis.mapper.event.getVoteHistory", paramObj);
	}
	
	/**
	 * 221113 댓글을 등록한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int insertEventReply(EventReply paramObj)
	{
		return cmnDao.insert("mybatis.mapper.event.insertReplyData", paramObj);
	}
	
	/**
	 * 230411 해당 댓글이 사용자의 것인지 확인한다(위변조 방지)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public EventReply getEventReplyMine(EventReply paramObj)
	{
		return cmnDao.selectOne("mybatis.mapper.event.getEventReplyMine", paramObj);
	}
	
	/**
	 * 221117 댓글을 수정한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int updateEventReply(EventReply paramObj)
	{
		return cmnDao.update("mybatis.mapper.event.updateReplyData", paramObj);
	}
	
	/**
	 * 221113 댓글 목록을 조회한다.
	 */
	public List<EventReply> getEventReplyList(int evtSeq, int endSeq, String cntrNo, String userId, int limit)
	{
		EventReply paramObj = new EventReply();
		paramObj.setEvtSeq(evtSeq);
		paramObj.setEndSeq(endSeq);
		paramObj.setCntrNo(cntrNo);
		paramObj.setUserId(userId);
		paramObj.setLimit(limit);
		return cmnDao.selectList("mybatis.mapper.event.getReplyList", paramObj);
	}	
	
	/**
	 * 221113 댓글 갯수를 조회한다.
	 */
	public int getEventReplyCnt(int evtSeq)
	{
		EventReply paramObj = new EventReply();
		paramObj.setEvtSeq(evtSeq);
		return cmnDao.selectOne("mybatis.mapper.event.getReplyCount", paramObj);
	}	
	
	/**
	 * 221119 좋아요 갯수를 조회한다.
	 */
	public int getEventLikeCnt(int evtSeq)
	{
		EventLike paramObj = new EventLike();
		paramObj.setEvtSeq(evtSeq);
		return cmnDao.selectOne("mybatis.mapper.event.getLikeCount", paramObj);
	}	
	
	/**
	 * 221119 사용자 좋아요 정보를 조회한다.
	 */
	public EventLike getUserLikeInfo(int evtSeq, String cntrNo, String userId)
	{
		EventLike paramObj = new EventLike();
		paramObj.setEvtSeq(evtSeq);
		paramObj.setCntrNo(cntrNo);
		paramObj.setUserId(userId);
		return cmnDao.selectOne("mybatis.mapper.event.getUserLikeInfo", paramObj);
	}
	
	/**
	 * 221120 사용자의 좋아요 정보를 등록한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int insertLikeData(EventLike paramObj)
	{
		return cmnDao.insert("mybatis.mapper.event.insertLikeData", paramObj);
	}
	
	/**
	 * 221120 좋아요 정보를 수정한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int updateEventLike(EventLike paramObj)
	{
		return cmnDao.update("mybatis.mapper.event.updateLikeData", paramObj);
	}
	
	/**
	 * 230309 데이터 상품에 data_rew_seq를 가져온다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int selectEventDataCp(int giftSeq, int issueSeq)
	{
		Map<String, Object> paramObj = 
				YappUtil.makeParamMap(new String[]{"giftSeq", "issueSeq"}, new Object[]{giftSeq, issueSeq});
		int iRet = cmnDao.selectOne("mybatis.mapper.event.selectEventDataCp", paramObj);
		return iRet;
	}
	
	/**
	 * 230309 데이터 상품에 cntr_no를 수정한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int updateEventDataCp(int dataRewSeq, int issueSeq, String cntrNo)
	{
		Map paramObj = 
				YappUtil.makeParamMap(new String[]{"dataRewSeq","issueSeq","cntrNo"}, new Object[]{dataRewSeq, issueSeq, cntrNo});
		int returnVal =+ cmnDao.update("mybatis.mapper.event.updateEventDataCp", paramObj);
		returnVal += cmnDao.update("mybatis.mapper.event.updateGiftIssueInfo", paramObj);
		
		return returnVal;
	}

	/**
	 * 230320 운영자 댓글을 조회한다
	 */
	public List<EventReply> getEventAdminReplyList(int evtSeq) {
		EventReply paramObj = new EventReply();
		paramObj.setEvtSeq(evtSeq);
		return cmnDao.selectList("mybatis.mapper.event.getEventAdminReplyList", paramObj);
	}
}
