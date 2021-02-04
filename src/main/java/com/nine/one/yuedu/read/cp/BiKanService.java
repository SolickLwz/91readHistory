package com.nine.one.yuedu.read.cp;

import com.nine.one.yuedu.read.entity.cp.BiKanBookInfo;
import com.nine.one.yuedu.read.entity.cp.BiKanResult;
import com.nine.one.yuedu.read.entity.cp.BiKanVolume;

import java.util.List;
import java.util.Map;

public interface BiKanService {

    /**
     * 获取书籍的ID列表
     *
     * @param identity
     * @return
     */
    Map<String,Object> getBookIds(String identity);

    /**
     * 通过书籍ID获取书籍的详情
     *
     * @param bookId
     * @return
     */
    BiKanBookInfo getBookInfoByBookId(Integer bookId);

    /**
     * 获取卷信息和章节列表
     *
     * @param bookId
     * @return
     */
    BiKanVolume getBookStructureByBookId(Integer bookId);


    /**
     * 获取章节的内容,通过书籍的ID和章节的ID
     *
     * @param bookId
     * @param chapterId
     * @return
     */
    Map<String, Object> getChapterContentByBookIdAndChapterId(Integer bookId, Integer chapterId);
}
