package com.nine.one.yuedu.read.service.impl;

import com.nine.one.yuedu.read.config.ApiConstant;
import com.nine.one.yuedu.read.entity.*;
import com.nine.one.yuedu.read.mapper.AuditBookMapper;
import com.nine.one.yuedu.read.mapper.AuditChapterMapper;
import com.nine.one.yuedu.read.mapper.ChapterInfoMapper;
import com.nine.one.yuedu.read.mapper.CsChapterinfoMapper;
import com.nine.one.yuedu.read.service.ChangeService;
import com.nine.one.yuedu.read.utils.AliyunOSSUtil;
import com.nine.one.yuedu.read.utils.GX.Format91;
import com.nine.one.yuedu.read.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Author:李王柱
 * 2020/7/21
 */
@Service(value = "changeService")
public class ChangeServiceImpl implements ChangeService {
    @Autowired
    private ChapterInfoMapper chapterInfoMapper;
    @Autowired
    private AliyunOSSUtil aliyunOSSUtil;
    @Autowired
    private CsChapterinfoMapper csChapterinfoMapper;
    @Autowired
    private AuditBookMapper auditBookMapper;
    @Autowired
    private AuditChapterMapper auditChapterMapper;
    @Override
    public void changeChapterName(Integer bookId, Integer start, Integer end) {

        List<ChapterInfo> chapterInfos = chapterInfoMapper.selectChapterListByBookId(bookId);
        int i=1;

        for (ChapterInfo chapterInfo:chapterInfos){
            if (i>=start && i<end){
                Integer chapterId = chapterInfo.getChapterId();
                String chapterName = chapterInfo.getChapterName();

                Example example=new Example(ChapterInfo.class);
                example.createCriteria().andEqualTo("chapterId",chapterId+1).andEqualTo("bookId",bookId);
                ChapterInfo chapterInfo1 = chapterInfoMapper.selectOneByExample(example);
                String chapterName1 = chapterInfo1.getChapterName();

                String change = change(chapterName, chapterName1);
                chapterInfo.setChapterName(change);
                chapterInfoMapper.updateByPrimaryKey(chapterInfo);
            }
            i++;
        }
    }

    @Override
    public void changeChapterNameForMin(Integer bookId, Integer start, Integer end) {
        for (int i=end ;i>start;i--){
            //先查出现在的章节
            Example example=new Example(ChapterInfo.class);
            example.createCriteria().andEqualTo("chapterId",i).andEqualTo("bookId",bookId);
            ChapterInfo chapterInfo = chapterInfoMapper.selectOneByExample(example);
            System.out.println("现在的章节名是:"+chapterInfo.getChapterName());

            //查出前面的章节
            Example example1=new Example(ChapterInfo.class);
            example1.createCriteria().andEqualTo("chapterId",i-1).andEqualTo("bookId",bookId);
            ChapterInfo chapterInfo1 = chapterInfoMapper.selectOneByExample(example1);
            System.out.println("前面的章节名是:"+chapterInfo1.getChapterName());

            //参数1:之前的章节名,  返回:现在的章节名
            String change = change(chapterInfo1.getChapterName(), chapterInfo.getChapterName());
            System.out.println("要改为:"+change);
            chapterInfo.setChapterName(change);
            chapterInfoMapper.updateByPrimaryKey(chapterInfo);
        }
    }

    @Override
    public void changeChapterNameForNex(Integer bookId, Integer start, Integer end) {
        for (int i=end ;i>start;i--){
            //先查出现在的章节
            Example example=new Example(ChapterInfo.class);
            example.createCriteria().andEqualTo("chapterId",i).andEqualTo("bookId",bookId);
            ChapterInfo chapterInfo = chapterInfoMapper.selectOneByExample(example);
            System.out.println("现在的章节名是:"+chapterInfo.getChapterName());

            //查出前面的章节
            Example exampleLeft=new Example(ChapterInfo.class);
            exampleLeft.createCriteria().andEqualTo("chapterId",i-1).andEqualTo("bookId",bookId);
            ChapterInfo chapterInfoLeft = chapterInfoMapper.selectOneByExample(exampleLeft);
            System.out.println("前面的章节名是:"+chapterInfoLeft.getChapterName());

            //参数1:之前的章节名,  返回:前面的章节号+现在的章节名
            String change = change(chapterInfoLeft.getChapterName(), chapterInfo.getChapterName());
            System.out.println("要改为:"+change);
            chapterInfo.setChapterName(change);
            chapterInfoMapper.updateByPrimaryKey(chapterInfo);
        }
    }

    @Override
    public String getChapterContentAndRemoveSpace(Integer bookId) {
        //根据报联书id获取章节list
        //在遍历章节list中,先获取单个章节的内容,将内容进行去多余空格算法处理后,再更新到阿里云
        List<ChapterInfo> chapterInfos = chapterInfoMapper.selectChapterListByBookId(bookId);
        for (ChapterInfo chapterInfo : chapterInfos) {
            //从阿里云获取原本的章节内容,进行处理
            final String URL = String.format("%s/%s/%s/%s.txt", ApiConstant.Config.BASE_PATH, "booktxt", bookId, chapterInfo.getChapterId());
            String bookContent = HttpClientUtil.doGet(URL);
            String content = bookContent.replace(" ", "");
            //从OSS删除章节的内容
            String objectName = String.format("booktxt/%s/%s.txt", bookId, chapterInfo.getChapterId());
            aliyunOSSUtil.delete(objectName);
            //上传新的内容到OSS
            aliyunOSSUtil.stringToTxtAndUploadOSS(bookId, chapterInfo.getChapterId(), content);
        }
        return bookId+"完成";
    }

    @Override
    public String getChapterContentAndRemoveSpaceToJingXiang(int jingxiangBookId) {
        //根据景像书id获取章节list
        //在遍历章节list中,先从景像本地获取单个章节的内容,将内容进行去多余空格算法处理后,再更新到本地
        CsChapterinfo csChapterinfoBySelectList = new CsChapterinfo();
        csChapterinfoBySelectList.setBookid(jingxiangBookId);
        List<CsChapterinfo> thisBookList = csChapterinfoMapper.select(csChapterinfoBySelectList);
        for (CsChapterinfo csChapterinfo:thisBookList){
            //从景像本地获取这个章节内容
            //将章节内容去空格处理
            //删除景像本地章节内容文件
            //将处理过的内容文件添加到景像本地
        }
        return null;
    }

    @Override
    public String updateFormal(int bookid) {
        ChapterInfo chapterInfoBySelect = new ChapterInfo();
        chapterInfoBySelect.setBookId(bookid);
        List<ChapterInfo> infoList = chapterInfoMapper.select(chapterInfoBySelect);
        for (ChapterInfo chapterInfo:infoList){

        }
        return null;
    }

    @Override
    public String trimByBookId(int bookid) {
        //获取这本书的所有章节
        List<ChapterInfo> chapterInfos = chapterInfoMapper.selectChapterListByBookId(bookid);
        for (ChapterInfo chapterInfo:chapterInfos){
            String newName = Format91.chapterNameFromat(chapterInfo.getChapterName());
            chapterInfo.setChapterName(newName);
            int i = chapterInfoMapper.updateByPrimaryKeySelective(chapterInfo);
         if (i<0){
             return chapterInfo.getChapterName()+"失败!";
         }
        }
        return "通信成功";
    }

    @Override
    public String weedOut(Integer bookId, Integer start, Integer end, String dele) {
        //在遍历章节list中,先获取单个章节的内容,将内容进行去多余空格算法处理后,再更新到阿里云
        List<ChapterInfo> chapterInfos = chapterInfoMapper.selectRangeChapter(bookId,start,end);
        for (ChapterInfo chapterInfo : chapterInfos) {

            String bookContent = "";
            //从阿里云获取原本的章节内容,进行处理
            final String URL = String.format("%s/%s/%s/%s.txt", ApiConstant.Config.BASE_PATH, "booktxt", bookId, chapterInfo.getChapterId());
            try {
                bookContent = HttpClientUtil.doGet(URL);
            }catch (Exception e){
                System.out.println(chapterInfo.getChapterId()+"第一次获取网络失败");
                bookContent = HttpClientUtil.doGet(URL);
            }

            String content = bookContent.replace(dele, "");
            //从OSS删除章节的内容
            String objectName = String.format("booktxt/%s/%s.txt", bookId, chapterInfo.getChapterId());
            aliyunOSSUtil.delete(objectName);
            //上传新的内容到OSS
            try {
                aliyunOSSUtil.stringToTxtAndUploadOSS(bookId, chapterInfo.getChapterId(), content);
            }catch (Exception e){
                System.out.println(chapterInfo.getChapterId()+"第一次上传失败");
                aliyunOSSUtil.delete(objectName);
                aliyunOSSUtil.stringToTxtAndUploadOSS(bookId, chapterInfo.getChapterId(), content);
            }

        }
        return "通信成功";
    }

    @Override
    public String changeSumWordsToAudit() {
        //获取所有
        List<AuditBook> auditBooks = auditBookMapper.selectAll();
        for (AuditBook auditBook:auditBooks){
            int newWord=0;
            AuditChapter auditChapterByselect = new AuditChapter();
            auditChapterByselect.setBookId(auditBook.getId());
            List<AuditChapter> select = auditChapterMapper.select(auditChapterByselect);
            for (AuditChapter auditChapter1:select){
                newWord+=auditChapter1.getWords();
            }
            auditBook.setWords(newWord);
            auditBookMapper.updateByPrimaryKeySelective(auditBook);
            System.out.println(auditBook.getBookName()+"完毕");
        }
        return "通信成功";
    }

    public static void main(String[] args) {
        /*String mini="第二章 老节";
        String big="第三章 老章";
        System.out.println(NumBerForMinus(mini,big));*/

        String haveReplace="这算是正常章节没有留白\n　　见到赵西坡不搭理自己，曾大牛就是一阵的气愤，更让曾大牛气愤的是韩雪琪现在整个人都依偎在赵西坡的怀中，这让曾大牛杀人的心都有了。\n" +
                "　　“一对狗男女，婊 子……”\n" +
                "　　曾大牛大声的怒骂道。\n" +
                "　　“啪！";
        System.out.println(getRemoveSpaceContent(haveReplace));
    }
    public static String getRemoveSpaceContent(String oldContent){
        String replace = oldContent.replace(" ", "");
        return replace;
    }

    public static String NumBerForMinus(String mini,String big){
        int miniDi = mini.indexOf("第");
        int miniZhang=mini.indexOf("章");
        String substringMini = mini.substring(miniDi + 1, miniZhang);

        StringBuilder stringBuilder = new StringBuilder(big);
        stringBuilder.delete(big.indexOf("第")+1,big.indexOf("章"));
        stringBuilder.insert(big.indexOf("第")+1,substringMini);
        return stringBuilder.toString();
    }

    public static String change(String mini,String big){
        int miniDi = mini.indexOf("第");
        int miniZhang=mini.indexOf("章");
        String substringMini = mini.substring(miniDi + 1, miniZhang);

        StringBuilder stringBuilder = new StringBuilder(big);
        stringBuilder.delete(big.indexOf("第")+1,big.indexOf("章"));
        stringBuilder.insert(big.indexOf("第")+1,substringMini);
        return stringBuilder.toString();
    }
}
