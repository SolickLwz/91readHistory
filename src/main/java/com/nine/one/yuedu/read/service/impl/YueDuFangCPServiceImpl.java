package com.nine.one.yuedu.read.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nine.one.yuedu.read.config.ApiConstant;
import com.nine.one.yuedu.read.config.JXResult;
import com.nine.one.yuedu.read.entity.BookInfo;
import com.nine.one.yuedu.read.entity.ChapterInfo;
import com.nine.one.yuedu.read.mapper.BookInfoMapper;
import com.nine.one.yuedu.read.mapper.ChapterInfoMapper;
import com.nine.one.yuedu.read.service.YueDuFangCPService;
import com.nine.one.yuedu.read.utils.AliyunOSSUtil;
import com.nine.one.yuedu.read.utils.GX.HttpClientUtilsGX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.security.MessageDigest;
import java.util.Date;
import java.util.List;

/**
 * Author:李王柱
 * 2020/8/6
 */
@Service(value = "yueDuFangCPService")
public class YueDuFangCPServiceImpl implements YueDuFangCPService {
    @Autowired
    private BookInfoMapper bookInfoMapper;

    @Autowired
    private ChapterInfoMapper chapterInfoMapper;

    @Autowired
    private AliyunOSSUtil aliyunOSSUtil;

    String sid="85";
    String key="h49s734bca43htoc62gd";

   /* public static void main(String[] args) throws Exception {
        String thisParam="page="+2+"&sid="+85;
        String signStr=thisParam+"&key="+"h49s734bca43htoc62gd";
        String sign = DigestUtils.md5DigestAsHex(signStr.getBytes());//lwzMD5(signStr);//
        System.out.println("http://www.yuedufang.com/apis/jieqi/articlelist.php?"+thisParam+"&sign="+sign);
        String jsonArrayString= HttpClientUtilsGX.doGet("http://www.yuedufang.com/apis/jieqi/articlelist.php?"+thisParam+"&sign="+sign);

        JSONArray jsonArray = JSONArray.parseArray(jsonArrayString);
        System.out.println(jsonArray);
        for (int i=1;i<jsonArray.size();i++){
            JSONObject jsonObjectTemp = jsonArray.getJSONObject(i);
            String articlename = jsonObjectTemp.getString("articlename");
            System.out.println(articlename);
        }
    }*/

    @Override
    public String getAriticleList(Integer page) throws Exception {
        String UrlArticlelist="http://www.yuedufang.com/apis/jieqi/articlelist.php?";//获取小说列表的URL
        String thisParam="page="+page+"&sid="+sid;
        String signStr=thisParam+"&key="+key;
        String sign = DigestUtils.md5DigestAsHex(signStr.getBytes());
        //System.out.println(UrlArticlelist+thisParam+"&sign="+sign);
        String jsonArrayString= HttpClientUtilsGX.doGet(UrlArticlelist+thisParam+"&sign="+sign);
        JSONArray jsonArray = JSONArray.parseArray(jsonArrayString);

        for (int i=0;i<jsonArray.size();i++){
            JSONObject jsonObjectTemp = jsonArray.getJSONObject(i);
            String articlename = jsonObjectTemp.getString("articlename");
            String articleid = jsonObjectTemp.getString("articleid");
            BookInfo bookInfo=new BookInfo();
            bookInfo.setCpId(1003);//悦读坊(万年历)的cp方ID是1003
            bookInfo.setCpBookId(articleid);//cp方的BookId,从第一个接口就获取
            bookInfo.setBookName(articlename);//书名,从第一个接口就获取
            bookInfo.setProvider("悦读坊");//提供者是悦读坊
            //获得当前数据库中最大的ID
            Integer maxId = bookInfoMapper.getCurrentMaxId();
            //这本书的ID,让原来的ID+1
            bookInfo.setId(maxId + 1);
            //字数在上传章节以后要变化,现在先设置为0
            bookInfo.setWords(0);
            //上架下架,刚上传完毕,默认下架状态
            bookInfo.setValid(0);
            //书的创建时间
            bookInfo.setCreateTime(new Date());
            //书籍的更新时间
            bookInfo.setUpdateTime(new Date());
            //阅读人数
            bookInfo.setVisitCount(0);
            //是否允许搜索,默认不允许
            bookInfo.setOpenSearch(0);
            //上传
            int i1 = bookInfoMapper.insertSelective(bookInfo);
            if (i1<0){
                return articlename+articleid+"数据库添加失败!";
            }
        }
        return jsonArray.toJSONString();
    }

    @Override
    public JXResult ArticleinfoUpdate(Integer id) throws Exception {
        if (0==id){
            Example example=new Example(BookInfo.class);
            example.createCriteria().andEqualTo("cpId",1003);
            List<BookInfo> bookInfos = bookInfoMapper.selectByExample(example);
            if (bookInfos.size()>1){
                for (BookInfo bookInfo : bookInfos){
                    JSONObject resultJson = Getarticleinfo(bookInfo.getCpBookId());
                   // System.out.println(resultJson);//打印
                    bookInfo.setAuthor(resultJson.getString("author"));
                    bookInfo.setDescription(resultJson.getString("intro"));
                    bookInfo.setKeywords(resultJson.getString("keywords"));
                    bookInfo.setCategory(getCategory(resultJson.getString("sort")));
                    String fullflag = resultJson.getString("fullflag");
                    Integer completeState = 0;
                    completeState = "0".equals(fullflag) ? 1 : 0;//将完结状态转意
                    bookInfo.setCompleteState(completeState);
                    if (resultJson.getString("bigcover").length()<1){
                        bookInfo.setPicUrl(resultJson.getString("cover"));
                    }else {
                        bookInfo.setPicUrl(resultJson.getString("bigcover"));
                    }
                    bookInfo.setUpdateTime(new Date());
                    int i = bookInfoMapper.updateByPrimaryKeySelective(bookInfo);
                    if (i<0){
                        return new JXResult(false, ApiConstant.StatusCode.ERROR, "更新cpBookId为:"+bookInfo.getCpBookId()+"失败");
                    }
                }
            }
        }else {
            Example example=new Example(BookInfo.class);
            example.createCriteria().andEqualTo("cpId",1003).andEqualTo("cpBookId",id);
            BookInfo bookInfo = bookInfoMapper.selectOneByExample(example);
            JSONObject resultJson = Getarticleinfo(bookInfo.getCpBookId());
            bookInfo.setAuthor(resultJson.getString("author"));
            bookInfo.setDescription(resultJson.getString("intro"));
            bookInfo.setKeywords(resultJson.getString("keywords"));
            String fullflag = resultJson.getString("fullflag");
            Integer completeState = 0;
            completeState = "0".equals(fullflag) ? 1 : 0;//将完结状态转意
            bookInfo.setCompleteState(completeState);
            if (resultJson.getString("bigcover").length()<1){
                bookInfo.setPicUrl(resultJson.getString("cover"));
            }else {
                bookInfo.setPicUrl(resultJson.getString("bigcover"));
            }
            bookInfo.setUpdateTime(new Date());
            int i = bookInfoMapper.updateByPrimaryKeySelective(bookInfo);
            if (i<0){
                return new JXResult(false, ApiConstant.StatusCode.ERROR, "更新"+id+"失败");
            }
        }
        return new JXResult(true, ApiConstant.StatusCode.OK, "更新"+id+"成功");
    }

    @Override
    public JXResult ArticlechapterUpdate(Integer aid) throws Exception {
        if (0==aid){
            Example example=new Example(BookInfo.class);
            example.createCriteria().andEqualTo("cpId",1003);
            List<BookInfo> bookInfos = bookInfoMapper.selectByExample(example);
            if (bookInfos.size()>0){
                for (BookInfo bookInfo : bookInfos){
                    //根据书id拿到悦读坊的章节目录表
                    JSONArray jsonArray = GetArticlechapter(bookInfo.getCpBookId());
                    int chapterId=1;
                    for (int i=0;i<jsonArray.size();i++){
                        //将章节目录入库
                        ChapterInfo chapterInfo=new ChapterInfo();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        chapterInfo.setBookId(bookInfo.getId());
                        chapterInfo.setChapterId(chapterId);
                        chapterId++;//这里指的是章节排序
                        chapterInfo.setChapterName(jsonObject.getString("chaptername"));
                        chapterInfo.setCpChapterId(jsonObject.getString("chapterid"));
                        String isvip = jsonObject.getString("isvip");
                        byte isFree=0;
                        isFree = (byte) ("0".equals(isvip) ? 1 : 0);//转意
                        chapterInfo.setIsFree(isFree);//0收费 1免费
                        //入库操作
                        chapterInfoMapper.insertSelective(chapterInfo);
                    }
                }
            }

        }else {
            Example example=new Example(BookInfo.class);
            example.createCriteria().andEqualTo("cpId",1003).andEqualTo("cpBookId",aid);
            BookInfo bookInfo = bookInfoMapper.selectOneByExample(example);

                    //根据书id拿到悦读坊的章节目录表
                    JSONArray jsonArray = GetArticlechapter(bookInfo.getCpBookId());
                    int chapterId=1;
                    for (int i=0;i<jsonArray.size();i++){
                        //将章节目录入库
                        ChapterInfo chapterInfo=new ChapterInfo();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        chapterInfo.setBookId(bookInfo.getId());
                        chapterInfo.setChapterId(chapterId);
                        chapterId++;//这里指的是章节排序
                        chapterInfo.setChapterName(jsonObject.getString("chaptername"));
                        chapterInfo.setCpChapterId(jsonObject.getString("chapterid"));
                        String isvip = jsonObject.getString("isvip");
                        byte isFree=0;
                        isFree = (byte) ("0".equals(isvip) ? 1 : 0);//转意
                        chapterInfo.setIsFree(isFree);//0收费 1免费
                        //入库操作
                        chapterInfoMapper.insertSelective(chapterInfo);
                    }
        }
        return new JXResult(true, ApiConstant.StatusCode.OK, "更新"+aid+"成功");
    }

    @Override
    public JXResult GetChaptercontent(Integer aid) throws Exception {
        if (0==aid){
            //获取所有书籍
            Example example=new Example(BookInfo.class);
            example.createCriteria().andEqualTo("cpId",1003);
            List<BookInfo> bookInfos = bookInfoMapper.selectByExample(example);

            if (bookInfos.size()>0){
                for (BookInfo bookInfo : bookInfos){
                    //获取一本书籍,取得这本书籍下的所有章节
                    Example chapterExample=new Example(ChapterInfo.class);
                    chapterExample.createCriteria().andEqualTo("bookId",bookInfo.getId());
                    List<ChapterInfo> chapterInfos = chapterInfoMapper.selectByExample(chapterExample);
                    //将这本书下的章节内容更新
                    for (ChapterInfo chapterInfo : chapterInfos){
                        JSONObject resultJson = GetChaptercontentString(bookInfo.getCpBookId(), chapterInfo.getCpChapterId());
                        //先进行文本的上传
                        String content= resultJson.getString("content");
                        aliyunOSSUtil.stringToTxtAndUploadOSS(chapterInfo.getBookId(), chapterInfo.getChapterId(),content);
                        chapterInfo.setCreateTime(new Date());
                        chapterInfo.setUpdateTime(new Date());
                        chapterInfo.setWords(content.length());
                        //入库更新操作
                        chapterInfoMapper.updateByPrimaryKeySelective(chapterInfo);
                        //入库完毕,更新这本书的字数和最后一章
                        //要先查询出这本书的字数
                        bookInfo.setUpdateTime(new Date());
                        //字数增加
                        bookInfo.setWords(bookInfo.getWords() + content.length());

                        //修改book_info表
                        bookInfoMapper.updateByPrimaryKeySelective(bookInfo);
                    }

                }
            }

        }else {
             //获取指定书籍
            Example example=new Example(BookInfo.class);
            example.createCriteria().andEqualTo("cpBookId",aid).andEqualTo("cpId",1003);
            BookInfo bookInfo = bookInfoMapper.selectOneByExample(example);
                    //获取一本书籍,取得这本书籍下的所有章节
                    Example chapterExample=new Example(ChapterInfo.class);

                    chapterExample.createCriteria().andEqualTo("bookId",bookInfo.getId());

                    List<ChapterInfo> chapterInfos = chapterInfoMapper.selectByExample(chapterExample);
                    //将这本书下的章节内容更新
                    for (ChapterInfo chapterInfo : chapterInfos){
                        JSONObject resultJson = GetChaptercontentString(bookInfo.getCpBookId(), chapterInfo.getCpChapterId());
                        //从OSS删除章节的内容
                        String objectName = String.format("booktxt/%s/%s.txt", chapterInfo.getBookId(), chapterInfo.getChapterId());
                        aliyunOSSUtil.delete(objectName);
                        System.out.print(chapterInfo.getChapterName()+"内容进行删除  ");
                        //先进行文本的上传
                        String content= resultJson.getString("content");
                        aliyunOSSUtil.stringToTxtAndUploadOSS(chapterInfo.getBookId(), chapterInfo.getChapterId(),content);
                        System.out.println(chapterInfo.getBookId()+chapterInfo.getChapterName()+"上传成功");
                        chapterInfo.setCreateTime(new Date());
                        chapterInfo.setUpdateTime(new Date());
                        chapterInfo.setWords(content.length());
                        //入库更新操作
                        chapterInfoMapper.updateByPrimaryKeySelective(chapterInfo);
                        //入库完毕,更新这本书的字数和最后一章
                        //要先查询出这本书的字数
                        bookInfo.setUpdateTime(new Date());
                        //字数增加
                        bookInfo.setWords(bookInfo.getWords() + content.length());

                        //修改book_info表
                        bookInfoMapper.updateByPrimaryKeySelective(bookInfo);
                    }

        }
        return new JXResult(true, ApiConstant.StatusCode.OK, "更新"+aid+"成功");
    }

    public static void main(String[] args) throws Exception {
        //System.out.println(GetChaptercontentString("29681","114169"));
        //System.out.println(Getarticleinfo("70"));
    }

    public static JSONObject GetChaptercontentString(String cpBookId,String cpChapterId) {
        String urlHead="http://www.yuedufang.com/apis/jieqi/chaptercontent.php?";
        String sid="85";
        String key="h49s734bca43htoc62gd";
        String aid=cpBookId;
        String thisParam="aid="+aid+"&cid="+cpChapterId+"&sid="+sid;
        String signStr=thisParam+"&key="+key;
        String sign = DigestUtils.md5DigestAsHex(signStr.getBytes());
        String result= null;
        try {
            result = HttpClientUtilsGX.doGet(urlHead+thisParam+"&sign="+sign);
        } catch (Exception e) {
            System.out.println(cpBookId+"的"+cpChapterId+"网络卡慢,尝试第二次获取");
            try {
                result = HttpClientUtilsGX.doGet(urlHead+thisParam+"&sign="+sign);
            } catch (Exception e1) {
                System.out.println(cpBookId+"的"+cpChapterId+"网络卡慢,尝试第三次获取");
                try {
                    result = HttpClientUtilsGX.doGet(urlHead+thisParam+"&sign="+sign);
                } catch (Exception e2) {
                    System.out.println(cpBookId+"的"+cpChapterId+"网络特别卡!以下是失败信息:");
                    e2.printStackTrace();
                }
            }
        }
        //System.out.println(result);//打印
        JSONObject jsonObject = JSONObject.parseObject(result);

        return jsonObject;
    }

    public static JSONArray GetArticlechapter(String cpBookId) throws Exception {
        String urlHead="http://www.yuedufang.com/apis/jieqi/articlechapter.php?";
        String sid="85";
        String key="h49s734bca43htoc62gd";
        String aid=cpBookId;
        String thisParam="aid="+aid+"&sid="+sid;
        String signStr=thisParam+"&key="+key;
        String sign = DigestUtils.md5DigestAsHex(signStr.getBytes());
        String result= HttpClientUtilsGX.doGet(urlHead+thisParam+"&sign="+sign);
       // System.out.println(result);//打印
        JSONArray jsonArray = JSONArray.parseArray(result);

        return jsonArray;
    }

    public static JSONObject Getarticleinfo(String cpBookId) throws Exception {
        String UrlHead="http://www.yuedufang.com/apis/jieqi/articleinfo.php?";
        String sid="85";
        String key="h49s734bca43htoc62gd";
        String aid=cpBookId;
        String thisParam="aid="+aid+"&sid="+sid;
        String signStr=thisParam+"&key="+key;
        String sign = DigestUtils.md5DigestAsHex(signStr.getBytes());
        //System.out.println(UrlArticlelist+thisParam+"&sign="+sign);
        String result= HttpClientUtilsGX.doGet(UrlHead+thisParam+"&sign="+sign);
        JSONObject jsonObject = JSONObject.parseObject(result);

        return jsonObject;
    }

    public static String lwzMD5(String data) throws Exception {
        java.security.MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] array = md.digest(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        System.out.println(sb.toString());
        return sb.toString().toLowerCase();
    }

    public static String convert ( String u8hex )
    {
        String[] array = u8hex.replaceAll ("(.{6})", "$1,").replaceAll ("\\,$", "").split ("\\,");
        String result = "";
        for ( int i = 0; i < array.length; i++ )
        {
            String u8bin = Integer.toBinaryString (Integer.parseInt (array[i], 16));
            String bin = u8bin.substring (4, 8) + u8bin.substring (10, 14) + u8bin.substring (14, 16) + u8bin.substring (18, 20) + u8bin.substring (20, 24);
            String code = Integer.toHexString (Integer.parseInt (bin, 2));
            String nativeValue = "";
            try
            {
                nativeValue += (char) Integer.parseInt (code.substring (0, 4), 16);
                if (code.length () > 4)
                {
                    nativeValue += code.substring (4, code.length ());
                }
            }
            catch (NumberFormatException e)
            {
                result += code;
            }
            result += nativeValue;
        }
        return result;
    }
    public static String getCategory(String sort){
switch (sort){
    case "现代都市" :
        return "都市生活";
    case "玄幻仙侠" :
        return "仙侠武侠";
    case "职场商战" :
        return "都市生活";
    case "悬疑推理" :
        return "悬疑灵异";
    case "游戏竞技" :
        return "玄幻奇幻";
    case "豪门总裁" :
        return "现代言情";
    case "古代言情" :
        return "古代言情";
    case "穿越重生" :
        return "幻想言情";
    case "幻想言情" :
        return "幻想言情";
    case "恐怖灵异" :
        return "悬疑灵异";
    case "青春校园" :
        return "浪漫青春";
    case "青春文学" :
        return "浪漫青春";
    case "经典名著" :
        return "历史军事";
    case "励志成功" :
        return "都市生活";
    case "侦探推理" :
        return "悬疑灵异";
    case "人物传记" :
        return "都市生活";
    case "外国名著" :
        return "都市生活";
}
        return "都市生活";
    }
}
