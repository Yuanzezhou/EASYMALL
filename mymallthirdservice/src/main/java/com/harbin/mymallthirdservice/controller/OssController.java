package com.harbin.mymallthirdservice.controller;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.harbin.common.utils.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Yuanzz
 * @creat 2021-01-22-19:46
 */
@RestController
public class OssController {

    @Resource
    OSSClient ossClient;

    @Value("${spring.cloud.alicloud.oss.endpoint}")
    String endpoint;

    @Value("${spring.cloud.alicloud.oss.bucket}")
    String bucket;

    @Value("${spring.cloud.alicloud.access-key}")
    String accessId;

    @RequestMapping("/oss/policy")
//    @ResponseBody
    public R policy(){

/*        全都放在配置中。
        String accessId = "<yourAccessKeyId>"; // 请填写您的AccessKeyId。
        String accessKey = "<yourAccessKeySecret>"; // 请填写您的AccessKeySecret。
        String endpoint = "oss-cn-hangzhou.aliyuncs.com"; // 请填写您的 endpoint。
        String bucket = "mymall123"; // 请填写您的 bucketname 。*/

        String host = "https://" + bucket + "." + endpoint; // host的格式为 bucketname.endpoint

        //callbackUrl为 上传回调服务器的URL，请将下面的IP和Port配置为您自己的真实信息。
        //String callbackUrl = "http://88.88.88.88:8888";

        //String dir = "user-dir-prefix/"; // 用户上传文件时指定的前缀。
        //每天的文件都放在不同的文件夹里
        String dateFormat = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String dir = dateFormat+"/";

        Map<String, String> respMap = null;
        // 创建OSSClient实例。
        //        OSS ossClient = new OSSClientBuilder().build(endpoint, accessId, accessKey);
        try {
            long expireTime = 30;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            // PostObject请求最大可支持的文件大小为5 GB，即CONTENT_LENGTH_RANGE为5*1024*1024*1024。
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);
            respMap = new LinkedHashMap<String, String>();
            respMap.put("accessid", accessId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", dir);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));
            // respMap.put("expire", formatISO8601Date(expiration));

            //后面是处理跨域的，已经在gateway中统一解决。
//            JSONObject jasonCallback = new JSONObject();
//            jasonCallback.put("callbackUrl", callbackUrl);
//            jasonCallback.put("callbackBody",
//                    "filename=${object}&size=${size}&mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}");
//            jasonCallback.put("callbackBodyType", "application/x-www-form-urlencoded");
//            String base64CallbackBody = BinaryUtil.toBase64String(jasonCallback.toString().getBytes());
//            respMap.put("callback", base64CallbackBody);
//
//            JSONObject ja1 = JSONObject.fromObject(respMap);
//            // System.out.println(ja1.toString());
//            response.setHeader("Access-Control-Allow-Origin", "*");
//            response.setHeader("Access-Control-Allow-Methods", "GET, POST");
//            response(request, response, ja1.toString());

        } catch (Exception e) {
            // Assert.fail(e.getMessage());
            System.out.println(e.getMessage());
        }
        return R.ok().put("data",respMap);
    }
}
