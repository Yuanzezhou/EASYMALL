package com.harbin.mymall.mymallseckill.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.harbin.common.to.mq.SeckillOrderTo;
import com.harbin.common.utils.R;
import com.harbin.common.vo.MemberRespVo;
import com.harbin.mymall.mymallseckill.config.LoginUserInterceptor;
import com.harbin.mymall.mymallseckill.feign.CouponFeignService;
import com.harbin.mymall.mymallseckill.feign.ProductFeignService;
import com.harbin.mymall.mymallseckill.service.SeckillService;
import com.harbin.mymall.mymallseckill.to.SeckillSkuRedisTo;
import com.harbin.mymall.mymallseckill.vo.SeckillSessionWithSkusVo;
import com.harbin.mymall.mymallseckill.vo.SkuInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Yuanzz
 * @creat 2021-03-11-16:55
 */

@Slf4j
@Service
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    CouponFeignService couponFeignService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    ProductFeignService productFeignService;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    RabbitTemplate rabbitTemplate;

    private final String SESSIONS_CACHE_PREFIX = "seckill:sessions:";

    private final String SKUKILL_CACHE_PREFIX = "seckill:skus:";

    private final String SKU_STOCK_SEMAPHORE = "seckill:stock:";//+???????????????

    @Override
    public void uploadSeckillSkuLatest3Days() {
        // 1????????????????????????????????????????????????
        R session = couponFeignService.getLasts3DaySession();
        if (session.getCode() == 0){
            // ????????????
            List<SeckillSessionWithSkusVo> data = session.getData(new TypeReference<List<SeckillSessionWithSkusVo>>(){
            });
            // ?????????redis

            // 1?????????????????????
            saveSessionInfos(data);

            // 2?????????????????????????????????
            saveSessionSkuInfos(data);
        }
    }

    @Override
    public List<SeckillSkuRedisTo> getCurrentSeckillSkus() {
        Set<String> keys = redisTemplate.keys(SESSIONS_CACHE_PREFIX + "*");
        long currentTime = System.currentTimeMillis();
        for (String key : keys) {
            String replace = key.replace(SESSIONS_CACHE_PREFIX, "");
            String[] split = replace.split("_");
            long startTime = Long.parseLong(split[0]);
            long endTime = Long.parseLong(split[1]);
            // ????????????????????????????????????
            if (currentTime > startTime && currentTime < endTime) {
                // ?????????????????????????????????????????????
                List<String> range = redisTemplate.opsForList().range(key, -100, 100);
                BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
                assert range != null;
                List<String> strings = hashOps.multiGet(range);
                if (!CollectionUtils.isEmpty(strings)) {
                    return strings.stream().map(item -> JSON.parseObject(item, SeckillSkuRedisTo.class))
                            .collect(Collectors.toList());
                }
                break;
            }
        }
        return null;
    }

    @Override
    public SeckillSkuRedisTo getSkuSecKillInfo(Long skuId) {
        // 1?????????????????????????????????????????????key
        BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
        Set<String> keys = hashOps.keys();
        if (null != keys){
            //6_4
            String regx = "\\d_" + skuId;
            for (String key : keys) {
                if (Pattern.matches(regx, key)){
                    String json = hashOps.get(key);
                    SeckillSkuRedisTo skuRedisTo = JSON.parseObject(json, SeckillSkuRedisTo.class);

                    //?????????
                    long current = new Date().getTime();
                    if (current >= skuRedisTo.getStartTime() && current <= skuRedisTo.getEndTime()){

                    }else {
                        skuRedisTo.setRandomCode(null);
                    }
                    return skuRedisTo;
                }

            }
        }
        return null;
    }

    @Override
    public String kill(String killId, String key, Integer num) {
        long s1 = System.currentTimeMillis();
        // ??????????????????????????????
        MemberRespVo repsVo = LoginUserInterceptor.loginUser.get();
        // 1????????????????????????????????????
        BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
        String json = hashOps.get(killId);
        if (!StringUtils.isEmpty(json)){
            SeckillSkuRedisTo redis = JSON.parseObject(json, SeckillSkuRedisTo.class);
            // ???????????????
            Long startTime = redis.getStartTime();
            Long endTime = redis.getEndTime();
            long current = new Date().getTime();
            long ttl = endTime - startTime; //??????????????????
            // 1.1????????????????????????
            if (current >= startTime && current <= endTime){
                // 1.2????????????????????????id
                String randomCode = redis.getRandomCode();
                String skuId = redis.getPromotionSessionId() + "_" + redis.getSkuId();
                if (randomCode.equals(key) && skuId.equals(killId)){
                    // 1.3????????????????????????????????????
                    if (num <= redis.getSeckillLimit()){
                        // 1.4?????????????????????????????????????????????????????????????????????????????????????????????  userId_sessionId_skillId
                        // SETNX
                        String redisKey = repsVo.getId() + "_" + skuId;
                        // 1.4.1 ????????????--?????????redis????????? ??????id-skuId ????????????????????????
                        Boolean ifAbsent = redisTemplate.opsForValue().setIfAbsent(redisKey, num.toString(), ttl, TimeUnit.MILLISECONDS);
                        if (ifAbsent){
                            // 1.4.2 ??????????????????????????????????????????????????????????????????????????????????????????
                            RSemaphore semaphore = redissonClient.getSemaphore(SKU_STOCK_SEMAPHORE + randomCode);
                            boolean b = false;
//                                b = semaphore.tryAcquire(num,100, TimeUnit.MILLISECONDS);     //?????????100ms??????????????????????????????true??????????????????false???
                                b = semaphore.tryAcquire(num);
                                if (b){
                                    // ????????????
                                    // ??????????????????MQ?????? 10ms
                                    String timeId = IdWorker.getTimeId();
                                    SeckillOrderTo orderTo = new SeckillOrderTo();
                                    orderTo.setOrderSn(timeId);
                                    orderTo.setMemberId(repsVo.getId());
                                    orderTo.setNum(num);
                                    orderTo.setPromotionSessionId(redis.getPromotionSessionId());
                                    orderTo.setSkuId(redis.getSkuId());
                                    orderTo.setSeckillPrice(redis.getSeckillPrice());
                                    //?????????????????????????????????
                                    rabbitTemplate.convertAndSend("order-event-exchange", "order.seckill.order", orderTo);
                                    long s2 = System.currentTimeMillis();
                                    log.info("??????..." + (s2-s1));
                                    return timeId;
                                }
                                return null;

                        }else {
                            // ??????????????????
                            return null;
                        }

                    }

                }else {
                    return null;
                }
            }else {

                return null;
            }

        }
        return null;
    }

    private void saveSessionInfos(List<SeckillSessionWithSkusVo> sessions){
        if (!CollectionUtils.isEmpty(sessions)){
            sessions.stream().forEach(session -> {
                Long startTime = session.getStartTime().getTime();
                Long endTime = session.getEndTime().getTime();
                String key = SESSIONS_CACHE_PREFIX + startTime + "_" + endTime;
                Boolean hasKey = redisTemplate.hasKey(key);
                if (!hasKey){
                    List<String> collect = session.getRelationEntities()
                            .stream()
                            .map(item -> item.getPromotionSessionId().toString() +"_"+ item.getSkuId().toString())
                            .collect(Collectors.toList());
                    // ??????????????????
                    redisTemplate.opsForList().leftPushAll(key, collect);
                }

            });
        }
    }

    private void saveSessionSkuInfos(List<SeckillSessionWithSkusVo> sessions){
        if(!CollectionUtils.isEmpty(sessions)){
            // ??????hash??????
            BoundHashOperations<String, Object, Object> ops = redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
            sessions.stream().forEach(session -> {
                session.getRelationEntities().stream().forEach(seckillSkuVo -> {
                    // 4?????????????????????????????????????????????
                    String token = UUID.randomUUID().toString().replace("_", "");

                    if(!ops.hasKey(seckillSkuVo.getPromotionSessionId().toString()+"_"+seckillSkuVo.getSkuId().toString())){
                        // ????????????
                        SeckillSkuRedisTo redisTo = new SeckillSkuRedisTo();
                        // 1???sku???????????????
                        R r = productFeignService.getSkuInfo(seckillSkuVo.getSkuId());
                        if (0  == r.getCode()){
                            SkuInfoVo skuInfo = r.getData("skuInfo", new TypeReference<SkuInfoVo>() {
                            });
                            redisTo.setSkuInfoVo(skuInfo);
                        }
                        // 2???sku???????????????
                        BeanUtils.copyProperties(seckillSkuVo, redisTo);

                        // 3??????????????????????????????????????????
                        redisTo.setStartTime(session.getStartTime().getTime());
                        redisTo.setEndTime(session.getEndTime().getTime());

                        redisTo.setRandomCode(token);
                        // 5??????????????????????????????????????? ??????

                        String jsonString = JSON.toJSONString(redisTo);
                        ops.put(seckillSkuVo.getPromotionSessionId().toString()+"_"+seckillSkuVo.getSkuId().toString(), jsonString);

                        RSemaphore semaphore = redissonClient.getSemaphore(SKU_STOCK_SEMAPHORE + token);
                        // ??????????????????????????????????????????
                        semaphore.trySetPermits(seckillSkuVo.getSeckillCount());
                    }
                });
            });
        }
    }
}
