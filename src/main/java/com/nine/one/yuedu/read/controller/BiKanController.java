package com.nine.one.yuedu.read.controller;

import com.nine.one.yuedu.read.cp.BiKanService;
import com.nine.one.yuedu.read.entity.cp.BiKanResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@Api(tags = "必看小说书籍抓取接口", value = "必看小说书籍抓取接口")
@RequestMapping(value = "/book")
public class BiKanController {

    @Resource(name = "biKanService")
    private BiKanService biKanService;

    @GetMapping(value = "/list")
    @ApiOperation(value = "获取授权图书的id列表", notes = "获取授权图书的id列表")
    public BiKanResult list(@ApiParam("cp的唯一标识") @RequestParam("identity") String identity,
                            @ApiParam("最后更新时间") @RequestParam("lastUpdateTime") Long lastUpdateTime) {
        if (lastUpdateTime == null || lastUpdateTime == 0) {
            return new BiKanResult("A00001", null, "Request params not valid");
        }
        return new BiKanResult("A00000", biKanService.getBookIds(identity), "Success");
    }

    @GetMapping(value = "/info")
    @ApiOperation(value = "获取图书详情", notes = "获取图书详情")
    public BiKanResult info(@ApiParam("cp的唯一标识") @RequestParam("identity") String identity,
                            @ApiParam("图书ID") @RequestParam("bookId") Integer bookId) {
        if (!"FF483D1FF591898A9942916050D2CA3F".equals(identity)) {
            return new BiKanResult("A00001", null, "Request params not valid");
        }
        return new BiKanResult("A00000", biKanService.getBookInfoByBookId(bookId), "Success");
    }


    @GetMapping(value = "/structure")
    @ApiOperation(value = "获取图书结构", notes = "获取图书结构")
    public BiKanResult structure(@ApiParam("cp的唯一标识") @RequestParam("identity") String identity,
                                 @ApiParam("图书ID") @RequestParam("bookId") Integer bookId) {
        if (!"FF483D1FF591898A9942916050D2CA3F".equals(identity)) {
            return new BiKanResult("A00001", null, "Request params not valid");
        }
        return new BiKanResult("A00000", biKanService.getBookStructureByBookId(bookId), "Success");
    }

    @GetMapping(value = "/volume/chapter/info")
    @ApiOperation(value = "获取章节内容", notes = "获取章节内容")
    public BiKanResult getContent(@ApiParam("cp的唯一标识") @RequestParam("identity") String identity,
                                  @ApiParam("图书ID") @RequestParam("bookId") Integer bookId,
                                  @ApiParam("卷ID") @RequestParam("volumeId") Integer volumeId,
                                  @ApiParam("章节ID") @RequestParam("chapterId") Integer chapterId) {
        if (!"FF483D1FF591898A9942916050D2CA3F".equals(identity)) {
            return new BiKanResult("A00001", null, "Request params not valid");
        }
        return new BiKanResult("A00000", biKanService.getChapterContentByBookIdAndChapterId(bookId, chapterId), "Success");
    }
}
