package com.nine.one.yuedu.read.service;

import com.nine.one.yuedu.read.entity.QqToAuthuser;

public interface QQUserService {
    //根据用户唯一openid获取QQ用户对象
    QqToAuthuser getAuthorUserByOpenid(String openid);

    //保存QQ对象
    int addQQToAuthorUser(QqToAuthuser qqToAuthuser);
}
