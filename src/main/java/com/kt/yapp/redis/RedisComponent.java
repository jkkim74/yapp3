package com.kt.yapp.redis;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
public class RedisComponent 
{
	@Resource(name = "redisTemplate")
	private ValueOperations<String, String> valusOps;
	
	@Resource(name="redisTemplate")
	private SetOperations<String, String> setOperations;

	@Resource(name="redisTemplate")
	private HashOperations<String, String, String> redisMap;

	@Resource(name="redisTemplate")
	private ListOperations<String, String> listOperations;

	/** 친구찾기 휴대폰 번호 접두어 */
	public static final String MOBILE_NO_PFX_FRD = "FRD";
	/** 세션저장 휴대폰 번호 접두어 */
	public static final String MOBILE_NO_PFX_SSN = "SSN";

	public static final String KEY1_USER_INFO = "USER_INFO";
	public static final String KEY2_CNTR_NO = "CNTR_NO";
	public static final String KEY2_MOBILE_NO = "MOBILE_NO";
	public static final String KEY2_DBOX_STATUS = "DBOX_STATUS";
	public static final String KEY2_REQ_RCV_YN = "REQ_RCV_YN";

	public void put(String key, String val)
	{
		valusOps.set(key, val);
	}
	
	public void put(String key, String val, long timeout, TimeUnit timeUnit)
	{
		valusOps.set(key, val, timeout, timeUnit);
	}
	
	public String get(String key)
	{
		return valusOps.get(key);
	}
	
	public void put(String key1, String key2, String val)
	{
		redisMap.put(key1, key2, val);
	}
	
	public String get(String key1, String key2)
	{
		Map<String, String> entry = redisMap.entries(key1);
		if ( entry == null )
			return null;
		else
			return entry.get(key2);
	}
	
	public void del(String key1, String key2)
	{
		redisMap.delete(key1, key2);
	}
}
