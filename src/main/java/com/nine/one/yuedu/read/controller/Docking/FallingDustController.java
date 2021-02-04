package com.nine.one.yuedu.read.controller.Docking;

import com.nine.one.yuedu.read.service.FallingDustService;
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
 * 2020/11/3
 */
@RestController
@Api(value = "向落尘提供接口",tags = "向落尘提供接口")
@RequestMapping("/fallingDust")
public class FallingDustController {
    @Autowired
    private FallingDustService fallingDustService;

    @GetMapping("/booklist")
    @ApiOperation("获取更新作品列表(按更新时间排序)")
    public String booklist(@ApiParam("页码,默认1") @RequestParam(value ="page",required = false, defaultValue = "1") Integer page) throws Exception {
        return fallingDustService.booklist(page);
    }

    @GetMapping("/book")
    @ApiOperation("获取作品基本信息")
    public String book(@ApiParam("作品编号") @RequestParam(value ="bid") Integer bid) throws Exception {
        return fallingDustService.book(bid);
    }

    @GetMapping("/chepters")
    @ApiOperation("获取作品章节列表")
    public String chepters(@ApiParam("作品编号") @RequestParam(value ="bid") Integer bid) throws Exception {
        return fallingDustService.chepters(bid);
    }

    @GetMapping("/content")
    @ApiOperation("获取章节内容")
    public String content(@ApiParam("章节具体id") @RequestParam(value ="cid") Integer cid) throws Exception {
        String resultXml= fallingDustService.content(cid);
        return resultXml;
    }
}
