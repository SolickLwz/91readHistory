package com.nine.one.yuedu.read.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.nine.one.yuedu.read.config.JXResult;
import com.nine.one.yuedu.read.entity.AuditBook;
import com.nine.one.yuedu.read.entity.AuditChapter;
import com.nine.one.yuedu.read.entity.BookInfo;
import com.nine.one.yuedu.read.entity.ChapterInfo;
import com.nine.one.yuedu.read.entity.vo.AuditChapterAndBookNameVo;
import com.nine.one.yuedu.read.entity.vo.BookInfoVO;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface AuthorCreationService {

    int addAuditBook(AuditBook auditBook);

    /**
     * 根据不同的条件分页查询书籍
     *
     * @param pageIndex 开始页
     * @param pageSize  每页显示的数量
     * @param privoder  作品提供者
     * @param bookName  书的名字
     * @param author    作者
     * @param id        图书Id
     * @param category  图书类型
     * @param auditStatus     是否审核
     * @return
     */
    PageInfo<AuditBook> getAuditBook4PageAndParam(Integer pageIndex,
                                                  Integer pageSize,
                                                  String privoder,
                                                  String bookName,
                                                  String author,
                                                  Integer id,
                                                  String category,
                                                  Integer auditStatus) throws ParseException;

    JXResult toReview(Integer id, Integer auditStatus);

    /**
     * 根据创作书籍的ID获得这本书的章节列表,分页,并以不同的排序方式排序
     *
     * @param bookId
     * @param pageIndex
     * @param pageSize
     * @param sort
     * @return
     */
    PageInfo<AuditChapter> getChapterListByBookIdPageAndSort(Integer bookId, Integer pageIndex, Integer pageSize, Integer sort,Integer draftStatus);

    void addAuditChapter(AuditChapter auditChapterInfo, String content);

    /**
     * 根据图书ID和章节ID获得章节的详情
     *
     * @param bookId
     * @param chapterId
     * @return
     */
    Map<String, Object> getChapterInfoByBookIdAndChapterId(Integer bookId, Integer chapterId, String type);

    void updateChapter(ChapterInfo chapterInfo, String content);

    void insertOption(AuditChapter auditChapter);

    void delChapterById(Integer id);

    AuditBook findById(Integer id);

    void updateBookInfoToauthorBook(BookInfo bookInfo);

    List<AuditBook> getThatAuthorBoxBookList(String nickname);

    PageInfo<AuditChapterAndBookNameVo> getThatAuthorBoxChapterAllAndBookNameForVo(Integer pageIndex, Integer pageSize, String nickname, Integer sort, Integer draftStatus);

    String judgeChapterId(String chapterId,Integer bookId);

    Integer getMaxChapterId(Integer bookId);

    boolean getHaveNickName(String nickname);

    void passed(String bookId, String chapterId,Integer isFree);

    String isSensitive(String content);//判断是否含有敏感词

    JSONArray getFeeDetail(String nickName,String start,String end) throws ParseException;

    JXResult ReReview(int bookId, String chapterName);

    JSONArray getFeeDetailByAdmin() throws ParseException;

    JSONArray getFeeDetailByAuthor(String nickName) throws ParseException;

    JXResult confirm(String creatyearandmonth, String bookname,String author);

    JXResult modifyFee(String creatyearandmonth, String bookname, Integer feebysum, Integer reward, Integer share);

    JXResult queryUnread(String authorName);

    JXResult callBack(Integer id);

    boolean replaceAuditToPutshelf(AuditChapter auditChapterBySelectChapterId);
}
