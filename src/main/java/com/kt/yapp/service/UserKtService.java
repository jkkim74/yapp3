package com.kt.yapp.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kt.yapp.common.repository.CommonDao;
import com.kt.yapp.domain.PreferenceInfo;
import com.kt.yapp.domain.ReviewInfo;
import com.kt.yapp.domain.UserInfo;
import com.kt.yapp.exception.YappException;
import com.kt.yapp.util.YappUtil;

@Service
public class UserKtService {
	private static final Logger logger = LoggerFactory.getLogger(UserKtService.class);

	@Autowired
	private CommonDao cmnDao;
	@Autowired
	private CommonService cmnService;
	
	/**
	 * kt비회선 사용자 리뷰정보 조회
	 * @param reviewInfo
	 * @return
	 */
	public ReviewInfo getUserKtReviewInfo(ReviewInfo reviewInfo) {
		return cmnDao.selectOne("mybatis.mapper.userkt.getUserKtReviewInfo", reviewInfo);
	}
	
	/**
	 * kt비회선 사용자 리뷰정보 업데이트
	 * @param reviewInfo
	 * @return
	 */
	public int setUserKtReviewInfo(ReviewInfo reviewInfo) {
		return cmnDao.update("mybatis.mapper.userkt.setUserKtReviewInfo", reviewInfo);
	}
	
	/**220406
	 * kt비회선 선호도 정보를 조회한다.
	 */
	public List<PreferenceInfo> getPreferenceListKt(String userId)
	{
		Map<String, Object> paramObj = YappUtil.makeParamMap(new String[]{"userId"}, new String[]{userId});
		return cmnDao.selectList("mybatis.mapper.userkt.getPreferenceListKt", paramObj);
	}
	
	/**220406
	 * kt비회선 선호도 정보를 삭제한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int deletePreferenceInfoKt(String userId)
	{
		Map<String, Object> paramObj = YappUtil.makeParamMap(new String[]{"userId"}, new String[]{userId});
		int insCnt = cmnDao.delete("mybatis.mapper.userkt.deletePreferenceInfoKt", paramObj);
		return insCnt;
	}
	
	/**220406
	 * kt비회선 선호도 정보를 등록한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int insertPreferenceInfoKt(int prfSeq, String userId)
	{
		Map<String, Object> paramObj = YappUtil.makeParamMap(new String[]{"prfSeq", "userId"}, new Object[]{prfSeq, userId});
		int insCnt = cmnDao.insert("mybatis.mapper.userkt.insertPreferenceInfoKt", paramObj);
		return insCnt;
	}
	
	/**220406
	 * kt비회선 사용자테이블 선호도 정보를 수정한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int updateUserPreferenceInfoKt(String prfFlag, String userId)
	{
		Map<String, Object> paramObj = YappUtil.makeParamMap(new String[]{"prfFlag", "userId"}, new String[]{prfFlag, userId});
		int insCnt = cmnDao.update("mybatis.mapper.userkt.updateUserPreferenceInfoKt", paramObj);
		return insCnt;
	}
	
	/**220426
	 * shop 회원정보를 조회한다.
	 */
	public UserInfo getShopMemGradeInfo(String userId)
	{
		Map<String, Object> paramObj = YappUtil.makeParamMap(new String[]{"userId"}, new String[]{userId});
		return cmnDao.selectOne("mybatis.mapper.userkt.getShopMemGradeInfo", paramObj);
	}
	
	/** 220523
	 * shop 연동 uuid 생성 로직
	 * @param userId
	 * @param uuid
	 * @return
	 * @throws YappException 
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int uuidCheck(String userId) throws YappException{
		Map<String, Object> paramObj = YappUtil.makeParamMap(new String[]{"userId"}, new String[]{userId});

		//user_id 존재 uuid 패스
		int userIdChk = cmnDao.selectOne("mybatis.mapper.userkt.getKtUserIdChk", paramObj);
		int insCnt = 0;

		//userId 존재 uuid insert pass
		if(userIdChk > 0){
			return 1;
		
		//userId 미존재 uuid insert
		}else{
			
			//shop연동 uuid 생성 14자리
			String uuid = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 14);

			paramObj.put("uuid", uuid);

			//uuid 중복 체크
			int uuidChk = cmnDao.selectOne("mybatis.mapper.userkt.getKtUuidChk", paramObj);

			//uuid 중복 존재
			if(uuidChk > 0){
				for(int i = 0; i < 3; i++){
					uuid = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 14);

					paramObj.put("uuid", uuid);
					uuidChk = cmnDao.selectOne("mybatis.mapper.userkt.getKtUuidChk", paramObj);

					if(uuidChk > 0){
						if(i==2){
							throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_SYS_ERROR"));
						}
						continue;
					//중복시, 재생성
					}else{
						insCnt = insertKtUuid(paramObj);
						break;
					}
				}
				
			//uuid 생성
			}else{
				//uuid insert
				insCnt = insertKtUuid(paramObj);
				
			}
		}
		
		return insCnt;
	}
	
	/**220523
	 * kt shop연동 uuid 정보를 등록한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int insertKtUuid(Map<String, Object> paramObj){
		int insCnt = cmnDao.insert("mybatis.mapper.userkt.insertKtUuid", paramObj);
		return insCnt;
	}
	
	/**220523
	 * shop 연동 uuid 정보를 조회한다.
	 */
	public UserInfo getShopUuidInfo(Map<String,Object> paramObj)
	{
		return cmnDao.selectOne("mybatis.mapper.userkt.getShopUuidInfo", paramObj);
	}
}
