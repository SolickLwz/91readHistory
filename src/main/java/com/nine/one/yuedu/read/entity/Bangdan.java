package com.nine.one.yuedu.read.entity;

import javax.persistence.*;

@Table(name = "bangdan")
public class Bangdan {
    /**
     * id主键自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 榜单id
     */
    @Column(name = "bangdan_id")
    private Integer bangdanId;

    /**
     * 榜单名
     */
    @Column(name = "bangdan_name")
    private String bangdanName;

    /**
     * 获取id主键自增
     *
     * @return id - id主键自增
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置id主键自增
     *
     * @param id id主键自增
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取榜单id
     *
     * @return bangdan_id - 榜单id
     */
    public Integer getBangdanId() {
        return bangdanId;
    }

    /**
     * 设置榜单id
     *
     * @param bangdanId 榜单id
     */
    public void setBangdanId(Integer bangdanId) {
        this.bangdanId = bangdanId;
    }

    /**
     * 获取榜单名
     *
     * @return bangdan_name - 榜单名
     */
    public String getBangdanName() {
        return bangdanName;
    }

    /**
     * 设置榜单名
     *
     * @param bangdanName 榜单名
     */
    public void setBangdanName(String bangdanName) {
        this.bangdanName = bangdanName;
    }
}
