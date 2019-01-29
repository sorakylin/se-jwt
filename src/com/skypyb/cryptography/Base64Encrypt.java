package com.skypyb.cryptography;


import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class Base64Encrypt implements Encrypt {

    private Base64.Encoder encoder;
    private Base64.Decoder decoder;

    public Base64Encrypt(String type) {

        switch (type) {
            case "Base64": {
                this.encoder = Base64.getEncoder();
                this.decoder = Base64.getDecoder();
            }
            case "Base64URL": {
                this.encoder = Base64.getUrlEncoder();
                this.decoder = Base64.getUrlDecoder();
            }
            case "Base64MIME": {
                this.encoder = Base64.getMimeEncoder();
                this.decoder = Base64.getMimeDecoder();
            }
            default: {
                this.encoder = Base64.getEncoder();
                this.decoder = Base64.getDecoder();
            }
        }//switch end ...
    }

    @Override
    public String encrypt(String str, String secret) {
        try {
            return encoder.encodeToString(str.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public String decrypt(String str, String secret) {
        try {
            return new String(decoder.decode(str), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

}
