package com.nine.one.yuedu.read.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nine.one.yuedu.read.config.ApiConstant;
import com.nine.one.yuedu.read.entity.BookInfo;
import com.nine.one.yuedu.read.entity.ChapterInfo;
import com.nine.one.yuedu.read.mapper.BookInfoMapper;
import com.nine.one.yuedu.read.mapper.ChapterInfoMapper;
import com.nine.one.yuedu.read.mapper.CpAuthBookMapper;
import com.nine.one.yuedu.read.service.LinkNobleProvideService;
import com.nine.one.yuedu.read.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Author:李王柱
 * 2020/6/16 0016
 */
@Service(value = "linkNobleProvideService")
public class LinkNobleProvideServiceImpl implements LinkNobleProvideService {

    @Autowired
    private BookInfoMapper bookInfoMapper;

    @Autowired
    private ChapterInfoMapper chapterInfoMapper;

    @Autowired
    private CpAuthBookMapper cpAuthBookMapper;

    @Override
    public JSONObject queryAll() {
        JSONObject result = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        Integer cp_authId = 14;
        if (cpAuthBookMapper.getBookIdListByCpAuthId(cp_authId).size()<1){
            result.put("msg","授权失败");
            result.put("请联系提供者","是否存在授权问题?");
            return result;
        }
        List<Integer> bookIdListByCpAuthId = cpAuthBookMapper.getBookIdListByCpAuthId(cp_authId);
        for (Integer cpBookIdOne:bookIdListByCpAuthId){
            BookInfo bookInfo = bookInfoMapper.selectByPrimaryKey(cpBookIdOne);
            JSONObject object = new JSONObject();
            object.put("bookId", bookInfo.getId());
            object.put("lastUpdate", simpleDateFormat.format(bookInfo.getUpdateTime()));
            jsonArray.add(object);
        }
        result.put("code", 0);
        result.put("msg", "ok");
        result.put("data", jsonArray);
        return result;
    }

    @Override
    public JSONObject queryBookById(String bookId) {
        JSONObject result = new JSONObject();
        JSONObject data = new JSONObject();
        JSONObject lastUpdateChapter = new JSONObject();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

        BookInfo bookInfo = bookInfoMapper.selectByPrimaryKey(bookId);
        if (null == bookInfo) {
            result.put("code", -1);
            result.put("msg", "请检查书籍id是否正确!");
            return result;
        }
        if (null==cpAuthBookMapper.getOneByCpAuthIdAndBookId(14,Integer.parseInt(bookId))){
            result.put("code", -2);
            result.put("msg", "暂时没有得到此书的授权!");
            return result;
        }
        data.put("bookId", bookId);
        data.put("bookName", bookInfo.getBookName());
        data.put("authorId", 0);
        data.put("authorName", bookInfo.getAuthor());
        data.put("cateId", MyCate(bookInfo.getCategory()));
        data.put("cateName", bookInfo.getCategory());
        data.put("subcateId","");
        data.put("subcateName","");
        data.put("cover", bookInfo.getPicUrl());
        data.put("tags", bookInfo.getKeywords().replace("、", ","));
        data.put("description", bookInfo.getDescription());
        data.put("wordNum", bookInfo.getWords());
        data.put("chapterCount", chapterInfoMapper.selectMaxSortByBookId(Integer.parseInt(bookId)));
        data.put("tomeCount", 0);
        data.put("isVip", 0);
        Integer completeState = bookInfo.getCompleteState();
        //将连载状态转意
        completeState= completeState>0 ? 0 :1;
        data.put("finish",completeState);
        if (0 == completeState) {
            data.put("finishTime","");
        }else {
            data.put("finishTime", simpleDateFormat.format(bookInfo.getUpdateTime()));
        }
        data.put("lastUpdate", simpleDateFormat.format(bookInfo.getUpdateTime()));
        lastUpdateChapter.put("id", chapterInfoMapper.selectMaxSortByBookId(Integer.parseInt(bookId)));
        ChapterInfo lastCharpter = chapterInfoMapper.selectOneByBookIdAndChapterId(Integer.parseInt(bookId), chapterInfoMapper.selectMaxSortByBookId(Integer.parseInt(bookId)));
        lastUpdateChapter.put("name", lastCharpter.getChapterName());
        lastUpdateChapter.put("time", simpleDateFormat.format(lastCharpter.getUpdateTime()));
        data.put("lastUpdateChapter", lastUpdateChapter);
        data.put("status", 0);
        result.put("code", 0);
        result.put("msg", "ok");
        result.put("data", data);

        return result;
    }

    @Override
    public JSONObject queryChapterByBookId(String bookId) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        JSONObject result = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        BookInfo bookInfo = bookInfoMapper.selectByPrimaryKey(bookId);
        if (null == bookInfo) {
            result.put("msg", "请检查书籍id是否正确!");
            result.put("code", -1);
            result.put("data", jsonArray);
            return result;
        }
        if (null==cpAuthBookMapper.getOneByCpAuthIdAndBookId(14,Integer.parseInt(bookId))){
            result.put("code", -2);
            result.put("msg", "暂时没有得到此书的授权!");
            return result;
        }
        result.put("code", 0);
        result.put("msg", "ok");
        List<ChapterInfo> chapterInfos = chapterInfoMapper.selectChapterListByBookId(Integer.parseInt(bookId));
        for (ChapterInfo chapterInfo : chapterInfos) {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("chapterId", chapterInfo.getChapterId());
            jsonObj.put("chapterName", chapterInfo.getChapterName());
            jsonObj.put("tomeId", 0);
            jsonObj.put("tomeName", "正文卷");
            jsonObj.put("seqId", chapterInfo.getChapterId());
            jsonObj.put("isVip", chapterInfo.getIsFree());
            jsonObj.put("wordNum", chapterInfo.getWords());
            jsonObj.put("lastUpdate", simpleDateFormat.format(chapterInfo.getUpdateTime()));
            jsonObj.put("publishTime", simpleDateFormat.format(chapterInfo.getCreateTime()));
            jsonArray.add(jsonObj);
        }
        result.put("data", jsonArray);
        return result;
    }

    @Override
    public JSONObject getChapterByBookIdAndChapterId(Integer bookId, Integer chapterId) {
        ChapterInfo chapterInfo = chapterInfoMapper.selectOneByBookIdAndChapterId(bookId, chapterId);
        JSONObject result = new JSONObject();
        JSONObject data = new JSONObject();

        if (null==chapterInfoMapper.selectOneByBookIdAndChapterId(bookId, chapterId)){
            result.put("code",-1);
            result.put("msg","不存在该书籍id,或者章节id超出");
            result.put("data",data);
            return result;
        }
        if (null==cpAuthBookMapper.getOneByCpAuthIdAndBookId(14,bookId)){
            result.put("code", -2);
            result.put("msg", "暂时没有得到此书的授权!");
            return result;
        }
        data.put("chapterId", chapterInfo.getChapterId());
        //在去OSS中拿章节内容的信息
        final String URL = String.format("%s/%s/%s/%s.txt", ApiConstant.Config.BASE_PATH, "booktxt", bookId, chapterInfo.getChapterId());
        String bookContent = HttpClientUtil.doGet(URL);
        data.put("chapterContent",bookContent);

        data.put("md5", DigestUtils.md5DigestAsHex(bookContent.getBytes()));
        result.put("code",0);
        result.put("msg","ok");
        result.put("data",data);
        return result;
    }

    @Override
    public JSONObject getCategory() {
        JSONObject result = new JSONObject();
        JSONArray data = new JSONArray();

        JSONObject re1=new JSONObject();
        re1.put("channelId",0);
        re1.put("channelName","");
        re1.put("cateId",1);
        re1.put("cateName","都市生活");
        re1.put("parentId",0);
        re1.put("parentName","");
        data.add(re1);

        JSONObject re2=new JSONObject();
        re2.put("channelId",0);
        re2.put("channelName","");
        re2.put("cateId",2);
        re2.put("cateName","古代言情");
        re2.put("parentId",0);
        re2.put("parentName","");
        data.add(re2);

        JSONObject re3=new JSONObject();
        re3.put("channelId",0);
        re3.put("channelName","");
        re3.put("cateId",3);
        re3.put("cateName","幻想言情");
        re3.put("parentId",0);
        re3.put("parentName","");
        data.add(re3);

        JSONObject re4=new JSONObject();
        re4.put("channelId",0);
        re4.put("channelName","");
        re4.put("cateId",4);
        re4.put("cateName","科幻末世");
        re4.put("parentId",0);
        re4.put("parentName","");
        data.add(re4);

        JSONObject re5=new JSONObject();
        re5.put("channelId",0);
        re5.put("channelName","");
        re5.put("cateId",5);
        re5.put("cateName","浪漫青春");
        re5.put("parentId",0);
        re5.put("parentName","");
        data.add(re5);

        JSONObject re6=new JSONObject();
        re6.put("channelId",0);
        re6.put("channelName","");
        re6.put("cateId",6);
        re6.put("cateName","历史军事");
        re6.put("parentId",0);
        re6.put("parentName","");
        data.add(re6);

        JSONObject re7=new JSONObject();
        re7.put("channelId",0);
        re7.put("channelName","");
        re7.put("cateId",7);
        re7.put("cateName","仙侠武侠");
        re7.put("parentId",0);
        re7.put("parentName","");
        data.add(re7);

        JSONObject re8=new JSONObject();
        re8.put("channelId",0);
        re8.put("channelName","");
        re8.put("cateId",8);
        re8.put("cateName","现代言情");
        re8.put("parentId",0);
        re8.put("parentName","");
        data.add(re8);

        JSONObject re9=new JSONObject();
        re9.put("channelId",0);
        re9.put("channelName","");
        re9.put("cateId",9);
        re9.put("cateName","玄幻奇幻");
        re9.put("parentId",0);
        re9.put("parentName","");
        data.add(re9);

        JSONObject re10=new JSONObject();
        re10.put("channelId",0);
        re10.put("channelName","");
        re10.put("cateId",10);
        re10.put("cateName","玄幻仙侠");
        re10.put("parentId",0);
        re10.put("parentName","");
        data.add(re10);

        JSONObject re11=new JSONObject();
        re11.put("channelId",0);
        re11.put("channelName","");
        re11.put("cateId",11);
        re11.put("cateName","悬疑灵异");
        re11.put("parentId",0);
        re11.put("parentName","");
        data.add(re11);
                result.put("code",0);
                result.put("msg","ok");
                result.put("data",data);
        return result;
    }

    public static Integer MyCate(String cate) {
        switch (cate) {
            case "都市生活":
                return 1;
            case "古代言情":
                return 2;
            case "幻想言情":
                return 3;
            case "科幻末世":
                return 4;
            case "浪漫青春":
                return 5;
            case "历史军事":
                return 6;
            case "仙侠武侠":
                return 7;
            case "现代言情":
                return 8;
            case "玄幻奇幻":
                return 9;
            case "玄幻仙侠":
                return 10;
            case "悬疑灵异":
                return 11;
        }
        return 1;
    }
}
