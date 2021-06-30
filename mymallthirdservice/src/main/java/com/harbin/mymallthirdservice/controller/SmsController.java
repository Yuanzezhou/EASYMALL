package com.harbin.mymallthirdservice.controller;

import com.harbin.common.utils.R;
import com.harbin.mymallthirdservice.component.SmsComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yuanzz
 * @creat 2021-03-04-22:06
 */
@RestController
@RequestMapping("/sms")
public class SmsController {

    @Autowired
    SmsComponent smsComponent;
    /**
     * 提供给别的服务进行调用
     * @param phone
     * @param code
     * @return
     */
    @GetMapping("/sendcode")
    public R sendCode(@RequestParam("phone") String phone, @RequestParam("code") String code){
        smsComponent.sendSmsCode(phone,code);
        return R.ok();
    }
}