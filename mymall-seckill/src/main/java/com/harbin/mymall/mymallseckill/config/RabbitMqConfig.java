package com.harbin.mymall.mymallseckill.config;

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

    /**
     * 还可以配置几个回调，来确认Mq服务器收到客户端的消息，以及消费者收到Mq服务器的消息。见视频323的某一刻
     */
}
