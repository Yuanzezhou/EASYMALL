package com.harbin.mymall.mymallmember.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * @author Yuanzz
 * @creat 2021-03-05-20:34
 */
@Configuration
public class MyMallSessionConfig {
    //设置cookie作用域为父域，使得session可以跨服务。
    @Bean
    public CookieSerializer cookieSerializer(){
        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
        cookieSerializer.setDomainName("easymall.com");
        cookieSerializer.setCookieName("EASYSESSION");
        return cookieSerializer;
    }

    //spring-session默认是jdk序列化后存入redis，这时所有序列化的类要实现seriazable。此处配置为json序列化方式
    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer(){
        return new GenericJackson2JsonRedisSerializer();
    }
}

