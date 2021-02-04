package com.nine.one.yuedu.read;

/**
 * Author:李王柱
 * 2020/7/21
 */
public class chapterTest {
    public static void main(String[] args) {
        String san="第三章 ssss";
        String si="第四d章 dddd";//改成第三章 dddd
        String wu="第五章 wwww";//改成第四章 wwww
        System.out.println(change(san,si));
    }
    public static String change(String mini,String big){
        int miniDi = mini.indexOf("第");
        int miniZhang=mini.indexOf("章");
        String substringMini = mini.substring(miniDi + 1, miniZhang);

        StringBuilder stringBuilder = new StringBuilder(big);
        stringBuilder.delete(big.indexOf("第")+1,big.indexOf("章"));
        stringBuilder.insert(big.indexOf("第")+1,substringMini);
        return stringBuilder.toString();
    }
}
