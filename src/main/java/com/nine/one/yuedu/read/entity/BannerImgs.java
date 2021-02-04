package com.nine.one.yuedu.read.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import javax.persistence.*;

@Table(name = "banner_imgs")
public class BannerImgs {
    /**
     * 主键自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * banner图id
     */
    @Column(name = "banner_id")
    private Integer bannerId;

    /**
     * banner图的url
     */
    @Column(name = "img_url")
    private String imgUrl;

    /**
     * 轮播图对应的书的id
     */
    @Column(name = "book_id")
    private Integer bookId;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 添加时间
     */
    @Column(name = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

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
     * 获取banner图id
     *
     * @return banner_id - banner图id
     */
    public Integer getBannerId() {
        return bannerId;
    }

    /**
     * 设置banner图id
     *
     * @param bannerId banner图id
     */
    public void setBannerId(Integer bannerId) {
        this.bannerId = bannerId;
    }

    /**
     * 获取banner图的url
     *
     * @return img_url - banner图的url
     */
    public String getImgUrl() {
        return imgUrl;
    }

    /**
     * 设置banner图的url
     *
     * @param imgUrl banner图的url
     */
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    /**
     * 获取轮播图对应的书的id
     *
     * @return book_id - 轮播图对应的书的id
     */
    public Integer getBookId() {
        return bookId;
    }

    /**
     * 设置轮播图对应的书的id
     *
     * @param bookId 轮播图对应的书的id
     */
    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    /**
     * 获取排序
     *
     * @return sort - 排序
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * 设置排序
     *
     * @param sort 排序
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * 获取添加时间
     *
     * @return create_time - 添加时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置添加时间
     *
     * @param createTime 添加时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
