package com.kt.yapp.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kt.yapp.domain.Event;
import com.kt.yapp.domain.EventAppl;
import com.kt.yapp.domain.EventYData;
import com.kt.yapp.domain.SessionContractInfo;
import com.kt.yapp.domain.req.EventReq;
import com.kt.yapp.domain.resp.ResultInfo;
import com.kt.yapp.exception.YappException;
import com.kt.yapp.service.CmsService;
import com.kt.yapp.service.CommonService;
import com.kt.yapp.util.SessionKeeper;
import com.kt.yapp.util.YappUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * YCollaboController.java
 * 
 * @author seungman.yu
 * @since 2018. 9. 12.
 * @version 1.0
 * 
 * Modification Information
 * Mod Date		Modifier		Description
 * ========================================
 * 2018. 9. 12.	seungman.yu 	Y Event 기능 추가
 * Copyright (c) 2018 KTDS, Inc. All Rights Reserved
 */
@RestController
@Api(description="Y콜라보 처리 컨트롤러")
public class YCollaboController 
{
	private static final Logger logger = LoggerFactory.getLogger(YCollaboController.class);
	
	@Autowired
	private CmsService cmsService;
	@Autowired
	private CommonService cmnService;
	
	@RequestMapping(value = "/ycollabo/yCollaboList", method = RequestMethod.GET)
	@ApiOperation(value="Y콜라보 목록조회")
	public ResultInfo<List<EventYData>> yCollaboList(EventReq eventReq, HttpServletRequest req) throws Exception
	{
		ResultInfo<List<EventYData>> resultList = new ResultInfo<>();
		String cntrNo = SessionKeeper.getCntrNo(req);
		eventReq.setCntrNo(cntrNo);
		eventReq.setEvtTp("G0006");
		SessionContractInfo cntrInfo = (SessionContractInfo)SessionKeeper.get(req, SessionKeeper.KEY_CNTR_INFO);
		logger.info("yCollaboList cntrInfo :: " + cntrInfo.toString());
		
		resultList.setResultData(cmsService.getCollaboEventList(eventReq, cntrInfo));
		
		return resultList;
	}
	
	@RequestMapping(value = "/ycollabo/yCollaboInfo", method = RequestMethod.GET)
	@ApiOperation(value="Y콜라보 정보조회")
	public ResultInfo<EventYData> yCollaboInfo(EventReq eventReq, HttpServletRequest req) throws Exception
	{
		logger.info("yCollaboInfo start...");
		ResultInfo<EventYData> resultInfo = new ResultInfo<>();
		String cntrNo = SessionKeeper.getCntrNo(req);
		logger.info("cntrNo :: " + cntrNo);
		eventReq.setCntrNo(cntrNo);
		SessionContractInfo cntrInfo = (SessionContractInfo)SessionKeeper.get(req, SessionKeeper.KEY_CNTR_INFO);
		logger.info("yCollaboInfo cntrInfo :: " + cntrInfo.toString());
		resultInfo.setResultData(cmsService.getCollaboEventInfo(eventReq, cntrInfo));
		
		return resultInfo;
	}
	
	@RequestMapping(value = "/ycollabo/yCollaboAppl", method = RequestMethod.POST)
	@ApiOperation(value="Y콜라보 신청")
	public ResultInfo<String> yCollaboAppl(EventAppl appl, HttpServletRequest req) throws Exception
	{
		logger.info("yCollaboAppl start...");
		String msg = "";
		String cntrNo = SessionKeeper.getCntrNo(req);
		SessionContractInfo cntrInfo = (SessionContractInfo)SessionKeeper.get(req, SessionKeeper.KEY_CNTR_INFO);
		logger.info("yCollaboAppl cntrInfo :: " + cntrInfo.toString());
		appl.setCntrNo(cntrNo);
		appl.setName(cntrInfo.getOrgUserNm());
		appl.setMobileNo(cntrInfo.getMobileNo());
		
		String modYn = appl.getModYn();	//응모정보 수정여부
		
		List<EventAppl> evtApplList = cmsService.getEventApplList(appl.getEvtSeq(), appl.getCntrNo());
		if(YappUtil.isEq("Y", modYn)) {	//수정
			if ( YappUtil.isEmpty(evtApplList) ){
				throw new YappException("CHECK_MSG", cmnService.getMsg("ERR_NOT_EXST_APPL_EVENT"));		// 응모한 내역이 없습니다.
			}
		}else {	//등록
			if ( YappUtil.isNotEmpty(evtApplList) ){
				throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_EXST_APPL_EVENT"));	// 이미 응모한 이벤트입니다.
			}
		}
		
		EventReq eventRep = new EventReq();
		eventRep.setCntrNo(appl.getCntrNo());
		eventRep.setEvtSeq(appl.getEvtSeq());
		//List<Event> targetEventList = cmsService.getEventList(eventRep);
		List<EventYData> targetEventList = cmsService.getCollaboEventList(eventRep, null);
		if ( YappUtil.isNotEmpty(targetEventList) ){
			if(targetEventList.get(0).getEndYn().equals("Y")){
				throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_CLOSE_EVENT"));	// 종료된 이벤트 입니다.
			}
		}else{
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_NO_EVENT")); //해당하는 이벤트가 존재하지 않습니다. 다시 시도해주세요.
		}
		
		// 이벤트 응모
		if(YappUtil.isEq("Y", modYn)) {	//수정
			cmsService.updateApplCollaboEvent(appl);
			msg = cmnService.getMsg("MSG_APPL_EVENT_EDIT");
		}else {
			cmsService.applCollaboEvent(appl);
			msg = cmnService.getMsg("MSG_APPL_EVENT");
		}
		
		return new ResultInfo<String>(msg);
	}
}
