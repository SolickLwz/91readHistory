package com.nine.one.yuedu.read.mapper;

import com.nine.one.yuedu.read.entity.CsWxUsers;
import tk.mybatis.MyMapper;

public interface CsWxUsersMapper extends MyMapper<CsWxUsers> {
    //根据当前账号下的用户唯一标识,获取用户
    CsWxUsers getUserByOpenId(String openid);
}