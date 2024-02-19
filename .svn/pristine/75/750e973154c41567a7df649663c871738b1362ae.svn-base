package com.kt.yapp.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class YShopUtil {

  private static final Logger log = LoggerFactory.getLogger(YShopUtil.class);

  @Value("${yshop.rsa.privateKey}")
  private String base64EncodedPrivateKey;

  public String getUserId(String yAccessToken) {
    
    if (!StringUtils.hasText(yAccessToken)) {
      log.error("!@# empty y-access-token.");
      return "";
    }

    String userId = "";

    try {
      PrivateKey privateKey = getPrivateKeyFromBase64Encoded(base64EncodedPrivateKey);
      String decrypted = decrypt(yAccessToken, privateKey);

      List<String> elmt = Collections.list(new StringTokenizer(decrypted, ";")).stream()
          .map(token -> (String) token)
          .collect(Collectors.toList());

      long now = System.currentTimeMillis()/1000;
      long diff = now - Long.parseLong(elmt.get(1).substring(0, 10));
      if (elmt.size() != 2 || diff > 86400L || diff < 0L) {
        log.error("!@# invalid y-access-token.");
        return "";
      }
      
      userId = elmt.get(0);
      
    } catch (Exception e) {
      log.error("!@# failed to decrypt y-access-token.", e);
    }

    return userId;    
  }
  
  private String decrypt(String encrypted, PrivateKey privateKey) 
      throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.DECRYPT_MODE, privateKey);
    byte[] bytesEncrypted = Base64.getDecoder().decode(encrypted.getBytes());
    byte[] bytesPlain = cipher.doFinal(bytesEncrypted);
    return new String(bytesPlain, "utf-8");
  }
  
  private PrivateKey getPrivateKeyFromBase64Encoded(String encodedKey) throws InvalidKeySpecException, NoSuchAlgorithmException {
    byte[] bytesEncoded = Base64.getDecoder().decode(encodedKey);
    return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(bytesEncoded));
  }
}
