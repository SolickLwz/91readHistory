package com.nine.one.yuedu.read;

import com.nine.one.yuedu.read.utils.GX.HttpClientUtilsGX;
import org.springframework.util.DigestUtils;

import java.net.URLEncoder;

/**
 * Author:李王柱
 * 2020/8/28
 */
public class xiaoMiTest {
    public static void main(String[] args) {
        String key="ywmxzy6c6o17cso6onwfauouki0gltkn";//小米的key
        String AppId="1d52e322-0864-4fa4-b71c-807c931bf4a3";
        String AppKey="tiantianaikan.com";
        //String sign = DigestUtils.md5DigestAsHex(URLEncoder.encode(encodeStr.toString(), "UTF-8").getBytes()).toLowerCase();
        String apikey="";
        // https://channel.yueloo.com.cn/v1.0/book_list?AppId={}&AppKey={}&apikey={}
        String Oldurl="https://channel.yueloo.com.cn/v1.0/book_list?" +
                "AppId=1d52e322-0864-4fa4-b71c-807c931bf4a3" +
                "&AppKey=tiantianaikan.com&apikey=292b774dfb82918aa44b8c867455847b";

        String url="https://channel.yueloo.com.cn/v1.0/book_list?" +
                "AppId=" +AppId+
                "&AppKey=" +AppKey+
                "&apikey="+apikey;

        String s = null;
        try {
            s = HttpClientUtilsGX.doGet(Oldurl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(s);
    }
}
