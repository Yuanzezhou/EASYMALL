package com.harbin.mymallcart.feign;

import com.harbin.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author Yuanzz
 * @creat 2021-03-07-14:35
 */

@FeignClient("mymall-product")
public interface ProductFeignService {

    @RequestMapping("/mymallproduct/skuinfo/info/{skuId}")
    R getSkuInfo(@PathVariable("skuId") Long skuId);

    @GetMapping("/mymallproduct/skusaleattrvalue/stringlist/{skuId}")
    public List<String> getSkuSaleAttrValues(@PathVariable("skuId") Long skuId);

    @GetMapping("/mymallproduct/skuinfo/{skuId}/price")
    R getPrice(@PathVariable("skuId") Long skuId);
}
