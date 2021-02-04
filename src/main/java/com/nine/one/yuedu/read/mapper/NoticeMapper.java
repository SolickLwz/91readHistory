package com.nine.one.yuedu.read.mapper;

import com.nine.one.yuedu.read.entity.Notice;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.MyMapper;

public interface NoticeMapper extends MyMapper<Notice> {
    Notice queryUnreadByAuthorNickName(@Param("authorName") String authorName);
}