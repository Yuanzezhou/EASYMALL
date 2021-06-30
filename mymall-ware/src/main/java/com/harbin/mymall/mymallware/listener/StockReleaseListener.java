package com.harbin.mymall.mymallware.listener;

import com.harbin.common.to.mq.OrderTo;
import com.harbin.common.to.mq.StockLockedTo;
import com.harbin.mymall.mymallware.service.WareSkuService;
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
 * @creat 2021-03-10-15:22
 */
/**
 * 库存自动解锁。
 * 1）库存锁定成功，接下来的业务调用失败导致订单回滚，此时锁定的库存也要自动解锁。
 */

@Slf4j
@Component
@RabbitListener(queues = {"stock.release.stock.queue"})
public class StockReleaseListener {

    @Autowired
    WareSkuService wareSkuService;

    @RabbitHandler
    public void handleStockLockedRelease(StockLockedTo to, Message message, Channel channel) throws IOException {
        System.out.println("收到解锁库存的消息！");
        try{
            wareSkuService.unLock(to);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        }catch (Exception e){
//            System.out.println("错误。。。："+e.getMessage());
            e.printStackTrace();
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        }
    }

    @RabbitHandler
    public void handleOrderCloseRelease(OrderTo to, Message message, Channel channel) throws IOException {
        log.info("************************订单关闭准备解锁库存********************************");
        try {
            wareSkuService.unLockStockForOrder(to);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        }
    }
}
