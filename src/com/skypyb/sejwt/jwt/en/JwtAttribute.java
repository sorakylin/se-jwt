package com.skypyb.sejwt.jwt.en;

import java.io.Serializable;

/**
 * 标识 jwt 各个属性
 * Create by skypyb on 2019-12-13
 */
public interface JwtAttribute {

    enum Header implements JwtAttribute {
        TYP,//类型 --> "jwt"
        ALG;//签名算法
    }

    enum Payload implements JwtAttribute {
        JTI,//编号
        ISS,//签发人
        EXP, //过期时间
        SUB,//主题
        AUD,//受众
        NBF,//生效时间
        IAT;//签发时间
    }


}
