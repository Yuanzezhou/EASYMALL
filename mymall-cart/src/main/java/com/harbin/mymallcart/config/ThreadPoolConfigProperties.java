package com.harbin.mymallcart.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Yuanzz
 * @creat 2021-03-03-18:46
 */

@ConfigurationProperties(prefix = "easymall.thread")
@Component
@Data
public class ThreadPoolConfigProperties {
    private Integer core;
    private Integer maxSize;
    private Integer keepAliveTime;
}
