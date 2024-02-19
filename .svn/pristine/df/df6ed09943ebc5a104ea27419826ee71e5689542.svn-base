package com.kt.yapp.common.repository;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.kt.yapp.domain.UserInfo;

@Repository
public class TestDao
{
	@Autowired
	private SqlSessionTemplate sessionTmplt;
	@Autowired
	private JdbcTemplate tmplt;
	
	public Object selectList(String query) throws Exception
	{
		SqlSession session = sessionTmplt.getSqlSessionFactory().openSession(false);
		try {
		PreparedStatement ps = session.getConnection().prepareStatement(query);
//		ps.executeQuery().
//		session.commit();
		} finally {
			session.close();
		}
		return sessionTmplt.getConnection().prepareStatement(query);
	}
	public List<UserInfo> getUserInfo()
	{
		List<UserInfo> userList = sessionTmplt.selectList("mybatis.user.getUserInfoList");
		return userList;
	}
	
	public void insertUserInfo(UserInfo userInfo)
	{
		sessionTmplt.insert("mybatis.user.insertUserInfo", userInfo);
	}

	public void insertUserInfoBatch()
	{
		Date date = new Date();
		List<Object[]> userInfoList = new ArrayList<>();
		for ( int i = 0; i < 1000000; i++ )
		{
			String cntrNo = "T" + UUID.randomUUID().toString().substring(0, 18);
			String mobileNo = "0100000000";
			
			Object[] args = {cntrNo, mobileNo, "USER_ID", "G0001", "G0001", date, date};
			userInfoList.add(args);
		}
		
		String sql = "INSERT INTO TB_USER (CNTR_NO, MOBILE_NO, USER_ID, JOIN_STATUS, DBOX_STATUS, REG_DT, MOD_DT) "
				+ " values (?, ?, ?, ?, ?, ?, ?)";
		
		tmplt.batchUpdate(sql, userInfoList);
//		sessionTmplt.insert("mybatis.user.insertUserInfo", userInfo);
	}

	public void insertImg(Map<String, Object> paramMap) throws Exception
	{
		SqlSession session = sessionTmplt.getSqlSessionFactory().openSession(false);
		try {
		session.insert("mybatis.mapper.test.insertImg", paramMap);
		session.commit();
		} finally {
			session.close();
		}
//		session.getConnection().setAutoCommit(false);
//		session.getConnection().commit();
	}
	
	public List<Map<String, Object>> getImg(Map<String, Object> paramMap)
	{
		return sessionTmplt.selectList("mybatis.mapper.test.getImg", paramMap);
	}
	
	
}
