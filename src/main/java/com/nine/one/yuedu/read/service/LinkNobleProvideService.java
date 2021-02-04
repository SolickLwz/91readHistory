package com.nine.one.yuedu.read.service;

import com.alibaba.fastjson.JSONObject;

/**
 * Author:李王柱
 * 2020/6/16 0016
 */
public interface LinkNobleProvideService {

    /**
     * 返回连尚 已授权书籍列表的 查询结果
     * */
    JSONObject queryAll();

    /**
     * 返回连尚 书籍详情
     * */
    JSONObject queryBookById(String bookId);

    /**
     * 返回 章节列表
     * */
    JSONObject queryChapterByBookId(String bookId);

    /**
     * 返回章节的内容
     * */
    JSONObject getChapterByBookIdAndChapterId(Integer bookId, Integer chapterId);

    /**
     * 返回分类id映射和分类名称
     * */
    JSONObject getCategory();
}
