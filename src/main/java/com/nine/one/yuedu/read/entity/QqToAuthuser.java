package com.nine.one.yuedu.read.entity;

import javax.persistence.*;

@Table(name = "qq_to_authuser")//read_91yuedu..
public class QqToAuthuser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nickname;

    private String openid;

    private String clientid;

    private Integer authorid;

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
     * @return nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @param nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * @return openid
     */
    public String getOpenid() {
        return openid;
    }

    /**
     * @param openid
     */
    public void setOpenid(String openid) {
        this.openid = openid;
    }

    /**
     * @return clientid
     */
    public String getClientid() {
        return clientid;
    }

    /**
     * @param clientid
     */
    public void setClientid(String clientid) {
        this.clientid = clientid;
    }

    /**
     * @return authorid
     */
    public Integer getAuthorid() {
        return authorid;
    }

    /**
     * @param authorid
     */
    public void setAuthorid(Integer authorid) {
        this.authorid = authorid;
    }
}