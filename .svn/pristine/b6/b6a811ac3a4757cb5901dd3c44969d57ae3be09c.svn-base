package com.kt.yapp.em;

import com.kt.yapp.util.YappUtil;

/**
 * 보너스 타입보너스 타입(G0001: 프로모션, G0002: 보너스, G0003: 보너스(최초선물가입))
 */
public enum EnumBonusTp 
{
	/** 프로모션 */
	G0001("G0001", "S")
	/** 보너스 */
	, G0002("G0002", "B")
	/** 보너스 */
	, G0003("G0003", "B")
	;
	
	private String key;
	private String val;
	
	private EnumBonusTp(String key, String val) {
		this.key = key;
		this.val = val;
	}
	
	public String getKey() {
		return this.key;
	}
	
	public String getValue() {
		return this.val;
	}
	
	public static EnumBonusTp getObj(String key)
	{
		for ( int i = 0; i < values().length; i++ )
		{
			EnumBonusTp bonusTp = values()[i];
			if ( YappUtil.isEq(key, bonusTp.getKey()) )
				return bonusTp;
		}
		return null;
	}
}
