package com.nine.one.yuedu.read.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import javax.persistence.*;

@Table(name = "book_info")
public class BookInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 书籍名称
     */
    @Column(name = "book_name")
    private String bookName;

    /**
     * 作者
     */
    private String author;

    /**
     * 关键字
     */
    private String keywords;

    /**
     * 字数
     */
    private Integer words;

    /**
     * 分类
     */
    private String category;

    /**
     * 1上架,2,下架
     */
    private Integer valid;

    /**
     * 0完结,1连载
     */
    @Column(name = "complete_state")
    private Integer completeState;

    /**
     * 最后章节ID
     */
    @Column(name = "last_chapter_id")
    private Integer lastChapterId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @Column(name = "update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * picture url
     */
    @Column(name = "pic_url")
    private String picUrl;

    /**
     * 接口的书籍id
     */
    @Column(name = "cp_book_id")
    private String cpBookId;

    /**
     * 是否搜索
     */
    @Column(name = "open_search")
    private Integer openSearch;

    /**
     * 阅读次数
     */
    @Column(name = "visit_count")
    private Integer visitCount;

    @Column(name = "cp_id")
    private Integer cpId;

    /**
     * 提供者
     */
    private String provider;

    /**
     * 千字稿费,默认0
     */
    @Column(name = "feebySum")
    private Integer feebysum;

    /**
     * 男主姓名
     */
    @Column(name = "male_lead")
    private String maleLead;

    /**
     * 女主姓名
     */
    private String heroine;

    /**
     * 一句话推荐
     */
    private String recommend;

    private String recommendtwo;

    /**
     * 推荐理由
     */
    private String reason;

    /**
     * 简介
     */
    private String description;

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
     * 获取书籍名称
     *
     * @return book_name - 书籍名称
     */
    public String getBookName() {
        return bookName;
    }

    /**
     * 设置书籍名称
     *
     * @param bookName 书籍名称
     */
    public void setBookName(String bookName) {
        this.bookName = bookName;
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

    /**
     * 获取关键字
     *
     * @return keywords - 关键字
     */
    public String getKeywords() {
        return keywords;
    }

    /**
     * 设置关键字
     *
     * @param keywords 关键字
     */
    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    /**
     * 获取字数
     *
     * @return words - 字数
     */
    public Integer getWords() {
        return words;
    }

    /**
     * 设置字数
     *
     * @param words 字数
     */
    public void setWords(Integer words) {
        this.words = words;
    }

    /**
     * 获取分类
     *
     * @return category - 分类
     */
    public String getCategory() {
        return category;
    }

    /**
     * 设置分类
     *
     * @param category 分类
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * 获取1上架,2,下架
     *
     * @return valid - 1上架,2,下架
     */
    public Integer getValid() {
        return valid;
    }

    /**
     * 设置1上架,2,下架
     *
     * @param valid 1上架,2,下架
     */
    public void setValid(Integer valid) {
        this.valid = valid;
    }

    /**
     * 获取0完结,1连载
     *
     * @return complete_state - 0完结,1连载
     */
    public Integer getCompleteState() {
        return completeState;
    }

    /**
     * 设置0完结,1连载
     *
     * @param completeState 0完结,1连载
     */
    public void setCompleteState(Integer completeState) {
        this.completeState = completeState;
    }

    /**
     * 获取最后章节ID
     *
     * @return last_chapter_id - 最后章节ID
     */
    public Integer getLastChapterId() {
        return lastChapterId;
    }

    /**
     * 设置最后章节ID
     *
     * @param lastChapterId 最后章节ID
     */
    public void setLastChapterId(Integer lastChapterId) {
        this.lastChapterId = lastChapterId;
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
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取picture url
     *
     * @return pic_url - picture url
     */
    public String getPicUrl() {
        return picUrl;
    }

    /**
     * 设置picture url
     *
     * @param picUrl picture url
     */
    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    /**
     * 获取接口的书籍id
     *
     * @return cp_book_id - 接口的书籍id
     */
    public String getCpBookId() {
        return cpBookId;
    }

    /**
     * 设置接口的书籍id
     *
     * @param cpBookId 接口的书籍id
     */
    public void setCpBookId(String cpBookId) {
        this.cpBookId = cpBookId;
    }

    /**
     * 获取是否搜索
     *
     * @return open_search - 是否搜索
     */
    public Integer getOpenSearch() {
        return openSearch;
    }

    /**
     * 设置是否搜索
     *
     * @param openSearch 是否搜索
     */
    public void setOpenSearch(Integer openSearch) {
        this.openSearch = openSearch;
    }

    /**
     * 获取阅读次数
     *
     * @return visit_count - 阅读次数
     */
    public Integer getVisitCount() {
        return visitCount;
    }

    /**
     * 设置阅读次数
     *
     * @param visitCount 阅读次数
     */
    public void setVisitCount(Integer visitCount) {
        this.visitCount = visitCount;
    }

    /**
     * @return cp_id
     */
    public Integer getCpId() {
        return cpId;
    }

    /**
     * @param cpId
     */
    public void setCpId(Integer cpId) {
        this.cpId = cpId;
    }

    /**
     * 获取提供者
     *
     * @return provider - 提供者
     */
    public String getProvider() {
        return provider;
    }

    /**
     * 设置提供者
     *
     * @param provider 提供者
     */
    public void setProvider(String provider) {
        this.provider = provider;
    }

    /**
     * 获取千字稿费,默认0
     *
     * @return feebySum - 千字稿费,默认0
     */
    public Integer getFeebysum() {
        return feebysum;
    }

    /**
     * 设置千字稿费,默认0
     *
     * @param feebysum 千字稿费,默认0
     */
    public void setFeebysum(Integer feebysum) {
        this.feebysum = feebysum;
    }

    /**
     * 获取男主姓名
     *
     * @return male_lead - 男主姓名
     */
    public String getMaleLead() {
        return maleLead;
    }

    /**
     * 设置男主姓名
     *
     * @param maleLead 男主姓名
     */
    public void setMaleLead(String maleLead) {
        this.maleLead = maleLead;
    }

    /**
     * 获取女主姓名
     *
     * @return heroine - 女主姓名
     */
    public String getHeroine() {
        return heroine;
    }

    /**
     * 设置女主姓名
     *
     * @param heroine 女主姓名
     */
    public void setHeroine(String heroine) {
        this.heroine = heroine;
    }

    /**
     * 获取一句话推荐
     *
     * @return recommend - 一句话推荐
     */
    public String getRecommend() {
        return recommend;
    }

    /**
     * 设置一句话推荐
     *
     * @param recommend 一句话推荐
     */
    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    /**
     * 获取推荐理由
     *
     * @return reason - 推荐理由
     */
    public String getReason() {
        return reason;
    }

    /**
     * 设置推荐理由
     *
     * @param reason 推荐理由
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * 获取简介
     *
     * @return description - 简介
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置简介
     *
     * @param description 简介
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public String getRecommendtwo() {
        return recommendtwo;
    }

    public void setRecommendtwo(String recommendtwo) {
        this.recommendtwo = recommendtwo;
    }
}