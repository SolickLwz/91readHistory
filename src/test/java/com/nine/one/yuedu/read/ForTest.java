package com.nine.one.yuedu.read;

/**
 * Author:李王柱
 * 2020/8/20
 */
public class ForTest {
    public static void main(String[] args) {
        for (int i=0;i<9;i++){
            for (int j=0;j<9;j++){
                if (j==4){
                    System.out.println("不进行的循环");
                    continue;
                }
                System.out.println("外"+i+"内"+j);
            }
        }
    }
}
