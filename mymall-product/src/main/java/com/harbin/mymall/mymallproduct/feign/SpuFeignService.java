package com.harbin.mymall.mymallproduct.feign;

import com.harbin.common.to.SkuReductionTo;
import com.harbin.common.to.SpuBoundTo;
import com.harbin.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Yuanzz
 * @creat 2021-02-05-22:41
 */

/**
 * 1、@FeignClient声明这个接口为远程调用功能
 * 2、@EnableFeignClients("com.harbin.mymall.mymallproduct.feign")开启远程调用功能
 * 3、
 */
@FeignClient("mymall-coupon")   //对应服务中心中的服务名
public interface SpuFeignService {

    /**
     * 1、CouponFeignService.saveSpuBounds(spuBoundTo);
     *  1）@RequestBody 将这个对象转为json对象；
     *  2）SPringCloud到注册中心找到mymall-coupon服务，给服务mymallcoupon/spubounds/save发送请求。
     *      将上一步转的json对象放在请求体的位置，发送请求。
     *  3）对方服务收到请求；请求体里面有json数据；
     *  （@RequestBody SpuBoundsEntity spuBounds），将请求体的json数据转为spuBoundsEntity对象。
     * @param spuBoundTo
     */
    @PostMapping("mymallcoupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundTo spuBoundTo);

    @PostMapping("mymallcoupon/skufullreduction/saveinfo")
    R saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo);
}
