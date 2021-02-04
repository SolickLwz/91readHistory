package com.nine.one.yuedu.read.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nine.one.yuedu.read.config.ApiConstant;
import com.nine.one.yuedu.read.config.JXResult;
import com.nine.one.yuedu.read.entity.BookInfo;
import com.nine.one.yuedu.read.entity.ChapterInfo;
import com.nine.one.yuedu.read.entity.CpAuth;
import com.nine.one.yuedu.read.entity.CpAuthBook;
import com.nine.one.yuedu.read.entity.vo.CpAuthBookVO;
import com.nine.one.yuedu.read.mapper.BookInfoMapper;
import com.nine.one.yuedu.read.mapper.ChapterInfoMapper;
import com.nine.one.yuedu.read.mapper.CpAuthBookMapper;
import com.nine.one.yuedu.read.mapper.CpAuthMapper;
import com.nine.one.yuedu.read.service.CpAuthService;
import com.nine.one.yuedu.read.utils.HttpClientUtil;
import com.nine.one.yuedu.read.utils.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(value = "cpAuthService")
public class CpAuthServiceImpl implements CpAuthService {

    @Autowired
    private CpAuthMapper cpAuthMapper;
    @Autowired
    private CpAuthBookMapper cpAuthBookMapper;
    @Autowired
    private BookInfoMapper bookInfoMapper;

    @Autowired
    private ChapterInfoMapper chapterInfoMapper;


    @Override
    public void addCpAuth(CpAuth cpAuth) {
        cpAuth.setCpKey(UUIDUtil.getUUID());
        cpAuthMapper.insertSelective(cpAuth);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delCpAuthById(Integer id) {
        //首先删除授权给这个CP的所有书
        Example example = new Example(CpAuthBook.class);
        example.createCriteria().andEqualTo("cpAuthId", id);
        cpAuthBookMapper.deleteByExample(example);
        //删除CP
        cpAuthMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<CpAuth> getCpAuthList() {
        return cpAuthMapper.selectAll();
    }

    @Override
    public PageInfo<CpAuthBookVO> getCpAuthBookList(Integer cpAuthId, Integer pageIndex, Integer pageSize) {
        //先查询出BookId
        PageHelper.startPage(pageIndex, pageSize);

        List<CpAuthBookVO> cpAuthBookVOList = cpAuthBookMapper.selectBookListByCpAuthId(cpAuthId);

        PageInfo<CpAuthBookVO> pageInfo = new PageInfo<>(cpAuthBookVOList);
        return pageInfo;
    }

    @Override
    public List<CpAuthBook> getCpAuthBookIdList(Integer cpAuthId) {
        Example exampleCpAuthBook = new Example(CpAuthBook.class);
        exampleCpAuthBook.createCriteria().andEqualTo("cpAuthId", cpAuthId);
        List<CpAuthBook> cpAuthBooks = cpAuthBookMapper.selectByExample(exampleCpAuthBook);
        return cpAuthBooks;
    }

    @Override
    public JXResult addCpAuthBook(Integer cpAuthId, String bookIds) {
        JXResult jxResult = new JXResult();
        //先查询出所有书的ID
        List<Integer> bookIdEds = cpAuthBookMapper.getBookIdListByCpAuthId(cpAuthId);
        //比较是否存在
        if (StringUtils.isNotBlank(bookIds)) {
            try {
                String[] split = bookIds.split(",");
                List<CpAuthBook> addList = new ArrayList<>();
                for (String bookId : split) {
                    if (!bookIdEds.contains(Integer.valueOf(bookId))) {
                        CpAuthBook cpAuthBook = new CpAuthBook();
                        cpAuthBook.setCpAuthId(cpAuthId);
                        cpAuthBook.setBookId(Integer.valueOf(bookId));
                        addList.add(cpAuthBook);
                    }
                }
                if (addList != null && addList.size() > 0) {
                    cpAuthBookMapper.insertList(addList);
                }
            } catch (Exception e) {
                e.printStackTrace();
                jxResult.setFlag(false);
                jxResult.setCode(ApiConstant.StatusCode.SERVERERROR);
                jxResult.setMessage("您输入的格式有误");
                return jxResult;
            }
        }
        jxResult.setFlag(true);
        jxResult.setCode(ApiConstant.StatusCode.OK);
        jxResult.setMessage("添加成功");
        return jxResult;
    }

    @Override
    public void deleteCpAuthBook(Integer id) {
        cpAuthBookMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Map<String, Object> getBookInfoByBookId(Integer bookId) {
        //书籍ID,书名,作者,类型,字数,是否连载,封面图,简介
        BookInfo bookInfo = bookInfoMapper.selectByPrimaryKey(bookId);
        Map<String, Object> result = new HashMap<>();
        result.put("bookId", bookInfo.getId());
        result.put("bookName", bookInfo.getBookName());
        result.put("author", bookInfo.getAuthor());
        result.put("category", bookInfo.getCategory());
        result.put("words", bookInfo.getWords());
        result.put("status", bookInfo.getCompleteState());
        result.put("bookImage", bookInfo.getPicUrl());
        result.put("description", bookInfo.getDescription());
        return result;
    }

    @Override
    public List<ChapterInfo> getChapterInfoList(Integer bookId) {
        Example example = new Example(ChapterInfo.class);
        example.createCriteria().andEqualTo("bookId", bookId);
        List<ChapterInfo> chapterInfos = chapterInfoMapper.selectByExample(example);
        return chapterInfos;
    }

    @Override
    public String getChapterContentByBookIdAndChapterId(Integer bookId, Integer chapterId) {
        //在去OSS中拿章节内容的信息
        final String URL = String.format("%s/%s/%s/%s.txt", ApiConstant.Config.BASE_PATH, "booktxt", bookId, chapterId);
        String bookContent = HttpClientUtil.doGet(URL);
        return bookContent;
    }

    /**
     * 检查时间戳,参数,key是否一致
     *
     * @param request
     */
    @Override
    public JXResult checkKeyAndParam(HttpServletRequest request) {
        JXResult jxResult = new JXResult();
        jxResult.setFlag(false);
        jxResult.setCode(ApiConstant.StatusCode.ACCESSERROR);
        //获取时间戳
        String timestamp = request.getParameter("timestamp");
        System.out.println(timestamp);
        if (StringUtils.isBlank(timestamp)) {
            jxResult.setMessage("请求失败,时间戳不能为空");
            return jxResult;
        }
        //程序执行至此说明时间戳不为空
        String key = request.getParameter("key");
        if (StringUtils.isBlank(key)) {
            jxResult.setMessage("请求失败,key不能为空");
            return jxResult;
        }
        //获取签名
        String sign = request.getParameter("sign");
        if (StringUtils.isBlank(sign)) {
            jxResult.setMessage("请求失败,签名不能为空");
            return jxResult;
        }
        //对比时间戳
        if (Math.abs(System.currentTimeMillis() - Long.parseLong(timestamp)) > 10 * 60 * 1000) {
            jxResult.setMessage("请求失败,时间戳有误");
            return jxResult;
        }
        //检验签名
        Example example = new Example(CpAuth.class);
        example.createCriteria().andEqualTo("cpKey", key);
        CpAuth cpAuth = cpAuthMapper.selectOneByExample(example);
        if (cpAuth == null) {
            jxResult.setMessage("请求失败,key有误");
            return jxResult;
        } else {
            //校验SIGN
            String digest = DigestUtils.md5DigestAsHex((cpAuth.getId() + cpAuth.getCpKey()).getBytes());
            System.out.println("正确的签名" + digest);
            System.out.println("传来的签名" + sign);
            if (sign.toLowerCase().equals(digest)) {
                jxResult.setMessage("校验成功");
                jxResult.setCode(ApiConstant.StatusCode.OK);
                jxResult.setFlag(true);
                return jxResult;
            }
        }
        jxResult.setFlag(false);
        jxResult.setMessage("请求失败,签名校验失败");
        return jxResult;

    }
}
