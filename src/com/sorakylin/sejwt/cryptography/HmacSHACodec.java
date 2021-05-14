package com.sorakylin.sejwt.cryptography;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


/**
 * 哈希算法 Hmac 实现
 * 此算法为不可逆的
 */
public final class HmacSHACodec implements Codec {

    private String algorithm;

    public HmacSHACodec(String algorithm) {
        this.algorithm = algorithm;
    }

    public String encrypt(String str, String secret) {

        try {

            Mac hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes("UTF-8"), algorithm);

            hmac.init(secret_key);

            byte[] array = hmac.doFinal(str.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();

            for (byte item : array) {
                sb.append(Integer.toHexString((item & 0xFF) | 0x100), 1, 3);
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return "";
    }

    public String decrypt(String str, String secret) {
        throw new UnsupportedOperationException("HmacSHA is irreversible encryption!");
    }

}