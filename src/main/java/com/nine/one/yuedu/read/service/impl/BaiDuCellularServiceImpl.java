package com.nine.one.yuedu.read.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nine.one.yuedu.read.config.ApiConstant;
import com.nine.one.yuedu.read.entity.BookInfo;
import com.nine.one.yuedu.read.entity.ChapterInfo;
import com.nine.one.yuedu.read.entity.CpAuthBook;
import com.nine.one.yuedu.read.entity.vo.CpAuthBookVO;
import com.nine.one.yuedu.read.mapper.BookInfoMapper;
import com.nine.one.yuedu.read.mapper.ChapterInfoMapper;
import com.nine.one.yuedu.read.mapper.CpAuthBookMapper;
import com.nine.one.yuedu.read.service.BaiDuCellularService;
import com.nine.one.yuedu.read.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author:李王柱
 * 2021/1/27
 */
@Service
public class BaiDuCellularServiceImpl implements BaiDuCellularService {
    @Autowired
    private CpAuthBookMapper cpAuthBookMapper;
    @Autowired
    private BookInfoMapper bookInfoMapper;
    @Autowired
    private ChapterInfoMapper chapterInfoMapper;

    private Integer cpAuthId=5;//手百的授权id是5
    @Override
    public JSONObject getBookList() {
        JSONObject result = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        List<CpAuthBookVO> cpAuthBookVOS = cpAuthBookMapper.selectBookListByCpAuthId(cpAuthId);
        if (cpAuthBookVOS.size()<1){
            result.put("code",5300);
            result.put("message","未知错误");
            result.put("result","没有授权书籍!");
        }
        for (CpAuthBookVO cpAuthBookVO:cpAuthBookVOS){
            JSONObject jsonMember = new JSONObject();
            jsonMember.put("bookId",cpAuthBookVO.getBookId());
            jsonMember.put("bookName",cpAuthBookVO.getBookName());
            jsonArray.add(jsonMember);
        }

        result.put("code",5100);
        result.put("message","成功");
        result.put("result",jsonArray);
        return result;
    }

    @Override
    public JSONObject getBookInfo(String bookIdStr) {
        int bookId;

        JSONObject JSONobj = new JSONObject();
        JSONObject result = new JSONObject();
        try {
            bookId = Integer.parseInt(bookIdStr);
        }catch (Exception e){
            JSONobj.put("code",5200);
            JSONobj.put("message","请求参数错误");
            JSONobj.put("result","请传入正确书id");
            return JSONobj;
        }
        BookInfo bookInfo = bookInfoMapper.selectByPrimaryKey(bookId);
        List<ChapterInfo> chapterInfos = chapterInfoMapper.selectChapterListByBookId(bookId);
        int freeChapterCount=0;
        for (ChapterInfo chapterInfo:chapterInfos){
            if (chapterInfo.getIsFree()==1){
                freeChapterCount++;
            }
        }
        result.put("bookId",bookInfo.getId());
        result.put("bookName",bookInfo.getBookName());
        result.put("bookAuthor",bookInfo.getAuthor());
        result.put("bookCover",bookInfo.getPicUrl());
        result.put("isFinished",bookInfo.getCompleteState()==0 ? 1 : 0);
        String category = bookInfo.getCategory();
        result.put("category",category);//图书分类(贵方分类名称)
        result.put("classify",getClassify(category));//我方男女频 id
        result.put("classifySecond",getClassifySecond(category));// int 我方二级分类 id
        result.put("classifyThird",getClassifyThird(category));// int 我方三级分类 id
        result.put("level","");// string 图书评级(S,A,B,C,D,E,没有传"")
        result.put("tags",bookInfo.getKeywords().replace("，|、|；| ", ","));// string 图书标签，用英文逗号分隔
        result.put("bookIntroduction",bookInfo.getDescription());// string 图书简介
        result.put("freeChapterCount",freeChapterCount);//int 免费章节数
        result.put("chapterCount",chapterInfos.size());//int 章节数
        result.put("wordCount",bookInfo.getWords());//int 字数

        JSONobj.put("code",5100);
        JSONobj.put("message","成功");
        JSONobj.put("result",result);
        return JSONobj;
    }

    @Override
    public JSONObject getChapterList(String bookIdStr) {
        int bookId;

        JSONObject JSONobj = new JSONObject();
        JSONArray result = new JSONArray();
        try {
            bookId = Integer.parseInt(bookIdStr);
        }catch (Exception e){
            JSONobj.put("code",5200);
            JSONobj.put("message","请求参数错误");
            JSONobj.put("result","请传入正确书id");
            return JSONobj;
        }
        List<ChapterInfo> chapterInfos = chapterInfoMapper.selectChapterListByBookId(bookId);
        for (ChapterInfo chapterInfo:chapterInfos){
            JSONObject oneChapter = new JSONObject();
            oneChapter.put("chapterId",chapterInfo.getChapterId());
            oneChapter.put("chapterName",chapterInfo.getChapterName());
            oneChapter.put("isFree",chapterInfo.getIsFree());
            oneChapter.put("wordCount",chapterInfo.getWords());
            result.add(oneChapter);
        }
        JSONobj.put("code",5100);
        JSONobj.put("message","成功");
        JSONobj.put("result",result);
        return JSONobj;
    }

    @Override
    public JSONObject getContent(String bookIdStr, String chapterIdStr) {
        int bookId;int chapterId;

        JSONObject JSONobj = new JSONObject();
        JSONObject result = new JSONObject();
        try {
            bookId = Integer.parseInt(bookIdStr);
            chapterId=Integer.parseInt(chapterIdStr);
        }catch (Exception e){
            JSONobj.put("code",5200);
            JSONobj.put("message","请求参数错误");
            JSONobj.put("result","请传入正确书id");
            return JSONobj;
        }
        CpAuthBook cpAuthBook = new CpAuthBook();
        cpAuthBook.setCpAuthId(cpAuthId);cpAuthBook.setBookId(bookId);
        List<CpAuthBook> select = cpAuthBookMapper.select(cpAuthBook);
        if (select.size()<1){
            JSONobj.put("code",5200);
            JSONobj.put("message","请求参数错误");
            JSONobj.put("result","请传入授权范围内的id!");
            return JSONobj;
        }
        //在去OSS中拿章节内容的信息
        final String URL = String.format("%s/%s/%s/%s.txt", ApiConstant.Config.BASE_PATH, "booktxt", bookId, chapterId);
        String bookContent = HttpClientUtil.doGet(URL);
        while (null==bookContent){
            bookContent = HttpClientUtil.doGet(URL);
        }

        result.put("content",bookContent);
        JSONobj.put("code",5100);
        JSONobj.put("message","成功");
        JSONobj.put("result",result);
        return JSONobj;
    }

    public static Integer getClassifyThird(String category) {//我方三级分类 id
        switch (category){
            case "都市生活":
                return 1210;
            case "科幻末世":
                return 1240;
            case "历史军事":
                return 1224;
            case "仙侠武侠":
                return 1190;
            case "玄幻奇幻":
                return 1189;
            case "玄幻仙侠":
                return 1197;
            case "悬疑灵异":
                return 1189;
            case "浪漫青春":
                return 1266;
            case "古代言情":
                return 1254;
            case "幻想言情":
                return 1264;
            case "现代言情":
                return 1251;
        }
        return 1210;
    }

    public static Integer getClassifySecond(String category) {//我方二级分类 id
        switch (category){
            case "都市生活":
                return 1172;//男频->都市
            case "科幻末世":
                return 1178;//男频->科幻
            case "历史军事":
                return 1175;//男频->军事
            case "仙侠武侠":
                return 1170;//男频->武侠
            case "玄幻奇幻":
                return 1169;//男频->奇幻
            case "玄幻仙侠":
                return 1171;//男频->仙侠
            case "悬疑灵异":
                return 1169;//男频->奇幻
            case "浪漫青春":
                return 1248;//女频->青春校园
            case "古代言情":
                return 1246;//女频->古代言情
            case "幻想言情":
                return 1247;//女频->幻想言情
            case "现代言情":
                return 1245;//女频->现代言情
        }
        return 1172;
    }

    public static Integer getClassify(String category){//我方男女频 id
        switch (category){
            case "都市生活":
            case "科幻末世":
            case "历史军事":
            case "仙侠武侠":
            case "玄幻奇幻":
            case "玄幻仙侠":
            case "悬疑灵异":
                return 1167;
            case "浪漫青春":
            case "古代言情":
            case "幻想言情":
            case "现代言情":
                return 1244;
        }
        return 1167;
    }
}
