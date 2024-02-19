package com.kt.yapp.aop;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kt.yapp.domain.ApiError;
import com.kt.yapp.domain.resp.ResultInfo;
import com.kt.yapp.em.EnumRsltCd;
import com.kt.yapp.exception.YShopException;
import com.kt.yapp.exception.YappException;
import com.kt.yapp.service.CommonService;
import com.kt.yapp.service.HistService;
import com.kt.yapp.util.YappUtil;

/**
 * 에러 처리 클래스
 */
@ControllerAdvice
public class GlobalExceptionHandler 
{
	@Autowired
	private CommonService cmnService;
	@Autowired
	private HistService histService;
	
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
//	@ResponseBody
//	@ExceptionHandler(AuthenticationException.class)
//	public ResultInfo<String> authErrHandler(HttpServletRequest req, Throwable th)
//	{
//		th.printStackTrace();
//		
//		// API Error 로그 추가 TODO
//		// insertErrorLog()
//		
//		ResultInfo<String> resultInfo = new ResultInfo<>();
//		resultInfo.setResultCd("401");
//		resultInfo.setResultMsg(th.getMessage());
//		
//		return resultInfo;
//	}
	
	/**
	 * 필수 파라미터 미입력 에러
	 */
	@ResponseBody
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResultInfo<String> missingParamErrHandler(HttpServletRequest req, MissingServletRequestParameterException th)
	{
		th.printStackTrace();
		
		String errMsg = YappUtil.nToStr(th.getMessage());
		String paramNm = errMsg.split("'").length > 2 ? errMsg.split("'")[1] : null;
		String newErrMsg = paramNm + " :  입력값이 NULL 입니다."; 
		String msgDetail = newErrMsg;
		String msgTypeCd = "PARAM_ERROR";
		String msgKey = "";
		String msgCd = EnumRsltCd.C999.getRsltCd();
		// API 에러 로그 추가.
		ResultInfo<String> resultInfo = new ResultInfo<>();
		setErrResultInfo(resultInfo, msgTypeCd, msgCd, newErrMsg, msgDetail, msgKey, req, cmnService);
		
		return resultInfo;
	}
	
	/**
	 * yapp 에러 처리
	 */
	@ResponseBody
	@ExceptionHandler(YappException.class)
	public ResultInfo<String> yappErrHandler(HttpServletRequest req, YappException th)
	{
		th.printStackTrace();
		
		// API 에러 로그 추가.
		ResultInfo<String> resultInfo = new ResultInfo<>();
		String msg = th.getMessage();
		String msgDetail = th.getMsgDetail();
		String msgTypeCd = th.getMsgTypeCode();
		String msgKey = th.getMsgKey();
		String msgCd = th.getMsgCd();
/*		if ( msg != null && msg.length() > 200) {
			msg = "시스템 오류가 발생했습니다.잠시 후 다시 시도해주세요.";
		}*/
		logger.error("[0]========================================================");
		logger.error("msg : " + msg);
		logger.error("msgDetail : " + msgDetail);
		logger.error("[0]========================================================");		
		setErrResultInfo(resultInfo, msgTypeCd, msgCd, msg, msgDetail, msgKey, req, cmnService);
		
		return resultInfo;
	}

	/**
	 * 디폴트 에러 처리
	 */
	@ResponseBody
	@ExceptionHandler(Throwable.class)
	public ResultInfo<String> defaultErrHandler(HttpServletRequest req, Throwable th)
	{
		//th.printStackTrace();

		// API 에러 로그 추가.
		ResultInfo<String> resultInfo = new ResultInfo<>();
		String msg = th.getMessage();
		if(msg != null){
			if(msg.contains("java.lang.NumberFormatException")){
				msg = "시스템 오류가 발생했습니다.잠시 후 다시 시도해주세요.";
			}
		}
		
		String msgDetail = "";
		String msgTypeCd = "SYSTEM_MSG";
		String msgKey = "";
		String msgCd = EnumRsltCd.C999.getRsltCd();
				
		if(msg != null && (msg.length() > 2500)){
			if(msg != null) {
				msgDetail = msg.substring(0, 2500);
			}else{
				msgDetail = msg;
			}
			
		}else{
			msgDetail = msg;
		}
		logger.error("[1]========================================================");
		logger.error("msg : " + msg);
		logger.error("msgDetail : " + msgDetail);
		logger.error("[1]========================================================");		
		if ( msg != null && msg.length() > 200) {
			msg = "시스템 오류가 발생했습니다.잠시 후 다시 시도해주세요.";
		}

		//211007 null일때 분기
		String apiUrl = YappUtil.nToStr(req.getRequestURI());
		
		if(YappUtil.isEmpty(msg) && apiUrl.indexOf("/yboxMain") > -1 ){
			setErrResultInfo(resultInfo, msgTypeCd, msgCd, msg, msgDetail, msgKey, req, cmnService, th);
		}else{
			setErrResultInfo(resultInfo, msgTypeCd, msgCd, msg, msgDetail, msgKey, req, cmnService);
		}
		
		return resultInfo;
	}
	
	/**
	 * 에러 결과 정보를 세팅한다.<br>
	 * API 에러 정보를 추가한다.
	 */
	public void setErrResultInfo(ResultInfo<?> resultInfo, String msgTypeCd, String rlstCd, String errMsg, String msgDetail, String msgKey, HttpServletRequest request, CommonService cmnService)
	{
		YappUtil.setErrResultInfo(resultInfo, rlstCd, errMsg);

		// API 에러 로그를 추가한다.
		ApiError apiError = YappUtil.makeApiErrInfo(request, rlstCd, errMsg, msgTypeCd, msgDetail, msgKey);
		histService.insertApiErrLog(apiError, request);
		logger.error("[2]========================================================");
		logger.error("errMsg : " + errMsg);
		logger.error("msgDetail : " + msgDetail);
		logger.error("[2]======================================================== ");

		if(errMsg != null && (msgTypeCd.indexOf("SYSTEM_MSG") > -1 || msgTypeCd.indexOf("REDIS_ERROR") > -1 || errMsg.indexOf("exception") > -1 || errMsg.indexOf("Exception") > -1 || errMsg.indexOf("Connection") > -1 || rlstCd.indexOf("RATS") > -1 || rlstCd.indexOf("ESBS1000") > -1
				|| rlstCd.indexOf("ESBS1100") > -1 || rlstCd.indexOf("COMS9999") > -1 || rlstCd.indexOf("SPT8050") > -1 || rlstCd.indexOf("FWCS") > -1 ||msgDetail.indexOf("exception") > -1 || msgDetail.indexOf("Exception") > -1 || msgDetail.indexOf("Connection") > -1)){
			histService.insertErrControl(apiError, request);	
		}
	}
	
	/**211007 errMsg NULL일때
	 * 에러 결과 정보를 세팅한다.<br>
	 * API 에러 정보를 추가한다.
	 */
	public void setErrResultInfo(ResultInfo<?> resultInfo, String msgTypeCd, String rlstCd, String errMsg, String msgDetail, String msgKey, HttpServletRequest request, CommonService cmnService, Throwable th)
	{
		YappUtil.setErrResultInfo(resultInfo, rlstCd, errMsg);
		if(YappUtil.isEmpty(errMsg)){
			
//			errMsg = YappUtil.nToStr(th);
			
			errMsg = "yboxMainDataNullException";
			
			// API 에러 로그를 추가한다.
			ApiError apiError = YappUtil.makeApiErrInfo(request, rlstCd, errMsg, msgTypeCd, msgDetail, msgKey);
			histService.insertApiErrLog(apiError, request);
			logger.error("[3]========================================================");
			logger.error("restApiUrl : " + apiError.getApiUrl());
			logger.error("errMsg : " + errMsg);
			logger.error("msgDetail : " + msgDetail);
			logger.error("[3]======================================================== ");
	
			if(errMsg != null && (msgTypeCd.indexOf("SYSTEM_MSG") > -1 || msgTypeCd.indexOf("REDIS_ERROR") > -1 || errMsg.indexOf("exception") > -1 || errMsg.indexOf("Exception") > -1 || errMsg.indexOf("Connection") > -1 || rlstCd.indexOf("RATS") > -1 || rlstCd.indexOf("ESBS1000") > -1
					|| rlstCd.indexOf("ESBS1100") > -1 || rlstCd.indexOf("COMS9999") > -1 || rlstCd.indexOf("SPT8050") > -1 || rlstCd.indexOf("FWCS") > -1 ||msgDetail.indexOf("exception") > -1 || msgDetail.indexOf("Exception") > -1 || msgDetail.indexOf("Connection") > -1)){
				histService.insertErrControl(apiError, request);	
			}
		}
	}
	
	/** 220418
	 * yshop 에러 처리
	 */
	@ResponseBody
	@ExceptionHandler(YShopException.class)
	public ResultInfo<String> yshopErrHandler(HttpServletRequest req, YShopException th)
	{
		// API 에러 로그 추가.
		ResultInfo<String> resultInfo = new ResultInfo<>();
		String msg = th.getMessage();
		String msgDetail = th.getMsgDetail();
		String msgTypeCd = th.getMsgTypeCode();
		String msgKey = th.getMsgKey();
		String msgCd = th.getMsgCd();
		String userId = th.getUserId();

		logger.error("[4]========================================================");
		logger.error("msg : " + msg);
		logger.error("msgDetail : " + msgDetail);
		logger.error("userId : " + userId);
		logger.error("[4]========================================================");		
		setShopErrResultInfo(resultInfo, msgTypeCd, msgCd, msg, msgDetail, msgKey, req, cmnService, userId);
		
		return resultInfo;
	}
	
	/** 220418 yshop
	 * 에러 결과 정보를 세팅한다.<br>
	 * API 에러 정보를 추가한다.
	 */
	public void setShopErrResultInfo(ResultInfo<?> resultInfo, String msgTypeCd, String rlstCd, String errMsg, String msgDetail, String msgKey, HttpServletRequest request, CommonService cmnService, String userId)
	{
		YappUtil.setErrResultInfo(resultInfo, rlstCd, errMsg);

		// API 에러 로그를 추가한다.
		ApiError apiError = YappUtil.makeApiErrInfo(request, rlstCd, errMsg, msgTypeCd, msgDetail, msgKey, userId);
		histService.insertApiErrLog(apiError, request);
		logger.error("[5]========================================================");
		logger.error("errMsg : " + errMsg);
		logger.error("msgDetail : " + msgDetail);
		logger.error("[5]======================================================== ");

		if(errMsg != null && (msgTypeCd.indexOf("SYSTEM_MSG") > -1 || msgTypeCd.indexOf("REDIS_ERROR") > -1 || errMsg.indexOf("exception") > -1 || errMsg.indexOf("Exception") > -1 || errMsg.indexOf("Connection") > -1 || rlstCd.indexOf("RATS") > -1 || rlstCd.indexOf("ESBS1000") > -1
				|| rlstCd.indexOf("ESBS1100") > -1 || rlstCd.indexOf("COMS9999") > -1 || rlstCd.indexOf("SPT8050") > -1 || rlstCd.indexOf("FWCS") > -1 ||msgDetail.indexOf("exception") > -1 || msgDetail.indexOf("Exception") > -1 || msgDetail.indexOf("Connection") > -1)){
			histService.insertErrControl(apiError, request);	
		}
	}
}
