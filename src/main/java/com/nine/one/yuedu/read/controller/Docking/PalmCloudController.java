package com.nine.one.yuedu.read.controller.Docking;

import com.alibaba.fastjson.JSONObject;
import com.nine.one.yuedu.read.service.PalmCloudService;
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
 * 2020/9/8
 */
@Api(tags = "向掌中云提供接口",value = "向掌中云提供接口")
@RestController
@RequestMapping("91yuedu/palmcloud")
public class PalmCloudController {

    @Autowired
    private PalmCloudService palmCloudService;

    String ZhangZhongKey="9872410da71a138d723e8f925625267c";
    String NotMd5="227c55423b42c543aabe2ecf0dd8b81bd1";

    @GetMapping("/novelList")
    @ApiOperation(value = "获取小说列表")
    public JSONObject novelList(@RequestParam (value = "key") @ApiParam(value = "key值=md5(cpid+密钥)") String key){
        if (!StringUtils.equals(key,ZhangZhongKey)){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code",-6);
            jsonObject.put("message","密钥参数错误!请联系获取密钥!key=md5(cpid+密钥)");
            return jsonObject;
        }
        return palmCloudService.novelList() ;
    }

    @GetMapping("/novel_info")
    @ApiOperation(value = "获取小说基本信息")
    public JSONObject novelInfo(@RequestParam (value = "nid") @ApiParam(value = "小说id") String nid,
                                @RequestParam (value = "key") @ApiParam(value = "key值=md5(cpid+密钥)") String key){
        if (!StringUtils.equals(key,ZhangZhongKey)){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code",-6);
            jsonObject.put("message","密钥参数错误!请联系获取密钥!key=md5(cpid+密钥)");
            return jsonObject;
        }
        try {
            Integer.parseInt(nid);
        }catch (Exception e){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code",-3);
            jsonObject.put("message","传参不合法!请检查是否包含特殊符号");
            return jsonObject;
        }
        return palmCloudService.novelInfo(Integer.parseInt(nid)) ;
    }

    @GetMapping("/catalog")
    @ApiOperation(value = "获取小说目录(章节列表)")
    public JSONObject catalog(@RequestParam (value = "nid") @ApiParam(value = "小说id") String nid,
                              @RequestParam (value = "key") @ApiParam(value = "key值=md5(cpid+密钥)") String key){
        if (!StringUtils.equals(key,ZhangZhongKey)){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code",-6);
            jsonObject.put("message","密钥参数错误!请联系获取密钥!key=md5(cpid+密钥)");
            return jsonObject;
        }
        try {
            Integer.parseInt(nid);
        }catch (Exception e){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code",-3);
            jsonObject.put("message","传参不合法!请检查是否包含特殊符号");
            return jsonObject;
        }
        return palmCloudService.cataLog(Integer.parseInt(nid)) ;
    }

    @GetMapping("/chapter_content")
    @ApiOperation(value = "获取章节内容")
    public JSONObject chapterContent(@RequestParam (value = "nid") @ApiParam(value = "小说id") String nid,
                              @RequestParam (value = "aid") @ApiParam(value = "章节id") String aid,
                                     @RequestParam (value = "key") @ApiParam(value = "key值=md5(cpid+密钥)") String key){
        if (!StringUtils.equals(key,ZhangZhongKey)){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code",-6);
            jsonObject.put("message","密钥参数错误!请联系获取密钥!key=md5(cpid+密钥)");
            return jsonObject;
        }
        try {
            Integer.parseInt(nid);Integer.parseInt(aid);
        }catch (Exception e){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code",-3);
            jsonObject.put("message","传参不合法!请检查是否包含特殊符号");
            return jsonObject;
        }
        return palmCloudService.chapterContent(Integer.parseInt(nid),Integer.parseInt(aid)) ;
    }
}
