package com.harbin.mymall.mymallproduct.feign;

import com.harbin.common.utils.R;
import com.harbin.mymall.mymallproduct.feign.fallback.SeckillFeignServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Yuanzz
 * @creat 2021-03-12-17:06
 */

@FeignClient(value="mymall-seckill")
public interface SeckillFeignService {

    @GetMapping("/sku/seckill/{skuId}")
    R getSkuSecKillInfo(@PathVariable("skuId") Long skuId);
}
