package com.nine.one.yuedu.read.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public interface PointCrowdService {
    JSONArray getBookList();

    JSONObject getBookInfo(String book_id);

    JSONArray getVolumeList(String book_id);

    JSONObject getChapterInfo(String book_id, String chapter_id);

    JSONArray categoryList();
}
