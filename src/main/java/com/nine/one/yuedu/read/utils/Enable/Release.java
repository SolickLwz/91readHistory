package com.nine.one.yuedu.read.utils.Enable;

import com.nine.one.yuedu.read.entity.AuditChapter;
import com.nine.one.yuedu.read.mapper.AuditChapterMapper;
import com.nine.one.yuedu.read.service.AuthorCreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Author:李王柱
 * 2020/9/22
 */
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class Release {
    @Autowired
    private AuditChapterMapper auditChapterMapper;

    @Autowired
    private AuthorCreationService authorCreationService;
    //3.添加定时任务
    @Scheduled(cron = "0 0 4 * * ?")
    //或直接指定时间间隔，例如：5秒
   //@Scheduled(fixedRate=5000)
    private void configureTasks() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat smalFrormat = new SimpleDateFormat("yyyy-MM-dd");

        //获取今天的时间date
        String thatDateString = smalFrormat.format(new Date());//获取今天0点
        Date nowaday = smalFrormat.parse(thatDateString);//解析今天0点

        //获取明天的时间date
        Calendar calendar = new GregorianCalendar();calendar.setTime(new Date());calendar.add(calendar.DATE,1);
        Date specific= calendar.getTime();
        String tomorrowString= smalFrormat.format(specific);//获取明天0点
        Date tomorrow= smalFrormat.parse(tomorrowString);//解析明天0点

        //System.out.println(simpleDateFormat.format(nowaday));
        //System.out.println(simpleDateFormat.format(tomorrow));
        //调用数据库,查出创建时间大于今天小于明天,并且章节状态是已经过审的章节list
        List<AuditChapter> PassList= auditChapterMapper.selectTiming(nowaday,tomorrow);
        //遍历list,调用service将它们上架
        for (AuditChapter auditChapter:PassList){
            System.out.println("即将上架:"+PassList.size());
            //System.out.println(auditChapter.getBookId());
           // System.out.println(auditChapter.getChapterId());
            authorCreationService.passed(Integer.toString(auditChapter.getBookId()),Integer.toString(auditChapter.getChapterId()),0);
        }
        //List<AuditChapter> AllowReleaseList= auditChapterMapper.selectAllowRelease();
        //System.err.println("执行静态定时任务时间: " + LocalDateTime.now());
    }

}
