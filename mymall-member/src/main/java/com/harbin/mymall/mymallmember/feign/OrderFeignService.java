package com.harbin.mymall.mymallmember.feign;

import com.harbin.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author Yuanzz
 * @creat 2021-06-10-18:09
 */

@FeignClient("mymall-order")
public interface OrderFeignService {

    @PostMapping("/mymallorder/order/listWithItems")
    public R listWithItems(@RequestBody Map<String,Object> params);
}
