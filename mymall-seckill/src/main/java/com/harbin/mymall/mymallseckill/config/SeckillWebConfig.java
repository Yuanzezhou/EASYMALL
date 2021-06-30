package com.harbin.mymall.mymallseckill.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description: 把拦截器配置到spring中，否则用户登录拦截器不生效
 * @Author: WangTianShun
 * @Date: 2020/12/16 13:44
 * @Version 1.0
 */
@Configuration
public class SeckillWebConfig implements WebMvcConfigurer {

    @Autowired
    LoginUserInterceptor loginUserInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginUserInterceptor).addPathPatterns("/**");
    }
}