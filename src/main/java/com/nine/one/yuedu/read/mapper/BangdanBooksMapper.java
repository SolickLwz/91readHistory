package com.nine.one.yuedu.read.mapper;

import com.nine.one.yuedu.read.entity.BangdanBooks;
import com.nine.one.yuedu.read.entity.vo.BangdanBooksVO;
import tk.mybatis.MyMapper;

import java.util.List;

public interface BangdanBooksMapper extends MyMapper<BangdanBooks> {

    /**
     * 查询书籍列表,通过榜单ID
     * @param bangdanId
     * @return
     */
    List<BangdanBooksVO> selectBooksByBangdanId(Integer bangdanId);
}
