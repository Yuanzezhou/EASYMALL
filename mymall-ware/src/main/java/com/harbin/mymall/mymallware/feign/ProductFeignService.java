package com.harbin.mymall.mymallware.feign;

import com.harbin.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Yuanzz
 * @creat 2021-02-06-22:37
 */

@FeignClient("mymall-gateway")
public interface ProductFeignService {
    /**
     *      *   1)、让所有请求过网关；
     *      *          1、@FeignClient("mymallmall-gateway")：给gulimall-gateway所在的机器发请求
     *      *          2、/api/product/skuinfo/info/{skuId}
     *      *   2）、直接让后台指定服务处理
     *      *          1、@FeignClient("mymallmall-product")
     *      *          2、/product/skuinfo/info/{skuId}
     * @return
     */
    @PostMapping("api/mymallproduct/skuinfo/info/{skuId}")
    public R info(@PathVariable("skuId") Long skuId);
}
