package com.kt.yapp.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kt.yapp.domain.GrpCode;
import com.kt.yapp.domain.RsaKeyInfo;
import com.kt.yapp.domain.resp.LoginAcctResp;
import com.kt.yapp.domain.resp.ResultInfo;
import com.kt.yapp.service.CmsService;
import com.kt.yapp.util.AppEncryptUtils;
import com.kt.yapp.util.RsaCipherUtil;
import com.kt.yapp.util.YappUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.config.location=" +
        "classpath:application-local.properties,"+
        "classpath:application.properties")
@AutoConfigureMockMvc
public class UserControllerTest2 {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    CmsService cmsService;

    @Autowired
    private AppEncryptUtils appEncryptUtils;

    /**
     * ID/PW 로그인 테스트
     * @throws Exception
     */
    @Test
    public void loginTest() throws Exception {
        //logger.info("\n==== ID/PW 로그인 : 정상 동작 확인");
        //			url = URL + "/na/user/login/accnt?userId=" + PARAM_MAP.get("userId") + "&pwd=" + PARAM_MAP.get("pwd");
        String userId = "ydbox16";
        String pwd = "new1234!";
        RsaKeyInfo resultData = getRsaPublicKeyInfo(userId).getResultData();
        String publicKey = resultData.getPublicKey();
        int keySeq = resultData.getKeySeq();
        String encPwd = RsaCipherUtil.encryptRSA(pwd, publicKey);

        mockMvc.perform(post("/na/user/login/accntusrforpwdbykey")
                .param("userId","ydbox16")
                .param("pwd",encPwd)
                .param("keySeq", String.valueOf(keySeq))
                .header("Content-Type",MediaType.APPLICATION_JSON)
                .header("Accept",MediaType.APPLICATION_JSON)
                .header("osTp","G0002")
                .header("appVrsn","3.0.4")
                .header("mobileCd","iPhone X"))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void getOneLineTest() throws Exception {
        String osTp = "G0002";
        String appVrsn = "3.0.4";
        String userId = "ydbox16";
        String pwd = "new1234!";
        RsaKeyInfo resultData = getRsaPublicKeyInfo(userId).getResultData();
        String publicKey = resultData.getPublicKey();
        int keySeq = resultData.getKeySeq();
        String encPwd = RsaCipherUtil.encryptRSA(pwd, publicKey);

        MvcResult mvcResult = mockMvc.perform(post("/na/user/login/accntusrforpwdbykey")
                        .param("userId", "ydbox16")
                        .param("pwd", encPwd)
                        .param("keySeq", String.valueOf(keySeq))
                        .header("Content-Type", MediaType.APPLICATION_JSON)
                        .header("Accept", MediaType.APPLICATION_JSON)
                        .header("osTp", osTp)
                        .header("appVrsn", appVrsn)
                        .header("mobileCd", "iPhone X"))
                //.andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        System.out.println("#######################################################");
        System.out.println(contentAsString);
        System.out.println("#######################################################");
        ResultInfo<LoginAcctResp> resultInfo = new ResultInfo<>();
        ObjectMapper objectMapper = new ObjectMapper();
        ResultInfo<Map> resultInfo1 = objectMapper.readValue(contentAsString, ResultInfo.class);


        Map<String,Object> resultData1 = resultInfo1.getResultData();
        List<Map<String, String>> cntrInfoList = (List<Map<String, String>>) resultData1.get("cntrInfoList");

        String encCntrNo = "";
        if(cntrInfoList.size() == 1) {
            //cntrInfoList.forEach(c -> System.out.println(c));
            String cntrNo = cntrInfoList.get(0).get("cntrNo");
            encCntrNo= appEncryptUtils.aesEnc128(cntrNo, osTp+appVrsn);
            //encCntrNo = RsaCipherUtil.encryptRSA(cntrNo, publicKey);
        }

        mockMvc.perform(post("/na/user/oneline")
                        .param("cntrNo", encCntrNo)
                        .header("Content-Type", MediaType.APPLICATION_JSON)
                        .header("Accept", MediaType.APPLICATION_JSON)
                        .header("osTp", osTp)
                        .header("appVrsn", appVrsn)
                        .header("osVrsn", "13")
                        .header("autoLogin", false)
                        .header("mobileCd", "iPhone X"))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void getOneLineTest_2() throws Exception {

    }

    /**
     * 공개키가져오기...
     * @param userId
     * @return
     * @throws Exception
     */
    public ResultInfo<RsaKeyInfo> getRsaPublicKeyInfo(String userId) throws Exception
    {
        GrpCode grpCode_rsa = cmsService.getCodeNm("RSA_GRP_CODE", "RSA01");
        int rsa_count = Integer.parseInt(grpCode_rsa.getCodeNm());

        SecureRandom tmpRandom = new SecureRandom();
        tmpRandom.setSeed(new java.util.Date().getTime());

        int index = (int)(31+rsa_count*tmpRandom.nextDouble());

        //int index = (int)(31+rsa_count*Math.random());

        RsaKeyInfo rsaKeyInfo = cmsService.getRsaPublicKeyInfo(index);

        //220712
        if(!YappUtil.isEmpty(userId)){
            cmsService.LoginKeyLogic(userId, rsaKeyInfo.getKeySeq(), "N");
        }

        return new ResultInfo<>(rsaKeyInfo);
    }
}
