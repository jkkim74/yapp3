package com.kt.yapp.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kt.yapp.annotation.NotCheckLogin;
import com.kt.yapp.domain.OmBanner;
import com.kt.yapp.domain.req.OmReq;
import com.kt.yapp.em.EnumRsltCd;
import com.kt.yapp.exception.YappAuthException;
import com.kt.yapp.exception.YappException;
import com.kt.yapp.service.CommonService;
import com.kt.yapp.util.CommonCodes;
import com.kt.yapp.util.KeyFixUtil;
import com.kt.yapp.util.SessionKeeper;
import com.kt.yapp.util.YappUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

@Slf4j
@Controller
public class OmController {

    @Autowired
    private CommonService cmnService;

    @Autowired
    Environment environment;

    @Autowired
    private KeyFixUtil keyFixUtil;

    @Value("${om.banner.stat.url}")
    private String omStatUrl;

    @RequestMapping(value = "/om/banner/{zone}",method = RequestMethod.POST)
    //@NotCheckLogin
    public ModelAndView omBannerInfo(@RequestBody OmReq omReq, @PathVariable String zone,
                                     HttpServletRequest req, ModelAndView mv) throws Exception {
        String profileInfo = environment.getActiveProfiles()[0];
        String loginUserId = null;
        //String type = req.getParameter("type");
        // session 정보 체크하여 kt Id를 가져옴..
        if("local".equals(profileInfo) || "test".equals(req.getHeader("mode"))){
            loginUserId = "ydbox16";
        }else {
            if (SessionKeeper.getSdata(req) != null) {
                loginUserId = SessionKeeper.getSdata(req).getUserId();
            } else {
                log.error("omBannerInfo : 서버세션에 정보가 없습니다.");
                throw new YappAuthException(CommonCodes.LOGIN_MSG_TYPE_LOGIN_FAIL, cmnService.getMsg("ERR_NO_LOGIN"));
            }
        }
        if(!YappUtil.isBetweenDate(omReq.getBnStDt(), omReq.getBnFnsDt(), CommonCodes.OmCodes.OM_DATE_FORMAT)){
            log.error("omBannerInfo : 유효한 날짜가 아닙니다.");
            throw new YappException(CommonCodes.OmCodes.OM_MSG_TYPE_CODE,
                                    EnumRsltCd.OM700.getRsltCd(),
                                    cmnService.getMsg("OM_BNR_NOT_VALID_DATE")
            );
        }

        String strBanner = omReq.getHtml();
        if(isJSONValid(strBanner)){
            final ObjectMapper mapper = new ObjectMapper();
            OmBanner omBanner = mapper.readValue(strBanner, OmBanner.class);
            mv.addObject("bodyType", "json");
            mv.addObject("jsonBanner",omBanner);
        } else {
            throw new YappException(CommonCodes.OmCodes.OM_MSG_TYPE_CODE,
                    EnumRsltCd.OM710.getRsltCd(),
                    cmnService.getMsg("OM_BNR_NOT_VALID_JSON")
            );
            //mv.addObject("bodyType", "html");
            //mv.addObject("htmlBanner",strBanner);
        }
        mv.addObject("omReqInfo",omReq);
        mv.addObject("appVrsn",req.getHeader("appVrsn"));
        mv.addObject("encloginUserId", keyFixUtil.encode(loginUserId));
        mv.addObject("profileInfo",profileInfo);
        mv.addObject("omStatUrl",omStatUrl);
        mv.addObject("gaId",YappUtil.getUUIDStr());
        // Y데이타박스 메인 페이지
        if("yboxMain".equals(zone)){
            mv.setViewName("om/banner");
        } else if("pointdata".equals(zone) || "afterData".equals(zone) || "pulling".equals(zone)){
            mv.setViewName("om/bannerPnt");// Y데이타박스 포인트 목록..
        } else {
            mv.setViewName("om/banner"); // Y데이타박스 메인 페이지
        }

        return mv;

    }

    private static boolean isJSONValid(String jsonString){
        try {
           final ObjectMapper mapper = new ObjectMapper();
            mapper.readTree(jsonString);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

}
