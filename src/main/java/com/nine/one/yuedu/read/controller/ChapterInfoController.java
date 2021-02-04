package com.nine.one.yuedu.read.controller;

import com.nine.one.yuedu.read.config.ApiConstant;
import com.nine.one.yuedu.read.config.JXResult;
import com.nine.one.yuedu.read.entity.ChapterInfo;
import com.nine.one.yuedu.read.entity.vo.ChapterInfoVO;
import com.nine.one.yuedu.read.service.ChapterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Map;

@Api(value = "章节接口", tags = "章节接口")
@RestController
@RequestMapping(value = "/91yuedu/chapterInfo")
public class ChapterInfoController {

    @Resource(name = "chapterService")
    private ChapterService chapterService;

    /**
     * 上传完整的一本书,并且分离出章节
     *
     * @param bookId
     * @param file
     * @return
     */
    @PostMapping(value = "/upload/book/{bookId}")
    @ApiOperation(value = "上传完整的一本书,并自动分离出章节", notes = "上传完整的一本书,并自动分离出章节")
    public JXResult uploadTxtChangeChapterList(@PathVariable @ApiParam(value = "书籍ID") Integer bookId
            , @RequestPart @ApiParam(value = "上传的txt文件") MultipartFile file) {
        Map<String, Object> result = chapterService.uploadTxtChangeChapterList(bookId, file);
        if ((boolean) result.get("flag")) {
            return new JXResult(true, ApiConstant.StatusCode.OK, "上传成功", result);
        }
        return new JXResult(false, ApiConstant.StatusCode.SERVERERROR, "上传失败", result);
    }

    /**
     * 添加章节
     *
     * @param chapterInfoVo
     */
    @PostMapping(value = "/add")
    @ApiOperation(value = "添加章节接口", notes = "添加章节接口")
    public JXResult addChapter(@RequestBody @ApiParam(value = "章节对象", required = true) ChapterInfoVO chapterInfoVo) {
        ChapterInfo chapterInfo = chapterInfoVo.getChapterInfo();
        String content = chapterInfoVo.getContent();
        chapterService.addChapter(chapterInfo, content);
        return new JXResult(true, ApiConstant.StatusCode.OK, "添加成功");
    }

    /**
     * 根据ID删除章节
     *
     * @param id
     */
    @DeleteMapping(value = "/del/{id}")
    @ApiOperation(value = "删除章节", notes = "删除章节")
    public JXResult delChapterById(@PathVariable @ApiParam(value = "章节ID主键") Integer id) {
        chapterService.delChapterById(id);
        return new JXResult(true, ApiConstant.StatusCode.OK, "删除成功");
    }

    /**
     * 修改章节
     *
     * @param chapterInfoVo
     */
    @PutMapping(value = "/update")
    @ApiOperation(value = "修改章节接口", notes = "修改章节接口")
    public JXResult updateChapter(@RequestBody @ApiParam(value = "章节对象", required = true) ChapterInfoVO chapterInfoVo) {
        ChapterInfo chapterInfo = chapterInfoVo.getChapterInfo();
        String content = chapterInfoVo.getContent();
        chapterService.updateChapter(chapterInfo, content);
        return new JXResult(true, ApiConstant.StatusCode.OK, "修改成功");
    }

    /**
     * 根据图书ID和章节ID获得章节的详情
     *
     * @param bookId
     * @param chapterId
     * @return
     */
    @GetMapping(value = "/find/{bookId}/{chapterId}")
    @ApiOperation(value = "根据书籍ID和章节ID获得章节详情", notes = "根据书籍ID和章节ID获得章节详情")
    public JXResult getChapterInfoByBookIdAndChapterId(@PathVariable @ApiParam(value = "图书Id") Integer bookId,
                                                       @PathVariable @ApiParam(value = "章节Id") Integer chapterId,
                                                       @RequestParam(value = "type", required = false, defaultValue = "") @ApiParam(value = "调用的类型") String type) {
        return new JXResult(true, ApiConstant.StatusCode.OK, "请求成功"
                , chapterService.getChapterInfoByBookIdAndChapterId(bookId, chapterId, type));
    }

    /**
     * 根据书籍的ID获得这本书的章节列表,分页,并以不同的排序方式排序
     *
     * @param bookId
     * @param pageIndex
     * @param pageSize
     * @param sort
     * @return
     */
    @GetMapping(value = "/list/{bookId}/{pageIndex}/{pageSize}")
    public JXResult getChapterListByBookIdPageAndSort(@PathVariable @ApiParam(value = "书籍Id") Integer bookId,
                                                      @PathVariable @ApiParam(value = "开始页") Integer pageIndex,
                                                      @PathVariable @ApiParam(value = "每页显示数量") Integer pageSize,
                                                      @RequestParam(value = "sort", required = false, defaultValue = "1") @ApiParam(value = "排序:1.正序,2倒序") Integer sort) {
        return new JXResult(true, ApiConstant.StatusCode.OK, "请求成功",
                chapterService.getChapterListByBookIdPageAndSort(bookId, pageIndex, pageSize, sort));
    }

    /**
     * 备份章节
     *
     * @param bookStr
     * @param Prefix
     */
    @PostMapping(value = "/backUp")
    @ApiOperation(value = "备份章节接口", notes = "备份章节接口")
    public JXResult addChapter(@ApiParam(value = "书籍ID") @RequestParam("bookStr")String bookStr,
                               @ApiParam(value = "前缀ID") @RequestParam("Prefix")String Prefix) {
        /*ChapterInfo chapterInfo = chapterInfoVo.getChapterInfo();
        String content = chapterInfoVo.getContent();
        chapterService.addChapter(chapterInfo, content);*/
        StringBuilder isMsg=new StringBuilder();
        String[] split = bookStr.split("[,]");
        for (int i = 0; i < split.length; i++) {

            String msg =chapterService.BackUpByPrefix(Integer.parseInt(split[i]),Prefix);
            if (!msg.equals("通信成功")) {
                isMsg.append(msg);
            }
        }
        if (isMsg.length()<1){
            isMsg.append("通信成功!");
        }
        return new JXResult(true, ApiConstant.StatusCode.OK, isMsg.toString());
    }


}
