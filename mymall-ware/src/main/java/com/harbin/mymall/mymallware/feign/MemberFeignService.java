package com.harbin.mymall.mymallware.feign;

import com.harbin.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Yuanzz
 * @creat 2021-03-08-14:54
 */

@FeignClient("mymall-member")
public interface MemberFeignService {
    @RequestMapping("/mymallmember/memberreceiveaddress/info/{id}")
    R addrInfo(@PathVariable("id") Long addrId);
}
