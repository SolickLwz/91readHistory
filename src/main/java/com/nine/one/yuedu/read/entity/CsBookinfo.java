package com.nine.one.yuedu.read.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "csbook.cs_bookinfo")
public class CsBookinfo {
    /**
     * 书编号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 书名
     */
    private String name;

    private String articlename;

    /**
     * 作者
     */
    private String author;

    private String cover;

    /**
     * 合作方id
     */
    private Integer cpid;

    /**
     * 合作方书id
     */
    private Long cpbookid;

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 图书分类编号
     */
    private Integer category;

    private String categoryname;

    /**
     * 字数
     */
    private Integer words;

    /**
     * 是否连载-1为连载，0为完本
     */
    private Integer serial;

    /**
     * 图书状态，0录入，1上线，2下线
     */
    private Integer status;

    /**
     * 消费过、下架仍可读
     */
    private Boolean readtype;

    /**
     * 添加时间
     */
    private Date addtime;

    /**
     * 最后更新时间
     */
    private Date updatetime;

    /**
     * 计费方式，1：按章，2：按本
     */
    private Integer chargetype;

    /**
     * 按章价格
     */
    private Integer unitprice;

    /**
     * 按本价格
     */
    private Integer totalprice;

    /**
     * 图书简介
     */
    private String intro;

    /**
     * 获取书编号
     *
     * @return id - 书编号
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置书编号
     *
     * @param id 书编号
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取书名
     *
     * @return name - 书名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置书名
     *
     * @param name 书名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return articlename
     */
    public String getArticlename() {
        return articlename;
    }

    /**
     * @param articlename
     */
    public void setArticlename(String articlename) {
        this.articlename = articlename;
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
     * @return cover
     */
    public String getCover() {
        return cover;
    }

    /**
     * @param cover
     */
    public void setCover(String cover) {
        this.cover = cover;
    }

    /**
     * 获取合作方id
     *
     * @return cpid - 合作方id
     */
    public Integer getCpid() {
        return cpid;
    }

    /**
     * 设置合作方id
     *
     * @param cpid 合作方id
     */
    public void setCpid(Integer cpid) {
        this.cpid = cpid;
    }

    /**
     * 获取合作方书id
     *
     * @return cpbookid - 合作方书id
     */
    public Long getCpbookid() {
        return cpbookid;
    }

    /**
     * 设置合作方书id
     *
     * @param cpbookid 合作方书id
     */
    public void setCpbookid(Long cpbookid) {
        this.cpbookid = cpbookid;
    }

    /**
     * 获取关键字
     *
     * @return keyword - 关键字
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * 设置关键字
     *
     * @param keyword 关键字
     */
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    /**
     * 获取图书分类编号
     *
     * @return category - 图书分类编号
     */
    public Integer getCategory() {
        return category;
    }

    /**
     * 设置图书分类编号
     *
     * @param category 图书分类编号
     */
    public void setCategory(Integer category) {
        this.category = category;
    }

    /**
     * @return categoryname
     */
    public String getCategoryname() {
        return categoryname;
    }

    /**
     * @param categoryname
     */
    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
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
     * 获取是否连载-1为连载，0为完本
     *
     * @return serial - 是否连载-1为连载，0为完本
     */
    public Integer getSerial() {
        return serial;
    }

    /**
     * 设置是否连载-1为连载，0为完本
     *
     * @param serial 是否连载-1为连载，0为完本
     */
    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    /**
     * 获取图书状态，0录入，1上线，2下线
     *
     * @return status - 图书状态，0录入，1上线，2下线
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置图书状态，0录入，1上线，2下线
     *
     * @param status 图书状态，0录入，1上线，2下线
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取消费过、下架仍可读
     *
     * @return readtype - 消费过、下架仍可读
     */
    public Boolean getReadtype() {
        return readtype;
    }

    /**
     * 设置消费过、下架仍可读
     *
     * @param readtype 消费过、下架仍可读
     */
    public void setReadtype(Boolean readtype) {
        this.readtype = readtype;
    }

    /**
     * 获取添加时间
     *
     * @return addtime - 添加时间
     */
    public Date getAddtime() {
        return addtime;
    }

    /**
     * 设置添加时间
     *
     * @param addtime 添加时间
     */
    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    /**
     * 获取最后更新时间
     *
     * @return updatetime - 最后更新时间
     */
    public Date getUpdatetime() {
        return updatetime;
    }

    /**
     * 设置最后更新时间
     *
     * @param updatetime 最后更新时间
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    /**
     * 获取计费方式，1：按章，2：按本
     *
     * @return chargetype - 计费方式，1：按章，2：按本
     */
    public Integer getChargetype() {
        return chargetype;
    }

    /**
     * 设置计费方式，1：按章，2：按本
     *
     * @param chargetype 计费方式，1：按章，2：按本
     */
    public void setChargetype(Integer chargetype) {
        this.chargetype = chargetype;
    }

    /**
     * 获取按章价格
     *
     * @return unitprice - 按章价格
     */
    public Integer getUnitprice() {
        return unitprice;
    }

    /**
     * 设置按章价格
     *
     * @param unitprice 按章价格
     */
    public void setUnitprice(Integer unitprice) {
        this.unitprice = unitprice;
    }

    /**
     * 获取按本价格
     *
     * @return totalprice - 按本价格
     */
    public Integer getTotalprice() {
        return totalprice;
    }

    /**
     * 设置按本价格
     *
     * @param totalprice 按本价格
     */
    public void setTotalprice(Integer totalprice) {
        this.totalprice = totalprice;
    }

    /**
     * 获取图书简介
     *
     * @return intro - 图书简介
     */
    public String getIntro() {
        return intro;
    }

    /**
     * 设置图书简介
     *
     * @param intro 图书简介
     */
    public void setIntro(String intro) {
        this.intro = intro;
    }
}
