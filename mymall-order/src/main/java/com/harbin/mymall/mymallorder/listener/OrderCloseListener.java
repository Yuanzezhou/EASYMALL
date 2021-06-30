package com.harbin.mymall.mymallorder.listener;

import com.harbin.mymall.mymallorder.entity.OrderEntity;
import com.harbin.mymall.mymallorder.service.OrderService;
import com.rabbitmq.client.AMQP.*;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Yuanzz
 * @creat 2021-03-10-18:06
 */

@RabbitListener(queues = {"order.release.order.queue"})
@Component
public class OrderCloseListener {

    @Autowired
    OrderService orderService;

    @RabbitHandler
    public void listener(OrderEntity entity, Channel channel, Message message) throws IOException {
        System.out.println("收到过期的订单信息：准备关闭订单"+entity.getOrderSn());
        try{
            orderService.closeOrder(entity);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        }catch(Exception e){
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        }
    }

}
