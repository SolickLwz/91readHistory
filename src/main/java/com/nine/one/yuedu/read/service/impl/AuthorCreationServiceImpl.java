package com.nine.one.yuedu.read.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nine.one.yuedu.read.config.ApiConstant;
import com.nine.one.yuedu.read.config.JXResult;
import com.nine.one.yuedu.read.entity.*;
import com.nine.one.yuedu.read.entity.vo.AuditChapterAndBookNameVo;
import com.nine.one.yuedu.read.entity.vo.BookInfoVO;
import com.nine.one.yuedu.read.mapper.*;
import com.nine.one.yuedu.read.service.AuthorCreationService;
import com.nine.one.yuedu.read.utils.AliyunOSSUtil;
import com.nine.one.yuedu.read.utils.GX.Format91;
import com.nine.one.yuedu.read.utils.GX.SensitivewordFilter;
import com.nine.one.yuedu.read.utils.HttpClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Author:李王柱
 * 2020/8/12
 */
@Service
public class AuthorCreationServiceImpl implements AuthorCreationService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    SimpleDateFormat yyyy = new SimpleDateFormat("yyyy");
    SimpleDateFormat MM = new SimpleDateFormat("yyyy-MM");
    SimpleDateFormat Month = new SimpleDateFormat("MM");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private AliyunOSSUtil aliyunOSSUtil;
    @Autowired
    private AuditBookMapper auditBookMapper;
    @Autowired
    private BookInfoMapper bookInfoMapper;
    @Autowired
    private ChapterInfoMapper chapterInfoMapper;
    @Autowired
    private AuditChapterMapper auditChapterMapper;
    @Autowired
    private UserAuthorMapper userAuthorMapper;
    @Autowired
    private AllowFreeMapper allowFreeMapper;
    @Autowired
    private NoticeMapper noticeMapper;
    @Override
    public int addAuditBook(AuditBook auditBook) {
        //获得当前数据库中最大的ID
        /*Integer maxId = bookInfoMapper.getCurrentMaxId();
        //这本书的ID,让原来的ID+1
        auditBook.setBookId(maxId + 1);*/
        //字数在上传章节以后要变化,现在先设置为0
        auditBook.setWords(0);
        //上架下架,刚上传完毕,默认下架状态
        auditBook.setValid(0);
        //书的创建时间
        auditBook.setCreateTime(new Date());
        //书籍的更新时间
        auditBook.setUpdateTime(new Date());
        //阅读人数
        auditBook.setVisitCount(0);
        //是否允许搜索,默认不允许
        auditBook.setOpenSearch(0);
        //上传
        int result= auditBookMapper.insertSelective(auditBook);
        return result;
    }

    @Override
    public PageInfo<AuditBook> getAuditBook4PageAndParam(Integer pageIndex, Integer pageSize, String privoder, String bookName, String author, Integer id, String category, Integer auditStatus) throws ParseException {
        PageHelper.startPage(pageIndex, pageSize);
        List<AuditBook> bookInfos = auditBookMapper.selectBookInfo4PageAndParam(privoder, bookName, author, id, category, auditStatus);
        /*for (AuditBook auditBook : bookInfos){
            System.out.println(auditBook.getCreateTime());
            SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format1 = format.format(auditBook.getCreateTime());
            auditBook.setCreateTime(format.parse(format1));
        }*/
        PageInfo<AuditBook> bookInfoPageInfo = new PageInfo<>(bookInfos);
       /* List<AuditBook> list = bookInfoPageInfo.getList();
        for (AuditBook auditBook : list){
            System.out.println(auditBook.getCreateTime());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");    //格式化规则
            Date date = auditBook.getCreateTime();         //获得你要处理的时间 Date型
            String strDate= sdf.format(date ); //格式化成yyyy-MM-dd格式的时间字符串
            Date newDate =sdf.parse(strDate);
            java.sql.Date resultDate = new java.sql.Date(newDate.getTime());//最后转换成 java.sql.Date类型数据就可以了
            System.out.println(resultDate);
            auditBook.setCreateTime(resultDate);
        }*/
        return bookInfoPageInfo;
    }

    @Override
    public JXResult toReview(Integer id, Integer auditStatus) {
        if (auditStatus==2){//如果是要驳回,查看该书是否有驳回章节,如果没有驳回章节,不能驳回这本书
            AuditChapter auditChapterBySelect = new AuditChapter();
            auditChapterBySelect.setBookId(id);
            auditChapterBySelect.setDraftStatus(2);
            List<AuditChapter> isReject = auditChapterMapper.select(auditChapterBySelect);
            if (0==isReject.size()){
                return new JXResult(false, ApiConstant.StatusCode.ERROR, "是哪一章节不通过了、请最少给1个章节提交驳回理由");
            }
        }
        if (auditStatus==1){//如果是要提交审核,把这章的驳回意见清除//现在驳回意见又不清除了
            /*AuditChapter updateByOptions= new AuditChapter();
            updateByOptions.setBookId(id);
            List<AuditChapter> select = auditChapterMapper.select(updateByOptions);
            for (AuditChapter auditChapter:select ){
                if (auditChapter.getOpinionsynopsis().length()>1){
                    auditChapter.setOpinionsynopsis("");
                    auditChapter.setOpinion("");
                    auditChapterMapper.updateByPrimaryKeySelective(auditChapter);
                }
            }*/
        }
        AuditBook auditBook = new AuditBook();
        auditBook.setId(id);
        auditBook.setUpdateTime(new Date());
        auditBook.setAuditStatus(auditStatus);
        auditBookMapper.updateByPrimaryKeySelective(auditBook);
        return new JXResult(true, ApiConstant.StatusCode.OK, "已驳回,请等待作者修改~");
    }

    @Override
    public PageInfo<AuditChapter> getChapterListByBookIdPageAndSort(Integer bookId, Integer pageIndex, Integer pageSize, Integer sort,Integer draftStatus) {
        //设置排除的方式:1为正序排列,2为倒序排列
        String orderBy = sort == 1 ? " chapter_id ASC" : " chapter_id DESC";
        PageHelper.startPage(pageIndex, pageSize, orderBy);

        //获取数据
        Example example = new Example(AuditChapter.class);
        //example.createCriteria().andEqualTo("bookId",bookId).andEqualTo("draftStatus",draftStatus);
        if (1==draftStatus){
            example.createCriteria().andEqualTo("bookId",bookId).andNotEqualTo("draftStatus",0);
        }else {
            example.createCriteria().andEqualTo("bookId",bookId).andEqualTo("draftStatus",draftStatus);
        }

        List<AuditChapter> auditChapters = auditChapterMapper.selectByExample(example);
        PageInfo<AuditChapter> pageInfo = new PageInfo<>(auditChapters);
        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void addAuditChapter(AuditChapter auditChapterInfo, String content) {
        content= Format91.chapterFromat(content);
        //先进行文本的上传
        String autidOss="20202121"+auditChapterInfo.getBookId();
        aliyunOSSUtil.stringToTxtAndUploadOSS(Integer.parseInt(autidOss), auditChapterInfo.getChapterId(), content);
        logger.info("文本上传成功了");
        int isFree = auditChapterInfo.getIsFree();//创作阶段暂时不用设置收费,在这先代表延迟发布时间,取出代表延时的值后,isFree设置为0
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(calendar.DATE,isFree);
        auditChapterInfo.setIsFree((byte) 0);
        auditChapterInfo.setCreateTime(calendar.getTime());
        auditChapterInfo.setUpdateTime(new Date());
        auditChapterInfo.setWords(content.replaceAll("　|\r|\n| |\t", "").length());
        auditChapterInfo.setCpChapterId(String.format("%s%s", auditChapterInfo.getBookId(), auditChapterInfo.getChapterId()));

        //入库操作
        int i = auditChapterMapper.insertSelective(auditChapterInfo);
        if (i<0){ logger.info("章节增加失败!"); }
        //入库完毕,更新这本书的字数和最后一章
        //要先查询出这本书的字数
        AuditBook auditBook = auditBookMapper.selectByPrimaryKey(auditChapterInfo.getBookId());
        auditBook.setUpdateTime(new Date());
        //字数增加
        //auditBook.setWords(auditBook.getWords() + content.length());
        auditBook.setWords(auditBook.getWords() + auditChapterInfo.getWords());//字数现在要把空格去掉以后的
        //最后更新的章节
        //查询当前的最新章节
        Integer maxSort = auditChapterMapper.selectMaxSortByAuditBookId(auditBook.getId());
        auditBook.setLastChapterId(maxSort);
        //修改book_info表
        auditBookMapper.updateByPrimaryKeySelective(auditBook);
    }

    @Override
    public Map<String, Object> getChapterInfoByBookIdAndChapterId(Integer bookId, Integer chapterId, String type) {
        Map<String, Object> map = new HashMap<>();
        //先查询出这本书的信息
        Example example = new Example(AuditChapter.class);
        example.createCriteria().andEqualTo("bookId", bookId).andEqualTo("chapterId", chapterId);
        AuditChapter auditChapter = auditChapterMapper.selectOneByExample(example);
        map.put("chapterInfo", auditChapter);
        //在去OSS中拿章节内容的信息
        String authorBookOss="20202121"+bookId;
        final String URL = String.format("%s/%s/%s/%s.txt", ApiConstant.Config.BASE_PATH, "booktxt", authorBookOss, chapterId);
        String bookContent = HttpClientUtil.doGet(URL);
        if ("H5".equals(type)) {
            StringBuilder sb = new StringBuilder();
            bookContent = sb.append("&nbsp;&nbsp;&nbsp;").append(bookContent.replace("\r\n", "</p><p>&nbsp;&nbsp;&nbsp;").replace("\n", "</p><p>&nbsp;&nbsp;&nbsp;"))
                    .append("<p>&nbsp;&nbsp;&nbsp;").toString();
        }
        map.put("content", bookContent);
        return map;
    }

    @Override
    public void updateChapter(ChapterInfo chapterInfo, String content) {

        content= Format91.chapterFromat(content);
            //从OSS删除章节的内容
        String authorBookId="20202121"+chapterInfo.getBookId();
            String objectName = String.format("booktxt/%s/%s.txt", authorBookId , chapterInfo.getChapterId());
            aliyunOSSUtil.delete(objectName);
            //上传新的内容到OSS
            aliyunOSSUtil.stringToTxtAndUploadOSS(Integer.parseInt(authorBookId), chapterInfo.getChapterId(), content);
            //更新章节
        AuditChapter auditChapter=new AuditChapter();
        auditChapter.setChapterId(chapterInfo.getChapterId());//章节序号变动
        auditChapter.setId(chapterInfo.getId());

        //auditChapter.setCreateTime(chapterInfo.getCreateTime());//如果要修改创建时间的话
        auditChapter.setUpdateTime(new Date());
        auditChapter.setBookId(chapterInfo.getBookId());
        auditChapter.setChapterName(chapterInfo.getChapterName());
        int thatWords= content.replaceAll("　|\r|\n| |\t", "").length();
        auditChapter.setWords(thatWords);
        auditChapter.setIsFree(chapterInfo.getIsFree());
            auditChapterMapper.updateByPrimaryKeySelective(auditChapter);

        //章节更新完毕,字数有可能变动,修改这本书的字数
        AuditBook auditBook = auditBookMapper.selectByPrimaryKey(chapterInfo.getBookId());
        auditBook.setUpdateTime(new Date());
        //字数变动
        Integer old= auditBook.getWords();
        Integer thatBookWord= old+thatWords-chapterInfo.getWords();
        auditBook.setWords(thatBookWord);
        //修改audit_Book表
        auditBook.setUpdateTime(new Date());//书籍也在这时更新了
        auditBookMapper.updateByPrimaryKeySelective(auditBook);
    }


    @Override
    public void insertOption(AuditChapter auditChapter) {
        auditChapterMapper.updateByPrimaryKeySelective(auditChapter);
    }

    @Override
    public void delChapterById(Integer id) {
        //查询到这个章节
        AuditChapter auditChapter = auditChapterMapper.selectByPrimaryKey(id);
        //从OSS删除章节的内容
        String authorBookId="20202121"+auditChapter.getBookId();
        String objectName = String.format("booktxt/%s/%s.txt", authorBookId, auditChapter.getChapterId());
        aliyunOSSUtil.delete(objectName);
        //修改这本书的字数
        //要先查询出这本书的字数
        AuditBook bookInfo = auditBookMapper.selectByPrimaryKey(auditChapter.getBookId());
        bookInfo.setUpdateTime(new Date());
        //字数减少
        bookInfo.setWords(bookInfo.getWords() - auditChapter.getWords());
        //最后更新的章节
        Integer maxSort = auditChapterMapper.selectMaxSortByAuditBookId(bookInfo.getId());
        bookInfo.setLastChapterId(maxSort);
        //修改book_info表
        auditBookMapper.updateByPrimaryKeySelective(bookInfo);
        //进行章节的删除操作
        auditChapterMapper.deleteByPrimaryKey(id);
    }

    @Override
    public AuditBook findById(Integer id) {
        AuditBook bookInfo = auditBookMapper.selectByPrimaryKey(id);
        //如果有人查看,就让阅读次数+1
        bookInfo.setVisitCount(bookInfo.getValid() + 1);
        auditBookMapper.updateByPrimaryKeySelective(bookInfo);
        return bookInfo;
    }

    @Override
    public void updateBookInfoToauthorBook(BookInfo bookInfo) {
        AuditBook auditBook = new AuditBook();
        //这里要做一下转换
        auditBook.setId(bookInfo.getId());
        auditBook.setBookName(bookInfo.getBookName());
        auditBook.setOpenSearch(bookInfo.getOpenSearch());
        auditBook.setPicUrl(bookInfo.getPicUrl());
        auditBook.setDescription(bookInfo.getDescription());
        auditBook.setCompleteState(bookInfo.getCompleteState());
        auditBook.setCategory(bookInfo.getCategory());
        auditBook.setKeywords(bookInfo.getKeywords());//关键词
        auditBook.setUpdateTime(new Date());
        auditBook.setReason(bookInfo.getReason());
        auditBook.setMaleLead(bookInfo.getMaleLead());
        auditBook.setHeroine(bookInfo.getHeroine());
        auditBook.setRecommend(bookInfo.getRecommend());
        auditBook.setRecommendtwo(bookInfo.getRecommendtwo());
        auditBookMapper.updateByPrimaryKeySelective(auditBook);

        //需求:在审核中的书修改简介的时候,同步到上架书,这里就把类型和关键词也都同步了
        //根据书名和作者获取正式书
        BookInfo bookInfoBySelect = new BookInfo();
        bookInfoBySelect.setBookName(bookInfo.getBookName());
        bookInfoBySelect.setAuthor(bookInfo.getAuthor());
        List<BookInfo> select = bookInfoMapper.select(bookInfoBySelect);
        if (select.size()!=1){
            return;
        }else{
            BookInfo bookInfoToUpdate = select.get(0);
            bookInfoToUpdate.setDescription(bookInfo.getDescription());
            if (null != bookInfo.getPicUrl() && !StringUtils.equals("",bookInfo.getPicUrl())){
                bookInfoToUpdate.setPicUrl(bookInfo.getPicUrl());
            }
            bookInfoToUpdate.setKeywords(bookInfo.getKeywords());
            bookInfoMapper.updateByPrimaryKeySelective(bookInfoToUpdate);
        }
    }

    @Override
    public List<AuditBook> getThatAuthorBoxBookList(String author) {

        Integer draftStatus=0;//这里要查的是该作者的草稿
        List<AuditBook> auditBookList= auditBookMapper.getThatAuthorBookListByDraftStatus(author,draftStatus);
        return auditBookList;
    }

    @Override
    public PageInfo<AuditChapterAndBookNameVo> getThatAuthorBoxChapterAllAndBookNameForVo(Integer pageIndex, Integer pageSize, String author, Integer sort, Integer draftStatus) {
        //设置排除的方式:1为正序排列,2为倒序排列
        String orderBy = sort == 1 ? " chapter_id ASC" : " chapter_id DESC";
        PageHelper.startPage(pageIndex, pageSize, orderBy);
        //获取数据
        List<AuditChapterAndBookNameVo> auditChapterAndBookNameVo= auditChapterMapper.getThatAuthorBoxChapterAllAndBookNameForVo(author,draftStatus);
        //打包分页
        PageInfo<AuditChapterAndBookNameVo> pageInfo = new PageInfo<>(auditChapterAndBookNameVo);
        return pageInfo;
    }

    @Override
    public String judgeChapterId(String chapterId,Integer bookId) {
        Integer thatchapterId=0;
        try {
            thatchapterId=Integer.parseInt(chapterId);
        } catch (Exception e) {
            return "请输入数字排序!"+chapterId+" 中是否存在空格或者标点符号";
        }
        AuditChapter auditChapter = new AuditChapter();
        auditChapter.setBookId(bookId);
        auditChapter.setChapterId(Integer.parseInt(chapterId));
        List<AuditChapter> select = auditChapterMapper.select(auditChapter);
        if (select.size() > 0) {
            return "重复的排序!这本书已经有 "+chapterId+" 这个排序的章节了";
        }

        //再去上架的章节中,看看这个准备添加的章节是否在对应的正式书里面已经有了
        return "成功";
    }

    @Override
    public Integer getMaxChapterId(Integer bookId) {
        Integer integer = auditChapterMapper.selectMaxSortByAuditBookId(bookId);
        if (null==integer){
            //有可能是因为过审了,根据bookid查出书籍,从书籍中拿到当前书的最大章节
            AuditBook auditBook = auditBookMapper.selectByPrimaryKey(bookId);
            Integer lastChapterId = auditBook.getLastChapterId();
            integer=lastChapterId;
            //integer=0;
        }
        return integer+1;
    }

    @Override
    public boolean getHaveNickName(String nickname) {
        UserAuthor userAuthor = new UserAuthor();
        userAuthor.setNickname(nickname);
        if (null==userAuthorMapper.selectOne(userAuthor)){
            return true;
        }
        return false;
    }

    @Override
    public void passed(String bookId, String chapterId,Integer isFree) {
        if (isFree!=0){//如果传来的isFree不是0,那就是规定了时间,交给定时任务,跳过后面的代码
            //获取这个章节,将状态改为4(即将上架),创建时间改为当前时间+isFree
            AuditChapter modify= new AuditChapter();
            modify.setBookId(Integer.parseInt(bookId));modify.setChapterId(Integer.parseInt(chapterId));
            AuditChapter ready= auditChapterMapper.selectOne(modify);

            Calendar calendar = new GregorianCalendar();calendar.setTime(new Date());calendar.add(calendar.DATE,isFree);
            ready.setCreateTime(calendar.getTime());ready.setDraftStatus(4);ready.setUpdateTime(calendar.getTime());
            auditChapterMapper.updateByPrimaryKeySelective(ready);
            return;
        }
        //根据书id找到创作书对象,看正式书库中有没有这个书名和作者的书
        AuditBook auditBook = auditBookMapper.selectByPrimaryKey(Integer.parseInt(bookId));
        BookInfo bookInfo = new BookInfo();
        bookInfo.setBookName(auditBook.getBookName());
        bookInfo.setAuthor(auditBook.getAuthor());
        BookInfo bookInfoHave = bookInfoMapper.selectOne(bookInfo);
        if (null==bookInfoHave){//如果没有,就创建正式书

            bookInfo.setCategory(auditBook.getCategory());
            bookInfo.setCompleteState(auditBook.getCompleteState());
            bookInfo.setPicUrl(auditBook.getPicUrl());
            bookInfo.setDescription(auditBook.getDescription());
            bookInfo.setKeywords(auditBook.getKeywords());
            bookInfo.setValid(1);
            bookInfo.setCreateTime(new Date());
            bookInfo.setUpdateTime(new Date());
            bookInfo.setCompleteState(1);
            bookInfo.setProvider("报联中视");
            bookInfo.setWords(0);
            bookInfo.setLastChapterId(0);
            bookInfo.setOpenSearch(0);
            bookInfo.setCpBookId("0");
            bookInfo.setVisitCount(0);//阅读人数
            bookInfo.setCpId(1000);//1000报联中视
            bookInfoMapper.insert(bookInfo);
        } //在这里就是一定有了,获取创作章节,把内容添加到正式章节中,然后删除创作章节的内容
            AuditChapter auditChapterByselect = new AuditChapter();
            auditChapterByselect.setBookId(Integer.parseInt(bookId));
            auditChapterByselect.setChapterId(Integer.parseInt(chapterId));

            AuditChapter auditChapterHave = auditChapterMapper.selectOne(auditChapterByselect);

            ChapterInfo chapterInfo = new ChapterInfo();
            chapterInfo.setChapterId(Integer.parseInt(chapterId));
            if (Integer.parseInt(chapterId)>20){
                chapterInfo.setIsFree((byte) 0);
            }else {
                chapterInfo.setIsFree((byte) 1);
            }
            chapterInfo.setChapterName(auditChapterHave.getChapterName());
            chapterInfo.setWords(auditChapterHave.getWords());
            chapterInfo.setCreateTime(new Date());

            //chapterInfo.setUpdateTime(auditChapterHave.getUpdateTime());
        chapterInfo.setUpdateTime(new Date());//这里作出修改,正式章节的更新时间就是过审时间
            BookInfo bookInfoByselect= new BookInfo();
            bookInfoByselect.setAuthor(auditBook.getAuthor());
            bookInfoByselect.setBookName(auditBook.getBookName());
            BookInfo bookInfoWhereGetId = bookInfoMapper.selectOne(bookInfoByselect);
            chapterInfo.setBookId(bookInfoWhereGetId.getId());
            chapterInfo.setCpChapterId(auditChapterHave.getCpChapterId());
            chapterInfoMapper.insert(chapterInfo);

            //从阿里云 获取创作章节内容,并替换到正式
            //在去OSS中拿章节内容的信息
            String authorBookOss="20202121"+bookId;
            final String URL = String.format("%s/%s/%s/%s.txt", ApiConstant.Config.BASE_PATH, "booktxt", authorBookOss, chapterId);
            String bookContent ="";
            try {
                bookContent = HttpClientUtil.doGet(URL);
            } catch (Exception e) {
                logger.info("获取创作章节内容网络超时1次");
                try {
                    bookContent = HttpClientUtil.doGet(URL);
                } catch (Exception e1) {
                    logger.info("获取创作章节内容网络超时2次");
                    bookContent = HttpClientUtil.doGet(URL);
                }
            }

            //将内容上传到阿里云
            Integer ossBookId=bookInfoWhereGetId.getId();
            aliyunOSSUtil.stringToTxtAndUploadOSS(ossBookId, Integer.parseInt(chapterId), bookContent);
            logger.info("文本上传成功了");

            //上传后,更新字数
        bookInfoWhereGetId.setWords(bookInfoWhereGetId.getWords()+auditChapterHave.getWords());
        Integer maxSort = chapterInfoMapper.selectMaxSortByBookId(bookInfoWhereGetId.getId());
        bookInfoWhereGetId.setLastChapterId(maxSort);//更新最新章节
        bookInfoWhereGetId.setUpdateTime(new Date());//更新书籍的修改时间
        bookInfoMapper.updateByPrimaryKeySelective(bookInfoWhereGetId);

        //删除这个创作章节,并且删除阿里云的内容
        //从OSS删除章节的内容
        String authorBookId="20202121"+bookId;
        String objectName = String.format("booktxt/%s/%s.txt", authorBookId, chapterId);
        aliyunOSSUtil.delete(objectName);

        //进行章节的删除操作
        auditChapterMapper.deleteByPrimaryKey(auditChapterHave.getId());
        Integer words = auditBook.getWords();//更新未上架书籍的字数
        auditBook.setWords(words-auditChapterHave.getWords());
        auditBookMapper.updateByPrimaryKeySelective(auditBook);
        }

    @Override
    public String isSensitive(String content) {
        //最开始,判断是否含有敏感词,如果没有就直接返回
        SensitivewordFilter filter = new SensitivewordFilter();
        boolean containtSensitiveWord = filter.isContaintSensitiveWord(content, 2);
       if (! containtSensitiveWord){
           return "通过";
       }
        //到这里就是有敏感词了,返回字符串,是有哪些敏感词
        Set<String> set = filter.getSensitiveWord(content, 2);
        return  "语句中有" + set.size() + "个敏感词QAQ..包含：" + set;
    }

    @Override
    public JSONArray getFeeDetail(String nickName,String start,String end) throws ParseException {
        JSONArray result=new JSONArray();
        DecimalFormat decimalFormat = new DecimalFormat("#0.000");
        //取出这个作者审核通过的书,一本书是一个json对象,对象里面包含返回数据
        //再遍历这个作者已经上架的书,如果array里面没有这本书的json,就创建一个新的,加入到array里面
        /*AuditBook auditBookBySelectiveWhereNickname = new AuditBook();
        auditBookBySelectiveWhereNickname.setAuthor(nickName);
        List<AuditBook> select = auditBookMapper.select(auditBookBySelectiveWhereNickname);
        for (AuditBook auditBook:select){//遍历
            System.out.println(auditBook.getBookName());
        }*/

        //取出这个作者已经上架的书list
        //遍历书list,一本书是一个json,往json里面添加数据,放到jsonArray中
        List<BookInfo> bookInfos;
        if ("admin".equals(nickName)){
            bookInfos=bookInfoMapper.selectHaveFeeBySumBookList();
        }else {
            BookInfo bookInfoBySelectiveWhereNickname = new BookInfo();
            bookInfoBySelectiveWhereNickname.setAuthor(nickName);
            bookInfos = bookInfoMapper.select(bookInfoBySelectiveWhereNickname);
        }

        String format = yyyy.format(new Date());
        String startTime=format+"-"+start+"-01 00:00:00";
        String endTime=format+"-"+end+"-01 00:00:00";
        Calendar cal = Calendar.getInstance();
        cal.setTime(simpleDateFormat.parse(endTime)); //设置时间
        cal.add(Calendar.MONTH, 1);  //在当前时间基础上加一个月
        cal.add(Calendar.SECOND, -1);  //在这时间基础上减一秒,得到月末时间

        Date startDate = simpleDateFormat.parse(startTime);
        Date endDate=cal.getTime();
        for (BookInfo bookInfo:bookInfos){
            JSONObject jsonObject = new JSONObject();

            BookInfo bookInfo1 = bookInfoMapper.selectOne(bookInfo);
            jsonObject.put("bookName",bookInfo1.getBookName());
            Integer wordsByMonth=0;

            Integer wordsByMonthResul= chapterInfoMapper.getWordsByMonth(bookInfo.getId(),startDate,endDate);
            if (null !=wordsByMonthResul){
                wordsByMonth=wordsByMonthResul;
            }
           try {
           }catch (Exception e){
               return null;
           }
           //如果这本书在创作中有章节的话
            AuditBook auditBookBySelect = new AuditBook();
           auditBookBySelect.setBookName(bookInfo1.getBookName());
            AuditBook auditBook;  //先获取这本书在创作中的书id
            if (null != auditBookMapper.selectOne(auditBookBySelect)){
                auditBook=auditBookMapper.selectOne(auditBookBySelect);
                if (null !=auditChapterMapper.selectByNickNameAndTimeInterval(auditBook.getId(),startDate,endDate)){
                    wordsByMonth+=auditChapterMapper.selectByNickNameAndTimeInterval(auditBook.getId(),startDate,endDate);
                }
            }
            if (wordsByMonth==0){
                continue;
            }
            jsonObject.put("wordsByMonth",wordsByMonth);
            //计算稿费,
            double originalFeeByMonth=wordsByMonth*bookInfo.getFeebysum();
            double feeByMonth=originalFeeByMonth/1000;
            jsonObject.put("feeByMonth",decimalFormat.format(feeByMonth));
            jsonObject.put("reward",0);//奖励先为0
            jsonObject.put("total",decimalFormat.format(feeByMonth));//总和=稿费+奖励
            //计算税率
            double Taxes;
            if (feeByMonth<=800){
                Taxes=0;
            }else if (feeByMonth<=4000){
                Taxes=(feeByMonth-800)*0.2*0.7;
            }else {
                Taxes=feeByMonth*0.8*0.2*0.7;
            }
            jsonObject.put("Taxes",decimalFormat.format(Taxes));
            jsonObject.put("taxIncome",decimalFormat.format(feeByMonth-Taxes));
            jsonObject.put("feebysum",bookInfo.getFeebysum());
            result.add(jsonObject);
        }
        return result;
    }

    @Override
    public JXResult ReReview(int bookId, String chapterName) {
        //先根据书id找到对象
        BookInfo bookInfo = bookInfoMapper.selectByPrimaryKey(bookId);
        //根据书名找到创作书
        AuditBook auditBookBySelect = new AuditBook();
        auditBookBySelect.setBookName(bookInfo.getBookName());
        AuditBook auditBook = auditBookMapper.selectOne(auditBookBySelect);
        auditBook.setAuditStatus(2);
        auditBookMapper.updateByPrimaryKeySelective(auditBook);//取到这本创作书,将它的状态改为有驳回章节
        //拿到该章节,获取内容后删除,把内容转为驳回的创作中章节
        ChapterInfo chapterInfoBySelect = new ChapterInfo();
        chapterInfoBySelect.setBookId(bookId);
        chapterInfoBySelect.setChapterName(chapterName);
        ChapterInfo chapterInfo = chapterInfoMapper.selectOne(chapterInfoBySelect);

        AuditChapter auditChapter = new AuditChapter();
        auditChapter.setBookId(auditBook.getId());
        auditChapter.setOpinionsynopsis("已上线的章节驳回重审");
        auditChapter.setDraftStatus(2);
        auditChapter.setCreateTime(new Date());
        auditChapter.setUpdateTime(new Date());
        auditChapter.setChapterName(chapterName);
        auditChapter.setWords(chapterInfo.getWords());
        auditChapter.setIsFree(chapterInfo.getIsFree());
        auditChapter.setChapterId(chapterInfo.getChapterId());
        int i = auditChapterMapper.insertSelective(auditChapter);
        if (i<0){
            return new JXResult(false, ApiConstant.StatusCode.ERROR, "请求失败", "章节添加失败");
        }

        //到这里就是创作章节添加成功了
        ////////////////////////////////////////////////////
        //从阿里云 获取创作章节内容,并替换到创作
        String BookOss=bookId+"";//"20202121"+bookId;
        final String URL = String.format("%s/%s/%s/%s.txt", ApiConstant.Config.BASE_PATH, "booktxt", BookOss, chapterInfo.getChapterId());//正式的chapterid应该和创作的chapterid一样
        String bookContent ="";
        try {
            bookContent = HttpClientUtil.doGet(URL);
        } catch (Exception e) {
            logger.info("获取正式章节内容网络超时1次");
            try {
                bookContent = HttpClientUtil.doGet(URL);
            } catch (Exception e1) {
                logger.info("获取正式章节内容网络超时2次");
                bookContent = HttpClientUtil.doGet(URL);
            }
        }

        //将内容上传到阿里云
        String authorBookOss="20202121"+auditBook.getId();
        aliyunOSSUtil.stringToTxtAndUploadOSS(Integer.parseInt(authorBookOss) , chapterInfo.getChapterId(), bookContent);
        logger.info("文本上传成功了");

        //上传后,减去转成创作的章节字数
        bookInfo.setWords(bookInfo.getWords()-chapterInfo.getWords());
        bookInfoMapper.updateByPrimaryKeySelective(bookInfo);

        //删除这个正式章节,并且删除阿里云的内容
        //从OSS删除章节的内容
        //由于删除后会改变id,但是咪咕是根据id判断更新的,所以在这里
        /*String BookId=Integer.toString(bookId);
        String objectName = String.format("booktxt/%s/%s.txt", BookId, chapterInfo.getChapterId());
        aliyunOSSUtil.delete(objectName);

        //进行章节的删除操作
        chapterInfoMapper.deleteByPrimaryKey(chapterInfo.getId());*/
        ///////////////////////////////////////////////////
        return new JXResult(true, ApiConstant.StatusCode.OK, "请求成功", "通信成功");
    }

    @Override
    public JSONArray getFeeDetailByAdmin() throws ParseException {
        List<Integer> bookids;
        JSONArray result=new JSONArray();
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        //SELECT * FROM chapter_info WHERE create_time BETWEEN '2020-09-01 00:00:00' and '2020-09-30 23:59:59'
        // 编辑查询上个月稿酬信息(在外提供时间参数)
        //获取上个月1号的0点
        //获取上个月月末的23点59:59
        String todayString = MM.format(new Date());//得到今天的时间
        String thatTimeString=todayString+"-01 00:00:00";//得到现在月初时间

        Calendar cal = Calendar.getInstance();
        cal.setTime(simpleDateFormat.parse(thatTimeString));//设置时间
        cal.add(Calendar.MONTH, -1);//在当前时间基础上减去一个月,得到上月月初时间
        Date startTime = cal.getTime();

        cal.setTime(simpleDateFormat.parse(thatTimeString));//再次设置时间
        cal.add(Calendar.SECOND, -1);//在当前时间基础上减一秒,得到上月月末时间
        Date endTime = cal.getTime();
        String endTimeEndString = MM.format(endTime);
        bookids=auditChapterMapper.getLastMonthInformation(startTime,endTime);//找到在上个月有创作的章节,并且返回书id
        for (Integer bookid :bookids){
            JSONObject jsonObject = new JSONObject();
            BookInfo bookInfo = bookInfoMapper.selectByPrimaryKey(bookid);//查询到这本书
            //获取字数信息
            Integer wordsByMonth=0;
            Integer wordsByMonthResul= chapterInfoMapper.getWordsByMonth(bookInfo.getId(),startTime,endTime);
            if (null !=wordsByMonthResul){
                wordsByMonth=wordsByMonthResul;
            }
            //如果这本书在创作中有章节的话确认
            AuditBook auditBookBySelect = new AuditBook();
            auditBookBySelect.setBookName(bookInfo.getBookName());
            AuditBook auditBook;  //先获取这本书在创作中的书id
            if (null != auditBookMapper.selectOne(auditBookBySelect)){
                auditBook=auditBookMapper.selectOne(auditBookBySelect);
                if (null !=auditChapterMapper.selectByNickNameAndTimeInterval(auditBook.getId(),startTime,endTime)){
                    wordsByMonth+=auditChapterMapper.selectByNickNameAndTimeInterval(auditBook.getId(),startTime,endTime);
                }
            }

            AllowFree allowFreeByselectCreateyearandmonthSelect = new AllowFree();
            allowFreeByselectCreateyearandmonthSelect.setBookname(bookInfo.getBookName());
            allowFreeByselectCreateyearandmonthSelect.setCreatyearandmonth(endTimeEndString);
            AllowFree allowFreeByselectCreateyearandmonth=allowFreeMapper.selectOne(allowFreeByselectCreateyearandmonthSelect);
            //如果主编点过确认,就会有已经确认过的字数,就先把字数存在chapterid里面
            if (null !=allowFreeByselectCreateyearandmonth){
                if(null!=allowFreeByselectCreateyearandmonth.getAuthor() &&null!=allowFreeByselectCreateyearandmonth.getChapterid()){
                    wordsByMonth=allowFreeByselectCreateyearandmonth.getChapterid();
                }
                if (null==allowFreeByselectCreateyearandmonth.getChapterid()){//主编在查询之后,如果没有确认该月字数,就暂存在bookid里面
                    allowFreeByselectCreateyearandmonth.setBookid(wordsByMonth);
                    allowFreeMapper.updateByPrimaryKeySelective(allowFreeByselectCreateyearandmonth);
                }
            }
            jsonObject.put("wordsByMonth",wordsByMonth);//这个月的字数
            jsonObject.put("bookName",bookInfo.getBookName());
            jsonObject.put("creatyearandmonth",endTimeEndString);//年月时间
            Integer feebysum=0;
            Integer reward=0;
            Integer share=0;
            //如果千字稿费已经设置过了
            AllowFree allowFreeByselectOne = new AllowFree();
            allowFreeByselectOne.setBookname(bookInfo.getBookName());
            //AllowFree allowFree = allowFreeMapper.selectOne(allowFreeByselectOne);
            //AllowFree allowFree = allowFreeMapper.selectIsHaveByBooknameAndFeebysumIsNotZero(bookInfo.getBookName());
            AllowFree allowFree = allowFreeMapper.selectIsHaveByBooknameAndcreatyearandmonthIsNull(bookInfo.getBookName());
            if (null!=allowFree){
                feebysum=allowFree.getFeebysum();
            }

            //如果这本书当月的相关内容已经设置过了

            if (null !=allowFreeByselectCreateyearandmonth){
                reward=allowFreeByselectCreateyearandmonth.getReward();
                share=allowFreeByselectCreateyearandmonth.getShare();
            }
            jsonObject.put("feebysum",feebysum);

            double originalFeeByMonth=wordsByMonth*feebysum;//原始稿费
            double feeByMonth=originalFeeByMonth/1000;//计算税前稿费
            jsonObject.put("feeByMonth",decimalFormat.format(feeByMonth));
            jsonObject.put("reward",reward);
            jsonObject.put("share",share);
            double total=feeByMonth+reward+share;
            jsonObject.put("total",decimalFormat.format(total));
            //计算税率
            double Taxes;
            if (total<=800){
                Taxes=0;
            }else if (total<=4000){
                Taxes=(total-800)*0.2*0.7;
            }else {
                Taxes=total*0.8*0.2*0.7;
            }
            jsonObject.put("Taxes",decimalFormat.format(Taxes));//税费
            jsonObject.put("taxIncome",decimalFormat.format(total-Taxes));//税后收入
            boolean isTrue=true;
            AllowFree allowFreeByselect = new AllowFree();
            allowFreeByselect.setCreatyearandmonth(endTimeEndString);
            allowFreeByselect.setBookname(bookInfo.getBookName());
            allowFreeByselect.setAuthor(bookInfo.getAuthor());
            if (null !=allowFreeMapper.selectOne(allowFreeByselect)){
                isTrue=false;
            }
            jsonObject.put("isTrue",isTrue);
            jsonObject.put("author",bookInfo.getAuthor());
            HashMap<String,Integer> hashMap = chapterInfoMapper.selectMinToIdMaxToBookIdOnTimeInterval(bookid, startTime, endTime);
            jsonObject.put("interval",hashMap.get("minsort")+"-"+hashMap.get("maxsort"));//区间,从哪章到哪章
            result.add(jsonObject);
        }
        return result;
    }

    @Override
    public JSONArray getFeeDetailByAuthor(String nickName) throws ParseException {
        JSONArray resultArray = new JSONArray();
        //查询list,所有包含这个作者名的
        AllowFree allowFreeBySelect = new AllowFree();
        allowFreeBySelect.setAuthor(nickName);
        List<AllowFree> select = allowFreeMapper.select(allowFreeBySelect);
        if (select.size()<1){ return null; }
        AllowFree allowFreeByGetBookName = select.get(0);
        Integer feebysum = allowFreeMapper.selectIsHaveByBooknameAndcreatyearandmonthIsNull(allowFreeByGetBookName.getBookname()).getFeebysum();//根据书名获取千字稿费
        if (0==feebysum){ return null; }

        DecimalFormat decimalFormat = new DecimalFormat("#0.000");

        for (AllowFree allowFree:select){//查询出千字稿酬信息后遍历这个list,计算添加之后加入到返回结果中
            String creatyearandmonth = allowFree.getCreatyearandmonth();//开始时间是这个年月的1号0点
            String startTimeString=creatyearandmonth+"-01 00:00:00";//得到月初时间String
            Date startTime=simpleDateFormat.parse(startTimeString);//得到月初时间
            //解术时间是这个年月加一个月减一秒
            Calendar cal = Calendar.getInstance();
            cal.setTime(simpleDateFormat.parse(startTimeString));//设置时间
            cal.add(Calendar.MONTH, 1);//在开始时间基础上加一个月,得到下月月初时间
            cal.add(Calendar.SECOND,-1);//在当前时间基础上减一秒,得到上月月末时间
            Date endTime = cal.getTime();

            JSONObject jsonObject = new JSONObject();
            BookInfo bookInfoByselect = new BookInfo();
            bookInfoByselect.setBookName(allowFree.getBookname());
            BookInfo bookInfo = bookInfoMapper.selectOne(bookInfoByselect);//查询到这本书
            //获取字数信息
            Integer wordsByMonth=0;
            Integer wordsByMonthResul= chapterInfoMapper.getWordsByMonth(bookInfo.getId(),startTime,endTime);
            if (null !=wordsByMonthResul){
                wordsByMonth=wordsByMonthResul;
            }
            //如果这本书在创作中有章节的话
            AuditBook auditBookBySelect = new AuditBook();
            auditBookBySelect.setBookName(bookInfo.getBookName());
            AuditBook auditBook;  //先获取这本书在创作中的书id
            if (null != auditBookMapper.selectOne(auditBookBySelect)){
                auditBook=auditBookMapper.selectOne(auditBookBySelect);
                if (null !=auditChapterMapper.selectByNickNameAndTimeInterval(auditBook.getId(),startTime,endTime)){
                    wordsByMonth+=auditChapterMapper.selectByNickNameAndTimeInterval(auditBook.getId(),startTime,endTime);
                }
            }
            if (wordsByMonth==0){ continue; }
            if (null!=allowFree.getChapterid()){//针对新需求:主编点确认后不再变动字数
                wordsByMonth=allowFree.getChapterid();
            }
            jsonObject.put("wordsByMonth",wordsByMonth);
            jsonObject.put("bookName",bookInfo.getBookName());
            jsonObject.put("creatyearandmonth",allowFree.getCreatyearandmonth());

            Integer reward=allowFree.getReward();
            Integer share=allowFree.getShare();

            jsonObject.put("feebysum",feebysum);

            double originalFeeByMonth=wordsByMonth*feebysum;//原始稿费
            double feeByMonth=originalFeeByMonth/1000;//计算税前稿费
            jsonObject.put("feeByMonth",decimalFormat.format(feeByMonth));
            jsonObject.put("reward",reward);
            jsonObject.put("share",share);
            double total=feeByMonth+reward+share;
            jsonObject.put("total",decimalFormat.format(total));
            //计算税率
            double Taxes;
            if (total<=800){
                Taxes=0;
            }else if (total<=4000){
                Taxes=(total-800)*0.2*0.7;
            }else {
                Taxes=total*0.8*0.2*0.7;
            }
            jsonObject.put("Taxes",decimalFormat.format(Taxes));//税费
            jsonObject.put("taxIncome",decimalFormat.format(total-Taxes));//税后收入
            jsonObject.put("author",nickName);
            resultArray.add(jsonObject);
        }

        return resultArray;
    }

    @Override
    public JXResult confirm(String creatyearandmonth, String bookname,String author) {
        //能点确认,说明没有 书名、当前年月信息,和作者名都有的数据
        AllowFree allowFreeByInsert = new AllowFree();
        allowFreeByInsert.setBookname(bookname);
        allowFreeByInsert.setCreatyearandmonth(creatyearandmonth);
        AllowFree allowFree = allowFreeMapper.selectOne(allowFreeByInsert);
        if (null==allowFree){//暂时保留,什么都没有点确定干什么
           // allowFree.setAuthor(author);
            //allowFreeMapper.insertSelective(allowFree);
            return new JXResult(false,ApiConstant.StatusCode.ERROR,"操作失败!","防止误点,先修改再确认");
        }
        if (null !=allowFree.getBookid()){
            allowFree.setChapterid(allowFree.getBookid());//redis暂时用不了,先用其它字段代替redis
        }

        allowFree.setAuthor(author);
        int result = allowFreeMapper.updateByPrimaryKeySelective(allowFree);
        if (result<0){
return new JXResult(false,ApiConstant.StatusCode.ERROR,"操作失败!","数据库操作失败,请检查原因");
        }
        return new JXResult(true,ApiConstant.StatusCode.OK,"操作成功","作者可以查询到"+bookname+"在"+creatyearandmonth+"的稿酬信息了");
    }

    @Override
    public JXResult modifyFee(String creatyearandmonth, String bookname, Integer feebysum, Integer reward, Integer share) {
        AllowFree allowFreeBySelectFeebyusum = new AllowFree();//修改价格是修改书的
        allowFreeBySelectFeebyusum.setBookname(bookname);
        AllowFree isHave= allowFreeMapper.selectIsHaveByBooknameAndcreatyearandmonthIsNull(bookname);
        int i;
        if (null==isHave){
            allowFreeBySelectFeebyusum.setFeebysum(feebysum);
            i = allowFreeMapper.insertSelective(allowFreeBySelectFeebyusum);
        }else {
            isHave.setFeebysum(feebysum);
            i = allowFreeMapper.updateByPrimaryKeySelective(isHave);
        }
        if (i<0){
            return new JXResult(false,ApiConstant.StatusCode.ERROR,"千字价格修改失败","千字价格修改失败");
        }

        AllowFree allowFreeBySelectRewardAndShare = new AllowFree();//修改奖励和分成是修改月的
        allowFreeBySelectRewardAndShare.setBookname(bookname);
        allowFreeBySelectRewardAndShare.setCreatyearandmonth(creatyearandmonth);
        AllowFree allowFreeByIsHave = allowFreeMapper.selectOne(allowFreeBySelectRewardAndShare);
        if (null==allowFreeByIsHave){
            allowFreeBySelectRewardAndShare.setReward(reward);
            allowFreeBySelectRewardAndShare.setShare(share);
            int i1 = allowFreeMapper.insertSelective(allowFreeBySelectRewardAndShare);
            if (i1<0){
                return new JXResult(false,ApiConstant.StatusCode.ERROR,"添加失败","添加失败,时间错误!");
            }
        }else {
            allowFreeByIsHave.setShare(share);
            allowFreeByIsHave.setReward(reward);
            int i1 = allowFreeMapper.updateByPrimaryKeySelective(allowFreeByIsHave);
            if (i1<0){
                return new JXResult(false,ApiConstant.StatusCode.ERROR,"修改失败","修改失败,奖励或者分成错误!");
            }
        }
        return new JXResult(true,ApiConstant.StatusCode.OK,"修改成功","修改成功");
    }

    @Override
    public JXResult queryUnread(String authorName) {
        Notice earliest= noticeMapper.queryUnreadByAuthorNickName(authorName);
        return new JXResult(true,ApiConstant.StatusCode.OK,"成功",earliest);
    }

    @Override
    public JXResult callBack(Integer id) {
        Notice notice = noticeMapper.selectByPrimaryKey(id);
        notice.setIsreceive("yes");
        notice.setReceivetime(new Date());
        noticeMapper.updateByPrimaryKeySelective(notice);
        return new JXResult(true,ApiConstant.StatusCode.OK,"成功");
    }

    @Override
    public boolean replaceAuditToPutshelf(AuditChapter auditChapterBySelectChapterId) {
        //如果正式章节已经有这一章了,就将内容替换到正式章节,把审核章节的内容和数据库对象都删掉
        Integer chapterId = auditChapterBySelectChapterId.getChapterId();
        ChapterInfo chapterInfoByselect = new ChapterInfo();
        chapterInfoByselect.setChapterName(auditChapterBySelectChapterId.getChapterName());
        chapterInfoByselect.setChapterId(chapterId);//这个名字有点怪!不是id,是sort的意思!
        List<ChapterInfo> infoList = chapterInfoMapper.select(chapterInfoByselect);
        if (infoList.size()!=1){
            return false;
        }else {
            //获得两处的内容,进行替换
            ChapterInfo chapterInfo = infoList.get(0);

            BookInfo bookInfo = bookInfoMapper.selectByPrimaryKey(chapterInfo.getBookId());
            //在去OSS中拿章节内容的信息
            String authorBookOss="20202121"+auditChapterBySelectChapterId.getBookId();
            final String authorURL = String.format("%s/%s/%s/%s.txt", ApiConstant.Config.BASE_PATH, "booktxt", authorBookOss, chapterId);
            String authorBookContent ="";
            try {
                authorBookContent = HttpClientUtil.doGet(authorURL);
            } catch (Exception e) {
                logger.info("获取创作章节内容网络超时1次");
                try {
                    authorBookContent = HttpClientUtil.doGet(authorURL);
                } catch (Exception e1) {
                    logger.info("获取创作章节内容网络超时2次");
                    authorBookContent = HttpClientUtil.doGet(authorURL);
                }
            }
            Integer ossBookId=chapterInfo.getBookId();
            //先把正式os的内容删除
            String ossName = String.format("booktxt/%s/%s.txt", ossBookId, chapterId);
            aliyunOSSUtil.delete(ossName);
            //将内容上传到阿里云

            aliyunOSSUtil.stringToTxtAndUploadOSS(ossBookId, chapterId, authorBookContent);

            //从审核上传到正式后,更新字数
            bookInfo.setWords(bookInfo.getWords()+auditChapterBySelectChapterId.getWords()-chapterInfo.getWords());
            Integer maxSort = chapterInfoMapper.selectMaxSortByBookId(bookInfo.getId());
            bookInfo.setLastChapterId(maxSort);//更新最新章节
            bookInfo.setUpdateTime(new Date());//更新书籍的修改时间
            bookInfoMapper.updateByPrimaryKeySelective(bookInfo);
            chapterInfo.setWords(auditChapterBySelectChapterId.getWords());
            chapterInfoMapper.updateByPrimaryKeySelective(chapterInfo);//更新对象章节的字数

            //删除这个创作章节,并且删除阿里云的内容
            //从OSS删除章节的内容
            String authorBookId="20202121"+auditChapterBySelectChapterId.getBookId();
            String objectName = String.format("booktxt/%s/%s.txt", authorBookId, chapterId);
            aliyunOSSUtil.delete(objectName);

            //进行章节的删除操作
            auditChapterMapper.deleteByPrimaryKey(auditChapterBySelectChapterId.getId());

            //到这里就是完全执行了,减去审核书籍的字数
            AuditBook auditBook = auditBookMapper.selectByPrimaryKey(auditChapterBySelectChapterId.getBookId());
            Integer auditBookWords = auditBook.getWords();
            auditBook.setWords(auditBookWords-auditChapterBySelectChapterId.getWords());
            auditBookMapper.updateByPrimaryKeySelective(auditBook);
        }
        return true;
    }


}
