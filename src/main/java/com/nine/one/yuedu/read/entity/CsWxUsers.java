package com.nine.one.yuedu.read.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "weixin..cs_wx_users")
public class CsWxUsers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Id
    private String openid;

    private String nickname;

    /**
     * 性别 1：男 2：女
     */
    private Integer sex;

    private String language;

    /**
     * 城市
     */
    private String city;

    /**
     * 省份
     */
    private String province;

    /**
     * 国家
     */
    private String country;

    /**
     * 头像
     */
    @Column(name = "headImg")
    private String headimg;

    private String remark;

    /**
     * 公众号
     */
    private String appid;

    /**
     * 首次关注时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 最近一次关注时间
     */
    @Column(name = "subscribe_time")
    private Date subscribeTime;

    /**
     * 取关时间
     */
    @Column(name = "unsubscribe_time")
    private Date unsubscribeTime;

    /**
     * 关注状态 (0 关注 1 已取关)
     */
    @Column(name = "subscribe_status")
    private Integer subscribeStatus;

    /**
     * 最后互动时间
     */
    @Column(name = "last_mutual_time")
    private Date lastMutualTime;

    /**
     * 推广渠道参数
     */
    private String channelparam;

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
     * @return openid
     */
    public String getOpenid() {
        return openid;
    }

    /**
     * @param openid
     */
    public void setOpenid(String openid) {
        this.openid = openid;
    }

    /**
     * @return nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @param nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 获取性别 1：男 2：女
     *
     * @return sex - 性别 1：男 2：女
     */
    public Integer getSex() {
        return sex;
    }

    /**
     * 设置性别 1：男 2：女
     *
     * @param sex 性别 1：男 2：女
     */
    public void setSex(Integer sex) {
        this.sex = sex;
    }

    /**
     * @return language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @param language
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * 获取城市
     *
     * @return city - 城市
     */
    public String getCity() {
        return city;
    }

    /**
     * 设置城市
     *
     * @param city 城市
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * 获取省份
     *
     * @return province - 省份
     */
    public String getProvince() {
        return province;
    }

    /**
     * 设置省份
     *
     * @param province 省份
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * 获取国家
     *
     * @return country - 国家
     */
    public String getCountry() {
        return country;
    }

    /**
     * 设置国家
     *
     * @param country 国家
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * 获取头像
     *
     * @return headImg - 头像
     */
    public String getHeadimg() {
        return headimg;
    }

    /**
     * 设置头像
     *
     * @param headimg 头像
     */
    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    /**
     * @return remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取公众号
     *
     * @return appid - 公众号
     */
    public String getAppid() {
        return appid;
    }

    /**
     * 设置公众号
     *
     * @param appid 公众号
     */
    public void setAppid(String appid) {
        this.appid = appid;
    }

    /**
     * 获取首次关注时间
     *
     * @return create_time - 首次关注时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置首次关注时间
     *
     * @param createTime 首次关注时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取最近一次关注时间
     *
     * @return subscribe_time - 最近一次关注时间
     */
    public Date getSubscribeTime() {
        return subscribeTime;
    }

    /**
     * 设置最近一次关注时间
     *
     * @param subscribeTime 最近一次关注时间
     */
    public void setSubscribeTime(Date subscribeTime) {
        this.subscribeTime = subscribeTime;
    }

    /**
     * 获取取关时间
     *
     * @return unsubscribe_time - 取关时间
     */
    public Date getUnsubscribeTime() {
        return unsubscribeTime;
    }

    /**
     * 设置取关时间
     *
     * @param unsubscribeTime 取关时间
     */
    public void setUnsubscribeTime(Date unsubscribeTime) {
        this.unsubscribeTime = unsubscribeTime;
    }

    /**
     * 获取关注状态 (0 关注 1 已取关)
     *
     * @return subscribe_status - 关注状态 (0 关注 1 已取关)
     */
    public Integer getSubscribeStatus() {
        return subscribeStatus;
    }

    /**
     * 设置关注状态 (0 关注 1 已取关)
     *
     * @param subscribeStatus 关注状态 (0 关注 1 已取关)
     */
    public void setSubscribeStatus(Integer subscribeStatus) {
        this.subscribeStatus = subscribeStatus;
    }

    /**
     * 获取最后互动时间
     *
     * @return last_mutual_time - 最后互动时间
     */
    public Date getLastMutualTime() {
        return lastMutualTime;
    }

    /**
     * 设置最后互动时间
     *
     * @param lastMutualTime 最后互动时间
     */
    public void setLastMutualTime(Date lastMutualTime) {
        this.lastMutualTime = lastMutualTime;
    }

    /**
     * 获取推广渠道参数
     *
     * @return channelparam - 推广渠道参数
     */
    public String getChannelparam() {
        return channelparam;
    }

    /**
     * 设置推广渠道参数
     *
     * @param channelparam 推广渠道参数
     */
    public void setChannelparam(String channelparam) {
        this.channelparam = channelparam;
    }
}