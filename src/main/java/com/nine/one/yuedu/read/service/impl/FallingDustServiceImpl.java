package com.nine.one.yuedu.read.service.impl;

import com.github.wxpay.sdk.WXPayUtil;
import com.nine.one.yuedu.read.config.ApiConstant;
import com.nine.one.yuedu.read.config.FormateConstant;
import com.nine.one.yuedu.read.entity.BookInfo;
import com.nine.one.yuedu.read.entity.ChapterInfo;
import com.nine.one.yuedu.read.mapper.BookInfoMapper;
import com.nine.one.yuedu.read.mapper.ChapterInfoMapper;
import com.nine.one.yuedu.read.mapper.CpAuthBookMapper;
import com.nine.one.yuedu.read.service.FallingDustService;
import com.nine.one.yuedu.read.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static com.nine.one.yuedu.read.utils.GX.mapToXmlUtil.mapToXml;

/**
 * Author:李王柱
 * 2020/11/3
 */
@Service
public class FallingDustServiceImpl implements FallingDustService {
    @Autowired
    private BookInfoMapper bookInfoMapper;
    @Autowired
    private CpAuthBookMapper cpAuthBookMapper;
    @Autowired
    private ChapterInfoMapper chapterInfoMapper;
    Integer cpAuthId=27;
    @Override
    public String booklist(Integer page) throws Exception {
        page=(page-1) *100;
        List<Integer> bookIds= bookInfoMapper.selectBookIdOnPageOderByUpdateTime(cpAuthId,page);
        /*HashMap<String, String> result = new HashMap<>();
        LinkedHashMap<String, String> books = new LinkedHashMap<>();
        for (Integer bookId : bookIds){
            BookInfo bookInfo= bookInfoMapper.selectByPrimaryKey(bookId);
            HashMap<String, String> oneBook = new HashMap<>();
            oneBook.put("id",bookInfo.getId().toString());
            oneBook.put("booktitle",bookInfo.getBookName());
            oneBook.put("updatetime", FormateConstant.SIMPLE_DATE_FORMAT.format(bookInfo.getUpdateTime()));
            books.put("book",WXPayUtil.mapToXml(oneBook));
        }
        result.put("books",WXPayUtil.mapToXml(books));
        System.out.println(result);*/

        StringBuilder resultSB=new StringBuilder("<?xml version='1.0' encoding=\"UTF-8\"?><books>");
        for (Integer bookId : bookIds){
            LinkedHashMap<Object, Object> requestDataMap = new LinkedHashMap<>();
            BookInfo bookInfo= bookInfoMapper.selectByPrimaryKey(bookId);
            requestDataMap.put("id",bookId);
            requestDataMap.put("booktitle",bookInfo.getBookName());
            requestDataMap.put("updatetime", FormateConstant.SIMPLE_DATE_FORMAT.format(bookInfo.getUpdateTime()));
            resultSB.append("<book>").append(mapToXml(requestDataMap, true)).append("</book>");//, "<booklist>", "</booklist>"
        }
        String result=resultSB.append("</books>").toString();
        return result;
    }

    @Override
    public String book(Integer bid) throws Exception {
        LinkedHashMap<Object, Object> resultHashMap = new LinkedHashMap<>();
        StringBuilder resultSB=new StringBuilder("<?xml version='1.0' encoding=\"UTF-8\"?><book>");
        if (null==cpAuthBookMapper.getOneByCpAuthIdAndBookId(cpAuthId,bid)){
            resultHashMap.put("没有"+bid+"这本书的授权!","请检查id是否正确");
            return resultSB.append(mapToXml(resultHashMap,true)).toString()+"</book>";//,"<msg>","</msg>"
        }
        BookInfo bookInfo = bookInfoMapper.selectByPrimaryKey(bid);
        resultHashMap.put("id",bid);
        resultHashMap.put("channel",getSex(bookInfo.getCategory()));
        resultHashMap.put("category",bookInfo.getCategory());
        resultHashMap.put("title",bookInfo.getBookName());
        resultHashMap.put("author",bookInfo.getAuthor());
        resultHashMap.put("isVip",1);
        Integer completeState = bookInfo.getCompleteState();
        completeState = completeState==0? 1:0;//将完结状态转意
        resultHashMap.put("isFull",completeState);
        resultHashMap.put("cover",bookInfo.getPicUrl());
        resultHashMap.put("summary",bookInfo.getDescription());
        resultHashMap.put("url","http://authbook.91yuedu.com/chapters?bid="+bid);
        return resultSB.append(mapToXml(resultHashMap,true)).append("</book>").toString();
    }

    @Override
    public String chepters(Integer bid) {
        LinkedHashMap<Object, Object> resultHashMap = new LinkedHashMap<>();
        StringBuilder resultSB=new StringBuilder("<?xml version='1.0' encoding=\"UTF-8\"?><chapters>");
        if (null==cpAuthBookMapper.getOneByCpAuthIdAndBookId(cpAuthId,bid)){
            resultHashMap.put("没有"+bid+"这本书的授权!","请检查id是否正确");
            return resultSB.append(mapToXml(resultHashMap,true)).toString()+"</chapters>";//,"<msg>","</msg>"
        }
        List<ChapterInfo> chapterInfos = chapterInfoMapper.selectChapterListByBookId(bid);
        for (ChapterInfo chapterInfo:chapterInfos){
            resultSB.append("<chapter>");
            HashMap<Object, Object> oneChapter = new HashMap<>();
            oneChapter.put("id",chapterInfo.getId());
            oneChapter.put("title",chapterInfo.getChapterName());
            oneChapter.put("volume","正文");
            int isFree=chapterInfo.getIsFree();
            String isVip="";
            isVip = isFree == 0 ? "1" : "0";//将是否收费转意
            oneChapter.put("isVip",isVip);
            oneChapter.put("chapterLength",chapterInfo.getWords());
            oneChapter.put("chapterOrder",chapterInfo.getChapterId());
            oneChapter.put("updatetime",FormateConstant.SIMPLE_DATE_FORMAT.format(chapterInfo.getUpdateTime()));
            oneChapter.put("url","http://authbook.91yuedu.com/content?cid="+chapterInfo.getId());
            resultSB.append((mapToXml(oneChapter,true)));
            resultSB.append("</chapter>");
        }
        return resultSB.append("</chapters>").toString();
    }

    @Override
    public String content(Integer cid) {
        ChapterInfo chapterInfo = chapterInfoMapper.selectByPrimaryKey(cid);
        Integer bookid = chapterInfo.getBookId();
        Integer chapterid = chapterInfo.getChapterId();
        LinkedHashMap<Object, Object> resultHashMap = new LinkedHashMap<>();
        StringBuilder resultSB=new StringBuilder("<?xml version='1.0' encoding=\"UTF-8\"?>");
        if (null==cpAuthBookMapper.getOneByCpAuthIdAndBookId(cpAuthId,bookid)){

            resultHashMap.put("msg","没有"+bookid+"这本书的授权!请检查id是否正确");
            return resultSB.append(mapToXml(resultHashMap,true)).toString();//,"<msg>","</msg>"
        }
        if (null==chapterInfoMapper.selectOneByBookIdAndChapterId(bookid,chapterid)){
            resultHashMap.put("msg","没有"+chapterid+"这一章节!请检查id是否正确");
            return resultSB.append(mapToXml(resultHashMap,true)).toString();//,"<msg>","</msg>"
        }

        //在去OSS中拿章节内容的信息
        final String URL = String.format("%s/%s/%s/%s.txt", ApiConstant.Config.BASE_PATH, "booktxt", bookid, chapterid);
        String bookContent = HttpClientUtil.doGet(URL);
        for (int i=0;i<9;i++){
            if (null==bookContent){
                bookContent = HttpClientUtil.doGet(URL);
            }else {
                break;
            }
        }
        resultHashMap.put("content",bookContent);
        String contentinfoBody=mapToXml(resultHashMap,true);
        return resultSB.append(contentinfoBody).toString();
    }

    public static String getSex(String type) {
        switch (type) {
            case "浪漫青春":
            case "古代言情":
            case "幻想言情":
            case "现代言情":
                return "1";

            case "都市生活":
            case "科幻末世":
            case "历史军事":
            case "仙侠武侠":
            case "玄幻奇幻":
            case "玄幻仙侠":
            case "悬疑灵异":
                return "0";
        }
        return "1";
    }
}
