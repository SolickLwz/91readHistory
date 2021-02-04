package com.nine.one.yuedu.read.controller.Docking;

import com.alibaba.fastjson.JSONObject;
import com.nine.one.yuedu.read.config.ApiConstant;
import com.nine.one.yuedu.read.config.JXResult;
import com.nine.one.yuedu.read.service.ReadingLimitService;
import com.nine.one.yuedu.read.utils.HttpClientUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

/**
 * Author:李王柱
 * 2020/6/14 0014
 */
@Api(value = "推送到阅文", tags = "推送到阅文")
@RestController
@RequestMapping(value = "/91yuedu/readingLimit")
public class ReadingLimitController {

    @Resource(name = "readingLimitService")
    private ReadingLimitService readingLimitService;

    /**
     * 为阅文添加书和推送章节内容
     *
     * @param bookStr
     */
    @PostMapping(value = "/addBooks")
    @ApiOperation(value = "添加书和推送章节内容", notes = "添加书和推送章节内容 ")
    public JXResult addBook(@ApiParam(value = "书籍id") @RequestParam("bootStr") String bookStr) throws Exception {
        String[] split = bookStr.split("[,]");
        StringBuilder isMsg=new StringBuilder();
        for (int i = 0; i < split.length; i++) {
            String msg = readingLimitService.insertBookById(Integer.parseInt(split[i]));
            if (!msg.equals("通信成功")) {
                isMsg.append(msg);
            }
        }
        if (isMsg.length()>4){
            return new JXResult(false, ApiConstant.StatusCode.REMOTEERROR, isMsg.toString());
        }
        return new JXResult(true, ApiConstant.StatusCode.OK, "添加成功");
    }

    /**
     * 推送因网络问题有可能失败的章节
     * */
    @PostMapping("/retryCharpter")
    @ApiOperation(value = "重新推送该书的章节内容",notes = "重新推送章节,该接口并不可靠")
    public JXResult retryCharpter(@ApiParam(value = "书籍ID") @RequestParam("bookStr")String bookStr) throws IOException {
        String[] split = bookStr.split("[,]");
        StringBuilder isMsg=new StringBuilder();
        for (int i = 0; i < split.length; i++) {
            //因为推送章节的id是从腾讯返回来的,不好保存,就猜测"20000006080000000000"+
            String msg = readingLimitService.retryChapterByBookId(Integer.parseInt(split[i]));
            if (!msg.equals("通信成功")) {
                isMsg.append(msg);
            }
        }
        if (isMsg.length()>4){
            return new JXResult(false, ApiConstant.StatusCode.REMOTEERROR, isMsg.toString());
        }
        return new JXResult(true, ApiConstant.StatusCode.OK, "添加成功");
    }
}
