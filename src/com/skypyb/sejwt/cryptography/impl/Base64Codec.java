package com.skypyb.sejwt.cryptography.impl;


import com.skypyb.sejwt.cryptography.Codec;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public final class Base64Codec implements Codec {

    private Base64.Encoder encoder;
    private Base64.Decoder decoder;

    public Base64Codec() {
        this.encoder = Base64.getEncoder();
        this.decoder = Base64.getDecoder();
    }

    public Base64Codec(Base64.Encoder encoder, Base64.Decoder decoder) {
        this.encoder = encoder;
        this.decoder = decoder;
    }

    @Override
    public String encrypt(String str, String secret) {
        try {
            return encoder.encodeToString(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String decrypt(String str, String secret) {
        try {
            return new String(decoder.decode(str), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
