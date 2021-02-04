package com.nine.one.yuedu.read.controller;

import com.nine.one.yuedu.read.config.ApiConstant;
import com.nine.one.yuedu.read.config.JXResult;
import com.nine.one.yuedu.read.entity.CpAuth;
import com.nine.one.yuedu.read.service.CpAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/91yuedu/cp/auth")
@Api(tags = "书籍授权接口", value = "书籍授权接口")
public class CpAuthController {

    @Resource(name = "cpAuthService")
    private CpAuthService cpAuthService;
    @Autowired
    private HttpServletRequest request;


    /**
     * 添加授权方
     *
     * @param cpAuth
     */
    @PostMapping(value = "add/cpAuth")
    @ApiOperation(value = "添加授权CP方")
    public JXResult addCpAuth(@RequestBody @ApiParam(value = "CP方对象") CpAuth cpAuth) {
        cpAuthService.addCpAuth(cpAuth);
        return new JXResult(true, ApiConstant.StatusCode.OK, "添加成功");
    }

    /**
     * 删除授权方
     *
     * @param id
     */
    @DeleteMapping(value = "delete/cpAuth/{id}")
    @ApiOperation(value = "删除授权CP方")
    public JXResult delCpAuthById(@PathVariable @ApiParam("cp方Id") Integer id) {
        cpAuthService.delCpAuthById(id);
        return new JXResult(true, ApiConstant.StatusCode.OK, "删除失败");
    }

    /**
     * 获取Cp授权方的列表
     *
     * @return
     */
    @GetMapping(value = "get/cp/list")
    @ApiOperation(value = "获取cp方列表")
    public JXResult getCpAuthList() {
        return new JXResult(true, ApiConstant.StatusCode.OK, "请求成功", cpAuthService.getCpAuthList());
    }


    /**
     * 根据授权方的ID,获取授权的书籍列表
     *
     * @param cpAuthId
     * @return
     */
    @GetMapping(value = "get/cp/book/list/{cpAuthId}/{pageIndex}/{pageSize}")
    @ApiOperation(value = "根据CP授权的ID分页获取书籍列表信息")
    public JXResult getCpAuthBookList(@PathVariable @ApiParam("cp授权ID") Integer cpAuthId,
                                      @PathVariable @ApiParam("当前页") Integer pageIndex,
                                      @PathVariable @ApiParam("每页显示数量") Integer pageSize) {
        return new JXResult(true, ApiConstant.StatusCode.OK, "请求成功", cpAuthService.getCpAuthBookList(cpAuthId, pageIndex, pageSize));
    }


    /**
     * 根据授权方的ID,获取授权的书籍ID
     *
     * @param cpAuthId
     * @return
     */
    @GetMapping(value = "get/cp/booId/list/{cpAuthId}")
    @ApiOperation(value = "根据CP授权的ID获取书籍ID列表")
    public JXResult getCpAuthBookIdList(@PathVariable @ApiParam("cp授权ID") Integer cpAuthId) {
        JXResult jxResult = cpAuthService.checkKeyAndParam(request);
        if (!jxResult.isFlag()) {
            return jxResult;
        }
        return new JXResult(true, ApiConstant.StatusCode.OK, "请求成功", cpAuthService.getCpAuthBookIdList(cpAuthId));
    }


    /**
     * 插入多个
     *
     * @param cpAuthId
     * @param bookIds
     */
    @PostMapping(value = "add/cp/auth/book/{cpAuthId}")
    @ApiOperation(value = "向cp授权的列表中添加书籍")
    public JXResult addCpAuthBook(@PathVariable @ApiParam("cp授权ID") Integer cpAuthId,
                                  @RequestParam(value = "bookIds") @ApiParam("书籍ID数组") String bookIds) {
        return cpAuthService.addCpAuthBook(cpAuthId, bookIds);

    }


    /**
     * 删除授权的书
     *
     * @param id
     */
    @DeleteMapping(value = "delete/cp/auth/book/{id}")
    @ApiOperation(value = "删除CP授权书籍")
    public JXResult deleteCpAuthBook(@PathVariable @ApiParam("cp授权书籍对象的ID") Integer id) {
        cpAuthService.deleteCpAuthBook(id);
        return new JXResult(true, ApiConstant.StatusCode.OK, "删除成功");
    }

    /**
     * 通过书籍ID获取书籍详情
     *
     * @param bookId
     * @return
     */
    @GetMapping(value = "get/cp/bookInfo/{bookId}")
    @ApiOperation(value = "根据书籍ID获取书籍详情")
    public JXResult getBookInfoByBookId(@PathVariable @ApiParam(value = "书籍ID") Integer bookId) {
        JXResult jxResult = cpAuthService.checkKeyAndParam(request);
        if (!jxResult.isFlag()) {
            return jxResult;
        }
        return new JXResult(true, ApiConstant.StatusCode.OK, "请求成功", cpAuthService.getBookInfoByBookId(bookId));
    }


    /**
     * 通过
     *
     * @param bookId
     * @return
     */
    @GetMapping(value = "get/cp/chapter/{bookId}")
    @ApiOperation(value = "根据书籍ID获取章节列表")
    public JXResult getChapterInfoList(@PathVariable @ApiParam(value = "书籍ID") Integer bookId) {
        JXResult jxResult = cpAuthService.checkKeyAndParam(request);
        if (!jxResult.isFlag()) {
            return jxResult;
        }
        return new JXResult(true, ApiConstant.StatusCode.OK, "请求成功", cpAuthService.getChapterInfoList(bookId));
    }


    /**
     * 根据书籍ID和章节ID去获取章节的内容
     *
     * @param bookId
     * @param chapterId
     * @return
     */
    @GetMapping(value = "get/cp/content/{bookId}/{chapterId}")
    @ApiOperation(value = "根据书籍ID和章节ID获取章节内容")
    public JXResult getChapterContentByBookIdAndChapterId(@PathVariable @ApiParam(value = "书籍ID") Integer bookId,
                                                          @PathVariable @ApiParam(value = "章节Id") Integer chapterId) {
        JXResult jxResult = cpAuthService.checkKeyAndParam(request);
        if (!jxResult.isFlag()) {
            return jxResult;
        }
        return new JXResult(true, ApiConstant.StatusCode.OK, "请求成功", cpAuthService.getChapterContentByBookIdAndChapterId(bookId, chapterId));
    }


}
