package com.kt.yapp.util;

import junit.framework.TestCase;
import org.junit.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class RsaCipherUtilTest{

    @Test
    public void rsaEncodingTest() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
        String pwd = "new1234!";
        String pubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAm4tSnLmwxwEh8OM6Q1jAWC08r1HSePwPGTl05P91yuEFRZDftfQsksxH+8O4AQDVAkpz3hB4/cZG6DKLUkUwGa9aPdi5OYiSTxaw0/OE7byKkzPqrl5aROtL0HN0uj5jzrkJTsRkA8M8yDsG37Dnjc5U4an2TKUcrvleU7Tvjk5uu8E4uj54nr420iZKPJEtrXPlTiJ2pOzcYZTctjshlDYJjKSBa5vDgK8lmAhPSeohSFGo4mm73FuGIb/xLOeihnbc329EMbX7GC8OHhJSaSUVuWOtDaNfO8xGSzj1VG8nykq3WQmi7vUg6yPn/jMXJfEoMeeS2EQx/dnUFtBXKwIDAQAB";
        String rsaEncodingText = RsaCipherUtil.encryptRSA(pwd,pubKey);
        System.out.println("rsaEncodingInfo : " + rsaEncodingText);
    }

}