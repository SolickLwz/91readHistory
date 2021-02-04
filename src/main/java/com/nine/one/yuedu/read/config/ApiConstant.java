package com.nine.one.yuedu.read.config;

/**
 * @author 不忘初心
 * 本类用于配置常用的静态常量
 */
public class ApiConstant {

    //常用状态码
    public class StatusCode {
        public static final int OK = 20000;//成功
        public static final int ERROR = 20001;//失败
        public static final int LOGINERROR = 20002;//用户名或密码错误
        public static final int ACCESSERROR = 20003;//权限不足
        public static final int REMOTEERROR = 20004;//远程调用失败
        public static final int REPERROR = 20005;//重复操作
        public static final int SERVERERROR = 50000;//服务器异常
    }

    //用户类型
    public class UserType {
        public static final int GENNERAL_USER = 1;//普通用户
        public static final int VIP_MONTH_USER = 2;//包月VIP用户
        public static final int VIP_QUARTER_USER = 3;//包季VIP用户
        public static final int VIP_YEAR_USER = 4;//包年VIP用户
    }

    //小说类型
    public class BookType {
        public static final int VIP_BOOK = 1;//VIP小说
        public static final int NO_VIP_BOOK = 0;//非VIP小说
    }

    //时间间隔
    public class Timer {
        public static final long ONE_MIN = 60;//一分钟
        public static final long TEN_MIN = 60 * 10;//十分钟
        public static final long ONE_DAY = 86400;//一天
        public static final long THREE_DAY = 86400 * 3;//三天
        public static final long THREE_DAY_MSEC = 86400 * 1000 * 3;//三天的毫秒值
        public static final long ONE_DAY_MSEC = 86400 * 1000;//一天的毫秒值
    }

    //常用的一些配置
    public class Config {
        //书籍封面地址前缀
        public static final String BASE_PATH = "http://jxxs-app-img.oss-cn-hangzhou.aliyuncs.com";
        //默认头像
        public static final String DEFULT_HEAD = "http://jxxs-app-img.oss-cn-hangzhou.aliyuncs.com/images/2019/07/05/15623071073296766100.png";
    }

}
