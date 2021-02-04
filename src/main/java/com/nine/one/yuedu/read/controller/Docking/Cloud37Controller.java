package com.nine.one.yuedu.read.controller.Docking;

import com.alibaba.fastjson.JSONObject;
import com.nine.one.yuedu.read.service.Cloud37Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author:李王柱
 * 2020/9/16
 */
@Api(tags = "向云上阁提供接口",value = "向云上阁提供接口")
@RestController
@RequestMapping("91yuedu/37Cloud")
public class Cloud37Controller {
    String cp_key="u,gyMXVbNRp5KwDnZOGdh7392HYj6rl+";

    @Autowired
    Cloud37Service cloud37Service;

    /**
     * 书籍列表接口
     */

    @GetMapping(value = "/queryBookList")
    @ApiOperation(value = "获取书籍列表", notes = "获取书籍列表")
    public JSONObject queryBookList(@ApiParam(value = "时间戳") @RequestParam("ts") Integer ts,
                                    @ApiParam(value = "提供商编号") @RequestParam("cp_id") Integer cp_id,
                                    @ApiParam(value = "签名") @RequestParam("sign") String sign,
                                    @ApiParam(value = "第几页") @RequestParam("page") Integer page,
                                    @ApiParam(value = "每页几条") @RequestParam("rows") Integer rows) {
        /*String md5Taget="cp_id="+cp_id+"&page="+page+"&rows="+rows+"&ts="+ts+cp_key;
        String md5resul=DigestUtils.md5DigestAsHex(md5Taget.getBytes());
        System.out.println(md5resul);
        if (!StringUtils.equals(sign,md5resul)){
            JSONObject signFalseJson= new JSONObject();
            signFalseJson.put("errcode",-100);
            signFalseJson.put("errmsg","签名错误!");
            return signFalseJson;
        }*/
        return cloud37Service.queryBookList(page,rows);
    }

    /**
     * 获取指定书籍详情
     * 请求参数:
     *  bookId 书籍ID
     */
    @GetMapping(value = "/queryBook")
    @ApiOperation(value = "获取指定书籍详情", notes = "获取指定书籍详情")
    public JSONObject queryBookById(@ApiParam(value = "时间戳") @RequestParam("ts") Integer ts,
                                    @ApiParam(value = "提供商编号") @RequestParam("cp_id") Integer cp_id,
                                    @ApiParam(value = "签名") @RequestParam("sign") String sign,
                                    @ApiParam(value = "书籍id") @RequestParam("id") String id) {
        try {
            Integer.parseInt(id);
        } catch (Exception e) {
            JSONObject msg = new JSONObject();
            msg.put("code", -3);
            msg.put("msg", "传入参数违法!");
            msg.put("请检查参数是否正确", "检查参数中是否夹杂空格等");
            return msg;
        }
       /* String md5Taget="cp_id="+cp_id+"&id="+id+"&ts="+ts+cp_key;
        String md5resul=DigestUtils.md5DigestAsHex(md5Taget.getBytes());
        if (!StringUtils.equals(sign,md5resul)){
            JSONObject signFalseJson= new JSONObject();
            signFalseJson.put("errcode",-100);
            signFalseJson.put("errmsg","签名错误!");
            return signFalseJson;
        }*/
        return cloud37Service.queryBookById(Integer.parseInt(id));
    }


    /**
     * 获取指定书籍章节列表
     * 请求参数:
     *  bookId 书籍ID
     */
    @GetMapping(value = "/queryChapterList")
    @ApiOperation(value = "获取指定书籍章节列表", notes = "获取指定书籍章节列表")
    public JSONObject queryChapterList(@ApiParam(value = "时间戳") @RequestParam("ts") Integer ts,
                                    @ApiParam(value = "提供商编号") @RequestParam("cp_id") Integer cp_id,
                                    @ApiParam(value = "签名") @RequestParam("sign") String sign,
                                    @ApiParam(value = "书籍id") @RequestParam("id") String id) {
        try {
            Integer.parseInt(id);
        } catch (Exception e) {
            JSONObject msg = new JSONObject();
            msg.put("code", -3);
            msg.put("msg", "传入参数违法!");
            msg.put("请检查参数是否正确", "检查参数中是否夹杂空格等");
            return msg;
        }
        /*String md5Taget="cp_id="+cp_id+"&id="+id+"&ts="+ts+cp_key;
        String md5resul=DigestUtils.md5DigestAsHex(md5Taget.getBytes());
        System.out.println(md5resul);
        if (!StringUtils.equals(sign,md5resul)){
            JSONObject signFalseJson= new JSONObject();
            signFalseJson.put("errcode",-100);
            signFalseJson.put("errmsg","签名错误!");
            return signFalseJson;
        }*/
        return cloud37Service.queryChapterList(Integer.parseInt(id));
    }

    /**
     * 获取指定书籍指定章节详情
     * 请求参数:
     *  id 书籍ID
     *  chapter_id 章节id(这里希望给的是序列号)
     */
    @GetMapping(value = "/queryChapterContent")
    @ApiOperation(value = "获取指定书籍指定章节详情", notes = "获取指定书籍指定章节详情")
    public JSONObject queryChapterContent(@ApiParam(value = "时间戳") @RequestParam("ts") Integer ts,
                                       @ApiParam(value = "提供商编号") @RequestParam("cp_id") Integer cp_id,
                                       @ApiParam(value = "签名") @RequestParam("sign") String sign,
                                       @ApiParam(value = "书籍id") @RequestParam("id") String id,
                                          @ApiParam(value = "章节id") @RequestParam("chapter_id") String chapter_id) {
        try {
            Integer.parseInt(id);Integer.parseInt(chapter_id);
        } catch (Exception e) {
            JSONObject msg = new JSONObject();
            msg.put("code", -3);
            msg.put("msg", "传入参数违法!");
            msg.put("请检查参数是否正确", "检查参数中是否夹杂空格等");
            return msg;
        }
        return cloud37Service.queryChapterContent(Integer.parseInt(id),Integer.parseInt(chapter_id));
    }
}
