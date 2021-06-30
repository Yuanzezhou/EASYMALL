package com.harbin.mymall.mymallorder.feign;

import com.harbin.mymall.mymallorder.vo.OrderItemVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author Yuanzz
 * @creat 2021-03-08-12:14
 */

@FeignClient("mymall-cart")
public interface CartFeignService {

    @GetMapping("/currentUserCartItems")
    List<OrderItemVo> getCurrentUserCartItems();
}
