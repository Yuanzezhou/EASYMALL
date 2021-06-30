package com.harbin.mymall.mymallseckill.service;

import com.harbin.mymall.mymallseckill.to.SeckillSkuRedisTo;

import java.util.List;

/**
 * @author Yuanzz
 * @creat 2021-03-11-16:55
 */
public interface SeckillService {
    void uploadSeckillSkuLatest3Days();

    List<SeckillSkuRedisTo> getCurrentSeckillSkus();

    SeckillSkuRedisTo getSkuSecKillInfo(Long skuId);

    String kill(String killId, String key, Integer num);
}
