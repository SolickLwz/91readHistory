package com.nine.one.yuedu.read;

import org.springframework.util.DigestUtils;

/**
 * Author:李王柱
 * 2020/9/8
 */
public class md5Test {
    public static void main(String[] args) {
        String key="22"+"7c55423b42c543aabe2ecf0dd8b81bd1";
        String MD5ZhangZhong = DigestUtils.md5DigestAsHex(key.getBytes());
        System.out.println(MD5ZhangZhong);
    }
}
