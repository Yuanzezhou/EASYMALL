package com.harbin.mymall.mymallorder.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

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
////    @RabbitListener(queues = "order.release.order.queue")
////    public void handle(Message message){
////
////    }

    @Bean
    public Exchange orderEventExchange() {
        return new TopicExchange("order-event-exchange", true, false);
    }

    @Bean
    public Queue orderReleaseStockQueue() {
        return new Queue("order.release.order.queue", true, false, false);
    }


    @Bean
    public Queue orderDelayQueue() {
        // String name, boolean durable, boolean exclusive, boolean autoDelete,
        //			@Nullable Map<String, Object> arguments
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "order-event-exchange");    //死信交换机
        arguments.put("x-dead-letter-routing-key", "order.release.order");        //死信路由
        arguments.put("x-message-ttl", 30000*5);
        return new Queue("order.delay.queue", true, false, false, arguments);
    }


    @Bean
    public Binding orderCreateOrderBinding() {
        return new Binding("order.delay.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.create.order",
                new HashMap<>());
    }

    @Bean
    public Binding orderReleaseOrderBinding() {
        return new Binding("order.release.order.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.release.order",
                new HashMap<>());
    }

    @Bean
    public Binding orderReleaseStockBinding() {
        return new Binding("stock.release.stock.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.release.stock",
                new HashMap<>());
    }

//    @Bean
//    public Binding orderLockedBinding() {
//        return new Binding("order.delay.queue",
//                Binding.DestinationType.QUEUE,
//                "order-event-exchange",
//                "order.locked",
//                new HashMap<>());
//    }


    @Bean
    public Queue orderSeckillOrderQueue(){
        return new Queue("order.seckill.order.queue", true, false, false);
    }

    @Bean
    public Binding orderSeckillOrderBinding() {
        return new Binding("order.seckill.order.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.seckill.order",
                new HashMap<>());
    }
}
