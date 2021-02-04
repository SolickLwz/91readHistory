package com.nine.one.yuedu.read.service;

import java.io.UnsupportedEncodingException;

/**
 * Author:李王柱
 * 2020/6/12 0012
 * 对接网易云
 */
public interface NeteaseBookService {

    /**
     * 将要添加的书籍ID
     *
     * @param bookId
     */
    String addBookForNetease(String bookId) throws Exception;

    /**
     * 将要添加章节的书
     * */
    String addCharpterForNetease(String bookId) throws Exception;
}
