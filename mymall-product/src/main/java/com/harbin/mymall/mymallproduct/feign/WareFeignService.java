package com.harbin.mymall.mymallproduct.feign;

import com.harbin.common.utils.R;
import com.harbin.mymall.mymallproduct.vo.SkuHasStockVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author Yuanzz
 * @creat 2021-02-20-17:25
 */
@FeignClient("mymall-ware")
public interface WareFeignService {

    @PostMapping("/mymallware/waresku/hasStock")
    R getHasStock(@RequestBody List<Long>skuIds);
}
