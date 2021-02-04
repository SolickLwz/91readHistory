package com.nine.one.yuedu.read.entity;

import javax.persistence.*;

@Table(name = "csbook.tadu_to_jxtc")
public class TaduToJxtc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 书名
     */
    @Column(name = "book_name")
    private String bookName;

    /**
     * 塔读保存的章节id
     */
    @Column(name = "part_id")
    private Integer partId;

    /**
     * 章节名
     */
    private String title;

    /**
     * 章节排序
     */
    @Column(name = "part_numb")
    private Integer partNumb;

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
     * 获取书名
     *
     * @return book_name - 书名
     */
    public String getBookName() {
        return bookName;
    }

    /**
     * 设置书名
     *
     * @param bookName 书名
     */
    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    /**
     * 获取塔读保存的章节id
     *
     * @return part_id - 塔读保存的章节id
     */
    public Integer getPartId() {
        return partId;
    }

    /**
     * 设置塔读保存的章节id
     *
     * @param partId 塔读保存的章节id
     */
    public void setPartId(Integer partId) {
        this.partId = partId;
    }

    /**
     * 获取章节名
     *
     * @return title - 章节名
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置章节名
     *
     * @param title 章节名
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取章节排序
     *
     * @return part_numb - 章节排序
     */
    public Integer getPartNumb() {
        return partNumb;
    }

    /**
     * 设置章节排序
     *
     * @param partNumb 章节排序
     */
    public void setPartNumb(Integer partNumb) {
        this.partNumb = partNumb;
    }
}