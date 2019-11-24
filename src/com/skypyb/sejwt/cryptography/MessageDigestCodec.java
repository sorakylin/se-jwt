package com.skypyb.sejwt.cryptography;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 信息摘要
 * 不可逆
 */
public final class MessageDigestCodec implements Codec {

    private String type;

    public MessageDigestCodec(String type) {
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
        throw new UnsupportedOperationException("MessageDigest is irreversible encryption!");
    }

}
