package com.nine.one.yuedu.read.entity.vo;

import com.nine.one.yuedu.read.entity.AuditChapter;

/**
 * Author:李王柱
 * 2020/8/27
 */
public class AuditChapterAndBookNameVo extends AuditChapter {
    private String bookName;

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }
}
