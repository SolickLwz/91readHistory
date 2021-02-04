package com.nine.one.yuedu.read.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.wxpay.sdk.WXPayUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.nine.one.yuedu.read.entity.RechargeGold;
import com.nine.one.yuedu.read.entity.RechargeRecord;
import com.nine.one.yuedu.read.entity.User;
import com.nine.one.yuedu.read.mapper.RechargeGoldMapper;
import com.nine.one.yuedu.read.mapper.RechargeRecordMapper;
import com.nine.one.yuedu.read.mapper.UserMapper;
import com.nine.one.yuedu.read.service.PayService;
import com.nine.one.yuedu.read.utils.GX.HttpClientUtilsGX;
import com.nine.one.yuedu.read.utils.HttpClientUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Author:李王柱
 * 2020/10/30
 */
@Controller
@Api(tags = "微信支付相关",value = "微信支付相关")
public class weixinPayController {
    @Autowired
    private RechargeRecordMapper rechargeRecordMapper;
@Autowired
private RechargeGoldMapper rechargeGoldMapper;
@Autowired
private PayService payService;
@Autowired
private UserMapper userMapper;

    @RequestMapping("/91yuedu/api/wxpay")
    @ApiOperation("微信充值统一下单")
    public @ResponseBody Object wxpay(HttpServletRequest request, HttpServletResponse response,
                                      @RequestParam("uid")Integer uid,
                                      @RequestParam("total_fee")String total_fee) throws Exception {//@RequestParam("out_trade_no")String out_trade_no,

        String str= UUID.randomUUID().toString().replaceAll("-", "");//32位订单号不要前端传过来了
        //在这里直接生成充值记录表,获取用户数据,状态为0
        RechargeRecord rechargeRecord = new RechargeRecord();
        rechargeRecord.setRechargeDesc("client微信充值");
        rechargeRecord.setUid(uid);
        rechargeRecord.setRechargeTime(new Date());
        rechargeRecord.setRechargeStatus("0");
        rechargeRecord.setRechargeNo(str);
        rechargeRecord.setRechargeMoney(Long.valueOf(total_fee));
        //在这里查询金币对应的价格表,获取Gold
        //甚么都不和我说(一块钱多少币,充多少能送百分之几,分几个档位)那就不弄表了,一块钱100阅币
        Long rechargeGold=Long.valueOf(total_fee)*100;
        rechargeRecord.setRechargeGold(rechargeGold);
        rechargeRecordMapper.insertSelective(rechargeRecord);

        //调用微信支付的统一下单API接口

        // 该接口要求上传的参数是xml格式的字符串，响应的参数也是xml格式的字符串

        //创建一个map集合
        HashMap<String, String> requestDataMap = new HashMap<>();
        requestDataMap.put("appid","wxc6bc09b1e4756df5");//微信支付分配的公众账号ID（企业号corpid即为此appId）wxbdb72ed718668891
        requestDataMap.put("mch_id","1371239502");// 微信支付分配的商户号sub_mch_id 1488669112 //1371239502
        requestDataMap.put("nonce_str", WXPayUtil.generateNonceStr());//随机字符串，长度要求在32位以内。推荐随机数生成算法
        requestDataMap.put("body","微信充值");//商品简单描述，该字段请按照规范传递，具体请见参数规定
        requestDataMap.put("out_trade_no",str);//商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|* 且在同一个商户号下唯一。详见商户订单号

        //将充值金额单位从元变成分
        BigDecimal bigDecimal = new BigDecimal(total_fee);
        BigDecimal multiply = bigDecimal.multiply(new BigDecimal(100));
        int totalFee = multiply.intValue();
        requestDataMap.put("total_fee",String.valueOf(totalFee));
        requestDataMap.put("spbill_create_ip","59.110.17.4");//支持IPV4和IPV6两种格式的IP地址。用户的客户端IP127.0.0.1
        requestDataMap.put("notify_url","http://client.91yuedu.com/");//异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。https://api.nxstory.com/
        requestDataMap.put("trade_type","NATIVE");
        requestDataMap.put("product_id",str);//trade_type=NATIVE时，此参数必传。此参数为二维码中包含的商品ID，商户自行定义。

        String signature = WXPayUtil.generateSignature(requestDataMap, "da48e18d6b51466db7eb97f6ef27667b");
        requestDataMap.put("sign",signature);

        //将map集合的请求参数转换为xml格式的字符串
        String requestDataXml = WXPayUtil.mapToXml(requestDataMap);

        //将xml格式的请求参数传递给对应的接口

        String responseDataXml = HttpClientUtilsGX.doPostByXml("https://api.mch.weixin.qq.com/pay/unifiedorder", requestDataXml);
        //System.out.println(new String(responseDataXml.getBytes("iso-8859-1"), "utf-8"));
        //将响应的xml格式字符串转换为map集合
        Map<String, String> responseDataMap = WXPayUtil.xmlToMap(responseDataXml);

        //JSONObject jsonObject = JSONObject.parseObject(responseDataMap.toString());
        //判断通信标识
        String returnCode = responseDataMap.get("return_code");
        //String returnCode = jsonObject.getString("return_code");

        if (StringUtils.equals("SUCCESS",returnCode)){

            //获取code_url字符串
            String codeUrl = responseDataMap.get("code_url");

            //将code_url生成一个二维码
            Map<EncodeHintType,Object> encodeHintTypeObjectMap = new HashMap<EncodeHintType, Object>();
            encodeHintTypeObjectMap.put(EncodeHintType.CHARACTER_SET,"UTF-8");

            //创建一个矩阵对象
            BitMatrix bitMatrix = new MultiFormatWriter().encode(codeUrl, BarcodeFormat.QR_CODE,200,200,encodeHintTypeObjectMap);

            OutputStream outputStream = response.getOutputStream();

            //将矩阵对象转换为图片
            MatrixToImageWriter.writeToStream(bitMatrix,"png",outputStream);

            outputStream.flush();
            outputStream.close();
        }
        //在返回之前调用60秒的循环,查询订单状态,如果订单状态为充值成功,就停止循环,改变订单状态为充值成功,为用户充值余额
        /*String selfUrl="http://59.110.17.4:9998/91yuedu/api/queryAndUpdateStatusBy120s?recharge_no="+str;
        *//*String selfUrl="http://localhost:9004/91yuedu/api/queryAndUpdateStatusBy120s?recharge_no="+str;*//*
        HttpClientUtilsGX.doGet(selfUrl);*/
        String selfUrl="http://59.110.17.4:9998/91yuedu/api/queryAndUpdateStatusBy120s?recharge_no="+str;
        /*String selfUrl="http://localhost:9004/91yuedu/api/queryAndUpdateStatusBy120s?recharge_no="+str;*/
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClientUtilsGX.doGet(selfUrl);
                } catch (Exception e) {
                    System.out.println("调用线程异常");
                }
                /* HttpClientUtils.doGet(selfUrl);*/
            }
        });
        thread.run();//在新线程异步执行

        return responseDataMap;
    }

    @GetMapping("/91yuedu/api/queryStatus")
    @ApiOperation("调用微信的查询订单接口")
    public @ResponseBody Object queryStatus(HttpServletRequest request, HttpServletResponse response,
                                    @RequestParam("recharge_no") @ApiParam(value = "准确订单号") String recharge_no) throws Exception {//@RequestParam("out_trade_no")String out_trade_no,
        Object resultObj= payService.weixinQueryStatus(recharge_no);
        return resultObj;
    }

    @GetMapping("/91yuedu/api/queryAndUpdateStatusBy120s")
    @ApiOperation("每3秒查询一次这个订单,如果查询结果是充值成功,就进行一系列操作")
    public void queryAndUpdateStatusBy120s(@RequestParam("recharge_no") @ApiParam(value = "准确订单号") String recharge_no) throws Exception {//@RequestParam("out_trade_no")String out_trade_no,
        for (int i=0;i<40;i++){
            Object resultObj= payService.weixinQueryStatus(recharge_no);
            if (StringUtils.equals("支付成功",resultObj.toString())){
                //如果支付成功了,将订单改为已经充值成功,为用户的账户添加余额
                RechargeRecord rechargeRecordBySelect = new RechargeRecord();
                rechargeRecordBySelect.setRechargeNo(recharge_no);
                rechargeRecordBySelect.setRechargeStatus("0");
                RechargeRecord rechargeRecord = rechargeRecordMapper.selectOne(rechargeRecordBySelect);
                rechargeRecord.setRechargeStatus("1");
                //如果不是当天的订单,就给他添加充值掉单补充订单的
                rechargeRecordMapper.updateByPrimaryKeySelective(rechargeRecord);

                User userBySelect = new User();
                userBySelect.setId(rechargeRecord.getUid());
                User user = userMapper.selectOne(userBySelect);
                Long balance = user.getBalance();
                user.setBalance(balance+rechargeRecord.getRechargeGold());
                userMapper.updateByPrimaryKeySelective(user);
                return;
            }
            Thread.sleep(3*1000);
        }
    }

    @GetMapping("/91yuedu/api/userQuery")
    @ApiOperation("用户查询最近的订单状态")
    public @ResponseBody Object weixinUserQuery(@RequestParam("uid") @ApiParam(value = "二维码界面收到的用户id") Integer uid,
                                          @RequestParam("total_fee") @ApiParam(value = "二维码界面收到的用户付款金额") Long total_fee) throws Exception {//@RequestParam("out_trade_no")String out_trade_no,
        RechargeRecord RecordBySelect= payService.weixinUserQuery(uid,total_fee);
        String recharge_no=RecordBySelect.getRechargeNo();
        Object resultObj= payService.weixinQueryStatus(recharge_no);
        return resultObj;
    }
}
