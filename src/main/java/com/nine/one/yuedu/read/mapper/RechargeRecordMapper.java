package com.nine.one.yuedu.read.mapper;

import com.nine.one.yuedu.read.entity.RechargeRecord;
import tk.mybatis.MyMapper;

import java.util.Date;
import java.util.List;

public interface RechargeRecordMapper extends MyMapper<RechargeRecord> {
    //提供微信用户的最近订单
    RechargeRecord selectRecent(Integer uid, Long total_fee);

    //查询时间区间和某状态的订单,0充值中,1成功
    List<RechargeRecord> selectByDateBetweenAndStatus(Date earlMorningDate, Date specificDate, String status);
}