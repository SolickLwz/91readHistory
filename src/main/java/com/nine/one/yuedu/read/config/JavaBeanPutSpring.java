package com.nine.one.yuedu.read.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author wangyuliang
 *
 * 本类用于把常用的类注入到Spring
 */
@Configuration
public class JavaBeanPutSpring {

    /**
     * 注入密码加密的类
     *
     * @return
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
