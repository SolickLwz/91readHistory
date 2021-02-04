package com.nine.one.yuedu.read.mapper;

import com.nine.one.yuedu.read.entity.UserAuthor;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.MyMapper;

import java.util.List;

public interface UserAuthorMapper extends MyMapper<UserAuthor> {

    List<UserAuthor> selectUserAuthorLikeNickName(@Param("nickname")String nickname);

    UserAuthor selectOneAuthUserByUsernameAndPassword(@Param("username") String username,@Param("password") String password);
}
