package com.nine.one.yuedu.read.service;

public interface AliBookFlagService {
    /**
     * 向书旗提供书籍id列表
     * */
    String getBookList();

    /**
     * 根据书id提供详情
     * */
    String getBookDetailsById(Integer bookId);

    /**
     * 根据书id提供章节列表
     * */
    String getChapterListByBookId(Integer bookid);

    /**
     * 根据书id和章节id提供正文
     * */
    String getchapterContentByBookIdAndChapterId(Integer bookid, Integer chapterid);
}
