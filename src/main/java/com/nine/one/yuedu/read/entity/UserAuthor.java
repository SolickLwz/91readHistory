package com.nine.one.yuedu.read.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import javax.persistence.*;

@Table(name = "user_author")
public class UserAuthor {
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
     * 角色
     */
    @Column(name = "role_id")
    private Integer roleId;

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
     * 身份证号
     */
    @Column(name = "card_id")
    private String cardId;

    /**
     * 邮箱
     */
    private String emile;

    /**
     * 手机号
     */
    @Column(name = "phone_num")
    private String phoneNum;

    /**
     * QQ号
     */
    @Column(name = "qq_num")
    private String qqNum;

    /**
     * 开户行名称
     */
    @Column(name = "bank_name")
    private String bankName;

    /**
     * 银行账户
     */
    @Column(name = "bank_num")
    private String bankNum;

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
     * 获取角色
     *
     * @return role_id - 角色
     */
    public Integer getRoleId() {
        return roleId;
    }

    /**
     * 设置角色
     *
     * @param roleId 角色
     */
    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
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
     * 获取身份证号
     *
     * @return card_id - 身份证号
     */
    public String getCardId() {
        return cardId;
    }

    /**
     * 设置身份证号
     *
     * @param cardId 身份证号
     */
    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    /**
     * 获取邮箱
     *
     * @return emile - 邮箱
     */
    public String getEmile() {
        return emile;
    }

    /**
     * 设置邮箱
     *
     * @param emile 邮箱
     */
    public void setEmile(String emile) {
        this.emile = emile;
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
     * 获取QQ号
     *
     * @return qq_num - QQ号
     */
    public String getQqNum() {
        return qqNum;
    }

    /**
     * 设置QQ号
     *
     * @param qqNum QQ号
     */
    public void setQqNum(String qqNum) {
        this.qqNum = qqNum;
    }

    /**
     * 获取开户行名称
     *
     * @return bank_name - 开户行名称
     */
    public String getBankName() {
        return bankName;
    }

    /**
     * 设置开户行名称
     *
     * @param bankName 开户行名称
     */
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    /**
     * 获取银行账户
     *
     * @return bank_num - 银行账户
     */
    public String getBankNum() {
        return bankNum;
    }

    /**
     * 设置银行账户
     *
     * @param bankNum 银行账户
     */
    public void setBankNum(String bankNum) {
        this.bankNum = bankNum;
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
}
