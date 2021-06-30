package com.harbin.mymall.mymallmember;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/*
	1、想要远程调用别的服务
		1）引入open-feign的依赖
		2）编写一个接口，告诉springcloud这个接口需要调用的远程服务。如feign.CouponFeignService接口。
		3）开启远程调用功能。@EnableFeignClients
 */

@EnableRedisHttpSession
@SpringBootApplication
@EnableDiscoveryClient    //开启服务注册功能
@EnableFeignClients("com.harbin.mymall.mymallmember.feign")
public class MymallMemberApplication {

	public static void main(String[] args) {
		SpringApplication.run(MymallMemberApplication.class, args);
	}

}
