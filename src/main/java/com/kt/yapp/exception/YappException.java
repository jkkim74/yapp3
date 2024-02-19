package com.kt.yapp.exception;

import com.kt.yapp.em.EnumRsltCd;

import lombok.Getter;

/**
 * yapp exception
 */
public class YappException extends Exception
{
	private static final long serialVersionUID = -6968015521343236611L;
	@Getter
	private String msgCd = EnumRsltCd.C999.getRsltCd();
	@Getter
	private String msgTypeCode = "SYSTEM_ERROR";
	@Getter
	private String msgDetail = "";
	@Getter
	private String msgKey = "";

	public YappException() {
		super();
	}
	
	public YappException(String msgTypeCode, String msg) {
		super(msg);
		this.msgTypeCode = msgTypeCode;
	}
	
	public YappException(String msgTypeCode, String msgCd, String msg) {
		super(msg);
		this.msgTypeCode = msgTypeCode;
		this.msgCd = msgCd;
	}

	public YappException(String msgTypeCode, String msgCd, String msg, String msgDetail, String msgKey) {
		super(msg);
		this.msgTypeCode = msgTypeCode;
		this.msgCd = msgCd;
		this.msgDetail = msgDetail;
		this.msgKey = msgKey;
	}
}
