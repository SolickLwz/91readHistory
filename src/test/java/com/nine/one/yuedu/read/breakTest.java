package com.nine.one.yuedu.read;

/**
 * Author:李王柱
 * 2020/7/20
 */
public class breakTest {
    public static void main(String[] args) {
        int sex=1;
        String gender="女";
        switch (gender){
            case "男":
                break;
            case "女":
                sex=2;
                break;
        }
        System.out.println(sex);
    }
}
