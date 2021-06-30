package com.harbin.mymall.mymallcoupon.vo;

import lombok.Data;

import java.math.BigDecimal;


import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Yuanzz
 * @creat 2021-03-11-16:48
 */

@Data
public class SeckillSkuVo {
    private Long id;
    /**
     * 活动id
     */
    private Long promotionId;
    /**
     * 活动场次id
     */
    private Long promotionSessionId;
    /**
     * 商品id
     */
    private Long skuId;
    /**
     * 秒杀价格
     */
    private BigDecimal seckillPrice;
    /**
     * 秒杀总量
     */
    private Integer seckillCount;
    /**
     * 每人限购数量
     */
    private Integer seckillLimit;
    /**
     * 排序
     */
    private Integer seckillSort;

}
