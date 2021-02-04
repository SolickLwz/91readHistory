package com.nine.one.yuedu.read;

import com.nine.one.yuedu.read.utils.GX.HttpClientUtilsGX;

/**
 * Author:李王柱
 * 2020/8/27
 */
public class StringForNumberTest {
    public static void main(String[] args) {
        String ss="123";
        try {
           Integer.parseInt(ss);
        } catch (Exception e) {
            System.out.println("请输入数字排序!");
        }


    }
}
