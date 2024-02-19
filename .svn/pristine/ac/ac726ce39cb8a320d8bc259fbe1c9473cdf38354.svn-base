package com.kt.yapp.exception;

import com.kt.yapp.em.EnumRsltCd;

import lombok.Getter;

/**
 * yshop exception
 */
public class YShopException extends Exception {

	private static final long serialVersionUID = 8684788944826590379L;

	@Getter
	private String msgCd = EnumRsltCd.C999.getRsltCd();
	@Getter
	private String msgTypeCode = "SYSTEM_MSG";
	@Getter
	private String msgDetail = "";
	@Getter
	private String msgKey = "";
	@Getter
	private String userId = "";
	
	public YShopException() {
		super();
	}
	
	public YShopException(String msgTypeCode, String msg) {
		super(msg);
		this.msgTypeCode = msgTypeCode;
	}
	
	public YShopException(String msgTypeCode, String msgCd, String msg) {
		super(msg);
		this.msgTypeCode = msgTypeCode;
		this.msgCd = msgCd;
	}
	
	public YShopException(String msgTypeCode, String msgCd, String msg, String userId) {
		super(msg);
		this.msgTypeCode = msgTypeCode;
		this.msgCd = msgCd;
		this.userId = userId;
	}

	public YShopException(String msgTypeCode, String msgCd, String msg, String msgDetail, String msgKey) {
		super(msg);
		this.msgTypeCode = msgTypeCode;
		this.msgCd = msgCd;
		this.msgDetail = msgDetail;
		this.msgKey = msgKey;
	}
}
