package com.nine.one.yuedu.read.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nine.one.yuedu.read.config.ApiConstant;
import com.nine.one.yuedu.read.entity.BookInfo;
import com.nine.one.yuedu.read.entity.ChapterInfo;
import com.nine.one.yuedu.read.entity.UserAuthor;
import com.nine.one.yuedu.read.mapper.BookInfoMapper;
import com.nine.one.yuedu.read.mapper.ChapterInfoMapper;
import com.nine.one.yuedu.read.mapper.CpAuthBookMapper;
import com.nine.one.yuedu.read.mapper.UserAuthorMapper;
import com.nine.one.yuedu.read.service.Cloud37Service;
import com.nine.one.yuedu.read.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Author:李王柱
 * 2020/9/16
 */
@Service
public class Cloud37ServiceImpl implements Cloud37Service {

    @Autowired
    private BookInfoMapper bookInfoMapper;

    @Autowired
    private ChapterInfoMapper chapterInfoMapper;

    @Autowired
    private CpAuthBookMapper cpAuthBookMapper;

    @Autowired
    private UserAuthorMapper userAuthorMapper;

    @Override
    public JSONObject queryBookList(Integer page, Integer rows) {
        JSONObject result = new JSONObject();
        JSONObject data= new JSONObject();
        ArrayList<Object> list = new ArrayList<>();
        JSONArray jsonArray = new JSONArray();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Integer cp_authId = 24;//云上阁是24
        if (cpAuthBookMapper.getBookIdListByCpAuthId(cp_authId).size()<1){
            result.put("errcode",-1);
            result.put("errmsg","授权出错!请联系提供者获取授权书籍");
            return result;
        }
        //List<Integer> bookIdListByCpAuthId = cpAuthBookMapper.getBookIdListByCpAuthId(cp_authId);
        Integer jumpOver=(page-1)*rows;//跳过的条数:(页码-1)*每页条数
        List<Integer> bookIdListByCpAuthIdAndPageAndRows = cpAuthBookMapper.getBookIdListByCpAuthIdAndPageAndRows(cp_authId,jumpOver,rows);
        for (Integer cpBookIdOne:bookIdListByCpAuthIdAndPageAndRows){
            BookInfo bookInfo = bookInfoMapper.selectByPrimaryKey(cpBookIdOne);
            JSONObject object = new JSONObject();
            object.put("id", bookInfo.getId());
            object.put("title",bookInfo.getBookName());
            //  TimeStamp timeStamp=new TimeStamp(new Date.getTime);
            //object.put("last_modified_at", new Date());
            //object.put("last_modified_at", bookInfo.getUpdateTime());
            object.put("last_modified_at",simpleDateFormat.format(bookInfo.getUpdateTime()));
            //object.put("last_modified_at","2020-09-22T13:30:40+08:00");

            list.add(object);
        }
        data.put("total",bookIdListByCpAuthIdAndPageAndRows.size());
        data.put("list",list);
        result.put("errcode", 0);
        result.put("errmsg", "");
        result.put("data", data);
        return result;
    }

    @Override
    public JSONObject queryBookById(int id) {
        JSONObject result = new JSONObject();
        JSONObject data = new JSONObject();
        JSONObject author= new JSONObject();
        SimpleDateFormat YmdSimple = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        LinkedHashMap<Object, Object> category_map = new LinkedHashMap<>();//分类ID名称映射


        BookInfo bookInfo = bookInfoMapper.selectByPrimaryKey(id);
        if (null == bookInfo) {
            result.put("errcode", -1);
            result.put("errmsg", "请检查书籍id是否正确!");
            return result;
        }
        if (null==cpAuthBookMapper.getOneByCpAuthIdAndBookId(24,id)){
            result.put("errcode", -2);
            result.put("errmsg", "暂时没有得到此书的授权!");
            return result;
        }
        data.put("id", id);//书籍ID
        data.put("title", bookInfo.getBookName());//书籍标题
        data.put("word_count", bookInfo.getWords());//字数

        //准备作者
        UserAuthor userAuthorBySelect = new UserAuthor();
        userAuthorBySelect.setNickname(bookInfo.getAuthor());
        author.put("id", 0);
        author.put("name",bookInfo.getAuthor());
        author.put("introduction","");
        UserAuthor userAuthor = userAuthorMapper.selectOne(userAuthorBySelect);
        if (null==userAuthor){
            author.put("gender",0);
            author.put("birth","");
        }else {
            author.put("gender",userAuthor.getSex());
            String cardId = userAuthor.getCardId();
            String birth=cardId.substring(6,10)+"-"+cardId.substring(10,12)+"-"+cardId.substring(12,14);
            author.put("birth",birth);
        }
        data.put("author",author);//作者信息准备完毕,放入data
        data.put("cover_img",bookInfo.getPicUrl());//封面图片URL
        data.put("introduction",bookInfo.getDescription());//简介
        String[] tag_list=bookInfo.getKeywords().split("，|、|；| ");
        data.put("tag_list",tag_list);//标签名称列表。CP提供

        String category = bookInfo.getCategory();//从数据库查询分类
        int[] category_list=get37Category_list(category);//{1,2,3,4,6,7,8,9,10,11}
        data.put("category_list",category_list);//int[] 分类ID列表。参考【3.1 频道分类映射】
        data.put("category_map",get37Category_map(category));//分类ID名称映射。如{"1"=>"玄幻奇幻", "2"=>"武侠仙侠"}。
        int channel37= get37channel(category);
        data.put("channel",channel37);//频道ID。参考【3.1 频道分类映射】
        data.put("channel_name",get37channel(channel37));//频道名称
        Integer completeState = bookInfo.getCompleteState();
        //将连载状态转意
        completeState= completeState==0 ? 2 :1;
        data.put("complete_status",completeState);
        String complete_status_name=completeState==2 ? "已完成" :"连载中";
        data.put("complete_status_name",complete_status_name);
        data.put("length_type",1);
        data.put("length_type_name","长篇");
        data.put("chapter_cnt",chapterInfoMapper.selectMaxSortByBookId(id));//章节总数

        data.put("last_modified_at",simpleDateFormat.format(bookInfo.getUpdateTime()));//书籍的最近更新时间
        data.put("default_pay_begin_chapter",20);//默认付费章节id
        result.put("errcode", 0);
        result.put("errmsg", "");
        result.put("data", data);
        return result;
    }

    @Override
    public JSONObject queryChapterList(int id) {
        JSONObject result = new JSONObject();
        LinkedList<Object> data = new LinkedList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        BookInfo bookInfo = bookInfoMapper.selectByPrimaryKey(id);
        if (null == bookInfo) {
            result.put("errcode", -1);
            result.put("errmsg", "请检查书籍id是否正确!");
            return result;
        }
        if (null==cpAuthBookMapper.getOneByCpAuthIdAndBookId(24,id)){
            result.put("errcode", -2);
            result.put("errmsg", "暂时没有得到此书的授权!");
            return result;
        }
        List<ChapterInfo> chapterInfos = chapterInfoMapper.selectChapterListByBookId(id);
        for (ChapterInfo chapterInfo:chapterInfos){
            JSONObject oneChapter= new JSONObject();
            oneChapter.put("section_id",1);
            oneChapter.put("chapter_id",chapterInfo.getChapterId());
            oneChapter.put("title",chapterInfo.getChapterName());
            oneChapter.put("word_count",chapterInfo.getWords());
            oneChapter.put("last_modified_at",simpleDateFormat.format(chapterInfo.getUpdateTime()));
            data.add(oneChapter);
        }
        result.put("errcode", 0);
        result.put("errmsg", "");
        result.put("data", data);
        return result;
    }

    @Override
    public JSONObject queryChapterContent(int id, int chapter_id) {
        JSONObject result = new JSONObject();
        JSONObject data= new JSONObject();

        BookInfo bookInfo = bookInfoMapper.selectByPrimaryKey(id);
        if (null == bookInfo) {
            result.put("errcode", -1);
            result.put("errmsg", "请检查书籍id是否正确!");
            return result;
        }
        if (null==cpAuthBookMapper.getOneByCpAuthIdAndBookId(24,id)){
            result.put("errcode", -2);
            result.put("errmsg", "暂时没有得到此书的授权!");
            return result;
        }
        ChapterInfo chapterInfo =chapterInfoMapper.selectOneByBookIdAndChapterId(id,chapter_id);
        if (null==chapterInfo){
            result.put("errcode", -3);
            result.put("errmsg", "没有相应的内容!请检查相关参数以获取内容");
            return result;
        }
        data.put("id",chapter_id);
        data.put("title",chapterInfo.getChapterName());
        //在去OSS中拿章节内容的信息
        final String URL = String.format("%s/%s/%s/%s.txt", ApiConstant.Config.BASE_PATH, "booktxt", id, chapter_id);
        String content = HttpClientUtil.doGet(URL);
        data.put("content",content);
        result.put("errcode", 0);
        result.put("errmsg", "");
        result.put("data", data);
        return result;
    }

    public static int[] get37Category_list(String category){
        System.out.println("接收到的类型是:"+category);
        int[] result=new int[1];
        switch (category){
            case "玄幻奇幻" :result[0]=1;return result;
            case "仙侠武侠" :case "玄幻仙侠" :result[0]=2;return result;
            case "都市生活" :result[0]=3;return result;
            case "历史军事" :result[0]=4;return result;
            case "科幻末世" :result[0]=6;return result;
            case "悬疑灵异" :result[0]=7;return result;
            case "现代言情" :result[0]=8;return result;
            case "古代言情" :result[0]=9;return result;
            case "幻想言情" :result[0]=10;return result;
            case "浪漫青春" :result[0]=11;return result;
        }
        return result;
    }

    public static HashMap<Object,Object> get37Category_map(String category_map){
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
        switch (category_map){
            case "玄幻奇幻" : objectObjectHashMap.put("1","玄幻奇幻");return objectObjectHashMap;
            case "仙侠武侠" : case "玄幻仙侠" :objectObjectHashMap.put("2","武侠仙侠");return objectObjectHashMap;
            case "都市生活" : objectObjectHashMap.put("3","都市人生");return objectObjectHashMap;
            case "历史军事" : objectObjectHashMap.put("4","历史军事");return objectObjectHashMap;
            case "科幻末世" : objectObjectHashMap.put("6","科幻空间");return objectObjectHashMap;
            case "悬疑灵异" : objectObjectHashMap.put("7","悬疑灵异");return objectObjectHashMap;
            case "现代言情" : objectObjectHashMap.put("8","现代言情");return objectObjectHashMap;
            case "古代言情" : objectObjectHashMap.put("9","古代言情");return objectObjectHashMap;
            case "幻想言情" : objectObjectHashMap.put("10","幻想言情");return objectObjectHashMap;
            case "浪漫青春" : objectObjectHashMap.put("11","青春校园");return objectObjectHashMap;
        }
        objectObjectHashMap.put("0","其他");
        return objectObjectHashMap;
    }

    public static int get37channel(String category){//接收分类,返回37的频道id
        switch (category){
            case "玄幻奇幻" : case "仙侠武侠" :case "玄幻仙侠" : case "都市生活" :
            case "历史军事" : case "科幻末世" : case "悬疑灵异" :return 1;

            case "现代言情" : case "古代言情" : case "幻想言情" : case "浪漫青春" :return 2;
        }
        return 1;
    }

    public static String get37channel(int category){//接收分类id,返回37的频道中文名
        switch (category){
            case 1 :return "男频";
            case 2 :return "女频";
        }
        return "男频";
    }
}
