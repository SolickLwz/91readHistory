package com.nine.one.yuedu.read.service;

import org.apache.http.HttpResponse;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Author:李王柱
 * 2020/6/14 0014
 * 对接阅文
 */
public interface ReadingLimitService {
    /**
     * 将要添加的书籍ID
     *
     * @param bookId
     */
    String insertBookById(Integer bookId) throws Exception;

    /**
     * 为这本book重新推送章节
     * */
    String retryChapterByBookId(Integer bookId) throws IOException;
}
