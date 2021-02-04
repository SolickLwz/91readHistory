package com.nine.one.yuedu.read.mapper;

import com.nine.one.yuedu.read.entity.Reminder;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.MyMapper;

import java.util.Date;
import java.util.List;

public interface ReminderMapper extends MyMapper<Reminder> {
    List<Reminder> selectAuthorRewardByPreviousMonth(@Param("start") Date start,@Param("end") Date end);
}