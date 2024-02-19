package com.kt.yapp.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kt.yapp.domain.SnsShare;
import com.kt.yapp.domain.resp.ResultInfo;
import com.kt.yapp.service.HistService;
import com.kt.yapp.util.SessionKeeper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 이력 정보 처리 컨트롤러
 */
@RestController
@Api(description="이력 정보 처리 컨트롤러")
public class HistController 
{
	@Autowired
	private HistService histService;
	
	@ApiOperation(value="SNS 공유정보 추가")
	@RequestMapping(value = "/hist/sns/share", method = RequestMethod.POST)
	@ApiImplicitParams({@ApiImplicitParam(name="ysid", value="세션ID", dataType="string", paramType="header"),@ApiImplicitParam(name="autoLogin", value="자동로그인", dataType="string", paramType="header"), @ApiImplicitParam(name="osTp", value="단말 OS 유형(G0001: Android, G0002: IOS)", dataType="string", paramType="header"), @ApiImplicitParam(name="appVrsn", value="앱 버전", dataType="string", paramType="header")})
	public ResultInfo<Integer> insertSnsShareInfo(@RequestBody SnsShare paramObj, HttpServletRequest req) throws Exception
	{
		paramObj.setCntrNo(SessionKeeper.getCntrNo(req));
		//paramObj.setUserNm(SessionKeeper.getCntrInfo(req).getUserNm());
		
		// SNS 공유하기 이력 추가
		return new ResultInfo<>(histService.insertSnsShareInfo(paramObj));
	}

}
