package com.nine.one.yuedu.read.controller.Docking;

import com.alibaba.fastjson.JSONObject;
import com.nine.one.yuedu.read.service.BaiDuCellularService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author:李王柱
 * 2021/1/27
 */
@Api(tags = "向手百提供接口", value = "向手百提供接口")
@RestController
@RequestMapping("/baiDuCellular")
public class BaiDuCellularController {
    @Autowired
    private BaiDuCellularService baiDuCellularService;

    @GetMapping("/getBookList")
    @ApiOperation(value = "获取书籍列表",notes = "获取书籍列表")
    public JSONObject getBookList(){
        return baiDuCellularService.getBookList();
    }

    @GetMapping("/bookInfo")
    @ApiOperation(value = "获取图书信息",notes = "获取图书信息")
    public JSONObject bookInfo(@ApiParam(value = "单条报联书idStr" )@RequestParam("bookId") String bookId){
        return baiDuCellularService.getBookInfo(bookId);
    }

    @GetMapping("/getChapterList")
    @ApiOperation(value = "获取章节列表",notes = "获取章节列表")
    public JSONObject getChapterList(@ApiParam(value = "单条报联书idStr" )@RequestParam("bookId") String bookId){
        return baiDuCellularService.getChapterList(bookId);
    }

    @GetMapping("/getContent")
    @ApiOperation(value = "获取章节内容",notes = "获取章节内容")
    public JSONObject getContent(@ApiParam(value = "单条报联书idStr" )@RequestParam("bookId") String bookId,
                                 @ApiParam(value = "章节id" )@RequestParam("chapterId") String chapterId){
        return baiDuCellularService.getContent(bookId,chapterId);
    }
}
