package com.kt.yapp.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.util.Base64Utils;

public class RsaCipherUtil {
	/** * 1024비트 RSA 키쌍을 생성합니다. */
	public static KeyPair genRSAKeyPair() throws NoSuchAlgorithmException {
		SecureRandom secureRandom = new SecureRandom();
		KeyPairGenerator gen;
		gen = KeyPairGenerator.getInstance("RSA");
		gen.initialize(1024, secureRandom);
		KeyPair keyPair = gen.genKeyPair();
		return keyPair;
	}

	/**
	 * * Public Key로 RSA 암호화를 수행합니다. * @param plainText 암호화할 평문입니다. * @param
	 * publicKey 공개키 입니다. * @return
	 * @throws InvalidKeySpecException 
	 */
	public static String encryptRSA(String plainText, String pKey) throws NoSuchPaddingException,
			NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException {
		byte[] publicBytes = Base64.decodeBase64(pKey);
		
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey pubKey = keyFactory.generatePublic(keySpec);
		
		System.out.println("pubKey :" + pubKey);
		
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		byte[] bytePlain = cipher.doFinal(plainText.getBytes());		
		String encrypted = Base64Utils.encodeToString(bytePlain);
		return encrypted;
	}

	/**
	 * * Private Key로 RAS 복호화를 수행합니다. * * @param encrypted 암호화된 이진데이터를 base64
	 * 인코딩한 문자열 입니다. * @param privateKey 복호화를 위한 개인키 입니다. * @return * @throws
	 * Exception
	 * @throws InvalidKeySpecException 
	 */
	public static String decryptRSA(String encrypted, String pKey)
			throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException,
			IllegalBlockSizeException, UnsupportedEncodingException, InvalidKeySpecException {
		
		byte[] privateBytes = Base64.decodeBase64(pKey);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey priKey = keyFactory.generatePrivate(keySpec);
		
		System.out.println("priKey :" + priKey);
		
		Cipher cipher = Cipher.getInstance("RSA");		
		//byte[] byteEncrypted = Base64.getDecoder().decode(encrypted.getBytes());
		byte[] byteEncrypted = Base64Utils.decodeFromString(encrypted); 
		cipher.init(Cipher.DECRYPT_MODE, priKey);
		byte[] bytePlain = cipher.doFinal(byteEncrypted);
		String decrypted = new String(bytePlain, "utf-8");
		return decrypted;
	}
}