package com.nine.one.yuedu.read.service;

import com.nine.one.yuedu.read.entity.Banner;
import com.nine.one.yuedu.read.entity.BannerImgs;

import java.util.List;

/**
 * @author wangyuliang
 * 轮播图服务
 */
public interface BannerService {

    /**
     * 添加banner位置
     *
     * @param banner
     */
    void addBanner(Banner banner);

    /**
     * 删除banner位置
     *
     * @param id
     */
    void delBannerById(Integer id);

    /**
     * 查看所有的banner位置
     *
     * @return
     */
    List<Banner> getBanners();

    /**
     * 查看banner位的所有banner图
     *
     * @param bannerId
     * @return
     */
    List<BannerImgs> getBannerImagesByBannerId(Integer bannerId);

    /**
     * 向banner位添加banner图
     *
     * @param bannerImgs
     */
    void addBannerImagesToBanner( BannerImgs bannerImgs);


    /**
     * 删除banner位中的图片,根据图片的ID删除
     *
     * @param id
     */
    void delBannerImages(Integer id);

    /**
     * 修改banner位中banner图的信息
     *
     * @param bannerImgs
     */
    void updateBannerImages(BannerImgs bannerImgs);

    /**
     * 根据banner图的id查询它的详情
     *
     * @param id
     */
    BannerImgs findBannerImagesById(Integer id);

    /**
     * 根据ID排序
     * @param id
     * @param option
     */
    void sortBannerImagesById(Integer id,Integer option);


}
