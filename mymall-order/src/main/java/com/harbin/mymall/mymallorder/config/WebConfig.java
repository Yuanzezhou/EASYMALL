package com.harbin.mymall.mymallorder.config;

import com.harbin.mymall.mymallorder.interceptor.LoginUserInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Yuanzz
 * @creat 2021-03-07-21:27
 */

@Configuration
public class WebConfig implements WebMvcConfigurer {

//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/detail.html").setViewName("detail");
//        registry.addViewController("/list.html").setViewName("list");
//        registry.addViewController("/confirm.html").setViewName("confirm");
//        registry.addViewController("/pay.html").setViewName("pay");
//    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginUserInterceptor()).addPathPatterns("/**");
    }
}
