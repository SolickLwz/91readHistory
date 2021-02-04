package com.nine.one.yuedu.read.controller;

import com.nine.one.yuedu.read.cp.MiGuService;
import com.nine.one.yuedu.read.entity.cp.MiGuResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "咪咕书籍对接接口", value = "咪咕书籍对接接口")
@RequestMapping(value = "/MiGu")
public class MIGuController {

    @Resource(name = "miGuService")
    private MiGuService miGuService;


    @PostMapping(value = "/getBookList")
    @ApiOperation(value = "获取书籍列表")
    public MiGuResult getBookList(@ApiParam("授权appId") @RequestParam("appId") String appId
            , @ApiParam("签名") @RequestParam("sign") String sign) {
        MiGuResult miGuResult = miGuService.checkSign(appId, sign);
        if (miGuResult.getStatusCode() != 0) {
            return miGuResult;
        }
        List<Map<String, String>> bookList = miGuService.getBookList(appId);
        return new MiGuResult(0, "请求成功", bookList);
    }


    @PostMapping(value = "/getBookInfo")
    @ApiOperation(value = "获取书籍详情")
    public MiGuResult getBookInfo(@ApiParam("授权appId") @RequestParam("appId") String appId
            , @ApiParam("签名") @RequestParam("sign") String sign
            , @ApiParam("书籍ID") @RequestParam("bookId") String bookId) {
        MiGuResult miGuResult = miGuService.checkSign(appId, sign);
        if (miGuResult.getStatusCode() != 0) {
            return miGuResult;
        }
        return new MiGuResult(0, "请求成功", miGuService.getBookInfo(bookId));
    }

    @PostMapping(value = "/getVolumeList")
    @ApiOperation(value = "获取图书分卷信息")
    public MiGuResult getVolumeList(@ApiParam("授权appId") @RequestParam("appId") String appId
            , @ApiParam("签名") @RequestParam("sign") String sign
            , @ApiParam("书籍ID") @RequestParam("bookId") String bookId) {
        MiGuResult miGuResult = miGuService.checkSign(appId, sign);
        if (miGuResult.getStatusCode() != 0) {
            return miGuResult;
        }
        return new MiGuResult(0, "请求成功", miGuService.getVolumeList(bookId));
    }


    @PostMapping(value = "/getChapterList")
    @ApiOperation(value = "图书章节目录")
    public MiGuResult getChapterList(@ApiParam("授权appId") @RequestParam("appId") String appId
            , @ApiParam("签名") @RequestParam("sign") String sign
            , @ApiParam("书籍ID") @RequestParam("bookId") String bookId) {
        MiGuResult miGuResult = miGuService.checkSign(appId, sign);
        if (miGuResult.getStatusCode() != 0) {
            return miGuResult;
        }
        return new MiGuResult(0, "请求成功", miGuService.getChapterList(bookId));
    }


    @PostMapping(value = "/getChapterInfo")
    @ApiOperation(value = "图书章节详情")
    public MiGuResult getChapterInfo(@ApiParam("授权appId") @RequestParam("appId") String appId
            , @ApiParam("签名") @RequestParam("sign") String sign
            , @ApiParam("书籍ID") @RequestParam("bookId") String bookId
            , @ApiParam("章节ID") @RequestParam("chapterId") String chapterId) {
        MiGuResult miGuResult = miGuService.checkSign(appId, sign);
        if (miGuResult.getStatusCode() != 0) {
            return miGuResult;
        }
        return new MiGuResult(0, "请求成功", miGuService.getChapterInfo(bookId, chapterId));
    }
}
