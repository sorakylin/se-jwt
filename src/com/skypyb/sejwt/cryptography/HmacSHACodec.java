package com.skypyb.sejwt.cryptography;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * 哈希算法 Hmac 实现
 * 此算法为不可逆的
 */
public final class HmacSHACodec implements Codec {
    private static HexBinaryAdapter hexBinaryAdapter = new HexBinaryAdapter();
    private String type;

    public HmacSHACodec(String type) {
        this.type = type;
    }

    public String encrypt(String str, String secret) {
        String hash = "";
        Mac sha256_HMAC = null;
        try {
            sha256_HMAC = Mac.getInstance(this.type);
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), this.type);
            sha256_HMAC.init(secret_key);
            byte[] bytes = sha256_HMAC.doFinal(str.getBytes());
            hash = hexBinaryAdapter.marshal(bytes);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return hash;
    }

    public String decrypt(String str, String secret) {
        throw new UnsupportedOperationException("HmacSHA is irreversible encryption!");
    }

}