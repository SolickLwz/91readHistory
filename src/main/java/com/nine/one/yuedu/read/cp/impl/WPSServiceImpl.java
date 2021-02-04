package com.nine.one.yuedu.read.cp.impl;

import com.alibaba.fastjson.JSON;
import com.nine.one.yuedu.read.config.ApiConstant;
import com.nine.one.yuedu.read.cp.WPSService;
import com.nine.one.yuedu.read.entity.BookInfo;
import com.nine.one.yuedu.read.entity.ChapterInfo;
import com.nine.one.yuedu.read.entity.cp.WPSBook;
import com.nine.one.yuedu.read.mapper.BookInfoMapper;
import com.nine.one.yuedu.read.mapper.ChapterInfoMapper;
import com.nine.one.yuedu.read.mapper.CpAuthBookMapper;
import com.nine.one.yuedu.read.utils.HttpClientUtil;
import com.nine.one.yuedu.read.utils.HttpClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(value = "wpsService")
public class WPSServiceImpl implements WPSService {

    public static final String PUSH_URL = "https://outside.wps.cn/novel_partner/api/v1/partner/push";

    public static final String Access_Id = "AKBOOKZHONGSHI";

    public static final String Secret_Id = "q2jv2Fq1uUYUC0hcmqu7lIFJfZPqs7jttblDonNeEAujdy7DJjutZe7QnOf1AhQr";

    @Autowired
    private CpAuthBookMapper cpAuthBookMapper;
    @Autowired
    private BookInfoMapper bookInfoMapper;
    @Autowired
    private ChapterInfoMapper chapterInfoMapper;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void pushBook() {
        //查到授权的书的列表
        List<Integer> bookIdList = cpAuthBookMapper.getBookIdListByCpAuthId(34870);
        String authorization = getAuthorization();

        for (Integer bookId : bookIdList) {
            BookInfo bookInfo = bookInfoMapper.selectByPrimaryKey(bookId);
            logger.info("书籍的名称是:" + bookInfo.getBookName());
            Example example = new Example(ChapterInfo.class);
            example.createCriteria().andEqualTo("bookId", bookId);
            List<ChapterInfo> chapterInfos = chapterInfoMapper.selectByExample(example);
            for (ChapterInfo chapterInfo : chapterInfos) {
                logger.info("章节的名字:" + chapterInfo.getChapterName());
                WPSBook wpsBook = new WPSBook();
                WPSBook.BooksBean booksBean = new WPSBook.BooksBean();
                booksBean.setId(bookInfo.getId());
                booksBean.setName(bookInfo.getBookName());
                booksBean.setDescription(bookInfo.getDescription());
                booksBean.setCover(bookInfo.getPicUrl());
                booksBean.setAuthor(bookInfo.getAuthor());
                booksBean.setTag(bookInfo.getBookName());
                booksBean.setCategory(getCategory(bookInfo.getCategory()));
                booksBean.setState(bookInfo.getCompleteState() == 0 ? 1 : 0);
                List<WPSBook.BooksBean> list = new ArrayList<>();
                list.add(booksBean);
                wpsBook.setBooks(list);
                WPSBook.ChaptersBean chaptersBean = new WPSBook.ChaptersBean();
                chaptersBean.setId(chapterInfo.getId());
                chaptersBean.setBook_id(chapterInfo.getBookId());
                chaptersBean.setTitle(chapterInfo.getChapterName());
                String URL = String.format("%s/%s/%s/%s.txt", ApiConstant.Config.BASE_PATH, "booktxt", bookId, chapterInfo.getChapterId());
                String bookContent = HttpClientUtil.doGet(URL);

                chaptersBean.setContent(bookContent);
                chaptersBean.setSort(chapterInfo.getChapterId());
                List<WPSBook.ChaptersBean> chaptersBeans = new ArrayList<>();
                chaptersBeans.add(chaptersBean);
                wpsBook.setChapters(chaptersBeans);
                //参数构建完毕,开始调用接口
                Map<String, String> headers = new HashMap<>();
                headers.put("Access-Id", Access_Id);
                headers.put("Authorization", authorization);
                //将参数转化为JSON
                String result = null;
                try {
                    result = HttpClientUtils.sendPostByJson(PUSH_URL, JSON.toJSONString(wpsBook), headers);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                logger.info(result);
            }
        }


    }

    private String getAuthorization() {
        return DigestUtils.md5DigestAsHex((Access_Id + Secret_Id).getBytes());
    }

    public String getCategory(String category) {
        switch (category) {
            case "现代言情":
            case "豪门总裁":
                return "现言";
            case "古代言情":
                return "古言";
            case "女生灵异":
            case "幻想言情":
                return "幻言";
            case "浪漫青春":
                return "校园";
            case "悬疑灵异":
                return "悬疑";
            case "历史军事":
                return "历史军事";
            case "玄幻奇幻":
                return "玄幻奇幻";
            case "仙侠武侠":
                return "仙侠武侠";
            case "都市生活":
                return "都市";
            case "玄幻仙侠":
                return "仙侠武侠";
            case "科幻末世":
                return "科幻";
            case "现代都市":
                return "都市";
            case "恐怖惊悚":
                return "悬疑";
        }
        return "都市";
    }
}
