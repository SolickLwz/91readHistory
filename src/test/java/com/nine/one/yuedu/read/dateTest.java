package com.nine.one.yuedu.read;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Author:李王柱
 * 2020/10/9
 */
public class dateTest {
    public static void main(String[] args) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat MM = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat simpleDateFormatByDay = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat Month = new SimpleDateFormat("MM");

        String format = MM.format(new Date());
        String thatTimeString=format+"-01 00:00:00";//得到现在月初时间

        Calendar cal = Calendar.getInstance();
        cal.setTime(simpleDateFormat.parse(thatTimeString)); //设置时间
        cal.add(Calendar.MONTH, -1);  //在当前时间基础上减去一个月,得到上月月初时间
        Date startTime = cal.getTime();
        System.out.println(simpleDateFormat.format(startTime));

        cal.setTime(simpleDateFormat.parse(thatTimeString)); //再次设置时间
        cal.add(Calendar.SECOND, -1);  //在当前时间基础上减一秒,得到上月月末时间
        Date endTime = cal.getTime();
        System.out.println(simpleDateFormat.format(endTime));

        System.out.println(Month.format(new Date()));
    }
}
