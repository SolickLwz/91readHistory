package com.nine.one.yuedu.read.service.impl;

import com.nine.one.yuedu.read.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Author:李王柱
 * 2020/7/14
 */
    @Service(value = "redisService")
public class RedisServiceImpl implements RedisService {
    @Autowired
    private RedisTemplate redisTemplate;
  /*  @Autowired
    private RedisTemplate<String,Object> redisTemplate;*/

    @Override
    public void put(String key, String value) {
redisTemplate.opsForValue().set(key,value);
        //redisTemplate.opsForValue().set(key,value,60, TimeUnit.SECONDS);
    }

    @Override
    public String get(String key) {
        String messageCode = (String) redisTemplate.opsForValue().get(key);
        return messageCode;
    }


    @Override
    public Long getOnlyNumber() {
        return redisTemplate.opsForValue().increment("onlyNumber",1);
    }
}
