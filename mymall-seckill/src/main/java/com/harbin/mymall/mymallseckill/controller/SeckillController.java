package com.harbin.mymall.mymallseckill.controller;

import com.harbin.common.utils.R;
import com.harbin.mymall.mymallseckill.service.SeckillService;
import com.harbin.mymall.mymallseckill.to.SeckillSkuRedisTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Yuanzz
 * @creat 2021-03-12-16:08
 */
@Controller
public class SeckillController {

    @Autowired
    SeckillService seckillService;
    /**
     * 返回当前时间可以参与的秒杀商品信息
     * @return
     */

    @ResponseBody
    @GetMapping(value = "/getCurrentSeckillSkus")
    public R getCurrentSeckillSkus() {
        //获取到当前可以参加秒杀商品的信息
        List<SeckillSkuRedisTo> vos = seckillService.getCurrentSeckillSkus();

        return R.ok().setData(vos);
    }

    @ResponseBody
    @GetMapping("/sku/seckill/{skuId}")
    public R getSkuSecKillInfo(@PathVariable("skuId") Long skuId){
        SeckillSkuRedisTo to = seckillService.getSkuSecKillInfo(skuId);
        return R.ok().setData(to);
    }


    @GetMapping("/kill")
    public String seckill(@RequestParam("killId") String killId,
                     @RequestParam("key") String key,
                     @RequestParam("num") Integer num, Model model){
        // 1、判断是否登录(登录拦截器已经自动处理)

        String orderSn = seckillService.kill(killId, key, num);
        System.out.println(orderSn);
        model.addAttribute("orderSn",orderSn);
        return "success";
    }

    @GetMapping("/pay")
    public String toPay(){
        return "pay";
    }
}