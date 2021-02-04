package com.nine.one.yuedu.read.utils.Enable;

import com.nine.one.yuedu.read.cp.ZhongShiToNxstoryService;
import com.nine.one.yuedu.read.entity.BookInfo;
import com.nine.one.yuedu.read.mapper.BookInfoMapper;
import com.nine.one.yuedu.read.service.BookInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Author:李王柱
 * 2020/11/9
 */
@Configuration
@EnableScheduling
public class zhongshiToJingXiangUpdate {
    @Autowired
    ZhongShiToNxstoryService zhongShiToNxstoryService;
    @Autowired
    BookInfoService bookInfoService;
    @Autowired
    BookInfoMapper bookInfoMapper;

    //添加定时任务,每天凌晨5点
    @Scheduled(cron = "0 0 5 * * ?")
    public void updateByComplete(){//将连载中的 报联书籍 更新到景像
        Example example = new Example(BookInfo.class);
        example.createCriteria().andEqualTo("completeState",1);
        List<BookInfo> bookInfos = bookInfoMapper.selectByExample(example);
        for (BookInfo bookInfo:bookInfos){
            //获取报联中视连载中的书籍
            zhongShiToNxstoryService.updateChapterByBookId(bookInfo.getId());
        }
    }
}
