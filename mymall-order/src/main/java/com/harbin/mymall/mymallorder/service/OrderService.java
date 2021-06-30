package com.harbin.mymall.mymallorder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harbin.common.to.mq.SeckillOrderTo;
import com.harbin.common.utils.PageUtils;
import com.harbin.mymall.mymallorder.entity.OrderEntity;
import com.harbin.mymall.mymallorder.vo.*;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 订单
 *
 * @author yuanzz
 * @email 1002271900@qq.com
 * @date 2021-01-14 21:09:13
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);

    OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException;

    SubmitOrderResponseVo submitOrder(OrderSubmitVo submitVo);

    OrderEntity getOrderByOrderSn(String orderSn);

    void closeOrder(OrderEntity entity);

    void createSeckillOrder(SeckillOrderTo seckillOrder);

    //获取当前订单的支付信息
    PayVo getOrderPay(String orderSn);

    PageUtils queryPageWithItems(Map<String, Object> params);

    String handleOrderAfterPay(PayAsyncVo payAsyncVo);
}

