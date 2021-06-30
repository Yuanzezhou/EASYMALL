package com.harbin.mymall.mymallorder.listener;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.harbin.mymall.mymallorder.config.AlipayTemplate;
import com.harbin.mymall.mymallorder.service.OrderService;
import com.harbin.mymall.mymallorder.vo.PayAsyncVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Yuanzz
 * @creat 2021-06-11-17:49
 */

@RestController
public class OrderPayedListener {

    @Autowired
    AlipayTemplate alipayTemplate;

    @Autowired
    OrderService orderService;

    /**
     * 支付宝成功后的异步通知
     * @param request
     * @return
     */

    @PostMapping("/payed/notify")
    public String notifyAliPayed(PayAsyncVo payAsyncVo,HttpServletRequest request) throws AlipayApiException {
        System.out.println("收到支付宝异步通知******************");
        // 只要收到支付宝的异步通知，返回 success 支付宝便不再通知
        // 获取支付宝POST过来反馈信息
        //TODO 需要验签
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        boolean signVerified = AlipaySignature.rsaCheckV1(params, alipayTemplate.getAlipay_public_key(),
                alipayTemplate.getCharset(), alipayTemplate.getSign_type()); //调用SDK验证签名
        if(signVerified){
            //验签成功
            System.out.println("验签成功");
            //修改订单状态
            orderService.handleOrderAfterPay(payAsyncVo);
            return "success";
        }else{
            System.out.println("验签失败");
            return "error";
        }
    }
}
