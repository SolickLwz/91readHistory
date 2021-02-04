package com.nine.one.yuedu.read.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nine.one.yuedu.read.entity.UserAuthor;
import com.nine.one.yuedu.read.mapper.UserAuthorMapper;
import com.nine.one.yuedu.read.service.UserAuthorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangyuliang
 */
@Service(value = "userAuthorService")
public class UserAuthorServiceImpl implements UserAuthorService {

    /**
     * 打印日志
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserAuthorMapper userAuthorMapper;


    @Override
    public void register(UserAuthor userAuthor) {
        userAuthor.setCreateTime(new Date());
        userAuthor.setUpdateTime(new Date());
        logger.info("用户进行注册操作", userAuthor);
        //2代表是普通作者,管理员不允许直接注册
        userAuthor.setRoleId(2);
        //1代表启用状态,新注册的用户默认启用
        userAuthor.setStatus(1);
        userAuthor.setCreateTime(new Date());
        userAuthor.setUpdateTime(new Date());
        //入库
        userAuthorMapper.insertSelective(userAuthor);
    }

    @Override
    public void delUserAuthorById(Integer id) {
        userAuthorMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void updateUserAuthor(UserAuthor userAuthor) {
        //在原来的基础上加判断,手机号要唯一
        /*String phoneNum = userAuthor.getPhoneNum();
        Example example = new Example(UserAuthor.class);
        example.createCriteria().andEqualTo("phoneNum", phoneNum);
        List<UserAuthor> userAuthors = userAuthorMapper.selectByExample(example);*/
        //if (null==userAuthors){
            userAuthor.setUpdateTime(new Date());
            userAuthorMapper.updateByPrimaryKeySelective(userAuthor);
        //}
    }

    @Override
    public UserAuthor findById(Integer id) {
        UserAuthor userAuthor = userAuthorMapper.selectByPrimaryKey(id);
        return userAuthor;
    }

    @Override
    public PageInfo<UserAuthor> getUserAuthorsByPage(Integer pageIndex, Integer pageSize
            , String nikeName) {
        PageHelper.startPage(pageIndex, pageSize);
        List<UserAuthor> userAuthors = userAuthorMapper.selectUserAuthorLikeNickName(nikeName);
        PageInfo<UserAuthor> pageInfo = new PageInfo<>(userAuthors);
        return pageInfo;
    }

    @Override
    public Map<String, Object> login(String username, String password) {
        Map<String, Object> result = new HashMap<>();
        //先用用户名去查询
        Example example = new Example(UserAuthor.class);
        example.createCriteria().andEqualTo("username", username).andEqualTo("password", password);
        UserAuthor userAuthor = userAuthorMapper.selectOneByExample(example);
        if (userAuthor != null && userAuthor.getStatus() == 1) {
            result.put("flag", true);
            result.put("userAuthor", userAuthor);
            return result;
        }
        result.put("flag", false);
        return result;
    }

    @Override
    public boolean judgeUsername(String username) {
        logger.info(username);
        Example example = new Example(UserAuthor.class);
        example.createCriteria().andEqualTo("username", username);
        List<UserAuthor> userAuthors = userAuthorMapper.selectByExample(example);
        if (userAuthors != null && userAuthors.size() > 0) {
            return false;
        }
        return true;
    }

    @Override
    public UserAuthor getUserByPhone(String phoneNum) {

        Example example = new Example(UserAuthor.class);
        example.createCriteria().andEqualTo("phoneNum",phoneNum);
        UserAuthor userAuthor = userAuthorMapper.selectOneByExample(example);
        return userAuthor;
    }

    @Override
    public UserAuthor getAuthUserByOneAuthUser(UserAuthor userAuthor) {
        UserAuthor userAuthor1 = userAuthorMapper.selectOne(userAuthor);
        return userAuthor1;
    }

    @Override
    public UserAuthor getAuthUserByUsernameAndPassword(String username, String password) {
        /*Example example = new Example(UserAuthor.class);
        example.createCriteria().andEqualTo("username",username).andEqualTo("password",password);
        UserAuthor userAuthor = userAuthorMapper.selectOneByExample(example);*/
        UserAuthor userAuthor=userAuthorMapper.selectOneAuthUserByUsernameAndPassword(username,password);
        return userAuthor;
    }
}
