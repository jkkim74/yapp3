package com.kt.yapp.lamp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.gson.Gson;
import com.kt.yapp.domain.AuthLogin;
import com.kt.yapp.domain.LampMenu;
import com.kt.yapp.domain.RsaKeyInfo;
import com.kt.yapp.domain.resp.ResultInfo;
import com.kt.yapp.em.EnumRsltCd;
import com.kt.yapp.service.CmsService;
import com.kt.yapp.util.RsaCipherUtil;
import com.kt.yapp.util.SessionKeeper;
import com.kt.yapp.util.YappUtil;


@Component
public class LampLogUtil 
{
	@Autowired
	private CmsService cmsService;
	
	@Value("${lamp.service.name}")
    private String lampServiceName;
	
	@Value("${lamp.server.type}")
    private String lampServerType;
	
	@Value("${lamp.log.path}")
    private String lampLogPath;
	
	@Value("${lamp.log.servicedomain}")
    private String lampLogServiceDomain;
	
	private LampMenu lampMenuInfo;
	
	private static final Logger logger = LoggerFactory.getLogger(LampLogUtil.class);
	
	public LampLogUtil() {
		lampMenuInfo = new LampMenu();
	}
	
	public void setLampMenuInfo(LampMenu lampMenuInfo) {
		this.lampMenuInfo = lampMenuInfo;
	}
	
	public LampMenu getLampMenuInfo() {
		return this.lampMenuInfo;
	}
	
	public void lampAccessCall() {
		try {
			HttpServletRequest req = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
			
			String LampHostName = YappUtil.getServerHostName();
			//220718
			if(LampHostName.contains(".")){
				String[] LampHostNameArry = LampHostName.split("\\.");
				if(LampHostNameArry.length > 0){
					LampHostName = LampHostNameArry[0];
				}
				logger.info("LampHostName 1 >>>" + LampHostName);
			}

			String ServiceName = lampServiceName;
			String isLampUrl = "N";
			
			//220713 ip userAgent
			String serverIp = InetAddress.getLocalHost().getHostAddress();
			String userAgent = req.getHeader("User-Agent");
			
			logger.info("userAgent 1 >>>" + userAgent);

			if(userAgent.contains(";")){
				String[] userAgentArry = userAgent.split(";");
				
				if(userAgentArry.length > 2){
					userAgent = userAgentArry[2];
					userAgent = userAgent.replace(")", ",");
				}
			}
			
			
			
			logger.info("lampMenuInfo >>>" + lampMenuInfo.toString());
			if(YappUtil.isEq("IN_REQ", lampMenuInfo.getLogType())) {
				isLampUrl = "N";
				LampMenu reqLampMenuInfo = new LampMenu();
				reqLampMenuInfo.setLampUrl(lampMenuInfo.getLampUrl());
				reqLampMenuInfo.setLampService(lampServerType);
				//20220627
				reqLampMenuInfo.setServiceDomain(lampLogServiceDomain);
				
				//logger.info("reqLampMenuInfo :::" + reqLampMenuInfo.toString());
				LampMenu lampMenuRlt = LampMenuHelper.getInstance().getMenuInfo(reqLampMenuInfo);
				
				if(YappUtil.isNotEmpty(lampMenuRlt)) {
					isLampUrl = "Y";
					lampMenuInfo.setIsLampUrl("Y");
					this.copyLampMenu(lampMenuRlt);
				}
			}else {
				isLampUrl = lampMenuInfo.getIsLampUrl();
			}
			
			//logger.info("isLampUrl :" + isLampUrl);
			
			//lamp request 파일 저장
			if(YappUtil.isEq("Y", isLampUrl)) {
				LogStandard standard = new LogStandard();
				String userId = "";
				if(SessionKeeper.getSdata(req) != null){
					userId = YappUtil.isEmpty(SessionKeeper.getSdata(req).getUserId()) ? "" : SessionKeeper.getSdata(req).getUserId();
				}
				
				if((YappUtil.isEq("/na/user/login/newsimple", lampMenuInfo.getLampUrl())) || (YappUtil.isEq("/na/user/login/accntusrforpwdbykey", lampMenuInfo.getLampUrl()))){
					userId = YappUtil.isEmpty(req.getParameter("userId")) ? "" : req.getParameter("userId");
				}
				
				standard.setTimestamp(YappUtil.getCurDate("yyyy-MM-dd HH:mm:ss.SSS", Calendar.DATE, 0));
				standard.setService(ServiceName);
				standard.setOperation(lampMenuInfo.getLampNm());
				standard.setTransactionId(LampHostName+"_"+lampMenuInfo.getTransactionId());
				standard.setLogType(lampMenuInfo.getLogType());
				standard.setUrl(lampMenuInfo.getLampUrl());
				standard.setHost(new LOGSTANDARDHost());
				standard.getHost().setIp(serverIp);	
				standard.getHost().setName(LampHostName);
				if(YappUtil.isEq("IN_RES", lampMenuInfo.getLogType())) {
					standard.setResponse(new LOGSTANDARDResponse());
					if(YappUtil.isEq(EnumRsltCd.C200.getRsltCd(), lampMenuInfo.getResultCd())) {
						standard.getResponse().setType("I");
					}else if(YappUtil.isEq(EnumRsltCd.C999.getRsltCd(), lampMenuInfo.getResultCd())) {
						standard.getResponse().setType("S");
					}else {
						standard.getResponse().setType("E");
					}
					standard.getResponse().setCode(lampMenuInfo.getResultCd());
					standard.getResponse().setDesc(lampMenuInfo.getResultMsg());
				}
				standard.setUser(new LOGSTANDARDUser());
				standard.getUser().setAgent(userAgent);
				standard.getUser().setId(userId);
				standard.getUser().setIp(req.getRemoteAddr());
				standard.setSecurity(new LOGSTANDARDSecurity());
				standard.getSecurity().setEvent(lampMenuInfo.getLampEvent());
				standard.getSecurity().setType(lampMenuInfo.getLampType());
				
				//20220627
				standard.setServiceDomain(lampLogServiceDomain);
				
				//20220714 휴대폰인증
				if(YappUtil.isEq("AUTH", lampMenuInfo.getLampType())){
					standard.setOperation(lampMenuInfo.getLampOperation());
					
					String mobileNo = null;

					mobileNo = lampMenuInfo.getMobileNo();
					logger.info("mobileNo : "+mobileNo);
					standard.getUser().setId(mobileNo);

					standard.getSecurity().setTarget(mobileNo);
					standard.getSecurity().setDetail("본인확인");
				}
				
				logger.info("standard : "+standard);
				
				Gson gson = new Gson();
				String lampLog = gson.toJson(standard);
				//logger.info("lampLog :" + lampLog);
				//this.lampAccessLog(lampMenuInfo.getLogType(), new Gson().toJson(standard));
				this.lampAccessLog(lampMenuInfo.getLogType(), lampLog);
			}
		} catch (RuntimeException | IOException e) {
			//logger.error("error :" + e.getMessage());
			logger.error("error :");
		}
	}	
	
	private void copyLampMenu(LampMenu lampMenu) throws RuntimeException {
		lampMenuInfo.setLampSeq(lampMenu.getLampSeq());
		lampMenuInfo.setLampNm(lampMenu.getLampNm());
		lampMenuInfo.setLampUrl(lampMenu.getLampUrl());
		lampMenuInfo.setLampOperation(lampMenu.getLampOperation());
		lampMenuInfo.setLampType(lampMenu.getLampType());
		lampMenuInfo.setLampEvent(lampMenu.getLampEvent());
		lampMenuInfo.setLampService(lampMenu.getLampService());
		lampMenuInfo.setUseYn(lampMenu.getUseYn());
		lampMenuInfo.setServiceDomain(lampMenu.getServiceDomain()); //20220627
	} 
	
	public boolean isLampMenuCheck(String lampUrl) throws RuntimeException {
		boolean isLampCheck = false;
		
		if(YappUtil.isEq("Y", lampMenuInfo.getIsLampUrl())) {
			LampMenu paramObj = new LampMenu();
			paramObj.setLampService(lampServerType);
			paramObj.setLampUrl(lampUrl);
			isLampCheck = LampMenuHelper.getInstance().getLampMenuCheck(paramObj);
		}
		
		return isLampCheck;
	}
	
	public LampMenu beforeLampLog(String accClassName, String lampUrl, ProceedingJoinPoint joinPoint) {
		try {
			
//			String apiUrl = YappUtil.nToStr(req.getRequestURI());

			
			//logger.info("url:" + lampUrl + ",isLampCheck :::" + isLampCheck);
			lampMenuInfo = new LampMenu();
			lampMenuInfo.setLampUrl(lampUrl);
			lampMenuInfo.setLampService(lampServerType);
			
			lampMenuInfo.setLogType("IN_REQ");
			lampMenuInfo.setTransactionId(YappUtil.getUUIDStr());
			
			//20220627
			lampMenuInfo.setServiceDomain(lampLogServiceDomain);
			//logger.info("lampMenuInfo :::" + lampMenuInfo.toString());
			
			//220718
			if((YappUtil.isEq("/na/user/login/authreqbykey", lampUrl))){
				CodeSignature codeSignature = (CodeSignature)joinPoint.getSignature();
				String[] parameterNames = codeSignature.getParameterNames();
				Object[] args = joinPoint.getArgs();
				Map<String, Object> params = new HashMap<>();
				for(int i = 0; i < parameterNames.length; i++){
					params.put(parameterNames[i], args[i]);
				}
				
				AuthLogin authLogin = (AuthLogin) params.get("authLogin");
				RsaKeyInfo rsaKeyInfo = cmsService.getRsaPublicKeyInfo(authLogin.getKeySeq());
				String decoded_mobileNo = RsaCipherUtil.decryptRSA(authLogin.getAuthMobileNo(), rsaKeyInfo.getPrivateKey());

				lampMenuInfo.setMobileNo(decoded_mobileNo);
				logger.info("before lampMenuInfo : "+lampMenuInfo);

			}
			
			
			this.lampAccessCall();
		} catch(RuntimeException e) {
			logger.error("lamp error:" + e.getMessage());
		}catch(Exception e) {
			logger.error("lamp error:" + e.getMessage());
		}
		return lampMenuInfo;
	}
	
	public LampMenu afterLampLog(Object result) {
		//logger.info("lampMenuInfo.getLampUrl():" + lampMenuInfo.getLampUrl() + ", isLampMenuCheck:"+isLampMenuCheck);
		
		try {
			boolean isLampMenuCheck = this.isLampMenuCheck(lampMenuInfo.getLampUrl());
			
			if(isLampMenuCheck) {
				lampMenuInfo.setLogType("IN_RES");
				if(result != null) {
					if(result instanceof ResultInfo) {
						lampMenuInfo.setResultCd(((ResultInfo)result).getResultCd());
						lampMenuInfo.setResultMsg(((ResultInfo)result).getResultMsg());
					}else {
						lampMenuInfo.setResultCd(EnumRsltCd.C200.getRsltCd());
						lampMenuInfo.setResultMsg("SUCCESS");
					}
				}
				
				this.lampAccessCall();
			}else {
				lampMenuInfo.setIsLampUrl("N");
			}
		} catch(RuntimeException e) {
			logger.error("lamp error:" + e.getMessage());
		} catch(Exception e) {
			logger.error("lamp error:" + e.getMessage());
		}
		
		return lampMenuInfo;
	}
	
	
	public LampMenu throwLampLog(String resultMsg) {
		boolean isLampMenuCheck = false;
		
		try {
			if(YappUtil.isEq("Y", lampMenuInfo.getIsLampUrl())) {
				isLampMenuCheck = this.isLampMenuCheck(lampMenuInfo.getLampUrl());
				
				if(isLampMenuCheck) {
					lampMenuInfo.setLogType("IN_RES");
					lampMenuInfo.setResultCd(EnumRsltCd.C999.getRsltCd());
					lampMenuInfo.setResultMsg(resultMsg);
					this.lampAccessCall();
				}
			}
		} catch (RuntimeException e) {
			if(YappUtil.isEq("Y", lampMenuInfo.getIsLampUrl())) {
				lampMenuInfo.setLogType("IN_RES");
				lampMenuInfo.setResultCd(EnumRsltCd.C999.getRsltCd());
				lampMenuInfo.setResultMsg(e.getMessage());
				this.lampAccessCall();
			}
		} catch (Exception e) {
			if(YappUtil.isEq("Y", lampMenuInfo.getIsLampUrl())) {
				lampMenuInfo.setLogType("IN_RES");
				lampMenuInfo.setResultCd(EnumRsltCd.C999.getRsltCd());
				lampMenuInfo.setResultMsg(e.getMessage());
				this.lampAccessCall();
			}
		} 
		
		return lampMenuInfo;
	}
	
	/**
	 * lamp 접속 로그 파일 저장
	 * @param logType : IN_REQ, IN_RES
	 * @param log
	 */
	public void lampAccessLog(String logType, String log) throws IOException {
		String filePath = lampLogPath;
		//String fileName = "Lamp_"+logType+".log." + YappUtil.getCurDate("yyyyMMdd", Calendar.DATE, 0);
		String fileName = "ybox_security.log";
		//logger.info("filePath:" + filePath + ",fileName : " + fileName);
		File dir = new File(filePath);
		if(!dir.exists()) {
			dir.mkdirs();
		}
		
		File file = new File(filePath+fileName);
		FileWriter fw;
		fw = new FileWriter(file, true);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);
		
		out.println(log);
		out.close();
		
	}
}