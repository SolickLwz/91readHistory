package com.nine.one.yuedu.read.service;

import java.io.UnsupportedEncodingException;

public interface FlyreadService {
    String login() throws Exception;

    String addBook(Integer bookId,String key) throws UnsupportedEncodingException, Exception;

    String addChapterByBookId(int bookid, String key) throws UnsupportedEncodingException, Exception;

    String updateChapter(Integer bookId,Integer chapterId,String c_chapterid, String key) throws Exception;

    String getUpdateInfo(String bookidStr, String key)throws Exception;

    String putChapterBySelective(String bookidStr, String chaptername, String key)throws Exception;
}
