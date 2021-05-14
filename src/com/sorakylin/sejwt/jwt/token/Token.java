package com.sorakylin.sejwt.jwt.token;

import com.sorakylin.sejwt.cryptography.Codec;
import com.sorakylin.sejwt.jwt.en.JwtAttribute;

import java.util.Optional;

public interface Token {

    /**
     * 获取Jwt的头部Json
     *
     * @return header of json
     */
    String getHeaderJson();

    /**
     * 获取Jwt的有效负载Json
     *
     * @return header of json
     */
    String getPayloadJson();

    /**
     * 指定 JwtAttribute 来获取 Jwt属性的具体值
     *
     * @param jwtAttribute
     * @return string of attribute value
     */
    Optional<String> getAttributeValue(JwtAttribute jwtAttribute);

    /**
     * 指定 attribute 来获取 Jwt属性的具体值
     * 相比于{@link this#getAttributeValue(JwtAttribute)}而言,此方法可以获取自定义参数
     *
     * @param attribute
     * @return string of attribute value
     */
    Optional<String> getAttributeValue(String attribute);

    /**
     * 设置好签名的秘钥
     *
     * @param secret 签名秘钥
     * @return This token
     */
    Token signatureSecret(String secret);

    /**
     * 指定在效验时使用的编解码器
     *
     * @param codec 编解码器
     * @return This token
     */
    Token useSpecifiedCodec(Codec codec);

    /**
     * 效验令牌
     *
     * @return 通过签名效验并且令牌未过期
     */
    boolean validateToken();

    /**
     * 判断令牌是否是正确的
     * 只要格式正确并通过签名效验即可
     *
     * @return check result
     */
    boolean tokenIsCorrect();

    /**
     * 判断令牌是否是过期的
     *
     * @return true is expired
     */
    boolean tokenHasExpired();

}
