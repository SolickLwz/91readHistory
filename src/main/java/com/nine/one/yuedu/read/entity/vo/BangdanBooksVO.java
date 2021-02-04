package com.nine.one.yuedu.read.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class BangdanBooksVO {

    private Integer bookId;
    private String bookName;
    private String author;
    private Integer words;
    private String category;
    private Integer completeState;
    private Integer lastChapterId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    private String picUrl;
    private String description;
    private Integer visitCount;
    private Integer sort;
    private Integer id;
    private Integer bangdanId;


    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBangdanId() {
        return bangdanId;
    }

    public void setBangdanId(Integer bangdanId) {
        this.bangdanId = bangdanId;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getWords() {
        return words;
    }

    public void setWords(Integer words) {
        this.words = words;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getCompleteState() {
        return completeState;
    }

    public void setCompleteState(Integer completeState) {
        this.completeState = completeState;
    }

    public Integer getLastChapterId() {
        return lastChapterId;
    }

    public void setLastChapterId(Integer lastChapterId) {
        this.lastChapterId = lastChapterId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(Integer visitCount) {
        this.visitCount = visitCount;
    }
}
