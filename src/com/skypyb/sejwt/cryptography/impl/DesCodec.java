package com.skypyb.sejwt.cryptography.impl;


import com.skypyb.sejwt.cryptography.Codec;
import com.skypyb.sejwt.cryptography.SymmetricCodec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * DES 对称加密
 *
 * @author pyb
 * @time 2019-11-24
 */
public class DesCodec extends SymmetricCodec {

    public DesCodec() {
        super("DES/ECB/PKCS5PADDING");
    }

    @Override
    protected Key getKey(String secret) {
        KeyGenerator kgen = null;
        SecureRandom random = null;
        try {
            kgen = KeyGenerator.getInstance("DES");
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        random.setSeed(secret.getBytes());
        kgen.init(56, random);
        Key key = kgen.generateKey();
        return key;
    }

}
