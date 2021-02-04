package com.nine.one.yuedu.read.mapper;

import com.nine.one.yuedu.read.entity.CsBookinfo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.MyMapper;

import java.util.List;

public interface CsBookinfoMapper extends MyMapper<CsBookinfo> {
    List<Integer> selectCpBookIdInfo(@Param("cpid") int cpid);

    List<CsBookinfo> selectSerialAndOnlineByCpid(@Param("cpid") int cpid);
}