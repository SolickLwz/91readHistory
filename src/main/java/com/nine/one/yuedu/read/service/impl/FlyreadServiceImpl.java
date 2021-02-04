package com.nine.one.yuedu.read.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.nine.one.yuedu.read.config.ApiConstant;
import com.nine.one.yuedu.read.entity.BookInfo;
import com.nine.one.yuedu.read.entity.ChapterInfo;
import com.nine.one.yuedu.read.entity.Temp;
import com.nine.one.yuedu.read.mapper.BookInfoMapper;
import com.nine.one.yuedu.read.mapper.ChapterInfoMapper;
import com.nine.one.yuedu.read.mapper.CpAuthBookMapper;
import com.nine.one.yuedu.read.mapper.TempMapper;
import com.nine.one.yuedu.read.service.FlyreadService;
import com.nine.one.yuedu.read.utils.FileOptionUtil;
import com.nine.one.yuedu.read.utils.HttpClientUtil;
import com.nine.one.yuedu.read.utils.HttpClientUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.io.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.List;

import static com.nine.one.yuedu.read.utils.GX.myMD5Util.myMD5;

/**
 * Author:李王柱
 * 2020/10/20
 */
@Service
public class FlyreadServiceImpl implements FlyreadService {
    //解决乱码,指定
    ContentType contentType = ContentType.create("text/plain", Charset.forName("UTF-8"));

    int FlyRedCpAuthId=26;//飞读的id是26

    String tencetCpId ="2000002177";//腾讯提供的cpid是2000000608 飞读String cpid="2000002177";
    String username="jingxiangdushu";//用户名 = 平台登录用户名
    String cpassw="JXdushu2020";
    String password = myMD5(myMD5(tencetCpId) + myMD5(cpassw) + myMD5(username));


    String addCofreeBookUrl= "http://open.book.qq.com/push/addCofreeBook";//推送书的url

    String addChapterUrl= "http://open.book.qq.com/push/addChapter";//推送章节的url

    String getUpdateUrl="http://open.book.qq.com/push/getUpdateInfo";//获取更新信息的url

    String updateChapterUrl="http://open.book.qq.com/push/updateChapter";//实际上是覆盖章节

    @Autowired
    CpAuthBookMapper cpAuthBookMapper;
    @Autowired
    BookInfoMapper bookInfoMapper;
    @Autowired
    private FileOptionUtil fileOptionUtil;
    @Autowired
    private ChapterInfoMapper chapterInfoMapper;
    @Autowired
    private TempMapper tempMapper;

    public FlyreadServiceImpl() throws Exception {
    }

    @Override
    public String login() throws Exception {

        String url="http://open.book.qq.com/push/login?username="+username+"&password="+password;
        String jsonStr = HttpClientUtils.doGet(url);
        JSONObject resultJson = JSONObject.parseObject(jsonStr);
        String key= resultJson.getJSONObject("result").getString("key");
        System.out.println("得到提供给飞读的"+key);
        return key;
    }

    @Override
    public String addBook(Integer bookId,String key) throws Exception {
        //1,判断阅文是否拥有此书的权限
        if (null==cpAuthBookMapper.getOneByCpAuthIdAndBookId(FlyRedCpAuthId,bookId)){
            return "授权失败,此书:"+bookId+"不在飞读的授权中";
        }

        CloseableHttpResponse response = null;

        BookInfo bookInfo = bookInfoMapper.selectByPrimaryKey(bookId);

        //2,客户端对象

        CloseableHttpClient client = HttpClientBuilder.create().build();

        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();

        entityBuilder.addTextBody("key",new String(key.getBytes(),"utf-8"));//String 通过登录接口获得的key值

        entityBuilder.addTextBody("b.cpid", new String(tencetCpId.getBytes(),"utf-8"));//long 腾讯分配的CP编号

        entityBuilder.addTextBody("b.cpBid", new String(String.valueOf(bookId).getBytes(),"utf-8"));//long	CP侧的图书id

        entityBuilder.addTextBody("b.title",new String(bookInfo.getBookName().getBytes(),"utf-8"),contentType);//String 图书名称

        entityBuilder.addTextBody("b.author",new String(bookInfo.getAuthor().getBytes(),"utf-8"),contentType);//String 作者

        Integer completeState = bookInfo.getCompleteState();
        completeState = completeState==0? 1:0;//将完结状态转意

        entityBuilder.addTextBody("b.finish",new String(completeState.toString().getBytes(),"utf-8"));//int	是否完结  1 已完结；0 未完结

        entityBuilder.addTextBody("b.intro",new String(bookInfo.getDescription().getBytes(),"utf-8"),contentType);//String	图书简介

        File fileByUrl = fileOptionUtil.getFileByUrl(bookInfo.getPicUrl());//File	图书的封面图片

        entityBuilder.addPart("b.cover",new FileBody(fileByUrl,fileByUrl.getName(),"image/jpeg","utf-8"));

        entityBuilder.addTextBody("b.category",new String(getCategory(bookInfo.getCategory()).getBytes(),"utf-8"));//三级分类ID

        entityBuilder.addTextBody("b.sex",new String(getSex(bookInfo.getCategory()).getBytes(),"utf-8")); //int	针对群体  0 通用；1 男版；2 女版(现在不允许通用了)

        entityBuilder.addTextBody("b.form",new String("0".getBytes(),"utf-8"));//int	形式属性  0原创  1出版

        entityBuilder.addTextBody("b.language",new String("0".getBytes(),"utf-8"));//int	语言属性  0中文  1英文  2其他

        entityBuilder.addTextBody("b.free",new String("0".getBytes(),"utf-8"));//收费状态 0 收费 1 免费 2 vip包月 （默认值 1）

        entityBuilder.addTextBody("b.pushlishTime",new String(new SimpleDateFormat("yyyy-mm-dd").format(bookInfo.getCreateTime()).getBytes(),"utf-8"));

        HttpEntity entity = entityBuilder.build();

        HttpPost post = new HttpPost(addCofreeBookUrl);

        String jsonString="";

        post.setEntity(entity);

        try {
            response = client.execute(post);

            jsonString = getResponseResult(response);

            if (null== net.sf.json.JSONObject.fromObject(jsonString)){
                return "<br>id为:"+bookId+"的书通信失败"+jsonString.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "<br>添加书籍"+bookId+"网络超时";
        }
        //System.out.println(jsonString);//打印结果,测完就注解
        //{"code":0,"message":"Success","result":{"bookid":"2000000608000000000029795"}}
        net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(jsonString);
        //判断通信标识
        String message = jsonObject.getString("message");

        if (!StringUtils.equals("Success",message)){
            System.out.println("id为"+bookId+"的书籍添加失败"+jsonString);
            return "<br>id为"+bookId+"的书籍添加失败"+jsonString;
        }

        net.sf.json.JSONObject result = jsonObject.getJSONObject("result");
        String ResultBookid = result.getString("bookid");
        //System.out.println(bookId+"返回的bookId是:"+ResultBookid);//打印返回的bookid
        Temp temp = new Temp();
        temp.setOld(bookId.toString());
        temp.setThat(ResultBookid);
        temp.setType("飞读返回的bookid");
        if (null==tempMapper.selectOne(temp)){
            System.out.println("没有这条记录");
            int i = tempMapper.insertSelective(temp);
            if (i<0){
                return "添加至记录失败";
            }
        }
        return "成功";
    }

    @Override
    public String addChapterByBookId(int bookId, String key) throws Exception {
        //1,判断阅文是否拥有此书的权限
        if (null == cpAuthBookMapper.getOneByCpAuthIdAndBookId(FlyRedCpAuthId, bookId)) {
            return "授权失败,此书:" + bookId + "不在阅文的授权中";
        }
        StringBuilder CharpterMsg=new StringBuilder();
        //2.从temp表中获取飞读返回的bookid
        Example example = new Example(Temp.class);
        example.createCriteria().andEqualTo("old",bookId).andEqualTo("type","飞读返回的bookid");
        Temp resultTemp = tempMapper.selectOneByExample(example);
        String ResultBookid = resultTemp.getThat();

        //3.准备response之后,遍历这本书的章节
        CloseableHttpResponse response = null;

        List<ChapterInfo> RetryChapterInfos = chapterInfoMapper.selectChapterListByBookId(bookId);
        for (ChapterInfo cinfo : RetryChapterInfos){
            if (cinfo.getChapterName().indexOf("91阅读xjzj")>=0){
                continue;
            }
            //客户端对象
            CloseableHttpClient clientChapter = HttpClientBuilder.create().build();
            MultipartEntityBuilder entityBuilderChapter = MultipartEntityBuilder.create();
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
            entityBuilderChapter.addTextBody("c.content",new String(bookContent.getBytes(),"utf-8"),contentType );//String	增加的章节内容

            HttpEntity buildChapter = entityBuilderChapter.build();
            HttpPost postChapter = new HttpPost(addChapterUrl);
            postChapter.setEntity(buildChapter);

            String respMsg="";
            try {
                response = clientChapter.execute(postChapter);

                respMsg = getResponseResult(response);

                if (null== net.sf.json.JSONObject.fromObject(respMsg)){
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

            net.sf.json.JSONObject msgJson = net.sf.json.JSONObject.fromObject(respMsg);
            //判断通信标识
            String msgCode = msgJson.getString("message");

            if (StringUtils.equals("Success!",msgCode)){
                //System.out.print(bookId+"_"+cinfo.getChapterId()+":成功");//打印
                //System.out.print(cinfo.getChapterName()+"上传成功");
            }else {
                CharpterMsg.append("书id"+bookId+"的"+cinfo.getChapterId()+msgCode);
                System.out.print("书id"+bookId+"的"+cinfo.getChapterId()+msgCode);
            }
            release(response,clientChapter);
        }
        if (CharpterMsg.length()<5){
            System.out.println(bookId+"已上传成功");
            return "成功";
        }
        System.out.println(bookId+"已完毕");
        return CharpterMsg.toString();
    }

    @Override
    public String updateChapter(Integer bookId,Integer chapterId,String c_chapterid, String key) throws Exception{

        StringBuilder CharpterMsg=new StringBuilder();
        //2.从temp表中获取飞读返回的bookid
        Example example = new Example(Temp.class);
        example.createCriteria().andEqualTo("old",bookId).andEqualTo("type","飞读返回的bookid");
        Temp resultTemp = tempMapper.selectOneByExample(example);
        String ResultBookid = resultTemp.getThat();
        ChapterInfo chapterInfo = chapterInfoMapper.selectOneByBookIdAndChapterId(bookId, chapterId);

        //3.准备response之后,遍历这本书的章节
        CloseableHttpResponse response = null;

            //客户端对象
            CloseableHttpClient clientChapter = HttpClientBuilder.create().build();
            MultipartEntityBuilder entityBuilderChapter = MultipartEntityBuilder.create();
            entityBuilderChapter.addTextBody("key",new String(key.getBytes(),"utf-8"));
            entityBuilderChapter.addTextBody("c.cpid",new String(tencetCpId.getBytes(),"utf-8"));//long	腾讯分配的CP编号
            entityBuilderChapter.addTextBody("c.bookid",new String(ResultBookid.getBytes(),"utf-8"));//long	增加图书后返回给CP的bookid值
            entityBuilderChapter.addTextBody("c.title",new String(chapterInfo.getChapterName().getBytes(),"utf-8"),contentType);//	String	可选	章节名称
            //在去OSS中拿章节内容的信息
             final String URL = String.format("%s/%s/%s/%s.txt", ApiConstant.Config.BASE_PATH, "booktxt", bookId, chapterId);
             String bookContent = HttpClientUtil.doGet(URL);
             for (int i=0;i<20;i++){
                if (null==bookContent){
                bookContent = HttpClientUtil.doGet(URL);
                    }else {
                         break;
                        }
              }
        entityBuilderChapter.addTextBody("c.content",new String(bookContent.getBytes(),"utf-8"),contentType);//String	可选	增加的章节内容

        entityBuilderChapter.addTextBody("c.chapterid",new String(c_chapterid.getBytes(),"utf-8"));//必填	章节序号，阅文侧章节id（可在开放平台查看）
        entityBuilderChapter.addTextBody("c.cpcid",new String("0".getBytes(),"utf-8"));//String 必填	CP侧章节id  （如果在新增章节的时候没有填此参数，则此处填“0”）
            HttpEntity buildChapter = entityBuilderChapter.build();
            HttpPost postChapter = new HttpPost(updateChapterUrl);
            postChapter.setEntity(buildChapter);

            String respMsg="";
            try {
                response = clientChapter.execute(postChapter);

                respMsg = getResponseResult(response);

                if (null== net.sf.json.JSONObject.fromObject(respMsg)){
                    CharpterMsg.append("书"+bookId+"通信失败").append(respMsg).toString();
                }else {
                    System.out.println("resMsg是:"+respMsg);
                }
            }catch (Exception e) {
                CharpterMsg.append("书id"+bookId+"网络超时");
                System.out.print("书id"+bookId+"网络超时");
                e.printStackTrace();
            }
            //System.out.println("内容来自章节:"+cinfo.getChapterName());//打印内容出处
            //System.out.println(respMsg);//打印结果,测完就注解

            net.sf.json.JSONObject msgJson = net.sf.json.JSONObject.fromObject(respMsg);
            //判断通信标识
            String msgCode = msgJson.getString("message");

            if (StringUtils.equals("Success!",msgCode)){
                System.out.println(bookId+":成功");//打印
            }
            release(response,clientChapter);

        if (CharpterMsg.length()<5){
            System.out.println(bookId+"已成功");
            return "成功";
        }
        System.out.println(bookId+"已完毕");
        return CharpterMsg.toString();
    }

    @Override
    public String getUpdateInfo(String bookId, String key) throws Exception{
        //获取飞读的更新信息

        //1.从temp表中获取飞读返回的bookid
        Example example = new Example(Temp.class);
        example.createCriteria().andEqualTo("old",bookId).andEqualTo("type","飞读返回的bookid");
        Temp resultTemp = tempMapper.selectOneByExample(example);
        String ResultBookid = resultTemp.getThat();

        //3.准备response之后,遍历这本书的章节
        CloseableHttpResponse response = null;

        //客户端对象
        CloseableHttpClient clientChapter = HttpClientBuilder.create().build();
        MultipartEntityBuilder entityBuilderChapter = MultipartEntityBuilder.create();
        entityBuilderChapter.addTextBody("key",new String(key.getBytes(),"utf-8"));
        entityBuilderChapter.addTextBody("b.cpid",new String(tencetCpId.getBytes(),"utf-8"));//long	腾讯分配的CP编号
        entityBuilderChapter.addTextBody("b.cpBid",new String(bookId.getBytes(),"utf-8"));//CP侧的图书id

        HttpEntity buildChapter = entityBuilderChapter.build();
        HttpPost postChapter = new HttpPost(getUpdateUrl);
        postChapter.setEntity(buildChapter);

        String respMsg="";
        try {
            response = clientChapter.execute(postChapter);

            respMsg = getResponseResult(response);

            if (null== net.sf.json.JSONObject.fromObject(respMsg)){
                return "通信失败";
            }
        }catch (Exception e) {
            System.out.print("书id"+bookId+"网络超时");
            e.printStackTrace();
            return "通信失败";
        }
        release(response,clientChapter);

        System.out.println(bookId+"已完毕");
        return respMsg;
    }

    @Override
    public String putChapterBySelective(String bookidStr, String chaptername, String key) throws Exception{
        int bookId = Integer.parseInt(bookidStr);
        StringBuilder CharpterMsg=new StringBuilder();
        //2.从temp表中获取飞读返回的bookid
        Example example = new Example(Temp.class);
        example.createCriteria().andEqualTo("old",bookId).andEqualTo("type","飞读返回的bookid");
        Temp resultTemp = tempMapper.selectOneByExample(example);
        String ResultBookid = resultTemp.getThat();

        //3.准备response之后,遍历这本书的章节
        CloseableHttpResponse response = null;
        int indexOf = chaptername.indexOf("章 ");
        String str2=chaptername.substring(indexOf+2,chaptername.length());
        List<ChapterInfo> RetryChapterInfos=chapterInfoMapper.selectChapterListByBookIdAndLikeName(bookId,str2);
        for (ChapterInfo cinfo : RetryChapterInfos){
            if (cinfo.getChapterName().indexOf("91阅读xjzj")>=0){
                continue;
            }
            //客户端对象
            CloseableHttpClient clientChapter = HttpClientBuilder.create().build();
            MultipartEntityBuilder entityBuilderChapter = MultipartEntityBuilder.create();
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
            entityBuilderChapter.addTextBody("c.content",new String(bookContent.getBytes(),"utf-8"),contentType );//String	增加的章节内容

            HttpEntity buildChapter = entityBuilderChapter.build();
            HttpPost postChapter = new HttpPost(addChapterUrl);
            postChapter.setEntity(buildChapter);

            String respMsg="";
            try {
                response = clientChapter.execute(postChapter);

                respMsg = getResponseResult(response);

                if (null== net.sf.json.JSONObject.fromObject(respMsg)){
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

            net.sf.json.JSONObject msgJson = net.sf.json.JSONObject.fromObject(respMsg);
            //判断通信标识
            String msgCode = msgJson.getString("message");

            if (StringUtils.equals("Success!",msgCode)){
                //System.out.print(bookId+"_"+cinfo.getChapterId()+":成功");//打印
                System.out.print(cinfo.getChapterName()+"上传成功");
            }else {
                CharpterMsg.append("书id"+bookId+"的"+cinfo.getChapterId()+msgCode);
                System.out.print("书id"+bookId+"的"+cinfo.getChapterId()+msgCode);
            }
            release(response,clientChapter);
        }
        if (CharpterMsg.length()<5){
            System.out.println(bookId+"已上传成功");
            return "成功";
        }
        System.out.println(bookId+"已完毕");
        return CharpterMsg.toString();
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

    public static String getCategory(String type){
        switch (type) {
            case "浪漫青春":
                return "30033";//青春纯爱
            case "古代言情":
                return "30016";//古代情缘
            case "幻想言情":
                return "30113";//幻想纯爱
            case "现代言情":
                return "30021";//都市生活

            case "都市生活":
                return "20020";//都市生活
            case "科幻末世":
                return "20047";//超级科技
            case "历史军事":
                return "20091";//军事战争
            case "仙侠武侠":
                return "20012";//武侠幻想
            case "玄幻奇幻":
                return "20007";//史诗奇幻
            case "玄幻仙侠":
                return "20016";//幻想修仙
            case "悬疑灵异":
                return "20039";//诡秘悬疑
        }
        return "20096";//短篇小说
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
}
