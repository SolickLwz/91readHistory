package com.nine.one.yuedu.read.entity;

import javax.persistence.*;

@Table(name = "banner")
public class Banner {
    /**
     * 主键自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * banner图的id
     */
    @Column(name = "banner_id")
    private Integer bannerId;

    /**
     * banner图的适用位置
     */
    @Column(name = "banner_name")
    private String bannerName;

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
     * 获取banner图的id
     *
     * @return banner_id - banner图的id
     */
    public Integer getBannerId() {
        return bannerId;
    }

    /**
     * 设置banner图的id
     *
     * @param bannerId banner图的id
     */
    public void setBannerId(Integer bannerId) {
        this.bannerId = bannerId;
    }

    /**
     * 获取banner图的适用位置
     *
     * @return banner_name - banner图的适用位置
     */
    public String getBannerName() {
        return bannerName;
    }

    /**
     * 设置banner图的适用位置
     *
     * @param bannerName banner图的适用位置
     */
    public void setBannerName(String bannerName) {
        this.bannerName = bannerName;
    }
}
