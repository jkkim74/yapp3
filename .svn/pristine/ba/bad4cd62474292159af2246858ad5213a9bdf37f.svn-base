package com.kt.yapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.kt.yapp.common.repository.CommonDao;
import com.kt.yapp.domain.AppInfo;
import com.kt.yapp.domain.AppPushLandVo;
import com.kt.yapp.domain.BannerBenefit;
import com.kt.yapp.domain.BannerBenefitEvent;
import com.kt.yapp.domain.BannerBenefitUser;
import com.kt.yapp.domain.CallingPlan;
import com.kt.yapp.domain.DeviceTokenInfo;
import com.kt.yapp.domain.Event;
import com.kt.yapp.domain.EventAppl;
import com.kt.yapp.domain.EventMaster;
import com.kt.yapp.domain.EventYData;
import com.kt.yapp.domain.GrpCode;
import com.kt.yapp.domain.Guide;
import com.kt.yapp.domain.KosAccess;
import com.kt.yapp.domain.LampMenu;
import com.kt.yapp.domain.Notice;
import com.kt.yapp.domain.NoticeMsg;
import com.kt.yapp.domain.ParentsInfo;
import com.kt.yapp.domain.PushResult;
import com.kt.yapp.domain.RsaKeyInfo;
import com.kt.yapp.domain.SendSms;
import com.kt.yapp.domain.SessionContractInfo;
import com.kt.yapp.domain.SysCheck;
import com.kt.yapp.domain.Terms;
import com.kt.yapp.domain.req.EventReq;
import com.kt.yapp.domain.req.NoticeReq;
import com.kt.yapp.em.EnumMasterNotiMsg;
import com.kt.yapp.em.EnumRsltCd;
import com.kt.yapp.em.EnumYn;
import com.kt.yapp.exception.YShopException;
import com.kt.yapp.exception.YappException;
import com.kt.yapp.push.FcmMessage;
import com.kt.yapp.push.FcmResult;
import com.kt.yapp.push.FcmSender;
import com.kt.yapp.push.Notification;
import com.kt.yapp.util.YappUtil;

/**
 * CmsService.java
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
@Service
public class CmsService
{
	private static final Logger logger = LoggerFactory.getLogger(CmsService.class);
	
	@Autowired
	private CommonService cmnService;
	@Autowired
	private CmsService cmsService;
	@Autowired
	private KosService kosService;
	@Autowired
	private CommonDao cmnDao;	
	
	@Value("${fcm.api.serverkey}")
	public String serverkey;

	/**
	 * APP 정보를 조회한다. (버전 등)
	 */
	public AppInfo getAppInfo(String osTp)
	{
		AppInfo paramObj = new AppInfo();
		paramObj.setOsTp(osTp);
		AppInfo appInfoResult = new AppInfo();
		if(YappUtil.isNotEmpty(cmnDao.selectOne("mybatis.mapper.cms.getAppInfo", paramObj))){
			appInfoResult = cmnDao.selectOne("mybatis.mapper.cms.getAppInfo", paramObj);
		}

		GrpCode snsResult = this.getCodeNm("SNS_YN", osTp);
		appInfoResult.setSnsLoginYn(snsResult.getUseYn());
		
		return appInfoResult;
	}
	
	/** 220509 강업여부 로직 추가
	 * APP 정보를 조회한다. (버전 등)
	 */
	public AppInfo getAppInfo(String osTp, String appVrsn)
	{
		
		AppInfo paramObj = new AppInfo();
		paramObj.setOsTp(osTp);
		paramObj.setAppVrsn(appVrsn);
		AppInfo appInfoResult = new AppInfo();
		
		//220512
		AppInfo appInfo = new AppInfo();
		appInfo = cmnDao.selectOne("mybatis.mapper.cms.getAppInfo", paramObj);
		
		if(YappUtil.isNotEmpty(appInfo)){
			appInfoResult = appInfo;
		}

		//220509 앱강제업데이트 Y
		int forceCnt = cmnDao.selectOne("mybatis.mapper.cms.getAppInfoForceCnt", paramObj);
		
		if(forceCnt > 0){
			appInfoResult.setForceUptYn("Y");
		}

		GrpCode snsResult = this.getCodeNm("SNS_YN", osTp);
		appInfoResult.setSnsLoginYn(snsResult.getUseYn());

		return appInfoResult;
	}

	/**
	 * 시스템 점검 정보를 조회한다.
	 */
	public SysCheck getSysChkInfo(Map<String, Object> paramObj)
	{
		return cmnDao.selectOne("mybatis.mapper.cms.getSysChkInfo", paramObj);
	}
	
	/**
	 * 알림 마스터 정보를 조회한다.
	 */
	public NoticeMsg getMasterNotiMsgInfo(int seq)
	{
		return cmnDao.selectOne("mybatis.mapper.cms.getMasterNotiMsgList", YappUtil.makeParamMap("seq", seq));
	}
	
	/**
	 * 알림 목록을 조회한다.
	 */
	public List<NoticeMsg> getNotiMsgList(NoticeMsg paramObj)
	{
		return cmnDao.selectList("mybatis.mapper.cms.getNotiMsgList", paramObj);
	}
	
	public List<NoticeMsg> getPushList(NoticeMsg paramObj)
	{
		return cmnDao.selectList("mybatis.mapper.cms.getPushList", paramObj);
	}
	 
	
	/**
	 * 알림 new 건수를 조회한다.
	 */
	public int getNotiMsgNewCount(String cntrNo)
	{
		return cmnDao.selectOne("mybatis.mapper.cms.getNotiMsgNewCount", YappUtil.makeParamMap("cntrNo", cntrNo));
	}
	
	public int getThreadPushMsgNewCount(NoticeMsg noticeMsg)
	{ 
		return cmnDao.selectOne("mybatis.mapper.cms.getPushMsgCount", noticeMsg);
	}
	// getPushMsgCount

	/**
	 * 알림 정보를 추가한다.
	 */
	//@Transactional(rollbackFor = Throwable.class)
	public int insertNotiMsg(String cntrNo, int masterNotiSeq, String[] replaceMapKey, Object[] replaceMapVal, String linkUrl, String appVrsn) throws Exception
	{
		// 알림 메시지 추가
		NoticeMsg masterNotiMsg = getMasterNotiMsgInfo(masterNotiSeq);
		if ( masterNotiMsg == null )
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_NOTICE_MSG_MASTER_DATA"));
		
		String notiMsg = masterNotiMsg.getNotiMsg();
		String pushYn = masterNotiMsg.getPushYn();
		Map<String, Object> paramMap = YappUtil.makeParamMap(replaceMapKey, replaceMapVal);
		notiMsg = YappUtil.replaceMapData(notiMsg, paramMap);
		
		masterNotiMsg.setCntrNo(cntrNo);
		masterNotiMsg.setNotiMsg(notiMsg);
		masterNotiMsg.setLinkUrl(linkUrl);
		if(masterNotiSeq == EnumMasterNotiMsg.G0003.getSeq()){
			masterNotiMsg.setReqRcvYn("Y");
		}else{
			masterNotiMsg.setReqRcvYn("N");
		}
		masterNotiMsg.setNotiSeq(masterNotiSeq);
		if(("Y").equals(pushYn)){
			DeviceTokenInfo deiviceInfo = cmnService.getDeviceTokenIdInfo(cntrNo);

			if(deiviceInfo != null && deiviceInfo.getDeviceTokenId() != null && !deiviceInfo.getDeviceTokenId().equals("")){
				//PushMessage message = new PushMessage();
				//message.setTitle("");
				//message.setContent(notiMsg);
				//message.setLinkUrl(linkUrl);
				List<PushResult> pushRslt = new ArrayList<PushResult>(); 
				
				if(deiviceInfo.getOsTp() != null){
					// new 알림 카운트 조회
					int badgeCnt = cmsService.getNotiMsgNewCount(cntrNo) + 1;
					FcmMessage message = null;
					if(deiviceInfo.getOsTp().equals("G0001")){
						message = new FcmMessage.Builder()
							 .collapseKey("1")
							 .timeToLive(3)
							 .priority(FcmMessage.Priority.HIGH)
							 .mutableContent(false)
							 .dryRun(false)
							 .addData("appName", "")
							 .addData("content", notiMsg)
							 .addData("badge", String.valueOf(badgeCnt))
							 .addData("linkurl", linkUrl)							 
							 .addData("evtSeq", "")
							 .addData("evtTypeNm", "")
							 .build();
					}else if(deiviceInfo.getOsTp().equals("G0002")){
						message = new FcmMessage.Builder()
								 .collapseKey("1")
								 .timeToLive(3)
								 .priority(FcmMessage.Priority.NORMAL)
								 .mutableContent(false)
								 .dryRun(false)
								 .notification(new Notification.Builder(null)
								 .title("")
								 .body(notiMsg)
								 .badge(String.valueOf(badgeCnt))
								 .build())
								 .addData("appName", "")
								 .addData("content", notiMsg)
								 .addData("badge", String.valueOf(badgeCnt))
								 .addData("linkurl", linkUrl)							 
								 .addData("evtSeq", "")
								 .addData("evtTypeNm", "")								 
								 .build();
					}
					

												
					FcmSender fcmSender = new FcmSender(serverkey);
					FcmResult result = fcmSender.send(message, deiviceInfo.getDeviceTokenId(), 1);	
					
					if (YappUtil.isNotEmpty(result.getMessageId())) {                        
                        masterNotiMsg.setPushResultCd("P");
                    }else{
                    	masterNotiMsg.setPushResultCd("F");
                    }
				}
			}
		}

		return insertNotiMsg(masterNotiMsg);
	}
	
	/**
	 * 알림 정보를 추가한다.
	 */	
	public int insertNotiMsg(NoticeMsg paramObj) throws Exception
	{
		return cmnDao.insert("mybatis.mapper.cms.insertNotiMsg", paramObj);
	}

	/** 220413
	 * 알림 new 건수를 조회한다.
	 */
	public int getShopNotiMsgNewCount(String userId)
	{
		return cmnDao.selectOne("mybatis.mapper.cms.getShopNotiMsgNewCount", YappUtil.makeParamMap("userId", userId));
	}
	
	/** 220412
	 * shop 알림 정보를 추가한다.
	 */
	public int insertShopNotiMsg(String userId, NoticeMsg masterNotiMsg) throws Exception
	{
		logger.info("insertShopNotiMsg : "+masterNotiMsg);
		// 알림 메시지 추가
		if ( masterNotiMsg == null )
			throw new YShopException("SHOP_MSG",EnumRsltCd.C999.getRsltCd(),cmnService.getMsg("ERR_NOTICE_MSG_MASTER_DATA"), userId);
		
		
		DeviceTokenInfo deiviceInfo = cmnService.getDeviceTokenIdInfoKt(userId);
		
		if(deiviceInfo != null && deiviceInfo.getDeviceTokenId() != null && !deiviceInfo.getDeviceTokenId().equals("")){
			String pushYn = deiviceInfo.getPushRcvYn();
			
			if(("Y").equals(pushYn)){
				String notiMsg = masterNotiMsg.getNotiMsg();

				AppPushLandVo appPushLandVo = new AppPushLandVo();
				appPushLandVo.setTitle(masterNotiMsg.getNotiTitle());
				appPushLandVo.setUrl(masterNotiMsg.getLinkUrl());
				appPushLandVo.setTab("yShop");
				
				Gson gson = new Gson();
				
				if(deiviceInfo.getOsTp() != null){
					// new 알림 카운트 조회
					int badgeCnt = cmsService.getShopNotiMsgNewCount(userId) + 1;
					FcmMessage message = null;
					if(deiviceInfo.getOsTp().equals("G0001")){
						message = new FcmMessage.Builder()
							 .collapseKey("1")
							 .timeToLive(3)
							 .priority(FcmMessage.Priority.HIGH)
							 .mutableContent(false)
							 .dryRun(false)
							 .addData("appName", "")
							 .addData("content", notiMsg)
							 .addData("badge", String.valueOf(badgeCnt))
							 .addData("linkurl", "")
							 .addData("landingInfo", gson.toJson(appPushLandVo)) 
							 .addData("evtSeq", "")
							 .addData("evtTypeNm", "")
							 .build();
					}else if(deiviceInfo.getOsTp().equals("G0002")){
						message = new FcmMessage.Builder()
								 .collapseKey("1")
								 .timeToLive(3)
								 .priority(FcmMessage.Priority.NORMAL)
								 .mutableContent(false)
								 .dryRun(false)
								 .notification(new Notification.Builder(null)
								 .title("")
								 .body(notiMsg)
								 .badge(String.valueOf(badgeCnt))
								 .build())
								 .addData("appName", "")
								 .addData("content", notiMsg)
								 .addData("badge", String.valueOf(badgeCnt))
								 .addData("linkurl", "")
								 .addData("landingInfo", gson.toJson(appPushLandVo)) 
								 .addData("evtSeq", "")
								 .addData("evtTypeNm", "")								 
								 .build();
					}
					

												
					FcmSender fcmSender = new FcmSender(serverkey);
					FcmResult result = fcmSender.send(message, deiviceInfo.getDeviceTokenId(), 1);	
					
					if (YappUtil.isNotEmpty(result.getMessageId())) {                        
                        masterNotiMsg.setPushResultCd("P");
                    }else{
                    	masterNotiMsg.setPushResultCd("F");
                    }
				}
			}
		}

		return insertShopNotiMsg(masterNotiMsg);
	}
	
	/**220412
	 * shop 알림 정보를 추가한다.
	 */	
	public int insertShopNotiMsg(NoticeMsg paramObj) throws Exception
	{
		return cmnDao.insert("mybatis.mapper.cms.insertShopNotiMsg", paramObj);
	}
	
	/**
	 * 알림 정보를 읽음 처리한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int updateNotiMsgNewYn(String cntrNo)
	{
		int rtnVal = 0;
		NoticeMsg paramObj = new NoticeMsg();
		paramObj.setCntrNo(cntrNo);
		paramObj.setNewYn(EnumYn.C_N.getValue());

		rtnVal += cmnDao.update("mybatis.mapper.cms.updatePushMsg", paramObj);
		rtnVal += cmnDao.update("mybatis.mapper.cms.updateNotiMsg", paramObj);
		rtnVal += cmnDao.update("mybatis.mapper.cms.updateThreadPushMsg", paramObj);

		return rtnVal;
	}

	/**
	 * 알림 정보를 읽음 처리한다.(개별)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int updateNotiMsgOneNewYn(int notiMsgSeq)
	{
		NoticeMsg paramObj = new NoticeMsg();
		paramObj.setNotiMsgSeq(notiMsgSeq);
		paramObj.setNewYn(EnumYn.C_N.getValue());
		
		return cmnDao.update("mybatis.mapper.cms.updateNotiMsgOne", paramObj);
	}

	/**
	 * 알림 정보를 삭제 처리한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int deleteNotiMsg(String cntrNo)
	{
		int rtnVal = 0;
		NoticeMsg paramObj = new NoticeMsg();
		paramObj.setCntrNo(cntrNo);
		paramObj.setDelYn(EnumYn.C_Y.getValue());

		rtnVal += cmnDao.update("mybatis.mapper.cms.updatePushMsg", paramObj);
		rtnVal += cmnDao.update("mybatis.mapper.cms.updateNotiMsg", paramObj);

		return rtnVal;
	}

	/**
	 * 알림 정보를 삭제 처리한다.(개별)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int deleteNotiMsgOne(int notiMsgSeq)
	{
		NoticeMsg paramObj = new NoticeMsg();
		paramObj.setNotiMsgSeq(notiMsgSeq);
		paramObj.setDelYn(EnumYn.C_Y.getValue());
		
		return cmnDao.update("mybatis.mapper.cms.updateNotiMsgOne", paramObj);
	}
	/**
	 * 약관 정보를 조회한다.
	 */
	public Terms getTermsInfo()
	{
		return cmnDao.selectOne("mybatis.mapper.cms.getTermsInfo", null);
	}
	
	/**
	 * 약관 정보를 조회한다. (230228 url 정보)
	 */
	public Terms getTermsAgreeInfo()
	{
		return cmnDao.selectOne("mybatis.mapper.cms.getTermsAgreeInfo", null);
	}

	/**
	 * 공지사항 목록을 조회한다.
	 */
	public List<Notice> getNoticeList(NoticeReq paramObj)
	{
		return cmnDao.selectList("mybatis.mapper.cms.getNoticeList", paramObj);
	}


	/**
	 * 공지사항 목록을 조회한다.(뉴버젼)
	 */
	public List<Notice> getNewNoticeList()
	{
		return cmnDao.selectList("mybatis.mapper.cms.getNewNoticeList", null);
	}

	/**
	 * 이벤트 목록(Hidden 영역)을 조회한다.
	 */
	public List<Event> getHiddenEventList(EventReq paramObj)
	{
		return cmnDao.selectList("mybatis.mapper.cms.getHiddenEventList", paramObj);
	}
	
	//TODO 혜택배너 목록 가져오기
	/**
	 * 이벤트 목록(메인왼쪽 움직이는 gif)을 조회한다.
	 */
	public List<BannerBenefit> getBannerBanefitList()
	{
		
		List<BannerBenefit> bannerBenefitList = new ArrayList<>();
		
		
		//cmnDao.selectList("mybatis.mapper.cms.getEventGifList", null)
		
		
		return bannerBenefitList;
	}

	/**
	 * 이벤트 목록(메인왼쪽 움직이는 gif)을 조회한다.
	 */
	public List<Event> getEventGifList()
	{
		return cmnDao.selectList("mybatis.mapper.cms.getEventGifList", null);
	}

	/**
	 * 이벤트 응모 정보를 조회한다.
	 */
	public List<EventAppl> getEventApplList(int evtSeq, String cntrNo)
	{
		Map<String, Object> paramObj = 
				YappUtil.makeParamMap(new String[]{"evtSeq", "cntrNo"}, new Object[]{evtSeq, cntrNo});
		
		return cmnDao.selectList("mybatis.mapper.cms.getEventApplList", paramObj);
	}
	
	/**
	 * 이벤트에 응모한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int applEvent(EventAppl paramObj)
	{
		return cmnDao.insert("mybatis.mapper.cms.insertEventAppl", paramObj);
	}
	
	/**
	 * 이벤트에 응모한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int applEventMaster(EventAppl paramObj)
	{
		return cmnDao.insert("mybatis.mapper.cms.insertEventMasterAppl", paramObj);
	}
	

	/**
	 * 바로가기(link) 이력을 저장한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int applyLinkEvent(EventAppl paramObj)
	{
		return cmnDao.insert("mybatis.mapper.cms.insertEventLinkAppl", paramObj);
	}

	/**
	 * 요금제를 조회한다.
	 */
	public CallingPlan getCallingPlanInfo(String ppCd)
	{
		return cmnDao.selectOne("mybatis.mapper.cms.getCallingPlanList", YappUtil.makeParamMap("ppCd", ppCd));
	}
	
	/**
	 * 요금제 목록을 조회한다.
	 */
	public List<CallingPlan> getCallingPlanList(CallingPlan paramObj)
	{
		return cmnDao.selectList("mybatis.mapper.cms.getCallingPlanList", paramObj);
	}
	
	/**
	 * 이용안내 목록을 조회한다.
	 */
	public List<Guide> getGuideList()
	{
		return cmnDao.selectList("mybatis.mapper.cms.getGuideList", null);
	}

	/**
	 * 그룹코드 목록을 조회한다.
	 */
	public List<GrpCode> getGrpCodeList(String grpCodeId)
	{
		GrpCode paramObj = new GrpCode();
		paramObj.setGrpCodeId(grpCodeId);
		
		return cmnDao.selectList("mybatis.mapper.cms.getGrpCodeList", paramObj);
	}
	
	/**
	 * 그룹코드 목록을 조회한다.EVT_TEST_CNTR_NO
	 */
	public List<GrpCode> getGrpCodeList(String grpCodeId, String cntrNo)
	{
		GrpCode paramObj = new GrpCode();
		paramObj.setGrpCodeId(grpCodeId);
		paramObj.setCodeNm(cntrNo); //210714 관리자 이벤트 테스트 용이성 향상
		
		return cmnDao.selectList("mybatis.mapper.cms.getGrpCodeList", paramObj);
	}
	
	/**
	 * YBox 부분차단(단말) 목록을 조회한다.
	 */
	public List<GrpCode> getYBoxSrvBlockList()
	{
		return cmnDao.selectList("mybatis.mapper.cms.getYBoxSrvBlockList", null);
	}
	
	/**
	 * YBox 부분차단(서버) 목록을 조회한다.
	 */
	public List<GrpCode> getYBoxSrvBlockServerList()
	{
		return cmnDao.selectList("mybatis.mapper.cms.getYBoxSrvBlockServerList", null);
	}
	
	/**
	 * 코드 명을 조회한다.
	 */
	public GrpCode getCodeNm(String grpCodeId, String codeId)
	{
		GrpCode paramObj = new GrpCode();
		paramObj.setCodeId(codeId);
		paramObj.setGrpCodeId(grpCodeId);
		
		return cmnDao.selectOne("mybatis.mapper.cms.getCodeNm", paramObj);
	}
	
	/**
	 * 데이터멘트를 조회한다.
	 */
	public GrpCode getDataMent(String codeId)
	{
		return cmnDao.selectOne("mybatis.mapper.cms.getDataMent", YappUtil.makeParamMap("codeId", codeId));
	}

	
	/**
	 * 인증 문자 발송을 한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public SendSms sndSmsAuth(String cntrNo, String telno, String authTp, String smsBody) throws Exception
	{
		List<ParentsInfo> parentsList = kosService.retrieveLegalAgntSvcNo(telno);
		String revTelno = parentsList.get(0).getParentsTelno();
		Map<String, Object> paramObj = 
				YappUtil.makeParamMap(new String[]{"telno", "cntrNo", "authTp", "authSmsSeq"}, new Object[]{revTelno, cntrNo, authTp, ""});
		
		int authSmsSeq = cmnDao.insert("mybatis.mapper.cms.insertAuthSms", paramObj);
		
		logger.info("====================================[result]=============================================");
		logger.info("authSmsSeq : " + authSmsSeq);
		logger.info("authSmsSeq : " + paramObj.get("authSmsSeq"));
		logger.info("====================================[result]=============================================");
		
		kosService.sendSms(revTelno, smsBody, YappUtil.lpad(paramObj.get("authSmsSeq"), 13, "0"));
		
		SendSms smsParam = new SendSms();
		smsParam.setCntrNo(cntrNo);
		smsParam.setAuthSmsSeq(Integer.parseInt(String.valueOf(paramObj.get("authSmsSeq"))));
		return smsParam;
	}
	
	/**
	 * 인증 에러 카운트를 조회한다.
	 */
	public SendSms getAuthSmsFailCnt(SendSms paramObj)
	{
		return cmnDao.selectOne("mybatis.mapper.cms.getAuthSmsFailCnt", paramObj);
	}
	
	
	/**
	 * 인증 에러 카운트를 업데이트 한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int updateAuthSmsFailCnt(SendSms paramObj, String authSmsYn)
	{
		int rtnVal = 0;
		String authTp = "";
		rtnVal += cmnDao.update("mybatis.mapper.cms.updateAuthSmsFail", paramObj);
		if(authSmsYn.equals("Y")){
			paramObj.setAuthSmsYn(authSmsYn);
			rtnVal += cmnDao.update("mybatis.mapper.cms.updateAuthSmsYn", paramObj);
			authTp = paramObj.getAuthTp();
			if(("AUTH_TP_SMS").equals(authTp)){
				rtnVal += cmnDao.update("mybatis.mapper.cms.updateAuthSmsUser", paramObj);
			}
		}
		return rtnVal;
	}
	
	/**
	 * Y콜라보 이벤트 목록을 조회한다.
	 */
	public List<EventYData> getCollaboEventList(EventReq paramObj, SessionContractInfo cntrInfo)
	{
		List<EventYData> eventYList = cmnDao.selectList("mybatis.mapper.cms.getCollaboEventList", paramObj);
		
		if(YappUtil.isNotEmpty(cntrInfo)) {
			for(EventYData eventYInfo : eventYList) {
				//masking 처리
				eventYInfo.setName(YappUtil.dataToMask("N", cntrInfo.getOrgUserNm()));
				eventYInfo.setMobileNo(YappUtil.dataToMask("T", cntrInfo.getMobileNo()));
				eventYInfo.setAddr(YappUtil.dataToMask("A", eventYInfo.getAddr()));
				eventYInfo.setAddrDtl(YappUtil.rpad("", '*', YappUtil.isEmpty(eventYInfo.getAddrDtl()) ? 0 : eventYInfo.getAddrDtl().length()));
			}
		}
		
		return eventYList;
	}

	/**
	 * Y콜라보 이벤트 정보를 조회한다.
	 */
	public EventYData getCollaboEventInfo(EventReq paramObj, SessionContractInfo cntrInfo)
	{
		EventYData eventYInfo = cmnDao.selectOne("mybatis.mapper.cms.getCollaboEventList", paramObj);
		
		//masking 처리
		eventYInfo.setName(YappUtil.dataToMask("N", cntrInfo.getOrgUserNm()));
		eventYInfo.setMobileNo(YappUtil.dataToMask("T", cntrInfo.getMobileNo()));
		eventYInfo.setAddr(YappUtil.dataToMask("A", eventYInfo.getAddr()));
		eventYInfo.setAddrDtl(YappUtil.rpad("", '*', YappUtil.isEmpty(eventYInfo.getAddrDtl()) ? 0 : eventYInfo.getAddrDtl().length()));
		
		return eventYInfo;
	}
	
	/**
	 *  Y콜라보 이벤트에 응모한다.
	 */
	public int applCollaboEvent(EventAppl paramObj)
	{
		return cmnDao.insert("mybatis.mapper.cms.insertApplCollaboEvent", paramObj);
	}

	/**
	 * 콜라보 이벤트 응모정보 수정한다.
	 * @param paramObj
	 */
	public int updateApplCollaboEvent(EventAppl paramObj) {
		return cmnDao.insert("mybatis.mapper.cms.updateApplCollaboEvent", paramObj);
	}
	
	public List<LampMenu> getLampMenuList(LampMenu paramObj) {
		return cmnDao.selectList("mybatis.mapper.cms.getLampMenuList", paramObj);
	}

	public RsaKeyInfo getRsaPublicKeyInfo(int keySeq) {		
		return cmnDao.getRsaPublicKeyInfo("mybatis.mapper.cms.getRsaPublicKeyInfo", YappUtil.makeParamMap("keySeq", keySeq));	
	}
	
	/**
	 *  KOS API 연동 정보를 저장한다.
	 */
	public int insertKosApiAccess(KosAccess paramObj)
	{
		return cmnDao.insert("mybatis.mapper.cms.insertKosApiAccess", paramObj);
	}
	
	/** 220502
	 * shop 알림 목록을 조회한다.
	 */
	public List<NoticeMsg> getShopNotiMsgList(NoticeMsg paramObj)
	{
		return cmnDao.selectList("mybatis.mapper.cms.getShopNotiMsgList", paramObj);
	}
	
	/**22.05.02
	 * Shop 알림 정보를 삭제 처리한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int deleteShopNotiMsg(String userId)
	{
		int rtnVal = 0;
		NoticeMsg paramObj = new NoticeMsg();
		paramObj.setUserId(userId);
		paramObj.setDelYn(EnumYn.C_Y.getValue());

		rtnVal += cmnDao.update("mybatis.mapper.cms.updateShopNotiMsg", paramObj);

		return rtnVal;
	}
	
	/** 22.05.03
	 * Shop 알림 정보를 읽음 처리한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int updateShopNotiMsgNewYn(String userId)
	{
		int rtnVal = 0;
		NoticeMsg paramObj = new NoticeMsg();
		paramObj.setUserId(userId);
		paramObj.setNewYn(EnumYn.C_N.getValue());

		rtnVal += cmnDao.update("mybatis.mapper.cms.updateShopNotiMsg", paramObj);

		return rtnVal;
	}
	
	
	/** 2022.05.03
	 * 알림+shop알림 new 건수를 조회한다.
	 */
	public int getNotiShopNotiMsgNewCount(NoticeMsg notiMsg, Boolean cntrNoYn)
	{
		int rtnVal = 0;
		int notiCnt = 0;
		int shopCnt = 0;
		int pushCnt = 0;
		int threadPushCnt = 0;
		
		if(cntrNoYn){
			notiCnt = getNotiMsgNewCount(notiMsg.getCntrNo());

			if(YappUtil.isNotEmpty(notiMsg.getUserId())){
				shopCnt = getShopNotiMsgNewCount(notiMsg.getUserId());
			}
			
		}else{

			if(YappUtil.isNotEmpty(notiMsg.getUserId())){
				shopCnt = getShopNotiMsgNewCount(notiMsg.getUserId());
				pushCnt = getUserIdNotiMsgNewCount(notiMsg.getUserId());
				
			}
		}
		
		threadPushCnt = getThreadPushMsgNewCount(notiMsg);
		
		rtnVal = notiCnt+shopCnt+pushCnt+threadPushCnt;
		
		return rtnVal;
	}
	
	/** 2022.05.04
	 * 준회원 PUSH 알림 목록을 조회한다.
	 */
	public List<NoticeMsg> getPushMsgList(NoticeMsg paramObj)
	{
		return cmnDao.selectList("mybatis.mapper.cms.getPushMsgList", paramObj);
	}
	
	
	/** 2022.05.04
	 * 준회원 userId push 알림 정보를 읽음 처리한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int updateUserIdPushMsg(String userId)
	{
		int rtnVal = 0;
		NoticeMsg paramObj = new NoticeMsg();
		paramObj.setUserId(userId);
		paramObj.setNewYn(EnumYn.C_N.getValue());

		rtnVal += cmnDao.update("mybatis.mapper.cms.updateUserIdPushMsg", paramObj);

		return rtnVal;
	}
	
	/** 2022.05.04
	 * 준회원 userid push 알림 정보를 삭제 처리한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int deleteUserIdNotiMsg(String userId)
	{
		int rtnVal = 0;
		NoticeMsg paramObj = new NoticeMsg();
		paramObj.setUserId(userId);
		paramObj.setDelYn(EnumYn.C_Y.getValue());

		rtnVal += cmnDao.update("mybatis.mapper.cms.updateUserIdPushMsg", paramObj);

		return rtnVal;
	}
	
	
	/** 2022.05.04
	 * 준회원 push 알림 new 건수를 조회한다.
	 */
	public int getUserIdNotiMsgNewCount(String userId)
	{
		return cmnDao.selectOne("mybatis.mapper.cms.getUserIdNotiMsgNewCount", YappUtil.makeParamMap("userId", userId));
	}
	
	/** 2022.06.01
	 * 혜택배너 이벤트 목록을 조회한다.
	 */
	public List<BannerBenefitEvent> getBenefitBannerEventList(BannerBenefitUser paramObj)
	{
		return cmnDao.selectList("mybatis.mapper.cms.getBenefitBannerEventList", paramObj);
	}
	
	/** 2022.07.12
	 *  login key값을 저장한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int insertLoginKey(String userId, int keySeq, String useYn)
	{
		Map<String, Object> paramObj = 
				YappUtil.makeParamMap(new String[]{"keySeq", "userId","useYn"}, new Object[]{keySeq, userId, useYn});
		
		return cmnDao.insert("mybatis.mapper.cms.insertLoginKey", paramObj);
	}
	
	/**
	 * 2022.07.12
	 * login key값을 업데이트 한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int updateLoginKey(String userId, int keySeq, String useYn)
	{
		Map<String, Object> paramObj = 
				YappUtil.makeParamMap(new String[]{"keySeq", "userId","useYn"}, new Object[]{keySeq, userId, useYn});

		return cmnDao.update("mybatis.mapper.cms.updateLoginKey", paramObj);
	}
	
	
	/** 2022.07.12
	 * login key 건수를 조회한다.
	 */
	public int getLoginKeyCount(String userId)
	{
		return cmnDao.selectOne("mybatis.mapper.cms.getLoginKeyCount", YappUtil.makeParamMap("userId", userId));
	}
	
	/** 2022.07.12
	 *  login key 처리 로직
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void LoginKeyLogic(String userId, int keySeq, String useYn){
		
		if(cmsService.getLoginKeyCount(userId) > 0){
			cmsService.updateLoginKey(userId, keySeq, useYn);
		}else{
			cmsService.insertLoginKey(userId, keySeq, useYn);
		}
	}
	
	/** 2022.07.12
	 * login keySeq 조회한다.
	 */
	public RsaKeyInfo getLoginKeySeq(String userId){
		return cmnDao.selectOne("mybatis.mapper.cms.getLoginKeySeq", YappUtil.makeParamMap("userId", userId));
	}
	
	/**
	 * 요금제 카테고리 코드를 조회한다.
	 */
	public EventMaster getPpCatLCode(String ppCd)
	{
		return cmnDao.selectOne("mybatis.mapper.cms.getPpCatLCode", YappUtil.makeParamMap("ppCd", ppCd));
	}
	
	/**
	 * 부가서비스 목록을 조회한다.
	 */
	public List<GrpCode> getVasCodeList(String grpCodeId)
	{
		GrpCode paramObj = new GrpCode();
		paramObj.setGrpCodeId(grpCodeId);
		
		return cmnDao.selectList("mybatis.mapper.cms.getVasCodeList", paramObj);
	}
}
