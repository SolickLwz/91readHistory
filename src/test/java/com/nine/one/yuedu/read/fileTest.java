package com.nine.one.yuedu.read;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Author:李王柱
 * 2020/9/4
 */
public class fileTest {
    public static void main(String[] args) {
        //String lwzFilePath="/SensitiveWord.txt";

        //E:\1905基础\04-JavaSE\code\05File类\src\com\bjpowernode\File常用方法\lwzFiles.txt
        /*File have=new File("/SensitiveWord.txt");
        if (have.exists()){
            System.out.println("cunzai");
        }else {
            System.out.println("不存在");
        }*/
        System.out.println("得到"+read("data2\\nfs\\www\\booktxt\\16614\\16614"));

        //看文件夹存在吗,创建并抓取
       /* File fileDir = new File("/data3/nfs/www/booktxt/" + 29516 + "/" + 29516);
        if (!fileDir.exists() || !fileDir.isDirectory()) {
            //新建出文件夹
            System.out.println("新建出文件夹");
            fileDir.mkdirs();
        }*/
    }

    /**
     * 文件输入流，用于读取文件中的数据到Java程序中
     */
    public  static String  read(String lwzPath){
        FileInputStream fis=null;
        try {
            //1.定义文件输入流对象并指定要读取的文件的绝对路径 例如 d:/aa.txt
            fis=new FileInputStream(lwzPath);
            //2.定义一个字节数组，用于存放从文件中读取出来数据（它起到一个中转的作用）
            byte b[]=new byte[1024];
            //3.定义一个整型变量，表示每次中文件中读取多少个字节到Java程序中
            int len=0;
            //4.循环读取数据到程序中
            //读取数据到字节数组中，并返回本次读取了多少个字节，如果返回-1 表示文件读取完成没有更多的内容
            while((len=fis.read(b))>=0){
                //对文件中的数据进行操作，我们的操作就是将数据输出到控制台
                return new String(b,0,len);
              /*  System.out.println(new String(b,0,len));*/
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fis!=null){
                //5.关闭所用的流（必须关闭）
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
