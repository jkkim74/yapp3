package com.kt.yapp.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.initech.inisafenet.KeyFixManager;

/**
 * 로열티용 INITSAFE 암호화 유틸
 */
@Component
public class KeyFixUtilForRyt
{
	@Value("${initsafe.royalty.proppath}")
	private String inisafePropPath;
	
	private KeyFixManager keyFixManagerRyt;
	
	public void init() throws Exception
	{
		keyFixManagerRyt = new KeyFixManager(false, "RYT_SVC", inisafePropPath);
	}
	
	public String encode(String str) throws Exception
	{
		if ( keyFixManagerRyt == null ){
			init();
		}
		return new String(keyFixManagerRyt.encrypt(str.getBytes()));
	}
	
	public String decode(String str) throws Exception
	{
		if ( keyFixManagerRyt == null ){
			init();
		}
		return new String(keyFixManagerRyt.decrypt(str.getBytes()));
	}
}