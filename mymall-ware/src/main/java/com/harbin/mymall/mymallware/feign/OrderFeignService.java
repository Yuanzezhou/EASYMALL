package com.harbin.mymall.mymallware.feign;

import com.harbin.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author Yuanzz
 * @creat 2021-03-10-14:36
 */

@FeignClient("mymall-order")
public interface OrderFeignService {

    @GetMapping("/mymallorder/order/status/{orderSn}")
    R getOrderStatus(@PathVariable("orderSn") String orderSn);
}
