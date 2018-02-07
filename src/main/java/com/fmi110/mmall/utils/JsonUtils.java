package com.fmi110.mmall.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 静态内部类实现单例模式
 * json工具类,使用 jacksonJSON
 */
public final class JsonUtils {
    private JsonUtils() {}
    private static final Locale CHINA = Locale.CHINA; // 时区
    private static boolean config = false;

    /**
     * 配置ObjectMapper对象,设置时区 , 解决json转换后时间类型相差8小时的问题
     */
    public static void config(){
        if (!config) {
            ObjectMapper instance = getInstance();
            instance.setLocale(CHINA);
            instance.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", CHINA));
            instance.setTimeZone(TimeZone.getTimeZone("GMT+8"));

//            instance.enable(JsonGenerator.Feature.IGNORE_UNKNOWN, JsonGenerator.Feature.QUOTE_FIELD_NAMES);
            // null值 不序列化
            instance.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        }
    }
    /**
     * 将对象序列化成json字符串 , 默认不输出值为 NULL 的字段
     * @param object javaBean
     * @return jsonString json字符串
     */
    public static String toJson(Object object) {
        config();
        try {
            return getInstance().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 输出json字符串,是否输出 NULL 值
     * @param object
     * @param includeNULL  是否输出 null
     * @return
     */
    public static String toJson(Object object,boolean includeNULL) {
        config();
        try {
            if (includeNULL) {
                getInstance().setSerializationInclusion(JsonInclude.Include.ALWAYS);
            }
            return getInstance().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将json反序列化成对象
     * @param jsonString jsonString
     * @param valueType class
     * @param <T> T 泛型标记
     * @return Bean
     */
    public static <T> T parse(String jsonString, Class<T> valueType) {
        config();
        try {
            return getInstance().readValue(jsonString, valueType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 带泛型参数的集合的反序列化
     * @param jsonString
     * @param valueTypeRef
     * <p>new TypeReference<List<OrderDTO>>(){}</p>
     * <p>new TypeReference<Hashmap<String,OrderDTO>>(){}</p>
     * @param <T>
     * @return
     */
    public static <T> T parse(String jsonString, TypeReference valueTypeRef){
        config();
        try {
            return getInstance().readValue(jsonString, valueTypeRef);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static ObjectMapper getInstance() {
        return JacksonHolder.INSTANCE;
    }


    private static class JacksonHolder {
        private static ObjectMapper INSTANCE = new ObjectMapper();
    }

}
