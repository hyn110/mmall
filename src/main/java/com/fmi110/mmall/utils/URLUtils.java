package com.fmi110.mmall.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * url处理工具类
 *
 * @author L.cm
 */
public class URLUtils extends org.springframework.web.util.UriUtils {

    /**
     * url 编码
     *
     * @param source  url
     * @param charset 字符集
     * @return 编码后的url
     */
    public static String encodeURL(String source, Charset charset) {
        try {
            return URLUtils.encode(source, charset.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * url 编码
     *
     * @param source  url
     * @param charset 字符集
     * @return 编码后的url
     */
    public static String encodeURL(String source, String charset) {
        try {
            return URLUtils.encode(source, charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * url 解码
     * @param source  url
     * @param charset 字符集
     * @return
     */
    public static String decodeURL(String source, String charset) {
        return URLUtils.decodeURL(source, charset);
    }

}
