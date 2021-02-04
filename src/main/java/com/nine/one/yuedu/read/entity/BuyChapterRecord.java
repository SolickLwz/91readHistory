package com.nine.one.yuedu.read.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "buy_chapter_record")
public class BuyChapterRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * client用户id
     */
    private Integer userid;

    private Date buytime;

    private Integer bookid;

    private Integer chapterid;

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
     * 获取client用户id
     *
     * @return userid - client用户id
     */
    public Integer getUserid() {
        return userid;
    }

    /**
     * 设置client用户id
     *
     * @param userid client用户id
     */
    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    /**
     * @return buytime
     */
    public Date getBuytime() {
        return buytime;
    }

    /**
     * @param buytime
     */
    public void setBuytime(Date buytime) {
        this.buytime = buytime;
    }

    /**
     * @return bookid
     */
    public Integer getBookid() {
        return bookid;
    }

    /**
     * @param bookid
     */
    public void setBookid(Integer bookid) {
        this.bookid = bookid;
    }

    /**
     * @return chapterid
     */
    public Integer getChapterid() {
        return chapterid;
    }

    /**
     * @param chapterid
     */
    public void setChapterid(Integer chapterid) {
        this.chapterid = chapterid;
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