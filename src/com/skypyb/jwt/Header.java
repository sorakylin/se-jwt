package com.skypyb.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.skypyb.cryptography.Encrypt;


/**
 * jwt 头部主要用于储存算法类型。
 * 在 web 端请求敏感资源时返回 jwt 可方便的供服务端选择对应的算法进行匹配/解密
 *
 * @author pyb
 * @time 2019-01-28
 */
public class Header {
    private Encrypt.Type encrypt;
    private String alg;//签名的算法(algorithm)
    private static final String typ = "JWT";


    protected Header(Encrypt.Type encrypt) {
        this.encrypt = encrypt;
        this.alg = encrypt.name();
    }

    public Encrypt.Type getEncrypt() {
        return encrypt;
    }

    public String toJson() {
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("alg", alg);
        objectNode.put("typ", typ);
        return objectNode.toString();
    }


}