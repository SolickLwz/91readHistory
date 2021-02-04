package com.nine.one.yuedu.read.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import javax.persistence.*;

@Table(name = "user")
public class User {
    /**
     * 主键自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 真实姓名
     */
    @Column(name = "real_name")
    private String realName;

    /**
     * 头像
     */
    @Column(name = "head_image")
    private String headImage;

    /**
     * 1.启用，2禁用
     */
    private Integer status;

    /**
     * 1.男，2.女
     */
    private Integer sex;

    /**
     * 手机号
     */
    @Column(name = "phone_num")
    private String phoneNum;

    /**
     * 注册时间
     */
    @Column(name = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    /**
     * 账户余额,默认有66红包,1点余额是1分钱
     */
    private Long balance;

    /**
     * 1和2不好记,no为不自动扣费,每次都询问,yes为不再询问,直接从账户中扣除章节价格
     */
    private String auto;

    /**
     * 推荐票,一块钱一张(100阅读币)
     */
    private Long ticket;

    /**
     * 获取主键自增
     *
     * @return id - 主键自增
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键自增
     *
     * @param id 主键自增
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取用户名
     *
     * @return username - 用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户名
     *
     * @param username 用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取登录密码
     *
     * @return password - 登录密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置登录密码
     *
     * @param password 登录密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取昵称
     *
     * @return nickname - 昵称
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * 设置昵称
     *
     * @param nickname 昵称
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 获取真实姓名
     *
     * @return real_name - 真实姓名
     */
    public String getRealName() {
        return realName;
    }

    /**
     * 设置真实姓名
     *
     * @param realName 真实姓名
     */
    public void setRealName(String realName) {
        this.realName = realName;
    }

    /**
     * 获取头像
     *
     * @return head_image - 头像
     */
    public String getHeadImage() {
        return headImage;
    }

    /**
     * 设置头像
     *
     * @param headImage 头像
     */
    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    /**
     * 获取1.启用，2禁用
     *
     * @return status - 1.启用，2禁用
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置1.启用，2禁用
     *
     * @param status 1.启用，2禁用
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取1.男，2.女
     *
     * @return sex - 1.男，2.女
     */
    public Integer getSex() {
        return sex;
    }

    /**
     * 设置1.男，2.女
     *
     * @param sex 1.男，2.女
     */
    public void setSex(Integer sex) {
        this.sex = sex;
    }

    /**
     * 获取手机号
     *
     * @return phone_num - 手机号
     */
    public String getPhoneNum() {
        return phoneNum;
    }

    /**
     * 设置手机号
     *
     * @param phoneNum 手机号
     */
    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    /**
     * 获取注册时间
     *
     * @return create_time - 注册时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置注册时间
     *
     * @param createTime 注册时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取账户余额,默认有66红包,1点余额是1分钱
     *
     * @return balance - 账户余额,默认有66红包,1点余额是1分钱
     */
    public Long getBalance() {
        return balance;
    }

    /**
     * 设置账户余额,默认有66红包,1点余额是1分钱
     *
     * @param balance 账户余额,默认有66红包,1点余额是1分钱
     */
    public void setBalance(Long balance) {
        this.balance = balance;
    }

    /**
     * 获取1和2不好记,no为不自动扣费,每次都询问,yes为不再询问,直接从账户中扣除章节价格
     *
     * @return auto - 1和2不好记,no为不自动扣费,每次都询问,yes为不再询问,直接从账户中扣除章节价格
     */
    public String getAuto() {
        return auto;
    }

    /**
     * 设置1和2不好记,no为不自动扣费,每次都询问,yes为不再询问,直接从账户中扣除章节价格
     *
     * @param auto 1和2不好记,no为不自动扣费,每次都询问,yes为不再询问,直接从账户中扣除章节价格
     */
    public void setAuto(String auto) {
        this.auto = auto;
    }

    /**
     * 获取推荐票,一块钱一张(100阅读币)
     *
     * @return ticket - 推荐票,一块钱一张(100阅读币)
     */
    public Long getTicket() {
        return ticket;
    }

    /**
     * 设置推荐票,一块钱一张(100阅读币)
     *
     * @param ticket 推荐票,一块钱一张(100阅读币)
     */
    public void setTicket(Long ticket) {
        this.ticket = ticket;
    }
}