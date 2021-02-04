package com.nine.one.yuedu.read.service.impl;

import com.nine.one.yuedu.read.entity.QqToAuthuser;
import com.nine.one.yuedu.read.mapper.QqToAuthuserMapper;
import com.nine.one.yuedu.read.service.QQUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

/**
 * Author:李王柱
 * 2020/7/20
 */
@Service(value = "qQUserService")
public class QQUserServiceImpl implements QQUserService {

    @Autowired
    private QqToAuthuserMapper qqToAuthuserMapper;
    @Override
    public QqToAuthuser getAuthorUserByOpenid(String openid) {
        Example example=new Example(QqToAuthuser.class);
        example.createCriteria().andEqualTo("openid",openid);
        QqToAuthuser qqToAuthuser = qqToAuthuserMapper.selectOneByExample(example);
        return qqToAuthuser;
    }

    @Override
    public int addQQToAuthorUser(QqToAuthuser qqToAuthuser) {

        return qqToAuthuserMapper.insertSelective(qqToAuthuser);
    }
}
