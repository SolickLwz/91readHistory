package com.nine.one.yuedu.read.mapper;

import com.nine.one.yuedu.read.entity.BannerImgs;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.MyMapper;

import java.util.List;

public interface BannerImgsMapper extends MyMapper<BannerImgs> {

    List<BannerImgs> selectByBannerId(@Param("bannerId") Integer bannerId);

}
