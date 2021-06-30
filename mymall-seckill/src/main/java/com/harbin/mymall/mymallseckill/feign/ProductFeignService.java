package com.harbin.mymall.mymallseckill.feign;

import com.harbin.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("mymall-product")
public interface ProductFeignService {
 
    @RequestMapping("/mymallproduct/skuinfo/info/{skuId}")
    R getSkuInfo(@PathVariable("skuId") Long skuId);
}