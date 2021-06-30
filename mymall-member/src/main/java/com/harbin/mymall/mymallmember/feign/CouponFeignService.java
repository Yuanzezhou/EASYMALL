package com.harbin.mymall.mymallmember.feign;

import com.harbin.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Yuanzz
 * @creat 2021-01-14-23:21
 */

@FeignClient("mymall-coupon")   //注册中心中的服务名，指定要调用的服务
public interface CouponFeignService {

    @RequestMapping("mymallcoupon/coupon/member/list")
    public R membercoupons();

    /*
        这个意思就是，如果以后调用这个接口的这个方法，就会到注册中心去找相应的服务，并且找到"mymall-coupon"服务中"mymallcoupon/coupon/member/list"对应方法。
     */
}
