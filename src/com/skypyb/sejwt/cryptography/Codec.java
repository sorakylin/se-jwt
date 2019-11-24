package com.skypyb.sejwt.cryptography;


import com.skypyb.sejwt.cryptography.impl.*;

/**
 * 加密 or 解密的接口,并不规定只有加解密能够实现
 * 加密/哈希/散列/编码 等都可以实现这个接口
 * 若是不需要使用 key secret 的算法,secret 字段直传 null。
 *
 * @author pyb
 * @time 2019-01-29
 */
public interface Codec {

    //加密,无需秘钥的不管secret参数
    String encrypt(String str, String secret);

    //解密,不可逆加密的解密实现直接 throw UnsupportedOperationException
    String decrypt(String str, String secret);


    enum Type {
        AES(new AesCodec()),
        DES(new DesCodec()),
        DES3(new DESedeCodec()),
        HmacMD5(new HmacSHACodec("HmacMD5")),
        HS1(new HmacSHACodec("HmacSHA1")),
        HS224(new HmacSHACodec("HmacSHA224")),
        HS256(new HmacSHACodec("HmacSHA256")),
        HS384(new HmacSHACodec("HmacSHA384")),
        HS512(new HmacSHACodec("HmacSHA512")),
        Base64(new Base64Codec(java.util.Base64.getEncoder(), java.util.Base64.getDecoder())),
        Base64URL(new Base64Codec(java.util.Base64.getUrlEncoder(), java.util.Base64.getUrlDecoder())),
        Base64MIME(new Base64Codec(java.util.Base64.getMimeEncoder(), java.util.Base64.getMimeDecoder())),
        MD5(new MessageDigestCodec("MD5")),
        SHA(new MessageDigestCodec("SHA"));

        private Codec codec;

        Type(Codec codec) {
            this.codec = codec;
        }

        public Codec create() {
            return codec;
        }

    }

}


