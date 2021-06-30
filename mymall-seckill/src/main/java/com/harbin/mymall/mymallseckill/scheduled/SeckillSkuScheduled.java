package com.harbin.mymall.mymallseckill.scheduled;

import com.harbin.mymall.mymallseckill.service.SeckillService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SeckillSkuScheduled {

    @Autowired
    SeckillService seckillService;

    @Autowired
    RedissonClient redissonClient;


    private final String upload_lock = "seckill:upload:lock";


    //TODO 幂等性处理:加分布式锁
    @Scheduled(cron = "0 * 0 * * ?")
    public void uploadSeckillSkuLatest3Days(){
        //重复上架无需处理
        log.info("上架秒杀的信息......");
        RLock lock = redissonClient.getLock(upload_lock);
        lock.lock(10, TimeUnit.SECONDS);
        try{
            seckillService.uploadSeckillSkuLatest3Days();
        }finally {
            lock.unlock();
        }
    }
}