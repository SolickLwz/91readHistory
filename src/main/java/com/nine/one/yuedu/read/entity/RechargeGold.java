package com.nine.one.yuedu.read.entity;

import javax.persistence.*;

@Table(name = "recharge_gold")
public class RechargeGold {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 金币数量
     */
    private Long gold;

    /**
     * 用户充值的金额
     */
    private Long money;

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
     * 获取金币数量
     *
     * @return gold - 金币数量
     */
    public Long getGold() {
        return gold;
    }

    /**
     * 设置金币数量
     *
     * @param gold 金币数量
     */
    public void setGold(Long gold) {
        this.gold = gold;
    }

    /**
     * 获取用户充值的金额
     *
     * @return money - 用户充值的金额
     */
    public Long getMoney() {
        return money;
    }

    /**
     * 设置用户充值的金额
     *
     * @param money 用户充值的金额
     */
    public void setMoney(Long money) {
        this.money = money;
    }
}