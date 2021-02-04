package com.nine.one.yuedu.read.utils.Enable;

import com.nine.one.yuedu.read.config.FormateConstant;
import com.nine.one.yuedu.read.entity.AllowFree;
import com.nine.one.yuedu.read.entity.Reminder;
import com.nine.one.yuedu.read.mapper.AllowFreeMapper;
import com.nine.one.yuedu.read.mapper.ReminderMapper;
import com.nine.one.yuedu.read.utils.DateMonthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * Author:李王柱
 * 2020/12/9
 */
@Configuration
@EnableScheduling
public class Reward {
    @Autowired
    private ReminderMapper reminderMapper;
    @Autowired
    private AllowFreeMapper allowFreeMapper;
    //添加定时任务,每个月1号凌晨1点
    @Scheduled(cron = "0 0 1 1 * ?")
    /*@Scheduled(cron = "0 26 15 * * ?")*/
    public void reward() throws ParseException {
        //获取上个月得到打赏的作者

        Date start= DateMonthUtil.obtainEarlyOnAmount(new Date(),-1);//上月初
        Date end= DateMonthUtil.obtainEndOnAmount(new Date(),-1);//上月末

        List<Reminder> Renameds =reminderMapper.selectAuthorRewardByPreviousMonth(start,end);
        for (Reminder oneRenamed:Renameds){
            AllowFree allowFreeBySelect = new AllowFree();
            allowFreeBySelect.setCreatyearandmonth(FormateConstant.YEAR_MONTH.format(start));
            allowFreeBySelect.setBookname(oneRenamed.getBookname());
            if (null==allowFreeMapper.selectOne(allowFreeBySelect)){
                allowFreeMapper.insertSelective(allowFreeBySelect);
            }
            //为这本书的结算奖励增加一半的打赏余额
            AllowFree allowFree = allowFreeMapper.selectOne(allowFreeBySelect);
            Integer reward = allowFree.getReward();
            Integer increase=oneRenamed.getReminderval().intValue()/200;
            allowFree.setReward(reward+increase);
            allowFreeMapper.updateByPrimaryKeySelective(allowFree);
        }
    }



    //添加定时任务,每个月1号零点1分
    /*@Scheduled(cron = "0 1 0 1 * ?")
    public void reward() throws ParseException {
        String todayString = FormateConstant.YEAR_MONTH.format(new Date());//得到今天的时间
        String thatTimeString=todayString+"-01 00:00:00";//得到现在月初时间

        Calendar cal = Calendar.getInstance();
        cal.setTime(FormateConstant.SIMPLE_DATE_FORMAT.parse(thatTimeString));//设置当前月初时间
        cal.add(Calendar.MONTH, -1);//在当前时间基础上减去一个月,得到上月月初时间
        Date startTime = cal.getTime();

        cal.setTime(simpleDateFormat.parse(thatTimeString));//再次设置时间
        cal.add(Calendar.SECOND, -1);//在当前时间基础上减一秒,得到上月月末时间
        Date endTime = cal.getTime();
        String endTimeEndString = MM.format(endTime);
        bookids=auditChapterMapper.getLastMonthInformation(startTime,endTime);//找到在上个月有创作的章节,并且返回书id
        //获取上个月得到打赏的作者

        Date start= DateMonthUtil.obtainEarlyOnAmount(new Date(),-1);//上月初
        Date end= DateMonthUtil.obtainEndOnAmount(new Date(),-1);//上月末

        List<Reminder> Renameds =reminderMapper.selectAuthorRewardByPreviousMonth(start,end);
        for (Reminder oneRenamed:Renameds){
            AllowFree allowFreeBySelect = new AllowFree();
            allowFreeBySelect.setCreatyearandmonth(FormateConstant.YEAR_MONTH.format(start));
            allowFreeBySelect.setBookname(oneRenamed.getBookname());
            if (null==allowFreeMapper.selectOne(allowFreeBySelect)){
                allowFreeMapper.insertSelective(allowFreeBySelect);
            }
            //为这本书的结算奖励增加一半的打赏余额
            AllowFree allowFree = allowFreeMapper.selectOne(allowFreeBySelect);
            Integer reward = allowFree.getReward();
            Integer increase=oneRenamed.getReminderval().intValue()/200;
            allowFree.setReward(reward+increase);
            allowFreeMapper.updateByPrimaryKeySelective(allowFree);
        }
    }*/
}
