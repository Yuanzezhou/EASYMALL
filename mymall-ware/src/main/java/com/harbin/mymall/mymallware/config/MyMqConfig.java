package com.harbin.mymall.mymallware.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yuanzz
 * @creat 2021-03-10-12:23
 */

/**
 * 疑问：为什么自动注入会失效，只有通过test类随便调用一个创建交换机或队列的方法后才生效?
 * 要在第一次监听后才会创建这些队列和交换机
 */

@Configuration
public class MyMqConfig {

    //为了能够立即创建以下交换机和队列
//    @RabbitListener(queues = "stock.release.stock.queue")
//    public void handle(Message message){
//
//    }

    @Bean
    public Exchange stockEventExchange() {
        return new TopicExchange("stock-event-exchange", true, false);
    }

    @Bean
    public Queue stockReleaseStockQueue() {
        return new Queue("stock.release.stock.queue", true, false, false);
    }

    @Bean
    public Queue stockDelayQueue() {
        // String name, boolean durable, boolean exclusive, boolean autoDelete,
        //			@Nullable Map<String, Object> arguments
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "stock-event-exchange");
        arguments.put("x-dead-letter-routing-key", "stock.release.stock.queue");
        arguments.put("x-message-ttl", 60000);
        return new Queue("stock.delay.queue", true, false, false, arguments);
    }

    @Bean
    public Binding stockReleaseStockBinding() {
        return new Binding("stock.release.stock.queue",
                Binding.DestinationType.QUEUE,
                "stock-event-exchange",
                "stock.release.#",
                new HashMap<>());
    }

    @Bean
    public Binding orderLockedBinding() {
        return new Binding("stock.delay.queue",
                Binding.DestinationType.QUEUE,
                "stock-event-exchange",
                "stock.locked",
                new HashMap<>());
    }

}
