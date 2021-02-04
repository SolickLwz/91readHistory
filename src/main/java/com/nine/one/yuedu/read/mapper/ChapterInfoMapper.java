package com.nine.one.yuedu.read.mapper;

import com.nine.one.yuedu.read.entity.ChapterInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import tk.mybatis.MyMapper;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Component
public interface ChapterInfoMapper extends MyMapper<ChapterInfo> {

    /**
     * 根据书籍的ID删除章节
     * @param bookId
     * @return
     */
    int delByBookId(@Param("bookId") Integer bookId);

    Integer selectMaxSortByBookId(@Param("bookId") Integer bookId);

    List<ChapterInfo> selectChapterListByBookId(@Param("bookId") Integer bookId);

    /**
     * 根据书籍id和书籍中的章节id查单条
     */
    ChapterInfo selectOneByBookIdAndChapterId(@Param("bookId") Integer bookId,@Param("chapterId") Integer chapterId);

    Integer getWordsByMonth(@Param("bookId") int bookId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    List<ChapterInfo> selectRangeChapter(@Param("bookid")Integer bookid, @Param("start")Integer start, @Param("end")Integer end);

    List<ChapterInfo> selectChapterListByBookIdAndLikeName(@Param("bookId")Integer bookId,@Param("nameEnd") String nameEnd);

    HashMap<String, Integer> selectMinToIdMaxToBookIdOnTimeInterval(@Param("bookid")Integer bookid,@Param("startTime") Date startTime,@Param("endTime") Date endTime);
}
