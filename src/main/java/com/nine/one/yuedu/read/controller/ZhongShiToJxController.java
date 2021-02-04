package com.nine.one.yuedu.read.controller;

import com.nine.one.yuedu.read.config.ApiConstant;
import com.nine.one.yuedu.read.config.JXResult;
import com.nine.one.yuedu.read.cp.ZhongShiToNxstoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Api(value = "报联中视到景像后台的书籍同步接口",tags = "报联中视到景像后台的书籍同步接口")
@RequestMapping(value = "/91yuedu/cp/nxstory/")
public class ZhongShiToJxController {

    @PostMapping(value = "/updateChapterName")
    @ApiOperation(value = "根据报联书id,将报联的章节名替换到景像")
    public JXResult updateChapterName(@ApiParam(value = "报联书籍id") @RequestParam("bootStr") Integer zhongshiBookId) {


        String msg = zhongShiToNxstoryService.updateChapterName(zhongshiBookId);
        if (!msg.equals("通信成功")) {
            return new JXResult(false, ApiConstant.StatusCode.REMOTEERROR, msg);
        }
        return new JXResult(true, ApiConstant.StatusCode.OK, "更新成功");
    }

    @PostMapping(value = "/updateChapterContentByDelete")
    @ApiOperation(value = "根据报联书id和章节区间,将报联的内容替换到景像")
    public JXResult updateChapterContentByDelete(@ApiParam(value = "报联书籍id") @RequestParam("bootStr") Integer bookid,
                                                 @ApiParam(value = "开始章节") @RequestParam("start") Integer start,
                                                 @ApiParam(value = "结束章节") @RequestParam("end") Integer end) {


            String msg = zhongShiToNxstoryService.updateChapterContentByDelete(bookid,start,end);
            if (!msg.equals("通信成功")) {
                return new JXResult(false, ApiConstant.StatusCode.REMOTEERROR, msg);
            }

        return new JXResult(true, ApiConstant.StatusCode.OK, "更新成功");
    }

    @Resource(name = "zhongShiToNxstoryService")
    private ZhongShiToNxstoryService zhongShiToNxstoryService;


    @GetMapping(value = "/book")
    @ApiOperation(value = "抓取所有书籍")
    public JXResult getBook() {
        zhongShiToNxstoryService.syncBookToNxstory();
        return new JXResult(true, ApiConstant.StatusCode.OK, "抓取成功");
    }

    @GetMapping(value = "/chapter")
    @ApiOperation(value = "抓取所有章节")
    public JXResult getChapter() {
        zhongShiToNxstoryService.syncChapter();
        return new JXResult(true, ApiConstant.StatusCode.OK, "抓取成功");
    }

    @PostMapping(value = "/updateBookByBookId")
    @ApiOperation(value = "根据书Id串,将书对象添加/更新到景像")
    public JXResult updateBookByBookId(@ApiParam(value = "书籍id,逗号或空格隔开") @RequestParam("bootStr") String bookStr) {
        String[] split = bookStr.split(",| ");
        for (int i = 0; i < split.length; i++) {
            String msg = zhongShiToNxstoryService.updateBookByBookId(Integer.parseInt(split[i]));
            if (!msg.equals("通信成功")) {
                return new JXResult(false, ApiConstant.StatusCode.REMOTEERROR, msg);
            }
        }
        return new JXResult(true, ApiConstant.StatusCode.OK, "更新成功");
    }

    @PostMapping(value = "/updateChapterByBookId")
    @ApiOperation(value = "根据报联中视书Id将章节和内容更新到景像(必须在景像本地执行)")
    public JXResult updateChapterByBookId(@ApiParam(value = "报联中视书籍id,逗号隔开") @RequestParam("bootStr") String bookStr) {
        String[] split = bookStr.split("[,]");
        for (int i = 0; i < split.length; i++) {
            String msg = zhongShiToNxstoryService.updateChapterByBookId(Integer.parseInt(split[i]));
            if (!msg.equals("通信成功")) {
                return new JXResult(false, ApiConstant.StatusCode.REMOTEERROR, msg);
            }
        }
        return new JXResult(true, ApiConstant.StatusCode.OK, "更新成功");
    }

    @PostMapping(value = "/addTaDuToJxtcFromXml")
    @ApiOperation(value = "将xml中的数据添加到塔读临时表中")
    public JXResult addTaDuBookFromXml(@ApiParam(value = "从第几行开始") @RequestParam("start") Integer start,
                                       @ApiParam(value = "到第几行结束") @RequestParam("end") Integer end) throws Exception {
        //先从塔读的书单里面,查出书名为xxx的书,select * from bookinfo where name=xxx,and id in
        //先从塔读授权的书里面,查出书名为xxx的书.拿到书在景像的id
            String msg = zhongShiToNxstoryService.addTaDuToJxtcFromXml(start,end);
            if (msg.length()>3) {
                return new JXResult(false, ApiConstant.StatusCode.REMOTEERROR, msg);
            }
        return new JXResult(true, ApiConstant.StatusCode.OK, "更新成功");
    }

    @PostMapping(value = "/modifyOnlyChapterIdToJxtc")
    @ApiOperation(value = "将临时表中的章节id替换到景像的数据库中")
    public JXResult modifyOnlyChapterIdToJxtc() throws Exception {

        //1,找出给塔读授权的书id
        //2,根据书id找出章节id  (拿到了授权的章节list)
        //3.再去拿到重复的书单,where塔读的list
        //4.遍历授权章节list,如果塔读list章节名一样,就把章节id改成塔读list的

        String msg = zhongShiToNxstoryService.modifyOnlyChapterIdToJxtc();
        if (msg.length()>3) {
            return new JXResult(false, ApiConstant.StatusCode.REMOTEERROR, msg);
        }
        return new JXResult(true, ApiConstant.StatusCode.OK, "更新成功");
    }

    @PostMapping(value = "/deleteJTaduToJxtcFromNoRepeat")
    @ApiOperation(value = "将临时表中,一本书里面不重复的数据删掉,并且记录在temp表")
    public JXResult deleteJTaduToJxtcFromNoRepeat() throws Exception {
        //1,从临时表中获取所有数据
        //2.遍历,删掉上下id中,章节名都不重复的数据
        JXResult msg = zhongShiToNxstoryService.deleteJTaduToJxtcFromNoRepeat();
        return msg;
    }

    @PostMapping(value = "/deleteJTaduToJxtcFromNoBookName")
    @ApiOperation(value = "将临时表中,没有给塔读授权的书都删掉,并且记录在temp表")
    public JXResult deleteJTaduToJxtcFromNoBookName() throws Exception {

        //1,从临时表中获取所有数据
        //2.遍历,删掉上下id中,章节名都不重复的数据

        JXResult msg = zhongShiToNxstoryService.deleteJTaduToJxtcFromNoRepeat();
        return msg;
    }

    @PostMapping(value = "/repair")
    @ApiOperation(value = "将景像本地已经存在的章节和报联比对,为景像补充上缺失的章节对象")
    public JXResult repair() throws Exception {
            String result = zhongShiToNxstoryService.repair();
        return new JXResult(true, ApiConstant.StatusCode.OK, result);
        }

    @PostMapping(value = "/CompareList")
    @ApiOperation(value = "将景像的书的数据库和报联比较,查出景像数据库缺失的")
    public JXResult repair(@ApiParam(value = "景像书籍id串,英文逗号隔开") @RequestParam("ids") String ids) throws Exception {
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
    }
}
