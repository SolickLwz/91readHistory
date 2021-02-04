package com.nine.one.yuedu.read.controller.Docking;

import com.nine.one.yuedu.read.config.ApiConstant;
import com.nine.one.yuedu.read.config.JXResult;
import com.nine.one.yuedu.read.service.YueDuFangCPService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author:李王柱
 * 2020/8/6
 */
@Api(value = "cp悦读坊抓取接口", tags = "cp悦读坊抓取接口")
@RestController
@RequestMapping(value = "/91yuedu/cp/yuedufang")
public class YueDuFangCPController {
    @Autowired
    YueDuFangCPService yueDuFangCPService;

    @GetMapping(value = "/articlelist")
    @ApiOperation(value = "获取小说列表并添加到数据库", notes = "获取小说列表并添加到数据库")
    public JXResult GetArticle(@ApiParam(value = "悦读坊那边的页数,每100条一页") @RequestParam("page") Integer page) throws Exception {
        String flag = yueDuFangCPService.getAriticleList(page);
        if (flag.length() > 10) {
            return new JXResult(true, ApiConstant.StatusCode.OK, flag);
        }
        return new JXResult(false, ApiConstant.StatusCode.ERROR, flag);
    }

    @GetMapping(value = "/articleinfo")
    @ApiOperation(value = "获取小说详情并进行更新", notes = "获取小说详情并进行更新")
    public JXResult GetArticleinfoToUpdate(@ApiParam(value = "悦读坊的小说id,传0既全部更新") @RequestParam("aid") Integer aid) throws Exception {
        JXResult result=yueDuFangCPService.ArticleinfoUpdate(aid);
        return result;
    }

    @GetMapping(value = "/articlechapter")
    @ApiOperation(value = "获取章节目录并更新到数据库", notes = "获取章节目录并更新到数据库")
    public JXResult GetArticlechapterToUpdate(@ApiParam(value = "悦读坊的小说id,传0既全部更新") @RequestParam("aid") Integer aid) throws Exception {
        JXResult result=yueDuFangCPService.ArticlechapterUpdate(aid);
        return result;
    }

    @GetMapping(value = "/chaptercontent")
    @ApiOperation(value = "获取章内容并上传到云", notes = "获取章内容并上传到云")
    public JXResult GetChaptercontent(@ApiParam(value = "悦读坊的小说id,传0既全部抓取") @RequestParam("aid") String aid) throws Exception {
        StringBuilder isMsg=new StringBuilder();
        if ("0".equals(aid)){
            JXResult result=yueDuFangCPService.GetChaptercontent(Integer.parseInt(aid));
            return result;
        }
        else {
            String[] split = aid.split("[,]");
            for (int i = 0; i < split.length; i++) {
                JXResult result=yueDuFangCPService.GetChaptercontent(Integer.parseInt(split[i]));
                if (result.isFlag()) {
                    System.out.println(split[i]+"内容获取完毕!");
                   isMsg.append(result.getMessage());
                }else {
                    return result;
                }
            }
        }
        return new JXResult(true, ApiConstant.StatusCode.OK, isMsg.toString());
    }
}
