package com.fmi110.mmall.utils;

import java.util.UUID;

/**
 * @author fmi110
 * @Description: 生成主键的字符串
 * @Date 2018/1/25 22:15
 */
public class KeyUtils {
    /**
     * 获取长度为 32 位的字符串 , 高并发下唯一性有待商榷
     * 生成策略 : uuid 截取一部分 + 系统当前时间
     * @return
     */
    public static synchronized String getUniqueKey() {
        String   uuid = UUID.randomUUID().toString().replace("-","").substring(14);
        return uuid + "_" + System.currentTimeMillis();
    }

    public static void main(String[] args) {
        String uniqueKey = getUniqueKey();
        System.out.println(uniqueKey+"   "+uniqueKey.length());
    }
}
