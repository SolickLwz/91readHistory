package com.nine.one.yuedu.read.controller;

import com.nine.one.yuedu.read.config.ApiConstant;
import com.nine.one.yuedu.read.config.JXResult;
import com.nine.one.yuedu.read.entity.Bangdan;
import com.nine.one.yuedu.read.entity.BangdanBooks;
import com.nine.one.yuedu.read.service.BangdanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/91yuedu/bangdan")
@Api(tags = "榜单接口", value = "榜单接口")
public class BangdanController {

    @Resource(name = "bangdanService")
    private BangdanService bangdanService;


    /**
     * 添加榜单
     *
     * @param bangdan
     */
    @PostMapping(value = "/add")
    @ApiOperation(value = "添加榜单", notes = "添加榜单")
    public JXResult addBandan(@RequestBody @ApiParam(value = "榜单对象") Bangdan bangdan) {
        bangdanService.addBandan(bangdan);
        return new JXResult(true, ApiConstant.StatusCode.OK, "添加成功");
    }

    /**
     * 删除榜单
     *
     * @param id
     */
    @DeleteMapping(value = "/del/{id}")
    @ApiOperation(value = "删除榜单", notes = "删除榜单")
    public JXResult delBangdanById(@PathVariable @ApiParam(value = "id") Integer id) {
        bangdanService.delBangdanById(id);
        return new JXResult(true, ApiConstant.StatusCode.OK, "删除成功");
    }


    @GetMapping(value = "/list")
    @ApiOperation(value = "获得榜单列表", notes = "获得榜单列表")
    public JXResult getBandanList() {
        return new JXResult(true, ApiConstant.StatusCode.OK, "请求成功", bangdanService.getBangdanList());
    }

    /**
     * 像榜单里面放书
     *
     * @param bangdanBooks
     */
    @PostMapping(value = "/books/add")
    @ApiOperation(value = "向榜单中添加书籍", notes = "向榜单中添加书籍")
    public JXResult addBangdanBooks(@RequestBody @ApiParam(value = "榜单书对象") BangdanBooks bangdanBooks) {
        bangdanService.addBangdanBooks(bangdanBooks);
        return new JXResult(true, ApiConstant.StatusCode.OK, "添加成功");
    }


    /**
     * 删除榜单里面的书
     *
     * @param id
     */
    @DeleteMapping(value = "/books/del/{id}")
    @ApiOperation(value = "删除榜单中的书", notes = "删除榜单中的书")
    public JXResult delBangdanBooksById(@PathVariable @ApiParam(value = "榜单书对象的ID(主键)") Integer id) {
        bangdanService.delBangdanBooksById(id);
        return new JXResult(true, ApiConstant.StatusCode.OK, "删除成功");
    }

    /**
     * 更改排序
     *
     * @param id
     * @param option
     */
    @PutMapping(value = "/books/sequence/{id}/{option}")
    @ApiOperation(value = "排序榜单中的书", notes = "排序榜单中的书")
    public JXResult updateBooksSequenceById(@PathVariable @ApiParam(value = "需要操作的榜单书对象的ID(主键)") Integer id,
                                            @PathVariable @ApiParam(value = "1:上移,2:下移") Integer option) {
        bangdanService.updateBooksSequenceById(id, option);
        return new JXResult(true, ApiConstant.StatusCode.OK, "操作成功");
    }


    /**
     * 根据榜单ID查询榜单中书的列表,并分页
     *
     * @param bangdanId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/books/list/{bangdanId}/{pageIndex}/{pageSize}")
    @ApiOperation(value = "获得榜单中书的列表根据榜单ID", notes = "获得榜单中书的列表根据榜单ID")
    public JXResult getBangdanBooksByBangdanIdAndPage(@PathVariable @ApiParam(value = "榜单ID") Integer bangdanId,
                                                      @PathVariable @ApiParam(value = "起始页") Integer pageIndex,
                                                      @PathVariable @ApiParam(value = "每页显示的数量") Integer pageSize) {
        return new JXResult(true, ApiConstant.StatusCode.OK, "请求成功"
                , bangdanService.getBangdanBooksByBangdanIdAndPage(bangdanId, pageIndex, pageSize));
    }


}
