package com.harbin.mymall.mymallorder.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.harbin.mymall.mymallorder.vo.PayVo;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "alipay")
@Component
@Data
public class AlipayTemplate {

    //在支付宝创建的应用的id
    private   String app_id = "2021000117673108";

    // 商户私钥，您的PKCS8格式RSA2私钥
    private  String merchant_private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCvFE551XDOw6hUysNlJPCHf63RPiYFHqTpMrfFLpCiRm9nNo9GUTtVD9y8cmor/I2JvfaR/4YvyshMYBwsJq6G9xCyLZj6DipBS26RZV89y38HENHmtePk0ShLr/DnI6rgSYU/1bDgcq3zioaldy90qLWHYAR4fIbsYyOXmR8XqaK6oJJHO5CQ1Bp3yfJVc7iH/hk0cQT2JMhPclQNAa8aB8cTSGYj+tecdc5TprhTq4T9nCPjanY62sy/9bOgf+oho+2iGtaW5Yx0va9LzT8r8eDrXI5CDXxCnSiN3dtQZvor4DUgj84LsTqkjKJwvSYyb5OS4TwxmwsH6la+JpQRAgMBAAECggEAdCOSUzuFU4NzHJxMtcGlVR0bXpxNeEP1vUQJp+HVy+/RYrRMoqasJycYlGGJkJg+A8thz0Oj7fwklpWC2r7mM12dU7Tvr6DJ4vp45DZM0Z82Kqe7tcGe5WDhzCNwOMPuTE44+Dl21cz9G3sfCpBfiJGJqb3F+W8RyamSKLRIbdbecKHNwDDuHrKvPQvpPhepqeztZpf7bcu2pxvl2q6NIntJYBETGOlK//ZhbG3xD6vQ73eS/GvOUTqAUVqtnGa5jYAieIJy3bL2XlYVEuRTFqPRXvxj2Xj+HStXiO4LLns5QjEZpxdtXuvNbcmi9Vxs2pGVeFSNr/GJHy3pFiARqQKBgQDfqfHDyuVfiFaVLr8/T2QrTsmvEqhtqMZFbmg227TLfs+eeGfL2/aLbTNIjsjIpPnJ8dypDd82rRhQmuvhuv3NpvGjucBE0Bzzdq8jknadDgWtcBiczeeWQUBntjrpZWqmQ7/5ubRNvX9pzMLZcAWiedq46/n7AriN19qjNoKIewKBgQDIZDF1x7riZjhOguhwGBYT38P7Z+6SfJa+mJ5Y70Z1uCpGMWkIvUSQ6J/HFJER3KrHT2SXxUlr4HpFJW7TridVTX9gUNXVMyhwkMklgRsloG6G6vwiCQelN97E2E9uuqZbJzPFcNCFrHzDd2vYoA2414d8n6kGJzLlq6IH5rL94wKBgQDIybXiwTjdZHXmA0tpOIqCXn6sjqFpoIQuPImOBfruXG9iolD8MAHK9UN+tcAGcCRWaKxhj7R9TzNu2UU9JjNG9cIj61Nx8yqqXjeoRYn6QGZUZzeRPT/UIcwBzxRgBs2RaWzkcRQuXhoODuY1cV19mMsEC8Qk1TJ9E/SRZRQWFQKBgGnZ0u5+FE+m4zNFnvYnIvJYjrNsEO879Hy1LV+Y9MUWBO6TBwJgx6kr18pXKNIgl+00GNS6l6/uIWgI/+O86HX8u73qtSkrHr9nG3k+G1Lizb9ivNBVqL4zJu8fu8WnvlwD9yriDqOtmzG9ETCaSuVKo/zGcYgYiKAc6zf1OrM1AoGBAIdElB7xRM6KcCrA8lGOpolUd/Q1Lo2M1erfl8vT3ntthYmiQD+C+vY8Jeja5y4wmN4QVq/Sv7kGpG3VjK/T0fxp1FrtD3uRmhCcv9Y84nj29mWBY8ya24ynpz6XmUEgmAcDPBXbIp+8wjIDUFR3Y1I37+fssv9j1EEOhYbUwhM1";
    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。

    private  String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAi3pn+27M6IxBeqQTi1uiorEnrCoPKn816OrTEZM+OYMW7KsiBBhZjbC4etj+lvvXtOUoeRMa2V/JESqB+b+TWt5b7IOdokf7vuNKjtV1ulvUVLofwfnZVX2Y0XkkMoP/GNtgv4yfyKsBZZ+Qr9hg5yj7kqk5/xxVD+BIySuOhQetUQhq3ReIWV9n5f3g7IJbp0AHhiJAOnuGvFwjM4/K9f0Hr8wEd5Sfowbuegm8zJxUozooi2i2mq6aRzZG/Y9gkZSuSuNymk1C544Mye56yCOqODrNcvz+d6vNvAmef4SuU9YUTl2ZR+ZR1OOgsgotlgCcys93mcu/YOgX9fpgUQIDAQAB";

    // 服务器[异步通知]页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    // 支付宝会悄悄的给我们发送一个请求，告诉我们支付成功的信息
    private  String notify_url="http://4032z16k85.wicp.vip/payed/notify";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    //同步通知，支付成功，一般跳转到成功页
    private  String return_url="http://member.easymall.com/memberOrder.html";

    // 签名方式
    private  String sign_type = "RSA2";

    // 字符编码格式
    private  String charset = "utf-8";

    // 支付宝网关； https://openapi.alipaydev.com/gateway.do
    private  String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    public  String pay(PayVo vo) throws AlipayApiException {

        //AlipayClient alipayClient = new DefaultAlipayClient(AlipayTemplate.gatewayUrl, AlipayTemplate.app_id, AlipayTemplate.merchant_private_key, "json", AlipayTemplate.charset, AlipayTemplate.alipay_public_key, AlipayTemplate.sign_type);
        //1、根据支付宝的配置生成一个支付客户端
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl,
                app_id, merchant_private_key, "json",
                charset, alipay_public_key, sign_type);

        //2、创建一个支付请求 //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(return_url);
        alipayRequest.setNotifyUrl(notify_url);

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = vo.getOut_trade_no();
        //付款金额，必填
        String total_amount = vo.getTotal_amount();
        //订单名称，必填
        String subject = vo.getSubject();
        //商品描述，可空
        String body = vo.getBody();

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\""+ total_amount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"body\":\""+ body +"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        String result = alipayClient.pageExecute(alipayRequest).getBody();

        //会收到支付宝的响应，响应的是一个页面，只要浏览器显示这个页面，就会自动来到支付宝的收银台页面
        System.out.println("支付宝的响应："+result);

        return result;

    }
}
