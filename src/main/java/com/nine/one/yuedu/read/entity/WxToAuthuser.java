package com.nine.one.yuedu.read.entity;

import javax.persistence.*;

@Table(name = "wx_to_authuser")//read_91yuedu..
public class WxToAuthuser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 微信用户名
     */
    private String nickname;

    /**
     * 唯一标识,对当前账号唯一
     */
    private String openid;

    private String appid;

    /**
     * 同一用户的unionid是唯一的
     */
    private String unionid;

    private Integer authorid;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取微信用户名
     *
     * @return nickname - 微信用户名
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * 设置微信用户名
     *
     * @param nickname 微信用户名
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 获取唯一标识,对当前账号唯一
     *
     * @return openid - 唯一标识,对当前账号唯一
     */
    public String getOpenid() {
        return openid;
    }

    /**
     * 设置唯一标识,对当前账号唯一
     *
     * @param openid 唯一标识,对当前账号唯一
     */
    public void setOpenid(String openid) {
        this.openid = openid;
    }

    /**
     * @return appid
     */
    public String getAppid() {
        return appid;
    }

    /**
     * @param appid
     */
    public void setAppid(String appid) {
        this.appid = appid;
    }

    /**
     * 获取同一用户的unionid是唯一的
     *
     * @return unionid - 同一用户的unionid是唯一的
     */
    public String getUnionid() {
        return unionid;
    }

    /**
     * 设置同一用户的unionid是唯一的
     *
     * @param unionid 同一用户的unionid是唯一的
     */
    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    /**
     * @return authorid
     */
    public Integer getAuthorid() {
        return authorid;
    }

    /**
     * @param authorid
     */
    public void setAuthorid(Integer authorid) {
        this.authorid = authorid;
    }
}