package com.nine.one.yuedu.read.service;

public interface RedisService {
    /**
     * 使用Redis将value存放到指定key中
     * @param key
     * @param value
     */
    void put(String key, String value);

    /**
     * 从redis中获取指定key的value值
     * @param key
     * @return
     */
    String get(String key);

    /**
     * 获取redis的唯一数字
     * @return
     */
    Long getOnlyNumber();
}
