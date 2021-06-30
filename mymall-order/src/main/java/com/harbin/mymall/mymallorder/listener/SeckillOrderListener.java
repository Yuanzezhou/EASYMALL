package com.harbin.mymall.mymallorder.listener;

import com.harbin.common.to.mq.SeckillOrderTo;
import com.harbin.mymall.mymallorder.service.OrderService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Yuanzz
 * @creat 2021-03-12-23:53
 */


@Slf4j
@Component
@RabbitListener(queues = {"order.seckill.order.queue"})
public class SeckillOrderListener {
    @Autowired
    private OrderService orderService;

    @RabbitHandler
    public void listener(SeckillOrderTo seckillOrder, Channel channel, Message message) throws IOException {
        try {
            log.info("准备创建秒杀单的详细信息。。。");
            orderService.createSeckillOrder(seckillOrder);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            // 修改失败 拒绝消息 使消息重新入队
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }
}
