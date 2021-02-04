package com.nine.one.yuedu.read;

/**
 * Author:李王柱
 * 2020/6/26
 */
public class codeTest {
    public static void main(String[] args) {
        System.out.println("向".getBytes());
        System.out.println("buxiang".getBytes());
        System.out.println("长文本nei容".getBytes());

        for (int i=0;i<6;i++){
            if (i==4){
                continue;
            }
            System.out.println(i);
        }
    }
}
