package com.nine.one.yuedu.read.utils.GX;

/**
 * Author:李王柱
 * 2021/1/15
 */
public class Format91 {
    public static String chapterNameFromat(String oldName){
        //先去除两边空白
        String Str = oldName.trim();
        if (Str.indexOf("章 ")<0){
            if (Str.indexOf("章:")>0){
                StringBuilder stringBuilder=new StringBuilder(Str);
                Str= stringBuilder.insert(Str.indexOf("章:")+2, " ").toString();
                return Str;
            }
            if (Str.indexOf("章：")>0){
                StringBuilder stringBuilder=new StringBuilder(Str);
                Str= stringBuilder.insert(Str.indexOf("章：")+2, " ").toString();
                return Str;
            }
            StringBuilder stringBuilder=new StringBuilder(Str);
            Str= stringBuilder.insert(Str.indexOf("章")+1, " ").toString();
        }
        return Str;
    }

    public static String chapterFromat(String oldStr){

        String replaceAllStr = oldStr.replaceAll("　|\r| |\t", "");//这里有改动,把单个的换行留着,双换行去掉
        replaceAllStr=replaceAllStr.replaceAll("\n\n\n\n|\n\n\n|\n\n", "\n");
        StringBuilder sB = new StringBuilder(replaceAllStr);
        while (sB.substring(0,1).equals("\n")){
            sB.delete(0,1);
        }
        replaceAllStr=sB.toString();

        String newStr= replaceAllStr.replaceAll("\n", "\n    ");

        String result="    "+newStr;
        return result;

        /*while (content.indexOf("\n　　\n")>1){
            content=content.replace("\n　　\n","");
        }
        while (content.indexOf("\n \n")>1){
            content=content.replace("\n\n","\n");
        }
        StringBuilder str=new StringBuilder(content);
        for (int i=0;i<str.length()-1;i++){
            if (str.substring(i,i+1).equals("\n")){
                if (str.substring(i+1,i+2).equals("　")){
                    if (!str.substring(i+2,i+3).equals("　")){
                        str.insert(i+2,"　");
                    }
                }else {
                    str.insert(i+1,"　　");
                }
            }
        }*/

    }
}
