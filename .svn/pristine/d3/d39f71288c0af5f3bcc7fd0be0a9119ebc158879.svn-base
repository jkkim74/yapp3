package com.kt.yapp.common.repository;

import java.util.List;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kt.yapp.domain.RsaKeyInfo;

@Repository
public class CommonDao 
{
	@Autowired
	private SqlSessionTemplate sessionTmplt;
	@Autowired
	private DataSource dataSource;

	public <T> T selectOne(String sqlId, Object paramObj) 
	{
		return sessionTmplt.selectOne(sqlId, paramObj);
	}
	
	public <T> List<T> selectList(String sqlId, Object paramObj) 
	{
		return sessionTmplt.selectList(sqlId, paramObj);
	}
	
	public int insert(String sqlId, Object paramObj)
	{
		return sessionTmplt.insert(sqlId, paramObj);
	}
	
	public int update(String sqlId, Object paramObj)
	{
		return sessionTmplt.update(sqlId, paramObj);
	}
	
	public int delete(String sqlId, Object paramObj)
	{
		return sessionTmplt.delete(sqlId, paramObj);
	}

	public RsaKeyInfo getRsaPublicKeyInfo(String sqlId, Object paramObj) {		
		return sessionTmplt.selectOne(sqlId, paramObj);
	}
}
