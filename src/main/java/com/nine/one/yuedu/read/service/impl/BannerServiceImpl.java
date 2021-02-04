package com.nine.one.yuedu.read.service.impl;

import com.nine.one.yuedu.read.entity.Banner;
import com.nine.one.yuedu.read.entity.BannerImgs;
import com.nine.one.yuedu.read.mapper.BannerImgsMapper;
import com.nine.one.yuedu.read.mapper.BannerMapper;
import com.nine.one.yuedu.read.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service(value = "bannerService")
public class BannerServiceImpl implements BannerService {

    @Autowired
    private BannerMapper bannerMapper;
    @Autowired
    private BannerImgsMapper bannerImgsMapper;


    @Override
    public void addBanner(Banner banner) {
        bannerMapper.insertSelective(banner);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBannerById(Integer id) {
        Example example = new Example(BannerImgs.class);
        example.createCriteria().andEqualTo("bannerId", id);
        bannerImgsMapper.deleteByExample(example);
        bannerMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<Banner> getBanners() {
        return bannerMapper.selectAll();
    }

    @Override
    public List<BannerImgs> getBannerImagesByBannerId(Integer bannerId) {
        List<BannerImgs> bannerImgs = bannerImgsMapper.selectByBannerId(bannerId);
        return bannerImgs;
    }

    @Override
    public void addBannerImagesToBanner(BannerImgs bannerImgs) {
        bannerImgs.setCreateTime(new Date());
        bannerImgsMapper.insertSelective(bannerImgs);
    }

    @Override
    public void delBannerImages(Integer id) {
        bannerImgsMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void updateBannerImages(BannerImgs bannerImgs) {
        bannerImgsMapper.updateByPrimaryKeySelective(bannerImgs);
    }

    @Override
    public BannerImgs findBannerImagesById(Integer id) {
        return bannerImgsMapper.selectByPrimaryKey(id);
    }

    @Override
    public void sortBannerImagesById(Integer id, Integer option) {
        BannerImgs bannerImgs = bannerImgsMapper.selectByPrimaryKey(id);
        Integer sort = bannerImgs.getSort();
        //下移
        if (option == 1) {
            sort = sort + 1;
        } else {
            if (sort != 1) {
                sort = sort - 1;
            }
        }
        BannerImgs bannerImgsUP = new BannerImgs();
        bannerImgsUP.setId(id);
        bannerImgsUP.setSort(sort);
        bannerImgsMapper.updateByPrimaryKeySelective(bannerImgsUP);
    }
}
