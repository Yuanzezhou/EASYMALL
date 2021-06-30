package com.harbin.mymall.mymallseckill.feign;

import com.harbin.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Yuanzz
 * @creat 2021-03-11-16:44
 */
@FeignClient("mymall-coupon")
public interface CouponFeignService {

    @GetMapping("/mymallcoupon/seckillsession/lasts3DaySession")
    R getLasts3DaySession();
}