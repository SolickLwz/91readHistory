package com.nine.one.yuedu.read.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nine.one.yuedu.read.config.ApiConstant;
import com.nine.one.yuedu.read.entity.BookInfo;
import com.nine.one.yuedu.read.entity.ChapterInfo;
import com.nine.one.yuedu.read.mapper.BookInfoMapper;
import com.nine.one.yuedu.read.mapper.ChapterInfoMapper;
import com.nine.one.yuedu.read.service.ChapterService;
import com.nine.one.yuedu.read.utils.AliyunOSSUtil;
import com.nine.one.yuedu.read.utils.GX.Format91;
import com.nine.one.yuedu.read.utils.HttpClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service(value = "chapterService")
public class ChapterServiceImpl implements ChapterService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ChapterInfoMapper chapterInfoMapper;
    @Autowired
    private AliyunOSSUtil aliyunOSSUtil;
    @Autowired
    private BookInfoMapper bookInfoMapper;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Map<String, Object> uploadTxtChangeChapterList(Integer bookId, MultipartFile file) {
        Map<String, Object> map = new HashMap<>();
        //判断该文件是不是一个txt文件,如果不是,就不往下进行了
        if (!StringUtils.endsWithIgnoreCase(file.getOriginalFilename(), ".txt")) {
            map.put("flag", false);
            map.put("msg", "上传失败,文件类型必须是.txt");
            return map;
        }
        //程序执行到此,说明文件一定是txt类型的
        //读取文件
        byte[] bytes = null;
        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //这个字符型就是上传的txt文件读出的字符串
        String txtContent = new String(bytes, Charset.forName("UTF-8"));
        //开始构建一系列参数,然后上传
        String[] contentList = txtContent.split("###");
        //此时得到的每一个字符串,应该是文章标题\n加内容的格式
        //初始化一个章节Id
        for (int i = 1; i < contentList.length; i++) {
            //以\n拆分字符串,得到的就是标题和内容
            /**
             * 这个方法的介绍:str.split(参数一,参数二)
             * 参数1:是一个正则表达式,匹配的是符合这个类型的所有字符串
             * 参数2:限制拆分后得到的数组的长度,拆分从左到右进行
             */
            String[] titleAndContentList = contentList[i].split("\n", 2);
            //数组的第一个元素是标题
            String title = titleAndContentList[0];
            //数组的第二个元素就是内容
            String content = titleAndContentList[1];
            //创建章节对象,做持久化操作,入库或者存入对象存储
            ChapterInfo chapterInfo = new ChapterInfo();
            chapterInfo.setBookId(bookId);
            chapterInfo.setChapterId(i);
            chapterInfo.setChapterName(title);
            chapterInfo.setWords(content.length());
            chapterInfo.setIsFree((byte) 0);
            //设置cp合作方的章节Id
            chapterInfo.setCpChapterId(String.format("%s%s", bookId, i));
            chapterInfo.setCreateTime(new Date());
            chapterInfo.setUpdateTime(new Date());
            //去查询这个章节是否存在,如果存在就替换,不存在就添加
            Example example = new Example(ChapterInfo.class);
            example.createCriteria().andEqualTo("bookId", bookId).andEqualTo("chapterId", i);
            ChapterInfo chapterInfoIsHave = chapterInfoMapper.selectOneByExample(example);
            if (chapterInfoIsHave != null) {
                chapterInfoMapper.updateByPrimaryKey(chapterInfoIsHave);
            } else {
                chapterInfoMapper.insertSelective(chapterInfo);
            }
            //此处需要上传文章的内容到OSS
            aliyunOSSUtil.stringToTxtAndUploadOSS(bookId, i, content);
            logger.info(String.format("第%s章上传成功", i));
        }
        //书的章节上传完毕,需要修改book_info中的书籍信息
        BookInfo bookInfo = bookInfoMapper.selectByPrimaryKey(bookId);
        //设置书籍的字数
        bookInfo.setWords(txtContent.length());
        //设置最后的章节
        bookInfo.setLastChapterId(contentList.length - 1);
        //设置更新时间
        bookInfo.setUpdateTime(new Date());
        bookInfoMapper.updateByPrimaryKeySelective(bookInfo);
        //程序执行到了这里,没有出现异常,说明文件上传是成功的
        map.put("flag", true);
        map.put("msg", "书籍上传成功,章节已自动生成");
        return map;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void addChapter(ChapterInfo chapterInfo, String content) {
        content= Format91.chapterFromat(content);
        //先进行文本的上传
        aliyunOSSUtil.stringToTxtAndUploadOSS(chapterInfo.getBookId(), chapterInfo.getChapterId(), content);
        logger.info("文本上传成功了");
        //进行章节相关信息的入库

        if (chapterInfo.getChapterId()>20){//前20章免费,后面的收费
            chapterInfo.setIsFree((byte) 1);
        }else {
            chapterInfo.setIsFree((byte) 0);
        }
        chapterInfo.setCreateTime(new Date());
        chapterInfo.setUpdateTime(new Date());
        chapterInfo.setWords(content.length());
        chapterInfo.setCpChapterId(String.format("%s%s", chapterInfo.getBookId(), chapterInfo.getChapterId()));
        //入库操作
        chapterInfoMapper.insertSelective(chapterInfo);
        //入库完毕,更新这本书的字数和最后一章
        //要先查询出这本书的字数
        BookInfo bookInfo = bookInfoMapper.selectByPrimaryKey(chapterInfo.getBookId());
        bookInfo.setUpdateTime(new Date());
        //字数增加
        bookInfo.setWords(bookInfo.getWords() + content.length());
        //最后更新的章节
        //查询当前的最新章节
        Integer maxSort = chapterInfoMapper.selectMaxSortByBookId(bookInfo.getId());
        bookInfo.setLastChapterId(maxSort);
        //修改book_info表
        bookInfoMapper.updateByPrimaryKeySelective(bookInfo);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delChapterById(Integer id) {
        //查询到这个章节
        ChapterInfo chapterInfo = chapterInfoMapper.selectByPrimaryKey(id);
        //从OSS删除章节的内容
        String objectName = String.format("booktxt/%s/%s.txt", chapterInfo.getBookId(), chapterInfo.getChapterId());
        aliyunOSSUtil.delete(objectName);
        //修改这本书的字数
        //要先查询出这本书的字数
        BookInfo bookInfo = bookInfoMapper.selectByPrimaryKey(chapterInfo.getBookId());
        bookInfo.setUpdateTime(new Date());
        //字数减少
        bookInfo.setWords(bookInfo.getWords() - chapterInfo.getWords());
        //最后更新的章节
        Integer maxSort = chapterInfoMapper.selectMaxSortByBookId(bookInfo.getId());
        bookInfo.setLastChapterId(maxSort);
        //修改book_info表
        bookInfoMapper.updateByPrimaryKeySelective(bookInfo);
        //进行章节的删除操作
        chapterInfoMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void updateChapter(ChapterInfo chapterInfo, String content) {
        content= Format91.chapterFromat(content);
        //从OSS删除章节的内容
        String objectName = String.format("booktxt/%s/%s.txt", chapterInfo.getBookId(), chapterInfo.getChapterId());
        aliyunOSSUtil.delete(objectName);
        //上传新的内容到OSS
        aliyunOSSUtil.stringToTxtAndUploadOSS(chapterInfo.getBookId(), chapterInfo.getChapterId(), content);
        //获取这次更新的去除回车空格字数
        int thatWords= content.replaceAll("　|\r|\n| |\t", "").length();
        //获取书籍对象
        BookInfo bookInfo = bookInfoMapper.selectByPrimaryKey(chapterInfo.getBookId());
        bookInfo.setUpdateTime(new Date());
        //书籍字数变动
        int old=bookInfo.getWords();
        Integer thatBookWord= old+thatWords-chapterInfo.getWords();
        bookInfo.setWords(thatBookWord);
        //章节字数也变动
        chapterInfo.setWords(thatWords);
        //更新章节
        chapterInfo.setUpdateTime(new Date());
        chapterInfoMapper.updateByPrimaryKeySelective(chapterInfo);

        //修改bookInfo表
        bookInfoMapper.updateByPrimaryKeySelective(bookInfo);
    }

    @Override
    public Map<String, Object> getChapterInfoByBookIdAndChapterId(Integer bookId, Integer chapterId, String type) {
        Map<String, Object> map = new HashMap<>();
        //先查询出这本书的信息
        Example example = new Example(ChapterInfo.class);
        example.createCriteria().andEqualTo("bookId", bookId).andEqualTo("chapterId", chapterId);
        ChapterInfo chapterInfo = chapterInfoMapper.selectOneByExample(example);
        map.put("chapterInfo", chapterInfo);
        //在去OSS中拿章节内容的信息
        final String URL = String.format("%s/%s/%s/%s.txt", ApiConstant.Config.BASE_PATH, "booktxt", bookId, chapterId);
        String bookContent = HttpClientUtil.doGet(URL);
        /*logger.info(bookContent);*/
        if ("H5".equals(type)) {
            StringBuilder sb = new StringBuilder();
            bookContent = sb.append("&nbsp;&nbsp;&nbsp;").append(bookContent.replace("\r\n", "</p><p>&nbsp;&nbsp;&nbsp;").replace("\n", "</p><p>&nbsp;&nbsp;&nbsp;"))
                    .append("<p>&nbsp;&nbsp;&nbsp;").toString();
        }
        map.put("content", bookContent);
        return map;
    }

    @Override
    public PageInfo<ChapterInfo> getChapterListByBookIdPageAndSort(Integer bookId, Integer pageIndex, Integer pageSize, Integer sort) {
        //设置排除的方式:1为正序排列,2为倒序排列
        String orderBy = sort == 1 ? " chapter_id ASC" : " chapter_id DESC";
        PageHelper.startPage(pageIndex, pageSize, orderBy);
        //获取数据
        Example example = new Example(ChapterInfo.class);
        example.createCriteria().andEqualTo("bookId", bookId);
        List<ChapterInfo> chapterInfos = chapterInfoMapper.selectByExample(example);
        PageInfo<ChapterInfo> pageInfo = new PageInfo<>(chapterInfos);
        return pageInfo;
    }

    @Override
    public String BackUpByPrefix(int bookId, String prefix) {
        StringBuilder stringBuilder=new StringBuilder("通信成功");
        String prefixBookId=prefix+bookId;
        List<ChapterInfo> chapterInfos = chapterInfoMapper.selectChapterListByBookId(bookId);
        for (ChapterInfo chapterInfo:chapterInfos){

                System.out.println("备份:"+bookId+chapterInfo.getChapterName());
               /* //从OSS删除章节的内容
                String objectName = String.format("booktxt/%s/%s.txt", Integer.parseInt(prefixBookId), chapterInfo.getChapterId());
                aliyunOSSUtil.delete(objectName);*/

            try {
                /*//先上传有前缀的
                aliyunOSSUtil.stringToTxtAndUploadOSS(Integer.parseInt(prefixBookId), chapterInfo.getChapterId(), content);*/

                //从前缀拿,没有就把源处的放进前缀
                final String prefixURL = String.format("%s/%s/%s/%s.txt", ApiConstant.Config.BASE_PATH, "booktxt", prefixBookId, chapterInfo.getChapterId());
                for (int j=0;j<5;j++){
                    if (HttpClientUtil.doGet(prefixURL).length()<10){
                        //在去OSS中拿章节内容的信息
                        final String URL = String.format("%s/%s/%s/%s.txt", ApiConstant.Config.BASE_PATH, "booktxt", bookId, chapterInfo.getChapterId());
                        String content = HttpClientUtil.doGet(URL);

                        for (int i=0;i<5;i++){
                            if (null==content){
                                //在去OSS中拿章节内容的信息
                                content = HttpClientUtil.doGet(URL);
                            }else {
                                break;
                            }
                        }

                        aliyunOSSUtil.stringToTxtAndUploadOSS(Integer.parseInt(prefixBookId), chapterInfo.getChapterId(), content);
                    }else {
                        break;
                    }
                }

            } catch (Exception e) {
                System.out.println("书籍"+bookId+"的"+chapterInfo.getId()+"网络错误");
                stringBuilder.append("书籍"+bookId+"的"+chapterInfo.getId()+"网络错误");
                e.printStackTrace();
            }

        }

        return stringBuilder.toString();
    }
}
