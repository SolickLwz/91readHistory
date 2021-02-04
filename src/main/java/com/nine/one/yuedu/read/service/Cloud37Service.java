package com.nine.one.yuedu.read.service;

import com.alibaba.fastjson.JSONObject;

public interface Cloud37Service {
    JSONObject queryBookList(Integer page, Integer rows);

    JSONObject queryBookById(int id);

    JSONObject queryChapterList(int id);

    JSONObject queryChapterContent(int id, int chapter_id);
}
