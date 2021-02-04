package com.nine.one.yuedu.read.interceptoe;

import com.nine.one.yuedu.read.config.ApiConstant;
import com.nine.one.yuedu.read.config.JXResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author wangyuliang
 * 本类是服务器异常的拦截类
 */
@ControllerAdvice
public class ServerErrorInterceptor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @ExceptionHandler(RuntimeException.class) //拦截所有运行时的全局异常
    @ResponseBody //返回 JOSN
    public JXResult test(Exception e) {

        logger.error("系统异常 ", e);
        return new JXResult(false, ApiConstant.StatusCode.SERVERERROR,
                "服务器开小差了,快去禀报我的开发者吧!");
    }
}
