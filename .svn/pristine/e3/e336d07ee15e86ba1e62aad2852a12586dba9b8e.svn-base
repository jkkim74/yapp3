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
import com.kt.yapp.domain.Attend;
import com.kt.yapp.domain.EventMaster;
import com.kt.yapp.util.YappUtil;

/**
 * AttendService.java
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
public class AttendService
{
	private static final Logger logger = LoggerFactory.getLogger(AttendService.class);
	
	@Autowired
	private CommonDao cmnDao;

	public Attend getAttend(int evtSeq, String cntrNo) throws Exception
	{
		EventMaster paramObj = new EventMaster();
		paramObj.setEvtSeq(evtSeq);
		paramObj.setCntrNo(cntrNo);
		return cmnDao.selectOne("mybatis.mapper.attend.getAttend", paramObj);
	}
	
	public int getAttendNowChk(int evtSeq, String cntrNo) throws Exception
	{
		EventMaster paramObj = new EventMaster();
		paramObj.setEvtSeq(evtSeq);
		paramObj.setCntrNo(cntrNo);
		return cmnDao.selectOne("mybatis.mapper.attend.getAttendNowChk", paramObj);
	}
	
	public int getAttendEvtSeq() throws Exception
	{
		return cmnDao.selectOne("mybatis.mapper.attend.getAttendEvtSeq", "");
	}
}
