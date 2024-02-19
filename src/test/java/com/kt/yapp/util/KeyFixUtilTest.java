package com.kt.yapp.util;

import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.config.location=" +
        "classpath:application-local.properties,"+
        "classpath:application.properties")
public class KeyFixUtilTest {

    @Autowired
    KeyFixUtil keyFixUtil;

    @Autowired
    private KeyFixUtilForRyt keyFixUtilRyt;

    @Ignore
    @Test
    public void testEncode() throws Exception {
        keyFixUtil.init();
        String encUserId = keyFixUtil.encode("ydbox16");
        log.info("inisafe encode user[{}]",encUserId);
    }

    @Test
    public void testDecode() throws Exception {
        keyFixUtilRyt.init();
        String decode = keyFixUtilRyt.decode("G%2FHv5z7X7UXEQfvYuOBsX2BSCT6O%2FrFRVdDkhLJ5UsAfWHVQ7dW%2FHGJy574LaHqU%0A");
        log.info("inisafe decode[{}]",decode);
    }
}