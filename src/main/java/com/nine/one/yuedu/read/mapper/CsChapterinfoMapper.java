package com.nine.one.yuedu.read.mapper;

import com.nine.one.yuedu.read.entity.CsChapterinfo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.MyMapper;

import java.util.List;

public interface CsChapterinfoMapper extends MyMapper<CsChapterinfo> {
    List<CsChapterinfo> selectCpChapterAll(@Param("cpid") Integer cpid);

    int updateIdByBeforToAfter(@Param("id")Integer id,@Param("partId") Integer partId);

    List<CsChapterinfo> CsChapterInfoIdByTimeTaDu();

    CsChapterinfo selectMaxChapterByBookid(@Param("bookid") Integer bookid);

    List<CsChapterinfo> selectAfterThatChapterList(@Param("bookid") Integer bookid,@Param("sort")Integer sort);

    List<CsChapterinfo> selectAfterThatChapterListBySupplement(@Param("bookid") Integer bookid,@Param("startsort")Integer start,@Param("endsort")Integer end);
}