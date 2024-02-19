package com.kt.yapp.web;

import java.io.IOException;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kt.yapp.annotation.NotCheckLogin;
import com.kt.yapp.domain.MemPointGet;
import com.kt.yapp.domain.NoticeMsg;
import com.kt.yapp.domain.ShopCustNoti;
import com.kt.yapp.domain.UserInfo;
import com.kt.yapp.domain.resp.ResultInfo;
import com.kt.yapp.em.EnumRsltCd;
import com.kt.yapp.exception.YShopException;
import com.kt.yapp.service.CmsService;
import com.kt.yapp.service.CommonService;
import com.kt.yapp.service.HistService;
import com.kt.yapp.service.MemPointService;
import com.kt.yapp.service.UserKtService;
import com.kt.yapp.service.UserService;
import com.kt.yapp.soap.response.SoapResponseMemPointUse;
import com.kt.yapp.util.KeyFixUtilForRyt;
import com.kt.yapp.util.YShopUtil;
import com.kt.yapp.util.YappUtil;

@RestController
public class YShopController {
    
	private static final Logger log = LoggerFactory.getLogger(YShopController.class);
  
	@Autowired
	private YShopUtil yShopUtil;
  
	@Autowired
	private CmsService cmsService;
  
	@Autowired
	private CommonService cmnService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserKtService userKtService;
	
	@Autowired
	private MemPointService memPointService;
	
	@Autowired
	private KeyFixUtilForRyt keyFixUtilRyt;
	
	@Autowired
	private HistService histService;
	
  @NotCheckLogin
  @SuppressWarnings("unchecked")
  @PostMapping(value = "/shop/userinfo", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ResponseEntity<Map<String, Object>> getShopUserInfo(@RequestParam("access_token") String accessToken, HttpServletRequest req)  {
    
    if (!StringUtils.hasText(accessToken)) {
      log.error("!@# no access_token.");
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    
    String[] chunks = accessToken.split("\\.");
    Base64.Decoder decoder = Base64.getUrlDecoder();
    
    String header = new String(decoder.decode(chunks[0]));
    String payload = new String(decoder.decode(chunks[1]));
    
    log.debug("!@# header: {}", header);
    log.debug("!@# payload: {}", payload);
    
    Map<String, Object> payloadMap = new HashMap<>();
    
    try {
      ObjectMapper mapper = new ObjectMapper();
      payloadMap = mapper.readValue(payload, Map.class);
    } catch (IOException e) {
      log.error("!@# invalid access_token payload.", e);
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    
    Object sub = payloadMap.get("sub");
    Object iat = payloadMap.get("iat");
    Object exp = payloadMap.get("exp");
    if (sub == null || iat == null || exp == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    } else {
      long now = System.currentTimeMillis()/1000;
      log.debug("!@# iat={}", iat);
      log.debug("!@# exp={}", exp);
      log.debug("!@# now={}", now);
      
      /*if (now > Long.parseLong(String.valueOf(exp)) || now < Long.parseLong(String.valueOf(iat))) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
      }*/
    }
    
    String ktid = String.valueOf(sub);
 
    UserInfo userInfo = userService.getYappUserKtInfo(ktid);
    if (userInfo == null) {
      log.error("!@# [{}] user not found.", ktid);
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    
    Map<String, Object> paramObj = new HashMap<>();
    paramObj.put("userId", ktid);
    
    UserInfo uuidInfo = userKtService.getShopUuidInfo(paramObj);
   
    Map<String, Object> res = new HashMap<>();
    
    if(uuidInfo != null){
    	histService.insertShopApiAccessLog(req, uuidInfo.getUserId());

    	res.put("id", uuidInfo.getShud()); //shop연동 uuid
    	res.put("name", YappUtil.blindNameToName(userInfo.getUserNm(), 1));
    	res.put("email", ""); //220727
    }else{
    	res.put("id", "");
    	res.put("name", "");
    	res.put("email", "");
    }
    
    return ResponseEntity.ok(res);
  }
  
  @NotCheckLogin
  @GetMapping("/shop/shopPointInfo")
  public ResultInfo<Map<String, Object>> getShopPointInfo(@RequestHeader(value = "y-access-token", required = false) String token, HttpServletRequest req) throws Exception {
    
    log.debug("!@# y-access-token={}", token);
    
    ResultInfo<Map<String, Object>> resultInfo = new ResultInfo<>();
    
    // 유효화지 않은 y-access-token일 경우 Exception throw 하지않고 userId=""로 설정됩니다.
    String uuid = yShopUtil.getUserId(token);
    log.debug("!@# userId={}", uuid);
    if (!StringUtils.hasText(uuid)) {
      // userId가 empty string일 경우 '유효하지 않은 접근' 관련 에러 처리 
		throw new YShopException("SHOP_MSG",EnumRsltCd.C401.getRsltCd(), cmnService.getMsg("NOT_VALID_TOKEN"), uuid);

    }
    
    //220523 shop 연동 uuid 추가
    Map<String, Object> paramObj = new HashMap<>();
    paramObj.put("uuid", uuid);
    UserInfo uuidInfo = userKtService.getShopUuidInfo(paramObj);

    if(uuidInfo == null){
		throw new YShopException("SHOP_MSG",EnumRsltCd.C999.getRsltCd(), cmnService.getMsg("NOT_SHOP_INFO"), "UNKNWON");

    }else{
    	histService.insertShopApiAccessLog(req, uuidInfo.getUserId());

	    // 아래 포인트조회 구현
	    //kt회원인지 확인
	    UserInfo userInfo = userService.getUserIdCntrNo(uuidInfo.getUserId());
	    Map<String, Object> map = new HashMap<String, Object>();
	
	    if(userInfo != null){
	    	//kt회원일경우에만 로얄티 조회
			MemPointGet memPointInfo = new MemPointGet();
			memPointInfo = memPointService.getMemPoint(userInfo.getCntrNo());
			
			if(memPointInfo != null){
				map.put("memPoint", Integer.parseInt(keyFixUtilRyt.decode(memPointInfo.getRmnPoint())));
				log.info("memPoint.getMemPoint:"+memPointInfo.getRmnPoint());
				log.info("memPoint.getMemPoint:"+map.get("memPoint"));
			}else{
				map.put("memPoint", -1);
	
				log.info("memPoint.getMemPoint:-1");
			}
	
			//다중회선일경우 계약번호는 어떤걸 사용해야하는지
			
	    }else{
			throw new YShopException("SHOP_MSG",EnumRsltCd.C600.getRsltCd(), cmnService.getMsg("NOT_KT_MOBILE_USER"), uuidInfo.getUserId());
	
	    }
	    resultInfo.setResultData(map);
    }
    
    return resultInfo;
  }
  
  @NotCheckLogin
  @PostMapping("/shop/shopPointUse")
  public ResultInfo<Map<String, Object>> shopPointUse(@RequestHeader(value = "y-access-token", required = false) String token, @RequestBody Map<String, Integer> map, HttpServletRequest req) throws Exception {
    
    log.debug("!@# y-access-token={}", token);
    
    ResultInfo<Map<String, Object>> resultInfo = new ResultInfo<>();
    
    // 유효화지 않은 y-access-token일 경우 Exception throw 하지않고 userId=""로 설정됩니다.
    String uuid = yShopUtil.getUserId(token);
    log.debug("!@# userId={}", uuid);
    if (!StringUtils.hasText(uuid)) {
      // userId가 empty string일 경우 '유효하지 않은 접근' 관련 에러 처리 
		throw new YShopException("SHOP_MSG",EnumRsltCd.C401.getRsltCd(), cmnService.getMsg("NOT_VALID_TOKEN"), uuid);
    }
    
    log.info("shop 포인트 사용 구현 파람 : useMemPoint : "+map.get("useMemPoint"));
    
    if(YappUtil.isEmpty(map)){
		throw new YShopException("SHOP_MSG",EnumRsltCd.C999.getRsltCd(), cmnService.getMsg("EMPTY_CONTENT"), uuid);

    }
    
    //220523 shop 연동 uuid 추가
    Map<String, Object> paramObj = new HashMap<>();
    paramObj.put("uuid", uuid);
    UserInfo uuidInfo = userKtService.getShopUuidInfo(paramObj);
    if(uuidInfo == null){
		throw new YShopException("SHOP_MSG",EnumRsltCd.C999.getRsltCd(), cmnService.getMsg("NOT_SHOP_INFO"), "UNKNWON");

    }else{
    	histService.insertShopApiAccessLog(req, uuidInfo.getUserId());

	    //kt회원인지 확인
	    UserInfo userInfo = userService.getUserIdCntrNo(uuidInfo.getUserId());
	    Map<String, Object> pointMap = new HashMap<String, Object>();
	    
	    if(userInfo != null){
			// 점검시간 체크(매일 23:50 ~ 24:10)
			Calendar curCal = Calendar.getInstance();
			String curTime = YappUtil.lpad(curCal.get(Calendar.HOUR_OF_DAY), 2, "0") + YappUtil.lpad(curCal.get(Calendar.MINUTE), 2, "0");
			if ( curTime.compareTo("2350") > -1 || curTime.compareTo("0010") < 0 ){
				throw new YShopException("SHOP_MSG",EnumRsltCd.C999.getRsltCd(),cmnService.getMsg("ERR_SYS_CHK_TIME"),uuidInfo.getUserId());		// 지금은 시스템점검시간(매일 23:50 ~ 24:10) 입니다.
			}
	    	
	    	//kt회원일경우에만 로얄티 조회
			MemPointGet memPointInfo = new MemPointGet();
			memPointInfo = memPointService.getMemPoint(userInfo.getCntrNo());
			
			log.info("memPointInfo : "+memPointInfo);
			
			if(memPointInfo != null){
				if(!keyFixUtilRyt.decode(memPointInfo.getMbrClCd()).equals("R")){
					throw new YShopException("SHOP_MSG",EnumRsltCd.C999.getRsltCd(),cmnService.getMsg("ERR_APPL_NO_MEMBER"),uuidInfo.getUserId()); //멤버십 가입자가 아닙니다. \n멤버십 가입 후 다시 시도해주세요.
				}
				
				if (Integer.parseInt(keyFixUtilRyt.decode(memPointInfo.getRmnPoint())) < map.get("useMemPoint") ){
					throw new YShopException("SHOP_MSG",EnumRsltCd.C999.getRsltCd(),cmnService.getMsg("ERR_LACK_MEMPOINT"),uuidInfo.getUserId());	// 멤버십포인트가 부족합니다.
				}
				log.info("point use  getMemId 1 : "+keyFixUtilRyt.decode(memPointInfo.getMemId()));
	
				//멤버십 사용
	//			SoapResponseMemPointUse respMemPointInfo = memPointService.useMemPoint(userInfo.getCntrNo(), keyFixUtilRyt.decode(memPointInfo.getMemId()), "YBOXSHOPPOINTCD", map.get("useMemPoint"));
				SoapResponseMemPointUse respMemPointInfo = memPointService.useShopMemPoint(userInfo.getCntrNo(), keyFixUtilRyt.decode(memPointInfo.getMemId()), "R2YBOX", map.get("useMemPoint")); //test용
				log.info("point use getRmnPoint : "+ respMemPointInfo.getRmnPoint());
				pointMap.put("rmnMemPoint", respMemPointInfo.getRmnPoint());
				
			}else{
				throw new YShopException("SHOP_MSG",EnumRsltCd.C999.getRsltCd(),cmnService.getMsg("ERR_GET_MEM"),uuidInfo.getUserId());	// 멤버십정보 조회에 실패했습니다.
	
			}
	    }else{
			throw new YShopException("SHOP_MSG",EnumRsltCd.C600.getRsltCd(), cmnService.getMsg("NOT_KT_MOBILE_USER"), uuidInfo.getUserId());
	
	    }
	    // shop 포인트 사용 구현
	//    Map<String, Object> pointMap = new HashMap<String, Object>();
	//    pointMap.put("rmnMemPoint", 10000);
	    
	    resultInfo.setResultData(pointMap);
    }

    return resultInfo;
  }
  
  @NotCheckLogin
  @PostMapping("/shop/shopCustNoti")
  public ResultInfo<Map<String, Object>> shopCustNoti(@RequestHeader(value = "y-access-token", required = false) String token, @RequestBody ShopCustNoti shopCustNoti, HttpServletRequest req) throws Exception {
    
    log.debug("!@# y-access-token={}", token);
    
    ResultInfo<Map<String, Object>> resultInfo = new ResultInfo<>();
    
    // 유효화지 않은 y-access-token일 경우 Exception throw 하지않고 userId=""로 설정됩니다.
    String uuid = yShopUtil.getUserId(token);
    log.debug("!@# userId={}", uuid);
    if (!StringUtils.hasText(uuid)) {
      // userId가 empty string일 경우 '유효하지 않은 접근' 관련 에러 처리 
        log.error("!@# no access_token.");
        
		throw new YShopException("SHOP_MSG",EnumRsltCd.C401.getRsltCd(), cmnService.getMsg("NOT_VALID_TOKEN"), uuid);
    }
    log.info("1 : "+shopCustNoti);

    log.info("shop알림함 적재 파람 : yshopNotiStatusCd : "+shopCustNoti.getYshopNotiStatusCd()+" : yshopNotiTitle : "+shopCustNoti.getYshopNotiTitle()+" : yshopNotiDetail : "+shopCustNoti.getYshopNotiDetail()+" : yshopNotiUrl : "+shopCustNoti.getYshopNotiUrl()+" : yshopNotiImgUrl : "+shopCustNoti.getYshopNotiImgUrl());
    log.info("2 : "+shopCustNoti);

    //220523 shop 연동 uuid 추가
    Map<String, Object> paramObj = new HashMap<>();
    paramObj.put("uuid", uuid);
    UserInfo uuidInfo = userKtService.getShopUuidInfo(paramObj);
    
    if(uuidInfo == null){
    	
		throw new YShopException("SHOP_MSG",EnumRsltCd.C999.getRsltCd(), cmnService.getMsg("NOT_SHOP_INFO"), "UNKNWON");

    }else{
    	histService.insertShopApiAccessLog(req, uuidInfo.getUserId());

	    // shop알림함 적재 구현
	    //TB_SHOP_NOTICE_MSG
	    NoticeMsg noticeMsg = new NoticeMsg();
	    noticeMsg.setUserId(uuidInfo.getUserId());
	    
	    if(!YappUtil.isEmpty(shopCustNoti.getYshopNotiDetail()) && !YappUtil.isEmpty(shopCustNoti.getYshopNotiTitle()) && !YappUtil.isEmpty(shopCustNoti.getYshopNotiStatusCd())){
	    	noticeMsg.setNotiMsg(shopCustNoti.getYshopNotiDetail());
	    	noticeMsg.setNotiTitle(shopCustNoti.getYshopNotiTitle());
	    	noticeMsg.setNotiTp(shopCustNoti.getYshopNotiStatusCd());
	    }else{
			throw new YShopException("SHOP_MSG",EnumRsltCd.C400.getRsltCd(), cmnService.getMsg("EMPTY_CONTENT"), uuidInfo.getUserId());
	
	    }
	    
	    noticeMsg.setImgUrl(shopCustNoti.getYshopNotiImgUrl());
	    noticeMsg.setLinkUrl(shopCustNoti.getYshopNotiUrl());
	
	   cmsService.insertShopNotiMsg(uuidInfo.getUserId(), noticeMsg);
    
    }

    return resultInfo;
  }
  
  @NotCheckLogin
  @SuppressWarnings("unchecked")
  @GetMapping("/shop/memGradeInfo")
  public ResultInfo<JSONObject> getMemGradeInfo(@RequestHeader(value = "y-access-token", required = false) String token, HttpServletRequest req) throws Exception {
    
    log.debug("!@# y-access-token={}", token);
    
    ResultInfo<JSONObject> resultInfo = new ResultInfo<>();
    
    // 유효화지 않은 y-access-token일 경우 Exception throw 하지않고 userId=""로 설정됩니다.
    String uuid = yShopUtil.getUserId(token);
    log.debug("!@# userId={}", uuid);
    if (!StringUtils.hasText(uuid)) {
      // userId가 empty string일 경우 '유효하지 않은 접근' 관련 에러 처리 
    	throw new YShopException("SHOP_MSG",EnumRsltCd.C401.getRsltCd(), cmnService.getMsg("NOT_VALID_TOKEN"), uuid);
    }
    
    //220523 shop 연동 uuid 추가
    Map<String, Object> paramObj = new HashMap<>();
    paramObj.put("uuid", uuid);
    UserInfo uuidInfo = userKtService.getShopUuidInfo(paramObj);
    
    if(uuidInfo == null){
		throw new YShopException("SHOP_MSG",EnumRsltCd.C999.getRsltCd(), cmnService.getMsg("NOT_SHOP_INFO"), "UNKNWON");

    }else{
    	
    	histService.insertShopApiAccessLog(req, uuidInfo.getUserId());
    	
	    // 회원등급 조회 구현
	    JSONObject jsonObj = new JSONObject();
	    
	    UserInfo userInfo = userKtService.getShopMemGradeInfo(uuidInfo.getUserId());
	    
	    	if(YappUtil.isNotEmpty(userInfo)){
		    	jsonObj.put("ktYn", userInfo.getKtYn());
	    		if(userInfo.getCurrentAge() > 19 && userInfo.getCurrentAge() < 30){
	    			jsonObj.put("twentysYn", "Y");
	    		}else{
	    			jsonObj.put("twentysYn", "N");
	    		}
//		    	jsonObj.put("addInfo", userInfo.getEmail());
	    		jsonObj.put("addInfo", "");
	        	jsonObj.put("mktYn", userInfo.getMktRcvYn());	
	    	}else{
		    	jsonObj.put("ktYn", "N");
	        	jsonObj.put("twentysYn", "N");	
		    	jsonObj.put("addInfo", null);
	        	jsonObj.put("mktYn", "N");	
	    	}
	//    }
	    
	    resultInfo.setResultData(jsonObj);
    }

    return resultInfo;
  }
  
}

