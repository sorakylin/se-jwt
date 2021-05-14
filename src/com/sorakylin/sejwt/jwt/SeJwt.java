package com.sorakylin.sejwt.jwt;

import com.sorakylin.sejwt.jwt.token.DefaultToken;
import com.sorakylin.sejwt.jwt.token.EmptyToken;
import com.sorakylin.sejwt.jwt.token.Token;

import java.util.Objects;

/**
 * 用于对 Token 进行操作的工具类
 * <p>
 * Create by sorakylin on 2019-12-13
 */
public final class SeJwt {


    public static Token asToken(String jwt) {
        Objects.requireNonNull(jwt);

        String[] jwtAttr = jwt.split("\\.");
        if (!Objects.equals(jwtAttr.length, 3)) return EmptyToken.getInstance();
        return new DefaultToken(jwtAttr[0], jwtAttr[1], jwtAttr[2]);
    }


}
