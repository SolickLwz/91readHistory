package com.nine.one.yuedu.read.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "csbook.cs_chapterinfo")
public class CsChapterinfo {
    /**
     * 章节编号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 章节名
     */
    private String name;

    /**
     * 书编号
     */
    private Integer bookid;

    private Long cpbookid;

    private Long cpchapterid;

    /**
     * 卷id
     */
    private Integer volumnid;

    private Integer sort;

    private Integer size;

    @Column(name = "create_time")
    private Date createTime;

    /**
     * 状态，0录入，1上线，2下线
     */
    private Integer status;

    /**
     * 是否收费，0免费，1收费
     */
    private Integer isvip;

    /**
     * 获取章节编号
     *
     * @return id - 章节编号
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置章节编号
     *
     * @param id 章节编号
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取章节名
     *
     * @return name - 章节名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置章节名
     *
     * @param name 章节名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取书编号
     *
     * @return bookid - 书编号
     */
    public Integer getBookid() {
        return bookid;
    }

    /**
     * 设置书编号
     *
     * @param bookid 书编号
     */
    public void setBookid(Integer bookid) {
        this.bookid = bookid;
    }

    /**
     * @return cpbookid
     */
    public Long getCpbookid() {
        return cpbookid;
    }

    /**
     * @param cpbookid
     */
    public void setCpbookid(Long cpbookid) {
        this.cpbookid = cpbookid;
    }

    /**
     * @return cpchapterid
     */
    public Long getCpchapterid() {
        return cpchapterid;
    }

    /**
     * @param cpchapterid
     */
    public void setCpchapterid(Long cpchapterid) {
        this.cpchapterid = cpchapterid;
    }

    /**
     * 获取卷id
     *
     * @return volumnid - 卷id
     */
    public Integer getVolumnid() {
        return volumnid;
    }

    /**
     * 设置卷id
     *
     * @param volumnid 卷id
     */
    public void setVolumnid(Integer volumnid) {
        this.volumnid = volumnid;
    }

    /**
     * @return sort
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * @param sort
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * @return size
     */
    public Integer getSize() {
        return size;
    }

    /**
     * @param size
     */
    public void setSize(Integer size) {
        this.size = size;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取状态，0录入，1上线，2下线
     *
     * @return status - 状态，0录入，1上线，2下线
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态，0录入，1上线，2下线
     *
     * @param status 状态，0录入，1上线，2下线
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取是否收费，0免费，1收费
     *
     * @return isvip - 是否收费，0免费，1收费
     */
    public Integer getIsvip() {
        return isvip;
    }

    /**
     * 设置是否收费，0免费，1收费
     *
     * @param isvip 是否收费，0免费，1收费
     */
    public void setIsvip(Integer isvip) {
        this.isvip = isvip;
    }
}
