package com.harbin.mymall.mymallorder.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderSubmitVo {
    // 收获地址的id
    private Long addrId;
 
    // 支付方式
    private Integer payType;
 
    //无需提交需要购买的商品，去购物车在获取一遍
 
    // 防重令牌
    private String orderToken;
 
    // 应付价格 验价
    private BigDecimal payPrice;
 
    // 订单备注
    private String note;
 
}