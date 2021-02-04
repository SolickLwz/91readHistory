package com.nine.one.yuedu.read.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nine.one.yuedu.read.config.ApiConstant;
import com.nine.one.yuedu.read.config.FormateConstant;
import com.nine.one.yuedu.read.config.JXResult;
import com.nine.one.yuedu.read.entity.*;
import com.nine.one.yuedu.read.entity.vo.ChapterInfoVO;
import com.nine.one.yuedu.read.mapper.AuditBookMapper;
import com.nine.one.yuedu.read.mapper.AuditChapterMapper;
import com.nine.one.yuedu.read.mapper.UserAuthorMapper;
import com.nine.one.yuedu.read.service.AuthorCreationService;
import com.nine.one.yuedu.read.service.BookInfoService;
import com.nine.one.yuedu.read.utils.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Author:李王柱
 * 2020/8/12
 */
@RestController
@Api(tags = "作者创作接口", value = "作者创作接口")
@RequestMapping(value = "/91yuedu/authorCreation")
public class AuthorCreationController {
    @Autowired
    private BookInfoService bookInfoService;
    @Autowired
    private AuthorCreationService authorCreationService;
    @Autowired
    private AuditChapterMapper auditChapterMapper;
    @Autowired
    private AuditBookMapper auditBookMapper;
    @Autowired
    private UserAuthorMapper userAuthorMapper;
    /**
     * 添加创作中的书
     *
     * @param auditBook
     */
    @PostMapping(value = "/add")
    @ApiOperation(value = "添加创作中的书", notes = "添加创作中的书")
    public JXResult addBookInfo(@ApiParam(value = "创作中的书对象", required = true)
                                @RequestBody AuditBook auditBook) {
        int flag= authorCreationService.addAuditBook(auditBook);
        if (flag<0){
            return new JXResult(false, ApiConstant.StatusCode.ERROR, "添加失败");
        }
        return new JXResult(true, ApiConstant.StatusCode.OK, "添加成功");
    }

    /**
     * 根据不同的条件分页查询书籍
     *
     * @param pageIndex 开始页
     * @param pageSize  每页显示的数量
     * @param privoder  作品提供者
     * @param bookName  书的名字
     * @param author    作者
     * @param id        图书Id
     * @param category  图书类型
     * @param auditStatus     是否审核
     * @return
     */
    @GetMapping(value = "/list/{pageIndex}/{pageSize}")
    @ApiOperation(value = "分页根据条件查询书籍列表", notes = "分页根据条件查询书籍列表")
    public JXResult getBookInfo4PageAndParam(@ApiParam(value = "页码") @PathVariable Integer pageIndex,
                                             @ApiParam(value = "每页显示的数量") @PathVariable Integer pageSize,
                                             @RequestParam(required = false, value = "privoder") @ApiParam(value = "书籍提供者") String privoder,
                                             @RequestParam(required = false, value = "bookName") @ApiParam(value = "书名") String bookName,
                                             @RequestParam(required = false, value = "author") @ApiParam(value = "作者") String author,
                                             @RequestParam(required = false, value = "id") @ApiParam(value = "书籍id") Integer id,
                                             @RequestParam(required = false, value = "category") @ApiParam(value = "书籍类型") String category,
                                             @RequestParam(required = false, value = "auditStatus") @ApiParam(value = "是否审核") Integer auditStatus,
                                             @RequestParam(required = false, value = "valid") @ApiParam(value = "是否上架") Integer valid,
                                             @RequestParam(required = false, value = "formal") @ApiParam(value = "是否正式") boolean formal) throws ParseException {
        if (formal){
            return new JXResult(true, ApiConstant.StatusCode.OK, "请求成功", bookInfoService.getBookInfo4PageAndParamFormal(pageIndex, pageSize, privoder, bookName, author, id, category, valid));
        }
        return new JXResult(true, ApiConstant.StatusCode.OK, "请求成功", authorCreationService.getAuditBook4PageAndParam(pageIndex, pageSize, privoder, bookName, author, id, category, auditStatus));

    }



    @GetMapping(value = "/find/bookInfo/{id}")
    @ApiOperation(value = "通过Id查询图书和章节详情", notes = "通过Id查询图书和章节详情")
    public JXResult findBookInfoById(@ApiParam(value = "图书ID") @PathVariable Integer id) {
        return new JXResult(true, ApiConstant.StatusCode.OK, "请求成功", bookInfoService.findBookInfoById(id));
    }

    @GetMapping(value = "/toReview/{bookId}/{auditStatus}")
    @ApiOperation(value = "将一本作品的审核状态改变")
    public JXResult downBookById(@PathVariable @ApiParam(value = "书籍ID") Integer bookId,
                             @PathVariable @ApiParam(value = "审核状态") Integer auditStatus,
                             HttpServletResponse response) {
        return authorCreationService.toReview(bookId,auditStatus);
    }


    /**
     * 根据创作书籍的ID获得这本书的章节列表,分页,并以不同的排序方式排序
     *
     * @param bookId
     * @param pageIndex
     * @param pageSize
     * @param sort
     * @return
     */
    @GetMapping(value = "/chapterlist/{bookId}/{pageIndex}/{pageSize}")
    public JXResult getChapterListByBookIdPageAndSort(@PathVariable @ApiParam(value = "书籍Id") String bookId,
                                                      @PathVariable @ApiParam(value = "开始页") Integer pageIndex,
                                                      @PathVariable @ApiParam(value = "每页显示数量") Integer pageSize,
                                                     /* @RequestParam(value = "sort", required = false, defaultValue = "1") @ApiParam(value = "排序:1.正序,2倒序") String sort*/
                                                      @RequestParam(value = "sort", required = false) @ApiParam(value = "排序:1.正序,2倒序") Integer sort,
                                                      @RequestParam(value = "draftStatus", required = false) @ApiParam(value = "0草稿.1正文.2驳回") String draftStatus) {
        return new JXResult(true, ApiConstant.StatusCode.OK, "请求成功",
                authorCreationService.getChapterListByBookIdPageAndSort(Integer.parseInt(bookId), pageIndex, pageSize, sort,Integer.parseInt(draftStatus)));
    }

    /**
     * 添加章节
     *
     * @param AuditChapter
     */
    @PostMapping(value = "/addChapter")
    @ApiOperation(value = "添加创作章节", notes = "添加创作章节")
    public JXResult addChapter(@RequestBody @ApiParam(value = "普通章节对象", required = true) ChapterInfoVO AuditChapter
                               ) {
        //先以普通方式获取数据
        ChapterInfo chapterInfo = AuditChapter.getChapterInfo();
        String content = AuditChapter.getContent();
        content=content.replace(" ","");//去掉空格
        //判断一下,是否包含敏感词
        String isResult= authorCreationService.isSensitive(content);
        if (!StringUtils.equals("通过",isResult)){
            return new JXResult(false, ApiConstant.StatusCode.ERROR, isResult);
        }
        //再转为创作类型的章节
        AuditChapter auditChapterInfo = new AuditChapter();
        auditChapterInfo.setBookId(chapterInfo.getBookId());
        auditChapterInfo.setChapterName(chapterInfo.getChapterName());
        auditChapterInfo.setIsFree(chapterInfo.getIsFree());
        /*if (null==chapterInfo.getChapterId()){
            return new JXResult(false, ApiConstant.StatusCode.ERROR, "序号错误,请认真排序!");
        }*/
        auditChapterInfo.setChapterId(chapterInfo.getChapterId());

        authorCreationService.addAuditChapter(auditChapterInfo, content);
        return new JXResult(true, ApiConstant.StatusCode.OK, "添加成功");
    }

    /**
     * 根据图书ID和章节ID获得章节的详情
     *
     * @param bookId
     * @param chapterId
     * @return
     */
    @GetMapping(value = "/findChapter/{bookId}/{chapterId}")
    @ApiOperation(value = "根据创作书籍ID和章节ID获得章节详情", notes = "根据书籍ID和章节ID获得章节详情")
    public JXResult getChapterInfoByBookIdAndChapterId(@PathVariable @ApiParam(value = "创作图书Id") Integer bookId,
                                                       @PathVariable @ApiParam(value = "创作章节Id") Integer chapterId,
                                                       @RequestParam(value = "type", required = false, defaultValue = "") @ApiParam(value = "调用的类型") String type) {
        return new JXResult(true, ApiConstant.StatusCode.OK, "请求成功"
                , authorCreationService.getChapterInfoByBookIdAndChapterId(bookId, chapterId, type));
    }

    /**
     * 修改章节
     *
     * @param chapterInfoVo
     */
    @PutMapping(value = "/updateChapter")
    @ApiOperation(value = "修改章节接口", notes = "修改章节接口")
    public JXResult updateChapter(@RequestBody @ApiParam(value = "章节对象", required = true) ChapterInfoVO chapterInfoVo) {
        ChapterInfo chapterInfo = chapterInfoVo.getChapterInfo();
        String content = chapterInfoVo.getContent();
        content=content.replace(" ","");//要求将所有的空格都去掉
        //如果是审核通过或者将上线的章节,不能再修改
        AuditChapter auditChapterBySelectStatus = new AuditChapter();//获取该章节的状态
        auditChapterBySelectStatus.setId(chapterInfo.getId());
        auditChapterBySelectStatus.setBookId(chapterInfo.getBookId());
        Integer draftStatus = auditChapterMapper.selectOne(auditChapterBySelectStatus).getDraftStatus();
        if (draftStatus==3){ return new JXResult(false, ApiConstant.StatusCode.ACCESSERROR, chapterInfo.getChapterName()+"属于过审章节,不能修改!"); }
        if (draftStatus==4){ return new JXResult(false, ApiConstant.StatusCode.ACCESSERROR, chapterInfo.getChapterName()+"该章节即将上架,不能修改!"); }
        //判断一下,是否包含敏感词
        String isResult= authorCreationService.isSensitive(content);
        if (!StringUtils.equals("通过",isResult)){
            return new JXResult(false, ApiConstant.StatusCode.ERROR, isResult);
        }
        authorCreationService.updateChapter(chapterInfo, content);
        return new JXResult(true, ApiConstant.StatusCode.OK, "修改成功");
    }

    /**
     * 修改章节
     *
     * @param chapterInfoVo
     */
    /*@PutMapping(value = "/updateAuditChapter")
    @ApiOperation(value = "修改创作章节接口", notes = "修改创作章节接口")
    public JXResult updateChapter(@RequestBody @ApiParam(value = "创作章节对象", required = true) ChapterInfo  auditChapter) {
        *//*System.out.println(auditChapter.getOpinionsynopsis());
        System.out.println(auditChapter.getOpinion());*//*
        System.out.println(auditChapter.getBookId());
       *//* authorCreationService.updateAuditChapter(auditChapter);*//*
        return new JXResult(true, ApiConstant.StatusCode.OK, "修改成功");
    }*/

    @PostMapping(value = "/insertOption/{id}/{draftStatus}")
    @ApiOperation(value = "根据创作章节id添加审核意见", notes = "根据创作章节id添加审核意见")
    public JXResult insertOption(@PathVariable @ApiParam(value = "章节id") Integer id,
                                 @PathVariable @ApiParam(value = "修改审核状态") Integer draftStatus,
                                 @RequestParam @ApiParam(value = "简介") String opinionsynopsis,
                                 @RequestParam @ApiParam(value = "详细说明") String opinion) throws ParseException {
        AuditChapter auditChapter = new AuditChapter();
        auditChapter.setId(id);
        auditChapter.setDraftStatus(draftStatus);
        if (draftStatus==2){//现在改成什么都不写也是驳回了
            /*if(opinionsynopsis.length()<1){
                auditChapter.setDraftStatus(1);
            }*///else { //如果将要修改为驳回,并且填写了驳回理由
                //根据章节id找到这本书
                AuditChapter auditChapterBySelectBook = auditChapterMapper.selectByPrimaryKey(id);
                //根据章节的bookId字段找到书,并设置已驳回,更新到数据库
                AuditBook auditBook = new AuditBook();
                auditBook.setAuditStatus(2);
                auditBook.setId(auditChapterBySelectBook.getBookId());
                auditBookMapper.updateByPrimaryKeySelective(auditBook);
           // }
        }if (draftStatus==3){//审核通过,上架时间按照作者定的时间来
            //根据章节id找到这一章
            AuditChapter auditChapterBySelectChapterId = auditChapterMapper.selectByPrimaryKey(id);
            System.out.println("请求目的是审核通过,章节名是:"+auditChapterBySelectChapterId.getChapterName());

            //如果正式章节已经有这一章了,就将内容替换到正式章节,把审核章节的内容和数据库对象都删掉
            if (authorCreationService.replaceAuditToPutshelf(auditChapterBySelectChapterId)){
return new JXResult(true, ApiConstant.StatusCode.OK, "修改成功");
            }

            //计算延后时间isFree,如果是当天或者小于当天,就isFree=0;
            Integer isFree=0;
            SimpleDateFormat simpleDateFormatByDay = new SimpleDateFormat("yyyy-MM-dd");
            Date dateAll = new Date();
            String dateString = simpleDateFormatByDay.format(dateAll);
            String createString = simpleDateFormatByDay.format(auditChapterBySelectChapterId.getCreateTime());

            if (dateString.equals(createString)){
                System.out.println("是当天");
                //根据章节id找到这一章
                AuditChapter auditChapterBySelectBookId = auditChapterMapper.selectByPrimaryKey(id);
                authorCreationService.passed(Integer.toString(auditChapterBySelectChapterId.getBookId()),Integer.toString(auditChapterBySelectChapterId.getChapterId()),isFree);

                //如果没有正文章节了,就修改书籍状态:未提交审核
                AuditChapter have1= new AuditChapter();
                have1.setDraftStatus(1);have1.setBookId(auditChapterBySelectBookId.getBookId());
                List<AuditChapter> select = auditChapterMapper.select(have1);
                if (select.size()<1){
                    AuditBook auditBook = auditBookMapper.selectByPrimaryKey(auditChapterBySelectBookId.getBookId());
                    auditBook.setAuditStatus(0);
                    auditBookMapper.updateByPrimaryKeySelective(auditBook);
                }
                //return new JXResult(true, ApiConstant.StatusCode.OK, "修改成功");
            }else {
                long completeCreateTime = FormateConstant.YEAR_MONTH_DAY.parse(createString).getTime();
                long completeDateTime = FormateConstant.YEAR_MONTH_DAY.parse(dateString).getTime();
                long thatFree=(completeCreateTime-completeDateTime)/86400000;
                isFree = Integer.parseInt(String.valueOf(thatFree));
                if (auditChapterBySelectChapterId.getCreateTime().getTime()<new Date().getTime()){
                    isFree=0;
                }
                //根据章节id找到这一章
                System.out.println("找到这一张");
                AuditChapter auditChapterBySelectBookId = auditChapterMapper.selectByPrimaryKey(id);

                authorCreationService.passed(Integer.toString(auditChapterBySelectChapterId.getBookId()),Integer.toString(auditChapterBySelectChapterId.getChapterId()),isFree);

                //如果没有正文章节了,就修改书籍状态:未提交审核
                AuditChapter have1= new AuditChapter();
                have1.setDraftStatus(1);have1.setBookId(auditChapterBySelectBookId.getBookId());
                List<AuditChapter> select = auditChapterMapper.select(have1);
                if (select.size()<1){
                    AuditBook auditBook = auditBookMapper.selectByPrimaryKey(auditChapterBySelectBookId.getBookId());
                    auditBook.setAuditStatus(0);
                    auditBookMapper.updateByPrimaryKeySelective(auditBook);
                }

            }
            //有可能会把驳回的章节直接审核通过,在这时候修改书籍状态
            if (auditChapterBySelectChapterId.getDraftStatus()==2){
                AuditChapter auditChapterByselect = new AuditChapter();
                auditChapterByselect.setDraftStatus(2);auditChapterByselect.setBookId(auditChapterBySelectChapterId.getBookId());
                List<AuditChapter> selectByDraftStatus2 = auditChapterMapper.select(auditChapterByselect);
                if (selectByDraftStatus2.size()<1){//如果没有驳回章节了,取出这本书,把这本书改为审核中
                    AuditBook auditBook = auditBookMapper.selectByPrimaryKey(auditChapterBySelectChapterId.getBookId());
                    Integer auditStatus = auditBook.getAuditStatus();
                    if (auditStatus==2){//如果这本书原来是被驳回的
                        auditBook.setAuditStatus(1);
                        auditBookMapper.updateByPrimaryKeySelective(auditBook);
                    }
                }
            }

            return new JXResult(true, ApiConstant.StatusCode.OK, "修改成功");//改完了就这章就不在了 ,下面的就不用进行
        }
        auditChapter.setOpinionsynopsis(opinionsynopsis);
        auditChapter.setOpinion(opinion);
        authorCreationService.insertOption(auditChapter);

        //根据章节id找到这一章
        AuditChapter auditChapterBySelectBookId = auditChapterMapper.selectByPrimaryKey(id);
        //如果没有正文章节了,就修改书籍状态:未提交审核
        AuditChapter have1= new AuditChapter();
        have1.setDraftStatus(1);have1.setBookId(auditChapterBySelectBookId.getBookId());
        List<AuditChapter> select = auditChapterMapper.select(have1);
        AuditBook auditBook = auditBookMapper.selectByPrimaryKey(auditChapterBySelectBookId.getBookId());
        if (select.size()<1){
            auditBook.setAuditStatus(0);
            auditBookMapper.updateByPrimaryKeySelective(auditBook);
        }else {
            if (auditBook.getAuditStatus()==2){//如果这本书是被驳回的,查看书在章节变动后还有驳回章节不
                have1.setDraftStatus(2);
                List<AuditChapter> selectByDown = auditChapterMapper.select(have1);
                if (selectByDown.size()<1){
                    auditBook.setAuditStatus(1);
                    auditBookMapper.updateByPrimaryKeySelective(auditBook);
                }
            }
        }
        return new JXResult(true, ApiConstant.StatusCode.OK, "修改成功");
    }

    /**
     * 根据ID删除章节
     *
     * @param id
     */
    @DeleteMapping(value = "/delChapter/{id}")
    @ApiOperation(value = "删除章节", notes = "删除章节")
    public JXResult delChapterById(@PathVariable @ApiParam(value = "章节ID主键") Integer id) {
        authorCreationService.delChapterById(id);
        return new JXResult(true, ApiConstant.StatusCode.OK, "删除成功");
    }

    /**
     * 根据ID查询图书详情
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/findBook/{id}")
    @ApiOperation(value = "通过Id查询图书详情", notes = "通过Id查询图书详情")
    public JXResult findById(@ApiParam(value = "图书ID") @PathVariable Integer id) {
        return new JXResult(true, ApiConstant.StatusCode.OK, "请求成功", authorCreationService.findById(id));
    }

    /**
     * 修改图书信息
     *
     * @param bookInfo
     */
    @PutMapping(value = "/updateBook")
    @ApiOperation(value = "修改图书信息", notes = "修改图书信息")
    public JXResult updateBookInfo(@ApiParam(value = "图书对象")
                                   @RequestBody BookInfo bookInfo) {
        authorCreationService.updateBookInfoToauthorBook(bookInfo);
        return new JXResult(true, ApiConstant.StatusCode.OK, "修改成功");
    }

    /**
     * 从session中获取作者昵称,再将这个作者包含草稿的书籍放到数据区中
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/getThatAuthorBoxBookList/{nickname}")
    @ApiOperation(value = "通过作者名查询草稿列表", notes = "通过作者名查询草稿列表")
    public JXResult getThatAuthorBoxBookList(@ApiParam(value = "作者昵称") @PathVariable String nickname) {
        return new JXResult(true, ApiConstant.StatusCode.OK, "请求成功", authorCreationService.getThatAuthorBoxBookList(nickname));
    }

    /**
     * 从session中获取作者昵称,再将这个作者包含草稿的章节
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/getThatAuthorBoxChapterAllAndBookNameForVo/{pageIndex}/{pageSize}/{nickname}")
    @ApiOperation(value = "通过作者名查询草稿列表", notes = "通过作者名查询草稿列表")
    public JXResult getThatAuthorBoxChapterAllAndBookNameForVo(@PathVariable @ApiParam(value = "开始页") Integer pageIndex,
                                                               @PathVariable @ApiParam(value = "每页显示数量") Integer pageSize,
                                                               @ApiParam(value = "作者昵称") @PathVariable String nickname,
                                                               @RequestParam(value = "sort", required = false, defaultValue = "1") @ApiParam(value = "排序:1.正序,2倒序") Integer sort,
                                                               @RequestParam(value = "draftStatus", required = false, defaultValue = "1") @ApiParam(value = "0草稿.1正文.2驳回") Integer draftStatus) {

        return new JXResult(true, ApiConstant.StatusCode.OK, "请求成功", authorCreationService.getThatAuthorBoxChapterAllAndBookNameForVo(pageIndex,pageSize,nickname,sort,draftStatus));
    }

    /**
     * 删除图书
     *
     * @param id
     */
    @DeleteMapping(value = "/del/{id}")
    @ApiOperation(value = "删除图书", notes = "删除图书")
    public JXResult delBookInfoById(@ApiParam(value = "图书ID") @PathVariable Integer id) {
        bookInfoService.delBookInfoById(id);
        return new JXResult(true, ApiConstant.StatusCode.OK, "删除成功");
    }

    @GetMapping(value = "/judge/{bookId}/{chapterId}")
    @ApiOperation(value = "判断章节排序是否重复", notes = "判断章节排序是否重复")
    public JXResult judgeChapterId(@ApiParam(value = "创作书id") @PathVariable Integer bookId,
                                   @ApiParam(value = "章节排序") @PathVariable String chapterId) {
        String result=authorCreationService.judgeChapterId(chapterId,bookId);

        if ("成功".equals(result)){
            return new JXResult(true, ApiConstant.StatusCode.OK, "请求成功", result);
        }
        return new JXResult(false, ApiConstant.StatusCode.ERROR, "需要修改", result);
    }

    @GetMapping(value = "/getMaxChapterId/{bookId}")
    @ApiOperation(value = "给作者添加新章节生成排序", notes = "给作者添加新章节生成排序")
    public JXResult getMaxChapterId(@ApiParam(value = "创作书id") @PathVariable Integer bookId) {
        Integer result=authorCreationService.getMaxChapterId(bookId);
            return new JXResult(true, ApiConstant.StatusCode.OK, "请求成功", result);
    }

    @GetMapping(value = "/checkNickName/{nickname}")
    @ApiOperation(value = "检查笔名是否可用", notes = "检查笔名是否可用")
    public JXResult getMaxChapterId(@ApiParam(value = "笔名") @PathVariable String nickname) {
        boolean result= authorCreationService.getHaveNickName(nickname);
        return new JXResult(result, ApiConstant.StatusCode.OK, "请求成功", "通信成功");
    }

    @GetMapping(value = "/passed/{bookId}/{chapterId}/{isFree}")
    @ApiOperation(value = "将作品审核通过", notes = "将作品审核通过")
    public JXResult passed(@ApiParam(value = "书id") @PathVariable String bookId,
                           @ApiParam(value = "章节序号") @PathVariable String chapterId,
                           @ApiParam(value = "延迟时间") @PathVariable Integer isFree) {
        authorCreationService.passed(bookId,chapterId,isFree);
        //如果没有正文章节了,就修改书籍状态:未提交审核
        AuditChapter have1= new AuditChapter();
        have1.setDraftStatus(1);have1.setBookId(Integer.parseInt(bookId));
        List<AuditChapter> select = auditChapterMapper.select(have1);
        AuditBook auditBook = auditBookMapper.selectByPrimaryKey(Integer.parseInt(bookId));
        if (select.size()<1){//没有正文章节直接该
            auditBook.setAuditStatus(0);
            auditBookMapper.updateByPrimaryKeySelective(auditBook);
        }else {//有正文章节,但是没有被驳回的,将这本书改成审核中
            have1.setDraftStatus(2);
            List<AuditChapter> selectByDown = auditChapterMapper.select(have1);
            if (selectByDown.size()<1){//没有驳回章节
                if (auditBook.getAuditStatus()==2){
                    auditBook.setAuditStatus(1);
                    auditBookMapper.updateByPrimaryKeySelective(auditBook);
                } } }
        return new JXResult(true, ApiConstant.StatusCode.OK, "请求成功", "通信成功");
    }

    @GetMapping(value = "/Detail/{nickName}")
    @ApiOperation(value = "根据笔名和需求查询相关稿酬信息", notes = "根据笔名和需求查询相关稿酬信息")
    public JXResult brief(@ApiParam(value = "笔名") @PathVariable String nickName
                          //@ApiParam(value = "previous是编辑要查上个月,uncon是未确认") @PathVariable String previous
                          //@ApiParam(value = "终止月份") @PathVariable String endMonth
    ) throws ParseException {
        /*boolean result= authorCreationService.getHaveNickName(nickName);
        if (result){//返回结果空,就是true (没有这个笔名)
            return null;
        }*/
        JSONArray data;
        if ("admin".equals(nickName)){//如果是admin
            data=authorCreationService.getFeeDetailByAdmin();
        }else {//如果是作者,就根据作者名查询所有
            data=authorCreationService.getFeeDetailByAuthor(nickName);
        }
        //JSONArray data=authorCreationService.getFeeDetail(nickName,statrMonth,endMonth);

        //JSONObject data = new JSONObject();
        //昨日字数:
       /* data.put("YesterdayWord",2400);
        //昨日千字稿费:
        data.put("YesterdayFee",4200);
        //昨日分成收入
        data.put("YesterdayDivided",23);
        //本月字数:
        data.put("MonthWord",32100);
        //本月千字稿费:
        data.put("MonthFee",64200);
        //本月分成收入
        data.put("MonthDivided",600);
        //累计收入
        data.put("GrandFee",100000);//奖励*/
       if (null==data){
           return new JXResult(false, ApiConstant.StatusCode.ERROR, "请求失败", "暂无数据,等待结算");
       }
        return new JXResult(true, ApiConstant.StatusCode.OK, "请求成功", data);
    }

    @GetMapping(value = "/ReReview/{bookId}/{chapterName}")
    @ApiOperation(value = "将线上章节驳回", notes = "将线上章节驳回")
    public JXResult ReReview(@ApiParam(value = "书id") @PathVariable String bookId,
                           @ApiParam(value = "章节名") @PathVariable String chapterName) {
        return authorCreationService.ReReview(Integer.parseInt(bookId),chapterName);
        //如果没有正文章节了,就修改书籍状态:未提交审核
        /*AuditChapter have1= new AuditChapter();
        have1.setDraftStatus(1);have1.setBookId(Integer.parseInt(bookId));
        List<AuditChapter> select = auditChapterMapper.select(have1);
        if (select.size()<1){
            AuditBook auditBook = auditBookMapper.selectByPrimaryKey(Integer.parseInt(bookId));
            auditBook.setAuditStatus(0);
            auditBookMapper.updateByPrimaryKeySelective(auditBook);
        }*/

    }

    @GetMapping(value = "/confirm/{creatyearandmonth}/{bookname}/{author}")
    @ApiOperation(value = "主编确认稿酬信息", notes = "主编确认稿酬信息")
    public JXResult confirm(@ApiParam(value = "年月时间") @PathVariable String creatyearandmonth,
                             @ApiParam(value = "书名") @PathVariable String bookname,
                            @ApiParam(value = "作者名") @PathVariable String author) {
        return authorCreationService.confirm(creatyearandmonth,bookname,author);
    }

    @GetMapping(value = "/modifyFee/{creatyearandmonth}/{bookname}/{feebysum}/{reward}/{share}")
    @ApiOperation(value = "主编修改稿酬信息", notes = "主编修改稿酬信息")
    public JXResult modifyFee(@ApiParam(value = "年月时间") @PathVariable String creatyearandmonth,
                            @ApiParam(value = "书名") @PathVariable String bookname,
                              @ApiParam(value = "价格") @PathVariable Integer feebysum,
                              @ApiParam(value = "奖励") @PathVariable Integer reward,
                              @ApiParam(value = "渠道分成") @PathVariable Integer share) {
        return authorCreationService.modifyFee(creatyearandmonth,bookname,feebysum,reward,share);
    }

    @GetMapping(value = "/queryUnread/{authorName}")
    @ApiOperation(value = "查询该作者的未读消息", notes = "查询该作者的未读消息")
    public JXResult queryUnread(@ApiParam(value = "作者昵称") @PathVariable String authorName) {
        return authorCreationService.queryUnread(authorName);
    }

    @GetMapping(value = "/callBack/{id}")
    @ApiOperation(value = "回调消息,改为已读", notes = "回调消息,改为已读")
    public JXResult callBack(@ApiParam(value = "消息对象id") @PathVariable Integer id) {
        return authorCreationService.callBack(id);
    }


    @GetMapping(value = "/exportOnAuthorInformation")
    @ApiOperation(value = "将稿酬信息连同作者身份证、银行卡信息一同导出")
    @ResponseBody
    public void exportOnAuthorInformation(HttpServletRequest request, HttpServletResponse response) throws Exception {

        JSONArray authorArray=authorCreationService.getFeeDetailByAdmin();

        //excel标题
        String[] title = {"时间", "书名", "作者", "千字价格", "字数", "税前稿费", "奖励", "渠道分成",
                "合计", "税费", "税后收入", "真实姓名","身份证号", "性别","银行名称","银行卡号"};

        //excel文件名
        String fileName = "稿酬表" + System.currentTimeMillis() + ".xls";

        //sheet名
        String sheetName = "稿酬表";
        String[][] content = new String[authorArray.size()][5];
        for (int i = 0; i < authorArray.size(); i++) {
            content[i] = new String[title.length];
            JSONObject obj = authorArray.getJSONObject(i);
            content[i][0] = obj.getString("creatyearandmonth");
            content[i][1] = obj.getString("bookName");
            content[i][2] = obj.getString("author");
            content[i][3] = obj.getString("feebysum");//千字价格
            content[i][4] = obj.getString("wordsByMonth");//字数
            content[i][5] = obj.getString("feeByMonth");//税前稿费
            content[i][6] = obj.getString("reward");//奖励
            content[i][7] = obj.getString("share");//渠道分成
            content[i][8] = obj.getString("total");
            content[i][9] = obj.getString("Taxes");//税费
            content[i][10] = obj.getString("taxIncome");//税后收入
            //除此之外,还需要作者信息
            UserAuthor bySelect= new UserAuthor();
            bySelect.setNickname(obj.getString("author"));
            UserAuthor userAuthor = userAuthorMapper.selectOne(bySelect);
            content[i][11]=userAuthor.getRealName();
            content[i][12]=userAuthor.getCardId();
            content[i][13] = userAuthor.getSex() == 1? "男" :"女";
            content[i][14] = userAuthor.getBankName();
            content[i][15] = userAuthor.getBankNum();
        }

        //创建HSSFWorkbook
        HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName, title, content, null);

        //响应到客户端
        try {
            this.setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //发送响应流方法
    public void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
