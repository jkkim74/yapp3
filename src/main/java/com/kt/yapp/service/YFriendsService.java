package com.kt.yapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kt.yapp.common.repository.CommonDao;
import com.kt.yapp.domain.CallingPlan;
import com.kt.yapp.domain.EventCalling;
import com.kt.yapp.domain.EventGift;
import com.kt.yapp.domain.YfriendsMenu;
import com.kt.yapp.domain.RoomChkInfo;
import com.kt.yapp.domain.RoomInfo;
import com.kt.yapp.domain.RoomInvt;
import com.kt.yapp.domain.RoomJoin;
import com.kt.yapp.domain.RoomTitle;
import com.kt.yapp.domain.YfriendsInfo;
import com.kt.yapp.util.YappUtil;

import io.swagger.annotations.ApiModelProperty;

/**
 * yfriendsService.java
 * 
 * @author seungman.yu
 * @since 2019. 7. 29.
 * @version 1.0
 * 
 * Modification Information
 * Mod Date		Modifier		Description
 * ========================================
 * 2019. 7. 29.	min 			yfriends 기능 
 * Copyright (c) 2018 KTDS, Inc. All Rights Reserved
 */
@Service
public class YFriendsService
{
	private static final Logger logger = LoggerFactory.getLogger(YFriendsService.class);
	
	@Autowired
	private CommonDao cmnDao;
	
	/**
	 * 와이프렌즈 메뉴 목록을 조회한다.
	 */
	public List<YfriendsMenu> getYfriendsMenuList(String osTp, String yfriendsMenuYn, String cntrNo, String adminYn)
	{
		YfriendsMenu paramObj = new YfriendsMenu();
		if("G0001".equals(osTp)){
			paramObj.setAndrdUseYn("Y");
		}else if("G0002".equals(osTp)){
			paramObj.setIosUseYn("Y");
		}
		paramObj.setCntrNo(cntrNo);
		paramObj.setYfriendsMenuYn(yfriendsMenuYn);
		paramObj.setAdminYn(adminYn);
		
		return cmnDao.selectList("mybatis.mapper.yfriends.getYfriendsMenuList", paramObj);
	}	
	
	

	/**
	 * Y프렌즈 메뉴 정보를 조회한다.
	 */
	public YfriendsMenu getEventMenu(int evtSeq, String endYn)
	{
		YfriendsMenu paramObj = new YfriendsMenu();
		paramObj.setEvtSeq(evtSeq);
		paramObj.setEndYn(endYn);
		return cmnDao.selectOne("mybatis.mapper.yfriends.getEventMenu", paramObj);
	}	

	
	/**
	 * 전체참여횟수 조회한다.
	 */
	public RoomChkInfo getJoinCount(String cntrNo, int rmSeq)
	{
		RoomChkInfo paramObj = new RoomChkInfo();
		paramObj.setCntrNo(cntrNo);
		paramObj.setRmSeq(rmSeq);
		return cmnDao.selectOne("mybatis.mapper.yfriends.getJoinCount", paramObj);
	}
	
	/**
	 * 전체참여횟수 조회한다.
	 */
	public RoomChkInfo getEventJoinCountOne(String cntrNo, int evtSeq)
	{
		RoomChkInfo paramObj = new RoomChkInfo();
		paramObj.setCntrNo(cntrNo);
		paramObj.setEvtSeq(evtSeq);
		return cmnDao.selectOne("mybatis.mapper.yfriends.getEventJoinCountOne", paramObj);
	}
	
	/**
	 * 이벤트별 참여횟수 조회한다.
	 */
	public List<RoomChkInfo> getEventJoinCount(String cntrNo, int rmSeq)
	{
		RoomChkInfo paramObj = new RoomChkInfo();
		paramObj.setCntrNo(cntrNo);
		paramObj.setRmSeq(rmSeq);
		return cmnDao.selectList("mybatis.mapper.yfriends.getEventJoinCount", paramObj);
	}

	/**
	 * 참여횟수 조회한다.
	 */
	public RoomChkInfo getRoomJoinCount(int rmSeq)
	{
		RoomChkInfo paramObj = new RoomChkInfo();
		paramObj.setRmSeq(rmSeq);
		return cmnDao.selectOne("mybatis.mapper.yfriends.getRoomJoinCount", paramObj);
	}

	/**
	 * Y프렌즈 정보를 조회한다.
	 */
	public YfriendsInfo getYfriendsInfo(int evtSeq)
	{
		YfriendsInfo paramObj = new YfriendsInfo();
		paramObj.setEvtSeq(evtSeq);
		return cmnDao.selectOne("mybatis.mapper.yfriends.getYfriendsInfo", paramObj);
	}	

	/**
	 * 방 정보를 조회한다.
	 */
	public RoomInfo getRoomInfo(int rmSeq)
	{
		RoomInfo paramObj = new RoomInfo();
		paramObj.setRmSeq(rmSeq);
		return cmnDao.selectOne("mybatis.mapper.yfriends.getRoomInfo", paramObj);
	}

	/**
	 * 방 록록을 조회한다.
	 */
	public List<RoomInfo> getRoomInfoList(int evtSeq, String cntrNo)
	{
		RoomInfo paramObj = new RoomInfo();
		paramObj.setEvtSeq(evtSeq);
		paramObj.setJoinCntrNo(cntrNo);
		return cmnDao.selectList("mybatis.mapper.yfriends.getRoomInfoList", paramObj);
	}

	/**
	 * 방참여록록을 조회한다.
	 */
	public List<RoomJoin> getRoomJoinList(int evtSeq, String cntrNo, int rmSeq)
	{
		RoomJoin paramObj = new RoomJoin();
		paramObj.setEvtSeq(evtSeq);
		paramObj.setJoinCntrNo(cntrNo);
		paramObj.setRmSeq(rmSeq);
		return cmnDao.selectList("mybatis.mapper.yfriends.getRoomJoinList", paramObj);
	}

	/**
	 * 방초대 록록을 조회한다.
	 */
	public List<RoomInvt> getRoomChkList(int rmSeq)
	{
		RoomInvt paramObj = new RoomInvt();
		paramObj.setRmSeq(rmSeq);
		return cmnDao.selectList("mybatis.mapper.yfriends.getRoomChkList", paramObj);
	}

	/**
	 * 방참여정보을 조회한다.
	 */
	public RoomJoin getRoomJoin(int rmSeq, String cntrNo)
	{
		RoomJoin paramObj = new RoomJoin();
		paramObj.setRmSeq(rmSeq);
		paramObj.setJoinCntrNo(cntrNo);
		return cmnDao.selectOne("mybatis.mapper.yfriends.getRoomJoin", paramObj);
	}

	/**
	 * 방초대 록록을 조회한다.
	 */
	public List<RoomInvt> getRoomInvtList(String cntrNo, int rmSeq, String osTp, ArrayList<Object> emSeqList)
	{
		RoomInvt paramObj = new RoomInvt();
		paramObj.setRmSeq(rmSeq);
		paramObj.setRcvCntrNo(cntrNo);
		if("G0001".equals(osTp)){
			paramObj.setAndrdUseYn("Y");
		}else if("G0002".equals(osTp)){
			paramObj.setIosUseYn("Y");
		}
		paramObj.setEmSeqList(emSeqList);
		return cmnDao.selectList("mybatis.mapper.yfriends.getRoomInvtList", paramObj);
	}

	/**
	 * 방을 개설한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int insertRoomInfo(RoomInfo paramObj, RoomJoin paramObj2) throws Exception
	{
		int rtnVal = 0;
		rtnVal +=cmnDao.insert("mybatis.mapper.yfriends.insertRoomInfo", paramObj);
		paramObj2.setRmSeq(paramObj.getRmSeq());
		rtnVal +=cmnDao.insert("mybatis.mapper.yfriends.insertRoomJoin", paramObj2);
		return rtnVal;
	}

	/**
	 * 방에 초대한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int insertRoomInvt(RoomInvt paramObj) throws Exception
	{
		return cmnDao.insert("mybatis.mapper.yfriends.insertRoomInvt", paramObj);
	}

	/**
	 * 방에  참여한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int insertRoomJoin(RoomJoin paramObj) throws Exception
	{
		return cmnDao.insert("mybatis.mapper.yfriends.insertRoomJoin", paramObj);
	}

	/**
	 * 방정보를 수정한다
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int updateRoomInfo(RoomInfo paramObj)
	{
		return cmnDao.update("mybatis.mapper.yfriends.updateRoomInfo", paramObj);
	}

	/**
	 * 초대방 정보를 삭제한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int deleteRoomInvt(RoomInvt paramObj)
	{
		return cmnDao.delete("mybatis.mapper.yfriends.deleteRoomInvt", paramObj);
	}

	/**
	 * 참여방 정보를 수정한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int updateRoomJoin(RoomJoin paramObj)
	{
		return cmnDao.update("mybatis.mapper.yfriends.updateRoomJoin", paramObj);
	}
	
	/**
	 * 요금제를 조회한다.
	 */
	public EventCalling getEventCalling(String ppCd, int evtSeq)
	{
		Map<String, Object> paramObj = 
				YappUtil.makeParamMap(new String[]{"ppCd", "evtSeq"}, new Object[]{ppCd, evtSeq});
		return cmnDao.selectOne("mybatis.mapper.yfriends.getEventCalling", paramObj);
	}
	
	/**
	 * 단말정보를 조회한다.
	 */
	public RoomJoin getDeviceCode(int joinSeq)
	{
		return cmnDao.selectOne("mybatis.mapper.yfriends.getDeviceCode", YappUtil.makeParamMap("joinSeq", joinSeq));
	}
	
	/**
	 * Y프렌즈 방가입 여부 조회
	 */
	public int getEventRoomCount(String joinCntrNo, int emSeq)
	{
		Map<String, Object> paramObj = 
				YappUtil.makeParamMap(new String[]{"joinCntrNo", "emSeq"}, new Object[]{joinCntrNo, emSeq});
		return cmnDao.selectOne("mybatis.mapper.yfriends.getEventRoomCount", paramObj);
	}
	
	/**
	 * Y프렌즈 경품 잔여수량 조회 (전체 인원별)
	 */
	public int getAllGiftRemainCount(int evtSeq, Integer joinCnt)
	{
		Map<String, Object> paramObj = 
				YappUtil.makeParamMap(new String[]{"evtSeq", "joinCnt"}, new Object[]{evtSeq, joinCnt});
		int result = cmnDao.selectOne("mybatis.mapper.yfriends.getAllGiftRemainCount", paramObj);
		return result;
	}
	
	/**
	 * Y프렌즈 경품 잔여수량 조회 (다음 인원별)
	 */
	public int getNextGiftRemainCount(int evtSeq, Integer joinCnt)
	{
		Map<String, Object> paramObj = 
				YappUtil.makeParamMap(new String[]{"evtSeq", "joinCnt"}, new Object[]{evtSeq, joinCnt});
		int result = cmnDao.selectOne("mybatis.mapper.yfriends.getNextGiftRemainCount", paramObj);
		return result;
	}
	
	/**
	 * Y프렌즈 경품 잔여수량 조회 (현재 인원별)
	 */
	public int getCurrentGiftRemainCount(int evtSeq, Integer joinCnt)
	{
		Map<String, Object> paramObj = 
				YappUtil.makeParamMap(new String[]{"evtSeq", "joinCnt"}, new Object[]{evtSeq, joinCnt});
		int result = cmnDao.selectOne("mybatis.mapper.yfriends.getCurrentGiftRemainCount", paramObj);
		return result;
	}
	
	/**
	 * Y프렌즈 경품 잔여수량 조회 (단순 잔여)
	 */
	public int getGiftRemainCount(int evtSeq, Integer joinCnt)
	{
		Map<String, Object> paramObj = 
				YappUtil.makeParamMap(new String[]{"evtSeq", "joinCnt"}, new Object[]{evtSeq, joinCnt});
		int result = cmnDao.selectOne("mybatis.mapper.yfriends.getGiftRemainCount", paramObj);
		return result;
	}
	
	/**
	 * Y프렌즈 인원수별 제목,혜택 조회
	 */
	public RoomTitle getMemberJoinTitle(int evtSeq, Integer joinCnt)
	{
		Map<String, Object> paramObj = 
				YappUtil.makeParamMap(new String[]{"evtSeq", "joinCnt"}, new Object[]{evtSeq, joinCnt});
		RoomTitle result = cmnDao.selectOne("mybatis.mapper.yfriends.getMemberJoinTitle", paramObj);
		return result;
	}
	
}
