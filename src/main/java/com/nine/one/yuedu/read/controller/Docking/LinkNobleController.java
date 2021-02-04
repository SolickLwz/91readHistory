package com.nine.one.yuedu.read.controller.Docking;

import com.alibaba.fastjson.JSONObject;
import com.nine.one.yuedu.read.config.ApiConstant;
import com.nine.one.yuedu.read.config.JXResult;
import com.nine.one.yuedu.read.service.LinkNobleProvideService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Author:李王柱
 * 2020/6/16 0016
 */
@Api(tags = "向连尚读书提供接口",value = "向连尚读书提供接口")
@RestController
@RequestMapping("91yuedu/linknoble")
public class LinkNobleController {

    @Autowired
    LinkNobleProvideService linkNobleProvideService;

    /**
     * 书籍列表接口
     */

    @GetMapping(value = "/queryAll")
    @ApiOperation(value = "获取已授权书籍", notes = "获取已授权书籍")
    public JSONObject queryAllBook() {
        return linkNobleProvideService.queryAll();
    }

    /**
     * 书籍详情接⼝
     * 请求参数:
     *  bookId 书籍ID
     */
    @GetMapping(value = "/queryBook")
    @ApiOperation(value = "获取书籍详情", notes = "获取书籍详情")
    public JSONObject getBook(@ApiParam(value = "书籍id") @RequestParam("bookId") String bookId) {
        return linkNobleProvideService.queryBookById(bookId);
    }


    /**
     * 章节列表接口
     * */
    @GetMapping(value = "/queryChapter")
    @ApiOperation(value = "获取章节列表" , notes = "获取章节列表")
    public JSONObject getChapterDetails(@ApiParam(value = "书籍id") @RequestParam("bookId")  String bookId){
        return linkNobleProvideService.queryChapterByBookId(bookId);
    }

    /**
     * 章节内容接⼝
     * */
    @GetMapping(value = "/getChapter")
    @ApiOperation(value = "根据书籍id和章节id获取内容",notes = "根据书籍id和章节id获取内容")
    public JSONObject getChapter(@ApiParam(value = "书籍id") @RequestParam("bookId") String bookId, @ApiParam(value = "章节id") @RequestParam("chapterId") String chapterId) {
        try {
            Integer.parseInt(bookId);
            Integer.parseInt(chapterId);
        } catch (Exception e) {
            JSONObject msg = new JSONObject();
            msg.put("code", -3);
            msg.put("msg", "传入参数违法!");
            msg.put("请检查参数是否正确", "检查参数中是否夹杂空格等");
            return msg;
        }
        return linkNobleProvideService.getChapterByBookIdAndChapterId(Integer.parseInt(bookId), Integer.parseInt(chapterId));
    }

   /**
    * 分类接口
    * */
   @GetMapping(value = "/Category")
   @ApiOperation(value = "获取分类信息",notes = "现在只给了映射分类id+分类名")
   public JSONObject Category(){
       return linkNobleProvideService.getCategory();
   }
}
