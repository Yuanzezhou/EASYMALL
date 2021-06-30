package com.harbin.mymallcart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Yuanzz
 * @creat 2021-03-03-18:50
 */

@Configuration
//当ThreadPoolConfigProperties没有加载到ioc容器中时，就需要@EnableCon...
//@EnableConfigurationProperties({ThreadPoolConfigProperties.class})
public class ThreadPoolConfig {

    @Bean
    public ThreadPoolExecutor createThreadPool(ThreadPoolConfigProperties pool){
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                pool.getCore(),
                pool.getMaxSize(),
                pool.getKeepAliveTime(),
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(100000),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
        return threadPoolExecutor;
    }
}
