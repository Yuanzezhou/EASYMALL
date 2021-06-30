package com.harbin.mymall.mymallware.vo;

import lombok.Data;

import java.util.List;

/**
 * @author Yuanzz
 * @creat 2021-03-08-19:37
 */

@Data
public class WareSkuLockVo {

    private String orderSn; //订单号

    List<OrderItemVo> locks;//所有需要锁的商品
}
