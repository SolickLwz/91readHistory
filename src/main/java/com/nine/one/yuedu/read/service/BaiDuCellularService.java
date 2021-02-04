package com.nine.one.yuedu.read.service;

import com.alibaba.fastjson.JSONObject;

public interface BaiDuCellularService {
    JSONObject getBookList();

    JSONObject getBookInfo(String bookId);

    JSONObject getChapterList(String bookId);

    JSONObject getContent(String bookId, String chapterId);
}
