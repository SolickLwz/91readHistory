package com.nine.one.yuedu.read.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nine.one.yuedu.read.entity.Bangdan;
import com.nine.one.yuedu.read.entity.BangdanBooks;
import com.nine.one.yuedu.read.entity.vo.BangdanBooksVO;
import com.nine.one.yuedu.read.mapper.BangdanBooksMapper;
import com.nine.one.yuedu.read.mapper.BangdanMapper;
import com.nine.one.yuedu.read.service.BangdanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service(value = "bangdanService")
public class BangdanServiceImpl  implements BangdanService {

    @Autowired
    private BangdanBooksMapper bangdanBooksMapper;
    @Autowired
    private BangdanMapper bangdanMapper;


    @Override
    public void addBandan(Bangdan bangdan) {
        bangdanMapper.insertSelective(bangdan);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBangdanById(Integer id) {
        //删除所有榜单中的书
        Example example = new Example(BangdanBooks.class);
        example.createCriteria().andEqualTo("bangdanId", id);
        bangdanBooksMapper.deleteByExample(example);
        bangdanMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<Bangdan> getBangdanList() {
        return bangdanMapper.selectAll();
    }

    @Override
    public void addBangdanBooks(BangdanBooks bangdanBooks) {
        bangdanBooks.setCreateTime(new Date());
        bangdanBooks.setUpdateTime(new Date());
        bangdanBooksMapper.insertSelective(bangdanBooks);
    }

    @Override
    public void delBangdanBooksById(Integer id) {
        bangdanBooksMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void updateBooksSequenceById(Integer id, Integer option) {
        BangdanBooks bangdanBooks = bangdanBooksMapper.selectByPrimaryKey(id);
        Integer sort = bangdanBooks.getSort();
        if (option == 1) {
            //往下移动
            sort = sort + 1;
        } else {
            //向上移动
            if (sort != 1) {
                sort = sort - 1;
            }
        }
        BangdanBooks bangdanBooksUp = new BangdanBooks();
        bangdanBooksUp.setId(id);
        bangdanBooksUp.setSort(sort);
        bangdanBooksMapper.updateByPrimaryKeySelective(bangdanBooksUp);
    }

    @Override
    public PageInfo<BangdanBooksVO> getBangdanBooksByBangdanIdAndPage(Integer bangdanId, Integer pageIndex, Integer pageSize) {
        PageHelper.startPage(pageIndex, pageSize);
        List<BangdanBooksVO> bangdanBooksVOS = bangdanBooksMapper.selectBooksByBangdanId(bangdanId);
        PageInfo<BangdanBooksVO> pageInfo = new PageInfo<>(bangdanBooksVOS);
        return pageInfo;
    }
}
