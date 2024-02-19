package com.kt.yapp.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kt.yapp.common.repository.CommonDao;
import com.kt.yapp.domain.AdultAgree;
import com.kt.yapp.domain.CustAgreeInfoRetvListDtoDetail;
import com.kt.yapp.domain.GrpCode;
import com.kt.yapp.domain.Invitation;
import com.kt.yapp.domain.JoinInfo;
import com.kt.yapp.domain.PreferenceInfo;
import com.kt.yapp.domain.ReviewInfo;
import com.kt.yapp.domain.SendSms;
import com.kt.yapp.domain.SnsInfo;
import com.kt.yapp.domain.SvcOut;
import com.kt.yapp.domain.TermsAgree;
import com.kt.yapp.domain.UserInfo;
import com.kt.yapp.domain.UserPass;
import com.kt.yapp.domain.WsgDataUseQnt;
import com.kt.yapp.em.EnumJoinStatus;
import com.kt.yapp.exception.YappException;
import com.kt.yapp.exception.YappRuntimeAuthException;
import com.kt.yapp.soap.response.SoapResponse184;
import com.kt.yapp.util.YappUtil;

/**
 * UserService.java
 * 
 * @author
 * @since
 * @version 1.0
 * 
 * Modification Information
 * Mod Date		Modifier		Description
 * ========================================
 * 2019. 3. 23.	seungman.yu 	사용자 리뷰
 * Copyright (c) 2018 KTDS, Inc. All Rights Reserved
 */

@Service
public class UserService
{
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private ShubService shubService;
	
	@Autowired
	private CommonService cmnService;
	
	@Autowired
	private WsgService wsgService;
	
	@Autowired
	private KosService kosService;
	
	@Autowired
	private CommonDao cmnDao;
	
	@Autowired
	private CmsService cmsService;
	
	@Autowired
	private UserKtService userKtService;

	/**
	 * YApp 사용자 정보를 조회한다.
	 */
	public UserInfo getYappUserInfo(String cntrNo)
	{
		UserInfo paramUserInfo = new UserInfo();
		paramUserInfo.setCntrNo(cntrNo);
		
		GrpCode prefrenceLimit = cmsService.getCodeNm("PRF_CODE", "P0002");
		paramUserInfo.setPrfDay(Integer.parseInt(prefrenceLimit.getCodeNm()));
		
		UserInfo userInfo = cmnDao.selectOne("mybatis.mapper.user.getUserInfo", paramUserInfo);
		
		logger.info("getYappUserInfo ==============================================");
		logger.info("getYappUserInfo -> userInfo:" + userInfo);
		logger.info("getYappUserInfo ==============================================");
		
		if(userInfo != null){
			
			userInfo.setMaskingMobileNo(YappUtil.blindMidEndMobileNo(userInfo.getMobileNo()));
			
			if(!YappUtil.isEmpty(userInfo.getUserId())){
				
				Map<String, Object> paramObj = YappUtil.makeParamMap(new String[]{"userId"}, new String[]{userInfo.getUserId().trim()});
				
				UserInfo uuidInfo =userKtService.getShopUuidInfo(paramObj);
		
				if(uuidInfo != null){
					
					logger.info("getYappUserKtInfo -> uuidInfo: " + uuidInfo.getShud());
					userInfo.setShud(uuidInfo.getShud());
				}
			}
		}

		return userInfo;
	}
	
	/**
	 * KTID 사용자 정보를 조회한다.
	 */
	public UserInfo getYappUserKtInfo(String userId)
	{
		UserInfo paramUserInfo = new UserInfo();
		paramUserInfo.setUserId(userId);
		
		GrpCode prefrenceLimit = cmsService.getCodeNm("PRF_CODE", "P0002");
		paramUserInfo.setPrfDay(Integer.parseInt(prefrenceLimit.getCodeNm()));
		
		UserInfo userInfo = cmnDao.selectOne("mybatis.mapper.userkt.getUserKtInfo", paramUserInfo);

		logger.info("getYappUserKtInfo ==============================================");
		logger.info("getYappUserKtInfo -> userInfo:" + userInfo);
		logger.info("getYappUserKtInfo ==============================================");

		if(userInfo != null){
			
			userInfo.setMaskingMobileNo(YappUtil.blindMidEndMobileNo(userInfo.getMobileNo()));
			
			if(!YappUtil.isEmpty(userInfo.getUserId())){
				
				Map<String, Object> paramObj = YappUtil.makeParamMap(new String[]{"userId"}, new String[]{userId});
				
				UserInfo uuidInfo =userKtService.getShopUuidInfo(paramObj);
		
				if(uuidInfo != null){
					
					logger.info("getYappUserKtInfo -> uuidInfo: " + uuidInfo.getShud());
					userInfo.setShud(uuidInfo.getShud());
				}
			}
		}
		
		return userInfo;
	}
	
	/**userId로
	 * YApp 사용자 정보를 조회한다.
	 */
	public UserInfo getUserIdCntrNo(String userId)
	{
		UserInfo paramUserInfo = new UserInfo();
		paramUserInfo.setUserId(userId);
		GrpCode prefrenceLimit = cmsService.getCodeNm("PRF_CODE", "P0002");
		paramUserInfo.setPrfDay(Integer.parseInt(prefrenceLimit.getCodeNm()));
		
		// 사용자 정보 조회
		UserInfo userInfo = cmnDao.selectOne("mybatis.mapper.user.getUserIdCntrNo", paramUserInfo);
		return userInfo;
	}

	/**
	 * YApp 사용자 PASS 정보를 조회한다.
	 */
	public UserPass getYappPass(String mobileNo)
	{
		UserPass paramUserPass = new UserPass();
		paramUserPass.setMobileNo(mobileNo);

		// 사용자 정보 조회
		UserPass UserPass = cmnDao.selectOne("mybatis.mapper.user.getUserPass", paramUserPass);
		
		return UserPass;
	}
	
	/**
	 * 서비스 가입 처리.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int insertUserInfo(UserInfo paramObj) throws Exception
	{
		logger.info("insertUserInfo : ");
		UserInfo loginUser = getYappUserInfo(paramObj.getCntrNo());
		int rtnVal = 0;
		// 회원 정보 추가
		if ( loginUser == null ) 
		{
			
			//유휴계정여부 확인
			boolean isSleepUser = false;
			UserInfo sleepUserInfo = cmnDao.selectOne("mybatis.mapper.user.getSleepUserInfo", paramObj);
			if(YappUtil.isNotEmpty(sleepUserInfo)) {
				isSleepUser = true;
				if(YappUtil.isNotEmpty(sleepUserInfo.getUserId())) {
					paramObj.setUserId(sleepUserInfo.getUserId());
				}
			}
			
			//20220111 순서 1
			// yapp 가입
			rtnVal = cmnDao.insert("mybatis.mapper.user.insertUserInfo", paramObj);
			if(isSleepUser) {
				//유휴계정 삭제
				rtnVal += cmnDao.delete("mybatis.mapper.user.deleteSleepUserInfo", paramObj);
			}
			
			//20220111 순서 2
			String dboxId = "";
			if(isSleepUser) {
				logger.info("휴면계정 데이터 박스 생성");
				dboxId = kosService.createDbox(paramObj.getCntrNo(), isSleepUser);
			}else {
				logger.info("데어터 박스 생성 실행");
				dboxId = kosService.createDbox(paramObj.getCntrNo());
			}
			
			//20220111 kos dbox생성 중복 호출 보완 (kos 호출과 insert 순서 변경)
			paramObj.setDboxStatus("G0001");
			paramObj.setDboxId(dboxId);			
			cmnDao.update("mybatis.mapper.user.updateUserSettingInfo", paramObj);
			
			
			//220606 ktYn 업데이트(준회원이 정회원이 되엇을때 kt yn 업데이트)
			if(!YappUtil.isEmpty(paramObj.getUserId())){
				UserInfo UserInfoKt = getYappUserKtInfo(paramObj.getUserId());
				if(!YappUtil.isEmpty(UserInfoKt) ){
					if(YappUtil.isEq(UserInfoKt.getKtYn(), "N")){
						UserInfo userInfoKtYn = new UserInfo();
						userInfoKtYn.setUserId(paramObj.getUserId());
						userInfoKtYn.setKtYn("Y");
						updateUserKtSettingInfo(userInfoKtYn);
					}
				}
			}
			
		} 
		// 이미 가입된 사용자 정보가 존재하면 에러
		else if ( YappUtil.isEq(loginUser.getJoinStatus(), EnumJoinStatus.G0001.getStatusCd()) ) 
		{
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_EXIST_USER"));	// 이미 가입된 사용자입니다.
		}
		// 1개월 이전 재가입
		else
		{
			//가입정보가 있으면 재가입
			//20220111 가입상태 먼저 update
			Map<String, Object> map = YappUtil.makeParamMap(new String[]{"joinStatus", "memStatus", "cntrNo"}, 
					new String[]{EnumJoinStatus.G0001.getStatusCd(), paramObj.getMemStatus(), paramObj.getCntrNo()});
			int joinUpCnt = cmnDao.update("mybatis.mapper.user.updateJoinStatus", map);
			
			//업데이트 성공
			if(joinUpCnt > 0){
			
			// 1개월 이후 재가입은 데이터박스 새로 생성
			//if ( YappUtil.isEq(loginUser.getJoinStatus(), EnumJoinStatus.G0003.getStatusCd()) )
			//{
				String dboxId = kosService.createDbox(paramObj.getCntrNo());
				paramObj.setDboxStatus("G0001");
				paramObj.setDboxId(dboxId);
			//}
				cmnDao.update("mybatis.mapper.user.updateUserSettingInfo", paramObj);
			}
		}

		// 약관동의 정보가 없으면 추가
		paramObj.getTermsAgree().setCntrNo(paramObj.getCntrNo());
		
		TermsAgree termsAgree = cmnDao.selectOne("mybatis.mapper.user.getTermsAgreeInfo", paramObj.getTermsAgree());
		if ( termsAgree == null ){
			rtnVal += cmnDao.insert("mybatis.mapper.user.insertTermsAgreeInfo", paramObj.getTermsAgree());
		}else{
			rtnVal += cmnDao.insert("mybatis.mapper.user.updateTermsAgreeInfo", paramObj.getTermsAgree());
		}
		try{
			shubService.callFn11268(paramObj.getCntrNo(), paramObj.getOpt3TermsAgreeYn(), paramObj.getOpt4TermsAgreeYn(), paramObj.getOpt3TermsVrsn(), paramObj.getOpt4TermsVrsn());
		
		}catch (RuntimeException e) {
			logger.info("이용약관 변경 error");
		}catch (Exception e) {
			logger.info("이용약관 변경 error");
		}
		
		return rtnVal;
	}
	

	/**
	 * 서비스 가입 처리 (KTID)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int insertUserKtInfo(UserInfo paramObj) throws Exception
	{
		logger.info("insertUserInfoKt : ");
		logger.info("insertUserInfoKt paramObj : "+paramObj);
		
		UserInfo loginUser = getYappUserKtInfo(paramObj.getUserId());
		int rtnVal = 0;
		
		// 회원 정보 추가
		if ( loginUser == null ) 
		{
			//유휴계정여부 확인
			boolean isSleepUser = false;
			UserInfo sleepUserInfo = cmnDao.selectOne("mybatis.mapper.userkt.getSleepUserKtInfo", paramObj);
			if(YappUtil.isNotEmpty(sleepUserInfo)) {
				isSleepUser = true;
				if(YappUtil.isNotEmpty(sleepUserInfo.getUserId())) {
					paramObj.setUserId(sleepUserInfo.getUserId());
				}
			}
			
			//20220111 순서 1
			// yapp 가입
			rtnVal = cmnDao.insert("mybatis.mapper.userkt.insertUserKtInfo", paramObj);
			
			//220523 shop연동 uuid 중복 chk후 insert
			userKtService.uuidCheck(paramObj.getUserId());
			
			if(isSleepUser) {
				//유휴계정 삭제
				rtnVal += cmnDao.delete("mybatis.mapper.userkt.deleteSleepUserKtInfo", paramObj);
			}
						
			cmnDao.update("mybatis.mapper.userkt.updateUserKtSettingInfo", paramObj);
			
		} 
		// 이미 가입된 사용자 정보가 존재하면 에러
		/*else if ( YappUtil.isEq(loginUser.getJoinStatus(), EnumJoinStatus.G0001.getStatusCd()) ) 
		{
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_EXIST_USER"));	// 이미 가입된 사용자입니다.
		}*/
		// 1개월 이전 재가입
		else
		{
			//가입정보가 있으면 재가입
			//20220111 가입상태 먼저 update
			Map<String, Object> map = YappUtil.makeParamMap(new String[]{"joinStatus", "memStatus", "userId"}, 
					new String[]{EnumJoinStatus.G0001.getStatusCd(), paramObj.getMemStatus(), paramObj.getUserId()});
			int joinUpCnt = cmnDao.update("mybatis.mapper.userkt.updateJoinStatusKt", map);
			
			//업데이트 성공
			if(joinUpCnt > 0){
			
			// 1개월 이후 재가입은 데이터박스 새로 생성
			//if ( YappUtil.isEq(loginUser.getJoinStatus(), EnumJoinStatus.G0003.getStatusCd()) )
			//{
				/*String dboxId = kosService.createDbox(paramObj.getCntrNo());
				paramObj.setDboxStatus("G0001");
				paramObj.setDboxId(dboxId);*/
			//}
				cmnDao.update("mybatis.mapper.userkt.updateUserKtSettingInfo", paramObj);
			
			}
		}

		// 약관동의 정보가 없으면 추가
		paramObj.getTermsAgree().setUserId(paramObj.getUserId());
		
		TermsAgree termsAgree = cmnDao.selectOne("mybatis.mapper.userkt.getTermsAgreeKtInfo", paramObj.getTermsAgree());
		if ( termsAgree == null ){
			rtnVal += cmnDao.insert("mybatis.mapper.userkt.insertTermsAgreeKtInfo", paramObj.getTermsAgree());
		}else{
			rtnVal += cmnDao.insert("mybatis.mapper.userkt.updateTermsAgreeKtInfo", paramObj.getTermsAgree());
		}
		
		//계약번호가 존재하면 기존 유저 테이블 업데이트
		if(YappUtil.isNotEmpty(paramObj.getCntrNo())){
		  
		  cmnDao.update("mybatis.mapper.user.updateUserSettingInfo", paramObj);
		}
		
		return rtnVal;
	}
	
	@Transactional(rollbackFor = Throwable.class)
	public int insertUserAllInfo(UserInfo paramObj) throws Exception
	{
		logger.info("insertUserAllInfo : ");
		UserInfo loginUserKt = getYappUserKtInfo(paramObj.getUserId());
		int rtnVal = 0;
		
		// 회원 정보 추가
		if ( loginUserKt == null ) 
		{
			//유휴계정여부 확인
			boolean isSleepUserKt = false;
			UserInfo sleepUserKtInfo = cmnDao.selectOne("mybatis.mapper.userkt.getSleepUserKtInfo", paramObj);
			if(YappUtil.isNotEmpty(sleepUserKtInfo)) {
				isSleepUserKt = true;
				if(YappUtil.isNotEmpty(sleepUserKtInfo.getUserId())) {
					paramObj.setUserId(sleepUserKtInfo.getUserId());
				}
			}
			
			//20220111 순서 1
			// yapp 가입
			rtnVal = cmnDao.insert("mybatis.mapper.userkt.insertUserKtInfo", paramObj);
			
			//220523 shop연동 uuid 중복 chk후 insert
			userKtService.uuidCheck(paramObj.getUserId());
			
			if(isSleepUserKt) {
				//유휴계정 삭제
				rtnVal += cmnDao.delete("mybatis.mapper.userkt.deleteSleepUserKtInfo", paramObj);
			}
						
			cmnDao.update("mybatis.mapper.userkt.updateUserKtSettingInfo", paramObj);
			
		} 
		// 이미 가입된 사용자 정보가 존재하면 에러
		/*else if ( YappUtil.isEq(loginUserKt.getJoinStatus(), EnumJoinStatus.G0001.getStatusCd()) ) 
		{
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_EXIST_USER"));	// 이미 가입된 사용자입니다.
		}*/
		// 1개월 이전 재가입
		else
		{
			//가입정보가 있으면 재가입
			//20220111 가입상태 먼저 update
			Map<String, Object> map = YappUtil.makeParamMap(new String[]{"joinStatus", "memStatus", "userId"}, 
					new String[]{EnumJoinStatus.G0001.getStatusCd(), paramObj.getMemStatus(), paramObj.getUserId()});
			int joinUpCnt = cmnDao.update("mybatis.mapper.userkt.updateJoinStatusKt", map);
			
			//업데이트 성공
			if(joinUpCnt > 0){
			
			// 1개월 이후 재가입은 데이터박스 새로 생성
			//if ( YappUtil.isEq(loginUser.getJoinStatus(), EnumJoinStatus.G0003.getStatusCd()) )
			//{
				/*String dboxId = kosService.createDbox(paramObj.getCntrNo());
				paramObj.setDboxStatus("G0001");
				paramObj.setDboxId(dboxId);*/
			//}
				cmnDao.update("mybatis.mapper.userkt.updateUserKtSettingInfo", paramObj);
			
			}
		}

		// 약관동의 정보가 없으면 추가
		paramObj.getTermsAgree().setUserId(paramObj.getUserId());
		
		TermsAgree termsAgreeKt = cmnDao.selectOne("mybatis.mapper.userkt.getTermsAgreeKtInfo", paramObj.getTermsAgree());
		
		logger.info("==============================================================");
		logger.info("insertUserAllInfo -> termsAgreeKt : "+termsAgreeKt);
		logger.info("==============================================================");
		
		if ( termsAgreeKt == null ){
			rtnVal += cmnDao.insert("mybatis.mapper.userkt.insertTermsAgreeKtInfo", paramObj.getTermsAgree());
			
			logger.info("==============================================================");
			logger.info("insertUserAllInfo -> insertTermsAgreeKtInfo : ");
			logger.info("==============================================================");
			
		}else{
			rtnVal += cmnDao.insert("mybatis.mapper.userkt.updateTermsAgreeKtInfo", paramObj.getTermsAgree());
			
			logger.info("==============================================================");
			logger.info("insertUserAllInfo -> updateTermsAgreeKtInfo : ");
			logger.info("==============================================================");
		}
		
		/**========================================================
		 * (2단계) YBOX 가입처리
		 * ========================================================
		 */
		
		UserInfo loginUser = getYappUserInfo(paramObj.getCntrNo());
		//int rtnVal = 0;
		// 회원 정보 추가
		if ( loginUser == null ) 
		{
			
			//유휴계정여부 확인
			boolean isSleepUser = false;
			UserInfo sleepUserInfo = cmnDao.selectOne("mybatis.mapper.user.getSleepUserInfo", paramObj);
			if(YappUtil.isNotEmpty(sleepUserInfo)) {
				isSleepUser = true;
				if(YappUtil.isNotEmpty(sleepUserInfo.getUserId())) {
					paramObj.setUserId(sleepUserInfo.getUserId());
				}
			}
			
			//20220111 순서 1
			// yapp 가입
			rtnVal = cmnDao.insert("mybatis.mapper.user.insertUserInfo", paramObj);
			if(isSleepUser) {
				//유휴계정 삭제
				rtnVal += cmnDao.delete("mybatis.mapper.user.deleteSleepUserInfo", paramObj);
			}
			
			//20220111 순서 2
			String dboxId = "";
			if(isSleepUser) {
				logger.info("휴면계정 데이터 박스 생성");
				dboxId = kosService.createDbox(paramObj.getCntrNo(), isSleepUser);
			}else {
				logger.info("데어터 박스 생성 실행");
				dboxId = kosService.createDbox(paramObj.getCntrNo());
			}
			
			//20220111 kos dbox생성 중복 호출 보완 (kos 호출과 insert 순서 변경)
			paramObj.setDboxStatus("G0001");
			paramObj.setDboxId(dboxId);			
			
			cmnDao.update("mybatis.mapper.user.updateUserSettingInfo", paramObj);
			
		} 
		// 이미 가입된 사용자 정보가 존재하면 에러
		else if ( YappUtil.isEq(loginUser.getJoinStatus(), EnumJoinStatus.G0001.getStatusCd()) ) 
		{
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_EXIST_USER"));	// 이미 가입된 사용자입니다.
		}
		// 1개월 이전 재가입
		else
		{
			//가입정보가 있으면 재가입
			//20220111 가입상태 먼저 update
			Map<String, Object> map = YappUtil.makeParamMap(new String[]{"joinStatus", "memStatus", "cntrNo"}, 
					new String[]{EnumJoinStatus.G0001.getStatusCd(), paramObj.getMemStatus(), paramObj.getCntrNo()});
			int joinUpCnt = cmnDao.update("mybatis.mapper.user.updateJoinStatus", map);
			
			//업데이트 성공
			if(joinUpCnt > 0){
			
			// 1개월 이후 재가입은 데이터박스 새로 생성
			//if ( YappUtil.isEq(loginUser.getJoinStatus(), EnumJoinStatus.G0003.getStatusCd()) )
			//{
				String dboxId = kosService.createDbox(paramObj.getCntrNo());
				paramObj.setDboxStatus("G0001");
				paramObj.setDboxId(dboxId);
			//}
				cmnDao.update("mybatis.mapper.user.updateUserSettingInfo", paramObj);
			}
		}

		// 약관동의 정보가 없으면 추가
		paramObj.getTermsAgree().setCntrNo(paramObj.getCntrNo());
		
		TermsAgree termsAgree = cmnDao.selectOne("mybatis.mapper.user.getTermsAgreeInfo", paramObj.getTermsAgree());
		if ( termsAgree == null ){
			rtnVal += cmnDao.insert("mybatis.mapper.user.insertTermsAgreeInfo", paramObj.getTermsAgree());
		}else{
			rtnVal += cmnDao.insert("mybatis.mapper.user.updateTermsAgreeInfo", paramObj.getTermsAgree());
		}
		try{
			shubService.callFn11268(paramObj.getCntrNo(), paramObj.getOpt3TermsAgreeYn(), paramObj.getOpt4TermsAgreeYn(), paramObj.getOpt3TermsVrsn(), paramObj.getOpt4TermsVrsn());
		
		}catch (RuntimeException e) {
			logger.info("이용약관 변경 error");
		}catch (Exception e) {
			logger.info("이용약관 변경 error");
		}
		
		return rtnVal;
	}
	
	/**
	 * 약관동의 정보 추가
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void insertTermsAgree(TermsAgree paramObj) throws Exception
	{
		// 약관동의 정보가 없으면 추가
		TermsAgree termsAgree = cmnDao.selectOne("mybatis.mapper.user.getTermsAgreeInfo", paramObj);
		
		if ( termsAgree == null ){
			cmnDao.insert("mybatis.mapper.user.insertTermsAgreeInfo", paramObj);
		}else{
			cmnDao.insert("mybatis.mapper.user.updateTermsAgreeInfo", paramObj);
		}
		
		try {
			shubService.callFn11268(paramObj.getCntrNo(), paramObj.getOpt3TermsAgreeYn(), paramObj.getOpt4TermsAgreeYn(), paramObj.getOpt3TermsVrsn(), paramObj.getOpt4TermsVrsn());
							//선택이용약관4 버전
		} catch (RuntimeException e) {
			logger.info("이용약관 동의 변경 error");
			
		}catch (Exception e) {
			logger.info("이용약관 동의 변경 error");
			
		}
	}
	
	@Transactional(rollbackFor = Throwable.class)
	public void insertTermsAgreeKt(TermsAgree paramObj) throws Exception
	{
		// 약관동의 정보가 없으면 추가
		TermsAgree termsAgree = cmnDao.selectOne("mybatis.mapper.userkt.getTermsAgreeKtInfo", paramObj);
		
		if ( termsAgree == null ){
			cmnDao.insert("mybatis.mapper.userkt.insertTermsAgreeKtInfo", paramObj);
		}else{
			cmnDao.insert("mybatis.mapper.userkt.updateTermsAgreeKtInfo", paramObj);
		}
	}
	
	@Transactional(rollbackFor = Throwable.class)
	public void insertTermsAgreeAll(TermsAgree paramObj) throws Exception
	{
		// 약관동의 정보가 없으면 추가
		TermsAgree termsAgree = cmnDao.selectOne("mybatis.mapper.user.getTermsAgreeInfo", paramObj);
		
		if ( termsAgree == null ){
			cmnDao.insert("mybatis.mapper.user.insertTermsAgreeInfo", paramObj);
		}else{
			cmnDao.insert("mybatis.mapper.user.updateTermsAgreeInfo", paramObj);
		}
		
		try {
			shubService.callFn11268(paramObj.getCntrNo(), paramObj.getOpt3TermsAgreeYn(), paramObj.getOpt4TermsAgreeYn(), paramObj.getOpt3TermsVrsn(), paramObj.getOpt4TermsVrsn());
							//선택이용약관4 버전
		} catch (RuntimeException e) {
			logger.info("이용약관 동의 변경 error");
			
		}catch (Exception e) {
			logger.info("이용약관 동의 변경 error");
			
		}
		
		TermsAgree termsAgreeKt = cmnDao.selectOne("mybatis.mapper.userkt.getTermsAgreeKtInfo", paramObj);
		
		if ( termsAgreeKt == null ){
			cmnDao.insert("mybatis.mapper.userkt.insertTermsAgreeKtInfo", paramObj);
		}else{
			cmnDao.insert("mybatis.mapper.userkt.updateTermsAgreeKtInfo", paramObj);
		}
	}
	
	/**
	 * 아이디 기준 약관정보를 조회한다.
	 */
	public TermsAgree getTermsAgreeKtInfoWithUserId(String userId) 
	{
		Map<String, Object> paramObj = YappUtil.makeParamMap(new String[]{"userId"}, new String[]{userId});

		return cmnDao.selectOne("mybatis.mapper.userkt.getTermsAgreeKtInfoWithUserId", paramObj);
	}
	
	/**
	 * 샵 약관 정보를 변경한다. (KTID미가입자 용)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int updateShopTermsAgreeInfo( String cntrNo)
	{
		Map<String, Object> paramObj = YappUtil.makeParamMap(new String[]{"cntrNo"}, new String[]{cntrNo});
		
		return cmnDao.update("mybatis.mapper.user.updateShopTermsAgreeInfo", paramObj);
	}
	
	/**
	 * 선택 약관 정보를 변경한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int updateOptTerms(TermsAgree paramObj)
	{
		int rtnVal = cmnDao.update("mybatis.mapper.user.updateOptTerms", paramObj);
		 rtnVal += cmnDao.update("mybatis.mapper.user.updateUserSettingMkt", paramObj);
		
		return rtnVal;
	}
	
	@Transactional(rollbackFor = Throwable.class)
	public int updateOptTermsKt(TermsAgree paramObj)
	{
		int rtnVal = cmnDao.update("mybatis.mapper.userkt.updateOptTerms", paramObj);
		 rtnVal += cmnDao.update("mybatis.mapper.userkt.updateUserSettingMkt", paramObj);
		
		return rtnVal;
	}
	
	/**
	 * Yapp 가입 정보를 조회한다.
	 */
	public List<JoinInfo> getJoinInfoList(List<String> mobileNoList)
	{
		return cmnDao.selectList("mybatis.mapper.user.getJoinInfoList", mobileNoList);
	}

	/**
	 * 사용자 설정 정보를 저장한다.
	 * @throws Exception 
	 */
	@Transactional(rollbackFor = Throwable.class)
	public UserInfo updateUserSettingInfo(UserInfo paramObj) throws Exception
	{
		UserInfo updateUserSettingInfo= new UserInfo();
		String mktRcvYn = paramObj.getMktRcvYn();
		String opt2TermsAgreeYn = paramObj.getOpt2TermsAgreeYn();
		if(!YappUtil.isEmpty(mktRcvYn) || !YappUtil.isEmpty(opt2TermsAgreeYn)){
			TermsAgree param = new TermsAgree();
			param.setCntrNo(paramObj.getCntrNo());
			param.setMktRcvAgreeYn(mktRcvYn);
			cmnDao.update("mybatis.mapper.user.updateOptTerms", param);
			
			TermsAgree param2 = new TermsAgree();
			param2.setCntrNo(paramObj.getCntrNo());
			param2.setOpt2TermsAgreeYn(opt2TermsAgreeYn);
			cmnDao.update("mybatis.mapper.user.updateOptTerms", param2);
		}
		
		if(!YappUtil.isEmpty(mktRcvYn)){
			cmnDao.update("mybatis.mapper.user.updateUserSettingInfo", paramObj);
		}
		
		updateUserSettingInfo=getYappUserInfo(paramObj.getCntrNo());
		
		if(paramObj.getOpt3TermsAgreeYn() != null || paramObj.getOpt4TermsAgreeYn() != null){
			try {
				logger.info(paramObj.getCntrNo()+"|"+ paramObj.getOpt3TermsAgreeYn()+"|"+ paramObj.getOpt4TermsAgreeYn()+"|"+ paramObj.getOpt3TermsVrsn()+"|"+  paramObj.getOpt4TermsVrsn());
				shubService.callFn11268(paramObj.getCntrNo(), paramObj.getOpt3TermsAgreeYn(), paramObj.getOpt4TermsAgreeYn(), paramObj.getOpt3TermsVrsn(), paramObj.getOpt4TermsVrsn());
								//선택이용약관4 버전
			} catch (RuntimeException e) {
				logger.info("이용약관 동의 변경 error");
				
			}catch (Exception e) {
				logger.info("이용약관 동의 변경 error");
				
			}
		}
		
		try{
			List<CustAgreeInfoRetvListDtoDetail> custAgreeList = shubService.callFn11267(paramObj.getCntrNo());
			updateUserSettingInfo.getTermsAgree().setOpt3TermsAgreeYn(custAgreeList.get(1).getCustInfoAgreeYn().toString());
			updateUserSettingInfo.getTermsAgree().setOpt3TermsVrsn(custAgreeList.get(1).getAgreeVerNo().toString());
			updateUserSettingInfo.getTermsAgree().setOpt4TermsAgreeYn(custAgreeList.get(0).getCustInfoAgreeYn().toString());
			updateUserSettingInfo.getTermsAgree().setOpt4TermsVrsn(custAgreeList.get(0).getAgreeVerNo().toString());
			logger.info(custAgreeList.get(0).getCustInfoAgreeYn().toString() + "|" + custAgreeList.get(0).getAgreeVerNo().toString() + "|" + custAgreeList.get(1).getCustInfoAgreeYn().toString() + "|" + custAgreeList.get(0).getAgreeVerNo().toString() + "|");
			updateUserSettingInfo.setOpt3TermsAgreeYn(custAgreeList.get(1).getCustInfoAgreeYn().toString());	//선택이용약관3 동의여부
			updateUserSettingInfo.setOpt3TermsVrsn(custAgreeList.get(1).getAgreeVerNo().toString());			//선택이용약관3 버전
			updateUserSettingInfo.setOpt4TermsAgreeYn(custAgreeList.get(0).getCustInfoAgreeYn().toString());	//선택이용약관4 동의여부
			updateUserSettingInfo.setOpt4TermsVrsn(custAgreeList.get(0).getAgreeVerNo().toString());			//선택이용약관4 버전
		} catch (RuntimeException e) {
			logger.info("이용약관 동의 변경 후 조회 error");
			updateUserSettingInfo.getTermsAgree().setOpt3TermsAgreeYn("N");
			updateUserSettingInfo.getTermsAgree().setOpt3TermsVrsn("1.0");
			updateUserSettingInfo.getTermsAgree().setOpt4TermsAgreeYn("N");
			updateUserSettingInfo.getTermsAgree().setOpt4TermsVrsn("1.0");
			updateUserSettingInfo.setOpt3TermsAgreeYn("N");				//선택이용약관3 동의여부
			updateUserSettingInfo.setOpt3TermsVrsn("1.0")	;			//선택이용약관3 버전
			updateUserSettingInfo.setOpt4TermsAgreeYn("N");				//선택이용약관4 동의여부
			updateUserSettingInfo.setOpt4TermsVrsn("1.0");				//선택이용약관4 버전
		} catch (Exception e) {
			logger.info("이용약관 동의 변경 후 조회 error");
			updateUserSettingInfo.getTermsAgree().setOpt3TermsAgreeYn("N");
			updateUserSettingInfo.getTermsAgree().setOpt3TermsVrsn("1.0");
			updateUserSettingInfo.getTermsAgree().setOpt4TermsAgreeYn("N");
			updateUserSettingInfo.getTermsAgree().setOpt4TermsVrsn("1.0");
			updateUserSettingInfo.setOpt3TermsAgreeYn("N");				//선택이용약관3 동의여부
			updateUserSettingInfo.setOpt3TermsVrsn("1.0")	;			//선택이용약관3 버전
			updateUserSettingInfo.setOpt4TermsAgreeYn("N");				//선택이용약관4 동의여부
			updateUserSettingInfo.setOpt4TermsVrsn("1.0");				//선택이용약관4 버전
		} 
		
		return updateUserSettingInfo;
	}

	@Transactional(rollbackFor = Throwable.class)
	public UserInfo updateUserKtSettingInfo(UserInfo paramObj) throws Exception
	{
		UserInfo updateUserSettingInfo= new UserInfo();
		String mktRcvYn = paramObj.getMktRcvYn();
		String opt2TermsAgreeYn = paramObj.getOpt2TermsAgreeYn();
		if(!YappUtil.isEmpty(mktRcvYn)  || !YappUtil.isEmpty(opt2TermsAgreeYn)){
			TermsAgree param = new TermsAgree();
			param.setUserId(paramObj.getUserId());
			param.setMktRcvAgreeYn(mktRcvYn);
			cmnDao.update("mybatis.mapper.userkt.updateOptTerms", param);
			
			TermsAgree param2 = new TermsAgree();
			param2.setUserId(paramObj.getUserId());
			param2.setOpt2TermsAgreeYn(opt2TermsAgreeYn);
			cmnDao.update("mybatis.mapper.userkt.updateOptTerms", param2);
		}
		if(!YappUtil.isEmpty(mktRcvYn)){
			cmnDao.update("mybatis.mapper.userkt.updateUserKtSettingInfo", paramObj);
		}
		updateUserSettingInfo=getYappUserKtInfo(paramObj.getUserId());
		
		return updateUserSettingInfo;
	}
	
	@Transactional(rollbackFor = Throwable.class)
	public Integer updateUserKtMobileInfo(UserInfo paramObj) throws Exception
	{
		int retVal = 0; 
		//Null처리
		if(!YappUtil.isEmpty(paramObj)){
			retVal = cmnDao.update("mybatis.mapper.userkt.updateUserKtSettingInfo", paramObj);
		}
		
		return retVal;
	}
	
	/**
	 * 사용자  정보에서 요금제 코드와 모바일코드를 업데이트 한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int updateUserPpcdAndMobileCd(
			boolean existUser, boolean existUserKt, String cntrNo, String userId, String ppCd, String mobileCd, String memStatus, String pushRcvYn)
	{
		
		logger.info("=========================================================================");
		logger.info("updateUserPpcdAndMobileCd -> existUser:"+existUser);
		logger.info("updateUserPpcdAndMobileCd -> existUserKt:"+existUserKt);
		logger.info("updateUserPpcdAndMobileCd -> cntrNo:"+cntrNo);
		logger.info("updateUserPpcdAndMobileCd -> userId:"+userId);
		logger.info("updateUserPpcdAndMobileCd -> ppCd:"+ppCd);
		logger.info("updateUserPpcdAndMobileCd -> mobileCd:"+mobileCd);
		logger.info("updateUserPpcdAndMobileCd -> memStatus:"+memStatus);
		logger.info("updateUserPpcdAndMobileCd -> pushRcvYn:"+pushRcvYn);
		logger.info("=========================================================================");
		
		int insCnt = 0;
		
		if(existUser){
			Map<String, Object> paramObj = YappUtil.makeParamMap(new String[]{"cntrNo", "ppCd", "mobileCd", "memStatus"}, new String[]{cntrNo, ppCd, mobileCd, memStatus});
			insCnt = cmnDao.update("mybatis.mapper.user.updateUserPpcdAndMobileCd", paramObj);
		}
		
		if(existUserKt){
			if(existUser){
				Map<String, Object> paramObj = YappUtil.makeParamMap(new String[]{"userId", "ppCd", "mobileCd", "memStatus", "pushRcvYn"}, new String[]{userId, ppCd, mobileCd, memStatus, pushRcvYn});
				insCnt = cmnDao.update("mybatis.mapper.userkt.updateUserPpcdAndMobileCd", paramObj);
			}else{
				Map<String, Object> paramObj = YappUtil.makeParamMap(new String[]{"userId", "ppCd", "mobileCd", "memStatus"}, new String[]{userId, ppCd, mobileCd, memStatus});
				insCnt = cmnDao.update("mybatis.mapper.userkt.updateUserPpcdAndMobileCd", paramObj);
			}
		}
		
		return insCnt;
	}
	
	/**
	 * 사용자  정보에서 변경된 전화번호를 저장한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int updateUserMobileInfo(String cntrNo, String mobileNo)
	{
		Map<String, Object> paramObj = YappUtil.makeParamMap(new String[]{"cntrNo", "mobileNo"}, new String[]{cntrNo, mobileNo});
		int insCnt = cmnDao.update("mybatis.mapper.user.updateUserMobileInfo", paramObj);
		return insCnt;
	}
	
	/**
	 * 보호자 동의 정보를 추가한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int insertAdultAgree(AdultAgree paramObj)
	{
		// 발송정보 추가
		int insCnt = cmnDao.insert("mybatis.mapper.user.insertAdultAgree", paramObj);

		// 이메일 발송 TODO
		
		return insCnt;
	}
	
	/**
	 * 보호자 동의 정보를 조회한다.
	 */
	public AdultAgree getAdultAgree(String mailKey)
	{
		Map<String, Object> paramObj = YappUtil.makeParamMap(new String[]{"mailKey"}, new String[]{mailKey});

		return cmnDao.selectOne("mybatis.mapper.user.getAdultAgree", paramObj);
	}

	/**
	 * 보호자 동의 정보를 조회한다.
	 */
	public AdultAgree getAdultAgreeCntrNo(String cntrNo)
	{
		Map<String, Object> paramObj = YappUtil.makeParamMap(new String[]{"cntrNo"}, new String[]{cntrNo});

		return cmnDao.selectOne("mybatis.mapper.user.getAdultAgreeCntrNo", paramObj);
	}
	/**
	 * 보호자 동의 정보를 승인한다.
	 */
	public int apprAdultAgree(String mailKey)
	{
		Map<String, Object> paramObj = YappUtil.makeParamMap(new String[]{"mailKey"}, new String[]{mailKey});
		
		int uptCnt = cmnDao.update("mybatis.mapper.user.apprAdultAgree", paramObj);
		
		return uptCnt;
	}

	/**
	 * 보호자 동의 정보를 미동의 처리한다.
	 */
	public int cancelAdultAgree(String cntrNo)
	{
		Map<String, Object> paramObj = YappUtil.makeParamMap(new String[]{"cntrNo"}, new String[]{cntrNo});
		
		int uptCnt = cmnDao.update("mybatis.mapper.user.cancelAdultAgree", paramObj);
		
		return uptCnt;
	}
	
	/**
	 * 데이터 사용량 정보를 조회한다.
	 */
	public WsgDataUseQnt getDataUseInfo(String cntrNo, String mobileNo, String srchYmd) throws Exception
	{
		// 데이터 사용량 정보 조회
		WsgDataUseQnt dataUseQnt = wsgService.getMobileTotalUseWeb(cntrNo, mobileNo, srchYmd, null);
		return dataUseQnt;
	}

	/**
	 * 서비스 탈퇴
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int serviceOut(SvcOut paramObj) throws Exception
	{
		// 서비스 탈퇴
		UserInfo loginUser = getYappUserInfo(paramObj.getCntrNo());
		loginUser.setJoinStatus(EnumJoinStatus.G0002.getStatusCd());
		updateUserSettingInfo(loginUser);
		
		// 탈퇴정보를 추가한다.
		int insCnt = cmnDao.insert("mybatis.mapper.user.insertSvcOut", paramObj);
		insCnt +=cmnService.updateDeviceToken("", paramObj.getCntrNo(), "", "");
		insCnt +=cancelAdultAgree(paramObj.getCntrNo());
		//유휴계정정보 조회 후 삭제한다.
		UserInfo svcOutUserInfo = new UserInfo();
		svcOutUserInfo.setCntrNo(paramObj.getCntrNo());
		UserInfo sleepUserInfo = cmnDao.selectOne("mybatis.mapper.user.getSleepUserInfo", svcOutUserInfo);
		if(YappUtil.isNotEmpty(sleepUserInfo)) {
			insCnt += cmnDao.insert("mybatis.mapper.user.deleteSleepUserInfo", svcOutUserInfo);
		}
		
		// KOS 탈퇴정보를 추가한다.
		kosService.closeDbox(loginUser.getCntrNo());
		
		return insCnt;
	}
	
	@Transactional(rollbackFor = Throwable.class)
	public int serviceOutKt(SvcOut paramObj) throws Exception
	{
		// 서비스 탈퇴
		UserInfo loginUser = getYappUserKtInfo(paramObj.getUserId());
		loginUser.setJoinStatus(EnumJoinStatus.G0002.getStatusCd());
		updateUserKtSettingInfo(loginUser);
		cmnDao.update("mybatis.mapper.userkt.updateUserKtSettingInfo", loginUser);
		
		// 탈퇴정보를 추가한다.
		int insCnt = cmnDao.insert("mybatis.mapper.user.insertSvcOut", paramObj);
		insCnt +=cmnService.updateDeviceTokenKt("", paramObj.getUserId(), "", "","");

		//유휴계정정보 조회 후 삭제한다.
		UserInfo svcOutUserInfo = new UserInfo();
		svcOutUserInfo.setUserId(paramObj.getUserId());
		UserInfo sleepUserInfo = cmnDao.selectOne("mybatis.mapper.userkt.getSleepUserKtInfo", svcOutUserInfo);
		if(YappUtil.isNotEmpty(sleepUserInfo)) {
			insCnt += cmnDao.insert("mybatis.mapper.userkt.deleteSleepUserKtInfo", svcOutUserInfo);
		}
		
		return insCnt;
	}
	
	@Transactional(rollbackFor = Throwable.class)
	public int serviceOutAll(SvcOut paramObj) throws Exception
	{
		// KTID 서비스 탈퇴
		UserInfo loginUserKt = getYappUserKtInfo(paramObj.getUserId());
		loginUserKt.setJoinStatus(EnumJoinStatus.G0002.getStatusCd());
		updateUserKtSettingInfo(loginUserKt);
		cmnDao.update("mybatis.mapper.userkt.updateUserKtSettingInfo", loginUserKt);
		
		// 탈퇴정보를 추가한다.
		int insCnt = cmnDao.insert("mybatis.mapper.user.insertSvcOut", paramObj);
		insCnt +=cmnService.updateDeviceTokenKt("", paramObj.getUserId(), "", "","");

		//유휴계정정보 조회 후 삭제한다.
		UserInfo svcOutUserKtInfo = new UserInfo();
		svcOutUserKtInfo.setUserId(paramObj.getUserId());
		UserInfo sleepUserKtInfo = cmnDao.selectOne("mybatis.mapper.userkt.getSleepUserKtInfo", svcOutUserKtInfo);
		if(YappUtil.isNotEmpty(sleepUserKtInfo)) {
			insCnt += cmnDao.insert("mybatis.mapper.userkt.deleteSleepUserKtInfo", svcOutUserKtInfo);
		}
		
		// YBOX 서비스 탈퇴
		UserInfo loginUser = getYappUserInfo(paramObj.getCntrNo());
		loginUser.setJoinStatus(EnumJoinStatus.G0002.getStatusCd());
		updateUserSettingInfo(loginUser);
		
		// 탈퇴정보를 추가한다.
		//int insCnt = cmnDao.insert("mybatis.mapper.user.insertSvcOut", paramObj);
		cmnDao.insert("mybatis.mapper.user.insertSvcOut", paramObj);
		insCnt +=cmnService.updateDeviceToken("", paramObj.getCntrNo(), "", "");
		insCnt +=cancelAdultAgree(paramObj.getCntrNo());
		//유휴계정정보 조회 후 삭제한다.
		UserInfo svcOutUserInfo = new UserInfo();
		svcOutUserInfo.setCntrNo(paramObj.getCntrNo());
		UserInfo sleepUserInfo = cmnDao.selectOne("mybatis.mapper.user.getSleepUserInfo", svcOutUserInfo);
		if(YappUtil.isNotEmpty(sleepUserInfo)) {
			insCnt += cmnDao.insert("mybatis.mapper.user.deleteSleepUserInfo", svcOutUserInfo);
		}
		
		// KOS 탈퇴정보를 추가한다.
		kosService.closeDbox(loginUser.getCntrNo());
		
		return insCnt;
	}
	
	/**
	 * 초대 정보를 추가한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int insertInvt(Invitation paramObj)
	{
		return cmnDao.insert("mybatis.mapper.user.insertInvt", paramObj);
	}
	
	/**
	 * 초대 횟수를 조회한다.
	 */
	public int getInvtCnt(String cntrNo)
	{
		return cmnDao.selectOne("mybatis.mapper.user.getInvtCnt", YappUtil.makeParamMap("cntrNo", cntrNo));
	}
	
	/**
	 * KOS 에서 추가된 사용자 정보를 YAPP 에 추가한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int insertKosJoinUser() throws Exception
	{
		// KOS 에서 추가된 사용자 목록을 조회한다.
		int insCnt = 0;
		List<UserInfo> userInfoList = cmnDao.selectList("mybatis.mapper.user.getKosJoinUserList", null);
		for ( UserInfo userInfo : userInfoList )
		{
			try{
				SoapResponse184 resp = shubService.callFn184(userInfo.getCntrNo());
				userInfo.setMobileNo(resp.getMobileNo());
				userInfo.setDboxStatus("G0001");
			}catch(RuntimeException e){
				continue;
			}catch(Exception e){
				continue;
			}
			// 사용자를 추가한다.
			insCnt += cmnDao.insert("mybatis.mapper.user.insertUserInfo", userInfo);
			// 추가 완료처리한다.
			cmnDao.update("mybatis.mapper.user.updateKosJoinUser", YappUtil.makeParamMap("cntrNo", userInfo.getCntrNo()));
		}
		return insCnt;
	}
	
	/**
	 * 단말기 Id 를 조회한다.
	 */
	public JoinInfo getDeviceIdInfo(String cntrNo)
	{
		UserInfo paramUserInfo = new UserInfo();
		paramUserInfo.setCntrNo(cntrNo);
		
		// 단말기 Id 를 조회
		JoinInfo joinInfo = cmnDao.selectOne("mybatis.mapper.user.getDeviceIdInfo", paramUserInfo);
		
		return joinInfo;
	}

	/**
	 * 중복로그인 Id 를 조회한다.
	 */
	public JoinInfo getDupIdInfo(String cntrNo)
	{
		UserInfo paramUserInfo = new UserInfo();
		paramUserInfo.setCntrNo(cntrNo);
		
		// 단말기 Id 를 조회
		JoinInfo joinInfo = cmnDao.selectOne("mybatis.mapper.user.getDupIdInfo", paramUserInfo);
		
		return joinInfo;
	}
	
	/**
	 * 중복로그인 Id 를 조회한다.
	 */
	public JoinInfo getDupIdInfoKt(String userId)
	{
		UserInfo paramUserInfo = new UserInfo();
		paramUserInfo.setUserId(userId);
		
		// 단말기 Id 를 조회
		JoinInfo joinInfo = cmnDao.selectOne("mybatis.mapper.userkt.getDupIdInfoKt", paramUserInfo);
		
		return joinInfo;
	}

	/**
	 * 단말기 Id를 저장한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int updateDeviceIdInfo(String deviceId, String cntrNo) throws Exception
	{
		Map<String, Object> paramObj = YappUtil.makeParamMap(new String[]{"deviceId", "cntrNo"}, new String[]{deviceId, cntrNo});
		
		int uptCnt = cmnDao.update("mybatis.mapper.user.updateDeviceIdInfo", paramObj);
		
		return uptCnt;
	}
	
	/**
	 * 중복로그인 Id를 저장한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int updateDupIdInfo(String dupId, String cntrNo, String osTp, String userId, String appVrsn) throws Exception
	{
		Map<String, Object> paramObj = YappUtil.makeParamMap(new String[]{"dupId", "cntrNo", "osTp", "userId", "appVrsn"}, new String[]{dupId, cntrNo, osTp, userId, appVrsn});
		
		int uptCnt = cmnDao.update("mybatis.mapper.user.updateDupIdInfo", paramObj);
		
		return uptCnt;
	}
	
	/**
	 * 중복로그인 Id를 저장한다. userKt
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int updateDupIdInfoKt(String dupId, String osTp, String userId, String appVrsn) throws Exception
	{
		Map<String, Object> paramObj = YappUtil.makeParamMap(new String[]{"dupId", "osTp", "userId", "appVrsn"}, new String[]{dupId, osTp, userId, appVrsn});
		
		int uptCnt = cmnDao.update("mybatis.mapper.userkt.updateDupIdInfo", paramObj);
		
		return uptCnt;
	}
	
	/**
	 * 보호자 SMS 정보 동의를 조회한다.
	 */
	public SendSms getAuthYnCntrNo(String cntrNo) 
	{
		Map<String, Object> paramObj = YappUtil.makeParamMap(new String[]{"cntrNo"}, new String[]{cntrNo});

		return cmnDao.selectOne("mybatis.mapper.user.getAuthYnCntrNo", paramObj);
	}
	
	/**
	 * 사용자 리뷰정보 조회
	 * @param reviewInfo
	 * @return
	 */
	public ReviewInfo getUserReviewInfo(ReviewInfo reviewInfo) {
		return cmnDao.selectOne("mybatis.mapper.user.getUserReviewInfo", reviewInfo);
	}
	
	/**
	 * 사용자 리뷰정보 업데이트
	 * @param reviewInfo
	 * @return
	 */
	public int setUserReviewInfo(ReviewInfo reviewInfo) {
		return cmnDao.update("mybatis.mapper.user.setUserReviewInfo", reviewInfo);
	}
	
	
	/**
	 * 사용자  정보에서 변경된 전화번호를 저장한다.
	 */
	public int updateUserModDtInfo(String cntrNo)
	{
		Map<String, Object> paramObj = YappUtil.makeParamMap("cntrNo", cntrNo);
		int insCnt = cmnDao.update("mybatis.mapper.user.updateUserModDtInfo", paramObj);
		return insCnt;
	}
	
	public int updateUserModDtInfo(String cntrNo, String userId)
	{
		int insCnt = 0;
		
		if(YappUtil.isNotEmpty(cntrNo) && YappUtil.isNotEmpty(userId)) {
			Map<String, Object> paramObj = YappUtil.makeParamMap(new String[]{"cntrNo", "userId"}, new String[]{cntrNo, userId});
			insCnt = cmnDao.update("mybatis.mapper.user.updateUserModDtInfo", paramObj);
		}
		
		return insCnt;
	}
	
	public int updateUserKtModDtInfo(String userId)
	{
		Map<String, Object> paramObj = YappUtil.makeParamMap("userId", userId);
		int insCnt = cmnDao.update("mybatis.mapper.userkt.updateUserKtModDtInfo", paramObj);
		return insCnt;
	}
	
	@Transactional(rollbackFor = Throwable.class)
	public boolean sleepUserRestore(String cntrNo, String mobileNo, String userId, String dupId) throws Exception {
		int rtnVal = 0;
		//유휴계정여부 확인
		boolean isSleepUser = false;
		UserInfo userInfo = new UserInfo();
		userInfo.setCntrNo(cntrNo);
		userInfo.setMobileNo(mobileNo);
		if(YappUtil.isEq("null", userId) || YappUtil.isEmpty(userId)) {
			userInfo.setUserId(null);
		}else {
			userInfo.setUserId(userId);
		}
		userInfo.setDupId(dupId);
		
		try {
			if(YappUtil.isEmpty(this.getYappUserInfo(cntrNo))) {
				UserInfo sleepUserInfo = cmnDao.selectOne("mybatis.mapper.user.getSleepUserInfo", userInfo);
				if(YappUtil.isNotEmpty(sleepUserInfo)) {
					isSleepUser = true;
					
					
					if(YappUtil.isEmpty(userInfo.getUserId()) && YappUtil.isNotEmpty(sleepUserInfo.getUserId())) {
						userInfo.setUserId(sleepUserInfo.getUserId());
					}
					
				}
				
				String dboxId = "";
				if(isSleepUser) {
					dboxId = kosService.createDbox(userInfo.getCntrNo(), isSleepUser);
				
					userInfo.setDboxStatus("G0001");
					userInfo.setDboxId(dboxId);
					
					// yapp 가입
					rtnVal = cmnDao.insert("mybatis.mapper.user.insertUserInfo", userInfo);
					//유휴계정 삭제
					rtnVal += cmnDao.delete("mybatis.mapper.user.deleteSleepUserInfo", userInfo);
				}
			}
		}catch (RuntimeException e) {
			logger.error("휴면계정 복구 오류");
		}catch (Exception e) {
			logger.error("휴면계정 복구 오류");
		}
		
		return isSleepUser;
	}
	
	@Transactional(rollbackFor = Throwable.class)
	public boolean sleepUserKtRestore(String cntrNo, String mobileNo, String userId, String dupId) throws Exception {
		int rtnVal = 0;
		//유휴계정여부 확인
		boolean isSleepUser = false;
		UserInfo userInfo = new UserInfo();
		userInfo.setCntrNo(cntrNo);
		userInfo.setMobileNo(mobileNo);
		if(YappUtil.isEq("null", userId) || YappUtil.isEmpty(userId)) {
			userInfo.setUserId(null);
		}else {
			userInfo.setUserId(userId);
		}
		userInfo.setDupId(dupId);
		
		try {
			if(YappUtil.isEmpty(this.getYappUserKtInfo(userId))) {
				UserInfo sleepUserInfo = cmnDao.selectOne("mybatis.mapper.userkt.getSleepUserKtInfo", userInfo);
				if(YappUtil.isNotEmpty(sleepUserInfo)) {
					isSleepUser = true;
					
					
					if(YappUtil.isEmpty(userInfo.getUserId()) && YappUtil.isNotEmpty(sleepUserInfo.getUserId())) {
						userInfo.setUserId(sleepUserInfo.getUserId());
					}
					
				}
				
				String dboxId = "";
				if(isSleepUser) {
					/*dboxId = kosService.createDbox(userInfo.getCntrNo(), isSleepUser);
				
					userInfo.setDboxStatus("G0001");
					userInfo.setDboxId(dboxId);*/
					
					// yapp 가입
					rtnVal = cmnDao.insert("mybatis.mapper.userkt.insertUserKtInfo", userInfo);
					//유휴계정 삭제
					rtnVal += cmnDao.delete("mybatis.mapper.userkt.deleteSleepUserKtInfo", userInfo);
				}
			}
		}catch (RuntimeException e) {
			logger.error("휴면계정 복구 오류");
		}catch (Exception e) {
			logger.error("휴면계정 복구 오류");
		}
		
		return isSleepUser;
	}
	
	public UserInfo getSleepUserKtInfo(String userId) throws RuntimeException {
		UserInfo sleepUserInfo = null;
		
		UserInfo userInfo = new UserInfo();
		userInfo.setUserId(userId);
		if(YappUtil.isEmpty(this.getYappUserKtInfo(userId))) {
			sleepUserInfo = cmnDao.selectOne("mybatis.mapper.userkt.getSleepUserKtInfo", userInfo);
			if(YappUtil.isNotEmpty(sleepUserInfo)) {
				sleepUserInfo.setSleepUserYn("Y");
			}else {
				throw new YappRuntimeAuthException(cmnService.getMsg("ERR_NOT_JOIN_YAPP"));
			}
		}else {
			userInfo.setSleepUserYn("N");
			sleepUserInfo = userInfo;
		}
		
		return sleepUserInfo;
	}

	public UserInfo getSleepUserInfo(String cntrNo) throws RuntimeException {
		UserInfo sleepUserInfo = null;
		
		UserInfo userInfo = new UserInfo();
		userInfo.setCntrNo(cntrNo);
		if(YappUtil.isEmpty(this.getYappUserInfo(cntrNo))) {
			sleepUserInfo = cmnDao.selectOne("mybatis.mapper.user.getSleepUserInfo", userInfo);
			if(YappUtil.isNotEmpty(sleepUserInfo)) {
				sleepUserInfo.setSleepUserYn("Y");
			}else {
				throw new YappRuntimeAuthException(cmnService.getMsg("ERR_NOT_JOIN_YAPP"));
			}
		}else {
			userInfo.setSleepUserYn("N");
			sleepUserInfo = userInfo;
		}
		
		return sleepUserInfo;
	}
	
	/**
	 * 생년월일을 저장한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int updateUserBirthDayInfo(String cntrNo, String birthDay)
	{
		Map<String, Object> paramObj = YappUtil.makeParamMap(new String[]{"cntrNo", "birthDay"}, new String[]{cntrNo, birthDay});
		int insCnt = cmnDao.update("mybatis.mapper.user.updateUserBirthDayInfo", paramObj);
		return insCnt;
	}
	
	/**
	 * SNS 매핑정보를 조회한다.
	 */
	public SnsInfo getSnsInfo(String snsId, String snsType) 
	{
		Map<String, Object> paramObj = YappUtil.makeParamMap(new String[]{"snsId", "snsType"}, new String[]{snsId, snsType});

		return cmnDao.selectOne("mybatis.mapper.user.getSnsInfo", paramObj);
	}
	
	/**
	 * SNS 매핑정보를 조회한다.
	 */
	public SnsInfo getSnsCntrInfo(String cntrNo, String snsType) 
	{
		Map<String, Object> paramObj = YappUtil.makeParamMap(new String[]{"cntrNo", "snsType"}, new String[]{cntrNo, snsType});

		return cmnDao.selectOne("mybatis.mapper.user.getSnsCntrInfo", paramObj);
	}
	
	/**
	 * SNS 매핑정보를 등록한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int insertSnsInfo(String snsId, String snsType, String cntrNo)
	{
		Map<String, Object> paramObj = YappUtil.makeParamMap(new String[]{"snsId", "snsType", "cntrNo"}, new String[]{snsId, snsType, cntrNo});
		int insCnt = cmnDao.insert("mybatis.mapper.user.insertSnsInfo", paramObj);
		return insCnt;
	}
	
	/**
	 * SNS 매핑정보를 수정한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int updateSnsInfo(String snsId, String snsType, String cntrNo)
	{
		Map<String, Object> paramObj = YappUtil.makeParamMap(new String[]{"snsId", "snsType", "cntrNo"}, new String[]{snsId, snsType, cntrNo});
		int insCnt = cmnDao.update("mybatis.mapper.user.updateSnsInfo", paramObj);
		return insCnt;
	}
	
	/**
	 * SNS 매핑정보를 수정한다.(계약번호기준)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int updateSnsCntrInfo(String snsId, String snsType, String cntrNo)
	{
		Map<String, Object> paramObj = YappUtil.makeParamMap(new String[]{"snsId", "snsType", "cntrNo"}, new String[]{snsId, snsType, cntrNo});
		int insCnt = cmnDao.update("mybatis.mapper.user.updateSnsCntrInfo", paramObj);
		return insCnt;
	}
	
	/**
	 * SNS 매핑정보를 삭제한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int deleteSnsInfo(String snsType, String cntrNo)
	{
		Map<String, Object> paramObj = YappUtil.makeParamMap(new String[]{"snsType", "cntrNo"}, new String[]{snsType, cntrNo});
		int insCnt = cmnDao.delete("mybatis.mapper.user.deleteSnsInfo", paramObj);
		return insCnt;
	}
	
	/**
	 * SNS 매핑정보를 전부 삭제한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int deleteAllSnsInfo(String cntrNo)
	{
		Map<String, Object> paramObj = YappUtil.makeParamMap(new String[]{"cntrNo"}, new String[]{cntrNo});
		int insCnt = cmnDao.delete("mybatis.mapper.user.deleteAllSnsInfo", paramObj);
		return insCnt;
	}
	
	/**
	 * 선호도 정보를 조회한다. (혜택배너 사용)
	 */
	public String getPreferenceArrayString(String cntrNo)
	{
		return cmnDao.selectOne("mybatis.mapper.user.getPreferenceArrayString", YappUtil.makeParamMap("cntrNo", cntrNo));
	}
	
	/**
	 * 선호도 정보를 조회한다.
	 */
	public List<PreferenceInfo> getPreferenceList(String cntrNo)
	{
		Map<String, Object> paramObj = YappUtil.makeParamMap(new String[]{"cntrNo"}, new String[]{cntrNo});
		return cmnDao.selectList("mybatis.mapper.user.getPreferenceList", paramObj);
	}
	
	/**
	 * 선호도 정보를 삭제한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int deletePreferenceInfo(String cntrNo)
	{
		Map<String, Object> paramObj = YappUtil.makeParamMap(new String[]{"cntrNo"}, new String[]{cntrNo});
		int insCnt = cmnDao.delete("mybatis.mapper.user.deletePreferenceInfo", paramObj);
		return insCnt;
	}
	
	/**
	 * 선호도 정보를 등록한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int insertPreferenceInfo(int prfSeq, String cntrNo)
	{
		Map<String, Object> paramObj = YappUtil.makeParamMap(new String[]{"prfSeq", "cntrNo"}, new Object[]{prfSeq, cntrNo});
		int insCnt = cmnDao.insert("mybatis.mapper.user.insertPreferenceInfo", paramObj);
		return insCnt;
	}
	
	/**
	 * 사용자테이블 선호도 정보를 수정한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int updateUserPreferenceInfo(String prfFlag, String cntrNo)
	{
		Map<String, Object> paramObj = YappUtil.makeParamMap(new String[]{"prfFlag", "cntrNo"}, new String[]{prfFlag, cntrNo});
		int insCnt = cmnDao.update("mybatis.mapper.user.updateUserPreferenceInfo", paramObj);
		return insCnt;
	}
	
	/**
	 * 아이디에 종속된 계약번호 갯수를 조회한다.
	 */
	public int getCntrCnt(String userId)
	{
		return cmnDao.selectOne("mybatis.mapper.user.getUserIdCntrNoCount", YappUtil.makeParamMap("userId", userId));
	}
}
