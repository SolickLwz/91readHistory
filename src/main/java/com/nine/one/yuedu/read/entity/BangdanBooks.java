package com.nine.one.yuedu.read.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import javax.persistence.*;

@Table(name = "bangdan_books")
public class BangdanBooks {
    /**
     * 主键id,自增
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
     * 书籍id
     */
    @Column(name = "book_id")
    private Integer bookId;

    /**
     * 顺序
     */
    private Integer sort;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    /**
     * 获取主键id,自增
     *
     * @return id - 主键id,自增
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键id,自增
     *
     * @param id 主键id,自增
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
     * 获取书籍id
     *
     * @return book_id - 书籍id
     */
    public Integer getBookId() {
        return bookId;
    }

    /**
     * 设置书籍id
     *
     * @param bookId 书籍id
     */
    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    /**
     * 获取顺序
     *
     * @return sort - 顺序
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * 设置顺序
     *
     * @param sort 顺序
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取修改时间
     *
     * @return update_time - 修改时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置修改时间
     *
     * @param updateTime 修改时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
