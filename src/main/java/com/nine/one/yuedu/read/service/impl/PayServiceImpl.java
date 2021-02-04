package com.nine.one.yuedu.read.service.impl;

import com.github.wxpay.sdk.WXPayUtil;
import com.nine.one.yuedu.read.entity.RechargeRecord;
import com.nine.one.yuedu.read.mapper.RechargeRecordMapper;
import com.nine.one.yuedu.read.service.PayService;
import com.nine.one.yuedu.read.utils.GX.HttpClientUtilsGX;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Author:李王柱
 * 2020/11/18
 */
@Service
public class PayServiceImpl implements PayService {
    @Autowired
    private RechargeRecordMapper rechargeRecordMapper;
    @Override
    public Object weixinQueryStatus(String recharge_no) throws Exception {
        //微信的查询接口地址
        String weixinQueryUrl="https://api.mch.weixin.qq.com/pay/orderquery";

        //调用微信查询订单接口

        // 该接口要求上传的参数是xml格式的字符串，响应的参数也是xml格式的字符串

        //创建一个map集合
        HashMap<String, String> requestDataMap = new HashMap<>();
        requestDataMap.put("appid","wxc6bc09b1e4756df5");//微信支付分配的公众账号ID（企业号corpid即为此appId）wxbdb72ed718668891
        requestDataMap.put("mch_id","1371239502");// 微信支付分配的商户号sub_mch_id 1488669112 //1371239502

        requestDataMap.put("out_trade_no",recharge_no);//商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。
        requestDataMap.put("nonce_str", WXPayUtil.generateNonceStr());//随机字符串，长度要求在32位以内。推荐随机数生成算法

        String signature = WXPayUtil.generateSignature(requestDataMap, "da48e18d6b51466db7eb97f6ef27667b");
        requestDataMap.put("sign",signature);

        //将map集合的请求参数转换为xml格式的字符串
        String requestDataXml = WXPayUtil.mapToXml(requestDataMap);

        //将xml格式的请求参数传递给对应的接口

        String responseDataXml = HttpClientUtilsGX.doPostByXml(weixinQueryUrl, requestDataXml);
        //将响应的xml格式字符串转换为map集合
        Map<String, String> responseDataMap = WXPayUtil.xmlToMap(responseDataXml);

        //判断通信标识
        String returnCode = responseDataMap.get("return_code");

        if (StringUtils.equals("SUCCESS",returnCode)){
            //获取trade_state字符串
            String trade_state = responseDataMap.get("trade_state");

            switch (trade_state){
                case "SUCCESS" : return "支付成功";
                case "REFUND": return "转入退款";
                case "NOTPAY" : return "未支付";
                case "CLOSED": return "已关闭";
                case "REVOKED": return "已撤销（付款码支付）";
                case "USERPAYING": return "用户支付中（付款码支付）";
                case "PAYERROR":return "支付失败(其他原因，如银行返回失败)";
            }

        }else {
            //得到的结果不是success的情况下,返回错误信息
            String return_msg = responseDataMap.get("return_msg");
            return return_msg;
        }

        return responseDataMap;
    }

    @Override
    public RechargeRecord weixinUserQuery(Integer uid, Long total_fee) {
        RechargeRecord rechargeRecordBySelect = new RechargeRecord();
        rechargeRecordBySelect.setUid(uid);
        rechargeRecordBySelect.setRechargeMoney(total_fee);
        RechargeRecord byResult= rechargeRecordMapper.selectRecent(uid,total_fee);
        return byResult;
    }
}
