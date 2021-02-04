package com.nine.one.yuedu.read.controller.Docking;

import com.nine.one.yuedu.read.config.ApiConstant;
import com.nine.one.yuedu.read.config.JXResult;
import com.nine.one.yuedu.read.entity.Bangdan;
import com.nine.one.yuedu.read.service.NeteaseBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Author:李王柱
 * 2020/6/12 0012
 */
@Api(tags = "推送到网易云", value = "推送到网易云")
@RestController
@RequestMapping(value = "/91yuedu/netease")
public class NeteaseController {

    @Resource(name = "neteaseBookService")
    private NeteaseBookService neteaseBookService;

    /**
     * 添加授权书籍
     *
     * @param bookStr
     */
    @PostMapping(value = "/addBooks")
    @ApiOperation(value = "添加书籍", notes = "添加书籍")
    public JXResult addBook(@ApiParam(value = "书籍id,逗号隔开") @RequestParam("bootStr") String bookStr) throws Exception {

        String[] split = bookStr.split("[,]");
        for (int i = 0; i < split.length; i++) {
            String msg = neteaseBookService.addBookForNetease(split[i]);
                if (!msg.equals("通信成功")) {
                    return new JXResult(false, ApiConstant.StatusCode.REMOTEERROR, msg);
            }
        }
        return new JXResult(true, ApiConstant.StatusCode.OK, "添加成功");
    }

    /**
     * 为添加授权的书籍,推送章节内容
     * */
    @RequestMapping(value ="/addBookSection" ,method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
    @ApiOperation(value = "推送章节内容",notes = "推送章节内容")
    public JXResult addCharpter(String bookStr) throws Exception {
        String[] split = bookStr.split("[,]");
        for (int i = 0; i < split.length; i++) {
            String msg = neteaseBookService.addCharpterForNetease(split[i]);
                if (!msg.equals("通信成功")) {
                    return new JXResult(false, ApiConstant.StatusCode.REMOTEERROR, msg);
            }
        }
        return new JXResult(true, ApiConstant.StatusCode.OK, "添加成功");
    }
}
