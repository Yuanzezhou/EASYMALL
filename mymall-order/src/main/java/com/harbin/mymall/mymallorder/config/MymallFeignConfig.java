package com.harbin.mymall.mymallorder.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Yuanzz
 * @creat 2021-03-08-12:59
 */


@Configuration
public class MymallFeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor(){
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if(requestAttributes != null){
                    HttpServletRequest request = requestAttributes.getRequest();
                    if(request != null){
                        String cookie = request.getHeader("Cookie");
                        //给feign构造的新请求，加上原来请求的cookie数据。
                        requestTemplate.header("Cookie",cookie);
                    }
                }
            }
        };
    }
}
