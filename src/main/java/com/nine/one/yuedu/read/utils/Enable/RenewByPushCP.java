package com.nine.one.yuedu.read.utils.Enable;

import com.nine.one.yuedu.read.mapper.BookInfoMapper;
import com.nine.one.yuedu.read.service.BookInfoService;
import com.nine.one.yuedu.read.service.FlyreadService;
import com.nine.one.yuedu.read.service.NeteaseBookService;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

/**
 * Author:李王柱
 * 2020/12/8
 */
@Configuration
@EnableScheduling
public class RenewByPushCP {
    @Autowired
    private NeteaseBookService neteaseBookService;
    @Autowired
    private BookInfoMapper bookInfoMapper;
    @Autowired
    private FlyreadService flyreadService;
    //添加定时任务,凌晨5点40
    @Scheduled(cron = "0 40 5 * * ?")
    public void renewByPushCP() throws Exception {
        //获取连载中的书,分别调用接口,查询连载中的书id,接收参数:
        //1.网易的接口是,调用方法推送这本书的内容,没授权的不执行;cpid是12

        List<Integer> bookids= bookInfoMapper.selectBookIdOnCpidAndCompletestate(12,1);//1连载
        for (Integer bookid: bookids){
            neteaseBookService.addCharpterForNetease(bookid.toString());
        }


        //2.飞读的cpid是26
        String key= flyreadService.login();//登录飞读
        List<Integer> FlyReadBookids= bookInfoMapper.selectBookIdOnCpidAndCompletestate(26,1);
        for (Integer flyReadBookid: FlyReadBookids){
            System.out.println(flyReadBookid+"开始");
            //flyreadService.addChapterByBookId(flyReadBookid,key);
            String msg= flyreadService.getUpdateInfo(flyReadBookid.toString(),key);
            net.sf.json.JSONObject msgJson = net.sf.json.JSONObject.fromObject(msg);
            //判断通信标识
            String msgCode = msgJson.getString("message");
            if (!StringUtils.equals("Success",msgCode)){
                return ;
            }
            JSONObject result = msgJson.getJSONObject("result");
            String isSuccess= flyreadService.putChapterBySelective(flyReadBookid.toString(),result.getString("chaptername"),key);
            if (! isSuccess.equals("成功")){
                return ;
            }
            System.out.println(flyReadBookid+"完毕");
        }

    }
}
