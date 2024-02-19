package com.kt.yapp.exception;

import com.kt.yapp.em.EnumRsltCd;

/**
 * yapp exception
 */
public class YappRuntimeAuthException extends YappRuntimeException
{
	private static final long serialVersionUID = 2997438541172592263L;
	private static final String msgCd = EnumRsltCd.C401.getRsltCd();
	private static final String msg = "로그인 정보가 없습니다.";
	private static final String msgGrpCode = "AUTH_MSG";

	public YappRuntimeAuthException() {
		super(msgGrpCode, msgCd, msg);
	}
	
	public YappRuntimeAuthException(String msg) {
		super(msgGrpCode, msgCd, msg);
	}
	
	public YappRuntimeAuthException(String msgCd, String msg) {
		super(msgGrpCode, msgCd, msg);
	}
}
