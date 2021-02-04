package com.nine.one.yuedu.read.entity.vo;

public class CpAuthBookVO {

    private Integer bookId;

    private String picUrl;

    private String bookName;

    private String author;

    private String category;

    private Integer cpAuthId;


    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getCpAuthId() {
        return cpAuthId;
    }

    public void setCpAuthId(Integer cpAuthId) {
        this.cpAuthId = cpAuthId;
    }

    @Override
    public String toString() {
        return "CpAuthBookVO{" +
                "bookId=" + bookId +
                ", picUrl='" + picUrl + '\'' +
                ", bookName='" + bookName + '\'' +
                ", author='" + author + '\'' +
                ", category='" + category + '\'' +
                ", cpAuthId=" + cpAuthId +
                '}';
    }
}
