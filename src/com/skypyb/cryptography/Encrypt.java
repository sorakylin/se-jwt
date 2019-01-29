package com.skypyb.cryptography;


/**
 * 加密 or 解密的接口,并不规定只有加解密能够实现
 * 加密/哈希/散列/编码 等都可以实现这个接口
 * 若是不需要使用 key secret 的算法,secret 字段直传 null。
 *
 * @author pyb
 * @time 2019-01-29
 */
public interface Encrypt {

    //加密
    String encrypt(String str, String secret);

    //解密,不可逆加密的解密实现直接 throw IllegalStateException
    String decrypt(String str, String secret);


    enum Type {
        HmacMD5(new HmacSHAEncrypt("HmacMD5")),
        HS1(new HmacSHAEncrypt("HmacSHA1")),
        HS224(new HmacSHAEncrypt("HmacSHA224")),
        HS256(new HmacSHAEncrypt("HmacSHA256")),
        HS384(new HmacSHAEncrypt("HmacSHA384")),
        HS512(new HmacSHAEncrypt("HmacSHA512")),
        Base64(new Base64Encrypt("Base64")),
        Base64URL(new Base64Encrypt("Base64URL")),
        Base64MIME(new Base64Encrypt("Base64MIME")),
        MD5(new MessageDigestEncrypt("MD5")),
        SHA(new MessageDigestEncrypt("SHA"));

        private Encrypt encrypt;

        Type(Encrypt encrypt) {
            this.encrypt = encrypt;
        }

        public Encrypt create() {
            return encrypt;
        }

    }

}


