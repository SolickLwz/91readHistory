package com.nine.one.yuedu.read.utils.Enable;

import com.nine.one.yuedu.read.config.JXResult;
import com.nine.one.yuedu.read.service.TaDuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Author:李王柱
 * 2020/10/27
 */
@Configuration
@EnableScheduling
public class TaDuUpdate {
    @Autowired
    private TaDuService taDuService;
    //添加定时任务,凌晨三点
    @Scheduled(cron = "0 0 3 * * ?")
    public void updateBySerialize() throws Exception {
        taDuService.updateChapterBySerialization();
    }
}
