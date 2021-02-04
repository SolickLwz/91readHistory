package com.nine.one.yuedu.read.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import javax.persistence.*;

@Table(name = "reminder")
public class Reminder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * client用户id
     */
    private Integer uid;

    /**
     * 打赏的作者
     */
    private String author;

    private String bookname;

    /**
     * 打赏金额
     */
    private Long reminderval;

    /**
     * 打赏的时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date creagetime;

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
     * @return uid - client用户id
     */
    public Integer getUid() {
        return uid;
    }

    /**
     * 设置client用户id
     *
     * @param uid client用户id
     */
    public void setUid(Integer uid) {
        this.uid = uid;
    }

    /**
     * 获取打赏的作者
     *
     * @return author - 打赏的作者
     */
    public String getAuthor() {
        return author;
    }

    /**
     * 设置打赏的作者
     *
     * @param author 打赏的作者
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * @return bookname
     */
    public String getBookname() {
        return bookname;
    }

    /**
     * @param bookname
     */
    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    /**
     * 获取打赏金额
     *
     * @return reminderval - 打赏金额
     */
    public Long getReminderval() {
        return reminderval;
    }

    /**
     * 设置打赏金额
     *
     * @param reminderval 打赏金额
     */
    public void setReminderval(Long reminderval) {
        this.reminderval = reminderval;
    }

    /**
     * 获取打赏的时间
     *
     * @return creagetime - 打赏的时间
     */
    public Date getCreagetime() {
        return creagetime;
    }

    /**
     * 设置打赏的时间
     *
     * @param creagetime 打赏的时间
     */
    public void setCreagetime(Date creagetime) {
        this.creagetime = creagetime;
    }
}