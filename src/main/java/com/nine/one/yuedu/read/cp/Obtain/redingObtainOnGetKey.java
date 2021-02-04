package com.nine.one.yuedu.read.cp.Obtain;

import com.alibaba.fastjson.JSONObject;
import com.nine.one.yuedu.read.utils.HttpClientUtils;
import org.springframework.util.DigestUtils;

import java.security.MessageDigest;

/**
 * Author:李王柱
 * 2020/6/15 0015
 */
public class redingObtainOnGetKey {
    public static void main(String[] args) {
        //登录账号和密码为申请的开发平台账户和分配的cpid

        String cpid="2000000608";
        String username="jingxiangtiancheng";//用户名 = 平台登录用户名
        String cpassw="JXtiancheng2018";

        //密码= md5(md5(cpid)+md5(平台登录密码)+md5(username))
        //DigestUtils.md5DigestAsHex(DigestUtils.md5Digest(cpid)+DigestUtils.md5DigestAsHex(username));
        String password = "";
        try {
            password = myMD5(myMD5(cpid) + myMD5(cpassw) + myMD5(username));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //http://open.book.qq.com/push/login?username=ceshi&password=E10ADC394BABEF231CD212DF
        String url="http://open.book.qq.com/push/login?username="+username+"&password="+password;
        String jsonStr = HttpClientUtils.doGet(url);
        JSONObject resultJson = JSONObject.parseObject(jsonStr);
        String key= resultJson.getJSONObject("result").getString("key");
        System.out.println("得到有效期为7天的"+key);//LxOp3_0CrhTw1bPuj-DWBiaAMSddPqv5dA3UiMwBIO7pPQETZ6xrPks8DVcp7XpBILMf8PkWYdBY*BdCulaANM38cbAxZLxb-zh8oV0b9yKUFRNQROZczu5kKI7mwA08BHkLQZyA-LFU@
    }
    public static String myMD5(String data) throws Exception {
        java.security.MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] array = md.digest(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }
}
