package com.nine.one.yuedu.read.controller.Docking;

import com.nine.one.yuedu.read.service.AliBookFlagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author:李王柱
 * 2020/6/22
 */
@Api(tags = "对接阿里书旗,提供接口", value = "对接阿里书旗,提供接口")
@RestController
@RequestMapping(value = "/91yuedu/bookFlag")
public class AliBookFlagController {

    @Autowired
    private AliBookFlagService aliBookFlagService;

    /**
     * 为书旗提供作品列表
     */
    @GetMapping(value = "/booklist")
    @ApiOperation(value = "提供书籍列表", notes = "提供书籍列表")
    public String getIdList() {
        String bookListXml = aliBookFlagService.getBookList();
        return bookListXml;
    }

    /**
     * 为书旗提供对应id的作品信息
     * */
    @GetMapping(value = "/BookDetails")
    @ApiOperation(value = "提供书籍详情" ,notes = "提供书籍详情")
    public String getDetails(@ApiParam(value = "书籍id") @RequestParam("bookId")Integer bookId){
        return aliBookFlagService.getBookDetailsById(bookId);
    }

    /**
     * 根据书籍id提供章节列表
     * */
    @GetMapping(value = "/chapterList")
    @ApiOperation(value = "根据id提供章节列表",notes = "根据id提供章节列表")
    public String getChapterListByBookId(@ApiParam(value = "书籍id") @RequestParam("bookid")Integer bookid){
return aliBookFlagService.getChapterListByBookId(bookid);
    }

    /**
     * 提供正文
     * */
    @GetMapping(value = "/chapterContent")
    @ApiOperation(value = "根据书id和章节id提供正文",notes = "根据书id和章节id提供正文")
    public String getChapterListByBookId(@ApiParam(value = "书籍id") @RequestParam("bookid")Integer bookid,@ApiParam(value = "章节id") @RequestParam("chapterid")Integer chapterid){
        return aliBookFlagService.getchapterContentByBookIdAndChapterId(bookid,chapterid);
    }
}
