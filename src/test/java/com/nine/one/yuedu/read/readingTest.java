package com.nine.one.yuedu.read;

/**
 * Author:李王柱
 * 2020/7/16
 */
public class readingTest {
    public static void main(String[] args) {
        String s="第   385   章    睡了你，是我主动";
        System.out.println(toSpace(s));
    }
    public static String toSpace(String oldStr){
        StringBuilder stringBuilder=new StringBuilder(oldStr);
        int indexOfHave = stringBuilder.indexOf("章");
        if (indexOfHave>=0){
            if (stringBuilder.indexOf("章 ")<0){
                stringBuilder.insert(indexOfHave+1," ");
            }
        }
        return stringBuilder.toString();
    }
}
