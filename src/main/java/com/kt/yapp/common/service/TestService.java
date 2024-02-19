package com.kt.yapp.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kt.yapp.common.repository.CommonDao;
import com.kt.yapp.common.repository.TestDao;
import com.kt.yapp.domain.UserInfo;

@Service
public class TestService {

	@Autowired
	private TestDao dao;
	@Autowired
	private CommonDao cmnDao;
	
	public List<UserInfo>  getUserInfo()
	{
		return dao.getUserInfo();
	}
	
	public void insertUserInfo(UserInfo userInfo)
	{
		dao.insertUserInfo(userInfo);
	}

	public List getTestTableInfo()
	{
		return cmnDao.selectList("getTestTableInfo", null);
	}
	
	public void insertUserInfoBatch()
	{
		dao.insertUserInfoBatch();
	}

}
