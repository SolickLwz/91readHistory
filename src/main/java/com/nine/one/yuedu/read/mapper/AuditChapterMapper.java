package com.nine.one.yuedu.read.mapper;

import com.nine.one.yuedu.read.entity.AuditChapter;
import com.nine.one.yuedu.read.entity.vo.AuditChapterAndBookNameVo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.MyMapper;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface AuditChapterMapper extends MyMapper<AuditChapter> {
    Integer selectMaxSortByAuditBookId(@Param("bookId") Integer bookId);

    List<AuditChapterAndBookNameVo> getThatAuthorBoxChapterAllAndBookNameForVo(@Param("author") String author,
                                                                               @Param("draftStatus") Integer draftStatus);

    List<AuditChapter> selectTiming(@Param("nowaday") Date nowaday,@Param("tomorrow") Date tomorrow);

    Integer selectByNickNameAndTimeInterval(@Param("bookId") int bookId,@Param("startDate") Date startDate,@Param("endDate") Date endDate);

    List<Integer> getLastMonthInformation(@Param("startTime")Date startTime,@Param("endTime") Date endTime);

}