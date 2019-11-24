package com.skypyb.sejwt.jwt;


import com.skypyb.sejwt.cryptography.Codec;

/**
 * 此类为 jwt (Json Web Token) 的建造者对象
 *
 * @author pyb
 * @time 2019-01-28
 */
public class JwtBuilder {

    private Header header;
    private Payload payload;


    public JwtBuilder header() {
        this.header = new Header(Codec.Type.HS256);
        return this;
    }

    /**
     * 自定义 jwt 方法签名(signature)的加密方式
     * 标准为 HMAC 哈希摘要,可更换为其余的加密方式( 如 AES/DES/RSA 等 )
     *
     * @param encrypt type
     * @return this
     */
    public JwtBuilder header(Codec.Type encrypt) {
        return this.header(encrypt.create(), encrypt.name());
    }


    public JwtBuilder header(Codec codec, String alg) {
        this.header = new Header(codec, alg);
        return this;
    }

    public Payload.PayloadBuilder payload() {
        return new Payload.PayloadBuilder(this);
    }

    public JwtBuilder payload(Payload payload) {
        this.payload = payload;
        return this;
    }

    /**
     * 生成 jwt 的方法
     *
     * @param secret 秘钥，用来生成 jwt 签名
     * @return jwt
     */
    public String build(String secret) {
        if (this.header == null) this.header();
        if (this.payload == null)
            throw new IllegalArgumentException("Jwt payload is null!");

        Codec codec = Codec.Type.Base64URL.create();//用于加密 json 字符串
        String header = codec.encrypt(this.header.toJson(), null);
        String payload = codec.encrypt(this.payload.toJson(), null);

        StringBuilder sb = new StringBuilder(header).append('.').append(payload);

        //方法签名
        String signature = this.header.getEncryptor().encrypt(sb.toString(), secret);

        return sb.append('.').append(signature).toString();
    }


}
