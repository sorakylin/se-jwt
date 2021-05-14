package com.sorakylin.sejwt.jwt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sorakylin.sejwt.jwt.strategy.DefaultJtiGenerateStrategy;
import com.sorakylin.sejwt.util.JsonUtil;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * jwt 负载
 * 用来存放实际需要传递的数据
 * 可以通过 PayloadBuilder 来建造得出
 * 除默认字段外,可通过 PayloadBuilder.data() 方法增加自定义字段
 * 注: jwt 的 Payload 是可以被解密的,不要存放敏感字段
 *
 * @author pyb
 * @time 2019-01-28
 */
public class Payload {
    private final String jti;//编号
    private final String iss;//签发人
    private final Long exp; //过期时间
    private final String sub;//主题
    private final String aud;//受众
    private final Long nbf;//生效时间
    private final Long iat;//签发时间
    //维护的私有字段,设置需要的值,map的 key 与 value 即为 json 字符串的 key 与value
    private final Map<String, Object> dataMap;

    public Payload(PayloadBuilder builder) {
        this.jti = builder.jti;
        this.iss = builder.iss;
        this.exp = builder.exp;
        this.sub = builder.sub;
        this.aud = builder.aud;
        this.nbf = builder.nbf;
        this.iat = builder.iat;
        this.dataMap = builder.dataMap;
    }

    public String toJson() {
        ObjectNode objectNode = JsonUtil.createObjectNode();
        objectNode.put("jti", jti);
        objectNode.put("exp", exp);
        objectNode.put("nbf", nbf);
        objectNode.put("iat", iat);

        if (this.iss != null)
            objectNode.put("iss", iss);
        if (this.sub != null)
            objectNode.put("sub", sub);
        if (this.aud != null)
            objectNode.put("aud", aud);

        if (this.dataMap.size() == 0) return objectNode.toString();

        this.dataMap.entrySet().forEach(entry -> {
            Object value = entry.getValue();

            if (value instanceof String) {
                objectNode.put(entry.getKey(), value.toString());
            } else {
                objectNode.set(entry.getKey(), (JsonNode) value);
            }
        });

        return objectNode.toString();
    }


    public static class PayloadBuilder {

        //内部维护的 JwtBuilder对象,为了使 and() 方法能够进行优雅的链式调用
        private JwtBuilder jwtBuilder;

        private Supplier<String> jtiGenerateStrategy = DefaultJtiGenerateStrategy.getInstance();


        private String jti = null;//编号
        private String iss;//签发人
        private Long exp = -1L; //过期时间,-1为永不过期
        private String sub;//主题
        private String aud;//受众
        private Long nbf;//生效时间,若不设置则默认 build() 方法调用的时间
        private Long iat;//签发时间
        //维护的私有字段,可设置需要的值
        private Map<String, Object> dataMap = new HashMap();

        public PayloadBuilder() {

        }

        public PayloadBuilder(JwtBuilder jwtBuilder) {
            this.jwtBuilder = jwtBuilder;
        }

        public PayloadBuilder setJtiGenerator(Supplier<String> jtiGenerateStrategy) {
            this.jtiGenerateStrategy = jtiGenerateStrategy;
            return this;
        }

        public PayloadBuilder data(String jsonKey, JsonNode jsonNode) {
            this.dataMap.put(jsonKey, jsonNode);
            return this;
        }

        public PayloadBuilder data(String jsonKey, String jsonValue) {
            this.dataMap.put(jsonKey, jsonValue);
            return this;
        }

        public PayloadBuilder iss(String iss) {
            this.iss = iss;
            return this;
        }

        public PayloadBuilder jti(String jti) {
            this.jti = jti;
            return this;
        }

        //多久过期,如设置30000则表示30秒后过期
        //只要设置了则最终建造出来的实际过期时间为(生效时间+过期时间)
        public PayloadBuilder exp(Long exp) {
            this.exp = exp;
            return this;
        }

        public PayloadBuilder sub(String sub) {
            this.sub = sub;
            return this;
        }

        public PayloadBuilder aud(String aud) {
            this.aud = aud;
            return this;
        }

        public PayloadBuilder nbf(Long nbf) {
            this.nbf = nbf;
            return this;
        }

        public PayloadBuilder nbf(Date nbf) {
            this.nbf = nbf.getTime();
            return this;
        }

        public PayloadBuilder nbf(LocalDateTime nbf) {
            this.nbf = nbf.toInstant(ZoneOffset.of("+8")).toEpochMilli();
            return this;
        }

        /**
         * 建造一个 jwt 的负载,包含了该 jwt 的信息
         *
         * @return Payload 负载对象
         */
        public Payload build() {
            long time = System.currentTimeMillis();
            this.iat = time;
            this.nbf = this.nbf == null ? time : this.nbf;
            this.exp = this.exp == -1L ? -1L : this.exp + this.nbf;

            if (Objects.isNull(this.jti)) this.jti = jtiGenerateStrategy.get();

            return new Payload(this);
        }

        public JwtBuilder and() {
            if (this.jwtBuilder == null) return new JwtBuilder().payload(this.build());
            return this.jwtBuilder.payload(this.build());
        }


    }


}
