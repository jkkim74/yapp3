package com.kt.yapp.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;

import com.kt.yapp.domain.ContractInfo;
import com.kt.yapp.domain.SessionContractInfo;
import com.kt.yapp.domain.TermsAgree;
import com.kt.yapp.domain.UserInfo;
import com.kt.yapp.domain.req.UserInfoReq;

/**
 * Object 변환 유틸
 */
public class YappCvtUtil 
{
	public static <T> T cvt(Object src, T target)
	{
		if ( src == null ) return null;
		
		BeanUtils.copyProperties(src, target);
		return target;
	}
	
	public static List<SessionContractInfo> cvtToSessionCntrInfo(List<ContractInfo> cntrInfoList)
	{
		List<SessionContractInfo> sessionCntrInfoList = new ArrayList<>();
		for ( ContractInfo cntrInfo : cntrInfoList )
		{
			SessionContractInfo sessionCntrInfo = new SessionContractInfo();
			YappCvtUtil.cvt(cntrInfo, sessionCntrInfo);
			sessionCntrInfoList.add(sessionCntrInfo);
		}
		return sessionCntrInfoList;
	}

	public static UserInfo cvtReqToUserInfo(UserInfoReq userInfoReq)
	{
		if ( userInfoReq == null ) return null;
		
		UserInfo userInfo = new UserInfo();
		BeanUtils.copyProperties(userInfoReq, userInfo);

		if ( userInfoReq.getTermsAgree() != null ) {
			userInfo.setTermsAgree(new TermsAgree());
			BeanUtils.copyProperties(userInfoReq.getTermsAgree(), userInfo.getTermsAgree());
		}
		return userInfo;
	}

//	public static ContractInfoResp cvtCntrInfoToResp(ContractInfo cntrInfo)
//	{
//		if ( cntrInfo == null ) return null;
//		
//		ContractInfoResp cntrResp = new ContractInfoResp();
//		BeanUtils.copyProperties(cntrInfo, cntrResp);
//
//		if ( cntrInfo.getUserInfo() != null ) {
//			cntrResp.setUserInfo(new UserInfoResp());
//			BeanUtils.copyProperties(cntrInfo.getUserInfo(), cntrResp.getUserInfo());
//			if ( cntrInfo.getUserInfo().getTermsAgree() != null ) {
//				cntrResp.getUserInfo().setTermsAgree(new TermsAgreeResp());
//				BeanUtils.copyProperties(cntrInfo.getUserInfo().getTermsAgree(), cntrResp.getUserInfo().getTermsAgree());
//			}			
//		}
//		
//		if ( cntrInfo.getCallingPlan() != null ) {
//			cntrResp.setCallingPlan(new CallingPlanResp());
//			BeanUtils.copyProperties(cntrInfo.getCallingPlan(), cntrResp.getCallingPlan());
//		}
//		
//		return cntrResp;
//	}
	
//	public static List<ContractInfoResp> cvtCntrInfoToRespList(List<ContractInfo> cntrInfoList)
//	{
//		List<ContractInfoResp> cntrInfoRespList = new ArrayList<>();
//		if ( cntrInfoList != null )
//		{
//			for ( ContractInfo cntrInfo: cntrInfoList )
//			{
//				cntrInfoRespList.add(cvtCntrInfoToResp(cntrInfo));
//			}
//		}
//		
//		return cntrInfoRespList;
//	}
	
}
