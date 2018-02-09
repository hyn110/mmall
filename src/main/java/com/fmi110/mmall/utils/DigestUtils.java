package com.fmi110.mmall.utils;


import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * md5 加密和 base64 加密的工具类 , 继承 spring 的工具类
 * base64 编解码依赖 apache 的 commons-codec.jar
 */
public class DigestUtils extends org.springframework.util.DigestUtils {

    /**
     * Calculates the MD5 digest and returns the value as a 32 character hex string.
     *
     * @param data Data to digest
     * @return MD5 digest as a hex string
     */
    public static String md5Hex(final String data) {
        return DigestUtils.md5DigestAsHex(data.getBytes(Charset.forName("utf-8")));
    }

    /**
     * Return a hexadecimal string representation of the MD5 digest of the given bytes.
     *
     * @param bytes the bytes to calculate the digest over
     * @return a hexadecimal digest string
     */
    public static String md5Hex(final byte[] bytes) {
        return DigestUtils.md5DigestAsHex(bytes);
    }

    /**
     * 二进制数据编码为BASE64字符串
     *
     * @param bytes
     * @return
     * @throws Exception
     */
    public static String encodeBase64(final byte[] bytes) {
        return new String(Base64.encodeBase64(bytes));
    }

    public static String encodeBase64(final String data, String charSetName) throws UnsupportedEncodingException {
        return new String(Base64.encodeBase64(data.getBytes(charSetName)));
    }

    /**
     * 字符串 base64 编码 , 使用 utf-8 转换
     *
     * @param data
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String encodeBase64(final String data) throws UnsupportedEncodingException {
        return encodeBase64(data, "utf-8");
    }

    /**
     * base64 解码
     *
     * @param bytes
     * @return
     */
    public static byte[] decodeBase64(final byte[] bytes) {
        return Base64.decodeBase64(bytes);
    }

    /**
     * 将字符串使用Base64解码 , 指定字符串的字符集
     *
     * @param data
     * @param charsetName
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] decodeBase64(final String data, String charsetName) throws UnsupportedEncodingException {

        return Base64.decodeBase64(data.getBytes(charsetName));
    }

    /**
     * 字符串 base64 解码 , 使用 utf-8
     *
     * @param data
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] decodeBase64(final String data) throws UnsupportedEncodingException {
        return decodeBase64(data, "utf-8");
    }


    //    /**
//     * 使用shiro的hash方式
//     * @param algorithmName 算法
//     * @param source 源对象
//     * @param salt 加密盐
//     * @param hashIterations hash次数
//     * @return 加密后的字符
//     */
//    public static String hashByShiro(String algorithmName, Object source, Object salt, int hashIterations) {
//        return new SimpleHash(algorithmName, source, salt, hashIterations).toHex();
//    }

    public static void main(String[] a) throws UnsupportedEncodingException {
        System.out.println(DigestUtils.md5Hex("fmi110"));
        System.out.println(DigestUtils.encodeBase64("fmi110"));
        System.out.println(new String(DigestUtils.decodeBase64("Zm1pMTEw")));
    }

}
