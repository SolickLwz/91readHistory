package com.nine.one.yuedu.read.mapper;

import com.nine.one.yuedu.read.entity.AllowFree;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.MyMapper;

public interface AllowFreeMapper extends MyMapper<AllowFree> {
    AllowFree selectIsHaveByBooknameAndFeebysumIsNotZero(@Param("bookname")String bookname);

    AllowFree selectIsHaveByBooknameAndcreatyearandmonthIsNull(@Param("bookname")String bookName);
}