package com.skypyb.cryptography;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 信息摘要
 * 不可逆
 */
public class MessageDigestEncrypt implements Encrypt {

    private String type;

    public MessageDigestEncrypt(String type) {
        this.type = type;
    }

    @Override
    public String encrypt(String str, String secret) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance(type);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        messageDigest.update(str.getBytes());
        byte[] digest = messageDigest.digest();

        BigInteger md5 = new BigInteger(digest);
        return md5.toString(16);
    }

    @Override
    public String decrypt(String str, String secret) {
        throw new IllegalStateException("MessageDigest is irreversible encryption!");
    }

}
