package com.nine.one.yuedu.read.controller;

import com.nine.one.yuedu.read.config.ApiConstant;
import com.nine.one.yuedu.read.config.JXResult;
import com.nine.one.yuedu.read.cp.XiaoMaoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/91yuedu/cp/xiaomao")
@Api(value = "cp小猫抓取接口", tags = "cp小猫抓取接口")
public class XiaoMaoCPController {

    @Resource(name = "xiaoMaoService")
    private XiaoMaoService xiaoMaoService;

    @GetMapping(value = "/chapter")
    @ApiOperation(value = "获取章节列表")
    public JXResult getChapter() {
        xiaoMaoService.getChapter();
        return new JXResult(true, ApiConstant.StatusCode.OK, "抓取成功");
    }

    @ApiOperation(value = "获取图书列表")
    public JXResult getBook() {
        xiaoMaoService.getBookList();
        return new JXResult(true, ApiConstant.StatusCode.OK, "抓取成功");
    }
}
