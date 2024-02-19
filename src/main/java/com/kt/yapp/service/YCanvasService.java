package com.kt.yapp.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kt.yapp.common.repository.CommonDao;
import com.kt.yapp.domain.EventGiftJoin;
import com.kt.yapp.domain.VoteHistory;
import com.kt.yapp.domain.VoteItem;
import com.kt.yapp.domain.YcanvasItem;

@Service
public class YCanvasService {

	private static final Logger logger = LoggerFactory.getLogger(YCanvasService.class);
	
	@Autowired
	private CommonDao cmnDao;
	
	/**
	 * 230406 Y캔버스 목록을 조회 한다.
	 * @param evtSeq, clsSeq, giftSeq, cntrNo, userId
	 * @return List<YcanvasItem>
	 * @throws Exception
	 */
	public List<YcanvasItem> getYcanvasItemList(int evtSeq, Integer clsSeq, Integer giftSeq, String cntrNo, String userId) throws Exception{
		Map<String, Object> YcanvasMap = new HashMap<String, Object>();
		YcanvasMap.put("evtSeq", evtSeq);
		YcanvasMap.put("clsSeq", clsSeq);
		YcanvasMap.put("giftSeq", giftSeq);
		YcanvasMap.put("cntrNo", cntrNo);
		YcanvasMap.put("userId", userId);

		return cmnDao.selectList("mybatis.mapper.ycanvas.getYCanvasItemList", YcanvasMap);
		
	}
	
	/**
	 * 230406 Y캔버스 이벤트 수강신청 완료 횟수를 조회한다.
	 * @param evtSeq, clsSeq, giftSeq, cntrNo, userId
	 * @return int
	 * @throws Exception
	 */
	public int getYCanvasJoinCount(int evtSeq, String cntrNo, String userId) throws Exception{
		Map<String, Object> YcanvasMap = new HashMap<String, Object>();
		YcanvasMap.put("evtSeq", evtSeq);
		YcanvasMap.put("cntrNo", cntrNo);
		YcanvasMap.put("userId", userId);
		return cmnDao.selectOne("mybatis.mapper.ycanvas.getYCanvasJoinCount", YcanvasMap);
		
	}
	
	/**
	 * 230412 Y캔버스 클래스 날짜가 유효한지 조회한다.
	 * @param evtSeq, clsSeq, giftSeq, cntrNo, userId
	 * @return boolean
	 * @throws Exception
	 */
	public boolean getClassValidCheck(EventGiftJoin paramObj) throws Exception{
		return cmnDao.selectOne("mybatis.mapper.ycanvas.getClassValidCheck", paramObj);
		
	}
	
	/**
	 * 230406 Y캔버스 클래스 신청정보를 수정 한다.
	 * @param YcanvasItem paramObj
	 * @return int
	 * @throws Exception
	 */
	public int updateYCanvasJoin(YcanvasItem paramObj) throws Exception{
		return cmnDao.update("mybatis.mapper.ycanvas.updateYCanvasJoin", paramObj);
		
	}
	
	/**
	 * 230406 Y캔버스 클래스 신청정보를 등록 한다.
	 * @param YcanvasItem paramObj
	 * @return int
	 * @throws Exception
	 */
	public int insertYCanvasJoin(YcanvasItem paramObj) throws Exception{
		int returnVal = 0;
		
		returnVal += cmnDao.update("mybatis.mapper.event.updateClassRewardInfo", paramObj);
		
		returnVal += cmnDao.insert("mybatis.mapper.ycanvas.insertYCanvasJoin", paramObj);
		
		return returnVal;
		
	}
	
}
