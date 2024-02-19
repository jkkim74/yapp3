package com.kt.yapp.web;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.config.location=" +
        "classpath:application-local.properties,"+
        "classpath:application.properties")
@AutoConfigureMockMvc
public class OmControllerTest{

    @Autowired
    MockMvc mockMvc;

    @Test
    public void omBannerTest() throws Exception {

        mockMvc.perform(post("/om/banner")
                        .header("Content-Type", MediaType.APPLICATION_JSON)
                        .header("Accept",MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"caVal1\": \"CMP000020237\",\n" +
                                "    \"caVal2\": \"혜택토스트팝업\",\n" +
                                "    \"caVal3\": \"OFFERAREA188\",\n" +
                                "    \"caVal4\": \"BNR000020936\",\n" +
                                "    \"html\": \"{\\n  \\\"bgColor\\\" : \\\"#3A3A3A\\\"\\n , \\\"urlNm\\\" : \\\"https://m.my.kt.com/product/s_MobilePriceView.do\\\"\\n , \\\"imageFileName\\\": \\\"https://tb.offer.onmas.kt.com/images/omscms_20230426151204.png\\\"\\n}\",\n" +
                                "    \"bnStDt\": \"20230426000000\",\n" +
                                "    \"bnFnsDt\": \"20230531235900\",\n" +
                                "    \"statCd\": \"CMP000020237\",\n" +
                                "    \"hashtagId1\": null,\n" +
                                "    \"hashtagId2\": null,\n" +
                                "    \"hashtagId3\": null\n" +
                                "}"))
                .andDo(print())
                .andExpect(status().isOk());

    }
}