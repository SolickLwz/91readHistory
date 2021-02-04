package com.nine.one.yuedu.read.service;


import com.github.pagehelper.PageInfo;
import com.nine.one.yuedu.read.entity.UserAuthor;

import java.util.Map;

/**
 * @author wangyuliang
 * 作者和管理员服务
 */
public interface UserAuthorService {

    /**
     * 作者注册
     *
     * @param userAuthor
     */
    void register(UserAuthor userAuthor);


    /**
     * 通过Id删除作者
     *
     * @param id
     */
    void delUserAuthorById(Integer id);

    /**
     * 修改作者信息
     *
     * @param userAuthor
     */
    void updateUserAuthor(UserAuthor userAuthor);


    /**
     * 查询作者信息通过Id
     *
     * @param id
     * @return
     */
    UserAuthor findById(Integer id);


    /**
     * 分页查询列表
     *
     * @param pageIndex
     * @param pageSize
     * @param nikeName
     * @return
     */
    PageInfo<UserAuthor> getUserAuthorsByPage(Integer pageIndex, Integer pageSize, String nikeName);


    /**
     * 作者用户或管理员用户登录
     *
     * @param username
     * @param password
     * @return
     */
    Map<String, Object> login(String username, String password);

    /**
     * 判断用户名是否可以被注册
     *
     * @param username
     * @return
     */
    boolean judgeUsername(String username);


    UserAuthor getUserByPhone(String phoneNum);

    //根据一个user对象获取完整的信息
    UserAuthor getAuthUserByOneAuthUser(UserAuthor userAuthor);

    //根据账号密码获取完整的信息
    UserAuthor getAuthUserByUsernameAndPassword(String username, String password);
}
