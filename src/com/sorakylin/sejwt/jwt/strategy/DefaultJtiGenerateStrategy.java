package com.sorakylin.sejwt.jwt.strategy;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

/**
 * 默认的jti生成策略
 * 使用一个long型数字进行自增
 * <p>
 * Create by sorakylin on 2019-11-24
 */
public class DefaultJtiGenerateStrategy implements Supplier<String> {

    private static volatile AtomicLong jti = new AtomicLong(0);

    private DefaultJtiGenerateStrategy() {

    }

    public static DefaultJtiGenerateStrategy getInstance() {
        return GeneratorInstance.INSTANCE;
    }

    @Override
    public String get() {
        return String.valueOf(DefaultJtiGenerateStrategy.jti.incrementAndGet());
    }

    private static class GeneratorInstance {
        private static final DefaultJtiGenerateStrategy INSTANCE = new DefaultJtiGenerateStrategy();
    }
}
