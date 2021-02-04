package com.nine.one.yuedu.read;

import com.nine.one.yuedu.read.config.ApiConstant;
import com.nine.one.yuedu.read.utils.HttpClientUtil;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.LowerCase;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Author:李王柱
 * 2020/9/1
 */
public class httpTest {
    public static void main(String[] args) {
        String content = lwzPostByNoContent("http://59.110.17.4:9998/91yuedu/userAuthor/login?username=1233&password=1233");
        System.out.println(content);
    }

    public static String lwzPostByNoContent(String urlStr){
        StringBuffer sb=new StringBuffer();
        try {
            //1.获得HttpURLConnection对象
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();

            //2.设置一些参数（如请求头等）

            //设置一些需要的请求头参数
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            //设置通用属性
            //POST请求
            conn.setRequestMethod("POST");
            //对于post请求，这个必须要写，表示参数写入请求体中
            conn.setDoOutput(true);
            //是否接受从httpUrlConnection中读取服务器返回数据，默认是true
            conn.setDoInput(true);
            //设置连接超时时间
            conn.setConnectTimeout(10000);
            //设置接收数据超时时间
            conn.setReadTimeout(10000);

            //3.传输 POST 请求

            PrintWriter out = new PrintWriter(conn.getOutputStream());
            /*String request = JSON.toJSONString(requestData);
            out.print(request);*/
//注意这里一定要刷写
            out.flush();
            out.close();
            //4.接收服务器响应

            InputStream in = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String str1 = "";
            while ((str1 = br.readLine()) != null) {
                //System.out.println(str1);
                sb.append(str1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String testPost(String postUrl,String JsonStr) {
        String result="";
        System.out.println("取到的是:"+JsonStr);
        HttpURLConnection conn = null;
        try {
            URL url = new URL(postUrl);
            //String para = new String("username=admin&password=admin");
            String para=JsonStr;
            //1.得到HttpURLConnection实例化对象
            conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);//补充:设置允许输出
            conn.setDoInput(true);
            //2.设置请求方式
            conn.setRequestMethod("POST");
            //3.设置post提交内容的类型和长度
            /*
             * 只有设置contentType为application/x-www-form-urlencoded，
             * servlet就可以直接使用request.getParameter("username");直接得到所需要信息
             */
            //conn.setRequestProperty("contentType","application/x-www-form-urlencoded");
            conn.setRequestProperty("contentType","application/json");

            conn.setRequestProperty("Content-Length", String.valueOf(para.getBytes().length));

            //默认为false
            conn.setDoOutput(true);
            //4.向服务器写入数据
            conn.getOutputStream().write(para.getBytes());
            //5.得到服务器相应
            if (conn.getResponseCode() == 200) {
                //先将服务器得到的流对象 包装 存入缓冲区，忽略了正在缓冲时间
                InputStream in = new BufferedInputStream(conn.getInputStream());
                System.out.println("服务器已经收到表单数据！");
                // 得到servlet写入的头信息，response.setHeader("year", "2013");
                String year = conn.getHeaderField("year");
                System.out.println("year="+year);
                //byte[] bytes = readFromInput(in);   //封装的一个方法，通过指定的输入流得到其字节数据
                byte[] bytes = getData(in);//用了别处的方法,作用就是给输入流返回byte数组
                //System.out.println(new String(bytes, "utf-8"));
                System.out.println("[浏览器]成功！");
                result= new String(bytes, "utf-8");
            } else {
                System.out.println("请求失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //6.释放资源
            if (conn != null) {
                //关闭连接 即设置 http.keepAlive = false;
                conn.disconnect();
            }
        }
        return result;
    }

    public static byte[] getData(InputStream input) throws Exception
    {
        //存放数据的byte数组
        byte[] buffer=new byte[5000];
        //保存数据的输出流对象
        ByteArrayOutputStream output=new ByteArrayOutputStream();
        int len=0;
        while((len=input.read(buffer))!=-1)
        {
            //写入数据
            output.write(buffer, 0, len);
        }
        //返回输入流中的数据
        return output.toByteArray();

    }
}
