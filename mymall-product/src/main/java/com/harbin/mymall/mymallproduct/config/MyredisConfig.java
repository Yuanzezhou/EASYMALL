package com.harbin.mymall.mymallproduct.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author Yuanzz
 * @creat 2021-02-25-20:38
 */

@Configuration
public class MyredisConfig {
    @Bean(destroyMethod="shutdown")
    public RedissonClient redisson() throws IOException {
    //测试是否注入了RedissonClient，
        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.112.100:6379");
        RedissonClient redisson = Redisson.create(config);
    return redisson;
    }
}
