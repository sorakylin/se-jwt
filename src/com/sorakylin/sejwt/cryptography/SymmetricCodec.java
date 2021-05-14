package com.sorakylin.sejwt.cryptography;

import javax.crypto.Cipher;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.Base64;

public abstract class SymmetricCodec implements Codec {

    private String type;

    public SymmetricCodec(String type) {
        this.type = type;
    }

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

    protected abstract Key getKey(String secret);


    private String encrypt(String string, Key secretKey) {
        try {
            Cipher cipher = Cipher.getInstance(type);
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
            Cipher cipher = Cipher.getInstance(type);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(string)));
        } catch (GeneralSecurityException e) {
            //The key does not match
            e.printStackTrace();
        }
        return null;
    }
}
