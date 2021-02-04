package com.nine.one.yuedu.read.entity;

import javax.persistence.*;

@Table(name = "allow_free")
public class AllowFree {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 允许作者查询到的章节id
     */
    private Integer chapterid;

    /**
     * 创作章节id
     */
    private Integer auditchapterid;

    /**
     * 允许作者查询到的正式书id
     */
    private Integer bookid;

    /**
     * 创作书id
     */
    private Integer auditbookid;

    /**
     * 书名
     */
    private String bookname;

    /**
     * 章节名
     */
    private String chaptername;

    /**
     * 章节排序
     */
    private String sort;

    /**
     * 主编设置的年月,按理说这些东西应该另建一张表,在这都放一起方便一些
     */
    private String creatyearandmonth;

    /**
     * 千字价格
     */
    private Integer feebysum;

    /**
     * 当月奖励,奖金由主编设置
     */
    private Integer reward;

    /**
     * 当月的渠道分成,主编设置
     */
    private Integer share;

    /**
     * 作者
     */
    private String author;

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
     * 获取允许作者查询到的章节id
     *
     * @return chapterid - 允许作者查询到的章节id
     */
    public Integer getChapterid() {
        return chapterid;
    }

    /**
     * 设置允许作者查询到的章节id
     *
     * @param chapterid 允许作者查询到的章节id
     */
    public void setChapterid(Integer chapterid) {
        this.chapterid = chapterid;
    }

    /**
     * 获取创作章节id
     *
     * @return auditchapterid - 创作章节id
     */
    public Integer getAuditchapterid() {
        return auditchapterid;
    }

    /**
     * 设置创作章节id
     *
     * @param auditchapterid 创作章节id
     */
    public void setAuditchapterid(Integer auditchapterid) {
        this.auditchapterid = auditchapterid;
    }

    /**
     * 获取允许作者查询到的正式书id
     *
     * @return bookid - 允许作者查询到的正式书id
     */
    public Integer getBookid() {
        return bookid;
    }

    /**
     * 设置允许作者查询到的正式书id
     *
     * @param bookid 允许作者查询到的正式书id
     */
    public void setBookid(Integer bookid) {
        this.bookid = bookid;
    }

    /**
     * 获取创作书id
     *
     * @return auditbookid - 创作书id
     */
    public Integer getAuditbookid() {
        return auditbookid;
    }

    /**
     * 设置创作书id
     *
     * @param auditbookid 创作书id
     */
    public void setAuditbookid(Integer auditbookid) {
        this.auditbookid = auditbookid;
    }

    /**
     * 获取书名
     *
     * @return bookname - 书名
     */
    public String getBookname() {
        return bookname;
    }

    /**
     * 设置书名
     *
     * @param bookname 书名
     */
    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    /**
     * 获取章节名
     *
     * @return chaptername - 章节名
     */
    public String getChaptername() {
        return chaptername;
    }

    /**
     * 设置章节名
     *
     * @param chaptername 章节名
     */
    public void setChaptername(String chaptername) {
        this.chaptername = chaptername;
    }

    /**
     * 获取章节排序
     *
     * @return sort - 章节排序
     */
    public String getSort() {
        return sort;
    }

    /**
     * 设置章节排序
     *
     * @param sort 章节排序
     */
    public void setSort(String sort) {
        this.sort = sort;
    }

    /**
     * 获取主编设置的年月,按理说这些东西应该另建一张表,在这都放一起方便一些
     *
     * @return creatyearandmonth - 主编设置的年月,按理说这些东西应该另建一张表,在这都放一起方便一些
     */
    public String getCreatyearandmonth() {
        return creatyearandmonth;
    }

    /**
     * 设置主编设置的年月,按理说这些东西应该另建一张表,在这都放一起方便一些
     *
     * @param creatyearandmonth 主编设置的年月,按理说这些东西应该另建一张表,在这都放一起方便一些
     */
    public void setCreatyearandmonth(String creatyearandmonth) {
        this.creatyearandmonth = creatyearandmonth;
    }

    /**
     * 获取千字价格
     *
     * @return feebysum - 千字价格
     */
    public Integer getFeebysum() {
        return feebysum;
    }

    /**
     * 设置千字价格
     *
     * @param feebysum 千字价格
     */
    public void setFeebysum(Integer feebysum) {
        this.feebysum = feebysum;
    }

    /**
     * 获取当月奖励,奖金由主编设置
     *
     * @return reward - 当月奖励,奖金由主编设置
     */
    public Integer getReward() {
        return reward;
    }

    /**
     * 设置当月奖励,奖金由主编设置
     *
     * @param reward 当月奖励,奖金由主编设置
     */
    public void setReward(Integer reward) {
        this.reward = reward;
    }

    /**
     * 获取当月的渠道分成,主编设置
     *
     * @return share - 当月的渠道分成,主编设置
     */
    public Integer getShare() {
        return share;
    }

    /**
     * 设置当月的渠道分成,主编设置
     *
     * @param share 当月的渠道分成,主编设置
     */
    public void setShare(Integer share) {
        this.share = share;
    }

    /**
     * 获取作者
     *
     * @return author - 作者
     */
    public String getAuthor() {
        return author;
    }

    /**
     * 设置作者
     *
     * @param author 作者
     */
    public void setAuthor(String author) {
        this.author = author;
    }
}