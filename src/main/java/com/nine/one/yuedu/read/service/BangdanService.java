package com.nine.one.yuedu.read.service;

import com.github.pagehelper.PageInfo;
import com.nine.one.yuedu.read.entity.Bangdan;
import com.nine.one.yuedu.read.entity.BangdanBooks;
import com.nine.one.yuedu.read.entity.vo.BangdanBooksVO;

import java.util.List;

/**
 * @author wangyuliang
 * 榜单服务
 */
public interface BangdanService {

    /**
     * 添加榜单
     *
     * @param bangdan
     */
    void addBandan(Bangdan bangdan);

    /**
     * 删除榜单
     *
     * @param id
     */
    void delBangdanById(Integer id);

    /**
     * 获得榜单列表
     * @return
     */
    List<Bangdan> getBangdanList();

    /**
     * 向榜单里面放书
     *
     * @param bangdanBooks
     */
    void addBangdanBooks(BangdanBooks bangdanBooks);


    /**
     * 删除榜单里面的书
     *
     * @param id
     */
    void delBangdanBooksById(Integer id);

    /**
     * 更改排序
     *
     * @param id
     * @param option
     */
    void updateBooksSequenceById(Integer id, Integer option);


    /**
     * 根据榜单ID查询榜单中书的列表,并分页
     *
     * @param bangdanId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    PageInfo<BangdanBooksVO> getBangdanBooksByBangdanIdAndPage(Integer bangdanId, Integer pageIndex, Integer pageSize);
}
