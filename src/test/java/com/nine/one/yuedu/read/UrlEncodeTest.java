package com.nine.one.yuedu.read;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Author:李王柱
 * 2020/7/15
 */
public class UrlEncodeTest {
    public static void main(String[] args) {
        String encodeStr="http://client.91yuedu.com/#/main/bookList";//http%3A%2F%2Fclient.91yuedu.com%2F%23%2F
        String getWeiXin页面="\"https://open.weixin.qq.com/connect/qrconnect?appid=wx2e9c2975c24f2f3c&redirect_uri=http%3A%2F%2Fadmin.91yuedu.com%2F%23%2Flogin&response_type=code&scope=snsapi_login&state=6a5f0cc86af67c3883f21f73924d7223#wechat_redirect\"";
        String weibo="http://www.91yuedu.com";//http%3A%2F%2Fwww.91yuedu.com%2F http%3A%2F%2Fwww.91yuedu.com
        try {
            String encode = URLEncoder.encode(encodeStr, "UTF-8");
            System.out.println(encode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
}
