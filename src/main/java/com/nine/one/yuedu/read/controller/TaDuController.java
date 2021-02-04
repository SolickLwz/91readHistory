package com.nine.one.yuedu.read.controller;

import com.alibaba.fastjson.JSONObject;
import com.nine.one.yuedu.read.config.ApiConstant;
import com.nine.one.yuedu.read.config.JXResult;
import com.nine.one.yuedu.read.service.TaDuService;
import com.nine.one.yuedu.read.utils.GX.HttpClientUtilsGX;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Author:李王柱
 * 2020/8/28
 */
@Api(value = "塔读对接接口", tags = "塔读对接接口")
@RestController
@RequestMapping(value = "/91yuedu/tadu")
public class TaDuController {
    @Autowired
    private TaDuService taDuService;

    /*@GetMapping(value = "/articlelist")
    @ApiOperation(value = "向塔读推送更新章节", notes = "向塔读推送更新章节")
    public JXResult uptateChapter(@ApiParam(value = "悦读坊那边的页数,每100条一页") @RequestParam("page") Integer page) throws Exception {
        String message = taDuService.uptateChapter(page);
        if (message.length() < 10) {
            return new JXResult(true, ApiConstant.StatusCode.OK, message);
        }
        return new JXResult(false, ApiConstant.StatusCode.ERROR, message);
    }*/
    @GetMapping(value = "/addTaDuChapterByGetChapterList")
    @ApiOperation(value = "调用塔读的查询章节目录接口后往塔读测试环境推送章节", notes = "调用塔读的查询章节目录接口后往塔读测试环境推送章节")
    public JXResult addTaDuChapterByGetChapterList() throws Exception {
        JXResult result = taDuService.addTaDuChapterByGetChapterList();
        return result;
    }

   /* @GetMapping(value = "/addTaDuChapterByGetChapterListAndProvideJingXiangIds")
    @ApiOperation(value = "调用塔读的查询章节目录接口后往塔读测试环境推送章节(根据提供的景像书id)", notes = "调用塔读的查询章节目录接口后往塔读测试环境推送章节(根据提供的景像书id)")
    public JXResult addTaDuChapterByGetChapterListAndProvideJingXiangIds(@ApiParam(value = "景像书籍id串,英文逗号或者n空格隔开") @RequestParam("ids") String ids) throws Exception {
        StringBuilder result=new StringBuilder();
        String[] split = ids.split("\n|,| ");
        int have=0;
        for (int i = 0; i < split.length; i++) {
            String msg = zhongShiToNxstoryService.CompareList(split[i]);
            if (msg.length()>3){
                have+=1;
            }
            result.append(msg);
        }
        System.out.println("一共"+have+"本不同");
        return new JXResult(true, ApiConstant.StatusCode.OK, result.toString());
        JXResult result = taDuService.addTaDuChapterByGetChapterList();
        return result;
    }*/

    @GetMapping(value = "/queryDontHave")
    @ApiOperation(value = "查看数据库中,不存在景像书库的章节", notes = "查看数据库中,不存在景像书库的章节")
    public JXResult queryDontHave() throws Exception {
        JXResult result = taDuService.queryDontHave();
        return result;
    }

    @GetMapping(value = "/addTaDuChapter")
    @ApiOperation(value = "向塔读测试环境推送章节", notes = "向塔读测试环境推送章节")
    public JXResult addTaDuChapter() throws Exception {
        JXResult result = taDuService.addTaDuChapter();
        return result;
    }

    @GetMapping(value = "/addTaDuChapterBySelective")
    @ApiOperation(value = "向塔读确认是否存在后往塔读测试环境推送章节", notes = "向塔读确认是否存在后往塔读测试环境推送章节")
    public JXResult addTaDuChapterBySelective() throws Exception {
        JXResult result = taDuService.addTaDuChapterBySelective();
        return result;
    }


    @GetMapping(value = "/setCsChapterInfoIdByTimeTaDu")
    @ApiOperation(value = "将景像的章节id改为time表中的part_id", notes = "将景像的章节id改为time表中的part_id")
    public JXResult setCsChapterInfoIdByTimeTaDu() throws Exception {
        //查出塔读授权的书列表,遍历
        //在遍历中,调用塔读的查询章节目录接口
        //
        String flag = taDuService.setCsChapterInfoIdByTimeTaDu();
        if (flag.length() < 10) {
            return new JXResult(true, ApiConstant.StatusCode.OK, flag);
        }
        return new JXResult(false, ApiConstant.StatusCode.ERROR, flag);
    }

    @GetMapping(value = "/addBookByTaDu")
    @ApiOperation(value = "将景像的授权书单添加到塔读测试环境", notes = "将景像的授权书单添加到塔读测试环境")
    public JXResult addBookByTaDu() throws Exception {
        //查出塔读授权的书列表,遍历
        //在遍历中,调用塔读的查询章节目录接口
        //
        JXResult result = taDuService.addBookByTaDu();
        return result;
    }

    @GetMapping(value = "/updateChapterBySerialization")
    @ApiOperation(value = "取出景像连载中的数据,根据章节排序更新到塔读",notes ="取出景像连载中的数据,根据章节排序更新到塔读" )
    public JXResult updateChapterBySerialization() throws Exception {
        JXResult result = taDuService.updateChapterBySerialization();
        return result;
    }

    @GetMapping(value = "/updateChapterByBookid")
    @ApiOperation(value = "根据书id向塔读更新章节",notes ="根据书号向塔读更新章节" )
    public JXResult updateChapterByBookid(@ApiParam("景像的书id") @RequestParam("bookid")Integer bookid) throws Exception {
        JXResult result = taDuService.updateChapterByBookid(bookid);
        return result;
    }

    @GetMapping(value = "/updateChapterByBookidAndChapterSortBetwn")
    @ApiOperation(value = "根据书id和章节区间向塔读更新章节",notes ="根据书id和章节区间向塔读更新章节" )
    public JXResult updateChapterByBookidAndChapterSortBetwn(@ApiParam("景像的书id") @RequestParam("bookid")Integer bookid,
                                                             @ApiParam("开始章节序号") @RequestParam("startsort")Integer startsort,
                                                             @ApiParam("解术章节序号") @RequestParam("endsort")Integer endsort) throws Exception {
        JXResult result = taDuService.updateChapterByBookidAndChapterSortBetwn(bookid,startsort,endsort);
        return result;
    }
    @GetMapping(value = "/测试字数")
    @ApiOperation(value = "根据章节id获取字数和内容",notes ="根据章节id获取字数和内容" )
    public JXResult TestWords(@ApiParam("景像的书id") @RequestParam("csBookid") Integer csBookid,
            @ApiParam("景像的章节id") @RequestParam("csChapterid") Integer csChapterid) throws Exception {
        JXResult result = taDuService.TestWords(csBookid,csChapterid);
        return result;
    }

   /* @GetMapping(value = "/sortInconsistent")
    @ApiOperation(value = "找出这本书在塔读和景像章节名与排序不一致的",notes ="找出这本书在塔读和景像章节名与排序不一致的" )
    public JXResult sortInconsistent(@ApiParam("景像的书id") @RequestParam("csBookid") Integer csBookid) throws Exception {
        JXResult result = taDuService.sortInconsistent(csBookid);
        return result;
    }*/
}
