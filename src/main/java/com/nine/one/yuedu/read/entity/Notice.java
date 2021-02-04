package com.nine.one.yuedu.read.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import javax.persistence.*;

@Table(name = "notice")
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 通知的内容,用###分行
     */
    private String content;

    /**
     * 消息建立的时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date craeatetime;

    /**
     * 收到消息的时间,消息在收到一次后,就不再提示了
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date receivetime;

    /**
     * 消息的标题
     */
    private String head;

    /**
     * 接收消息的后台用户Id
     */
    private Integer authorid;

    /**
     * 作者名字
     */
    private String authornickname;

    /**
     * 作者是否读过这条消息,保存为string类型,yes是读过
     */
    private String isreceive;

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
     * 获取通知的内容,用###分行
     *
     * @return content - 通知的内容,用###分行
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置通知的内容,用###分行
     *
     * @param content 通知的内容,用###分行
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取消息建立的时间
     *
     * @return craeatetime - 消息建立的时间
     */
    public Date getCraeatetime() {
        return craeatetime;
    }

    /**
     * 设置消息建立的时间
     *
     * @param craeatetime 消息建立的时间
     */
    public void setCraeatetime(Date craeatetime) {
        this.craeatetime = craeatetime;
    }

    /**
     * 获取收到消息的时间,消息在收到一次后,就不再提示了
     *
     * @return receivetime - 收到消息的时间,消息在收到一次后,就不再提示了
     */
    public Date getReceivetime() {
        return receivetime;
    }

    /**
     * 设置收到消息的时间,消息在收到一次后,就不再提示了
     *
     * @param receivetime 收到消息的时间,消息在收到一次后,就不再提示了
     */
    public void setReceivetime(Date receivetime) {
        this.receivetime = receivetime;
    }

    /**
     * 获取消息的标题
     *
     * @return head - 消息的标题
     */
    public String getHead() {
        return head;
    }

    /**
     * 设置消息的标题
     *
     * @param head 消息的标题
     */
    public void setHead(String head) {
        this.head = head;
    }

    /**
     * 获取接收消息的后台用户Id
     *
     * @return authorid - 接收消息的后台用户Id
     */
    public Integer getAuthorid() {
        return authorid;
    }

    /**
     * 设置接收消息的后台用户Id
     *
     * @param authorid 接收消息的后台用户Id
     */
    public void setAuthorid(Integer authorid) {
        this.authorid = authorid;
    }

    /**
     * 获取作者名字
     *
     * @return authornickname - 作者名字
     */
    public String getAuthornickname() {
        return authornickname;
    }

    /**
     * 设置作者名字
     *
     * @param authornickname 作者名字
     */
    public void setAuthornickname(String authornickname) {
        this.authornickname = authornickname;
    }

    /**
     * 获取作者是否读过这条消息,保存为string类型,yes是读过
     *
     * @return isreceive - 作者是否读过这条消息,保存为string类型,yes是读过
     */
    public String getIsreceive() {
        return isreceive;
    }

    /**
     * 设置作者是否读过这条消息,保存为string类型,yes是读过
     *
     * @param isreceive 作者是否读过这条消息,保存为string类型,yes是读过
     */
    public void setIsreceive(String isreceive) {
        this.isreceive = isreceive;
    }
}