package com.nine.one.yuedu.read.service;

import com.github.pagehelper.PageInfo;
import com.nine.one.yuedu.read.entity.ChapterInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface ChapterService {

    /**
     * 上传完整的一本书,并且分离出章节
     *
     * @param bookId
     * @param file
     * @return
     */
    Map<String, Object> uploadTxtChangeChapterList(Integer bookId, MultipartFile file);

    /**
     * 添加章节
     *
     * @param chapterInfo
     */
    void addChapter(ChapterInfo chapterInfo,String content);

    /**
     * 根据ID删除章节
     *
     * @param id
     */
    void delChapterById(Integer id);

    /**
     * 修改章节
     *
     * @param chapterInfo
     */
    void updateChapter(ChapterInfo chapterInfo,String content);

    /**
     * 根据图书ID和章节ID获得章节的详情
     *
     * @param bookId
     * @param chapterId
     * @return
     */
    Map<String, Object> getChapterInfoByBookIdAndChapterId(Integer bookId, Integer chapterId,String type);

    /**
     * 根据书籍的ID获得这本书的章节列表,分页,并以不同的排序方式排序
     *
     * @param bookId
     * @param pageIndex
     * @param pageSize
     * @param sort
     * @return
     */
    PageInfo<ChapterInfo> getChapterListByBookIdPageAndSort(Integer bookId, Integer pageIndex, Integer pageSize, Integer sort);

    /*
    * 根据书籍id和前缀备份这本书
    * */
    String BackUpByPrefix(int bookId, String prefix);
}
