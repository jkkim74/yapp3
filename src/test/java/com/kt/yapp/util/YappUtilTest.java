package com.kt.yapp.util;

import junit.framework.TestCase;

public class YappUtilTest extends TestCase {

    public void testIsBetweenDate() throws Exception {
        boolean bResult = YappUtil.isBetweenDate("202305301632", "202306011615", "yyyyMMddHHmm");
        System.out.println("result : "+bResult);
    }
}