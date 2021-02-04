package com.nine.one.yuedu.read.entity;

import javax.persistence.*;

@Table(name = "temp")
public class Temp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 备注消息,改了些什么
     */
    private String havedesc;

    /**
     * 修改前
     */
    private String old;

    /**
     * 修改后
     */
    private String that;

    /**
     * 这次操作的类型 
     */
    private String type;

    /**
     * 这是保留字段
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
     * 获取备注消息,改了些什么
     *
     * @return havedesc - 备注消息,改了些什么
     */
    public String getHavedesc() {
        return havedesc;
    }

    /**
     * 设置备注消息,改了些什么
     *
     * @param havedesc 备注消息,改了些什么
     */
    public void setHavedesc(String havedesc) {
        this.havedesc = havedesc;
    }

    /**
     * 获取修改前
     *
     * @return old - 修改前
     */
    public String getOld() {
        return old;
    }

    /**
     * 设置修改前
     *
     * @param old 修改前
     */
    public void setOld(String old) {
        this.old = old;
    }

    /**
     * 获取修改后
     *
     * @return that - 修改后
     */
    public String getThat() {
        return that;
    }

    /**
     * 设置修改后
     *
     * @param that 修改后
     */
    public void setThat(String that) {
        this.that = that;
    }

    /**
     * 获取这次操作的类型 
     *
     * @return type - 这次操作的类型 
     */
    public String getType() {
        return type;
    }

    /**
     * 设置这次操作的类型 
     *
     * @param type 这次操作的类型 
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取这是保留字段
     *
     * @return keep - 这是保留字段
     */
    public String getKeep() {
        return keep;
    }

    /**
     * 设置这是保留字段
     *
     * @param keep 这是保留字段
     */
    public void setKeep(String keep) {
        this.keep = keep;
    }
}