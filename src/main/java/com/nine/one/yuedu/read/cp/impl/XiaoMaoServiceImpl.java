package com.nine.one.yuedu.read.cp.impl;

import com.nine.one.yuedu.read.cp.XiaoMaoService;
import com.nine.one.yuedu.read.entity.BookInfo;
import com.nine.one.yuedu.read.entity.ChapterInfo;
import com.nine.one.yuedu.read.mapper.BookInfoMapper;
import com.nine.one.yuedu.read.mapper.ChapterInfoMapper;
import com.nine.one.yuedu.read.utils.AliyunOSSUtil;
import com.nine.one.yuedu.read.utils.HttpClientUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service(value = "xiaoMaoService")
public class XiaoMaoServiceImpl implements XiaoMaoService {


    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //定义小猫科技的cpID为1001
    public final int CP_ID = 1001;
    //获取书籍 ID 列表
    public final String BOOK_LIST_URL = "http://api.baiwancangshu.com/Book/Books";
    //获取图书信息的接口
    public String BOOK_INFO_URL = "http://api.baiwancangshu.com/Book/BookInfo/book_id/";
    //获取卷ID的接口
    public String VOLUMES_URL = "http://api.baiwancangshu.com/Book/Volumes/book_id/";
    //获取章节列表
    public String CHAPTER_LIST_URL = "http://api.baiwancangshu.com/Book/Chapters/book_id/BOOKID/volume_id/VOLUMEID";
    //获取章节内容接口
    public String CHAPTER_CONTENT_URL = "http://api.baiwancangshu.com/Book/ChapterInfo/book_id/BOOKID/chapter_id/CHAPTERID";


    @Autowired
    private BookInfoMapper bookInfoMapper;
    @Autowired
    private ChapterInfoMapper chapterInfoMapper;
    @Autowired
    private AliyunOSSUtil aliyunOSSUtil;



    @Override
    public void getBookList() {
        //首先获取书籍的ID列表
        String jsonBookIds = HttpClientUtil.doGet(BOOK_LIST_URL);
        //解析成数组
        JSONObject jsonObject = JSONObject.fromObject(jsonBookIds);
        Object books = jsonObject.get("books");
        JSONObject jsonBookList = JSONObject.fromObject(books);
        Object list = jsonBookList.get("list");
        JSONArray jsonArray = JSONArray.fromObject(list);
        //拿到书籍列表
        for (Object o : jsonArray) {
            JSONObject book = JSONObject.fromObject(o);
            Integer cpBookId = book.getInt("book_id");
            //查数据库,看这本书是否已经存在
            Example example = new Example(BookInfo.class);
            example.createCriteria().andEqualTo("cpId", CP_ID).andEqualTo("cpBookId", cpBookId);
            BookInfo bookInfo = bookInfoMapper.selectOneByExample(example);
            if (bookInfo == null) {
                //不存在直接抓取
                //获取图书信息
                BookInfo bookInfoNew = getBookInfoByCpBookId(cpBookId);
                //入库
                bookInfoMapper.insertSelective(bookInfoNew);
                logger.info("抓取" + bookInfoNew.getBookName() + "成功");
            } else {
                //判断这本书是不是连载书
                if (bookInfo.getCompleteState() == 1) {
                    //连载书的话更新
                    BookInfo bookUpdate = getBookInfoByCpBookId(cpBookId);
                    bookUpdate.setId(bookInfo.getId());
                    bookInfoMapper.updateByPrimaryKeySelective(bookUpdate);
                }
            }
        }
    }

    @Override
    public void getChapter() {
        //获取所以小猫的书
        Example example = new Example(BookInfo.class);
        example.createCriteria().andEqualTo("cpId", CP_ID);
        List<BookInfo> bookInfos = bookInfoMapper.selectByExample(example);
        if (bookInfos != null && bookInfos.size() > 0) {
            for (BookInfo bookInfo : bookInfos) {
                //请求数据
                String jsonVolumes = HttpClientUtil.doGet(VOLUMES_URL + bookInfo.getCpBookId());
                JSONObject jsonObject = JSONObject.fromObject(jsonVolumes);
                JSONArray jsonArray = jsonObject.getJSONObject("book").getJSONObject("volumes").getJSONArray("list");
                for (Object o : jsonArray) {
                    JSONObject volume = JSONObject.fromObject(o);
                    String jsonChapterList = HttpClientUtil.doGet(CHAPTER_LIST_URL.replace("BOOKID", bookInfo.getCpBookId()).replace("VOLUMEID", volume.getString("volume_id")));
                    logger.info("章节列表" + jsonChapterList);
                    JSONArray cpChapterList = JSONObject.fromObject(jsonChapterList).getJSONObject("book").getJSONObject("chapters").getJSONArray("list");
                    for (Object o1 : cpChapterList) {
                        JSONObject cpChapterInfo = JSONObject.fromObject(o1);
                        Example exampleChapter = new Example(ChapterInfo.class);
                        exampleChapter.createCriteria().andEqualTo("bookId", bookInfo.getId()).andEqualTo("cpChapterId", Integer.valueOf(cpChapterInfo.getString("chapter_id")));
                        ChapterInfo chapterInfoFlag = chapterInfoMapper.selectOneByExample(exampleChapter);
                        if (chapterInfoFlag == null) {
                            try {
                                ChapterInfo chapterInfo = new ChapterInfo();
                                chapterInfo.setBookId(bookInfo.getId());
                                chapterInfo.setChapterId(Integer.valueOf(cpChapterInfo.getString("chapter_id")));
                                chapterInfo.setChapterName(cpChapterInfo.getString("chapter_name"));
                                chapterInfo.setWords(Integer.valueOf(cpChapterInfo.getString("chapter_size")));
                                chapterInfo.setIsFree((byte) 0);
                                chapterInfo.setCreateTime(new Date());
                                chapterInfo.setUpdateTime(new Date());
                                chapterInfo.setCpChapterId(cpChapterInfo.getString("chapter_id"));
                                //没有被抓取过
                                chapterInfoMapper.insertSelective(chapterInfo);
                                logger.info("抓取:" + chapterInfo.getChapterName() + "成功");
                                //抓取章节的内容
                                String jsonContent = HttpClientUtil.doGet(CHAPTER_CONTENT_URL.replace("BOOKID", bookInfo.getCpBookId()).replace("CHAPTERID", chapterInfo.getCpChapterId()));
                                String content = JSONObject.fromObject(jsonContent).getJSONObject("chapter").getString("chapter_content");
                                aliyunOSSUtil.stringToTxtAndUploadOSS(bookInfo.getId(), chapterInfo.getChapterId(), content);
                                logger.info("章节上传成功");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                    }
                }
            }
        }

    }




    //获取图书详情
    private BookInfo getBookInfoByCpBookId(Integer cpBookId) {
        //请求数据
        String jsonBookInfo = HttpClientUtil.doGet(BOOK_INFO_URL + cpBookId);
        Object book_info = JSONObject.fromObject(jsonBookInfo).get("book_info");
        JSONObject cpBookInfo = JSONObject.fromObject(book_info);
        //创建我方的图书对象
        BookInfo bookInfo = new BookInfo();
        try {
            bookInfo.setId(bookInfoMapper.getCurrentMaxId() + 1);
            bookInfo.setBookName(cpBookInfo.getString("book_name"));
            bookInfo.setAuthor(cpBookInfo.getString("author_name"));
            bookInfo.setKeywords(cpBookInfo.getString("book_name"));
            bookInfo.setWords(Integer.valueOf(cpBookInfo.getString("word_size")));
            bookInfo.setCategory(cpBookInfo.getString("first_type_name"));
            bookInfo.setValid(1);
            bookInfo.setCompleteState(Integer.valueOf(cpBookInfo.getString("status")) == 1 ? 1 : 0);
            bookInfo.setLastChapterId(Integer.valueOf(cpBookInfo.getString("total_chapter")));
            bookInfo.setCreateTime(new Date());
            bookInfo.setUpdateTime(new Date());
            bookInfo.setPicUrl(cpBookInfo.getString("img_path_src"));
            bookInfo.setVisitCount(0);
            bookInfo.setOpenSearch(1);
            bookInfo.setProvider("小猫科技");
            bookInfo.setDescription(cpBookInfo.getString("brief"));
            bookInfo.setCpId(1001);
            bookInfo.setCpBookId(cpBookId + "");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookInfo;
    }


}
