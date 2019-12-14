package com.skypyb.sejwt.jwt.token;

import com.skypyb.sejwt.cryptography.Codec;
import com.skypyb.sejwt.jwt.en.JwtAttribute;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * 默认的Token实现
 * Create by skypyb on 2019-12-14
 */
public final class DefaultToken extends AbstractToken implements Serializable {

    private static final Long NOT_EXPIRE = -1L;

    /**
     * 是否正确
     * 用于缓存Token 处理结果,避免多次操作
     */
    private Boolean correct;

    public DefaultToken(String header, String payload, String signature) {
        super(header, payload, signature);
    }


    private boolean setCorrect(boolean correct) {
        this.correct = correct;
        return correct;
    }


    @Override
    public boolean validateToken() {
        return tokenIsCorrect() && !tokenHasExpired();
    }

    @Override
    public boolean tokenIsCorrect() {
        if (Objects.nonNull(this.correct)) return this.correct;

        final String secret = getSecret();

        Codec codec = getCodec();

        try {
            if (Objects.isNull(codec)) {
                String codeType = getAttributeValue(JwtAttribute.Header.ALG).get();
                codec = Codec.Type.valueOf(codeType).create();
            }

            //获取对应算法的 enum 并进行算法实体的创建
            String cipherText = codec.encrypt(getHeader() + "." + getPayload(), secret);
            return setCorrect(Objects.equals(getSignature(), cipherText));
        } catch (Exception e) {
            return setCorrect(false);
        }
    }

    @Override
    public boolean tokenHasExpired() {
        if (Objects.equals(Boolean.FALSE, this.correct)) return false;
        if (Objects.isNull(this.correct) && !tokenIsCorrect()) return false;

        Optional<String> expTime = getAttributeValue(JwtAttribute.Payload.EXP);
        Optional<String> nbfTime = getAttributeValue(JwtAttribute.Payload.NBF);

        if (!expTime.isPresent() || !nbfTime.isPresent()) return false;

        //生效时间和过期时间
        Long nbfMillisecondValue = nbfTime.map(Long::valueOf).get();
        Long expMillisecondValue = expTime.map(Long::valueOf).get();

        if (Objects.equals(expMillisecondValue, NOT_EXPIRE)) return false;

        final long localMillisecondValue = System.currentTimeMillis();

        return localMillisecondValue < nbfMillisecondValue || localMillisecondValue > expMillisecondValue;
    }

    @Override
    public Token signatureSecret(String secret) {
        Token token = super.signatureSecret(secret);
        this.correct = null;
        return token;
    }

    @Override
    public Token useSpecifiedCodec(Codec codec) {
        Token token = super.useSpecifiedCodec(codec);
        this.correct = null;
        return token;
    }
}
