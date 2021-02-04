package com.nine.one.yuedu.read.entity;

import javax.persistence.*;

@Table(name = "cp_auth_book")
public class CpAuthBook {
    /**
     * 主键自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * cp合作方的ID
     */
    @Column(name = "cp_auth_id")
    private Integer cpAuthId;

    /**
     * 书籍ID
     */
    @Column(name = "book_id")
    private Integer bookId;

    /**
     * 获取主键自增
     *
     * @return id - 主键自增
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键自增
     *
     * @param id 主键自增
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取cp合作方的ID
     *
     * @return cp_auth_id - cp合作方的ID
     */
    public Integer getCpAuthId() {
        return cpAuthId;
    }

    /**
     * 设置cp合作方的ID
     *
     * @param cpAuthId cp合作方的ID
     */
    public void setCpAuthId(Integer cpAuthId) {
        this.cpAuthId = cpAuthId;
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

    @Override
    public String toString() {
        return "CpAuthBook{" +
                "id=" + id +
                ", cpAuthId=" + cpAuthId +
                ", bookId=" + bookId +
                '}';
    }
}
