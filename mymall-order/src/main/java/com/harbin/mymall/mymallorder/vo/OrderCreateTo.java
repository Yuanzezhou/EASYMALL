package com.harbin.mymall.mymallorder.vo;

import com.harbin.mymall.mymallorder.entity.OrderEntity;
import com.harbin.mymall.mymallorder.entity.OrderItemEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderCreateTo {
 
    // 订单
    private OrderEntity order;
 
    // 订单项
    private List<OrderItemEntity> orderItems;
 
    // 订单应付的价格
    private BigDecimal payPrice;
 
    //运费
    private BigDecimal fare;
}