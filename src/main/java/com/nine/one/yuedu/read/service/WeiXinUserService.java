package com.nine.one.yuedu.read.service;

import com.nine.one.yuedu.read.entity.CsWxUsers;
import com.nine.one.yuedu.read.entity.WeixinUser;
import com.nine.one.yuedu.read.entity.WxToAuthuser;

public interface WeiXinUserService {
    //根据用户在账号下的唯一标识,获取这个用户
    CsWxUsers getUserByOpenId(String openid);

    //根据用户在账号下的唯一标识,获取这个用户
    WeixinUser getUserByAppIdAndName(String appid, String openid);

    int addWeiXinUser(WeixinUser oneWeiXinUser);

    //查询微信用户是否存在
    WxToAuthuser getHaveUserByWxToAuxthor(String openid);

    //根据用户在应用下的唯一标识获取微信用户
    WxToAuthuser getAuthorUserByUnionid(String unionid);

    //添加微信用户
    int addWxToAuthorUser(WxToAuthuser wxToAuthuser);
}
