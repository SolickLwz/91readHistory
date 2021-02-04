package com.nine.one.yuedu.read.service;

import com.alibaba.fastjson.JSONObject;

import java.text.ParseException;

public interface AiqiyiService {
    JSONObject getBookIdListByLastUpdateTime(Long lastUpdateTime) throws ParseException;

    JSONObject getAiqiyiBookInfoById(Integer bookId);

    JSONObject getAiqiyiBookstructureById(Integer bookId);

    JSONObject getAiqiyiChapterByBookIdAndChapterId(Integer bookId, Integer chapterId);
}
