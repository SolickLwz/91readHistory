package com.nine.one.yuedu.read;

import com.nine.one.yuedu.read.mapper.CsBookinfoMapper;
import com.nine.one.yuedu.read.utils.GX.Format91;
import com.nine.one.yuedu.read.utils.GX.SensitivewordFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Author:李王柱
 * 2020/9/14
 */
public class tst1 {
    public static void main(String[] args) {
        String test="第一千五百四十四章 汤姆章和章的";
        int indexOf = test.indexOf("章 ");
        String str2=test.substring(indexOf+2,test.length());
        System.out.println(str2);
    }

}
