package com.harbin.mymall.mymallorder.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Yuanzz
 * @creat 2021-03-06-17:03
 */

@Configuration
public class RabbitMqConfig {

    /**
     * Json进行数据序列化
     * @return
     */
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }
}
