package com.harbin.mymallauthserver.feign;

import com.harbin.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Yuanzz
 * @creat 2021-03-04-22:07
 */

@FeignClient("third-service")
public interface SmsServiceFeign {

    @GetMapping("/sms/sendcode")
    R sendCode(@RequestParam("phone") String phone, @RequestParam("code") String code);
}
