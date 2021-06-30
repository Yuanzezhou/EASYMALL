package com.harbin.mymallauthserver.feign;

import com.harbin.common.utils.R;
import com.harbin.mymallauthserver.vo.UserLoginVo;
import com.harbin.mymallauthserver.vo.UserRegistVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Yuanzz
 * @creat 2021-03-05-13:21
 */

@FeignClient("mymall-member")
public interface MemberServiceFeign {

    @PostMapping("/mymallmember/member/regist")
    public R regist(@RequestBody UserRegistVo vo);


    @PostMapping("/mymallmember/member/login")
    R login(@RequestBody UserLoginVo vo);
}
