package com.kt.yapp.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kt.yapp.common.repository.CommonDao;
import com.kt.yapp.domain.GiftData;
import com.kt.yapp.domain.JoinInfo;
import com.kt.yapp.domain.RecentContact;
import com.kt.yapp.domain.YfriendsInfo;
import com.kt.yapp.em.EnumMasterNotiMsg;
import com.kt.yapp.em.EnumYn;
import com.kt.yapp.redis.RedisComponent;
import com.kt.yapp.util.AppEncryptUtils;
import com.kt.yapp.util.YappUtil;

/**
 * RctContactService.java
 * 
 * @author kkb
 * @since 2020. 08. 21.
 * @version 1.0
 * 
 * Modification Information
 * Mod Date		Modifier		Description
 * ========================================
 *
 * Copyright (c) 2018 KTDS, Inc. All Rights Reserved
 */
@Service
public class RctContactService
{
	private static final Logger logger = LoggerFactory.getLogger(RctContactService.class);
	
	@Autowired
	private CommonDao cmnDao;
	@Autowired
	private RedisComponent redisComponent;
	@Autowired
	private UserService userService;
	@Autowired
	private AppEncryptUtils appEncryptUtils;
	/**
	 * 최근 연락처 록록을 조회한다.
	 */
	public List<RecentContact> getRecentContactList(String cntrNo, HttpServletRequest req)
	{
		String osTp = req.getHeader("osTp");
		String appVrsn = req.getHeader("appVrsn");
		
		RecentContact paramObj = new RecentContact();
		paramObj.setCntrNo(cntrNo);
		List<RecentContact> contactList = cmnDao.selectList("mybatis.mapper.rctcontact.getRecentContactList", paramObj);
		if(contactList.size() > 0){
			String redisPassYn = "N";
			try{
				// redis 작동여부를 임의적으로 조회해서 상태를 알아봄.
				String test = redisComponent.get(RedisComponent.MOBILE_NO_PFX_FRD + "00011112222", RedisComponent.KEY2_DBOX_STATUS);
			}catch(RuntimeException e){
				redisPassYn = "Y";
				logger.error("============= REDIS 접속이상 =============== :  친구목록 redis DB에서 조회불가");
				//e.printStackTrace();
			}catch(Exception e){
				redisPassYn = "Y";
				logger.error("============= REDIS 접속이상 =============== :  친구목록 redis DB에서 조회불가");
				//e.printStackTrace();
			}
			if(redisPassYn.equals("N")){
				for (RecentContact rct : contactList) {
					String dboxStatus = redisComponent.get(RedisComponent.MOBILE_NO_PFX_FRD + rct.getRecvMobileNo(), RedisComponent.KEY2_DBOX_STATUS);
					String reqRcvYn = redisComponent.get(RedisComponent.MOBILE_NO_PFX_FRD + rct.getRecvMobileNo(), RedisComponent.KEY2_REQ_RCV_YN);
					String yappJoinYn = "N";
					String reqRcvYnTmp = "N";
					if ( YappUtil.isNotEmpty(dboxStatus) ) {
						yappJoinYn = EnumYn.C_Y.getValue();
						reqRcvYnTmp = reqRcvYn;
					}
					rct.setYappJoinYn(yappJoinYn);
					rct.setReqRcvYn(reqRcvYnTmp);
					
					//220608 최근연락처 모바일번호 마스킹
					rct.setMaskingRecvMobileNo(YappUtil.blindMidEndMobileNo(rct.getRecvMobileNo()));
					
					//220621 수신자 연락처 암호화
					rct.setRecvMobileNo(appEncryptUtils.aesEnc128(rct.getRecvMobileNo(), osTp + appVrsn));
				}
			}else{
				List<String> rtnMobileList = new ArrayList<String>();
				for (RecentContact rct : contactList) {
					rtnMobileList.add(rct.getRecvMobileNo());
				}
				// 가입정보 조회
				if(rtnMobileList.size() > 0){
					List<JoinInfo> joinMobileNoList = userService.getJoinInfoList(rtnMobileList);
					for (RecentContact rct : contactList) {
						String reqRcvYn = "N";
						String yappJoinYn = "N";
						for ( int i = 0; i < joinMobileNoList.size(); i++ ){
							if(rct.getRecvMobileNo().equals(joinMobileNoList.get(i).getMobileNo())){
								yappJoinYn = "Y";
								reqRcvYn = joinMobileNoList.get(i).getReqRcvYn();
							}
						}
						rct.setYappJoinYn(yappJoinYn);
						rct.setReqRcvYn(reqRcvYn);
						
						//220608 최근연락처 모바일번호 마스킹
						rct.setMaskingRecvMobileNo(YappUtil.blindMidEndMobileNo(rct.getRecvMobileNo()));
						
						//220621 수신자 연락처 암호화
						rct.setRecvMobileNo(appEncryptUtils.aesEnc128(rct.getRecvMobileNo(), osTp + appVrsn));
					}
				}
			}
		}
		return contactList;
	}
	
	/**
	 * 최근 연락처를 추가한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int insertRctContact(RecentContact recentContact) throws Exception
	{
		int insCnt = 0;
		int dupContactCnt = cmnDao.selectOne("mybatis.mapper.rctcontact.dupContactCnt", recentContact);
		if(dupContactCnt > 0){
			//이미 중복된 수신연락처가 있는 경우 삭제후 등록
			cmnDao.delete("mybatis.mapper.rctcontact.deleteRecentContact", recentContact);
			insCnt = cmnDao.insert("mybatis.mapper.rctcontact.insertRecentContact", recentContact);
		}else{
			//최근연락처가 5개이상일 경우 삭제후 등록
			List<RecentContact> rctList = cmnDao.selectList("mybatis.mapper.rctcontact.getRecentContactList", recentContact);
			if(rctList.size() > 4){
				cmnDao.delete("mybatis.mapper.rctcontact.deleteRecentContact", recentContact);
			}
			insCnt = cmnDao.insert("mybatis.mapper.rctcontact.insertRecentContact", recentContact);
		}
		
		return insCnt;
	}
}
