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
import com.nine.one.yuedu.read.service.PointCrowdService;
import com.nine.one.yuedu.read.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author:李王柱
 * 2021/1/20
 */
@Service
public class PointCrowdServiceImpl implements PointCrowdService {

    @Autowired
    private BookInfoMapper bookInfoMapper;
    @Autowired
    private CpAuthBookMapper cpAuthBookMapper;
    @Autowired
    private ChapterInfoMapper chapterInfoMapper;

    Integer cpauthid=28;//点众的id是28
    @Override
    public JSONArray getBookList() {
        //点众获得合作方所有授权的书籍ID列表
        List<CpAuthBookVO> cpAuthBookVOS = cpAuthBookMapper.selectBookListByCpAuthId(cpauthid);
        JSONArray jsonArray = new JSONArray();
        for (CpAuthBookVO cpAuthBookVO:cpAuthBookVOS ){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name",cpAuthBookVO.getBookName());
            jsonObject.put("id",cpAuthBookVO.getBookId());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    @Override
    public JSONObject getBookInfo(String book_id) {
        int bookid = Integer.parseInt(book_id);
        BookInfo bookInfo = bookInfoMapper.selectByPrimaryKey(bookid);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",bookInfo.getId());
        jsonObject.put("name",bookInfo.getBookName());
        String keywords = bookInfo.getKeywords().replaceAll("，|、|；| ",",");
        jsonObject.put("tag",keywords);
        jsonObject.put("author",bookInfo.getAuthor());
        jsonObject.put("brief",bookInfo.getDescription());
        jsonObject.put("complete_status",bookInfo.getCompleteState() == 0 ? 1 : 0);
        jsonObject.put("cover",bookInfo.getPicUrl());
        jsonObject.put("category",pointGetCategoryId(bookInfo.getCategory()));
        return jsonObject;
    }

    @Override
    public JSONArray getVolumeList(String book_id) {
        int bookid = Integer.parseInt(book_id);
        JSONArray result = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",1);
        jsonObject.put("name","第一卷");
        List<ChapterInfo> chapterInfos = chapterInfoMapper.selectChapterListByBookId(bookid);//在这里对接口的sql做了修改,加了order by chapter_id
        JSONArray chapterlist = new JSONArray();
        for (ChapterInfo chapterInfo:chapterInfos){
            JSONObject detail = new JSONObject();
            detail.put("id",chapterInfo.getChapterId());
            detail.put("name",chapterInfo.getChapterName());
            chapterlist.add(detail);
        }
        jsonObject.put("chapterlist",chapterlist);
        result.add(jsonObject);
        return result;
    }

    @Override
    public JSONObject getChapterInfo(String book_id, String chapter_id) {
        int bookid = Integer.parseInt(book_id);
        int chapterid = Integer.parseInt(chapter_id);
        CpAuthBook cpAuthBook = new CpAuthBook();
        cpAuthBook.setBookId(bookid);
        cpAuthBook.setCpAuthId(cpauthid);
        if (null==cpAuthBookMapper.selectOne(cpAuthBook)){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code",3);
            jsonObject.put("message","内容不允许共享");
            return jsonObject;
        }

        ChapterInfo chapterInfo = chapterInfoMapper.selectOneByBookIdAndChapterId(bookid,chapterid);
        JSONObject result = new JSONObject();
        result.put("id",chapterid);
        result.put("name",chapterInfo.getChapterName());
        //在去OSS中拿章节内容的信息
        final String URL = String.format("%s/%s/%s/%s.txt", ApiConstant.Config.BASE_PATH, "booktxt", bookid, chapterid);
        String bookContent = HttpClientUtil.doGet(URL);
        for (int i=0;i<20;i++){
            if (null==bookContent){
                bookContent = HttpClientUtil.doGet(URL);
            }else {
                break;
            }
        }
        result.put("content",bookContent);
        return result;
    }

    @Override
    public JSONArray categoryList() {
        JSONArray result = new JSONArray();
        JSONObject jsonObject = new JSONObject();


        jsonObject.put("name","都市生活");
        jsonObject.put("id",1);
        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("name","科幻末世");
        jsonObject2.put("id",2);
        JSONObject jsonObject3 = new JSONObject();
        jsonObject3.put("name","历史军事");
        jsonObject3.put("id",3);
        JSONObject jsonObject4 = new JSONObject();
        jsonObject4.put("name","仙侠武侠");
        jsonObject4.put("id",4);
        JSONObject jsonObject5 = new JSONObject();
        jsonObject5.put("name","玄幻奇幻");
        jsonObject5.put("id",5);
        JSONObject jsonObject6 = new JSONObject();
        jsonObject6.put("name","玄幻仙侠");
        jsonObject6.put("id",6);
        JSONObject jsonObject7 = new JSONObject();
        jsonObject7.put("name","悬疑灵异");
        jsonObject7.put("id",7);
        JSONObject jsonObject8 = new JSONObject();
        jsonObject8.put("name","浪漫青春");
        jsonObject8.put("id",8);
        JSONObject jsonObject9 = new JSONObject();
        jsonObject9.put("name","古代言情");
        jsonObject9.put("id",9);
        JSONObject jsonObject10 = new JSONObject();
        jsonObject10.put("name","幻想言情");
        jsonObject10.put("id",10);
        JSONObject jsonObject11 = new JSONObject();
        jsonObject11.put("name","现代言情");
        jsonObject11.put("id",11);
        result.add(jsonObject);result.add(jsonObject2);result.add(jsonObject3);result.add(jsonObject4);result.add(jsonObject5);result.add(jsonObject6);
        result.add(jsonObject7);result.add(jsonObject8);result.add(jsonObject9);result.add(jsonObject10);result.add(jsonObject11);
        return result;
    }

    public static int pointGetCategoryId(String cateStr){
        switch (cateStr){
            case "都市生活":return 1;
            case "科幻末世":return 2;
            case "历史军事":return 3;
            case "仙侠武侠":return 4;
            case "玄幻奇幻":return 5;
            case "玄幻仙侠":return 6;
            case "悬疑灵异":return 7;
            case "浪漫青春":return 8;
            case "古代言情":return 9;
            case "幻想言情":return 10;
            case "现代言情":return 11;
        }
        return 1;
    }
}
