package com.nine.one.yuedu.read.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 安全配置类
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * 在原来的密码加密中常用的技术为SHA或MD5
     * 然而以上两种技术的加密更容易被破解,所以在此我们采用spring security中的BCryptPasswordEncoder进行加密
     * 但是spring boot为了减少spring security的配置文件,他的依赖会直接拦截所有的访问
     * 但是我们今天进行验证并不是想用spring security完成
     * 我们使用spring security仅仅是想要使用它的密码加密技术
     * 所以以下的配置是为了放行所有的请求
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable();
    }
}

