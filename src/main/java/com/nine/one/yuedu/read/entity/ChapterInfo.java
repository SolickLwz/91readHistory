package com.nine.one.yuedu.read.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import javax.persistence.*;

@Table(name = "chapter_info")
public class ChapterInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 书籍ID
     */
    @Column(name = "book_id")
    private Integer bookId;

    /**
     * 章节ID(序号)
     */
    @Column(name = "chapter_id")
    private Integer chapterId;

    /**
     * 章节名称
     */
    @Column(name = "chapter_name")
    private String chapterName;

    /**
     * 字数
     */
    private Integer words;

    /**
     * 是否免费:0收费,1免费
     */
    @Column(name = "is_free")
    private Byte isFree;

    @Column(name = "cp_chapter_id")
    private String cpChapterId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    @Column(name = "update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

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
     * 获取书籍ID
     *
     * @return book_id - 书籍ID
     */
    public Integer getBookId() {
        return bookId;
    }

    /**
     * 设置书籍ID
     *
     * @param bookId 书籍ID
     */
    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    /**
     * 获取章节ID(序号)
     *
     * @return chapter_id - 章节ID(序号)
     */
    public Integer getChapterId() {
        return chapterId;
    }

    /**
     * 设置章节ID(序号)
     *
     * @param chapterId 章节ID(序号)
     */
    public void setChapterId(Integer chapterId) {
        this.chapterId = chapterId;
    }

    /**
     * 获取章节名称
     *
     * @return chapter_name - 章节名称
     */
    public String getChapterName() {
        return chapterName;
    }

    /**
     * 设置章节名称
     *
     * @param chapterName 章节名称
     */
    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
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
     * 获取是否免费:0收费,1免费
     *
     * @return is_free - 是否免费:0收费,1免费
     */
    public Byte getIsFree() {
        return isFree;
    }

    /**
     * 设置是否免费:0收费,1免费
     *
     * @param isFree 是否免费:0收费,1免费
     */
    public void setIsFree(Byte isFree) {
        this.isFree = isFree;
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


    public String getCpChapterId() {
        return cpChapterId;
    }

    public void setCpChapterId(String cpChapterId) {
        this.cpChapterId = cpChapterId;
    }
}
