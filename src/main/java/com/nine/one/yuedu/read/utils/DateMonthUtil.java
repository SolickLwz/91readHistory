package com.nine.one.yuedu.read.utils;

import com.nine.one.yuedu.read.config.FormateConstant;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Author:李王柱
 * 2020/12/9
 */
public class DateMonthUtil {
    //获得月初时间
    public static Date obtainEarlyOnAmount(Date date,Integer addMonthTime) throws ParseException {
        String year_month = FormateConstant.YEAR_MONTH.format(date);
        String thatTimeString=year_month+"-01 00:00:00";//得到月初时间

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(FormateConstant.SIMPLE_DATE_FORMAT.parse(thatTimeString));
        calendar.add(Calendar.MONTH,addMonthTime);//根据传入参数增减月份时间
        Date startTime = calendar.getTime();
        return startTime;
    }

    //获得月末时间
    public static Date obtainEndOnAmount(Date date,Integer addMonthTime) throws ParseException {
        String year_month = FormateConstant.YEAR_MONTH.format(date);
        String thatTimeString=year_month+"-01 00:00:00";//得到给定的月初时间

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(FormateConstant.SIMPLE_DATE_FORMAT.parse(thatTimeString));
        calendar.add(Calendar.MONTH,addMonthTime);//根据传入参数增减月份时间
        calendar.add(Calendar.MONTH,1);//给的时间加1个月,获得下个月月初时间
        calendar.add(Calendar.SECOND, -1);  //在下月时间基础上减一秒,得到本月月末时间
        Date startTime = calendar.getTime();
        return startTime;
    }
}
