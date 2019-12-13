package com.skypyb.sejwt.jwt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.skypyb.sejwt.cryptography.Codec;
import com.skypyb.sejwt.util.JsonUtil;


/**
 * jwt 头部主要用于储存算法类型。
 * 在 web 端请求敏感资源时返回 jwt 可方便的供服务端选择对应的算法进行匹配/解密
 *
 * @author pyb
 * @time 2019-01-28
 */
public final class Header {

    private static final String JWT_TYPE = "JWT";

    private Codec codec;
    private String alg;//签名的算法(algorithm)
    private String typ = JWT_TYPE;//签名的算法(algorithm)


    protected Header(Codec.Type codecType) {
        this.codec = codecType.create();
        this.alg = codecType.name();
    }

    protected Header(Codec codec, String alg) {
        this.codec = codec;
        this.alg = alg;
    }

    public Codec getEncryptor() {
        return codec;
    }

    public String toJson() {
        ObjectNode objectNode = JsonUtil.createObjectNode();
        objectNode.put("alg", alg);
        objectNode.put("typ", typ);
        return JsonUtil.toJson(objectNode).get();
    }


}