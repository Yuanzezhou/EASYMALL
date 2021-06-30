package com.harbin.mymall.mymallmember.web;

import com.alibaba.fastjson.JSON;
import com.harbin.common.utils.R;
import com.harbin.mymall.mymallmember.feign.OrderFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yuanzz
 * @creat 2021-06-10-16:48
 */

@Controller
public class MemberWebController {

    @Autowired
    OrderFeignService orderFeignService;

    @GetMapping("/memberOrder.html")
    public String memberOrderPage(@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum, Model model){

        //获取支付宝

        //查询当前用户所有的订单
        Map<String, Object> param = new HashMap<>();
        param.put("page",pageNum.toString());
        R r = orderFeignService.listWithItems(param);
        model.addAttribute("orders", r);
        System.out.println(JSON.toJSONString(r));
        return "orderList";
    }
}
