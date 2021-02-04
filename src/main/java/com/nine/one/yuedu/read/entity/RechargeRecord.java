package com.nine.one.yuedu.read.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import javax.persistence.*;

@Table(name = "recharge_record")
public class RechargeRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户id
     */
    private Integer uid;

    /**
     * 订单号
     */
    @Column(name = "recharge_no")
    private String rechargeNo;

    /**
     * 订单状态,0未付款,1付款成功,2付款失败
     */
    @Column(name = "recharge_status")
    private String rechargeStatus;

    /**
     * 充值金额
     */
    @Column(name = "recharge_money")
    private Long rechargeMoney;

    /**
     * 金币,用户的账户金额,因为活动或者充的多送的多之类的会导致这一单的金币和充值金额比例不固定
     */
    @Column(name = "recharge_gold")
    private Long rechargeGold;

    /**
     * 订单创建时间
     */
    @Column(name = "recharge_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date rechargeTime;

    @Column(name = "recharge_desc")
    private String rechargeDesc;

    /**
     * 保留字段
     */
    private String keep;

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
     * 获取用户id
     *
     * @return uid - 用户id
     */
    public Integer getUid() {
        return uid;
    }

    /**
     * 设置用户id
     *
     * @param uid 用户id
     */
    public void setUid(Integer uid) {
        this.uid = uid;
    }

    /**
     * 获取订单号
     *
     * @return recharge_no - 订单号
     */
    public String getRechargeNo() {
        return rechargeNo;
    }

    /**
     * 设置订单号
     *
     * @param rechargeNo 订单号
     */
    public void setRechargeNo(String rechargeNo) {
        this.rechargeNo = rechargeNo;
    }

    /**
     * 获取订单状态,0未付款,1付款成功
     *
     * @return recharge_status - 订单状态,0未付款,1付款成功
     */
    public String getRechargeStatus() {
        return rechargeStatus;
    }

    /**
     * 设置订单状态,0未付款,1付款成功,2付款失败
     *
     * @param rechargeStatus 订单状态,0未付款,1付款成功,2付款失败
     */
    public void setRechargeStatus(String rechargeStatus) {
        this.rechargeStatus = rechargeStatus;
    }

    /**
     * 获取充值金额
     *
     * @return recharge_money - 充值金额
     */
    public Long getRechargeMoney() {
        return rechargeMoney;
    }

    /**
     * 设置充值金额
     *
     * @param rechargeMoney 充值金额
     */
    public void setRechargeMoney(Long rechargeMoney) {
        this.rechargeMoney = rechargeMoney;
    }

    /**
     * 获取金币,用户的账户金额,因为活动或者充的多送的多之类的会导致这一单的金币和充值金额比例不固定
     *
     * @return recharge_gold - 金币,用户的账户金额,因为活动或者充的多送的多之类的会导致这一单的金币和充值金额比例不固定
     */
    public Long getRechargeGold() {
        return rechargeGold;
    }

    /**
     * 设置金币,用户的账户金额,因为活动或者充的多送的多之类的会导致这一单的金币和充值金额比例不固定
     *
     * @param rechargeGold 金币,用户的账户金额,因为活动或者充的多送的多之类的会导致这一单的金币和充值金额比例不固定
     */
    public void setRechargeGold(Long rechargeGold) {
        this.rechargeGold = rechargeGold;
    }

    /**
     * 获取订单创建时间
     *
     * @return recharge_time - 订单创建时间
     */
    public Date getRechargeTime() {
        return rechargeTime;
    }

    /**
     * 设置订单创建时间
     *
     * @param rechargeTime 订单创建时间
     */
    public void setRechargeTime(Date rechargeTime) {
        this.rechargeTime = rechargeTime;
    }

    /**
     * @return recharge_desc
     */
    public String getRechargeDesc() {
        return rechargeDesc;
    }

    /**
     * @param rechargeDesc
     */
    public void setRechargeDesc(String rechargeDesc) {
        this.rechargeDesc = rechargeDesc;
    }

    /**
     * 获取保留字段
     *
     * @return keep - 保留字段
     */
    public String getKeep() {
        return keep;
    }

    /**
     * 设置保留字段
     *
     * @param keep 保留字段
     */
    public void setKeep(String keep) {
        this.keep = keep;
    }
}