package com.kt.yapp.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.initech.inisafenet.KeyFixManager;

/**
 * INITSAFE 암호화 유틸
 */
@Component
public class KeyFixUtil
{
	@Value("${initsafe.proppath}")
	private String inisafePropPath;
	
	private KeyFixManager keyFixManager;
	
	public void init() throws Exception
	{
		keyFixManager = new KeyFixManager(false, "GNL_SVC", inisafePropPath);
	}
	
	public String encode(String str) throws Exception
	{
		if ( keyFixManager == null ){
			init();
		}
		return new String(keyFixManager.encrypt(str.getBytes()));
	}
	
	public String decode(String str) throws Exception
	{
		if ( keyFixManager == null ){
			init();
		}
		return new String(keyFixManager.decrypt(str.getBytes()));
	}

}