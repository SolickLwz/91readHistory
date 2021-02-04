package com.nine.one.yuedu.read.cp.impl;

import com.nine.one.yuedu.read.config.ApiConstant;
import com.nine.one.yuedu.read.cp.MiGuService;
import com.nine.one.yuedu.read.entity.BookInfo;
import com.nine.one.yuedu.read.entity.ChapterInfo;
import com.nine.one.yuedu.read.entity.cp.MiGuBookInfo;
import com.nine.one.yuedu.read.entity.cp.MiGuChapter;
import com.nine.one.yuedu.read.entity.cp.MiGuResult;
import com.nine.one.yuedu.read.entity.vo.CpAuthBookVO;
import com.nine.one.yuedu.read.mapper.BookInfoMapper;
import com.nine.one.yuedu.read.mapper.ChapterInfoMapper;
import com.nine.one.yuedu.read.mapper.CpAuthBookMapper;
import com.nine.one.yuedu.read.utils.HttpClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.*;

@Service(value = "miGuService")
public class MiGuServiceImpl implements MiGuService {

    @Autowired
    private CpAuthBookMapper cpAuthBookMapper;
    @Autowired
    private BookInfoMapper bookInfoMapper;
    @Autowired
    private ChapterInfoMapper chapterInfoMapper;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public List<Map<String, String>> getBookList(String appId) {
        appId = "8";
        List<Map<String, String>> list = new ArrayList<>();
        List<CpAuthBookVO> cpAuthBookVOS = cpAuthBookMapper.selectBookListByCpAuthId(Integer.valueOf(appId));
        for (CpAuthBookVO cpAuthBookVO : cpAuthBookVOS) {
            Map<String, String> map = new HashMap<>();
            map.put("id", cpAuthBookVO.getBookId() + "");
            map.put("name", cpAuthBookVO.getBookName());
            list.add(map);
        }
        logger.info("咪咕获取书籍列表");
        return list;
    }

    @Override
    public MiGuBookInfo getBookInfo(String bookId) {
        BookInfo bookInfo = bookInfoMapper.selectByPrimaryKey(bookId);
        MiGuBookInfo miGuBookInfo = new MiGuBookInfo();
        miGuBookInfo.setId(bookInfo.getId() + "");
        miGuBookInfo.setName(bookInfo.getBookName());
        miGuBookInfo.setCover(bookInfo.getPicUrl());
        miGuBookInfo.setAuthor(bookInfo.getAuthor());
        miGuBookInfo.setTags(bookInfo.getKeywords());
        miGuBookInfo.setBookseries("1");
        miGuBookInfo.setIntro(bookInfo.getDescription());
        String bookName = bookInfo.getBookName();
        if (bookName.length() > 8) {
            bookName = bookName.substring(0, 8);
        }
        miGuBookInfo.setNominateinfor("精品推荐：" + bookName);
        miGuBookInfo.setLongdescription(bookInfo.getDescription());
        miGuBookInfo.setShortdescription(bookInfo.getDescription());
        miGuBookInfo.setPublishflag("否");
       // miGuBookInfo.setFinishflag("是");//这里统统写死的,要修改,
        Integer completeState = bookInfo.getCompleteState();
        String Chargeflag;
        if (0==completeState){
            Chargeflag="是";
        }else {
            Chargeflag="否";
        }
        miGuBookInfo.setFinishflag(Chargeflag);
        miGuBookInfo.setChargeflag("是");
        miGuBookInfo.setChargeway("单章收费");
        miGuBookInfo.setInfopricesuggest("10");
        miGuBookInfo.setMcpchargefristnum("21");
        miGuBookInfo.setLastestChapter(bookInfo.getLastChapterId() + "");
        miGuBookInfo.setLastestChapterId(bookInfo.getLastChapterId() + "");
        miGuBookInfo.setType(getType(bookInfo.getCategory()));
        miGuBookInfo.setSubType(bookInfo.getCategory());
        miGuBookInfo.setPublisherdate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        logger.info("咪咕获取书籍详情:" + bookInfo.getBookName());
        return miGuBookInfo;
    }

    @Override
    public List<Map<String, String>> getVolumeList(String bookId) {
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("id", bookId + "1");
        map.put("name", "第一卷 正文");
        list.add(map);
        logger.info("咪咕获取书籍卷" + bookId);
        return list;
    }

    @Override
    public List<MiGuChapter> getChapterList(String bookId) {
        List<MiGuChapter> miGuChapters = new ArrayList<>();
        Example example = new Example(ChapterInfo.class);
        example.createCriteria().andEqualTo("bookId", bookId);
        List<ChapterInfo> chapterInfos = chapterInfoMapper.selectByExample(example);
        for (ChapterInfo chapterInfo : chapterInfos) {
            MiGuChapter miGuChapter = new MiGuChapter();
            miGuChapter.setId(chapterInfo.getId() + "");
            miGuChapter.setName(chapterInfo.getChapterName());
            miGuChapter.setSortNo(chapterInfo.getChapterId());
            miGuChapter.setVolumeId(bookId + "1");
            miGuChapters.add(miGuChapter);
        }
        logger.info("咪咕获取书籍章节列表" + bookId);
        return miGuChapters;
    }

    @Override
    public Map<String, Object> getChapterInfo(String bookId, String chapterId) {
        ChapterInfo chapterInfo = chapterInfoMapper.selectByPrimaryKey(chapterId);
        final String BOOK_URL = String.format("%s/%s/%s/%s.txt", ApiConstant.Config.BASE_PATH, "booktxt", bookId, chapterInfo.getChapterId());
        String bookContent = HttpClientUtil.doGet(BOOK_URL);
        Map<String, Object> map = new HashMap<>();
        map.put("id", chapterInfo.getId() + "");
        map.put("name", chapterInfo.getChapterName());
        map.put("wordNumber", chapterInfo.getWords());
        map.put("content", bookContent);
        logger.info("咪咕获取书籍章节内容:" + bookId + "\t章节ID:" + chapterId);
        return map;
    }

    @Override
    public MiGuResult checkSign(String appId, String sign) {
        MiGuResult result = new MiGuResult();
        if (!"yd0084".equals(appId)) {
            result.setStatusCode(500);
            result.setMessage("授权appId有误,请重试");
            return result;
        }
        if (StringUtils.isBlank(sign) || sign.length() < 10) {
            result.setStatusCode(500);
            result.setMessage("签名有误,请重试");
            return result;
        }
        result.setStatusCode(0);
        return result;
    }

    private String getType(String category) {
        String type = "";
        switch (category) {
            case "现代言情":
            case "古代言情":
            case "幻想言情":
            case "豪门总裁":
            case "女生灵异":
            case "浪漫青春":
                type = "女频";
                break;
            case "悬疑灵异":
            case "历史军事":
            case "玄幻奇幻":
            case "仙侠武侠":
            case "都市生活":
            case "玄幻仙侠":
            case "科幻末世":
            case "现代都市":
            case "恐怖惊悚":
                type = "男频";
                break;
        }
        return type;
    }
}
