package com.nine.one.yuedu.read.controller.Docking;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nine.one.yuedu.read.service.PointCrowdService;
import com.nine.one.yuedu.read.utils.GX.myMD5Util;
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

import java.net.URLEncoder;

/**
 * Author:李王柱
 * 2021/1/20
 */
@Api(tags = "向点众提供接口,根据点众文档",value = "向点众提供接口")
@RestController
@RequestMapping("91yuedu/PointCrowd")
public class PointCrowdController {
    String  key="05f210cdeda893a8c11c270aa919e1f1";

    @Autowired
    private PointCrowdService pointCrowdService;

    @GetMapping("/getBookList")
    @ApiOperation(value = "获得合作方所有授权的书籍ID列表", notes = "获得合作方所有授权的书籍ID列表")
    public JSONArray getBookList(@ApiParam("合作方唯一标识") @RequestParam("client_id")String client_id ,
                                 @ApiParam("sign") @RequestParam("sign")String sign ){
        try {
            String resultSign = myMD5Util.myMD5ByLowerCase(client_id+key);
            System.out.println(resultSign);
            if (! StringUtils.equals(resultSign,sign)){
                JSONArray jsonArray = new JSONArray();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("code",2);
                jsonObject.put("message","sign签名验证错误");
                jsonArray.add(jsonObject);
                return jsonArray;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pointCrowdService.getBookList();
    }


    @GetMapping("/getBookInfo")
    @ApiOperation(value = "获得合作方指定书籍ID的书籍详情", notes = "获得合作方指定书籍ID的书籍详情")
    public JSONObject getBookInfo(@ApiParam("合作方唯一标识") @RequestParam("client_id")String client_id ,
                                 @ApiParam("书籍id") @RequestParam("book_id")String book_id ,
                                 @ApiParam("sign") @RequestParam("sign")String sign ){
        try {
            String resultSign = myMD5Util.myMD5ByLowerCase(client_id+key+book_id);
            System.out.println(resultSign);
            if (! StringUtils.equals(resultSign,sign)){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("code",2);
                jsonObject.put("message","sign签名验证错误");
                return jsonObject;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pointCrowdService.getBookInfo(book_id);
    }

    @GetMapping("/getVolumeList")
    @ApiOperation(value = "获得合作方指定书籍ID的书籍章节列表", notes = "获得合作方指定书籍ID的书籍章节列表")
    public JSONArray getVolumeList(@ApiParam("合作方唯一标识") @RequestParam("client_id")String client_id ,
                                   @ApiParam("书籍id") @RequestParam("book_id")String book_id ,
                                 @ApiParam("sign") @RequestParam("sign")String sign ){
        try {
            String resultSign = myMD5Util.myMD5ByLowerCase(client_id+key+book_id);
            System.out.println(resultSign);
            if (! StringUtils.equals(resultSign,sign)){
                JSONArray jsonArray = new JSONArray();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("code",2);
                jsonObject.put("message","sign签名验证错误");
                jsonArray.add(jsonObject);
                return jsonArray;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pointCrowdService.getVolumeList(book_id);
    }

    @GetMapping("/getChapterInfo")
    @ApiOperation(value = "获得章节内容", notes = "获得章节内容")
    public JSONObject getChapterInfo(@ApiParam("合作方唯一标识") @RequestParam("client_id")String client_id ,
                                  @ApiParam("书籍id") @RequestParam("book_id")String book_id ,
                                  @ApiParam("章节id") @RequestParam("chapter_id")String chapter_id ,
                                  @ApiParam("sign") @RequestParam("sign")String sign ){
        try {
            String resultSign = myMD5Util.myMD5ByLowerCase(client_id+key+book_id+chapter_id);
            System.out.println(resultSign);
            if (! StringUtils.equals(resultSign,sign)){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("code",2);
                jsonObject.put("message","sign签名验证错误");
                return jsonObject;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pointCrowdService.getChapterInfo(book_id,chapter_id);
    }

    @GetMapping("/categoryList")
    @ApiOperation(value = "获得合作方书籍分类列表", notes = "获得合作方书籍分类列表")
    public JSONArray categoryList(@ApiParam("合作方唯一标识") @RequestParam("client_id")String client_id ,
                                   @ApiParam("sign") @RequestParam("sign")String sign ){
        try {
            String resultSign = myMD5Util.myMD5ByLowerCase(client_id+key);
            System.out.println(resultSign);
            if (! StringUtils.equals(resultSign,sign)){
                JSONArray jsonArray = new JSONArray();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("code",2);
                jsonObject.put("message","sign签名验证错误");
                jsonArray.add(jsonObject);
                return jsonArray;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pointCrowdService.categoryList();
    }
}
