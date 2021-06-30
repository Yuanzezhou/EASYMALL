package com.harbin.mymallthirdservice;

import com.aliyun.oss.OSSClient;
import com.harbin.mymallthirdservice.component.SmsComponent;
import com.harbin.mymallthirdservice.utils.HttpUtils;
import org.apache.http.HttpResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class MymallthirdserviceApplicationTests {

	@Resource
	OSSClient ossClient;

	@Autowired
	SmsComponent smsComponent;

	@Test
	void contextLoads() throws FileNotFoundException {
			// Endpoint以杭州为例，其它Region请按实际情况填写。
//		String endpoint = "oss-cn-beijing.aliyuncs.com";
//// 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录RAM控制台创建RAM账号。
//		String accessKeyId = "LTAI4FzvCL76TNUueg6hv27b";
//		String accessKeySecret = "6sCJtLaYGrxlJ2E1zyGKWa4A6Nclce";
			String bucketName = "mymall123";
// <yourObjectName>上传文件到OSS时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。
			String objectName = "C:\\Users\\ASUS\\Pictures\\yzz.jpg";

//// 创建OSSClient实例。
//		OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

// 上传文件到指定的存储空间（bucketName）并将其保存为指定的文件名称（objectName）。
			FileInputStream fileInputStream = new FileInputStream(objectName);

			ossClient.putObject(bucketName,"yzz3.jpg", fileInputStream);
// 关闭OSSClient。
			ossClient.shutdown();
			System.out.println("上传成功。。");
	}

	@Test
	public void testSms() {
		String host = "https://gyytz.market.alicloudapi.com";
		String path = "/sms/smsSend";
		String method = "POST";
		String appcode = "你自己的AppCode";
		Map<String, String> headers = new HashMap<String, String>();
		//最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
		headers.put("Authorization", "APPCODE " + appcode);
		Map<String, String> querys = new HashMap<String, String>();
		querys.put("mobile", "mobile");
		querys.put("param", "**code**:12345,**minute**:5");
		querys.put("smsSignId", "2e65b1bb3d054466b82f0c9d125465e2");
		querys.put("templateId", "908e94ccf08b4476ba6c876d13f084ad");
		Map<String, String> bodys = new HashMap<String, String>();


		try {
			/**
			 * 重要提示如下:
			 * HttpUtils请从
			 * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
			 * 下载
			 *
			 * 相应的依赖请参照
			 * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
			 */
			HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
			System.out.println(response.toString());
			//获取response的body
			//System.out.println(EntityUtils.toString(response.getEntity()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testSendCode(){
		smsComponent.sendSmsCode("18856335721", "66666");
	}
}
