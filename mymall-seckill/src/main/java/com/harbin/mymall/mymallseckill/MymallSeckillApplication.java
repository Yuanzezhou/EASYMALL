package com.harbin.mymall.mymallseckill;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 1、整合Sentinel
 * 	1)导入依赖spring-cloud-starter-alibaba-sentinel
 * 	2)下载sentinel的控制台
 * 	3）配置sentinel控制台地址信息：dashboard.以及控制台传输端口
 * 	4）在控制台调整参数。默认所有的流控设置保存在内存中，重启就失效。
 *
* 2、每一个服务都导入审计模块-----spring-boot-starter-actuator  ;并配合management.endpoint.web.exposure.include=*
 *
 *3、自定义snetinel流控返回数据
 *
* 4、使用Sentinel来保护feign远程调用：熔断。
 *
 * 1）、调用方的熔断保护开启  feign.sentinel.enabled=true
 *
 * 2）、调用方手动指定远程服务的降级策略。远程服务被降级处理，触发我们的熔断回调方法(在调用方的降级策略)
 *
 * 3）、超大浏览的时候，必须牺牲一些远程服务。在服务的提供方（远程服务）指定降级策略; 提供方是在运行。但是不运行自己的业务逻辑。返回的是默认的降级数据（限流的数据）		（在服务方的降级策略，通过3、自定义流控返回数据实现）
 *
 *
 *
 */


//@EnableRabbit		不同监听消息，只需要发送消息
@EnableRedisHttpSession
@EnableFeignClients("com.harbin.mymall.mymallseckill.feign")
@EnableDiscoveryClient
@SpringBootApplication
public class MymallSeckillApplication {

	public static void main(String[] args) {
		SpringApplication.run(MymallSeckillApplication.class, args);
	}

}
