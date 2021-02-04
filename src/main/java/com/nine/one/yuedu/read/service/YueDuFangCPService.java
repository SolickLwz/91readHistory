package com.nine.one.yuedu.read.service;

import com.nine.one.yuedu.read.config.JXResult;

public interface YueDuFangCPService {
    /*
     *从悦读坊获取小说列表
     * */
    String getAriticleList(Integer page) throws Exception;

    /*
    * 从悦读坊获取书籍详情,完善到数据库
    * */
    JXResult ArticleinfoUpdate(Integer id) throws Exception;

    /*
    * 从悦读坊获取章节详情
    * */
    JXResult ArticlechapterUpdate(Integer aid) throws Exception;

    /*
    * 从悦读坊获取内容,并上传到阿里云
    * */
    JXResult GetChaptercontent(Integer aid) throws Exception;
}
