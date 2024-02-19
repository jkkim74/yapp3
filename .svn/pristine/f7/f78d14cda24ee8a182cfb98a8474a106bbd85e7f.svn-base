package com.kt.yapp.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kt.yapp.common.repository.CommonDao;
import com.kt.yapp.domain.VoteHistory;
import com.kt.yapp.domain.VoteItem;

@Service
public class VoteService {

	private static final Logger logger = LoggerFactory.getLogger(VoteService.class);
	
	@Autowired
	private CommonDao cmnDao;
	
	/**
	 * 210610 투표대상 목록을 조회 한다.
	 * @param voteSeq
	 * @return
	 * @throws Exception
	 */
	public List<VoteItem> getVoteItemList(int voteSeq) throws Exception{
		return cmnDao.selectList("mybatis.mapper.vote.getVoteItemList",voteSeq);
		
	}
	
	/**
	 * 210628 투표 대상의 투표건수를 업데이트한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int updateVoteCnt(VoteHistory paramObj)
	{
		return cmnDao.update("mybatis.mapper.vote.updateVoteCnt", paramObj);
	}
	
}
