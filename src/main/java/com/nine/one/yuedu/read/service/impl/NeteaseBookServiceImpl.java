package com.nine.one.yuedu.read.service.impl;

import com.nine.one.yuedu.read.config.ApiConstant;
import com.nine.one.yuedu.read.entity.BookInfo;
import com.nine.one.yuedu.read.entity.ChapterInfo;
import com.nine.one.yuedu.read.mapper.BookInfoMapper;
import com.nine.one.yuedu.read.mapper.ChapterInfoMapper;
import com.nine.one.yuedu.read.mapper.CpAuthBookMapper;
import com.nine.one.yuedu.read.service.NeteaseBookService;
import com.nine.one.yuedu.read.utils.GX.HttpClientUtilsGX;
import com.nine.one.yuedu.read.utils.HttpClientUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.*;

/**
 * Author:李王柱
 * 2020/6/12 0012
 */
@Service(value = "neteaseBookService")
public class NeteaseBookServiceImpl implements NeteaseBookService {

    int NeteaseCpauId = 12;

    //String consumerKey = "67698357";//测试环境
    String consumerKey = "95094607";

    //String consumerSecret="Jvdr1WbmagaBvklL";//测试环境
    String consumerSecret="3r5mPoLiZMJgFoJR";

    //String TestNeteaseAddBook = "http://testapi.yuedu.163.com/book/add.json";//测试环境
    String NeteaseAddBook = "http://api.yuedu.163.com/book/add.json";

    //String TestNeteasePushCharpter = "http://testapi.yuedu.163.com/bookSection/add.json";
    String NeteasePushCharpter = "http://api.yuedu.163.com/bookSection/add.json";

    @Autowired
    private BookInfoMapper bookInfoMapper;

    @Autowired
    private CpAuthBookMapper cpAuthBookMapper;

    @Autowired
    private ChapterInfoMapper chapterInfoMapper;

    @Override
    public String addBookForNetease(String bookId) throws Exception {

        if (null == cpAuthBookMapper.getOneByCpAuthIdAndBookId(NeteaseCpauId, Integer.parseInt(bookId))) {
            return "授权失败," + bookId + "这本书不在网易的授权列表中!请核实后再进行操作";
        }

        BookInfo bookInfo = bookInfoMapper.selectByPrimaryKey(Integer.parseInt(bookId));

        //创建一个请求参数map集合
        Map<String, Object> requestDataMap = new HashMap<String, Object>();

        requestDataMap.put("consumerKey", new String(consumerKey.getBytes(), "utf-8")); //string	合作方API的资源操作合法性标识符，由云阅读向合作方提供

        requestDataMap.put("timestamp", new String((System.currentTimeMillis() + "").getBytes(), "utf-8"));// int64	用户发起请求时的13位毫秒数。本次请求签名的默认有效时间为该时间戳+10分钟，可以使用expires参数进行调整，但是必须小于10分钟。

        requestDataMap.put("categoryId", new String(getCategoryStringToInt(bookInfo.getCategory()).getBytes(), "utf-8"));//int64	类别id，由云阅读提供

        requestDataMap.put("bookKey", new String(bookId.getBytes(), "utf-8"));//string 书籍用户方对该书的标识符。该参数旨在防止多次重复提交一本书籍,该值可以选择为己方书籍的唯一标识符id等。

        requestDataMap.put("payType",new String("1".getBytes(),"utf-8"));//int32	否	付费方式 0:免费 1:章节付费 2:全本付费，默认为0。同一本书，全本付费和章节付费方式之间不能切换。

        requestDataMap.put("price", new String("5".getBytes(), "utf-8"));//int32	书籍按章付费千字价格，以人民币分为单位，如 5分/千字。payType=1时有效

        requestDataMap.put("coverUrl",new String(bookInfo.getPicUrl().getBytes(), "utf-8"));//string	否	封面图片链接，必须以http开头。

        requestDataMap.put("author", new String(bookInfo.getAuthor().getBytes(), "UTF-8"));//string	作者
        //requestDataMap.put("author",new String("中文作者名".getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));

        Integer completeState = bookInfo.getCompleteState();
        completeState = completeState == 0 ? 1 : 0;//将完结状态转意
        requestDataMap.put("status", new String(completeState.toString().getBytes(), "utf-8"));//int32		书籍状态，0：连载，1：完结，2：试读非完整

        //1、请求的httpMethod和请求url拼接
        StringBuilder encodeStr = new StringBuilder();

        encodeStr.append("POST" + NeteaseAddBook);
//2、对非文本类型、数据流类型参数按照k=v的格式化,并按照字典顺序排序，如“k1=v1k2=v2k3=v3”
        List<String> params = new ArrayList<String>();
        for (Map.Entry<String, Object> entry : requestDataMap.entrySet()) {
            String key = entry.getKey();
            String value = (String) entry.getValue();
            //这里注意文本类型、数据流类型字段是不参与签名的
            String keyValue = key + "=" + value;
            params.add(keyValue);
        }
        //按照字典顺序,对格式化为"k=v"的参数列表进行排序
        Collections.sort(params);
        //3、继续拼接排序后的参数窜以及secretKey
        for (int i = 0; i < params.size(); i++) {
            encodeStr.append(params.get(i));
        }
        encodeStr.append(consumerSecret);
        //4、对待加密窜先进行urlencode,再进行md5

        //System.out.println("生成签名的原串 $basic_string:" + encodeStr);//打印原串

        //System.out.println("URLencode参与签名的字符串:" + URLEncoder.encode(encodeStr.toString(), "UTF-8"));//打印签名之前的字符串

        String sign = DigestUtils.md5DigestAsHex(URLEncoder.encode(encodeStr.toString(), "UTF-8").getBytes()).toLowerCase();


        requestDataMap.put("sign", new String(sign.getBytes(), "utf-8"));
        //System.out.println("生成的签名:" + new String(sign.getBytes(), "utf-8"));//打印签名
        requestDataMap.put("title", new String(bookInfo.getBookName().getBytes(), "utf-8"));//text	书籍标题/名称
        requestDataMap.put("description", new String(bookInfo.getDescription().getBytes(), "utf-8"));//text	否	书籍描述

        //String jsonString= HttpClientUtil.doPost(NeteaseAddBook,requestDataMap);

        String jsonString = HttpClientUtilsGX.doPost(NeteaseAddBook, requestDataMap);


        //System.out.println("请求返回的jsonString是:" + jsonString);//打印返回的结果

        try {
            if (null == JSONObject.fromObject(jsonString)) {
                return "添加"+bookId+"通信失败:" + jsonString;
            }
        } catch (net.sf.json.JSONException e) {
            e.printStackTrace();
            return "添加书籍"+bookId+"失败:" + jsonString;
        }



        JSONObject jsonObject = JSONObject.fromObject(jsonString);
        //判断通信标识
        String code = jsonObject.getString("code");
        if (!StringUtils.equals("200", code)) {
            return "添加错误的书籍id为" + bookId + "  错误详情:" + jsonString;
        }
        return "通信成功";
    }

    @Override
    public String addCharpterForNetease(String bookId) throws Exception {
        StringBuilder CharpterMsg = new StringBuilder();

        if (null==cpAuthBookMapper.getOneByCpAuthIdAndBookId(NeteaseCpauId,Integer.parseInt(bookId))){
            return "授权失败,"+bookId+"这本书不在网易的授权列表中!请核实后再进行操作";
        }

        List<ChapterInfo> chapterInfos = chapterInfoMapper.selectChapterListByBookId(Integer.parseInt(bookId));
        /*List<ChapterInfo> chapterInfos=new ArrayList<>();
        chapterInfos.add(chapterInfoMapper.selectOneByBookIdAndChapterId(Integer.parseInt(bookId),657));
        chapterInfos.add(chapterInfoMapper.selectOneByBookIdAndChapterId(Integer.parseInt(bookId),658));*/

        for (ChapterInfo chapterInfo : chapterInfos) {
//创建一个请求参数map集合
            Map<String, Object> requestDataMap = new HashMap<String, Object>();

            requestDataMap.put("consumerKey", new String(consumerKey.getBytes(), "utf-8")); //string	合作方API的资源操作合法性标识符，由云阅读向合作方提供

            requestDataMap.put("timestamp", new String((System.currentTimeMillis() + "").getBytes(), "utf-8"));//int64	用户发起请求时的13位毫秒数。本次请求签名的默认有效时间为该时间戳+10分钟

            requestDataMap.put("bookKey", new String(bookId.getBytes(), "utf-8"));//string 书籍用户方对该书的标识符。该参数旨在防止多次重复提交一本书籍,该值可以选择为己方书籍的唯一标识符id等。

//chapterId string	否	章节所在卷的id，如果只有一级目录的书籍，则卷id为空。对于二级目录chapterId和chapterKey必选其一。

            //chapterKey	string	否	用户方提供的章节所在卷的id，如果只有一级目录的书籍，则卷id为空。对于二级目录chapterId和chapterKey必选其一。

            requestDataMap.put("indexId", new String(chapterInfo.getChapterId().toString().getBytes(), "utf-8"));//Long	章节排序ID

            requestDataMap.put("sectionKey", new String(chapterInfo.getId().toString().getBytes(), "utf-8"));//string	用户方提供的主键id。

            int isFree=chapterInfo.getIsFree();
            isFree = isFree == 0 ? 1 : 0;//将是否收费转意
            requestDataMap.put("needPay", new String(Integer.valueOf(isFree).toString().getBytes(), "utf-8"));//	int32	该章节是否需要付费，0是不需要付费阅读，1是需要付费阅读。按本付费书籍同按章付费书籍一样，需要定义章节是否收费。

            requestDataMap.put("price", new String("5".getBytes(), "utf-8"));//int32	书籍按章付费千字价格，以人民币分为单位，如 5分/千字。payType=1时有效

            requestDataMap.put("wordcount", new String(chapterInfo.getWords().toString().getBytes(), "utf-8"));//int32	否	该章节字数，建议选择，如果没有则由云阅读按照相关规则计算字数


            //1、请求的httpMethod和请求url拼接
            StringBuilder encodeStr = new StringBuilder();

            encodeStr.append("POST" + NeteasePushCharpter);
//2、对非文本类型、数据流类型参数按照k=v的格式化,并按照字典顺序排序，如“k1=v1k2=v2k3=v3”
            List<String> params = new ArrayList<String>();
            for (Map.Entry<String, Object> entry : requestDataMap.entrySet()) {
                String key = entry.getKey();
                String value = (String) entry.getValue();
                //这里注意文本类型、数据流类型字段是不参与签名的
                String keyValue = key + "=" + value;
                params.add(keyValue);
            }
            //按照字典顺序,对格式化为"k=v"的参数列表进行排序
            Collections.sort(params);
            //3、继续拼接排序后的参数窜以及secretKey
            for (int i = 0; i < params.size(); i++) {
                encodeStr.append(params.get(i));
            }
            encodeStr.append(consumerSecret);
            //4、对待加密窜先进行urlencode,再进行md5

           /* System.out.println("生成签名的串 $basic_string:"+encodeStr);//打印生成签名之前的串
            try {
                System.out.println("URLencode参与签名的字符串:"+URLEncoder.encode(encodeStr.toString(),"UTF-8"));//打印生成签名之后的串
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }*/


            String sign = "";
            try {
                sign = DigestUtils.md5DigestAsHex(URLEncoder.encode(encodeStr.toString(), "UTF-8").getBytes()).toLowerCase();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            requestDataMap.put("sign", sign);
            //System.out.println("生成的签名:"+sign);//打印签名

            requestDataMap.put("title", new String(chapterInfo.getChapterName().getBytes(), "utf-8"));//text	章节标题


            //在去OSS中拿章节内容的信息
            final String URL = String.format("%s/%s/%s/%s.txt", ApiConstant.Config.BASE_PATH, "booktxt", bookId, chapterInfo.getChapterId());

            String bookContentOld = HttpClientUtil.doGet(URL);
            //将换行符转义
            String bookContent = bookContentOld.replace("\n", "</p><p>");
            for (int i = 0; i < 9; i++) {
                if (null == bookContent) {
                    bookContent = HttpClientUtil.doGet(URL);
                } else {
                    break;
                }
            }


            requestDataMap.put("content", new String(bookContent.getBytes(), "utf-8"));//text	章节内容，请不要把标题置于content第一行

            //String jsonString= HttpClientUtil.doPost(NeteasePushCharpter,requestDataMap);
            String jsonString = HttpClientUtilsGX.doPost(NeteasePushCharpter, requestDataMap);
            //System.out.println(jsonString);//打印返回的结果

            if (null==JSONObject.fromObject(jsonString)){
                return "通信失败!"+jsonString;
            }
            JSONObject jsonObject = JSONObject.fromObject(jsonString);
            //判断通信标识
            String code = jsonObject.getString("code");
            if (!StringUtils.equals("200", code)) {
                CharpterMsg.append("添加错误的书籍id为" + bookId + "出错章节为:" + chapterInfo.getId() + chapterInfo.getChapterName() + "  错误详情:" + jsonString);
                //return "添加错误的书籍id为"+bookId+"出错章节为:"+chapterInfo.getId()+chapterInfo.getChapterName()+"  错误详情:"+jsonString;
            }
        }
        if (CharpterMsg.length() < 9) {
            System.out.println(bookId+"推送成功");//打印成功推送的书id
            return "通信成功";
        }
        return CharpterMsg.toString();
    }


    public static String getCategoryStringToInt(String str) {
        switch (str) {
            case "玄幻奇幻":
            case "玄幻仙侠":
            case "幻想言情":
                return "1";
            case "科幻末世":
                return "2";
            case "历史军事":
                return "3";
            case "悬疑灵异":
                return "5";
            case "恐怖惊悚":
                return "6";
            case "都市生活":
                return "7";
            case "现代言情":
            case "豪门总裁":
                return "8";
            case "仙侠武侠":
                return "13";
            case "女生灵异":
                return "14";
            case "浪漫青春":
                return "22";
            case "古代言情":
                return "24";
        }
        return "12";
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
    ///*String encodeUtf8 = "";
    //        try {
    //            encodeUtf8 = URLEncoder.encode(encodeStr.toString(), "utf-8");
    //        } catch (UnsupportedEncodingException e) {
    //            e.printStackTrace();
    //        }
    //        String sign= (String) DigestUtils.md5DigestAsHex(encodeUtf8);*/
    /*public static void main(String[] args) {
        String $basic_string1="POSThttp://testapi.yuedu.163.com/book/add.jsonauthor=sgllbookKey=1categoryId=0consumerKey=67698357price=0status=1timestamp=1592364994678Jvdr1WbmagaBvklL";

        //12004,Sign invalid,encodestr:POSThttp://testapi.yuedu.163.com/book/add.jsonauthor=sgllbookKey=1categoryId=0consumerKey=67698357price=0status=1timestamp=1592364994678Jvdr1WbmagaBvklL
        String result="POSThttp://testapi.yuedu.163.com/book/add.jsonauthor=sgllbookKey=1categoryId=0consumerKey=67698357price=0status=1timestamp=1592364994678Jvdr1WbmagaBvklL";

       String  $basic_string="POSThttp://testapi.yuedu.163.com/book/add.jsonauthor=饿狼bookKey=1categoryId=0consumerKey=67698357price=0status=1timestamp=1592366915039Jvdr1WbmagaBvklL9e4dd647c6a0e0a4de36ca4aa1d91a15";//author=饿狼
       String result2="POSThttp://testapi.yuedu.163.com/book/add.jsonauthor=??bookKey=1categoryId=0consumerKey=67698357price=0status=1timestamp=1592366915039Jvdr1WbmagaBvklL";//author=??
        System.out.println($basic_string.equals(result));//true
        System.out.println(StringUtils.equals($basic_string,result));//true
    }*/
}
