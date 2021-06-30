package com.harbin.mymallauthserver.controller;

import com.alibaba.fastjson.TypeReference;
import com.harbin.common.constant.AuthServerConstant;
import com.harbin.common.exception.BizCodeEnum;
import com.harbin.common.utils.R;
import com.harbin.common.vo.MemberRespVo;
import com.harbin.mymallauthserver.feign.MemberServiceFeign;
import com.harbin.mymallauthserver.feign.SmsServiceFeign;
import com.harbin.mymallauthserver.vo.UserLoginVo;
import com.harbin.mymallauthserver.vo.UserRegistVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.harbin.common.constant.AuthServerConstant.LOGIN_USER;

/**
 * @author Yuanzz
 * @creat 2021-03-04-22:11
 */

@Controller
public class LoginController {

    @Autowired
    SmsServiceFeign smsServiceFeign;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    MemberServiceFeign memberServiceFeign;

    @ResponseBody
    @GetMapping("/sms/sendSms")
    public R sendSms(@RequestParam("phone") String phone){

        //1、接口防刷
        String prefixPhone = AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone;
        String redisCode = redisTemplate.opsForValue().get(prefixPhone);
        if(!StringUtils.isEmpty(redisCode)){
            String[] s = redisCode.split("_");
            long time = Long.parseLong(s[1]);
            if(System.currentTimeMillis()-time < 60000){
                return R.error(BizCodeEnum.SMS_CODE_EXCEPTION.getCode(),BizCodeEnum.SMS_CODE_EXCEPTION.getMsg());
            }
        }

//        String code = UUID.randomUUID().toString().substring(0, 5);
        //2、验证码的再次校验。redis 存key-phone, value-code   sms:code:18896736055 ->12345
        String code = String.valueOf((int)((Math.random() + 1) * 100000));
        //redis缓存验证码   防止同一个phone在60s内再次发送验证码  set(K var1, V var2, long var3, TimeUnit var5)
        redisTemplate.opsForValue().set(prefixPhone,code + "_" + System.currentTimeMillis(),10, TimeUnit.MINUTES);
        smsServiceFeign.sendCode(phone,code);
        return R.ok();
    }

    @PostMapping("/regist")
    public String regist(@Valid UserRegistVo userRegistVo, BindingResult result, RedirectAttributes redirectAttributes){
        if(result.hasErrors()){
            //数据有异常，返回登录页面
            Map<String, String> errors = result.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            redirectAttributes.addFlashAttribute("errors",errors);      //下次请求就会失效

            /**
             * 使用 return "forward:/reg.html"; 会出现
             * 问题：Request method 'POST' not supported的问题
             * 原因：用户注册-> /regist[post] ------>转发/reg.html (路径映射默认都是get方式访问的)
             * 校验出错转发到注册页
             */
            //return "reg";    //转发会出现重复提交的问题，不要以转发的方式
            //使用重定向  解决重复提交的问题。但面临着数据不能携带的问题，就用RedirectAttributes
            return "redirect:http://auth.easymall.com/regist.html";
        }

        //1、校验验证码
        String code = userRegistVo.getCode();
        String s = redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + userRegistVo.getPhone());
        if (!StringUtils.isEmpty(s)) {
            if (code.equals(s.split("_")[0])) {
                //验证码通过,删除缓存中的验证码；令牌机制
                redisTemplate.delete(AuthServerConstant.SMS_CODE_CACHE_PREFIX + userRegistVo.getPhone());
                //真正注册调用远程服务注册
                R r = memberServiceFeign.regist(userRegistVo);
                if (r.getCode() == 0) {
                    //成功
                    return "redirect:http://auth.easymall.com/login.html";
                } else {
                    Map<String, String> errors = new HashMap<>();
                    errors.put("msg", r.getData("msg",new TypeReference<String>() {
                    }));
                    redirectAttributes.addFlashAttribute("errors", errors);
                }
            } else {
                Map<String, String> errors = new HashMap<>();
                errors.put("code", "验证码错误");
                redirectAttributes.addFlashAttribute("errors", errors);
                return "redirect:http://auth.easymall.com/regist.html";
            }
        } else {
            Map<String, String> errors = new HashMap<>();
            errors.put("code", "验证码错误");
            redirectAttributes.addFlashAttribute("errors", errors);
            //校验出错转发到注册页
            return "redirect:http://auth.easymall.com/regist.html";
        }

        //注册成功返回登录页面
        return "redirect:http://auth.easymall.com/login.html";
    }

    @PostMapping("/login")
    public String login(UserLoginVo vo, RedirectAttributes redirectAttributes, HttpSession session){
        //远程登录
        R login = memberServiceFeign.login(vo);
        if (login.getCode() == 0){
            //成功
            MemberRespVo data = login.getData(new TypeReference<MemberRespVo>() {
            });
            session.setAttribute(LOGIN_USER,data);
            return "redirect:http://easymall.com";
        }else{
            Map<String,String> errors = new HashMap<>();
            errors.put("msg",login.getData("msg",new TypeReference<String>(){}));
            redirectAttributes.addFlashAttribute("errors",errors);
            return "redirect:http://auth.easymall.com/login.html";
        }
    }
}
