package com.nine.one.yuedu.read.entity;

import javax.persistence.*;

@Table(name = "cp_auth")
public class CpAuth {
    /**
     * 主键自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 合作方的名字
     */
    @Column(name = "cp_name")
    private String cpName;

    /**
     * 秘钥
     */
    @Column(name = "cp_key")
    private String cpKey;

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
     * 获取合作方的名字
     *
     * @return cp_name - 合作方的名字
     */
    public String getCpName() {
        return cpName;
    }

    /**
     * 设置合作方的名字
     *
     * @param cpName 合作方的名字
     */
    public void setCpName(String cpName) {
        this.cpName = cpName;
    }

    /**
     * 获取秘钥
     *
     * @return cp_key - 秘钥
     */
    public String getCpKey() {
        return cpKey;
    }

    /**
     * 设置秘钥
     *
     * @param cpKey 秘钥
     */
    public void setCpKey(String cpKey) {
        this.cpKey = cpKey;
    }
}
