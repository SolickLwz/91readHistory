package com.nine.one.yuedu.read.cp;

import com.nine.one.yuedu.read.entity.cp.MiGuBookInfo;
import com.nine.one.yuedu.read.entity.cp.MiGuChapter;
import com.nine.one.yuedu.read.entity.cp.MiGuResult;

import java.util.List;
import java.util.Map;

/**
 * @author wangyuliang
 * 给咪咕授权书
 */
public interface MiGuService {

    /**
     * 获取书籍列表
     *
     * @param appId
     * @return
     */
    List<Map<String, String>> getBookList(String appId);


    /**
     * 获取书籍详情
     *
     * @param bookId
     * @return
     */
    MiGuBookInfo getBookInfo(String bookId);

    /**
     * 获取图书分卷信息
     *
     * @param bookId
     * @return
     */
    List<Map<String, String>> getVolumeList(String bookId);

    /**
     * 获取章节列表
     *
     * @param bookId
     * @return
     */
    List<MiGuChapter> getChapterList(String bookId);

    /**
     * 获取章节详情
     *
     * @param bookId
     * @param chapterId
     * @return
     */
    Map<String, Object> getChapterInfo( String bookId, String chapterId);

    /**
     * 检验签名是否正确
     * @param appId
     * @param sign
     * @return
     */
    MiGuResult checkSign(String appId, String sign);
}
