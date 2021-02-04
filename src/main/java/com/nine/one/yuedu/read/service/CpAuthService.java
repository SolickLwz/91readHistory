package com.nine.one.yuedu.read.service;

import com.github.pagehelper.PageInfo;
import com.nine.one.yuedu.read.config.JXResult;
import com.nine.one.yuedu.read.entity.BookInfo;
import com.nine.one.yuedu.read.entity.ChapterInfo;
import com.nine.one.yuedu.read.entity.CpAuth;
import com.nine.one.yuedu.read.entity.CpAuthBook;
import com.nine.one.yuedu.read.entity.vo.CpAuthBookVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author wangyuliang
 */
public interface CpAuthService {

    /**
     * 添加授权方
     *
     * @param cpAuth
     */
    void addCpAuth(CpAuth cpAuth);

    /**
     * 删除授权方
     *
     * @param id
     */
    void delCpAuthById(Integer id);

    /**
     * 获取Cp授权方的列表
     *
     * @return
     */
    List<CpAuth> getCpAuthList();


    /**
     * 根据授权方的ID,获取授权的书籍列表
     *
     * @param cpAuthId
     * @return
     */
  PageInfo<CpAuthBookVO> getCpAuthBookList(Integer cpAuthId, Integer pageIndex, Integer pageSize);


    /**
     * 根据授权方的ID,获取授权的书籍ID
     *
     * @param cpAuthId
     * @return
     */
    List<CpAuthBook> getCpAuthBookIdList(Integer cpAuthId);


    /**
     * 插入多个
     *
     * @param cpAuthId
     * @param bookIds
     */
    JXResult addCpAuthBook(Integer cpAuthId, String bookIds);


    /**
     * 删除授权的书
     *
     * @param id
     */
    void deleteCpAuthBook(Integer id);

    /**
     * 通过书籍ID获取书籍详情
     *
     * @param bookId
     * @return
     */
    Map<String,Object> getBookInfoByBookId(Integer bookId);


    /**
     * 通过
     *
     * @param bookId
     * @return
     */
    List<ChapterInfo> getChapterInfoList(Integer bookId);


    /**
     * 根据书籍ID和章节ID去获取章节的内容
     *
     * @param bookId
     * @param chapterId
     * @return
     */
    String getChapterContentByBookIdAndChapterId(Integer bookId, Integer chapterId);


   JXResult checkKeyAndParam(HttpServletRequest request);
}
