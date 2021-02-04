package com.nine.one.yuedu.read.controller;

import com.nine.one.yuedu.read.config.ApiConstant;
import com.nine.one.yuedu.read.config.JXResult;
import com.nine.one.yuedu.read.service.ChangeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Author:李王柱
 * 2020/7/21
 */
@RestController
@Api(tags = "对内容进行修改接口", value = "对内容进行修改接口")
@RequestMapping(value = "/91yuedu/change")
public class ChangeController {

    @Resource(name = "changeService")
    private ChangeService changeService;

    @GetMapping(value = "/updateFormal")
    @ApiOperation(value = "将该id的正式书籍云内容替换,调用这本书全部章节的update接口")
    public JXResult updateFormal(@ApiParam(value = "正式书id,空格") @RequestParam("start") String bookidStr) {

        String[] split = bookidStr.split(",| ");
        for (int i = 0; i < split.length; i++) {
            String msg = changeService.updateFormal(Integer.parseInt(split[i]));
            if (!msg.equals("通信成功")) {
                return new JXResult(false, ApiConstant.StatusCode.REMOTEERROR, msg);
            }
        }

        return new JXResult(true, ApiConstant.StatusCode.OK, "修改成功");
    }


    /**
     * 将章节名称替换,例如,第三章XXX 替换为 第二章XXX
     *
     * @param bookStr
     */
    @PostMapping(value = "/changeChapterName")
    @ApiOperation(value = "将章节名称替换", notes = "将章节名称替换 ")
    public JXResult changeChapterName(@ApiParam(value = "书籍id") @RequestParam("bookId") Integer bookId,
                            @ApiParam(value = "开始章节") @RequestParam("start") Integer start,
                            @ApiParam(value = "结束章节") @RequestParam("end") Integer end) throws Exception {
        StringBuilder isMsg=new StringBuilder();
changeService.changeChapterName(bookId,start,end);
        return new JXResult(true, ApiConstant.StatusCode.OK, "修改成功");
    }

    /**
     * 将章节名称替换,例如,第三章XXX 替换为 第二章XXX
     *
     * @param bookStr
     */
    @PostMapping(value = "/changeChapterNameForMin")
    @ApiOperation(value = "将章节名替换为上一章的", notes = "将章节名替换为上一章的 ")
    public JXResult changeChapterNameForMin(@ApiParam(value = "书籍id") @RequestParam("bookId") Integer bookId,
                                      @ApiParam(value = "开始章节") @RequestParam("start") Integer start,
                                      @ApiParam(value = "结束章节") @RequestParam("end") Integer end) throws Exception {
        StringBuilder isMsg=new StringBuilder();
        changeService.changeChapterNameForMin(bookId,start,end);
        return new JXResult(true, ApiConstant.StatusCode.OK, "修改成功");
    }

    /**
     * 将章号减一,例如,第二章'第2' 替换为 第一章'第2'
     *
     * @param bookStr
     */
    @PostMapping(value = "/changeChapterNameForNex")
    @ApiOperation(value = "将章节号替换为上一章的", notes = "将章节号替换为上一章的 ")
    public JXResult changeChapterNameForNex(@ApiParam(value = "书籍id") @RequestParam("bookId") Integer bookId,
                                            @ApiParam(value = "开始章节") @RequestParam("start") Integer start,
                                            @ApiParam(value = "结束章节") @RequestParam("end") Integer end) throws Exception {
        StringBuilder isMsg=new StringBuilder();
        changeService.changeChapterNameForNex(bookId,start,end);
        return new JXResult(true, ApiConstant.StatusCode.OK, "修改成功");
    }

    /**
     *将多余的空格去除掉.例如“赵西坡，你 他 妈 的这是找死”,但是不去除段落开头的长空格
     * @param bookStr
     */
    @PostMapping(value = "/getChapterContentAndRemoveSpace")
    @ApiOperation(value = "将报联多余的空格去除掉,但是不去除段落开头的长空格", notes = "将报联多余的空格去除掉,但是不去除段落开头的长空格")
    public JXResult getChapterContentAndRemoveSpace(@ApiParam(value = "书籍id串") @RequestParam("bookStr") String bookStr) throws Exception {
        StringBuilder isMsg=new StringBuilder();
        String[] split = bookStr.split(",| ");
        for (String bookIdString :split){
            isMsg.append(changeService.getChapterContentAndRemoveSpace(Integer.parseInt(bookIdString))) ;
        }

        return new JXResult(true, ApiConstant.StatusCode.OK, isMsg.toString());
    }

    /**
     *将多余的空格去除掉.例如“赵西坡，你 他 妈 的这是找死”,但是不去除段落开头的长空格
     * @param bookStr
     */
    @PostMapping(value = "/getChapterContentAndRemoveSpaceToJingXiang")
    @ApiOperation(value = "将景像多余的空格去除掉,但是不去除段落开头的长空格", notes = "将景像多余的空格去除掉,但是不去除段落开头的长空格")
    public JXResult getChapterContentAndRemoveSpaceToJingXiang(@ApiParam(value = "景像书籍id串") @RequestParam("bookStr") String bookStr) throws Exception {
        StringBuilder isMsg=new StringBuilder();
        String[] split = bookStr.split(",| ");
        for (String bookIdString :split){
            isMsg.append(changeService.getChapterContentAndRemoveSpaceToJingXiang(Integer.parseInt(bookIdString))) ;
        }

        return new JXResult(true, ApiConstant.StatusCode.OK, isMsg.toString());
    }

    @PostMapping(value = "/trimByBookIdStr")
    @ApiOperation(value = "将这本书去除首尾空格,章后面没有空格的加上空格", notes = "将这本书去除首尾空格,章后面没有空格的加上空格")
    public JXResult trim(@ApiParam(value = "书籍id") @RequestParam("bookId") String bookIdStr) throws Exception {
        String[] split = bookIdStr.split(",| ");
        for (int i = 0; i < split.length; i++) {
            String msg = changeService.trimByBookId(Integer.parseInt(split[i]));
            if (!msg.equals("通信成功")) {
                return new JXResult(false, ApiConstant.StatusCode.REMOTEERROR, msg);
            }
        }
        return new JXResult(true, ApiConstant.StatusCode.OK, "通信成功");
    }
    @GetMapping(value = "/trimByBookIdStr")
    @ApiOperation(value = "遍历这本书的章节区间,把区间内的章节的某个符号剔除", notes = "遍历这本书的章节区间,把区间内的章节的某个符号剔除")
    public JXResult Weedout(@ApiParam(value = "书籍id") @RequestParam("bookId") Integer bookId,
                            @ApiParam(value = "开始章节") @RequestParam("开始章节") Integer start,
                            @ApiParam(value = "结束章节") @RequestParam("结束章节") Integer end,
                            @ApiParam(value = "剔除的符号or文字") @RequestParam("剔除的符号or文字") String dele) throws Exception {

            String msg = changeService.weedOut(bookId,start,end,dele);
            if (!msg.equals("通信成功")) {
                return new JXResult(false, ApiConstant.StatusCode.REMOTEERROR, msg);
            }

        return new JXResult(true, ApiConstant.StatusCode.OK, "通信成功");
    }

    @GetMapping(value = "/changeSumWordsToAudit")
    @ApiOperation(value = "遍历所有未上架书,把字数改成目前创作章节的总和", notes = "遍历所有未上架书,把字数改成目前创作章节的总和")
    public JXResult changeSumWordsToAudit() throws Exception {

        String msg = changeService.changeSumWordsToAudit();
        if (!msg.equals("通信成功")) {
            return new JXResult(false, ApiConstant.StatusCode.REMOTEERROR, msg);
        }

        return new JXResult(true, ApiConstant.StatusCode.OK, "通信成功");
    }
}
