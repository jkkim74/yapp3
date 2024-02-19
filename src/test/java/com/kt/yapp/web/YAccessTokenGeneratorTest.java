//package com.kt.yapp.web;
//
//import java.security.KeyFactory;
//import java.security.PublicKey;
//import java.security.spec.X509EncodedKeySpec;
//import java.util.Base64;
//import javax.crypto.Cipher;
//import org.junit.Test;
//
///**
// * @author 91303563
// * y-access-token 생성
// *
// */
//public class YAccessTokenGeneratorTest {
//
//  private static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCxO77i/aJdYGDqSOI0hkGWeRKjbUJK1jJA8xXgj+0rVKMTsybrEfxB1JRZRLuK9YHBpY97l2KnNTwT/oK9UyCZKVzOaAZIcflFcuHTKPHbylT+QjcJtfr6rh8S8UN1yKiiFf3/Nbv3SYx2cA5UHxS9A+T3bbwnvPe42bkXU6gUDQIDAQAB";
////  private static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCahSvopovGxHJ/ivlAyX1FWjzZJAz8rH/K7Yp/L1kbZCkbaP6Y1YsD3doLtbD9svACWjYWqg8rKoTIUeD5PNNj6qDmQYXqXPnXaWLk6eQpXs+wObIpCR+oYL/tpM9q6cHBhMGe61W6D3LECLzeONGTlWBJfbdR4qCHpTJJ1EF5lQIDAQAB";
//  
////  private static final String USER_ID = "f1ad8c5a09c445"; //giwon
////  private static final String USER_ID = "5fa4566fd5ca41"; //siana
////  private static final String USER_ID = "6c4da14f28a049"; //chanook0204
////  private static final String USER_ID = "add778b7ea8846"; //ljhhahah
//private static final String USER_ID = "bb07be9aeb3d4c"; //ydbox15
//
//  @Test
//  public void generateToken() {
//    
//    try {
//      byte[] bytesEncoded = Base64.getDecoder().decode(PUBLIC_KEY);
//      PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(bytesEncoded));
//      Cipher cipher = Cipher.getInstance("RSA");
//      cipher.init(Cipher.ENCRYPT_MODE, publicKey);
//      String plainText = USER_ID + ";" + String.valueOf(System.currentTimeMillis());
//      byte[] bytes = cipher.doFinal(plainText.getBytes());
//      System.out.println(Base64.getEncoder().encodeToString(bytes));
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//}
//  