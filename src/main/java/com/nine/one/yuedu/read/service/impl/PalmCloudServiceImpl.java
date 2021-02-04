package com.nine.one.yuedu.read.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nine.one.yuedu.read.config.ApiConstant;
import com.nine.one.yuedu.read.entity.BookInfo;
import com.nine.one.yuedu.read.entity.ChapterInfo;
import com.nine.one.yuedu.read.mapper.BookInfoMapper;
import com.nine.one.yuedu.read.mapper.ChapterInfoMapper;
import com.nine.one.yuedu.read.mapper.CpAuthBookMapper;
import com.nine.one.yuedu.read.service.PalmCloudService;
import com.nine.one.yuedu.read.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Author:李王柱
 * 2020/9/8
 */
@Service
public class PalmCloudServiceImpl implements PalmCloudService {
    @Autowired
    private BookInfoMapper bookInfoMapper;

    @Autowired
    private ChapterInfoMapper chapterInfoMapper;

    @Autowired
    private CpAuthBookMapper cpAuthBookMapper;

    @Override
    public JSONObject novelList() {
        JSONObject result = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        Integer cp_authId = 22;
        if (cpAuthBookMapper.getBookIdListByCpAuthId(cp_authId).size()<1){
            result.put("msg","授权失败");
            result.put("code",-1);
            return result;
        }
        List<Integer> bookIdListByCpAuthId = cpAuthBookMapper.getBookIdListByCpAuthId(cp_authId);
        for (Integer cpBookIdOne:bookIdListByCpAuthId){
            BookInfo bookInfo = bookInfoMapper.selectByPrimaryKey(cpBookIdOne);
            JSONObject object = new JSONObject();
            object.put("id", cpBookIdOne);
            object.put("title", bookInfo.getBookName());
            jsonArray.add(object);
        }
        result.put("data", jsonArray);
        return result;
    }

    @Override
    public JSONObject novelInfo(Integer nid) {
        JSONObject result = new JSONObject();
        JSONObject data = new JSONObject();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

        BookInfo bookInfo = bookInfoMapper.selectByPrimaryKey(nid);
        if (null == bookInfo) {
            result.put("code", -1);
            result.put("msg", "请检查书籍id是否正确!");
            return result;
        }
        if (null==cpAuthBookMapper.getOneByCpAuthIdAndBookId(22,nid)){
            result.put("code", -2);
            result.put("msg", "暂时没有得到此书的授权!");
            return result;
        }
        data.put("id", nid);
        data.put("title", bookInfo.getBookName());
        data.put("author", bookInfo.getAuthor());
        data.put("category", bookInfo.getCategory());
        data.put("gender", MyGender(bookInfo.getCategory()));
        Integer completeState = bookInfo.getCompleteState();
        String status="";
        //将连载状态转意
        status= completeState>0 ? "ongoing" :"completed";
        data.put("status",status);
        data.put("words", bookInfo.getWords());
        data.put("cover", bookInfo.getPicUrl());
        data.put("summary", bookInfo.getDescription());
        result.put("data", data);

        return result;
    }

    @Override
    public JSONObject cataLog(int nid) {
        JSONObject result = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        BookInfo bookInfo = bookInfoMapper.selectByPrimaryKey(nid);
        if (null == bookInfo) {
            result.put("msg", "请检查书籍id是否正确!");
            result.put("code", -1);
            return result;
        }
        if (null==cpAuthBookMapper.getOneByCpAuthIdAndBookId(22,nid)){
            result.put("code", -2);
            result.put("msg", "暂时没有得到此书的授权!");
            return result;
        }
        List<ChapterInfo> chapterInfos = chapterInfoMapper.selectChapterListByBookId(nid);
        for (ChapterInfo chapterInfo : chapterInfos) {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("id", chapterInfo.getChapterId());
            jsonObj.put("title", chapterInfo.getChapterName());
            //转义,0 表示免费，1 表示收费
            int isFree=chapterInfo.getIsFree();
            isFree=isFree==0 ? 1:0;
            jsonObj.put("is_free", isFree);
            jsonArray.add(jsonObj);
        }
        result.put("data", jsonArray);
        return result;
    }

    @Override
    public JSONObject chapterContent(int nid, int aid) {
        ChapterInfo chapterInfo = chapterInfoMapper.selectOneByBookIdAndChapterId(nid, aid);
        JSONObject result = new JSONObject();
        JSONObject data = new JSONObject();

        if (null==chapterInfoMapper.selectOneByBookIdAndChapterId(nid, aid)){
            result.put("code",-1);
            result.put("msg","不存在该书籍id,或者章节id超出");
            return result;
        }
        if (null==cpAuthBookMapper.getOneByCpAuthIdAndBookId(22,nid)){
            result.put("code", -2);
            result.put("msg", "暂时没有得到此书的授权!");
            return result;
        }

        //在去OSS中拿章节内容的信息
        final String URL = String.format("%s/%s/%s/%s.txt", ApiConstant.Config.BASE_PATH, "booktxt", nid, aid);
        String Content = HttpClientUtil.doGet(URL);
        data.put("id", aid);
        data.put("title",chapterInfo.getChapterName());
        data.put("content",Content);
        result.put("data",data);
        return result;
    }

    private String MyGender(String category) {
        switch (category) {
            case "都市生活":case "古代言情": case "幻想言情":  case "浪漫青春":   case "现代言情":
                return "female";
            case "科幻末世":  case "历史军事":  case "仙侠武侠": case "玄幻奇幻":  case "玄幻仙侠": case "悬疑灵异":
                return "male";
        }
        return "female";
    }
}
