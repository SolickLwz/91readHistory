package com.nine.one.yuedu.read.service.impl;

import com.nine.one.yuedu.read.entity.CsWxUsers;
import com.nine.one.yuedu.read.entity.WeixinUser;
import com.nine.one.yuedu.read.entity.WxToAuthuser;
import com.nine.one.yuedu.read.mapper.CsWxUsersMapper;
import com.nine.one.yuedu.read.mapper.WeixinUserMapper;
import com.nine.one.yuedu.read.mapper.WxToAuthuserMapper;
import com.nine.one.yuedu.read.service.WeiXinUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

/**
 * Author:李王柱
 * 2020/7/16
 */
@Service(value = "weiXinUserService")
public class WeiXinUserServiceImpl implements WeiXinUserService {
    @Autowired
    private CsWxUsersMapper csWxUsersMapper;

    @Autowired
    private WeixinUserMapper weixinUserMapper;

    @Autowired
    private WxToAuthuserMapper wxToAuthuserMapper;

    @Override
    public CsWxUsers getUserByOpenId(String openid) {
        Example example = new Example(CsWxUsers.class);
        //example.createCriteria().andEqualTo("openid",openid);
        CsWxUsers csWxUser = new CsWxUsers();
        csWxUser.setOpenid(openid);
        CsWxUsers csWxUserOne = csWxUsersMapper.selectOne(csWxUser);
        return csWxUserOne;
    }

    @Override
    public WeixinUser getUserByAppIdAndName(String appid, String openid) {

        Example example = new Example(WeixinUser.class);
        example.createCriteria().andEqualTo("openid",openid);
        WeixinUser weixinUser = weixinUserMapper.selectOneByExample(example);
        return weixinUser;
    }

    @Override
    public int addWeiXinUser(WeixinUser oneWeiXinUser) {
        return weixinUserMapper.insertSelective(oneWeiXinUser);
    }

    @Override
    public WxToAuthuser getHaveUserByWxToAuxthor(String openid) {
       /* Example example=new Example(WxToAuthuser.class);
        example.createCriteria().andEqualTo("openid",fsaf);
        WxToAuthuser wxToAuthuser = wxToAuthuserMapper.selectOneByExample(example);*/
        WxToAuthuser wxToAuthuser = wxToAuthuserMapper.selectOneByOpenId(openid);
        return wxToAuthuser;
    }

    @Override
    public WxToAuthuser getAuthorUserByUnionid(String unionid) {
        Example example = new Example(WxToAuthuser.class);
        example.createCriteria().andEqualTo("unionid",unionid);
        WxToAuthuser wxToAuthuser = wxToAuthuserMapper.selectAuthorUserByUnionid(unionid);
        return wxToAuthuser;
    }

    @Override
    public int addWxToAuthorUser(WxToAuthuser wxToAuthuser) {
        return wxToAuthuserMapper.insert(wxToAuthuser);
    }
}
