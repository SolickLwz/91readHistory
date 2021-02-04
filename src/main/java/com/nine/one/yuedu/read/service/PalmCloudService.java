package com.nine.one.yuedu.read.service;

import com.alibaba.fastjson.JSONObject;

public interface PalmCloudService{
    JSONObject novelList();

    JSONObject novelInfo(Integer nid);

    JSONObject cataLog(int parseInt);

    JSONObject chapterContent(int nid, int aid);
}
