package com.nine.one.yuedu.read.service;

import com.nine.one.yuedu.read.entity.RechargeRecord;

public interface PayService {
    //根据订单号查询充值状态String,"支付成功"是成功
    Object weixinQueryStatus(String recharge_no) throws Exception;

    RechargeRecord weixinUserQuery(Integer uid, Long total_fee);
}
