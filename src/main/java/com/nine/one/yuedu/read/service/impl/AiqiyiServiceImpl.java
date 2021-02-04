package com.nine.one.yuedu.read.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nine.one.yuedu.read.config.ApiConstant;
import com.nine.one.yuedu.read.config.FormateConstant;
import com.nine.one.yuedu.read.entity.BookInfo;
import com.nine.one.yuedu.read.entity.ChapterInfo;
import com.nine.one.yuedu.read.mapper.BookInfoMapper;
import com.nine.one.yuedu.read.mapper.ChapterInfoMapper;
import com.nine.one.yuedu.read.mapper.CpAuthBookMapper;
import com.nine.one.yuedu.read.service.AiqiyiService;
import com.nine.one.yuedu.read.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Author:李王柱
 * 2020/10/19
 */
@Service
public class AiqiyiServiceImpl implements AiqiyiService {
    @Autowired
    private BookInfoMapper bookInfoMapper;

    @Autowired
    private ChapterInfoMapper chapterInfoMapper;

    @Autowired
    private CpAuthBookMapper cpAuthBookMapper;
    @Override
    public JSONObject getBookIdListByLastUpdateTime(Long lastUpdateTime) throws ParseException {
        JSONObject result = new JSONObject();
        JSONArray books = new JSONArray();
        JSONObject data=new JSONObject();
        long timestamp=new Date().getTime();
        String lastUpdateTimeString = FormateConstant.SIMPLE_DATE_FORMAT.format(lastUpdateTime);
        Date lastUpdateTimeDate = FormateConstant.SIMPLE_DATE_FORMAT.parse(lastUpdateTimeString);
        List<Integer> aiqiyiBookIdList=bookInfoMapper.selectAiqiyiBookIdListByLastUpdateTime(lastUpdateTimeDate);
        for (Integer oneBookid : aiqiyiBookIdList){
            JSONObject oneBookJson = new JSONObject();
            oneBookJson.put("id",oneBookid);
            books.add(oneBookJson);
        }
        result.put("code","A00000");
        data.put("timestamp",timestamp);
        data.put("books",books);
        result.put("data",data);
        result.put("msg","Success");
        return result;
    }

    @Override
    public JSONObject getAiqiyiBookInfoById(Integer bookId) {
        JSONObject result = new JSONObject();
        JSONArray books = new JSONArray();
        JSONObject data=new JSONObject();
        BookInfo bookInfo = bookInfoMapper.selectByPrimaryKey(bookId);
        data.put("id",bookId);
        data.put("name",bookInfo.getBookName());
        data.put("author",bookInfo.getAuthor());
        data.put("briefDescription",bookInfo.getDescription());
        String keywords = bookInfo.getKeywords().replaceAll("，|、|；| ",",");
        data.put("keywords",keywords);
        data.put("coverImg",bookInfo.getPicUrl());
        data.put("bookType",2);//图书类型。1:出版物； 2：网络文学； 3：IP文学。
        data.put("bookMediaType",1);//图文类型。1：纯文字 2：图文 3：纯图片。
        //将连载状态转意
        Integer completeState = bookInfo.getCompleteState();
        completeState= completeState==0 ? 1 :2;
        data.put("progress",completeState);//完结状态。1：已完结；2：连载中。
        data.put("cpUpdateTime",bookInfo.getUpdateTime().getTime());//图书在CP的最后更新时间（时间戳，ms级别）
        data.put("category",getAiqiyiCategory(bookInfo.getCategory()));
        data.put("free",0);//免付费，就是该书有无付费章节 1 免费 0付费
        data.put("wordCount",bookInfo.getWords());
        result.put("data",data);
        result.put(	"code","A00000");
        result.put("msg","Success");
        return result;
    }

    @Override
    public JSONObject getAiqiyiBookstructureById(Integer bookId) {
        JSONObject result = new JSONObject();
        JSONArray volumes = new JSONArray();
        JSONObject data=new JSONObject();
        JSONObject volume = new JSONObject();
        volume.put("volumeType",3);
        volume.put("volumeId",1);
        volume.put("volumeNumber",1);
        JSONArray chaptersArray = new JSONArray();
        List<ChapterInfo> chapterInfos = chapterInfoMapper.selectChapterListByBookId(bookId);//获取这本书的章节列表
        for (ChapterInfo chapterInfo:chapterInfos){
            JSONObject oneChapterJson = new JSONObject();
            oneChapterJson.put("chapterId",chapterInfo.getChapterId());
            oneChapterJson.put("chapterNumber",chapterInfo.getChapterId());
            oneChapterJson.put("updateTime",chapterInfo.getUpdateTime().getTime());
            oneChapterJson.put("title",chapterInfo.getChapterName());
            oneChapterJson.put("free",chapterInfo.getIsFree());
            chaptersArray.add(oneChapterJson);
        }
        volume.put("chapters",chaptersArray);
        volumes.add(volume);
        data.put("volumes",volumes);
        result.put("data",data);
        result.put(	"code","A00000");
        result.put("msg","Success");
        return result;
    }

    @Override
    public JSONObject getAiqiyiChapterByBookIdAndChapterId(Integer bookId, Integer chapterId) {
        JSONObject result = new JSONObject();
        if (null == cpAuthBookMapper.getOneByCpAuthIdAndBookId(25, bookId)) {
            result.put("code", "A00001");
            result.put("msg", "没有" + bookId + "这本书的授权!请检查id是否正确");
            return result;
        }
        if (null==chapterInfoMapper.selectOneByBookIdAndChapterId(bookId,chapterId)){
            result.put("code", "A00001");
            result.put("msg","没有"+chapterId+"这一章节!请检查id是否正确");
            return result;
        }
        JSONObject data=new JSONObject();
        ChapterInfo chapterInfo = chapterInfoMapper.selectOneByBookIdAndChapterId(bookId, chapterId);
        data.put("id",chapterId);
        data.put("title",chapterInfo.getChapterName());
        data.put("bookId",bookId);
//在去OSS中拿章节内容的信息
        final String URL = String.format("%s/%s/%s/%s.txt", ApiConstant.Config.BASE_PATH, "booktxt", bookId, chapterId);
        String bookContent = HttpClientUtil.doGet(URL);
        for (int i=0;i<9;i++){
            if (null==bookContent){
                bookContent = HttpClientUtil.doGet(URL);
            }else {
                break;
            }
        }
        bookContent=bookContent.replace("\r\n","\n");
        data.put("content",bookContent);
        data.put("wordCount",chapterInfo.getWords());
        result.put("data",data);
        result.put(	"code","A00000");
        result.put("msg","Success");
        return result;
    }

    public static String getAiqiyiCategory(String oldCate){
        switch (oldCate){
            case "都市生活":
            case "科幻末世":
            case "历史军事":
            case "仙侠武侠":
            case "玄幻奇幻":
            case "玄幻仙侠":
            case "悬疑灵异":
                return "男频,"+oldCate;
            case "浪漫青春":
            case "古代言情":
            case "幻想言情":
            case "现代言情":
                return "女频,"+oldCate;
        }
        return "女频,"+oldCate;
    }
}
