package com.harbin.mymall.mymallorder.feign;

import com.harbin.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Yuanzz
 * @creat 2021-03-08-19:01
 */

@FeignClient("mymall-product")
public interface ProductFeignService {

    @GetMapping("/mymallproduct/spuinfo/skuId/{id}")
    R getSpuInfoBySkuId(@PathVariable("id") Long skuId);
}
