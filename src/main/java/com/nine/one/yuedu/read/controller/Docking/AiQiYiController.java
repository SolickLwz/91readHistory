package com.nine.one.yuedu.read.controller.Docking;

import com.alibaba.fastjson.JSONObject;
import com.nine.one.yuedu.read.config.ApiConstant;
import com.nine.one.yuedu.read.service.AiqiyiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

/**
 * Author:李王柱
 * 2020/9/29
 */
@Api(tags = "向爱奇艺提供接口", value = "向爱奇艺提供接口")
@RestController
@RequestMapping(value = "/aiqiyi")
public class AiQiYiController {
    private String AiqiyiIdentity="39db07863e9f43b7b24c22647d997da9";

    @Autowired
    private AiqiyiService aiqiyiService;

    /**
     * 获取授权图书的id列表
     */
    @GetMapping(value = "/book/list")
    @ApiOperation(value = "提供书籍列表", notes = "提供书籍列表")
    public JSONObject getAiqiyiBookIdList(@ApiParam (value = "给爱奇艺的身份标识" )@RequestParam ("identity") String identity,
                                      @ApiParam (value = "上一次拉取书单时返回的戳" )@RequestParam ("lastUpdateTime") Long lastUpdateTime) throws ParseException {
        if (!AiqiyiIdentity.equals(identity)){
            JSONObject result = new JSONObject();
            result.put("code","A00001");
            result.put("msg","请联系主编获取身份标识!");
            return result;
        }
        JSONObject bookListJSON = aiqiyiService.getBookIdListByLastUpdateTime(lastUpdateTime);
        return bookListJSON;
    }


    /**
     * 根据id获取图书信息
     */
    @GetMapping(value = "/book/info")
    @ApiOperation(value = "根据id获取图书信息", notes = "根据id获取图书信息")
    public JSONObject getAiqiyiBookInfoById(@ApiParam (value = "给爱奇艺的身份标识" )@RequestParam ("identity") String identity,
                                          @ApiParam (value = "图书id信息" )@RequestParam ("bookId") Integer bookId) throws ParseException {
        if (!AiqiyiIdentity.equals(identity)){
            JSONObject result = new JSONObject();
            result.put("code","A00001");
            result.put("msg","请联系主编获取身份标识!");
            return result;
        }
        JSONObject bookListJSON = aiqiyiService.getAiqiyiBookInfoById(bookId);
        return bookListJSON;
    }

    /**
     * 获取图书结构（书——卷——章）
     */
    @GetMapping(value = "/book/structure")
    @ApiOperation(value = "根据id获取图书章节列表", notes = "根据id获取图书章节列表")
    public JSONObject getAiqiyiBookstructureById(@ApiParam (value = "给爱奇艺的身份标识" )@RequestParam ("identity") String identity,
                                            @ApiParam (value = "图书id" )@RequestParam ("bookId") Integer bookId) {
        if (!AiqiyiIdentity.equals(identity)){
            JSONObject result = new JSONObject();
            result.put("code","A00001");
            result.put("msg","请联系主编获取身份标识!");
            return result;
        }
        JSONObject structureJSON = aiqiyiService.getAiqiyiBookstructureById(bookId);
        return structureJSON;
    }

    /**
     * 获取章信息(根据书id+卷id+章id+sign)
     */
    @GetMapping(value = "/book/volume/chapter/info")
    @ApiOperation(value = "根据书id和章节获取章节内容", notes = "根据书id和章节获取章节内容")
    public JSONObject getAiqiyiChapterByBookIdAndChapterIdAndSign(@ApiParam (value = "给爱奇艺的身份标识" )@RequestParam ("identity") String identity,
                                                 @ApiParam (value = "图书id" )@RequestParam ("bookId") Integer bookId,
                                                 @ApiParam (value = "卷id" )@RequestParam (value = "volumeId",required = false) Integer volumeId,
                                                 @ApiParam (value = "章节id" )@RequestParam ("chapterId") Integer chapterId,
                                                 @ApiParam (value = "md加密后的值" )@RequestParam (value = "sign",required = false) String sign,
                                                 @ApiParam (value = "随机串" )@RequestParam (value = "verify",required = false) String verify){
        if (!AiqiyiIdentity.equals(identity)){
            JSONObject result = new JSONObject();
            result.put("code","A00001");
            result.put("msg","请联系主编获取身份标识!");
            return result;
        }
        JSONObject chapterJSON = aiqiyiService.getAiqiyiChapterByBookIdAndChapterId(bookId,chapterId);
        return chapterJSON;
    }
}
