package com.nine.one.yuedu.read.service.impl;

import com.nine.one.yuedu.read.config.ApiConstant;
import com.nine.one.yuedu.read.entity.BookInfo;
import com.nine.one.yuedu.read.entity.ChapterInfo;
import com.nine.one.yuedu.read.mapper.BookInfoMapper;
import com.nine.one.yuedu.read.mapper.ChapterInfoMapper;
import com.nine.one.yuedu.read.mapper.CpAuthBookMapper;
import com.nine.one.yuedu.read.service.AliBookFlagService;
import com.nine.one.yuedu.read.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Author:李王柱
 * 2020/6/23
 */
@Service
public class AliBookFlagServiceImpl implements AliBookFlagService {
    //书旗的CpId是16
    Integer aliBookFlagCpId=16;

    @Autowired
    private CpAuthBookMapper cpAuthBookMapper;

    @Autowired
    private BookInfoMapper bookInfoMapper;

    @Autowired
    private ChapterInfoMapper chapterInfoMapper;
    @Override
    public String getBookList() {
        List<Integer> bookIdList = cpAuthBookMapper.getBookIdListByCpAuthId(aliBookFlagCpId);
        LinkedHashMap<Object, Object> requestDataMap = new LinkedHashMap<>();
        StringBuilder resultSB=new StringBuilder("<?xml version='1.0' encoding=\"UTF-8\"?><booklist>");
        for (Integer bookId : bookIdList){
            requestDataMap.put("bookid",bookId);
            resultSB.append(mapToXml(requestDataMap, true));//, "<booklist>", "</booklist>"
        }
        //String result = mapToXml(requestDataMap, true, "<booklist>", "</booklist>");
        String result=resultSB.append("</booklist>").toString();
        //System.out.println(result);//打印
        return result;
    }

    @Override
    public String getBookDetailsById(Integer bookid) {
        LinkedHashMap<Object, Object> resultHashMap = new LinkedHashMap<>();
        StringBuilder resultSB=new StringBuilder("<?xml version='1.0' encoding=\"UTF-8\"?><bookinfo>");
        if (null==cpAuthBookMapper.getOneByCpAuthIdAndBookId(aliBookFlagCpId,bookid)){
            resultHashMap.put("没有"+bookid+"这本书的授权!","请检查id是否正确");
            return resultSB.append(mapToXml(resultHashMap,true)).toString()+"</bookinfo>";//,"<msg>","</msg>"
        }
        BookInfo bookInfo = bookInfoMapper.selectByPrimaryKey(bookid);
        resultHashMap.put("bookid",bookid);
        resultHashMap.put("bookname",bookInfo.getBookName());
        resultHashMap.put("authorname",bookInfo.getAuthor());
        resultHashMap.put("intro",bookInfo.getDescription());
        Integer completeState = bookInfo.getCompleteState();
        //将连载状态转意
        completeState= completeState>0 ? 0 :1;
        resultHashMap.put("bookstatus",completeState);
        resultHashMap.put("keywords",bookInfo.getKeywords().replace("、", ","));
        resultHashMap.put("coverpath",bookInfo.getPicUrl());
        return resultSB.append(mapToXml(resultHashMap,true)).append("</bookinfo>").toString();
    }

    @Override
    public String getChapterListByBookId(Integer bookid) {
        LinkedHashMap<Object, Object> resultHashMap = new LinkedHashMap<>();
        StringBuilder resultSB=new StringBuilder("<?xml version='1.0' encoding=\"UTF-8\"?><chapterinfo>");
        if (null==cpAuthBookMapper.getOneByCpAuthIdAndBookId(aliBookFlagCpId,bookid)){
            resultHashMap.put("没有"+bookid+"这本书的授权!","请检查id是否正确");
            return resultSB.append(mapToXml(resultHashMap,true)).toString()+"</chapterinfo>";//,"<msg>","</msg>"
        }
        List<ChapterInfo> chapterInfos = chapterInfoMapper.selectChapterListByBookId(bookid);
        HashMap<Object, Object> Headbookid = new HashMap<>();
        Headbookid.put("bookid",bookid);
        resultSB.append(mapToXmlHasRoot(Headbookid,true,"<bookid>","</bookid>"));
        LinkedHashMap<Object, Object> vomueMap = new LinkedHashMap<>();
        vomueMap.put("volumeid",0);
        vomueMap.put("volname","正文");
        String vomueHead = mapToXml(vomueMap, true);
        StringBuilder chaptersBody=new StringBuilder();
        LinkedHashMap<Object, Object> chapterMap = new LinkedHashMap<>();
        for (ChapterInfo chapterInfo : chapterInfos) {
            chapterMap.put("chapterid", chapterInfo.getChapterId());
            chapterMap.put("chaptername", chapterInfo.getChapterName());
            chaptersBody.append(appendRoot(mapToXml(chapterMap, true), "<chapter>", "</chapter>"));
        }
        String chapters=appendRoot(chaptersBody.toString(),"<chapters>","</chapters>");
        String vomueBody=vomueHead+chapters;
        String volume = appendRoot(vomueBody, "<volume>", "</volume>");
        HashMap<Object, Object> volumesMap = new HashMap<>();
        volumesMap.put("volumes",volume);
        String volumesString = mapToXml(volumesMap, false);
        resultSB.append(volumesString);
        return resultSB.append(mapToXml(resultHashMap,true)).append("</chapterinfo>").toString();
    }

    @Override
    public String getchapterContentByBookIdAndChapterId(Integer bookid, Integer chapterid) {
        LinkedHashMap<Object, Object> resultHashMap = new LinkedHashMap<>();
        StringBuilder resultSB=new StringBuilder("<?xml version='1.0' encoding=\"UTF-8\"?>");
        if (null==cpAuthBookMapper.getOneByCpAuthIdAndBookId(aliBookFlagCpId,bookid)){

            resultHashMap.put("msg","没有"+bookid+"这本书的授权!请检查id是否正确");
            return resultSB.append(mapToXml(resultHashMap,true)).toString();//,"<msg>","</msg>"
        }
        if (null==chapterInfoMapper.selectOneByBookIdAndChapterId(bookid,chapterid)){
            resultHashMap.put("msg","没有"+chapterid+"这一章节!请检查id是否正确");
            return resultSB.append(mapToXml(resultHashMap,true)).toString();//,"<msg>","</msg>"
        }
        LinkedHashMap<Object, Object> contendinfoMap = new LinkedHashMap<>();
        contendinfoMap.put("bookid",bookid);
        contendinfoMap.put("chapterid",chapterid);
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
        contendinfoMap.put("content",bookContent);
        String contentinfoBody=mapToXml(contendinfoMap,false);
        resultHashMap.put("contentinfo",contentinfoBody);
        String contentinfoString = mapToXml(resultHashMap,false);
        resultSB.append(contentinfoString);
        return resultSB.toString();
    }

    public static void main(String[] args) {
        //HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
        LinkedHashMap<Object, Object> objectObjectHashMap = new LinkedHashMap<>();
        objectObjectHashMap.put("测试1","结果1");
        objectObjectHashMap.put("测试1","结果2");
        objectObjectHashMap.put("测试2","结果3");
        objectObjectHashMap.put("测试2","结果4");
        objectObjectHashMap.put("测试5","结果5");

       // String s = mapToXml(objectObjectHashMap, true,"<booklist>","</booklist>");
        String s = mapToXml(objectObjectHashMap, true);
        String m=mapToXml(objectObjectHashMap,false);
        String root=mapToXmlHasRoot(objectObjectHashMap,true,"<xml>","/<xml>");
        String rootf=mapToXmlHasRoot(objectObjectHashMap,false,"<xml>","/<xml>");
        System.out.println(s);
        System.out.println(m);
        System.out.println(root);
        System.out.println(rootf);
    }

    private static final String PREFIX_XML = "<xml>";
    private static final String SUFFIX_XML = "</xml>";

    private static final String PREFIX_CDATA = "<![CDATA[";

    private static final String SUFFIX_CDATA = "]]>";

    /**
     * 转化成xml, 单层无嵌套
     * @param parm
     * @param isAddCDATA
     * @return
     */
    public static String mapToXml(Map<Object, Object> parm, boolean isAddCDATA) {//,String node ,String opNode
        //StringBuffer strbuff = new StringBuffer(PREFIX_XML);
        //StringBuilder strbuff = new StringBuilder("<?xml version='1.0' encoding=\"UTF-8\"?>"+node);
        StringBuilder strbuff = new StringBuilder();
        if (null != parm) {
            for (Map.Entry<Object, Object> entry : parm.entrySet()) {
                strbuff.append("<").append(entry.getKey()).append(">");
                if (isAddCDATA) {
                    strbuff.append(PREFIX_CDATA);
                    if (null != entry.getValue()) {
                        strbuff.append(entry.getValue());
                    }
                    strbuff.append(SUFFIX_CDATA);
                } else {
                    if (null != entry.getValue()) {
                        strbuff.append(entry.getValue());
                    }
                }
                strbuff.append("</").append(entry.getKey()).append(">");
            }
        }
        //return strbuff.append(SUFFIX_XML).toString();
        //strbuff.append(opNode).toString();
        return strbuff.toString();
    }



    public static String mapToXmlHasRoot(Map<Object, Object> parm, boolean isAddCDATA,String node ,String opNode) {
        //StringBuffer strbuff = new StringBuffer(PREFIX_XML);
        StringBuilder strbuff = new StringBuilder(node);
        if (null != parm) {
            for (Map.Entry<Object, Object> entry : parm.entrySet()) {
                strbuff.append("<").append(entry.getKey()).append(">");
                if (isAddCDATA) {
                    strbuff.append(PREFIX_CDATA);
                    if (null != entry.getValue()) {
                        strbuff.append(entry.getValue());
                    }
                    strbuff.append(SUFFIX_CDATA);
                } else {
                    if (null != entry.getValue()) {
                        strbuff.append(entry.getValue());
                    }
                }
                strbuff.append("</").append(entry.getKey()).append(">");
            }
        }
        //return strbuff.append(SUFFIX_XML).toString();

        return strbuff.append(opNode).toString();
    }

    public static String appendRoot(String parm,String node ,String opNode) {
        StringBuilder strbuff = new StringBuilder(node);
        strbuff.append(parm);
        return strbuff.append(opNode).toString();
    }
}
