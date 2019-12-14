package com.skypyb.sejwt.jwt.token;

import com.skypyb.sejwt.cryptography.Codec;
import com.skypyb.sejwt.jwt.en.JwtAttribute;

import java.util.Objects;
import java.util.Optional;

public final class EmptyToken implements Token {

    private static final EmptyToken INSTANCE = new EmptyToken();

    private EmptyToken() {
        if (Objects.nonNull(INSTANCE))
            throw new UnsupportedOperationException();
    }

    public static EmptyToken getInstance() {
        return INSTANCE;
    }

    @Override
    public String getHeaderJson() {
        return "";
    }

    @Override
    public String getPayloadJson() {
        return "";
    }

    @Override
    public Optional<String> getAttributeValue(JwtAttribute jwtAttribute) {
        return Optional.empty();
    }

    @Override
    public Optional<String> getAttributeValue(String attribute) {
        return Optional.empty();
    }

    @Override
    public Token signatureSecret(String secret) {
        return this;
    }

    @Override
    public Token useSpecifiedCodec(Codec codec) {
        return this;
    }

    @Override
    public boolean validateToken() {
        return false;
    }

    @Override
    public boolean tokenIsCorrect() {
        return false;
    }

    @Override
    public boolean tokenHasExpired() {
        return false;
    }
}
