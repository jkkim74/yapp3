package com.kt.yapp.exception;

/**
 * yapp exception
 */
public class YappWsgException extends YappException
{
	private static final long serialVersionUID = 6704895185487081439L;
	private static final String msg = "WSG 연동중 오류가 발생했습니다.";
	private static final String msgGrpCode = "WSG_ERROR";
	
	public YappWsgException() {
		super(msgGrpCode, msg);
	}
	
	public YappWsgException(String msg) {
		super(msgGrpCode, msg);
	}
	
	public YappWsgException(String msgCd, String msg) {
		super(msgGrpCode, msgCd, msg);
	}
}
