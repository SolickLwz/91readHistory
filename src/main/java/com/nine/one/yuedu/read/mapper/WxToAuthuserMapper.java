package com.nine.one.yuedu.read.mapper;

import com.nine.one.yuedu.read.entity.WxToAuthuser;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.MyMapper;

public interface WxToAuthuserMapper extends MyMapper<WxToAuthuser> {
    WxToAuthuser selectOneByOpenId(@Param("openid")String openid);

    WxToAuthuser selectAuthorUserByUnionid(@Param("unionid") String unionid);
}