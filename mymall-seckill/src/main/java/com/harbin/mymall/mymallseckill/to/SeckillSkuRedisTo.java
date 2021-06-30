package com.harbin.mymall.mymallseckill.to;

import com.harbin.mymall.mymallseckill.vo.SkuInfoVo;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Yuanzz
 * @creat 2021-03-11-17:23
 */

@Data
public class SeckillSkuRedisTo {


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

    private SkuInfoVo skuInfoVo ;

    //秒杀开始时间
    private Long startTime;

    //秒杀结束时间
    private Long endTime;

    //
    private String randomCode;

}
