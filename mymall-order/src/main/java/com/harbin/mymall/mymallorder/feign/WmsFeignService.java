package com.harbin.mymall.mymallorder.feign;

import com.harbin.common.utils.R;
import com.harbin.mymall.mymallorder.vo.WareSkuLockVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Yuanzz
 * @creat 2021-03-08-13:55
 */

@FeignClient("mymall-ware")
public interface WmsFeignService {
    @PostMapping("/mymallware/waresku/hasStock")
    R getSkuHasStock(@RequestBody List<Long> skuIds);

    @GetMapping("/mymallware/wareinfo/fare")
    R getFare(@RequestParam("addrId") Long addrId);

    @PostMapping("mymallware/waresku/lock/order")
    public R orderLockStock(@RequestBody WareSkuLockVo vo);
}
