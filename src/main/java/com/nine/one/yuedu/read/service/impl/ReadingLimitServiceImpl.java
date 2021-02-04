package com.nine.one.yuedu.read.service.impl;

import com.nine.one.yuedu.read.config.ApiConstant;
import com.nine.one.yuedu.read.entity.BookInfo;
import com.nine.one.yuedu.read.entity.ChapterInfo;
import com.nine.one.yuedu.read.mapper.BookInfoMapper;
import com.nine.one.yuedu.read.mapper.ChapterInfoMapper;
import com.nine.one.yuedu.read.mapper.CpAuthBookMapper;
import com.nine.one.yuedu.read.service.ReadingLimitService;
import com.nine.one.yuedu.read.utils.FileOptionUtil;
import com.nine.one.yuedu.read.utils.GX.HttpClientUtilsGX;
import com.nine.one.yuedu.read.utils.HttpClientUtil;
import com.nine.one.yuedu.read.utils.HttpClientUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author:李王柱
 * 2020/6/14 0014
 */
@Service(value = "readingLimitService")
public class ReadingLimitServiceImpl implements ReadingLimitService {

    @Autowired
    private BookInfoMapper bookInfoMapper;

    @Autowired
    private FileOptionUtil fileOptionUtil;

    @Autowired
    private ChapterInfoMapper chapterInfoMapper;

    @Autowired
    private CpAuthBookMapper cpAuthBookMapper;

    //阅文的id是13
    int RedingCpAuthId=13;

    //腾讯提供的cpid是2000000608
    String tencetCpId ="2000000608";

    //String tencetBookUrl= "http://open.book.qq.com/push/addBook";//推送书的url
    String tencetBookUrl= "http://open.book.qq.com/push/addCofreeBook";//阅文通知修改(此时已经接完飞读几个月了)

    String tencetPushUrl= "http://open.book.qq.com/push/addChapter";//推送章节的url

    //登陆取得的key
    String key=getHebdomadalKey();

    //解决乱码,指定
    ContentType contentType = ContentType.create("text/plain",Charset.forName("UTF-8"));

    //返回结果集
    StringBuilder CharpterMsg=new StringBuilder();

    //推送时出现网络卡顿的章节
StringBuilder timeOut=new StringBuilder();

    @Override
    public String insertBookById(Integer bookId) throws Exception {
        //1,判断阅文是否拥有此书的权限
        if (null==cpAuthBookMapper.getOneByCpAuthIdAndBookId(RedingCpAuthId,bookId)){
            return "授权失败,此书不在阅文的授权中";
        }

        CloseableHttpResponse response = null;

        BookInfo bookInfo = bookInfoMapper.selectByPrimaryKey(bookId);

        //2,客户端对象

        CloseableHttpClient client = HttpClientBuilder.create().build();

        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();//.setMode(HttpMultipartMode.RFC6532);

        entityBuilder.addTextBody("key",new String(key.getBytes(),"utf-8"));//String	通过登录接口获得的key值

        entityBuilder.addTextBody("b.cpid", new String(tencetCpId.getBytes(),"utf-8"));//long	腾讯分配的CP编号

        entityBuilder.addTextBody("b.cpBid", new String(String.valueOf(bookId).getBytes(),"utf-8"));//long	CP侧的图书id

        entityBuilder.addTextBody("b.title",new String(bookInfo.getBookName().getBytes(),"utf-8"),contentType);//String 图书名称

        entityBuilder.addTextBody("b.author",new String(bookInfo.getAuthor().getBytes(),"utf-8"),contentType);//String 作者

        Integer completeState = bookInfo.getCompleteState();
        completeState = completeState==0? 1:0;//将完结状态转意

        entityBuilder.addTextBody("b.finish",new String(completeState.toString().getBytes(),"utf-8"));//int	是否完结  1 已完结；0 未完结

            //entityBuilder.addTextBody("b.intro",new String(bookInfo.getDescription().getBytes(),"utf-8"));//String	图书简介
        entityBuilder.addTextBody("b.intro",new String(bookInfo.getDescription().getBytes(),"utf-8"),contentType);//String	图书简介
        //entityBuilder.addTextBody("b.author",bookInfo.getDescription(),ContentType.create("text/plain","UTF-8"));

        File fileByUrl = fileOptionUtil.getFileByUrl(bookInfo.getPicUrl());//File	图书的封面图片

        entityBuilder.addPart("b.cover",new FileBody(fileByUrl,fileByUrl.getName(),"image/jpeg","utf-8"));

        //requestDataMap.put("b.sex",new String("0".getBytes(),"utf-8"));
        entityBuilder.addTextBody("b.sex",new String(getSex(bookInfo.getCategory()).getBytes(),"utf-8")); //int	针对群体  0 通用；1 男版；2 女版(现在不允许通用了)

        //requestDataMap.put("b.form",new String("1".getBytes(),"utf-8"));
        entityBuilder.addTextBody("b.form",new String("0".getBytes(),"utf-8"));//int	形式属性  0原创  1出版

       // requestDataMap.put("b.language",new String("0".getBytes(),"utf-8"));
        entityBuilder.addTextBody("b.language",new String("0".getBytes(),"utf-8"));//int	语言属性  0中文  1英文  2其他

        //String	可选	出版时间（出版书b.form=1必填） yyyy-mm-dd格式
        entityBuilder.addTextBody("b.pushlishTime",new String(new SimpleDateFormat("yyyy-mm-dd").format(bookInfo.getCreateTime()).getBytes(),"utf-8"));

        HttpEntity entity = entityBuilder.build();

        HttpPost post = new HttpPost(tencetBookUrl);

       post.setEntity(entity);

        String jsonString="";
        try {
            response = client.execute(post);

            jsonString = getResponseResult(response);

            if (null==JSONObject.fromObject(jsonString)){
                return "<br>id为:"+bookId+"的书通信失败"+jsonString.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "<br>添加书籍"+bookId+"网络超时";
        }
        //System.out.println(jsonString);//打印结果,测完就注解

        JSONObject jsonObject =JSONObject.fromObject(jsonString);
        //判断通信标识
        String message = jsonObject.getString("message");

        if (!StringUtils.equals("Success",message)){
            System.out.println("id为"+bookId+"的书籍添加失败"+jsonString);
            return "<br>id为"+bookId+"的书籍添加失败"+jsonString;
        }

        JSONObject result = jsonObject.getJSONObject("result");
        String ResultBookid = result.getString("bookid");
        //System.out.println(bookId+"返回的bookId是:"+ResultBookid);//打印返回的bookid

        List<ChapterInfo> chapterInfos = chapterInfoMapper.selectChapterListByBookId(bookId);

        for (ChapterInfo cinfo : chapterInfos){
            if (cinfo.getChapterName().indexOf("91阅读xjzj")>=0){
                continue;
            }
            //客户端对象
           // HttpClient clientChapter = HttpClients.createDefault();
            CloseableHttpClient clientChapter = HttpClientBuilder.create().build();
            MultipartEntityBuilder entityBuilderChapter = MultipartEntityBuilder.create();//.setMode(HttpMultipartMode.RFC6532);;

            entityBuilderChapter.addTextBody("key",new String(key.getBytes(),"utf-8"));
            entityBuilderChapter.addTextBody("c.cpid",new String(tencetCpId.getBytes(),"utf-8"));//long	腾讯分配的CP编号
            entityBuilderChapter.addTextBody("c.bookid",new String(ResultBookid.getBytes(),"utf-8"));//long	增加图书后返回给CP的bookid值

            entityBuilderChapter.addTextBody("c.title",new String(toSpace(cinfo.getChapterName()).getBytes(),"utf-8"),contentType);//String	章节名称

            //在去OSS中拿章节内容的信息
            final String URL = String.format("%s/%s/%s/%s.txt", ApiConstant.Config.BASE_PATH, "booktxt", bookId, cinfo.getChapterId());
            String bookContent = HttpClientUtil.doGet(URL);
            for (int i=0;i<20;i++){
                if (null==bookContent){
                    bookContent = HttpClientUtil.doGet(URL);
                }else {
                    break;
                }
            }
            //System.out.println(bookContent.substring(0,111));//打印拿到的章节内容前111字
            entityBuilderChapter.addTextBody("c.content",new String(bookContent.getBytes(),"utf-8"),contentType );//String	增加的章节内容

            HttpEntity buildChapter = entityBuilderChapter.build();
            HttpPost postChapter = new HttpPost(tencetPushUrl);

            postChapter.setEntity(buildChapter);

            String respMsg="";
            try {
                response = client.execute(postChapter);

                respMsg = getResponseResult(response);

                if (null==JSONObject.fromObject(respMsg)){
                    CharpterMsg.append("书"+bookId+"的"+cinfo.getId()+"通信失败").append(respMsg).toString();
                    continue;
                }
            }catch (Exception e) {
                CharpterMsg.append("书id"+bookId+"的"+cinfo.getId()+"网络超时");
                e.printStackTrace();
                continue;
            }
            //System.out.println("内容来自章节:"+cinfo.getChapterName());//打印内容出处
            //System.out.println(respMsg);//打印结果,测完就注解

            JSONObject msgJson =JSONObject.fromObject(respMsg);
            //判断通信标识
            String msgCode = msgJson.getString("message");

            if (StringUtils.equals("Success!",msgCode)){
                System.out.print(bookId+"_"+cinfo.getChapterId()+":");
                //System.out.print(cinfo.getChapterName()+"上传成功");
            }else {
                CharpterMsg.append("书id"+bookId+"的"+cinfo.getId()+msgCode);
            }
            for (int i=2;i<=3;i++){
                if (StringUtils.equals("Success!",msgCode)){
                    //System.out.println(cinfo.getChapterName()+"上传成功");
                    break;
                    //return "通信失败";
                }else {
                    System.out.print("章节"+cinfo.getChapterName()+"  尝试第"+i+"次上传  :");
                    try {
                        response = client.execute(postChapter);

                        respMsg = getResponseResult(response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //System.out.println("章节:"+cinfo.getChapterName());
                    //System.out.println(respMsg);//打印结果,测完就注解
                    msgJson =JSONObject.fromObject(respMsg);
                    //判断通信标识
                    msgCode = jsonObject.getString("message");
                }
            }
            release(response,clientChapter);
        }
        if (CharpterMsg.length()<9){
            System.out.println(bookId);
            System.out.println("已上传成功");
                release(response,client);
            return "通信成功";
        }
        System.out.println(bookId+"已完毕");
        return CharpterMsg.toString();
    }

    @Override
    public String retryChapterByBookId(Integer bookId) throws IOException {
        //1,判断阅文是否拥有此书的权限
        if (null==cpAuthBookMapper.getOneByCpAuthIdAndBookId(RedingCpAuthId,bookId)){
            return "授权失败,此书不在阅文的授权中";
        }

        String ResultBookid="20000006080000000000"+bookId;

        CloseableHttpResponse response = null;

        List<ChapterInfo> RetryChapterInfos = chapterInfoMapper.selectChapterListByBookId(bookId);

        for (ChapterInfo cinfo : RetryChapterInfos){
            if (cinfo.getChapterName().indexOf("91阅读xjzj")>=0){
                continue;
            }
            //客户端对象
            CloseableHttpClient clientChapter = HttpClientBuilder.create().build();
            MultipartEntityBuilder entityBuilderChapter = MultipartEntityBuilder.create();//.setMode(HttpMultipartMode.RFC6532);;

            entityBuilderChapter.addTextBody("key",new String(key.getBytes(),"utf-8"));
            entityBuilderChapter.addTextBody("c.cpid",new String(tencetCpId.getBytes(),"utf-8"));//long	腾讯分配的CP编号
            entityBuilderChapter.addTextBody("c.bookid",new String(ResultBookid.getBytes(),"utf-8"));//long	增加图书后返回给CP的bookid值

            entityBuilderChapter.addTextBody("c.title",new String(toSpace(cinfo.getChapterName()).getBytes(),"utf-8"),contentType);//String	章节名称

            //在去OSS中拿章节内容的信息
            final String URL = String.format("%s/%s/%s/%s.txt", ApiConstant.Config.BASE_PATH, "booktxt", bookId, cinfo.getChapterId());
            String bookContent = HttpClientUtil.doGet(URL);
            for (int i=0;i<20;i++){
                if (null==bookContent){
                    bookContent = HttpClientUtil.doGet(URL);
                }else {
                    break;
                }
            }
            //System.out.println(bookContent.substring(0,111));//打印拿到的章节内容前111字
            entityBuilderChapter.addTextBody("c.content",new String(bookContent.getBytes(),"utf-8"),contentType );//String	增加的章节内容
            //entityBuilder.addTextBody("c.content",bookContent,ContentType.create("text/plain","UTF-8"));

            HttpEntity buildChapter = entityBuilderChapter.build();
            HttpPost postChapter = new HttpPost(tencetPushUrl);

            postChapter.setEntity(buildChapter);

            String respMsg="";
            try {
                response = clientChapter.execute(postChapter);

                respMsg = getResponseResult(response);

                if (null==JSONObject.fromObject(respMsg)){
                    CharpterMsg.append("书"+bookId+"的"+cinfo.getChapterId()+"通信失败").append(respMsg).toString();
                    continue;
                }
            }catch (Exception e) {
                CharpterMsg.append("书id"+bookId+"的"+cinfo.getChapterId()+"网络超时");
                System.out.print("书id"+bookId+"的"+cinfo.getChapterId()+"网络超时");
                e.printStackTrace();
                continue;
            }
            //System.out.println("内容来自章节:"+cinfo.getChapterName());//打印内容出处
            //System.out.println(respMsg);//打印结果,测完就注解

            JSONObject msgJson =JSONObject.fromObject(respMsg);
            //判断通信标识
            String msgCode = msgJson.getString("message");

            if (StringUtils.equals("Success!",msgCode)){
                System.out.print(bookId+"_"+cinfo.getChapterId()+":成功");
                //System.out.print(cinfo.getChapterName()+"上传成功");
            }else {
                CharpterMsg.append("书id"+bookId+"的"+cinfo.getChapterId()+msgCode);
                System.out.print("书id"+bookId+"的"+cinfo.getChapterId()+msgCode);
            }
            release(response,clientChapter);
        }
        if (CharpterMsg.length()<5){
            System.out.println(bookId);
            System.out.println("已上传成功");
            return "通信成功";
        }
        System.out.println(bookId+"已完毕");
        return CharpterMsg.toString();
    }


    private String getResponseResult(HttpResponse response)throws Exception{
        HttpEntity entity = response.getEntity();
        InputStream in=entity.getContent();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
               StringBuffer stringBuffer = new StringBuffer();
               String oneLine = "";
               try {
                    while ((oneLine = bufferedReader.readLine()) != null) {
                           stringBuffer.append(oneLine);
                       }
                   return stringBuffer.toString();
               } catch (IOException e) {
                   e.printStackTrace();
               }
                return null;
    }
    public String getHebdomadalKey(){
        //登录账号和密码为申请的开发平台账户和分配的cpid

        String cpid="2000000608";
        String username="jingxiangtiancheng";//用户名 = 平台登录用户名
        String cpassw="JXtiancheng2018";

        //密码= md5(md5(cpid)+md5(平台登录密码)+md5(username))
        //DigestUtils.md5DigestAsHex(DigestUtils.md5Digest(cpid)+DigestUtils.md5DigestAsHex(username));
        String password = "";
        try {
            password = myMD5(myMD5(cpid) + myMD5(cpassw) + myMD5(username));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //http://open.book.qq.com/push/login?username=ceshi&password=E10ADC394BABEF231CD212DF
        String url="http://open.book.qq.com/push/login?username="+username+"&password="+password;
        String jsonStr = HttpClientUtils.doGet(url);
        com.alibaba.fastjson.JSONObject resultJson = com.alibaba.fastjson.JSONObject.parseObject(jsonStr);
        String key= resultJson.getJSONObject("result").getString("key");
        System.out.println("得到有效期为7天的"+key);
        return key;
    }
    public static String myMD5(String data) throws Exception {
        java.security.MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] array = md.digest(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }

    public static String getSex(String type) {
        switch (type) {
            case "浪漫青春":
            case "古代言情":
            case "幻想言情":
            case "现代言情":
                return "2";

            case "都市生活":
            case "科幻末世":
            case "历史军事":
            case "仙侠武侠":
            case "玄幻奇幻":
            case "玄幻仙侠":
            case "悬疑灵异":
                return "1";
        }
        return "1";
    }

    /**
     * Description: 释放资源
     *
     * @param httpResponse
     * @param httpClient
     * @throws IOException
     */
    public static void release(CloseableHttpResponse httpResponse, CloseableHttpClient httpClient) throws IOException {
        // 释放资源
        if (httpResponse != null) {
            httpResponse.close();
        }
        if (httpClient != null) {
            httpClient.close();
        }
    }

    //阅文要求章节名那里要一致,'第xx章 内容'(统一在章节加上空格)
    public static String toSpace(String oldStr){
        StringBuilder stringBuilder=new StringBuilder(oldStr);
        int indexOfHave = stringBuilder.indexOf("章");
        if (indexOfHave>=0){
            if (stringBuilder.indexOf("章 ")<0){
                stringBuilder.insert(indexOfHave+1," ");
            }
        }
        return stringBuilder.toString();
    }
}
