package com.nine.one.yuedu.read.utils;

import java.util.UUID;

/**
 * @author wangyuliang
 * 获取UUID的工具类
 */
public class UUIDUtil {

    public static String getUUID() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid;
    }
}
