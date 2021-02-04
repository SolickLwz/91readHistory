package com.nine.one.yuedu.read.controller;

import com.nine.one.yuedu.read.config.ApiConstant;
import com.nine.one.yuedu.read.config.JXResult;
import com.nine.one.yuedu.read.entity.Banner;
import com.nine.one.yuedu.read.entity.BannerImgs;
import com.nine.one.yuedu.read.service.BannerService;
import com.nine.one.yuedu.read.service.ChapterService;
import com.nine.one.yuedu.read.utils.AliyunOSSUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping(value = "/91yuedu/banner")
@Api(tags = "轮播图接口", value = "轮播图接口")
public class BannerController {

    @Resource(name = "bannerService")
    private BannerService bannerService;
    @Autowired
    private AliyunOSSUtil aliyunOSSUtil;


    @PostMapping(value = "/imgs/upload")
    @ApiOperation(value = "上传轮播图", notes = "上传轮播图")
    public JXResult uploadHead(@RequestParam("file") @ApiParam(value = "上传的文件") MultipartFile uploadFile) {
        AliyunOSSUtil.FileUploadResult uploadResult = aliyunOSSUtil.upload(uploadFile);
        return new JXResult(true, ApiConstant.StatusCode.OK, "上传成功", uploadResult);
    }




    /**
     * 添加banner位置
     *
     * @param banner
     */
    @PostMapping(value = "/add")
    @ApiOperation(value = "添加banner位", notes = "添加banner位")
    public JXResult addBanner(@ApiParam(value = "banner位对象", required = true) @RequestBody Banner banner) {
        bannerService.addBanner(banner);
        return new JXResult(true, ApiConstant.StatusCode.OK, "添加成功");
    }

    /**
     * 删除banner位置
     *
     * @param id
     */
    @DeleteMapping(value = "/del/{id}")
    @ApiOperation(value = "删除banner位置", notes = "删除banner位置")
    public JXResult delBannerById(@ApiParam(value = "banner位的id") @PathVariable Integer id) {
        bannerService.delBannerById(id);
        return new JXResult(true, ApiConstant.StatusCode.OK, "删除成功");

    }

    /**
     * 查看所有的banner位置
     *
     * @return
     */
    @GetMapping(value = "/list")
    @ApiOperation(value = "获得banner位列表", notes = "获得banner位列表")
    public JXResult getBanners() {
        return new JXResult(true, ApiConstant.StatusCode.OK, "请求成功", bannerService.getBanners());
    }

    /**
     * 查看banner位的所有banner图
     *
     * @param bannerId
     * @return
     */
    @GetMapping(value = "/imgs/list/{bannerId}")
    @ApiOperation(value = "获得banner位中banner图列表", notes = "获得banner位中banner图列表")
    public JXResult getBannerImagesByBannerId(@PathVariable @ApiParam(value = "banner位的ID") Integer bannerId) {
        return new JXResult(true, ApiConstant.StatusCode.OK, "请求成功", bannerService.getBannerImagesByBannerId(bannerId));

    }

    /**
     * 向banner位添加banner图
     *
     * @param bannerImgs
     */
    @PostMapping(value = "/imgs/add")
    @ApiOperation(value = "向banner位中添加banner图", notes = "向banner位中添加banner图")
    public JXResult addBannerImagesToBanner(@RequestBody @ApiParam(value = "banner图对象") BannerImgs bannerImgs) {
        bannerService.addBannerImagesToBanner(bannerImgs);
        return new JXResult(true, ApiConstant.StatusCode.OK, "添加成功");
    }


    /**
     * 删除banner位中的图片,根据图片的ID删除
     *
     * @param id
     */
    @DeleteMapping(value = "/imgs/del/{id}")
    @ApiOperation(value = "删除banner图", notes = "删除banner图")
    public JXResult delBannerImages(@PathVariable @ApiParam(value = "banner图的id") Integer id) {
        bannerService.delBannerImages(id);
        return new JXResult(true, ApiConstant.StatusCode.OK, "删除成功");
    }

    /**
     * 修改banner位中banner图的信息
     *
     * @param bannerImgs
     */
    @PutMapping(value = "/imgs/update")
    @ApiOperation(value = "修改banner图", notes = "修改banner图")
    public JXResult updateBannerImages(@RequestBody @ApiParam(value = "banner对象") BannerImgs bannerImgs) {
        bannerService.updateBannerImages(bannerImgs);
        return new JXResult(true, ApiConstant.StatusCode.OK, "修改成功");

    }

    /**
     * 根据banner图的id查询它的详情
     *
     * @param id
     */
    @GetMapping(value = "/imgs/find/{id}")
    @ApiOperation(value = "根据banner图的id查询它的详情",notes = "根据banner图的id查询它的详情")
    public JXResult findBannerImagesById(@ApiParam("banner图的ID") @PathVariable Integer id) {
        return new JXResult(true, ApiConstant.StatusCode.OK, "请求成功", bannerService.findBannerImagesById(id));
    }

    @PutMapping(value = "/imgs/sort/{option}/{id}")
    @ApiOperation(value = "根据ID给banner图排序",notes = "根据ID给banner图排序")
    public JXResult sortBannerImagesById(@PathVariable @ApiParam(value = "排序方式:1.下移,2上移") Integer option,
                                         @PathVariable @ApiParam(value = "id")Integer id){
        bannerService.sortBannerImagesById(id,option);
        return new JXResult(true,ApiConstant.StatusCode.OK,"排序成功");
    }

}
