package com.skypyb.sejwt.cryptography.impl;


import com.skypyb.sejwt.cryptography.Codec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

/**
 * AES 对称加密,这里填充方式选用 PKCS5Padding
 *
 * @author pyb
 * @time 2019-01-31
 */
public final class AesCodec implements Codec {

    private static final String TYPE = "AES/ECB/PKCS5PADDING";

    @Override
    public String encrypt(String str, String secret) {
        Key key = getKey(secret);
        return encrypt(str, key);
    }

    @Override
    public String decrypt(String str, String secret) {
        Key key = getKey(secret);
        return decrypt(str, key);
    }

    private Key getKey(String secret) {
        KeyGenerator kgen = null;
        SecureRandom random = null;
        try {
            kgen = KeyGenerator.getInstance("AES");
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        random.setSeed(secret.getBytes());
        kgen.init(128, random);
        Key key = kgen.generateKey();
        return key;
    }
//    private SecretKeySpec getKey(String secret) {
//        SecretKeySpec secretKey = null;
//        try {
//            //为了安全 key 先进行消息摘要
//            String sha = Type.SHA.create().encrypt(secret, null);
//            byte[] key = sha.getBytes("UTF-8");
//            key = Arrays.copyOf(key, 16);//仅使用前128位
//            secretKey = new SecretKeySpec(key, "AES");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return secretKey;
//    }

    private String encrypt(String string, Key secretKey) {
        try {
            Cipher cipher = Cipher.getInstance(TYPE);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(string.getBytes("UTF-8")));
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String decrypt(String string, Key secretKey) {
        try {
            Cipher cipher = Cipher.getInstance(TYPE);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(string)));
        } catch (GeneralSecurityException e) {
            //The key does not match
            e.printStackTrace();
        }
        return null;
    }
}
