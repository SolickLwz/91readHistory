package com.nine.one.yuedu.read;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

/**
 * Author:李王柱
 * 2020/7/15
 */
public class jsonTest {
    public static void main(String[] args) {
        String jsonString="{\"access_token\":\"35_8u2MXnuMkVGojPbMR6AsXRmA5ShNukj6Fd6yTtCGE7rvBslXCa8RdBMqOSc8z158NYOSgOskZUEx6ngcgFMt7Lm6HsjfiVW7qYDmO3DfxCg\",\"expires_in\":7200,\"refresh_token\":\"35_gjL3eDJbDyTxhaKx9LXsiODl3a06GqlNtbdJ-6HMCDVnE-N7g8R3pvj2LwPiyCazOsEKn5Sa2g0ugcy-laNVCTk965Gk3rQ0zJAbHyVYlOo\",\"openid\":\"oNoP208_r6Qsox0Ob1WgRx3H3cGM\",\"scope\":\"snsapi_login\",\"unionid\":\"od_VcxHHCgoM-w4o1o2G7eDNBlOo\"}" ;

      /*  String openid = jsonObject.getString("openid");
        System.out.println(openid);*/
        String json2="callback( {\"client_id\":\"YOUR_APPID\",\"openid\":\"YOUR_OPENID\"} )";
        System.out.println(json2);
        String qqJson = getQQJson(json2);
        System.out.println(qqJson);
        JSONObject jsonObject=JSONObject.parseObject(qqJson);

        System.out.println( jsonObject.getString("openid"));

        String resu="access_token=FE04************************CCE2&expires_in=7776000&refresh_token=88E4************************BE14";
        int access_token= resu.indexOf("access_token=");
        int and = resu.indexOf("&");
        String substring = resu.substring(access_token, and);
        System.out.println(substring);

        String intstr="{\n" +
                "    \"id\": 1404376560,\n" +
                "    \"screen_name\": \"zaku\",\n" +
                "    \"name\": \"zaku\"}";
        JSONObject jsonObject1 = JSONObject.parseObject(intstr);
        String id = jsonObject1.getString("id");
        System.out.println(id);
    }
    public static String getQQJson(String qqString){
        if (StringUtils.equals("callback",qqString.substring(0,8))){
            return qqString.substring(9,qqString.length()-1);
        }
        return "{\"message\":\"返回错误值\",\"messageCont\":\"+qqString+\"}";
    }
}
