package com.harbin.mymall.mymallproduct.feign.fallback;

import com.harbin.common.exception.BizCodeEnum;
import com.harbin.common.utils.R;
import com.harbin.mymall.mymallproduct.feign.SeckillFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Yuanzz
 * @creat 2021-03-14-17:01
 */
@Slf4j
@Component
public class SeckillFeignServiceFallBack implements SeckillFeignService {
    @Override
    public R getSkuSecKillInfo(Long skuId) {

        System.out.println("远程调用触发熔断。。。。。。");
        return  R.error(BizCodeEnum.TOO_MANY_REQUEST.getCode(),BizCodeEnum.TOO_MANY_REQUEST.getMsg());
    }
}
