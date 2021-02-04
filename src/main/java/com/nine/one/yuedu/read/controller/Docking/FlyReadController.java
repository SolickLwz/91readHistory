package com.nine.one.yuedu.read.controller.Docking;

import com.nine.one.yuedu.read.config.JXResult;
import com.nine.one.yuedu.read.entity.ChapterInfo;
import com.nine.one.yuedu.read.mapper.ChapterInfoMapper;
import com.nine.one.yuedu.read.service.FlyreadService;
import com.nine.one.yuedu.read.service.RedisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Author:李王柱
 * 2020/10/19
 */
@Api(tags = "推送到飞读",value = "推送到飞读")
@RestController
@RequestMapping(value = "/flyRead")
public class FlyReadController {
    @Autowired
    private FlyreadService flyreadService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ChapterInfoMapper chapterInfoMapper;

    @GetMapping(value = "/login")
    @ApiOperation(value = "登录飞读")
    public String loginFeiDu() throws Exception {
        String result= flyreadService.login();
        return result;
    }
    /**
     * 为飞读添加书和推送章节内容,如果
     */
    @GetMapping(value = "/addBook")
    @ApiOperation(value = "添加书")
    public String addBookByBookIds(@ApiParam (value = "书的id串,0代表全部的飞读书籍") @RequestParam(value = "bookIds")String bookIds) throws Exception {
        StringBuilder isMsg=new StringBuilder("访问结果:");
        String key= flyreadService.login();
        if (StringUtils.equals("0",bookIds)){
            //添加全部的书,介于腾讯的失败概率,先不这么做
        }
        else {
            String[] split=bookIds.split(",| ");
            for (int i=0;i<split.length;i++){
                String msg= flyreadService.addBook(Integer.parseInt(split[i]),key);
                if (msg.length()>4){
                    isMsg.append(msg);
                }
            }
        }
        return isMsg.toString();
    }

    @ApiOperation(value = "直接推送这本书的章节")
    @GetMapping(value = "/putChapter")
    public String putChapterByBookIds(@ApiParam("书的id串,可以是逗号和空格,如果传0就推送全部") @RequestParam("bookIds") String bookIds) throws Exception {
        StringBuilder isMsg = new StringBuilder("推送章节的访问结果:");
        String key= flyreadService.login();
        if (StringUtils.equals("0",bookIds)){
            return "添加全部的章节,介于腾讯的失败概率,先不这么做";
        }
        else {
            String[] split=bookIds.split(",| ");
            for (int i=0;i<split.length;i++){
                String msg= flyreadService.addChapterByBookId(Integer.parseInt(split[i]),key);
                if (msg.length()>4){
                    isMsg.append(msg);
                }
            }
        }
        return isMsg.toString();
    }

    @ApiOperation(value = "先调用获取更新信息接口,再根据对方本地的最新章节推送排在后面的章节")
    @GetMapping(value = "/putChapterBySelective")
    public String putChapterBySelectiveAndBookIds(@ApiParam("书的id串,可以是逗号和空格,如果传0就推送全部") @RequestParam("bookIds") String bookIds) throws Exception {
        StringBuilder isMsg = new StringBuilder("推送章节的访问结果:");
        String key= flyreadService.login();
        if (StringUtils.equals("0",bookIds)){
            return "添加全部的章节,介于腾讯的失败概率,先不这么做";
        }
        else {
            String[] split=bookIds.split(",| ");
            for (int i=0;i<split.length;i++){
                String msg= flyreadService.getUpdateInfo(split[i],key);
                System.out.println(msg);
                net.sf.json.JSONObject msgJson = net.sf.json.JSONObject.fromObject(msg);
                //判断通信标识
                String msgCode = msgJson.getString("message");
                if (!StringUtils.equals("Success",msgCode)){
                    return split[i]+"通信失败!";
                }
                if (msg.length()>4){
                    isMsg.append(msg);
                }
                JSONObject result = msgJson.getJSONObject("result");
                String isSuccess= flyreadService.putChapterBySelective(split[i],result.getString("chaptername"),key);
                if (! isSuccess.equals("成功")){
                    return isSuccess;
                }
            }
        }
        return isMsg.toString();
    }


    @ApiOperation(value = "调用更新章节接口,覆盖章节")
    @GetMapping(value = "/updateChapter")
    public String updateChapter(@ApiParam("报联书id") @RequestParam("bookId") Integer bookId,
                                @ApiParam("从哪开始") @RequestParam("start") Integer start,
                                @ApiParam("从哪结束") @RequestParam("end") Integer end,
                                @ApiParam("阅文方开始前缀,会每次往后加1") @RequestParam("prefix") Integer prefix,
                                @ApiParam("阅文方后缀,每次会在前缀后面追加") @RequestParam(value = "suffix", required = false, defaultValue = "") String suffix) throws Exception {
        StringBuilder isMsg = new StringBuilder("覆盖章节的访问结果:");
        String key= flyreadService.login();
        List<ChapterInfo> chapterInfos = chapterInfoMapper.selectRangeChapter(bookId, start, end);
        for (ChapterInfo chapterInfo: chapterInfos){
            String c_chapterid=prefix+suffix;
            String msg= flyreadService.updateChapter(bookId,chapterInfo.getChapterId(),c_chapterid,key);
            prefix+=1;
            if (msg.length()>4){
                isMsg.append(msg);
            }
        }

        return isMsg.toString();
    }

}
