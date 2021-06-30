package com.harbin.mymall.mymallorder.web;

import com.alipay.api.AlipayApiException;
import com.harbin.mymall.mymallorder.config.AlipayTemplate;
import com.harbin.mymall.mymallorder.service.OrderService;
import com.harbin.mymall.mymallorder.vo.PayVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author Yuanzz
 * @creat 2021-06-09-23:17
 */
@Controller
public class PayWebController {

    @Resource
    AlipayTemplate alipayTemplate;

    @Resource
    OrderService orderService;

    @ResponseBody
    @GetMapping(value="/payOrder",produces ="text/html")
    public String payOrder(@RequestParam("orderSn") String orderSn) throws AlipayApiException {

//        PayVo payVo = new PayVo();
//        payVo.setBody();    //订单的备注
//        payVo.setOut_trade_no();    //订单号
//        payVo.setSubject(); //订单的主题
//        payVo.setTotal_amount();    //金额
        PayVo payVo = orderService.getOrderPay(orderSn);
        String pay = alipayTemplate.pay(payVo);
        return pay;
    }
}
