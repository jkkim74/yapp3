package com.kt.yapp.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import com.kt.yapp.exception.YappException;



/**
 * <p><b>Description</b></p>
 * <p>Copyright (c) 2018 kt corp. All rights reserved.</p>
 * Shub Rest API Access implement
 * 2018-11-21 간편로그인 구간암호화 처리를 위한 SHUB에서 제공받은 소스(패밀리박스에 지공받음)
 * AES128/CBC/PKCS5Padding 방식
 * @FileName AppEncryptUtils.java
 * @Package com.kt.yapp.util
 * @author min™
 * @since 2018. 11. 21.
 */
@Service
public class AppEncryptUtils {
	private static final Logger logger = LoggerFactory.getLogger(AppEncryptUtils.class);
	
	private static final String ALGO = "AES";
	private static final String AES_CBC_PKCS5 = "AES/CBC/PKCS5Padding";

	//private static String aesLock;
	//private static String IV;

	@Value("${shub.rest.conn.k}")
	private String k;

	@Value("${shub.rest.conn.i}")
	public String i;

	/*@SuppressWarnings("static-access")
	@Value("${shub.rest.conn.k}")
	public void setShubRestK(String value){
		this.k = value;
	}
	
	@SuppressWarnings("static-access")
	@Value("${shub.rest.conn.i}")
	public void setShubRestI(String value){
		this.i = value;
	}*/
	/**
	 * <pre>
	 * 설명	 : AES 방식을 이용해서 입력받은 문자열을 암호화 처리한다.
	 *			BASE64 Encoding  에따른 '+' 기호 치환 문제가 있기때문에
	 *			ajax 요청이나 ,get 방식일경우 + 기호의 공백문자 치환을 처리해줘야된다.(urlencoding 처리)
	 *			form submit 에 의한 post 전송일경우 상관없음
	 * @param data 처리할 문자열
	 * @return 암호화된 문자열
	 * </pre>
	 */
	public String aesEnc(String data) {
		try {
			logger.info("k : " + k);
			logger.info("i : " + i);
			logger.info("data : " + data);
			String aesLock = YappUtil.decode(k);
			String IV = YappUtil.decode(i);
			logger.info("aesLock : " + aesLock);
			logger.info("IV : " + IV);
			Key key = new SecretKeySpec(aesLock.getBytes("8859_1"), ALGO);
			byte[] ivBytes = IV.getBytes("UTF-8");
			logger.info("ivBytes : " + ivBytes);
			Cipher c = Cipher.getInstance(AES_CBC_PKCS5);
			c.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(ivBytes));
			byte[] encVal = c.doFinal(data.getBytes("utf-8"));
			logger.info("encVal : " + encVal);
			String encryptedValue = Base64Utils.encodeToString(encVal);
			logger.info("encryptedValue : " + encryptedValue);
			return encryptedValue;
			
		} catch (RuntimeException e) {
			logger.error("SHUB AES128 aesEnc RuntimeException ERROR : " + e.getMessage());
			return"";

		} catch (UnsupportedEncodingException e) {
			logger.error("SHUB AES128 aesEnc UnsupportedEncodingException ERROR : " + e.getMessage());
			return"";

		} catch (NoSuchAlgorithmException e) {
			logger.error("SHUB AES128 aesEnc NoSuchAlgorithmException ERROR : " + e.getMessage());
			return"";

		} catch (InvalidKeyException e) {
			logger.error("SHUB AES128 aesEnc InvalidKeyException ERROR : " + e.getMessage());
			return"";

		} catch (NoSuchPaddingException e) {
			logger.error("SHUB AES128 aesEnc NoSuchPaddingException ERROR : " + e.getMessage());
			return"";

		} catch (InvalidAlgorithmParameterException e) {
			logger.error("SHUB AES128 aesEnc InvalidAlgorithmParameterException ERROR : " + e.getMessage());
			return"";

		} catch (IllegalBlockSizeException e) {
			logger.error("SHUB AES128 aesEnc IllegalBlockSizeException ERROR : " + e.getMessage());
			return"";

		} catch (BadPaddingException e) {
			logger.error("SHUB AES128 aesEnc BadPaddingException ERROR : " + e.getMessage());
			return"";
			
		} catch (Exception e) {
			logger.error("SHUB AES128 aesEnc Exception ERROR : " + e.getMessage());
			return"";
		}
	}
	/**
	 * <pre>
	 * 설명	 : AES방식으로 암호회된 문자열을 복원한다.
	 *			BASE64 Encoding  에따른 '+' 기호 치환 문제가 있기때문에
	 *			ajax 요청이나 ,get 방식일경우  기호의 공백문자 치환을 처리해줘야된다.(urlencoding 처리)
	 *			form submit 에 의한 post 전송일경우 상관없음
	 * @param encryptedData 암호화된 난수 문자열
	 * @return 복원된 문자열 값
	 * </pre>
	 */
	public String aesDec(String encryptedDataParam) {
		try {
			String aesLock = YappUtil.decode(k);
			String IV = YappUtil.decode(i);
			Key key = new SecretKeySpec(aesLock.getBytes("8859_1"), ALGO);
			byte[] ivBytes = IV.getBytes("UTF-8");
			Cipher c = Cipher.getInstance(AES_CBC_PKCS5);
			c.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(ivBytes));
			byte[] decordedValue = Base64Utils.decodeFromString(encryptedDataParam);
			byte[] decValue = c.doFinal(decordedValue);
			String decryptedValue = new String(decValue, "utf-8");

			return decryptedValue;
		
		} catch (RuntimeException e) {
			logger.error("SHUB AES128 aesDec RuntimeException ERROR : " + e.getMessage());
			return"";
			
		} catch (UnsupportedEncodingException e) {
			logger.error("SHUB AES128 aesDec UnsupportedEncodingException ERROR : " + e.getMessage());
			return"";
			
		} catch (NoSuchAlgorithmException e) {
			logger.error("SHUB AES128 aesDec NoSuchAlgorithmException ERROR : " + e.getMessage());
			return"";
			
		} catch (NoSuchPaddingException e) {
			logger.error("SHUB AES128 aesDec NoSuchPaddingException ERROR : " + e.getMessage());
			return"";
			
		} catch (InvalidKeyException e) {
			logger.error("SHUB AES128 aesDec InvalidKeyException ERROR : " + e.getMessage());
			return"";
			
		} catch (InvalidAlgorithmParameterException e) {
			logger.error("SHUB AES128 aesDec InvalidAlgorithmParameterException ERROR : " + e.getMessage());
			return"";
			
		} catch (IllegalBlockSizeException e) {
			logger.error("SHUB AES128 aesDec IllegalBlockSizeException ERROR : " + e.getMessage());
			return"";
		
		} catch (BadPaddingException e) {
			logger.error("SHUB AES128 aesDec BadPaddingException ERROR : " + e.getMessage());
			return"";
			
		} catch (Exception e) {
			logger.error("SHUB AES128 aesDec Exception ERROR : " + e.getMessage());
			return"";
		}
	}

	public String aesEnc128(String data, String ivTmp) {
		//220613 빈값 체크 추가
		if(YappUtil.isEmpty(data) || YappUtil.isEmpty(ivTmp)){
			return "";
		}
		
		try {
			String aesLock = YappUtil.decode(k);
			String IV = YappUtil.lpad(ivTmp, 16, "0");
			//IV = YappUtil.decode(ivTmp);
			logger.info("aesLock : " + aesLock);
			logger.info("IV : " + IV);
			Key key = new SecretKeySpec(aesLock.getBytes("8859_1"), ALGO);
			byte[] ivBytes = IV.getBytes("UTF-8");
			logger.info("ivBytes : " + ivBytes);
			Cipher c = Cipher.getInstance(AES_CBC_PKCS5);
			c.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(ivBytes));
			byte[] encVal = c.doFinal(data.getBytes("utf-8"));
			logger.info("encVal : " + encVal);
			String encryptedValue = Base64Utils.encodeToString(encVal);
			logger.info("encryptedValue : " + encryptedValue);
			return encryptedValue;
			
		} catch (RuntimeException e) {
			logger.error("SHUB AES128 aesEnc128 RuntimeException ERROR : " + e.getMessage());
			return"";
			
		} catch (UnsupportedEncodingException e) {
			logger.error("SHUB AES128 aesEnc128 UnsupportedEncodingException ERROR : " + e.getMessage());
			return"";
			
		} catch (NoSuchAlgorithmException e) {
			logger.error("SHUB AES128 aesEnc128 NoSuchAlgorithmException ERROR : " + e.getMessage());
			return"";
			
		} catch (NoSuchPaddingException e) {
			logger.error("SHUB AES128 aesEnc128 NoSuchPaddingException ERROR : " + e.getMessage());
			return"";
			
		} catch (InvalidKeyException e) {
			logger.error("SHUB AES128 aesEnc128 InvalidKeyException ERROR : " + e.getMessage());
			return"";
			
		} catch (InvalidAlgorithmParameterException e) {
			logger.error("SHUB AES128 aesEnc128 InvalidAlgorithmParameterException ERROR : " + e.getMessage());
			return"";
			
		} catch (IllegalBlockSizeException e) {
			logger.error("SHUB AES128 aesEnc128 IllegalBlockSizeException ERROR : " + e.getMessage());
			return"";
			
		} catch (BadPaddingException e) {
			logger.error("SHUB AES128 aesEnc128 BadPaddingException ERROR : " + e.getMessage());
			return"";
			
		} catch (Exception e) {
			logger.error("SHUB AES128 aesEnc128 Exception ERROR : " + e.getMessage());
			return"";
		}
	}
	
	public String aesEnc128(String data, String ivTmp, String apiData) {
		//220613 빈값 체크 추가
		if(YappUtil.isEmpty(data) || YappUtil.isEmpty(ivTmp)){
			return "";
		}
		
		try {
			logger.info("aesEnc128 : " );

			String aesLock = YappUtil.decode(k);
			String IV = YappUtil.lpad(ivTmp, 16, "0");
			//IV = YappUtil.decode(ivTmp);
			logger.info("aesLock : " + aesLock);
			logger.info("IV : " + IV);
			Key key = new SecretKeySpec(aesLock.getBytes("8859_1"), ALGO);
			byte[] ivBytes = IV.getBytes("UTF-8");
			logger.info("ivBytes : " + ivBytes);
			Cipher c = Cipher.getInstance(AES_CBC_PKCS5);
			c.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(ivBytes));
			byte[] encVal = c.doFinal(data.getBytes("utf-8"));
			logger.info("encVal : " + encVal);
			String encryptedValue = Base64Utils.encodeToString(encVal);
			logger.info("encryptedValue : " + encryptedValue);
			return encryptedValue;
			
		} catch (RuntimeException e) {
			logger.error("SHUB AES128 aesEnc128 RuntimeException ERROR apiData : "+apiData+" : data : "+data+" : " + e.getMessage());
//			logger.error("SHUB AES128 aesEnc128 RuntimeException ERROR apiData : "+apiData+"  : " + e.getMessage());
			return"";
			
		} catch (UnsupportedEncodingException e) {
			logger.error("SHUB AES128 aesEnc128 UnsupportedEncodingException ERROR : " + e.getMessage());
			return"";
			
		} catch (NoSuchAlgorithmException e) {
			logger.error("SHUB AES128 aesEnc128 NoSuchAlgorithmException ERROR : " + e.getMessage());
			return"";
			
		} catch (NoSuchPaddingException e) {
			logger.error("SHUB AES128 aesEnc128 NoSuchPaddingException ERROR : " + e.getMessage());
			return"";
			
		} catch (InvalidKeyException e) {
			logger.error("SHUB AES128 aesEnc128 InvalidKeyException ERROR : " + e.getMessage());
			return"";
			
		} catch (InvalidAlgorithmParameterException e) {
			logger.error("SHUB AES128 aesEnc128 InvalidAlgorithmParameterException ERROR : " + e.getMessage());
			return"";
			
		} catch (IllegalBlockSizeException e) {
			logger.error("SHUB AES128 aesEnc128 IllegalBlockSizeException ERROR : " + e.getMessage());
			return"";
			
		} catch (BadPaddingException e) {
			logger.error("SHUB AES128 aesEnc128 BadPaddingException ERROR : " + e.getMessage());
			return"";
			
		} catch (Exception e) {
			logger.error("SHUB AES128 aesEnc128 Exception ERROR : " + e.getMessage());
			return"";
		}
	}
	
	public String aesDec128(String encryptedDataParam, String ivTmp) throws Exception{
		//220613 빈값 체크 추가
		if(YappUtil.isEmpty(encryptedDataParam) || YappUtil.isEmpty(ivTmp)){
			return "";
		}
		
		try {
			logger.info("encryptedDataParam : " + encryptedDataParam);
			logger.info("ivTmp : " + ivTmp);

			String aesLock = YappUtil.decode(k);
			String IV  = "";
			if(ivTmp.length() > 16){
				IV = ivTmp.substring(0, 16);
			}else{
				IV = YappUtil.lpad(ivTmp, 16, "0");
			}
			
			//IV = YappUtil.decode(ivTmp);
			logger.info("aesLock : " + aesLock);
			logger.info("IV : " + IV);
			Key key = new SecretKeySpec(aesLock.getBytes("8859_1"), ALGO);
			byte[] ivBytes = IV.getBytes("UTF-8");
			Cipher c = Cipher.getInstance(AES_CBC_PKCS5);
			c.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(ivBytes));
			byte[] decordedValue = Base64Utils.decodeFromString(encryptedDataParam);
			byte[] decValue = c.doFinal(decordedValue);
			String decryptedValue = new String(decValue, "utf-8");

			return decryptedValue;
		} catch (RuntimeException e) {
//			logger.error("SHUB AES128 aesDec128 RuntimeException ERROR : "+ e.getMessage());
			logger.error("SHUB AES128 aesDec128 RuntimeException ERROR encryptedDataParam : "+encryptedDataParam+" : ivTmp : "+ivTmp+" : " + e.getMessage());
			throw new YappException("CHECK_MSG", "시스템 에러가 발생했습니다. 잠시 후에 다시 시도해주세요.");
		}catch (Exception e) {
//			logger.error("SHUB AES128 aesDec128 Exception ERROR : "+ e.getMessage());
			logger.error("SHUB AES128 aesDec128 Exception ERROR encryptedDataParam : "+encryptedDataParam+" : ivTmp : "+ivTmp+" : " + e.getMessage());
			throw new YappException("CHECK_MSG", "시스템 에러가 발생했습니다. 잠시 후에 다시 시도해주세요.");
		}
	}
	
	public String aesDec128(String encryptedDataParam, String ivTmp,String appVrsnOsTp) throws Exception{
		//220613 빈값 체크 추가
		if(YappUtil.isEmpty(encryptedDataParam) || YappUtil.isEmpty(ivTmp)){
			return "";
		}
		
		try {
			logger.info("encryptedDataParam : " + encryptedDataParam);
			logger.info("ivTmp : " + ivTmp);

			String aesLock = YappUtil.decode(k);
			String IV  = "";
			if(ivTmp.length() > 16){
				IV = ivTmp.substring(0, 16);
			}else{
				IV = YappUtil.lpad(ivTmp, 16, "0");
			}
			
			//IV = YappUtil.decode(ivTmp);
			logger.info("aesLock : " + aesLock);
			logger.info("IV : " + IV);
			Key key = new SecretKeySpec(aesLock.getBytes("8859_1"), ALGO);
			byte[] ivBytes = IV.getBytes("UTF-8");
			Cipher c = Cipher.getInstance(AES_CBC_PKCS5);
			c.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(ivBytes));
			byte[] decordedValue = Base64Utils.decodeFromString(encryptedDataParam);
			byte[] decValue = c.doFinal(decordedValue);
			String decryptedValue = new String(decValue, "utf-8");

			return decryptedValue;
		} catch (RuntimeException e) {
//			logger.error("SHUB AES128 aesDec128 RuntimeException ERROR : "+ e.getMessage());
			logger.error("SHUB AES128 aesDec128 RuntimeException ERROR encryptedDataParam : "+encryptedDataParam+" : ivTmp : "+ivTmp+" : " + e.getMessage() + " : appVrsnOsTp : "+appVrsnOsTp);
			throw new YappException("CHECK_MSG", "시스템 에러가 발생했습니다. 잠시 후에 다시 시도해주세요.");
		}catch (Exception e) {
//			logger.error("SHUB AES128 aesDec128 Exception ERROR : "+ e.getMessage());
			logger.error("SHUB AES128 aesDec128 Exception ERROR encryptedDataParam : "+encryptedDataParam+" : ivTmp : "+ivTmp+" : " + e.getMessage() + " : appVrsnOsTp : "+appVrsnOsTp);
			throw new YappException("CHECK_MSG", "시스템 에러가 발생했습니다. 잠시 후에 다시 시도해주세요.");
		}
	}
	
	public String aesDec128ForPwd(String encryptedDataParam, String ivTmp) throws Exception{
		try {
			String aesLock = YappUtil.decode(k);
			String IV  = "";
			if(ivTmp.length() > 16){
				IV = ivTmp.substring(0, 16);
			}else{
				IV = YappUtil.lpad(ivTmp, 16, "0");
			}
			
			//IV = YappUtil.decode(ivTmp);
			logger.info("aesLock : " + aesLock);
			logger.info("IV : " + IV);
			Key key = new SecretKeySpec(aesLock.getBytes("8859_1"), ALGO);
			byte[] ivBytes = IV.getBytes("UTF-8");
			Cipher c = Cipher.getInstance(AES_CBC_PKCS5);
			c.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(ivBytes));
			byte[] decordedValue = Base64Utils.decodeFromString(encryptedDataParam);
			byte[] decValue = c.doFinal(decordedValue);
			String decryptedValue = new String(decValue, "utf-8");

			return decryptedValue.substring(0, decryptedValue.length()-6);
		} catch (RuntimeException e) {
			logger.error("SHUB AES128ForPwd ERROR : " + e.getMessage());
			throw new YappException("CHECK_MSG", "시스템 에러가 발생했습니다. 잠시 후에 다시 시도해주세요.");
		}catch (Exception e) {
			logger.error("SHUB AES128ForPwd ERROR : " + e.getMessage());
			throw new YappException("CHECK_MSG", "시스템 에러가 발생했습니다. 잠시 후에 다시 시도해주세요.");
		}
	}
}	