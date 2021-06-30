package com.harbin.mymall.mymallorder.feign;

import com.harbin.mymall.mymallorder.vo.MemberAddressVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author Yuanzz
 * @creat 2021-03-07-22:52
 */

@FeignClient("mymall-member")
public interface MemberFeignService {

    @GetMapping("/mymallmember/member/{memberId}/addresses")
    List<MemberAddressVo> getAddress(@PathVariable("memberId") Long id);
}
