package com.sorakylin.sejwt.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Create by sorakylin on 2019-12-13
 */
public final class JsonUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        //序列化的时候序列对象的所有属性
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.ALWAYS);

        //反序列化的时候如果多了其他属性,不抛出异常
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        //如果是空对象的时候,不抛异常
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        //取消时间的转化格式,默认是时间戳,可以取消,同时需要设置要表现的时间格式
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        OBJECT_MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    }

    private JsonUtil() {
        throw new UnsupportedOperationException();
    }


    public static ObjectNode createObjectNode() {
        return OBJECT_MAPPER.createObjectNode();
    }

    public static ArrayNode createArrayNode() {
        return OBJECT_MAPPER.createArrayNode();
    }

    public static Optional<String> toJson(Object obj) {

        Optional<String> result;

        try {
            result = Optional.ofNullable(objectConvertToJsonString(obj));
        } catch (Exception e) {
            result = Optional.empty();
        }
        return result;
    }

    private static String objectConvertToJsonString(Object obj) {

        if (Objects.nonNull(obj) &&
                (obj instanceof ObjectNode || obj instanceof ArrayNode)) return obj.toString();

        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> Optional<T> fromJsonToObject(String json, Class<T> clazz) {

        Optional<T> result;

        try {
            result = Optional.of(OBJECT_MAPPER.readValue(json, clazz));
        } catch (IOException e) {
            result = Optional.empty();
        }

        return result;
    }

    public static <T> List<T> fromJsonToList(String json, Class<T> clazz) {

        List<T> result;

        try {
            JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructParametricType(List.class, clazz);
            result = OBJECT_MAPPER.readValue(json, javaType);

        } catch (IOException e) {
            result = new ArrayList<>();
        }

        return result;
    }


}
