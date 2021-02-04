package com.nine.one.yuedu.read.utils.Enable;

import com.nine.one.yuedu.read.config.FormateConstant;
import com.nine.one.yuedu.read.entity.RechargeRecord;
import com.nine.one.yuedu.read.mapper.RechargeRecordMapper;
import com.nine.one.yuedu.read.service.PayService;
import com.nine.one.yuedu.read.utils.GX.HttpClientUtilsGX;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Author:李王柱
 * 2020/11/23
 */
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class Recharge {
    @Autowired
    private PayService payService;
    @Autowired
    private RechargeRecordMapper rechargeRecordMapper;

    //3.添加定时任务,每天5点检查昨天充值成功,但是没有给用户添加上余额的订单
    @Scheduled(cron = "0 0 5 * * ?")
    public void Recharge() throws Exception {
        //获取昨天凌晨5点和今天凌晨5点的时间
        String EarlFormat = FormateConstant.YEAR_MONTH_DAY.format(new Date());//今天的day
        String EarlMorning=EarlFormat+" 05:00:00";//今天的详细时间,5点
        Date EarlMorningDate = FormateConstant.SIMPLE_DATE_FORMAT.parse(EarlMorning);//得到今天5点的时间对象

        //获取昨天的时间
        //获取明天的时间date
        Calendar calendar = new GregorianCalendar();calendar.setTime(EarlMorningDate);calendar.add(calendar.DATE,-1);
        Date specificDate= calendar.getTime();

        //获取昨天5点到今天凌晨5点的未成功充值订单
        /*List<RechargeRecord> needConfirmation = rechargeRecordMapper.selectByDateBetweenAndStatus(FormateConstant.SIMPLE_DATE_FORMAT.parse("2020-11-02 05:00:00"),specificDate,"0");*/
        List<RechargeRecord> needConfirmation = rechargeRecordMapper.selectByDateBetweenAndStatus(EarlMorningDate,specificDate,"0");
        //遍历,循环中都是没有给用户充值的,如果有查询结果是充值成功的,就给用户补上
        for (RechargeRecord rechargeRecord:needConfirmation){
            String rechargeNo = rechargeRecord.getRechargeNo();
            Object o = payService.weixinQueryStatus(rechargeNo);
            String result=o.toString();
            if (StringUtils.equals("支付成功",result)){
                System.out.println("有充值成功但是没给用户添加余额的,准备操作"+rechargeRecord.getId());
                String selfUrl="http://59.110.17.4:9998/91yuedu/api/queryAndUpdateStatusBy120s?recharge_no="+rechargeNo;
                HttpClientUtilsGX.doGet(selfUrl);
            }
        }
    }

}
